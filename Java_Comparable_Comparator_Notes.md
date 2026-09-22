# Comparable vs Comparator - Complete Interview Notes

---

## 1. CORE CONCEPT

```
    Both define how objects are ORDERED/SORTED.
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  Comparable:  Defines NATURAL ordering (inside the class)    │
    │               One ordering per class                          │
    │               Class implements Comparable<T>                  │
    │                                                              │
    │  Comparator:  Defines CUSTOM ordering (outside the class)    │
    │               Multiple orderings possible                     │
    │               Separate Comparator object                      │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. COMPARABLE - NATURAL ORDERING

### 2.1 Interface Definition

```java
public interface Comparable<T> {
    public int compareTo(T o);
}

// Returns:
//   negative → this < o
//   zero     → this == o
//   positive → this > o
```

### 2.2 Implementation Example

```java
class Student implements Comparable<Student> {
    String name;
    int age;
    double marks;

    public Student(String name, int age, double marks) {
        this.name = name;
        this.age = age;
        this.marks = marks;
    }

    @Override
    public int compareTo(Student other) {
        // Natural ordering by age
        return this.age - other.age;
        // Negative: this < other
        // Zero:     this == other
        // Positive: this > other
    }
}
```

### 2.3 How compareTo Works

```
    Student s1 = new Student("Amit", 20, 85.5);
    Student s2 = new Student("Bob", 22, 90.0);

    s1.compareTo(s2):

    this.age = 20, other.age = 22
    return 20 - 22 = -2 (negative)

    Meaning: s1 < s2 (s1 comes before s2 in sorted order)
```

### 2.4 Common compareTo Patterns

```java
// Pattern 1: By single field (primitive)
public int compareTo(Student other) {
    return this.age - other.age;  // ⚠️ Risk of overflow with large values
}

// Pattern 2: By single field (safe)
public int compareTo(Student other) {
    return Integer.compare(this.age, other.age);  // ✅ Safe
}

// Pattern 3: By multiple fields
public int compareTo(Student other) {
    int result = Integer.compare(this.age, other.age);
    if (result != 0) return result;

    result = Double.compare(this.marks, other.marks);
    if (result != 0) return result;

    return this.name.compareTo(other.name);
}

// Pattern 4: Strings
public int compareTo(Student other) {
    return this.name.compareTo(other.name);  // Lexicographic
}
```

### 2.5 Comparator Pattern for Primitives

```
    ⚠️ DANGER: this.age - other.age OVERFLOW

    this.age = Integer.MAX_VALUE;
    other.age = -1;
    this.age - other.age = 2147483647 - (-1) = OVERFLOW! → negative!

    Safe alternatives:
    ┌──────────────────────────────────────────────────────────────┐
    │  Integer.compare(this.age, other.age)     ✅ Safe            │
    │  Integer.valueOf(this.age).compareTo(other.age) ✅ Safe     │
    │  Comparator.comparingInt(Student::getAge) ✅ Safe (Java 8+) │
    └──────────────────────────────────────────────────────────────┘
```

---

## 3. COMPARATOR - CUSTOM ORDERING

### 3.1 Interface Definition

```java
public interface Comparator<T> {
    int compare(T o1, T o2);
}

// Returns:
//   negative → o1 < o2
//   zero     → o1 == o2
//   positive → o1 > o2
```

### 3.2 Implementation Examples

```java
// Method 1: Separate class
class StudentAgeComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return Integer.compare(s1.age, s2.age);
    }
}

// Method 2: Anonymous class
Comparator<Student> comp = new Comparator<Student>() {
    @Override
    public int compare(Student s1, Student s2) {
        return Double.compare(s1.marks, s2.marks);
    }
};

// Method 3: Lambda (Java 8+)
Comparator<Student> comp = (s1, s2) -> Integer.compare(s1.age, s2.age);

// Method 4: Method reference (Java 8+)
Comparator<Student> comp = Comparator.comparingInt(s -> s.age);
Comparator<Student> comp = Comparator.comparingInt(Student::getAge);
```

---

## 4. JAVA 8+ COMPARATOR METHODS

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Method                          │ Description                │
    ├──────────────────────────────────────────────────────────────┤
    │  Comparator.comparing(keyExtractor)│ Create from key function │
    │  Comparator.comparingInt(keyExtractor)│ For int keys          │
    │  Comparator.comparingLong(keyExtractor)│ For long keys        │
    │  Comparator.comparingDouble(keyExtractor)│ For double keys    │
    │                                                          │
    │  comp.reversed()                │ Reverse the comparator     │
    │  comp.thenComparing(other)      │ Chain comparators          │
    │  comp.thenComparingInt(fn)      │ Chain with int key         │
    │                                                          │
    │  Comparator.naturalOrder()      │ Natural ordering            │
    │  Comparator.reverseOrder()      │ Reverse natural ordering    │
    │  Comparator.nullsFirst(comp)    │ Nulls before non-nulls      │
    │  Comparator.nullsLast(comp)     │ Nulls after non-nulls       │
    │                                                          │
    │  Comparator.identityHashCode()  │ Compare by identity hash   │
    └──────────────────────────────────────────────────────────────┘
```

### 4.1 Chaining Example

```java
class Student {
    String name;
    int age;
    double marks;
    String city;
}

// Sort by age, then by marks, then by name
Comparator<Student> comp = Comparator
    .comparingInt(Student::getAge)
    .thenComparingDouble(Student::getMarks)
    .thenComparing(Student::getName);

// Sort by city (reverse), then by age (natural)
Comparator<Student> comp2 = Comparator
    .comparing(Student::getCity)
    .reversed()
    .thenComparingInt(Student::getAge);
```

```
    Chaining visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Comparator.comparingInt(Student::getAge)                    │
    │      .thenComparingDouble(Student::getMarks)                 │
    │      .thenComparing(Student::getName);                       │
    │                                                              │
    │  Sort by: 1st → age                                         │
    │           2nd → marks (if age same)                          │
    │           3rd → name (if age AND marks same)                 │
    └──────────────────────────────────────────────────────────────┘
```

### 4.2 Null Handling

```java
List<String> list = Arrays.asList("Charlie", null, "Alice", null, "Bob");

// Nulls first
Collections.sort(list, Comparator.nullsFirst(Comparator.naturalOrder()));
// [null, null, Alice, Bob, Charlie]

// Nulls last
Collections.sort(list, Comparator.nullsLast(Comparator.naturalOrder()));
// [Alice, Bob, Charlie, null, null]
```

---

## 5. COMPARABLE VS COMPARATOR

```
    ┌──────────────────────┬─────────────────────────┬─────────────────────────┐
    │ Feature               │ Comparable              │ Comparator              │
    ├──────────────────────┼─────────────────────────┼─────────────────────────┤
    │ Package               │ java.lang               │ java.util               │
    │ Method                │ compareTo(T o)          │ compare(T o1, T o2)     │
    │ Where defined         │ Inside the class        │ Outside the class       │
    │ Modifies class        │ Yes (implements)        │ No (separate class)     │
    │ Number of orderings   │ 1 (natural ordering)    │ Multiple possible       │
    │ Parameter             │ 1 object                │ 2 objects               │
    │ Used by               │ Collections.sort(list)  │ Collections.sort(list, c)│
    │                      │ TreeSet, TreeMap         │ PriorityQueue          │
    │ Default               │ Class defines it        │ None                    │
    │ Override              │ compareTo()             │ compare()               │
    └──────────────────────┴─────────────────────────┴─────────────────────────┘

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Comparable (inside class):                                  │
    │  class Student implements Comparable<Student> {              │
    │      public int compareTo(Student other) { ... }            │
    │  }                                                          │
    │  ONE ordering per class                                      │
    │                                                              │
    │  Comparator (outside class):                                 │
    │  Comparator<Student> byAge = (s1, s2) -> s1.age - s2.age;   │
    │  Comparator<Student> byName = (s1, s2) -> s1.name.compareTo(s2.name); │
    │  Comparator<Student> byMarks = (s1, s2) -> Double.compare(s1.marks, s2.marks); │
    │  UNLIMITED orderings!                                        │
    └──────────────────────────────────────────────────────────────┘
```

---

## 6. WHEN TO USE WHAT

```
    ┌──────────────────────────────────────────────────────────────┐
    │  USE COMPARABLE when:                                        │
    │  ✅ There's ONE obvious natural ordering                     │
    │  ✅ The class should define its own ordering                 │
    │  ✅ You control the class source code                        │
    │  ✅ Examples: Integer, String, Date (already implement it)   │
    │                                                              │
    │  USE COMPARATOR when:                                        │
    │  ✅ You need MULTIPLE orderings                              │
    │  ✅ You don't control the class source code                  │
    │  ✅ You want to sort by different fields                     │
    │  ✅ You want to override natural ordering                     │
    │  ✅ Examples: Sort students by age, name, marks, etc.        │
    └──────────────────────────────────────────────────────────────┘
```

---

## 7. BUILT-IN COMPARABLE CLASSES

```
    Java provides Comparable for common classes:
    ┌──────────────────┬──────────────────────────────────────────┐
    │ Class             │ Natural Ordering                         │
    ├──────────────────┼──────────────────────────────────────────┤
    │ String            │ Lexicographic (dictionary)               │
    │ Integer, Long...  │ Numeric                                 │
    │ Double, Float     │ Numeric                                 │
    │ Character         │ Unicode value                            │
    │ Date              │ Chronological                            │
    │ File              │ Path lexicographic                       │
    │ BigDecimal        │ Numeric (exact)                          │
    └──────────────────┴──────────────────────────────────────────┘

    // They already implement Comparable:
    String s = "Hello";
    s.compareTo("World");  // -7 (H < W)

    Integer a = 10;
    a.compareTo(20);  // -10 (10 < 20)
```

---

## 8. SORTING WITH COMPARABLE VS COMPARATOR

```java
    // Student class implements Comparable<Student>
    class Student implements Comparable<Student> {
        String name;
        int age;
        double marks;

        @Override
        public int compareTo(Student other) {
            return Integer.compare(this.age, other.age);  // Natural: by age
        }
    }

    List<Student> students = getStudents();

    // Using Comparable (natural ordering)
    Collections.sort(students);  // Sorts by age
    students.sort(null);          // Sorts by age (null = natural order)

    // Using Comparator (custom ordering)
    Collections.sort(students, Comparator.comparing(s -> s.name));
    students.sort(Comparator.comparingDouble(Student::getMarks));

    // Comparator OVERRIDES Comparable
    // If both exist, Comparator takes precedence in sort(list, comp)
```

---

## 9. COMPARABLE & COMPARATOR - INTERVIEW QUESTIONS (30+)

### ⭐ BASIC

**Q1: What is Comparable?**
> Interface in java.lang with compareTo() method. Defines natural ordering of objects. Class implements it.

**Q2: What is Comparator?**
> Interface in java.util with compare() method. Defines custom ordering. Separate from the class being compared.

**Q3: Difference between compareTo() and compare()?**
```
compareTo(): 1 parameter (this vs other), defined in Comparable
compare():   2 parameters (o1 vs o2), defined in Comparator
```

**Q4: What should compareTo() return?**
```
Negative: this < other
Zero:     this == other
Positive: this > other
```

**Q5: How does Collections.sort() use Comparable?**
```java
Collections.sort(list);  // Elements must implement Comparable
// Internally calls: ((Comparable)list.get(i)).compareTo(list.get(j))
```

**Q6: Can Comparable and Comparator be used together?**
> Yes. Comparator overrides Comparable when used in sort(list, comp).

**Q7: What is natural ordering?**
> The ordering defined by the class's compareTo() method. Default when no Comparator provided.

**Q8: Which Java classes implement Comparable?**
> String, Integer, Long, Double, Float, Character, BigDecimal, Date, File, etc.

---

### ⭐⭐ MIDDLE

**Q9: Why not use this.age - other.age for compareTo?**
```
Overflow risk:
this.age = Integer.MAX_VALUE (2147483647)
other.age = -1
this.age - other.age = 2147483647 - (-1) = OVERFLOW → wrong result!

Use Integer.compare() instead: safe from overflow.
```

**Q10: How to implement multi-field compareTo?**
```java
public int compareTo(Student other) {
    int result = Integer.compare(this.age, other.age);
    if (result != 0) return result;

    result = this.name.compareTo(other.name);
    if (result != 0) return result;

    return Double.compare(this.marks, other.marks);
}
```

**Q11: How to sort by multiple fields using Comparator?**
```java
Comparator<Student> comp = Comparator
    .comparingInt(Student::getAge)
    .thenComparing(Student::getName)
    .thenComparingDouble(Student::getMarks);
```

**Q12: What is Comparator.comparing()?**
```java
// Creates Comparator from key extraction function
Comparator<Student> comp = Comparator.comparing(Student::getName);
// Equivalent to: (s1, s2) -> s1.getName().compareTo(s2.getName())
```

**Q13: How to reverse a Comparator?**
```java
Comparator<Student> asc = Comparator.comparingInt(Student::getAge);
Comparator<Student> desc = asc.reversed();
// Or directly:
Comparator<Student> desc2 = Comparator.comparingInt(Student::getAge).reversed();
```

**Q14: How to handle nulls in Comparator?**
```java
// Nulls first
Comparator.comparing(Student::getName, Comparator.nullsFirst(Comparator.naturalOrder()));

// Nulls last
Comparator.comparing(Student::getName, Comparator.nullsLast(Comparator.naturalOrder()));
```

**Q15: What is the difference between Comparable and Comparator in TreeSet?**
```java
// TreeSet uses Comparable (natural ordering)
TreeSet<Student> set = new TreeSet<>();
// Student must implement Comparable

// TreeSet uses Comparator (custom ordering)
TreeSet<Student> set2 = new TreeSet<>(Comparator.comparingInt(Student::getAge));
// Student doesn't need to implement Comparable
```

**Q16: What is Comparator.chaining?**
```java
Comparator<Student> comp = Comparator
    .comparingInt(Student::getAge)        // First: by age
    .thenComparing(Student::getName)       // Then: by name
    .thenComparingDouble(Student::getMarks) // Then: by marks
    .reversed();                           // Reverse entire thing
```

**Q17: What is Comparator.naturalOrder()?**
```java
Comparator<Student> comp = Comparator.naturalOrder();
// Only works if Student implements Comparable
// Returns Comparator based on class's compareTo()
```

**Q18: What is Comparator.reverseOrder()?**
```java
Comparator<Student> comp = Comparator.reverseOrder();
// Only works if Student implements Comparable
// Returns reverse of natural ordering
```

**Q19: What happens if compareTo() violates contract?**
```
Contract:
1. If a.compareTo(b) > 0, then b.compareTo(a) < 0
2. If a.compareTo(b) > 0 && b.compareTo(c) > 0, then a.compareTo(c) > 0
3. If a.compareTo(b) == 0, then for all c: sign(a.compareTo(c)) == sign(b.compareTo(c))

Violation causes:
- TreeSet/TreeMap: undefined behavior, may not detect duplicates
- Collections.sort: may throw exception or produce wrong order
- Unpredictable results in sorted collections
```

**Q20: How to sort String case-insensitively?**
```java
// Using Comparator
list.sort(String.CASE_INSENSITIVE_ORDER);

// Or
list.sort(Comparator.comparing(String::toLowerCase));
```

**Q21: What is Comparator.comparingInt vs Comparator.comparing?**
```
comparingInt(fn): Returns Comparator based on int extraction
                  Avoids boxing/unboxing overhead
                  More efficient for int comparisons

comparing(fn):    Returns Comparator based on Comparable extraction
                  Used for String, Date, etc.
```

**Q22: What is the difference between sort(list) and sort(list, null)?**
```
sort(list):         Uses natural ordering (Comparable)
sort(list, null):   Same as sort(list) - null means natural ordering
```

**Q23: How to implement Comparable for a class you don't control?**
```java
// Use Comparator instead
Comparator<ExternalClass> comp = Comparator.comparing(e -> e.getName());
list.sort(comp);

// Or wrap in adapter class
class SortableWrapper implements Comparable<SortableWrapper> {
    ExternalClass original;
    @Override
    public int compareTo(SortableWrapper other) {
        return this.original.getName().compareTo(other.original.getName());
    }
}
```

**Q24: What is the difference between Comparable and Comparable<T>?**
```
Comparable:    Raw type (not recommended)
Comparable<T>: Parameterized type (type-safe)
               compareTo(T o) - no casting needed
               Always use Comparable<T>
```

**Q25: How does TreeMap use Comparable/Comparator?**
```java
// TreeMap uses Comparable (natural ordering)
TreeMap<Student, String> map = new TreeMap<>();
// Student must implement Comparable

// TreeMap uses Comparator (custom ordering)
TreeMap<Student, String> map2 = new TreeMap<>(
    Comparator.comparingInt(Student::getAge)
);
```

---

### ⭐⭐⭐ ADVANCED

**Q26: What is the hashCode() and equals() contract with compareTo()?**
```
If compareTo() returns 0 → equals() should return true (recommended)
If not: TreeSet/TreeMap considers them equal even if equals() says no

Example:
class Student implements Comparable<Student> {
    int age;
    public int compareTo(Student other) {
        return Integer.compare(this.age, other.age);
    }
    public boolean equals(Object o) {
        return this.age == ((Student)o).age && this.name.equals(((Student)o).name);
    }
}

// compareTo returns 0 for same age, but equals requires same name too!
// TreeSet uses compareTo → two students with same age = treated as one!
```

**Q27: What is the sorted() method with Comparator?**
```java
// Stream sorted with Comparator
List<Student> sorted = list.stream()
    .sorted(Comparator.comparingInt(Student::getAge))
    .collect(Collectors.toList());

// Comparator.reversed() for descending
List<Student> sorted2 = list.stream()
    .sorted(Comparator.comparingInt(Student::getAge).reversed())
    .collect(Collectors.toList());
```

**Q28: What is Comparator.comparing() with keyExtractor and keyComparator?**
```java
// Compare by name, but case-insensitive
Comparator<Student> comp = Comparator.comparing(
    Student::getName,
    String.CASE_INSENSITIVE_ORDER
);

// Compare by city using custom comparator
Comparator<Student> comp2 = Comparator.comparing(
    Student::getCity,
    Comparator.nullsFirst(Comparator.naturalOrder())
);
```

**Q29: What is the performance of Comparable vs Comparator?**
```
Comparable:  Slight edge (method called directly on object)
Comparator:  Extra object creation (lambda/anonymous class)
             Negligible difference in practice

Both use TimSort: O(n log n)
```

**Q30: What is the practical scenario for Comparable vs Comparator?**
```
┌───────────────────────────────┬──────────────────────────────────┐
│ Scenario                       │ Use                              │
├───────────────────────────────┼──────────────────────────────────┤
│ String sorting                 │ Comparable (natural)             │
│ Integer sorting                │ Comparable (natural)             │
│ Single obvious ordering        │ Comparable                       │
│ Multiple sort orders           │ Comparator                       │
│ Sort by different fields       │ Comparator                       │
│ Override natural ordering      │ Comparator                       │
│ Third-party class sorting      │ Comparator                       │
│ Null handling in sort          │ Comparator (nullsFirst/Last)     │
│ Case-insensitive String sort   │ Comparator (CASE_INSENSITIVE)    │
│ Complex multi-field sort       │ Comparator chaining              │
│ Reverse natural order          │ Comparator.reverseOrder()        │
│ Sort in TreeSet with custom    │ Comparator (constructor arg)     │
│ Collections.sort(list)         │ Comparable                       │
│ Collections.sort(list, comp)   │ Comparator                       │
└───────────────────────────────┴──────────────────────────────────┘
```

---

## 10. QUICK REFERENCE CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║           COMPARABLE vs COMPARATOR CHEAT SHEET                    ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  COMPARABLE (java.lang):                                         ║
║  - Interface in class itself                                     ║
║  - compareTo(T o) → int                                         ║
║  - ONE natural ordering per class                                ║
║  - Used by: TreeSet, TreeMap, Collections.sort(list)             ║
║  - Example: Integer, String already implement it                 ║
║                                                                  ║
║  COMPARATOR (java.util):                                         ║
║  - Separate class/lambda                                         ║
║  - compare(T o1, T o2) → int                                    ║
║  - MULTIPLE orderings possible                                   ║
║  - Used by: Collections.sort(list, comp), constructor args      ║
║  - Override Comparable when used                                 ║
║                                                                  ║
║  JAVA 8+ COMPARATOR METHODS:                                     ║
║  Comparator.comparing(fn)        → from key extractor            ║
║  Comparator.comparingInt(fn)     → from int key                  ║
║  comp.reversed()                 → reverse ordering               ║
║  comp.thenComparing(fn)          → chain comparators             ║
║  Comparator.naturalOrder()       → natural ordering               ║
║  Comparator.reverseOrder()       → reverse natural                ║
║  Comparator.nullsFirst(comp)     → nulls before                   ║
║  Comparator.nullsLast(comp)      → nulls after                    ║
║                                                                  ║
║  RETURN VALUES:                                                  ║
║  Negative: first < second                                        ║
║  Zero:     first == second                                       ║
║  Positive: first > second                                        ║
║                                                                  ║
║  ⚠️  Use Integer.compare() not subtraction (overflow risk)       ║
║  ⚠️  compareTo() == 0 should align with equals()                 ║
║  ⚠️  Comparable defines natural order, Comparator defines custom  ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: Comparable, Comparator, Java 8+ Methods, Interview Questions*
