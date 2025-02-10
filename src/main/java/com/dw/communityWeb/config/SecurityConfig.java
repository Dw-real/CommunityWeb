package com.dw.communityWeb.config;

import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(String.valueOf(PathRequest.toStaticResources().atCommonLocations()));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
        );
        http.formLogin(login -> login
                .loginPage("/login")
                .permitAll()
        );
        http.logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
        );

        //http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}