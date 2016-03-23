package uk.ac.ebi.ep.xml.util;

import java.util.concurrent.TimeUnit;
import javax.batch.api.chunk.listener.ChunkListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs events related to chunk processing.
 *
 * This listener is NOT thread safe.
 *
 * @author Ricardo Antunes
 */
public class LogChunkListener implements ChunkListener {
    private static final Logger logger = LoggerFactory.getLogger(LogChunkListener.class);

    private long counter;
    private long startProcessingTime;

    private final int chunkSize;

    public LogChunkListener(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    @Override public void beforeChunk() throws Exception {
        startProcessingTime = System.nanoTime();
    }

    @Override public void onError(Exception e) throws Exception {

    }

    @Override public void afterChunk() throws Exception {
        long finishProcessing = System.nanoTime();
        counter += chunkSize;

        String timeText = TimeUtil.convertToText(startProcessingTime, finishProcessing, TimeUnit.NANOSECONDS);

        logger.info("Time taken to process current chunk [" + counter + "]: " + timeText + ". Total processed: "
                + counter);
    }
}
