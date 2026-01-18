### Balanced Search Trees are type of binary search tree where costs are guaranteed to be logarithmic.

You can learn the main structure and algorithms of it here:
https://algs4.cs.princeton.edu/33balanced/


## What is a 2-3 Search Tree?

A 2-3 search tree is a balanced search tree designed to overcome the performance issues of standard Binary Search Trees (BSTs) by ensuring logarithmic height regardless of the order of insertion.

It consists of two types of nodes:

- 2-node: Contains one key and two links (left and right). Keys in the left subtree are smaller than the node's key; keys in the right subtree are larger.

- 3-node: Contains two keys and three links (left, middle, and right).

    - Left link: Keys smaller than the first key.

    - Middle link: Keys between the first and second keys.

    - Right link: Keys larger than the second key.

- Perfect Balance: A key property of a 2-3 tree is that it is perfectly balanced, meaning every path from the root to a null link (leaf) has the exact same length.

## Algorithms
  
  - ### 1. Search: Searching in a 2-3 tree is a generalization of searching in a BST:

    - Compare the search key against the key(s) in the current node.

    - If it matches any key, the search is a hit.

    - If it doesn't match, follow the link corresponding to the interval containing the search key:

        - In a 2-node: Left if smaller, right if larger.

        - In a 3-node: Left if smaller than the first key, middle if between the keys, right if larger than the second key.

    - Repeat recursively until a hit is found or a null link is reached (a miss).


  - ### 2. Insertion (Maintaining Balance)
      Unlike standard BSTs which grow downward, 2-3 trees grow upward. Insertion always starts by searching for the key and reaching a node at the bottom.

      - Insert into a 2-node: If the search ends at a 2-node, simply convert it into a 3-node by adding the new key. No change in tree height occurs.

      - Insert into a 3-node: If the search ends at a 3-node, there is no room for a third key. The algorithm handles this by:

          - Creating a temporary 4-node: Temporarily hold three keys in the node.

          - Splitting the 4-node: The middle key is moved up to the parent node, and the remaining two keys become two separate 2-nodes (children of the moved middle key).

  - Propagating the Split:

      - If the parent was a 2-node, it becomes a 3-node, and the process stops.

      - If the parent was also a 3-node, it becomes a temporary 4-node, and the split process continues up the tree toward the root.

  - Splitting the Root: If the split reaches the root and the root is a 3-node, it splits into three 2-nodes. This is the only way the tree increases in height, ensuring balance is maintained globally.

-------------------------------------------------------------------------

In this tutorial we use vibe coding to implement and test a 2-3 search tree code including its main algorithms:
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