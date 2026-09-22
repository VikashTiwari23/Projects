# Java String & Immutable Classes - Complete Interview Notes

---

## 1. STRING OVERVIEW

```
    ┌──────────────────────────────────────────────────────────────┐
    │  String = Sequence of characters ("Hello", "Java", etc.)     │
    │                                                              │
    │  Key Facts:                                                  │
    │  ✅ String is a class (java.lang.String)                     │
    │  ✅ String is IMMUTABLE (cannot change after creation)       │
    │  ✅ String objects are stored in String Pool (heap)          │
    │  ✅ String implements Serializable, Comparable, CharSequence │
    │  ✅ String is final class (cannot be extended)               │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. STRING CREATION

```java
// ═══════════════════════════════════════════════════════════════
// TWO WAYS TO CREATE STRING
// ═══════════════════════════════════════════════════════════════

// 1. String Literal (uses String Pool)
String s1 = "Hello";
String s2 = "Hello";
System.out.println(s1 == s2);  // true (same reference in pool)

// 2. new String (creates new object in heap)
String s3 = new String("Hello");
String s4 = new String("Hello");
System.out.println(s3 == s4);  // false (different objects)
System.out.println(s3.equals(s4));  // true (same content)
```

### 2.1 String Pool Visual

```
    String Pool (in Heap):
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  String s1 = "Hello";                                       │
    │  String s2 = "Hello";                                       │
    │                                                              │
    │  s1 ──────┐                                                  │
    │           ▼                                                  │
    │        ┌──────┐     ┌──────┐                                 │
    │  s2 ───┤      │     │      │                                 │
    │        │"Hello"│     │      │  ← Only ONE "Hello" in pool   │
    │        └──────┘     └──────┘                                 │
    │                                                              │
    │  String s3 = new String("Hello");                            │
    │                                                              │
    │  s3 ──────────────────────────────┐                          │
    │                                   ▼                          │
    │                              ┌──────┐                        │
    │                              │"Hello"│  ← NEW object in heap│
    │                              └──────┘                        │
    │                                                              │
    │  Pool: 1 object        Heap: 1 object                       │
    └──────────────────────────────────────────────────────────────┘
```

---

## 3. STRING IMMUTABILITY

```
    ┌──────────────────────────────────────────────────────────────┐
    │  IMMOVABLE = Once created, CANNOT be modified                │
    │                                                              │
    │  Why?                                                        │
    │  1. Security: Strings used in class loading, network, etc.   │
    │  2. Thread Safety: Immutable objects are thread-safe         │
    │  3. Caching: String Pool works because strings don't change  │
    │  4. Performance: Hash code can be cached                     │
    └──────────────────────────────────────────────────────────────┘
```

```java
// ═══════════════════════════════════════════════════════════════
// STRING IS IMMUTABLE
// ═══════════════════════════════════════════════════════════════
String s = "Hello";
s.concat(" World");      // Returns NEW string "Hello World"
System.out.println(s);   // Still "Hello" (unchanged!)

// This is what happens:
// 1. "Hello World" created
// 2. Assigned to NEW reference (not s)
// 3. s still points to "Hello"

String s2 = s.concat(" World");  // Now s2 = "Hello World"
System.out.println(s);           // "Hello" (unchanged)
System.out.println(s2);          // "Hello World"
```

---

## 4. STRING METHODS

```java
String s = "Hello World";

// ═══════════════════════════════════════════════════════════════
// LENGTH & CHARACTERS
// ═══════════════════════════════════════════════════════════════
s.length();              // 11
s.charAt(0);             // 'H'
s.charAt(6);             // 'W'

// ═══════════════════════════════════════════════════════════════
// SEARCH
// ═══════════════════════════════════════════════════════════════
s.indexOf('o');          // 4
s.lastIndexOf('o');      // 7
s.indexOf("World");     // 6
s.contains("World");    // true
s.startsWith("Hello");  // true
s.endsWith("World");    // true

// ═══════════════════════════════════════════════════════════════
// EXTRACT
// ═══════════════════════════════════════════════════════════════
s.substring(6);          // "World"
s.substring(0, 5);       // "Hello"
s.substring(6, 11);      // "World"

// ═══════════════════════════════════════════════════════════════
// CASE
// ═══════════════════════════════════════════════════════════════
s.toUpperCase();         // "HELLO WORLD"
s.toLowerCase();         // "hello world"

// ═══════════════════════════════════════════════════════════════
// TRIM & STRIP
// ═══════════════════════════════════════════════════════════════
"  Hello  ".trim();      // "Hello" (removes spaces)
"  Hello  ".strip();     // "Hello" (Java 11+, removes all whitespace)

// ═══════════════════════════════════════════════════════════════
// REPLACE
// ═══════════════════════════════════════════════════════════════
s.replace('o', '0');     // "Hell0 W0rld"
s.replace("Hello", "Hi"); // "Hi World"
s.replaceAll("[aeiou]", "*"); // "H*ll* W*rld"

// ═══════════════════════════════════════════════════════════════
// SPLIT & JOIN
// ═══════════════════════════════════════════════════════════════
s.split(" ");            // ["Hello", "World"]
"a,b,c".split(",");     // ["a", "b", "c"]

String.join("-", "2026", "09", "22");  // "2026-09-22"
String.join(" ", "Hello", "World");    // "Hello World"

// ═══════════════════════════════════════════════════════════════
// COMPARE
// ═══════════════════════════════════════════════════════════════
s.equals("Hello World");       // true (content comparison)
s.equalsIgnoreCase("hello world"); // true (case-insensitive)
s.compareTo("Hello World");   // 0 (lexicographic comparison)
s.compareTo("ABC");           // positive (H > A)

// ═══════════════════════════════════════════════════════════════
// CHECK EMPTY/BLANK
// ═══════════════════════════════════════════════════════════════
"".isEmpty();            // true
"  ".isEmpty();          // false
"  ".isBlank();          // true (Java 11+)

// ═══════════════════════════════════════════════════════════════
// CONVERSION
// ═══════════════════════════════════════════════════════════════
s.toCharArray();          // char array
String.valueOf(123);     // "123"
String.valueOf(3.14);    // "3.14"
String.valueOf(true);    // "true"
Integer.parseInt("123"); // 123
Double.parseDouble("3.14"); // 3.14
```

---

## 5. STRINGBUILDER & STRINGBUFFER

```
    ┌──────────────────────────────────────────────────────────────┐
    │  String = IMMUTABLE (slow for modifications)                 │
    │  StringBuilder = MUTABLE (fast, NOT thread-safe)            │
    │  StringBuffer = MUTABLE (fast, THREAD-SAFE)                 │
    └──────────────────────────────────────────────────────────────┘

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  String: "Hello" → "Hello World" → NEW object created!      │
    │           [H][e][l][l][o]    [H][e][l][l][o][ ][W][o][r][l][d]│
    │                                                              │
    │  StringBuilder: "Hello" → "Hello World" → SAME object!      │
    │           [H][e][l][l][o][ ][W][o][r][l][d]  (modified)     │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

```java
// ═══════════════════════════════════════════════════════════════
// STRING BUILDER (Fast string manipulation)
// ═══════════════════════════════════════════════════════════════
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World");     // "Hello World"
sb.append("!");          // "Hello World!"
sb.insert(5, ",");       // "Hello, World!"
sb.delete(5, 6);         // "Hello World!"
sb.replace(6, 11, "Java"); // "Hello Java!"
sb.reverse();            // "avaJ olleH"
sb.toString();           // Convert to String
sb.length();             // 11
sb.charAt(0);            // 'a' (after reverse)

// ═══════════════════════════════════════════════════════════════
// STRING BUFFER (Thread-safe version)
// ═══════════════════════════════════════════════════════════════
StringBuffer sbf = new StringBuffer("Hello");
sbf.append(" World");
sbf.toString();
// Same methods as StringBuilder, but synchronized (thread-safe)
```

---

## 6. STRING vs STRINGBUILDER vs STRINGBUFFER

```
╔════════════════════╦═══════════════════╦═══════════════════╦═══════════════════╗
║ Feature            ║ String            ║ StringBuilder     ║ StringBuffer      ║
╠════════════════════╬═══════════════════╬═══════════════════╬═══════════════════╣
║ Mutability         ║ Immutable         ║ Mutable           ║ Mutable           ║
║ Thread Safety      ║ Yes (immutable)   ║ No                ║ Yes (synchronized)║
║ Performance        ║ Slow (creates new)║ Fastest           ║ Fast              ║
║ Use Case           ║ Constant strings  ║ Single-threaded   ║ Multi-threaded    ║
║ Memory             ║ More objects      ║ Less              ║ Less              ║
║ Pool               ║ Yes               ║ No                ║ No                ║
╚════════════════════╩═══════════════════╩═══════════════════╩═══════════════════╝

    When to use:
    ┌──────────────────────────────────────────────────────────────┐
    │  String:        Constant strings, minimal modifications     │
    │  StringBuilder: Single-threaded string building (preferred) │
    │  StringBuffer:  Multi-threaded string building              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 7. STRING POOL DEEP DIVE

```java
// ═══════════════════════════════════════════════════════════════
// STRING POOL BEHAVIOR
// ═══════════════════════════════════════════════════════════════

// Same literal → same object in pool
String a = "Hello";
String b = "Hello";
System.out.println(a == b);  // true

// new keyword → new object in heap
String c = new String("Hello");
System.out.println(a == c);  // false

// intern() → returns pool reference
String d = c.intern();
System.out.println(a == d);  // true

// Concatenation with literals → pooled
String e = "Hello" + " World";  // Compiler optimizes
System.out.println(e == "Hello World");  // true

// Concatenation with variables → NOT pooled
String f = "Hello";
String g = f + " World";  // Variable involved
System.out.println(g == "Hello World");  // false
```

---

## 8. IMMOVABLE CLASSES

```
    ┌──────────────────────────────────────────────────────────────┐
    │  IMMUTABLE CLASS = Object that CANNOT be modified            │
    │  after creation.                                             │
    │                                                              │
    │  Built-in Immutable Classes:                                 │
    │  ✅ String                                                   │
    │  ✅ Integer, Long, Double, Float, etc. (Wrapper classes)     │
    │  ✅ LocalDate, LocalTime, LocalDateTime                      │
    │  ✅ BigDecimal, BigInteger                                   │
    │  ✅ Color, Point (JavaFX)                                    │
    └──────────────────────────────────────────────────────────────┘
```

```java
// ═══════════════════════════════════════════════════════════════
// HOW TO CREATE CUSTOM IMMUTABLE CLASS
// ═══════════════════════════════════════════════════════════════
public final class Money {           // 1. Make class final
    private final double amount;     // 2. Make fields final
    private final String currency;

    public Money(double amount, String currency) {  // 3. Constructor
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {      // 4. Only getters (no setters)
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    // No setter methods!
}

// Usage
Money m1 = new Money(100.0, "USD");
// m1.amount = 200;  // COMPILE ERROR! (final field)
// m1.setAmount(200); // COMPILE ERROR! (no setter)
```

---

## 9. STRING CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║              STRING CHEAT SHEET                                  ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  CREATION:                                                       ║
║  String s = "Hello";              (literal - uses pool)          ║
║  String s = new String("Hello");  (new object - heap)            ║
║                                                                  ║
║  POOL:                                                           ║
║  s.intern()   → Returns reference from pool                     ║
║  == compares references                                          ║
║  .equals() compares content                                      ║
║                                                                  ║
║  COMMON METHODS:                                                 ║
║  length(), charAt(), indexOf(), lastIndexOf()                   ║
║  substring(), toUpperCase(), toLowerCase()                       ║
║  contains(), startsWith(), endsWith()                            ║
║  replace(), replaceAll(), split(), join()                        ║
║  equals(), equalsIgnoreCase(), compareTo()                       ║
║  trim(), strip(), isEmpty(), isBlank()                           ║
║  toCharArray(), valueOf()                                        ║
║                                                                  ║
║  STRING vs STRINGBUILDER vs STRINGBUFFER:                        ║
║  String:        Immutable, slow for changes                      ║
║  StringBuilder: Mutable, fastest, NOT thread-safe               ║
║  StringBuffer:  Mutable, fast, thread-safe                      ║
║                                                                  ║
║  IMMUTABLE CLASS RULES:                                          ║
║  1. Make class final                                             ║
║  2. Make fields final                                           ║
║  3. No setter methods                                           ║
║  4. Return copies of mutable objects (not originals)            ║
║                                                                  ║
║  TIPS:                                                           ║
║  ✅ Use StringBuilder for string concatenation in loops          ║
║  ✅ Use .equals() for content comparison (not ==)                ║
║  ✅ Use String Pool (literal) for repeated strings               ║
║  ✅ Use intern() to add to pool                                  ║
║  ⚠️  Avoid == for string comparison (unless interned)            ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 10. STRING INTERVIEW QUESTIONS (40+)

### ⭐ BASIC

**Q1: What is String Pool?**
> Special memory area in Heap where String literals are stored. Ensures same literal is not created multiple times (saves memory).

**Q2: What is difference between == and .equals()?**
```
==:      Compares object references (memory address)
.equals(): Compares object content (actual value)
Always use .equals() for String comparison!
```

**Q3: Why is String immutable?**
> 1. Security (used in networking, class loading)
> 2. Thread safety (no synchronization needed)
> 3. String Pool works (values don't change)
> 4. Hash code caching (faster HashMap operations)

**Q4: What is difference between String and StringBuilder?**
```
String:      Immutable (creates new object for changes)
StringBuilder: Mutable (modifies same object, faster)
```

**Q5: What is difference between StringBuilder and StringBuffer?**
```
StringBuilder: Not thread-safe (faster)
StringBuffer:  Thread-safe (synchronized, slower)
Use StringBuilder unless multi-threaded
```

**Q6: How many objects are created?**
```java
String s = "Hello";                    // 1 (pool)
String s2 = new String("Hello");       // 2 (pool + heap)
String s3 = "Hello" + "World";        // 1 (pool, compiler optimizes)
String s4 = "Hello" + s;             // 3 (pool + heap)
```

**Q7: What is intern()?**
> Returns reference from String Pool. If not in pool, adds it and returns reference.

**Q8: What is the difference between String, StringBuffer, StringBuilder?**
```
String:        Immutable, slow changes
StringBuffer:  Mutable, thread-safe
StringBuilder: Mutable, fastest
```

**Q9: What is the difference between char array and String?**
```
char[]: Mutable, not stored in pool, faster modifications
String: Immutable, stored in pool, secure
```

**Q10: What is the difference between String and CharSequence?**
```
String: Concrete class, immutable
CharSequence: Interface, implemented by String, StringBuilder, StringBuffer
```

---

### ⭐⭐ MIDDLE

**Q11: How many objects are created in this code?**
```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = new String("Hello");
String s4 = "Hel" + "lo";
String s5 = "Hel" + s1;
```
```
Answer: 4 objects (pool: "Hello", heap: new String("Hello"), heap: s5)
```

**Q12: What is the difference between String and Immutable classes?**
```
String:       Built-in immutable class
Immutable:    Any class that cannot be modified after creation
All Strings are immutable, but not all immutable classes are Strings
```

**Q13: What is the difference between String and text blocks?**
```
String:     Single line, use \n for newlines
Text Block: Multi-line (Java 13+), use triple quotes """
```

**Q14: What is the difference between String and StringBuffer performance?**
```
String: Slower (creates new objects for modifications)
StringBuffer: Faster (modifies same object)
String is 6x slower than StringBuffer for concatenation
```

**Q15: What is the difference between String and StringBuilder in loops?**
```
String: Creates multiple objects (slow)
StringBuilder: Modifies same object (fast)
Always use StringBuilder for loops!
```

**Q16: What is the difference between equals() and compareTo()?**
```
equals(): Returns boolean (equal or not)
compareTo(): Returns int (less than, equal, greater than)
Both compare content
```

**Q17: What is the difference between trim() and strip()?**
```
trim():   Removes leading/trailing spaces (ASCII only)
strip():  Removes all Unicode whitespace (Java 11+)
```

**Q18: What is the difference between isEmpty() and isBlank()?**
```
isEmpty():  Returns true if length() == 0
isBlank():  Returns true if all characters are whitespace (Java 11+)
```

**Q19: What is the difference between substring() and split()?**
```
substring(): Extracts part of string
split(): Splits string into array based on delimiter
```

**Q20: What is the difference between replace() and replaceAll()?**
```
replace(): Replaces literal characters
replaceAll(): Replaces using regex (regular expression)
```

---

### ⭐⭐⭐ ADVANCED

**Q21: What is the difference between String and StringBuffer thread safety?**
```
String: Thread-safe (immutable, no changes possible)
StringBuffer: Thread-safe (synchronized methods)
StringBuilder: NOT thread-safe (no synchronization)
```

**Q22: What is the difference between String and char[]?**
```
String: Immutable, stored in pool, secure
char[]: Mutable, not in pool, not secure
char[] is preferred for passwords (can be zeroed out)
```

**Q23: What is the difference between String and byte[]?**
```
String: Character sequence
byte[]: Raw bytes
String.getBytes() → byte[]
new String(bytes) → String
```

**Q24: What is the difference between String and ByteBuffer?**
```
String: Character sequence (text)
ByteBuffer: Byte buffer (binary data)
Use ByteBuffer for I/O, String for text
```

**Q25: What is the difference between String and Optional?**
```
String: Can be null
Optional: Cannot be null (wrapper)
Use Optional to avoid NullPointerException
```

**Q26: What is the difference between String and Duration?**
```
String: Text representation
Duration: Time amount (hours, minutes, seconds)
Duration.parse("PT1H") → 1 hour
```

**Q27: What is the difference between String and Instant?**
```
String: Text representation
Instant: Specific point in time (timestamp)
Instant.now() → current timestamp
```

**Q28: What is the difference between String and LocalDate?**
```
String: Text representation
LocalDate: Date without time (2026-09-22)
LocalDate.parse("2026-09-22")
```

**Q29: What is the difference between String and UUID?**
```
String: Manual creation
UUID: Universally unique identifier
UUID.randomUUID() → random unique ID
```

**Q30: What is the difference between String and Regex?**
```
String: Raw text
Regex: Pattern matching
String.matches("[0-9]+") → checks if all digits
```

**Q31: What is the difference between String and Pattern?**
```
String: Text
Pattern: Compiled regex (faster for repeated use)
Pattern.compile("[0-9]+").matcher(s).matches()
```

**Q32: What is the difference between String and Matcher?**
```
String: Text
Matcher: Regex matching engine
Pattern.matcher(input) → Matcher object
```

**Q33: What is the difference between String and Format?**
```
String: Raw text
Format: String formatting
String.format("Name: %s, Age: %d", "Amit", 25)
```

**Q34: What is the difference between String and MessageFormat?**
```
String: Simple formatting
MessageFormat: Complex formatting with patterns
MessageFormat.format("Hello {0}!", "World")
```

**Q35: What is the difference between String and TextProcessor?**
```
String: Static text
TextProcessor: Dynamic text building
```

**Q36: What is the difference between String and StringBuilder capacity?**
```
String: Fixed size (immutable)
StringBuilder: Dynamic capacity (grows automatically)
Initial capacity: 16, doubles when full
```

**Q37: What is the difference between String and StringBuffer capacity?**
```
String: Fixed size
StringBuffer: Dynamic capacity
Same as StringBuilder but thread-safe
```

**Q38: What is the difference between String and text blocks (Java 13+)?**
```
String: Single line, use \n
Text Block: Multi-line, use """
Text Block preserves formatting
```

**Q39: What is the difference between String and records (Java 14+)?**
```
String: Mutable reference (immutable content)
Record: Immutable class (auto-generated constructor, getters)
```

**Q40: What is the difference between String and sealed classes (Java 17)?**
```
String: Final class (cannot be extended)
Sealed: Restricts which classes can extend
```

---

*Last Updated: September 2026*
*Covers: String, String Pool, Immutability, StringBuilder, StringBuffer, Interview Questions*
