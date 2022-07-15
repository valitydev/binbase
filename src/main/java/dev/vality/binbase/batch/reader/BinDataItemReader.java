package dev.vality.binbase.batch.reader;

import dev.vality.binbase.batch.reader.classifier.ResourceClassifier;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.Resource;

import java.util.Objects;

import static dev.vality.binbase.util.BinBaseConstant.FILE_EXTENSION_CSV;
import static dev.vality.binbase.util.BinBaseConstant.FILE_EXTENSION_XML;

@RequiredArgsConstructor
public class BinDataItemReader<T> implements ResourceAwareItemReaderItemStream<T> {

    private ResourceClassifier<T> classifier;
    private Resource resource;
    private final StaxEventItemReader<T> staxEventItemReader;
    private final FlatFileItemReader<T> flatFileItemReader;

    public void setClassifier(ResourceClassifier<T> classifier) {
        this.classifier = classifier;
    }

    @Override
    public T read() throws Exception {
        return readItem(classifier.classify(resource));
    }

    private T readItem(ResourceAwareItemReaderItemStream<T> reader) throws Exception {
        return reader.read();
    }

    @Override
    public void setResource(@NotNull Resource resource) {
        this.resource = resource;
    }

    @Override
    public void open(@NotNull ExecutionContext executionContext) throws ItemStreamException {
        String fileExtension = Objects.requireNonNull(FilenameUtils.getExtension(resource.getFilename()));
        switch (fileExtension) {
            case FILE_EXTENSION_XML -> {
                staxEventItemReader.setResource(resource);
                staxEventItemReader.open(executionContext);
            }
            case FILE_EXTENSION_CSV -> {
                flatFileItemReader.setResource(resource);
                flatFileItemReader.open(executionContext);
            }
            default -> throw new IllegalStateException("Unexpected extension: " + fileExtension);
        }
    }

    @Override
    public void update(@NotNull ExecutionContext executionContext) throws ItemStreamException {
        staxEventItemReader.update(executionContext);
        flatFileItemReader.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        staxEventItemReader.close();
        flatFileItemReader.close();
    }
}
