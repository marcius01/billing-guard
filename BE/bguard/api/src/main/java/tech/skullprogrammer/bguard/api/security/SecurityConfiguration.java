package tech.skullprogrammer.bguard.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/customers", "/api/customers/{id}").hasAnyRole("VIEWER", "ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/customers").hasAnyRole("ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/supply-points", "/api/supply-points/{id}").hasAnyRole("VIEWER", "ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/supply-points").hasAnyRole("ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/invoices", "/api/invoices/{id}").hasAnyRole("VIEWER", "ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/invoices").hasAnyRole("ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/import-jobs/upload").hasAnyRole("ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/import-jobs", "/api/import-jobs/{id}").hasAnyRole("VIEWER", "ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/anomalies", "/api/anomalies/{id}").hasAnyRole("VIEWER", "ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/anomalies/{id}/explanation").hasAnyRole("VIEWER", "ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/anomalies/{id}/resolve").hasAnyRole("ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/anomalies/{id}/ignore").hasAnyRole("ANALYST", "ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
