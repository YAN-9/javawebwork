package com.gzu.chuanxinrecruitment.config;

import com.gzu.chuanxinrecruitment.utils.JWTFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    // 构造函数注入 JWTFilter
    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // 密码加密方式
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 配置 AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 配置 SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // 禁用 CSRF
                .authorizeHttpRequests(authorize -> authorize
                        // 配置允许访问的路径
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/logout",
                                "/api/auth/register",
                                "/js/**",
                                "/images/**",
//                                "/**",
                                "/*.ico",
                                "/*.html",
                                "*/*.html"
                        ).permitAll()
                        // 配置需要认证的路径
//                        .requestMatchers("/**").hasRole("管理员") // 只有管理员角色能访问 /admin/**
//                        .requestMatchers("/user/**").hasRole("普通用户") // 只有普通用户能访问 /user/**
                        // 默认所有其他请求都需要认证
                        .anyRequest().authenticated()
                );

        // 添加 JWT 过滤器
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
