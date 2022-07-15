package dev.vality.binbase.util;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import dev.vality.binbase.domain.BinRange;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@UtilityClass
public class BinRangeUtil {

    public static List<BinRange> getLastIntersectionRanges(List<BinRange> binRanges) {
        List<BinRange> intersectionRanges = new ArrayList<>(binRanges);
        for (int i = 0; i < intersectionRanges.size(); i++) {
            for (int j = i + 1; j < intersectionRanges.size(); j++) {
                BinRange first = intersectionRanges.get(i);
                BinRange second = intersectionRanges.get(j);

                if (first.getRange().isConnected(second.getRange())
                        && !first.getRange().intersection(second.getRange()).isEmpty()) {
                    intersectionRanges.remove(first);
                    intersectionRanges.remove(second);

                    Range<Long> range = first.getRange().intersection(second.getRange());
                    BinRange newRange = first.getVersionId() > second.getVersionId()
                            ? new BinRange(range, first.getVersionId(), first.getBinDataId())
                            : new BinRange(range, second.getVersionId(), second.getBinDataId());
                    intersectionRanges.add(newRange);
                    if (!first.getRange().lowerEndpoint().equals(second.getRange().lowerEndpoint())) {
                        BinRange leftRange = first.getRange().lowerEndpoint() < second.getRange().lowerEndpoint()
                                ? new BinRange(
                                first.getRange().lowerEndpoint(),
                                second.getRange().lowerEndpoint(),
                                first.getVersionId(),
                                first.getBinDataId())
                                : new BinRange(
                                second.getRange().lowerEndpoint(),
                                first.getRange().lowerEndpoint(),
                                second.getVersionId(),
                                second.getBinDataId());
                        intersectionRanges.add(leftRange);
                    }
                    if (!first.getRange().upperEndpoint().equals(second.getRange().upperEndpoint())) {
                        BinRange rightRange = first.getRange().upperEndpoint() < second.getRange().upperEndpoint()
                                ? new BinRange(
                                first.getRange().upperEndpoint(),
                                second.getRange().upperEndpoint(),
                                second.getVersionId(),
                                second.getBinDataId())
                                : new BinRange(
                                second.getRange().upperEndpoint(),
                                first.getRange().upperEndpoint(),
                                first.getVersionId(),
                                first.getBinDataId());
                        intersectionRanges.add(rightRange);
                    }
                    return getLastIntersectionRanges(intersectionRanges);
                }
            }
        }
        intersectionRanges.sort(Comparator.comparing(o -> o.getRange().lowerEndpoint()));
        return intersectionRanges;
    }

    public static <T extends Comparable> Set<Range<T>> subtractFromRange(Range<T> range,
                                                                         List<Range<T>> rangesForSubtract) {
        RangeSet<T> result = TreeRangeSet.create();
        result.add(range);
        rangesForSubtract.forEach(subtract -> result.remove(subtract));
        return result.asRanges();
    }

}
