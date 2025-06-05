package dev.vality.binbase.config;

import com.google.common.collect.Range;
import dev.vality.binbase.batch.BinBaseCsvData;
import dev.vality.binbase.batch.BinBaseXmlData;
import dev.vality.binbase.batch.listener.DefaultChunkListener;
import dev.vality.binbase.batch.processor.BinBaseCsvProcessor;
import dev.vality.binbase.batch.processor.BinBaseXmlProcessor;
import dev.vality.binbase.batch.processor.classifier.ProcessorClassifier;
import dev.vality.binbase.batch.reader.BinDataItemReader;
import dev.vality.binbase.batch.reader.classifier.ResourceClassifier;
import dev.vality.binbase.batch.writer.BinRangeWriter;
import dev.vality.binbase.domain.BinData;
import dev.vality.binbase.service.BinbaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemProcessorBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@RequiredArgsConstructor
public class BatchConfig {

    private static final String CSV_DELIMITER = ";";
    private static final String[] FILE_CSV_FIELDS = new String[]{
            "bin", "brand", "bank", "type", "category", "isoname", "isoa2", "isoa3",
            "isonumber", "url", "phone", "bin_length", "affiliation", "mark"
    };

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final BinbaseService binbaseService;

    @Value("${batch.strict_mode}")
    private boolean strictMode;

    @Bean
    @StepScope
    public MultiResourceItemReader multiResourceItemReader(BinDataItemReader itemReader,
                                                           @Value("${batch.file_path}/*.*") Resource[] resources) {
        return new MultiResourceItemReaderBuilder<Resource[]>()
                .name("multiResourceItemReader")
                .delegate(itemReader)
                .resources(resources)
                .setStrict(strictMode)
                .saveState(true)
                .build();
    }

    @Bean
    public BinDataItemReader binBaseDataBinDataItemReader() {
        BinDataItemReader binDataItemReader =
                new BinDataItemReader(buildBinBaseXmlReader(), buildBinBasePsbCsvReader());
        binDataItemReader.setClassifier(new ResourceClassifier(
                buildBinBaseXmlReader(), buildBinBasePsbCsvReader()));
        return binDataItemReader;
    }

    @Bean
    public StaxEventItemReader<BinBaseXmlData> buildBinBaseXmlReader() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(BinBaseXmlData.class);

        StaxEventItemReader<BinBaseXmlData> staxEventItemReader = new StaxEventItemReader<>();
        staxEventItemReader.setFragmentRootElementName("record");
        staxEventItemReader.setUnmarshaller(marshaller);
        staxEventItemReader.setStrict(strictMode);
        staxEventItemReader.setSaveState(true);

        return staxEventItemReader;
    }

    @Bean
    public FlatFileItemReader<BinBaseCsvData> buildBinBasePsbCsvReader() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(FILE_CSV_FIELDS);
        lineTokenizer.setDelimiter(CSV_DELIMITER);
        lineTokenizer.setStrict(strictMode);

        BeanWrapperFieldSetMapper<BinBaseCsvData> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BinBaseCsvData.class);

        DefaultLineMapper<BinBaseCsvData> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        FlatFileItemReader<BinBaseCsvData> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setLineMapper(lineMapper);
        return flatFileItemReader;
    }

    @Bean
    public Job binBaseJob(Step step) {
        return new JobBuilder("binBaseJob", jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public ClassifierCompositeItemProcessor compositeProcessor() {
        return new ClassifierCompositeItemProcessorBuilder()
                .classifier(new ProcessorClassifier(new BinBaseXmlProcessor(), new BinBaseCsvProcessor()))
                .build();
    }


    @Bean
    public Step step(MultiResourceItemReader<BinData> multiResourceItemReader) {
        return new StepBuilder("binBaseStep", jobRepository)
                .<BinData, Map.Entry<BinData, Range<Long>>>chunk(1000, transactionManager)
                .reader(multiResourceItemReader)
                .processor(compositeProcessor())
                .writer(new BinRangeWriter(binbaseService))
                .listener(new DefaultChunkListener())
                .build();
    }
}
