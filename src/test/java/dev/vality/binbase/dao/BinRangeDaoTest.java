package dev.vality.binbase.dao;

import com.google.common.collect.Range;
import dev.vality.binbase.config.PostgresqlTest;
import dev.vality.binbase.domain.BinData;
import dev.vality.binbase.domain.BinRange;
import dev.vality.binbase.exception.DaoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@PostgresqlTest
@SpringBootTest
class BinRangeDaoTest {

    public static final long LOWER_EDGE = 1_000_000_000_000_000_000L;
    public static final long UPPER_EDGE = 2_000_000_000_000_000_000L;
    public static final long UPPER_ENDPOINT = 3_000_000_000_000_000_000L;
    public static final long MORE_UPPER_ENDPOINT = 4_000_000_000_000_000_000L;
    @Autowired
    private BinRangeDao binRangeDao;

    @Autowired
    private BinDataDao binDataDao;

    @Test
    void testSaveAndGetByCardPanAndRange() throws DaoException {
        BinData binData = random(BinData.class);
        long binDataId = binDataDao.save(binData);

        BinRange binRange = new BinRange();
        binRange.setRange(Range.openClosed(LOWER_EDGE, UPPER_EDGE));
        binRange.setVersionId(1L);
        binRange.setBinDataId(binDataId);
        binRangeDao.save(binRange);
        assertEquals(binRange, binRangeDao.getIntersectionRanges(binRange.getRange()).get(0));
    }

    @Test
    void testWhenRangesConflict() throws DaoException {
        BinData binData = random(BinData.class);
        long binDataId = binDataDao.save(binData);

        BinRange binRange = new BinRange();
        binRange.setRange(Range.openClosed(LOWER_EDGE, UPPER_EDGE));
        binRange.setVersionId(1L);
        binRange.setBinDataId(binDataId);
        binRangeDao.save(binRange);

        long lower = 1_500_000_000_000_000_000L;
        long upper = 2_500_000_000_000_000_000L;
        binRange.setRange(Range.openClosed(lower, upper));
        binRangeDao.save(binRange);

        assertEquals(1, binRangeDao.getIntersectionRanges(Range.openClosed(LOWER_EDGE, upper)).size());
    }

    @Test
    void testMergeAdjacent() throws DaoException {
        BinData binData = random(BinData.class);
        long binDataId = binDataDao.save(binData);

        BinRange binRange = new BinRange();
        binRange.setRange(Range.openClosed(LOWER_EDGE, UPPER_EDGE));
        binRange.setVersionId(1L);
        binRange.setBinDataId(binDataId);
        binRangeDao.save(binRange);

        long upper = 3_000_000_000_000_000_000L;
        binRange.setRange(Range.openClosed(UPPER_EDGE, upper));
        binRangeDao.save(binRange);

        long moreUpper = 4_000_000_000_000_000_000L;
        binRange.setRange(Range.openClosed(upper, moreUpper));
        binRangeDao.save(binRange);

        assertEquals(1, binRangeDao.getIntersectionRanges(Range.openClosed(LOWER_EDGE, UPPER_EDGE)).size());
    }

    @Test
    void testBatchInsertBinRanges() throws DaoException {
        BinData binData = random(BinData.class);
        long binDataId = binDataDao.save(binData);
        List<BinRange> binRanges = List.of(
                new BinRange(LOWER_EDGE, UPPER_EDGE, 1L, binDataId),
                new BinRange(UPPER_EDGE, UPPER_ENDPOINT, 1L, binDataId),
                new BinRange(UPPER_ENDPOINT, MORE_UPPER_ENDPOINT, 1L, binDataId)
        );
        assertDoesNotThrow(() -> binRangeDao.save(binRanges));

    }

}
