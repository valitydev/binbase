package dev.vality.binbase.batch.processor.classifier;

import com.google.common.collect.Range;
import dev.vality.binbase.batch.BinBaseData;
import dev.vality.binbase.batch.processor.BinBaseCsvProcessor;
import dev.vality.binbase.batch.processor.BinBaseXmlProcessor;
import dev.vality.binbase.domain.BinData;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.Map;

@RequiredArgsConstructor
public class ProcessorClassifier
        implements Classifier<BinBaseData, ItemProcessor<?, Map.Entry<BinData, Range<Long>>>> {

    private final BinBaseXmlProcessor binBaseXmlProcessor;
    private final BinBaseCsvProcessor binBaseCsvProcessor;

    @Override
    public ItemProcessor<?, Map.Entry<BinData, Range<Long>>> classify(BinBaseData data) {
        return switch (data.getDataType()) {
            case XML -> binBaseXmlProcessor;
            case CSV -> binBaseCsvProcessor;
        };
    }
}
