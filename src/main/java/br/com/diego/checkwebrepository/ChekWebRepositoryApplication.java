package br.com.diego.checkwebrepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ChekWebRepositoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChekWebRepositoryApplication.class, args);
	}

}
