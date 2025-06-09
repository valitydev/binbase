package dev.vality.binbase.batch.writer;

import com.google.common.collect.Range;
import dev.vality.binbase.domain.BinData;
import dev.vality.binbase.service.BinbaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BinRangeWriter implements ItemWriter<Map.Entry<BinData, Range<Long>>> {

    private final BinbaseService binbaseService;

    @Override
    public void write(Chunk<? extends Map.Entry<BinData, Range<Long>>> binDataRanges) throws Exception {
        binDataRanges.forEach(binDataRange -> binbaseService.saveRange(binDataRange.getKey(), binDataRange.getValue()));
    }

}
