package com.example.todo.todo_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.todo.todo_api.security.CustomOidcUserService;
import com.example.todo.todo_api.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomOidcUserService customOidcUserService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, CustomOidcUserService customOidcUserService) {
        this.userDetailsService = userDetailsService;
        this.customOidcUserService = customOidcUserService;
        System.out.println("SecurityConfig: CustomOidcUserService injected: " + (customOidcUserService != null));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/todos/new", "/todos/save").authenticated()
                .requestMatchers("/todos/edit/**", "/todos/update/**", "/todos/delete/**").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .permitAll()
                .failureUrl("/login?error")
                .defaultSuccessUrl("/todos", true)
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOidcUserService))
                .defaultSuccessUrl("/todos", true)
                .failureUrl("/login?error"))
            .logout((logout) -> logout.permitAll());

        return http.build();
    }
}