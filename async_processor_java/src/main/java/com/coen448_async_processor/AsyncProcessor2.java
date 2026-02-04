package com.coen448.async_processor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * AsyncProcessor: three exception-handling strategies for concurrent microservice calls.
 */
public class AsyncProcessor2 {

    /**
     * Task A: Fail-Fast Strategy
     */
    public CompletableFuture<String> processAsyncFailFast(
            List<Microservice2> microservices,
            List<String> messages) {
        
        if (microservices.size() != messages.size()) {
            return CompletableFuture.failedFuture(
                new IllegalArgumentException("Services and messages size mismatch")
            );
        }

        System.out.println("[FailFast] Starting concurrent calls to " + microservices.size() + " services");

        // Launch all calls concurrently
        List<CompletableFuture<String>> futures = microservices.stream()
                .map(m -> m.retrieveAsync("data"))
                .toList();

        // allOf waits for all; if any fails, the result fails
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    System.out.println("[FailFast] All services completed successfully");
                    return futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.joining(" | "));
                });
    }

    /**
     * Task B: Fail-Partial Strategy
     */
    public CompletableFuture<List<String>> processAsyncFailPartial(
            List<Microservice2> microservices,
            List<String> messages) {
        
        if (microservices.size() != messages.size()) {
            return CompletableFuture.failedFuture(
                new IllegalArgumentException("Services and messages size mismatch")
            );
        }

        System.out.println("[FailPartial] Starting concurrent calls to " + microservices.size() + " services");

        // Launch all calls and wrap each with exception handling
        List<CompletableFuture<String>> futures = microservices.stream()
                .map(m -> m.retrieveAsync("data")
                        .exceptionally(ex -> {
                            System.err.println("[FailPartial] Service failed: " + ex.getMessage());
                            return null; // mark as failed but don't throw
                        })
                )
                .toList();

        // Collect results, filtering out nulls (failed services)
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    System.out.println("[FailPartial] All services attempted");
                    return futures.stream()
                            .map(CompletableFuture::join)
                            .filter(result -> result != null) // skip failed services
                            .collect(Collectors.toList());
                });
    }

    /**
     * Task C: Fail-Soft Strategy
     */
    public CompletableFuture<String> processAsyncFailSoft(
            List<Microservice2> microservices,
            List<String> messages,
            String fallbackValue) {
        
        if (microservices.size() != messages.size()) {
            return CompletableFuture.failedFuture(
                new IllegalArgumentException("Services and messages size mismatch")
            );
        }

        System.out.println("[FailSoft] Starting concurrent calls to " + microservices.size() + " services");

        // Launch all calls concurrently
        List<CompletableFuture<String>> futures = microservices.stream()
                .map(m -> m.retrieveAsync("data"))
                .toList();

        // allOf waits for all; if any fails, catch and return fallback
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    System.out.println("[FailSoft] All services completed successfully");
                    return futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.joining(" | "));
                })
                .exceptionally(ex -> {
                    System.err.println("[FailSoft] Failure detected, returning fallback: " + ex.getMessage());
                    return fallbackValue;
                });
    }
}

/**
 * Microservice interface: represents a remote async service.
 */
interface Microservice2 {
    CompletableFuture<String> retrieveAsync(String query);
}
