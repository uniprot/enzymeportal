/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Configuration
@EnableAsync
public class EnzymePortalAsyncConfig {

    private final int corePoolSize = 5;
    private final int maxPoolSize = 50;
    private final int keepAliveSeconds = 120;
    private final int queueCapacity = 10;

    @Bean
    public TaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.setCorePoolSize(corePoolSize);
        te.setMaxPoolSize(maxPoolSize);
        te.setQueueCapacity(queueCapacity);
        te.setKeepAliveSeconds(keepAliveSeconds);
        return te;
    }
}
