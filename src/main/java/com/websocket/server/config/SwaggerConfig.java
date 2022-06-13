package com.websocket.server.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SwaggerConfig {
	
    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<>(
    		Arrays.asList("application/json"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.websocket.server.controller"))
                .paths(PathSelectors.any())
                .build()
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .apiInfo(apiInfo());
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("api swagger")
                .description("api swagger")
                .version("1.0")
                .build();
    }
}