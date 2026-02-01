package com.coen448.async_processor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AsyncProcessor {
    public CompletableFuture<String> processAsync(List<Microservice> microservices) {
        // Asynchronous execution: launch concurrent calls [cite: 47]
        List<CompletableFuture<String>> futures = microservices.stream()
                .map(m -> m.retrieveAsync("data"))
                .toList();

        // Synchronization (fan-in): use allOf as a barrier to wait for all to complete 
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        // Deterministic aggregation in input-list order
                        .map(CompletableFuture::join)
                        .collect(Collectors.joining(" ")));
    }
}

interface Microservice {
    CompletableFuture<String> retrieveAsync(String query);
}