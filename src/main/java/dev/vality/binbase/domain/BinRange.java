package dev.vality.binbase.domain;

import com.google.common.collect.Range;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BinRange {

    private Range<Long> range;
    private Long versionId;
    private Long binDataId;

    public BinRange(Long lowerEndpoint, Long upperEndpoint, Long versionId, Long binDataId) {
        this(Range.openClosed(lowerEndpoint, upperEndpoint), versionId, binDataId);
    }
}
