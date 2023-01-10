package com.shzhangji.proton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class ProtonApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProtonApplication.class, args);
	}

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests()
        .requestMatchers("/api/login").permitAll()
        .requestMatchers("/api/**").authenticated()
        .and().csrf().disable()
        .build();
  }
}
