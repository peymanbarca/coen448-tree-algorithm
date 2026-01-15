# ...existing code...
class Node:
    def __init__(self, keys=None, vals=None, children=None):
        self.keys = list(keys) if keys else []
        self.vals = list(vals) if vals else []
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
        for i, k in enumerate(node.keys):
            if key == k:
                return node.vals[i]
            if key < k:
                return self._get(node.children[i] if not node.is_leaf() else None, key)
        return self._get(node.children[-1] if not node.is_leaf() else None, key)

    def put(self, key, val):
        if self.root is None:
            self.root = Node(keys=[key], vals=[val], children=[])
            return
        promoted = self._insert(self.root, key, val)
        if promoted:
            mid_key, mid_val, left, right = promoted
            self.root = Node(keys=[mid_key], vals=[mid_val], children=[left, right])

    def _insert(self, node, key, val):
        for i, k in enumerate(node.keys):
            if key == k:
                node.vals[i] = val
                return None
        if node.is_leaf():
            import bisect
            idx = bisect.bisect_left(node.keys, key)
            node.keys.insert(idx, key)
            node.vals.insert(idx, val)
        else:
            # descend to correct child
            idx = 0
            while idx < len(node.keys) and key > node.keys[idx]:
                idx += 1
            promoted = self._insert(node.children[idx], key, val)
            if promoted:
                mid_key, mid_val, left_child, right_child = promoted
                # replace the child at idx with left_child and insert right_child at idx+1
                node.children[idx] = left_child
                node.children.insert(idx+1, right_child)
                # insert mid_key/mid_val at position idx in node.keys/vals
                node.keys.insert(idx, mid_key)
                node.vals.insert(idx, mid_val)

        if len(node.keys) == 3:
            return self._split_node(node)
        return None


    def _split_node(self, node):
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
        left = Node(keys=left_keys, vals=left_vals, children=left_children)
        right = Node(keys=right_keys, vals=right_vals, children=right_children)
        return (mid_key, mid_val, left, right)


def test_update_existing_key_and_invariants():
    t = TwoThreeTree()
    t.put(7, "old")
    t.put(7, "new")
    assert t.get(7) == "new"
    # simple invariant: in any node keys are strictly increasing
    def check_node(node):
        if node is None:
            return
        assert all(node.keys[i] < node.keys[i+1] for i in range(len(node.keys)-1))
        for c in node.children:
            check_node(c)
    check_node(t.root)