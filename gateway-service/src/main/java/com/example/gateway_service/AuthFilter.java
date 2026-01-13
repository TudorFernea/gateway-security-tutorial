package com.example.gateway_service;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 1. Check if the header "X-API-KEY" is present
            if (!exchange.getRequest().getHeaders().containsKey("X-API-KEY")) {
                return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
            }

            // 2. Check if the key value is correct (Hardcoded for demo)
            String apiKey = exchange.getRequest().getHeaders().getFirst("X-API-KEY");
            if (!"tutorial-secret-123".equals(apiKey)) {
                return onError(exchange, "Invalid API Key", HttpStatus.FORBIDDEN);
            }

            // 3. If valid, continue to the next step
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // Configuration properties if needed
    }
}