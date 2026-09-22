# Java Collections Framework - SET (Complete Interview Notes)

---

## 1. SET INTERFACE HIERARCHY (UML)

```
                     <<interface>>
                     ┌──────────────────┐
                     │      Iterable    │
                     └────────┬─────────┘
                              │
                     ┌────────▼─────────┐
                     │    Collection    │
                     └────────┬─────────┘
                              │
                     ┌────────▼─────────┐
                     │       Set        │
                     │──────────────────│
                     │ add(E)           │
                     │ remove(Object)   │
                     │ contains(Object) │
                     │ size()           │
                     │ isEmpty()        │
                     │ iterator()       │
                     │ clear()          │
                     │ toArray()        │
                     │ stream()         │
                     └────────┬─────────┘
                              │
         ┌────────────────────┼──────────────────────┐
         │                    │                      │
         ▼                    ▼                      ▼
  ┌─────────────┐    ┌──────────────┐     ┌──────────────────┐
  │  AbstractSet │    │ SortedSet    │     │  NavigableSet    │
  │  (abstract)  │    │ (interface)  │     │  (interface)     │
  └──────┬──────┘    └──────┬───────┘     └────────┬─────────┘
         │                  │                      │
         │                  ▼                      │
         │           ┌──────────────┐             │
         │           │  TreeSet     │◄────────────┘
         │           │ (Red-Black)  │
         │           └──────────────┘
         │
         ▼
  ┌─────────────────────────────────────────┐
  │            HashSet                      │
  │   (HashMap backed, DEFAULT CHOICE)      │
  │   NOT thread-safe | No ordering         │
  └──────────────────┬──────────────────────┘
                     │
                     ▼
  ┌─────────────────────────────────────────┐
  │        LinkedHashSet                     │
  │   (LinkedHashMap backed)                │
  │   Maintains INSERTION order             │
  └─────────────────────────────────────────┘


  ┌─────────────────────────────────────────┐
  │        CopyOnWriteArraySet              │
  │   (CopyOnWriteArrayList backed)        │
  │   Thread-safe | Read-heavy              │
  └─────────────────────────────────────────┘

  ┌─────────────────────────────────────────┐
  │        EnumSet                          │
  │   (Bit-vector backed)                  │
  │   All enum elements | Fastest           │
  └─────────────────────────────────────────┘

  ┌─────────────────────────────────────────┐
  │        ImmutableSet (Google Guava)      │
  │        Set.of() (Java 9+)              │
  └─────────────────────────────────────────┘
```

---

## 2. SET INTERFACE - ALL METHODS

```java
public interface Set<E> extends Collection<E> {

    // === ADD ===
    boolean add(E e);                    // Add element, false if already exists
    boolean addAll(Collection<? extends E> c);  // Add all from collection

    // === REMOVE ===
    boolean remove(Object o);            // Remove specific element
    boolean removeAll(Collection<?> c);  // Remove all matching elements
    boolean removeIf(Predicate<? super E> filter);  // JDK 8+ conditional remove

    // === CHECK ===
    boolean contains(Object o);
    boolean containsAll(Collection<?> c);
    boolean isEmpty();
    int size();

    // === RETRIEVE ===
    Iterator<E> iterator();
    Object[] toArray();
    <T> T[] toArray(T[] a);

    // === UTILITY ===
    void clear();
    int hashCode();
    boolean equals(Object o);

    // === JDK 8+ DEFAULT METHODS ===
    Stream<E> stream();
    Stream<E> parallelStream();
    void forEach(Consumer<? super E> action);
}
```

---

## 3. HASHSET - INTERNAL WORKING (MOST ASKED)

### 3.1 Internal Structure

```
HashSet Internals: Backed by HashMap!

HashSet<String> set = new HashSet<>();
Internally creates: HashMap<String, Boolean> with value = PRESENT (static dummy)

    ┌─────────────────────────────────────────────────────────────┐
    │  HashSet = HashMap<String, Boolean>                        │
    │                                                             │
    │  HashMap<Integer, Object> table (Node[])                    │
    │                                                             │
    │  ┌──────────────────────────────────────────────────────┐   │
    │  │  [0]  → null                                         │   │
    │  │  [1]  → null                                         │   │
    │  │  [2]  → Node("Amit" → PRESENT) → null               │   │
    │  │  [3]  → Node("Rahul" → PRESENT) → null              │   │
    │  │  [4]  → null                                         │   │
    │  │  ...                                                 │   │
    │  │  [12] → Node("Suresh" → PRESENT) → null             │   │
    │  │  [15] → null                                         │   │
    │  └──────────────────────────────────────────────────────┘   │
    │                                                             │
    │  size()   → map.size()                                     │
    │  add(e)   → map.put(e, PRESENT) == null                    │
    │  remove(e)→ map.remove(e) == PRESENT                       │
    │  contains(e)→ map.containsKey(e)                           │
    └─────────────────────────────────────────────────────────────┘
```

### 3.2 Source Code Connection

```java
// HashSet source (simplified)
public class HashSet<E> extends AbstractSet<E> implements Set<E> {

    private transient HashMap<E, Object> map;
    private static final Object PRESENT = new Object();  // Dummy value

    public HashSet() {
        map = new HashMap<>();  // Creates HashMap internally
    }

    public boolean add(E e) {
        return map.put(e, PRESENT) == null;  // Returns null if new entry
    }

    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public int size() {
        return map.size();
    }
}
```

### 3.3 add() Operation Flow

```
    set.add("Amit")

    Step 1: Compute hash
    ┌─────────────────────────────────────────┐
    │  hash("Amit")                           │
    │  = key.hashCode() ^ (h >>> 16)          │
    └──────────────────┬──────────────────────┘
                       │
    Step 2: Find bucket index
    ┌─────────────────────────────────────────┐
    │  index = hash & (table.length - 1)      │
    └──────────────────┬──────────────────────┘
                       │
    Step 3: Check bucket
    ┌─────────────────────────────────────────┐
    │  if (table[index] == null)              │
    │      → CREATE node, return true         │
    │  else                                   │
    │      → TRAVERSE chain/tree              │
    │        if (e.hash == hash &&            │
    │            (e.key == key ||             │
    │             key.equals(e.key)))         │
    │            → REPLACE, return false      │
    │        else                             │
    │            → ADD to end (or treeify)    │
    │            → return true                │
    └──────────────────┬──────────────────────┘
                       │
    Step 4: Check resize
    ┌─────────────────────────────────────────┐
    │  if (size > threshold)                  │
    │      → resize() → double the array     │
    └─────────────────────────────────────────┘
```

### 3.4 How Duplicates Are Prevented

```
    HashSet<Integer> set = new HashSet<>();
    set.add(10);  // bucket[2] → Node(10) → null    → returns TRUE
    set.add(10);  // bucket[2] → Node(10) → null
    │                ↑
    │                Found! 10.equals(10) → true
    │                → Returns false, no new entry
    │
    └── set.size() = 1  (duplicate rejected!)

    Key: add() returns false if element already exists
```

---

## 4. LINKEDHASHSET - ORDERED SET

```
LinkedHashSet: LinkedHashMap + Doubly Linked List

Maintains INSERTION ORDER

    HashSet<String> hs = new HashSet<>();
    hs.add("C"); hs.add("A"); hs.add("B");
    System.out.println(hs);  // [A, B, C] (no order)

    LinkedHashSet<String> lhs = new LinkedHashSet<>();
    lhs.add("C"); lhs.add("A"); lhs.add("B");
    System.out.println(lhs);  // [C, A, B] (insertion order!)

Internal Structure:
    ┌──────────────────────────────────────────────────┐
    │  LinkedHashMap<K, Boolean> (accessOrder=false)   │
    │                                                  │
    │  Bucket Array:                                   │
    │  [2] → Node("C", PRESENT)                       │
    │  [5] → Node("A", PRESENT)                       │
    │  [8] → Node("B", PRESENT)                       │
    │                                                  │
    │  Doubly Linked List (INSERTION ORDER):           │
    │  head ⇄ "C" ⇄ "A" ⇄ "B" ⇄ tail               │
    └──────────────────────────────────────────────────┘

    Traversal follows the linked list, not the bucket array.
```

---

## 5. TREESET - SORTED SET (Red-Black Tree)

### 5.1 Internal Structure

```
TreeSet<Integer> set = new TreeSet<>();
set.add(50);
set.add(30);
set.add(70);
set.add(20);
set.add(40);

Internal: TreeMap<Integer, Boolean> (Red-Black Tree)

                    ┌─────────┐
                    │    50   │ (BLACK)
                    └────┬────┘
                   ┌─────┴──────┐
              ┌────▼────┐  ┌────▼────┐
              │   30    │  │   70    │  (BLACK)
              └────┬────┘  └─────────┘
             ┌─────┴─────┐
        ┌────▼────┐ ┌────▼────┐
        │   20    │ │   40    │  (RED)
        └─────────┘ └─────────┘

    In-Order Traversal → 20, 30, 40, 50, 70 (SORTED!)
```

### 5.2 TreeSet Operations

```java
TreeSet<Integer> ts = new TreeSet<>();
ts.add(50); ts.add(30); ts.add(70); ts.add(20); ts.add(40);

// Navigation Methods
ts.first();              // 20 (lowest)
ts.last();               // 70 (highest)
ts.lower(40);            // 30 (just below 40)
ts.higher(40);           // 50 (just above 40)
ts.floor(40);            // 40 (≤ 40)
ts.ceiling(40);          // 40 (≥ 40)
ts.headSet(50);          // [20, 30, 40] (< 50)
ts.tailSet(50);          // [50, 70] (≥ 50)
ts.subSet(30, 70);       // [30, 40, 50] (30 ≤ x < 70)
ts.subSet(30, true, 70, true); // [30, 40, 50, 70]
```

### 5.3 NavigableSet Interface (Exclusive to TreeSet)

```
    ┌──────────────────────────────────────────────────────────────┐
    │ Method            │ Description                │ Time        │
    ├──────────────────────────────────────────────────────────────┤
    │ first()           │ Lowest element             │ O(1)        │
    │ last()            │ Highest element            │ O(1)        │
    │ lower(e)          │ Largest element < e        │ O(log n)    │
    │ higher(e)         │ Smallest element > e       │ O(log n)    │
    │ floor(e)          │ Largest element ≤ e        │ O(log n)    │
    │ ceiling(e)        │ Smallest element ≥ e       │ O(log n)    │
    │ headSet(e)        │ Elements < e               │ O(log n)    │
    │ tailSet(e)        │ Elements ≥ e               │ O(log n)    │
    │ subSet(a,b)       │ Elements in [a, b)         │ O(log n)    │
    │ descendingSet()   │ Reverse sorted set         │ O(n)        │
    │ pollFirst()       │ Remove & return lowest     │ O(log n)    │
    │ pollLast()        │ Remove & return highest    │ O(log n)    │
    └──────────────────────────────────────────────────────────────┘
```

---

## 6. ENUMSET - BIT-VECTOR BACKED

```java
enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }

EnumSet<Day> weekend = EnumSet.of(SAT, SUN);
EnumSet<Day> weekdays = EnumSet.range(MON, FRI);
EnumSet<Day> all = EnumSet.allOf(Day.class);
EnumSet<Day> none = EnumSet.noneOf(Day.class);
EnumSet<Day> complement = EnumSet.complementOf(weekend);
```

```
Internal Representation (Bit Vector):
    ┌────────────────────────────────────────────────────────────┐
    │ Element:  MON   TUE   WED   THU   FRI   SAT   SUN         │
    │ Bit:       0     1     2     3     4     5     6           │
    │                                                            │
    │ weekend = EnumSet.of(SAT, SUN)                             │
    │ Binary: 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0  │
    │                  ↑     ↑                                    │
    │                 bit5  bit6  → Set                           │
    │                                                            │
    │ Memory: Only 1 long needed (7 enums)                       │
    │ Operations: Bitwise AND, OR, NOT (CPU native, fastest!)    │
    └────────────────────────────────────────────────────────────┘

    Performance: O(1) for contains, add, remove
    Memory: Incredibly compact (1 long for ≤ 64 enums)
```

---

## 7. COPYONWRITEARRAYSET

```java
CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<>();
set.add("A");
set.add("B");

// Internally backed by CopyOnWriteArrayList
// Every write (add/remove) copies the entire array
// Reads are lock-free (iterate over snapshot)

// ┌──────────────────────────────────────────────────────┐
// │  Thread A (Read):                                    │
// │  Iterator iterates over array reference → No lock     │
// │                                                      │
// │  Thread B (Write):                                   │
// │  add("C") → Lock → Copy entire array → Modify copy   │
// │            → Replace reference → Unlock               │
// │                                                      │
// │  Thread C (Read):                                    │
// │  Sees OLD array (snapshot before Thread B's write)    │
// └──────────────────────────────────────────────────────┘
```

---

## 8. SET vs COLLECTION vs MAP

```
    ┌─────────────────┬───────────────────────────────────────────────┐
    │ Feature          │ Set vs List vs Map                           │
    ├─────────────────┼───────────────────────────────────────────────┤
    │ Structure        │ Set: Unique elements only                    │
    │                  │ List: Ordered, allows duplicates             │
    │                  │ Map: Key-Value pairs                         │
    ├─────────────────┼───────────────────────────────────────────────┤
    │ Duplicates       │ Set: NO                                     │
    │                  │ List: YES                                   │
    │                  │ Map: Keys NO, Values YES                    │
    ├─────────────────┼───────────────────────────────────────────────┤
    │ Order            │ Set: Depends on implementation              │
    │                  │ List: INSERTION ORDER guaranteed            │
    │                  │ Map: Depends on implementation              │
    ├─────────────────┼───────────────────────────────────────────────┤
    │ Null             │ Set: 1 null (HashSet/LinkedHashSet)         │
    │                  │ List: Multiple nulls                        │
    │                  │ Map: 1 null key (HashMap)                   │
    ├─────────────────┼───────────────────────────────────────────────┤
    │ Indexed access   │ Set: NO get(index)                          │
    │                  │ List: YES get(index)                        │
    │                  │ Map: NO (use key)                           │
    ├─────────────────┼───────────────────────────────────────────────┤
    │ Internal backup  │ HashSet → HashMap                            │
    │                  │ TreeSet → TreeMap                            │
    │                  │ LinkedHashSet → LinkedHashMap                │
    └─────────────────┴───────────────────────────────────────────────┘
```

---

## 9. ALL SET IMPLEMENTATIONS COMPARISON

```
    ┌──────────────────┬──────────────┬──────────────┬──────────────┬──────────────────┐
    │ Feature           │ HashSet      │ LinkedHashSet│ TreeSet      │ EnumSet          │
    ├──────────────────┼──────────────┼──────────────┼──────────────┼──────────────────┤
    │ Backed by         │ HashMap      │ LHM          │ TreeMap      │ Bit Vector       │
    │ Order             │ None         │ Insertion    │ Sorted       │ Enum ordinal     │
    │ Null elements     │ 1 allowed    │ 1 allowed    │ NOT allowed* │ NOT allowed      │
    │ Duplicates        │ NO           │ NO           │ NO           │ NO               │
    │ Time: add         │ O(1)         │ O(1)         │ O(log n)     │ O(1)             │
    │ Time: contains    │ O(1)         │ O(1)         │ O(log n)     │ O(1)             │
    │ Time: remove      │ O(1)         │ O(1)         │ O(log n)     │ O(1)             │
    │ Thread-safe       │ No           │ No           │ No           │ No               │
    │ Memory            │ Moderate     │ Higher       │ Higher       │ Minimal          │
    │ Null allowed      │ 1 null       │ 1 null       │ NO           │ NO               │
    │ Best for          │ Fast lookup  │ Insert order │ Sorted data  │ Enum elements    │
    └──────────────────┴──────────────┴──────────────┴──────────────┴──────────────────┘

    * TreeSet allows 1 null IF comparator handles nulls
```

---

## 10. JAVA 9+ IMMUTABLE SETS

```java
// Factory methods
Set<String> set1 = Set.of("A", "B", "C");           // Fixed size
Set<Integer> set2 = Set.of();                          // Empty set
Set<String> set3 = Set.of("A", "B", "C", "D", "E"); // Up to 10

// Copy existing
Set<String> original = new HashSet<>();
Set<String> copy = Set.copyOf(original);              // Unmodifiable copy

// From array
Set<int[]> set4 = Set.of(new int[]{1, 2, 3});        // Contains ONE int[] element

// Characteristics:
// - Fixed size (add/remove throws UnsupportedOperationException)
// - No null elements
// - Serializable
// - Disallows duplicate elements at creation time
```

---

## 11. HASHING IN SET (Why hashCode() Matters)

```
    Without proper hashCode():
    ┌──────────────────────────────────────────────────────────┐
    │  class Student {                                         │
    │      String name;                                        │
    │      int roll;                                           │
    │      // No hashCode() or equals() override!              │
    │  }                                                       │
    │                                                          │
    │  Set<Student> set = new HashSet<>();                     │
    │  Student s1 = new Student("Amit", 1);                   │
    │  Student s2 = new Student("Amit", 1);                   │
    │                                                          │
    │  set.add(s1);                                            │
    │  set.add(s2);                                            │
    │  System.out.println(set.size());  // 2! (WRONG - both are same student) │
    └──────────────────────────────────────────────────────────┘

    With proper hashCode() and equals():
    ┌──────────────────────────────────────────────────────────┐
    │  class Student {                                         │
    │      String name;                                        │
    │      int roll;                                           │
    │                                                          │
    │      @Override                                           │
    │      public boolean equals(Object o) {                   │
    │          if (this == o) return true;                     │
    │          if (!(o instanceof Student)) return false;      │
    │          Student s = (Student) o;                        │
    │          return this.roll == s.roll;                     │
    │      }                                                   │
    │                                                          │
    │      @Override                                           │
    │      public int hashCode() {                             │
    │          return Objects.hash(roll);                      │
    │      }                                                   │
    │  }                                                       │
    │                                                          │
    │  set.add(s1); set.add(s2);                              │
    │  System.out.println(set.size());  // 1! (CORRECT)       │
    └──────────────────────────────────────────────────────────┘
```

---

## 12. SET - ALL INTERVIEW QUESTIONS (50+)

### ⭐ BASIC LEVEL (Q1-Q15)

**Q1: What is Set in Java?**
> Set is a Collection that stores unique elements. No duplicates allowed. Implements mathematical set concept.

**Q2: What are the main implementations of Set?**
> HashSet, LinkedHashSet, TreeSet, EnumSet, CopyOnWriteArraySet, ConcurrentSkipListSet

**Q3: Can Set store duplicate elements?**
> No. add() returns false if element already exists. Internally uses equals() and hashCode() to check.

**Q4: Can Set store null elements?**
```
┌────────────────────┬──────────────┐
│ Implementation      │ Null Allowed │
├────────────────────┼──────────────┤
│ HashSet            │ 1 null       │
│ LinkedHashSet      │ 1 null       │
│ TreeSet            │ Depends*     │
│ EnumSet            │ NO           │
│ CopyOnWriteArraySet│ 1 null       │
│ ConcurrentSkipListSet │ NO        │
└────────────────────┴──────────────┘
* TreeSet: null allowed only if Comparator handles nulls
```

**Q5: Difference between Set and List?**
```
┌──────────────┬──────────────────────┬──────────────────────┐
│ Feature       │ Set                  │ List                 │
├──────────────┼──────────────────────┼──────────────────────┤
│ Duplicates   │ NO                   │ YES                  │
│ Order        │ Depends              │ INSERTION guaranteed │
│ Null         │ 1 (HashSet)          │ Multiple             │
│ Indexed      │ NO get(index)        │ YES get(index)       │
│ add(index,e) │ NO                   │ YES                  │
│ SubList      │ NO                   │ YES                  │
└──────────────┴──────────────────────┴──────────────────────┘
```

**Q6: What is HashSet internally?**
> HashSet is backed by HashMap. Elements are stored as keys with a dummy value PRESENT. add() calls map.put(e, PRESENT).

**Q7: Why HashSet uses HashMap?**
> Code reusability. HashMap already handles hashing, collision resolution, and resizing. Set just needs uniqueness (duplicate keys → rejected by HashMap).

**Q8: What is difference between HashSet and LinkedHashSet?**
```
HashSet:           No order, fastest
LinkedHashSet:     Maintains insertion order, slightly slower
```

**Q9: What is difference between HashSet and TreeSet?**
```
HashSet:           O(1) operations, no order, allows null
TreeSet:           O(log n) operations, sorted order, no null*
TreeSet uses Red-Black tree.
```

**Q10: How does Set prevent duplicates?**
```
1. hashCode() → find bucket
2. equals() → compare with existing elements in bucket
3. If equals() returns true → NOT added, add() returns false
```

**Q11: What is the default capacity of HashSet?**
> 16 (same as HashMap). Load factor 0.75. Threshold 12.

**Q12: What happens when you add null to HashSet?**
> First null goes in. Second null: hashCode() same, equals() true → rejected. Only 1 null allowed.

**Q13: Can elements be retrieved by index from Set?**
> No. Set doesn't support indexed access. Use iterator or convert to array/List.

**Q14: What is the time complexity of HashSet operations?**
```
┌──────────────────┬───────────────┬────────────────┐
│ Operation         │ Average       │ Worst (Tree)   │
├──────────────────┼───────────────┼────────────────┤
│ add()             │ O(1)          │ O(log n)       │
│ remove()          │ O(1)          │ O(log n)       │
│ contains()        │ O(1)          │ O(log n)       │
│ size()            │ O(1)          │ O(1)           │
│ iterator()        │ O(n)          │ O(n)           │
└──────────────────┴───────────────┴────────────────┘
```

**Q15: What is fail-fast iterator in Set?**
> If Set is structurally modified during iteration, iterator throws ConcurrentModificationException. Works via modCount.

---

### ⭐⭐ MIDDLE LEVEL (Q16-Q35)

**Q16: How HashSet internally works (detailed)?**
```
add(element):
1. Compute hash = element.hashCode() ^ (h >>> 16)
2. Index = hash & (table.length - 1)
3. If bucket empty → add node, return true
4. If bucket has elements:
   a. Compare via hash && equals()
   b. If match found → replace (returns false)
   c. If no match → add to tail (treeify if ≥ 8)
5. If size > threshold → resize
```

**Q17: What is difference between HashSet and HashMap?**
```
┌──────────────┬──────────────────────┬──────────────────────┐
│ Feature       │ HashSet              │ HashMap              │
├──────────────┼──────────────────────┼──────────────────────┤
│ Interface     │ Set                  │ Map                  │
│ Elements      │ Single values        │ Key-Value pairs      │
│ add/put       │ add(E)               │ put(K, V)            │
│ Internal      │ HashMap<K, Boolean>  │ HashMap<K, V>        │
│ Values        │ All same PRESENT     │ Any value            │
│ Iteration     │ element only         │ key, value, entry    │
└──────────────┴──────────────────────┴──────────────────────┘
```

**Q18: Can we create a Set from an array?**
```java
String[] arr = {"A", "B", "C"};

// JDK 9+
Set<String> set = Set.of(arr);

// Stream
Set<String> set2 = Arrays.stream(arr).collect(Collectors.toSet());

// HashSet
Set<String> set3 = new HashSet<>(Arrays.asList(arr));

// Set.copyOf
Set<String> set4 = Set.copyOf(Arrays.asList(arr));
```

**Q19: How to sort a Set?**
```java
// HashSet → no order
// LinkedHashSet → insertion order
// TreeSet → sorted!

Set<Integer> sorted = new TreeSet<>(originalSet);

// Or using stream
Set<Integer> sorted2 = originalSet.stream()
    .sorted()
    .collect(Collectors.toCollection(TreeSet::new));
```

**Q20: How to find common elements between two Sets?**
```java
Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));

// Method 1: retainAll
Set<Integer> common = new HashSet<>(set1);
common.retainAll(set2);  // {3, 4}

// Method 2: Stream
Set<Integer> common2 = set1.stream()
    .filter(set2::contains)
    .collect(Collectors.toSet());
```

**Q21: How to find difference between two Sets?**
```java
Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));

// Method 1: removeAll
Set<Integer> diff = new HashSet<>(set1);
diff.removeAll(set2);  // {1, 2}

// Method 2: Stream
Set<Integer> diff2 = set1.stream()
    .filter(e -> !set2.contains(e))
    .collect(Collectors.toSet());
```

**Q22: How to union of two Sets?**
```java
Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3));
Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5));

// Method 1: addAll
Set<Integer> union = new HashSet<>(set1);
union.addAll(set2);  // {1, 2, 3, 4, 5}

// Method 2: Stream
Set<Integer> union2 = Stream.concat(set1.stream(), set2.stream())
    .collect(Collectors.toSet());
```

**Q23: How to iterate a Set?**
```java
Set<String> set = new HashSet<>(Arrays.asList("A", "B", "C"));

// Method 1: For-each
for (String s : set) { ... }

// Method 2: Iterator
Iterator<String> it = set.iterator();
while (it.hasNext()) { it.next(); }

// Method 3: forEach (JDK 8+)
set.forEach(s -> System.out.println(s));

// Method 4: Stream
set.stream().forEach(System.out::println);
```

**Q24: Can we convert Set to List?**
```java
Set<String> set = new HashSet<>(Arrays.asList("A", "B", "C"));

// Method 1: Constructor
List<String> list = new ArrayList<>(set);

// Method 2: Stream
List<String> list2 = set.stream().collect(Collectors.toList());

// Method 3: List.copyOf (JDK 10+)
List<String> list3 = List.copyOf(set);
```

**Q25: What is ConcurrentModificationException?**
```java
Set<String> set = new HashSet<>(Arrays.asList("A", "B", "C"));

// ❌ WRONG
for (String s : set) {
    if (s.equals("B")) set.remove(s);  // CME!
}

// ✅ CORRECT - Iterator
Iterator<String> it = set.iterator();
while (it.hasNext()) {
    if (it.next().equals("B")) it.remove();
}

// ✅ CORRECT - removeIf (JDK 8+)
set.removeIf(s -> s.equals("B"));
```

**Q26: What is the difference between contains() and equals()?**
```
contains(o): Internally uses o.hashCode() to find bucket, then o.equals() to compare
equals(otherSet): Compares ALL elements of two sets (size + all elements match)
```

**Q27: Can we create a thread-safe Set?**
```java
// Option 1: ConcurrentHashMap.newKeySet()
Set<String> set = ConcurrentHashMap.newKeySet();

// Option 2: Collections.synchronizedSet
Set<String> set = Collections.synchronizedSet(new HashSet<>());

// Option 3: CopyOnWriteArraySet (read-heavy)
Set<String> set = new CopyOnWriteArraySet<>();

// Option 4: ConcurrentSkipListSet (sorted, concurrent)
Set<String> set = new ConcurrentSkipListSet<>();
```

**Q28: What is CopyOnWriteArraySet and when to use?**
> Creates a fresh copy of the underlying array on every write. Read operations are lock-free. Best for read-heavy, write-rare scenarios like listener lists.

**Q29: What is ConcurrentSkipListSet?**
> Concurrent version of TreeSet. Uses Skip List data structure (not Red-Black Tree). O(log n) operations. Thread-safe and sorted.

**Q30: What is EnumSet advantages?**
```
1. Memory efficient (bit vector, 1 long for ≤ 64 enums)
2. Fastest Set implementation
3. Type-safe (only specified enum type)
4. All operations O(1)
5. No hash collisions
```

**Q31: How to remove duplicates from a List?**
```java
List<Integer> list = Arrays.asList(1, 2, 2, 3, 3, 3);

// Method 1: HashSet (loses order)
List<Integer> unique = new ArrayList<>(new HashSet<>(list));

// Method 2: LinkedHashSet (preserves order)
List<Integer> unique2 = new ArrayList<>(new LinkedHashSet<>(list));

// Method 3: Stream
List<Integer> unique3 = list.stream().distinct().collect(Collectors.toList());
```

**Q32: What is difference between HashSet and LinkedHashSet performance?**
```
HashSet:        Slightly faster (no linked list maintenance)
LinkedHashSet:  ~5-10% slower (maintains doubly linked list for order)
Use LinkedHashSet only when order matters.
```

**Q33: What happens if you add duplicate to TreeSet with Comparator?**
```java
TreeSet<String> ts = new TreeSet<>(Comparator.naturalOrder());
ts.add("Apple");
ts.add("Apple");  // Returns false, not added
ts.size();  // 1
// TreeSet uses compareTo() (or Comparator) instead of equals()
```

**Q34: Can TreeSet have null elements?**
```java
// Default: NO (null throws NullPointerException on compareTo)
TreeSet<String> ts = new TreeSet<>();
ts.add(null);  // NPE!

// With custom Comparator that handles null: YES
TreeSet<String> ts2 = new TreeSet<>(Comparator.nullsFirst(Comparator.naturalOrder()));
ts2.add(null);  // Works!
```

**Q35: What is the difference between Set and Map in terms of internal structure?**
```
Set uses Map internally!
HashSet      = HashMap<E, PRESENT>
LinkedHashSet = LinkedHashMap<E, PRESENT>
TreeSet       = TreeMap<E, PRESENT>
EnumSet       = Bit vector (custom)
```

---

### ⭐⭐⭐ ADVANCED LEVEL (Q36-Q50)

**Q36: What is the time complexity of TreeSet operations?**
```
┌──────────────────┬───────────────┐
│ Operation         │ Complexity    │
├──────────────────┼───────────────┤
│ add()             │ O(log n)      │
│ remove()          │ O(log n)      │
│ contains()        │ O(log n)      │
│ first() / last()  │ O(1)*         │
│ lower() / higher()│ O(log n)      │
│ headSet()         │ O(log n)      │
│ tailSet()         │ O(log n)      │
│ size()            │ O(1)          │
│ iterator()        │ O(n)          │
└──────────────────┴───────────────┘
* first() is O(1) because TreeMap tracks the leftmost node
```

**Q37: How does TreeSet maintain sorted order?**
```
1. Uses TreeMap (Red-Black Tree) internally
2. Compares elements using:
   a. Comparable (natural ordering) - if element implements Comparable
   b. Comparator (custom ordering) - if provided in constructor
3. In-order traversal of Red-Black tree gives sorted sequence
```

**Q38: What is the contract between hashCode() and equals() in Set?**
```
Rule 1: If equals() → true, hashCode() MUST be same
Rule 2: If hashCode() same, equals() CAN be false (collision)
Rule 3: If equals() → false, hashCode() SHOULD be different

Violation example:
class Student {
    int id;
    // Missing hashCode/equals → uses Object defaults (memory address)
    // Two students with same id → different hashCode → both added!
}
```

**Q39: How to create a deep copy of a Set?**
```java
Set<Obj> original = new HashSet<>();

// Shallow copy (references shared)
Set<Obj> shallow = new HashSet<>(original);

// Deep copy (new objects)
Set<Obj> deep = original.stream()
    .map(Obj::clone)  // or new Obj(...)
    .collect(Collectors.toSet());
```

**Q40: What is the difference between remove() and removeAll()?**
```
remove(Object o):     Removes ONE specific element, returns boolean
removeAll(Collection): Removes ALL elements that match the collection
```

**Q41: What is the power set of a Set?**
```java
// All possible subsets
Set<Integer> set = Set.of(1, 2, 3);
// Power set = { {}, {1}, {2}, {3}, {1,2}, {1,3}, {2,3}, {1,2,3} }
// Size = 2^n = 8

Set<Set<Integer>> powerSet = new HashSet<>();
// Implementation using bit masking or recursion
```

**Q42: What is difference between HashSet and IdentityHashSet?**
```
HashSet:        Uses equals() and hashCode() for comparison
IdentityHashMap: Uses == (reference equality) for comparison
                Two different String objects with same content:
                HashSet: considers them EQUAL
                IdentityHashMap: considers them DIFFERENT
```

**Q43: Can we use Set with streams effectively?**
```java
Set<Integer> set = Set.of(1, 2, 3, 4, 5, 6, 7, 8);

// Filter even numbers
Set<Integer> evens = set.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toSet());

// To sorted set
TreeSet<Integer> sorted = set.stream()
    .collect(Collectors.toCollection(TreeSet::new));

// Grouping
Map<Boolean, Set<Integer>> partition = set.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0,
        Collectors.toSet()));
```

**Q44: What is the memory overhead of HashSet vs LinkedHashSet vs TreeSet?**
```
HashSet:          Node array + nodes (same as HashMap)
LinkedHashSet:    + Doubly linked list (extra 2 references per entry)
TreeSet:          + Red-Black tree nodes (parent, left, right, color)
                  TreeNode uses 2x memory than regular Node
EnumSet:          Bit vector (minimal - 1 long per 64 enums)
```

**Q45: Practical scenario - When to use which Set?**
```
┌───────────────────────────────┬──────────────────────────────────┐
│ Scenario                       │ Best Choice                      │
├───────────────────────────────┼──────────────────────────────────┤
│ Fast lookup, no order          │ HashSet                          │
│ Preserve insertion order       │ LinkedHashSet                    │
│ Sorted elements                │ TreeSet                          │
│ Type-safe enum collection      │ EnumSet                          │
│ Thread-safe, read-heavy        │ CopyOnWriteArraySet              │
│ Thread-safe, sorted            │ ConcurrentSkipListSet            │
│ Thread-safe, no duplicates     │ ConcurrentHashMap.newKeySet()    │
│ Immutable set (Java 9+)        │ Set.of()                         │
│ Remove duplicates from List    │ LinkedHashSet (preserves order)  │
│ Set operations (union/intersect│ HashSet                          │
└───────────────────────────────┴──────────────────────────────────┘
```

**Q46: How to find subsets of a Set?**
```java
Set<Integer> set = Set.of(1, 2, 3);
List<Set<Integer>> subsets = new ArrayList<>();

for (int mask = 0; mask < (1 << set.size()); mask++) {
    Set<Integer> subset = new HashSet<>();
    int i = 0;
    for (int val : set) {
        if ((mask & (1 << i)) != 0) subset.add(val);
        i++;
    }
    subsets.add(subset);
}
// [{}, {1}, {2}, {1,2}, {3}, {1,3}, {2,3}, {1,2,3}]
```

**Q47: What is the use of Set in database operations?**
```java
// Check if value exists (O(1) vs O(n) in List)
Set<String> validIds = new HashSet<>(getAllIds());
if (validIds.contains(inputId)) { ... }  // Fast!

// Remove duplicates from query results
Set<String> uniqueNames = names.stream()
    .distinct()
    .collect(Collectors.toSet());
```

**Q48: What is SortedSet vs NavigableSet?**
```
SortedSet:
- Elements sorted
- first(), last()
- headSet(to), tailSet(from), subSet(from, to)

NavigableSet (extends SortedSet):
- Lower(e), higher(e)     (strict <, >)
- Floor(e), ceiling(e)    (≤, ≥)
- descendingSet()
- pollFirst(), pollLast()
```

**Q49: Can we synchronize a Set in Java 8+?**
```java
// Java 8 parallelStream on unsynchronized Set may cause CME
// Solution: Use synchronized wrapper or ConcurrentHashMap.newKeySet()

Set<String> syncSet = Collections.synchronizedSet(new HashSet<>());
// or
Set<String> concurrentSet = ConcurrentHashMap.newKeySet();
```

**Q50: What is the difference between Set.contains() and Map.containsKey()?**
```
They are the same operation!
HashSet.contains() → map.containsKey()
HashMap.containsKey() → internally same as HashSet.contains()
Both use hashCode() + equals() to check existence.
```

---

## 13. JAVA 8+ SET METHODS

```java
Set<String> set = new HashSet<>(Arrays.asList("A", "B", "C", "D", "E"));

// removeIf (JDK 8+)
set.removeIf(s -> s.length() > 1);  // Remove elements matching predicate

// stream
set.stream().filter(s -> s.startsWith("A")).forEach(System.out::println);

// parallelStream
set.parallelStream().forEach(System.out::println);

// toArray with generator
String[] arr = set.toArray(String[]::new);

// addAll
set.addAll(Set.of("F", "G"));

// copyOf (JDK 10+)
Set<String> copy = Set.copyOf(set);
```

---

## 14. QUICK REFERENCE CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║                    JAVA SET CHEAT SHEET                          ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  HashSet       → Fastest, no order, 1 null                       ║
║  LinkedHashSet → Insertion order, ~5% slower than HashSet        ║
║  TreeSet       → Sorted, O(log n), no null*                      ║
║  EnumSet       → Bit-vector, fastest for enums, minimal memory   ║
║  ConcurrentSkipListSet → Concurrent + Sorted                     ║
║  CopyOnWriteArraySet → Thread-safe, read-heavy                   ║
║  ConcurrentHashMap.newKeySet → Modern thread-safe Set            ║
║                                                                  ║
║  HashSet internals: HashMap<E, PRESENT>                          ║
║  Default Capacity: 16 | Load Factor: 0.75 | Resize: 2x          ║
║  Treeify: bucket ≥ 8 & table ≥ 64 | Detreeify: bucket ≤ 6       ║
║                                                                  ║
║  Duplicates prevented by: hashCode() + equals()                  ║
║  add() → hash → index → check → add/update → resize?            ║
║  contains() → hash → index → traverse → equals() → true/false   ║
║                                                                  ║
║  Set.of() → Immutable, no null, fixed size (Java 9+)            ║
║  Set.copyOf() → Unmodifiable copy (Java 10+)                    ║
║  Collections.unmodifiableSet() → Read-only wrapper               ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: Java 8+ features, Interview Focus, All Major Topics*
