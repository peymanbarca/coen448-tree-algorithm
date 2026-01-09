# ...existing code...
class Node:
    def __init__(self, keys=None, vals=None, children=None):
        self.keys = list(keys) if keys else []
        self.vals = list(vals) if vals else []
        # children is a list of Node; for leaves it's empty list
        self.children = list(children) if children else []

    def is_leaf(self):
        return len(self.children) == 0

    def __repr__(self):
        return f"Node(keys={self.keys})"


class TwoThreeTree:
    def __init__(self):
        self.root = None

    def get(self, key):
        return self._get(self.root, key)

    def _get(self, node, key):
        if node is None:
            return None
        # scan through keys
        for i, k in enumerate(node.keys):
            if key == k:
                return node.vals[i]
            if key < k:
                return self._get(node.children[i] if not node.is_leaf() else None, key)
        # greater than all keys
        return self._get(node.children[-1] if not node.is_leaf() else None, key)

    def put(self, key, val):
        if self.root is None:
            self.root = Node(keys=[key], vals=[val], children=[])
            return

        promoted = self._insert(self.root, key, val)
        if promoted:
            # promoted is tuple (mid_key, mid_val, left_node, right_node)
            mid_key, mid_val, left, right = promoted
            self.root = Node(keys=[mid_key], vals=[mid_val], children=[left, right])

    def _insert(self, node, key, val):
        # If key already in this node, update and return None
        for i, k in enumerate(node.keys):
            if key == k:
                node.vals[i] = val
                return None

        if node.is_leaf():
            # insert into leaf
            self._insert_into_node(node, key, val, None)
        else:
            # find child index to descend
            idx = 0
            while idx < len(node.keys) and key > node.keys[idx]:
                idx += 1
            promoted = self._insert(node.children[idx], key, val)
            if promoted:
                mid_key, mid_val, left_child, right_child = promoted
                # replace child at idx with left_child and insert right_child at idx+1
                # and insert mid_key/mid_val into node at position idx
                self._insert_into_node(node, mid_key, mid_val, right_child, child_replace_index=idx, left_child=left_child)

        # if node has become a 4-node (3 keys), split it
        if len(node.keys) == 3:
            return self._split_node(node)
        return None

    def _insert_into_node(self, node, key, val, new_right_child, child_replace_index=None, left_child=None):
        # Find insert position
        import bisect
        idx = bisect.bisect_left(node.keys, key)
        node.keys.insert(idx, key)
        node.vals.insert(idx, val)
        if node.is_leaf():
            # children remain empty
            return
        # If left_child provided, we need to replace node.children[child_replace_index] with left_child
        if left_child is not None and child_replace_index is not None:
            node.children[child_replace_index] = left_child
        # Insert new_right_child to the right of the key
        if new_right_child is not None:
            node.children.insert(idx + 1, new_right_child)

    def _split_node(self, node):
        # node.keys has length 3, node.children length is either 0 or 4
        mid_key = node.keys[1]
        mid_val = node.vals[1]

        left_keys = [node.keys[0]]
        left_vals = [node.vals[0]]
        right_keys = [node.keys[2]]
        right_vals = [node.vals[2]]

        if node.is_leaf():
            left_children = []
            right_children = []
        else:
            left_children = [node.children[0], node.children[1]]
            right_children = [node.children[2], node.children[3]]

        left_node = Node(keys=left_keys, vals=left_vals, children=left_children)
        right_node = Node(keys=right_keys, vals=right_vals, children=right_children)

        return (mid_key, mid_val, left_node, right_node)