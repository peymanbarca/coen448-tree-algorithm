import pytest
from lru_cache import LRUCache

@pytest.fixture
def cache():
    """Context: Provides a fresh cache instance for each test."""
    return LRUCache(capacity=2)

def test_basic_put_and_get(cache):
    """Outcome: Verify basic storage and retrieval."""
    cache.put(1, 1)
    cache.put(2, 2)
    assert cache.get(1) == 1
    assert cache.get(2) == 2

def test_eviction_logic(cache):
    """Outcome: Verify oldest item is evicted when capacity is exceeded."""
    cache.put(1, 1)
    cache.put(2, 2)
    cache.put(3, 3)  # This should evict key 1
    
    assert cache.get(1) == -1
    assert cache.get(2) == 2
    assert cache.get(3) == 3

def test_lru_update_priority(cache):
    """Outcome: Verify that accessing a key moves it to 'Most Recent'."""
    cache.put(1, 1)
    cache.put(2, 2)
    
    # Access key 1, making key 2 the 'oldest'
    cache.get(1)
    
    cache.put(3, 3)  # This should now evict key 2, not key 1
    
    assert cache.get(2) == -1
    assert cache.get(1) == 1
    assert cache.get(3) == 3

def test_overwrite_existing_key(cache):
    """Outcome: Verify updating an existing key updates value and priority."""
    cache.put(1, 1)
    cache.put(1, 10)
    assert cache.get(1) == 10
    assert len(cache) == 1

def test_capacity_of_one():
    """Outcome: Edge case - capacity of 1 should always evict on new put."""
    small_cache = LRUCache(capacity=1)
    small_cache.put(1, 1)
    small_cache.put(2, 2)
    assert small_cache.get(1) == -1
    assert small_cache.get(2) == 2