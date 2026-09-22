# Java Collections Utility Class - Complete Interview Notes

---

## 1. COLLECTIONS CLASS OVERVIEW

```
    java.util.Collections (not java.util.Collection!)
    ┌──────────────────────────────────────────────────────────────┐
    │  Collections is a utility class containing static methods    │
    │  for operating on Collections. It has private constructor    │
    │  (cannot be instantiated).                                   │
    │                                                              │
    │  All methods are static. Works on List, Set, Map, etc.       │
    └──────────────────────────────────────────────────────────────┘

    Import: import java.util.Collections;
```

---

## 2. ALL METHODS BY CATEGORY

### 2.1 SORTING

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                    │ Description                      │
    ├──────────────────────────────────────────────────────────────┤
    │  sort(List<T>)             │ Sort using natural ordering      │
    │  sort(List<T>, Comparator) │ Sort using custom Comparator     │
    └──────────────────────────────────────────────────────────────┘

    List<Integer> list = new ArrayList<>(Arrays.asList(5, 3, 1, 4, 2));
    Collections.sort(list);                    // [1, 2, 3, 4, 5]
    Collections.sort(list, Comparator.reverseOrder()); // [5, 4, 3, 2, 1]

    List<String> names = new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob"));
    Collections.sort(names);                   // [Alice, Bob, Charlie]
    Collections.sort(names, Comparator.comparingInt(String::length)); // [Bob, Alice, Charlie]
```

### 2.2 SEARCHING

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                         │ Description                 │
    ├──────────────────────────────────────────────────────────────┤
    │  binarySearch(List, key)        │ Search in SORTED list       │
    │  binarySearch(List, key, Comp)  │ Search with Comparator      │
    │  indexOfSubList(List, target)   │ Find first occurrence       │
    │  lastIndexOfSubList(List, sub)  │ Find last occurrence        │
    └──────────────────────────────────────────────────────────────┘

    List<Integer> sorted = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
    int idx = Collections.binarySearch(sorted, 5);  // 2
    int idx2 = Collections.binarySearch(sorted, 4);  // -3 (insertion point)
    // Negative return = insertion point = -(index + 1)

    List<String> list = Arrays.asList("A", "B", "C", "B", "A");
    int first = Collections.indexOfSubList(list, Arrays.asList("B", "C"));  // 1
    int last = Collections.lastIndexOfSubList(list, Arrays.asList("B"));   // 3
```

### 2.3 SHUFFLING

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                         │ Description                 │
    ├──────────────────────────────────────────────────────────────┤
    │  shuffle(List)                  │ Random shuffle              │
    │  shuffle(List, Random)          │ Shuffle with specific Random│
    └──────────────────────────────────────────────────────────────┘

    List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    Collections.shuffle(list);  // [3, 1, 5, 2, 4] (random order)

    // Reproducible shuffle
    Random random = new Random(42);  // seed
    Collections.shuffle(list, random);  // Same result every time
```

### 2.4 REVERSING

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                         │ Description                 │
    ├──────────────────────────────────────────────────────────────┤
    │  reverse(List)                  │ Reverse in-place            │
    │  reverseOrder()                 │ Comparator for reverse order│
    │  reverseOrder(Comparator)       │ Reverse of given Comparator │
    └──────────────────────────────────────────────────────────────┘

    List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    Collections.reverse(list);  // [5, 4, 3, 2, 1]

    Comparator<String> comp = Collections.reverseOrder();
    Collections.sort(names, comp);  // Sorts in reverse natural order
```

### 2.5 ROTATION

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                         │ Description                 │
    ├──────────────────────────────────────────────────────────────┤
    │  rotate(List, distance)         │ Rotate elements             │
    └──────────────────────────────────────────────────────────────┘

    List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    Collections.rotate(list, 2);  // [4, 5, 1, 2, 3]
    // Last 2 elements moved to front

    Collections.rotate(list, -2); // [1, 2, 3, 4, 5] (back to original)
    // First 2 elements moved to end

    Visual (rotate by 2):
    BEFORE: [1][2][3][4][5]
    AFTER:  [4][5][1][2][3]
             ↑  ↑
          Last 2 moved to front
```

### 2.6 SWAPPING

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                         │ Description                 │
    ├──────────────────────────────────────────────────────────────┤
    │  swap(List, i, j)               │ Swap elements at indices    │
    └──────────────────────────────────────────────────────────────┘

    List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    Collections.swap(list, 0, 4);  // [5, 2, 3, 4, 1]
    Collections.swap(list, 1, 3);  // [5, 4, 3, 2, 1]
```

### 2.7 FREQUENCY & DISJOINT

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                         │ Description                 │
    ├──────────────────────────────────────────────────────────────┤
    │  frequency(Collection, obj)     │ Count occurrences           │
    │  disjoint(Collection, Collection)│ Check if no common element │
    └──────────────────────────────────────────────────────────────┘

    List<String> list = Arrays.asList("A", "B", "A", "C", "A");
    int count = Collections.frequency(list, "A");  // 3
    int count2 = Collections.frequency(list, "B"); // 1

    List<Integer> l1 = Arrays.asList(1, 2, 3);
    List<Integer> l2 = Arrays.asList(4, 5, 6);
    boolean disj = Collections.disjoint(l1, l2);  // true (no common)

    List<Integer> l3 = Arrays.asList(3, 4, 5);
    boolean disj2 = Collections.disjoint(l1, l3); // false (3 is common)
```

### 2.8 MIN & MAX

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                         │ Description                 │
    ├──────────────────────────────────────────────────────────────┤
    │  min(Collection)                │ Min by natural ordering     │
    │  min(Collection, Comparator)    │ Min by Comparator           │
    │  max(Collection)                │ Max by natural ordering     │
    │  max(Collection, Comparator)    │ Max by Comparator           │
    └──────────────────────────────────────────────────────────────┘

    List<Integer> list = Arrays.asList(5, 3, 1, 4, 2);
    int min = Collections.min(list);  // 1
    int max = Collections.max(list);  // 5

    List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
    String shortest = Collections.min(names, Comparator.comparingInt(String::length));  // Bob
    String longest = Collections.max(names, Comparator.comparingInt(String::length));   // Charlie
```

### 2.9 UNMODIFIABLE COLLECTIONS

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                         │ Description                 │
    ├──────────────────────────────────────────────────────────────┤
    │  unmodifiableList(List)         │ Read-only wrapper           │
    │  unmodifiableSet(Set)           │ Read-only wrapper           │
    │  unmodifiableMap(Map)           │ Read-only wrapper           │
    │  unmodifiableCollection(Coll)   │ Read-only wrapper           │
    └──────────────────────────────────────────────────────────────┘

    List<String> original = new ArrayList<>(Arrays.asList("A", "B"));
    List<String> unmodifiable = Collections.unmodifiableList(original);

    unmodifiable.add("C");  // UnsupportedOperationException!
    unmodifiable.remove("A"); // UnsupportedOperationException!

    // BUT: original can still be modified!
    original.add("C");  // This works! unmodifiable now sees ["A", "B", "C"]
    // unmodifiable is just a WRAPPER, not a copy
```

### 2.10 SYNCHRONIZED COLLECTIONS

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                         │ Description                 │
    ├──────────────────────────────────────────────────────────────┤
    │  synchronizedList(List)         │ Thread-safe list wrapper    │
    │  synchronizedSet(Set)           │ Thread-safe set wrapper     │
    │  synchronizedMap(Map)           │ Thread-safe map wrapper     │
    │  synchronizedCollection(Coll)   │ Thread-safe collection      │
    └──────────────────────────────────────────────────────────────┘

    List<String> list = new ArrayList<>();
    List<String> syncList = Collections.synchronizedList(list);

    syncList.add("A");  // Internally synchronized

    // ⚠️ Iteration still needs manual synchronization:
    synchronized (syncList) {
        for (String s : syncList) {
            System.out.println(s);
        }
    }
```

### 2.11 EMPTY COLLECTIONS

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                         │ Description                 │
    ├──────────────────────────────────────────────────────────────┤
    │  emptyList()                    │ Empty immutable List        │
    │  emptySet()                     │ Empty immutable Set         │
    │  emptyMap()                     │ Empty immutable Map         │
    │  singletonList(Object)          │ List with single element    │
    │  singleton(Object)              │ Set with single element     │
    │  singletonMap(k, v)             │ Map with single entry       │
    └──────────────────────────────────────────────────────────────┘

    List<String> empty = Collections.emptyList();  // []
    Set<Integer> single = Collections.singleton(42);  // [42]
    Map<String, Integer> one = Collections.singletonMap("A", 1);  // {A=1}

    // All are IMMUTABLE - cannot add/remove elements
```

### 2.12 FILL & COPY

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                         │ Description                 │
    ├──────────────────────────────────────────────────────────────┤
    │  fill(List, obj)                │ Fill with single value      │
    │  copy(dest, src)                │ Copy elements               │
    │  nCopies(n, obj)                │ List with n copies          │
    └──────────────────────────────────────────────────────────────┘

    List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
    Collections.fill(list, "X");  // [X, X, X]

    List<String> src = Arrays.asList("A", "B", "C");
    List<String> dest = new ArrayList<>(Arrays.asList("X", "X", "X", "X", "X"));
    Collections.copy(dest, src);  // dest = [A, B, C, X, X]
    // dest must be at least as large as src!

    List<String> copies = Collections.nCopies(5, "hello");
    // [hello, hello, hello, hello, hello]
```

### 2.13 ENUMERATION & LIST

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                         │ Description                 │
    ├──────────────────────────────────────────────────────────────┤
    │  enumeration(List)              │ Get Enumeration             │
    │  list(Enumeration)              │ Convert Enumeration to List │
    └──────────────────────────────────────────────────────────────┘

    List<String> list = Arrays.asList("A", "B", "C");
    Enumeration<String> en = Collections.enumeration(list);
    List<String> backToList = Collections.list(en);
```

---

## 3. UNMODIFIABLE vs IMMUTABLE

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Unmodifiable (wrapper):                                     │
    │  - Wrapper prevents add/remove/set on wrapper                │
    │  - Original list can still be modified                       │
    │  - Changes to original reflect in wrapper                    │
    │  - Created by Collections.unmodifiableXxx()                  │
    │                                                              │
    │  Immutable (copy):                                           │
    │  - No reference to original                                  │
    │  - Completely immutable, no modification possible            │
    │  - Created by List.of(), List.copyOf(), Set.copyOf()        │
    └──────────────────────────────────────────────────────────────┘

    // Unmodifiable wrapper
    List<String> orig = new ArrayList<>(Arrays.asList("A"));
    List<String> unmod = Collections.unmodifiableList(orig);
    orig.add("B");  // unmod now shows ["A", "B"] ← still sees changes!

    // Immutable copy
    List<String> orig2 = new ArrayList<>(Arrays.asList("A"));
    List<String> imm = List.copyOf(orig2);
    orig2.add("B");  // imm still ["A"] ← no reference to orig2
```

---

## 4. SYNCHRONIZED WRAPPER vs CONCURRENT COLLECTIONS

```
    ┌──────────────────────┬──────────────────────┬──────────────────────┐
    │ Feature               │ synchronizedXxx      │ ConcurrentHashMap    │
    ├──────────────────────┼──────────────────────┼──────────────────────┤
    │ Lock                  │ Single lock (whole)  │ Per-bucket lock      │
    │ Null keys/values      │ Allowed              │ NOT allowed          │
    │ Concurrency           │ Low                  │ High                 │
    │ Performance           │ Slow                 │ Fast                 │
    │ Iterator              │ fail-fast            │ weakly consistent    │
    │ Complexity            │ Simple               │ Complex              │
    │ When to use           │ Simple sync need     │ High concurrency     │
    └──────────────────────┴──────────────────────┴──────────────────────┘
```

---

## 5. COLLECTIONS UTILITY CLASS - INTERVIEW QUESTIONS (30+)

### ⭐ BASIC

**Q1: What is Collections class?**
> Utility class in java.util with static methods for operating on collections. Cannot be instantiated (private constructor).

**Q2: Difference between Collection and Collections?**
```
Collection: Interface (List, Set, Queue parent)
Collections: Utility class with static methods
```

**Q3: How to sort a list?**
```java
Collections.sort(list);                    // natural order
Collections.sort(list, comparator);         // custom order
list.sort(comparator);                      // Java 8+ method on List
```

**Q4: How to reverse a list?**
```java
Collections.reverse(list);  // In-place reversal
```

**Q5: How to shuffle a list?**
```java
Collections.shuffle(list);  // Random order
```

**Q6: What is binarySearch?**
> Searches in a SORTED list. Returns index if found, negative value if not found. Negative return = -(insertion point) - 1.

**Q7: What does negative return from binarySearch mean?**
```
If not found: return -(insertionPoint) - 1
Insertion point = index where element would be inserted to maintain order

Example: [1, 3, 5, 7], search for 4
Not found → -(2) - 1 = -3
Insertion point = 2 (between 3 and 5)
```

**Q8: How to make a list unmodifiable?**
```java
List<String> unmod = Collections.unmodifiableList(list);
// Throws UnsupportedOperationException on add/remove/set
```

**Q9: How to make a thread-safe list?**
```java
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
// Still need manual synchronization for iteration
```

**Q10: What is Collections.emptyList()?**
> Returns an empty immutable List. No elements can be added. Useful as return type when list might be empty.

---

### ⭐⭐ MIDDLE

**Q11: What is the difference between unmodifiableList and List.of()?**
```
unmodifiableList: Wrapper around mutable list, original can change
List.of():        Immutable copy, no reference to original
```

**Q12: How does binarySearch work internally?**
```
1. List must be sorted
2. Uses iterative binary search
3. Compares middle element with key
4. If equal → return index
5. If key < middle → search left half
6. If key > middle → search right half
7. Time: O(log n)
```

**Q13: How to find duplicate elements using Collections?**
```java
List<Integer> list = Arrays.asList(1, 2, 2, 3, 3, 3);
Set<Integer> seen = new HashSet<>();
Set<Integer> duplicates = new HashSet<>();
for (Integer i : list) {
    if (!seen.add(i)) duplicates.add(i);
}
// duplicates = {2, 3}

// Or using frequency
for (Integer i : list) {
    if (Collections.frequency(list, i) > 1) {
        duplicates.add(i);
    }
}
```

**Q14: What is difference between swap() and manual swap?**
```
Collections.swap(list, i, j):  One line, handles edge cases
Manual swap: More code, same result
Use Collections.swap for cleaner code.
```

**Q15: How to rotate a list?**
```java
Collections.rotate(list, 2);   // Positive: right rotation
Collections.rotate(list, -2);  // Negative: left rotation
```

**Q16: What is difference between frequency() and manual count?**
```java
// Manual count
int count = 0;
for (String s : list) if (s.equals("A")) count++;

// Collections.frequency (cleaner)
int count = Collections.frequency(list, "A");
```

**Q17: What is disjoint() used for?**
```java
boolean hasNoCommon = Collections.disjoint(list1, list2);
// Returns true if no elements in common
// Useful for checking overlap between collections
```

**Q18: How to fill a list?**
```java
Collections.fill(list, "X");  // Replaces all with "X"
```

**Q19: What is difference between fill() and nCopies()?**
```
fill(list, x):   Modifies existing list, replaces all with x
nCopies(n, x):   Creates NEW immutable list with n copies of x
```

**Q20: How to copy one list to another?**
```java
List<String> src = Arrays.asList("A", "B");
List<String> dest = new ArrayList<>(Arrays.asList("X", "X", "X"));
Collections.copy(dest, src);
// dest = ["A", "B", "X"]
// dest must be >= src.size()
```

**Q21: What is singletonList()?**
```java
List<String> single = Collections.singletonList("A");
// Immutable list with exactly one element
// Cannot add, remove, or modify
```

**Q22: What is the difference between emptyList() and new ArrayList()?**
```
emptyList():  Immutable, fixed size (0), shared instance
new ArrayList(): Mutable, resizable, new object each time
```

**Q23: How to sort a map by values?**
```java
Map<String, Integer> map = new HashMap<>();
map.put("A", 3); map.put("B", 1); map.put("C", 2);

Map<String, Integer> sorted = map.entrySet().stream()
    .sorted(Map.Entry.comparingByValue())
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (a, b) -> a,
        LinkedHashMap::new));
// {B=1, C=2, A=3}
```

**Q24: What is reverseOrder() comparator?**
```java
Comparator<String> comp = Collections.reverseOrder();
// Returns Comparator that sorts in reverse natural order
// Equivalent to Comparator.reverseOrder()
```

**Q25: How to convert Enumeration to List?**
```java
Enumeration<String> en = Collections.enumeration(list);
List<String> back = Collections.list(en);
```

---

### ⭐⭐⭐ ADVANCED

**Q26: What is the difference between Collections.sort() and List.sort()?**
```java
Collections.sort(list, comp);  // Static method, creates new ListIterator
list.sort(comp);               // Default method on List interface
// Both use TimSort algorithm (O(n log n))
// list.sort() is slightly more efficient (fewer allocations)
```

**Q27: What sorting algorithm does Collections.sort() use?**
> TimSort (hybrid of Merge Sort and Insertion Sort). Stable, O(n log n), uses natural runs in data.

**Q28: How to handle null in Collections.sort()?**
```java
List<String> list = Arrays.asList("A", null, "B", null);
Collections.sort(list);  // NullPointerException!

// Solution: Use Comparator that handles nulls
Collections.sort(list, Comparator.nullsFirst(Comparator.naturalOrder()));
// [null, null, A, B]

Collections.sort(list, Comparator.nullsLast(Comparator.naturalOrder()));
// [A, B, null, null]
```

**Q29: What is the difference between synchronizedList and CopyOnWriteArrayList?**
```
synchronizedList:      Single lock, fail-fast iterator
CopyOnWriteArrayList:  Write copies array, fail-safe iterator
Use synchronizedList for general sync, CopyOnWrite for read-heavy
```

**Q30: Practical scenario - When to use Collections methods?**
```
┌───────────────────────────────┬──────────────────────────────────┐
│ Scenario                       │ Method                           │
├───────────────────────────────┼──────────────────────────────────┤
│ Sort list                      │ sort()                           │
│ Search in sorted list          │ binarySearch()                   │
│ Randomize order                │ shuffle()                        │
│ Reverse order                  │ reverse()                        │
│ Rotate elements                │ rotate()                         │
│ Swap two elements              │ swap()                           │
│ Count occurrences              │ frequency()                      │
│ Check overlap                  │ disjoint()                       │
│ Find min/max                   │ min() / max()                    │
│ Make read-only                 │ unmodifiableList/Set/Map()       │
│ Make thread-safe               │ synchronizedList/Set/Map()       │
│ Empty immutable collection     │ emptyList/Set/Map()              │
│ Single element collection      │ singleton/singletonList/Map()    │
│ Fill all elements              │ fill()                           │
│ Copy lists                     │ copy()                           │
└───────────────────────────────┴──────────────────────────────────┘
```

---

## 6. QUICK REFERENCE CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║             COLLECTIONS UTILITY CLASS CHEAT SHEET                 ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  SORTING:        sort(list) / sort(list, comparator)             ║
║  SEARCHING:      binarySearch(list, key)  [MUST BE SORTED]       ║
║  SHUFFLING:      shuffle(list)                                    ║
║  REVERSING:      reverse(list)                                    ║
║  ROTATING:       rotate(list, distance)                           ║
║  SWAPPING:       swap(list, i, j)                                 ║
║  MIN/MAX:        min(list) / max(list)                            ║
║  FREQUENCY:      frequency(collection, obj)                       ║
║  DISJOINT:       disjoint(col1, col2) → true if no common        ║
║                                                                  ║
║  UNMODIFIABLE:   unmodifiableList/Set/Map()  [wrapper, no add]   ║
║  SYNCHRONIZED:   synchronizedList/Set/Map()  [thread-safe]       ║
║  EMPTY:          emptyList/Set/Map()          [immutable, 0 size] ║
║  SINGLETON:      singleton(obj) / singletonList / singletonMap    ║
║  FILL:           fill(list, value)                                ║
║  COPY:           copy(dest, src)  [dest.size >= src.size]        ║
║  NCOPIES:        nCopies(n, value) → immutable list              ║
║  ENUMERATION:    enumeration(list) / list(enumeration)           ║
║                                                                  ║
║  Difference:                                                     ║
║  Collection → Interface (List, Set, Queue parent)                ║
║  Collections → Utility class with static methods                 ║
║                                                                  ║
║  ⚠️  binarySearch requires SORTED list                           ║
║  ⚠️  unmodifiable ≠ immutable (original can change)              ║
║  ⚠️  synchronizedList still needs sync block for iteration       ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: All Collections Utility Methods, Interview Questions*
