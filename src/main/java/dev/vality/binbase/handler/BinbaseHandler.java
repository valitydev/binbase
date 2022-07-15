package dev.vality.binbase.handler;

import dev.vality.binbase.domain.BinData;
import dev.vality.binbase.domain.CountryCode;
import dev.vality.binbase.exception.BinNotFoundException;
import dev.vality.binbase.service.BinbaseService;
import dev.vality.damsel.binbase.*;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.msgpack.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static dev.vality.binbase.util.PanUtil.formatPan;

@Component
@Slf4j
@RequiredArgsConstructor
public class BinbaseHandler implements BinbaseSrv.Iface {

    private final BinbaseService binbaseService;

    @Override
    public ResponseData lookup(String pan, Reference reference) throws TException {
        log.info("Lookup bin data, pan='{}', reference='{}'", formatPan(pan), reference);
        try {
            Map.Entry<Long, BinData> binDataWithVersion = reference.isSetLast()
                    ? binbaseService.getBinDataByCardPan(pan)
                    : binbaseService.getBinDataByCardPanAndVersion(pan, reference.getVersion());

            ResponseData responseData = toResponseData(binDataWithVersion);
            log.info("Lookup result: {}, pan='{}', reference='{}'", responseData, formatPan(pan), reference);
            return responseData;
        } catch (BinNotFoundException | IllegalArgumentException ex) {
            log.info("Cannot lookup bin data, pan='{}', reference='{}'", formatPan(pan), reference, ex);
            throw new BinNotFound();
        }
    }

    @Override
    public ResponseData getByBinDataId(Value binDataId) throws TException {
        log.info("Get bin data, binDataId='{}'", binDataId);
        try {
            Map.Entry<Long, BinData> binDataWithVersion = binbaseService.getBinDataByBinDataId(binDataId.getI());
            ResponseData responseData = toResponseData(binDataWithVersion);
            log.info("Result: {}, binDataId='{}'", responseData, binDataId);
            return responseData;
        } catch (BinNotFoundException | IllegalArgumentException ex) {
            log.info("Cannot get bin data, binDataId='{}'", binDataId, ex);
            throw new BinNotFound();
        }
    }

    @Override
    public ResponseData getByCardToken(String cardToken) throws TException {
        log.info("Get bin data, cardToken='{}'", cardToken);
        try {
            Map.Entry<Long, BinData> binDataWithVersion = binbaseService.getBinDataByCardToken(cardToken);
            ResponseData responseData = toResponseData(binDataWithVersion);
            log.info("Result: {}, cardToken='{}'", responseData, cardToken);
            return responseData;
        } catch (BinNotFoundException | IllegalArgumentException ex) {
            log.info("Cannot get bin data, cardToken='{}'", cardToken, ex);
            throw new BinNotFound();
        }
    }

    private ResponseData toResponseData(Map.Entry<Long, BinData> binDataWithVersion) {
        BinData binData = binDataWithVersion.getValue();
        dev.vality.damsel.binbase.BinData damselBinData = new dev.vality.damsel.binbase.BinData();
        damselBinData.setBinDataId(Value.i(binData.getId()));
        damselBinData.setPaymentSystem(binData.getPaymentSystem());
        damselBinData.setBankName(binData.getBankName());
        damselBinData.setCardType(
                TypeUtil.toEnumField(
                        Optional.ofNullable(binData.getCardType())
                                .map(Enum::toString)
                                .orElse(null),
                        CardType.class));
        damselBinData.setIsoCountryCode(
                Optional.ofNullable(binData.getIsoCountryCode())
                        .map(CountryCode::getAlpha3)
                        .orElse(null));
        damselBinData.setCategory(binData.getCategory());
        long versionId = binDataWithVersion.getKey();
        return new ResponseData(damselBinData, versionId);
    }

}
