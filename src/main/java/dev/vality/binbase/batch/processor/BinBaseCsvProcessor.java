package dev.vality.binbase.batch.processor;

import com.google.common.collect.Range;
import dev.vality.binbase.batch.BinBaseCsvData;
import dev.vality.binbase.domain.BinData;
import dev.vality.binbase.domain.CountryCode;
import org.springframework.batch.item.ItemProcessor;

import java.util.AbstractMap;
import java.util.Map;

import static dev.vality.binbase.util.CardTypeConverter.convertToCardType;

public class BinBaseCsvProcessor extends BinBaseItemProcessor
        implements ItemProcessor<BinBaseCsvData, Map.Entry<BinData, Range<Long>>> {

    @Override
    public Map.Entry<BinData, Range<Long>> process(BinBaseCsvData binBaseCsvData) {
        BinData binData = new BinData();
        binData.setPaymentSystem(binBaseCsvData.getBrand());
        binData.setBankName(binBaseCsvData.getBank());
        binData.setIsoCountryCode(CountryCode.getByNumericCode(binBaseCsvData.getIsonumber()));
        binData.setCardType(convertToCardType(binBaseCsvData.getType()));
        binData.setCategory(binBaseCsvData.getCategory());
        Range<Long> range = buildRange(binBaseCsvData.getBin());
        return new AbstractMap.SimpleEntry<>(binData, range);
    }

}
