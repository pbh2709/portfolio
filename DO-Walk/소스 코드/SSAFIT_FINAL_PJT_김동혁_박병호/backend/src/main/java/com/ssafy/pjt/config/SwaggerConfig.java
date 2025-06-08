package com.ssafy.pjt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
     OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SSAFY PJT API 문서")
                .description("Spring Boot + Swagger 연동 프로젝트입니다.")
                .version("v1.0.0")
                .contact(new Contact()
                    .name("SSAFY Team")
                    .email("ssafy@ssafy.com")
                    .url("https://www.ssafy.com")
                )
            );
    }
}