package com.demo.personalfinancetracker.config;

import jakarta.servlet.SessionCookieConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookieConfig {

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
                sessionCookieConfig.setHttpOnly(true);
                sessionCookieConfig.setSecure(true); // Required for SameSite=None
                sessionCookieConfig.setName("JSESSIONID");
                sessionCookieConfig.setPath("/");
            }
        };
    }
}
