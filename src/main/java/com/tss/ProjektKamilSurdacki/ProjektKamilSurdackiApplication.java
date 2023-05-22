package com.tss.ProjektKamilSurdacki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages={"com.tss"})
public class ProjektKamilSurdackiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjektKamilSurdackiApplication.class, args);
	}
}
