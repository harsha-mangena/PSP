package com.enterprise.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Dedicated pool for the parallel product lookup / stock validation calls, so
 * they never contend with Tomcat's request threads.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "cartTaskExecutor")
    public Executor cartTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("cart-async-");
        executor.initialize();
        return executor;
    }
}
