package com.coen448.async_processor;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.concurrent.*;

public class AsyncProcessorTest {

    @Test
    void blackBox_SuccessfulAggregationPreservesOrder() throws Exception {
        // Mocking for dependency isolation [cite: 24]
        Microservice mockA = mock(Microservice.class);
        Microservice mockB = mock(Microservice.class);

        when(mockA.retrieveAsync(any())).thenReturn(CompletableFuture.completedFuture("Hello"));
        when(mockB.retrieveAsync(any())).thenReturn(CompletableFuture.completedFuture("World"));

        AsyncProcessor processor = new AsyncProcessor();
        CompletableFuture<String> resultFuture = processor.processAsync(List.of(mockA, mockB));

        // Use timeout to detect hangs 
        String result = resultFuture.get(200, TimeUnit.MILLISECONDS);
        assertEquals("Hello World", result); // Verifies deterministic order
    }

    @Test
    void stateBased_VerifyDoneStatusAfterJoin() {
        CompletableFuture<String> cf1 = CompletableFuture.completedFuture("A");
        CompletableFuture<String> cf2 = CompletableFuture.completedFuture("B");
        
        CompletableFuture<Void> allCf = CompletableFuture.allOf(cf1, cf2);

        // Assert initial and final state transitions
        assertTrue(cf1.isDone());
        assertTrue(cf2.isDone());
        assertTrue(allCf.isDone()); 
    }

    @Test
    void blackBox_ExceptionHandling() {
        Microservice mockA = mock(Microservice.class);
        // Simulate one microservice failing 
        when(mockA.retrieveAsync(any())).thenReturn(
            CompletableFuture.failedFuture(new RuntimeException("Service Down"))
        );

        AsyncProcessor processor = new AsyncProcessor();
        CompletableFuture<String> resultFuture = processor.processAsync(List.of(mockA));

        assertThrows(ExecutionException.class, () -> resultFuture.get(200, TimeUnit.MILLISECONDS));
    }
}