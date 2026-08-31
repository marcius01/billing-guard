package tech.skullprogrammer.bguard.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationFilter extends OncePerRequestFilter {

    private final String correlationKey;

    public CorrelationFilter(@Value("${skullprogrammer.observability.mdc.key}") String correlationKey) {
        this.correlationKey = correlationKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UUID correlationId = UUID.randomUUID();
        MDC.put(correlationKey, correlationId.toString());
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(correlationKey);
            MDC.clear();
        }
    }
}
