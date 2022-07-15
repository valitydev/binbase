package dev.vality.binbase.service.impl;

import com.google.common.collect.Range;
import dev.vality.binbase.dao.BinDataDao;
import dev.vality.binbase.dao.BinRangeDao;
import dev.vality.binbase.domain.BinData;
import dev.vality.binbase.domain.BinRange;
import dev.vality.binbase.exception.BinNotFoundException;
import dev.vality.binbase.exception.DaoException;
import dev.vality.binbase.exception.StorageException;
import dev.vality.binbase.service.BinbaseService;
import dev.vality.binbase.util.BinBaseConstant;
import dev.vality.binbase.util.BinRangeUtil;
import dev.vality.binbase.util.PanUtil;
import dev.vality.cds.storage.CardData;
import dev.vality.cds.storage.StorageSrv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BinbaseServiceImpl implements BinbaseService {

    private final BinRangeDao binRangeDao;
    private final BinDataDao binDataDao;
    private final StorageSrv.Iface cardStorageSrv;

    @Override
    public Map.Entry<Long, BinData> getBinDataByCardPan(String pan)
            throws BinNotFoundException, StorageException, IllegalArgumentException {
        try {
            log.info("Trying to get bin data by pan, pan='{}'", PanUtil.formatPan(pan));
            Map.Entry<Long, BinData> binDataWithVersion = binDataDao.getBinDataByCardPan(PanUtil.toLongValue(pan));
            if (binDataWithVersion == null) {
                throw new BinNotFoundException(String.format("Bin data not found, pan='%s'", PanUtil.formatPan(pan)));
            }
            log.info("Bin data have been retrieved, pan='{}', binDataWithVersion='{}'",
                    PanUtil.formatPan(pan), binDataWithVersion);
            return binDataWithVersion;
        } catch (DaoException ex) {
            String errorMessage = String.format("Failed to get bin data by card pan, pan='%s'",
                    PanUtil.formatPan(pan));
            throw new StorageException(errorMessage, ex);
        }
    }

    @Override
    public Map.Entry<Long, BinData> getBinDataByCardPanAndVersion(String pan, long version)
            throws BinNotFoundException, StorageException, IllegalArgumentException {
        try {
            log.info("Trying to get bin data by pan and version, pan='{}', version='{}'",
                    PanUtil.formatPan(pan), version);
            Map.Entry<Long, BinData> binDataWithVersion =
                    binDataDao.getBinDataByCardPanAndVersion(PanUtil.toLongValue(pan), version);
            if (binDataWithVersion == null) {
                String errorMessage = String.format("Bin data not found, pan='%s', version='%d'",
                        PanUtil.formatPan(pan), version);
                throw new BinNotFoundException(errorMessage);
            }
            log.info("Bin data have been retrieved, pan='{}', version='{}', binDataWithVersion='{}'",
                    PanUtil.formatPan(pan), version, binDataWithVersion);
            return binDataWithVersion;
        } catch (DaoException ex) {
            String errorMessage =
                    String.format("Failed to get bin data by card pan and version, pan='%s', version='%d'",
                            PanUtil.formatPan(pan), version);
            throw new StorageException(errorMessage, ex);
        }
    }

    @Override
    public Map.Entry<Long, BinData> getBinDataByBinDataId(Long binDataId)
            throws BinNotFoundException, StorageException, IllegalArgumentException {
        try {
            log.info("Trying to get bin data by binDataId, binDataId='{}'", binDataId);
            Map.Entry<Long, BinData> binDataWithVersion = binDataDao.getBinDataByBinDataId(binDataId);
            if (binDataWithVersion == null) {
                throw new BinNotFoundException(String.format("Bin data not found, binDataId='%s'", binDataId));
            }
            log.info("Bin data have been retrieved, binDataId='{}', binDataWithVersion='{}'",
                    binDataId, binDataWithVersion);
            return binDataWithVersion;
        } catch (DaoException ex) {
            String errorMessage = String.format("Failed to get bin data by binDataId, binDataId='%d'", binDataId);
            throw new StorageException(errorMessage, ex);
        }
    }

    @Override
    public Map.Entry<Long, BinData> getBinDataByCardToken(String cardToken)
            throws BinNotFoundException, StorageException, IllegalArgumentException {
        try {
            CardData cardData = cardStorageSrv.getCardData(cardToken);
            String pan = cardData.getPan();
            return getBinDataByCardPan(pan);
        } catch (TException e) {
            throw new IllegalArgumentException(String.format("Incorrect cardToken, cardToken='%s'", cardToken));
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRange(BinData binData, Range<Long> range)
            throws StorageException, IllegalArgumentException {
        log.info("Trying to save bin range, binData='{}', range='{}'", binData, range);
        try {
            if (!isCorrectRange(range)) {
                throw new IllegalArgumentException(String.format("Incorrect range, range='%s'", range));
            }

            long binDataId = saveBinData(binData);
            List<BinRange> lastIntersectionRanges = getLastIntersectionBinRanges(range);

            List<Range<Long>> intersectionRanges = new ArrayList<>();
            List<BinRange> newBinRanges = new ArrayList<>();
            for (BinRange intersectionRange : lastIntersectionRanges) {
                if (intersectionRange.getRange().isConnected(range)
                        && !intersectionRange.getRange().intersection(range).isEmpty()) {
                    Range<Long> newIntersectRange = intersectionRange.getRange().intersection(range);
                    intersectionRanges.add(newIntersectRange);
                    if (intersectionRange.getBinDataId().equals(binDataId)) {
                        log.info("Range of intersections with same data was found, binData='{}', " +
                                        "range='{}', intersectionRange='{}'. Skipped...",
                                binData, range, intersectionRange);
                        continue;
                    }
                    long versionId = intersectionRange.getVersionId() + 1L;
                    newBinRanges.add(new BinRange(newIntersectRange, versionId, binDataId));
                }
            }
            if (!newBinRanges.isEmpty()) {
                log.info("Ranges with new version was created, binData='{}', range='{}', rangesWithNewVersion='{}'",
                        binData, range, newBinRanges);
            }

            List<BinRange> otherRanges = BinRangeUtil.subtractFromRange(range, intersectionRanges).stream()
                    .map(
                            subtractRange -> new BinRange(subtractRange, 1L, binDataId)
                    ).collect(Collectors.toList());
            if (!otherRanges.isEmpty()) {
                log.info("New ranges was created, binData='{}', range='{}', newRanges='{}'",
                        binData, range, otherRanges);
                newBinRanges.addAll(otherRanges);
            }

            if (!newBinRanges.isEmpty()) {
                binRangeDao.save(newBinRanges);
                log.info("Bin range have been saved, binData='{}', range='{}', binRanges='{}'",
                        binData, range, newBinRanges);
            } else {
                log.info("No new ranges were created, nothing to save, binData='{}', range='{}'",
                        binData, range);
            }
        } catch (DaoException ex) {
            String errorMessage = String.format("Failed to save range, binData='%s', range='%s'", binData, range);
            throw new StorageException(errorMessage, ex);
        }
    }

    private boolean isCorrectRange(Range<Long> range) {
        return range.lowerEndpoint() > BinBaseConstant.MIN_LOWER_ENDPOINT
                && range.upperEndpoint() < BinBaseConstant.MAX_UPPER_ENDPOINT;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public long saveBinData(BinData binData)
            throws StorageException {
        try {
            log.info("Trying to save bin data, binData='{}'", binData);
            long id = binDataDao.save(binData);
            log.info("Bin data have been saved, id='{}', binData='{}'", id, binData);
            return id;
        } catch (DaoException ex) {
            throw new StorageException(String.format("Failed to save bin data, binData='%s'", binData), ex);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<BinRange> getLastIntersectionBinRanges(Range<Long> range)
            throws StorageException {
        try {
            log.info("Trying to get last ranges of intersections, range='{}'", range);
            List<BinRange> lastIntersectionRanges =
                    BinRangeUtil.getLastIntersectionRanges(binRangeDao.getIntersectionRanges(range));
            log.info("Last ranges of intersections have been retrieved, range='{}', lastIntersectionRanges='{}'",
                    range, lastIntersectionRanges);
            return lastIntersectionRanges;
        } catch (DaoException ex) {
            String errorMessage = String.format("Failed to get last ranges of intersections, range='%s'", range);
            throw new StorageException(errorMessage, ex);
        }
    }

}
