package com.baton.Exchange;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class ExchangeApplication {

	@Value("${threadpool.corepoolsize}")
	int corePoolSize;

	@Value("${threadpool.maxpoolsize}")
	int maxPoolSize;

	@Value("${threadpool.keepaliveseconds}")
	int keepAliveSeconds;

	@Value("${threadpool.queuecapacity}")
	int queueCapacity;

	public static void main(String[] args) {
		SpringApplication.run(ExchangeApplication.class, args);
	}

	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(corePoolSize);
		pool.setMaxPoolSize(maxPoolSize);
		pool.setKeepAliveSeconds(keepAliveSeconds);
		pool.setQueueCapacity(queueCapacity);
		pool.setThreadNamePrefix("Baton-");
		pool.setWaitForTasksToCompleteOnShutdown(true);
		pool.initialize();
		return pool;
	}
}
