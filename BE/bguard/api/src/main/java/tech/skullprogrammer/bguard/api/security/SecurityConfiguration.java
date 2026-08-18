package tech.skullprogrammer.bguard.api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tech.skullprogrammer.bguard.api.filter.JWTAuthFilter;
import tech.skullprogrammer.bguard.api.service.JWTService;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JWTService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JWTAuthFilter jwtAuthFilter = new JWTAuthFilter(jwtService);
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
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration conf) throws Exception {
        return conf.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
