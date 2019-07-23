package uk.ac.ebi.ep.xml.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;
import uk.ac.ebi.ep.xml.util.Preconditions;

/**
 * Logs events related to chunk processing.
 *
 * This listener is NOT thread safe.
 *
 * @author Joseph
 */
@Slf4j
public class LogChunkListener implements ChunkListener {
   // private static final Logger logger = LoggerFactory.getLogger(LogChunkListener.class);

    private long counter;
    private long startProcessingTime;

    private final int chunkSize;

    public LogChunkListener(int chunkSize) {
        Preconditions.checkArgument(chunkSize < 1, "Chunk size can not be negative");
        this.chunkSize = chunkSize;
    }

    @Override public void beforeChunk(ChunkContext context) {
        startProcessingTime = System.currentTimeMillis();
    }

    @Override public void afterChunk(ChunkContext context) {
        long finishProcessing = System.currentTimeMillis();
        counter += chunkSize;

        String timeText = DateTimeUtil.convertToText(startProcessingTime, finishProcessing);

        log.warn("Time taken to process current chunk [" + chunkSize + "]: " + timeText + ". Total processed: "
                + counter);
    }

    @Override public void afterChunkError(ChunkContext context) {
        //do nothing
    }
}