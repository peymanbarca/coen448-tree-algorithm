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

In this tutorial we implement a 2-3 search tree code including its main algorithms:
    - search a key
    - insert a key/value
  
The code should be developed gradually, and in parallel in another script, the test cases should be developed gradually to verify the code so far 


- source code: https://github.com/peymanbarca/coen448-tree-algorithm
- all steps main tree code and its test cases are in gradual_development folder.


## Step 0:

  - 1. Environment & Integration Setup
  To implement this with GitHub Copilot (or Codex), VS Code, and GitHub, follow this structured workflow.
    https://developers.openai.com/codex/ide/

  - GitHub Integration: Initialize a local repository in VS Code, create a .gitignore for Java/Python, and link it to a new GitHub repository using the Source Control tab.

  - Copilot/Codex extensions should be installed in VS Code and activated (by login)

## Step 1 — Skeleton & empty-get

- Goal: confirm tree and get() handle empty state.
- Generate first test case (creates a tree and queries empty key):
    
        cd gradual_development
        pytest 2_3_tree_step1.py




## Step 2 — Single insert & get

- Goal: implement root creation in TwoThreeTree.put and verify retrieval.

        pytest 2_3_tree_step2.py

## Step 3 — Leaf 2-node -> 3-node (ordering)

- Goal: inserting a second key into a leaf preserves key order and both values are reachable.

        pytest 2_3_tree_step3.py

## Step 4 — Leaf split & root promotion

- Goal: when a leaf becomes a temporary 4-node, split it and promote middle key to become root.

        pytest 2_3_tree_step4.py

## Step 5 — Internal promotions & multi-level splits

- Goal: ensure promotions propagate upward (internal node insertions) and root splitting increases height. Use the sequence from 2_3_insertion.md as a deterministic example.

        pytest 2_3_tree_step5.py

## Step 6 — Update existing key & edge cases

Goal: ensure duplicate-key puts update values instead of inserting duplicates.

        pytest 2_3_tree_step6.py

## Step 7 — Randomized validation vs dict

- Perform many inserts and compare gets against a Python dict.
- Perform many inserts and search for each one, and check the value 

        pytest 2_3_tree_step7.py


## Finalized tree code and all test cases:
    2_3_tree.py
    pytest test_2_3_tree.py