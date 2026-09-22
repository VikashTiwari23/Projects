# Java Collections Framework - QUEUE & DEQUE (Complete Interview Notes)

---

## 1. QUEUE INTERFACE HIERARCHY (UML)

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
                     │      Queue       │
                     │──────────────────│
                     │ add(E)           │  → throws exception
                     │ offer(E)         │  → returns false
                     │ remove()         │  → throws exception
                     │ poll()           │  → returns null
                     │ element()        │  → throws exception
                     │ peek()           │  → returns null
                     └────────┬─────────┘
                              │
                              │
                     ┌────────▼─────────┐
                     │      Deque       │
                     │ (Double-Ended)   │
                     │──────────────────│
                     │ addFirst(E)      │
                     │ addLast(E)       │
                     │ removeFirst()    │
                     │ removeLast()     │
                     │ peekFirst()      │
                     │ peekLast()       │
                     │ offerFirst(E)    │
                     │ offerLast(E)     │
                     │ pollFirst()      │
                     │ pollLast()       │
                     └────────┬─────────┘
                              │
         ┌────────────────────┼──────────────────────┐
         │                    │                      │
         ▼                    ▼                      ▼
  ┌──────────────┐   ┌──────────────┐     ┌──────────────────┐
  │  ArrayDeque  │   │  LinkedList  │     │  BlockingDeque   │
  │  (Resizable  │   │  (DLL)       │     │  (Interface)     │
  │   Array)     │   │              │     │                  │
  └──────────────┘   └──────────────┘     └──────────────────┘
         │
         │  Implements both Queue AND Deque
         │  Fastest queue/deque implementation
         ▼
  ┌──────────────────────────────────────────┐
  │  PriorityQueue                           │
  │  (Heap-based, NOT same as ArrayDeque)    │
  │  Sorted by natural ordering / Comparator │
  └──────────────────────────────────────────┘
```

---

## 2. QUEUE - FUNDAMENTAL CONCEPT

### 2.1 FIFO (First-In-First-Out)

```
    Queue<Integer> queue = new LinkedList<>();
    queue.add(10);
    queue.add(20);
    queue.add(30);

    Visual:
    ENQUEUE (add)                            DEQUEUE (remove)
         ↓                                        ↓
    ┌────────────────────────────────────────────────┐
    │                                                │
    │   [10]  →  [20]  →  [30]                       │
    │   FRONT                    REAR                │
    │   (head)                   (tail)              │
    │                                                │
    └────────────────────────────────────────────────┘

    add(40):
    ┌────────────────────────────────────────────────┐
    │   [10]  →  [20]  →  [30]  →  [40]              │
    │   FRONT                         REAR           │
    └────────────────────────────────────────────────┘

    remove():
    Returns 10 (first element removed):
    ┌────────────────────────────────────────────────┐
    │               [20]  →  [30]  →  [40]           │
    │   FRONT                         REAR           │
    └────────────────────────────────────────────────┘
```

---

## 3. QUEUE METHODS - TWO STYLES

```
    ┌─────────────────┬────────────────────────┬────────────────────────┐
    │ Operation        │ Throws Exception       │ Returns Special Value  │
    ├─────────────────┼────────────────────────┼────────────────────────┤
    │ Insert           │ add(E e)               │ offer(E e)             │
    │                  │ → throws IllegalStateException│ → returns false   │
    ├─────────────────┼────────────────────────┼────────────────────────┤
    │ Remove           │ remove()               │ poll()                 │
    │                  │ → throws NoSuchElementException│ → returns null  │
    ├─────────────────┼────────────────────────┼────────────────────────┤
    │ Examine          │ element()              │ peek()                 │
    │                  │ → throws NoSuchElementException│ → returns null  │
    └─────────────────┴────────────────────────┴────────────────────────┘

    RULE OF THUMB:
    ┌────────────────────────────────────────────────────────────────┐
    │  add/remove/element  → Use in bounded queues (throws on fail) │
    │  offer/poll/peek     → Use in unbounded/null-safe scenarios   │
    │  ALWAYS prefer offer/poll/peek for safety                     │
    └────────────────────────────────────────────────────────────────┘
```

---

## 4. ARRAYDEQUE - FASTEST QUEUE/DEQUE

### 4.1 Internal Structure

```
    ArrayDeque<Integer> deque = new ArrayDeque<>();
    deque.add(10);
    deque.add(20);
    deque.add(30);

    Internal: Circular Array (Object[])
    ┌──────────────────────────────────────────────────────────┐
    │  Object[] elements (capacity = 16 initially)             │
    │                                                          │
    │  Index:  0    1    2    3    4   ...  14   15           │
    │        ┌────┬────┬────┬────┬────┬───┬────┬────┐         │
    │        │null│null│ 10 │ 20 │ 30 │...│null│null│         │
    │        └────┴────┴────┴────┴────┴───┴────┴────┘         │
    │              ↑           ↑                               │
    │           head=2      tail=4                             │
    │                                                          │
    │  head = index of first element                           │
    │  tail = index AFTER last element                         │
    └──────────────────────────────────────────────────────────┘
```

### 4.2 Circular Array Behavior

```
    When head/tail reach end → wraps around to 0

    Adding elements:
    ┌────┬────┬────┬────┬────┐
    │ 30 │ 40 │ 10 │ 20 │ 30 │   ← tail wraps around!
    └────┴────┴────┴────┴────┘
     ↑                   ↑
    tail=0             head=3

    Index calculation (with bitwise AND for circular wrap):
    head = (head - 1) & (elements.length - 1)   // for addFirst
    tail = (tail + 1) & (elements.length - 1)   // for addLast

    This works because length is always power of 2!
```

### 4.3 addFirst() vs addLast()

```
    ArrayDeque<Integer> deque = new ArrayDeque<>();

    addFirst(10):
    ┌─────────────────────────────────────────────────────┐
    │  [  _ ] [  _ ] [  _ ] [ 10 ] [  _ ] [  _ ]         │
    │         ↑                                           │
    │       head = tail = 3                               │
    └─────────────────────────────────────────────────────┘

    addLast(20):
    ┌─────────────────────────────────────────────────────┐
    │  [  _ ] [  _ ] [  _ ] [ 10 ] [ 20 ] [  _ ]         │
    │         ↑                 ↑                         │
    │       head=3            tail=4                      │
    └─────────────────────────────────────────────────────┘

    addFirst(5):
    ┌─────────────────────────────────────────────────────┐
    │  [  _ ] [  5 ] [  _ ] [ 10 ] [ 20 ] [  _ ]         │
    │         ↑         ↑                                 │
    │       head=1    tail=4                              │
    └─────────────────────────────────────────────────────┘

    removeFirst():
    Returns 5, head moves forward:
    ┌─────────────────────────────────────────────────────┐
    │  [  _ ] [  _ ] [  _ ] [ 10 ] [ 20 ] [  _ ]         │
    │                     ↑           ↑                    │
    │                   head=3      tail=4                 │
    └─────────────────────────────────────────────────────┘
```

---

## 5. PRIORITYQUEUE - HEAP BASED

### 5.1 Min-Heap (Default)

```
    PriorityQueue<Integer> pq = new PriorityQueue<>();
    pq.add(50);
    pq.add(30);
    pq.add(70);
    pq.add(10);
    pq.add(40);

    Internal: Binary Heap (Min-Heap)
                    ┌─────────┐
                    │   10    │  ← root (always minimum)
                    └────┬────┘
                   ┌─────┴──────┐
              ┌────▼────┐  ┌────▼────┐
              │   30    │  │   40    │
              └────┬────┘  └────┬────┘
             ┌─────┴─────┐  ┌───┴─────┐
        ┌────▼────┐ ┌────▼────┐
        │   50    │ │   70    │
        └─────────┘ └─────────┘

    poll() → 10 (minimum removed first)
    poll() → 30
    poll() → 40
    poll() → 50
    poll() → 70

    ⚠️ NOT same as sorted! Just guarantees minimum at root.
    Iterator does NOT give sorted order. Use poll() for sorted extraction.
```

### 5.2 Max-Heap

```java
// Reverse order for max-heap
PriorityQueue<Integer> maxPQ = new PriorityQueue<>(Comparator.reverseOrder());
maxPQ.add(50);
maxPQ.add(30);
maxPQ.add(70);

// Internal:
//         ┌─────────┐
//         │   70    │  ← root (maximum)
//         └────┬────┘
//        ┌─────┴──────┐
//   ┌────▼────┐  ┌────▼────┐
//   │   50    │  │   30    │
//   └─────────┘  └─────────┘

maxPQ.poll() → 70, 50, 30
```

### 5.3 Heap Operations Diagram

```
    Add 25 to min-heap:
    
    Step 1: Add at end          Step 2: Bubble up (heapify)
         ┌─────────┐                ┌─────────┐
         │   10    │                │   10    │
         └────┬────┘                └────┬────┘
        ┌─────┴──────┐              ┌─────┴──────┐
   ┌────▼────┐  ┌────▼────┐    ┌────▼────┐  ┌────▼────┐
   │   30    │  │   40    │    │   30    │  │   25    │  ← swapped!
   └────┬────┘  └────┬────┘    └────┬────┘  └────┬────┘
  ┌─────┴─────┐ ┌────▼────┐   ┌─────┴─────┐ ┌────▼────┐
  │   50     │ │   70    │   │   50     │ │   70    │
  └────┬─────┘ └─────────┘   └────┬─────┘ └─────────┘
  ┌────▼────┐                      │
  │   25    │ ← added here        25 > 10? YES, stop.
  └─────────┘                      Min-heap property restored.
```

---

## 6. LINKEDLIST AS QUEUE/DEQUE

```
    LinkedList<Integer> list = new LinkedList<>();
    // Implements both Queue<E> and Deque<E>

    Queue<Integer> queue = list;     // As Queue
    Deque<Integer> deque = list;     // As Deque

Internal: Doubly Linked List
    ┌─────────┐    ┌─────────┐    ┌─────────┐
    │ null    │←──→│ 10      │←──→│ 20      │←──→│ null    │
    │ (head)  │    │         │    │         │    │ (tail)  │
    └─────────┘    └─────────┘    └─────────┘    └─────────┘

    addFirst(5):
    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
    │ null    │←──→│ 5       │←──→│ 10      │←──→│ 20      │←──→│ null    │
    │         │    │ (head)  │    │         │    │ (tail)  │    │         │
    └─────────┘    └─────────┘    └─────────┘    └─────────┘    └─────────┘

    addLast(25):
    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
    │ null    │←──→│ 5       │←──→│ 10      │←──→│ 20      │←──→│ 25      │←──→│ null    │
    └─────────┘    └─────────┘    └─────────┘    └─────────┘    └─────────┘
                                   ↑ (head)                      ↑ (tail)
```

---

## 7. BLOCKINGQUEUE (Producer-Consumer Pattern)

```
    ┌──────────────────────────────────────────────────────────────┐
    │  BlockingQueue Interface (java.util.concurrent)              │
    │                                                              │
    │  Operations that BLOCK until completed:                      │
    │  ┌──────────────────┬──────────────────────────────────┐    │
    │  │ Throws Exception │ Returns Special Value            │    │
    ├──────────────────┼──────────────────────────────────┤    │
    │  │ Throws Exception │ Returns Special Value            │    │
    │  ├─────────────────┼──────────────────────────────────┤    │
    │  │ Insert: add(e)  │ offer(e) - returns false         │    │
    │  │         put(e)  │ offer(e, time, unit) - waits     │    │
    │  ├─────────────────┼──────────────────────────────────┤    │
    │  │ Remove: remove()│ poll() - returns null            │    │
    │  │         take()  │ poll(time, unit) - waits         │    │
    │  ├─────────────────┼──────────────────────────────────┤    │
    │  │ Examine:element │ peek() - returns null            │    │
    │  └─────────────────┴──────────────────────────────────┘    │
    └──────────────────────────────────────────────────────────────┘

    Implementations:
    ┌────────────────────┬────────────────────────────────────────┐
    │ ArrayBlockingQueue │ Fixed-size circular array, bounded     │
    │ LinkedBlockingQueue│ Linked list, optionally bounded        │
    │ PriorityBlockingQueue │ Min-heap, unbounded                │
    │ SynchronousQueue   │ Zero-size, handoff (no storage)       │
    │ LinkedTransferQueue│ Unbounded, transfer support            │
    │ DelayQueue        │ Elements become eligible after delay   │
    └────────────────────┴────────────────────────────────────────┘
```

### 7.1 Producer-Consumer Pattern

```
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  ┌──────────┐    ┌───────────────────┐    ┌──────────┐      │
    │  │ PRODUCER │───→│ BlockingQueue      │───→│ CONSUMER │      │
    │  │          │    │ [A][B][C][D][E]... │    │          │      │
    │  │ put(data)│    │                    │    │ take()   │      │
    │  │          │    │ Full → blocks put  │    │          │      │
    │  │          │    │ Empty → blocks take│    │          │      │
    │  └──────────┘    └───────────────────┘    └──────────┘      │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘

    ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

    // Producer Thread
    Thread producer = new Thread(() -> {
        int i = 0;
        while (true) {
            queue.put(i++);     // Blocks if full
            System.out.println("Produced: " + i);
        }
    });

    // Consumer Thread
    Thread consumer = new Thread(() -> {
        while (true) {
            int val = queue.take();  // Blocks if empty
            System.out.println("Consumed: " + val);
        }
    });
```

---

## 8. DEQUE - DOUBLE ENDED QUEUE

```
    Deque<String> deque = new ArrayDeque<>();

    Deque can work as:
    1. QUEUE (FIFO)  - addLast + removeFirst
    2. STACK (LIFO)  - push + pop (or addFirst + removeFirst)

    ┌──────────────────────────────────────────────────────────────┐
    │                    DEQUE OPERATIONS                          │
    ├──────────────────────────────────────────────────────────────┤
    │                                                              │
    │   addFirst(E)  ←──  [  A  ]  [  B  ]  [  C  ]  ──→  addLast(E)  │
    │   removeFirst()←──              │              ──→  removeLast() │
    │   peekFirst()  ←──              ↓              ──→  peekLast()   │
    │                           FRONT → REAR                        │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

### 8.1 Deque as Stack

```java
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(10);     // addFirst
    stack.push(20);     // addFirst
    stack.push(30);     // addFirst

    // Stack:
    //    ┌────┐
    //    │ 30 │ ← top (peek)
    //    ├────┤
    //    │ 20 │
    //    ├────┤
    //    │ 10 │
    //    └────┘

    stack.pop();    // removeFirst → 30
    stack.peek();   // peekFirst → 20
    stack.isEmpty(); // false
    stack.size();    // 2
```

### 8.2 Deque vs Stack Class

```
    ┌──────────────────────┬──────────────────────────────────────┐
    │ ArrayDeque (as Stack)│ Stack (legacy)                       │
    ├──────────────────────┼──────────────────────────────────────┤
    │ Not thread-safe      │ Thread-safe (synchronized)           │
    │ Faster               │ Slower                               │
    │ No legacy methods    │ Has legacy methods (search, etc.)    │
    │ Recommended (JDK 6+) │ Avoid (legacy from JDK 1.0)         │
    │ Can also be Queue    │ Only Stack operations                │
    └──────────────────────┴──────────────────────────────────────┘

    Java 6+ Recommendation:
    // OLD (avoid)
    Stack<Integer> stack = new Stack<>();

    // NEW (recommended)
    Deque<Integer> stack = new ArrayDeque<>();
```

---

## 9. QUEUE IMPLEMENTATIONS COMPARISON

```
    ┌──────────────────┬──────────────────┬──────────────┬──────────────┬──────────────┐
    │ Feature           │ ArrayDeque       │ LinkedList   │ PriorityQueue│ ArrayBlockQ  │
    ├──────────────────┼──────────────────┼──────────────┼──────────────┼──────────────┤
    │ Structure         │ Circular Array   │ Doubly LL    │ Binary Heap  │ Circular Arr│
    │ Resizable         │ Yes (2x)         │ Yes          │ Yes          │ Fixed        │
    │ Bounded           │ No (unbounded)   │ No           │ No           │ Yes          │
    │ Null elements     │ NO               │ YES          │ NO           │ NO           │
    │ Order             │ FIFO             │ FIFO         │ Priority     │ FIFO         │
    │ Sorted            │ No               │ No           │ Yes (min)    │ No           │
    │ Thread-safe       │ No               │ No           │ No           │ Yes          │
    │ Blocking          │ No               │ No           │ No           │ Yes          │
    │ Best for          │ Queue/Deque      │ Deque        │ Min/Max      │ Concurrency  │
    │ add time          │ O(1) amortized   │ O(1)         │ O(log n)     │ O(1)         │
    │ remove time       │ O(1)             │ O(1)         │ O(log n)     │ O(1)         │
    │ peek time         │ O(1)             │ O(1)         │ O(1)         │ O(1)         │
    │ Memory            │ Compact          │ High (nodes) │ Moderate     │ Compact      │
    └──────────────────┴──────────────────┴──────────────┴──────────────┴──────────────┘

    RECOMMENDATION:
    ┌──────────────────────────────────────────────────────────────┐
    │  Queue:  ArrayDeque (fastest, most memory-efficient)         │
    │  Deque:  ArrayDeque (both Stack and Queue)                   │
    │  Stack:  ArrayDeque (replaces legacy Stack class)            │
    │  Priority: PriorityQueue (for min/max extraction)            │
    │  Concurrent: ArrayBlockingQueue or LinkedBlockingQueue       │
    │  Non-concurrent: LinkedList (rarely use for queue)           │
    └──────────────────────────────────────────────────────────────┘
```

---

## 10. LINKEDLIST - INTERNAL STRUCTURE

```
    LinkedList<Integer> list = new LinkedList<>();
    list.add(10);
    list.add(20);
    list.add(30);

    Internal: Doubly Linked List
    ┌─────────┐    ┌─────────┐    ┌─────────┐
    │  null   │←──→│   10    │←──→│   20    │←──→│   30    │←──→│  null   │
    └─────────┘    └─────────┘    └─────────┘    └─────────┘    └─────────┘
       ↑               ↑                               ↑
     head          Node{                             Node{
                   prev=null,                       prev=20node,
                   item=10,                         item=30,
                   next=20node}                     next=null}
                                                    ↑
                                                 tail

    Node structure:
    ┌────────────────────────────────┐
    │ class Node<E> {                │
    │     E item;                    │
    │     Node<E> next;              │
    │     Node<E> prev;              │
    │ }                              │
    └────────────────────────────────┘

    Access by index: O(n) - must traverse from head/tail
    Add/remove at ends: O(1)
    addFirst/addLast: O(1)
    removeFirst/removeLast: O(1)
```

---

## 11. WHEN TO USE WHAT - FLOWCHART

```
    ┌─────────────────────────────────────────────────────────────┐
    │                    WHICH QUEUE TO USE?                       │
    └───────────────────────┬─────────────────────────────────────┘
                            │
                    ┌───────▼────────┐
                    │ Need FIFO?      │
                    └───────┬────────┘
                            │
                ┌───────────┴───────────────┐
                │                           │
            YES ▼                       NO  ▼
        ┌───────────────┐        ┌─────────────────┐
        │ Need blocking?│        │ Need Stack?      │
        └───────┬───────┘        └────────┬────────┘
                │                          │
        ┌───────┴───────┐          ┌───────┴───────┐
        │               │          │               │
    YES ▼           NO  ▼      YES ▼           NO  ▼
    ┌─────────────┐ ┌──────────┐ ┌──────────────┐ ┌────────────────┐
    │ BlockingQ   │ │ArrayDeque│ │ ArrayDeque   │ │PriorityQueue   │
    │ or           │ │          │ │ (push/pop)   │ │ (min/max heap) │
    │ ArrayBlockQ │ │          │ │              │ │                │
    └─────────────┘ └──────────┘ └──────────────┘ └────────────────┘

    ┌──────────────────────────────────────────────────────────────┐
    │ SUMMARY:                                                     │
    │                                                              │
    │  General Queue      → ArrayDeque                             │
    │  General Stack      → ArrayDeque                             │
    │  Sorted Queue       → PriorityQueue                          │
    │  Thread-safe Queue  → ArrayBlockingQueue                     │
    │  Producer-Consumer  → LinkedBlockingQueue                    │
    │  Delayed tasks      → DelayQueue                             │
    │  Zero-buffer handoff→ SynchronousQueue                       │
    │  High-throughput    → LinkedTransferQueue                    │
    └──────────────────────────────────────────────────────────────┘
```

---

## 12. QUEUE & DEQUE - ALL INTERVIEW QUESTIONS (50+)

### ⭐ BASIC LEVEL (Q1-Q15)

**Q1: What is Queue in Java?**
> Queue is a Collection designed for holding elements prior to processing. Follows FIFO (First-In-First-Out) principle.

**Q2: What is the difference between Queue and Stack?**
```
Queue: FIFO (First-In-First-Out)
        add from rear, remove from front
        → Like a line at bank counter

Stack: LIFO (Last-In-First-Out)
        add/remove from top
        → Like a stack of plates
```

**Q3: What is difference between add() and offer()?**
```
add():    Throws IllegalStateException if queue is full
offer():  Returns false if queue is full (safe)
Prefer offer() for bounded queues.
```

**Q4: What is difference between remove() and poll()?**
```
remove(): Throws NoSuchElementException if queue is empty
poll():   Returns null if queue is empty (safe)
Prefer poll() for safety.
```

**Q5: What is difference between element() and peek()?**
```
element(): Throws NoSuchElementException if queue is empty
peek():    Returns null if queue is empty (safe)
Prefer peek() for safety.
```

**Q6: What is Deque?**
> Deque (Double-Ended Queue) supports insertion and removal from both ends. Can function as both Queue (FIFO) and Stack (LIFO).

**Q7: What is ArrayDeque internally?**
> Circular array (Object[]) with head and tail pointers. Doubles in size when full. Most efficient Queue/Deque implementation.

**Q8: Why ArrayDeque is better than LinkedList?**
```
ArrayDeque:  Contiguous memory, cache-friendly, no node objects
LinkedList:  Separate node objects, more memory, cache-unfriendly
Performance: ArrayDeque 2-3x faster than LinkedList for queue operations
```

**Q9: What is PriorityQueue?**
> A queue based on binary heap. Elements processed in priority order (natural ordering or Comparator). NOT the same as sorted order.

**Q10: Can PriorityQueue have null elements?**
> No. Adding null throws NullPointerException. poll() returns null when empty (which is different from "null element").

**Q11: What is the difference between ArrayDeque and LinkedList?**
```
┌──────────────────┬──────────────────┬──────────────────┐
│ Feature           │ ArrayDeque       │ LinkedList       │
├──────────────────┼──────────────────┼──────────────────┤
│ Internal          │ Circular Array   │ Doubly Linked LL │
│ Memory            │ Contiguous       │ Scattered nodes  │
│ Null elements     │ NOT allowed      │ Allowed          │
│ Performance       │ 2-3x faster      │ Slower           │
│ Implements        │ Deque only       │ List, Deque, Queue│
│ Cache-friendly    │ Yes              │ No               │
│ Queue/Deque usage │ Recommended      │ Avoid            │
└──────────────────┴──────────────────┴──────────────────┘
```

**Q12: What is the default capacity of ArrayDeque?**
> 16 (unlike ArrayList which is 10). Grows by factor of 2 when full.

**Q13: What is difference between Deque and Queue?**
```
Queue:  One end for add (rear), one end for remove (front)
Deque:  Both ends support add AND remove
        Can work as Queue OR Stack
```

**Q14: Can we use ArrayDeque as a Stack?**
> Yes. push() = addFirst(), pop() = removeFirst(), peek() = peekFirst(). Recommended over legacy Stack class since Java 6.

**Q15: What is the time complexity of queue operations?**
```
┌─────────────────┬──────────────┬──────────────┬────────────────┐
│ Operation        │ ArrayDeque   │ LinkedList   │ PriorityQueue  │
├─────────────────┼──────────────┼──────────────┼────────────────┤
│ add/addLast     │ O(1) amortized│ O(1)        │ O(log n)       │
│ addFirst        │ O(1)         │ O(1)         │ N/A            │
│ remove/removeFirst│ O(1)       │ O(1)         │ O(log n)       │
│ removeLast      │ O(1)         │ O(1)         │ N/A            │
│ peek/element    │ O(1)         │ O(1)         │ O(1)           │
│ contains        │ O(n)         │ O(n)         │ O(n)           │
│ size            │ O(1)         │ O(1)         │ O(1)           │
└─────────────────┴──────────────┴──────────────┴────────────────┘
```

---

### ⭐⭐ MIDDLE LEVEL (Q16-Q35)

**Q16: How ArrayDeque works internally (detailed)?**
```
1. Internal array: Object[] elements
2. head: index of first element
3. tail: index AFTER last element (next available slot)
4. Capacity always power of 2
5. Circular indexing: index = (index ± 1) & (length - 1)
6. Resize: double capacity when full, rehash all elements
7. addLast: elements[tail] = e; tail = (tail + 1) & (length - 1)
8. addFirst: head = (head - 1) & (length - 1); elements[head] = e
```

**Q17: What happens when ArrayDeque is full and we add?**
```
addLast() when full:
1. Allocate new array (2x size)
2. Copy elements: elements[head..tail] → new array
3. Reset head = 0, tail = old size
4. Add new element

addFirst() when full:
Same doubling strategy
```

**Q18: Why ArrayDeque doesn't allow null elements?**
> Null is used as sentinel value internally to indicate empty slots. If null were allowed, it would be impossible to distinguish between an empty slot and a null element.

**Q19: What is the difference between PriorityQueue and TreeSet?**
```
┌──────────────────┬──────────────────┬──────────────────┐
│ Feature           │ PriorityQueue    │ TreeSet          │
├──────────────────┼──────────────────┼──────────────────┤
│ Structure         │ Binary Heap      │ Red-Black Tree   │
│ Duplicates        │ YES              │ NO               │
│ Null              │ NO               │ Depends*         │
│ Sorted iteration  │ NO               │ YES              │
│ peek              │ O(1) min/max     │ O(1) min/max     │
│ add               │ O(log n)         │ O(log n)         │
│ remove            │ O(log n)         │ O(log n)         │
│ contains          │ O(n)             │ O(log n)         │
│ Duplicate handling│ All stored        │ Rejected         │
└──────────────────┴──────────────────┴──────────────────┘
```

**Q20: What is the difference between PriorityQueue and HashMap?**
```
PriorityQueue: Orders by priority (min-heap)
HashMap:       Orders by hash (no priority)

Use PriorityQueue when you need min/max extraction.
Use HashMap when you need key-value lookup.
```

**Q21: How PriorityQueue handles duplicates?**
```java
PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.add(5);
pq.add(5);   // Both 5s are stored!
pq.add(5);
pq.size();   // 3 (all duplicates kept)
// poll() returns all three 5s before other elements
```

**Q22: How to create a max-heap PriorityQueue?**
```java
// Method 1: Reverse order comparator
PriorityQueue<Integer> maxPQ = new PriorityQueue<>(Comparator.reverseOrder());

// Method 2: Custom comparator
PriorityQueue<Integer> maxPQ2 = new PriorityQueue<>((a, b) -> b - a);

// Method 3: Collections.reverseOrder()
PriorityQueue<Integer> maxPQ3 = new PriorityQueue<>(Collections.reverseOrder());
```

**Q23: How to iterate PriorityQueue?**
```java
PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.addAll(Arrays.asList(5, 3, 1, 4, 2));

// ❌ WRONG - iterator does NOT give sorted order
for (Integer i : pq) System.out.print(i);  // Random order!

// ✅ CORRECT - poll() gives sorted order
while (!pq.isEmpty()) {
    System.out.print(pq.poll());  // 1, 2, 3, 4, 5
}

// ✅ Stream (also not sorted by default)
pq.stream().sorted().forEach(System.out::println);
```

**Q24: What is difference between remove(Object) and remove(int index)?**
```
remove(Object o):     Removes first occurrence of element
remove(int index):    List only - removes element at index (not in Queue/Deque)
Queue.remove():       Removes head element (no arguments)
```

**Q25: How does BlockingQueue handle full/empty scenarios?**
```
┌─────────────────┬─────────────────────────────────────────────┐
│ Scenario         │ add/offer        │ put                    │
├─────────────────┼─────────────────────────────────────────────┤
│ Queue not full   │ Success          │ Success                │
│ Queue full       │ add → exception  │ put → BLOCKS until space│
│                  │ offer → false    │                        │
├─────────────────┼─────────────────────────────────────────────┤
│ Scenario         │ remove/poll      │ take                   │
├─────────────────┼─────────────────────────────────────────────┤
│ Queue not empty  │ Success          │ Success                │
│ Queue empty      │ remove → except  │ take → BLOCKS until     │
│                  │ poll → null      │ element available       │
└─────────────────┴─────────────────────────────────────────────┘
```

**Q26: What is ArrayBlockingQueue vs LinkedBlockingQueue?**
```
┌────────────────────┬────────────────────┬────────────────────┐
│ Feature             │ ArrayBlockingQueue │ LinkedBlockingQueue │
├────────────────────┼────────────────────┼────────────────────┤
│ Structure           │ Fixed-size array   │ Linked nodes       │
│ Bounded             │ Always bounded     │ Optional bound     │
│ Memory              │ Pre-allocated      │ Grows on demand    │
│ Locking             │ Single lock        │ Two locks (head/tail)│
│ Throughput          │ Lower              │ Higher             │
│ Memory overhead     │ Lower              │ Higher (node objects)│
│ Best for            │ Fixed size         │ Variable load       │
└────────────────────┴────────────────────┴────────────────────┘
```

**Q27: What is SynchronousQueue?**
> Zero-capacity queue. Every put() must wait for take(). No internal storage. Used for direct handoff between threads.

```
    Producer put(10) ──→ Waits for Consumer ──→ Consumer take() gets 10
    No buffering! Direct transfer.
```

**Q28: What is DelayQueue?**
```java
DelayedQueue<DelayedTask> queue = new DelayedQueue<>();

class DelayedTask implements Delayed {
    long delay;  // delay in milliseconds
    
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delay - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
    
    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), 
                           o.getDelay(TimeUnit.MILLISECONDS));
    }
}

// Elements only become eligible for take() after delay expires
// poll() returns null if no element is ready
```

**Q29: What is Deque used for in real applications?**
```
1. Browser history (back/forward navigation)
2. Undo/Redo operations
3. Function call stack (recursion)
4. Sliding window algorithms
5. Palindrome checking
6. Task scheduling
```

**Q30: What is the difference between addFirst() and push()?**
```
ArrayDeque:
addFirst(e) → add element at front, returns true
push(e)     → same as addFirst(), throws exception if full

In Deque, push() = addFirst()
In Stack, push() = add element at top
```

**Q31: How to convert between Queue implementations?**
```java
// Queue to List
List<Integer> list = new ArrayList<>(queue);

// PriorityQueue to sorted List
List<Integer> sorted = new ArrayList<>();
while (!pq.isEmpty()) sorted.add(pq.poll());

// LinkedList to ArrayDeque
Deque<Integer> deque = new ArrayDeque<>(linkedList);

// BlockingQueue to regular Queue (no conversion needed, BlockingQueue extends Queue)
```

**Q32: What is the use of peek() in multithreaded code?**
```java
// Safe check before remove
if (queue.peek() != null) {
    Object item = queue.poll();  // Still may return null in concurrent scenario
}
// Better: Use poll() directly and check for null
Object item = queue.poll();
if (item != null) {
    process(item);
}
```

**Q33: How does PriorityQueue handle Comparator vs Comparable?**
```java
// Comparable: Element implements Comparable<T>
PriorityQueue<String> pq = new PriorityQueue<>();  // Uses String.compareTo()

// Comparator: Custom ordering
PriorityQueue<String> pq2 = new PriorityQueue<>(Comparator.reverseOrder());

// Combined: Comparator takes precedence over Comparable
PriorityQueue<String> pq3 = new PriorityQueue<>(Comparator.comparingInt(String::length));
```

**Q34: What is the difference between offer() and add() in BlockingQueue?**
```
offer(): Returns false if queue is full (non-blocking)
add():   Throws IllegalStateException if queue is full

offer(e, timeout, unit): Waits up to timeout for space
put(e): Waits indefinitely for space
```

**Q35: Can we use PriorityQueue for max-heap operations?**
```java
PriorityQueue<Integer> maxPQ = new PriorityQueue<>(Comparator.reverseOrder());
maxPQ.add(10);
maxPQ.add(30);
maxPQ.add(20);

maxPQ.peek();  // 30 (maximum)
maxPQ.poll();  // 30, then 20, then 10
```

---

### ⭐⭐⭐ ADVANCED LEVEL (Q36-Q50)

**Q36: What is the internal algorithm of PriorityQueue heapify?**
```
Build heap from array (O(n)):
1. Start from last non-leaf node: (size/2 - 1)
2. Sift down each node to restore heap property
3. Result: valid min-heap

Array before heapify: [5, 3, 8, 1, 2]
                      5
                     / \
                    3   8
                   / \
                  1   2

After heapify: [1, 2, 8, 3, 5]
                      1
                     / \
                    2   8
                   / \
                  3   5
```

**Q37: How does ArrayDeque avoid memory waste?**
```
Circular array: No shifting needed
When head moves past index 0, it wraps to end
When tail reaches end, it wraps to 0

Example (capacity=8):
Before: [_][_][10][20][30][_][_][_]
         h=0         t=3

After adding 40, 50, 60, 70:
[60][70][10][20][30][40][50][_]
 h=6            t=7 (wraps to 0)
```

**Q38: What is the relationship between BlockingQueue and Executor framework?**
```java
// ThreadPoolExecutor uses BlockingQueue for task queue
ExecutorService executor = new ThreadPoolExecutor(
    2,  // core pool size
    4,  // max pool size
    60L, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(100)  // task queue
);

// Tasks exceeding pool size go to queue
// If queue is full AND max pool reached → RejectedExecutionHandler
```

**Q39: What is the difference between LinkedList and Deque?**
```
LinkedList: Implements List, Deque, Queue (implements all three)
Deque:      Interface that LinkedList implements

Use Deque reference when you only need Queue/Deque operations:
Deque<Integer> deque = new LinkedList<>();  // Better than:
LinkedList<Integer> list = new LinkedList<>();
```

**Q40: How to find median using two PriorityQueues?**
```java
class MedianFinder {
    PriorityQueue<Integer> maxHeap;  // lower half
    PriorityQueue<Integer> minHeap;  // upper half

    public MedianFinder() {
        maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        minHeap = new PriorityQueue<>();
    }

    public void addNum(int num) {
        maxHeap.offer(num);
        minHeap.offer(maxHeap.poll());  // Balance
        if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double findMedian() {
        if (maxHeap.size() > minHeap.size()) return maxHeap.peek();
        return (maxHeap.peek() + minHeap.peek()) / 2.0;
    }
}
```

**Q41: What is the difference between priority queue and heap?**
```
PriorityQueue = Java class (API)
Heap = Data structure (concept)

PriorityQueue uses binary heap internally.
Heap is the algorithm, PriorityQueue is the implementation.
```

**Q42: What are the thread-safe alternatives for Queue?**
```
┌──────────────────────────┬──────────────────────────────────────┐
│ Non-thread-safe           │ Thread-safe alternative              │
├──────────────────────────┼──────────────────────────────────────┤
│ ArrayDeque                │ ArrayBlockingQueue / LinkedBlockingQ │
│ LinkedList (as Queue)     │ LinkedBlockingQueue                  │
│ PriorityQueue            │ PriorityBlockingQueue                │
│ Deque                     │ ConcurrentLinkedDeque                │
│ Queue                     │ ConcurrentLinkedQueue                │
└──────────────────────────┴──────────────────────────────────────┘
```

**Q43: What is ConcurrentLinkedQueue?**
> Unbounded thread-safe queue using CAS (lock-free). No null elements. Best for high-concurrency scenarios without blocking.

```java
ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
queue.offer(10);
queue.poll();
// Lock-free, non-blocking, high throughput
```

**Q44: What is LinkedTransferQueue?**
> Extension of LinkedBlockingQueue with transfer() method. Allows producer to wait for consumer to receive element.

```java
LinkedTransferQueue<Integer> queue = new LinkedTransferQueue<>();
// Producer waits until consumer takes the element
queue.transfer(10);  // Blocks until taken
```

**Q45: What is Deque used for in BFS/DFS algorithms?**
```java
// DFS using Deque as Stack
Deque<Integer> stack = new ArrayDeque<>();
stack.push(start);
while (!stack.isEmpty()) {
    int node = stack.pop();
    for (int neighbor : graph[node]) {
        stack.push(neighbor);
    }
}

// BFS using Deque as Queue
Deque<Integer> queue = new ArrayDeque<>();
queue.offer(start);
while (!queue.isEmpty()) {
    int node = queue.poll();
    for (int neighbor : graph[node]) {
        queue.offer(neighbor);
    }
}
```

**Q46: What happens when PriorityQueue is used in multithreaded code?**
> PriorityQueue is NOT thread-safe. Concurrent access may cause:
> - ConcurrentModificationException
> - Corrupted heap structure
> - Infinite loops in offer/poll
> Use PriorityBlockingQueue or external synchronization.

**Q47: What is the difference between poll() and peek() in multithreaded BlockingQueue?**
```
poll(): Removes and returns element (atomic operation)
peek(): Returns element without removing (may be stale in concurrent env)
In concurrent code: always prefer poll() for consistency
```

**Q48: What is the time complexity of heap operations in PriorityQueue?**
```
┌──────────────────┬──────────────┐
│ Operation         │ Complexity   │
├──────────────────┼──────────────┤
│ add() / offer()   │ O(log n)     │
│ poll() / remove() │ O(log n)     │
│ peek()            │ O(1)         │
│ contains()        │ O(n)         │
│ remove(Object)    │ O(n)         │
│ size()            │ O(1)         │
│ heapify (n items) │ O(n)         │
└──────────────────┴──────────────┘
```

**Q49: How to use Deque for sliding window maximum?**
```java
// Monotonic Deque approach
int[] maxSlidingWindow(int[] nums, int k) {
    Deque<Integer> deque = new ArrayDeque<>();
    int[] result = new int[nums.length - k + 1];
    
    for (int i = 0; i < nums.length; i++) {
        while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
            deque.pollLast();
        }
        deque.offerLast(i);
        if (deque.peekFirst() <= i - k) deque.pollFirst();
        if (i >= k - 1) result[i - k + 1] = nums[deque.peekFirst()];
    }
    return result;
}
```

**Q50: Practical scenario - When to use which Queue/Deque?**
```
┌───────────────────────────────┬──────────────────────────────────┐
│ Scenario                       │ Best Choice                      │
├───────────────────────────────┼──────────────────────────────────┤
│ Simple FIFO queue              │ ArrayDeque                      │
│ Stack operations               │ ArrayDeque                      │
│ Task scheduling by priority    │ PriorityQueue                   │
│ Producer-Consumer (bounded)    │ ArrayBlockingQueue              │
│ Producer-Consumer (unbounded)  │ LinkedBlockingQueue             │
│ Direct thread handoff          │ SynchronousQueue                │
│ Scheduled tasks                │ DelayQueue                      │
│ High-concurrency non-blocking  │ ConcurrentLinkedQueue           │
│ Max throughput queue           │ LinkedTransferQueue             │
│ BFS/DFS traversal              │ ArrayDeque                      │
│ Browser back/forward           │ ArrayDeque (two deques)         │
│ Undo/Redo                      │ ArrayDeque                      │
│ Thread pool task queue         │ LinkedBlockingQueue             │
│ Priority-based scheduling      │ PriorityBlockingQueue           │
└───────────────────────────────┴──────────────────────────────────┘
```

---

## 13. JAVA 8+ QUEUE METHODS

```java
ArrayDeque<Integer> deque = new ArrayDeque<>(Arrays.asList(1, 2, 3, 4, 5));

// removeIf
deque.removeIf(n -> n % 2 == 0);  // removes 2, 4

// forEach
deque.forEach(n -> System.out.println(n));

// stream
deque.stream().filter(n -> n > 2).forEach(System.out::println);

// toArray
Integer[] arr = deque.toArray(Integer[]::new);

// List copy
List<Integer> list = new ArrayList<>(deque);
```

---

## 14. QUICK REFERENCE CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════════╗
║                JAVA QUEUE & DEQUE CHEAT SHEET                        ║
╠══════════════════════════════════════════════════════════════════════╣
║                                                                      ║
║  ArrayDeque       → Fastest Queue/Deque, no null, circular array     ║
║  LinkedList       → Doubly LL, allows null, implements List+Deque    ║
║  PriorityQueue    → Min-heap, sorted extraction, no null             ║
║  ArrayBlockingQ   → Fixed-size, blocking put/take, thread-safe       ║
║  LinkedBlockingQ  → Optional bound, blocking, two locks              ║
║  SynchronousQ     → Zero-size, direct handoff                        ║
║  PriorityBlockingQ→ Thread-safe PriorityQueue                        ║
║  ConcurrentLinkedQ→ Lock-free, high throughput                       ║
║                                                                      ║
║  Methods (safe):  offer() / poll() / peek()  → returns null/false   ║
║  Methods (throw): add()  / remove() / element() → throws exception  ║
║  Blocking:        put() / take() → blocks until possible            ║
║                                                                      ║
║  Queue (FIFO): addLast + removeFirst                                 ║
║  Stack (LIFO): addFirst + removeFirst (= push/pop)                  ║
║                                                                      ║
║  Deque > Stack class (Java 6+)                                       ║
║  ArrayDeque > LinkedList for Queue/Deque usage                       ║
║  ArrayDeque capacity: always power of 2                              ║
║  PriorityQueue: heap-based, O(log n) add/remove, O(1) peek          ║
║                                                                      ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: Java 8+ features, Interview Focus, All Major Topics*
