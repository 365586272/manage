package com.cy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableCaching
@EnableAsync//会在spring启动时创建一个线程池,启动异步配置
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		/*
		 * int s= Runtime.getRuntime().availableProcessors(); System.out.println(s);
		 */
		SpringApplication.run(Application.class, args);
	}

}
