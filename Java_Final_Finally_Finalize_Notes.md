# Java Final, Finally, Finalize - Complete Interview Notes

---

## 1. FINAL, FINALLY, FINALIZE OVERVIEW

```
    ┌──────────────────────────────────────────────────────────────┐
    │  3 words that sound similar but have VERY different meanings:│
    │                                                              │
    │  FINAL     = Keyword (constant, no inheritance/overriding)   │
    │  FINALLY   = Block (always executes, cleanup code)           │
    │  FINALIZE  = Method (GC cleanup, deprecated)                 │
    └──────────────────────────────────────────────────────────────┘

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  final    →修饰符 (modifier) → Used with variable/method/class│
    │  finally  →代码块 (block) → Used with try-catch              │
    │  finalize →方法 (method) → Called by GC before destruction    │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. FINAL KEYWORD

```
    ┌──────────────────────────────────────────────────────────────┐
    │  FINAL = "Cannot be changed"                                 │
    │                                                              │
    │  3 Uses:                                                     │
    │  1. final variable   → Constant (value cannot change)        │
    │  2. final method     → Cannot be overridden                   │
    │  3. final class      → Cannot be extended                     │
    └──────────────────────────────────────────────────────────────┘
```

### 2.1 Final Variable

```java
// ═══════════════════════════════════════════════════════════════
// FINAL VARIABLE (Constant)
// ═══════════════════════════════════════════════════════════════
final int MAX = 100;
MAX = 200;  // COMPILE ERROR! Cannot reassign

// Final with reference
final int[] arr = {1, 2, 3};
arr[0] = 10;     // OK! (modifying content)
arr = new int[5]; // COMPILE ERROR! (reassigning reference)

// Final with object
final Student s = new Student("Amit");
s.name = "Rahul";  // OK! (modifying object)
s = new Student("Rahul"); // COMPILE ERROR! (reassigning reference)

// Final with blank variable
final int x;
x = 10;  // OK (initialize once)
x = 20;  // COMPILE ERROR!
```

### 2.2 Final Variable Visual

```
    Final Reference:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  final Student s = new Student("Amit");                      │
    │                                                              │
    │  s ──────────┐                                               │
    │              ▼                                               │
    │         ┌──────────┐                                         │
    │         │ Student   │                                         │
    │         │ name:Amit │                                         │
    │         └──────────┘                                         │
    │                                                              │
    │  s = new Student("Rahul");  ← COMPILE ERROR!                │
    │  (Cannot change WHERE s points)                              │
    │                                                              │
    │  s.name = "Rahul";           ← OK!                          │
    │  (Can change WHAT s points to)                               │
    └──────────────────────────────────────────────────────────────┘
```

### 2.3 Final Method

```java
// ═══════════════════════════════════════════════════════════════
// FINAL METHOD (Cannot be overridden)
// ═══════════════════════════════════════════════════════════════
class Parent {
    final void display() {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    void display() {  // COMPILE ERROR! Cannot override final method
        System.out.println("Child");
    }
}
```

### 2.4 Final Class

```java
// ═══════════════════════════════════════════════════════════════
// FINAL CLASS (Cannot be extended)
// ═══════════════════════════════════════════════════════════════
final class Parent {
    // ...
}

class Child extends Parent {  // COMPILE ERROR! Cannot extend final class
    // ...
}

// Built-in final classes: String, Integer, Long, etc.
```

### 2.5 Final Summary

```
╔══════════════════════════════════════════════════════════════════╗
║                 FINAL KEYWORD SUMMARY                            ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  final VARIABLE:                                                 ║
║  ✅ Cannot reassign value                                        ║
║  ✅ Can modify object content (if reference)                     ║
║  ✅ Must initialize (constructor or declaration)                 ║
║                                                                  ║
║  final METHOD:                                                   ║
║  ✅ Cannot be overridden in subclass                             ║
║  ✅ Can still be overloaded                                      ║
║                                                                  ║
║  final CLASS:                                                    ║
║  ✅ Cannot be extended (no subclasses)                           ║
║  ✅ All methods implicitly final                                 ║
║                                                                  ║
║  Benefits:                                                       ║
║  ✅ Security (prevent malicious modification)                    ║
║  ✅ Performance (JVM can optimize)                               ║
║  ✅ Thread safety (immutable)                                    ║
║  ✅ Clear intent (constant values)                               ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 3. FINALLY BLOCK

```
    ┌──────────────────────────────────────────────────────────────┐
    │  FINALLY = Code that ALWAYS executes                         │
    │                                                              │
    │  Used for cleanup: closing files, connections, releasing     │
    │  resources. Runs after try and catch blocks.                 │
    └──────────────────────────────────────────────────────────────┘
```

```java
// ═══════════════════════════════════════════════════════════════
// BASIC FINALLY
// ═══════════════════════════════════════════════════════════════
try {
    int result = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Caught: " + e.getMessage());
} finally {
    System.out.println("Finally block executed!");
}

// ═══════════════════════════════════════════════════════════════
// FINALLY WITHOUT CATCH
// ═══════════════════════════════════════════════════════════════
try {
    int result = 10 / 0;
} finally {
    System.out.println("This runs even without catch!");
}

// ═══════════════════════════════════════════════════════════════
// FINALLY WITH RETURN
// ═══════════════════════════════════════════════════════════════
public static int test() {
    try {
        return 1;
    } finally {
        System.out.println("Finally runs before return!");
        // This prints BEFORE method returns!
    }
}
// Output: Finally runs before return!
// Returns: 1
```

### 3.1 Finally Rules

```
    ┌──────────────────────────────────────────────────────────────┐
    │  FINALLY BLOCK RULES:                                        │
    │                                                              │
    │  ✅ ALWAYS executes (even with exception)                    │
    │  ✅ Executes after try and catch                             │
    │  ✅ Executes even if return in try/catch                     │
    │  ✅ Can have finally without catch (Java 7+)                 │
    │  ❌ Does NOT execute if System.exit() is called              │
    │  ❌ Does NOT execute if JVM crashes                          │
    │  ❌ Does NOT execute if thread is killed                     │
    │  ⚠️  Finally wins over return (if both have return)          │
    └──────────────────────────────────────────────────────────────┘
```

---

## 4. FINALIZE METHOD

```
    ┌──────────────────────────────────────────────────────────────┐
    │  FINALIZE = Called by Garbage Collector before destroying    │
    │             object. Used for cleanup.                        │
    │                                                              │
    │  ⚠️  DEPRECATED since Java 9!                                │
    │  ⚠️  Don't use in new code!                                  │
    │  Use try-with-resources or Cleaner instead.                  │
    └──────────────────────────────────────────────────────────────┘
```

```java
// ═══════════════════════════════════════════════════════════════
// FINALIZE METHOD
// ═══════════════════════════════════════════════════════════════
class MyClass {
    @Override
    protected void finalize() throws Throwable {
        try {
            // Cleanup code (close resources)
            System.out.println("Object destroyed: " + this);
        } finally {
            super.finalize();  // Always call super.finalize()
        }
    }
}

// Usage
MyClass obj = new MyClass();
obj = null;  // Object becomes eligible for GC
// finalize() called when GC runs (time unknown!)
```

### 4.1 Finalize Visual

```
    Object Lifecycle:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  1. Object Created (new)                                     │
    │     │                                                        │
    │     ▼                                                        │
    │  2. Object Used (referenced by variable)                     │
    │     │                                                        │
    │     ▼                                                        │
    │  3. Object eligible for GC (no more references)              │
    │     │                                                        │
    │     ▼                                                        │
    │  4. GC runs (time unknown!)                                  │
    │     │                                                        │
    │     ▼                                                        │
    │  5. finalize() called (BEFORE destruction)                   │
    │     │                                                        │
    │     ▼                                                        │
    │  6. Object destroyed (memory freed)                          │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

### 4.2 Why Finalize is Deprecated

```
    ┌──────────────────────────────────────────────────────────────┐
    │  PROBLEMS WITH finalize():                                   │
    │                                                              │
    │  1. Unpredictable: GC timing unknown                        │
    │  2. Performance: Slow (objects with finalize are slower)     │
    │  3. Resurrection: Can revive dead object!                   │
    │  4. Thread safety: Runs in finalizer thread                 │
    │  5. No guarantee: May never run (JVM exits)                 │
    │                                                              │
    │  BETTER ALTERNATIVES:                                        │
    │  ✅ try-with-resources (AutoCloseable)                       │
    │  ✅ Cleaner class (Java 9+)                                  │
    │  ✅ Explicit close() method                                  │
    └──────────────────────────────────────────────────────────────┘
```

---

## 5. FINAL vs FINALLY vs FINALIZE

```
╔════════════════════╦════════════════════╦════════════════════╦════════════════════╗
║ Feature            ║ final              ║ finally            ║ finalize           ║
╠════════════════════╬════════════════════╬════════════════════╬════════════════════╣
║ Type               ║ Keyword            ║ Block              ║ Method             ║
║ Purpose            ║ Prevent change     ║ Always execute     ║ GC cleanup         ║
║ Used With          ║ var/method/class   ║ try-catch          ║ Object             ║
║ When               ║ Compile-time       ║ Runtime            ║ GC time            ║
║ Default            ║ No                 ║ No                 ║ Yes (Object class) ║
║ Override           ║ No                 ║ N/A                ║ Yes                ║
║ Status             ║ Active             ║ Active             ║ Deprecated (Java 9)║
╚════════════════════╩════════════════════╩════════════════════╩════════════════════╝
```

---

## 6. CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║         FINAL / FINALLY / FINALIZE CHEAT SHEET                   ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  FINAL KEYWORD:                                                  ║
║  final int MAX = 100;      → Constant (cannot change)           ║
║  final void display() {}   → Cannot be overridden               ║
║  final class MyClass {}    → Cannot be extended                 ║
║                                                                  ║
║  FINALLY BLOCK:                                                  ║
║  try { } catch { } finally { }  → Always runs                  ║
║  ✅ Executes even with exception                                ║
║  ✅ Executes even with return                                   ║
║  ❌ Not with System.exit()                                      ║
║                                                                  ║
║  FINALIZE METHOD:                                                ║
║  protected void finalize() {}  → Called by GC                   ║
║  ⚠️  DEPRECATED (don't use!)                                    ║
║  Use try-with-resources instead                                  ║
║                                                                  ║
║  KEY DIFFERENCES:                                                ║
║  final: Prevents modification (variable/method/class)            ║
║  finally: Always executes (cleanup code)                         ║
║  finalize: GC cleanup (deprecated)                               ║
║                                                                  ║
║  BEST PRACTICES:                                                 ║
║  ✅ Use final for constants                                     ║
║  ✅ Use final methods for security                              ║
║  ✅ Use finally for cleanup (but prefer try-with-resources)     ║
║  ✅ NEVER use finalize() (use Cleaner or close())               ║
║  ✅ Use final class for immutable objects                       ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 7. INTERVIEW QUESTIONS (30+)

### ⭐ BASIC

**Q1: What is final keyword?**
> Prevents modification. Can be used with variables (constant), methods (no override), and classes (no extension).

**Q2: What is finally block?**
> Code that ALWAYS executes after try-catch, used for cleanup. Even runs with exceptions and return statements.

**Q3: What is finalize method?**
> Called by Garbage Collector before destroying object. Deprecated in Java 9. Don't use in new code.

**Q4: What is difference between final, finally, finalize?**
```
final:     Keyword (constant, no override/extend)
finally:   Block (always executes)
finalize:  Method (GC cleanup, deprecated)
```

**Q5: Can we override final method?**
> No! Compiler error. Final methods cannot be overridden.

**Q6: Can we extend final class?**
> No! Compiler error. Final classes cannot have subclasses.

**Q7: Can finally block have return?**
> Yes, but it's bad practice! Finally's return wins over try/catch return.

**Q8: What happens if finally and try both return?**
> Finally's return wins:
```java
try { return 1; } finally { return 2; }  // Returns 2
```

**Q9: Can we have try without finally or catch?**
> Yes (Java 7+): `try { } finally { }`

**Q10: When does finally NOT execute?**
> When System.exit() is called, JVM crashes, or thread is killed.

---

### ⭐⭐ MIDDLE

**Q11: Can final variable be initialized in constructor?**
> Yes! Blank final variables must be initialized in constructor:
```java
final int x;
MyClass() { x = 10; }  // OK
```

**Q12: Can final variable be static?**
> Yes! Static final = class constant:
```java
static final int MAX = 100;
```

**Q13: Can final method be overloaded?**
> Yes! Final methods can be overloaded, just not overridden.

**Q14: Can final class have methods?**
> Yes! Final class can have methods, just cannot be extended.

**Q15: Can finalize method be called manually?**
> Yes, but don't! It's just a regular method. GC calls it automatically.

**Q16: Can finally block throw exception?**
> Yes, but it will override any exception from try/catch.

**Q17: Can we have multiple finally blocks?**
> No! Only one finally block per try.

**Q18: Can final variable be null?**
> Yes! Final reference can be null (just can't be reassigned).

**Q19: What is blank final variable?**
> Final variable declared without value, must be initialized in constructor:
```java
final int x;  // Blank final
```

**Q20: What is the difference between final and immutable?**
```
final: Cannot reassign reference
immutable: Cannot modify object content
String is both final AND immutable
```

---

### ⭐⭐⭐ ADVANCED

**Q21: What is the difference between final and effectively final?**
```
final: Explicitly declared with final keyword
effectively final: Never reassigned (even without final keyword)
Lambda variables must be effectively final
```

**Q22: Can finalize method resurrect object?**
> Yes! By assigning `this` to a static variable:
```java
static Object resurrection;
protected void finalize() {
    resurrection = this;  // Revives object!
}
```

**Q23: What is the performance impact of final?**
> JVM can optimize final variables (inlining constants, faster access).

**Q24: Can final variable be serialized?**
> Yes, but value may change during deserialization (special handling).

**Q25: What is the difference between final and volatile?**
```
final: Cannot modify (constant)
volatile: Can be modified (thread-safe, always reads from main memory)
```

**Q26: Can final variable be cloned?**
> Yes, but clone creates new object (original unchanged).

**Q27: What is the difference between final and static final?**
```
final: Instance constant (each object has own copy)
static final: Class constant (shared by all objects)
```

**Q28: Can final class implement interface?**
> Yes! Final class can implement interfaces, just cannot be extended.

**Q29: Can final method be synchronized?**
> Yes! Final methods can be synchronized.

**Q30: What is the difference between finalize and close?**
```
finalize: Called by GC (unpredictable, deprecated)
close: Manual cleanup (predictable, preferred)
Use close() or try-with-resources instead of finalize()
```

**Q31: Can finally block modify return value?**
> Yes! If return is primitive, finally can't change it. But if return is object, finally can modify object's fields.

**Q32: What is the difference between final and readonly?**
> Java doesn't have readonly keyword. final is Java's equivalent.

**Q33: Can final variable be volatile?**
> No! Final and volatile have opposite meanings (constant vs. always from main memory).

**Q34: Can final class be abstract?**
> No! Abstract class must be extended, but final class cannot be extended. Contradiction!

**Q35: What is the difference between finally and finalization?**
```
finally: Code block (always executes)
finalization: Process of calling finalize() (GC cleanup)
```

**Q36: Can finally block have catch?**
> No! Finally block cannot have catch. Only try can have catch.

**Q37: What is the difference between final and Immutable?**
```
final: Cannot change reference
Immutable: Cannot change object content
String: Both final AND immutable
```

**Q38: Can final variable be initialized in static block?**
> Yes! Static final variables can be initialized in static block:
```java
static final int x;
static { x = 10; }
```

**Q39: Can final class have static methods?**
> Yes! Final class can have static methods.

**Q40: What is the difference between final and constant?**
> Same concept! final = Java keyword for constants.

---

*Last Updated: September 2026*
*Covers: Final Keyword, Finally Block, Finalize Method, Interview Questions*
