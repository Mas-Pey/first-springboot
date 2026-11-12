package com.example.api_gateway.config;

import com.example.api_gateway.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(@NotNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = header.substring(7);
        String username;
        try {
            username = jwtUtil.getUsername(token);
        } catch (JwtException | IllegalArgumentException exception) {
            log.info("JWT rejected: {}", exception.getMessage());
            return chain.filter(exchange);
        }

        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                .header("X-User-Id", username)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

        return chain.filter(exchange.mutate().request(serverHttpRequest).build())
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }
}
