# python
import importlib.util
from pathlib import Path
import pytest
import random

def _load_two_three_tree_module():
    # Load the module from the same directory as this test file
    module_path = Path(__file__).parent / "2_3_tree.py"
    spec = importlib.util.spec_from_file_location("two_three_tree_module", str(module_path))
    module = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(module)
    return module

# step 1
def test_get_on_empty_tree():
    mod = _load_two_three_tree_module()
    TwoThreeTree = mod.TwoThreeTree

    tree = TwoThreeTree()
    assert tree.get(10) is None
    assert tree.get("nope") is None

# step 2
def test_put_and_get_single_key():
    mod = _load_two_three_tree_module()
    TwoThreeTree = mod.TwoThreeTree
    tree = TwoThreeTree()
    tree.put(42, "answer")
    assert tree.get(42) == "answer"
    assert tree.get(0) is None

# step 3
def test_leaf_becomes_3node():
    mod = _load_two_three_tree_module()
    TwoThreeTree = mod.TwoThreeTree
    tree = TwoThreeTree()
    tree.put(10, "a")
    tree.put(5, "b")
    # both keys must be found and in right order internally
    assert tree.get(10) == "a"
    assert tree.get(5) == "b"


# step 4
def test_leaf_split_promotes_to_root():
    mod = _load_two_three_tree_module()
    TwoThreeTree = mod.TwoThreeTree
    tree = TwoThreeTree()
    tree.put(50, "v50")
    tree.put(20, "v20")
    tree.put(70, "v70")  # causes a split if leaf had 3 inserts
    # After split, middle key should be a root key and all values retrievable
    for k, v in [(50, "v50"), (20, "v20"), (70, "v70")]:
        assert tree.get(k) == v

# step 5
def test_sequence_causes_internal_promotions():
    mod = _load_two_three_tree_module()
    TwoThreeTree = mod.TwoThreeTree
    tree = TwoThreeTree()
    seq = [100, 50, 150, 25, 75, 125, 175, 60, 80, 30]
    for k in seq:
        tree.put(k, f"v{k}")
    # verify internal and leaf keys retrievable (matches tutorial)
    for k in seq:
        assert tree.get(k) == f"v{k}"            


# step 6
def test_update_existing_key():
    mod = _load_two_three_tree_module()
    TwoThreeTree = mod.TwoThreeTree
    tree = TwoThreeTree()
    tree.put(7, "old")
    tree.put(7, "new")
    assert tree.get(7) == "new"

# step 7
def test_randomized_against_dict():
    mod = _load_two_three_tree_module()
    TwoThreeTree = mod.TwoThreeTree
    tree = TwoThreeTree()
    ref = {}
    keys = random.sample(range(0, 200), 50)
    for k in keys:
        v = f"val{k}"
        tree.put(k, v)
        ref[k] = v
    for k in keys:
        assert tree.get(k) == ref[k]    

# step 7        
def test_put_multiple_and_get_all_keys():
    mod = _load_two_three_tree_module()
    TwoThreeTree = mod.TwoThreeTree

    tree = TwoThreeTree()
    inserts = [
        (50, "v50"),
        (20, "v20"),
        (70, "v70"),
        (10, "v10"),
        (30, "v30"),
        (60, "v60"),
        (80, "v80"),
        (25, "v25"),
    ]
    for k, v in inserts:
        tree.put(k, v)

    # Verify every inserted key can be retrieved
    for k, v in inserts:
        assert tree.get(k) == v, f"expected key {k} to return {v}"

    # Non-existent key
    assert tree.get(40) is None


# step 7
def test_put_multiple_and_get_all_keys_v2():
    mod = _load_two_three_tree_module()
    TwoThreeTree = mod.TwoThreeTree

    tree = TwoThreeTree()
    # Insert keys in an order likely to force promotions into internal nodes
    keys = [8, 4, 3, 11, 16, 12, 10, 19, 15, 13]
    for k in keys:
        tree.put(k, f"val{k}")

    # Verify every inserted key can be retrieved
    for k in keys:
        assert tree.get(k) == f"val{k}", f"expected key {k} to return {v}"


def test_get_internal_node_key():
    mod = _load_two_three_tree_module()
    TwoThreeTree = mod.TwoThreeTree

    tree = TwoThreeTree()
    # Insert keys in an order likely to force promotions into internal nodes
    keys = [100, 50, 150, 25, 75, 125, 175, 60, 80, 30]
    for k in keys:
        tree.put(k, f"val{k}")

    # Check some keys that are likely to be in internal nodes after splits
    for k in [50, 100, 150, 75]:
        assert tree.get(k) == f"val{k}"

    # Check leaf keys as well
    for k in [25, 30, 60, 80, 125, 175]:
        assert tree.get(k) == f"val{k}"

    # Missing key
    assert tree.get(999) is None# python
