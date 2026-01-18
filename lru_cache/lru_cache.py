from collections import OrderedDict
import logging

# Set up basic logging to match the "robust" vibe requirement
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("LRUCache")

class LRUCache:
    def __init__(self, capacity: int):
        self.capacity = capacity
        self.cache = OrderedDict()
        logger.info(f"Initialized LRU Cache with capacity: {capacity}")

    def get(self, key: int) -> int:
        if key not in self.cache:
            return -1
        
        # Move to end to mark as most recently used
        self.cache.move_to_end(key)
        return self.cache[key]

    def put(self, key: int, value: int) -> None:
        if key in self.cache:
            # Update existing and move to end
            self.cache.move_to_end(key)
        
        self.cache[key] = value
        
        if len(self.cache) > self.capacity:
            # Pop the first item (Least Recently Used)
            oldest_key, _ = self.cache.popitem(last=False)
            logger.info(f"Evicted key: {oldest_key} (Capacity reached)")

    def __len__(self):
        return len(self.cache)