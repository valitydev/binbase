package dev.vality.binbase.service;

import com.google.common.collect.Range;
import dev.vality.binbase.domain.BinData;
import dev.vality.binbase.domain.BinRange;
import dev.vality.binbase.exception.BinNotFoundException;
import dev.vality.binbase.exception.StorageException;

import java.util.List;
import java.util.Map;

public interface BinbaseService {

    Map.Entry<Long, BinData> getBinDataByCardPan(String pan)
            throws BinNotFoundException, StorageException, IllegalArgumentException;

    Map.Entry<Long, BinData> getBinDataByCardPanAndVersion(String pan, long version)
            throws BinNotFoundException, StorageException, IllegalArgumentException;

    Map.Entry<Long, BinData> getBinDataByBinDataId(Long binDataId)
            throws BinNotFoundException, StorageException, IllegalArgumentException;

    Map.Entry<Long, BinData> getBinDataByCardToken(String cardToken)
            throws BinNotFoundException, StorageException, IllegalArgumentException;

    void saveRange(BinData binData, Range<Long> range)
            throws StorageException, IllegalArgumentException;

    long saveBinData(BinData binData)
            throws StorageException;

    List<BinRange> getLastIntersectionBinRanges(Range<Long> range)
            throws StorageException;

}
