package ru.vlsu.psytest.api;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        List<SecurityScheme> schemeList = new ArrayList<>();
        schemeList.add(new ApiKey(HttpHeaders.AUTHORIZATION, "JWT", "header"));
        return new Docket(DocumentationType.SWAGGER_2)
                .produces(Collections.singleton("application/json"))
                .consumes(Collections.singleton("application/json"))
                .ignoredParameterTypes(Authentication.class)
                .securitySchemes(schemeList)
                .useDefaultResponseMessages(false)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "REST API для приложения по определению типологии по Юнгу",
                "API предназначено для работы с вопросами теста, обработке результатов, регистрации и авторизации пользователей",
                "API 1.0.0",
                "Terms of service",
                new Contact("", "", ""),
                "License of API", "API license URL", Collections.emptyList());
    }
}
