package com.koreait.jejuguide.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.koreait.jejuguide.security.CustomUserDetailsService;



@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // ★ 우리가 만든 UserDetailsService를 명시적으로 사용
            .userDetailsService(customUserDetailsService)

            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/join",
                                 "/account/find**", "/account/find-id", "/account/find-pw",
                                 "/css/**", "/images/**", "/js/**",
                                 "/api/members/**", "/ai/**", "/api/**",
                                 "/spots/**","/foods/**").permitAll()

                // ★ PathPattern에선 "/admin/**"가 "/admin"을 포함하지 않음 → 둘 다 지정
                .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")

                .requestMatchers("/account/me", "/account/password", "/account/delete").authenticated()
                .requestMatchers(HttpMethod.GET, "/board", "/board/*").permitAll()
                .requestMatchers("/board/create").authenticated()
                .requestMatchers(HttpMethod.POST, "/board").authenticated()
                .requestMatchers(HttpMethod.POST, "/board/*/comments").authenticated()
                .requestMatchers(HttpMethod.POST, "/board/*/delete").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
            );

        return http.build();
    }
}
    
    
