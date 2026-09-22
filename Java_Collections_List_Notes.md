# Java Collections Framework - LIST (Complete Interview Notes)

---

## 1. LIST INTERFACE HIERARCHY (UML)

```
                     <<interface>>
                     ┌──────────────────┐
                     │     Iterable     │
                     └────────┬─────────┘
                              │
                     ┌────────▼─────────┐
                     │    Collection    │
                     └────────┬─────────┘
                              │
                     ┌────────▼─────────┐
                     │       List       │
                     │──────────────────│
                     │ get(int index)   │
                     │ set(int, E)      │
                     │ add(int, E)      │
                     │ remove(int)      │
                     │ indexOf(Object)  │
                     │ lastIndexOf()    │
                     │ subList(int,int) │
                     │ listIterator()   │
                     │ sort(Comparator) │
                     │ of() (Java 9+)   │
                     └────────┬─────────┘
                              │
         ┌────────────────────┼─────────────────────┐
         │                    │                     │
         ▼                    ▼                     ▼
  ┌──────────────┐   ┌──────────────┐     ┌──────────────────┐
  │  AbstractList │   │  RandomAccess│     │  AbstractSequent │
  │  (abstract)   │   │ (interface)  │     │  ialList (abstract)│
  └──────┬───────┘   └──────┬───────┘     └────────┬─────────┘
         │                  │                      │
    ┌────┴─────────────────┐│              ┌───────┴───────┐
    │                     ││              │               │
    ▼                     ▼▼              ▼               ▼
┌──────────┐    ┌────────────────┐ ┌──────────────┐ ┌──────────────┐
│ ArrayList│    │  Vector        │ │  LinkedList  │ │ CopyOnWrite  │
│          │    │  (legacy)      │ │  (DLL)       │ │ ArrayList    │
└──────────┘    └───────┬────────┘ └──────────────┘ └──────────────┘
                        │
                        ▼
                 ┌──────────────┐
                 │    Stack     │
                 │  (legacy)    │
                 └──────────────┘
```

---

## 2. LIST INTERFACE - ALL METHODS

```java
public interface List<E> extends Collection<E> {

    // === INDEX-BASED ACCESS ===
    E get(int index);
    E set(int index, E element);
    void add(int index, E element);
    E remove(int index);

    // === SEARCH ===
    int indexOf(Object o);
    int lastIndexOf(Object o);
    boolean contains(Object o);

    // === SIZE ===
    int size();
    boolean isEmpty();

    // === BULK OPERATIONS ===
    boolean add(E e);                      // append to end
    boolean addAll(Collection<? extends E> c);
    boolean addAll(int index, Collection<? extends E> c);
    boolean removeAll(Collection<?> c);
    boolean removeIf(Predicate<? super E> filter);
    boolean retainAll(Collection<?> c);

    // === SUBVIEW ===
    List<E> subList(int fromIndex, int toIndex);

    // === CONVERSION ===
    Object[] toArray();
    <T> T[] toArray(T[] a);

    // === LIST-SPECIFIC ===
    ListIterator<E> listIterator();
    ListIterator<E> listIterator(int index);
    void sort(Comparator<? super E> c);
    void replaceAll(UnaryOperator<E> operator);

    // === JAVA 9+ ===
    static <E> List<E> of(E... elements);           // immutable
    static <E> List<E> copyOf(Collection<E> coll);  // unmodifiable copy

    // === JAVA 10+ ===
    default void replaceAll(UnaryOperator<E> operator);
    default void sort(Comparator<? super E> c);
}
```

---

## 3. ARRAYLIST - INTERNAL WORKING (MOST ASKED)

### 3.1 Internal Structure

```
    ArrayList<Integer> list = new ArrayList<>();

    Internal: Resizable Array (Object[])
    ┌──────────────────────────────────────────────────────────────┐
    │  ArrayList Object                                            │
    │  ├── elementData (Object[]) ──→ [10][20][30][null][null]...  │
    │  ├── size = 3                                                │
    │  └── modCount = 3                                            │
    │                                                              │
    │  elementData.length = 10  (capacity)                         │
    │  size = 3                    (actual elements)               │
    │                                                              │
    │  Index:  0    1    2    3    4    5    6    7    8    9      │
    │        ┌────┬────┬────┬────┬────┬────┬────┬────┬────┬────┐  │
    │        │ 10 │ 20 │ 30 │null│null│null│null│null│null│null│  │
    │        └────┴────┴────┴────┴────┴────┴────┴────┴────┴────┘  │
    │         ↑         ↑                                          │
    │       index 0   index 2 = size-1                             │
    │                                                              │
    │  ⚠️ Capacity ≠ Size!                                         │
    │  Capacity = elementData.length                               │
    │  Size = number of actual elements                            │
    └──────────────────────────────────────────────────────────────┘
```

### 3.2 Source Code Structure

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
    private static final Object[] EMPTY_ELEMENTDATA = {};

    transient Object[] elementData;  // array buffer
    private int size;                // actual element count

    // Constructors
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;  // lazy init
    }

    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0)
            this.elementData = new Object[initialCapacity];
        else if (initialCapacity == 0)
            this.elementData = EMPTY_ELEMENTDATA;
        else
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
    }

    public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();
        size = elementData.length;
    }
}
```

### 3.3 add() Operation Flow

```
    list.add(40);

    Step 1: Check capacity
    ┌──────────────────────────────────────────────────────────┐
    │  if (size == elementData.length)                         │
    │      → resize (grow) first                              │
    │  else                                                   │
    │      → add directly                                     │
    └──────────────────────────────────────────────────────────┘

    Step 2: Place element
    ┌──────────────────────────────────────────────────────────┐
    │  elementData[size] = 40                                  │
    │  size++;                                                 │
    └──────────────────────────────────────────────────────────┘

    Step 3: Ensure capacity (if needed)
    ┌──────────────────────────────────────────────────────────┐
    │  ensureCapacityInternal(size + 1)                       │
    │  if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA)   │
    │      → capacity = max(10, minCapacity)  // first add = 10 │
    │  else if (size + 1 > elementData.length)                 │
    │      → grow()                                           │
    └──────────────────────────────────────────────────────────┘
```

### 3.4 Resize (Growing) Mechanism

```
    ArrayList capacity: 10 → 15 → 22 → 33 → 49 → ...

    Formula: newCapacity = oldCapacity + (oldCapacity >> 1)
           = oldCapacity * 1.5

    Why 1.5x (not 2x like ArrayDeque)?
    ┌──────────────────────────────────────────────────────────┐
    │  ArrayList: 10 → 15 → 22 → 33 → 49 → 73 → ...          │
    │  ArrayDeque: 16 → 32 → 64 → 128 → ...                   │
    │                                                          │
    │  ArrayList saves memory (grows slower)                   │
    │  ArrayDeque saves time (fewer copies)                    │
    └──────────────────────────────────────────────────────────┘

    Grow Algorithm:
    ┌──────────────────────────────────────────────────────────┐
    │  private void grow(int minCapacity) {                    │
    │      int oldCapacity = elementData.length;               │
    │      int newCapacity = oldCapacity + (oldCapacity >> 1); │
    │      if (newCapacity - minCapacity < 0)                  │
    │          newCapacity = minCapacity;                      │
    │      elementData = Arrays.copyOf(elementData, newCapacity);│
    │  }                                                       │
    └──────────────────────────────────────────────────────────┘

    Visual:
    BEFORE (capacity=10, full):
    ┌────┬────┬────┬────┬────┬────┬────┬────┬────┬────┐
    │ 10 │ 20 │ 30 │ 40 │ 50 │ 60 │ 70 │ 80 │ 90 │100 │
    └────┴────┴────┴────┴────┴────┴────┴────┴────┴────┘
    size=10, capacity=10

    add(110) → grow():
    ┌────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┐
    │ 10 │ 20 │ 30 │ 40 │ 50 │ 60 │ 70 │ 80 │ 90 │100 │110 │null│null│null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┘
    size=11, capacity=15 (old 10 + 10/2 = 15)
```

### 3.5 add(int index, E element)

```
    list = [10, 20, 30, 40]
    list.add(2, 25);  // Insert 25 at index 2

    BEFORE:
    Index: 0    1    2    3
          ┌────┬────┬────┬────┐
          │ 10 │ 20 │ 30 │ 40 │
          └────┴────┴────┴────┘

    Step 1: Check capacity, grow if needed
    Step 2: Shift elements right from index 2
    ┌──────────────────────────────────────────────────────────┐
    │  System.arraycopy(elementData, 2, elementData, 3, 2);   │
    │  Copies: elementData[2..3] → elementData[3..4]          │
    └──────────────────────────────────────────────────────────┘

    AFTER shifting:
    Index: 0    1    2    3    4
          ┌────┬────┬────┬────┬────┐
          │ 10 │ 20 │ 30 │ 30 │ 40 │
          └────┴────┴────┴────┴────┘
                         ↑
                    Shifted right

    Step 3: Place element at index
    elementData[2] = 25;

    FINAL:
    Index: 0    1    2    3    4
          ┌────┬────┬────┬────┬────┐
          │ 10 │ 20 │ 25 │ 30 │ 40 │
          └────┴────┴────┴────┴────┘

    Time: O(n) - must shift elements
```

### 3.6 remove(int index)

```
    list = [10, 20, 25, 30, 40]
    list.remove(2);  // Remove element at index 2

    BEFORE:
    Index: 0    1    2    3    4
          ┌────┬────┬────┬────┬────┐
          │ 10 │ 20 │ 25 │ 30 │ 40 │
          └────┴────┴────┴────┴────┘

    Step 1: Get old value
    E oldValue = elementData[2];  // 25

    Step 2: Shift elements left from index 3
    ┌──────────────────────────────────────────────────────────┐
    │  System.arraycopy(elementData, 3, elementData, 2, 2);   │
    │  Copies: elementData[3..4] → elementData[2..3]          │
    └──────────────────────────────────────────────────────────┘

    Step 3: Null last slot for GC
    elementData[--size] = null;

    FINAL:
    Index: 0    1    2    3
          ┌────┬────┬────┬────┬────┐
          │ 10 │ 20 │ 30 │ 40 │null│
          └────┴────┴────┴────┴────┘
    size=4

    Time: O(n) - must shift elements
```

### 3.7 get(int index) - O(1)

```
    list.get(2);

    ┌──────────────────────────────────────────────────────────┐
    │  rangeCheck(2);                                          │
    │  return elementData[2];  // Direct array access          │
    └──────────────────────────────────────────────────────────┘

    Index: 0    1    2    3    4
          ┌────┬────┬────┬────┬────┐
          │ 10 │ 20 │ 30 │ 40 │ 50 │
          └────┴────┴────┴────┴────┘
                  ↑
             elementData[2] = 30  → O(1) random access!
```

---

## 4. LINKEDLIST - INTERNAL WORKING

### 4.1 Internal Structure

```
    LinkedList<Integer> list = new LinkedList<>();
    list.add(10);
    list.add(20);
    list.add(30);

    Internal: Doubly Linked List
    ┌──────────────────────────────────────────────────────────────┐
    │  LinkedList Object                                           │
    │  ├── first = Node{10}                                       │
    │  ├── last = Node{30}                                        │
    │  └── size = 3                                               │
    └──────────────────────────────────────────────────────────────┘

    Node Structure:
    ┌────────────────────────────────────────────────────┐
    │  static class Node<E> {                            │
    │      E item;                                       │
    │      Node<E> next;                                 │
    │      Node<E> prev;                                 │
    │  }                                                 │
    └────────────────────────────────────────────────────┘

    Linked List:
    ┌─────────┐    ┌─────────┐    ┌─────────┐
    │  null   │←──→│   10    │←──→│   20    │←──→│   30    │←──→│  null   │
    └─────────┘    └─────────┘    └─────────┘    └─────────┘    └─────────┘
     ↑ (head)                       ↑               ↑            ↑ (tail)
    first                         prev=10node    prev=20node
                                  next=20node    next=30node

    Node{10}: { item=10, prev=null,      next=Node{20} }
    Node{20}: { item=20, prev=Node{10},  next=Node{30} }
    Node{30}: { item=30, prev=Node{20},  next=null      }
```

### 4.2 add() Operation

```
    list.add(10);  // addLast

    BEFORE (empty):
    first = null, last = null, size = 0

    Step 1: Create node
    ┌──────────────────────────────────────────────┐
    │  Node<E> newNode = new Node<>(last, 10, null);│
    └──────────────────────────────────────────────┘

    Step 2: Link
    ┌──────────────────────────────────────────────┐
    │  last = newNode;                             │
    │  if (first == null) first = newNode;         │
    │  else { prevNode.next = newNode; }           │
    │  size++;                                     │
    └──────────────────────────────────────────────┘

    AFTER:
    ┌─────────┐
    │  null   │←──→│   10    │←──→│  null   │
    └─────────┘    └─────────┘    └─────────┘
      (head) first                  last      (tail)
```

### 4.3 add(int index, E element)

```
    list = [10, 20, 30]
    list.add(1, 15);  // Insert at index 1

    Step 1: Find node at index
    ┌──────────────────────────────────────────────────────────┐
    │  node(1) → Node{20}  (traverse from head or tail)        │
    │  if (index < size/2) traverse from head (faster)         │
    │  else traverse from tail                                 │
    └──────────────────────────────────────────────────────────┘

    Step 2: Create new node and link
    BEFORE:
    Node{10} ⇄ Node{20} ⇄ Node{30}

    AFTER:
    Node{10} ⇄ Node{15} ⇄ Node{20} ⇄ Node{30}

    Time: O(n) for finding node, O(1) for actual insertion
```

### 4.4 get(int index) - O(n)

```
    list.get(2);

    ┌──────────────────────────────────────────────────────────┐
    │  node(2) → traverse from head: 0→1→2                     │
    │  or from tail: 2→1→0 (whichever is closer)               │
    └──────────────────────────────────────────────────────────┘

    ┌─────────┐    ┌─────────┐    ┌─────────┐
    │   10    │───→│   20    │───→│   30    │
    └─────────┘    └─────────┘    └─────────┘
       ↑              ↑              ↑
    index 0        index 1        index 2  ← Found!

    Time: O(n) - must traverse
    ⚠️ NOT O(1) like ArrayList!
```

---

## 5. VECTOR & STACK (LEGACY)

### 5.1 Vector

```
    Vector<Integer> vector = new Vector<>();
    // Same as ArrayList but SYNCHRONIZED (thread-safe)

    ┌──────────────────────────────────────────────────────────────┐
    │  Vector vs ArrayList                                         │
    ├──────────────────┬──────────────────┬───────────────────────┤
    │ Feature           │ ArrayList        │ Vector                │
    ├──────────────────┼──────────────────┼───────────────────────┤
    │ Thread-safe       │ No               │ Yes (synchronized)   │
    │ Growth            │ 1.5x             │ 2x                   │
    │ Iterator          │ fail-fast        │ fail-fast            │
    │ Performance       │ Fast             │ Slow (lock overhead) │
    │ Since             │ JDK 1.2          │ JDK 1.0              │
    │ Legacy            │ No               │ Yes                  │
    │ Preferred         │ Yes              │ No (use ArrayList +  │
    │                   │                  │  Collections.synchronizedList)│
    └──────────────────┴──────────────────┴───────────────────────┘

    Vector growth:
    ┌──────────────────────────────────────────────────────────┐
    │  ArrayList: 10 → 15 → 22 → 33 → 49 → ...  (1.5x)       │
    │  Vector:    10 → 20 → 40 → 80 → 160 → ...  (2x)        │
    └──────────────────────────────────────────────────────────┘
```

### 5.2 Stack

```
    Stack<Integer> stack = new Stack<>();
    // Extends Vector, adds stack operations

    ┌──────────────────────────────────────────────────────────────┐
    │  Stack Methods (extends Vector):                              │
    │  push(E item)  → addElement(item)  → returns item            │
    │  pop()         → removeElementAt(size-1) → returns top       │
    │  peek()        → elementAt(size-1)  → returns top (no remove)│
    │  empty()       → isEmpty()                                   │
    │  search(Object)→ lastIndexOf(Object) + 1 (or -1)            │
    └──────────────────────────────────────────────────────────────┘

    ⚠️ PROBLEM: Stack is SYNCHRONIZED (unnecessary overhead)
    ✅ SOLUTION: Use ArrayDeque instead (Java 6+)

    // OLD (avoid)
    Stack<Integer> stack = new Stack<>();

    // NEW (recommended)
    Deque<Integer> stack = new ArrayDeque<>();

    Performance:
    ┌──────────────────┬──────────────┬──────────────┐
    │ Operation         │ Stack        │ ArrayDeque   │
    ├──────────────────┼──────────────┼──────────────┤
    │ push              │ O(1)*        │ O(1)         │
    │ pop               │ O(1)*        │ O(1)         │
    │ peek              │ O(1)*        │ O(1)         │
    │ contains          │ O(n)         │ O(n)         │
    │ thread-safe       │ Yes          │ No           │
    │ synchronization   │ Every op     │ None         │
    │ legacy            │ Yes (1.0)    │ No (1.6)     │
    └──────────────────┴──────────────┴──────────────┘
    * O(1) but with synchronized overhead
```

---

## 6. COPYONWRITEARRAYLIST

```
    CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

    Every write operation (add, set, remove) creates a NEW copy of the array.
    Iterators work on the OLD array (snapshot).

    ┌──────────────────────────────────────────────────────────────┐
    │  Thread A: Reading (Iterator)                                │
    │  ┌────────────────────────────────────────┐                 │
    │  │ Reference: array[1,2,3] (old array)     │                 │
    │  │ Iterator traverses old array            │                 │
    │  └────────────────────────────────────────┘                 │
    │                                                              │
    │  Thread B: Writing (add)                                     │
    │  ┌────────────────────────────────────────┐                 │
    │  │ Lock acquired                          │                 │
    │  │ Copy array[1,2,3] → new array[1,2,3,4] │                 │
    │  │ Add element to new array               │                 │
    │  │ Replace reference to new array         │                 │
    │  │ Release lock                           │                 │
    │  └────────────────────────────────────────┘                 │
    │                                                              │
    │  Thread A: Still sees [1,2,3] (old snapshot)                │
    │  Thread C: New reader sees [1,2,3,4] (new array)            │
    └──────────────────────────────────────────────────────────────┘

    When to use:
    ┌──────────────────────────────────────────────────────────────┐
    │  ✅ Read-heavy, write-rare scenarios                         │
    │  ✅ Listener lists, observer patterns                        │
    │  ✅ When iteration should not be interrupted                  │
    │  ❌ NOT for write-heavy scenarios (expensive copies)          │
    └──────────────────────────────────────────────────────────────┘

    Comparison:
    ┌──────────────────────┬──────────────────────┐
    │ ArrayList            │ CopyOnWriteArrayList  │
    ├──────────────────────┼──────────────────────┤
    │ Not thread-safe     │ Thread-safe           │
    │ CME on modification │ No CME (snapshot)     │
    │ Fast writes          │ Slow writes (copy)   │
    │ Regular Iterator     │ Snapshot Iterator     │
    │ One lock per op      │ One lock per write   │
    └──────────────────────┴──────────────────────┘
```

---

## 7. ALL LIST IMPLEMENTATIONS COMPARISON

```
    ┌──────────────────┬──────────────┬──────────────┬──────────────┬──────────────┐
    │ Feature           │ ArrayList    │ LinkedList   │ Vector       │ CopyOnWrite  │
    ├──────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
    │ Internal          │ Object[]     │ Doubly LL    │ Object[]     │ Object[]     │
    │ Index access      │ O(1)         │ O(n)         │ O(1)*        │ O(1)         │
    │ Add (end)         │ O(1) amort   │ O(1)         │ O(1)*        │ O(n)         │
    │ Add (middle)      │ O(n) shifting│ O(1) node    │ O(n)*        │ O(n) copy    │
    │ Remove (end)      │ O(1)         │ O(1)         │ O(1)*        │ O(n) copy    │
    │ Remove (middle)   │ O(n) shifting│ O(1) node    │ O(n)*        │ O(n) copy    │
    │ Search            │ O(n)         │ O(n)         │ O(n)*        │ O(n)         │
    │ Memory            │ Low          │ High (nodes) │ High         │ High (copy)  │
    │ Cache-friendly    │ Yes          │ No           │ Yes          │ Yes          │
    │ Thread-safe       │ No           │ No           │ Yes          │ Yes          │
    │ Null allowed      │ Yes          │ Yes          │ Yes          │ Yes          │
    │ RandomAccess      │ ✅ Yes        │ ❌ No         │ ✅ Yes        │ ✅ Yes        │
    │ Iterator          │ fail-fast    │ fail-fast    │ fail-fast    │ fail-safe    │
    │ Legacy            │ No           │ No           │ Yes          │ No           │
    │ Preferred for     │ General use  │ Queue/Deque  │ Avoid        │ Read-heavy   │
    └──────────────────┴──────────────┴──────────────┴──────────────┴──────────────┘

    * With synchronized overhead

    MEMORY COMPARISON (1 million integers):
    ┌──────────────────┬──────────────┬──────────────────┐
    │ Implementation    │ Memory        │ Per element      │
    ├──────────────────┼──────────────┼──────────────────┤
    │ ArrayList         │ ~16 MB        │ ~16 bytes        │
    │ LinkedList        │ ~56 MB        │ ~56 bytes (3 refs)│
    │ Vector            │ ~16 MB        │ ~16 bytes        │
    └──────────────────┴──────────────┴──────────────────┘
```

---

## 8. ARRAYLIST vs LINKEDLIST (DEEP COMPARISON)

```
    ┌──────────────────────────────────────────────────────────────────────┐
    │                    ArrayList vs LinkedList                             │
    ├──────────────────────────────────────────────────────────────────────┤
    │                                                                      │
    │  ArrayList:                     LinkedList:                          │
    │  ┌──────────────────┐           ┌──────────────────────────┐        │
    │  │ [A][B][C][D][E]   │           │ null←→A←→B←→C←→D←→E→null │        │
    │  │ ↑ contiguous      │           │ ↑ scattered nodes         │        │
    │  └──────────────────┘           └──────────────────────────┘        │
    │                                                                      │
    │  get(0): O(1) ✅               get(0): O(1) ✅                      │
    │  get(4): O(1) ✅               get(4): O(n) ❌ (traverse)            │
    │  get(2): O(1) ✅               get(2): O(n) ❌ (traverse)            │
    │                                                                      │
    │  add(0,x): O(n) ❌             add(0,x): O(1) ✅                    │
    │  add(4,x): O(1) ✅             add(4,x): O(1) ✅ (if at end)        │
    │  add(x):   O(1) ✅             add(x):   O(1) ✅                    │
    │                                                                      │
    │  remove(0): O(n) ❌            remove(0): O(1) ✅                   │
    │  remove(4): O(1) ✅            remove(4): O(1) ✅ (if at end)       │
    │  remove(x): O(n) ❌            remove(x): O(n) ❌ (search)          │
    │                                                                      │
    │  BEST FOR:                      BEST FOR:                            │
    │  ✅ Random access (get/set)      ✅ Add/remove at both ends          │
    │  ✅ Iteration (cache-friendly)   ✅ Queue/Deque operations            │
    │  ✅ When size is known           ✅ When adding/removing often        │
    │  ✅ Search by index              ✅ Implementing Queue/Deque          │
    │                                                                      │
    │  WORST FOR:                     WORST FOR:                           │
    │  ❌ Adding/removing at start     ❌ Random access (get by index)      │
    │  ❌ Inserting in middle          ❌ Iteration (cache misses)          │
    │                                                                      │
    └──────────────────────────────────────────────────────────────────────┘
```

---

## 9. ARRAYLIST vs LINKEDLIST - BENCHMARKS

```
    ┌────────────────────────────┬──────────────┬──────────────┐
    │ Operation (1M elements)     │ ArrayList    │ LinkedList   │
    ├────────────────────────────┼──────────────┼──────────────┤
    │ add(0, x) - 1000 times     │ 2 ms         │ 0.3 ms       │
    │ add(size, x) - 1000 times  │ 0.1 ms       │ 180 ms       │
    │ add(500K, x) - 1000 times  │ 85 ms        │ 0.5 ms       │
    │ get(0) - 1M times          │ 2 ms         │ 2 ms         │
    │ get(500K) - 1M times       │ 2 ms         │ 450 ms       │
    │ get(999K) - 1M times       │ 2 ms         │ 900 ms       │
    │ remove(0) - 1000 times     │ 3 ms         │ 0.2 ms       │
    │ remove(size-1) - 1000 times│ 0.1 ms       │ 180 ms       │
    │ contains(x) - 1M times     │ 120 ms       │ 180 ms       │
    │ iterate all - 1 time       │ 5 ms         │ 15 ms        │
    │ Memory (1M integers)        │ ~16 MB       │ ~56 MB       │
    └────────────────────────────┴──────────────┴──────────────┘

    Key takeaway:
    ┌──────────────────────────────────────────────────────────┐
    │  ArrayList: Fast random access, slow insertion at start  │
    │  LinkedList: Fast insertion at any position, slow access │
    │  ArrayList wins in 90% of real-world use cases           │
    └──────────────────────────────────────────────────────────┘
```

---

## 10. WHEN TO USE WHAT - FLOWCHART

```
    ┌─────────────────────────────────────────────────────────────┐
    │                    WHICH LIST TO USE?                         │
    └───────────────────────┬─────────────────────────────────────┘
                            │
                    ┌───────▼────────┐
                    │ Need random     │
                    │ access (get)?   │
                    └───────┬────────┘
                            │
                ┌───────────┴───────────────┐
                │                           │
            YES ▼                       NO  ▼
        ┌───────────────┐        ┌─────────────────┐
        │ Need thread   │        │ Need queue/deque │
        │ safety?       │        │ operations?      │
        └───────┬───────┘        └────────┬────────┘
                │                          │
        ┌───────┴───────┐          ┌───────┴───────┐
        │               │          │               │
    YES ▼           NO  ▼      YES ▼           NO  ▼
    ┌─────────────┐ ┌──────────┐ ┌──────────────┐ ┌────────────────┐
    │CopyOnWrite  │ │ArrayList │ │ArrayDeque    │ │ ArrayList      │
    │ArrayList    │ │          │ │(not List)    │ │ (default)      │
    └─────────────┘ └──────────┘ └──────────────┘ └────────────────┘

    ┌──────────────────────────────────────────────────────────────┐
    │ SUMMARY:                                                     │
    │                                                              │
    │  Default choice       → ArrayList                            │
    │  Thread-safe          → CopyOnWriteArrayList or sync wrapper │
    │  Queue/Deque          → ArrayDeque (NOT LinkedList)          │
    │  Insert/remove often  → LinkedList (rarely better)           │
    │  Legacy code          → Vector (avoid in new code)           │
    │  Stack operations     → ArrayDeque (NOT Stack class)         │
    │                                                              │
    │  ⚠️ LinkedList is almost NEVER the best choice!              │
    │  Use ArrayDeque instead for queue/deque operations           │
    └──────────────────────────────────────────────────────────────┘
```

---

## 11. JAVA 9+ IMMUTABLE LISTS

```java
// Factory methods
List<String> list = List.of("A", "B", "C");
List<Integer> empty = List.of();
List<String> five = List.of("A", "B", "C", "D", "E");  // up to 10

// Copy existing
List<String> original = new ArrayList<>();
List<String> copy = List.copyOf(original);  // Unmodifiable

// Characteristics:
// - Fixed size (add/remove throws UnsupportedOperationException)
// - No null elements
// - Serializable
// - Disallows duplicates at creation
// - Thread-safe (immutable)
// - Smaller memory footprint than ArrayList
```

---

## 12. SUBLIST VIEW

```
    List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    List<Integer> sub = list.subList(1, 4);  // [2, 3, 4] (view, not copy!)

    ┌──────────────────────────────────────────────────────────────┐
    │  list: [ 1 ][ 2 ][ 3 ][ 4 ][ 5 ]                            │
    │                   ↑           ↑                               │
    │               fromIndex=1  toIndex=4 (exclusive)             │
    │                                                              │
    │  sub: [ 2 ][ 3 ][ 4 ]  ← VIEW into original list!           │
    │                                                              │
    │  sub.set(0, 99) → list becomes [1, 99, 3, 4, 5]             │
    │  sub.add(0, 99) → ConcurrentModificationException if list   │
    │                     is modified directly                     │
    └──────────────────────────────────────────────────────────────┘

    ⚠️ WARNING: sublist is a VIEW, not a copy
    Modifications to sublist affect original list!
    Modifications to original list may cause CME on sublist.
```

---

## 13. LIST - ALL INTERVIEW QUESTIONS (50+)

### ⭐ BASIC LEVEL (Q1-Q15)

**Q1: What is List in Java?**
> List is an ordered Collection (sequence). Allows duplicates. Provides index-based access.

**Q2: What are the main implementations of List?**
> ArrayList, LinkedList, Vector, Stack, CopyOnWriteArrayList, AbstractList

**Q3: Can List store duplicate elements?**
> Yes. List allows any number of duplicate elements.

**Q4: Can List store null elements?**
> Yes. ArrayList, LinkedList allow multiple nulls. Vector allows nulls.

**Q5: What is difference between List and Set?**
```
List:  Ordered, duplicates allowed, index access
Set:   Unordered (mostly), no duplicates, no index access
```

**Q6: What is difference between ArrayList and LinkedList?**
```
ArrayList:  Backed by array, O(1) get, O(n) insert/remove at start
LinkedList: Backed by doubly linked list, O(1) add/remove at ends, O(n) get
ArrayList wins in 90% of use cases.
```

**Q7: What is the default capacity of ArrayList?**
> 10. Created lazily on first add(). Grows by 1.5x when full.

**Q8: Why ArrayList is preferred over LinkedList?**
```
1. O(1) random access (get/set)
2. Cache-friendly (contiguous memory)
3. Less memory (no node objects)
4. Faster iteration
5. LinkedList rarely better in practice
```

**Q9: What is RandomAccess interface?**
> Marker interface indicating list supports efficient random access. ArrayList and Vector implement it. LinkedList does not.

**Q10: Can ArrayList store null elements?**
> Yes. Multiple null elements allowed.

**Q11: How does ArrayList grow?**
> New capacity = oldCapacity + (oldCapacity >> 1) = 1.5x. Uses Arrays.copyOf().

**Q12: What is difference between ArrayList and Vector?**
```
ArrayList: Not thread-safe, grows 1.5x, faster
Vector: Thread-safe (synchronized), grows 2x, slower
Use ArrayList + Collections.synchronizedList instead of Vector.
```

**Q13: What is Stack?**
> Legacy class extending Vector. Provides push/pop/peek. Use ArrayDeque instead.

**Q14: What is the time complexity of ArrayList operations?**
```
┌──────────────────────┬──────────────┐
│ Operation             │ Complexity   │
├──────────────────────┼──────────────┤
│ get(index)            │ O(1)         │
│ set(index, e)         │ O(1)         │
│ add(e) (end)          │ O(1) amort   │
│ add(index, e)         │ O(n)         │
│ remove(index)         │ O(n)         │
│ remove(Object)        │ O(n)         │
│ contains(Object)      │ O(n)         │
│ indexOf(Object)       │ O(n)         │
│ size()                │ O(1)         │
└──────────────────────┴──────────────┘
```

**Q15: What is the time complexity of LinkedList operations?**
```
┌──────────────────────┬──────────────┐
│ Operation             │ Complexity   │
├──────────────────────┼──────────────┤
│ get(index)            │ O(n)         │
│ set(index, e)         │ O(n)         │
│ add(e) (end)          │ O(1)         │
│ add(index, e)         │ O(n) find +  │
│                       │ O(1) insert  │
│ remove(index)         │ O(n)         │
│ remove(Object)        │ O(n)         │
│ contains(Object)      │ O(n)         │
│ addFirst/addLast      │ O(1)         │
│ removeFirst/removeLast│ O(1)         │
│ size()                │ O(1)         │
└──────────────────────┴──────────────┘
```

---

### ⭐⭐ MIDDLE LEVEL (Q16-Q35)

**Q16: How ArrayList internally works?**
```
1. Object[] elementData stores elements
2. size tracks actual element count
3. add(e): if full → grow 1.5x → place at end → size++
4. get(i): return elementData[i] (direct array access)
5. add(i,e): shift right → place → size++
6. remove(i): get old → shift left → null for GC → size--
```

**Q17: What happens when ArrayList is full?**
```
1. grow() called: newCapacity = oldCapacity + (oldCapacity >> 1)
2. elementData = Arrays.copyOf(elementData, newCapacity)
3. Old array becomes eligible for GC
4. New array has 1.5x capacity
```

**Q18: Why ArrayList uses 1.5x growth instead of 2x?**
```
1.5x growth:
- More memory efficient
- Less wasted space
- Amortized O(1) still holds

2x growth:
- Fewer resizes
- More memory waste
- Used by ArrayDeque (speed > memory)

Both are valid. ArrayList prioritizes memory.
```

**Q19: What is difference between remove(Object) and remove(int)?**
```
remove(Object o): Removes first occurrence of object, returns boolean
remove(int index): Removes element at index, returns element
Overloaded methods - different behavior!
```

**Q20: What happens when you remove while iterating?**
```java
// ❌ WRONG - for-each
for (String s : list) {
    if (s.equals("B")) list.remove(s);  // CME!
}

// ✅ CORRECT - Iterator
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("B")) it.remove();
}

// ✅ CORRECT - removeIf
list.removeIf(s -> s.equals("B"));
```

**Q21: How to convert ArrayList to array?**
```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B"));

// Method 1
String[] arr1 = list.toArray(new String[0]);

// Method 2
String[] arr2 = list.toArray(new String[list.size()]);

// Method 3 (Java 11+)
String[] arr3 = list.toArray(String[]::new);
```

**Q22: How to convert array to ArrayList?**
```java
String[] arr = {"A", "B", "C"};

// Method 1
ArrayList<String> list1 = new ArrayList<>(Arrays.asList(arr));

// Method 2 (Java 9+)
List<String> list2 = List.of(arr);

// Method 3 (mutable)
ArrayList<String> list3 = new ArrayList<>();
Collections.addAll(list3, arr);

// Method 4 (Stream)
ArrayList<String> list4 = Arrays.stream(arr)
    .collect(Collectors.toCollection(ArrayList::new));
```

**Q23: What is the difference between subList() and new ArrayList(subList)?**
```
subList(1,4):     VIEW into original list (changes reflected)
new ArrayList(subList(1,4)):  COPY (independent)
```

**Q24: How to sort ArrayList?**
```java
ArrayList<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5));

// Method 1: Collections.sort
Collections.sort(list);

// Method 2: List.sort (JDK 8+)
list.sort(Comparator.naturalOrder());

// Method 3: Stream
List<Integer> sorted = list.stream()
    .sorted()
    .collect(Collectors.toList());

// Descending
list.sort(Comparator.reverseOrder());
```

**Q25: How to synchronize ArrayList?**
```java
List<String> list = new ArrayList<>();
List<String> syncList = Collections.synchronizedList(list);

// Or use CopyOnWriteArrayList
List<String> cowList = new CopyOnWriteArrayList<>(list);
```

**Q26: What is difference between indexOf() and lastIndexOf()?**
```
indexOf(Object):    Returns index of FIRST occurrence (-1 if not found)
lastIndexOf(Object): Returns index of LAST occurrence (-1 if not found)
```

**Q27: What is the difference between addAll() and add()?**
```
add(E e):           Adds single element at end
addAll(Collection): Adds all elements from collection at end
addAll(index, coll): Adds all elements at specified index
```

**Q28: What is the difference between retainAll() and removeAll()?**
```
retainAll(Collection): Keeps ONLY elements in BOTH this list and argument
removeAll(Collection): Removes ALL elements that are in argument
```

**Q29: How to find duplicate elements in ArrayList?**
```java
ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 2, 3, 3, 3));

Set<Integer> seen = new HashSet<>();
Set<Integer> duplicates = new HashSet<>();

for (Integer i : list) {
    if (!seen.add(i)) duplicates.add(i);
}
// duplicates = {2, 3}
```

**Q30: How to remove duplicates from ArrayList?**
```java
// Method 1: LinkedHashSet (preserves order)
ArrayList<Integer> unique = new ArrayList<>(
    new LinkedHashSet<>(list));

// Method 2: Stream
ArrayList<Integer> unique2 = list.stream()
    .distinct()
    .collect(Collectors.toCollection(ArrayList::new));
```

**Q31: What is the difference between set() and add()?**
```
set(index, e): Replaces element at index, returns old element
               List size unchanged
add(index, e): Inserts element at index, shifts right
               List size increases by 1
```

**Q32: What happens when you set element at invalid index?**
```java
list.set(10, "X");  // throws IndexOutOfBoundsException
list.get(10);        // throws IndexOutOfBoundsException
list.remove(10);     // throws IndexOutOfBoundsException
```

**Q33: How to iterate ArrayList?**
```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));

// Method 1: For-each (best for simple iteration)
for (String s : list) { ... }

// Method 2: Index-based (need index)
for (int i = 0; i < list.size(); i++) { ... }

// Method 3: Iterator (need to remove)
Iterator<String> it = list.iterator();
while (it.hasNext()) { ... }

// Method 4: ListIterator (need backward/insert/replace)
ListIterator<String> lit = list.listIterator();
while (lit.hasNext()) { ... }

// Method 5: forEach (JDK 8+)
list.forEach(s -> System.out.println(s));

// Method 6: Stream
list.stream().forEach(System.out::println);
```

**Q34: What is the difference between ArrayList and CopyOnWriteArrayList?**
```
ArrayList: Fast, not thread-safe, fail-fast iterator
CopyOnWriteArrayList: Slow writes, thread-safe, fail-safe iterator
Use CopyOnWriteArrayList for read-heavy, write-rare scenarios
```

**Q35: What is the difference between AbstractList and AbstractSequentialList?**
```
AbstractList: For random-access lists (override get/set)
AbstractSequentialList: For sequential-access lists (override listIterator)
ArrayList extends AbstractList
LinkedList extends AbstractSequentialList
```

---

### ⭐⭐⭐ ADVANCED LEVEL (Q36-Q50)

**Q36: What is the internal array growth strategy of ArrayList?**
```
Capacity grows by 50% each time:
10 → 15 → 22 → 33 → 49 → 73 → 109 → 163 → ...

Amortized O(1) for add(e):
- Most adds: O(1) (no resize)
- Every ~log(n) adds: O(n) (resize)
- Total for n adds: O(n) + O(n) = O(2n) = O(n)
- Per add: O(n)/n = O(1)
```

**Q37: How does LinkedList handle null elements?**
```java
LinkedList<String> list = new LinkedList<>();
list.add(null);     // Works
list.addFirst(null); // Works
list.add(null);     // Another null works

// Node structure:
// Node{item=null, prev=..., next=...}
// Null is a valid element value
```

**Q38: What is the difference between LinkedList and Deque?**
```
LinkedList: Implements List + Deque + Queue
Deque: Interface only

Use Deque reference when you only need queue/deque operations:
Deque<Integer> deque = new LinkedList<>();  // Better intent
LinkedList<Integer> list = new LinkedList<>();  // If need List methods too
```

**Q39: How to implement a custom List?**
```java
class MyList<E> extends AbstractList<E> {
    private Object[] data;
    private int size;

    public MyList(int capacity) { data = new Object[capacity]; }

    @Override
    public E get(int index) { return (E) data[index]; }

    @Override
    public int size() { return size; }

    @Override
    public E set(int index, E element) {
        E old = (E) data[index];
        data[index] = element;
        return old;
    }

    @Override
    public void add(int index, E element) {
        // Shift and insert
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    @Override
    public E remove(int index) {
        E old = (E) data[index];
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        data[--size] = null;
        return old;
    }
}
```

**Q40: What is the difference between ArrayList and Arrays.asList()?**
```java
// Arrays.asList returns FIXED-SIZE list (view of array)
String[] arr = {"A", "B", "C"};
List<String> fixedList = Arrays.asList(arr);
fixedList.add("D");      // UnsupportedOperationException!
fixedList.set(0, "X");   // Works! (modifies underlying array)

// ArrayList is resizable
ArrayList<String> arrayList = new ArrayList<>(fixedList);
arrayList.add("D");      // Works!
```

**Q41: What is the performance impact of autoboxing in ArrayList?**
```
ArrayList<Integer> list = new ArrayList<>();
list.add(10);  // Autoboxing: int 10 → Integer.valueOf(10)

For millions of operations:
- Autoboxing creates Integer objects → GC pressure
- Use int[] or IntStream for performance-critical code
- ArrayList<Integer> ≈ 4x memory of int[]
```

**Q42: What is the difference between removeIf and Iterator.remove()?**
```
removeIf(Predicate):
  - JDK 8+, cleaner syntax
  - May use batch operations internally
  - Works on Collection directly
  - Returns boolean (removed any?)

Iterator.remove():
  - Manual control
  - One element at a time
  - Need iterator reference
  - Available since JDK 1.2
```

**Q43: What is the difference between clear() and new ArrayList()?**
```java
list.clear();      // Removes all, but internal array stays
                   // capacity unchanged
list = new ArrayList<>();  // Creates new empty list
                   // old list becomes garbage
```

**Q44: How does ArrayList handle ConcurrentModificationException?**
```
modCount tracks structural modifications:
- add(), remove(), clear() → modCount++
- Iterator saves expectedModCount at creation
- next()/remove() check: modCount == expectedModCount
- If not equal → ConcurrentModificationException

Use Iterator.remove() or removeIf() to avoid CME.
```

**Q45: What is the difference between toArray() and toArray(T[])?**
```java
// toArray() → Object[] (may cause ClassCastException)
Object[] arr = list.toArray();

// toArray(T[]) → T[] (type-safe)
String[] arr2 = list.toArray(new String[0]);
String[] arr3 = list.toArray(new String[list.size()]);
```

**Q46: What is ArrayList trimToSize()?**
```java
list.trimToSize();  // Trims capacity to current size
// Reduces memory if list is much smaller than capacity
// elementData = Arrays.copyOf(elementData, size);
```

**Q47: What is ArrayList ensureCapacity()?**
```java
list.ensureCapacity(100);  // Pre-allocate capacity
// Avoids multiple resizes if you know approximate size
// More efficient than default growth for large lists
```

**Q48: What is the difference between List.of() and ArrayList?**
```
List.of():
  - Fixed size (no add/remove)
  - No null elements
  - Immutable
  - More memory efficient
  - Java 9+

ArrayList:
  - Resizable
  - Allows null
  - Mutable
  - More flexible
```

**Q49: How to create a thread-safe List?**
```java
// Option 1: CopyOnWriteArrayList (best for read-heavy)
List<String> list = new CopyOnWriteArrayList<>();

// Option 2: Synchronized wrapper (general purpose)
List<String> list = Collections.synchronizedList(new ArrayList<>());

// Option 3: Synchronized block
synchronized (list) {
    list.add("A");
    list.remove("B");
}

// Option 4: List.copyOf (immutable, thread-safe by design)
List<String> list = List.copyOf(original);
```

**Q50: Practical scenario - When to use which List?**
```
┌───────────────────────────────┬──────────────────────────────────┐
│ Scenario                       │ Best Choice                      │
├───────────────────────────────┼──────────────────────────────────┤
│ General purpose                │ ArrayList                        │
│ Random access by index         │ ArrayList                        │
│ Frequent add/remove at ends    │ ArrayDeque (not LinkedList!)     │
│ Queue/Deque operations         │ ArrayDeque                       │
│ Thread-safe, read-heavy        │ CopyOnWriteArrayList             │
│ Thread-safe, general           │ Collections.synchronizedList    │
│ Memory-efficient               │ ArrayList                        │
│ Immutable list                 │ List.of()                        │
│ Known size upfront             │ new ArrayList<>(capacity)        │
│ Insert in middle frequently    │ Rare - reconsider data structure │
│ Stack operations               │ ArrayDeque                       │
│ Legacy code                    │ Vector (don't use in new code)  │
└───────────────────────────────┴──────────────────────────────────┘
```

---

## 14. QUICK REFERENCE CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║                   JAVA LIST CHEAT SHEET                          ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  ArrayList      → Default choice, O(1) get, array-backed         ║
║  LinkedList     → Doubly LL, O(1) add/remove ends, rarely best   ║
║  Vector         → Legacy, synchronized ArrayList (avoid)         ║
║  Stack          → Legacy, extends Vector (use ArrayDeque)        ║
║  CopyOnWriteAL  → Thread-safe, snapshot iterator, read-heavy     ║
║  List.of()      → Immutable, fixed-size, no null (Java 9+)      ║
║  List.copyOf()  → Unmodifiable copy (Java 10+)                  ║
║                                                                  ║
║  ArrayList:                                                       ║
║  - Default capacity: 10 | Growth: 1.5x                          ║
║  - get/set: O(1) | add(end): O(1)* | add(middle): O(n)          ║
║  - remove: O(n) shifting | contains: O(n)                        ║
║                                                                  ║
║  LinkedList:                                                      ║
║  - addFirst/addLast: O(1) | removeFirst/removeLast: O(1)        ║
║  - get(index): O(n) | contains: O(n)                            ║
║  - Implements List + Deque + Queue                               ║
║                                                                  ║
║  Thread-safe options:                                             ║
║  1. CopyOnWriteArrayList (read-heavy)                            ║
║  2. Collections.synchronizedList (general)                       ║
║  3. List.copyOf() (immutable)                                    ║
║                                                                  ║
║  ⚠️  ArrayList > LinkedList in 90% of cases                      ║
║  ⚠️  Use ArrayDeque instead of LinkedList for queue/deque        ║
║  ⚠️  Use ArrayDeque instead of Stack class                       ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: ArrayList, LinkedList, Vector, Stack, CopyOnWriteArrayList, Interview Questions*
