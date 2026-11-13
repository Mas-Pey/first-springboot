package com.example.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("analyze-route", r -> r
                        .path("/analyze/**")
                        .uri("lb://analyzer-service"))
                .route("quote-route", r -> r
                        .path("/quote/**")
                        .filters(f -> f.addRequestHeader("X-Source", "gateway"))
                        .uri("lb://quote-service"))
                .build();
    }
}
