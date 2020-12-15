package com.contemporaryparking.tollparkinglibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket produceApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.contemporaryparking.tollparkinglibrary.controller"))
                .build();
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Toll Parking API",
                "Swagger documentation for Toll Parking REST API",
                "1.0",
                "",
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                "");
    }
}
