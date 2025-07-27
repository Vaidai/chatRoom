package com.example.javaTask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        var admin = User.withUsername("admin")
                .password(passwordEncoder.encode("adminpass"))
                .roles("ADMIN")
                .build();
        var user = User.withUsername("user")
                .password(passwordEncoder.encode("userpass"))
                .roles("ACCOUNT")
                .build();
        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))  // Allow frames
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/messages/**").hasAnyRole("ACCOUNT", "ADMIN")
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()  // <--- Allow H2 console
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {
                })
                .logout(logout -> logout
                        .logoutUrl("/logout") // Define the logout URL
                        .invalidateHttpSession(true) // Invalidate session on logout
                        .clearAuthentication(true) // Clear authentication info
                        .deleteCookies("JSESSIONID") // Delete session cookies
                        .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
                        .permitAll() // Allow logout to everyone
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")  // Specify custom login page if needed
                        .permitAll()
                );
        return http.build();
    }

}
