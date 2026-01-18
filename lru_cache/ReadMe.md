This tutorial combines the intuitive flow of Vibe Coding with the structured rigor of the COSTAR prompt engineering technique for generating test cases. We will use an LRU (Least Recently Used) Cache algorithm as our sample case.


## Phase 1: Vibe Coding the Program
Vibe coding is about describing the intent and feel of the solution rather than involving in syntax immediately. It allows the AI to handle the heavy lifting of the initial boilerplate.

### Prompt:

    "I want to build a Python implementation of an LRU Cache. It should feel lightweight and efficient with O(1) time complexity for both get and put operations. It needs to handle a specific capacity, and when it’s full, it should gracefully kick out the oldest item to make room for the new. Make the code clean, readable, and use modern Python type hints."


## Phase 2: Generating Tests with COSTAR

Now that we have our code, we use the COSTAR framework to generate a comprehensive automated test suite. 

### 2.1 Mapping the COSTAR Components to generate the perfect test prompt: 
we first define our parameters:

Context: A Python 3.11+ environment using pytest. The system is a high-concurrency cache layer.

Outcome: 100% branch coverage. We need to verify eviction logic, updates to existing keys, and edge cases.

Steps: 1. Initialize cache. 2. Perform put operations. 3. Verify get results. 4. Trigger eviction and verify the oldest key is gone.

Tools: pytest, pytest-cov, and time module for checking temporal logic.

AudienceBackend developers and CI/CD pipelines ensuring no regressions in performance or logic.

Relevance: High. Cache inconsistency can lead to stale data or memory leaks in production.

## 2.2 The COSTAR based Engineered Prompt to generate test cases
Use the mapping above to feed a structured prompt to your AI assistant.

## Prompt:

    Context: I have a Python LRU Cache implementation (provided below). The environment is a high-performance backend system.

    Outcome: Generate a comprehensive automated test suite using pytest. The goal is 100% branch coverage and validation of the eviction strategy.

    Steps: > 1. Create tests for basic get and put functionality. 2. Test the 'least recently used' logic by accessing an old item to see if it moves to the front. 3. Test edge cases: capacity of 1, empty cache, and overwriting existing keys.

    Tools: Use pytest fixtures for setup. Include comments explaining the logic of each test case.

    Audience: This is for a CI/CD pipeline and peer review by senior engineers.

    Relevance: This is a mission-critical component; failures in eviction logic will cause memory overflows.

    Code to test: > [Insert your Vibe-Coded LRU Cache code here]


## Phase 3: Validating the Test Suite
Once the AI generates the tests, you should run them and check the coverage.

## 3.1 Coverage Check
Run the following in your terminal:

    pytest -v

## 3.2 Iterative Fixing (Closing the Loop)
If any tests fail or coverage is low, use a final "Vibe" prompt to fix the code: 

## Prompt:

    "The tests showed that our cache fails when we update an existing key—it doesn't move it to the 'most recent' position. Can you fix the implementation so the vibe stays efficient but the logic handles updates correctly?"