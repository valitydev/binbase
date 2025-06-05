package dev.vality.binbase.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

@Slf4j
public class DefaultChunkListener implements ChunkListener {

    private final int loggingInterval = 10000;

    @Override
    public void beforeChunk(ChunkContext context) {
        //nothing
    }

    @Override
    public void afterChunk(ChunkContext context) {
        long readCount = context.getStepContext().getStepExecution().getReadCount();
        if (readCount > 0 && readCount % loggingInterval == 0) {
            log.info("{} rows processed", readCount);
        }
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        //nothing
    }
}
