package com.example.gateway_service;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.core.io.buffer.DataBuffer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private static final Map<String, String> VALID_KEYS = Map.of(
            "super-secret-admin", "ADMIN",
            "simple-guest-key", "GUEST"
    );

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey("X-API-KEY")) {
                return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
            }

            String apiKey = exchange.getRequest().getHeaders().getFirst("X-API-KEY");

            if (!VALID_KEYS.containsKey(apiKey)) {
                return onError(exchange, "Invalid API Key", HttpStatus.FORBIDDEN);
            }

            String role = VALID_KEYS.get(apiKey);

            var request = exchange.getRequest().mutate()
                    .header("X-USER-ROLE", role)
                    .build();

            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String json = "{\"error\": \"" + err + "\", \"status\": " + httpStatus.value() + "}";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    public static class Config {}
}