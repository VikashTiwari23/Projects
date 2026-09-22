# Java Multithreading & Concurrency - Complete Interview Notes

---

## 1. THREAD BASICS - WHAT IS A THREAD?

```
    Process vs Thread:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  PROCESS:                                                    │
    │  ┌──────────────────────────────────────────────────────┐   │
    │  │  JVM Process                                         │   │
    │  │  ├── Thread 1 (Main)      [Stack, Registers, PC]    │   │
    │  │  ├── Thread 2 (Worker)    [Stack, Registers, PC]    │   │
    │  │  ├── Thread 3 (Worker)    [Stack, Registers, PC]    │   │
    │  │  ├── Heap (Shared)                                   │   │
    │  │  ├── Method Area (Shared)                            │   │
    │  │  └── Resources (Shared)                              │   │
    │  └──────────────────────────────────────────────────────┘   │
    │                                                              │
    │  THREAD:                                                     │
    │  - Lightweight subprocess                                   │
    │  - Shares heap memory with other threads                    │
    │  - Has its own stack, program counter, registers            │
    │  - Context switching is cheaper than process                 │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘

    Thread Memory Model:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
    │  │   Thread 1    │  │   Thread 2    │  │   Thread 3    │      │
    │  ├──────────────┤  ├──────────────┤  ├──────────────┤      │
    │  │ Stack        │  │ Stack        │  │ Stack        │      │
    │  │ - local vars │  │ - local vars │  │ - local vars │      │
    │  │ - method calls│ │ - method calls│ │ - method calls│      │
    │  │ - PC register│  │ - PC register│  │ - PC register│      │
    │  └──────────────┘  └──────────────┘  └──────────────┘      │
    │         │                 │                 │                │
    │         └────────────────┼─────────────────┘                │
    │                          ▼                                  │
    │                 ┌──────────────────┐                        │
    │                 │   SHARED HEAP     │                        │
    │                 │  - Instance vars  │                        │
    │                 │  - Static vars    │                        │
    │                 │  - Objects        │                        │
    │                 └──────────────────┘                        │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. CREATING THREADS - 4 WAYS

### 2.1 Way 1: Extend Thread Class

```java
class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " → " + i);
        }
    }
}

// Usage
MyThread t1 = new MyThread();
MyThread t2 = new MyThread();
t1.setName("Worker-1");
t2.setName("Worker-2");
t1.start();  // ⚠️ start() not run()!
t2.start();
```

### 2.2 Way 2: Implement Runnable Interface (Recommended)

```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " → " + i);
        }
    }
}

// Usage
Thread t1 = new Thread(new MyRunnable(), "Worker-1");
Thread t2 = new Thread(new MyRunnable(), "Worker-2");
t1.start();
t2.start();
```

### 2.3 Way 3: Lambda (Java 8+)

```java
Thread t1 = new Thread(() -> {
    for (int i = 0; i < 5; i++) {
        System.out.println(Thread.currentThread().getName() + " → " + i);
    }
}, "Worker-1");

t1.start();
```

### 2.4 Way 4: Implement Callable + FutureTask

```java
Callable<Integer> callable = () -> {
    int sum = 0;
    for (int i = 1; i <= 100; i++) sum += i;
    return sum;
};

FutureTask<Integer> task = new FutureTask<>(callable);
Thread t = new Thread(task);
t.start();

Integer result = task.get();  // Blocks until done → 5050
```

### 2.5 Comparison of 4 Ways

```
    ┌──────────────────────┬──────────────────────────────────────┐
    │ Way                    │ Pros/Cons                            │
    ├──────────────────────┼──────────────────────────────────────┤
    │ extends Thread         │ ❌ Can't extend other class          │
    │ implements Runnable    │ ✅ Can implement other interfaces    │
    │ Lambda                 │ ✅ Cleanest, functional style        │
    │ Callable + Future      │ ✅ Returns result, throws exception  │
    └──────────────────────┴──────────────────────────────────────┘

    ⚠️ NEVER call run() directly! Always call start().
    start() → creates new thread → calls run()
    run() → runs in SAME thread (no new thread!)
```

---

## 3. THREAD LIFECYCLE

```
    ┌──────────────────────────────────────────────────────────────┐
    │                    THREAD STATES                              │
    │                                                              │
    │  NEW ──start()──→ RUNNABLE ──scheduling──→ RUNNING          │
    │                    │    ↑                    │                │
    │                    │    │                    │                │
    │                    │    └────────────────────┘                │
    │                    │    (yield/join/wait)                     │
    │                    │                                          │
    │                    ├──sleep()──→ TIMED_WAITING ──time──→ R   │
    │                    │                                          │
    │                    ├──wait()──→ WAITING ──notify()──→ R      │
    │                    │                                          │
    │                    ├──join()──→ WAITING ──thread ends──→ R   │
    │                    │                                          │
    │                    ├──lock()──→ BLOCKED ──acquired──→ R      │
    │                    │                                          │
    │                    └──finish──→ TERMINATED                    │
    └──────────────────────────────────────────────────────────────┘

    States in detail:
    ┌──────────────────────────────────────────────────────────────┐
    │  NEW            → Thread created, start() not called yet     │
    │  RUNNABLE       → Ready to run, waiting for CPU time         │
    │  RUNNING        → Actually executing on CPU                  │
    │  TIMED_WAITING  → Sleeping for specified time                │
    │  WAITING        → Waiting indefinitely (wait, join)          │
    │  BLOCKED        → Waiting for monitor/lock                   │
    │  TERMINATED     → Execution completed                        │
    └──────────────────────────────────────────────────────────────┘
```

---

## 4. THREAD METHODS CHEAT SHEET

```
    ┌─────────────────────┬──────────────────────────────────────┐
    │ Method                │ Description                          │
    ├─────────────────────┼──────────────────────────────────────┤
    │ start()              │ Start thread (new stack)             │
    │ run()                │ Task to execute (no new thread)      │
    │ sleep(ms)            │ Pause for ms milliseconds            │
    │ yield()              │ Hint to scheduler (pause briefly)    │
    │ join()               │ Wait for thread to die               │
    │ join(ms)             │ Wait for ms or until thread dies     │
    │ interrupt()          │ Interrupt sleeping/waiting thread    │
    │ isInterrupted()      │ Check if interrupted                 │
    │ interrupted()        │ Check AND clear interrupt flag       │
    │ isAlive()            │ Check if thread is running           │
    │ isDaemon()           │ Check if daemon thread               │
    │ setDaemon(true)      │ Set as daemon (before start())       │
    │ getName()            │ Get thread name                      │
    │ setName(name)        │ Set thread name                      │
    │ getPriority()        │ Get priority (1-10)                  │
    │ setPriority(n)       │ Set priority (1-10)                  │
    │ currentThread()      │ Get current thread reference         │
    │ holdLock(obj)        │ Check if current thread holds lock   │
    │ threadId()           │ Get thread ID                        │
    │ getState()           │ Get thread state                     │
    └─────────────────────┴──────────────────────────────────────┘
```

---

## 5. SLEEP VS YIELD VS JOIN

```
    ┌─── ───────────────────────────────────────────────────────────┐
    │  sleep(ms):                                                   │
    │  - Pauses CURRENT thread for ms milliseconds                 │
    │  - Releases CPU but NOT locks                                │
    │  - Thread goes to TIMED_WAITING state                        │
    │  - Another thread may run during sleep                       │
    │  - Static method (Thread.sleep())                           │
    │                                                              │
    │  yield():                                                     │
    │  - Hints scheduler to give CPU to other threads              │
    │  - May resume immediately (scheduler may ignore)             │
    │  - Stays in RUNNABLE state                                   │
    │  - Doesn't release locks                                     │
    │  - Static method                                             │
    │  - Lower priority threads may run                            │
    │                                                              │
    │  join():                                                      │
    │  - Current thread WAITS for another thread to finish         │
    │  - Thread goes to WAITING state                              │
    │  - join() → waits indefinitely                               │
    │  - join(1000) → waits up to 1 second                         │
    │  - Instance method (t1.join())                               │
    └──────────────────────────────────────────────────────────────┘

    Visual:
    Thread A:  ████ sleep(1000) ████ ████ ████
    Thread B:       ████ ████ ████ ████ ████    ← B runs while A sleeps

    Thread A:  ████ yield ████ ████ ████
    Thread B:       ████ ████ ████ ████          ← May or may not run

    Thread A:  ████ join(B) ████ ████ ████
    Thread B:       ████ ████ ████ ████          ← B must finish first
```

---

## 6. SYNCHRONIZED - MUTUAL EXCLUSION

### 6.1 Synchronized Method

```java
class Counter {
    private int count = 0;

    // Only ONE thread can execute this method at a time
    public synchronized void increment() {
        count++;  // Not atomic! (read, increment, write)
    }

    public synchronized int getCount() {
        return count;
    }
}
```

### 6.2 Synchronized Block

```java
class BankAccount {
    private int balance = 1000;

    public void withdraw(int amount) {
        synchronized (this) {  // Lock on this object
            if (balance >= amount) {
                balance -= amount;
                System.out.println("Withdrawn: " + amount);
            }
        }
    }
}
```

### 6.3 Synchronized on Specific Object

```java
class PaymentProcessor {
    private final Object lock = new Object();

    public void processPayment() {
        synchronized (lock) {  // Lock on specific object
            // Only this block is synchronized
            // Other methods not using 'lock' are not affected
        }
    }
}
```

### 6.4 Static Synchronized

```java
class Singleton {
    private static Singleton instance;

    // Locks on CLASS object, not instance
    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

### 6.5 Synchronized Visual

```
    Without synchronized:
    Thread A:  read count=5 ──┐
    Thread B:  read count=5 ──┤
    Thread A:  write count=6 ─┤  ← A's write lost!
    Thread B:  write count=6 ─┘  ← B overwrites A's increment

    Result: count=6 (wrong! should be 7)

    With synchronized:
    Thread A:  [LOCK] read=5, write=6 [UNLOCK]
    Thread B:                    [LOCK] read=6, write=7 [UNLOCK]

    Result: count=7 ✅

    ┌──────────────────────────────────────────────────────────────┐
    │  synchronized prevents concurrent access                     │
    │                                                              │
    │  Thread A: ████ LOCK ████ work ████ UNLOCK ████             │
    │  Thread B:           waiting...████ LOCK ████ work ████ UNL  │
    └──────────────────────────────────────────────────────────────┘
```

---

## 7. WAIT, NOTIFY, NOTIFYALL

```java
class SharedResource {
    private int data;
    private boolean hasData = false;

    public synchronized void produce(int value) {
        while (hasData) {         // While loop, NOT if!
            try { wait(); }       // Release lock, wait
            catch (InterruptedException e) {}
        }
        data = value;
        hasData = true;
        System.out.println("Produced: " + value);
        notify();                 // Wake up ONE waiting thread
    }

    public synchronized int consume() {
        while (!hasData) {        // While loop, NOT if!
            try { wait(); }
            catch (InterruptedException e) {}
        }
        hasData = false;
        System.out.println("Consumed: " + data);
        notify();
        return data;
    }
}
```

### 7.1 Wait vs Sleep vs Yield

```
    ┌──────────────────────┬──────────────────────────────────────┐
    │ Feature               │ wait()    │ sleep()   │ yield()      │
    ├──────────────────────┼──────────────────────────────────────┤
    │ Releases lock         │ ✅ Yes     │ ❌ No      │ ❌ No        │
    │ Needs synchronized    │ ✅ Yes     │ ❌ No      │ ❌ No        │
    │ Static method         │ ❌ No      │ ✅ Yes     │ ✅ Yes       │
    │ Thread state          │ WAITING   │ TIMED_WAIT│ RUNNABLE    │
    │ Wakes up via          │ notify()  │ time      │ scheduler   │
    │ Object method         │ ✅ Yes     │ ❌ No      │ ❌ No        │
    └──────────────────────┴──────────────────────────────────────┘
```

### 7.2 Why While Loop with Wait?

```java
// ❌ WRONG: if
if (!hasData) wait();  // Spurious wakeup → check fails

// ✅ CORRECT: while
while (!hasData) wait();  // Re-checks condition after wakeup
// Handles spurious wakeups (wakeup without notify)
```

---

## 8. PRODUCER-CONSUMER PATTERN

### 8.1 Using wait() and notify()

```java
class Buffer {
    private Queue<Integer> queue = new LinkedList<>();
    private int capacity;

    public Buffer(int capacity) { this.capacity = capacity; }

    public synchronized void produce(int item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();  // Buffer full, wait for consumer
        }
        queue.add(item);
        System.out.println("Produced: " + item + " | Buffer: " + queue.size());
        notify();  // Wake up consumer
    }

    public synchronized int consume() throws InterruptedException {
        while (queue.size() == 0) {
            wait();  // Buffer empty, wait for producer
        }
        int item = queue.poll();
        System.out.println("Consumed: " + item + " | Buffer: " + queue.size());
        notify();  // Wake up producer
        return item;
    }
}

// Usage
Buffer buffer = new Buffer(5);

Thread producer = new Thread(() -> {
    for (int i = 1; i <= 10; i++) {
        try { buffer.produce(i); Thread.sleep(100); }
        catch (InterruptedException e) {}
    }
});

Thread consumer = new Thread(() -> {
    for (int i = 1; i <= 10; i++) {
        try { buffer.consume(); Thread.sleep(200); }
        catch (InterruptedException e) {}
    }
});

producer.start();
consumer.start();
```

### 8.2 Visual Flow

```
    Buffer capacity = 5

    Producer: produce(1) → [1]
    Producer: produce(2) → [1,2]
    Producer: produce(3) → [1,2,3]
    Producer: produce(4) → [1,2,3,4]
    Producer: produce(5) → [1,2,3,4,5]  FULL!
    Producer: wait()...                          Consumer: consume() → [2,3,4,5]
    Producer: produce(6) → [2,3,4,5,6]          Consumer: consume() → [3,4,5]
    ...                                           ...

    ┌──────────────────────────────────────────────────────────────┐
    │  PRODUCER ────→ [ Buffer Queue ] ────→ CONSUMER              │
    │                  [1][2][3][4][5]                             │
    │                                                              │
    │  Full  → Producer waits, Consumer consumes                   │
    │  Empty → Consumer waits, Producer produces                   │
    └──────────────────────────────────────────────────────────────┘
```

### 8.3 Producer-Consumer with BlockingQueue (Better)

```java
BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

// Producer
Thread producer = new Thread(() -> {
    for (int i = 1; i <= 10; i++) {
        try {
            queue.put(i);   // Blocks if full
            System.out.println("Produced: " + i);
        } catch (InterruptedException e) {}
    }
});

// Consumer
Thread consumer = new Thread(() -> {
    for (int i = 1; i <= 10; i++) {
        try {
            int item = queue.take();  // Blocks if empty
            System.out.println("Consumed: " + item);
        } catch (InterruptedException e) {}
    }
});

producer.start();
consumer.start();
```

---

## 9. DEADLOCK

### 9.1 What is Deadlock?

```
    Two threads waiting for each other's locks → FOREVER WAITING

    Thread A:                     Thread B:
    ┌─────────────────┐          ┌─────────────────┐
    │ LOCK resource1   │          │ LOCK resource2   │
    │                  │          │                  │
    │ try LOCK res2    │          │ try LOCK res1    │
    │                  │          │                  │
    │ WAITING...       │          │ WAITING...       │
    │ (forever!)       │          │ (forever!)       │
    └─────────────────┘          └─────────────────┘
```

### 9.2 Deadlock Example

```java
Object lock1 = new Object();
Object lock2 = new Object();

Thread t1 = new Thread(() -> {
    synchronized (lock1) {
        System.out.println("T1: Holding lock1...");
        try { Thread.sleep(100); } catch (Exception e) {}
        synchronized (lock2) {
            System.out.println("T1: Holding lock1 & lock2");
        }
    }
});

Thread t2 = new Thread(() -> {
    synchronized (lock2) {
        System.out.println("T2: Holding lock2...");
        try { Thread.sleep(100); } catch (Exception e) {}
        synchronized (lock1) {
            System.out.println("T2: Holding lock2 & lock1");
        }
    }
});

t1.start();
t2.start();
// DEADLOCK! T1 holds lock1, T2 holds lock2, both wait for each other
```

### 9.3 Deadlock Prevention

```
    4 conditions for deadlock (all must be true):
    ┌──────────────────────────────────────────────────────────────┐
    │  1. Mutual Exclusion    → Locks can't be shared              │
    │  2. Hold and Wait       → Thread holds lock, waits for more │
    │  3. No Preemption       → Locks can't be forcibly taken      │
    │  4. Circular Wait       → A waits for B, B waits for A       │
    └──────────────────────────────────────────────────────────────┘

    Prevention strategies:
    ┌──────────────────────────────────────────────────────────────┐
    │  ✅ Always lock resources in SAME ORDER                      │
    │  ✅ Use tryLock() with timeout                               │
    │  ✅ Use Lock interface instead of synchronized               │
    │  ✅ Avoid nested locks                                       │
    │  ✅ Use deadlock detection tools                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 10. LOCK INTERFACE

```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

class SafeCounter {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private int count = 0;
    private final int capacity = 5;

    public void produce() throws InterruptedException {
        lock.lock();
        try {
            while (count == capacity) notFull.await();  // Wait for space
            count++;
            System.out.println("Produced: " + count);
            notEmpty.signal();  // Wake up consumer
        } finally {
            lock.unlock();  // ALWAYS in finally!
        }
    }

    public int consume() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) notEmpty.await();  // Wait for data
            int item = count--;
            System.out.println("Consumed: " + item);
            notFull.signal();  // Wake up producer
            return item;
        } finally {
            lock.unlock();
        }
    }
}
```

### 10.1 Lock vs Synchronized

```
    ┌──────────────────────┬──────────────────────┬──────────────────────┐
    │ Feature               │ synchronized         │ Lock                  │
    ├──────────────────────┼──────────────────────┼──────────────────────┤
    │ Unlock on exception   │ ✅ Automatic          │ ❌ Manual (finally)   │
    │ Try lock              │ ❌ No                 │ ✅ tryLock()          │
    │ Lock timeout          │ ❌ No                 │ ✅ tryLock(ms)        │
    │ Interruptible         │ ❌ No                 │ ✅ lockInterruptibly()│
    │ Multiple conditions   │ ❌ Only wait/notify  │ ✅ Multiple Conditions│
    │ Fairness              │ ❌ No                 │ ✅ new ReentrantLock(true)│
    │ Read/Write lock       │ ❌ No                 │ ✅ ReentrantReadWriteLock│
    │ Performance           │ Slightly faster      │ Similar               │
    └──────────────────────┴──────────────────────┴──────────────────────┘
```

---

## 11. READWRITELOCK

```java
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Cache {
    private final Map<String, Object> map = new HashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public Object get(String key) {
        rwLock.readLock().lock();  // Multiple readers allowed
        try {
            return map.get(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void put(String key, Object value) {
        rwLock.writeLock().lock();  // Only ONE writer allowed
        try {
            map.put(key, value);
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}

// Read: Multiple threads can read simultaneously
// Write: Only ONE thread can write (blocks all readers)
```

```
    ReadWriteLock Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Readers (concurrent):                                       │
    │  Thread R1: ████ read ████                                   │
    │  Thread R2: ████ read ████  ← Both can read together!       │
    │  Thread R3: ████ read ████                                   │
    │                                                              │
    │  Writer (exclusive):                                         │
    │  Thread W1: ████ write ████  ← Blocks ALL readers!          │
    │  Thread R1:      waiting...████ read ████                     │
    │  Thread R2:      waiting...████ read ████                     │
    └──────────────────────────────────────────────────────────────┘
```

---

## 12. EXECUTOR FRAMEWORK

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Executor Framework (java.util.concurrent)                   │
    │                                                              │
    │  Executor ──→ ExecutorService ──→ ThreadPoolExecutor         │
    │                     │                                        │
    │                     ├── ScheduledThreadPoolExecutor          │
    │                     └── ForkJoinPool                         │
    │                                                              │
    │  Benefits:                                                   │
    │  - Thread pool management (reuse threads)                    │
    │  - Task submission and execution                             │
    │  - Future results                                            │
    │  - Better than manually creating threads                     │
    └──────────────────────────────────────────────────────────────┘
```

### 12.1 ExecutorService Types

```java
// 1. Fixed Thread Pool
ExecutorService fixed = Executors.newFixedThreadPool(4);

// 2. Cached Thread Pool (creates as needed, reuses idle)
ExecutorService cached = Executors.newCachedThreadPool();

// 3. Single Thread Executor
ExecutorService single = Executors.newSingleThreadExecutor();

// 4. Scheduled Thread Pool
ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(4);

// 5. Work Stealing Pool (ForkJoinPool)
ExecutorService workStealing = Executors.newWorkStealingPool();
```

### 12.2 ExecutorService Visual

```
    Fixed Thread Pool (n=3):
    ┌──────────────────────────────────────────────────────────────┐
    │  Task Queue: [Task4][Task5][Task6]...                        │
    │                                                              │
    │  ┌──────┐  ┌──────┐  ┌──────┐                              │
    │  │Thread│  │Thread│  │Thread│                               │
    │  │  1   │  │  2   │  │  3   │                               │
    │  └──────┘  └──────┘  └──────┘                              │
    │  [Task1]   [Task2]   [Task3]  ← Currently executing        │
    │                                                              │
    │  Thread 1 finishes → takes Task4 from queue                 │
    │  Thread 2 finishes → takes Task5 from queue                 │
    └──────────────────────────────────────────────────────────────┘
```

### 12.3 Submit Tasks

```java
ExecutorService executor = Executors.newFixedThreadPool(3);

// Execute Runnable (no return)
executor.execute(() -> {
    System.out.println("Task running on: " + Thread.currentThread().getName());
});

// Submit Callable (returns Future)
Future<String> future = executor.submit(() -> {
    Thread.sleep(1000);
    return "Hello from " + Thread.currentThread().getName();
});

String result = future.get();  // Blocks until done

// Submit Runnable with result
Future<?> f = executor.submit(() -> System.out.println("Running"));

// Shutdown
executor.shutdown();           // Graceful shutdown
executor.shutdownNow();        // Immediate shutdown
executor.awaitTermination(5, TimeUnit.SECONDS);  // Wait for completion
```

---

## 13. CALLABLE AND FUTURE

```java
Callable<Integer> callable = new Callable<Integer>() {
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 1; i <= 100; i++) sum += i;
        Thread.sleep(1000);  // Simulate work
        return sum;
    }
};

// Method 1: ExecutorService
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<Integer> future = executor.submit(callable);

// Do other work while task runs...
System.out.println("Doing other work...");

// Get result (blocks if not done)
try {
    Integer result = future.get(5, TimeUnit.SECONDS);  // With timeout
    System.out.println("Result: " + result);  // 5050
} catch (TimeoutException e) {
    future.cancel(true);  // Cancel if takes too long
}
```

### 13.1 Future Methods

```
    ┌─────────────────────┬──────────────────────────────────────┐
    │ Method               │ Description                          │
    ├─────────────────────┼──────────────────────────────────────┤
    │ get()               │ Blocks until result available         │
    │ get(timeout, unit)  │ Blocks up to timeout                  │
    │ isDone()            │ Check if task completed               │
    │ isCancelled()       │ Check if task cancelled               │
    │ cancel(mayInterrupt)│ Cancel the task                       │
    └─────────────────────┴──────────────────────────────────────┘
```

---

## 14. CONCURRENT COLLECTIONS

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Collection         │ Concurrent Version                     │
    ├──────────────────────────────────────────────────────────────┤
    │  HashMap            │ ConcurrentHashMap                      │
    │  TreeMap            │ ConcurrentSkipListMap                   │
    │  HashSet            │ ConcurrentSkipListSet / CHM keySet    │
    │  ArrayList          │ CopyOnWriteArrayList                   │
    │  LinkedList         │ ConcurrentLinkedQueue / LinkedBlockingQueue │
    │  PriorityQueue      │ PriorityBlockingQueue                  │
    │  Queue              │ ConcurrentLinkedQueue                   │
    └──────────────────────┴──────────────────────────────────────┘
```

---

## 15. ATOMIC CLASSES

```java
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class AtomicCounter {
    private AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();  // Atomic: count++
    }

    public int get() {
        return count.get();
    }
}

// Atomic Operations:
// incrementAndGet()     → ++count (returns new value)
// getAndIncrement()     → count++ (returns old value)
// decrementAndGet()     → --count
// addAndGet(n)          → count += n
// compareAndSet(expect, update) → CAS operation
// getAndSet(newValue)   → returns old, sets new
```

### 15.1 CAS (Compare-And-Swap)

```
    CAS is a hardware-level atomic operation:
    ┌──────────────────────────────────────────────────────────────┐
    │  if (memory == expected) {                                   │
    │      memory = update;     // Atomic!                         │
    │      return true;                                            │
    │  } else {                                                    │
    │      return false;  // Retry                                  │
    │  }                                                           │
    └──────────────────────────────────────────────────────────────┘

    AtomicInteger count = new AtomicInteger(5);
    count.compareAndSet(5, 10);  // If count==5, set to 10
    // Returns true if successful

    Used in:
    - Non-blocking algorithms
    - Lock-free data structures
    - ConcurrentHashMap internally
```

---

## 16. SEMAPHORE

```java
import java.util.concurrent.Semaphore;

class ParkingLot {
    private final Semaphore semaphore = new Semaphore(3);  // 3 spots

    public void park(String car) throws InterruptedException {
        semaphore.acquire();  // Wait for a spot
        System.out.println(car + " parked | Spots available: " + semaphore.availablePermits());
        Thread.sleep(2000);  // Simulate parking time
        System.out.println(car + " leaving");
        semaphore.release();  // Free the spot
    }
}

// Usage
ParkingLot lot = new ParkingLot();
for (int i = 1; i <= 6; i++) {
    new Thread(() -> lot.park("Car-" + Thread.currentThread().getId())).start();
}

// Visual:
// Car-1: Acquire → Spots: 2  → Park → Release → Spots: 3
// Car-2: Acquire → Spots: 1  → Park → Release → Spots: 3
// Car-3: Acquire → Spots: 0  → Park → Release → Spots: 3
// Car-4: Wait... → (spots become available) → Acquire → Park
```

---

## 17. COUNTDOWNLATCH

```java
import java.util.concurrent.CountDownLatch;

class TeamLeader {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);  // 3 team members

        for (int i = 1; i <= 3; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    System.out.println("Member " + id + " is working...");
                    Thread.sleep(1000 * id);
                    System.out.println("Member " + id + " done!");
                    latch.countDown();  // Decrement count
                } catch (InterruptedException e) {}
            }).start();
        }

        System.out.println("Leader waiting for all members...");
        latch.await();  // Blocks until count reaches 0
        System.out.println("All members done! Leader starts meeting.");
    }
}

// Output:
// Member 1 is working...
// Member 2 is working...
// Member 3 is working...
// Member 1 done!
// Member 2 done!
// Member 3 done!
// All members done! Leader starts meeting.
```

```
    CountDownLatch(3):
    ┌──────────────────────────────────────────────────────────────┐
    │  count=3  →  count=2  →  count=1  →  count=0  → UNBLOCK!   │
    │  Member1    Member2      Member3      await() continues     │
    │  countDown  countDown    countDown                          │
    └──────────────────────────────────────────────────────────────┘
```

---

## 18. CYCLICBARRIER

```java
import java.util.concurrent.CyclicBarrier;

class Game {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("All players ready! Game starts!");
        });

        for (int i = 1; i <= 3; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    System.out.println("Player " + id + " preparing...");
                    Thread.sleep(1000 * id);
                    System.out.println("Player " + id + " ready!");
                    barrier.await();  // Wait for all players
                    System.out.println("Player " + id + " started playing!");
                } catch (Exception e) {}
            }).start();
        }
    }
}

// Reusable! After all reach barrier, resets for next round
```

```
    CyclicBarrier(3):
    ┌──────────────────────────────────────────────────────────────┐
    │  Player 1: ████ ready ████████████████ await() ──────────┐  │
    │  Player 2: ████████ ready ██████████ await() ──────────┐ │  │
    │  Player 3: ████████████ ready ██████ await() ────────┐ │ │  │
    │                                                      ↓ ↓ ↓  │
    │                                              All reached!    │
    │                                              → barrier action│
    │                                              → all continue  │
    └──────────────────────────────────────────────────────────────┘
```

### CountDownLatch vs CyclicBarrier

```
    ┌──────────────────────┬──────────────────────┬──────────────────────┐
    │ Feature               │ CountDownLatch       │ CyclicBarrier        │
    ├──────────────────────┼──────────────────────┼──────────────────────┤
    │ Reusable              │ ❌ No                 │ ✅ Yes                │
    │ Action on completion  │ ❌ No                 │ ✅ Yes (barrier action)│
    │ Threads wait          │ Any thread           │ All threads          │
    │ Count                 │ Count down           │ Count up/down        │
    │ Use case              │ One-shot event       │ Multi-phase          │
    │ Example               │ App startup          │ Parallel computation  │
    └──────────────────────┴──────────────────────┴──────────────────────┘
```

---

## 19. THREADLOCAL

```java
class UserContext {
    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();

    public static void setUser(String user) { currentUser.set(user); }
    public static String getUser() { return currentUser.get(); }
    public static void clear() { currentUser.remove(); }
}

// Each thread has its own copy
new Thread(() -> {
    UserContext.setUser("Alice");
    System.out.println(UserContext.getUser());  // Alice
}).start();

new Thread(() -> {
    UserContext.setUser("Bob");
    System.out.println(UserContext.getUser());  // Bob
}).start();

// No conflict! Each thread has its own "currentUser"
```

```
    ThreadLocal Memory Model:
    ┌──────────────────────────────────────────────────────────────┐
    │  Thread 1: ThreadLocal → "Alice"                             │
    │  Thread 2: ThreadLocal → "Bob"                               │
    │  Thread 3: ThreadLocal → "Charlie"                           │
    │                                                              │
    │  Each thread has its OWN copy. No synchronization needed!    │
    └──────────────────────────────────────────────────────────────┘
```

---

## 20. VIRTUAL THREADS (Java 21+)

```java
// Platform threads (traditional)
Thread platformThread = Thread.ofPlatform().start(() -> {
    System.out.println("Platform thread: " + Thread.currentThread());
});

// Virtual threads (lightweight)
Thread virtualThread = Thread.ofVirtual().start(() -> {
    System.out.println("Virtual thread: " + Thread.currentThread());
});

// Virtual threads with Executor
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 100_000).forEach(i -> {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return i;
        });
    });
}
// Creates 100,000 virtual threads (platform threads limited to ~1000)
```

```
    Platform vs Virtual Threads:
    ┌──────────────────────────────────────────────────────────────┐
    │  Platform Thread:                                            │
    │  - 1:1 mapping with OS thread                               │
    │  - ~1MB stack per thread                                     │
    │  - Limited to ~1000 threads                                  │
    │  - Expensive context switching                               │
    │                                                              │
    │  Virtual Thread:                                             │
    │  - M:N mapping (many virtual → few platform)                 │
    │  - ~1KB stack per thread                                     │
    │  - Millions of threads possible                              │
    │  - Cheap context switching (JVM managed)                     │
    └──────────────────────────────────────────────────────────────┘
```

---

## 21. THREAD CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════════╗
║                    MULTITHREADING CHEAT SHEET                        ║
╠══════════════════════════════════════════════════════════════════════╣
║                                                                      ║
║  CREATING:   extends Thread / implements Runnable / Lambda / Callable║
║  STARTING:   start() → new thread | run() → same thread             ║
║  SLEEPING:   Thread.sleep(ms) → TIMED_WAITING, doesn't release lock ║
║  JOINING:    t.join() → wait for t to finish                        ║
║  YIELDING:   Thread.yield() → hint to scheduler                     ║
║                                                                      ║
║  SYNCHRONIZED:                                                       ║
║  - synchronized method      → locks 'this'                          ║
║  - synchronized(block)      → locks specific object                  ║
║  - static synchronized      → locks CLASS object                     ║
║                                                                      ║
║  WAIT/NOTIFY:                                                        ║
║  - wait()     → release lock, wait (must be in synchronized)        ║
║  - notify()   → wake ONE waiting thread                              ║
║  - notifyAll()→ wake ALL waiting threads                             ║
║  - Always use in while loop (spurious wakeup)                       ║
║                                                                      ║
║  LOCK:  ReentrantLock (tryLock, lockInterruptibly, Conditions)       ║
║  READWRITE: ReentrantReadWriteLock (concurrent reads, exclusive write)║
║                                                                      ║
║  EXECUTOR:                                                           ║
║  - newFixedThreadPool(n)        → Fixed threads                      ║
║  - newCachedThreadPool()        → Create as needed                   ║
║  - newSingleThreadExecutor()    → One thread                         ║
║  - newScheduledThreadPool(n)    → Scheduled tasks                    ║
║                                                                      ║
║  CALLABLE/FUTURE:  Task with return value, future.get() blocks       ║
║  ATOMIC:  AtomicInteger, AtomicReference, CAS (lock-free)            ║
║  SEMAPHORE: Limit concurrent access (parking lot example)            ║
║  COUNTDOWNLATCH: Wait for N events (one-shot)                       ║
║  CYCLICBARRIER: Wait for N threads (reusable)                       ║
║  THREADLOCAL: Each thread has own copy                               ║
║                                                                      ║
║  DEADLOCK: Circular wait → prevent by locking in same order          ║
║  STARVATION: Thread never gets CPU → prevent by fair scheduling     ║
║  RACE CONDITION: Result depends on timing → prevent by sync          ║
║                                                                      ║
║  JAVA 21: Virtual Threads (millions of lightweight threads)          ║
║                                                                      ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

## 22. MULTITHREADING INTERVIEW QUESTIONS (50+)

### ⭐ BASIC (Q1-Q15)

**Q1: What is a thread?**
> Thread is a lightweight subprocess, smallest unit of execution. Shares heap memory with other threads.

**Q2: Difference between process and thread?**
```
Process: Independent, own memory, heavy context switch
Thread: Shared heap, own stack, lightweight context switch
```

**Q3: What is difference between start() and run()?**
```
start(): Creates new thread, calls run() in that thread
run():   Executes in SAME thread (no new thread created)
Always use start()!
```

**Q4: What is difference between Runnable and Callable?**
```
Runnable:  run() returns void, can't throw checked exceptions
Callable:  call() returns result, can throw exceptions
Used with ExecutorService, returns Future
```

**Q5: What is synchronized keyword?**
> Ensures only one thread can execute a block/method at a time. Prevents race conditions.

**Q6: What is race condition?**
```
Two threads accessing shared data simultaneously:
Thread A: read count=5
Thread B: read count=5
Thread A: write count=6
Thread B: write count=6  ← A's increment lost!
```

**Q7: What is deadlock?**
> Two threads waiting for each other's locks forever. Neither can proceed.

**Q8: What is difference between wait() and sleep()?**
```
wait():    Releases lock, needs synchronized, Object method
sleep():   Doesn't release lock, no sync needed, Thread static method
```

**Q9: What is difference between notify() and notifyAll()?**
```
notify():    Wakes ONE waiting thread
notifyAll(): Wakes ALL waiting threads (safer, recommended)
```

**Q10: What is a daemon thread?**
> Background thread that doesn't prevent JVM from exiting. setDaemon(true) before start(). Example: GC, Finalizer.

**Q11: What is thread priority?**
> Hint to scheduler (1-10, default 5). NOT guaranteed. Higher priority may get more CPU time.

**Q12: What is Thread.sleep()?**
> Pauses current thread for specified milliseconds. Doesn't release locks. Goes to TIMED_WAITING.

**Q13: What is difference between interrupt() and stop()?**
```
interrupt():  Graceful, sets flag, thread can handle
stop():       Deprecated, unsafe, releases all locks abruptly
```

**Q14: What is volatile keyword?**
> Ensures visibility: changes to variable are immediately visible to all threads. Doesn't provide atomicity.

**Q15: What is difference between synchronized and volatile?**
```
synchronized: Mutual exclusion + visibility (locks)
volatile:    Visibility only (no locking, no atomicity)
volatile int count; // not atomic! count++ still unsafe
```

---

### ⭐⭐ MIDDLE (Q16-Q35)

**Q16: What is Java Memory Model?**
```
Defines how threads interact through memory:
- Each thread has its own stack (local variables)
- All threads share heap (objects, static variables)
- volatile/synchronized ensure visibility ordering
- happens-before relationship defines memory guarantees
```

**Q17: What is happens-before?**
> If action A happens-before action B, then A's effects are visible to B. Created by:
> - Thread.start() → any action in started thread
> - Thread.join() → any action after join returns
> - volatile write → subsequent volatile read
> - synchronized unlock → subsequent lock

**Q18: What is ExecutorService?**
> Framework for managing thread pools. Submits tasks (Runnable/Callable), manages thread lifecycle, returns Future results.

**Q19: What is difference between Fixed and Cached thread pool?**
```
Fixed:     Fixed number of threads, tasks wait in queue
Cached:    Creates threads as needed, reuses idle threads, no limit
```

**Q20: What is ReentrantLock?**
> Lock interface with tryLock(), lockInterruptibly(), fairness. More flexible than synchronized. Must unlock in finally block.

**Q21: What is ReadWriteLock?**
> Multiple readers OR one writer at a time. Improves concurrency for read-heavy scenarios.

**Q22: What is CountDownLatch?**
> Wait for N events to complete. Count down on each event, await() blocks until count=0. Not reusable.

**Q23: What is CyclicBarrier?**
> Wait for N threads to reach a point. Reusable. Can execute barrier action when all arrive.

**Q24: What is Semaphore?**
> Controls access to N resources. acquire() waits if no permits, release() frees a permit. Used for rate limiting.

**Q25: What is AtomicInteger?**
> Thread-safe int variable using CAS. Avoids synchronization overhead for simple atomic operations.

**Q26: What is ThreadLocal?**
> Each thread has its own copy of the variable. No synchronization needed. Common in web apps (user context).

**Q27: What is starvation?**
> Thread never gets CPU time due to priority or scheduling. Prevent by using fair locks.

**Q28: What is livelock?**
> Threads keep responding to each other but make no progress. Like two people stepping aside for each other.

**Q29: How to create deadlock-free code?**
```
1. Lock resources in same order
2. Use tryLock() with timeout
3. Avoid nested locks
4. Use lock hierarchy
```

**Q30: What is difference between Concurrent and Synchronized collections?**
```
Concurrent:  Fine-grained locking (per-bucket), higher concurrency
Synchronized: Coarse-grained locking (whole collection), lower concurrency
```

**Q31: What is FutureTask?**
> A cancellable asynchronous computation. Implements Runnable and Future. Can be executed by Executor or Thread.

**Q32: What is ForkJoinPool?**
> Special executor for divide-and-conquer tasks. Uses work-stealing: idle threads steal from busy threads' queues.

**Q33: What is CompletableFuture?**
```java
CompletableFuture.supplyAsync(() -> fetchData())
    .thenApply(data -> processData(data))
    .thenAccept(result -> System.out.println(result))
    .exceptionally(e -> { e.printStackTrace(); return null; });
// Non-blocking, chainable, composition
```

**Q34: What is Exchanger?**
> Two threads exchange data at a synchronization point. Each thread calls exchange(data) and receives the other's data.

**Q35: What isPhaser?**
> More flexible CyclicBarrier + CountDownLatch combined. Supports dynamic number of parties.

---

### ⭐⭐⭐ ADVANCED (Q36-Q50)

**Q36: What is the difference between synchronized and volatile?**
```
synchronized: Atomicity + Visibility + Mutual exclusion
volatile:     Visibility only (no atomicity, no mutual exclusion)
volatile int x; x++ is NOT atomic (read-increment-write)
```

**Q37: What is CAS and ABA problem?**
```
CAS: Compare-And-Swap (hardware atomic operation)
ABA Problem: Value changes A→B→A, CAS thinks nothing changed
Solution: AtomicInteger stamp versions (AtomicStampedReference)
```

**Q38: What is happens-before in detail?**
```
Program Order Rule: Within thread, each action happens-before next
Monitor Lock Rule: Unlock happens-before subsequent lock
Volatile Variable Rule: Write happens-before subsequent read
Thread Start Rule: start() happens-before any action in started thread
Thread Termination: Any action happens-before join() returns
Transitivity: If A hb B and B hb C, then A hb C
```

**Q39: What is the difference between wait/notify and BlockingQueue?**
```
wait/notify: Manual, error-prone, single condition
BlockingQueue: Built-in, thread-safe, multiple conditions
Use BlockingQueue for producer-consumer!
```

**Q40: What is CompletableFuture in detail?**
```java
CompletableFuture<String> cf = CompletableFuture
    .supplyAsync(() -> "Hello")
    .thenApply(s -> s + " World")
    .thenApply(s -> s.toUpperCase())
    .thenCombine(CompletableFuture.completedFuture("!"), (s, t) -> s + t);

System.out.println(cf.get());  // HELLO WORLD!
```

**Q41: What is Fork/Join framework?**
> Divide-and-conquer parallelism. Tasks split into subtasks. Uses work-stealing for load balancing. ForkJoinPool is the executor.

**Q42: What is the difference between RecursiveTask and RecursiveAction?**
```
RecursiveTask: Returns result (has compute() returning V)
RecursiveAction: No result (has compute() returning void)
Both extend ForkJoinTask
```

**Q43: What is the difference between interrupt() and interrupted()?**
```
interrupt():      Sets interrupt flag on TARGET thread
interrupted():    Checks AND CLEARS flag on CURRENT thread
isInterrupted():  Checks flag WITHOUT clearing
```

**Q44: What is daemon thread?**
```java
Thread t = new Thread(() -> { ... });
t.setDaemon(true);  // Before start()
t.start();
// JVM exits when only daemon threads remain
// Examples: GC, Finalizer, Signal Dispatcher
```

**Q45: What is the difference between ThreadPoolExecutor parameters?**
```java
new ThreadPoolExecutor(
    corePoolSize,      // Minimum threads (always alive)
    maximumPoolSize,   // Maximum threads
    keepAliveTime,     // Idle time before thread dies
    TimeUnit,          // Time unit
    workQueue,         // Task queue
    threadFactory,     // Custom thread creation
    handler            // Rejection policy
);
```

**Q46: What are Rejection Policies?**
```
AbortPolicy:        Throws RejectedExecutionException (default)
CallerRunsPolicy:   Caller thread runs the task
DiscardPolicy:      Silently discard task
DiscardOldestPolicy: Discard oldest task in queue
```

**Q47: What is Thread Group?**
> Deprecated. Used to manage group of threads. Set max priority, daemon status for all threads in group. Use ExecutorService instead.

**Q48: What is the difference between Lock and synchronized?**
```
Lock:       tryLock, interruptible, fair, multiple conditions
Synchronized: Automatic unlock, simpler, no fairness
Use Lock for complex scenarios, synchronized for simple cases
```

**Q49: How to test multithreaded code?**
```
1. Use CountDownLatch/CyclicBarrier for synchronization
2. Stress test with many threads
3. Use Thread.sleep() for timing (not reliable)
4. Use CyclicBarrier to start threads simultaneously
5. Use JMH for benchmarking
6. Static analysis tools (FindBugs, SpotBugs)
```

**Q50: What are best practices for multithreading?**
```
1. Prefer ExecutorService over manual Thread creation
2. Use BlockingQueue for producer-consumer
3. Use atomic classes for simple operations
4. Use volatile for flags, not for compound operations
5. Always unlock in finally block (Lock)
6. Use while loop with wait() (spurious wakeup)
7. Minimize lock scope
8. Prefer ConcurrentHashMap over synchronizedMap
9. Use ThreadLocal for per-thread data
10. Avoid deadlock by locking in same order
```

---

## 23. PRODUCER-CONSUMER CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║              PRODUCER-CONSUMER PATTERNS                          ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  PATTERN 1: wait/notify                                         ║
║  ┌──────────────────────────────────────────────────────────┐   ║
║  │  synchronized void produce() {                           │   ║
║  │      while (full) wait();                                 │   ║
║  │      queue.add(item);                                     │   ║
║  │      notify();                                            │   ║
║  │  }                                                       │   ║
║  │  synchronized void consume() {                           │   ║
║  │      while (empty) wait();                                │   ║
║  │      item = queue.poll();                                 │   ║
║  │      notify();                                            │   ║
║  │  }                                                       │   ║
║  └──────────────────────────────────────────────────────────┘   ║
║                                                                  ║
║  PATTERN 2: BlockingQueue (RECOMMENDED)                          ║
║  ┌──────────────────────────────────────────────────────────┐   ║
║  │  Producer: queue.put(item);    // blocks if full         │   ║
║  │  Consumer: item = queue.take(); // blocks if empty       │   ║
║  └──────────────────────────────────────────────────────────┘   ║
║                                                                  ║
║  PATTERN 3: BlockingQueue with multiple producers/consumers      ║
║  ┌──────────────────────────────────────────────────────────┐   ║
║  │  Producer 1 ──→ ┌──────────────┐ ──→ Consumer 1         │   ║
║  │  Producer 2 ──→ │ BlockingQueue│ ──→ Consumer 2         │   ║
║  │  Producer 3 ──→ └──────────────┘ ──→ Consumer 3         │   ║
║  └──────────────────────────────────────────────────────────┘   ║
║                                                                  ║
║  PATTERN 4: Semaphore + BlockingQueue                            ║
║  ┌──────────────────────────────────────────────────────────┐   ║
║  │  Semaphore limits rate of production/consumption         │   ║
║  └──────────────────────────────────────────────────────────┘   ║
║                                                                  ║
║  PATTERN 5: CompletableFuture (Java 8+)                          ║
║  ┌──────────────────────────────────────────────────────────┐   ║
║  │  CompletableFuture                                     │   ║
║  │      .supplyAsync(() -> produce())                      │   ║
║  │      .thenAccept(item -> consume(item));                │   ║
║  └──────────────────────────────────────────────────────────┘   ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 24. VIRTUAL THREADS CHEAT SHEET (Java 21+)

```
╔══════════════════════════════════════════════════════════════════╗
║              VIRTUAL THREADS CHEAT SHEET                          ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  Platform Thread:  1:1 with OS, ~1MB stack, ~1000 max           ║
║  Virtual Thread:   M:N with OS, ~1KB stack, millions possible   ║
║                                                                  ║
║  CREATE:                                                       ║
║  Thread.start(() -> task());           // Simple                 ║
║  Thread.ofVirtual().name("vt").start(() -> task());             ║
║  Executors.newVirtualThreadPerTaskExecutor()                     ║
║                                                                  ║
║  USE:                                                           ║
║  try (var executor = Executors.newVirtualThreadPerTaskExecutor()){║
║      executor.submit(() -> blockingTask());                     ║
║  }                                                              ║
║                                                                  ║
║  BEST FOR:                                                      ║
║  ✅ I/O-bound tasks (HTTP, DB, File)                            ║
║  ✅ Many concurrent connections                                  ║
║  ✅ Replacing async/await patterns                               ║
║                                                                  ║
║  NOT BEST FOR:                                                  ║
║  ❌ CPU-bound tasks (limited by platform threads)               ║
║  ❌ Thread-local with large data                                 ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: All Multithreading Topics, Producer-Consumer, Virtual Threads, Interview Questions*
