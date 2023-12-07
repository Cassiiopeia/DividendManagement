package com.springboot.dividendmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();
        int coreCount = Runtime.getRuntime().availableProcessors();
        threadPool.setPoolSize(coreCount + 1);
        threadPool.initialize();
        taskRegistrar.setTaskScheduler(threadPool);
    }
}
