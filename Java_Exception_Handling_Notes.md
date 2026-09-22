# Java Exception Handling - Complete Interview Notes

---

## 1. WHAT IS EXCEPTION?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Exception = Unexpected event that disrupts program flow     │
    │                                                              │
    │  Example:                                                    │
    │  - Divide by zero                                            │
    │  - File not found                                            │
    │  - Array index out of bounds                                 │
    │  - Network connection lost                                   │
    │  - Null pointer access                                       │
    └──────────────────────────────────────────────────────────────┘

    Without Exception Handling:
    ┌──────────────────────────────────────────────────────────────┐
    │  public static void main(String[] args) {                    │
    │      int a = 10 / 0;     // ArithmeticException!            │
    │      System.out.println("This line never runs");            │
    │      // Program CRASHES here!                                │
    │  }                                                           │
    └──────────────────────────────────────────────────────────────┘

    With Exception Handling:
    ┌──────────────────────────────────────────────────────────────┐
    │  public static void main(String[] args) {                    │
    │      try {                                                   │
    │          int a = 10 / 0;                                     │
    │      } catch (ArithmeticException e) {                       │
    │          System.out.println("Cannot divide by zero!");       │
    │      }                                                       │
    │      System.out.println("Program continues!");               │
    │  }                                                           │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. EXCEPTION HIERARCHY

```
                    ┌─────────────────┐
                    │   Throwable      │  (Root of all errors)
                    │  (java.lang)     │
                    └────────┬────────┘
                             │
              ┌──────────────┴──────────────┐
              │                              │
    ┌─────────▼─────────┐        ┌──────────▼──────────┐
    │      Error         │        │    Exception          │
    │  (Serious/System)  │        │  (Recoverable)        │
    └─────────┬─────────┘        └──────────┬──────────┘
              │                              │
    ┌─────────▼─────────┐        ┌──────────▼──────────┐
    │ StackOverflowError │        │ RuntimeException     │
    │ OutOfMemoryError   │        │  (Unchecked)         │
    │ IOError            │        └──────────┬──────────┘
    │ ThreadDeath        │                   │
    └───────────────────┘        ┌──────────▼──────────┐
                                 │ NullPointerException  │
                                 │ ArithmeticException   │
                                 │ ArrayIndexOutOfBounds │
                                 │ ClassCastException     │
                                 │ NumberFormatException │
                                 │ IOException            │
                                 └───────────────────────┘

    Checked vs Unchecked:
    ┌──────────────────────────────────────────────────────────────┐
    │  UNCHECKED (Runtime): RuntimeException and its subclasses    │
    │  → Compiler doesn't force you to handle                      │
    │  → Examples: NullPointerException, ArithmeticException       │
    │                                                              │
    │  CHECKED (Compile-time): Exception and its subclasses        │
    │  → Compiler FORCES you to handle with try-catch or throws    │
    │  → Examples: IOException, SQLException, FileNotFoundException│
    └──────────────────────────────────────────────────────────────┘
```

---

## 3. TRY-CATCH-FINALLY

```java
// ═══════════════════════════════════════════════════════════════
// BASIC TRY-CATCH
// ═══════════════════════════════════════════════════════════════
try {
    int result = 10 / 0;
    System.out.println(result);
} catch (ArithmeticException e) {
    System.out.println("Error: " + e.getMessage());
}

// ═══════════════════════════════════════════════════════════════
// MULTIPLE CATCH BLOCKS
// ═══════════════════════════════════════════════════════════════
try {
    int[] arr = {1, 2, 3};
    System.out.println(arr[5]);
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Index out of bounds");
} catch (Exception e) {
    System.out.println("Some other error");
}

// ═══════════════════════════════════════════════════════════════
// MULTI-CATCH (Java 7+)
// ═══════════════════════════════════════════════════════════════
try {
    // code
} catch (ArithmeticException | NullPointerException e) {
    // Handle both exceptions the same way
}

// ═══════════════════════════════════════════════════════════════
// TRY-CATCH-FINALLY
// ═══════════════════════════════════════════════════════════════
try {
    int result = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Caught: " + e.getMessage());
} finally {
    System.out.println("Finally block ALWAYS runs!");
}

// ═══════════════════════════════════════════════════════════════
// FINALLY WITHOUT CATCH
// ═══════════════════════════════════════════════════════════════
try {
    int result = 10 / 0;
} finally {
    System.out.println("This runs even without catch!");
}
```

---

## 4. FINALLY BLOCK RULES

```
    ┌──────────────────────────────────────────────────────────────┐
    │  FINALLY BLOCK:                                              │
    │                                                              │
    │  ✅ ALWAYS executes (even if exception occurs                │
    │  ✅ Executes even if return statement in try/catch           │
    │  ✅ Executes after try and catch blocks                      │
    │  ❌ Does NOT execute if System.exit() is called              │
    │  ❌ Does NOT execute if JVM crashes                          │
    │  ❌ Does NOT execute if thread is killed                     │
    └──────────────────────────────────────────────────────────────┘

    VISUAL:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  try { code }                                                │
    │  catch (Exception e) { handle }                              │
    │  finally { cleanup }  ← ALWAYS RUNS                         │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Example: Finally always runs
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

---

## 5. THROW vs THROWS

```java
// ═══════════════════════════════════════════════════════════════
// THROW - Manually throw an exception
// ═══════════════════════════════════════════════════════════════
public static void validateAge(int age) {
    if (age < 18) {
        throw new IllegalArgumentException("Age must be 18+");  // throw
    }
    System.out.println("Valid age!");
}

// Usage
try {
    validateAge(15);  // Throws exception
} catch (IllegalArgumentException e) {
    System.out.println(e.getMessage());  // Age must be 18+
}

// ═══════════════════════════════════════════════════════════════
// THROWS - Declare exception in method signature
// ═══════════════════════════════════════════════════════════════
public static void readFile(String path) throws IOException {  // throws
    BufferedReader br = new BufferedReader(new FileReader(path));
    // ...
}
```

---

## 6. CUSTOM EXCEPTIONS

```java
// ═══════════════════════════════════════════════════════════════
// CUSTOM CHECKED EXCEPTION
// ═══════════════════════════════════════════════════════════════
class InsufficientBalanceException extends Exception {
    private double amount;

    InsufficientBalanceException(double amount) {
        super("Insufficient balance. Balance: " + amount);
        this.amount = amount;
    }

    double getAmount() {
        return amount;
    }
}

// ═══════════════════════════════════════════════════════════════
// CUSTOM UNCHECKED EXCEPTION
// ═══════════════════════════════════════════════════════════════
class InvalidAgeException extends RuntimeException {
    InvalidAgeException(String message) {
        super(message);
    }
}

// ═══════════════════════════════════════════════════════════════
// USING CUSTOM EXCEPTIONS
// ═══════════════════════════════════════════════════════════════
class BankAccount {
    private double balance;

    void withdraw(double amount) throws InsufficientBalanceException {
        if (amount > balance) {
            throw new InsufficientBalanceException(balance);  // Checked!
        }
        balance -= amount;
    }
}

// Must handle checked exception
try {
    account.withdraw(10000);
} catch (InsufficientBalanceException e) {
    System.out.println(e.getMessage());
}
```

---

## 7. EXCEPTION METHODS

```java
// ═══════════════════════════════════════════════════════════════
// COMMON EXCEPTION METHODS
// ═══════════════════════════════════════════════════════════════
try {
    int result = 10 / 0;
} catch (Exception e) {
    e.getMessage();        // "/ by zero" (short description)
    e.toString();          // "java.lang.ArithmeticException: / by zero"
    e.getClass().getName(); // "java.lang.ArithmeticException"
    e.printStackTrace();   // Full stack trace (for debugging)
    e.getStackTrace();     // Array of StackTraceElement
    e.getCause();          // Root cause (if wrapped)
    e.initCause();         // Set root cause
    e.addSuppressed();     // Add suppressed exception
}
```

---

## 8. TRY-WITH-RESOURCES

```java
// ═══════════════════════════════════════════════════════════════
// AUTO-CLOSEABLE (Java 7+)
// ═══════════════════════════════════════════════════════════════
// Resources must implement AutoCloseable
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"));
     PrintWriter pw = new PrintWriter("output.txt")) {

    String line = br.readLine();
    pw.println(line);

}  // br and pw automatically closed here!

// ═══════════════════════════════════════════════════════════════
// MULTIPLE EXCEPTIONS
// ═══════════════════════════════════════════════════════════════
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    // code
} catch (IOException e) {
    e.printStackTrace();
}

// ═══════════════════════════════════════════════════════════════
// SUPPRESSED EXCEPTIONS
// ═══════════════════════════════════════════════════════════════
// If both try block and close() throw exceptions,
// close() exception is "suppressed"
try {
    throw new Exception("Original");
} catch (Exception e) {
    Throwable[] suppressed = e.getSuppressed();
    // suppressed[0] contains the exception from close()
}
```

---

## 9. COMMON EXCEPTIONS CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║                   COMMON EXCEPTIONS                              ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  UNCHECKED (Runtime):                                            ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │ NullPointerException     → obj = null; obj.method();       │  ║
║  │ ArithmeticException      → 10 / 0                         │  ║
║  │ ArrayIndexOutOfBounds    → arr[100] (size 5)              │  ║
║  │ ClassCastException       → (String) obj (not String)      │  ║
║  │ NumberFormatException    → Integer.parseInt("abc")        │  ║
║  │ IllegalArgumentException → bad method arguments           │  ║
║  │ IndexOutOfBoundsException→ general index error            │  ║
║  │ ConcurrentModification   → modify collection while iterate│  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  CHECKED (Compile-time):                                         ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │ IOException              → file/network operations        │  ║
║  │ FileNotFoundException    → file doesn't exist              │  ║
║  │ SQLException             → database errors                 │  ║
║  │ ClassNotFoundException   → class not found                 │  ║
║  │ InterruptedException     → thread interrupted              │  ║
║  │ MalformedURLException    → bad URL format                  │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  ERRORS (Don't handle):                                          ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │ StackOverflowError       → infinite recursion             │  ║
║  │ OutOfMemoryError         → heap full                      │  ║
║  │ IOError                  → system I/O failure             │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 10. EXCEPTION HANDLING CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║            EXCEPTION HANDLING CHEAT SHEET                        ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  SYNTAX:                                                         ║
║  try { code }                                                    ║
║  catch (ExceptionType e) { handle }                              ║
║  finally { cleanup }                                             ║
║                                                                  ║
║  KEYWORDS:                                                       ║
║  try    → Monitor code for exceptions                            ║
║  catch  → Handle the exception                                   ║
║  finally→ Always execute (cleanup)                               ║
║  throw  → Manually throw exception                               ║
║  throws → Declare exception in method signature                  ║
║                                                                  ║
║  RULES:                                                          ║
║  ✅ Multiple catch blocks (order: specific → general)            ║
║  ✅ Multi-catch: catch (A | B e)                                 ║
║  ✅ Try-with-resources for AutoCloseable                         ║
║  ✅ Always catch specific exceptions (not just Exception)        ║
║  ✅ Use finally for cleanup (closing resources)                  ║
║  ✅ Create custom exceptions for business logic                  ║
║                                                                  ║
║  BEST PRACTICES:                                                 ║
║  ✅ Don't catch Exception or Throwable (too broad)               ║
║  ✅ Don't swallow exceptions (empty catch block)                 ║
║  ✅ Log exceptions properly                                      ║
║  ✅ Use try-with-resources (don't manually close)                ║
║  ✅ Prefer unchecked exceptions for programming errors           ║
║  ✅ Use checked exceptions for recoverable conditions            ║
║  ✅ Always include message in custom exceptions                  ║
║  ✅ Use initCause() to chain exceptions                          ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 11. EXCEPTION HANDLING INTERVIEW QUESTIONS (40+)

### ⭐ BASIC

**Q1: What is Exception?**
> An unexpected event that disrupts normal program flow. It's an object that contains error information.

**Q2: What is difference between Error and Exception?**
```
Error: Serious system error (OutOfMemoryError, StackOverflowError)
       → Can't be handled, JVM crashes
Exception: Recoverable error (IOException, NullPointerException)
           → Can be handled with try-catch
```

**Q3: What is checked vs unchecked exception?**
```
Checked: Compile-time, must handle (IOException, SQLException)
Unchecked: Runtime, optional handling (NullPointerException, ArithmeticException)
```

**Q4: What is difference between throw and throws?**
```
throw:    Manually throw exception (inside method body)
throws:   Declare exception in method signature (after parameter list)
```

**Q5: What is the purpose of finally block?**
> Cleanup code that ALWAYS runs (closing files, connections, releasing resources).

**Q6: What happens if we don't handle checked exception?**
> Compile-time error! Compiler forces you to handle or declare it.

**Q7: What is difference between final, finally, and finalize?**
```
final:     Keyword for constants, no inheritance, no overriding
finally:   Block that always executes
finalize:  Called by GC before object is destroyed (deprecated)
```

**Q8: What is try-with-resources?**
> Automatically closes resources (streams, connections) after use. Resource must implement AutoCloseable.

**Q9: What is Multi-catch?**
> Catch multiple exception types in one catch block:
```java
catch (IOException | SQLException e) { }
```

**Q10: What is the difference between getMessage() and toString()?**
```
getMessage(): "File not found"
toString():   "java.io.FileNotFoundException: File not found"
```

---

### ⭐⭐ MIDDLE

**Q11: Can we have try without catch?**
> Yes, with finally block (Java 7+):
```java
try { } finally { }
```

**Q12: Can we have multiple finally blocks?**
> No, only one finally block per try.

**Q13: Can finally block prevent exception propagation?**
> Yes, if finally has return statement:
```java
try { return 1; } finally { return 2; }  // Returns 2!
```

**Q14: What is exception chaining?**
> Wrapping one exception inside another:
```java
catch (Exception e) {
    throw new RuntimeException("Error", e);
}
```

**Q15: What is the best order for multiple catch blocks?**
> Specific → General (child → parent):
```java
catch (FileNotFoundException e) { }    // Most specific
catch (IOException e) { }              // Less specific
catch (Exception e) { }                // Most general
```

**Q16: What is ConcurrentModificationException?**
> Thrown when collection is modified while iterating. Use Iterator or Concurrent collections.

**Q17: What is the difference between ClassNotFoundException and NoClassDefFoundError?**
```
ClassNotFoundException: Class not found at runtime (dynamic loading)
NoClassDefFoundError: Class definition changed after compilation
```

**Q18: Can we throw null?**
> No, NullPointerException is thrown:
```java
throw null;  // Compiles but throws NullPointerException
```

**Q19: What is the difference between catch and throws?**
```
catch: Handles exception locally
throws: Passes exception to caller method
```

**Q20: What happens if exception is not caught?**
> JVM's default handler prints stack trace and terminates program.

---

### ⭐⭐⭐ ADVANCED

**Q21: What are suppressed exceptions?**
> Exceptions thrown during try-with-resources close() method. Added to primary exception.

**Q22: What is the difference between initCause() and chaining?**
```
initCause(): Sets root cause of exception
Chaining: Wrapping exception in another
```

**Q23: What is the difference between Exception and Throwable?**
```
Throwable: Root of everything (Exception + Error)
Exception: Subset of Throwable (recoverable errors only)
```

**Q24: What is the best practice for custom exceptions?**
```
1. Extend appropriate parent (Exception or RuntimeException)
2. Provide multiple constructors
3. Include message and cause
4. Add serialVersionUID for checked exceptions
5. Keep it meaningful (don't use generic names)
```

**Q25: What is the difference between System.exit() and throw?**
```
System.exit(): Terminates JVM, finally NOT executed
throw: Passes control to catch block, finally executed
```

**Q26: What is the difference between assert and throw?**
```
assert: For debugging, disabled in production
throw: For error handling, always works
```

**Q27: What is the difference between catch (Exception e) and catch (Throwable t)?**
```
catch (Exception): Catches all exceptions
catch (Throwable): Catches all exceptions + errors (too broad!)
```

**Q28: What happens if finally and try both return?**
> finally's return wins:
```java
try { return 1; } finally { return 2; }  // Returns 2
```

**Q29: What is the difference between ClassNotFoundException and ClassCastException?**
```
ClassNotFoundException: Class not found (loading issue)
ClassCastException: Wrong class type (casting issue)
```

**Q30: What is the difference between IOException and FileNotFoundException?**
```
FileNotFoundException extends IOException
IOException: General I/O error
FileNotFoundException: Specific file not found
```

**Q31: What is the difference between RuntimeException and Exception?**
```
RuntimeException: Unchecked (programming errors)
Exception: Checked (recoverable conditions)
```

**Q32: What is the difference between error and exception in Java?**
```
Error: System-level, can't recover (JVM crashes)
Exception: Application-level, can recover
```

**Q33: What is the difference between throw and throws in Java?**
```
throw: Keyword to manually throw exception
throws: Keyword to declare exception in method signature
```

**Q34: What is the difference between try-catch and throws?**
```
try-catch: Handle exception locally
throws: Pass to caller method
```

**Q35: What is the difference between checked and unchecked exceptions?**
```
Checked: Compile-time, must handle (IOException)
Unchecked: Runtime, optional (NullPointerException)
```

**Q36: What is the difference between Exception and Error in Java?**
```
Exception: Recoverable, application error
Error: Unrecoverable, system error
```

**Q37: What is the difference between throw and throws in Java?**
```
throw: Manually throw exception
throws: Declare in method signature
```

**Q38: What is the difference between try-catch and throws in Java?**
```
try-catch: Handle exception locally
throws: Pass to caller
```

**Q39: What is the difference between checked and unchecked in Java?**
```
Checked: Compile-time (must handle)
Unchecked: Runtime (optional)
```

**Q40: What is the difference between exception and error in Java?**
```
Exception: Recoverable
Error: Unrecoverable
```

---

*Last Updated: September 2026*
*Covers: Exception Hierarchy, Try-Catch-Finally, Throw/Throws, Custom Exceptions, Interview Questions*
