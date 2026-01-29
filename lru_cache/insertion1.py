from dataclasses import dataclass, field
from typing import List, Optional

@dataclass
class Node23:
    keys: List[int] = field(default_factory=list)          # 1 or 2 keys
    children: List["Node23"] = field(default_factory=list) # 0, 2, or 3 children

    def is_leaf(self) -> bool:
        return len(self.children) == 0

    def __repr__(self) -> str:
        return f"Node(keys={self.keys}, children={len(self.children)})"


class Tree23:
    def __init__(self):
        self.root: Optional[Node23] = None

    def insert(self, key: int) -> None:
        if self.root is None:
            self.root = Node23(keys=[key])
            return

        promoted = self._insert_recursive(self.root, key)
        if promoted is not None:
            # Root split: create new root with promoted key
            mid_key, left, right = promoted
            self.root = Node23(keys=[mid_key], children=[left, right])

    def _insert_recursive(self, node: Node23, key: int):
        # Returns None if no split; otherwise returns (promoted_key, left_node, right_node)
        if node.is_leaf():
            if key in node.keys:
                return None  # ignore duplicates (or handle however you want)
            node.keys.append(key)
            node.keys.sort()

            if len(node.keys) <= 2:
                return None

            # Split 3-key leaf into two 1-key nodes, promote middle key
            a, b, c = node.keys
            left = Node23(keys=[a])
            right = Node23(keys=[c])
            return (b, left, right)

        # Internal node: descend to correct child
        i = 0
        while i < len(node.keys) and key > node.keys[i]:
            i += 1

        promoted = self._insert_recursive(node.children[i], key)
        if promoted is None:
            return None

        mid_key, left_child, right_child = promoted

        # Insert promoted key and replace the child with the split pair
        node.keys.insert(i, mid_key)
        node.children[i] = left_child
        node.children.insert(i + 1, right_child)

        if len(node.keys) <= 2:
            return None

        # Split internal node with 3 keys (and 4 children)
        a, b, c = node.keys
        # children: 4 of them after insertion
        c0, c1, c2, c3 = node.children

        left = Node23(keys=[a], children=[c0, c1])
        right = Node23(keys=[c], children=[c2, c3])
        return (b, left, right)

    def pretty(self) -> str:
        if self.root is None:
            return "<empty>"

        lines = []
        def dfs(n: Node23, prefix: str = ""):
            lines.append(prefix + str(n.keys))
            for child in n.children:
                dfs(child, prefix + "  ")
        dfs(self.root)
        return "\n".join(lines)


if __name__ == "__main__":
    t = Tree23()
    for x in (100, 50, 150):
        t.insert(x)

    print(t.pretty())
    # Expected:
    # [100]
    #   [50]
    #   [150]
