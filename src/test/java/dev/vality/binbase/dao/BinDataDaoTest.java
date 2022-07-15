package dev.vality.binbase.dao;

import dev.vality.binbase.AbstractIntegrationTest;
import dev.vality.binbase.config.PostgresqlTest;
import dev.vality.binbase.domain.BinData;
import dev.vality.binbase.domain.BinRange;
import dev.vality.binbase.exception.DaoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@PostgresqlTest
class BinDataDaoTest extends AbstractIntegrationTest {

    @Autowired
    private BinDataDao binDataDao;

    @Autowired
    private BinRangeDao binRangeDao;

    @Test
    void testGetBinDataByPan() throws DaoException {
        BinData binData = random(BinData.class);

        long binDataId = binDataDao.save(binData);
        binRangeDao.save(new BinRange(1000000000000000000L, 2000000000000000000L, 1L, binDataId));

        assertEquals(binData, binDataDao.getBinDataByCardPan(1230679997775486545L).getValue());
        assertEquals(binData, binDataDao.getBinDataByCardPanAndVersion(1230679997775486545L, 1).getValue());
    }

    @Test
    void testGetBinDataById() throws DaoException {
        BinData binData = random(BinData.class);

        long binDataId = binDataDao.save(binData);
        binRangeDao.save(new BinRange(1L, 2L, 1L, binDataId));
        binRangeDao.save(new BinRange(2L, 3L, 1L, binDataId));
        long binRangeId = binRangeDao.save(new BinRange(3L, 4L, 1L, binDataId));

        assertEquals(binData, binDataDao.getBinDataByBinDataId(binRangeId).getValue());
    }

    @Test
    void testSaveBinData() throws DaoException {
        BinData binData = random(BinData.class);

        long binDataId = binDataDao.save(binData);
        long binRangeId = binRangeDao.save(new BinRange(1L, 2L, 1L, binDataId));

        assertEquals(binDataId, binDataDao.save(binData));
        assertEquals(binData, binDataDao.getBinDataByBinDataId(binRangeId).getValue());
    }

    @Test
    void testSaveBinDataWithEmptyValues() throws DaoException {
        BinData binData = new BinData();
        binData.setPaymentSystem("visa");

        long binDataId = binDataDao.save(binData);
        long binRangeId = binRangeDao.save(new BinRange(1L, 2L, 1L, binDataId));

        assertEquals(binDataId, binDataDao.save(binData));
        assertEquals(binData, binDataDao.getBinDataByBinDataId(binRangeId).getValue());
    }
}
