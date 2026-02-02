## Phase 1: Vibe Coding the Asynchronous Processor
In this phase, we use an intuitive prompt to generate the "System Under Test" (SUT). The goal is to create an asynchronous processor that handles a list of microservice calls and aggregates their results.


### Development Prompt:

"I want to create a Java AsyncProcessor, for non-blocking efficiency. It should take a list of Microservice objects, call each one's retrieveAsync method (which returns a CompletableFuture<String>), and then use a 'fan-in' pattern to wait for all of them. Use CompletableFuture.allOf as a barrier to ensure everything is ready before joining them into a single space-separated string. The order of the output must match the input list order, regardless of which service finishes first. Make it clean and professional."



## Phase 2: Generating Tests with COSTAR

We now use the COSTAR technique to generate tests that cover the two approaches identified in the documentation: Black-box (testing external behavior) and State-based (testing state transitions)

### The COSTAR Prompt:

Context: I have a Java AsyncProcessor that uses CompletableFuture for fan-out/fan-in tasks. The system environment is a Maven-based JUnit 5 project.


Outcome: Generate a suite of unit tests. I need to achieve high reliability through:
1. Black-box tests: Validate successful completion, result ordering, and exception handling.
2. State-based tests: Verify that the CompletableFuture transitions to isDone() correctly after synchronization.


Steps:
- Mock the microservices using Mockito.
- Assert that output is space-joined and preserves input order.
- Use get(timeout) to prevent test hangs.
- Verify that if one service fails, the aggregate future fails (Exceptional Completion).





Tools: JUnit 5, Mockito, and Maven.

Audience: QA and Backend Developers.


Relevance: Asynchronous code is prone to race conditions and timing issues; these tests must be deterministic.



### Phase 3: Maven Procedure and Coverage Report with JaCoCo plugin

Execution Commands

- Clean and Test:


        mvn clean test

- Check Coverage Report:
After the build finishes, open the report at:
target/site/jacoco/index.html
