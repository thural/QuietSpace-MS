package com.jellybrains.quietspace.common_service.webclient.client.impl;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.ReactionType;
import com.jellybrains.quietspace.common_service.model.response.ReactionResponse;
import com.jellybrains.quietspace.common_service.webclient.client.ReactionClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ReactionClientImpl implements ReactionClient {

    private final WebClient webClient;
    private final String REACTION_API_URI = "/api/v1/reactions/";

    @Override
    @CircuitBreaker(name = "reaction-service",
            fallbackMethod = "com.jellybrains.quietspace.common_service.service.shared.FallbackService#genericFallback")
    public CompletableFuture<Optional<String>> sayHello() {
        return webClient.get()
                .uri(REACTION_API_URI + "hello")
                .retrieve()
                .bodyToMono(String.class)
                .map(Optional::ofNullable)
                .onErrorReturn(Optional.empty())
                .toFuture();
    }

    @Override
    @CircuitBreaker(name = "reaction-service",
            fallbackMethod = "com.jellybrains.quietspace.common_service.service.shared.FallbackService#genericFallback")
    public CompletableFuture<Optional<ReactionResponse>> getUserReactionByContentId(String contentId, ContentType type) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(REACTION_API_URI + "content")
                        .queryParam("contentId", contentId)
                        .queryParam("contentType", type)
                        .build()
                )
                .retrieve()
                .bodyToMono(ReactionResponse.class)
                .map(Optional::ofNullable)
                .onErrorReturn(Optional.empty())
                .toFuture();
    }

    @Override
    @CircuitBreaker(name = "reaction-service",
            fallbackMethod = "com.jellybrains.quietspace.common_service.service.shared.FallbackService#genericFallback")
    public CompletableFuture<Optional<Integer>> countByContentIdAndReactionType(String contentId, ReactionType reactionType) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(REACTION_API_URI + "count")
                        .queryParam("contentId", contentId)
                        .queryParam("type", reactionType)
                        .build()
                )
                .retrieve()
                .bodyToMono(Integer.class)
                .map(Optional::ofNullable)
                .onErrorReturn(Optional.empty())
                .toFuture();
    }

}
