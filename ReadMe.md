### Balanced Search Trees are type of binary search tree where costs are guaranteed to be logarithmic.

You can learn the main structure and algorithms of it here:
https://algs4.cs.princeton.edu/33balanced/

More details are available in 2_3_search_tree.md

-------------------------------------------------------------------------

# Vibe-coding Dev → QA Test & Review Workflow 

### Goal
- Develop the 2-3 tree implementation gradually in small, testable iterations.
- Every two development steps, push code to `dev` branch and create a pull request.
- QA role pulls `dev`, writes/tests additional pytest cases using vibe-coding (Copilot + Codex), runs tests, fixes code if needed, and merges the validated changes into the `main` branch.

### Branching strategy
- main (protected) — only merged release-quality code and direct code push will not be accepted by developers.
- dev — integration branch for every two development steps pushed by developer.

### Roles & responsibilities
- Developer
  - Implement Steps N and N+1 locally (follow step-by-step plan).
  - Run local unit tests for those steps.
  - Push changes to `dev` every two steps, and create a pull request (to be merged with main branch by QA role).
- QA
  - Pull `dev`, create a `qa/stepN-N+1` branch.
  - Use vibe-coding (Copilot + Codex) to generate/extend tests and to propose fixes for the main code.
  - Run tests, fix failing behavior (in code or tests) and iterate.
  - Once all test passed and finalized code review, merge to `main` with optional tag.


### Quick checklist (every two steps)
- [ ] Developer implements steps N & N+1 locally in `dev` branch
- [ ] Developer runs step-level tests locally
- [ ] Developer pushes changes to `dev`
- [ ] QA pulls `dev`
- [ ] QA writes tests using vibe-coding, runs them
- [ ] QA fixes code or tests until all targeted tests pass
- [ ] QA merges fixes back into `dev` and merge it with `main` branch


### Developer workflow (commands)
1. Implement two steps locally (in dev branch) and run local tests:
   - git checkout -b dev               # or update existing dev
   - git pull
   - Edit code in VS Code.
   - Run targeted tests:
     - cd gradual_development
     - pytest -v 2_3_tree_stepN.py  # run the step tests that has beed modified or developed
2. Commit & push to dev:
   - git pull
   - git add .
   - git commit -m "dev: steps N-N+1 - <short description>"
   - git push origin dev

### QA workflow (commands)
1. Pull dev branch:
   - git checkout -b dev
   - git pull

2. Develop tests with vibe-coding:
   - Use the COSTAR-styled prompts (see "Vibe prompts" below) for both code and tests.
   - Generate or edit tests under gradual_development/ (e.g., 2_3_tree_stepN+1.py).
3. Run tests:
   - cd gradual_development
   - pytest -v 2_3_tree_stepN.py 
   - pytest -v 2_3_tree_stepN+1.py
4. Fix issues:
   - If tests reveal bugs, implement small fixes locally, continue until all issues fixed and all tests passed
5. if approved, QA can update `dev` directly (team policy dependent).
   - Commit fixes on `dev`:
     - git add .
     - git commit -m "qa: fix step N-N+1 - <short>"
     - git push origin dev

6. Merge with main branch after test and review.
   - git checkout -b dev
   - git pull
   - git merge dev
   - git push origin main


### Vibe-coding prompts (usage pattern)
- Use COSTAR-style prompts for test generation (concise, structured).
- Example short template for test generation:
  - Context: repo path, target step, constraints (fast, deterministic).
  - Outcome: single pytest file name and explicit asserts.
  - Steps: import module (importlib), create tree, perform inserts/gets, assert.
- Use the exact prompts from the Step-by-step COSTAR prompts in this repo.

### Test-writing guidelines
- Keep each step test minimal and fast (single assert block for that step).
- Use importlib to load the latest code under test to avoid stale imports:
  - Example: use importlib.util.spec_from_file_location to load ../2_3_tree.py
- Name tests exactly as `test_*` and files `2_3_tree_stepN.py`.



### Notes and best practices
- Keep commits small and focused per two-step change set.
- Track failing tests and root cause; prefer small fixes over large rewrites.




--------------------------------------------------------------------------

# Gradual Development of 2-3 search tree

In this tutorial we use vibe coding to implement and test a 2-3 search tree code gradually including its main algorithms:
    - search a key
    - insert a key/value
  
The code should be developed gradually with vibe coding (using at least 2 AI vendors such as OpenAI Codex and Github Copilot), and in parallel the test cases should be developed gradually to verify the code so far. 


- source code: https://github.com/peymanbarca/coen448-tree-algorithm
- all steps main tree code and its test cases are in gradual_development folder.


## Step 0:

  - 1. Environment & Integration Setup
  To implement this with GitHub Copilot (or Codex), VS Code, and GitHub, follow this structured workflow.
    
    - https://developers.openai.com/codex/ide/
    - https://code.visualstudio.com/docs/copilot/setup

  - GitHub Integration: Initialize a local repository in VS Code, create a .gitignore for Java/Python, and link it to a new GitHub repository using the Source Control tab.

  - Copilot/Codex extensions should be installed in VS Code and activated (by login)


## Vibe coding:
In each step of development and testing, you should use github copilot and/or codex for vibe coding, by using the same prompt first for completion of development of the tree code (by modifying gradual_development/2_3_tree_stepN.py) to fulfill goals on that step, and generate the test case to verify the tree code so far.

## Step 1 — Skeleton & empty-get

- Goal: tree supports empty state and get() method returns None on empty tree,
and confirm the tree and get() method handle empty state.
- ### Prompt:
        You are a Python code assistant. Task: make the minimal edits to 2_3_tree.py so TwoThreeTree.get(key) returns None on an empty tree and tree supports empty state. Keep Node class unchanged. 

- ### Test Generation Prompt (Engineered by CoSTAR Technique):

        Context:
        - Current code: ...
        - Constraint: Tests must be minimal and fast.

        Outcome:
        - Produce a pytest file gradual_development/2_3_tree_step1.py that verifies TwoThreeTree.get returns None on an empty tree.

        Steps:
        1. Import TwoThreeTree from the module in the repo root (use importlib to load ../2_3_tree.py or relative import).
        2. Create a TwoThreeTree() instance.
        3. Assert t.get(1) is None.

        Tools:
        - pytest, Python 3.
        - No mocks required.

        Audience:
        - Developer implementing step 1 and CI pipeline.

        Relevance:
        - Confirms empty-state handling; low risk but foundational for subsequent steps.

        Additional constraints:
        - Single test function named test_get_on_empty_tree.
        - Keep file self-contained and runnable with `pytest gradual_development/2_3_tree_step1.py`.

- Generate first test case using above prompt and evaluate it (creates a tree and queries empty key):
    
        cd gradual_development
        pytest -v 2_3_tree_step1.py



## Step 2 — Single insert & get

- Goal: implement root creation in TwoThreeTree.put and verify retrieval.

- ### Prompt:
        You are a Python code assistant. Task: minimally extend 2_3_tree.py to implement TwoThreeTree.put(key,val) so when root is None it creates a root Node and subsequent get(key) returns the inserted value. Do not add more features. 

- ### Test Generation Prompt (Engineered by CoSTAR Technique):

        Context:
        - After step1, TwoThreeTree.put is implemented to create a root when empty.
        - Current code: ...
        - Constraint: Tests must be minimal and fast.

        Outcome:
        - Generate pytest file gradual_development/2_3_tree_step2.py that inserts key 42 with value "answer" and verifies retrieval and that a missing key returns None.

        Steps:
        1. Load TwoThreeTree from repo root.
        2. Create tree, call tree.put(42, "answer").
        3. Assert tree.get(42) == "answer".
        4. Assert tree.get(0) is None.

        Tools:
        - pytest, importlib for dynamic loading.

        Audience:
        - Developer validating root creation behavior.

        Relevance:
        - Verifies basic insert+lookup; failing test indicates bug in put root creation.

        Additional constraints:
        - Single test function test_put_and_get_single_key; keep fast.

- Generate 2nd test case using above prompt and evaluate it:
 
        pytest -v 2_3_tree_step2.py

## Step 3 — Leaf 2-node -> 3-node (ordering)

- Goal: inserting a second key into a leaf preserves key order and both values are reachable.

- ### Prompt:
        You are a Python code assistant (vibe coding). Task: add minimal insertion logic so inserting into a leaf that already has one key becomes a 2-key leaf (no splitting). Implement _insert or _insert_into_node as needed; ensure keys stay ordered and duplicate keys update the value. 

- ### Test Generation Prompt (Engineered by CoSTAR Technique):

        Context:
        - put should now insert into leaf nodes; leaf with one key becomes two-key leaf, keys ordered.
        - Current code: ...


        Outcome:
        - Produce pytest file gradual_development/2_3_tree_step3.py that inserts 10:"a" then 5:"b" and asserts both are retrievable.

        Steps:
        1. Load TwoThreeTree.
        2. Insert 10:"a", then 5:"b".
        3. Assert tree.get(10) == "a" and tree.get(5) == "b".

        Tools:
        - pytest.

        Audience:
        - Developer verifying ordered insertion into leaf.

        Relevance:
        - Ensures correct ordering and basic duplicates handling; critical for tree invariants.

        Additional constraints:
        - Single test test_leaf_becomes_3node; keep no internal structure assertions (only public API).

- Generate 3rd test case using above prompt and evaluate it:
        
        pytest -v 2_3_tree_step3.py

## Step 4 — Leaf split & root promotion

- Goal: when a leaf becomes a temporary 4-node, split it and promote middle key to become root.

- ### Prompt:
        You are a Python code assistant (vibe coding). Task: implement splitting for a leaf that becomes a temporary 3-key node: implement a _split_node helper that returns a promotion tuple (mid_key, mid_val, left_node, right_node), and modify put to handle a promotion returned from _insert by creating a new root. Keep changes minimal and ensure existing tests still pass. 

- ### Test Generation Prompt (Engineered by CoSTAR Technique):

        Context:
        - Leaf split behavior: inserting third key into a leaf should split and promote middle key to root.

        Outcome:
        - Generate pytest file gradual_development/2_3_tree_step4.py that inserts 50:"v50", 20:"v20", 70:"v70" and verifies all three values via get().

        Steps:
        1. Load TwoThreeTree.
        2. Insert sequence [50,20,70] with corresponding values.
        3. Assert tree.get(k) == expected for each inserted key.

        Tools:
        - pytest.

        Audience:
        - Developer verifying leaf-split and root promotion logic.

        Relevance:
        - Confirms first split and tree height increase; medium risk for incorrect split index.

        Additional constraints:
        - Single test test_leaf_split_promotes_to_root; keep deterministic and fast.

- Generate 4th test case using above prompt and evaluate it:
        
        pytest -v 2_3_tree_step4.py

## Step 5 — Internal promotions & multi-level splits

- Goal: ensure promotions propagate upward (internal node insertions) and root splitting increases height. Use the sequence from 2_3_insertion.md as a deterministic example.

- ### Prompt:
        You are a Python code assistant (vibe coding). Task: implement descent into internal nodes in _insert: choose child by key intervals, recurse, and when a child returns a promotion tuple insert the promoted key into the parent and adjust children. Ensure _split_node handles internal children (split 3-key node with 4 children). Keep interface TwoThreeTree.put/get unchanged. 

- ### Test Generation Prompt (Engineered by CoSTAR Technique):

        Context:
        - Internal promotions: when child splits, parent must accept promoted key and may split recursively.

        Outcome:
        - Produce pytest file gradual_development/2_3_tree_step5.py that inserts sequence [100,50,150,25,75,125,175,60,80,30] and verifies each get(k) == f"v{k}".

        Steps:
        1. Load TwoThreeTree.
        2. Insert keys in given order, storing values as f"v{key}".
        3. After inserts, iterate keys and assert tree.get(k) == f"v{k}".

        Tools:
        - pytest.

        Audience:
        - Developer ensuring multi-level splits and promotions work.

        Relevance:
        - Validates propagation of splits up the tree; high-impact if incorrect.

        Additional constraints:
        - Single deterministic test test_sequence_causes_internal_promotions; keep runtime small.

- Generate 5th test using above prompt and evaluate it:
        
        pytest -v 2_3_tree_step5.py

## Step 6 — Update existing key & edge cases

Goal: ensure duplicate-key puts update values instead of inserting duplicates.

- ### Prompt:
        You are a Python code assistant (vibe coding). Task: ensure duplicate-key puts update the value (verify _insert checks for equality and replaces node.vals[i]). Also add or preserve simple invariants: node.keys sorted and internal node children count = len(keys)+1. Do not implement deletion.

- ### Test Generation Prompt (Engineered by CoSTAR Technique):

        Context:
        - Duplicate-key behavior and invariants: put(key, val) must update existing keys; node invariants should hold.

        Outcome:
        - Produce pytest file gradual_development/2_3_tree_step6.py that inserts 7:"old", then 7:"new", asserts update, and traverses nodes to assert keys strictly increasing in each node.

        Steps:
        1. Load TwoThreeTree.
        2. Insert 7:"old", then 7:"new".
        3. Assert tree.get(7) == "new".
        4. Walk the tree (recursive helper) and assert in every node that keys[i] < keys[i+1].

        Tools:
        - pytest.

        Audience:
        - Developer validating correctness of update-on-duplicate and node ordering invariant.

        Relevance:
        - Prevents duplicate-key bugs and broken ordering that break all subsequent operations.

        Additional constraints:
        - Single test test_update_existing_key_and_invariants; include small recursive checker using only public Node attributes.

- Generate 6th test case using above prompt and evaluate it:
        
        pytest -v 2_3_tree_step6.py

## Step 7 — Randomized validation vs dict

- Perform many inserts and compare gets against a Python dict.
- Perform many inserts and search for each one, and check the value 

- ### Test Generation Prompt (Engineered by CoSTAR Technique):

        Context:
        - Final validation by randomized inserts vs Python dict reference.

        Outcome:
        - Generate pytest file gradual_development/2_3_tree_step7.py that inserts ~50 random unique keys from range(0,200), stores values in a dict, then asserts tree.get(k) == dict[k] for each.

        Steps:
        1. Use random.sample to pick keys (seed optional for repeatability).
        2. Insert into TwoThreeTree and ref dict.
        3. For each key, assert equality of values.
        4. Keep sample size modest for CI (e.g., 50 keys).

        Tools:
        - pytest, random.

        Audience:
        - Developer and CI to catch edge-case sequences.

        Relevance:
        - Broad coverage, finds subtle ordering/propagation bugs before merging.

        Additional constraints:
        - Single test test_randomized_against_dict; keep deterministic if seeding random (recommended) to reproduce failures.

- Generate 7th test using above prompt and evaluate it:
    
        pytest -v 2_3_tree_step7.py


## Finalized tree code and all test cases:
    2_3_tree.py
    pytest -v test_2_3_tree.py