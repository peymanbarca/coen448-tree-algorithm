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
            # will add descent handling in next step
            pass

        # if node has become 3-key (temporary 4-node), split and return promotion
        if len(node.keys) == 3:
            return self._split_node(node)
        return None

    def _split_node(self, node):
        # node.keys length == 3
        mid_key = node.keys[1]
        mid_val = node.vals[1]
        left = Node(keys=[node.keys[0]], vals=[node.vals[0]], children=[])
        right = Node(keys=[node.keys[2]], vals=[node.vals[2]], children=[])
        return (mid_key, mid_val, left, right)


def test_leaf_split_promotes_to_root():
    t = TwoThreeTree()
    t.put(50, "v50")
    t.put(20, "v20")
    t.put(70, "v70")
    for k, v in [(50, "v50"), (20, "v20"), (70, "v70")]:
        assert t.get(k) == v