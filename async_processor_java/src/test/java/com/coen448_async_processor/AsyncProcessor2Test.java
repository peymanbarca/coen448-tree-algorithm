package com.coen448.async_processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AsyncProcessor - Exception Handling Strategies")
public class AsyncProcessor2Test {

    private AsyncProcessor2 processor;

    @BeforeEach
    void setUp() {
        processor = new AsyncProcessor2();
    }

    // ============================================================
    // Task A: Fail-Fast Tests
    // ============================================================

    @Test
    @DisplayName("Task A.1: FailFast - All services succeed, result aggregated")
    void testFailFastAllSuccess() throws ExecutionException, InterruptedException {
        List<Microservice2> services = List.of(
                mockService("result-1"),
                mockService("result-2"),
                mockService("result-3")
        );
        List<String> messages = List.of("msg-1", "msg-2", "msg-3");

        CompletableFuture<String> future = processor.processAsyncFailFast(services, messages);
        String result = future.get();

        assertNotNull(result);
        assertTrue(result.contains("result-1"));
        assertTrue(result.contains("result-2"));
        assertTrue(result.contains("result-3"));
        System.out.println("[TEST] FailFast result: " + result);
    }

    @Test
    @DisplayName("Task A.2: FailFast - First service fails, exception propagates")
    void testFailFastFirstServiceFails() {
        List<Microservice2> services = List.of(
                mockServiceWithException("Service-1 error"),
                mockService("result-2"),
                mockService("result-3")
        );
        List<String> messages = List.of("msg-1", "msg-2", "msg-3");

        CompletableFuture<String> future = processor.processAsyncFailFast(services, messages);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertTrue(exception.getCause().getMessage().contains("Service-1 error"));
        System.out.println("[TEST] FailFast exception caught: " + exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Task A.3: FailFast - Middle service fails, no partial results")
    void testFailFastMiddleServiceFails() {
        List<Microservice2> services = List.of(
                mockService("result-1"),
                mockServiceWithException("Service-2 error"),
                mockService("result-3")
        );
        List<String> messages = List.of("msg-1", "msg-2", "msg-3");

        CompletableFuture<String> future = processor.processAsyncFailFast(services, messages);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertTrue(exception.getCause() instanceof RuntimeException);
    }

    @Test
    @DisplayName("Task A.4: FailFast - Size mismatch throws IllegalArgumentException")
    void testFailFastSizeMismatch() {
        List<Microservice2> services = List.of(
                mockService("result-1"),
                mockService("result-2")
        );
        List<String> messages = List.of("msg-1"); // mismatch

        CompletableFuture<String> future = processor.processAsyncFailFast(services, messages);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    // ============================================================
    // Task B: Fail-Partial Tests
    // ============================================================

    @Test
    @DisplayName("Task B.1: FailPartial - All services succeed, all results returned")
    void testFailPartialAllSuccess() throws ExecutionException, InterruptedException {
        List<Microservice2> services = List.of(
                mockService("result-1"),
                mockService("result-2"),
                mockService("result-3")
        );
        List<String> messages = List.of("msg-1", "msg-2", "msg-3");

        CompletableFuture<List<String>> future = processor.processAsyncFailPartial(services, messages);
        List<String> results = future.get();

        assertEquals(3, results.size());
        assertTrue(results.contains("result-1"));
        assertTrue(results.contains("result-2"));
        assertTrue(results.contains("result-3"));
        System.out.println("[TEST] FailPartial all success: " + results);
    }

    @Test
    @DisplayName("Task B.2: FailPartial - First service fails, partial results returned")
    void testFailPartialFirstServiceFails() throws ExecutionException, InterruptedException {
        List<Microservice2> services = List.of(
                mockServiceWithException("Service-1 error"),
                mockService("result-2"),
                mockService("result-3")
        );
        List<String> messages = List.of("msg-1", "msg-2", "msg-3");

        CompletableFuture<List<String>> future = processor.processAsyncFailPartial(services, messages);
        List<String> results = future.get();

        // Should return only successful results, no exception
        assertEquals(2, results.size());
        assertTrue(results.contains("result-2"));
        assertTrue(results.contains("result-3"));
        assertFalse(results.contains(null));
        System.out.println("[TEST] FailPartial partial results: " + results);
    }

    @Test
    @DisplayName("Task B.3: FailPartial - All services fail, empty list returned")
    void testFailPartialAllServicesFail() throws ExecutionException, InterruptedException {
        List<Microservice2> services = List.of(
                mockServiceWithException("Service-1 error"),
                mockServiceWithException("Service-2 error"),
                mockServiceWithException("Service-3 error")
        );
        List<String> messages = List.of("msg-1", "msg-2", "msg-3");

        CompletableFuture<List<String>> future = processor.processAsyncFailPartial(services, messages);
        List<String> results = future.get();

        // Should return empty list, no exception
        assertEquals(0, results.size());
        assertTrue(results.isEmpty());
        System.out.println("[TEST] FailPartial all failed, empty result: " + results);
    }

    @Test
    @DisplayName("Task B.4: FailPartial - No exception escapes to caller")
    void testFailPartialNoExceptionEscape() {
        List<Microservice2> services = List.of(
                mockServiceWithException("Service-1 error"),
                mockService("result-2")
        );
        List<String> messages = List.of("msg-1", "msg-2");

        CompletableFuture<List<String>> future = processor.processAsyncFailPartial(services, messages);

        // Should NOT throw; future should complete normally with partial results
        assertDoesNotThrow(() -> {
            List<String> results = future.get();
            assertEquals(1, results.size());
            assertEquals("result-2", results.get(0));
        });
    }

    // ============================================================
    // Task C: Fail-Soft Tests
    // ============================================================

    @Test
    @DisplayName("Task C.1: FailSoft - All services succeed, result aggregated")
    void testFailSoftAllSuccess() throws ExecutionException, InterruptedException {
        List<Microservice2> services = List.of(
                mockService("result-1"),
                mockService("result-2"),
                mockService("result-3")
        );
        List<String> messages = List.of("msg-1", "msg-2", "msg-3");
        String fallback = "FALLBACK";

        CompletableFuture<String> future = processor.processAsyncFailSoft(services, messages, fallback);
        String result = future.get();

        assertNotEquals(fallback, result);
        assertTrue(result.contains("result-1"));
        assertTrue(result.contains("result-2"));
        assertTrue(result.contains("result-3"));
        System.out.println("[TEST] FailSoft success result: " + result);
    }

    @Test
    @DisplayName("Task C.2: FailSoft - First service fails, fallback returned")
    void testFailSoftFirstServiceFails() throws ExecutionException, InterruptedException {
        List<Microservice2> services = List.of(
                mockServiceWithException("Service-1 error"),
                mockService("result-2"),
                mockService("result-3")
        );
        List<String> messages = List.of("msg-1", "msg-2", "msg-3");
        String fallback = "DEFAULT_FALLBACK";

        CompletableFuture<String> future = processor.processAsyncFailSoft(services, messages, fallback);
        String result = future.get();

        // Should return fallback, NOT the successful results
        assertEquals(fallback, result);
        System.out.println("[TEST] FailSoft returned fallback: " + result);
    }

    @Test
    @DisplayName("Task C.3: FailSoft - Middle service fails, fallback returned")
    void testFailSoftMiddleServiceFails() throws ExecutionException, InterruptedException {
        List<Microservice2> services = List.of(
                mockService("result-1"),
                mockServiceWithException("Service-2 error"),
                mockService("result-3")
        );
        List<String> messages = List.of("msg-1", "msg-2", "msg-3");
        String fallback = "SERVICE_UNAVAILABLE";

        CompletableFuture<String> future = processor.processAsyncFailSoft(services, messages, fallback);
        String result = future.get();

        assertEquals(fallback, result);
    }

    @Test
    @DisplayName("Task C.4: FailSoft - No exception escapes, caller always gets result")
    void testFailSoftNoExceptionEscape() {
        List<Microservice2> services = List.of(
                mockServiceWithException("Service-1 error"),
                mockServiceWithException("Service-2 error")
        );
        List<String> messages = List.of("msg-1", "msg-2");
        String fallback = "ALL_FAILED";

        CompletableFuture<String> future = processor.processAsyncFailSoft(services, messages, fallback);

        // Should NOT throw; caller always gets a result
        assertDoesNotThrow(() -> {
            String result = future.get();
            assertEquals(fallback, result);
        });
    }

    @Test
    @DisplayName("Task C.5: FailSoft - Custom fallback values")
    void testFailSoftCustomFallback() throws ExecutionException, InterruptedException {
        List<Microservice2> services = List.of(
                mockServiceWithException("Error")
        );
        List<String> messages = List.of("msg-1");

        String customFallback = "CUSTOM_ERROR_HANDLING";
        CompletableFuture<String> future = processor.processAsyncFailSoft(services, messages, customFallback);
        String result = future.get();

        assertEquals(customFallback, result);
    }

    // ============================================================
    // Helper methods to create mock services
    // ============================================================

    /**
     * Create a mock service that succeeds with given result.
     */
    private Microservice2 mockService(String result) {
        return query -> CompletableFuture.completedFuture(result);
    }

    /**
     * Create a mock service that fails with given exception message.
     */
    private Microservice2 mockServiceWithException(String errorMessage) {
        return query -> CompletableFuture.failedFuture(
                new RuntimeException(errorMessage)
        );
    }
}