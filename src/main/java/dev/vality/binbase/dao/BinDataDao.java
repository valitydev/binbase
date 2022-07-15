package dev.vality.binbase.dao;

import dev.vality.binbase.domain.BinData;
import dev.vality.binbase.exception.DaoException;

import java.util.Map;

public interface BinDataDao {

    Map.Entry<Long, BinData> getBinDataByCardPan(long pan)
            throws DaoException;

    Map.Entry<Long, BinData> getBinDataByCardPanAndVersion(long pan, long versionId)
            throws DaoException;

    Map.Entry<Long, BinData> getBinDataByBinDataId(long id)
            throws DaoException;

    long save(BinData binData)
            throws DaoException;

}
