package dev.vality.binbase.dao;

import com.google.common.collect.Range;
import dev.vality.binbase.domain.BinRange;
import dev.vality.binbase.exception.DaoException;

import java.util.List;

public interface BinRangeDao {

    List<BinRange> getIntersectionRanges(Range<Long> range) throws DaoException;

    long save(BinRange binRange) throws DaoException;

    void save(List<BinRange> binRanges) throws DaoException;
}
