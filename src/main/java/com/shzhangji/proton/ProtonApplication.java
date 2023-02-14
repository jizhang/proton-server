package com.shzhangji.proton;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import java.lang.annotation.Inherited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;

@OpenAPIDefinition(
    info = @Info(
        title = "Proton",
        version = "0.1.0",
        description = "A GA-like dashboard."
    )
)
@SpringBootApplication
public class ProtonApplication {
  public static void main(String[] args) {
    SpringApplication.run(ProtonApplication.class, args);
  }

  @Autowired
  private ConfigurableBeanFactory beanFactory;

  @Bean("securityFilterChain")
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    var chain = http
        .authorizeHttpRequests(customizer -> customizer
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
            .requestMatchers("/api/login", "/api/csrf", "/error").permitAll()
            .requestMatchers("/api/**").authenticated())
        .exceptionHandling(customizer -> customizer
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .rememberMe(customizer -> customizer.alwaysRemember(true).key("proton"))
        .build();

    var rememberMeServices = http.getSharedObject(RememberMeServices.class);
    beanFactory.registerSingleton("rememberMeServices", rememberMeServices);

    return chain;
  }
}
