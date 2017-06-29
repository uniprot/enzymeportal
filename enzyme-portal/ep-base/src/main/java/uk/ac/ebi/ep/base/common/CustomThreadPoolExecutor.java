package uk.ac.ebi.ep.base.common;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public class CustomThreadPoolExecutor extends ThreadPoolExecutor {

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        allowCoreThreadTimeOut(true);
    }

    public void setThreads(int n) {
        setMaximumPoolSize(Math.max(1, n));
        setCorePoolSize(Math.max(1, n));
    }
}
