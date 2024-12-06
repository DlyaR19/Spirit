package com.spirit.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "spirit")
public class SpiritApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(SpiritApplication.class, args);
    }

//    @Bean
//    public WebMvcConfigurer webMvcConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
//                registry.addResourceHandler("/images/**").addResourceLocations("classpath:/META-INF/resources/images/");
//            }
//        };
//    }
}
