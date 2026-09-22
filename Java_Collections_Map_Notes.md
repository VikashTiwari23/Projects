# Java Collections Framework - MAP (Complete Interview Notes)

---

## 1. MAP INTERFACE HIERARCHY (UML)

```
                    <<interface>>
                    ┌──────────────┐
                    │      Map     │
                    │──────────────│
                    │ put(K,V)     │
                    │ get(K)       │
                    │ remove(K)    │
                    │ containsKey()│
                    │ containsVal()│
                    │ keySet()     │
                    │ values()     │
                    │ entrySet()   │
                    │ size()       │
                    │ isEmpty()    │
                    │ merge()      │
                    │ compute()    │
                    │ putIfAbsent()│
                    │ replace()    │
                    └──────┬───────┘
                           │
          ┌────────────────┼─────────────────┐
          │                │                 │
          ▼                ▼                 ▼
   ┌─────────────┐ ┌──────────────┐ ┌──────────────┐
   │  AbstractMap │ │  SortedMap   │ │  NavigableMap │
   │  (abstract)  │ │ (interface)  │ │  (interface)  │
   └──────┬──────┘ └──────┬───────┘ └──────┬───────┘
          │                │                │
          │                ▼                │
          │         ┌──────────────┐        │
          │         │  TreeMap     │◄───────┘
          │         │ (Red-Black)  │
          │         └──────────────┘
          │
          ▼
   ┌─────────────────────────────────────┐
   │           HashMap                   │
   │    (Array + LinkedList + Tree)       │
   │    JDK 1.2  |  NOT thread-safe      │
   └──────────────────┬──────────────────┘
                      │
                      ▼
   ┌─────────────────────────────────────┐
   │         LinkedHashMap                │
   │   (HashMap + Doubly Linked List)     │
   │   Maintains INSERTION/ACCESS order   │
   └─────────────────────────────────────┘


   ┌─────────────────────────────────────┐
   │          Hashtable                   │
   │   (Legacy - Vector family)          │
   │   Thread-safe (synchronized)        │
   └──────────────────┬──────────────────┘
                      │
                      ▼
   ┌─────────────────────────────────────┐
   │         Properties                   │
   │   (key=value String pairs)          │
   │   Load from .properties file        │
   └─────────────────────────────────────┘


   ┌─────────────────────────────────────┐
   │        ConcurrentHashMap            │
   │   (Segment Locking / CAS)           │
   │   Thread-safe | High performance    │
   │   JDK 1.5                           │
   └─────────────────────────────────────┘
```

---

## 2. MAP INTERFACE - ALL METHODS

```java
public interface Map<K, V> {

    // === INSERT / UPDATE ===
    V put(K key, V value);                    // Add or update
    void putAll(Map<? extends K, ? extends V> m);  // Copy all entries

    // === RETRIEVE ===
    V get(Object key);                        // Get value by key
    V getOrDefault(Object key, V defaultValue);

    // === DELETE ===
    V remove(Object key);                     // Remove by key
    boolean remove(Object key, Object value); // Remove only if matches

    // === CHECK ===
    boolean containsKey(Object key);
    boolean containsValue(Object value);
    boolean isEmpty();
    int size();

    // === COLLECTIONS VIEW ===
    Set<K> keySet();
    Collection<V> values();
    Set<Map.Entry<K, V>> entrySet();

    // === DEFAULT METHODS (JDK 8+) ===
    V getOrDefault(Object key, V defaultValue);
    V putIfAbsent(K key, V value);
    V replace(K key, V value);
    boolean replace(K key, V oldValue, V newValue);
    void replaceAll(BiFunction<? super K, ? super V, ? extends V> f);
    V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction);
    V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction);
    V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction);
    V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction);
    void forEach(BiConsumer<? super K, ? super V> action);
}
```

---

## 3. HASHMAP - INTERNAL WORKING (MOST ASKED)

### 3.1 Data Structure

```
HashMap Internals: Array + LinkedList + Tree (JDK 8+)

    Index Calculation: index = hash(key) & (n-1)
    where n = table.length (always power of 2)

    ┌──────────────────────────────────────────────────┐
    │  Bucket Array (Node[] table)                     │
    │  Default Capacity = 16                           │
    │  Load Factor = 0.75                              │
    │  Threshold = capacity * loadFactor = 12          │
    ├──────┬───────────────────────────────────────────┤
    │  [0] │ → null                                    │
    │  [1] │ → null                                    │
    │  [2] │ → Entry(key=5, val=A) → Entry(key=21, val=B) → null
    │  [3] │ → null                                    │
    │  ...│                                            │
    │  [12]│ → (AUTO-RESIZE triggered at this point)   │
    │  [15]│ → null                                    │
    └──────┴───────────────────────────────────────────┘
```

### 3.2 put() Operation Flow

```
    map.put("Amit", 100)

    Step 1: Compute hash
    ┌─────────────────────────────────────────┐
    │  hash("Amit")                           │
    │  = key.hashCode() ^ (h >>> 16)          │
    │  (Spreads upper bits to lower bits)     │
    └──────────────────┬──────────────────────┘
                       │
    Step 2: Find bucket index
    ┌─────────────────────────────────────────┐
    │  index = hash & (table.length - 1)      │
    │  e.g. hash=2048, len=16                 │
    │  index = 2048 & 15 = 0                  │
    └──────────────────┬──────────────────────┘
                       │
    Step 3: Check bucket
    ┌─────────────────────────────────────────┐
    │  if (table[index] == null)              │
    │      → CREATE new Node, place it        │
    │  else                                   │
    │      → TRAVERSE the chain/tree          │
    │        if key.equals(existingKey)       │
    │            → UPDATE value               │
    │        else                             │
    │            → ADD to end (or treeify)    │
    └──────────────────┬──────────────────────┘
                       │
    Step 4: Check resize
    ┌─────────────────────────────────────────┐
    │  if (size > threshold)                  │
    │      → resize() → double the array     │
    │      → rehash all entries               │
    └─────────────────────────────────────────┘
```

### 3.3 Hashing & Collision

```
    COLLISION: Two different keys → same bucket index

    Example: key=2 and key=18 both give index=2 (in size 16)

    ┌──────────┐
    │ Bucket[2]│ → Entry(key=2, val="A")
    │          │   └→ Entry(key=18, val="B")  ← Linked list (chain)
    └──────────┘

    Resolution Methods:
    ┌─────────────────┬───────────────────────────────────────┐
    │ JDK 7 and below │ JDK 8 and above                      │
    ├─────────────────┼───────────────────────────────────────┤
    │ Singly Linked   │ Singly Linked (when bucket < 8)      │
    │ List (Head      │ Red-Black Tree (when bucket ≥ 8 AND  │
    │ insertion)      │ array length ≥ 64)                   │
    │                 │                                       │
    │ Time: O(n)      │ Time: O(log n) for tree              │
    └─────────────────┴───────────────────────────────────────┘
```

### 3.4 Treeification (JDK 8+)

```
    Threshold: bucket size ≥ 8 AND table length ≥ 64

    BEFORE (Linked List):           AFTER (Red-Black Tree):
    Bucket[2]:                     Bucket[2]:
    ┌───────┐                      ┌───────┐
    │Entry(2)│                      │  21   │
    └───┬───┘                      └───┬───┘
        │                           ┌──┴──┐
    ┌───┴───┐                      │  5   │ 29
    │Entry(18)│                      └─────┘
    └───┬───┘
        │
    ┌───┴───┐
    │Entry(34)│
    └───┬───┘
        │
      ... (8+ entries)

    DETREEIFY: when bucket ≤ 6 → back to linked list
```

### 3.5 Resizing (Rehashing)

```
    Initial: capacity=16, threshold=12

    When size hits 13 (> threshold 12):

    ┌─────────────────────────────────────────────────────┐
    │ OLD TABLE (size=16)                                 │
    │                                                     │
    │ [0] → null          [8] → null                      │
    │ [1] → null          [9] → null                      │
    │ [2] → A → B         [10] → null                     │
    │ [3] → null          [11] → null                     │
    │ ...                ...                              │
    └──────────────────────────┬──────────────────────────┘
                               │
                         resize() → 2x capacity
                               │
    ┌──────────────────────────▼──────────────────────────┐
    │ NEW TABLE (size=32)                                 │
    │                                                     │
    │ [0] → null          [16] → null                     │
    │ [1] → null          [17] → null                     │
    │ [2] → A             [18] → B    ← B moves to index 18
    │ [3] → null          [19] → null                     │
    │ ...                ...                              │
    │                                                     │
    │ New index = hash & (newLength - 1)                  │
    │           = hash & 31                               │
    └─────────────────────────────────────────────────────┘

    Key: entries either stay at same index OR move to (oldIndex + oldCapacity)
    This is why capacity MUST be power of 2.
```

### 3.6 Key Features Summary

```
    ┌──────────────────────────────────────────────────────────────┐
    │              HashMap Key Points                              │
    ├─────────────────────┬────────────────────────────────────────┤
    │ Internal Structure   │ Node[] table + LinkedList + RBT(JDK8)│
    │ Default Capacity     │ 16                                   │
    │ Load Factor          │ 0.75                                 │
    │ Threshold            │ capacity × loadFactor (12 default)   │
    │ Resize Policy        │ Double when threshold exceeded       │
    │ Null Keys            │ 1 null key allowed (index=0)         │
    │ Null Values          │ Multiple null values allowed          │
    │ Order                │ NO guaranteed order                   │
    │ Thread Safety        │ NOT thread-safe                       │
    │ Hashing              │ key.hashCode() ^ (h >>> 16)          │
    │ Collision Resolution │ Chaining → Treeify (threshold 8)     │
    │ Duplicate Keys       │ Not allowed (overwrites old value)   │
    │ Average Complexity   │ O(1) get/put/remove                   │
    │ Worst Case           │ O(log n) with treeification           │
    └─────────────────────┴────────────────────────────────────────┘
```

---

## 4. HASHMAP vs HASHTABLE vs TREEMAP vs LINKEDHASHMAP

```
    ┌─────────────────┬──────────────┬──────────────┬──────────────┬──────────────────┐
    │ Feature          │ HashMap      │ Hashtable    │ TreeMap      │ LinkedHashMap    │
    ├─────────────────┼──────────────┼──────────────┼──────────────┼──────────────────┤
    │ Structure        │ Array+LL+Tree│ Array+LL     │ Red-Black T  │ HashMap+DLL      │
    │ Thread-safe      │ No           │ Yes (sync)   │ No           │ No               │
    │ Null keys        │ 1 allowed    │ NOT allowed  │ NOT allowed  │ 1 allowed        │
    │ Null values      │ Multiple     │ NOT allowed  │ NOT allowed  │ Multiple         │
    │ Order            │ None         │ None         │ Sorted(K)    │ Insertion/Access │
    │ Performance      │ O(1)         │ O(1)         │ O(log n)     │ O(1)             │
    │ Legacy           │ No           │ Yes (JDK1.0) │ No           │ No               │
    │ Hashtable import │ java.util    │ java.util    │ java.util    │ java.util        │
    │ Replaces         │ Hashtable    │ -            │ -            │ -                │
    │ Best for         │ Fast access  │ Thread-safe  │ Sorted data  │ Insertion order  │
    │ Initial Cap      │ 16           │ 11           │ -            │ 16               │
    │ Load Factor      │ 0.75         │ 0.75         │ -            │ 0.75             │
    └─────────────────┴──────────────┴──────────────┴──────────────┴──────────────────┘
```

---

## 5. LINKEDHASHMAP - ORDERING

```java
    // Insertion Order (default)
    LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

    // Access Order (for LRU Cache)
    LinkedHashMap<String, Integer> lru = new LinkedHashMap<>(16, 0.75f, true);

    // Access order: get() also reorders the entry to the end
```

```
    INSERTION ORDER:                ACCESS ORDER:
    ┌─────┐    ┌─────┐            ┌─────┐    ┌─────┐
    │ A:1 │───→│ B:2 │            │ A:1 │───→│ C:3 │  ← A accessed, moved
    └──┬──┘    └──┬──┘            └─────┘    └─────┘
       │         │                 after get("A"):
    ┌──▼──┐    ┌──▼──┐            B→A→C becomes B→C→A
    │ C:3 │───→│ D:4 │
    └─────┘    └─────┘
```

### LRU Cache Implementation

```java
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public LRUCache(int maxSize) {
        super(16, 0.75f, true);  // accessOrder = true
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;  // auto-remove oldest when size > maxSize
    }
}

// Usage
LRUCache<String, Integer> cache = new LRUCache<>(3);
cache.put("A", 1);  // [A]
cache.put("B", 2);  // [A, B]
cache.put("C", 3);  // [A, B, C]
cache.get("A");      // [B, C, A]  -- A moved to end
cache.put("D", 4);   // [C, A, D]  -- B evicted (eldest)
```

---

## 6. TREEMAP - SORTED MAP

```java
    TreeMap<String, Integer> treeMap = new TreeMap<>();
    treeMap.put("Charlie", 3);
    treeMap.put("Alice", 1);
    treeMap.put("Bob", 2);

    // Keys are sorted: Alice, Bob, Charlie

    // NavigableMap methods
    treeMap.firstKey();          // "Alice"
    treeMap.lastKey();           // "Charlie"
    treeMap.lowerKey("Bob");     // "Alice"  (< Bob)
    treeMap.higherKey("Bob");    // "Charlie" (> Bob)
    treeMap.headMap("Charlie");  // {Alice=1, Bob=2}
    treeMap.tailMap("Bob");      // {Bob=2, Charlie=3}
    treeMap.subMap("Alice", "Charlie"); // {Alice=1, Bob=2}
```

```
    TreeMap Red-Black Tree Structure:
    
                    Charlie(3)
                   /           \
              Alice(1)       null
              /     \
           null    Bob(2)

    inOrder traversal → Alice, Bob, Charlie (sorted!)
```

### TreeMap with Custom Comparator

```java
    // Reverse order
    TreeMap<String, Integer> reverse = new TreeMap<>(Comparator.reverseOrder());
    reverse.put("A", 1);  // Z→Y→...→A order

    // Case-insensitive
    TreeMap<String, Integer> caseInsensitive = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    caseInsensitive.put("apple", 1);
    caseInsensitive.put("Banana", 2);
    caseInsensitive.put("CHERRY", 3);
    // Sorted: apple, Banana, CHERRY
```

---

## 7. CONCURRENTHASHMAP (Thread-safe, High Performance)

### 7.1 JDK 7 vs JDK 8

```
    JDK 7: Segment Locking (Lock per segment)
    ┌──────────────────────────────────────────┐
    │  Segment[0] ──→ bucket0 → bucket1 → ...  │  ← One lock
    │  Segment[1] ──→ bucket4 → bucket5 → ...  │  ← One lock
    │  Segment[2] ──→ bucket8 → bucket9 → ...  │  ← One lock
    │  Segment[3] ──→ bucket12 → bucket13→ ... │  ← One lock
    └──────────────────────────────────────────┘
    Concurrency Level = 16 (default) → 16 locks

    JDK 8: CAS + synchronized (per bucket)
    ┌──────────────────────────────────────────┐
    │  [0] null                                 │
    │  [1] Node → Node → null  [synchronized]  │  ← Lock on bucket only
    │  [2] null                                 │
    │  [3] CAS UPDATE                           │
    │  ...                                      │
    └──────────────────────────────────────────┘
    No more segments. Fine-grained locking.
```

### 7.2 ConcurrentHashMap Methods

```java
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

    // Atomic operations
    map.putIfAbsent("key", 1);
    map.computeIfAbsent("key", k -> expensiveComputation(k));
    map.merge("key", 1, Integer::sum);  // atomic increment
    map.replace("key", 1, 2);           // CAS replace
    map.remove("key", 1);               // conditional remove

    // Bulk operations (JDK 8+)
    map.forEach(2, (key, val) -> System.out.println(key + "=" + val)); // parallelism threshold
    map.reduce(2, (k, v) -> v, Integer::sum);  // parallel reduce
    map.search(2, (k, v) -> v > 10 ? k : null); // parallel search

    // ❌ NOT ALLOWED
    map.putIfAbsent("key", null);  // throws NPE (null values not allowed)
    map.compute("key", (k, v) -> null);  // removes key if function returns null
```

### 7.3 ConcurrentHashMap vs Collections.synchronizedMap

```
    ┌──────────────────────────┬──────────────────────────┐
    │ ConcurrentHashMap        │ synchronizedMap           │
    ├──────────────────────────┼──────────────────────────┤
    │ Lock per bucket (JDK8)   │ Lock on whole map        │
    │ No null keys/values      │ Allows null key/values   │
    │ High concurrency         │ Low concurrency          │
    │ Read without lock        │ Read needs lock          │
    │ Atomic methods           │ No atomic methods        │
    │ Weakly consistent iter   │ Fail-fast iterator       │
    └──────────────────────────┴──────────────────────────┘
```

---

## 8. MAP - ALL INTERVIEW QUESTIONS (50+)

### ⭐ BASIC LEVEL (Q1-Q15)

**Q1: What is Map in Java?**
> Map is a collection that stores key-value pairs. Each key maps to exactly one value. Keys must be unique.

**Q2: What are the main implementations of Map?**
> HashMap, TreeMap, LinkedHashMap, Hashtable, ConcurrentHashMap, Properties, EnumMap, WeakHashMap, IdentityHashMap

**Q3: Can Map store duplicate keys?**
> No. put() overwrites the old value if key already exists. Returns old value.

**Q4: Can Map store null keys and values?**
```
┌────────────────────┬───────────┬───────────────┐
│ Implementation      │ Null Key  │ Null Values   │
├────────────────────┼───────────┼───────────────┤
│ HashMap            │ 1 allowed │ Multiple      │
│ LinkedHashMap      │ 1 allowed │ Multiple      │
│ TreeMap            │ NOT       │ Depends       │
│ Hashtable          │ NOT       │ NOT           │
│ ConcurrentHashMap  │ NOT       │ NOT           │
│ EnumMap            │ NOT       │ Depends       │
└────────────────────┴───────────┴───────────────┘
```

**Q5: Difference between Map and Collection?**
```
Collection:                          Map:
- Single values                      - Key-Value pairs
- List, Set, Queue                   - HashMap, TreeMap, etc.
- add(element)                       - put(key, value)
- Iterator to traverse               - entrySet(), keySet(), values()
```

**Q6: What is difference between HashMap and Hashtable?**
```
┌────────────────────┬───────────────┬───────────────┐
│ Feature             │ HashMap       │ Hashtable     │
├────────────────────┼───────────────┼───────────────┤
│ Thread Safety      │ No            │ Yes (sync)    │
│ Performance        │ Fast          │ Slow          │
│ Null Keys          │ 1 allowed     │ NOT allowed   │
│ Null Values        │ Yes           │ NOT allowed   │
│ Legacy             │ No            │ Yes           │
│ Iterator           │ fail-fast     │ fail-fast     │
│ Subclass           │ No            │ Properties    │
│ Since              │ JDK 1.2       │ JDK 1.0       │
└────────────────────┴───────────────┴───────────────┘
```

**Q7: What is fail-fast iterator?**
> If map is structurally modified (add/remove) during iteration, iterator throws `ConcurrentModificationException`. This is fail-fast behavior. Works via `modCount` variable.

**Q8: What is fail-safe iterator?**
> ConcurrentHashMap and CopyOnWriteArray iterators work on snapshot. No exception on modification. Iteration is slow but safe.

**Q9: What is default capacity of HashMap?**
> 16 buckets. Load factor = 0.75. Threshold = 12. Resizes when 13th element added.

**Q10: Why HashMap initial capacity is 16?**
> Power of 2 enables bitwise AND for index calculation (faster than modulo). Distribution is also better with power of 2.

**Q11: What happens when you put null key in HashMap?**
> HashMap allows 1 null key. `hash(null) = 0`. It goes to bucket index 0.

**Q12: What is the difference between HashMap and LinkedHashMap?**
```
HashMap:           No order guaranteed.
LinkedHashMap:     Maintains insertion order (default) or access order (if configured).
```

**Q13: What is TreeMap used for?**
> When you need keys in sorted order. Uses Red-Black tree internally. O(log n) operations.

**Q14: How does TreeMap maintain order?**
> Uses Red-Black tree. Keys compared using Comparable or Comparator. In-order traversal gives sorted keys.

**Q15: What is Properties class?**
> Subclass of Hashtable. Stores key-value as Strings. Used for configuration files.
```java
Properties props = new Properties();
props.load(new FileInputStream("config.properties"));
String dbUrl = props.getProperty("db.url");
```

---

### ⭐⭐ MIDDLE LEVEL (Q16-Q35)

**Q16: How HashMap internally works? (Deep)**
```
put(key, value):
1. Compute hash: h = key.hashCode() ^ (h >>> 16)
2. Find index: index = h & (table.length - 1)
3. If bucket empty → create node
4. If bucket has entries → traverse:
   a. If key.equals(existingKey) → update value
   b. Else → add to tail (or treeify if ≥ 8)
5. If size > threshold → resize (double capacity)
```

**Q17: Why HashMap uses AND (&) with (length-1) instead of modulo?**
> Because length is always power of 2, (length-1) in binary is all 1s. AND is faster than modulo operation. Same result when n = 2^k.

**Q18: Why HashMap capacity is always power of 2?**
```
length=16 (10000), length-1=15 (01111)

hash=2048 (100000000000)  → index = 2048 & 15 = 0
hash=2049 (100000000001)  → index = 2049 & 15 = 1

If length=15 (01111):
hash=2048 & 14 = 0
hash=2049 & 14 = 0   ← More collisions!
```

**Q19: Why load factor is 0.75?**
> Balance between time (fewer collisions) and space (wasted buckets). At 0.75, each bucket has ~0.63 entries on average (Poisson distribution). O(1) is maintained.

**Q20: What is TreeNode vs Node?**
```
Node (LinkedList):
┌─────────┐
│ hash | key | value | next │
└─────────┘

TreeNode (Red-Black Tree):
┌──────────────────────────────────────┐
│ hash | key | value | next |          │
│        parent | left | right | red   │
└──────────────────────────────────────┘
TreeNode uses 2x memory → only used when bucket ≥ 8
```

**Q21: When treeification happens?**
```
Bucket size ≥ 8  AND  Table length ≥ 64
                    ↓
If table < 64:     → resize() instead of treeify
If both conditions: → convert linked list to red-black tree
```

**Q22: Why 8 for treeification?**
> Based on Poisson distribution. Probability of 8 entries in same bucket ≈ 0.00000006. Extremely rare. Space-time tradeoff.

**Q23: How does HashMap handle key collisions?**
```
1. Linked List chaining (JDK 7: head insertion, JDK 8: tail insertion)
2. When bucket fills (≥ 8): Red-Black Tree
3. hash() method distributes keys across buckets
```

**Q24: What happens when two keys have same hashCode()?**
```
Both go to same bucket. They form a chain:
Bucket[2]:
┌──────────┐    ┌──────────┐
│ key=A    │───→│ key=B    │──→ null
│ hash=2   │    │ hash=18  │
│ val=X    │    │ val=Y    │
└──────────┘    └──────────┘
Both have different hashCode() but same index after & operation.
```

**Q25: Can two objects have same hashCode()?**
> Yes. hashCode() is 32-bit int, objects are infinite. But good hashCode() minimizes collisions. `equals()` must be consistent with `hashCode()`.

**Q26: Contract between hashCode() and equals()?**
```
Rules:
1. If equals() → true, then hashCode() MUST be same
2. If hashCode() same → equals() can be false (collision)
3. If equals() → false, hashCode() SHOULD be different
4. hashCode() must be consistent across same session
```

**Q27: Why ConcurrentHashMap doesn't allow null keys/values?**
> In a concurrent environment, null is ambiguous. `get(key)` returns null → is key absent or value is null? In single-threaded HashMap, you can use `containsKey()`. In concurrent, another thread could modify between `get()` and `containsKey()`.

**Q28: How ConcurrentHashMap achieves thread safety (JDK 8)?**
```
1. CAS (Compare-And-Swap) for empty bucket insertion
2. synchronized on bucket Node for collision handling
3. volatile reads for visibility
4. No lock on read operations
5. Fine-grained: only locks one bucket at a time
```

**Q29: What is computeIfAbsent()?**
```java
map.computeIfAbsent("key", k -> {
    // Only executed if key is absent
    // Thread-safe in ConcurrentHashMap
    return expensiveComputation(k);
});
// Equivalent to:
if (!map.containsKey("key")) {
    map.put("key", expensiveComputation("key"));
}
// But atomic!
```

**Q30: What is merge() method?**
```java
Map<String, Integer> map = new HashMap<>();
map.merge("A", 1, Integer::sum);  // {A=1}
map.merge("A", 1, Integer::sum);  // {A=2}  -- summed!
map.merge("A", 3, Integer::sum);  // {A=5}  -- summed!

// Use case: word count
String[] words = {"apple", "banana", "apple"};
Map<String, Integer> count = new HashMap<>();
for (String w : words) {
    count.merge(w, 1, Integer::sum);
}
// {apple=2, banana=1}
```

**Q31: What is the difference between put and putIfAbsent?**
```
put(k, v):         Always overwrites, returns old value (or null)
putIfAbsent(k, v): Only puts if key absent, returns old value (or null)
```

**Q32: What is replace() method?**
```java
V old = map.replace("key", "newValue");  // Replace if exists
boolean done = map.replace("key", "oldVal", "newVal");  // CAS replace
```

**Q33: How to iterate a Map?**
```java
// Method 1: entrySet (best)
for (Map.Entry<K, V> entry : map.entrySet()) {
    entry.getKey();
    entry.getValue();
}

// Method 2: forEach (JDK 8+)
map.forEach((k, v) -> System.out.println(k + "=" + v));

// Method 3: keySet
for (K key : map.keySet()) {
    V value = map.get(key);  // extra lookup
}
```

**Q34: How to sort a Map?**
```java
// Using TreeMap
TreeMap<String, Integer> sorted = new TreeMap<>(map);

// Using stream
Map<String, Integer> sorted = map.entrySet().stream()
    .sorted(Map.Entry.comparingByValue())
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (a, b) -> a,
        LinkedHashMap::new
    ));
```

**Q35: How to copy one map to another?**
```java
Map<K, V> copy = new HashMap<>(original);        // Shallow copy
Map<K, V> copy2 = new HashMap<>(original);
copy2.putAll(anotherMap);                         // Merge another map
```

---

### ⭐⭐⭐ ADVANCED LEVEL (Q36-Q55)

**Q36: What is IdentityHashMap?**
> Uses `==` instead of `equals()` for key comparison. Useful for topology-preserving transformations like serialization.

**Q37: What is WeakHashMap?**
> Keys are WeakReferences. GC can collect key if no strong reference. Used for caches that should not prevent GC.

**Q38: What is EnumMap?**
> Map with Enum keys. Internally uses an array indexed by enum ordinal. Very fast and memory-efficient.

```java
EnumMap<Day, String> map = new EnumMap<>(Day.class);
map.put(Day.MONDAY, "Start");
```

**Q39: What is the difference between HashMap and TreeMap?**
```
┌──────────────┬────────────────────┬────────────────────┐
│ Feature       │ HashMap            │ TreeMap            │
├──────────────┼────────────────────┼────────────────────┤
│ Structure    │ Array + LL + Tree  │ Red-Black Tree     │
│ Ordering     │ None               │ Sorted by key      │
│ Time (get)   │ O(1)               │ O(log n)           │
│ Time (put)   │ O(1)               │ O(log n)           │
│ Null keys    │ 1 allowed          │ NOT allowed        │
│ Use case     │ Fast lookup        │ Sorted iteration   │
└──────────────┴────────────────────┴────────────────────┘
```

**Q40: How to make HashMap thread-safe?**
```java
// Option 1: ConcurrentHashMap (best)
Map<K, V> map = new ConcurrentHashMap<>();

// Option 2: Collections.synchronizedMap
Map<K, V> map = Collections.synchronizedMap(new HashMap<>());

// Option 3: Manual synchronization
synchronized (map) { map.get("key"); }
```

**Q41: What is ConcurrentHashMap's size() method?**
> Returns an estimate, not exact count. Concurrent modifications may not be reflected. Use `mappingCount()` for long result.

**Q42: How to create immutable Map?**
```java
// JDK 9+
Map<K, V> map = Map.of("key1", val1, "key2", val2);

// JDK 10+
Map<K, V> map = Map.copyOf(original);

// Collections
Map<K, V> map = Collections.unmodifiableMap(original);

// Guava
Map<K, V> map = ImmutableMap.of("key", "value");
```

**Q43: How to handle ConcurrentModificationException?**
```java
// Wrong
for (Map.Entry<K,V> entry : map.entrySet()) {
    if (condition) map.remove(entry.getKey());  // ❌ CME!
}

// Correct - use iterator.remove()
Iterator<Map.Entry<K,V>> it = map.entrySet().iterator();
while (it.hasNext()) {
    if (condition) it.remove();  // ✅ Safe
}

// Or use removeIf (JDK 8+)
map.entrySet().removeIf(entry -> condition);

// ConcurrentHashMap - safe to remove during iteration
```

**Q44: What is hash() method in HashMap?**
```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
// Spreads upper 16 bits into lower 16 bits
// Reduces collisions
```

**Q45: How HashMap handles String keys vs Integer keys?**
```
String: hashCode() = s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
Integer: hashCode() = value (itself)

"AB".hashCode() = 65*31 + 66 = 2081
2081 & 15 = 1 (for capacity 16)

Integer 2081.hashCode() = 2081
2081 & 15 = 1  ← Same bucket!
```

**Q46: Can we use custom object as HashMap key?**
```java
class Student {
    String name;
    int rollNo;

    @Override
    public int hashCode() {
        return Objects.hash(rollNo);  // Use fields that define equality
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;
        Student s = (Student) obj;
        return this.rollNo == s.rollNo;
    }
}

Map<Student, String> map = new HashMap<>();
// Without hashCode/equals → uses memory address → always unique → no collision resolution
```

**Q47: What happens if key.hashCode() returns same value always?**
```
Degenerate case: All entries go to same bucket → O(n) for all operations
HashMap becomes like a linked list (worst case)

To avoid: hashCode() should distribute uniformly.
Example of bad hashCode():
public int hashCode() { return 1; }  // ALL collisions!
```

**Q48: Difference between clear() and empty Map?**
```java
map.clear();       // Removes all entries, but internal array remains (capacity unchanged)
map = new HashMap<>(); // Creates new empty map
```

**Q49: What is the initial capacity if we do new HashMap<>(30)?**
> HashMap internally rounds up to next power of 2.
> 30 → next power of 2 = 32. So actual capacity = 32.

**Q50: What is the time complexity of HashMap operations?**
```
┌──────────────┬────────────┬──────────────┐
│ Operation     │ Average    │ Worst Case   │
├──────────────┼────────────┼──────────────┤
│ put()         │ O(1)       │ O(log n)*    │
│ get()         │ O(1)       │ O(log n)*    │
│ remove()      │ O(1)       │ O(log n)*    │
│ containsKey() │ O(1)       │ O(log n)*    │
│ containsVal() │ O(n)       │ O(n)         │
│ size()        │ O(1)       │ O(1)         │
└──────────────┴────────────┴──────────────┘
* Worst case with treeification (JDK 8+)
JDK 7: Worst case O(n) without treeification
```

**Q51: What is LinkedHashMap's removeEldestEntry()?**
```java
protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
    return size() > MAX_ENTRIES;  // auto-eviction
}
// Used for LRU, LFU, FIFO caches
```

**Q52: Difference between entrySet(), keySet(), values()?**
```
┌──────────────┬───────────────────┬─────────────────┐
│ Method        │ Return Type       │ Use For          │
├──────────────┼───────────────────┼─────────────────┤
│ entrySet()    │ Set<Entry<K,V>>   │ Both K and V    │
│ keySet()      │ Set<K>            │ Only keys        │
│ values()      │ Collection<V>     │ Only values      │
└──────────────┴───────────────────┴─────────────────┘

entrySet() is most efficient for iteration (no extra lookup)
```

**Q53: Can Map be used in for-each loop directly?**
```
No. Map is not Iterable. You must convert to entrySet(), keySet(), or values().

for (Map.Entry<K,V> e : map.entrySet()) { ... }  // ✅
for (K key : map.keySet()) { ... }                 // ✅
for (V val : map.values()) { ... }                 // ✅
```

**Q54: What is difference between remove() in HashMap and ConcurrentHashMap?**
```
HashMap.remove(key):       Returns value, may throw CME during iteration
ConcurrentHashMap.remove(key, value): CAS-based, thread-safe, no CME
```

**Q55: Practical scenario - When to use which Map?**
```
┌─────────────────────────┬──────────────────────────────────────┐
│ Scenario                 │ Best Choice                          │
├─────────────────────────┼──────────────────────────────────────┤
│ Single thread, fast      │ HashMap                              │
│ Thread-safe access       │ ConcurrentHashMap                    │
│ Sorted keys              │ TreeMap                              │
│ Insertion order needed   │ LinkedHashMap                        │
│ LRU Cache                │ LinkedHashMap (accessOrder=true)     │
│ Configuration file       │ Properties                           │
│ Enum as key              │ EnumMap                              │
│ Weak references          │ WeakHashMap                          │
│ Immutable map            │ Map.of() / Map.copyOf()             │
│ Priority-based           │ TreeMap with custom Comparator       │
└─────────────────────────┴──────────────────────────────────────┘
```

---

## 9. JAVA 8+ MAP NEW METHODS

```java
Map<String, Integer> map = new HashMap<>();

// getOrDefault - returns default if key not found
int val = map.getOrDefault("key", 0);

// putIfAbsent - only puts if absent
map.putIfAbsent("key", 1);

// computeIfAbsent - compute value only if absent
map.computeIfAbsent("key", k -> k.length());

// computeIfPresent - compute only if present
map.computeIfPresent("key", (k, v) -> v + 1);

// compute - compute regardless
map.compute("key", (k, v) -> v == null ? 1 : v + 1);

// merge - merge with existing
map.merge("key", 1, Integer::sum);

// replaceAll - replace all values
map.replaceAll((k, v) -> v * 2);

// forEach - iterate
map.forEach((k, v) -> System.out.println(k + "=" + v));

// replace (3 variants)
map.replace("key", 10);                      // replace if exists
map.replace("key", 5, 10);                    // CAS replace
map.replaceAll((k, v) -> v * 2);             // replace all
```

```
Visual: computeIfAbsent vs putIfAbsent

putIfAbsent:
    if key absent → put(key, value)
    if key present → do nothing, return existing value

computeIfAbsent:
    if key absent → compute value using function, put it
    if key present → do nothing, return existing value

    computeIfAbsent is LAZY - function only called when needed
```

---

## 10. MEMORY LAYOUT DIAGRAM

```
HashMap<String, Integer> map = new HashMap<>(4);
map.put("A", 1);
map.put("B", 2);

Memory Layout:
┌─────────────────────────────────────────────────────────┐
│ HashMap Object                                          │
│ ├── table (Node[]) ──→ ┌────┬────┬────┬────┐           │
│ │                       │[0] │[1] │[2] │[3] │           │
│ │                       └──┬─┴────┴─┬──┴────┘           │
│ │                          │        │                    │
│ │                          ▼        ▼                    │
│ │                     ┌────────┐ ┌────────┐             │
│ │                     │Node    │ │Node    │             │
│ │                     │hash=65 │ │hash=66 │             │
│ │                     │key="A" │ │key="B" │             │
│ │                     │val=1   │ │val=2   │             │
│ │                     │next=null│ │next=null│             │
│ │                     └────────┘ └────────┘             │
│ ├── size = 2                                             │
│ ├── modCount = 2                                         │
│ ├── threshold = 3  (4 * 0.75)                           │
│ └── loadFactor = 0.75                                   │
└─────────────────────────────────────────────────────────┘
```

---

## 11. QUICK REFERENCE CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════╗
║                   JAVA MAP CHEAT SHEET                       ║
╠══════════════════════════════════════════════════════════════╣
║                                                              ║
║  HashMap    → Fastest, no order, null key allowed            ║
║  LinkedHashMap → Insertion/Access order, LRU cache           ║
║  TreeMap    → Sorted keys, O(log n), no null keys            ║
║  Hashtable  → Legacy, thread-safe, no null                   ║
║  ConcurrentHashMap → Concurrent, no null, CAS + sync         ║
║  EnumMap    → Enum keys, array-backed, fastest               ║
║  Properties → String config files                            ║
║                                                              ║
║  Default Capacity: 16 | Load Factor: 0.75 | Resize: 2x      ║
║  Treeify: bucket ≥ 8 & table ≥ 64 | Detreeify: bucket ≤ 6   ║
║  Null Key: HashMap allows 1 | Hashtable/ConcurrentMap: 0     ║
║                                                              ║
║  hash() = key.hashCode() ^ (h >>> 16)                       ║
║  index = hash & (length - 1)                                 ║
║                                                              ║
║  put() → hash → index → check → add/update → resize?        ║
║  get() → hash → index → traverse → equals() → return        ║
║                                                              ║
╚══════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: Java 8+ features, Interview Focus, All Major Topics*
