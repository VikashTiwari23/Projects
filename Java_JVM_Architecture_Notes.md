# Java Virtual Machine (JVM) Architecture - Complete Interview Notes

---

## 1. JVM OVERVIEW

```
    ┌──────────────────────────────────────────────────────────────┐
    │                    JAVA SOURCE CODE                           │
    │                    Hello.java                                 │
    └───────────────────────┬──────────────────────────────────────┘
                            │ javac Hello.java
                            ▼
    ┌──────────────────────────────────────────────────────────────┐
    │                  BYTECODE                                    │
    │                  Hello.class                                 │
    │                  (Platform Independent)                      │
    └───────────────────────┬──────────────────────────────────────┘
                            │ java Hello
                            ▼
    ┌──────────────────────────────────────────────────────────────┐
    │                     JVM                                       │
    │               (Platform Dependent)                           │
    │                                                              │
    │  ┌──────────────────────────────────────────────────────┐   │
    │  │                    JVM ARCHITECTURE                    │   │
    │  │                                                        │   │
    │  │  Class Loader → Runtime Data Areas → Execution Engine  │   │
    │  └──────────────────────────────────────────────────────┘   │
    └──────────────────────────────────────────────────────────────┘
                            │
                            ▼
    ┌──────────────────────────────────────────────────────────────┐
    │                   NATIVE CODE                                │
    │              (Platform Specific)                             │
    └──────────────────────────────────────────────────────────────┘

    "Write Once, Run Anywhere" → JVM makes it possible!
```

---

## 2. JVM ARCHITECTURE DIAGRAM

```
    ┌──────────────────────────────────────────────────────────────────────┐
    │                         JVM ARCHITECTURE                              │
    │                                                                      │
    │  ┌──────────────────────────────────────────────────────────────┐   │
    │  │                   CLASS LOADER SUBSYSTEM                      │   │
    │  │                                                               │   │
    │  │  ┌─────────────┐  ┌─────────────────┐  ┌─────────────────┐  │   │
    │  │  │ Bootstrap    │  │ Extension/       │  │ Application/    │  │   │
    │  │  │ ClassLoader  │→ │ Platform CL      │→ │ System CL       │  │   │
    │  │  │ (C/C++)      │  │ (Java)           │  │ (Java)          │  │   │
    │  │  └─────────────┘  └─────────────────┘  └─────────────────┘  │   │
    │  └──────────────────────────────────────────────────────────────┘   │
    │                              │                                      │
    │                              ▼                                      │
    │  ┌──────────────────────────────────────────────────────────────┐   │
    │  │                  RUNTIME DATA AREAS                          │   │
    │  │                                                               │   │
    │  │  ┌────────────────────────────────────────────────────────┐  │   │
    │  │  │                    METHOD AREA                          │  │   │
    │  │  │  - Class structure, Method data, Constant pool          │  │   │
    │  │  │  - Static variables, Constructor code                   │  │   │
    │  │  └────────────────────────────────────────────────────────┘  │   │
    │  │                                                               │   │
    │  │  ┌────────────────────────────────────────────────────────┐  │   │
    │  │  │                    HEAP                                 │  │   │
    │  │  │  - All objects, Instance variables                     │  │   │
    │  │  │  - Arrays, Wrapper objects                             │  │   │
    │  │  └────────────────────────────────────────────────────────┘  │   │
    │  │                                                               │   │
    │  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │   │
    │  │  │ Thread 1  │  │ Thread 2  │  │ Thread 3  │  │   ...    │   │   │
    │  │  │ Stack     │  │ Stack     │  │ Stack     │  │          │   │   │
    │  │  ├──────────┤  ├──────────┤  ├──────────┤  ├──────────┤   │   │
    │  │  │ PC Reg   │  │ PC Reg   │  │ PC Reg   │  │          │   │   │
    │  │  ├──────────┤  ├──────────┤  ├──────────┤  ├──────────┤   │   │
    │  │  │ Native   │  │ Native   │  │ Native   │  │          │   │   │
    │  │  │ Method   │  │ Method   │  │ Method   │  │          │   │   │
    │  │  │ Stack    │  │ Stack    │  │ Stack    │  │          │   │   │
    │  │  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │   │
    │  └──────────────────────────────────────────────────────────────┘   │
    │                              │                                      │
    │                              ▼                                      │
    │  ┌──────────────────────────────────────────────────────────────┐   │
    │  │                   EXECUTION ENGINE                           │   │
    │  │                                                               │   │
    │  │  ┌─────────────────┐  ┌─────────────────────────────────┐   │   │
    │  │  │ Interpreter      │  │ JIT Compiler                     │   │   │
    │  │  │ (fast startup,   │  │ (slow compile, fast execution)  │   │   │
    │  │  │  slow execution) │  │                                  │   │   │
    │  │  └─────────────────┘  └─────────────────────────────────┘   │   │
    │  │                                                               │   │
    │  │  ┌─────────────────────────────────────────────────────────┐ │   │
    │  │  │ Garbage Collector                                        │ │   │
    │  │  │ - Serial GC, Parallel GC, G1 GC, ZGC, Shenandoah       │ │   │
    │  │  └─────────────────────────────────────────────────────────┘ │   │
    │  │                                                               │   │
    │  │  ┌─────────────────────────────────────────────────────────┐ │   │
    │  │  │ Native Method Interface (JNI)                           │ │   │
    │  │  └─────────────────────────────────────────────────────────┘ │   │
    │  └──────────────────────────────────────────────────────────────┘   │
    └──────────────────────────────────────────────────────────────────────┘
```

---

## 3. CLASS LOADER SUBSYSTEM

### 3.1 Class Loader Hierarchy (Delegation Model)

```
    ┌──────────────────────────────────────────────────────────────┐
    │  1. Bootstrap ClassLoader (Primordial)                       │
    │     - Written in C/C++ (not Java)                            │
    │     - Loads: rt.jar, java.lang.*, java.util.*, etc.         │
    │     - Returns: null (not a Java object)                      │
    │     - Location: JAVA_HOME/lib/                               │
    │                                                              │
    │  2. Extension/Platform ClassLoader                            │
    │     - Written in Java (sun.misc.Launcher$ExtClassLoader)     │
    │     - Loads: ext/*.jar, javax.* packages                     │
    │     - Parent: Bootstrap                                      │
    │     - Location: JAVA_HOME/lib/ext/                           │
    │                                                              │
    │  3. Application/System ClassLoader                            │
    │     - Written in Java (sun.misc.Launcher$AppClassLoader)     │
    │     - Loads: classpath classes, your code                    │
    │     - Parent: Extension                                      │
    │     - Location: CLASSPATH                                    │
    │                                                              │
    │  4. Custom ClassLoader (Optional)                             │
    │     - You write it                                           │
    │     - Loads from network, database, encrypted files, etc.    │
    │     - Parent: Application                                    │
    └──────────────────────────────────────────────────────────────┘

    Delegation Model:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  Custom CL ──delegates──→ App CL ──delegates──→ Ext CL       │
    │                                                              │
    │  App CL ──delegates──→ Ext CL ──delegates──→ Bootstrap CL    │
    │                                                              │
    │  If Bootstrap can't find → delegates back to Ext              │
    │  If Ext can't find → delegates back to App                    │
    │  If App can't find → ClassNotFoundException!                  │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘

    Why Delegation?
    ┌──────────────────────────────────────────────────────────────┐
    │  1. Security: Prevents loading malicious class as java.lang  │
    │  2. Uniqueness: Same class loaded only once                   │
    │  3. modularity: Each level handles specific classes           │
    └──────────────────────────────────────────────────────────────┘
```

### 3.2 Class Loading Process

```
    Step 1: LOADING
    ┌──────────────────────────────────────────────────────────────┐
    │  - Find .class file (from file system, network, etc.)        │
    │  - Read binary data (bytecode)                               │
    │  - Create java.lang.Class object in Heap                     │
    └──────────────────────────────────────────────────────────────┘

    Step 2: LINKING
    ┌──────────────────────────────────────────────────────────────┐
    │  2a. VERIFICATION                                            │
    │      - Check bytecode format                                 │
    │      - Check magic number (0xCAFEBABE)                       │
    │      - Ensure no security violations                         │
    │                                                              │
    │  2b. PREPARATION                                             │
    │      - Allocate memory for static variables                  │
    │      - Set default values (0, null, false)                   │
    │      - NOT initial values!                                   │
    │                                                              │
    │  2c. RESOLUTION                                              │
    │      - Replace symbolic references with direct references    │
    │      - Link to other classes, methods, fields                 │
    └──────────────────────────────────────────────────────────────┘

    Step 3: INITIALIZATION
    ┌──────────────────────────────────────────────────────────────┐
    │  - Execute static blocks and static variable assignments     │
    │  - Initialize static fields with actual values               │
    │  - Thread-safe (synchronized on class object)                │
    │  - Parent initialized before child                           │
    └──────────────────────────────────────────────────────────────┘

    Visual:
    ┌──────────┐   ┌──────────┐   ┌──────────┐   ┌──────────┐
    │ .class   │──→│ LOADING  │──→│ LINKING  │──→│ INIT     │
    │ file     │   │ Read     │   │ Verify + │   │ static   │
    │          │   │ bytecode │   │ Prepare  │   │ blocks   │
    │          │   │          │   │ + Resolve│   │          │
    └──────────┘   └──────────┘   └──────────┘   └──────────┘
```

### 3.3 Runtime Constant Pool

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Constant Pool: Part of Method Area                          │
    │                                                              │
    │  Contains:                                                   │
    │  - Literal constants ("Hello", 42, 3.14)                    │
    │  - Class/method/field references                             │
    │  - Method handles                                            │
    │  - Dynamic constants                                         │
    │                                                              │
    │  Class constant pool → copies to Runtime constant pool       │
    │  in Method Area at link time                                 │
    └──────────────────────────────────────────────────────────────┘

    Example:
    String s = "Hello";
    int x = 42;

    Constant Pool:
    ┌─────┬──────────────────────┐
    │ #1  │ "Hello" (String)     │
    │ #2  │ 42 (Integer)         │
    │ #3  │ Method ref: hashCode │
    └─────┴──────────────────────┘
```

---

## 4. RUNTIME DATA AREAS

### 4.1 MEMORY MODEL

```
    JVM Memory:
    ┌──────────────────────────────────────────────────────────────┐
    │                     HEAP MEMORY                               │
    │               (Shared across all threads)                     │
    │                                                              │
    │  ┌────────────────────────────────────────────────────────┐  │
    │  │                  YOUNG GENERATION                       │  │
    │  │  ┌──────────┐  ┌───────────┐  ┌───────────┐          │  │
    │  │  │Eden Space │→ │ S0 (From) │  │ S1 (To)   │          │  │
    │  │  │ (80%)     │  │ (10%)     │  │ (10%)     │          │  │
    │  │  │           │  │ Survivor  │  │ Survivor  │          │  │
    │  │  │           │  │           │← │           │          │  │
    │  │  └──────────┘  └───────────┘  └───────────┘          │  │
    │  └────────────────────────────────────────────────────────┘  │
    │                          │                                    │
    │                          │ After GC (age threshold)           │
    │                          ▼                                    │
    │  ┌────────────────────────────────────────────────────────┐  │
    │  │                  OLD GENERATION                          │  │
    │  │  (Long-lived objects, Tenured space)                     │  │
    │  └────────────────────────────────────────────────────────┘  │
    │                                                              │
    │  ┌────────────────────────────────────────────────────────┐  │
    │  │                  METASPACE                               │  │
    │  │  (Class metadata, Method metadata)                       │  │
    │  │  (Replaces PermGen in Java 8+)                           │  │
    │  │  (Lives in native memory, not heap!)                     │  │
    │  └────────────────────────────────────────────────────────┘  │
    └──────────────────────────────────────────────────────────────┘

    ┌──────────────────────────────────────────────────────────────┐
    │                THREAD-LOCAL MEMORY                            │
    │             (Each thread has its own)                         │
    │                                                              │
    │  ┌──────────┐  ┌──────────┐  ┌──────────┐                  │
    │  │ Thread 1  │  │ Thread 2  │  │ Thread 3  │                  │
    │  │ Stack     │  │ Stack     │  │ Stack     │                  │
    │  ├──────────┤  ├──────────┤  ├──────────┤                  │
    │  │ PC Reg   │  │ PC Reg   │  │ PC Reg   │                  │
    │  ├──────────┤  ├──────────┤  ├──────────┤                  │
    │  │ Native   │  │ Native   │  │ Native   │                  │
    │  │ Method   │  │ Method   │  │ Method   │                  │
    │  │ Stack    │  │ Stack    │  │ Stack    │                  │
    │  └──────────┘  └──────────┘  └──────────┘                  │
    └──────────────────────────────────────────────────────────────┘
```

### 4.2 Each Memory Area Detail

```
    ┌──────────────────────────────────────────────────────────────┐
    │  HEAP                                                          │
    │  - Stores all objects and arrays                              │
    │  - Shared across all threads                                  │
    │  - Divided into Young and Old generations                     │
    │  - GC manages memory here                                     │
    │  - New objects created in Eden                                 │
    │  - Objects survive GC → moved to Survivor → Old gen           │
    │  - Size: -Xms (initial), -Xmx (maximum)                      │
    ├──────────────────────────────────────────────────────────────┤
    │  METHOD AREA (Metaspace in Java 8+)                           │
    │  - Stores class structure                                     │
    │  - Runtime constant pool                                      │
    │  - Static variables                                           │
    │  - Method bytecode                                            │
    │  - Constructor code                                           │
    │  - Shared across all threads                                  │
    │  - Size: -XX:MetaspaceSize                                    │
    ├──────────────────────────────────────────────────────────────┤
    │  STACK (Per Thread)                                           │
    │  - Stores method frames (activation records)                  │
    │  - Each method call = one frame pushed                        │
    │  - Frame contains:                                            │
    │    - Local variables (primitives + references)                │
    │    - Operand stack (for computation)                          │
    │    - Frame data (constant pool reference)                    │
    │  - LIFO order                                                 │
    │  - Size: -Xss (per thread)                                    │
    ├──────────────────────────────────────────────────────────────┤
    │  PC REGISTER (Per Thread)                                     │
    │  - Points to current bytecode instruction                     │
    │  - Each thread has its own PC register                        │
    │  - Native method: undefined                                   │
    ├──────────────────────────────────────────────────────────────┤
    │  NATIVE METHOD STACK (Per Thread)                             │
    │  - Stores native method calls (C/C++)                         │
    │  - Uses native method interface (JNI)                         │
    │  - Varies by OS and JVM implementation                        │
    └──────────────────────────────────────────────────────────────┘
```

### 4.3 Stack Frame Structure

```
    Method call: factorial(5)

    Stack Frame for factorial(5):
    ┌──────────────────────────────────────┐
    │  LOCAL VARIABLES                      │
    │  ┌──────────┬────────────────────┐   │
    │  │ Slot 0   │ this (if instance)  │   │
    │  │ Slot 1   │ n = 5               │   │
    │  │ Slot 2   │ result              │   │
    │  └──────────┴────────────────────┘   │
    ├──────────────────────────────────────┤
    │  OPERAND STACK                        │
    │  ┌────────────────────────────────┐  │
    │  │ 5, 4, 3, 2, 1 (during computation)│ │
    │  └────────────────────────────────┘  │
    ├──────────────────────────────────────┤
    │  FRAME DATA                           │
    │  - Reference to constant pool         │
    │  - Return address                     │
    └──────────────────────────────────────┘

    Stack growth:
    ┌──────────────────────────────────────┐
    │  main() frame                         │ ← Bottom
    │  factorial(5) frame                   │
    │  factorial(4) frame                   │
    │  factorial(3) frame                   │
    │  factorial(2) frame                   │ ← Top (growing up)
    │  factorial(1) frame                   │
    └──────────────────────────────────────┘

    StackOverflowError: Stack grows too deep (infinite recursion)
```

---

## 5. EXECUTION ENGINE

```
    ┌──────────────────────────────────────────────────────────────┐
    │                   EXECUTION ENGINE                            │
    │                                                              │
    │  ┌────────────────────────────────────────────────────────┐  │
    │  │  INTERPRETER                                            │  │
    │  │  - Reads bytecode line by line                          │  │
    │  │  - Translates to machine code immediately               │  │
    │  │  - Fast startup                                         │  │
    │  │  - Slow execution (reinterprets every time)             │  │
    │  └────────────────────────────────────────────────────────┘  │
    │                                                              │
    │  ┌────────────────────────────────────────────────────────┐  │
    │  │  JIT COMPILER (Just-In-Time)                           │  │
    │  │  - Compiles hot bytecode to native machine code        │  │
    │  │  - Caches compiled code                                 │  │
    │  │  - Slow startup (compilation takes time)                │  │
    │  │  - Fast execution (no reinterpretation)                 │  │
    │  │                                                          │  │
    │  │  Hot Spot Detection:                                     │  │
    │  │  - Method called > 10,000 times → HOT SPOT             │  │
    │  │  - Loop iterated > 10,000 times → HOT SPOT             │  │
    │  │  - These get compiled to native code                     │  │
    │  └────────────────────────────────────────────────────────┘  │
    │                                                              │
    │  ┌────────────────────────────────────────────────────────┐  │
    │  │  AHEAD-OF-TIME (AOT) - GraalVM Native Image           │  │
    │  │  - Compiles Java to native binary BEFORE execution     │  │
    │  │  - Fastest startup                                      │  │
    │  │  - Smaller binary size                                  │  │
    │  │  - Trade-off: no runtime optimization                   │  │
    │  └────────────────────────────────────────────────────────┘  │
    └──────────────────────────────────────────────────────────────┘

    Interpreter vs JIT:
    ┌──────────────────┬──────────────────┬──────────────────────┐
    │ Feature           │ Interpreter      │ JIT Compiler         │
    ├──────────────────┼──────────────────┼──────────────────────┤
    │ Startup          │ Fast             │ Slow                 │
    │ Execution        │ Slow             │ Fast                 │
    │ Compilation      │ Line by line     │ Method-level         │
    │ Optimization     │ None             │ Hot spot optimization│
    │ Memory           │ Less             │ More (cached code)   │
    └──────────────────┴──────────────────┴──────────────────────┘
```

---

## 6. GARBAGE COLLECTION

### 6.1 What is GC?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Garbage Collection: Automatic memory management              │
    │                                                              │
    │  - Finds objects that are NO LONGER REFERENCED               │
    │  - Reclaims memory used by those objects                      │
    │  - Runs automatically (developer doesn't manage)             │
    │  - Cannot guarantee timely collection                         │
    │                                                              │
    │  What makes an object eligible for GC?                       │
    │  1. No references pointing to it                              │
    │  2. All references set to null                                │
    │  3. Reference variable goes out of scope                      │
    │  4. Island of isolation (objects reference only each other)   │
    └──────────────────────────────────────────────────────────────┘

    Example:
    ┌──────────────────────────────────────────────────────────────┐
    │  Object obj = new Object();  // obj → Object (reachable)     │
    │  obj = null;                // Object now unreachable → GC!   │
    │                                                              │
    │  void method() {                                             │
    │      Object local = new Object();  // on stack               │
    │  }  // method returns → local gone → Object unreachable → GC │
    └──────────────────────────────────────────────────────────────┘
```

### 6.2 Generations & GC

```
    Why Generations?
    ┌──────────────────────────────────────────────────────────────┐
    │  Observation: Most objects die young!                         │
    │  - Short-lived: temporary variables, loop counters            │
    │  - Long-lived: caches, connection pools, singletons          │
    │                                                              │
    │  Solution: Separate generations → different GC strategies     │
    └──────────────────────────────────────────────────────────────┘

    Heap Generations:
    ┌──────────────────────────────────────────────────────────────┐
    │  YOUNG GENERATION (Minor GC target)                          │
    │  ┌─────────────┬─────────────┬─────────────┐                │
    │  │ Eden (80%)   │ S0 (10%)    │ S1 (10%)    │                │
    │  │ New objects  │ Survivor    │ Survivor    │                │
    │  │ born here    │ (From)      │ (To)        │                │
    │  └─────────────┴──────┬──────┴──────┬──────┘                │
    │                       │              │                        │
    │                       ▼              ▼                        │
    │                    Survivor 0    Survivor 1                   │
    │                    (objects that survived one GC)             │
    │                                                              │
    │  Objects surviving multiple GCs → promoted to Old Gen        │
    └──────────────────────────────────────────────────────────────┘

    Object Lifecycle:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  new Object() → Eden → Minor GC → Survivor S0               │
    │                                    ↓                         │
    │                    Minor GC (again) → Survivor S1            │
    │                                    ↓                         │
    │                    After N GCs → OLD GENERATION              │
    │                                    ↓                         │
    │                    Major GC → Full GC                         │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

### 6.3 Types of GC

```
    ┌──────────────────────────────────────────────────────────────┐
    │  1. MINOR GC (Young GC)                                       │
    │     - Cleans Young Generation only                            │
    │     - Triggered when Eden is full                             │
    │     - Fast (most objects die young)                           │
    │     - STW (Stop The World) is brief                           │
    │                                                              │
    │  2. MAJOR GC (Old GC)                                         │
    │     - Cleans Old Generation only                              │
    │     - Triggered when Old gen is full                          │
    │     - Slower (more objects to check)                          │
    │     - Longer STW pause                                        │
    │                                                              │
    │  3. FULL GC                                                   │
    │     - Cleans ENTIRE heap (Young + Old)                        │
    │     - Triggered by: System.gc(), memory pressure, etc.       │
    │     - Slowest, longest pause                                  │
    │     - AVOID if possible!                                      │
    │                                                              │
    │  4. Mixed GC (G1 GC)                                         │
    │     - Cleans Young gen + some Old gen regions                 │
    │     - Balanced pause time                                     │
    │                                                              │
    │  5. Concurrent GC                                             │
    │     - Runs mostly in parallel with application                │
    │     - Minimal STW pauses                                      │
    └──────────────────────────────────────────────────────────────┘
```

### 6.4 GC Algorithms

```
    ┌──────────────────────────────────────────────────────────────┐
    │  1. SERIAL GC (-XX:+UseSerialGC)                              │
    │     - Single thread for all GC                                │
    │     - STW for entire collection                               │
    │     - Simple, low overhead                                    │
    │     - Best for single-core, small apps                        │
    │                                                              │
    │  2. PARALLEL GC (-XX:+UseParallelGC)                          │
    │     - Multiple threads for Minor GC                           │
    │     - Single thread for Major GC                              │
    │     - STW for entire collection                               │
    │     - Best throughput (batch processing)                      │
    │     - Default in JDK 8                                        │
    │                                                              │
    │  3. CMS (-XX:+UseConcMarkSweepGC) [Deprecated]               │
    │     - Concurrent Mark Sweep                                   │
    │     - Most work done concurrently                             │
    │     - Short STW pauses                                        │
    │     - Fragmentation issues                                    │
    │                                                              │
    │  4. G1 GC (-XX:+UseG1GC) [Default JDK 9+]                    │
    │     - Garbage First                                           │
    │     - Divides heap into regions                               │
    │     - Predictable pause times                                 │
    │     - Balanced throughput and latency                         │
    │                                                              │
    │  5. ZGC (-XX:+UseZGC) [Java 15+]                              │
    │     - Ultra-low latency (<10ms pause)                         │
    │     - Concurrent processing                                   │
    │     - Scalable to TB-level heaps                              │
    │                                                              │
    │  6. Shenandoah (-XX:+UseShenandoahGC)                         │
    │     - Low-pause, concurrent                                   │
    │     - Similar to ZGC                                          │
    └──────────────────────────────────────────────────────────────┘

    Comparison:
    ┌──────────────┬──────────────┬──────────────┬──────────────┐
    │ GC             │ Pause Time   │ Throughput   │ Heap Size    │
    ├──────────────┼──────────────┼──────────────┼──────────────┤
    │ Serial         │ Longest      │ Low          │ Small        │
    │ Parallel       │ Long         │ Highest      │ Medium       │
    │ CMS            │ Short        │ Medium       │ Medium       │
    │ G1             │ Medium       │ Good         │ Large        │
    │ ZGC            │ <10ms        │ Good         │ Very Large   │
    │ Shenandoah     │ <10ms        │ Good         │ Very Large   │
    └──────────────┴──────────────┴──────────────┴──────────────┘
```

### 6.5 GC Roots

```
    What makes an object REACHABLE (not eligible for GC)?
    ┌──────────────────────────────────────────────────────────────┐
    │  GC ROOTS:                                                   │
    │  1. Local variables in stack frames                          │
    │  2. Static variables (Class objects)                          │
    │  3. JNI (Native) references                                  │
    │  4. Synchronized objects (locks)                              │
    │  5. Thread objects (running threads)                          │
    │  6. Objects in monitor (wait/notify)                          │
    └──────────────────────────────────────────────────────────────┘

    Reachability:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  GC Root → Object A → Object B → Object C  (REACHABLE)      │
    │                                                              │
    │  Object D → Object E → Object F  (NOT reachable → GC!)      │
    │                                                              │
    │  Object G ←→ Object H  (Isolated island → GC!)              │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

### 6.6 Reference Types

```
    ┌──────────────────────────────────────────────────────────────┐
    │  1. STRONG REFERENCE                                           │
    │     Object obj = new Object();                                │
    │     - Never collected while strong reference exists           │
    │     - Default reference type                                  │
    │                                                              │
    │  2. SOFT REFERENCE (java.lang.ref.SoftReference)              │
    │     SoftReference<Object> soft = new SoftReference<>(obj);    │
    │     - Collected when memory is low                            │
    │     - Used for caching                                        │
    │                                                              │
    │  3. WEAK REFERENCE (java.lang.ref.WeakReference)              │
    │     WeakReference<Object> weak = new WeakReference<>(obj);    │
    │     - Collected in next GC (regardless of memory)             │
    │     - Used in WeakHashMap, ThreadLocal                        │
    │                                                              │
    │  4. PHANTOM REFERENCE (java.lang.ref.PhantomReference)        │
    │     PhantomReference<Object> phantom = ...;                    │
    │     - Always returns null from get()                          │
    │     - Used for cleanup (finalize/reachability notification)   │
    └──────────────────────────────────────────────────────────────┘

    Priority: Strong > Soft > Weak > Phantom
```

---

## 7. JVM MEMORY SETTINGS

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Setting                  │ Description                       │
    ├──────────────────────────────────────────────────────────────┤
    │  -Xms<size>              │ Initial heap size                  │
    │  -Xmx<size>              │ Maximum heap size                  │
    │  -Xmn<size>              │ Young generation size              │
    │  -Xss<size>              │ Thread stack size                  │
    │  -XX:MetaspaceSize=size  │ Initial Metaspace size             │
    │  -XX:MaxMetaspaceSize    │ Maximum Metaspace size             │
    │  -XX:NewRatio=n          │ Old:Young ratio (n:1)              │
    │  -XX:SurvivorRatio=n     │ Eden:Survivor ratio                │
    │  -XX:MaxTenuringThreshold│ Max GCs before promotion           │
    │  -XX:+UseG1GC            │ Use G1 Garbage Collector           │
    │  -XX:+PrintGCDetails     │ Print GC logs                      │
    └──────────────────────────────────────────────────────────────┘

    Common Configurations:
    ┌──────────────────────────────────────────────────────────────┐
    │  Small app:     -Xms256m -Xmx512m                           │
    │  Medium app:    -Xms1g -Xmx2g                               │
    │  Large app:     -Xms4g -Xmx8g                               │
    │  Best practice: Set -Xms = -Xmx (avoid resize)              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 8. CLASS FILE FORMAT

```
    .class file structure:
    ┌──────────────────────────────────────────────────────────────┐
    │  Magic Number: 0xCAFEBABE (4 bytes)                          │
    │  Minor Version (2 bytes)                                      │
    │  Major Version (2 bytes) → JDK 8=52, JDK 11=55, JDK 17=61  │
    │  Constant Pool Count (2 bytes)                                │
    │  Constant Pool (variable)                                     │
    │  Access Flags (2 bytes) → public, abstract, final, etc.     │
    │  This Class (2 bytes)                                         │
    │  Super Class (2 bytes)                                        │
    │  Interfaces Count + Interfaces                                │
    │  Fields Count + Fields                                        │
    │  Methods Count + Methods                                      │
    │  Attributes Count + Attributes                                │
    └──────────────────────────────────────────────────────────────┘

    Magic Number:
    $ hexdump -C HelloWorld.class | head -1
    00000000  ca fe ba be 00 00 00 34  ...
             ──────── ────────
             Magic    Version (52 = Java 8)
```

---

## 9. JVM CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════════╗
║                     JVM ARCHITECTURE CHEAT SHEET                     ║
╠══════════════════════════════════════════════════════════════════════╣
║                                                                      ║
║  3 MAIN COMPONENTS:                                                  ║
║  1. Class Loader Subsystem                                           ║
║  2. Runtime Data Areas                                               ║
║  3. Execution Engine                                                 ║
║                                                                      ║
║  CLASS LOADER:                                                       ║
║  - Bootstrap → Extension → Application (Delegation model)           ║
║  - Loads .class files, verification, preparation, resolution        ║
║  - Parent-first delegation (prevents duplicate loading)              ║
║                                                                      ║
║  MEMORY AREAS:                                                       ║
║  ┌─────────────────────────────────────────────────────────────┐    ║
║  │  HEAP (Shared)                                               │    ║
║  │  - Young Gen (Eden + S0 + S1) → Minor GC                    │    ║
║  │  - Old Gen (Tenured) → Major GC                             │    ║
║  │  - Metaspace (Class metadata) - Java 8+ (native memory)     │    ║
║  ├─────────────────────────────────────────────────────────────┤    ║
║  │  STACK (Per Thread)                                          │    ║
║  │  - Stack Frames (local vars, operand stack, frame data)     │    ║
║  │  - LIFO order                                               │    ║
║  │  - StackOverflowError (too deep recursion)                  │    ║
║  ├─────────────────────────────────────────────────────────────┤    ║
║  │  PC REGISTER (Per Thread)                                    │    ║
║  │  - Current bytecode instruction                              │    ║
║  ├─────────────────────────────────────────────────────────────┤    ║
║  │  METHOD AREA (Shared)                                        │    ║
║  │  - Class structure, Static variables, Constant pool         │    ║
║  │  - Metaspace (Java 8+)                                      │    ║
║  ├─────────────────────────────────────────────────────────────┤    ║
║  │  NATIVE METHOD STACK (Per Thread)                            │    ║
║  │  - C/C++ method calls (JNI)                                  │    ║
║  └─────────────────────────────────────────────────────────────┘    ║
║                                                                      ║
║  EXECUTION ENGINE:                                                   ║
║  - Interpreter: Fast start, slow execution                          ║
║  - JIT Compiler: Slow start, fast execution (hot spot detection)    ║
║  - AOT (GraalVM): Ahead-of-time compilation                        ║
║                                                                      ║
║  GARBAGE COLLECTION:                                                 ║
║  - Minor GC: Young gen only (frequent, fast)                        ║
║  - Major GC: Old gen only (less frequent, slower)                   ║
║  - Full GC: Entire heap (rare, slowest)                             ║
║  - Algorithms: Serial, Parallel, CMS, G1, ZGC, Shenandoah          ║
║  - GC Roots: Local vars, Static vars, JNI, Monitors                ║
║                                                                      ║
║  REFERENCE TYPES:                                                    ║
║  - Strong → Never GC'd while referenced                             ║
║  - Soft → GC'd when memory low (caching)                            ║
║  - Weak → GC'd next cycle (WeakHashMap)                              ║
║  - Phantom → Cleanup (post-mortem)                                   ║
║                                                                      ║
║  KEY SETTINGS:                                                       ║
║  -Xms/-Xmx: Heap size  -Xss: Stack size                             ║
║  -XX:MetaspaceSize: Metadata   -XX:+UseG1GC: GC type                ║
║                                                                      ║
║  CLASS FILE: Magic 0xCAFEBABE → Version → Constant Pool → Methods   ║
║                                                                      ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

## 10. JVM INTERVIEW QUESTIONS (40+)

### ⭐ BASIC

**Q1: What is JVM?**
> Java Virtual Machine. Abstract machine that executes Java bytecode. Platform-dependent implementation of Java runtime.

**Q2: What is difference between JVM and JRE and JDK?**
```
JDK = JRE + Development Tools (javac, debugger)
JRE = JVM + Class Libraries (java.lang, java.util)
JVM = Bytecode interpreter/executor
```

**Q3: What is bytecode?**
> Platform-independent intermediate code (.class file) generated by compiler. Executed by JVM.

**Q4: What is "Write Once, Run Anywhere"?**
> Java bytecode can run on any platform with JVM. JVM implementation differs per OS.

**Q5: What are the main components of JVM?**
```
1. Class Loader Subsystem
2. Runtime Data Areas (Heap, Stack, Method Area, PC, Native Stack)
3. Execution Engine (Interpreter, JIT, GC)
```

**Q6: What is difference between Stack and Heap?**
```
Stack: Per-thread, local variables, primitives, method frames, LIFO
Heap: Shared, objects, instance variables, arrays, GC managed
```

**Q7: What is stored in Stack vs Heap?**
```
Stack: Local primitives (int, char), object references, method frames
Heap: Objects, instance variables, arrays, static variables
```

**Q8: What causes StackOverflowError?**
> Too deep method recursion. Each call adds frame, stack size exceeded.

**Q9: What causes OutOfMemoryError?**
> Heap full, can't allocate new object. Happens when too many objects or memory leak.

**Q10: What is difference between == and equals()?**
```
==: Reference comparison (same object?)
equals(): Content comparison (same value?)
```

---

### ⭐⭐ MIDDLE

**Q11: How does Class Loader work?**
```
1. Bootstrap loads core classes (rt.jar)
2. Extension loads ext classes
3. Application loads your classes
4. Each delegates to parent first
5. If parent can't find, child tries
```

**Q12: What is Delegation Model?**
> Class loaders delegate loading to parent first. Ensures security and uniqueness. Bootstrap → Extension → Application.

**Q13: What is difference between Class.forName() and ClassLoader.loadClass()?**
```
Class.forName(): Loads AND initializes the class
ClassLoader.loadClass(): Loads but doesn't initialize
```

**Q14: What happens when you create an object?**
```
1. Class loaded and initialized
2. Memory allocated in heap
3. Constructor called
4. Reference returned
5. Default values assigned before constructor
```

**Q15: What is difference between Minor and Major GC?**
```
Minor GC: Young generation, frequent, fast
Major GC: Old generation, less frequent, slower
Full GC: Entire heap, rare, slowest
```

**Q16: What is Stop The World (STW)?**
> GC pauses ALL application threads. No code executes during GC. Modern GCs minimize STW.

**Q17: What is difference between G1 and ZGC?**
```
G1: Region-based, predictable pauses, balanced
ZGC: Ultra-low latency (<10ms), concurrent, TB-level heaps
```

**Q18: What is JIT Compiler?**
> Compiles hot bytecode to native machine code at runtime. Faster execution but slower startup.

**Q19: What is Hot Spot?**
> Code that executes frequently (method called >10,000 times). JIT compiles these to native code.

**Q20: What is Metaspace?**
> Stores class metadata in native memory (not heap). Replaces PermGen in Java 8+. Doesn't cause OOM easily.

**Q21: What is difference between PermGen and Metaspace?**
```
PermGen: Fixed size, in heap, OOM common
Metaspace: Dynamic size, native memory, OOM rare
```

**Q22: What are GC Roots?**
> Objects always reachable: local vars, static vars, JNI, monitors, running threads.

**Q23: What is difference between Soft and Weak references?**
```
Soft: Collected when memory low (caching)
Weak: Collected next GC (WeakHashMap)
```

**Q24: What is the difference between Serial and Parallel GC?**
```
Serial: Single thread, simple, low overhead
Parallel: Multiple threads, high throughput
```

**Q25: How to trigger GC?**
```java
System.gc();           // Suggestion (not guaranteed)
Runtime.getRuntime().gc();
// GC runs automatically based on memory pressure
```

---

### ⭐⭐⭐ ADVANCED

**Q26: What is happens-before relationship?**
```
If action A happens-before B:
- A's effects are visible to B
- Program order: within thread, A before B
- Monitor: unlock before lock
- Volatile: write before read
- Thread: start() before thread actions
```

**Q27: What is escape analysis?**
> JIT determines if object escapes the method. If not:
> - Stack allocation (faster, no GC needed)
> - Scalar replacement (break into primitives)
> - Lock elision (remove synchronization)

**Q28: What is class initialization deadlock?**
```java
class A { static { B.b.getClass(); } }  // Waits for B
class B { static { A.a.getClass(); } }  // Waits for A
// DEADLOCK! Both waiting for other's class init
```

**Q29: What is the structure of .class file?**
```
Magic (0xCAFEBABE) → Version → Constant Pool → Access Flags →
This Class → Super Class → Interfaces → Fields → Methods → Attributes
```

**Q30: What is JVM Tuning?**
```
1. Set -Xms = -Xmx (avoid heap resize)
2. Set -Xss (thread stack size)
3. Choose right GC algorithm
4. Monitor GC logs
5. Tune survivor ratios
6. Set MaxMetaspaceSize
7. Use jstat, jmap, jhat tools
```

**Q31: What are JVM tools?**
```
jps     → List Java processes
jstat   → GC statistics
jmap    → Memory map, heap dump
jhat    → Heap dump analysis
jstack  → Thread dump
jcmd    → JVM diagnostic commands
jinfo   → JVM configuration
javap   → Disassemble class file
```

**Q32: What is difference between jmap and jstack?**
```
jmap: Heap dump, memory analysis
jstack: Thread dump, deadlock detection
```

**Q33: What is the difference between WeakHashMap and HashMap?**
```
WeakHashMap: Keys are WeakReferences, GC'd when no strong ref
HashMap: Strong references, never GC'd while in map
Use WeakHashMap for caches (auto-cleanup)
```

**Q34: What is the difference between final, finally, finalize?**
```
final: Constant, no override, no extend
finally: Code always runs after try-catch
finalize: Called by GC before object is collected (deprecated)
```

**Q35: What is the difference between ArrayList and Array?**
```
Array: Fixed size, primitive types, direct memory
ArrayList: Resizable, objects only, wrapper overhead
```

**Q36: What is memory leak in Java?**
```
Objects not used but still referenced → GC can't collect
Common causes:
- Static集合持有对象
- Inner class holding outer reference
- Unclosed resources (streams, connections)
- ThreadLocal not cleaned up
```

**Q37: How to find memory leak?**
```
1. Heap dump analysis (jmap, VisualVM)
2. Compare heap dumps over time
3. Look for growing object counts
4. Check for unclosed resources
5. Use profiling tools
```

**Q38: What is the difference between Young and Old Generation?**
```
Young: Eden + S0 + S1, new objects, Minor GC
Old: Long-lived objects, Major GC, promoted from Young
Ratio: -XX:NewRatio=2 (Old:Young = 2:1)
```

**Q39: What is the difference between Parallel and Concurrent GC?**
```
Parallel: Multiple GC threads, STW, high throughput
Concurrent: GC runs with app, short STW, low latency
```

**Q40: What is JIT compilation threshold?**
```
Default: Method called 10,000 times → compile to native
Loop: Back-edge count 10,000 → compile loop
-XX:CompileThreshold=N to change
```

**Q41: What is GraalVM Native Image?**
> AOT compilation to native binary. No JVM needed. Fast startup, smaller memory. Limited reflection support.

**Q42: What is the difference between Serial and G1 GC?**
```
Serial: Single thread, simple, small heaps
G1: Region-based, concurrent, predictable pauses, large heaps
```

**Q43: What are the default GC settings in Java?**
```
Java 8: Parallel GC
Java 9-15: G1 GC
Java 16+: G1 GC (ZGC optional)
Java 21: ZGC default (some distributions)
```

**Q44: What is the difference between heap dump and thread dump?**
```
Heap dump: Snapshot of all objects in heap (memory analysis)
Thread dump: Snapshot of all threads and their states (deadlock detection)
```

**Q45: What is the difference between jvm arguments -server and -client?**
```
-server: 64-bit, optimized for throughput, larger heap
-client: 32-bit, optimized for startup, smaller heap
Default: Depends on platform
```

---

## 11. QUICK REFERENCE - ALL JVM SETTINGS

```
╔══════════════════════════════════════════════════════════════════╗
║              JVM SETTINGS CHEAT SHEET                            ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  HEAP:                                                           ║
║  -Xms512m          Initial heap                                  ║
║  -Xmx2g            Maximum heap                                  ║
║  -Xmn256m          Young generation size                         ║
║  -XX:NewRatio=2    Old:Young ratio                               ║
║  -XX:SurvivorRatio=8  Eden:Survivor ratio                        ║
║                                                                  ║
║  STACK:                                                          ║
║  -Xss512k          Thread stack size                             ║
║                                                                  ║
║  METASPACE:                                                      ║
║  -XX:MetaspaceSize=256m  Initial                                 ║
║  -XX:MaxMetaspaceSize=512m  Maximum                              ║
║                                                                  ║
║  GC:                                                             ║
║  -XX:+UseSerialGC      Serial GC                                 ║
║  -XX:+UseParallelGC    Parallel GC (default JDK 8)              ║
║  -XX:+UseG1GC          G1 GC (default JDK 9+)                   ║
║  -XX:+UseZGC            ZGC (Java 15+)                           ║
║  -XX:MaxGCPauseMillis=200  Target max pause                      ║
║  -XX:+PrintGCDetails    Print GC logs                            ║
║                                                                  ║
║  DEBUGGING:                                                      ║
║  -XX:+HeapDumpOnOutOfMemoryError  Dump heap on OOM              ║
║  -XX:HeapDumpPath=/path     Heap dump location                   ║
║  -XX:+PrintFlagsFinal        Print all JVM flags                  ║
║                                                                  ║
║  TOOLS:                                                          ║
║  jps    → List Java processes                                    ║
║  jstat  → GC statistics                                          ║
║  jmap   → Heap dump / memory map                                 ║
║  jstack → Thread dump                                            ║
║  jcmd   → JVM diagnostic commands                                ║
║  jinfo  → JVM configuration info                                 ║
║  javap  → Disassemble .class file                                ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: JVM Architecture, Class Loading, Memory Model, GC, JIT, Interview Questions*
