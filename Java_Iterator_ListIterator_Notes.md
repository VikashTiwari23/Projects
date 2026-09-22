# Iterator & ListIterator - Complete Interview Notes

---

## 1. ITERATOR INTERFACE HIERARCHY (UML)

```
                     <<interface>>
                     ┌──────────────────┐
                     │    Iterable<E>   │
                     │──────────────────│
                     │ iterator()       │
                     │ forEach(Consumer)│
                     │ spliterator()    │
                     └────────┬─────────┘
                              │
                              │ implements
                              │
              ┌───────────────┼───────────────────┐
              │               │                   │
              ▼               ▼                   ▼
       ┌──────────┐   ┌──────────┐       ┌──────────────┐
       │Collection │   │   List   │       │    Set       │
       └──────────┘   └──────────┘       └──────────────┘
              │               │
              │    ┌──────────┴──────────────────────────────┐
              │    │                                         │
              │    │  listIterator() → returns ListIterator   │
              │    │  iterator() → returns Iterator           │
              │    │                                         │
              │    └─────────────────────────────────────────┘
              │
              │    iterator() → returns Iterator<E>
              │
              ▼
       ┌──────────────────────────────────────────────────────┐
       │                Iterator<E>                            │
       │──────────────────────────────────────────────────────│
       │ hasNext() → boolean                                  │
       │ next()    → E (throws NoSuchElementException)        │
       │ remove()  → void (throws IllegalStateException)      │
       │                                                       │
       │ forEachRemaining(Consumer) → default                  │
       └──────────────────────────────────────────────────────┘
              │
              │  extends
              ▼
       ┌──────────────────────────────────────────────────────┐
       │              ListIterator<E>                          │
       │──────────────────────────────────────────────────────│
       │ (inherits all Iterator methods)                       │
       │                                                       │
       │ Navigation:                                           │
       │ hasPrevious() → boolean                               │
       │ previous()    → E                                     │
       │ nextIndex()   → int                                   │
       │ previousIndex() → int                                 │
       │                                                       │
       │ Modification:                                         │
       │ set(E e)     → void (replace current element)         │
       │ add(E e)     → void (insert before cursor)            │
       └──────────────────────────────────────────────────────┘
```

---

## 2. ITERATOR - FUNDAMENTAL CONCEPT

### 2.1 What is Iterator?

```
    Iterator is a design pattern that provides a way to access
    elements of a collection sequentially WITHOUT exposing
    the underlying structure.

    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  ┌─────────────┐     ┌──────────┐     ┌─────────────┐      │
    │  │  Collection  │────→│ Iterator │────→│  Elements    │      │
    │  │  (ArrayList) │     │          │     │  one by one  │      │
    │  └─────────────┘     └──────────┘     └─────────────┘      │
    │                                                              │
    │  client code ←→ Iterator ←→ Collection internals            │
    │                                                              │
    │  Benefits:                                                   │
    │  1. Uniform interface for all collections                    │
    │  2. Hides implementation details                             │
    │  3. Supports safe removal during iteration                   │
    │  4. Single responsibility (traversal logic separate)         │
    └──────────────────────────────────────────────────────────────┘
```

### 2.2 How Iterator Works (Visual)

```
    ArrayList<String> list = new ArrayList<>();
    list.add("A"); list.add("B"); list.add("C"); list.add("D");

    Iterator<String> it = list.iterator();

    Internal state: cursor = 0, lastRet = -1

    Step 1: it.hasNext() → true (cursor=0 < size=4)
    Step 2: it.next() → "A"
    ┌────────────────────────────────────────────────────────┐
    │  list:  [ A ] [ B ] [ C ] [ D ]                        │
    │           ↑                                            │
    │        cursor=1, lastRet=0                             │
    └────────────────────────────────────────────────────────┘

    Step 3: it.hasNext() → true (cursor=1 < size=4)
    Step 4: it.next() → "B"
    ┌────────────────────────────────────────────────────────┐
    │  list:  [ A ] [ B ] [ C ] [ D ]                        │
    │                   ↑                                    │
    │                cursor=2, lastRet=1                     │
    └────────────────────────────────────────────────────────┘

    Step 5: it.hasNext() → true (cursor=2 < size=4)
    Step 6: it.next() → "C"
    ┌────────────────────────────────────────────────────────┐
    │  list:  [ A ] [ B ] [ C ] [ D ]                        │
    │                           ↑                            │
    │                        cursor=3, lastRet=2             │
    └────────────────────────────────────────────────────────┘

    Step 7: it.hasNext() → true (cursor=3 < size=4)
    Step 8: it.next() → "D"
    ┌────────────────────────────────────────────────────────┐
    │  list:  [ A ] [ B ] [ C ] [ D ]                        │
    │                                   ↑                    │
    │                                cursor=4, lastRet=3     │
    └────────────────────────────────────────────────────────┘

    Step 9: it.hasNext() → false (cursor=4 == size=4)
```

---

## 3. ITERATOR METHODS - DETAILED

### 3.1 hasNext()

```java
    public boolean hasNext();

    // Returns true if more elements exist
    // Does NOT move cursor
    // Safe to call multiple times

    Iterator<Integer> it = list.iterator();
    it.hasNext();  // true
    it.hasNext();  // true (still same position)
    it.hasNext();  // true (still same position)
```

### 3.2 next()

```java
    public E next();

    // Returns next element AND advances cursor
    // Throws NoSuchElementException if no more elements
    // Called after hasNext() returns true

    Iterator<String> it = list.iterator();
    it.next();  // "A" (cursor moves from 0 → 1)
    it.next();  // "B" (cursor moves from 1 → 2)
```

### 3.3 remove()

```java
    public void remove();

    // Removes element last returned by next()
    // Can ONLY be called after next() (not after hasNext())
    // Throws IllegalStateException if:
    //   1. next() was not called yet
    //   2. remove() was already called after last next()

    Iterator<String> it = list.iterator();
    it.next();      // "A"
    it.remove();    // Removes "A" from list
    it.next();      // "B"
    it.remove();    // Removes "B" from list
    // ✅ CORRECT: next() before every remove()

    // ❌ WRONG:
    it.remove();    // IllegalStateException! (no prior next())
```

### 3.4 forEachRemaining() (JDK 8+)

```java
    default void forEachRemaining(Consumer<? super E> action);

    // Calls action for each remaining element
    // Cursor advances to end
    // Cannot be called after next() or remove()

    Iterator<String> it = list.iterator();
    it.forEachRemaining(s -> System.out.println(s));
    // Prints all elements, cursor at end
```

---

## 4. LISTITERATOR - EXTENDED OPERATIONS

### 4.1 What ListIterator Adds

```
    ListIterator extends Iterator with:
    ┌──────────────────────────────────────────────────────────┐
    │  1. Bidirectional traversal (previous/hasPrevious)       │
    │  2. Cursor-based position tracking                       │
    │  3. Element modification during iteration (set)          │
    │  4. Element insertion during iteration (add)             │
    │  5. Position-aware operations (nextIndex, previousIndex) │
    └──────────────────────────────────────────────────────────┘
```

### 4.2 ListIterator Cursor Model

```
    ArrayList<String> list = new ArrayList<>();
    list.add("A"); list.add("B"); list.add("C");

    ListIterator<String> it = list.listIterator();

    Cursor positions (between elements):
    ┌──────────────────────────────────────────────────────────┐
    │                                                          │
    │   Position: 0      1      2      3                      │
    │             ↓      ↓      ↓      ↓                      │
    │          [  A  ][  B  ][  C  ]                           │
    │                                                          │
    │   cursor = current position (insertion point)            │
    │   lastRet = index of last element returned by next()     │
    │            or -1 if none returned yet                    │
    │                                                          │
    └──────────────────────────────────────────────────────────┘

    Cursor can be at:
    - Before first element (index 0)
    - Between elements (index 1, 2, ...)
    - After last element (index = size)
```

### 4.3 ListIterator - Bidirectional Traversal

```
    list: [ A ][ B ][ C ]

    // Forward traversal
    ListIterator<String> it = list.listIterator();
    while (it.hasNext()) {
        System.out.println(it.next());
    }
    // A, B, C
    // cursor = 3 (after last element)

    // Now traverse backward
    while (it.hasPrevious()) {
        System.out.println(it.previous());
    }
    // C, B, A
    // cursor = 0 (before first element)

    Full cycle:
    ┌──────────────────────────────────────────────────────────┐
    │  Forward:  A → B → C (cursor: 0→1→2→3)                  │
    │  Backward: C → B → A (cursor: 3→2→1→0)                  │
    └──────────────────────────────────────────────────────────┘
```

### 4.4 ListIterator - set() Method

```
    Replace current element with new value

    list: [ A ][ B ][ C ]
           ↑
        cursor=1, lastRet=0 (after calling next())

    it.next();      // Returns "A", cursor=1, lastRet=0
    it.set("X");    // Replace "A" with "X"

    list: [ X ][ B ][ C ]

    Rule: set() replaces element last returned by next() or previous()
    Throws IllegalStateException if:
    - next() or previous() not called yet
    - remove() or set() called after last next()/previous()
```

### 4.5 ListIterator - add() Method

```
    Insert element BEFORE cursor position

    list: [ A ][ B ][ C ]
                        ↑
                     cursor=3 (after traversing to end)

    it.add("D");     // Insert "D" before cursor

    list: [ A ][ B ][ C ][ D ]
                           ↑
                        cursor=4, lastRet=-1

    Behavior after add():
    - lastRet is reset to -1
    - cursor moves right by 1
    - New element is at position of old cursor

    Multiple adds:
    list: [ A ][ B ][ C ]

    it.next(); it.next();  // cursor=2, after "B"

    it.add("X");           // Insert before "C"
    list: [ A ][ B ][ X ][ C ]
                       ↑
                    cursor=3

    it.add("Y");           // Insert before "C" again
    list: [ A ][ B ][ X ][ Y ][ C ]
                            ↑
                         cursor=4
```

---

## 5. ITERATOR vs LISTITERATOR COMPARISON

```
    ┌──────────────────────┬──────────────────┬──────────────────────┐
    │ Feature               │ Iterator         │ ListIterator         │
    ├──────────────────────┼──────────────────┼──────────────────────┤
    │ Interface             │ java.util        │ java.util            │
    │ Collections           │ All collections  │ List only            │
    │ Direction             │ Forward only     │ Both (forward+back)  │
    │ hasNext/hasPrevious  │ hasNext only      │ Both                 │
    │ next/previous        │ next only         │ Both                 │
    │ remove()             │ ✅ Yes            │ ✅ Yes                │
    │ set() (replace)      │ ❌ No             │ ✅ Yes                │
    │ add() (insert)       │ ❌ No             │ ✅ Yes                │
    │ nextIndex()          │ ❌ No             │ ✅ Yes                │
    │ previousIndex()      │ ❌ No             │ ✅ Yes                │
    │ forEachRemaining     │ ✅ Yes (JDK 8+)  │ ✅ Yes (JDK 8+)      │
    │ Can add elements     │ ❌ No             │ ✅ Yes                │
    │ Can modify elements  │ ❌ No (only remove)│ ✅ Yes (set)         │
    │ Position tracking    │ ❌ No             │ ✅ Yes                │
    └──────────────────────┴──────────────────┴──────────────────────┘

    Summary:
    ┌──────────────────────────────────────────────────────────────┐
    │  Iterator:    Read + Remove                                  │
    │  ListIterator: Read + Remove + Add + Replace + Navigate     │
    └──────────────────────────────────────────────────────────────┘
```

---

## 6. HOW TO GET ITERATOR

```java
    // From Collection (all collections)
    Collection<String> col = new ArrayList<>();
    Iterator<String> it = col.iterator();          // From Iterable

    // From List (two options)
    List<String> list = new ArrayList<>();
    Iterator<String> it1 = list.iterator();         // Iterator
    ListIterator<String> it2 = list.listIterator(); // ListIterator

    // ListIterator with starting index
    ListIterator<String> it3 = list.listIterator(2); // Start at index 2

    // From Set
    Set<String> set = new HashSet<>();
    Iterator<String> it4 = set.iterator();

    // From Map (via entrySet/keySet/values)
    Map<String, Integer> map = new HashMap<>();
    Iterator<Map.Entry<String, Integer>> it5 = map.entrySet().iterator();
    Iterator<String> it6 = map.keySet().iterator();
    Iterator<Integer> it7 = map.values().iterator();
```

---

## 7. FAIL-FAST vs FAIL-SAFE

### 7.1 Fail-Fast Iterator

```
    Most collections (ArrayList, HashMap, HashSet, LinkedList)
    use modCount to detect concurrent modification.

    ┌──────────────────────────────────────────────────────────────┐
    │  ArrayList internal:                                         │
    │  transient int modCount = 0;                                 │
    │                                                              │
    │  add(), remove(), clear() → modCount++                      │
    │  iterator creation → saves expectedModCount = modCount       │
    │                                                              │
    │  Iterator.next():                                            │
    │    checkForComodification();                                 │
    │    if (modCount != expectedModCount)                         │
    │        throw ConcurrentModificationException();              │
    └──────────────────────────────────────────────────────────────┘

    Example:
    ┌──────────────────────────────────────────────────────────────┐
    │  ArrayList<Integer> list = new ArrayList<>(List.of(1,2,3));  │
    │                                                              │
    │  Iterator<Integer> it = list.iterator();                     │
    │  // expectedModCount = 0, modCount = 0                       │
    │                                                              │
    │  while (it.hasNext()) {                                      │
    │      Integer val = it.next();                                │
    │      // checkForComodification() → modCount == expected?     │
    │      if (val == 2) {                                         │
    │          list.remove(val);  // modCount++ → 1                │
    │          // Next it.next() checks: 1 != 0 → CME!            │
    │      }                                                       │
    │  }                                                           │
    │  // Throws ConcurrentModificationException                   │
    └──────────────────────────────────────────────────────────────┘
```

### 7.2 Fail-Safe Iterator

```
    ConcurrentHashMap, CopyOnWriteArrayList, CopyOnWriteArraySet
    work on SNAPSHOT of data. No CME.

    ┌──────────────────────────────────────────────────────────────┐
    │  CopyOnWriteArrayList:                                       │
    │  - Every write creates new array copy                        │
    │  - Iterator works on original array reference                │
    │  - Writes happen on new array                                │
    │  - Iterator never sees modifications                         │
    │                                                              │
    │  Thread A: Iterator on array[1,2,3]                          │
    │  Thread B: add(4) → creates new array[1,2,3,4]              │
    │  Thread A: next() → still sees [1,2,3] (old array)          │
    └──────────────────────────────────────────────────────────────┘

    ConcurrentHashMap:
    ┌──────────────────────────────────────────────────────────────┐
    │  Weakly consistent iterator:                                 │
    │  - Reflects state at some point at or since creation         │
    │  - May or may not reflect concurrent modifications           │
    │  - Never throws ConcurrentModificationException              │
    │  - Guaranteed not to infinite loop (modifications don't      │
    │    cause re-traversal)                                       │
    └──────────────────────────────────────────────────────────────┘
```

### 7.3 Comparison

```
    ┌─────────────────────┬──────────────────────┬──────────────────────┐
    │ Feature              │ Fail-Fast             │ Fail-Safe            │
    ├─────────────────────┼──────────────────────┼──────────────────────┤
    │ Collections          │ ArrayList, HashMap   │ CopyOnWriteArrayList │
    │                      │ HashSet, LinkedList  │ ConcurrentHashMap   │
    ├─────────────────────┼──────────────────────┼──────────────────────┤
    │ CME on modification  │ YES                  │ NO                   │
    │ Working copy         │ Same data            │ Snapshot             │
    │ Real-time view       │ Yes                  │ No (stale view)      │
    │ Performance          │ Fast                 │ Slower (copy)        │
    │ Use case             │ Single thread        │ Concurrent access    │
    └─────────────────────┴──────────────────────┴──────────────────────┘
```

---

## 8. CONCURRENTMODIFICATIONEXCEPTION - HANDLING

### 8.1 Wrong Way vs Right Way

```java
    List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));

    // ❌ WRONG 1: For-each loop with remove
    for (String s : list) {
        if (s.equals("B")) list.remove(s);  // CME!
    }

    // ❌ WRONG 2: Iterator.remove() without prior next()
    Iterator<String> it = list.iterator();
    it.remove();  // IllegalStateException!

    // ❌ WRONG 3: Index-based loop with remove
    for (int i = 0; i < list.size(); i++) {
        if (list.get(i).equals("B")) list.remove(i);  // Skips elements!
    }
```

### 8.2 Correct Ways

```java
    List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));

    // ✅ CORRECT 1: Iterator.remove()
    Iterator<String> it = list.iterator();
    while (it.hasNext()) {
        if (it.next().equals("B")) it.remove();  // Safe!
    }

    // ✅ CORRECT 2: removeIf() (JDK 8+)
    list.removeIf(s -> s.equals("B"));  // Cleanest!

    // ✅ CORRECT 3: Stream + collect
    list = list.stream()
        .filter(s -> !s.equals("B"))
        .collect(Collectors.toList());

    // ✅ CORRECT 4: CopyOnWriteArrayList (no CME at all)
    List<String> cowList = new CopyOnWriteArrayList<>(list);
    for (String s : cowList) {
        if (s.equals("B")) cowList.remove(s);  // Safe (fail-safe)
    }
```

### 8.3 Visual: Why Index-Based Loop Skips Elements

```
    list: [ A ][ B ][ C ][ D ]

    i=0: list.get(0) = "A" (not "B", continue)
    i=1: list.get(1) = "B" → remove(1)
    list becomes: [ A ][ C ][ D ]
                        ↑
    i=2: list.get(2) = "D"  ← SKIPS "C" because:
    After remove at index 1:
    - "C" shifts to index 1
    - "D" shifts to index 2
    - i increments to 2 → "C" never checked!

    i=3: i >= size (3), loop ends

    Result: "C" not checked → bug!
```

---

## 9. FOR-EACH LOOP UNDER THE HOOD

```
    for (String s : list) {
        System.out.println(s);
    }

    COMPILER converts to:

    Iterator<String> it = list.iterator();
    while (it.hasNext()) {
        String s = it.next();
        System.out.println(s);
    }

    That's why:
    1. for-each uses Iterator internally
    2. for-each cannot modify collection (no iterator reference)
    3. for-each cannot get index
    4. for-each cannot go backward (only forward)
```

---

## 10. PRACTICAL EXAMPLES

### 10.1 Remove All Even Numbers

```java
    List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

    // Method 1: Iterator
    Iterator<Integer> it = list.iterator();
    while (it.hasNext()) {
        if (it.next() % 2 == 0) it.remove();
    }
    // list = [1, 3, 5]

    // Method 2: removeIf
    list.removeIf(n -> n % 2 == 0);
```

### 10.2 Replace All Elements

```java
    List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

    // Method 1: ListIterator.set()
    ListIterator<Integer> lit = list.listIterator();
    while (lit.hasNext()) {
        lit.set(lit.next() * 10);  // Replace each with ×10
    }
    // list = [10, 20, 30, 40, 50]

    // Method 2: Collections.replaceAll
    Collections.replaceAll(list, 10, 100);  // Replace all 10s with 100s

    // Method 3: setAll (Arrays)
    // Method 4: Stream + map + collect
```

### 10.3 Insert Element During Iteration

```java
    List<String> list = new ArrayList<>(Arrays.asList("A", "B", "D"));

    ListIterator<String> it = list.listIterator();
    while (it.hasNext()) {
        String s = it.next();
        if (s.equals("B")) {
            it.add("C");  // Insert "C" before "D"
        }
    }
    // list = ["A", "B", "C", "D"]
```

### 10.4 Iterate in Reverse

```java
    List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));

    // Method 1: ListIterator
    ListIterator<String> it = list.listIterator(list.size());
    while (it.hasPrevious()) {
        System.out.println(it.previous());  // C, B, A
    }

    // Method 2: Stream
    list.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);

    // Method 3: Collections.reverse
    Collections.reverse(list);
    list.forEach(System.out::println);
```

### 10.5 Find Index During Iteration

```java
    List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));

    ListIterator<String> it = list.listIterator();
    while (it.hasNext()) {
        int index = it.nextIndex();
        String value = it.next();
        System.out.println("Index: " + index + ", Value: " + value);
    }
    // Index: 0, Value: A
    // Index: 1, Value: B
    // Index: 2, Value: C
    // Index: 3, Value: D
```

### 10.6 Map Iteration with Iterator

```java
    Map<String, Integer> map = new HashMap<>();
    map.put("A", 1); map.put("B", 2); map.put("C", 3);

    // Entry Iterator
    Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry<String, Integer> entry = it.next();
        if (entry.getValue() == 2) it.remove();  // Safe removal!
    }

    // Key Iterator
    Iterator<String> keyIt = map.keySet().iterator();
    while (keyIt.hasNext()) {
        String key = keyIt.next();
        if (map.get(key) == 3) keyIt.remove();
    }
```

---

## 11. SPLITERATOR (JDK 8+)

```
    Spliterator = Splittable + Iterator

    Supports parallel traversal and bulk operations.
    Used internally by Stream API.

    ┌──────────────────────────────────────────────────────────────┐
    │  Spliterator Interface                                       │
    │──────────────────────────────────────────────────────────────│
    │  tryAdvance(Consumer) → boolean  (like Iterator.next())     │
    │  forEachRemaining(Consumer)       (bulk traversal)           │
    │  trySplit() → Spliterator         (divide for parallel)     │
    │  estimateSize() → long            (remaining elements)      │
    │  characteristics() → int          (collection properties)   │
    └──────────────────────────────────────────────────────────────┘

    Characteristics:
    ┌──────────────────────────────────────────────────────────────┐
    │  ORDERED     → elements have defined encounter order         │
    │  SIZED       → estimateSize() is accurate                    │
    │  SUBSIZED    → split parts also SIZED                        │
    │  DISTINCT    → no duplicate elements                         │
    │  SORTED      → elements in sorted order                      │
    │  IMMUTABLE   → no structural modification                    │
    │  NONNULL     → no null elements                              │
    │  CONCURRENT  → concurrent modification allowed               │
    │  UNMUTABLE   → elements cannot be modified                   │
    └──────────────────────────────────────────────────────────────┘

    Parallel Stream example:
    ┌──────────────────────────────────────────────────────────────┐
    │  list.parallelStream()                                       │
    │      → uses spliterator.trySplit() to divide data            │
    │      → each thread gets a portion                            │
    │      → results merged at end                                 │
    └──────────────────────────────────────────────────────────────┘
```

---

## 12. ITERATOR & LISTITERATOR - INTERVIEW QUESTIONS (40+)

### ⭐ BASIC LEVEL (Q1-Q15)

**Q1: What is Iterator?**
> Iterator is an interface for traversing a collection one element at a time. Provides hasNext(), next(), and remove() methods.

**Q2: What is difference between Iterator and ListIterator?**
```
Iterator:       Forward only, all collections, read + remove only
ListIterator:   Both directions, List only, read + remove + add + replace + index
```

**Q3: Can Iterator add elements?**
> No. Iterator can only remove (via remove()). ListIterator can add (via add()) and replace (via set()).

**Q4: Can Iterator traverse backward?**
> No. Iterator is forward-only. Use ListIterator for backward traversal.

**Q5: What is ConcurrentModificationException?**
> Thrown when collection is structurally modified during iteration. Fail-fast behavior via modCount check.

**Q6: How to avoid ConcurrentModificationException?**
```
1. Use Iterator.remove() instead of collection.remove()
2. Use removeIf() (JDK 8+)
3. Use CopyOnWriteArrayList (fail-safe)
4. Use Stream + collect
```

**Q7: What is fail-fast iterator?**
> Iterator that throws ConcurrentModificationException if collection is modified during iteration. Works via modCount mechanism.

**Q8: What is fail-safe iterator?**
> Iterator that works on a snapshot of the collection. Does NOT throw CME. Examples: CopyOnWriteArrayList, ConcurrentHashMap.

**Q9: What is modCount?**
> Internal variable in AbstractList. Incremented on structural modification (add/remove). Iterator checks this to detect concurrent modification.

**Q10: What does for-each loop compile to?**
```java
for (String s : list) { ... }
// Compiles to:
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    ...
}
```

**Q11: Can we modify collection in for-each loop?**
> No. for-each doesn't expose iterator reference. Use Iterator or removeIf() for safe modification.

**Q12: Can Iterator be reused after exception?**
> After CME: No. The iterator state is invalid. Create new iterator.
> After NoSuchElementException: No. Create new iterator.

**Q13: What happens if next() called without hasNext()?**
> Throws NoSuchElementException when no more elements exist.

**Q14: Can we call remove() twice in a row?**
> No. Second remove() throws IllegalStateException. Must call next() between removes.

**Q15: What is the difference between Iterator.remove() and Collection.remove()?**
```
Iterator.remove():    Safe during iteration, updates cursor, no CME
Collection.remove():  Structural modification, causes CME if iterating
```

---

### ⭐⭐ MIDDLE LEVEL (Q16-Q30)

**Q16: What is the internal working of Iterator?**
```
1. Iterator created → saves expectedModCount = modCount
2. next() called → checks modCount == expectedModCount
3. If not equal → throw ConcurrentModificationException
4. Returns element and advances cursor
5. remove() called → syncs modCount after removal
```

**Q17: How does ListIterator cursor work?**
```
Cursor is an index between elements:
- Position 0: before first element
- Position 1: between element 0 and element 1
- Position size: after last element

next() returns element at cursor, then advances cursor
previous() moves cursor backward, then returns element at cursor
```

**Q18: What is the difference between set() and add() in ListIterator?**
```
set(E):    Replaces element at current cursor position
           Requires prior next() or previous()
           Does not change cursor position
           Does not change list size

add(E):    Inserts element BEFORE current cursor position
           Can be called at any time
           Moves cursor right by 1
           Increases list size by 1
           Resets lastRet to -1
```

**Q19: How to iterate ArrayList in reverse order?**
```java
List<String> list = Arrays.asList("A", "B", "C");

// Method 1: ListIterator (most efficient)
ListIterator<String> it = list.listIterator(list.size());
while (it.hasPrevious()) {
    System.out.println(it.previous());
}

// Method 2: Stream
list.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);

// Method 3: Collections.reverse + for-each
List<String> reversed = new ArrayList<>(list);
Collections.reverse(reversed);
for (String s : reversed) System.out.println(s);
```

**Q20: What is the time complexity of Iterator operations?**
```
┌──────────────────┬──────────────┬──────────────┬──────────────┐
│ Operation         │ ArrayList    │ LinkedList   │ HashSet      │
├──────────────────┼──────────────┼──────────────┼──────────────┤
│ next()            │ O(1)         │ O(1)         │ O(1)*        │
│ remove()          │ O(n) shifting│ O(1)         │ O(1)         │
│ hasNext()         │ O(1)         │ O(1)         │ O(1)         │
└──────────────────┴──────────────┴──────────────┴──────────────┘
* Amortized O(1) for hash table iteration
ArrayList remove() during iteration: O(n) because shifting
LinkedList remove(): O(1) node removal
```

**Q21: What is difference between iterator() and listIterator()?**
```
iterator():
  - Returns Iterator<E>
  - Available on all Collection types
  - Forward only
  - No add/replace

listIterator():
  - Returns ListIterator<E>
  - Available on List only
  - Bidirectional
  - Has add/replace/set/index methods
```

**Q22: What is the starting index of ListIterator?**
```
listIterator():       Starts at index 0 (before first element)
listIterator(n):      Starts at index n (before element at index n)

list: [A, B, C]
listIterator(0): → cursor at 0, next() returns "A"
listIterator(2): → cursor at 2, next() returns "C"
```

**Q23: How Iterator handles index?**
```
Iterator has NO index concept internally.
It just traverses elements sequentially.

ListIterator has index:
  nextIndex() → returns index of element that next() would return
  previousIndex() → returns index of element that previous() would return
```

**Q24: Can we use Iterator with Set?**
> Yes. Set has iterator(). But ListIterator is List-only.

```java
Set<String> set = new HashSet<>(Arrays.asList("A", "B", "C"));
Iterator<String> it = set.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("B")) it.remove();  // Safe!
}
```

**Q25: Can we use Iterator with Map?**
> Yes, via entrySet(), keySet(), or values().

```java
Map<String, Integer> map = new HashMap<>();
map.put("A", 1); map.put("B", 2); map.put("C", 3);

Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
while (it.hasNext()) {
    Map.Entry<String, Integer> entry = it.next();
    if (entry.getValue() == 2) it.remove();  // Safe!
}
```

**Q26: What is the relationship between Iterator and Iterable?**
```
Iterable<E> {
    Iterator<E> iterator();          // Must implement
    void forEach(Consumer);          // Default
    Spliterator<E> spliterator();    // Default
}

Any class implementing Iterable can be used in for-each loop.
Collection extends Iterable → all collections are Iterable.
```

**Q27: How to create custom Iterator?**
```java
class Counter implements Iterable<Integer> {
    private int limit;

    public Counter(int limit) { this.limit = limit; }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int current = 0;

            @Override
            public boolean hasNext() { return current < limit; }

            @Override
            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                return current++;
            }
        };
    }
}

// Usage
for (int n : new Counter(5)) {
    System.out.println(n);  // 0, 1, 2, 3, 4
}
```

**Q28: What is Iterator.remove() vs ListIterator.remove()?**
```
Both are identical in behavior:
- Remove element last returned by next() or previous()
- Can only be called once per next()/previous()
- Safe during iteration (no CME)
```

**Q29: How does ListIterator.add() affect subsequent iteration?**
```
list: [A, B, C]
it = list.listIterator()
it.next() → "A"
it.add("X")

list: [A, X, B, C]
cursor = 2

Next it.next() → "B" (not "X")
"X" is skipped in forward traversal after add()

To visit "X":
it.previous() → "X"
```

**Q30: What is forEachRemaining() and when to use?**
```java
Iterator<Integer> it = list.iterator();
it.forEachRemaining(n -> {
    // Processes all remaining elements
    // More efficient than while loop in some implementations
    // Can only be called once (consumes all elements)
});
// After calling, hasNext() returns false
```

---

### ⭐⭐⭐ ADVANCED LEVEL (Q31-Q40)

**Q31: What is the Spliterator characteristics?**
```
Spliterator characteristics describe collection properties:

ORDERED:      Elements have defined encounter order
SIZED:        estimateSize() is accurate
SUBSIZED:     trySplit() parts are also SIZED
DISTINCT:     No duplicate elements
SORTED:       Elements are sorted
IMMUTABLE:    No structural modifications allowed
NONNULL:      No null elements
CONCURRENT:   Concurrent modification allowed
UNMUTABLE:    Elements cannot be modified

ArrayList: SIZED | SUBSIZED | ORDERED | NONNULL | IMMUTABLE
HashSet: SIZED | SUBSIZED | DISTINCT | NONNULL
TreeSet: SIZED | SUBSIZED | DISTINCT | SORTED | NONNULL
```

**Q32: How Spliterator enables parallel streams?**
```
list.parallelStream()
    → collection.spliterator()
    → spliterator.trySplit() divides data
    → Each thread processes a chunk
    → Results combined

Example: 8 elements, 4 threads
┌──────────────────────────────────────────┐
│ Spliterator splits:                       │
│ [1,2,3,4,5,6,7,8]                        │
│   ↓                                      │
│ [1,2] [3,4] [5,6] [7,8]                 │
│   ↓     ↓     ↓     ↓                    │
│ T1     T2    T3     T4                   │
│   ↓     ↓     ↓     ↓                    │
│   └─────┴─────┴─────┘                    │
│         ↓                                │
│    Combined result                        │
└──────────────────────────────────────────┘
```

**Q33: What is the difference between Iterator and Iterable?**
```
Iterator:  Traversal mechanism (hasNext, next, remove)
Iterable:  Provides iterator() method, enables for-each loop

A collection is Iterable → provides iterator()
Iterator is the tool used to traverse
```

**Q34: Can we implement both Iterable and Iterator?**
```java
class NumberRange implements Iterable<Integer>, Iterator<Integer> {
    int current, max;

    public NumberRange(int max) { this.max = max; }

    // Iterator methods
    public boolean hasNext() { return current < max; }
    public Integer next() { return current++; }
    public void remove() { /* ... */ }

    // Iterable method
    public Iterator<Integer> iterator() { return this; }
}

// Usage
NumberRange range = new NumberRange(5);
for (int n : range) System.out.println(n);  // 0, 1, 2, 3, 4
```

**Q35: What is the difference between iterator.remove() and Collection.removeIf()?**
```
iterator.remove():
  - Manual: must call next() before remove()
  - Works on one element at a time
  - Available since JDK 1.2

removeIf():
  - Declarative: pass predicate
  - Handles all elements in one call
  - JDK 8+
  - May be optimized internally (batch removal)
  - Preferred for simplicity
```

**Q36: How to detect cycle in linked list using Iterator?**
```java
// Floyd's cycle detection (two pointers)
public boolean hasCycle(ListNode head) {
    ListNode slow = head, fast = head;
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
        if (slow == fast) return true;
    }
    return false;
}
// Iterator can't detect cycles directly - this is the algorithm
```

**Q37: What is the difference between Iterator and Stream?**
```
┌──────────────────┬──────────────────┬──────────────────┐
│ Feature           │ Iterator         │ Stream           │
├──────────────────┼──────────────────┼──────────────────┤
│ Direction         │ Forward only     │ Forward only     │
│ Parallel          │ No               │ Yes              │
│ Lazy              │ No               │ Yes              │
│ Pipelining        │ No               │ Yes (map, filter)│
│ Reusable          │ Depends          │ No (single use)  │
│ Internal/External │ External         │ Internal         │
│ Modification      │ remove() only    │ None             │
│ Functional        │ No               │ Yes              │
└──────────────────┴──────────────────┴──────────────────┘
```

**Q38: What is the use of Spliterator characteristics()?**
```java
Spliterator<String> spliterator = list.spliterator();

int chars = spliterator.characteristics();

if ((chars & Spliterator.SIZED) != 0) {
    System.out.println("Size is known: " + spliterator.estimateSize());
}
if ((chars & Spliterator.ORDERED) != 0) {
    System.out.println("Encounter order is defined");
}
if ((chars & Spliterator.CONCURRENT) != 0) {
    System.out.println("Concurrent modifications allowed");
}
```

**Q39: How does ArrayList Iterator handle resizing?**
```
If ArrayList resizes during iteration:
1. Array is replaced with new larger array
2. Iterator still holds reference to OLD array
3. Iterator continues on old array → no CME (modCount not changed by resize)
4. But: if someone calls list.add() → modCount changes → CME on next iteration

Resize is NOT a structural modification from Iterator's perspective.
```

**Q40: What is the practical scenario for Iterator vs ListIterator?**
```
┌───────────────────────────────┬──────────────────────────────────┐
│ Scenario                       │ Use                              │
├───────────────────────────────┼──────────────────────────────────┤
│ Read-only traversal            │ Iterator or for-each             │
│ Remove during iteration        │ Iterator.remove() or removeIf()  │
│ Replace during iteration       │ ListIterator.set()               │
│ Insert during iteration        │ ListIterator.add()               │
│ Reverse traversal              │ ListIterator (previous())        │
│ Get index during iteration     │ ListIterator (nextIndex())       │
│ Map entry removal              │ Iterator on entrySet             │
│ Thread-safe traversal          │ CopyOnWriteArrayList Iterator    │
│ Parallel processing            │ Spliterator + Stream             │
│ Simple for-each                │ for-each loop (compiles to Iterator)│
└───────────────────────────────┴──────────────────────────────────┘
```

---

## 13. QUICK REFERENCE CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║             ITERATOR & LISTITERATOR CHEAT SHEET                   ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  Iterator:       Forward-only, all collections                   ║
║  ListIterator:   Bidirectional, List only, +add/set/index       ║
║                                                                  ║
║  Iterator Methods:                                               ║
║    hasNext()     → boolean (check if more elements)              ║
║    next()        → E (return element, advance cursor)            ║
║    remove()      → void (remove last returned element)           ║
║    forEachRemaining(Consumer) → JDK 8+ bulk operation            ║
║                                                                  ║
║  ListIterator Methods (extends Iterator):                        ║
║    hasPrevious() → boolean                                       ║
║    previous()    → E (return element, move cursor back)          ║
║    nextIndex()   → int (index of next element)                   ║
║    previousIndex()→ int (index of previous element)              ║
║    set(E e)      → void (replace last returned element)          ║
║    add(E e)      → void (insert before cursor)                   ║
║                                                                  ║
║  Fail-Fast:   ArrayList, HashMap, HashSet → throws CME           ║
║  Fail-Safe:   CopyOnWriteArrayList, ConcurrentHashMap → no CME  ║
║                                                                  ║
║  CME = ConcurrentModificationException (modCount mismatch)       ║
║  Safe removal: Iterator.remove() or removeIf() (JDK 8+)         ║
║                                                                  ║
║  for-each compiles to Iterator internally                        ║
║  ListIterator cursor: position between elements (0 to size)      ║
║  Spliterator: parallel traversal, used by Stream API             ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: Iterator, ListIterator, Spliterator, Fail-Fast/Fail-Safe, Interview Questions*
