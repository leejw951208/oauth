package com.example.oauth.config;

import com.example.oauth.oauth.handler.CustomAuthenticationFailureHandler;
import com.example.oauth.oauth.handler.CustomAuthenticationSuccessHandler;
import com.example.oauth.oauth.service.CustomOAuthUserService;
import com.example.oauth.security.*;
import com.example.oauth.security.filter.JwtAuthenticationFilter;
import com.example.oauth.security.filter.JwtExceptionFilter;
import com.example.oauth.security.handler.CustomAccessDeniedHandler;
import com.example.oauth.security.handler.CustomLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuthUserService customOAuthUserService;
    private final CustomAuthenticationSuccessHandler oAuthSuccessHandler;
    private final CustomAuthenticationFailureHandler oAuthFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        httpSecuritySessionManagementConfigurer ->
                                httpSecuritySessionManagementConfigurer
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(
                        httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                                .clearAuthentication(true)
                                .invalidateHttpSession(true)
                                .logoutSuccessHandler(customLogoutSuccessHandler)
                        )
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/login", "/login/**", "/oauth2/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler)
                )
                .oauth2Login(oauth ->
                        oauth.userInfoEndpoint(endpoint -> endpoint.userService(customOAuthUserService))
                                .successHandler(oAuthSuccessHandler)
                                .failureHandler(oAuthFailureHandler)
                );

        return http.build();
    }
}
