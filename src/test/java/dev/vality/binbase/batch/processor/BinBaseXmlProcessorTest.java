package dev.vality.binbase.batch.processor;

import com.google.common.collect.Range;
import dev.vality.binbase.batch.BinBaseXmlData;
import dev.vality.binbase.domain.BinData;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BinBaseXmlProcessorTest {

    @Test
    void process() {
        BinBaseXmlData binBaseXmlData = new BinBaseXmlData();
        binBaseXmlData.setBin("021502");
        binBaseXmlData.setBrand("PRIVATE LABEL");
        binBaseXmlData.setBank("");
        binBaseXmlData.setType("DEBIT");
        binBaseXmlData.setCategory("");
        binBaseXmlData.setIsoname("UNITED STATES");
        binBaseXmlData.setIsoa2("US");
        binBaseXmlData.setIsoa3("USA");
        binBaseXmlData.setIsonumber("840");
        binBaseXmlData.setUrl("");
        binBaseXmlData.setPhone("");

        BinBaseXmlProcessor binBaseXmlProcessor = new BinBaseXmlProcessor();
        Map.Entry<BinData, Range<Long>> dataRangeEntry = binBaseXmlProcessor.process(binBaseXmlData);
        System.out.println(dataRangeEntry);
        assertEquals(Range.openClosed(21502000000000000L, 21503000000000000L), dataRangeEntry.getValue());

        binBaseXmlData.setBin("001502");
        dataRangeEntry = binBaseXmlProcessor.process(binBaseXmlData);
        System.out.println(dataRangeEntry);
        assertEquals(Range.openClosed(1502000000000000L, 1503000000000000L), dataRangeEntry.getValue());

        binBaseXmlData.setBin("000502");
        dataRangeEntry = binBaseXmlProcessor.process(binBaseXmlData);
        System.out.println(dataRangeEntry);
        assertEquals(Range.openClosed(502000000000000L, 503000000000000L), dataRangeEntry.getValue());

    }

}