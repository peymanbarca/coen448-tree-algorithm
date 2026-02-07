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