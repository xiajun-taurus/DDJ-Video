package com.xiajun;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author xiajun
 */
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.xiajun.mapper")
@Slf4j
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		log.info("启动成功");
	}
	
}
