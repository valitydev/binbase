package dev.vality.binbase.service;

import com.google.common.collect.Range;
import dev.vality.binbase.config.PostgresqlTest;
import dev.vality.binbase.domain.BinData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Random;

import static dev.vality.binbase.util.PanUtil.toLongValue;
import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@PostgresqlTest
@SpringBootTest
class BinbaseServiceTest {

    @Autowired
    private BinbaseService binbaseService;

    @Test
    void testSaveRangesWithSameData() {
        BinData binData = random(BinData.class);
        binbaseService.saveRange(binData, Range.openClosed(toLongValue("100000"), toLongValue("200000")));
        binbaseService.saveRange(binData, Range.openClosed(toLongValue("300000"), toLongValue("400000")));
        binbaseService.saveRange(binData, Range.openClosed(toLongValue("100000"), toLongValue("400000")));

        new Random().longs(100L, toLongValue("100000"), toLongValue("200000"))
                .forEach(
                        panValue -> {
                            Map.Entry<Long, BinData> binDataWithVersion =
                                    binbaseService.getBinDataByCardPan(String.valueOf(panValue));
                            assertEquals(1L, (long) binDataWithVersion.getKey());
                            assertEquals(binData, binDataWithVersion.getValue());
                        });

        new Random().longs(100L, toLongValue("200000"), toLongValue("300000"))
                .forEach(
                        panValue -> {
                            Map.Entry<Long, BinData> binDataWithVersion =
                                    binbaseService.getBinDataByCardPan(String.valueOf(panValue));
                            assertEquals(1L, (long) binDataWithVersion.getKey());
                            assertEquals(binData, binDataWithVersion.getValue());
                        });

        new Random().longs(100L, toLongValue("300000"), toLongValue("400000"))
                .forEach(
                        panValue -> {
                            Map.Entry<Long, BinData> binDataWithVersion =
                                    binbaseService.getBinDataByCardPan(String.valueOf(panValue));
                            assertEquals(1L, (long) binDataWithVersion.getKey());
                            assertEquals(binData, binDataWithVersion.getValue());
                        });
    }

    @Test
    void testSaveRangesWithSameDataWithZero() {
        BinData binData = random(BinData.class);
        binbaseService.saveRange(binData, Range.openClosed(toLongValue("030000"), toLongValue("040000")));
        binbaseService.saveRange(binData, Range.openClosed(toLongValue("003000"), toLongValue("004000")));

        Map.Entry<Long, BinData> binDataWithVersion = binbaseService.getBinDataByCardPan("003292003292003292");
        assertEquals(1L, (long) binDataWithVersion.getKey());
        assertEquals(binData, binDataWithVersion.getValue());

        binDataWithVersion = binbaseService.getBinDataByCardPan("003292");
        assertEquals(1L, (long) binDataWithVersion.getKey());
        assertEquals(binData, binDataWithVersion.getValue());

        binDataWithVersion = binbaseService.getBinDataByCardPan("032923");
        assertEquals(1L, (long) binDataWithVersion.getKey());
        assertEquals(binData, binDataWithVersion.getValue());
    }

    @Test
    void testSaveRangesWithDifferentData() {
        BinData firstBinData = random(BinData.class);
        binbaseService.saveRange(firstBinData, Range.openClosed(toLongValue("100000"), toLongValue("800000")));
        BinData secondBinData = random(BinData.class);
        binbaseService.saveRange(secondBinData, Range.openClosed(toLongValue("200000"), toLongValue("600000")));
        BinData thirdBinData = random(BinData.class);
        binbaseService.saveRange(thirdBinData, Range.openClosed(toLongValue("300000"), toLongValue("900000")));

        new Random().longs(100L, toLongValue("100000"), toLongValue("200000"))
                .forEach(
                        panValue -> {
                            Map.Entry<Long, BinData> binDataWithVersion =
                                    binbaseService.getBinDataByCardPan(String.valueOf(panValue));
                            assertEquals(1L, (long) binDataWithVersion.getKey());
                            assertEquals(firstBinData, binDataWithVersion.getValue());
                        });

        new Random().longs(100L, toLongValue("200000"), toLongValue("300000"))
                .forEach(
                        panValue -> {
                            Map.Entry<Long, BinData> binDataWithVersion =
                                    binbaseService.getBinDataByCardPan(String.valueOf(panValue));
                            assertEquals(2L, (long) binDataWithVersion.getKey());
                            assertEquals(secondBinData, binDataWithVersion.getValue());
                        });

        new Random().longs(100L, toLongValue("300000"), toLongValue("600000"))
                .forEach(
                        panValue -> {
                            Map.Entry<Long, BinData> binDataWithVersion =
                                    binbaseService.getBinDataByCardPan(String.valueOf(panValue));
                            assertEquals(3L, (long) binDataWithVersion.getKey());
                            assertEquals(thirdBinData, binDataWithVersion.getValue());
                        });

        new Random().longs(100L, toLongValue("600000"), toLongValue("800000"))
                .forEach(
                        panValue -> {
                            Map.Entry<Long, BinData> binDataWithVersion =
                                    binbaseService.getBinDataByCardPan(String.valueOf(panValue));
                            assertEquals(2L, (long) binDataWithVersion.getKey());
                            assertEquals(thirdBinData, binDataWithVersion.getValue());
                        });

        new Random().longs(100L, toLongValue("800000"), toLongValue("900000"))
                .forEach(
                        panValue -> {
                            Map.Entry<Long, BinData> binDataWithVersion =
                                    binbaseService.getBinDataByCardPan(String.valueOf(panValue));
                            assertEquals(1L, (long) binDataWithVersion.getKey());
                            assertEquals(thirdBinData, binDataWithVersion.getValue());
                        });

        Map.Entry<Long, BinData> binDataWithVersion = binbaseService.getBinDataByCardPanAndVersion("52304000", 2L);
        assertEquals(2L, (long) binDataWithVersion.getKey());
        assertEquals(secondBinData, binDataWithVersion.getValue());
    }

    @Test
    void testSaveMinAndMaxValueOfPan() {
        BinData binData = random(BinData.class);
        String lowerPan = "1000000000000000000";
        String upperPan = "9999999999999999999";
        Range<Long> range = Range.openClosed(toLongValue(lowerPan), toLongValue(upperPan));
        assertDoesNotThrow(() -> binbaseService.saveRange(binData, range));
    }

}
