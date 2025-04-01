package com.DevProj.Vakantes.service.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    private static final String[] PUBLIC_MATCHERS = {
            "/auth/login",
            "/auth/logar",
            "/auth/logout",
            "/images/**",
            "/css/**",
            "/js/**",
            "/helloworld"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).excludePathPatterns(
                PUBLIC_MATCHERS
        );
    }
}
