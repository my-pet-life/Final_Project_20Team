package com.example.mypetlife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MypetlifeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MypetlifeApplication.class, args);
	}

}
