# Java 8 Features - Complete Interview Notes

---

## 1. JAVA 8 OVERVIEW

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Java 8 (March 2014) - Major Release!                        │
    │                                                              │
    │  New Features:                                               │
    │  1. Lambda Expressions                                       │
    │  2. Functional Interfaces                                    │
    │  3. Method References                                        │
    │  4. Stream API                                               │
    │  5. Optional                                                 │
    │  6. Default Methods in Interfaces                            │
    │  7. Date/Time API (java.time)                                │
    │  8. CompletableFuture                                        │
    │  9. Base64 Encoding/Decoding                                 │
    │  10. String methods (join, splits)                           │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. LAMBDA EXPRESSIONS

### 2.1 What is Lambda?

```
    Lambda = Anonymous function (function without name)

    Before Java 8:
    ┌──────────────────────────────────────────────────────────────┐
    │  Runnable r = new Runnable() {                                │
    │      @Override                                               │
    │      public void run() {                                     │
    │          System.out.println("Hello");                        │
    │      }                                                       │
    │  };                                                          │
    └──────────────────────────────────────────────────────────────┘

    After Java 8:
    ┌──────────────────────────────────────────────────────────────┐
    │  Runnable r = () -> System.out.println("Hello");             │
    └──────────────────────────────────────────────────────────────┘
```

### 2.2 Lambda Syntax

```
    (parameters) -> expression
    (parameters) -> { statements; }

    ┌──────────────────────────────────────────────────────────────┐
    │  No params:      () -> System.out.println("Hello")          │
    │  One param:      (x) -> x * x    or  x -> x * x            │
    │  Multiple params: (x, y) -> x + y                           │
    │  With types:     (int x, int y) -> x + y                    │
    │  With body:      (x, y) -> { int sum = x + y; return sum; } │
    │  Return:         (x, y) -> x + y  (single expression)       │
    └──────────────────────────────────────────────────────────────┘
```

### 2.3 Lambda Examples

```java
// Before vs After
// 1. Runnable
Runnable r = () -> System.out.println("Hello");

// 2. Comparator
Comparator<Integer> comp = (a, b) -> a - b;

// 3. ActionListener
button.addActionListener(e -> handleClick());

// 4. forEach
list.forEach(item -> System.out.println(item));

// 5. Predicate
Predicate<String> isEmpty = s -> s.isEmpty();

// 6. Function
Function<String, Integer> length = s -> s.length();

// 7. Custom
(MathOperation) (a, b) -> a + b
```

### 2.4 Lambda vs Anonymous Class

```
    ┌──────────────────────┬──────────────────────┬──────────────────────┐
    │ Feature               │ Anonymous Class       │ Lambda               │
    ├──────────────────────┼──────────────────────┼──────────────────────┤
    │ this keyword          │ Refers to anonymous   │ Refers to enclosing  │
    │                       │ class                 │ class                │
    │ Functional interface  │ Not required          │ Required             │
    │ Multiple methods      │ Yes                   │ No (SAM only)        │
    │ Compilation           │ Separate .class file  │ Bytecode in outer    │
    │ Code                  │ Verbose               │ Concise              │
    └──────────────────────┴──────────────────────┴──────────────────────┘
```

---

## 3. FUNCTIONAL INTERFACES

### 3.1 What is Functional Interface?

```
    Interface with EXACTLY ONE abstract method (@FunctionalInterface)

    ┌──────────────────────────────────────────────────────────────┐
    │  @FunctionalInterface                                        │
    │  interface Calculator {                                      │
    │      int calculate(int a, int b);  // One abstract method   │
    │  }                                                           │
    │                                                              │
    │  Calculator add = (a, b) -> a + b;  // Lambda!              │
    └──────────────────────────────────────────────────────────────┘

    @FunctionalInterface annotation:
    - Optional but recommended
    - Compile-time check for exactly one abstract method
    - Can have any number of default methods
```

### 3.2 Built-in Functional Interfaces (java.util.function)

```
    ┌─────────────────────┬──────────────────┬──────────────────────┐
    │ Interface             │ Method            │ Description          │
    ├─────────────────────┼──────────────────┼──────────────────────┤
    │ Predicate<T>         │ boolean test(T)   │ Condition check      │
    │ Consumer<T>          │ void accept(T)    │ Consume/modify       │
    │ Function<T,R>        │ R apply(T)        │ Transform T → R      │
    │ Supplier<T>          │ T get()           │ Supply/provide       │
    │ UnaryOperator<T>     │ T apply(T)        │ Same type transform  │
    │ BinaryOperator<T>    │ T apply(T,T)      │ Combine two T        │
    │ BiPredicate<T,U>     │ boolean test(T,U) │ Two param predicate  │
    │ BiConsumer<T,U>      │ void accept(T,U)  │ Two param consumer   │
    │ BiFunction<T,U,R>    │ R apply(T,U)      │ Two param function   │
    └─────────────────────┴──────────────────┴──────────────────────┘
```

### 3.3 Predicate

```java
Predicate<String> isEmpty = s -> s.isEmpty();
Predicate<String> isNotEmpty = s -> !s.isEmpty();

isEmpty.test("");     // true
isEmpty.test("Hi");   // false

// Chaining
Predicate<String> startsWithA = s -> s.startsWith("A");
Predicate<String> hasLength3 = s -> s.length() == 3;

startsWithA.and(hasLength3).test("ABC");  // true (both)
startsWithA.or(hasLength3).test("ABC");   // true (either)
startsWithA.negate().test("ABC");         // false (not)
```

### 3.4 Consumer

```java
Consumer<String> print = s -> System.out.println(s);
Consumer<String> printUpper = s -> System.out.println(s.toUpperCase());

print.accept("Hello");  // Hello

// Chaining
Consumer<String> combined = print.andThen(printUpper);
combined.accept("Hello");  // Hello → HELLO
```

### 3.5 Function

```java
Function<String, Integer> length = s -> s.length();
Function<String, String> upper = s -> s.toUpperCase();

length.apply("Hello");  // 5

// Chaining
Function<String, String> pipeline = upper.andThen(s -> s + "!");
pipeline.apply("hello");  // HELLO!

// Compose
Function<Integer, Integer> doubleIt = n -> n * 2;
Function<Integer, Integer> addTen = n -> n + 10;
Function<Integer, Integer> composed = doubleIt.compose(addTen);
composed.apply(5);  // (5+10)*2 = 30
```

### 3.6 Supplier

```java
Supplier<Double> random = Math::random;
Double r = random.get();  // Random number

Supplier<List<String>> listFactory = ArrayList::new;
List<String> list = listFactory.get();
```

### 3.7 Visual: Functional Interface Flow

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Predicate: T → boolean (filter)                             │
    │  List.stream().filter(x -> x > 5)                           │
    │                                                              │
    │  Consumer: T → void (forEach)                                │
    │  List.forEach(x -> System.out.println(x))                   │
    │                                                              │
    │  Function: T → R (map)                                       │
    │  List.stream().map(x -> x.toUpperCase())                    │
    │                                                              │
    │  Supplier: () → T (create)                                   │
    │  Stream.generate(() -> Math.random())                        │
    │                                                              │
    │  UnaryOperator: T → T (transform same type)                  │
    │  List.replaceAll(x -> x.toUpperCase())                       │
    └──────────────────────────────────────────────────────────────┘
```

---

## 4. METHOD REFERENCES

```
    Shorthand for lambda when only calling an existing method

    ┌──────────────────────────────────────────────────────────────┐
    │  Lambda                  │ Method Reference                   │
    ├──────────────────────────────────────────────────────────────┤
    │  (s) -> System.out.println(s) │ System.out::println          │
    │  (x) -> x.length()      │ String::length                    │
    │  () -> new ArrayList()   │ ArrayList::new                    │
    │  (s) -> Integer.parseInt(s) │ Integer::parseInt              │
    │  (a, b) -> a.compareTo(b)   │ String::compareTo             │
    │  () -> Math.random()     │ Math::random                     │
    └──────────────────────────────────────────────────────────────┘

    4 Types of Method References:
    ┌──────────────────────────────────────────────────────────────┐
    │  1. Static method      → ClassName::staticMethod             │
    │     String::valueOf                                                  │
    │     Math::max                                                        │
    │                                                                      │
    │  2. Instance method    → object::instanceMethod              │
    │     System.out::println                                              │
    │     String::length  (on parameter)                                  │
    │                                                                      │
    │  3. Arbitrary object   → ClassName::instanceMethod           │
    │     String::toUpperCase (called on each element)                    │
    │     String::compareTo                                               │
    │                                                                      │
    │  4. Constructor        → ClassName::new                      │
    │     ArrayList::new                                                   │
    │     HashMap::new                                                     │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Examples
list.forEach(System.out::println);           // instance method
list.stream().map(String::toUpperCase);      // arbitrary object
list.stream().sorted(String::compareToIgnoreCase); // arbitrary object
Stream.generate(Math::random);               // static method
list.stream().map(ArrayList::new);           // constructor
```

---

## 5. STREAM API

### 5.1 What is Stream?

```
    Stream = Sequence of elements supporting functional operations

    ┌──────────────────────────────────────────────────────────────┐
    │  NOT a data structure! It's a VIEW over data                 │
    │                                                              │
    │  Collection: [1, 2, 3, 4, 5, 6, 7, 8]                       │
    │                    │                                         │
    │                    ▼                                         │
    │  Stream: filter → map → collect                              │
    │  [1,2,3,4,5,6,7,8] → [2,4,6,8] → [4,8,12,16] → [4,8,12,16]│
    │                                                              │
    │  Key Features:                                               │
    │  - Not modifies source data                                  │
    │  - Lazy (operations deferred until terminal)                 │
    │  - Functional (uses lambda expressions)                      │
    │  - Can be parallel                                           │
    │  - One-time use (can't reuse)                                │
    └──────────────────────────────────────────────────────────────┘
```

### 5.2 Stream Operations

```
    ┌──────────────────────────────────────────────────────────────┐
    │  INTERMEDIATE OPERATIONS (return Stream, lazy)                │
    │  ───────────────────────────────────────────────────────────  │
    │  filter(Predicate)     → Select elements matching condition   │
    │  map(Function)         → Transform each element              │
    │  flatMap(Function)     → Flatten nested streams              │
    │  sorted()              → Sort elements                       │
    │  distinct()            → Remove duplicates                   │
    │  limit(n)              → Take first n elements               │
    │  skip(n)               → Skip first n elements               │
    │  peek(Consumer)        → Peek at element (debug)             │
    │  takeWhile(Predicate)  → Take while condition true (Java 9+) │
    │  dropWhile(Predicate)  → Drop while condition true (Java 9+) │
    │                                                                      │
    │  TERMINAL OPERATIONS (trigger execution, consume stream)     │
    │  ───────────────────────────────────────────────────────────  │
    │  collect(Collector)    → Collect to collection               │
    │  forEach(Consumer)     → Iterate each element                │
    │  reduce(BinaryOp)      → Reduce to single value              │
    │  count()               → Count elements                      │
    │  anyMatch(Predicate)   → Any match?                          │
    │  allMatch(Predicate)   → All match?                          │
    │  noneMatch(Predicate)  → None match?                         │
    │  findFirst()           → First element (Optional)            │
    │  findAny()             → Any element (Optional)              │
    │  min/Max(Comparator)   → Min/Max element (Optional)          │
    │  toArray()             → Convert to array                     │
    └──────────────────────────────────────────────────────────────┘
```

### 5.3 Stream Visual Pipeline

```
    List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

    nums.stream()
        .filter(n -> n % 2 == 0)    // [2, 4, 6, 8]
        .map(n -> n * n)             // [4, 16, 36, 64]
        .limit(3)                    // [4, 16, 36]
        .forEach(System.out::println);

    Visual:
    ┌───┐  ┌───┐  ┌───┐  ┌───┐  ┌───┐  ┌───┐  ┌───┐  ┌───┐
    │ 1 │→│ 2 │→│ 3 │→│ 4 │→│ 5 │→│ 6 │→│ 7 │→│ 8 │
    └───┘  └───┘  └───┘  └───┘  └───┘  └───┘  └───┘  └───┘
             ↓           ↓           ↓           ↓
    filter:  ✓     ✗     ✓     ✗     ✓     ✗     ✓     ✗
             ↓           ↓           ↓           ↓
             2           4           6           8
             ↓           ↓           ↓           ↓
    map:      4          16          36          64
             ↓           ↓           ↓           ✗ (limit 3)
    result:   4          16          36
```

### 5.4 Stream Examples

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

// ═══════════════════════════════════════════════════════════════
// FILTER - DETAILED
// ═══════════════════════════════════════════════════════════════

// filter(Predicate) selects elements where condition is TRUE

List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

List<String> result = names.stream()
    .filter(n -> n.length() > 3)
    .collect(Collectors.toList());
// [Alice, Charlie, David]

// Visual:
// Input:   [Alice] [Bob] [Charlie] [David] [Eve]
//            ↓       ↓       ↓         ↓       ↓
// filter:   ✓       ✗       ✓         ✓       ✗
//            ↓               ↓         ↓
// Output:  [Alice]      [Charlie] [David]

// More filter examples
List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Even numbers
nums.stream().filter(n -> n % 2 == 0).collect(toList());  // [2,4,6,8,10]

// Numbers > 5
nums.stream().filter(n -> n > 5).collect(toList());  // [6,7,8,9,10]

// Strings starting with 'A'
names.stream().filter(n -> n.startsWith("A")).collect(toList());  // [Alice]

// Multiple conditions (chain filters)
nums.stream()
    .filter(n -> n > 3)
    .filter(n -> n < 8)
    .filter(n -> n % 2 == 0)
    .collect(toList());  // [4, 6]

// ═══════════════════════════════════════════════════════════════
// MAP - DETAILED
// ═══════════════════════════════════════════════════════════════

// map(Function) transforms each element into something else

List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

List<String> upper = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
// [ALICE, BOB, CHARLIE, DAVID, EVE]

// Visual:
// Input:   [Alice] [Bob] [Charlie] [David] [Eve]
//            ↓       ↓       ↓         ↓       ↓
// map:      ALICE   BOB   CHARLIE   DAVID    EVE
//            ↓       ↓       ↓         ↓       ↓
// Output:  [ALICE] [BOB] [CHARLIE] [DAVID] [EVE]

// More map examples
List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

// Square each number
List<Integer> squares = nums.stream()
    .map(n -> n * n)
    .collect(toList());  // [1, 4, 9, 16, 25]

// Convert to string
List<String> strings = nums.stream()
    .map(n -> "Number: " + n)
    .collect(toList());  // [Number: 1, Number: 2, ...]

// Get object property
List<Integer> lengths = names.stream()
    .map(String::length)
    .collect(toList());  // [5, 3, 7, 5, 3]

// Chain multiple maps
List<String> result = nums.stream()
    .map(n -> n * 2)           // [2, 4, 6, 8, 10]
    .map(n -> "Item-" + n)     // [Item-2, Item-4, Item-6, Item-8, Item-10]
    .collect(toList());

// COMPARISON: filter vs map
┌──────────────────────────────────────────────────────────────┐
│  filter: Input → Select/Reject → Output (same size or less)  │
│          [1,2,3,4,5] → filter(>3) → [4,5]                   │
│                                                              │
│  map:    Input → Transform → Output (same size)              │
│          [1,2,3,4,5] → map(*2) → [2,4,6,8,10]               │
└──────────────────────────────────────────────────────────────┘

// FlatMap - DETAILED
// flatMap converts Stream<Stream<T>> → Stream<T> (flattens nested streams)

// Example 1: Flatten nested lists
List<List<Integer>> nested = Arrays.asList(
    Arrays.asList(1, 2), Arrays.asList(3, 4), Arrays.asList(5, 6));

// WITHOUT flatMap (returns Stream<List<Integer>>)
nested.stream()
    .map(list -> list.stream())  // Stream<Stream<Integer>>
    // Not what we want!

// WITH flatMap (returns Stream<Integer>)
nested.stream()
    .flatMap(list -> list.stream())  // Stream<Integer>
    .collect(Collectors.toList());   // [1, 2, 3, 4, 5, 6]

// Shorter with method reference
nested.stream()
    .flatMap(Collection::stream)
    .collect(Collectors.toList());   // [1, 2, 3, 4, 5, 6]

// Visual:
// BEFORE flatMap:
// ┌─────────┐ ┌─────────┐ ┌─────────┐
// │[1, 2]   │ │[3, 4]   │ │[5, 6]   │   ← Stream of Lists
// └─────────┘ └─────────┘ └─────────┘
//
// AFTER flatMap:
// [1, 2, 3, 4, 5, 6]                     ← Single flattened Stream

// Example 2: Split sentences into words
List<String> sentences = Arrays.asList("Hello World", "Java 8 Features");

List<String> words = sentences.stream()
    .flatMap(s -> Arrays.stream(s.split(" ")))  // Split each sentence
    .collect(Collectors.toList());
// [Hello, World, Java, 8, Features]

// Visual:
// BEFORE: ["Hello World", "Java 8 Features"]
//              ↓ split        ↓ split
// AFTER:  [["Hello","World"], ["Java","8","Features"]]
//              ↓ flatMap flattens all into one stream
// RESULT: ["Hello", "World", "Java", "8", "Features"]

// Example 3: Find all unique characters
List<String> list = Arrays.asList("Hello", "World");

List<Character> chars = list.stream()
    .flatMap(s -> s.chars().mapToObj(c -> (char) c))
    .distinct()
    .collect(Collectors.toList());
// [H, e, l, o, W, r, d]

// Example 4: FlatMap with Optional
Optional<String> opt1 = Optional.of("Hello");
Optional<String> opt2 = Optional.of("World");

Optional<String> result = opt1.flatMap(s1 ->
    opt2.map(s2 -> s1 + " " + s2));
// Optional["Hello World"]

// Example 5: Order items from multiple shops
class Shop { List<String> items; }
List<Shop> shops = Arrays.asList(shop1, shop2, shop3);

List<String> allItems = shops.stream()
    .flatMap(shop -> shop.getItems().stream())
    .collect(Collectors.toList());

// COMPARISON: map vs flatMap
┌──────────────────────────────────────────────────────────────┐
│  map:        Stream<T> → Stream<R>         (1 to 1)         │
│              [1] [2] [3]  →  [10] [20] [30]                │
│                                                              │
│  flatMap:    Stream<Stream<R>> → Stream<R> (many to 1)      │
│              [[1,2],[3,4],[5,6]]  →  [1,2,3,4,5,6]          │
└──────────────────────────────────────────────────────────────┘

// ═══════════════════════════════════════════════════════════════
// SORTED - DETAILED
// ═══════════════════════════════════════════════════════════════

// sorted() - natural order (Comparable)
// sorted(Comparator) - custom order

names.stream().sorted().collect(toList());  // [Alice, Bob, Charlie, David, Eve]

// Custom sort
names.stream()
    .sorted(Comparator.comparingInt(String::length))
    .collect(toList());  // [Bob, Eve, Alice, David, Charlie]

// Reverse sort
nums.stream().sorted(Comparator.reverseOrder()).collect(toList());  // [10,9,8...1]

// ═══════════════════════════════════════════════════════════════
// DISTINCT - DETAILED
// ═══════════════════════════════════════════════════════════════

// distinct() removes duplicates (uses equals/hashCode)

List<Integer> nums = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
nums.stream().distinct().collect(toList());  // [1, 2, 3, 4]

// Visual:
// Input:   [1] [2] [2] [3] [3] [3] [4] [4] [4] [4]
//           ✓   ✓   ✗   ✓   ✗   ✗   ✓   ✗   ✗   ✗
// Output:  [1] [2]      [3]           [4]

// ═══════════════════════════════════════════════════════════════
// LIMIT & SKIP
// ═══════════════════════════════════════════════════════════════

// limit(n) - take first n
nums.stream().limit(3).collect(toList());  // [1, 2, 2]

// skip(n) - skip first n
nums.stream().skip(3).collect(toList());   // [3, 3, 3, 4, 4, 4, 4]

// Pagination
int page = 2, pageSize = 3;
list.stream()
    .skip((page - 1) * pageSize)
    .limit(pageSize)
    .collect(toList());

// ═══════════════════════════════════════════════════════════════
// REDUCE - DETAILED
// ═══════════════════════════════════════════════════════════════

// reduce(BinaryOperator) - combines elements to single value

// Sum
int sum = nums.stream().reduce(0, Integer::sum);  // 30

// Visual:
// [1, 2, 3, 4, 5]
//   ↓  reduce(0, +)
// 0+1=1 → 1+2=3 → 3+3=6 → 6+4=10 → 10+5=15
// Result: 15

// Max
int max = nums.stream().reduce(0, Integer::max);

// Concatenate strings
String joined = names.stream().reduce("", (a, b) -> a + b);

// ═══════════════════════════════════════════════════════════════
// COLLECT - DETAILED
// ═══════════════════════════════════════════════════════════════

// groupingBy - Group elements by classifier
Map<Integer, List<String>> byLength = names.stream()
    .collect(Collectors.groupingBy(String::length));
// {3=[Bob, Eve], 5=[Alice, David], 7=[Charlie]}

// Visual:
// [Alice(5), Bob(3), Charlie(7), David(5), Eve(3)]
//   ↓ groupingBy(length)
// { 3 → [Bob, Eve],
//   5 → [Alice, David],
//   7 → [Charlie] }

// groupingBy + counting
Map<Integer, Long> countByLength = names.stream()
    .collect(Collectors.groupingBy(String::length, Collectors.counting()));
// {3=2, 5=2, 7=1}

// groupingBy + toSet
Map<Character, List<String>> byFirstChar = names.stream()
    .collect(Collectors.groupingBy(n -> n.charAt(0)));
// {A=[Alice], B=[Bob], C=[Charlie], D=[David], E=[Eve]}

// partitioningBy - Divide into exactly 2 groups (true/false)
Map<Boolean, List<String>> partitioned = names.stream()
    .collect(Collectors.partitioningBy(n -> n.length() > 3));
// {false=[Bob, Eve], true=[Alice, Charlie, David]}

// joining
String csv = names.stream().collect(Collectors.joining(", "));
// Alice, Bob, Charlie, David, Eve

String all = names.stream().collect(Collectors.joining());
// AliceBobCharlieDavidEve

String joined = names.stream()
    .collect(Collectors.joining(", ", "[", "]"));
// [Alice, Bob, Charlie, David, Eve]

// toMap
Map<String, Integer> nameLengthMap = names.stream()
    .collect(Collectors.toMap(n -> n, String::length));
// {Alice=5, Bob=3, Charlie=7, David=5, Eve=3}

// counting
long count = names.stream().filter(n -> n.length() > 3).count();  // 3

// summarizingInt
IntSummaryStatistics stats = names.stream()
    .collect(Collectors.summarizingInt(String::length));
// count=5, sum=23, min=3, max=7, average=4.6
int sum = IntStream.rangeClosed(1, 100).reduce(0, Integer::sum);  // 5050

// Grouping
Map<Integer, List<String>> grouped = names.stream()
    .collect(Collectors.groupingBy(String::length));
// {3=[Bob], 4=[Eve], 5=[Alice, David], 7=[Charlie]}

// Partitioning
Map<Boolean, List<String>> partitioned = names.stream()
    .collect(Collectors.partitioningBy(n -> n.length() > 3));
// {false=[Bob, Eve], true=[Alice, Charlie, David]}

// Joining
String joined = names.stream()
    .collect(Collectors.joining(", "));  // Alice, Bob, Charlie, David, Eve
```

### 5.5 Collectors Cheat Sheet

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Collector                  │ Description                     │
    ├──────────────────────────────────────────────────────────────┤
    │  toList()                   │ Collect to List                 │
    │  toSet()                    │ Collect to Set                  │
    │  toMap(keyMapper,valMapper) │ Collect to Map                  │
    │  joining(delimiter)         │ Join strings                    │
    │  groupingBy(Function)       │ Group by classifier             │
    │  partitioningBy(Predicate)  │ Partition into two groups       │
    │  counting()                 │ Count elements                  │
    │  summingInt(ToIntFunction)  │ Sum integers                    │
    │  averagingInt(ToIntFunction)│ Average of integers             │
    │  reducing(BinaryOperator)   │ Reduce to single value          │
    │  collectingAndThen(down,fn) │ Collect then transform          │
    │  toUnmodifiableList()       │ Immutable list (Java 10+)       │
    │  toUnmodifiableMap()        │ Immutable map (Java 10+)        │
    └──────────────────────────────────────────────────────────────┘
```

### 5.6 Parallel Stream

```java
// Parallel stream (uses ForkJoinPool)
List<Integer> nums = IntStream.rangeClosed(1, 1000).boxed().toList();

// Parallel
long count = nums.parallelStream()
    .filter(n -> n % 2 == 0)
    .count();

// Or convert to parallel
nums.stream().parallel().filter(n -> n % 2 == 0).count();

// Sequential
nums.stream().sequential().filter(n -> n % 2 == 0).count();
```

---

## 6. OPTIONAL

### 6.1 What is Optional?

```
    Optional = Container that may or may not contain a value
    Prevents NullPointerException

    ┌──────────────────────────────────────────────────────────────┐
    │  WITHOUT Optional:                                           │
    │  String name = user.getAddress().getCity().getName();        │
    │  // NullPointerException if any is null!                     │
    │                                                              │
    │  WITH Optional:                                              │
    │  Optional.ofNullable(user)                                   │
    │      .map(User::getAddress)                                  │
    │      .map(Address::getCity)                                  │
    │      .map(City::getName)                                     │
    │      .orElse("Unknown");                                     │
    │  // Returns "Unknown" if any is null!                        │
    └──────────────────────────────────────────────────────────────┘
```

### 6.2 Optional Methods

```java
// Creating Optional
Optional<String> empty = Optional.empty();
Optional<String> present = Optional.of("Hello");
Optional<String> nullable = Optional.ofNullable(null);

// Checking
present.isPresent();     // true
empty.isPresent();       // false
present.isEmpty();       // false (Java 11+)

// Getting
present.get();           // "Hello"
empty.get();             // NoSuchElementException!

// OrElse
present.orElse("Default");   // "Hello"
empty.orElse("Default");     // "Default"

// OrElseGet
empty.orElseGet(() -> computeDefault());  // Lazy evaluation

// OrElseThrow
empty.orElseThrow(() -> new RuntimeException("Empty!"));

// IfPresent
present.ifPresent(s -> System.out.println(s));

// Map
Optional<Integer> len = present.map(String::length);  // Optional[5]

// FlatMap
Optional<String> flat = present.flatMap(s -> Optional.of(s.toUpperCase()));

// Filter
Optional<String> filtered = present.filter(s -> s.startsWith("H"));  // present
Optional<String> filtered2 = present.filter(s -> s.startsWith("X")); // empty
```

### 6.3 Optional Visual

```
    Optional.ofNullable(value)
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  value != null ──→ Optional.of(value) ──→ Optional[PRESENT] │
    │                                                              │
    │  value == null ──→ Optional.empty() ──→ Optional[EMPTY]     │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘

    Usage:
    Optional.ofNullable(user)      → Optional[User] or Optional[empty]
        .map(User::getName)        → Optional[String] or Optional[empty]
        .orElse("Unknown")         → "John" or "Unknown"
```

---

## 7. DEFAULT METHODS IN INTERFACES

```java
interface Vehicle {
    void start();  // Abstract method

    // Default method (Java 8+)
    default void honk() {
        System.out.println("Beep!");
    }

    // Static method (Java 8+)
    static boolean isElectric(Vehicle v) {
        return v instanceof ElectricCar;
    }
}

class Car implements Vehicle {
    public void start() {
        System.out.println("Car started");
    }
    // honk() inherited from interface (optional to override)
}
```

---

## 8. DATE/TIME API (java.time)

```java
// Old (bad): java.util.Date, java.util.Calendar
// New (good): java.time package (Java 8+)

// LocalDate
LocalDate today = LocalDate.now();
LocalDate birthday = LocalDate.of(1990, Month.MAY, 15);
int day = today.getDayOfMonth();
Month month = today.getMonth();
int year = today.getYear();

// LocalTime
LocalTime now = LocalTime.now();
LocalTime time = LocalTime.of(14, 30, 0);

// LocalDateTime
LocalDateTime dateTime = LocalDateTime.now();

// Duration
Duration duration = Duration.between(startTime, endTime);
long minutes = duration.toMinutes();

// Period
Period period = Period.between(birthday, today);
int years = period.getYears();

// Formatting
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
String formatted = today.format(fmt);
LocalDate parsed = LocalDate.parse("15/05/1990", fmt);
```

---

## 9. COMPLETABLEFUTURE

```java
// Asynchronous programming
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> fetchData())          // Async
    .thenApply(data -> processData(data))    // Transform
    .thenAccept(result -> save(result))      // Consume
    .exceptionally(e -> {                    // Handle error
        e.printStackTrace();
        return null;
    });

// Blocking
String result = future.get();

// Combine two futures
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "World");
CompletableFuture<String> combined = f1.thenCombine(f2, (a, b) -> a + " " + b);
// "Hello World"
```

---

## 10. STRING METHODS (Java 8+)

```java
// join
String joined = String.join("-", "2024", "01", "15");  // "2024-01-15"

// splits (Java 8+)
"hello".chars().mapToObj(c -> (char) c).toList();  // [h,e,l,l,o]

// strip (Java 11+)
"  hello  ".strip();      // "hello"
"  hello  ".stripLeading();  // "hello  "
"  hello  ".stripTrailing();  // "  hello"

// repeat (Java 11+)
"ha".repeat(3);  // "hahaha"

// isBlank (Java 11+)
"  ".isBlank();  // true
```

---

## 11. JAVA 8 FEATURES CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║                JAVA 8 FEATURES CHEAT SHEET                       ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  LAMBDA:   (params) -> expression / { statements; }             ║
║  METHOD REF: ClassName::methodName / object::method              ║
║                                                                  ║
║  FUNCTIONAL INTERFACES:                                          ║
║  Predicate<T>     T → boolean     (filter, test)                 ║
║  Consumer<T>      T → void        (forEach, accept)              ║
║  Function<T,R>    T → R           (map, apply)                   ║
║  Supplier<T>      () → T          (get, create)                  ║
║  UnaryOperator<T> T → T           (same type transform)          ║
║  BinaryOperator<T> T,T → T        (combine)                      ║
║                                                                  ║
║  STREAM:                                                         ║
║  filter(Predicate)   map(Function)     flatMap(Function)         ║
║  sorted()            distinct()        limit(n)    skip(n)       ║
║  forEach(Consumer)   reduce(BinaryOp)  count()                  ║
║  collect(Collector)  anyMatch()        allMatch()   noneMatch()  ║
║  findFirst()         findAny()         min()    max()            ║
║                                                                  ║
║  COLLECTORS:                                                     ║
║  toList() toSet() toMap() joining()                             ║
║  groupingBy() partitioningBy() counting()                       ║
║  summingInt() averagingInt() reducing()                         ║
║                                                                  ║
║  OPTIONAL:                                                       ║
║  of(value) empty() ofNullable(null)                              ║
║  get() isPresent() isEmpty()                                    ║
║  orElse() orElseGet() orElseThrow()                            ║
║  map() flatMap() filter() ifPresent()                          ║
║                                                                  ║
║  DEFAULT METHODS:  default void method() { }                    ║
║  STATIC METHODS:  static void method() { }                      ║
║                                                                  ║
║  DATE/TIME: LocalDate LocalTime LocalDateTime                    ║
║  Duration Period DateTimeFormatter                              ║
║                                                                  ║
║  COMPLETABLEFUTURE: supplyAsync thenApply thenAccept            ║
║  exceptionally thenCombine get()                                ║
║                                                                  ║
║  ⚠️  Lambda requires functional interface (SAM)                  ║
║  ⚠️  Stream is ONE-TIME USE (can't reuse)                        ║
║  ⚠️  Optional prevents NPE, don't use for every variable        ║
║  ⚠️  Method reference = shorthand for simple lambdas             ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 12. JAVA 8 INTERVIEW QUESTIONS (40+)

### ⭐ BASIC

**Q1: What are new features in Java 8?**
> Lambda, Stream API, Optional, Functional Interfaces, Method References, Default Methods, Date/Time API, CompletableFuture.

**Q2: What is Lambda expression?**
> Anonymous function (no name, no return type, no modifiers). Enables functional programming.

**Q3: What is functional interface?**
> Interface with exactly one abstract method. Can have multiple default/static methods. @FunctionalInterface annotation optional.

**Q4: What is method reference?**
> Shorthand for lambda that only calls an existing method. ClassName::methodName.

**Q5: What is Stream API?**
> Sequence of elements supporting functional operations. Not a data structure. Lazy, functional, one-time use.

**Q6: What is Optional?**
> Container that may or may not contain a value. Prevents NullPointerException.

**Q7: What is difference between map and flatMap?**
```
map:      One-to-one transformation
flatMap:  One-to-many, flattens nested streams
```

**Q8: What is difference between filter and map?**
```
filter: Selects elements (boolean condition)
map:    Transforms elements (returns new value)
```

**Q9: What is the difference between findFirst and findAny?**
```
findFirst: Returns first element (ordered, deterministic)
findAny:   Returns any element (faster in parallel)
```

**Q10: What is difference between reduce and collect?**
```
reduce: Combines elements to single value ( BinaryOperator)
collect: Uses Collector to accumulate (toList, toMap, etc.)
```

---

### ⭐⭐ MIDDLE

**Q11: What is difference between Lambda and Anonymous class?**
```
Lambda: Functional interface only, implicit this, concise
Anonymous: Any interface, explicit this, verbose
```

**Q12: What is difference between Predicate and Function?**
```
Predicate<T>: T → boolean (condition check)
Function<T,R>: T → R (transformation)
```

**Q13: What is the difference between map and flatMap in Stream?**
```java
// map: One-to-one
Stream.of("a", "b").map(s -> s.toUpperCase());  // [A, B]

// flatMap: One-to-many, flattens
Stream.of("ab", "cd").flatMap(s -> s.chars().mapToObj(c -> (char)c));
// [a, b, c, d]
```

**Q14: What is difference between map and flatMap in Optional?**
```java
// map: Transform value
Optional.of("hello").map(String::toUpperCase);  // Optional[HELLO]

// flatMap: Transform to Optional
Optional.of("hello").flatMap(s -> Optional.of(s.toUpperCase())); // Optional[HELLO]
```

**Q15: What is the difference between forEach and map?**
```
forEach: Terminal, consumes elements (void return)
map: Intermediate, transforms elements (returns Stream)
```

**Q16: What is difference between parallel and sequential stream?**
```
Sequential: Single thread, ordered, simpler
Parallel: Multiple threads, may be unordered, faster for large data
```

**Q17: What is the difference between toList and toUnmodifiableList?**
```
toList(): Mutable list (can modify)
toUnmodifiableList(): Immutable list (throws exception on modify)
```

**Q18: What is the difference between orElse and orElseGet?**
```
orElse(value):     Always evaluates the default value
orElseGet(Supplier): Lazy, only evaluates if empty
```

**Q19: What is the difference between map and peek in Stream?**
```
map: Transforms elements (functional)
peek: Debug only, no transformation (side effect)
```

**Q20: What is the difference between limit and skip?**
```
limit(n): Take first n elements
skip(n):  Skip first n elements
```

**Q21: What is difference between allMatch, anyMatch, noneMatch?**
```
allMatch(Predicate):    All elements match → boolean
anyMatch(Predicate):    At least one matches → boolean
noneMatch(Predicate):   No elements match → boolean
```

**Q22: What is the difference between collect and reduce?**
```
collect: Uses Collector (toList, groupingBy, joining)
reduce: Uses BinaryOperator (sum, max, concat)
```

**Q23: What is the difference between groupingBy and partitioningBy?**
```
groupingBy: Groups by classifier (many groups)
partitioningBy: Divides into exactly 2 groups (true/false)
```

**Q24: What is the difference between default and static method in interface?**
```
default: Instance method, can override, inherited
static: Cannot override, called via InterfaceName.method()
```

**Q25: What is the difference between Optional and null check?**
```
Optional: Explicit, chainable, no NPE risk
null: Implicit, error-prone, NPE risk
```

---

### ⭐⭐⭐ ADVANCED

**Q26: What is method reference types?**
```
1. Static:     ClassName::staticMethod
2. Instance:   object::instanceMethod
3. Arbitrary:  ClassName::instanceMethod (on parameter)
4. Constructor: ClassName::new
```

**Q27: What is the difference between map and flatMap in depth?**
```java
// map wraps in container
Stream.of(1, 2, 3).map(x -> x * x);  // [1, 4, 9]

// flatMap flattens containers
Stream.of(1, 2, 3).flatMap(x -> Stream.of(x, x*2));  // [1, 2, 2, 4, 3, 6]
```

**Q28: What is Stream laziness?**
```java
// Intermediate operations are DEFERRED
Stream<Integer> stream = list.stream()
    .filter(n -> { System.out.println("Filter: " + n); return n > 3; })
    .map(n -> { System.out.println("Map: " + n); return n * 2; });

// Nothing happens until terminal operation!
stream.collect(toList());  // Now it executes
```

**Q29: What is the difference between stream and parallelStream?**
```
stream(): Sequential, single thread, ordered
parallelStream(): Parallel, ForkJoinPool, may be unordered
Use parallelStream for large data, CPU-intensive operations
```

**Q30: What is Collector?**
```java
// Interface for accumulating elements
Collector<T, A, R> where:
T: input type
A: accumulator type
R: result type

// Custom collector
Collector<String, StringBuilder, String> joinCollector = Collector.of(
    StringBuilder::new,          // Supplier
    StringBuilder::append,       // Accumulator
    (a, b) -> a.append(b),       // Combiner
    StringBuilder::toString      // Finisher
);
```

**Q31: What is the difference betweenSupplier and Function?**
```
Supplier<T>: () → T (no input, produces value)
Function<T,R>: T → R (takes input, produces output)
```

**Q32: What is BiFunction?**
```java
BiFunction<String, Integer, String> repeat = (s, n) -> s.repeat(n);
repeat.apply("Ha", 3);  // "HaHaHa"
```

**Q33: What is the difference between UnaryOperator and Function?**
```
UnaryOperator<T>: T → T (same type in and out)
Function<T,T>: T → T (same thing, but UnaryOperator is clearer)
```

**Q34: What is the difference between collect(toList()) and toList()?**
```
collect(Collectors.toList()): Standard, mutable
toList(): Java 16+, returns unmodifiable list
```

**Q35: What is the difference between Stream.of and Arrays.stream?**
```java
Stream.of(1, 2, 3);           // From varargs
Arrays.stream(new int[]{1,2,3}); // From array
```

**Q36: What is the difference between Stream.empty and Stream.of?**
```java
Stream.empty();     // Empty stream
Stream.of();        // Also empty
Stream.of(1, 2, 3); // Stream with elements
```

**Q37: What is the difference between takeWhile and filter?**
```
takeWhile: Stops at first false (short-circuit)
filter: Processes ALL elements
```

**Q38: What is CompletableFuture used for?**
```java
// Non-blocking async programming
CompletableFuture.supplyAsync(() -> fetchData())
    .thenApply(data -> process(data))
    .thenAccept(result -> save(result))
    .exceptionally(e -> handleError(e));
```

**Q39: What is the difference between CompletableFuture and Future?**
```
Future: Blocking get(), no chaining, no composition
CompletableFuture: Non-blocking, chaining, composition, exception handling
```

**Q40: Practical scenario - When to use what?**
```
┌───────────────────────────────┬──────────────────────────────────┐
│ Scenario                       │ Use                              │
├───────────────────────────────┼──────────────────────────────────┤
│ Simple callback                │ Lambda                           │
│ Replace anonymous class        │ Lambda                           │
│ Filter collection              │ Stream.filter()                  │
│ Transform collection           │ Stream.map()                     │
│ Avoid NPE                      │ Optional                         │
│ Async processing               │ CompletableFuture                │
│ Format date                    │ DateTimeFormatter                │
│ Group data                     │ Collectors.groupingBy()          │
│ Join strings                   │ Collectors.joining()             │
│ Count elements                 │ Stream.count()                   │
│ Check condition                │ Predicate                        │
│ Side effects                    │ Consumer / Stream.peek()         │
└───────────────────────────────┴──────────────────────────────────┘
```

---

## 13. QUICK REFERENCE - ALL EXAMPLES

```java
// Lambda
list.forEach(x -> System.out.println(x));
list.sort((a, b) -> a - b);
button.addActionListener(e -> click());

// Method Reference
list.forEach(System.out::println);
list.stream().map(String::toUpperCase);
Stream.generate(Math::random);

// Stream
list.stream().filter(x -> x > 5).map(x -> x * 2).collect(toList());
list.stream().sorted().distinct().limit(10).collect(toList());
list.stream().collect(groupingBy(String::length));
list.stream().collect(joining(", "));
list.stream().reduce(0, Integer::sum);

// Optional
Optional.ofNullable(value).orElse("default");
Optional.ofNullable(value).map(String::toUpperCase).orElse("");
Optional.ofNullable(value).ifPresent(System.out::println);

// CompletableFuture
CompletableFuture.supplyAsync(() -> get())
    .thenApply(x -> process(x))
    .thenAccept(x -> save(x))
    .get();
```

---

*Last Updated: September 2026*
*Covers: Lambda, Stream, Optional, Functional Interfaces, Method References, Interview Questions*
