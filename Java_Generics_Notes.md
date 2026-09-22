# Java Generics - Complete Interview Notes

---

## 1. WHAT IS GENERICS?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Before Generics (JDK 1.4 and below):                        │
    │                                                              │
    │  List list = new ArrayList();                                 │
    │  list.add("Hello");                                          │
    │  list.add(42);     // No compile error! (Object type)        │
    │                                                              │
    │  String s = (String) list.get(1);  // ClassCastException!    │
    │  // Runtime error, no compile-time safety                     │
    │                                                              │
    ├──────────────────────────────────────────────────────────────┤
    │  With Generics (JDK 5+):                                     │
    │                                                              │
    │  List<String> list = new ArrayList<>();                       │
    │  list.add("Hello");                                          │
    │  list.add(42);     // COMPILE ERROR! ✅                       │
    │                                                              │
    │  String s = list.get(1);  // No casting needed               │
    │  // Compile-time safety + No casting                          │
    └──────────────────────────────────────────────────────────────┘

    Benefits:
    ┌──────────────────────────────────────────────────────────────┐
    │  1. Type Safety    → Compile-time error for wrong types      │
    │  2. No Casting     → Automatic type inference                │
    │  3. Code Reuse     → Same class for different types          │
    │  4. Clean Code     → No ugly type casts                      │
    │  5. Bug Prevention → Catch errors before runtime             │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. GENERIC CLASS

```java
// Generic class with single type parameter
class Box<T> {
    private T value;

    public void set(T value) { this.value = value; }
    public T get() { return value; }
}

// Usage
Box<String> stringBox = new Box<>();
stringBox.set("Hello");
String s = stringBox.get();  // No casting

Box<Integer> intBox = new Box<>();
intBox.set(42);
int n = intBox.get();
```

### 2.1 Multiple Type Parameters

```java
class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
}

// Usage
Pair<String, Integer> entry = new Pair<>("Age", 25);
String key = entry.getKey();     // "Age"
Integer val = entry.getValue();  // 25
```

### 2.2 Visual: Generic vs Non-Generic

```
    Non-Generic:                     Generic:
    ┌─────────────────┐             ┌─────────────────┐
    │ Box              │             │ Box<T>           │
    │ - Object value   │             │ - T value        │
    │ + set(Object)    │             │ + set(T)         │
    │ + get() → Object │             │ + get() → T      │
    └─────────────────┘             └─────────────────┘
           │                                │
           │                                │
    Box<String> box =           Box<String> box = new Box<>();
    new Box();                  box.set("Hello");
    box.set("Hello");          String s = box.get();
    String s = (String) box.get();  ← No cast!
       ↑ Need cast!
```

---

## 3. GENERIC INTERFACE

```java
// Generic interface
interface Repository<T> {
    void save(T entity);
    T findById(int id);
    List<T> findAll();
}

// Implementation with specific type
class UserRepository implements Repository<User> {
    @Override
    public void save(User entity) { /* ... */ }

    @Override
    public User findById(int id) { /* ... */ return user; }

    @Override
    public List<User> findAll() { /* ... */ }
}

// Implementation with generic type
class GenericRepository<T> implements Repository<T> {
    @Override
    public void save(T entity) { /* ... */ }
    @Override
    public T findById(int id) { /* ... */ return null; }
    @Override
    public List<T> findAll() { /* ... */ return null; }
}
```

---

## 4. GENERIC METHOD

```java
// Type parameter <T> before return type
public static <T> void printArray(T[] array) {
    for (T element : array) {
        System.out.println(element);
    }
}

// Usage (type inferred)
Integer[] nums = {1, 2, 3};
String[] names = {"A", "B", "C"};
printArray(nums);   // T inferred as Integer
printArray(names);  // T inferred as String

// Explicit type specification
printArray.<String>printArray(names);

// Generic method in non-generic class
class Utility {
    public static <T> List<T> arrayToList(T[] array) {
        return Arrays.stream(array).collect(Collectors.toList());
    }
}

// Multiple type parameters
public static <K, V> Map<K, V> createMap(K key, V value) {
    Map<K, V> map = new HashMap<>();
    map.put(key, value);
    return map;
}
```

---

## 5. BOUNDED TYPE PARAMETERS

### 5.1 Upper Bound (extends)

```java
// T must be Number or subclass of Number
class MathBox<T extends Number> {
    private T value;

    public double doubleValue() {
        return value.doubleValue();  // OK! Number has doubleValue()
    }
}

// Usage
MathBox<Integer> box1 = new MathBox<>();  // ✅
MathBox<Double> box2 = new MathBox<>();   // ✅
MathBox<String> box3 = new MathBox<>();   // ❌ Compile error!
```

### 5.2 Multiple Bounds

```java
// T must implement BOTH Comparable AND Serializable
class SortableBox<T extends Comparable<T> & Serializable> {
    private T value;

    public int compareTo(T other) {
        return value.compareTo(other);  // OK!
    }
}

// ⚠️ Class always comes FIRST in multiple bounds
<T extends Comparable<T> & Serializable>  // ✅ Correct
<T extends Serializable & Comparable<T>>  // ❌ Compile error
```

### 5.3 Lower Bound (super) - Wildcards

```java
// ? super Integer means: Integer or any parent of Integer
public static void addNumbers(List<? super Integer> list) {
    list.add(1);    // ✅ OK to add Integer
    list.add(2);    // ✅ OK to add Integer
    // Integer i = list.get(0);  // ❌ Can only add, not get as Integer
}
```

---

## 6. WILDCARDS

### 6.1 Upper Bounded Wildcard (? extends)

```java
// Accepts Number or any subclass (Integer, Double, etc.)
public static double sum(List<? extends Number> list) {
    double total = 0;
    for (Number n : list) {
        total += n.doubleValue();
    }
    return total;
}

// Usage
List<Integer> ints = Arrays.asList(1, 2, 3);
List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);

sum(ints);    // ✅
sum(doubles); // ✅
```

### 6.2 Lower Bounded Wildcard (? super)

```java
// Accepts Integer or any superclass (Number, Object)
public static void addIntegers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3);
}

// Usage
List<Number> numbers = new ArrayList<>();
addIntegers(numbers);  // ✅ Number is superclass of Integer

List<Object> objects = new ArrayList<>();
addIntegers(objects);  // ✅ Object is superclass of Integer
```

### 6.3 Unbounded Wildcard (?)

```java
// Accepts any type
public static void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

// Usage
List<String> strings = Arrays.asList("A", "B");
List<Integer> ints = Arrays.asList(1, 2, 3);

printList(strings);  // ✅
printList(ints);     // ✅
```

### 6.4 Wildcard Comparison

```
    ┌──────────────────┬──────────────────────────────────────────┐
    │ Wildcard          │ What it accepts       │ Can add?         │
    ├──────────────────┼──────────────────────────────────────────┤
    │ ? (unbounded)     │ Any type              │ Only null        │
    │ ? extends T       │ T or subclass of T    │ NOTHING          │
    │ ? super T         │ T or superclass of T  │ T or subclass    │
    └──────────────────┴──────────────────────────────────────────┘

    PECS Rule: Producer Extends, Consumer Super
    ┌──────────────────────────────────────────────────────────────┐
    │  If you PRODUCE data (read from collection):  use ? extends  │
    │  If you CONSUME data (write to collection):   use ? super    │
    │  If you do BOTH:                              use exact type  │
    └──────────────────────────────────────────────────────────────┘
```

---

## 7. TYPE ERASURE

```
    Generics are COMPILE-TIME only! JVM doesn't know about them.

    ┌──────────────────────────────────────────────────────────────┐
    │  Before Compilation:                                         │
    │  List<String> list = new ArrayList<>();                      │
    │  list.add("Hello");                                          │
    │  String s = list.get(0);                                     │
    │                                                              │
    │  After Compilation (Type Erasure):                           │
    │  List list = new ArrayList();                                │
    │  list.add("Hello");                                          │
    │  String s = (String) list.get(0);  ← Cast inserted!         │
    │                                                              │
    │  <T> becomes Object (or bound type)                          │
    │  <T extends Number> becomes Number                           │
    │  All generic type info is ERASED!                            │
    └──────────────────────────────────────────────────────────────┘

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  SOURCE CODE          →    BYTECODE (after erasure)          │
    │                                                              │
    │  Box<String>          →    Box                               │
    │  Box<Integer>         →    Box                               │
    │  List<String>         →    List                              │
    │  List<Integer>        →    List                              │
    │  <T>                  →    Object                            │
    │  <T extends Number>   →    Number                            │
    │                                                              │
    │  Both Box<String> and Box<Integer> become just Box!          │
    └──────────────────────────────────────────────────────────────┘
```

### 7.1 What Type Erasure Does

```
    1. Replaces type parameters with bounds (or Object)
       <T>              → Object
       <T extends Number> → Number

    2. Inserts casts where needed
       String s = list.get(0);  →  String s = (String) list.get(0);

    3. Generates bridge methods for polymorphism
       Ensures subclass override works correctly
```

### 7.2 Type Erasure Consequences

```java
// ❌ Cannot do these:

// 1. Cannot use instanceof with generic type
Box<Integer> box = new Box<>();
if (box instanceof Box<Integer>) { }  // ❌ Compile error!

// 2. Cannot create generic array
Box<String>[] arr = new Box<String>[10];  // ❌ Compile error!

// 3. Cannot create T instance
class Box<T> {
    T value = new T();  // ❌ Compile error!
}

// 4. Cannot overload with different generic types
class Box<T> { void print(T t) { } }
class Box<T> { void print(String s) { } }  // ❌ Compile error!
// After erasure: both become print(Object)

// 5. Cannot use primitive types
List<int> list = new ArrayList<>();  // ❌ Use Integer, not int
```

---

## 8. GENERIC ARRAYS

```java
// ❌ Cannot create generic array directly
T[] array = new T[10];  // ❌ Compile error!

// ✅ Workarounds

// Method 1: Array.newInstance (runtime)
@SuppressWarnings("unchecked")
T[] createArray(Class<T> clazz, int size) {
    return (T[]) Array.newInstance(clazz, size);
}

// Method 2: Cast from Object array
Object[] objArray = new Object[10];
T[] array = (T[]) objArray;

// Method 3: Pass Class object
class Box<T> {
    T[] createArray(int size, Class<T> clazz) {
        return (T[]) Array.newInstance(clazz, size);
    }
}
```

---

## 9. RAW TYPES

```java
// Raw type: Using generic class without type parameter
Box rawBox = new Box();        // Raw type
Box<String> safeBox = new Box<>();  // Parameterized type

// Raw type disables generics
rawBox.set("Hello");
String s = (String) rawBox.get();  // Need cast!

// Raw type is legacy (pre-generics compatibility)
// ⚠️ Always use parameterized types!
// ✅ Box<String> box = new Box<>();
// ❌ Box box = new Box();
```

---

## 10. DIAMOND OPERATOR

```java
// JDK 7+: Diamond operator <>
Box<String> box1 = new Box<>();        // Type inferred
Box<Integer> box2 = new Box<>();       // Type inferred

// Equivalent to (older syntax):
Box<String> box3 = new Box<String>();  // JDK 5/6
Box<Integer> box4 = new Box<Integer>();

// Works with any generic class
List<String> list = new ArrayList<>();
Map<String, Integer> map = new HashMap<>();
Set<Integer> set = new HashSet<>();
```

---

## 11. TYPE INFERENCE

```java
// JDK 8+: Target type inference
Box<String> box = createBox();  // Inferred from variable type

static <T> Box<T> createBox() {
    return new Box<>();
}

// Method chaining inference
List<String> list = List.of("A", "B", "C").stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// Explicit type inference
Box.<String>createBox().set("Hello");
```

---

## 12. BOUNDS VISUAL DIAGRAM

```
    extends vs super:

    Given: Integer extends Number extends Object

    ┌──────────────────────────────────────────────────────────────┐
    │  ? extends Number (Upper Bound)                              │
    │                                                              │
    │  ┌──────────────────────────────────────────────────────┐   │
    │  │ Number ←─ Integer ←─ Long ←─ Double ←─ Float        │   │
    │  │    ↑                                                  │   │
    │  │    └─ ACCEPTED (Number or subclass)                   │   │
    │  └──────────────────────────────────────────────────────┘   │
    │                                                              │
    │  Can READ as Number ✅    Can ADD nothing ❌                  │
    │                                                              │
    ├──────────────────────────────────────────────────────────────┤
    │  ? super Number (Lower Bound)                                │
    │                                                              │
    │  ┌──────────────────────────────────────────────────────┐   │
    │  │ Object ←─ Number ←─ Integer                          │   │
    │  │    ↑        ↑                                        │   │
    │  │    └─ ACCEPTED (Number or superclass)                 │   │
    │  └──────────────────────────────────────────────────────┘   │
    │                                                              │
    │  Can READ as Object ⚠️  Can ADD Number or subclass ✅        │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘

    PECS Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Producer (source) ──→ Consumer (destination)                │
    │                                                              │
    │  copy(List<? extends T> src, List<? super T> dest)          │
    │              ↑                        ↑                       │
    │         Producer (reads)        Consumer (writes)             │
    │         Use extends               Use super                   │
    └──────────────────────────────────────────────────────────────┘
```

---

## 13. PRACTICAL EXAMPLES

### 13.1 Generic Stack

```java
class Stack<T> {
    private T[] elements;
    private int top = -1;

    @SuppressWarnings("unchecked")
    public Stack(int capacity) {
        elements = (T[]) new Object[capacity];
    }

    public void push(T item) {
        elements[++top] = item;
    }

    public T pop() {
        return elements[top--];
    }

    public T peek() {
        return elements[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }
}

// Usage
Stack<String> stack = new Stack<>(10);
stack.push("A");
stack.push("B");
String top = stack.pop();  // "B"
```

### 13.2 Generic Method: Filter

```java
public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
    return list.stream()
               .filter(predicate)
               .collect(Collectors.toList());
}

// Usage
List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6);
List<Integer> evens = filter(nums, n -> n % 2 == 0);
// [2, 4, 6]
```

### 13.3 Generic Repository Pattern

```java
interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    void save(T entity);
    void delete(T entity);
}

class UserRepository implements Repository<User, Long> {
    @Override
    public User findById(Long id) { /* ... */ }

    @Override
    public List<User> findAll() { /* ... */ }

    @Override
    public void save(User entity) { /* ... */ }

    @Override
    public void delete(User entity) { /* ... */ }
}

class ProductRepository implements Repository<Product, String> {
    @Override
    public Product findById(String id) { /* ... */ }
    // ...
}
```

### 13.4 Generic Builder Pattern

```java
class Builder<T> {
    private T product;

    public Builder(T product) {
        this.product = product;
    }

    public <V> Builder<T> with(BiConsumer<T, V> setter, V value) {
        setter.accept(product, value);
        return this;
    }

    public T build() {
        return product;
    }
}

// Usage
User user = new Builder<>(new User())
    .with(User::setName, "Amit")
    .with(User::setAge, 25)
    .build();
```

---

## 14. GENERICS INTERVIEW QUESTIONS (40+)

### ⭐ BASIC

**Q1: What is Generics in Java?**
> Mechanism for parameterized types. Allows classes, interfaces, methods to operate on objects of various types while providing compile-time type safety.

**Q2: What are the benefits of Generics?**
```
1. Type Safety (compile-time)
2. No casting needed
3. Code reusability
4. Clean code
5. Bug prevention
```

**Q3: What is type erasure?**
> Generic type information is removed at compile time. `<T>` becomes Object or bound type. JVM doesn't know about generics.

**Q4: What is difference between List and List<String>?**
```
List: Raw type (no type safety, need casting)
List<String>: Parameterized type (type safe, no casting)
```

**Q5: What is the diamond operator?**
> `<>` in `new ArrayList<>()`. JDK 7+ feature. Infers type from variable.

**Q6: Can we use primitive types in generics?**
> No. Use wrapper classes: int→Integer, char→Character, boolean→Boolean.

**Q7: What is bounded type parameter?**
> `<T extends Number>` restricts T to Number or its subclasses. Provides access to bound type's methods.

**Q8: What is difference between ? extends and ? super?**
```
? extends T: Upper bound, accepts T or subclass, READ-only
? super T:   Lower bound, accepts T or superclass, WRITE T
```

---

### ⭐⭐ MIDDLE

**Q9: What is type erasure in detail?**
```
Compile time: <T> exists, type checking happens
Runtime:      <T> erased, replaced with Object or bound type
              Casts inserted where needed
              Bridge methods generated
```

**Q10: What are the limitations of type erasure?**
```
1. Cannot use instanceof with generic type
2. Cannot create generic array
3. Cannot create T instance
4. Cannot overload with different generic types
5. Cannot use primitives
6. Static context cannot reference type parameters
```

**Q11: What is PECS rule?**
```
Producer Extends, Consumer Super:
- Read from collection (? extends T): Producer
- Write to collection (? super T): Consumer
- copy(List<? extends T> src, List<? super T> dest)
```

**Q12: What is difference between List<? extends Number> and List<Number>?**
```
List<Number>: Accepts ONLY Number objects
List<? extends Number>: Accepts Number or any subclass (Integer, Double, etc.)
```

**Q13: What is raw type?**
> Using generic class without type parameter: `Box box = new Box()`. Disables type safety. Legacy compatibility. Always use parameterized types.

**Q14: What is difference between generic class and generic method?**
```
Generic class: <T> on class declaration, instance-level
Generic method: <T> on method, method-level (static or instance)
```

**Q15: What is bridge method?**
> Compiler-generated method that ensures polymorphism works after type erasure.
```java
// After erasure, override might not match. Bridge method fixes this.
public void set(Object value) { set((String) value); } // bridge
```

**Q16: What is unbounded wildcard (? mean)?**
> Accepts any type. Can only read as Object. Cannot add (except null). Used when type doesn't matter.

**Q17: What is the difference between <?> and <? extends Object>?**
```
<?>:             Same as <? extends Object>
<? extends Object>: Same as <?>
They are equivalent.
```

**Q18: What is the difference between Comparable and Comparator with generics?**
```java
// Comparable (inside class)
class Student implements Comparable<Student> {
    public int compareTo(Student other) { ... }
}

// Comparator (outside)
Comparator<Student> comp = (s1, s2) -> s1.getName().compareTo(s2.getName());
```

**Q19: What is the difference between <T> and <?>?**
```
<T>: Declares a type parameter (defines T)
<?>: Uses wildcard (uses unknown type)
<T> in class: class Box<T> { T value; }
<?> in method: void print(List<?> list) { }
```

**Q20: What is the difference between generic method and wildcards?**
```
Generic method: Defines its own type parameter
<T> T first(List<T> list)

Wildcard: Uses unknown type from context
void print(List<?> list)
```

**Q21: Can we have static method in generic class?**
```java
class Box<T> {
    // ❌ Cannot use T in static context
    // static T value;  // Compile error!

    // ✅ Static method can have its own type parameter
    public static <U> U helper(U item) {
        return item;
    }
}
```

**Q22: Can we create generic exception?**
```java
// ❌ Cannot extend Throwable with type parameter
class MyException<T> extends Exception { }  // Compile error!

// ✅ But can use type parameter in class that extends Exception
class GenericHandler<T extends Exception> {
    void handle(T exception) { }
}
```

**Q23: What is the difference between List<? extends Number> and List<Integer>?**
```
List<Integer>: Accepts ONLY Integer
List<? extends Number>: Accepts Integer, Double, Long, etc.
The wildcard is more flexible.
```

**Q24: What happens when you mix raw and parameterized types?**
```java
List raw = new ArrayList();
List<String> safe = raw;  // ⚠️ Unchecked warning
// Raw type disables type checking for that variable
```

**Q25: What is type inference in Java 8?**
```java
// Target type inference
Box<String> box = createBox();  // T inferred as String

// Method chaining inference
List<String> list = stream.map(x -> x.toString()).collect(toList());
```

---

### ⭐⭐⭐ ADVANCED

**Q26: What is the difference between ArrayList<?> and ArrayList<Object>?**
```
ArrayList<Object>: Can add ANY object
ArrayList<?>:      Can add NOTHING (except null)
ArrayList<?> is more restrictive but more flexible in method signatures
```

**Q27: What is recursive type bound?**
```java
// T must be Comparable of itself
<T extends Comparable<T>>
// Example: String implements Comparable<String>
// Used for sorting, max, min operations
```

**Q28: What is get/put principle?**
```
PECS:
- get (read): use ? extends T (producer)
- put (write): use ? super T (consumer)
- copy(List<? extends T> src, List<? super T> dest)
```

**Q29: What is type token?**
```java
// Passing type information at runtime
Class<String> type = String.class;
T instance = type.getDeclaredConstructor().newInstance();

// Generic type token
TypeToken<List<String>> token = new TypeToken<List<String>>(){};
Type type = token.getType();
```

**Q30: What is the difference between Serializable and Comparable with generics?**
```
Serializable: Marker interface, no methods, allows serialization
Comparable<T>: Generic interface, compareTo(T) method, defines ordering
```

**Q31: What is the difference between <T extends Comparable<T>> and <T extends Comparable>?**
```
<T extends Comparable<T>>: T must be comparable to itself (type-safe)
<T extends Comparable>: Raw type, not type-safe, unchecked warnings
Always use the parameterized form.
```

**Q32: What is the purpose of @SafeVarargs?**
```java
// Suppresses unchecked warnings for varargs with generics
@SafeVarargs
public static <T> List<T> asList(T... elements) {
    return Arrays.asList(elements);
}
// Safe because array is only read, not modified
```

**Q33: What is the difference between List<String> and List<Object>?**
```
List<String>: Accepts String only
List<Object>: Accepts any Object
They are NOT compatible!
List<String> is NOT a subtype of List<Object>
But List<String> IS a subtype of List<?>
```

**Q34: What is capture helper?**
```java
// Wildcard capture: use generic method to capture wildcard type
public static void swap(List<?> list) {
    swapHelper(list);  // Capture the wildcard type
}

private static <T> void swapHelper(List<T> list) {
    T temp = list.get(0);
    list.set(0, list.get(1));
    list.set(1, temp);
}
```

**Q35: What is the difference between T and Object?**
```
T: Type parameter (generic), erased to Object at runtime
Object: Root class, can hold any type
In generic context, T gives type safety at compile time
```

**Q36: What is the difference between List and List<Object>?**
```
List: Raw type (pre-generics), no type safety
List<Object>: Parameterized type, type safe, accepts any Object
Use List<Object> not List
```

**Q37: What happens when you serialize generic class?**
```
Type erasure applies! Generic type info is lost after serialization.
List<String> serialized as List (raw)
Need TypeToken or passing Class<T> to preserve type info
```

**Q38: What is the use of Class<T> in generics?**
```java
// Pass type information at runtime
public static <T> T create(Class<T> clazz) throws Exception {
    return clazz.getDeclaredConstructor().newInstance();
}

// Usage
User user = create(User.class);  // Class<User> passed
```

**Q39: What is the difference between generic interface and generic class?**
```
Same concept, different context:
Generic class: implements data structure
Generic interface: defines contract
Both use same syntax: <T>, <T extends Bound>
```

**Q40: Practical scenario - When to use what?**
```
┌───────────────────────────────┬──────────────────────────────────┐
│ Scenario                       │ Use                              │
├───────────────────────────────┼──────────────────────────────────┤
│ Type-safe collection           │ List<T>, Map<K,V>                │
│ Read-only method param         │ List<? extends T>               │
│ Write-only method param        │ List<? super T>                 │
│ Method that works for all types│ <T> void method(T item)         │
│ Generic class (Box, Stack)     │ class Box<T>                    │
│ Generic interface (Repo)       │ interface Repo<T>               │
│ Method with bounded type       │ <T extends Number> void method  │
│ Type-safe factory              │ Class<T> parameter              │
└───────────────────────────────┴──────────────────────────────────┘
```

---

## 15. QUICK REFERENCE CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║                  JAVA GENERICS CHEAT SHEET                       ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  SYNTAX:                                                         ║
║  class Box<T> { }            Generic class                       ║
║  interface Repo<T> { }       Generic interface                   ║
║  <T> T first(List<T> list)   Generic method                      ║
║  Box<String> box = new Box<>(); Diamond operator (Java 7+)       ║
║                                                                  ║
║  BOUNDS:                                                         ║
║  <T extends Number>          Upper bound (Number or subclass)    ║
║  <T extends A & B>           Multiple bounds (class first!)      ║
║                                                                  ║
║  WILDCARDS:                                                      ║
║  List<?>                     Any type (read as Object)           ║
║  List<? extends T>           T or subclass (READ-ONLY)           ║
║  List<? super T>             T or superclass (WRITE T)           ║
║                                                                  ║
║  PECS: Producer Extends, Consumer Super                          ║
║  - Read (? extends):  Producer → use extends                     ║
║  - Write (? super):   Consumer → use super                       ║
║  - Both:              Use exact type                             ║
║                                                                  ║
║  TYPE ERASURE:                                                   ║
║  - Generics = COMPILE-TIME only                                  ║
║  - <T> → Object (or bound type) at runtime                      ║
║  - <T extends Number> → Number at runtime                       ║
║  - Casts inserted automatically                                  ║
║  - Cannot: instanceof, new T[], new T, primitives               ║
║                                                                  ║
║  BUILT-IN GENERICS:                                              ║
║  List<E>, Set<E>, Map<K,V>, Comparable<T>, Comparator<T>        ║
║  Optional<T>, Stream<T>, Future<T>, Class<T>                     ║
║  Pair<K,V>, Triple<A,B,C>                                        ║
║                                                                  ║
║  ⚠️  No primitives (use wrapper classes)                         ║
║  ⚠️  No generic arrays (use Array.newInstance)                   ║
║  ⚠️  No static type parameters                                   ║
║  ⚠️  No generic exceptions                                       ║
║  ⚠️  Use parameterized types, not raw types                      ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: All Generics Concepts, Type Erasure, Wildcards, PECS, Interview Questions*
