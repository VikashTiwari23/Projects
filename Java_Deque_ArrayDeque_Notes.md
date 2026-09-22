# Deque & ArrayDeque - Deep Dive (Complete Interview Notes)

---

## 1. DEQUE INTERFACE - COMPLETE

```
    Deque<E> extends Queue<E>

    Deque = Double-Ended Queue
    Pronounced: "DECK" (not D-queue)

    Supports insertion, removal, and examination at BOTH ends.
```

### 1.1 All Deque Methods

```
    ┌─────────────────┬──────────────────────────┬──────────────────────────┐
    │ Operation        │ Throws Exception          │ Returns Special Value    │
    ├─────────────────┼──────────────────────────┼──────────────────────────┤
    │ Insert (head)    │ addFirst(E e)            │ offerFirst(E e)          │
    │ Insert (tail)    │ addLast(E e)             │ offerLast(E e)           │
    │ Remove (head)    │ removeFirst()            │ pollFirst()              │
    │ Remove (tail)    │ removeLast()             │ pollLast()               │
    │ Examine (head)   │ getFirst()               │ peekFirst()              │
    │ Examine (tail)   │ getLast()                │ peekLast()               │
    ├─────────────────┼──────────────────────────┼──────────────────────────┤
    │ Stack operation   │ push(E e) = addFirst(e) │                          │
    │ Stack operation   │ pop() = removeFirst()   │                          │
    │ Queue operation   │ add(E e) = addLast(e)   │ offer(E e) = offerLast(e)│
    │ Queue operation   │ remove() = removeFirst()│ poll() = pollFirst()     │
    │ Queue operation   │ element() = getFirst()  │ peek() = peekFirst()     │
    ├─────────────────┼──────────────────────────┼──────────────────────────┤
    │ Bulk              │ addAll(Collection c)     │                          │
    │ Remove            │ removeFirstOccurrence(o) │ removeLastOccurrence(o) │
    │ Search            │ contains(Object o)       │                          │
    │ Size              │ size()                   │ isEmpty()                │
    │ Iterator          │ iterator()               │ descendingIterator()     │
    │ Clear             │ clear()                  │                          │
    │ Array             │ toArray()                │ toArray(T[] a)           │
    └─────────────────┴──────────────────────────┴──────────────────────────┘
```

### 1.2 Queue Methods Inherited

```
    Deque also provides Queue interface methods:
    ┌─────────────────┬──────────────────────────────────────────────────┐
    │ Queue Method     │ Deque Equivalent                                │
    ├─────────────────┼──────────────────────────────────────────────────┤
    │ add(e)           │ addLast(e)                                       │
    │ offer(e)         │ offerLast(e)                                     │
    │ remove()         │ removeFirst()                                    │
    │ poll()           │ pollFirst()                                      │
    │ element()        │ getFirst()                                       │
    │ peek()           │ peekFirst()                                      │
    └─────────────────┴──────────────────────────────────────────────────┘

    Both Queue AND Deque methods are available on any Deque implementation.
```

---

## 2. ARRAYDEQUE - INTERNAL WORKING (DEEP DIVE)

### 2.1 Source Code Structure

```java
public class ArrayDeque<E> extends AbstractCollection<E>
        implements Deque<E>, Cloneable, Serializable {

    transient Object[] elements;  // internal array
    transient int head;           // index of first element
    transient int tail;           // index AFTER last element

    private static final int MIN_INITIAL_CAPACITY = 8;

    // Capacity is ALWAYS power of 2
    // Uses bitwise AND for circular indexing
}
```

### 2.2 Initialization

```
    ArrayDeque<Integer> deque = new ArrayDeque<>();

    Initial State:
    ┌──────────────────────────────────────────────────────┐
    │  elements = new Object[16]    // default capacity     │
    │  head = 0                                            │
    │  tail = 0                                            │
    │                                                      │
    │  Index:  0    1    2    3   ...   14   15           │
    │        ┌────┬────┬────┬────┬───┬────┬────┐         │
    │        │null│null│null│null│...│null│null│         │
    │        └────┴────┴────┴────┴───┴────┴────┘         │
    │        ↑                                            │
    │     head = tail = 0                                  │
    │     (empty deque)                                    │
    └──────────────────────────────────────────────────────┘

    Constructor Options:
    new ArrayDeque<>()              → capacity = 16
    new ArrayDeque<>(10)            → capacity = 16 (rounds up to power of 2)
    new ArrayDeque<>(collection)    → capacity = smallest power of 2 ≥ collection.size()
```

### 2.3 Circular Array Mechanism

```
    The KEY to ArrayDeque: circular array with bitwise AND

    Normal array (NOT circular):
    ┌──────────────────────────────────────────────────────────┐
    │  Problem: After removing from front, waste space at start│
    │                                                          │
    │  [USED][USED][USED][empty][empty][empty][empty][empty]   │
    │   ↑                                                     │
    │  head=0                                                 │
    │  After removing all, head moves right, space wasted!     │
    └──────────────────────────────────────────────────────────┘

    Circular array (ArrayDeque solution):
    ┌──────────────────────────────────────────────────────────┐
    │  Head wraps around to end, reusing space!                │
    │                                                          │
    │  [50][60][10][20][30][40][_][_][_][_][_][_][_][_][_][_] │
    │    ↑         ↑                                          │
    │   tail=2   head=3                                       │
    │                                                          │
    │  Elements: 10, 20, 30, 40, 50, 60 (in order from head)  │
    │  Head at index 3, wraps around to index 1               │
    └──────────────────────────────────────────────────────────┘

    Index calculation (circular):
    ┌──────────────────────────────────────────────────────────┐
    │  For adding at tail:                                     │
    │    tail = (tail + 1) & (elements.length - 1)            │
    │                                                          │
    │  For adding at head:                                     │
    │    head = (head - 1) & (elements.length - 1)            │
    │                                                          │
    │  Why & works: length is power of 2, so (length-1) is    │
    │  all 1s in binary. AND operation = modulo without divide │
    │                                                          │
    │  Example: length=16 (10000), length-1=15 (01111)        │
    │  (tail + 1) & 15 = (tail + 1) % 16  (faster!)          │
    └──────────────────────────────────────────────────────────┘
```

### 2.4 addLast() - Step by Step

```
    deque.addLast(40);

    BEFORE:
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │null│null│ 10 │ 20 │ 30 │null│null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┘
           ↑              ↑
        head=2          tail=4

    Step 1: Check if full
    ┌─────────────────────────────────────────────┐
    │  if (tail == head)  → resize first!         │
    │  Here tail(4) ≠ head(2), so not full        │
    └─────────────────────────────────────────────┘

    Step 2: Place element at tail
    ┌─────────────────────────────────────────────┐
    │  elements[tail] = 40                        │
    │  elements[4] = 40                           │
    └─────────────────────────────────────────────┘

    Step 3: Advance tail (circular)
    ┌─────────────────────────────────────────────┐
    │  tail = (tail + 1) & (length - 1)           │
    │  tail = (4 + 1) & 15 = 5 & 15 = 5          │
    └─────────────────────────────────────────────┘

    AFTER:
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │null│null│ 10 │ 20 │ 30 │ 40 │null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┘
           ↑                    ↑
        head=2                tail=5

    Size increments by 1.
```

### 2.5 addFirst() - Step by Step

```
    deque.addFirst(5);

    BEFORE:
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │null│null│ 10 │ 20 │ 30 │ 40 │null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┘
           ↑                    ↑
        head=2                tail=5

    Step 1: Move head backward (circular)
    ┌─────────────────────────────────────────────┐
    │  head = (head - 1) & (length - 1)           │
    │  head = (2 - 1) & 15 = 1 & 15 = 1          │
    │  (If head was 0: (0-1) & 15 = -1 & 15 = 15 │
    │   → wraps to end!)                           │
    └─────────────────────────────────────────────┘

    Step 2: Place element at new head
    ┌─────────────────────────────────────────────┐
    │  elements[head] = 5                         │
    │  elements[1] = 5                            │
    └─────────────────────────────────────────────┘

    AFTER:
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │null│ 5  │ 10 │ 20 │ 30 │ 40 │null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┘
          ↑                       ↑
       head=1                   tail=5

    Logical order: 5 → 10 → 20 → 30 → 40
```

### 2.6 removeFirst() - Step by Step

```
    deque.removeFirst();  // Returns 5

    BEFORE:
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │null│ 5  │ 10 │ 20 │ 30 │ 40 │null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┘
          ↑                       ↑
       head=1                   tail=5

    Step 1: Get element at head
    ┌─────────────────────────────────────────────┐
    │  E result = elements[head];  // 5           │
    │  elements[head] = null;  // help GC         │
    └─────────────────────────────────────────────┘

    Step 2: Advance head (circular)
    ┌─────────────────────────────────────────────┐
    │  head = (head + 1) & (length - 1)           │
    │  head = (1 + 1) & 15 = 2                   │
    └─────────────────────────────────────────────┘

    AFTER:
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │null│null│ 10 │ 20 │ 30 │ 40 │null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┘
           ↑                    ↑
        head=2                tail=5

    Returns: 5
```

### 2.7 removeLast() - Step by Step

```
    deque.removeLast();  // Returns 40

    BEFORE:
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │null│null│ 10 │ 20 │ 30 │ 40 │null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┘
           ↑                    ↑
        head=2                tail=5

    Step 1: Move tail backward (circular)
    ┌─────────────────────────────────────────────┐
    │  tail = (tail - 1) & (length - 1)           │
    │  tail = (5 - 1) & 15 = 4                   │
    └─────────────────────────────────────────────┘

    Step 2: Get and remove element
    ┌─────────────────────────────────────────────┐
    │  E result = elements[tail];  // 40          │
    │  elements[tail] = null;  // help GC         │
    └─────────────────────────────────────────────┘

    AFTER:
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │null│null│ 10 │ 20 │ 30 │null│null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┘
           ↑                 ↑
        head=2             tail=4

    Returns: 40
```

---

## 3. RESIZE (DOUBLING) MECHANISM

```
    When deque is full (head == tail):

    BEFORE (capacity=8, full):
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │ 50 │ 60 │ 10 │ 20 │ 30 │ 40 │ 70 │ 80 │
    └────┴────┴────┴────┴────┴────┴────┴────┘
      ↑                                           ↑
    tail=0                                      head=7
    (circular: elements are 80, 50, 60, 10, 20, 30, 40, 70)

    Resize Steps:
    ┌──────────────────────────────────────────────────────────┐
    │ 1. Allocate new array of 2x size (16)                    │
    │ 2. Copy elements in logical order (head → tail)          │
    │ 3. Reset head = 0, tail = old size                       │
    └──────────────────────────────────────────────────────────┘

    AFTER (capacity=16):
    ┌────┬────┬────┬────┬────┬────┬────┬────┬────┬───┬────┬────┐
    │ 80 │ 50 │ 60 │ 10 │ 20 │ 30 │ 40 │ 70 │null│...│null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┴────┴───┴────┴────┘
      ↑                                                           ↑
    head=0                                                     tail=8

    Elements copied in order: 80, 50, 60, 10, 20, 30, 40, 70
    (from logical head to logical tail)
```

### 3.1 Resize Algorithm Detail

```
    private void grow() {
        int newCapacity = elements.length << 1;  // double (bitwise shift)
        // or: elements.length * 2

        Object[] newElements = new Object[newCapacity];

        // Copy in logical order
        int i = head;
        int j = 0;
        while (i != tail) {
            newElements[j++] = elements[i];
            i = (i + 1) & (elements.length - 1);  // circular advance
        }

        elements = newElements;
        head = 0;
        tail = j;  // j = old size
    }

    Why (length << 1)?
    ┌──────────────────────────────────────────────┐
    │  length = 16 = 10000 in binary               │
    │  length << 1 = 100000 = 32                   │
    │  Bitwise shift is faster than multiplication  │
    └──────────────────────────────────────────────┘
```

---

## 4. PUSH/POP (STACK OPERATIONS)

```
    Deque<Integer> stack = new ArrayDeque<>();

    push(10):  // = addFirst(10)
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │null│null│null│null│ 10 │null│null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┘
                             ↑
                          head=4, tail=5

    push(20):  // = addFirst(20)
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │null│null│null│ 20 │ 10 │null│null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┘
                        ↑        ↑
                     head=3   tail=5

    push(30):  // = addFirst(30)
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │null│null│ 30 │ 20 │ 10 │null│null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┘
                     ↑           ↑
                  head=2      tail=5

    Stack visualization:
    ┌────┐
    │ 30 │ ← peek() = getFirst() = 30
    ├────┤
    │ 20 │
    ├────┤
    │ 10 │
    └────┘

    pop():  // = removeFirst() → returns 30
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │null│null│null│ 20 │ 10 │null│null│null│
    └────┴────┴────┴────┴────┴────┴────┴────┘
                        ↑        ↑
                     head=3   tail=5

    peek():  // = peekFirst() → returns 20
    (doesn't modify deque)

    isEmpty():  // head == tail → false
    size():     // 2
```

---

## 5. ITERATOR TRAVERSAL

```
    ArrayDeque<Integer> deque = new ArrayDeque<>();
    deque.addLast(10);
    deque.addLast(20);
    deque.addLast(30);
    deque.addFirst(5);

    Logical order: 5 → 10 → 20 → 30

    Forward Iterator:
    ┌──────────────────────────────────────────────────────┐
    │  iterator() starts at head, moves toward tail        │
    │                                                      │
    │  5 → 10 → 20 → 30                                   │
    │  ↑                                                   │
    │  head                                                │
    └──────────────────────────────────────────────────────┘

    Descending Iterator:
    ┌──────────────────────────────────────────────────────┐
    │  descendingIterator() starts at tail-1, moves to head│
    │                                                      │
    │  30 → 20 → 10 → 5                                   │
    │                   ↑                                  │
    │                   head                               │
    └──────────────────────────────────────────────────────┘

    // Traversal code
    for (Integer i : deque) {
        System.out.println(i);  // 5, 10, 20, 30
    }

    Iterator<Integer> desc = deque.descendingIterator();
    while (desc.hasNext()) {
        System.out.println(desc.next());  // 30, 20, 10, 5
    }
```

---

## 6. ARRAYDEQUE VS LINKEDLIST vs LINKEDDEQUE

```
    ┌──────────────────┬──────────────────┬──────────────────┬──────────────────┐
    │ Feature           │ ArrayDeque       │ LinkedList       │ LinkedDeque      │
    ├──────────────────┼──────────────────┼──────────────────┼──────────────────┤
    │ Internal          │ Circular Array   │ Doubly Linked LL │ Doubly Linked LL │
    │ Implements        │ Deque only       │ List + Deque     │ Deque only       │
    │ Null elements     │ NOT allowed      │ Allowed          │ NOT allowed      │
    │ Memory per elem   │ ~4 bytes (ref)   │ ~24 bytes (node) │ ~24 bytes (node) │
    │ Cache behavior    │ Excellent (contiguous)│ Poor (scattered)│ Poor (scattered)│
    │ Random access     │ O(n)             │ O(n)             │ O(n)             │
    │ addFirst/addLast  │ O(1)*            │ O(1)             │ O(1)             │
    │ removeFirst       │ O(1)             │ O(1)             │ O(1)             │
    │ removeLast        │ O(1)             │ O(1)             │ O(1)             │
    │ contains          │ O(n)             │ O(n)             │ O(n)             │
    │ Memory overhead   │ Low              │ High (node objects)│ High            │
    │ GC pressure       │ Low              │ High             │ High             │
    │ Thread-safe       │ No               │ No               │ No               │
    │ Recommended for   │ Queue/Deque/Stack│ List + Deque mix │ Never use        │
    └──────────────────┴──────────────────┴──────────────────┴──────────────────┘

    * O(1) amortized (occasional O(n) during resize)

    WHY ARRAYDEQUE WINS:
    ┌──────────────────────────────────────────────────────────────────┐
    │  1. Contiguous memory → CPU cache lines loaded efficiently      │
    │  2. No node objects → less GC overhead                          │
    │  3. Bitwise AND for indexing → faster than modulo               │
    │  4. Memory: 4 bytes/element vs 24 bytes/element (LinkedList)   │
    │  5. 2-3x faster in benchmarks for queue/stack operations       │
    └──────────────────────────────────────────────────────────────────┘
```

---

## 7. WHEN LINKEDLIST WINS (Rare Cases)

```
    LinkedList beats ArrayDeque ONLY when:
    ┌──────────────────────────────────────────────────────────────────┐
    │  1. You need to implement BOTH List and Deque interface         │
    │  2. You need to add/remove at middle of list (not queue ops)    │
    │  3. You need null elements in a Deque                           │
    │  4. Memory is not a concern and you add millions of elements    │
    │     without resizing (LinkedList pre-allocates nodes lazily)    │
    └──────────────────────────────────────────────────────────────────┘

    But for pure Queue/Deque/Stack usage:
    → ArrayDeque ALWAYS wins
```

---

## 8. DEQUE DESIGN PATTERNS

### 8.1 Browser History

```java
class BrowserHistory {
    Deque<String> backStack = new ArrayDeque<>();
    Deque<String> forwardStack = new ArrayDeque<>();
    String currentPage;

    public void visit(String url) {
        if (currentPage != null) backStack.push(currentPage);
        currentPage = url;
        forwardStack.clear();  // Clear forward history on new visit
    }

    public String back() {
        if (backStack.isEmpty()) return currentPage;
        forwardStack.push(currentPage);
        currentPage = backStack.pop();
        return currentPage;
    }

    public String forward() {
        if (forwardStack.isEmpty()) return currentPage;
        backStack.push(currentPage);
        currentPage = forwardStack.pop();
        return currentPage;
    }
}
```

```
    Visit A, B, C:
    backStack: [A][B]  currentPage: C  forwardStack: []

    Press Back:
    backStack: [A]  currentPage: B  forwardStack: [C]

    Press Back:
    backStack: []  currentPage: A  forwardStack: [C][B]

    Visit D:
    backStack: [A]  currentPage: D  forwardStack: []  ← cleared!

    Press Forward:
    backStack: [A][D]  currentPage: ?  ← nothing to go forward to
```

### 8.2 Undo/Redo

```java
class TextEditor {
    Deque<String> undoStack = new ArrayDeque<>();
    Deque<String> redoStack = new ArrayDeque<>();
    StringBuilder current = new StringBuilder();

    public void type(String text) {
        undoStack.push(current.toString());
        current.append(text);
        redoStack.clear();
    }

    public void undo() {
        if (undoStack.isEmpty()) return;
        redoStack.push(current.toString());
        current = new StringBuilder(undoStack.pop());
    }

    public void redo() {
        if (redoStack.isEmpty()) return;
        undoStack.push(current.toString());
        current = new StringBuilder(redoStack.pop());
    }
}
```

### 8.3 Sliding Window (Monotonic Deque)

```java
// Maximum of each window of size k
int[] maxSlidingWindow(int[] nums, int k) {
    Deque<Integer> deque = new ArrayDeque<>();  // stores indices
    int[] result = new int[nums.length - k + 1];

    for (int i = 0; i < nums.length; i++) {
        // Remove indices outside window
        while (!deque.isEmpty() && deque.peekFirst() <= i - k) {
            deque.pollFirst();
        }

        // Remove smaller elements (they're useless)
        while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
            deque.pollLast();
        }

        deque.offerLast(i);

        // Window is ready
        if (i >= k - 1) {
            result[i - k + 1] = nums[deque.peekFirst()];
        }
    }
    return result;
}
```

```
    nums = [1, 3, -1, -3, 5, 3, 6, 7], k = 3

    Window [1,3,-1]: deque=[1]     → max=3
    Window [3,-1,-3]: deque=[3]    → max=3
    Window [-1,-3,5]: deque=[5]    → max=5
    Window [-3,5,3]: deque=[5,3]   → max=5
    Window [5,3,6]: deque=[6]      → max=6
    Window [3,6,7]: deque=[7]      → max=7

    Result: [3, 3, 5, 5, 6, 7]
```

### 8.4 Palindrome Check

```java
boolean isPalindrome(String s) {
    Deque<Character> deque = new ArrayDeque<>();
    for (char c : s.toCharArray()) {
        deque.addLast(c);
    }

    while (deque.size() > 1) {
        if (deque.pollFirst() != deque.pollLast()) {
            return false;
        }
    }
    return true;
}
```

```
    "racecar"
    deque: [r][a][c][e][c][a][r]

    Step 1: r == r ✓  → deque: [a][c][e][c][a]
    Step 2: a == a ✓  → deque: [c][e][c]
    Step 3: c == c ✓  → deque: [e]
    → Palindrome!
```

---

## 9. PERFORMANCE BENCHMARKS

```
    ┌────────────────────────┬──────────────┬──────────────┬──────────────┐
    │ Operation (1M elements)│ ArrayDeque   │ LinkedList   │ Stack        │
    ├────────────────────────┼──────────────┼──────────────┼──────────────┤
    │ addLast (1M)           │ 8 ms         │ 35 ms        │ N/A          │
    │ addFirst (1M)          │ 7 ms         │ 33 ms        │ N/A          │
    │ removeFirst (1M)       │ 5 ms         │ 28 ms        │ N/A          │
    │ removeLast (1M)        │ 5 ms         │ 30 ms        │ N/A          │
    │ push (1M)              │ 7 ms         │ 33 ms        │ 42 ms        │
    │ pop (1M)               │ 5 ms         │ 28 ms        │ 35 ms        │
    │ contains (search 1M)   │ 12 ms        │ 25 ms        │ 30 ms        │
    │ Memory (1M elements)   │ ~8 MB        │ ~56 MB       │ ~56 MB       │
    ├────────────────────────┼──────────────┼──────────────┼──────────────┤
    │ GC pauses              │ Minimal      │ Frequent     │ Frequent     │
    │ Cache misses           │ Very Low     │ Very High    │ Very High    │
    └────────────────────────┴──────────────┴──────────────┴──────────────┘

    Key takeaway: ArrayDeque is 2-5x faster and uses 7x less memory
```

---

## 10. DEQUE & ARRAYDEQUE - INTERVIEW QUESTIONS (30+)

### ⭐ BASIC

**Q1: What does Deque stand for?**
> Double-Ended Queue. Supports insertion and removal from both ends.

**Q2: Can Deque be used as Stack?**
> Yes. push() = addFirst(), pop() = removeFirst(), peek() = peekFirst(). Recommended over legacy Stack class.

**Q3: Can Deque be used as Queue?**
> Yes. offer() = offerLast(), poll() = pollFirst(), peek() = peekFirst().

**Q4: Why is ArrayDeque better than LinkedList for Queue?**
```
1. Contiguous memory → better cache performance
2. No node objects → less GC pressure
3. ~4 bytes/element vs ~24 bytes/element
4. 2-5x faster in benchmarks
```

**Q5: Why ArrayDeque doesn't allow null?**
> Null is used as sentinel to mark empty slots. If null were allowed, couldn't distinguish empty slot from null element.

**Q6: What is circular array?**
> Array where index wraps around to 0 after reaching end. Uses (index + 1) & (length - 1) for circular increment. Avoids shifting elements.

**Q7: Why capacity is always power of 2?**
> Enables bitwise AND for circular indexing instead of modulo. (index + 1) & (length - 1) is equivalent to (index + 1) % length but much faster.

**Q8: What is the default capacity of ArrayDeque?**
> 16. If you specify a size, it rounds up to next power of 2.

**Q9: Can ArrayDeque be thread-safe?**
> No. Use Collections.synchronizedCollection() or ConcurrentLinkedDeque for thread safety.

**Q10: Can we iterate ArrayDeque backwards?**
> Yes. Use descendingIterator() or deque.descendingIterator().

---

### ⭐⭐ MIDDLE

**Q11: How ArrayDeque handles full array?**
```
1. When head == tail after addLast (or before addFirst)
2. Allocate new array with 2x capacity
3. Copy elements in logical order (head to tail)
4. Reset head = 0, tail = size
5. Amortized O(1) because resize happens rarely
```

**Q12: What is the time complexity of ArrayDeque operations?**
```
┌──────────────────┬──────────────┐
│ Operation         │ Complexity   │
├──────────────────┼──────────────┤
│ addFirst/addLast  │ O(1) amortized│
│ removeFirst/removeLast│ O(1)    │
│ peekFirst/peekLast│ O(1)        │
│ contains          │ O(n)         │
│ size              │ O(1)         │
│ toArray           │ O(n)         │
│ grow (resize)     │ O(n)         │
└──────────────────┴──────────────┘
```

**Q13: What is difference between peek() and element()?**
```
peek():    Returns null if deque is empty (safe)
element(): Throws NoSuchElementException if deque is empty
Always prefer peek() for safety.
```

**Q14: What is difference between poll() and remove()?**
```
poll():    Returns null if deque is empty (safe)
remove():  Throws NoSuchElementException if deque is empty
Always prefer poll() for safety.
```

**Q15: Can we add null to ArrayDeque?**
> No. Throws NullPointerException. ArrayDeque uses null internally as sentinel for empty slots.

**Q16: What happens if you call pop() on empty ArrayDeque?**
> Throws NoSuchElementException. Use poll() which returns null instead.

**Q17: What is the difference between add() and offer()?**
```
add():   Throws IllegalStateException if deque is full (shouldn't happen for ArrayDeque)
offer(): Returns false if deque is full (shouldn't happen for ArrayDeque)
Both = addLast(). For ArrayDeque, they behave the same since it's unbounded.
```

**Q18: How to check if deque is empty?**
```java
deque.isEmpty();      // Returns boolean
deque.size() == 0;    // Also works
// Always prefer isEmpty() - O(1) and clearer intent
```

**Q19: How to convert ArrayDeque to List?**
```java
ArrayDeque<Integer> deque = new ArrayDeque<>(Arrays.asList(1, 2, 3));
List<Integer> list = new ArrayList<>(deque);  // Maintains order
```

**Q20: How to find size of ArrayDeque?**
```java
deque.size();  // O(1) - stored as instance variable
```

**Q21: What is difference between push() and addFirst()?**
```
push(E e):     Calls addFirst(e), throws exception if capacity exceeded (won't happen for ArrayDeque)
addFirst(E e): Same as push, throws exception on capacity
In ArrayDeque: Both are identical.
```

**Q22: What is the relationship between head and tail?**
```
head = index of first element
tail = index AFTER last element (next available slot)

Empty:     head == tail
Has items: head != tail (logically)
Full:      head == tail (after resize was needed)
```

**Q23: How does circular wrap work for head=0?**
```
head = (head - 1) & (length - 1)
If head = 0:
  head = (0 - 1) & 15 = -1 & 15 = 15 (wraps to end!)

Binary: -1 in two's complement = 11111111111111111111111111111111
        15 = 00000000000000000000000000001111
        AND = 00000000000000000000000000001111 = 15
```

**Q24: Why ArrayDeque is called "amortized O(1)"?**
```
Most operations are O(1).
Resize operation is O(n) - copies all elements.
But resize happens only when adding the (n+1)th element after n elements.
Total cost of n adds: O(n) for copies + O(n) for adds = O(2n) = O(n)
Amortized per operation: O(n) / n = O(1)
```

**Q25: What is the memory layout of ArrayDeque?**
```
┌──────────────────────────────────────────────────────────┐
│ ArrayDeque Object                                        │
│ ├── elements (Object[]) ──→ [ref1][ref2][ref3]...       │
│ ├── head (int) = index of first element                  │
│ ├── tail (int) = index after last element                │
│ └── size (inherited from AbstractCollection)             │
│                                                          │
│ Each element in Object[]: reference to actual object     │
│ Array size: always power of 2 (8, 16, 32, 64, ...)      │
└──────────────────────────────────────────────────────────┘
```

---

### ⭐⭐⭐ ADVANCED

**Q26: Implement Queue using ArrayDeque?**
```java
class MyQueue<E> {
    private Deque<E> deque = new ArrayDeque<>();

    public void enqueue(E e) { deque.offerLast(e); }
    public E dequeue() { return deque.pollFirst(); }
    public E peek() { return deque.peekFirst(); }
    public boolean isEmpty() { return deque.isEmpty(); }
    public int size() { return deque.size(); }
}
```

**Q27: Implement Stack using ArrayDeque?**
```java
class MyStack<E> {
    private Deque<E> deque = new ArrayDeque<>();

    public void push(E e) { deque.push(e); }
    public E pop() { return deque.pop(); }
    public E peek() { return deque.peek(); }
    public boolean isEmpty() { return deque.isEmpty(); }
    public int size() { return deque.size(); }
}
```

**Q28: What is the difference between Iterator and ListIterator on ArrayDeque?**
```
Iterator:      Forward only, next() and remove()
ListIterator:  NOT available on ArrayDeque (only on List)
ArrayDeque:    Has descendingIterator() for reverse traversal
```

**Q29: How to synchronize ArrayDeque?**
```java
Deque<Integer> deque = new ArrayDeque<>();
Deque<Integer> syncDeque = Collections.synchronizedDeque(deque);

// Or manually
synchronized (deque) {
    deque.add(1);
    deque.remove();
}
```

**Q30: What is the difference between ArrayDeque and ArrayList?**
```
┌──────────────────┬──────────────────┬──────────────────┐
│ Feature           │ ArrayDeque       │ ArrayList        │
├──────────────────┼──────────────────┼──────────────────┤
│ Interface         │ Deque            │ List             │
│ add/remove head   │ O(1)             │ O(n) (shifting)  │
│ add/remove tail   │ O(1)             │ O(1) amortized   │
│ get(index)        │ O(n)             │ O(1)             │
│ Null allowed      │ No               │ Yes              │
│ Circular array    │ Yes              │ No               │
│ Queue/Stack ops   │ Optimized        │ Not optimized    │
│ Random access     │ No               │ Yes              │
└──────────────────┴──────────────────┴──────────────────┘
```

**Q31: Can ArrayDeque be used as circular buffer?**
> Yes. That's exactly what it is internally. The head and tail pointers create a circular buffer pattern.

```
    Example: circular buffer for streaming data
    ┌────┬────┬────┬────┬────┬────┬────┬────┐
    │ D  │ E  │ F  │ A  │ B  │ C  │ _  │ _  │
    └────┴────┴────┴────┴────┴────┴────┴────┘
              ↑                ↑
           tail=2           head=3

    New element overwrites oldest (addFirst when full → overwrites tail)
```

**Q32: What is the internal resize cost?**
```
Resize copies n elements. But:
- Capacity doubles each time
- Total copies: 1 + 2 + 4 + 8 + ... + n = 2n - 1
- Amortized cost per add: O(2n - 1) / n = O(1)
- Same as ArrayList amortized analysis
```

**Q33: What is difference between removeFirstOccurrence and remove?**
```
removeFirstOccurrence(Object o): Removes first occurrence of specified element
remove(Object o):               Same as removeFirstOccurrence (Deque method)
Both are identical for Deque.
```

**Q34: What is difference between removeLastOccurrence and removeLast?**
```
removeLastOccurrence(Object o): Removes LAST occurrence of specified element
removeLast():                   Removes the last ELEMENT (regardless of value)
Completely different operations!
```

**Q35: How to find middle element of ArrayDeque?**
```java
ArrayDeque<Integer> deque = new ArrayDeque<>(Arrays.asList(1, 2, 3, 4, 5));
int size = deque.size();
int mid = size / 2;

Iterator<Integer> it = deque.iterator();
for (int i = 0; i < mid; i++) it.next();
int middle = it.next();  // 3
```

**Q36: What is the use of toArray() in ArrayDeque?**
```java
ArrayDeque<Integer> deque = new ArrayDeque<>(Arrays.asList(1, 2, 3));
Integer[] arr = deque.toArray(Integer[]::new);  // [1, 2, 3]
Object[] arr2 = deque.toArray();                // [1, 2, 3]
```

**Q37: What is the difference between Deque.add() and Deque.offer()?**
```
For ArrayDeque: Both identical (unbounded, never fails)
For bounded Deque (ArrayBlockingQueue): add() throws, offer() returns false
Always use offer() for portability across implementations.
```

**Q38: How does ArrayDeque handle memory leaks?**
```
When element is removed:
  elements[head] = null  (removeFirst)
  elements[tail-1] = null  (removeLast)

This allows GC to collect removed objects.
Without nulling, Array would hold references forever.
```

**Q39: What is difference between ArrayDeque.clone() and copy constructor?**
```java
ArrayDeque<Integer> original = new ArrayDeque<>(Arrays.asList(1, 2, 3));
ArrayDeque<Integer> clone1 = original.clone();           // Shallow clone
ArrayDeque<Integer> clone2 = new ArrayDeque<>(original); // Shallow copy
// Both create new deque with same elements (references, not deep copy)
```

**Q40: What are the edge cases in ArrayDeque?**
```
1. Empty deque: head == tail, poll/peek returns null, remove/element throws
2. One element: head + 1 == tail
3. Full array: resize needed (head == tail after potential add)
4. Wrapping: head at end, tail at beginning (circular behavior)
5. After many add/remove cycles: no memory leak (null on remove)
```

---

## 11. QUICK REFERENCE CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║              DEQUE & ARRAYDEQUE CHEAT SHEET                      ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  Deque = Double-Ended Queue (pronounced "DECK")                  ║
║  ArrayDeque = Fastest Queue/Deque/Stack implementation           ║
║                                                                  ║
║  STRUCTURE: Circular Array (Object[] + head + tail)              ║
║  CAPACITY: Always power of 2 (default 16)                        ║
║  NULL: NOT allowed (null used as empty sentinel)                 ║
║  THREAD-SAFE: No (use Collections.synchronizedDeque)            ║
║                                                                  ║
║  QUEUE (FIFO):  offerLast + pollFirst + peekFirst               ║
║  STACK (LIFO):  push(addFirst) + pop(removeFirst) + peek       ║
║                                                                  ║
║  SAFE METHODS:  offer / poll / peek  → returns null/false        ║
║  THROW METHODS: add / remove / getFirst → throws exception      ║
║                                                                  ║
║  addFirst:  head = (head - 1) & (length - 1)                    ║
║  addLast:   elements[tail] = e; tail = (tail + 1) & (length - 1)│
║  removeFirst: head = (head + 1) & (length - 1)                  ║
║  removeLast:  tail = (tail - 1) & (length - 1)                  ║
║                                                                  ║
║  Resize: 2x capacity when full (copy in logical order)          ║
║  Amortized O(1) for add/remove                                  ║
║                                                                  ║
║  > ArrayDeque: Use for Queue/Deque/Stack (2-5x faster)          ║
║  > LinkedList: Only if need null elements or List+Deque combo   ║
║  > Stack class: Avoid, use ArrayDeque instead                    ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: Internal Implementation, Circular Array, All Operations, Interview Questions*
