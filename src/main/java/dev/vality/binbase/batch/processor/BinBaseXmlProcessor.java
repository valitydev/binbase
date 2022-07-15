package dev.vality.binbase.batch.processor;

import com.google.common.collect.Range;
import dev.vality.binbase.batch.BinBaseXmlData;
import dev.vality.binbase.domain.BinData;
import dev.vality.binbase.domain.CountryCode;
import org.springframework.batch.item.ItemProcessor;

import java.util.AbstractMap;
import java.util.Map;

import static dev.vality.binbase.util.CardTypeConverter.convertToCardType;

public class BinBaseXmlProcessor extends BinBaseItemProcessor
        implements ItemProcessor<BinBaseXmlData, Map.Entry<BinData, Range<Long>>> {

    @Override
    public Map.Entry<BinData, Range<Long>> process(BinBaseXmlData binBaseXmlData) {
        BinData binData = new BinData();
        binData.setPaymentSystem(binBaseXmlData.getBrand());
        binData.setBankName(binBaseXmlData.getBank());
        binData.setIsoCountryCode(CountryCode.getByNumericCode(binBaseXmlData.getIsonumber()));
        binData.setCardType(convertToCardType(binBaseXmlData.getType()));
        binData.setCategory(binBaseXmlData.getCategory());
        Range<Long> range = buildRange(binBaseXmlData.getBin());
        return new AbstractMap.SimpleEntry<>(binData, range);
    }

}
