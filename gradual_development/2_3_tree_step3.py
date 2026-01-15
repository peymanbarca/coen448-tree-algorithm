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
        self._insert(self.root, key, val)

    def _insert(self, node, key, val):
        # update if present
        for i, k in enumerate(node.keys):
            if key == k:
                node.vals[i] = val
                return None
        if node.is_leaf():
            # insert in order into keys and vals
            import bisect
            idx = bisect.bisect_left(node.keys, key)
            node.keys.insert(idx, key)
            node.vals.insert(idx, val)
            return None
        # descending code will be added in next steps


def test_leaf_becomes_3node():
    t = TwoThreeTree()
    t.put(10, "a")
    t.put(5, "b")
    assert t.get(10) == "a"
    assert t.get(5) == "b"