# Mastering multithreading for interviews: topics from basics to advance

**Session ID:** ses_2c267dc57ffeXJcatdtx0bX3ym
**Created:** 3/30/2026, 12:44:34 PM
**Updated:** 3/30/2026, 6:34:50 PM

---

## User

i want to master multithreading for interview , i literally want to master every topic , can you list all the topic basic to advance

---

## Assistant (Build · big-pickle · 27.9s)

_Thinking:_

The user wants to learn multithreading comprehensively for interview preparation. They want a complete list of topics from basic to advanced. Let me provide a comprehensive structured list covering all multithreading concepts.

Here's a complete multithreading roadmap from basic to advanced:

## **Fundamentals**
- Process vs Thread
- Concurrency vs Parallelism
- Synchronous vs Asynchronous
- Single-threaded vs Multi-threaded models

## **Thread Basics**
- Creating threads (Thread class, Runnable, Callable, Executors)
- Thread lifecycle (New, Runnable, Blocked, Waiting, Timed_Waiting, Terminated)
- Thread priorities
- Daemon threads
- Thread naming and identification

## **Thread Synchronization (Core)**
- `synchronized` keyword (methods, blocks, static)
- Lock objects (`ReentrantLock`, `ReentrantReadWriteLock`)
- `volatile` keyword
- `wait()`, `notify()`, `notifyAll()`
- `Condition` objects
- Atomic variables (`AtomicInteger`, `AtomicReference`, etc.)

## **Thread Communication**
- Producer-Consumer problem
- Deadlock (causes, detection, prevention)
- Livelock
- Starvation
- Race conditions

## **Advanced Synchronization**
- Semaphores
- CountDownLatch
- CyclicBarrier
- Phaser
- Exchanger
- StampedLock

## **Executor Framework**
- ExecutorService (ThreadPoolExecutor, ScheduledExecutorService)
- Fork/Join Framework
- ThreadPool types (Fixed, Cached, Single, Scheduled)
- Callable & Future
- CompletableFuture
- ThreadFactory

## **High-Level Concurrency**
- Thread-safe collections (ConcurrentHashMap, CopyOnWriteArrayList, BlockingQueue)
- Concurrent utilities (ConcurrentMap, ConcurrentNavigableMap)
- ThreadLocal variables
- InheritableThreadLocal

## **Memory & Performance**
- Happens-before relationship
- Memory barriers
- JMM (Java Memory Model)
- CPU caching (MESI protocol)
- False sharing
- Cache padding

## **Advanced Patterns**
- Immutable objects
- Thread-safe singletons (double-checked locking)
- Read-Write locks
- Lock striping
- Thread confinement
- Actor model basics

## **Interruption & Cancellation**
- Thread interruption mechanism
- Cooperative cancellation
- Handling InterruptedException

## **Debugging & Best Practices**
- Analyzing thread dumps
- Detecting deadlocks (jstack, VisualVM)
- Performance tuning
- Common pitfalls

## **Real-World Scenarios**
- Connection pooling
- Batch processing with threads
- Async logging
- Rate limiting
- Parallel stream processing

Would you like me to create study materials, code examples, or interview questions for any specific topic?

---

## User

start from fundamentals

---

## Assistant (Build · big-pickle · 37.6s)

_Thinking:_

The user wants to start learning multithreading from fundamentals. I should provide a comprehensive explanation of the basic concepts first. Let me start with the foundational concepts.

## Process vs Thread

### Process
- An executing program with its own memory space
- Isolated from other processes
- Heavyweight (expensive to create/destroy)
- Communication via IPC (pipes, sockets, etc.)

### Thread
- Lightweight subprocess within a process
- Shares memory with other threads in same process
- Shares heap, code, data segments
- Cheaper to create than processes

```
Process A                 Process B
┌─────────────┐          ┌─────────────┐
│ Thread 1    │          │ Thread 1    │
│ Thread 2    │          │ Thread 2    │
│ Thread 3    │          │             │
└─────────────┘          └─────────────┘
     ↑                         ↑
   ISOLATED                  ISOLATED
```

```java
// Getting current process and thread info
public class ProcessVsThread {
    public static void main(String[] args) {
        // Process info
        System.out.println("Process ID: " + ProcessHandle.current().pid());
        System.out.println("Process: " + ProcessHandle.current().info());
        
        // Thread info
        System.out.println("Thread ID: " + Thread.currentThread().getId());
        System.out.println("Thread Name: " + Thread.currentThread().getName());
        System.out.println("Thread Priority: " + Thread.currentThread().getPriority());
        System.out.println("Thread State: " + Thread.currentThread().getState());
        System.out.println("CPU Cores: " + Runtime.getRuntime().availableProcessors());
    }
}
```

---

## Concurrency vs Parallelism

### Concurrency
- **Handling multiple tasks at the same time** (not necessarily simultaneously)
- Single CPU core: rapidly switching between tasks
- Makes progress on multiple tasks by interleaving
- Focus: **managing** competing tasks

### Parallelism
- **Executing multiple tasks simultaneously**
- Requires multiple CPU cores
- True simultaneous execution
- Focus: **performing** multiple tasks

```
CONCURRENCY (1 Core)          PARALLELISM (2+ Cores)
─────────────────             ─────────────────────
Time ─────────────────►       Time ─────────────────►

Task A: ████░░░░████░░░░       Task A: ████████████████
Task B: ░░░░████░░░░████       Task B: ████████████████
       ↑ interleaving                 ↑ simultaneously
```

### When to use each:

| Scenario | Approach |
|----------|----------|
| I/O-bound tasks (database, network) | Concurrency |
| CPU-intensive tasks (computation) | Parallelism |
| Web servers handling requests | Concurrency |
| Data processing / scientific compute | Parallelism |

```java
public class ConcurrencyVsParallelism {
    public static void main(String[] args) throws InterruptedException {
        int taskCount = 4;
        
        // CONCURRENCY: Single thread, many tasks (interleaved)
        System.out.println("=== CONCURRENCY (Single Thread) ===");
        Thread concurrentThread = new Thread(() -> {
            for (int i = 0; i < taskCount; i++) {
                System.out.println("Task " + i + " started by " + Thread.currentThread().getName());
                simulateWork();
                System.out.println("Task " + i + " completed");
            }
        }, "Concurrent-Worker");
        concurrentThread.start();
        concurrentThread.join();
        
        // PARALLELISM: Multiple threads, multiple cores
        System.out.println("\n=== PARALLELISM (Multiple Threads) ===");
        Thread[] threads = new Thread[taskCount];
        for (int i = 0; i < taskCount; i++) {
            final int taskId = i;
            threads[i] = new Thread(() -> {
                System.out.println("Task " + taskId + " started by " + Thread.currentThread().getName());
                simulateWork();
                System.out.println("Task " + taskId + " completed");
            }, "Parallel-Worker-" + i);
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }
    
    static void simulateWork() {
        try { Thread.sleep(100); } catch (InterruptedException e) {}
    }
}
```

---

## Synchronous vs Asynchronous

### Synchronous
- Tasks execute one after another
- Caller waits for completion before continuing
- Order is predictable

### Asynchronous
- Caller initiates task and continues without waiting
- Completion notified via callbacks/futures
- Order may not be predictable

```
SYNCHRONOUS:
Caller ──────► Task ──────► [waits] ◄───── Result
                │
                └───────► continues only after result

ASYNCHRONOUS:
Caller ──────► Task ──────► [fires & forgets]
                │
                └───────► continues immediately
                          ↓
                    [later] Result/Callback
```

```java
public class SyncVsAsync {
    
    // SYNCHRONOUS
    static String syncOperation(String data) {
        System.out.println("Sync: Processing " + data);
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        return data.toUpperCase();
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== SYNCHRONOUS ===");
        long start = System.currentTimeMillis();
        String result1 = syncOperation("hello");
        String result2 = syncOperation("world");
        System.out.println("Results: " + result1 + ", " + result2);
        System.out.println("Time: " + (System.currentTimeMillis() - start) + "ms");
        
        System.out.println("\n=== ASYNCHRONOUS (with CompletableFuture) ===");
        start = System.currentTimeMillis();
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> syncOperation("hello"));
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> syncOperation("world"));
        
        System.out.println("Main thread continuing...");
        String combined = future1.get() + ", " + future2.get();
        System.out.println("Results: " + combined);
        System.out.println("Time: " + (System.currentTimeMillis() - start) + "ms");
    }
}
```

---

## Single-threaded vs Multi-threaded

### Single-threaded
- One task at a time
- No synchronization needed
- Simpler, but can't utilize multiple cores
- Example: Simple calculator, basic scripts

### Multi-threaded
- Multiple tasks concurrently
- Can utilize multiple CPU cores
- Requires synchronization
- Example: Web servers, games, data processors

```java
public class SingleVsMultiThread {
    
    static void processTasks(int count) {
        for (int i = 0; i < count; i++) {
            System.out.println("Processing task " + i + " by " + Thread.currentThread().getName());
        }
    }
    
    public static void main(String[] args) {
        int taskCount = 3;
        
        System.out.println("=== SINGLE-THREADED ===");
        processTasks(taskCount);
        
        System.out.println("\n=== MULTI-THREADED ===");
        Thread[] threads = new Thread[taskCount];
        for (int i = 0; i < taskCount; i++) {
            final int taskId = i;
            threads[i] = new Thread(() -> {
                System.out.println("Processing task " + taskId + " by " + Thread.currentThread().getName());
            });
            threads[i].start();
        }
        
        // Wait for all threads
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) {}
        }
    }
}
```

---

## Quick Comparison Table

| Aspect | Process | Thread |
|--------|---------|--------|
| Memory | Separate heap | Shared heap |
| Creation | Slow | Fast |
| Communication | IPC | Direct (shared memory) |
| Isolation | Strong | Weak |
| Overhead | High | Low |

| Aspect | Sync | Async |
|--------|------|-------|
| Order | Predictable | Unpredictable |
| Blocking | Yes | No |
| Complexity | Lower | Higher |
| Best for | CPU tasks | I/O tasks |

---

Want me to continue with **Thread Basics** (creating threads, lifecycle, priorities)?

---

## User

yes

---

## Assistant (Build · big-pickle · 70.7s)

_Thinking:_

The user wants me to continue with Thread Basics - creating threads, lifecycle, priorities. I'll cover:
1. Creating threads (Thread class, Runnable, Callable, Executors)
2. Thread lifecycle states
3. Thread priorities
4. Daemon threads
5. Thread naming and identification

Let me create comprehensive code examples for each.

## Thread Basics

### Creating Threads - 4 Ways

```java
// ============================================
// WAY 1: Extend Thread class
// ============================================
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running: " + getName());
    }
}

// ============================================
// WAY 2: Implement Runnable (preferred)
// ============================================
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable running in: " + Thread.currentThread().getName());
    }
}

// ============================================
// WAY 3: Implement Callable (returns value, throws exception)
// ============================================
class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
        return sum;
    }
}

// ============================================
// WAY 4: Executor Framework (BEST for production)
// ============================================
class ThreadCreationDemo {
    public static void main(String[] args) throws Exception {
        
        // WAY 1: Thread class
        Thread thread1 = new MyThread();
        thread1.start();  // Don't call run() - that's single-threaded!
        
        // WAY 2: Runnable with Thread
        Thread thread2 = new Thread(new MyRunnable());
        thread2.start();
        
        // WAY 3: Lambda (cleanest for simple tasks)
        Thread thread3 = new Thread(() -> {
            System.out.println("Lambda thread: " + Thread.currentThread().getName());
        });
        thread3.start();
        
        // WAY 4: ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Submit Runnable
        executor.submit(() -> System.out.println("Executor task 1"));
        
        // Submit Callable
        Future<Integer> future = executor.submit(new MyCallable());
        System.out.println("Sum from Callable: " + future.get());  // Blocks until result
        
        // Submit Lambda Callable
        Future<String> future2 = executor.submit(() -> {
            return "Hello from executor!";
        });
        System.out.println(future2.get());
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        
        // WAIT for all threads to complete
        thread1.join();
        thread2.join();
        thread3.join();
        
        System.out.println("\nAll threads completed!");
    }
}
```

**Key Point:** Always use `start()` not `run()`. `run()` runs in current thread, `start()` creates new thread.

---

### Thread Lifecycle (6 States)

```java
public class ThreadLifecycle {
    public static void main(String[] args) throws InterruptedException {
        
        Thread thread = new Thread(() -> {
            System.out.println("State during run(): " + Thread.currentThread().getState());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        System.out.println("1. NEW: " + thread.getState());  // NEW
        
        thread.start();
        System.out.println("2. RUNNABLE (after start): " + thread.getState());
        
        Thread.sleep(100);
        System.out.println("3. TIMED_WAITING (during sleep): " + thread.getState());
        
        thread.join();
        System.out.println("4. TERMINATED: " + thread.getState());
    }
}
```

```
                    ┌─────────────────────────────────────────┐
                    │                                         │
                    ▼                                         │
              ┌──────────┐                                    │
              │   NEW    │  (Thread created, not started)     │
              └────┬─────┘                                    │
                   │ start()                                  │
                   ▼                                          │
              ┌──────────┐                                    │
              │ RUNNABLE │◄──┐ (ready to run or running)      │
              └────┬─────┘   │                                │
                   │         │                               │
         ┌─────────┼─────────┼──────────┐                    │
         │         │         │          │                    │
         ▼         ▼         ▼          │                    │
   ┌──────────┐ ┌──────┐ ┌────────┐    │                    │
   │ BLOCKED  │ │WAITING│ │TIMED_  │────┘                    │
   │(waiting  │ │(wait()│ │WAITING │                         │
   │for lock) │ │notify │ │(sleep/ │                         │
   └────┬─────┘ └──┬───┘ │ join)  │                          │
        │          │     └────────┘                          │
        └──────────┴──────────────────────────────────────────┘
                              │
                              ▼
                        ┌──────────┐
                        │TERMINATED│
                        └──────────┘
```

**State Transitions:**
```java
public class StateTransitions {
    public static void main(String[] args) throws InterruptedException {
        
        // 1. NEW → RUNNABLE
        Thread t = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        });
        System.out.println("NEW: " + t.getState());  // NEW
        t.start();
        System.out.println("RUNNABLE: " + t.getState());  // RUNNABLE
        
        // 2. RUNNABLE → TIMED_WAITING (sleep)
        Thread.sleep(50);
        System.out.println("TIMED_WAITING (sleep): " + t.getState());  // TIMED_WAITING
        
        // 3. Demo BLOCKED state
        Object lock = new Object();
        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                try { Thread.sleep(200); } catch (InterruptedException e) {}
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("Thread 2 got lock");
            }
        });
        
        thread1.start();
        Thread.sleep(50);  // Let thread1 get lock
        thread2.start();   // This will be BLOCKED
        Thread.sleep(50);
        System.out.println("BLOCKED: " + thread2.getState());  // BLOCKED
        
        // 4. RUNNABLE → WAITING (wait())
        Thread waitThread = new Thread(() -> {
            synchronized (lock) {
                try { lock.wait(); } catch (InterruptedException e) {}
            }
        });
        waitThread.start();
        Thread.sleep(50);
        System.out.println("WAITING: " + waitThread.getState());  // WAITING
        
        // Notify to TERMINATE
        synchronized (lock) { lock.notify(); }
        waitThread.join();
        System.out.println("TERMINATED: " + waitThread.getState());  // TERMINATED
    }
}
```

---

### Thread Priorities

```java
public class ThreadPriorities {
    public static void main(String[] args) {
        
        // Priority range: 1 (MIN) to 10 (MAX), default is 5
        System.out.println("MIN_PRIORITY: " + Thread.MIN_PRIORITY);   // 1
        System.out.println("NORM_PRIORITY: " + Thread.NORM_PRIORITY); // 5
        System.out.println("MAX_PRIORITY: " + Thread.MAX_PRIORITY);   // 10
        
        Thread low = new Thread(() -> runTasks("Low"), "Low-Priority");
        Thread med = new Thread(() -> runTasks("Med"), "Med-Priority");
        Thread high = new Thread(() -> runTasks("High"), "High-Priority");
        
        low.setPriority(Thread.MIN_PRIORITY);    // 1
        med.setPriority(Thread.NORM_PRIORITY);  // 5
        high.setPriority(Thread.MAX_PRIORITY);  // 10
        
        low.start();
        med.start();
        high.start();
    }
    
    static void runTasks(String name) {
        for (int i = 0; i < 3; i++) {
            System.out.println(name + " thread running iteration " + i);
            Thread.yield();  // Suggests scheduler to give CPU to other threads
        }
    }
}
```

**Important:** Priority is just a *hint* to the OS scheduler. It doesn't guarantee execution order!

---

### Daemon Threads

```java
public class DaemonThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        
        // User thread - JVM waits for it to complete
        Thread userThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("User thread: " + i);
                sleep(100);
            }
        }, "User-Thread");
        
        // Daemon thread - doesn't prevent JVM from exiting
        Thread daemonThread = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                System.out.println("Daemon thread: " + i);
                sleep(50);
            }
        }, "Daemon-Thread");
        
        daemonThread.setDaemon(true);  // MUST set before start()
        
        userThread.start();
        daemonThread.start();
        
        // JVM will exit when only daemon threads remain
    }
    
    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }
}
```

**Daemon vs User Threads:**
| Aspect | Daemon | User |
|--------|--------|------|
| JVM waits | No | Yes |
| Use case | Background tasks | Main work |
| Cleanup | No guaranteed | Guaranteed |
| Examples | GC, monitoring | Business logic |

---

### Thread Naming & Identification

```java
public class ThreadIdentity {
    public static void main(String[] args) {
        
        // Main thread
        Thread main = Thread.currentThread();
        System.out.println("Main Thread Info:");
        System.out.println("  ID: " + main.getId());
        System.out.println("  Name: " + main.getName());
        System.out.println("  Priority: " + main.getPriority());
        System.out.println("  State: " + main.getState());
        System.out.println("  ThreadGroup: " + main.getThreadGroup().getName());
        System.out.println("  Is Alive: " + main.isAlive());
        System.out.println("  Is Daemon: " + main.isDaemon());
        
        // Create named threads
        Thread t1 = new Thread(() -> {}, "Worker-1");
        Thread t2 = new Thread(() -> {}, "Worker-2");
        Thread t3 = new Thread(() -> {}, "Worker-3");
        
        t1.start();
        t2.start();
        t3.start();
        
        // Enumerate all threads
        System.out.println("\nAll Active Threads:");
        Thread.getAllStackTraces().keySet().forEach(t -> 
            System.out.println("  " + t.getName() + " (id=" + t.getId() + ")")
        );
        
        // Thread ID uniqueness
        System.out.println("\nThread IDs: t1=" + t1.getId() + ", t2=" + t2.getId() + ", t3=" + t3.getId());
    }
}
```

**Output Example:**
```
Main Thread Info:
  ID: 1
  Name: main
  Priority: 5
  State: RUNNABLE
  ThreadGroup: main
  Is Alive: true
  Is Daemon: false
```

---

### Complete Practice Example

```java
public class ThreadBasicsPractice {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Thread Basics Practice ===\n");
        
        // 1. Create threads using different methods
        System.out.println("--- Creating Threads ---");
        
        // Thread with Runnable
        Thread runnableThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Runnable thread: " + Thread.currentThread().getName());
            }
        }, "MyRunnable");
        
        // Lambda (cleaner)
        Thread lambdaThread = new Thread(() -> {
            System.out.println("Lambda thread: " + Thread.currentThread().getName());
        }, "MyLambda");
        
        runnableThread.start();
        lambdaThread.start();
        
        // 2. Callable with Future
        System.out.println("\n--- Callable with Future ---");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(() -> {
            int sum = 0;
            for (int i = 1; i <= 100; i++) sum += i;
            return sum;
        });
        System.out.println("Sum 1-100: " + future.get());
        executor.shutdown();
        
        // 3. Check thread lifecycle
        System.out.println("\n--- Thread Lifecycle ---");
        Thread lifecycleThread = new Thread(() -> {
            System.out.println("Running: " + Thread.currentThread().getState());
        }, "Lifecycle");
        System.out.println("Before start: " + lifecycleThread.getState());
        lifecycleThread.start();
        lifecycleThread.join();
        System.out.println("After completion: " + lifecycleThread.getState());
        
        // 4. Daemon thread behavior
        System.out.println("\n--- Daemon Thread ---");
        Thread daemon = new Thread(() -> {
            while (true) {
                System.out.println("Daemon running...");
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
        }, "MyDaemon");
        daemon.setDaemon(true);
        daemon.start();
        
        // Give daemon time to run, then user thread ends
        Thread.sleep(1500);
        System.out.println("Main thread ending - JVM will exit if only daemon remains");
    }
}
```

---

### Interview Quick Reference

```
Q: Difference between start() and run()?
A: start() creates new thread and executes in parallel
   run() executes in current thread (no parallelism)

Q: Can we start a thread twice?
A: No! Will throw IllegalThreadStateException

Q: What is daemon thread?
A: Low-priority thread that doesn't prevent JVM from exiting

Q: How to make a thread daemon?
A: setDaemon(true) BEFORE start()

Q: Default thread priority?
A: 5 (NORM_PRIORITY)

Q: Callable vs Runnable?
A: Callable returns value & throws checked exceptions, Runnable doesn't
```

---

Want me to continue with **Thread Synchronization** (synchronized, volatile, locks)?

---

## User

yes 

---

## Assistant (Build · big-pickle · 67.9s)

_Thinking:_

The user wants to continue learning about thread synchronization. I should cover:
1. synchronized keyword (methods, blocks, static)
2. Lock objects (ReentrantLock, ReentrantReadWriteLock)
3. volatile keyword
4. wait(), notify(), notifyAll()
5. Condition objects
6. Atomic variables

Let me create comprehensive content with code examples.

## Thread Synchronization

### The Problem: Race Condition

```java
public class RaceConditionDemo {
    private static int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        int numThreads = 1000;
        Thread[] threads = new Thread[numThreads];
        
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> counter++);
            threads[i].start();
        }
        
        for (Thread t : threads) {
            t.join();
        }
        
        System.out.println("Expected: " + numThreads);
        System.out.println("Actual: " + counter);  // Probably wrong due to race condition!
    }
}
```

**Output:** `Actual: 997` (or some other wrong number) - because multiple threads read/write `counter` simultaneously.

---

### 1. `synchronized` Keyword

```java
public class SynchronizedDemo {
    private int counter = 0;
    private final Object lock = new Object();
    
    // ============================================
    // SYNCHRONIZED INSTANCE METHOD
    // Locks: 'this' object
    // ============================================
    public synchronized void incrementSyncMethod() {
        counter++;
        // Equivalent to: synchronized(this) { counter++; }
    }
    
    public void incrementSyncBlock() {
        // ============================================
        // SYNCHRONIZED BLOCK (more granular control)
        // Locks: specified object
        // ============================================
        synchronized (this) {
            counter++;
        }
        // Only this line is synchronized, rest is not
        doSomething();
    }
    
    public void incrementSyncBlockCustomLock() {
        // ============================================
        // SYNCHRONIZED ON CUSTOM OBJECT
        // Best practice: use dedicated lock object
        // ============================================
        synchronized (lock) {
            counter++;
        }
    }
    
    private static int staticCounter = 0;
    
    // ============================================
    // SYNCHRONIZED STATIC METHOD
    // Locks: Class object (SynchronizedDemo.class)
    // ============================================
    public static synchronized void incrementStaticSync() {
        staticCounter++;
        // Equivalent to: synchronized(SynchronizedDemo.class) { staticCounter++; }
    }
    
    public static void incrementStaticBlock() {
        // Synchronized on class object
        synchronized (SynchronizedDemo.class) {
            staticCounter++;
        }
    }
    
    private void doSomething() {
        // Non-synchronized work
    }
}
```

### Synchronized Rules Summary

```java
public class SynchronizedRules {
    private final Object lock = new Object();
    private int instanceVar = 0;
    private static int staticVar = 0;
    
    // RULE 1: Synchronized instance method = synchronized(this)
    public synchronized void instanceMethod() { /* locks 'this' */ }
    
    // RULE 2: Synchronized static method = synchronized(ClassName.class)
    public static synchronized void staticMethod() { /* locks ClassName.class */ }
    
    // RULE 3: Multiple locks are independent
    public void multipleLocks() {
        synchronized (this) {          // Lock 1
            synchronized (lock) {       // Lock 2 - different lock!
                instanceVar++;
            }
        }
    }
    
    // RULE 4: Never synchronize on String literal or autoboxed values
    // BAD - could cause deadlocks if other code uses same String
    public void badPractice() {
        synchronized ("myLock") { }  // AVOID THIS!
    }
    
    // GOOD - private final lock object
    public void goodPractice() {
        synchronized (lock) { }  // SAFE
    }
}
```

---

### 2. `volatile` Keyword

```java
public class VolatileDemo {
    
    // ============================================
    // PROBLEM WITHOUT volatile:
    // ============================================
    private boolean running = true;  // May get cached in CPU register
    
    public void badExample() {
        new Thread(() -> {
            while (running) {
                // Compiler might optimize: while(running) {} 
                // and never check main memory!
            }
        }).start();
        
        // Some time later...
        running = false;  // May not be seen by other thread!
    }
    
    // ============================================
    // SOLUTION: volatile
    // ============================================
    private volatile boolean runningVolatile = true;
    
    public void goodExample() {
        new Thread(() -> {
            while (runningVolatile) {
                // Always reads from main memory
            }
        }).start();
        
        runningVolatile = false;  // Always written to main memory
    }
    
    // ============================================
    // volatile vs synchronized
    // ============================================
    private volatile int volatileCounter = 0;
    
    public void volatileVsSync() {
        // VOLATILE: Guarantees visibility, not atomicity
        volatileCounter = 10;           // VISIBLE to other threads
        System.out.println(volatileCounter);  // VISIBLE from other threads
        
        // But this is NOT thread-safe:
        volatileCounter++;  // read-modify-write - NOT atomic!
        
        // SYNCHRONIZED: Guarantees both visibility AND atomicity
        synchronized (this) {
            volatileCounter++;  // ATOMIC
        }
    }
}
```

**volatile Guarantees:**
```
┌─────────────────────────────────────────────────────────┐
│                    volatile                             │
├─────────────────────────────────────────────────────────┤
│ ✓ Visibility across threads                             │
│ ✓ Always reads/writes from main memory                  │
│ ✓ Prevents instruction reordering                       │
│ ✗ Does NOT guarantee atomicity for compound actions    │
└─────────────────────────────────────────────────────────┘
```

---

### 3. `wait()`, `notify()`, `notifyAll()`

```java
public class WaitNotifyDemo {
    private final Object lock = new Object();
    private boolean ready = false;
    
    // ============================================
    // CONSUMER: Waits until data is ready
    // ============================================
    public void consume() throws InterruptedException {
        synchronized (lock) {
            while (!ready) {
                System.out.println("Consumer waiting...");
                lock.wait();  // Releases lock, waits for notify
            }
            System.out.println("Consumer: Data is ready!");
            ready = false;
        }
    }
    
    // ============================================
    // PRODUCER: Signals when data is ready
    // ============================================
    public void produce() throws InterruptedException {
        synchronized (lock) {
            System.out.println("Producer: Creating data...");
            ready = true;
            lock.notify();  // Wakes up ONE waiting thread
            // lock.notifyAll();  // Wakes up ALL waiting threads
        }
    }
    
    public static void main(String[] args) {
        WaitNotifyDemo demo = new WaitNotifyDemo();
        
        Thread consumer = new Thread(() -> {
            try { demo.consume(); } catch (InterruptedException e) {}
        }, "Consumer");
        
        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(1000);  // Simulate delay
                demo.produce();
            } catch (InterruptedException e) {}
        }, "Producer");
        
        consumer.start();
        producer.start();
    }
}
```

**Important Rules for wait/notify:**
```java
public class WaitNotifyRules {
    private final Object lock = new Object();
    
    public void correctUsage() {
        synchronized (lock) {  // Must own the monitor
            while (conditionIsFalse) {  // Always use while, not if
                try {
                    lock.wait();  // Must be in try-catch
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            // Do work with condition true
        }
    }
    
    public void commonMistakes() {
        // WRONG 1: Not in synchronized block
        // lock.wait();  // IllegalMonitorStateException!
        
        // WRONG 2: Using if instead of while
        // if (condition) { lock.wait(); }  // Spurious wakeups!
        
        // WRONG 3: Calling notify when not synchronized
        // lock.notify();  // IllegalMonitorStateException!
    }
}
```

---

### 4. Lock Objects

```java
import java.util.concurrent.locks.*;

public class ReentrantLockDemo {
    
    private final ReentrantLock lock = new ReentrantLock();
    private int counter = 0;
    
    // ============================================
    // BASIC LOCK USAGE
    // ============================================
    public void increment() {
        lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock();  // ALWAYS in finally!
        }
    }
    
    // ============================================
    // TRY LOCK (non-blocking)
    // ============================================
    public boolean tryIncrement() {
        if (lock.tryLock()) {  // Returns immediately, no waiting
            try {
                counter++;
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;  // Couldn't acquire lock
    }
    
    // ============================================
    // TRY LOCK WITH TIMEOUT
    // ============================================
    public boolean tryIncrementWithTimeout() throws InterruptedException {
        if (lock.tryLock(5, TimeUnit.SECONDS)) {  // Wait up to 5 seconds
            try {
                counter++;
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }
    
    // ============================================
    // FAIR vs UNFAIR LOCKS
    // ============================================
    public void fairVsUnfair() {
        // UNFAIR (default) - Better performance, no guarantee of order
        ReentrantLock unfairLock = new ReentrantLock();
        
        // FAIR - Threads get lock in order they requested
        ReentrantLock fairLock = new ReentrantLock(true);
    }
    
    // ============================================
    // LOCK STATISTICS
    // ============================================
    public void lockStats() {
        System.out.println("Is held by current thread: " + lock.isHeldByCurrentThread());
        System.out.println("Queue length: " + lock.getQueueLength());
        System.out.println("Is locked: " + lock.isLocked());
        System.out.println("Hold count: " + lock.getHoldCount());  // Reentrant count
    }
}
```

### ReentrantLock vs synchronized

| Feature | ReentrantLock | synchronized |
|---------|---------------|--------------|
| Basic lock | Yes | Yes |
| Try lock (non-blocking) | tryLock() | No |
| Try lock with timeout | tryLock(time) | No |
| Fair lock option | Yes | No |
| Multiple conditions | newCondition() | No |
| Lock interruptibly | lockInterruptibly() | No |
| Performance (pre-Java 6) | Better | Worse |
| Automatic release | Yes (finally) | Yes (block exit) |

---

### 5. ReentrantReadWriteLock

```java
import java.util.concurrent.locks.*;

public class ReadWriteLockDemo {
    
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
    
    private int value = 0;
    
    // ============================================
    // MULTIPLE READERS CAN ACCESS SIMULTANEOUSLY
    // ============================================
    public int read() {
        readLock.lock();
        try {
            System.out.println("Reading value: " + value + " by " + Thread.currentThread().getName());
            return value;
        } finally {
            readLock.unlock();
        }
    }
    
    // ============================================
    // WRITER GETS EXCLUSIVE ACCESS
    // ============================================
    public void write(int newValue) {
        writeLock.lock();
        try {
            System.out.println("Writing value: " + newValue + " by " + Thread.currentThread().getName());
            value = newValue;
        } finally {
            writeLock.unlock();
        }
    }
    
    // ============================================
    // WRITE-THEN-READ (upgrade scenario)
    // ============================================
    public void writeAndRead(int newValue) {
        writeLock.lock();
        try {
            value = newValue;
            // Can't upgrade to read lock - would cause deadlock!
            // readLock.lock();  // DEADLOCK!
        } finally {
            writeLock.unlock();
        }
        
        // Read after write (different transaction)
        readLock.lock();
        try {
            System.out.println("Read after write: " + value);
        } finally {
            readLock.unlock();
        }
    }
    
    public static void main(String[] args) {
        ReadWriteLockDemo demo = new ReadWriteLockDemo();
        
        // Multiple readers can run in parallel
        for (int i = 0; i < 3; i++) {
            new Thread(() -> demo.read(), "Reader-" + i).start();
        }
        
        // Writers block everything
        new Thread(() -> demo.write(42), "Writer-1").start();
    }
}
```

**ReadWriteLock Rules:**
```
┌────────────────────────────────────────────────────────┐
│ READ LOCK                                              │
├────────────────────────────────────────────────────────┤
│ Multiple readers can hold simultaneously               │
│ Writer must wait for ALL readers                      │
│ Reader blocks writer                                  │
└────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────┐
│ WRITE LOCK                                             │
├────────────────────────────────────────────────────────┤
│ Exclusive - only one writer                           │
│ Writer blocks all readers and other writers           │
│ Must wait for all existing readers to finish          │
└────────────────────────────────────────────────────────┘
```

---

### 6. Condition Objects

```java
import java.util.concurrent.locks.*;

public class ConditionDemo {
    private final ReentrantLock lock = new ReentrantLock();
    
    // Multiple conditions for different wait scenarios
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    
    private final int[] buffer = new int[10];
    private int count = 0;
    private int head = 0;
    private int tail = 0;
    
    // ============================================
    // PRODUCER: waits if buffer is full
    // ============================================
    public void produce(int value) throws InterruptedException {
        lock.lock();
        try {
            while (count == buffer.length) {
                System.out.println("Buffer full, producer waiting...");
                notFull.await();  // Wait for space
            }
            
            buffer[tail] = value;
            tail = (tail + 1) % buffer.length;
            count++;
            
            System.out.println("Produced: " + value + ", count: " + count);
            notEmpty.signal();  // Signal consumer
        } finally {
            lock.unlock();
        }
    }
    
    // ============================================
    // CONSUMER: waits if buffer is empty
    // ============================================
    public int consume() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                System.out.println("Buffer empty, consumer waiting...");
                notEmpty.await();  // Wait for data
            }
            
            int value = buffer[head];
            head = (head + 1) % buffer.length;
            count--;
            
            System.out.println("Consumed: " + value + ", count: " + count);
            notFull.signal();  // Signal producer
            return value;
        } finally {
            lock.unlock();
        }
    }
    
    public static void main(String[] args) {
        ConditionDemo demo = new ConditionDemo();
        
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try { demo.produce(i); } catch (InterruptedException e) {}
            }
        }, "Producer");
        
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try { demo.consume(); } catch (InterruptedException e) {}
            }
        }, "Consumer");
        
        producer.start();
        consumer.start();
    }
}
```

**Condition vs Object.wait/notify:**

| Feature | Condition | Object.wait/notify |
|---------|-----------|-------------------|
| Create | newCondition() | (built-in) |
| Multiple conditions | Yes | No (only one) |
| await() | Yes | wait() |
| signal() | Yes | notify() |
| signalAll() | Yes | notifyAll() |

---

### 7. Atomic Variables

```java
import java.util.concurrent.atomic.*;

public class AtomicDemo {
    
    // ============================================
    // ATOMIC INTEGER - atomic operations
    // ============================================
    private AtomicInteger atomicInt = new AtomicInteger(0);
    
    public void atomicOperations() {
        atomicInt.incrementAndGet();      // ++i
        atomicInt.decrementAndGet();       // --i
        atomicInt.addAndGet(5);           // i += 5
        atomicInt.getAndIncrement();       // i++
        atomicInt.compareAndSet(5, 10);   // if (i == 5) i = 10; return true;
        atomicInt.updateAndGet(x -> x * 2);  // lambda update
        atomicInt.getAndAccumulate(3, (x, y) -> x + y);  // accumulate
    }
    
    // ============================================
    // ATOMIC REFERENCE - thread-safe object reference
    // ============================================
    private AtomicReference<User> userRef = new AtomicReference<>();
    
    public void atomicReferenceOps() {
        userRef.set(new User("Alice"));
        userRef.getAndSet(new User("Bob"));
        userRef.compareAndSet(new User("Alice"), new User("Charlie"));
        userRef.updateAndGet(u -> new User(u.getName().toUpperCase()));
    }
    
    // ============================================
    // ATOMIC LONG, BOOLEAN, INTEGER ARRAY
    // ============================================
    private AtomicLong atomicLong = new AtomicLong();
    private AtomicBoolean atomicBoolean = new AtomicBoolean(true);
    private AtomicIntegerArray intArray = new AtomicIntegerArray(10);
    
    // ============================================
    // ATOMIC FIELD UPDATERS - for existing classes
    // ============================================
    private volatile long fieldToUpdate = 0;
    private final AtomicLongFieldUpdater<AtomicDemo> updater = 
        AtomicLongFieldUpdater.newUpdater(AtomicDemo.class, "fieldToUpdate");
    
    public void fieldUpdaterOps() {
        updater.incrementAndGet(this);
        updater.compareAndSet(this, 5, 10);
    }
    
    // ============================================
    // PROBLEM: NOT ATOMIC for compound actions!
    // ============================================
    private AtomicInteger naiveCounter = new AtomicInteger(0);
    
    public void notAtomic() {
        // THIS IS NOT ATOMIC - race condition!
        int current = naiveCounter.get();
        naiveCounter.set(current + 1);  // read-modify-write is NOT atomic!
    }
    
    // SOLUTION: Use getAndUpdate or compareAndSet loop
    public void atomicIncrement() {
        naiveCounter.updateAndGet(curr -> curr + 1);  // ATOMIC
    }
    
    public static void main(String[] args) throws InterruptedException {
        AtomicDemo demo = new AtomicDemo();
        int numThreads = 1000;
        Thread[] threads = new Thread[numThreads];
        
        // Test AtomicInteger vs regular int
        AtomicInteger atomicCounter = new AtomicInteger(0);
        int regularCounter = 0;
        
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                atomicCounter.incrementAndGet();
                synchronized (AtomicDemo.class) {  // need sync for regular
                    regularCounter++;
                }
            });
            threads[i].start();
        }
        
        for (Thread t : threads) {
            t.join();
        }
        
        System.out.println("Atomic counter: " + atomicCounter.get());
        System.out.println("Synchronized counter: " + regularCounter);
    }
    
    static class User {
        private String name;
        User(String name) { this.name = name; }
        String getName() { return name; }
    }
}
```

**Atomic Classes Available:**
```
┌──────────────────────────┐
│ AtomicInteger            │
│ AtomicLong               │
│ AtomicBoolean            │
│ AtomicReference<V>       │
│ AtomicIntegerArray       │
│ AtomicLongArray          │
│ AtomicReferenceArray<V>  │
│ AtomicIntegerFieldUpdater│
│ AtomicLongFieldUpdater   │
│ AtomicMarkableReference  │
│ AtomicStampedReference   │
└──────────────────────────┘
```

---

### Complete Producer-Consumer with All Concepts

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class ProducerConsumerComplete {
    
    private final BlockingQueue<Integer> queue;
    private final AtomicInteger totalProduced = new AtomicInteger(0);
    private final AtomicInteger totalConsumed = new AtomicInteger(0);
    private volatile boolean running = true;
    
    public ProducerConsumerComplete(int capacity) {
        this.queue = new ArrayBlockingQueue<>(capacity);
    }
    
    public void produce(int value) throws InterruptedException {
        queue.put(value);  // Blocks if full
        totalProduced.incrementAndGet();
    }
    
    public int consume() throws InterruptedException {
        int value = queue.take();  // Blocks if empty
        totalConsumed.incrementAndGet();
        return value;
    }
    
    public void stop() {
        running = false;
    }
    
    public int getProduced() { return totalProduced.get(); }
    public int getConsumed() { return totalConsumed.get(); }
    
    public static void main(String[] args) throws InterruptedException {
        ProducerConsumerComplete pc = new ProducerConsumerComplete(5);
        
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    pc.produce(i);
                    System.out.println("Produced: " + i);
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");
        
        Thread consumer = new Thread(() -> {
            try {
                while (pc.running || pc.getProduced() > pc.getConsumed()) {
                    int value = pc.consume();
                    System.out.println("Consumed: " + value);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");
        
        producer.start();
        consumer.start();
        
        producer.join();
        pc.stop();
        consumer.join();
        
        System.out.println("Total produced: " + pc.getProduced());
        System.out.println("Total consumed: " + pc.getConsumed());
    }
}
```

---

### Interview Quick Reference

```
┌─────────────────────────────────────────────────────────────────┐
│ Q: synchronized vs Lock?                                         │
│ A: Lock: tryLock, timeout, multiple conditions, fairness       │
│    synchronized: simpler, auto-release, JVM handles             │
├─────────────────────────────────────────────────────────────────┤
│ Q: volatile vs synchronized?                                    │
│ A: volatile: visibility only; synchronized: visibility+atomicity│
├─────────────────────────────────────────────────────────────────┤
│ Q: Why always wait() in while loop?                             │
│ A: Spurious wakeups - thread can wake without notify           │
├─────────────────────────────────────────────────────────────────┤
│ Q: wait() vs sleep()?                                           │
│ A: wait() releases lock, sleep() doesn't                       │
├─────────────────────────────────────────────────────────────────┤
│ Q: Reentrant means?                                             │
│ A: Same thread can reacquire lock it already holds              │
├─────────────────────────────────────────────────────────────────┤
│ Q: AtomicInteger vs synchronized int?                           │
│ A: AtomicInteger: lock-free, better for simple ops             │
│    synchronized: needed for compound actions                    │
└─────────────────────────────────────────────────────────────────┘
```

---

Want me to continue with **Thread Communication** (Producer-Consumer, Deadlock, Livelock, Starvation)?

---

## User

yes

---

## Assistant (Build · big-pickle · 102.4s)

_Thinking:_

The user wants me to continue with Thread Communication topics including Producer-Consumer problem, Deadlock, Livelock, and Starvation.

## Thread Communication

### 1. Producer-Consumer Problem

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ProducerConsumerSolutions {
    
    // ============================================
    // SOLUTION 1: Using BlockingQueue (Recommended)
    // ============================================
    static class BQSolution {
        private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(5);
        private final AtomicInteger produced = new AtomicInteger(0);
        private final AtomicInteger consumed = new AtomicInteger(0);
        
        public void produce(int value) throws InterruptedException {
            queue.put(value);
            produced.incrementAndGet();
            System.out.println("Produced: " + value);
        }
        
        public int consume() throws InterruptedException {
            int value = queue.take();
            consumed.incrementAndGet();
            System.out.println("Consumed: " + value);
            return value;
        }
        
        public static void main(String[] args) throws InterruptedException {
            BQSolution solution = new BQSolution();
            
            Thread producer = new Thread(() -> {
                for (int i = 0; i < 20; i++) {
                    try {
                        solution.produce(i);
                        Thread.sleep(100);
                    } catch (InterruptedException e) { break; }
                }
            }, "Producer");
            
            Thread consumer = new Thread(() -> {
                while (solution.consumed.get() < 20) {
                    try {
                        solution.consume();
                        Thread.sleep(150);
                    } catch (InterruptedException e) { break; }
                }
            }, "Consumer");
            
            producer.start();
            consumer.start();
            producer.join();
            consumer.join();
            
            System.out.println("Total produced: " + solution.produced.get());
            System.out.println("Total consumed: " + solution.consumed.get());
        }
    }
    
    // ============================================
    // SOLUTION 2: Using wait/notify
    // ============================================
    static class WaitNotifySolution {
        private final int CAPACITY = 5;
        private final Queue<Integer> buffer = new ConcurrentLinkedQueue<>();
        
        public synchronized void produce(int value) throws InterruptedException {
            while (buffer.size() == CAPACITY) {
                System.out.println("Buffer full, producer waits...");
                wait();
            }
            
            buffer.offer(value);
            System.out.println("Produced: " + value + ", size: " + buffer.size());
            notifyAll();  // Notify all consumers
        }
        
        public synchronized int consume() throws InterruptedException {
            while (buffer.isEmpty()) {
                System.out.println("Buffer empty, consumer waits...");
                wait();
            }
            
            int value = buffer.poll();
            System.out.println("Consumed: " + value + ", size: " + buffer.size());
            notifyAll();  // Notify all producers
            return value;
        }
    }
    
    // ============================================
    // SOLUTION 3: Using Lock and Condition
    // ============================================
    static class LockConditionSolution {
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition notFull = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();
        private final Queue<Integer> buffer = new ConcurrentLinkedQueue<>();
        private final int CAPACITY = 5;
        
        public void produce(int value) throws InterruptedException {
            lock.lock();
            try {
                while (buffer.size() == CAPACITY) {
                    System.out.println("Buffer full, producer waits...");
                    notFull.await();
                }
                
                buffer.offer(value);
                System.out.println("Produced: " + value);
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }
        
        public int consume() throws InterruptedException {
            lock.lock();
            try {
                while (buffer.isEmpty()) {
                    System.out.println("Buffer empty, consumer waits...");
                    notEmpty.await();
                }
                
                int value = buffer.poll();
                System.out.println("Consumed: " + value);
                notFull.signal();
                return value;
            } finally {
                lock.unlock();
            }
        }
    }
    
    // ============================================
    // SOLUTION 4: Multiple Producers and Consumers
    // ============================================
    static class MultiProducerConsumer {
        private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(5);
        private final AtomicInteger totalProduced = new AtomicInteger(0);
        private final AtomicInteger totalConsumed = new AtomicInteger(0);
        
        public void produce(int value) throws InterruptedException {
            queue.put(value);
            totalProduced.incrementAndGet();
        }
        
        public int consume() throws InterruptedException {
            int value = queue.take();
            totalConsumed.incrementAndGet();
            return value;
        }
        
        public static void main(String[] args) throws InterruptedException {
            MultiProducerConsumer pc = new MultiProducerConsumer();
            int totalItems = 10;
            
            // Multiple producers
            Thread[] producers = new Thread[2];
            for (int p = 0; p < producers.length; p++) {
                final int producerId = p;
                producers[p] = new Thread(() -> {
                    for (int i = 0; i < totalItems / 2; i++) {
                        try {
                            pc.produce(producerId * 100 + i);
                            System.out.println("Producer " + producerId + " produced: " + (producerId * 100 + i));
                        } catch (InterruptedException e) { break; }
                    }
                }, "Producer-" + producerId);
            }
            
            // Multiple consumers
            Thread[] consumers = new Thread[2];
            for (int c = 0; c < consumers.length; c++) {
                final int consumerId = c;
                consumers[c] = new Thread(() -> {
                    int count = 0;
                    while (count < totalItems / 2) {
                        try {
                            int value = pc.consume();
                            System.out.println("Consumer " + consumerId + " consumed: " + value);
                            count++;
                        } catch (InterruptedException e) { break; }
                    }
                }, "Consumer-" + consumerId);
            }
            
            // Start all threads
            for (Thread p : producers) p.start();
            for (Thread c : consumers) c.start();
            
            // Wait for completion
            for (Thread p : producers) p.join();
            for (Thread c : consumers) c.join();
            
            System.out.println("Total produced: " + pc.totalProduced.get());
            System.out.println("Total consumed: " + pc.totalConsumed.get());
        }
    }
}
```

---

### 2. Deadlock

```java
public class DeadlockExamples {
    
    // ============================================
    // CLASSIC DEADLOCK: Two threads, two locks
    // ============================================
    static class ClassicDeadlock {
        private final Object lock1 = new Object();
        private final Object lock2 = new Object();
        
        public void methodA() {
            synchronized (lock1) {
                System.out.println("Thread " + Thread.currentThread().getName() + " holds lock1");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                
                System.out.println("Thread " + Thread.currentThread().getName() + " waiting for lock2...");
                synchronized (lock2) {
                    System.out.println("Thread " + Thread.currentThread().getName() + " acquired lock2");
                }
            }
        }
        
        public void methodB() {
            synchronized (lock2) {
                System.out.println("Thread " + Thread.currentThread().getName() + " holds lock2");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                
                System.out.println("Thread " + Thread.currentThread().getName() + " waiting for lock1...");
                synchronized (lock1) {
                    System.out.println("Thread " + Thread.currentThread().getName() + " acquired lock1");
                }
            }
        }
        
        public static void main(String[] args) {
            ClassicDeadlock deadlock = new ClassicDeadlock();
            
            Thread t1 = new Thread(() -> deadlock.methodA(), "Thread-1");
            Thread t2 = new Thread(() -> deadlock.methodB(), "Thread-2");
            
            t1.start();
            t2.start();
            
            // Wait a bit and check if threads are deadlocked
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            
            System.out.println("Thread-1 state: " + t1.getState());  // BLOCKED
            System.out.println("Thread-2 state: " + t2.getState());  // BLOCKED
            System.out.println("DEADLOCK DETECTED!");
        }
    }
    
    // ============================================
    // DEADLOCK SCENARIO: Transfer money
    // ============================================
    static class BankAccount {
        private int balance;
        private final String name;
        
        public BankAccount(String name, int balance) {
            this.name = name;
            this.balance = balance;
        }
        
        public synchronized void withdraw(int amount, String from) {
            System.out.println(from + " withdrawing from " + name);
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            balance -= amount;
            System.out.println(from + " withdrew " + amount + ", new balance: " + balance);
        }
        
        public synchronized void deposit(int amount, String from) {
            System.out.println(from + " depositing to " + name);
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            balance += amount;
            System.out.println(from + " deposited " + amount + ", new balance: " + balance);
        }
        
        public int getBalance() { return balance; }
    }
    
    static class DangerousTransfer {
        public static void transfer(BankAccount from, BankAccount to, int amount) {
            // DEADLOCK: Different threads call with different order
            System.out.println(Thread.currentThread().getName() + " acquiring lock on " + from.getClass().getSimpleName() + from);
            synchronized (from) {
                System.out.println(Thread.currentThread().getName() + " acquired lock on " + from.getClass().getSimpleName());
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                
                System.out.println(Thread.currentThread().getName() + " acquiring lock on " + to.getClass().getSimpleName());
                synchronized (to) {
                    System.out.println(Thread.currentThread().getName() + " acquired lock on " + to.getClass().getSimpleName());
                    from.withdraw(amount, Thread.currentThread().getName());
                    to.deposit(amount, Thread.currentThread().getName());
                }
            }
        }
        
        public static void main(String[] args) {
            BankAccount account1 = new BankAccount("Account1", 1000);
            BankAccount account2 = new BankAccount("Account2", 1000);
            
            // Thread 1: transfers from 1 to 2
            Thread t1 = new Thread(() -> transfer(account1, account2, 100), "T1");
            
            // Thread 2: transfers from 2 to 1 (reversed order!)
            Thread t2 = new Thread(() -> transfer(account2, account1, 100), "T2");
            
            t1.start();
            t2.start();
        }
    }
}
```

### Deadlock Prevention Techniques

```java
public class DeadlockPrevention {
    
    static class BankAccount {
        private int balance;
        private final String name;
        private final int id;  // Unique ID for ordering
        
        public BankAccount(String name, int balance, int id) {
            this.name = name;
            this.balance = balance;
            this.id = id;
        }
        
        public static void transfer(BankAccount from, BankAccount to, int amount) {
            // PREVENTION 1: Always acquire locks in consistent order
            // Based on unique ID
            BankAccount first = from.id < to.id ? from : to;
            BankAccount second = from.id < to.id ? to : from;
            
            synchronized (first) {
                synchronized (second) {
                    from.withdraw(amount);
                    to.deposit(amount);
                }
            }
        }
        
        // PREVENTION 2: Try lock with timeout
        static class TryLockTransfer {
            private final ReentrantLock lock1 = new ReentrantLock();
            private final ReentrantLock lock2 = new ReentrantLock();
            
            public void transfer() {
                while (true) {
                    // Try to acquire both locks
                    if (lock1.tryLock(1, TimeUnit.SECONDS)) {
                        if (lock2.tryLock(1, TimeUnit.SECONDS)) {
                            try {
                                // Do transfer
                                break;
                            } finally {
                                lock2.unlock();
                                lock1.unlock();
                            }
                        } else {
                            lock1.unlock();
                        }
                    }
                    // Wait and retry
                    try { Thread.sleep(10); } catch (InterruptedException e) {}
                }
            }
        }
        
        // PREVENTION 3: Single lock instead of multiple
        static class SingleLockTransfer {
            private final ReentrantLock globalLock = new ReentrantLock();
            
            public void transfer(BankAccount from, BankAccount to, int amount) {
                globalLock.lock();
                try {
                    from.withdraw(amount);
                    to.deposit(amount);
                } finally {
                    globalLock.unlock();
                }
            }
        }
        
        public synchronized void withdraw(int amount) {
            balance -= amount;
        }
        
        public synchronized void deposit(int amount) {
            balance += amount;
        }
        
        public int getBalance() { return balance; }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Deadlock Prevention Techniques ===");
        System.out.println("1. Lock ordering by unique ID");
        System.out.println("2. Try lock with timeout");
        System.out.println("3. Single global lock (simplest but less concurrent)");
    }
}
```

---

### 3. Livelock

```java
public class LivelockExamples {
    
    // ============================================
    // LIVELOCK: Threads keep changing state but
    // never make progress
    // ============================================
    static class PolitenessExample {
        private static final String[] NAMES = {"Alice", "Bob"};
        private volatile int turn = 0;
        
        public static void main(String[] args) {
            // Two people trying to pass each other in hallway
            // Both keep stepping aside - never passing!
            
            for (int i = 0; i < NAMES.length; i++) {
                final int personIndex = i;
                new Thread(() -> {
                    String myName = NAMES[personIndex];
                    String otherName = NAMES[1 - personIndex];
                    
                    for (int j = 0; j < 5; j++) {
                        System.out.println(myName + ": waiting for " + otherName + " to move...");
                        
                        // Keep yielding until it's our turn
                        while (turn != personIndex) {
                            Thread.yield();  // Give CPU to other thread
                        }
                        
                        System.out.println(myName + ": taking step");
                        turn = 1 - personIndex;  // Give turn to other
                        
                        try { Thread.sleep(10); } catch (InterruptedException e) {}
                    }
                }, myName = NAMES[i]).start();
            }
        }
    }
    
    // ============================================
    // REAL-WORLD LIVELOCK: Database retries
    // ============================================
    static class DatabaseLivelock {
        private static final int MAX_RETRIES = 5;
        
        public static void processWithRetry(String transactionId) {
            int retries = 0;
            
            while (retries < MAX_RETRIES) {
                System.out.println(transactionId + ": Attempt " + (retries + 1));
                
                boolean success = attemptTransaction(transactionId);
                
                if (success) {
                    System.out.println(transactionId + ": Success!");
                    return;
                }
                
                retries++;
                
                // Each transaction waits proportionally more
                // But they're all waiting the same amounts!
                try {
                    Thread.sleep(100);  // Same delay for all
                } catch (InterruptedException e) {}
            }
            
            System.out.println(transactionId + ": Failed after " + MAX_RETRIES + " attempts");
        }
        
        private static boolean attemptTransaction(String id) {
            // Simulate conflict - always conflicts
            return false;
        }
        
        public static void main(String[] args) {
            // Multiple transactions keep retrying
            // They all retry at the same time - livelock!
            for (int i = 0; i < 3; i++) {
                final int id = i;
                new Thread(() -> processWithRetry("TX-" + id), "Transaction-" + i).start();
            }
        }
    }
    
    // ============================================
    // LIVELOCK SOLUTION: Random backoff
    // ============================================
    static class FixedPoliteness {
        private static final String[] NAMES = {"Alice", "Bob"};
        private static final Random random = new Random();
        private volatile int turn = 0;
        
        public static void main(String[] args) {
            for (int i = 0; i < NAMES.length; i++) {
                final int personIndex = i;
                String myName = NAMES[personIndex];
                String otherName = NAMES[1 - personIndex];
                
                new Thread(() -> {
                    for (int j = 0; j < 5; j++) {
                        System.out.println(myName + ": waiting...");
                        
                        while (turn != personIndex) {
                            try {
                                // FIX: Random backoff prevents livelock
                                Thread.sleep(random.nextInt(100));
                            } catch (InterruptedException e) {}
                            Thread.yield();
                        }
                        
                        System.out.println(myName + ": moving");
                        turn = 1 - personIndex;
                    }
                }, myName).start();
            }
        }
    }
}
```

### Livelock vs Deadlock

```
┌─────────────────────────────────────────────────────────────────┐
│                          DEADLOCK                               │
├─────────────────────────────────────────────────────────────────┤
│ Threads are BLOCKED, not doing anything                         │
│ A ──holds L1──► waiting for L2 ──► BLOCKED                      │
│ B ──holds L2──► waiting for L1 ──► BLOCKED                      │
│                                                                 │
│ Solution: Lock ordering, try-lock, single lock                  │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                          LIVELOCK                               │
├─────────────────────────────────────────────────────────────────┤
│ Threads are ACTIVE but making no progress                       │
│ A ──yields──► B yields ──► A yields ──► B yields ──► ...        │
│                                                                 │
│ Solution: Random backoff, stop yielding occasionally           │
└─────────────────────────────────────────────────────────────────┘
```

---

### 4. Starvation

```java
public class StarvationExamples {
    
    // ============================================
    // STARVATION: Low priority threads never get CPU
    // ============================================
    static class PriorityStarvation {
        public static void main(String[] args) {
            Thread low = new Thread(() -> {
                int count = 0;
                while (true) {
                    count++;
                    if (count % 1000000 == 0) {
                        System.out.println("Low priority: " + count);
                    }
                }
            }, "Low-Priority");
            low.setPriority(Thread.MIN_PRIORITY);
            
            Thread med = new Thread(() -> {
                int count = 0;
                while (true) {
                    count++;
                    if (count % 1000000 == 0) {
                        System.out.println("Med priority: " + count);
                    }
                }
            }, "Med-Priority");
            med.setPriority(Thread.NORM_PRIORITY);
            
            Thread high = new Thread(() -> {
                int count = 0;
                while (true) {
                    count++;
                    if (count % 1000000 == 0) {
                        System.out.println("High priority: " + count);
                    }
                }
            }, "High-Priority");
            high.setPriority(Thread.MAX_PRIORITY);
            
            low.start();
            med.start();
            high.start();
        }
    }
    
    // ============================================
    // STARVATION: Long-running synchronized block
    // ============================================
    static class LongLockStarvation {
        private final Object lock = new Object();
        private volatile boolean running = true;
        
        public void shortTask() {
            synchronized (lock) {
                System.out.println("Short task: " + Thread.currentThread().getName());
            }
        }
        
        public void longTask() {
            synchronized (lock) {
                System.out.println("Long task starting: " + Thread.currentThread().getName());
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                System.out.println("Long task done: " + Thread.currentThread().getName());
            }
        }
        
        public void starver() {
            // This thread only gets lock when longTask is not running
            // But if longTask keeps running, starver starves
            while (running) {
                shortTask();
                try { Thread.sleep(50); } catch (InterruptedException e) {}
            }
        }
        
        public static void main(String[] args) throws InterruptedException {
            LongLockStarvation demo = new LongLockStarvation();
            
            // One thread always holds the lock for a long time
            Thread blocker = new Thread(() -> demo.longTask(), "Blocker");
            
            // Many threads trying short tasks - will starve
            Thread[] starvers = new Thread[5];
            for (int i = 0; i < starvers.length; i++) {
                starvers[i] = new Thread(() -> demo.starver(), "Starver-" + i);
            }
            
            blocker.start();
            Thread.sleep(100);  // Let blocker get lock
            
            for (Thread s : starvers) s.start();
            
            Thread.sleep(2000);
            demo.running = false;
        }
    }
    
    // ============================================
    // FAIR LOCK to prevent starvation
    // ============================================
    static class FairLockSolution {
        // Fair lock gives threads in queue order
        private final ReentrantLock fairLock = new ReentrantLock(true);  // true = fair
        
        public void doWork() {
            fairLock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " acquired lock");
                Thread.sleep(100);
            } catch (InterruptedException e) {
            } finally {
                fairLock.unlock();
            }
        }
        
        public static void main(String[] args) {
            FairLockSolution solution = new FairLockSolution();
            
            for (int i = 0; i < 5; i++) {
                final int id = i;
                new Thread(() -> solution.doWork(), "Worker-" + id).start();
            }
            
            // With fair lock, each thread gets lock in FIFO order
            // No starvation
        }
    }
}
```

---

### 5. Race Condition

```java
public class RaceConditionExamples {
    
    // ============================================
    // RACE CONDITION: Check-then-act
    // ============================================
    static class LazyInitialization implements Runnable {
        private static Object resource;
        
        // PROBLEMATIC: Race condition between check and creation
        public static Object getResourceUnsafe() {
            if (resource == null) {  // Thread A checks, sees null
                // Thread B also checks, sees null
                resource = new Object();  // Both create!
            }
            return resource;
        }
        
        // SAFE: Double-checked locking
        public static Object getResourceSafe() {
            if (resource == null) {  // First check (no lock)
                synchronized (LazyInitialization.class) {
                    if (resource == null) {  // Second check (with lock)
                        resource = new Object();
                    }
                }
            }
            return resource;
        }
        
        // EVEN SAFER: Eager initialization (no synchronization needed)
        private static final Object RESOURCE_EAGER = new Object();
        
        // Alternative: Use holder class (lazy but thread-safe)
        private static class ResourceHolder {
            static final Object RESOURCE = new Object();
        }
        
        public static Object getResourceHolder() {
            return ResourceHolder.RESOURCE;
        }
        
        public void run() {
            // Test unsafe
            Object obj1 = getResourceUnsafe();
            System.out.println(Thread.currentThread().getName() + ": " + obj1);
        }
        
        public static void main(String[] args) throws InterruptedException {
            LazyInitialization demo = new LazyInitialization();
            
            Thread[] threads = new Thread[10];
            for (int i = 0; i < 10; i++) {
                threads[i] = new Thread(demo);
            }
            
            for (Thread t : threads) t.start();
            for (Thread t : threads) t.join();
        }
    }
    
    // ============================================
    // RACE CONDITION: Read-modify-write
    // ============================================
    static class Counter implements Runnable {
        private int count = 0;
        private AtomicInteger atomicCount = new AtomicInteger(0);
        
        // RACE: read-modify-write is not atomic
        public void incrementUnsafe() {
            count++;  // Not atomic! 1. read count, 2. increment, 3. write
        }
        
        // SAFE: Use synchronized
        public synchronized void incrementSync() {
            count++;
        }
        
        // SAFE: Use atomic
        public void incrementAtomic() {
            atomicCount.incrementAndGet();
        }
        
        public int getCount() { return count; }
        public int getAtomicCount() { return atomicCount.get(); }
        
        public void run() {
            for (int i = 0; i < 1000; i++) {
                incrementUnsafe();
                incrementSync();
                incrementAtomic();
            }
        }
        
        public static void main(String[] args) throws InterruptedException {
            Counter counter = new Counter();
            Thread[] threads = new Thread[10];
            
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(counter);
            }
            
            for (Thread t : threads) t.start();
            for (Thread t : threads) t.join();
            
            System.out.println("Expected: 10000");
            System.out.println("Unsafe count: " + counter.getCount());
            System.out.println("Sync count: " + counter.getCount());  // Also uses sync
            System.out.println("Atomic count: " + counter.getAtomicCount());
        }
    }
    
    // ============================================
    // RACE CONDITION: Compound operations
    // ============================================
    static class CompoundOperations implements Runnable {
        private int value = 0;
        
        public void checkThenAct() {
            if (value < 100) {  // Check
                value = value + 1;  // Act - race condition here!
            }
        }
        
        public synchronized void safeCheckThenAct() {
            if (value < 100) {
                value = value + 1;
            }
        }
        
        public void run() {
            for (int i = 0; i < 50; i++) {
                safeCheckThenAct();
            }
        }
        
        public static void main(String[] args) throws InterruptedException {
            CompoundOperations ops = new CompoundOperations();
            Thread[] threads = new Thread[5];
            
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(ops);
            }
            
            for (Thread t : threads) t.start();
            for (Thread t : threads) t.join();
            
            System.out.println("Final value: " + ops.value);
            System.out.println("Expected: " + (5 * 50));
        }
    }
}
```

---

### Complete Interview Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                    PROBLEMS IN CONCURRENCY                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  DEADLOCK          │ Threads blocked forever                    │
│  ─────────         │ A holds L1, waits L2                      │
│                    │ B holds L2, waits L1                       │
│                    │ Prevention: lock ordering, try-lock        │
├────────────────────┼────────────────────────────────────────────┤
│  LIVELOCK          │ Threads active but no progress            │
│  ────────          │ Continuously responding to each other     │
│                    │ Prevention: random backoff                 │
├────────────────────┼────────────────────────────────────────────┤
│  STARVATION        │ Thread never gets CPU or resources         │
│  ─────────         │ Low priority thread ignored               │
│                    │ Prevention: fair locks, priorities        │
├────────────────────┼────────────────────────────────────────────┤
│  RACE CONDITION    │ Outcome depends on execution timing        │
│  ───────────       │ Check-then-act, read-modify-write         │
│                    │ Prevention: synchronization, atomics      │
└────────────────────┴────────────────────────────────────────────┘
```

```
┌─────────────────────────────────────────────────────────────────┐
│                    PRODUCER-CONSUMER SOLUTIONS                   │
├─────────────────────────────────────────────────────────────────┤
│  1. BlockingQueue        │ Simplest, built-in                   │
│  2. wait()/notify()      │ Classic, manual                       │
│  3. Lock/Condition       │ More flexible                        │
│  4. Semaphore            │ Count-based signaling                │
└─────────────────────────────────────────────────────────────────┘
```

---

Want me to continue with **Advanced Synchronization** (Semaphores, CountDownLatch, CyclicBarrier, Phaser)?

---

## User

yes

---

## Assistant (Build · big-pickle · 121.7s)

_Thinking:_

The user wants to learn about advanced synchronization primitives - Semaphores, CountDownLatch, CyclicBarrier, Phaser, and Exchanger. Let me provide comprehensive examples and explanations.

## Advanced Synchronization

### 1. Semaphore

```java
import java.util.concurrent.*;

public class SemaphoreDemo {
    
    // ============================================
    // CONCEPT: Controls access to resources
    // A semaphore maintains a count of permits
    // acquire() - blocks until permit available
    // release() - returns permit
    // ============================================
    
    static class Connection {
        private static int created = 0;
        private final int id;
        
        public Connection() {
            id = ++created;
            System.out.println("Connection " + id + " created");
        }
        
        @Override
        public String toString() { return "Connection-" + id; }
    }
    
    // ============================================
    // USE CASE 1: Limiting connections (pooling)
    // ============================================
    static class ConnectionPool {
        private final Semaphore semaphore;
        private final Connection[] connections;
        private final boolean[] used;
        
        public ConnectionPool(int poolSize) {
            semaphore = new Semaphore(poolSize, true);  // fair = true
            connections = new Connection[poolSize];
            used = new boolean[poolSize];
            
            for (int i = 0; i < poolSize; i++) {
                connections[i] = new Connection();
            }
        }
        
        public void connect(String threadName) throws InterruptedException {
            semaphore.acquire();  // Get permit, blocks if none available
            
            Connection conn = null;
            synchronized (this) {
                for (int i = 0; i < connections.length; i++) {
                    if (!used[i]) {
                        conn = connections[i];
                        used[i] = true;
                        break;
                    }
                }
            }
            
            System.out.println(threadName + " acquired: " + conn);
            Thread.sleep(1000);  // Simulate work
            
            synchronized (this) {
                for (int i = 0; i < used.length; i++) {
                    if (used[i] && connections[i] == conn) {
                        used[i] = false;
                        break;
                    }
                }
            }
            
            semaphore.release();  // Return permit
            System.out.println(threadName + " released: " + conn);
        }
        
        public static void main(String[] args) {
            ConnectionPool pool = new ConnectionPool(3);  // Only 3 connections
            
            ExecutorService executor = Executors.newFixedThreadPool(10);
            
            for (int i = 0; i < 10; i++) {
                final int id = i;
                executor.submit(() -> {
                    try {
                        pool.connect("Thread-" + id);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            
            executor.shutdown();
        }
    }
    
    // ============================================
    // USE CASE 2: Binary Semaphore (Mutex)
    // ============================================
    static class BinarySemaphoreMutex {
        private final Semaphore mutex = new Semaphore(1);  // Only 1 permit = mutex
        
        public void criticalSection(String name) throws InterruptedException {
            mutex.acquire();
            try {
                System.out.println(name + " entered critical section");
                Thread.sleep(500);
                System.out.println(name + " leaving critical section");
            } finally {
                mutex.release();
            }
        }
        
        public static void main(String[] args) {
            BinarySemaphoreMutex demo = new BinarySemaphoreMutex();
            
            for (int i = 0; i < 5; i++) {
                final int id = i;
                new Thread(() -> {
                    try { demo.criticalSection("Thread-" + id); } 
                    catch (InterruptedException e) {}
                }).start();
            }
        }
    }
    
    // ============================================
    // USE CASE 3: Read-Write with permits
    // Multiple readers OR one writer
    // ============================================
    static class ReadWriteSemaphore {
        private final Semaphore readLock = new Semaphore(3);  // 3 concurrent readers
        private final Semaphore writeLock = new Semaphore(1);  // 1 writer
        private final Semaphore mutex = new Semaphore(1);  // Protect readCount
        private int readCount = 0;
        private int value = 0;
        
        public void read(String name) throws InterruptedException {
            readLock.acquire();
            mutex.acquire();
            readCount++;
            if (readCount == 1) {
                writeLock.acquire();  // First reader blocks writers
            }
            mutex.release();
            readLock.release();
            
            // Reading
            System.out.println(name + " read value: " + value);
            Thread.sleep(100);
            
            mutex.acquire();
            readCount--;
            if (readCount == 0) {
                writeLock.release();  // Last reader unblocks writers
            }
            mutex.release();
        }
        
        public void write(String name, int newValue) throws InterruptedException {
            writeLock.acquire();
            value = newValue;
            System.out.println(name + " wrote value: " + value);
            Thread.sleep(100);
            writeLock.release();
        }
        
        public static void main(String[] args) {
            ReadWriteSemaphore rw = new ReadWriteSemaphore();
            
            // Multiple readers can read together
            // But writer gets exclusive access
        }
    }
    
    // ============================================
    // USE CASE 4: Rate limiting
    // ============================================
    static class RateLimiter {
        private final Semaphore semaphore;
        private final int maxPermits;
        private final long timeWindow;
        private long lastReset = System.currentTimeMillis();
        
        public RateLimiter(int permits, long timeWindowMs) {
            semaphore = new Semaphore(permits);
            maxPermits = permits;
            timeWindow = timeWindowMs;
        }
        
        public boolean tryAcquire() {
            resetIfNeeded();
            return semaphore.tryAcquire();
        }
        
        public void acquire() throws InterruptedException {
            resetIfNeeded();
            semaphore.acquire();
        }
        
        public void release() {
            semaphore.release();
        }
        
        private synchronized void resetIfNeeded() {
            if (System.currentTimeMillis() - lastReset > timeWindow) {
                while (semaphore.availablePermits() < maxPermits) {
                    semaphore.release();
                }
                lastReset = System.currentTimeMillis();
            }
        }
        
        public static void main(String[] args) throws InterruptedException {
            RateLimiter limiter = new RateLimiter(2, 1000);  // 2 per second
            
            for (int i = 0; i < 10; i++) {
                if (limiter.tryAcquire()) {
                    System.out.println("Request " + i + " allowed");
                } else {
                    System.out.println("Request " + i + " rejected (rate limited)");
                }
                Thread.sleep(100);
            }
        }
    }
}
```

**Semaphore Quick Reference:**
```
┌─────────────────────────────────────────────────────────────────┐
│ SEMAPHORE TYPES                                                 │
├─────────────────────────────────────────────────────────────────┤
│ Binary Semaphore (1 permit)  │ Same as Mutex                    │
│ Counting Semaphore (N permits)│ Control concurrent access      │
│ Fair Semaphore                │ FIFO queue for permits          │
├─────────────────────────────────────────────────────────────────┤
│ KEY METHODS                                                     │
├─────────────────────────────────────────────────────────────────┤
│ acquire()                │ Get permit (blocks if none)          │
│ acquire(n)               │ Get n permits                        │
│ tryAcquire()            │ Get if available (non-blocking)      │
│ tryAcquire(timeout)      │ Get within time                      │
│ release()               │ Return permit                        │
│ release(n)              │ Return n permits                     │
│ availablePermits()      │ Get current permits                  │
└─────────────────────────────────────────────────────────────────┘
```

---

### 2. CountDownLatch

```java
import java.util.concurrent.*;

public class CountDownLatchDemo {
    
    // ============================================
    // CONCEPT: One or more threads wait for
    // a set of operations to complete
    // Countdown only, cannot be reset
    // ============================================
    
    // ============================================
    // USE CASE 1: Wait for all workers to finish
    // ============================================
    static class WorkerSimulation {
        private static final int NUM_WORKERS = 5;
        
        public static void main(String[] args) throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(NUM_WORKERS);
            
            System.out.println("Starting workers...");
            
            for (int i = 0; i < NUM_WORKERS; i++) {
                final int workerId = i;
                new Thread(() -> {
                    try {
                        System.out.println("Worker-" + workerId + " starting");
                        Thread.sleep((long) (Math.random() * 1000));
                        System.out.println("Worker-" + workerId + " finished");
                        latch.countDown();  // Decrement counter
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
            
            // Wait for all workers
            System.out.println("Main thread waiting...");
            latch.await();  // Blocks until count reaches 0
            System.out.println("All workers finished! Main thread continuing.");
        }
    }
    
    // ============================================
    // USE CASE 2: Service startup initialization
    // ============================================
    static class ServiceStarter {
        private static class DatabaseService implements Runnable {
            private final String name;
            private final CountDownLatch latch;
            
            public DatabaseService(String name, CountDownLatch latch) {
                this.name = name;
                this.latch = latch;
            }
            
            public void run() {
                try {
                    System.out.println(name + " initializing...");
                    Thread.sleep(500);
                    System.out.println(name + " ready");
                } catch (InterruptedException e) {
                } finally {
                    latch.countDown();
                }
            }
        }
        
        public static void main(String[] args) throws InterruptedException {
            CountDownLatch startupLatch = new CountDownLatch(3);
            
            new Thread(new DatabaseService("Database", startupLatch)).start();
            new Thread(new DatabaseService("Cache", startupLatch)).start();
            new Thread(new DatabaseService("MessageQueue", startupLatch)).start();
            
            // Wait for all services to start
            startupLatch.await();
            System.out.println("All services ready! Application starting...");
        }
    }
    
    // ============================================
    // USE CASE 3: Barrier - all threads start together
    // ============================================
    static class RacingStart {
        public static void main(String[] args) throws InterruptedException {
            CountDownLatch startLatch = new CountDownLatch(1);  // 1 big countdown
            
            for (int i = 1; i <= 5; i++) {
                final int runner = i;
                new Thread(() -> {
                    try {
                        System.out.println("Runner-" + runner + " ready");
                        startLatch.await();  // All wait here
                        System.out.println("Runner-" + runner + " GO!");
                    } catch (InterruptedException e) {}
                }).start();
            }
            
            Thread.sleep(1000);  // Preparation time
            System.out.println("On your marks...");
            Thread.sleep(500);
            System.out.println("Set...");
            Thread.sleep(500);
            startLatch.countDown();  // START!
        }
    }
    
    // ============================================
    // USE CASE 4: Parallel computation
    // ============================================
    static class ParallelComputation {
        public static void main(String[] args) throws InterruptedException {
            int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            int numThreads = 4;
            int chunkSize = data.length / numThreads;
            
            CountDownLatch latch = new CountDownLatch(numThreads);
            long[] partialSums = new long[numThreads];
            
            for (int i = 0; i < numThreads; i++) {
                final int threadIndex = i;
                final int start = i * chunkSize;
                final int end = (i == numThreads - 1) ? data.length : start + chunkSize;
                
                new Thread(() -> {
                    long sum = 0;
                    for (int j = start; j < end; j++) {
                        sum += data[j];
                    }
                    partialSums[threadIndex] = sum;
                    System.out.println("Thread " + threadIndex + " sum: " + sum);
                    latch.countDown();
                }).start();
            }
            
            latch.await();
            
            long total = 0;
            for (long s : partialSums) total += s;
            System.out.println("Total sum: " + total);
        }
    }
}
```

**CountDownLatch vs CyclicBarrier:**

```
┌─────────────────────────────────────────────────────────────────┐
│                      COUNTDOWNLATCH                             │
├─────────────────────────────────────────────────────────────────┤
│ One-time use (cannot reset)                                     │
│ One thread waits, others countdown                              │
│ Use case: Wait for workers to complete                          │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                      CYCLICBARRIER                              │
├─────────────────────────────────────────────────────────────────┤
│ Reusable (auto-resets after countdown)                         │
│ All threads wait at barrier, then all continue together        │
│ Use case: Iterative parallel processing                         │
└─────────────────────────────────────────────────────────────────┘
```

---

### 3. CyclicBarrier

```java
import java.util.concurrent.*;

public class CyclicBarrierDemo {
    
    // ============================================
    // CONCEPT: Threads wait for each other at
    // a barrier point, then all proceed together
    // Can be reused (cyclic)
    // ============================================
    
    // ============================================
    // USE CASE 1: Simulating multi-player game
    // ============================================
    static class GameSimulation {
        public static void main(String[] args) {
            int numPlayers = 4;
            CyclicBarrier barrier = new CyclicBarrier(numPlayers, () -> {
                System.out.println("=== ALL PLAYERS READY! Starting round ===");
            });
            
            for (int i = 1; i <= numPlayers; i++) {
                final int playerId = i;
                new Thread(() -> {
                    try {
                        System.out.println("Player " + playerId + " loading...");
                        Thread.sleep((long) (Math.random() * 1000));
                        System.out.println("Player " + playerId + " ready, waiting...");
                        
                        barrier.await();  // Wait for others
                        
                        System.out.println("Player " + playerId + " playing!");
                        Thread.sleep(500);
                        
                    } catch (InterruptedException | BrokenBarrierException e) {
                        Thread.currentThread().interrupt();
                    }
                }, "Player-" + i).start();
            }
        }
    }
    
    // ============================================
    // USE CASE 2: Matrix multiplication
    // ============================================
    static class MatrixMultiplier {
        private static final int SIZE = 4;
        private static final int NUM_THREADS = 2;
        
        public static void main(String[] args) throws InterruptedException {
            int[][] A = {{1, 2}, {3, 4}, {5, 6}, {7, 8}};
            int[][] B = {{1, 0}, {0, 1}};
            int[][] C = new int[SIZE][SIZE];
            
            CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS, () -> {
                System.out.println("Round complete, starting next...");
            });
            
            int rowsPerThread = SIZE / NUM_THREADS;
            
            for (int t = 0; t < NUM_THREADS; t++) {
                final int threadId = t;
                final int startRow = t * rowsPerThread;
                final int endRow = startRow + rowsPerThread;
                
                new Thread(() -> {
                    try {
                        for (int round = 0; round < 2; round++) {  // 2 iterations
                            System.out.println("Thread " + threadId + " processing rows " + startRow + "-" + endRow);
                            
                            // Do computation
                            for (int i = startRow; i < endRow; i++) {
                                for (int j = 0; j < SIZE; j++) {
                                    C[i][j] = 0;
                                    for (int k = 0; k < SIZE; k++) {
                                        C[i][j] += A[i][k] * B[k][j];
                                    }
                                }
                            }
                            
                            barrier.await();  // Sync before next round
                        }
                    } catch (InterruptedException | BrokenBarrierException e) {
                    }
                }).start();
            }
        }
    }
    
    // ============================================
    // USE CASE 3: Iterative parallel search
    // ============================================
    static class IterativeSearch {
        public static void main(String[] args) throws InterruptedException {
            int[] data = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
            int target = 5;
            int numThreads = 3;
            
            CyclicBarrier barrier = new CyclicBarrier(numThreads);
            int[] found = {0};  // {index or -1}
            int[] iterations = {0};
            
            int chunkSize = (data.length + numThreads - 1) / numThreads;
            
            for (int t = 0; t < numThreads; t++) {
                final int threadId = t;
                final int start = t * chunkSize;
                final int end = Math.min(start + chunkSize, data.length);
                
                new Thread(() -> {
                    try {
                        for (int iter = 0; iter < 3; iter++) {  // Max iterations
                            if (found[0] >= 0) {
                                barrier.await();
                                continue;
                            }
                            
                            System.out.println("Search iteration " + iter + " by thread " + threadId);
                            
                            for (int i = start; i < end; i++) {
                                if (data[i] == target) {
                                    synchronized (found) {
                                        if (found[0] < 0) {
                                            found[0] = i;
                                        }
                                    }
                                }
                            }
                            
                            barrier.await();
                        }
                    } catch (InterruptedException | BrokenBarrierException e) {
                    }
                }).start();
            }
            
            Thread.sleep(1000);
            System.out.println("Target " + target + " found at index: " + (found[0] >= 0 ? found[0] : "Not found"));
        }
    }
    
    // ============================================
    // CYCLIC vs COUNTDOWNLATCH
    // ============================================
    static class Comparison {
        public static void main(String[] args) throws InterruptedException {
            
            // COUNTDOWNLATCH - One time event
            System.out.println("=== CountDownLatch (one-time) ===");
            CountDownLatch cdl = new CountDownLatch(3);
            
            for (int i = 0; i < 3; i++) {
                new Thread(() -> {
                    try { Thread.sleep(100); } catch (Exception e) {}
                    System.out.println("Counting down");
                    cdl.countDown();
                }).start();
            }
            
            cdl.await();
            System.out.println("CountDownLatch opened - can't reuse!");
            
            // CYCLICBARRIER - Reusable, multiple phases
            System.out.println("\n=== CyclicBarrier (reusable) ===");
            CyclicBarrier cb = new CyclicBarrier(3);
            
            for (int phase = 0; phase < 3; phase++) {
                System.out.println("Phase " + phase);
                for (int i = 0; i < 3; i++) {
                    final int threadId = i;
                    new Thread(() -> {
                        try {
                            Thread.sleep(100);
                            System.out.println("Thread " + threadId + " at barrier");
                            cb.await();
                            System.out.println("Thread " + threadId + " proceeding");
                        } catch (InterruptedException | BrokenBarrierException e) {
                        }
                    }).start();
                }
                Thread.sleep(500);
            }
        }
    }
}
```

---

### 4. Phaser

```java
import java.util.concurrent.*;

public class PhaserDemo {
    
    // ============================================
    // CONCEPT: Flexible synchronization barrier
    // - Like CyclicBarrier but with dynamic parties
    // - Supports multiple phases
    // - Threads can register/deregister dynamically
    // ============================================
    
    // ============================================
    // USE CASE 1: Dynamic worker coordination
    // ============================================
    static class DynamicWorkers {
        public static void main(String[] args) throws InterruptedException {
            Phaser phaser = new Phaser(3);  // 3 initial parties
            
            System.out.println("Starting with " + phaser.getRegisteredParties() + " workers");
            
            // Phase 1: All workers do task A
            for (int i = 0; i < 3; i++) {
                final int workerId = i;
                new Thread(() -> {
                    System.out.println("Worker " + workerId + " doing Phase 0 (initial)");
                    phaser.arriveAndAwaitAdvance();
                    
                    System.out.println("Worker " + workerId + " doing Phase 1 task");
                    phaser.arriveAndAwaitAdvance();
                    
                    System.out.println("Worker " + workerId + " doing Phase 2 task");
                    phaser.arriveAndAwaitAdvance();
                    
                    System.out.println("Worker " + workerId + " finished all phases");
                }).start();
            }
            
            phaser.awaitAdvance(0);
            System.out.println("All workers completed phase 0");
        }
    }
    
    // ============================================
    // USE CASE 2: Tree-structured computation
    // ============================================
    static class TreeComputation {
        private final Phaser phaser;
        private final int[] results;
        private final int index;
        
        public TreeComputation(Phaser phaser, int[] results, int index) {
            this.phaser = phaser;
            this.results = results;
            this.index = index;
            this.phaser.register();  // Register for this phaser
        }
        
        public void compute(int[] data, int start, int end) {
            try {
                if (end - start <= 2) {
                    // Leaf computation
                    int sum = 0;
                    for (int i = start; i < end; i++) sum += data[i];
                    results[index] = sum;
                    System.out.println("Leaf " + index + " computed: " + sum);
                } else {
                    // Split into children
                    int mid = (start + end) / 2;
                    TreeComputation left = new TreeComputation(phaser, results, index * 2);
                    TreeComputation right = new TreeComputation(phaser, results, index * 2 + 1);
                    
                    left.compute(data, start, mid);
                    right.compute(data, mid, end);
                    
                    phaser.arriveAndAwaitAdvance();
                    results[index] = results[index * 2] + results[index * 2 + 1];
                    System.out.println("Internal node " + index + ": " + results[index]);
                }
                
                phaser.arriveAndAwaitAdvance();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        public static void main(String[] args) throws InterruptedException {
            int[] data = {1, 2, 3, 4, 5, 6, 7, 8};
            int[] results = new int[16];  // Enough for tree
            
            Phaser phaser = new Phaser();
            TreeComputation root = new TreeComputation(phaser, results, 1);
            
            Thread computeThread = new Thread(() -> root.compute(data, 0, data.length));
            computeThread.start();
            computeThread.join();
            
            phaser.arriveAndDeregister();  // Deregister main
            System.out.println("Total sum: " + results[1]);
        }
    }
    
    // ============================================
    // USE CASE 3: Phases with early termination
    // ============================================
    static class EarlyTermination {
        public static void main(String[] args) throws InterruptedException {
            Phaser phaser = new Phaser(3);
            
            for (int i = 0; i < 3; i++) {
                final int workerId = i;
                new Thread(() -> {
                    try {
                        for (int phase = 0; phase < 5; phase++) {
                            System.out.println("Worker " + workerId + " in phase " + phase);
                            
                            if (phase == 2 && workerId == 0) {
                                System.out.println("Worker 0 terminating early");
                                phaser.arriveAndDeregister();
                                return;
                            }
                            
                            phaser.arriveAndAwaitAdvance();
                        }
                    } catch (InterruptedException e) {
                    }
                }).start();
            }
        }
    }
    
    // ============================================
    // USE CASE 4: Bulk data loading
    // ============================================
    static class BulkDataLoading {
        public static void main(String[] args) throws InterruptedException {
            Phaser phaser = new Phaser(3);
            
            String[][] dataSources = {
                {"DB1", "DB2"},
                {"API1", "API2"},
                {"File1", "File2"}
            };
            
            for (int group = 0; group < dataSources.length; group++) {
                System.out.println("\n=== Loading group " + group + " ===");
                
                for (String source : dataSources[group]) {
                    final String src = source;
                    new Thread(() -> {
                        try {
                            System.out.println("Loading from " + src);
                            Thread.sleep(200);
                            System.out.println(src + " loaded");
                            phaser.arriveAndAwaitAdvance();
                        } catch (InterruptedException e) {}
                    }).start();
                }
                
                Thread.sleep(100);  // Wait for group to register
            }
        }
    }
    
    // ============================================
    // COMPARISON: Phaser vs Others
    // ============================================
    static class Comparison {
        public static void main(String[] args) {
            System.out.println("┌─────────────────────────────────────────────┐");
            System.out.println("│             SYNCHRONIZER COMPARISON         │");
            System.out.println("├─────────────────────────────────────────────┤");
            System.out.println("│ CountDownLatch                              │");
            System.out.println("│   - One-time use                            │");
            System.out.println("│   - Fixed count                             │");
            System.out.println("│   - One thread waits for others             │");
            System.out.println("├─────────────────────────────────────────────┤");
            System.out.println("│ CyclicBarrier                               │");
            System.out.println("│   - Reusable                               │");
            System.out.println("│   - Fixed parties                          │");
            System.out.println("│   - All wait, all proceed                  │");
            System.out.println("├─────────────────────────────────────────────┤");
            System.out.println("│ Phaser                                      │");
            System.out.println("│   - Reusable                               │");
            System.out.println("│   - Dynamic parties                        │");
            System.out.println("│   - Multiple phases                        │");
            System.out.println("│   - Can terminate early                    │");
            System.out.println("└─────────────────────────────────────────────┘");
        }
    }
}
```

**Phaser Methods:**
```
┌─────────────────────────────────────────────────────────────────┐
│ PHASER METHODS                                                  │
├─────────────────────────────────────────────────────────────────┤
│ arriveAndAwaitAdvance()     │ Wait for all parties, advance    │
│ arriveAndDeregister()       │ Leave and reduce party count     │
│ register()                  │ Add new party dynamically        │
│ bulkRegister(n)              │ Add n parties                    │
│ arrive()                    │ Signal arrival (don't wait)      │
│ awaitAdvance(phase)         │ Wait for specific phase          │
│ getPhase()                  │ Get current phase number         │
│ getRegisteredParties()      │ Get current party count          │
│ isTerminated()              │ Check if terminated              │
└─────────────────────────────────────────────────────────────────┘
```

---

### 5. Exchanger

```java
import java.util.concurrent.*;

public class ExchangerDemo {
    
    // ============================================
    // CONCEPT: Two threads exchange data
    // Thread A gives data, waits for Thread B's data
    // ============================================
    
    // ============================================
    // USE CASE 1: Data pipeline buffering
    // ============================================
    static class DataPipeline {
        private final Exchanger<int[]> exchanger = new Exchanger<>();
        private final int[] buffer1 = new int[5];
        private final int[] buffer2 = new int[5];
        
        public void producer() throws InterruptedException {
            for (int batch = 0; batch < 3; batch++) {
                // Fill buffer
                for (int i = 0; i < buffer1.length; i++) {
                    buffer1[i] = batch * 10 + i;
                }
                System.out.println("Producer filled buffer, exchanging...");
                
                // Exchange empty buffer for full one
                int[] exchanged = exchanger.exchange(buffer1);
                
                System.out.println("Producer received buffer with: " + java.util.Arrays.toString(exchanged));
            }
        }
        
        public void consumer() throws InterruptedException {
            for (int batch = 0; batch < 3; batch++) {
                System.out.println("Consumer waiting for data...");
                
                // Exchange full buffer for empty one
                int[] exchanged = exchanger.exchange(buffer2);
                
                System.out.println("Consumer received: " + java.util.Arrays.toString(exchanged));
                
                // Process data
                for (int val : exchanged) {
                    System.out.println("Processing: " + val);
                }
            }
        }
        
        public static void main(String[] args) {
            DataPipeline pipeline = new DataPipeline();
            
            Thread producer = new Thread(() -> {
                try { pipeline.producer(); } catch (InterruptedException e) {}
            }, "Producer");
            
            Thread consumer = new Thread(() -> {
                try { pipeline.consumer(); } catch (InterruptedException e) {}
            }, "Consumer");
            
            producer.start();
            consumer.start();
        }
    }
    
    // ============================================
    // USE CASE 2: Trading simulation
    // ============================================
    static class TradingExchange {
        static class Order {
            String trader;
            String symbol;
            int quantity;
            double price;
            
            Order(String trader, String symbol, int qty, double price) {
                this.trader = trader;
                this.symbol = symbol;
                this.quantity = qty;
                this.price = price;
            }
            
            @Override
            public String toString() {
                return trader + ": " + quantity + " " + symbol + " @ " + price;
            }
        }
        
        public static void main(String[] args) throws InterruptedException {
            Exchanger<Order> exchange = new Exchanger<>();
            
            Thread buyer = new Thread(() -> {
                try {
                    Order sellOrder = new Order("Seller", "AAPL", 100, 150.0);
                    System.out.println("Buyer offering: " + sellOrder);
                    
                    Order buyOrder = exchange.exchange(sellOrder);
                    System.out.println("Buyer received: " + buyOrder);
                } catch (InterruptedException e) {}
            }, "Buyer");
            
            Thread seller = new Thread(() -> {
                try {
                    Order buyOrder = new Order("Buyer", "AAPL", 100, 151.0);
                    System.out.println("Seller offering: " + buyOrder);
                    
                    Order sellOrder = exchange.exchange(buyOrder);
                    System.out.println("Seller received: " + sellOrder);
                } catch (InterruptedException e) {}
            }, "Seller");
            
            buyer.start();
            seller.start();
        }
    }
    
    // ============================================
    // USE CASE 3: Game state synchronization
    // ============================================
    static class GameSync {
        private static final Exchanger<String> sync = new Exchanger<>();
        
        public static void main(String[] args) throws InterruptedException {
            Thread server = new Thread(() -> {
                try {
                    String clientState = "Initial";
                    for (int turn = 0; turn < 3; turn++) {
                        System.out.println("Server sending state: " + clientState);
                        String received = sync.exchange("Server-State-" + turn);
                        System.out.println("Server received: " + received);
                    }
                } catch (InterruptedException e) {}
            }, "Server");
            
            Thread client = new Thread(() -> {
                try {
                    for (int turn = 0; turn < 3; turn++) {
                        String received = sync.exchange("Client-Action-" + turn);
                        System.out.println("Client received: " + received);
                    }
                } catch (InterruptedException e) {}
            }, "Client");
            
            server.start();
            client.start();
        }
    }
}
```

**Exchanger Characteristics:**
```
┌─────────────────────────────────────────────────────────────────┐
│ EXCHANGER                                                        │
├─────────────────────────────────────────────────────────────────┤
│ - Exactly 2 threads synchronize                                  │
│ - Both must be ready to exchange                                │
│ - Threads swap objects                                          │
│ - Use for producer-consumer with 1:1 ratio                     │
│ - Can timeout with exchange(V, timeout)                        │
└─────────────────────────────────────────────────────────────────┘
```

---

### Complete Summary Table

```
┌────────────────┬──────────────┬───────────┬─────────────────────────┐
│ Component      │ Reusable?    │ Dynamic?  │ Use Case                 │
├────────────────┼──────────────┼───────────┼─────────────────────────┤
│ synchronized   │ Yes         │ No       │ Basic mutual exclusion   │
│ Lock           │ Yes         │ No       │ Flexible locking         │
│ Semaphore      │ Yes         │ Yes      │ Resource pooling         │
│ CountDownLatch │ No          │ Yes      │ One-time wait            │
│ CyclicBarrier  │ Yes         │ Yes      │ Repeated sync points     │
│ Phaser         │ Yes         │ Yes      │ Multi-phase, dynamic     │
│ Exchanger      │ Yes         │ No       │ Two-thread data swap     │
└────────────────┴──────────────┴───────────┴─────────────────────────┘
```

---

### Interview Quick Reference

```
┌─────────────────────────────────────────────────────────────────┐
│ Q: When to use Semaphore vs Lock?                               │
│ A: Semaphore when multiple permits; Lock for single mutex      │
├─────────────────────────────────────────────────────────────────┤
│ Q: Difference between CyclicBarrier and CountDownLatch?         │
│ A: Barrier resets and reuses; Latch is one-time                 │
├─────────────────────────────────────────────────────────────────┤
│ Q: What makes Phaser more flexible?                              │
│ A: Dynamic parties, multiple phases, early termination          │
├─────────────────────────────────────────────────────────────────┤
│ Q: When would you use Exchanger?                                 │
│ A: Two threads need to swap data (e.g., buffer handover)       │
├─────────────────────────────────────────────────────────────────┤
│ Q: Can Semaphore be used as mutex?                              │
│ A: Yes, use Semaphore(1) - binary semaphore                    │
├─────────────────────────────────────────────────────────────────┤
│ Q: What happens if await() times out?                           │
│ A: Returns phase number or -1, or BrokenBarrierException       │
└─────────────────────────────────────────────────────────────────┘
```

---

Want me to continue with **Executor Framework** (ExecutorService, ThreadPoolExecutor, Fork/Join, CompletableFuture)?

---

## User

yes can you give some theory question

---

## Assistant (Build · big-pickle · 195.6s)

_Thinking:_

The user wants me to continue with Executor Framework AND wants some theory questions for interview preparation. Let me provide both - theory questions interspersed with the code examples for the Executor Framework topic.

## Executor Framework

### Theory Questions

---

**Q1: Why use Executor Framework instead of creating threads directly?**

```
DIRECT THREAD CREATION:
- Each task = new Thread()
- Problems:
  - Thread creation/destruction overhead
  - No resource management
  - No scheduling control
  - Difficult to manage large number of threads

EXECUTOR FRAMEWORK:
- Decouples task submission from thread management
- Thread reuse (pool)
- Better resource management
- Built-in scheduling, termination, monitoring
```

---

**Q2: ThreadPoolExecutor core parameters (keep-alive, queue selection)**

```
┌─────────────────────────────────────────────────────────────────┐
│ ThreadPoolExecutor(int corePoolSize,                            │
│                    int maximumPoolSize,                          │
│                    long keepAliveTime,                           │
│                    TimeUnit unit,                                │
│                    BlockingQueue<Runnable> workQueue,            │
│                    ThreadFactory threadFactory,                  │
│                    RejectedExecutionHandler handler)              │
└─────────────────────────────────────────────────────────────────┘

corePoolSize:
  - Core threads always kept alive
  - Tasks wait in queue before core threads start

maximumPoolSize:
  - Max threads when queue is full
  - Helps handle burst traffic

keepAliveTime:
  - Idle thread timeout
  - Applies to threads beyond corePoolSize

Queue selection strategy:
  - LinkedBlockingQueue: unbounded (can grow indefinitely)
  - ArrayBlockingQueue: bounded (controls memory)
  - SynchronousQueue: direct handoff (no storage)
  - PriorityBlockingQueue: for priority tasks
```

---

**Q3: How does ThreadPoolExecutor sizing work?**

```
FORMULA: Nthreads = Ncpu * Ucpu * (1 + W/C)

Where:
  Ncpu = Runtime.getRuntime().availableProcessors()
  Ucpu = target CPU utilization (0 to 1)
  W/C = wait time / compute time ratio

CPU-INTENSIVE tasks:
  - Optimal: Ncpu + 1 (handles context switching)
  - Rule of thumb: Ncpu + 1

I/O-INTENSIVE tasks:
  - Optimal: Much higher than CPU cores
  - Rule of thumb: Ncpu * Ucpu * (1 + W/C)
  - Example: 4 cores, W/C = 100 → 401 threads
```

---

### Code Examples

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ExecutorFrameworkDemo {
    
    // ============================================
    // 1. ExecutorService Types
    // ============================================
    
    static class ExecutorTypes {
        
        public static void main(String[] args) {
            
            // Single Thread Executor - Sequential execution
            ExecutorService single = Executors.newSingleThreadExecutor();
            
            // Fixed Thread Pool - Fixed number of threads
            ExecutorService fixed = Executors.newFixedThreadPool(4);
            
            // Cached Thread Pool - Grows/shrinks dynamically
            ExecutorService cached = Executors.newCachedThreadPool();
            
            // Scheduled Executor - For scheduling tasks
            ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);


---

## User

dir>output.txt

---

## Assistant (Plan · big-pickle · 126.2s)

_Thinking:_

The user wants me to provide interview questions for the Executor Framework topic (ExecutorService, ThreadPoolExecutor, Fork/Join, CompletableFuture) and save them to a file. However, I'm in plan mode which means I can only read and analyze - I cannot write/edit files.

Let me provide the interview questions directly in my response for now. I can write to a file once the user confirms they want me to proceed with implementation (out of plan mode).

Actually, the user asked me to give interview questions and save them to output.txt. But I'm in plan mode, so I can only provide the questions in my response text. Let me provide comprehensive interview questions for Executor Framework.

Here's the complete Executor Framework theory questions:

---

## Executor Framework - Interview Questions

### **1. ExecutorService Basics**
```
Q: What is ExecutorService and why was it introduced?
A: ExecutorService is a framework for managing thread execution, introduced in Java 5
   as replacement for direct thread creation. It provides thread pooling, lifecycle
   management, and separation of task submission from task execution.

Q: Difference between execute() and submit()?
A: execute() - void, no return, cannot check result, throws exception on failure
   submit()  - returns Future<?>, can get result, returns null on failure

Q: Difference between Runnable and Callable?
A: Runnable: run() returns void, cannot throw checked exceptions
   Callable: call() returns V, can throw checked exceptions

Q: What is Future? How do you use it?
A: Future represents async computation result.
   Methods: get() - blocks until result, get(timeout) - with timeout,
   isDone() - check completion, cancel() - cancel task
```

### **2. ThreadPoolExecutor**
```
Q: What are the thread pool types in Executors utility class?
A: newFixedThreadPool(n)     - Fixed size, unbounded queue
   newCachedThreadPool()     - Dynamic size, 60s timeout
   newSingleThreadExecutor() - Single thread, unbounded queue
   newScheduledThreadPool(n) - For scheduled tasks

Q: What happens when task submission exceeds queue capacity?
A: newFixedThreadPool: Tasks wait in unbounded queue (OOM risk)
   newCachedThreadPool: Creates new threads (can exhaust resources)
   Solution: Use ThreadPoolExecutor with bounded queue + RejectedExecutionHandler

Q: How to size a thread pool correctly?
A: For CPU-bound: N_threads = N_cpu * U * (1 + W/C)
   Where U = target CPU utilization, W = wait time, C = compute time
   For I/O-bound: Can use many more threads (threads wait for I/O)

Q: What is RejectedExecutionHandler?
A: Handler for tasks rejected when Executor is shut down or saturated.
   Options: CallerRunsPolicy, AbortPolicy (default throws), 
            DiscardPolicy, DiscardOldestPolicy
```

### **3. Fork/Join Framework**
```
Q: What is Fork/Join and when to use it?
A: Framework for divide-and-conquer parallelism.
   Work-stealing algorithm: idle threads steal work from busy threads.
   Use for: RecursiveTask<V>, RecursiveAction (void)

Q: Difference between ForkJoinPool and regular ThreadPoolExecutor?
A: ForkJoinPool: Work-stealing, designed for recursive tasks, lighter weight
   ThreadPoolExecutor: Fixed/thread-per-task, better for independent tasks

Q: What is work-stealing algorithm?
A: Each worker has its own deque. When idle, steals from other workers' deque.
   Better load balancing, reduces contention. Used by ForkJoinPool.

Q: invokeAll() vs fork() + join()?
A: invokeAll(): Forks all subtasks, then joins all (simpler)
   fork(): Submit to pool, then join (more control)
   invokeAll() is generally preferred for clarity
```

### **4. CompletableFuture**
```
Q: What is CompletableFuture and when to use it?
A: Represents future computation that can be explicitly completed.
   Use when: Chaining async operations, combining multiple futures,
   handling exceptions, non-blocking programming

Q: How to create CompletableFuture?
A: supplyAsync(Supplier)     - with return value
   runAsync(Runnable)        - without return value
   completedFuture(value)    - already completed
   future.thenApply(fn)      - transform result
   future.thenCompose(fn)    - flatMap (return another CF)
   future.thenCombine(cf, fn)- combine two CFs

Q: Difference between thenApply and thenCompose?
A: thenApply: Transforms result (T -> U)
   thenCompose: Chains futures (T -> CompletableFuture<U>), flatMap

Q: How to handle exceptions in CompletableFuture?
A: exceptionally(ex)    - handle exception
   handle(biHandler)    - always called with result or exception
   whenComplete(action) - called regardless of outcome

Q: How to run multiple CompletableFutures and wait for all?
A: CompletableFuture.allOf(cf1, cf2, cf3...) - waits for all
   CompletableFuture.anyOf(cf1, cf2...)      - waits for first
```

### **5. ThreadPool Tuning**
```
Q: Core pool size vs maximum pool size?
A: Core: minimum threads kept alive (even if idle)
   Max:  maximum threads created under load
   allowCoreThreadTimeOut: can idle core threads to 0

Q: What is keep-alive time?
A: Time idle threads wait before terminating (beyond core pool)

Q: Queue types and their trade-offs?
A: LinkedBlockingQueue: Optional capacity, FIFO
   ArrayBlockingQueue: Fixed capacity, FIFO
   SynchronousQueue: No storage, direct handoff
   PriorityBlockingQueue: Priority ordering

Q: How does rejection policy work with bounded queue?
A: When queue full and pool at max size:
   AbortPolicy: Reject with exception
   CallerRunsPolicy: Runs in caller's thread (throttling)
   DiscardPolicy: Silently discard
   DiscardOldestPolicy: Discard oldest unprocessed task
```

### **6. Best Practices & Common Mistakes**
```
Q: Common mistakes with ExecutorService?
A: 1. Not shutting down executor (resource leak)
   2. Forgetting to handle InterruptedException
   3. Using unbounded queues causing OOM
   4. Not handling Future.get() properly
   5. Blocking in tasks causing thread starvation

Q: Proper way to shut down ExecutorService?
A: shutdown()  - Stop accepting new, complete existing
   shutdownNow() - Attempt to stop all (interrupt)
   awaitTermination() - Wait for completion
   Best: shutdown() + awaitTermination() + shutdownNow() in finally

Q: Why avoid using Executors.newCachedThreadPool() in production?
A: Creates unlimited threads under load, can cause OOM.
   Better: ThreadPoolExecutor with bounded max pool size.
```

---

### **7. Advanced Scenarios**
```
Q: How to implement timeout for task execution?
A: future.get(timeout, TimeUnit) - throws TimeoutException
   or: CompletableFuture.orTimeout() (Java 9+)

Q: How to cancel a running task?
A: future.cancel(true) - may interrupt if running
   Task must check Thread.interrupted() and return early

Q: How to implement retry logic with ExecutorService?
A: Loop with counter, submit again on failure (up to max retries)

Q: Difference between parallel stream and ForkJoinPool?
A: Parallel stream uses common ForkJoinPool (can conflict with custom)
   ForkJoinPool: More control, configurable parallelism
   Parallel stream: Simpler, for simple operations
```

---

Would you like me to:
1. Save these questions to `output.txt`?
2. Add answers with code examples?
3. Continue with the next topic (High-Level Concurrency)?

---

## User

dir>>output.txt

---

## User

dir/xyz>output.txt 2>&1

---

## User

echo Running ipconfig >>log.txt

---

## Assistant (Plan · big-pickle · 19.4s)

_Thinking:_

The user wants me to save the interview questions to a file called `output.txt`. However, I'm in plan mode which is read-only - I cannot make any file edits or modifications.

Let me understand the situation:
1. The user asked for theory questions to be saved to `output.txt`
2. The system reminders indicate plan mode is active, which forbids any file edits
3. I can only read, search, and plan - no modifications allowed

I should inform the user that I'm currently in read-only plan mode and cannot write to files. I should ask if they want me to:
1. Continue providing the content that would go into the file
2. Or wait until plan mode is disabled so I can actually write the file

Let me respond appropriately.

I'm currently in **read-only plan mode**, so I cannot write to files yet. 

Would you like me to:
1. **Continue providing all the interview questions** for the remaining topics (Executor Framework + all subsequent topics)?
2. **Prepare the complete content first**, then save everything to `output.txt` when write mode is enabled?

Let me know your preference, and I'll continue building the comprehensive interview preparation content.

---

## User

ok look i want to top to bottom all and everything as it is in a file so that i can save it 

---

## Assistant (Plan · big-pickle · 15.0s)

_Thinking:_

The user wants comprehensive multithreading interview preparation material saved to a file. I understand now:

1. I'm in read-only/plan mode - I cannot write files yet
2. The user wants ALL multithreading topics covered thoroughly
3. They want it saved to `output.txt` when possible

I should confirm what they want and ask some questions to make sure I cover everything properly:

1. What language/technology? (I assumed Java, but they might want Python, C++, etc.)
2. How comprehensive? (Brief overview vs deep dive?)
3. Format preference? (Just theory, or theory + code examples?)
4. Any specific company/role they're targeting?

Let me ask these clarifying questions to make sure I deliver exactly what they need.

Understood! Since I'm in read-only mode, I'll prepare everything and save when ready.

Let me ask a few quick questions to ensure I give you exactly what you need:

1. **Language?** (Java / Python / C++ / Go / all?)

2. **Depth level?**
   - A) Core concepts + common questions (concise)
   - B) Deep dive with theory + code examples + real scenarios (comprehensive)

3. **Format?**
   - Theory questions with answers only
   - Theory + code examples
   - Include tricky follow-up questions

4. **Output structure?**
   - Single file (`output.txt`)
   - Multiple files (one per topic)

5. **Any target companies?** (FAANG, startup, specific domain like fintech, systems, etc.)

Once you confirm, I'll cover ALL topics from start to finish:
- Fundamentals
- Thread Basics
- Synchronization
- Thread Communication
- Advanced Synchronization
- Executor Framework
- High-Level Concurrency
- Memory & Performance
- Design Patterns
- Debugging & Best Practices

---

## User

1. java 2.deep dive 3.b&c 4.b 5.faang&maang

---

## Assistant (Plan · big-pickle · 86.3s)

_Thinking:_

The user wants:
1. Java
2. Deep dive with theory + code examples + real scenarios
3. Include tricky follow-up questions
4. Multiple files (one per topic)
5. FAANG/MAANG target

Since I'm in read-only mode, I need to create a comprehensive plan for all the topics and prepare the content. Let me structure this properly.

For FAANG/MAANG interviews, I need to cover:
- All Java concurrency primitives deeply
- Memory model and happens-before
- Lock-free programming
- Performance optimization
- Real-world patterns
- Tricky edge cases
- System design considerations

Let me outline the file structure:

1. **01_fundamentals.md** - Process vs Thread, Concurrency vs Parallelism, etc.
2. **02_thread_basics.md** - Creation, Lifecycle, Priorities, Daemon
3. **03_synchronization.md** - synchronized, volatile, locks, atomic
4. **04_thread_communication.md** - Producer-Consumer, Deadlock, Livelock, Starvation
5. **05_advanced_synchronization.md** - Semaphore, CountDownLatch, CyclicBarrier, Phaser, Exchanger
6. **06_executor_framework.md** - ExecutorService, ThreadPool, Fork/Join, CompletableFuture
7. **07_high_level_concurrency.md** - Concurrent collections, ThreadLocal
8. **08_memory_model.md** - JMM, Happens-Before, Memory barriers
9. **09_design_patterns.md** - Immutable, Singleton, Read-Write, etc.
10. **10_debugging_best_practices.md** - Thread dumps, common pitfalls

For each topic, I'll include:
- Theory explanation
- Code examples
- Tricky follow-up questions
- FAANG-style interview questions
- Real-world scenarios

Let me present this plan to the user.

Perfect! Let me outline the complete plan for your FAANG-level Java Multithreading preparation.

## 📚 Complete File Structure (10 Files)

```
java-multithreading-mastery/
├── 01_fundamentals.md              Process vs Thread, Concurrency vs Parallelism
├── 02_thread_basics.md            Creation, Lifecycle, Priorities, Daemon
├── 03_synchronization.md          synchronized, volatile, locks, atomic
├── 04_thread_communication.md     Producer-Consumer, Deadlock, Livelock, Starvation
├── 05_advanced_sync.md            Semaphore, CountDownLatch, CyclicBarrier, Phaser
├── 06_executor_framework.md       ExecutorService, ThreadPool, Fork/Join, CompletableFuture
├── 07_high_level_concurrency.md   ConcurrentHashMap, BlockingQueue, ThreadLocal
├── 08_memory_model.md             JMM, Happens-Before, Happens-before chains
├── 09_design_patterns.md          Immutable, Singleton, Read-Write, Actor
└── 10_debugging_pitfalls.md       Thread dumps, Deadlock detection, Best practices
```

## 📖 Each File Will Include:

| Section | Contents |
|---------|----------|
| **Theory** | In-depth concepts with visual diagrams |
| **Code Examples** | Production-ready snippets |
| **Tricky Questions** | Follow-ups that trip up candidates |
| **FAANG Questions** | Real interview questions with answers |
| **Common Pitfalls** | What interviewers watch for |
| **Real Scenarios** | System design considerations |

---

## ✅ Confirmation

Should I proceed with generating **ALL 10 files** with:

- **Java code examples** (fully compilable)
- **Deep theory** (not surface-level)
- **50+ FAANG interview questions** with detailed answers
- **Tricky follow-ups** and edge cases
- **Memory model internals** (crucial for FAANG)

**Note:** Once I switch to write mode, I'll create all files. Ready to proceed?

---

## User

yes

---

## Assistant (Plan · big-pickle · 17.6s)

_Thinking:_

The user confirmed "yes" to proceed, but I'm in read-only plan mode. I need to inform the user about this and explain that once they enable write mode, I can create all the files.

Let me provide a comprehensive summary of what I'll create and get final confirmation before the actual implementation.

Perfect! Since I'm currently in **read-only mode**, I'll prepare the complete content and create all files once write mode is enabled.

## 📋 Complete Content Summary

### **File 1: 01_fundamentals.md**
```
- Process vs Thread (deep internals)
- Concurrency vs Parallelism (with CPU diagrams)
- Synchronous vs Asynchronous
- Single vs Multi-threaded
- I/O-bound vs CPU-bound tasks
- + 10 FAANG interview questions
```

### **File 2: 02_thread_basics.md**
```
- 4 ways to create threads
- Thread lifecycle (6 states)
- Thread priorities (JVM internals)
- Daemon vs User threads
- Thread naming/identification
- ThreadGroup
- + 15 FAANG questions with follow-ups
```

### **File 3: 03_synchronization.md**
```
- synchronized (all variations)
- volatile (JMM deep dive)
- ReentrantLock (fair/unfair, tryLock)
- ReentrantReadWriteLock
- Condition objects
- Atomic classes (complete suite)
- Double-checked locking
- + 25 code examples + 20 FAANG questions
```

### **File 4: 04_thread_communication.md**
```
- Producer-Consumer (5 solutions)
- Deadlock (causes, detection, prevention)
- Livelock (real examples)
- Starvation (priority inversion)
- Race conditions (all types)
- + System design scenarios
```

### **File 5: 05_advanced_sync.md**
```
- Semaphore (resource pooling, rate limiting)
- CountDownLatch (all use cases)
- CyclicBarrier (reusable sync)
- Phaser (dynamic parties, multi-phase)
- Exchanger (data swap)
- + Comparison table
```

### **File 6: 06_executor_framework.md**
```
- ExecutorService deep dive
- ThreadPoolExecutor internals
- Fork/Join (work-stealing)
- CompletableFuture (complete guide)
- ThreadFactory
- ScheduledExecutorService
- + 30 FAANG questions
```

### **File 7: 07_high_level_concurrency.md**
```
- ConcurrentHashMap (segment locking, CAS)
- CopyOnWriteArrayList
- BlockingQueue (all implementations)
- ConcurrentLinkedQueue
- ThreadLocal & InheritableThreadLocal
- StampedLock
- + Real-world patterns
```

### **File 8: 08_memory_model.md**
```
- Java Memory Model (JMM)
- Happens-Before guarantees
- Memory barriers
- CPU caching (MESI)
- False sharing
- Cache padding
- CPU cores and threads
- + 15 deep questions
```

### **File 9: 09_design_patterns.md**
```
- Immutable objects
- Thread-safe Singleton
- Read-Write lock pattern
- Lock striping
- Thread confinement
- Actor model
- Producer-Consumer patterns
- + System design examples
```

### **File 10: 10_debugging_pitfalls.md**
```
- Thread dump analysis
- Deadlock detection (jstack, VisualVM)
- Performance tuning
- Common mistakes
- Best practices
- + Checklist for interviews
```

---

## 📊 Total Content

| Item | Count |
|------|-------|
| Code Examples | 150+ |
| Diagrams | 20+ |
| FAANG Questions | 100+ |
| Tricky Follow-ups | 50+ |
| Lines of Content | ~5000+ |

---

## ⏳ Next Step

Once you enable **write mode**, I'll:
1. Create a folder `java-multithreading-mastery/`
2. Generate all 10 files with complete content
3. Include production-ready code examples

**Ready to proceed when you enable write access!** 🚀

---

## User

ready

---

## Assistant (Plan · big-pickle · 12.7s)

_Thinking:_

The user said "ready" indicating they want me to proceed. But I'm in read-only plan mode according to the system reminder. I cannot make any file modifications or write any files.

I need to let the user know that I'm still in read-only mode and cannot create files yet. I should ask them to enable write mode so I can proceed with creating all the files.

I'm still in **read-only plan mode** and cannot create files yet.

To proceed with creating all 10 comprehensive files, I need **write access** enabled.

## Current Status

| Mode | Status |
|------|--------|
| Plan Mode | ✅ Active (Read-only) |
| Write Access | ❌ Disabled |
| File Creation | ❌ Not possible |

## To Enable Write Mode

Please:
1. Enable write/plan mode in your session settings, OR
2. Type `/plan` or `/write` to toggle, OR
3. Ask me to switch to execution mode

---

## What's Ready to Create

Once write access is enabled, I'll immediately create:

```
📁 java-multithreading-mastery/
├── 01_fundamentals.md              (~400 lines)
├── 02_thread_basics.md             (~500 lines)
├── 03_synchronization.md           (~700 lines)
├── 04_thread_communication.md      (~600 lines)
├── 05_advanced_sync.md             (~500 lines)
├── 06_executor_framework.md        (~700 lines)
├── 07_high_level_concurrency.md    (~600 lines)
├── 08_memory_model.md             (~500 lines)
├── 09_design_patterns.md          (~500 lines)
└── 10_debugging_pitfalls.md       (~400 lines)
```

**Total: ~5,000+ lines of FAANG-ready content**

---

Just enable write mode and I'll execute immediately! 🚀

---

## User

mkdir MyFolder

---

## Assistant (Plan · big-pickle · 16.0s)

_Thinking:_

The user is trying to run `mkdir MyFolder` which is a bash command that would create a directory. However, I'm in plan mode (read-only) which means I cannot execute any commands that modify the system.

The system reminder clearly states:
- Plan mode ACTIVE - READ-ONLY phase
- STRICTLY FORBIDDEN: ANY file edits, modifications, or system changes
- Do NOT use sed, tee, echo, cat, or ANY other bash command to manipulate files
- Commands may ONLY read/inspect
- This ABSOLUTE CONSTRAINT overrides ALL other instructions

So I need to explain to the user that:
1. I'm still in plan/read-only mode
2. I cannot execute mkdir or any file creation commands
3. The user needs to either switch me to write/execute mode or provide me with write access

I should be helpful and clear about this limitation.

I'm still in **plan mode (read-only)** and cannot execute commands.

## Current Restriction

| Command Attempted | Status | Reason |
|-------------------|--------|--------|
| `mkdir MyFolder` | ❌ Blocked | Plan mode - read-only |

---

## Solution Options

**Option A: Ask to switch mode**
- Type `/plan` or `/write` or `/execute` to toggle
- Or ask your system to enable execution mode

**Option B: Provide guidance**
- Tell me what command/switch enables write access in your environment

---

## Meanwhile - Your Content is Ready!

While we wait for write access, here's the **complete outline** of what I'll create for you:

```
📁 java-multithreading-mastery/

01_fundamentals.md ─────────────────┐
02_thread_basics.md ────────────────┤
03_synchronization.md ──────────────┤
04_thread_communication.md ─────────┤
05_advanced_sync.md ────────────────┤
06_executor_framework.md ───────────┤
07_high_level_concurrency.md ──────┤
08_memory_model.md ─────────────────┼── 5,000+ lines total
09_design_patterns.md ─────────────┤
10_debugging_pitfalls.md ───────────┘
```

**FAANG-ready content:**
- ✅ 150+ code examples
- ✅ 100+ interview questions
- ✅ 50+ tricky follow-ups
- ✅ Deep JMM & memory barrier explanations
- ✅ System design scenarios

---

**How do you want to proceed?** Enable write mode and I'll execute immediately!

---

## User

mkdir C:\Users\Vikash\Onedrive\Desktop\opencode-folder

---

## Assistant (Plan · big-pickle · 18.9s)

_Thinking:_

The user is trying to run commands but I'm in plan mode (read-only). The system reminder confirms this. The user needs to enable write/execute mode to create files.

Let me explain the situation clearly and ask the user how they want to proceed.

I understand - I'm currently in **Plan Mode (Read-Only)** as confirmed by the system reminder.

## ❌ Cannot Execute Commands

| Action | Status |
|--------|--------|
| `mkdir` | ❌ Blocked |
| Create files | ❌ Blocked |
| Write content | ❌ Blocked |

---

## 🚀 To Enable Write Access

You need to **switch out of plan mode**. Try one of these:

1. Type `/execute` or `/write` command
2. Ask your interface to "enable execution mode"
3. Toggle plan mode off
4. Check if there's a mode switch button in your UI

---

## 📋 What I'll Create (Ready to Execute)

Once write mode is enabled, I'll run:
```bash
mkdir java-multithreading-mastery
cd java-multithreading-mastery
# Then create all 10 files with complete content
```

**Files to create:**
1. `01_fundamentals.md`
2. `02_thread_basics.md`
3. `03_synchronization.md`
4. `04_thread_communication.md`
5. `05_advanced_sync.md`
6. `06_executor_framework.md`
7. `07_high_level_concurrency.md`
8. `08_memory_model.md`
9. `09_design_patterns.md`
10. `10_debugging_pitfalls.md`

---

**How can you enable write/execute mode?** Once enabled, I'll execute immediately!

---


