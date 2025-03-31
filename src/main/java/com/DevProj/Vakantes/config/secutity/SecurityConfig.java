package com.DevProj.Vakantes.config.secutity;

import com.DevProj.Vakantes.service.CookieService;
import com.DevProj.Vakantes.service.interceptor.AuthenticationSucessHandler;
import com.DevProj.Vakantes.service.interceptor.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PasswordService bCryptPasswordEncoder;

    @Autowired
    AuthenticationSucessHandler authenticationSucessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CookieAuthFilter cookieAuthFilter) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(bCryptPasswordEncoder.passwordEncoder());
        this.authenticationManager = authenticationManagerBuilder.build();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/images/**", "/css/**", "/js/**", "/helloworld").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                ).authenticationManager(authenticationManager)
                .addFilterBefore(cookieAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                                .authenticationEntryPoint((request, response, authException) -> {
                                    if (CookieService.getCookie(request, "usuarioId") == null) {
                                        response.sendRedirect("/auth/login");
                                    }

                                })
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
