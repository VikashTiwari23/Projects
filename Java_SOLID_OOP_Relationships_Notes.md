# SOLID Principles + OOP Relationships (Complete Interview Notes)

---

## PART 1: SOLID PRINCIPLES

---

## 1. S — SINGLE RESPONSIBILITY PRINCIPLE (SRP)

```
    "A class should have only ONE reason to change"
    "One class = One job"

    ┌──────────────────────────────────────────────────────────────┐
    │  BEFORE (Violation):                                         │
    │  class Employee {                                            │
    │      void calculateSalary() { ... }                          │
    │      void saveToDatabase() { ... }   ← TWO responsibilities │
    │      void generateReport() { ... }   ← THREE!               │
    │  }                                                           │
    │                                                              │
    │  AFTER (Followed):                                           │
    │  class Employee {                                            │
    │      void calculateSalary() { ... }                          │
    │  }                                                           │
    │  class EmployeeRepository {                                  │
    │      void saveToDatabase(Employee e) { ... }                 │
    │  }                                                           │
    │  class ReportGenerator {                                     │
    │      void generateReport(Employee e) { ... }                 │
    │  }                                                           │
    └──────────────────────────────────────────────────────────────┘

    WHY?
    ┌──────────────────────────────────────────────────────────────┐
    │  - Change salary logic → only Employee class changes         │
    │  - Change DB logic → only Repository class changes           │
    │  - Change report format → only ReportGenerator changes       │
    │  - Easier to test, understand, maintain                      │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. O — OPEN/CLOSED PRINCIPLE (OCP)

```
    "Classes should be OPEN for extension, CLOSED for modification"
    "Add new behavior WITHOUT changing existing code"

    ┌──────────────────────────────────────────────────────────────┐
    │  BEFORE (Violation): Every new shape = modify area method    │
    │  class AreaCalculator {                                      │
    │      double calculate(Object shape) {                        │
    │          if (shape instanceof Circle) { ... }                │
    │          else if (shape instanceof Rectangle) { ... }        │
    │          else if (shape instanceof Triangle) { ... }  ← BAD │
    │      }                                                       │
    │  }                                                           │
    │                                                              │
    │  AFTER (Followed):                                           │
    │  interface Shape {                                           │
    │      double area();                                          │
    │  }                                                           │
    │  class Circle implements Shape {                             │
    │      public double area() { return Math.PI * r * r; }        │
    │  }                                                           │
    │  class Rectangle implements Shape {                          │
    │      public double area() { return w * h; }                  │
    │  }                                                           │
    │  class AreaCalculator {                                      │
    │      double calculate(Shape shape) {                         │
    │          return shape.area();  ← NO modification needed!     │
    │      }                                                       │
    │  }                                                           │
    └──────────────────────────────────────────────────────────────┘

    Adding Triangle:
    ┌──────────────────────────────────────────────────────────────┐
    │  class Triangle implements Shape {                           │
    │      public double area() { return 0.5 * base * height; }   │
    │  }                                                           │
    │  // AreaCalculator NOT changed! ✅                            │
    └──────────────────────────────────────────────────────────────┘
```

---

## 3. L — LISKOV SUBSTITUTION PRINCIPLE (LSP)

```
    "Subtypes must be substitutable for their base types"
    "If S is a subtype of T, then S should work wherever T is expected"

    ┌──────────────────────────────────────────────────────────────┐
    │  BEFORE (Violation):                                         │
    │  class Bird {                                                │
    │      void fly() { ... }                                      │
    │  }                                                           │
    │  class Ostrich extends Bird {                                │
    │      void fly() {                                            │
    │          throw new UnsupportedOperationException(); ← VIOLATION│
    │      }                                                       │
    │  }                                                           │
    │                                                              │
    │  // Ostrich CANNOT substitute Bird (can't fly!)              │
    └──────────────────────────────────────────────────────────────┘

    ┌──────────────────────────────────────────────────────────────┐
    │  AFTER (Followed):                                           │
    │  interface Bird { ... }                                      │
    │  interface FlyingBird extends Bird {                         │
    │      void fly();                                             │
    │  }                                                           │
    │  class Sparrow implements FlyingBird {                       │
    │      public void fly() { ... }                               │
    │  }                                                           │
    │  class Ostrich implements Bird {  ← NOT FlyingBird          │
    │      // No fly() method required                             │
    │  }                                                           │
    │                                                              │
    │  // Ostrich can substitute Bird ✅ (no fly() expected)       │
    │  // Sparrow can substitute FlyingBird ✅                     │
    └──────────────────────────────────────────────────────────────┘

    REAL EXAMPLE:
    ┌──────────────────────────────────────────────────────────────┐
    │  // Rectangle-Square problem (LSP violation)                 │
    │  class Rectangle {                                           │
    │      int width, height;                                      │
    │      void setWidth(int w) { width = w; }                    │
    │      void setHeight(int h) { height = h; }                  │
    │  }                                                           │
    │  class Square extends Rectangle {                            │
    │      void setWidth(int w) { width = height = w; } ← CHANGES │
    │      void setHeight(int h) { width = height = h; } ← BEHAVIOR│
    │  }                                                           │
    │                                                              │
    │  void resize(Rectangle r) {                                  │
    │      r.setWidth(5);                                          │
    │      r.setHeight(10);                                        │
    │      // For Rectangle: w=5, h=10 ✅                          │
    │      // For Square: w=10, h=10 ❌ (unexpected!)              │
    │  }                                                           │
    └──────────────────────────────────────────────────────────────┘
```

---

## 4. I — INTERFACE SEGREGATION PRINCIPLE (ISP)

```
    "Clients should NOT be forced to depend on methods they don't use"
    "Fat interfaces → Split into smaller, specific interfaces"

    ┌──────────────────────────────────────────────────────────────┐
    │  BEFORE (Violation):                                         │
    │  interface Worker {                                          │
    │      void work();                                            │
    │      void eat();                                             │
    │      void sleep();                                           │
    │  }                                                           │
    │  class Robot implements Worker {                             │
    │      public void work() { ... }                              │
    │      public void eat() { }   ← Robot doesn't eat!           │
    │      public void sleep() { } ← Robot doesn't sleep!         │
    │  }                                                           │
    └──────────────────────────────────────────────────────────────┘

    ┌──────────────────────────────────────────────────────────────┐
    │  AFTER (Followed):                                           │
    │  interface Workable {                                        │
    │      void work();                                            │
    │  }                                                           │
    │  interface Feedable {                                        │
    │      void eat();                                             │
    │  }                                                           │
    │  interface Sleepable {                                       │
    │      void sleep();                                           │
    │  }                                                           │
    │                                                              │
    │  class Human implements Workable, Feedable, Sleepable {      │
    │      public void work() { ... }                              │
    │      public void eat() { ... }                               │
    │      public void sleep() { ... }                             │
    │  }                                                           │
    │  class Robot implements Workable { ← ONLY what it needs     │
    │      public void work() { ... }                              │
    │  }                                                           │
    └──────────────────────────────────────────────────────────────┘
```

---

## 5. D — DEPENDENCY INVERSION PRINCIPLE (DIP)

```
    "High-level modules should NOT depend on low-level modules"
    "Both should depend on ABSTRACTIONS"
    "Abstractions should NOT depend on details"
    "Details should depend on abstractions"

    ┌──────────────────────────────────────────────────────────────┐
    │  BEFORE (Violation):                                         │
    │  class MySQLDatabase {                                       │
    │      void save(String data) { ... }                          │
    │  }                                                           │
    │  class UserService {                                         │
    │      MySQLDatabase db = new MySQLDatabase(); ← DIRECT DEPEND │
    │      void saveUser(String user) {                            │
    │          db.save(user);                                      │
    │      }                                                       │
    │  }                                                           │
    │  // Want to switch to MongoDB? Must change UserService! ❌   │
    └──────────────────────────────────────────────────────────────┘

    ┌──────────────────────────────────────────────────────────────┐
    │  AFTER (Followed):                                           │
    │  interface Database {                                        │
    │      void save(String data);                                 │
    │  }                                                           │
    │  class MySQLDatabase implements Database {                   │
    │      public void save(String data) { ... }                   │
    │  }                                                           │
    │  class MongoDatabase implements Database {                   │
    │      public void save(String data) { ... }                   │
    │  }                                                           │
    │  class UserService {                                         │
    │      Database db;  ← depends on abstraction                  │
    │      UserService(Database db) { this.db = db; } ← Injection │
    │      void saveUser(String user) {                            │
    │          db.save(user);                                      │
    │      }                                                       │
    │  }                                                           │
    │  // Switch DB: only constructor call changes ✅              │
    │  new UserService(new MySQLDatabase());                       │
    │  new UserService(new MongoDatabase());                       │
    └──────────────────────────────────────────────────────────────┘
```

---

## 6. SOLID SUMMARY TABLE

```
    ┌──────────┬──────────────────────────────────────────────────┐
    │ Principle│ Key Idea                                         │
    ├──────────┼──────────────────────────────────────────────────┤
    │ SRP      │ One class, one job                               │
    │ OCP      │ Extend behavior, don't modify existing code      │
    │ LSP      │ Subtypes must be usable as parent type           │
    │ ISP      │ Small interfaces > fat interfaces                │
    │ DIP      │ Depend on abstractions, not concrete classes     │
    └──────────┴──────────────────────────────────────────────────┘

    MEMORY TRICK: "SoLDID"
    S - Single responsibility
    O - Open for extension, closed for modification
    L - Liskov substitution
    I - Interface segregation
    D - Dependency inversion
```

---

---

# PART 2: OOP RELATIONSHIPS

---

## 7. RELATIONSHIP TYPES OVERVIEW

```
    ┌──────────────────────────────────────────────────────────────┐
    │  OOP Relationships (Strongest → Weakest):                     │
    │                                                              │
    │  1. INHERITANCE          ──────── IS-A                        │
    │  2. COMPOSITION          ──────── HAS-A (owns, lifecycle)    │
    │  3. AGGREGATION          ──────── HAS-A (uses, independent)  │
    │  4. ASSOCIATION          ──────── KNOWS-A (uses temporarily) │
    │  5. DEPENDENCY           ──────── USES-A (method parameter)  │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘

    Strength:
    ┌──────────┬──────────────────────────────────────────────────┐
    │ Relation  │ Coupling  │ Lifecycle     │ Example              │
    ├──────────┼──────────────────────────────────────────────────┤
    │ Inheritance│ Tightest │ Parent-child  │ Dog IS-A Animal      │
    │ Composition│ Very Tight│ Owner owns  │ Engine HAS-A Car     │
    │ Aggregation│ Tight    │ Shared, indep │ Student HAS-A Dept   │
    │ Association│ Moderate │ Temporary     │ Doctor KNOWS Patient │
    │ Dependency │ Loosest  │ Method param  │ Order USES Payment   │
    └──────────┴──────────────────────────────────────────────────┘
```

---

## 8. INHERITANCE (IS-A)

```
    "Subclass IS-A Superclass"
    "Strongest relationship — child inherits parent"

    ┌──────────────────────────────────────────────────────────────┐
    │  class Animal {                                              │
    │      String name;                                            │
    │      void eat() { ... }                                      │
    │  }                                                           │
    │                                                              │
    │  class Dog extends Animal {                                  │
    │      void bark() { ... }                                     │
    │  }                                                           │
    │                                                              │
    │  // Dog IS-A Animal ✅                                        │
    │  // Dog inherits: name, eat()                                │
    │  // Dog adds: bark()                                         │
    └──────────────────────────────────────────────────────────────┘

    Visual:
    ┌─────────┐
    │ Animal  │
    │─────────│
    │ name    │
    │ eat()   │
    └────┬────┘
         │ extends
         ▼
    ┌─────────┐
    │   Dog   │
    │─────────│
    │ bark()  │
    └─────────┘

    Rules:
    ┌──────────────────────────────────────────────────────────────┐
    │  - Multiple inheritance NOT supported (Java)                 │
    │  - A class can extend only ONE class                         │
    │  - Interfaces allow multiple inheritance of type             │
    │  - Constructor chaining: super() called implicitly           │
    │  - Use IS-A test: "Dog IS-A Animal" → true = inheritance     │
    └──────────────────────────────────────────────────────────────┘
```

---

## 9. COMPOSITION (HAS-A — Owns)

```
    "Class A COMPOSES Class B"
    "B cannot exist without A (lifecycle coupled)"
    "If A is destroyed, B is destroyed"
    "Strongest HAS-A relationship"

    ┌──────────────────────────────────────────────────────────────┐
    │  class Car {                                                 │
    │      private Engine engine;  ← Car OWNS Engine               │
    │                                                              │
    │      Car() {                                                 │
    │          this.engine = new Engine();  ← CREATED inside       │
    │      }                                                       │
    │  }                                                           │
    │                                                              │
    │  class Engine {                                              │
    │      void start() { ... }                                    │
    │  }                                                           │
    │                                                              │
    │  // Engine CANNOT exist without Car                          │
    │  // If Car is destroyed → Engine is destroyed too            │
    └──────────────────────────────────────────────────────────────┘

    Visual:
    ┌─────────┐    ┌─────────┐
    │   Car   │◆───│ Engine  │
    │─────────│    │─────────│
    │ engine  │    │ start() │
    │ start() │    └─────────┘
    └─────────┘
     ◆─── Filled diamond = COMPOSITION
     Engine is created inside Car
     Engine's lifecycle = Car's lifecycle

    Key Characteristics:
    ┌──────────────────────────────────────────────────────────────┐
    │  - Child (Engine) cannot exist without Parent (Car)          │
    │  - Parent owns the child completely                          │
    │  - If parent dies, child dies                                │
    │  - Child is created inside parent                            │
    │  - Strong coupling                                           │
    └──────────────────────────────────────────────────────────────┘

    Java Example:
    ┌──────────────────────────────────────────────────────────────┐
    │  class University {                                          │
    │      private List<Department> departments;                   │
    │                                                              │
    │      University() {                                          │
    │          departments = new ArrayList<>();  ← Created here    │
    │          departments.add(new Department("CSE"));             │
    │          departments.add(new Department("ECE"));             │
    │      }                                                       │
    │  }                                                           │
    │                                                              │
    │  // When University is destroyed, Departments destroyed too  │
    │  // Departments don't make sense without University          │
    └──────────────────────────────────────────────────────────────┘
```

---

## 10. AGGREGATION (HAS-A — Shares)

```
    "Class A AGGREGATES Class B"
    "B can exist independently of A"
    "B is shared between multiple A's"
    "Weaker form of composition"

    ┌──────────────────────────────────────────────────────────────┐
    │  class Department {                                          │
    │      String name;                                            │
    │  }                                                           │
    │                                                              │
    │  class University {                                          │
    │      private List<Department> departments;                   │
    │                                                              │
    │      University(List<Department> depts) {                    │
    │          this.departments = depts;  ← PASSED in, not created│
    │      }                                                       │
    │  }                                                           │
    │                                                              │
    │  // Department CAN exist without University                  │
    │  // Same Department can be in multiple Universities          │
    │  // If University is destroyed, Department still exists      │
    └──────────────────────────────────────────────────────────────┘

    Visual:
    ┌─────────┐    ┌─────────┐
    │University│◇───│Department│
    │─────────│    │─────────│
    │ depts[] │    │ name    │
    └─────────┘    └─────────┘
     ◇─── Empty diamond = AGGREGATION
     Department passed from outside
     Department exists independently

    Key Characteristics:
    ┌──────────────────────────────────────────────────────────────┐
    │  - Child (Department) CAN exist without Parent (University)  │
    │  - Child is PASSED to parent, not created inside             │
    │  - If parent dies, child still lives                         │
    │  - Child can be shared between multiple parents              │
    │  - Weaker coupling than composition                          │
    └──────────────────────────────────────────────────────────────┘

    Java Example:
    ┌──────────────────────────────────────────────────────────────┐
    │  class Teacher {                                             │
    │      String name;                                            │
    │  }                                                           │
    │                                                              │
    │  class School {                                              │
    │      private List<Teacher> teachers;                         │
    │                                                              │
    │      School(List<Teacher> teachers) {                        │
    │          this.teachers = teachers;  ← External reference     │
    │      }                                                       │
    │  }                                                           │
    │                                                              │
    │  Teacher t1 = new Teacher("Amit");                           │
    │  School s1 = new School(List.of(t1));                        │
    │  School s2 = new School(List.of(t1));  ← Shared teacher!    │
    │  // t1 exists independently of s1 and s2                     │
    └──────────────────────────────────────────────────────────────┘
```

---

## 11. ASSOCIATION (KNOWS-A)

```
    "Class A ASSOCIATES with Class B"
    "A knows about B, uses it temporarily"
    "No ownership, no lifecycle control"
    "Can be bidirectional"

    ┌──────────────────────────────────────────────────────────────┐
    │  class Doctor {                                              │
    │      String name;                                            │
    │      List<Patient> patients;  ← Doctor KNOWS patients        │
    │  }                                                           │
    │                                                              │
    │  class Patient {                                             │
    │      String name;                                            │
    │      Doctor doctor;  ← Patient KNOWS doctor (bidirectional)  │
    │  }                                                           │
    │                                                              │
    │  // Doctor and Patient exist independently                    │
    │  // Relationship is temporary (during treatment)             │
    └──────────────────────────────────────────────────────────────┘

    Visual:
    ┌─────────┐         ┌─────────┐
    │ Doctor  │─────────│ Patient │
    │─────────│         │─────────│
    │ patients│         │ doctor  │
    └─────────┘         └─────────┘
     ────────── Simple line = ASSOCIATION
     Both exist independently
     Relationship is bidirectional

    Types of Association:
    ┌──────────────────────────────────────────────────────────────┐
    │  One-to-One:     Doctor ↔ Patient (one specific)             │
    │  One-to-Many:    Doctor → [Patient, Patient, ...]            │
    │  Many-to-Many:   [Doctor] ↔ [Patient]                        │
    └──────────────────────────────────────────────────────────────┘
```

---

## 12. DEPENDENCY (USES-A)

```
    "Class A DEPENDS on Class B"
    "A uses B temporarily (as method parameter or local variable)"
    "Weakest relationship"
    "B is not a field of A"

    ┌──────────────────────────────────────────────────────────────┐
    │  class OrderService {                                        │
    │      void processOrder(Order order) {  ← Parameter dependency│
    │          // Uses order temporarily                           │
    │          order.calculateTotal();                             │
    │      }                                                       │
    │  }                                                           │
    │                                                              │
    │  class ReportService {                                       │
    │      void generateReport(Employee emp) {  ← Param dependency │
    │          // Uses emp temporarily                             │
    │      }                                                       │
    │  }                                                           │
    │                                                              │
    │  // OrderService does NOT own Order                          │
    │  // Order is just a parameter                                │
    │  // Weakest form of relationship                             │
    └──────────────────────────────────────────────────────────────┘

    Visual:
    ┌───────────────┐     ┌───────┐
    │ OrderService  │- - -│ Order │
    └───────────────┘     └───────┘
     - - - - - - - Dashed line = DEPENDENCY
     Uses Order as method parameter
     No field reference
```

---

## 13. COMPOSITION vs AGGREGATION (KEY DIFFERENCE)

```
    ┌──────────────────────┬──────────────────────┬──────────────────────┐
    │ Feature               │ Composition          │ Aggregation          │
    ├──────────────────────┼──────────────────────┼──────────────────────┤
    │ Relationship          │ HAS-A (owns)         │ HAS-A (uses)         │
    │ Lifecycle             │ Coupled              │ Independent          │
    │ Child creation        │ Inside parent        │ Passed from outside  │
    │ If parent dies        │ Child dies too       │ Child survives       │
    │ Sharing               │ Cannot share         │ Can share            │
    │ Coupling              │ Strong               │ Moderate             │
    │ Diamond symbol        │ ◆ Filled             │ ◇ Empty              │
    │ Example               │ Car → Engine         │ University → Dept    │
    │ Code pattern          │ new Engine() inside  │ passed via constructor│
    │ Independence          │ Child depends on     │ Child works without  │
    │                       │ parent               │ parent               │
    └──────────────────────┴──────────────────────┴──────────────────────┘

    Quick Test:
    ┌──────────────────────────────────────────────────────────────┐
    │  "Does Engine make sense without Car?"                       │
    │  → NO = Composition (◆)                                      │
    │                                                              │
    │  "Does Department make sense without University?"            │
    │  → YES = Aggregation (◇)                                     │
    └──────────────────────────────────────────────────────────────┘
```

---

## 14. ALL RELATIONSHIPS COMPARISON

```
    ┌──────────────────────┬──────────────┬──────────────┬──────────────┬──────────────┐
    │ Feature               │ Inheritance  │ Composition  │ Aggregation  │ Association  │
    ├──────────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
    │ Keyword               │ extends/     │ (created     │ (passed      │ (field       │
    │                       │ implements   │ inside)      │ externally)  │ reference)  │
    │ Relationship          │ IS-A         │ HAS-A        │ HAS-A        │ KNOWS-A      │
    │ Coupling              │ Tightest     │ Very tight   │ Moderate     │ Loose        │
    │ Lifecycle             │ Parent-child │ Coupled      │ Independent  │ Independent  │
    │ Child creation        │ JVM creates  │ Parent creates│ External    │ External     │
    │ If parent destroyed   │ Child destroyed│ Child dies │ Child lives  │ Child lives  │
    │ Sharing possible      │ No           │ No           │ Yes          │ Yes          │
    │ Code example          │ class B      │ A{ B b =     │ A( B b )    │ A{ B b; }   │
    │                       │   extends A  │ new B(); }   │              │              │
    │ UML diamond           │ None         │ ◆ Filled     │ ◇ Empty      │ None         │
    │ UML line              │ Solid arrow  │ Solid + ◆    │ Solid + ◇    │ Solid line   │
    └──────────────────────┴──────────────┴──────────────┴──────────────┴──────────────┘
```

---

## 15. REAL-WORLD EXAMPLES

```
    ┌──────────────────────────────────────────────────────────────┐
    │  INHERITANCE (IS-A):                                         │
    │  Dog IS-A Animal                                             │
    │  Circle IS-A Shape                                           │
    │  SavingsAccount IS-A Account                                 │
    │  ArrayList IS-A List IS-A Collection IS-A Iterable           │
    │                                                              │
    │  COMPOSITION (owns lifecycle):                               │
    │  Car ◆── Engine (engine created inside car)                  │
    │  House ◆── Room (rooms created inside house)                 │
    │  Computer ◆── CPU (CPU created for computer)                 │
    │  University ◆── Department (university creates depts)        │
    │                                                              │
    │  AGGREGATION (shared, independent):                          │
    │  University ◇── Teacher (teacher exists independently)       │
    │  Library ◇── Book (book can exist without library)           │
    │  Department ◇── Student (student can enroll elsewhere)       │
    │  Team ◇── Player (player can be in multiple teams)           │
    │                                                              │
    │  ASSOCIATION (knows temporarily):                            │
    │  Doctor ──── Patient (during treatment)                      │
    │  Teacher ──── Student (during class)                         │
    │  Driver ──── Vehicle (during drive)                          │
    │                                                              │
    │  DEPENDENCY (method parameter):                              │
    │  OrderService - - - Order (processOrder method)              │
    │  ReportService - - - Employee (generateReport method)        │
    │  PaymentService - - - CreditCard (processPayment method)     │
    └──────────────────────────────────────────────────────────────┘
```

---

## 16. SOLID + RELATIONSHIPS - INTERVIEW QUESTIONS (35+)

### ⭐ SOLID

**Q1: What is SRP?**
> Single Responsibility Principle. A class should have only one reason to change. One class = one job.

**Q2: What is OCP?**
> Open/Closed Principle. Classes should be open for extension but closed for modification. Add new behavior without changing existing code (use interfaces, inheritance).

**Q3: What is LSP?**
> Liskov Substitution Principle. Subtypes must be substitutable for their base types without breaking program correctness.

**Q4: What is ISP?**
> Interface Segregation Principle. Clients should not be forced to depend on methods they don't use. Prefer small, specific interfaces.

**Q5: What is DIP?**
> Dependency Inversion Principle. High-level modules should depend on abstractions, not concrete implementations. Use dependency injection.

**Q6: How does Dependency Injection relate to DIP?**
```
DIP = Principle (depend on abstractions)
DI  = Technique (inject dependencies via constructor/setter)

DIP achieved through DI:
class Service {
    Repository repo;  // Abstraction
    Service(Repository repo) { this.repo = repo; }  // Injection
}
```

**Q7: Which SOLID principle is most important?**
> All are important, but DIP is most impactful for testability and maintainability. SRP is easiest to understand and follow.

**Q8: Can you violate SOLID intentionally?**
> Yes. For simple, stable code, strict SOLID may add unnecessary complexity. Trade-off: simplicity vs flexibility.

---

### ⭐ RELATIONSHIPS

**Q9: What is the difference between Composition and Aggregation?**
```
Composition: Child cannot exist without parent (lifecycle coupled)
Aggregation: Child exists independently (lifecycle independent)

Composition: Engine created inside Car → Car dies, Engine dies
Aggregation: Teacher passed to School → School dies, Teacher lives
```

**Q10: What is the difference between Association and Aggregation?**
```
Association: "knows about" — temporary reference, no ownership
Aggregation: "has-a" — stronger, implies ownership but not lifecycle

Doctor KNOWS Patient (association) — uses temporarily
University HAS Department (aggregation) — holds reference
```

**Q11: What is the diamond notation in UML?**
```
◆ Filled diamond = Composition (strong ownership)
◇ Empty diamond  = Aggregation (weak ownership)
Placed at the "whole" end of the relationship
```

**Q12: What is IS-A relationship?**
> Inheritance. "Dog IS-A Animal." Subclass inherits from superclass.

**Q13: What is HAS-A relationship?**
> Composition/Aggregation. "Car HAS-A Engine." One class contains another.

**Q14: What is the difference between HAS-A and IS-A?**
```
IS-A (Inheritance): Dog extends Animal
  → Dog inherits Animal's behavior
  → Can substitute Animal with Dog

HAS-A (Composition): Car has Engine
  → Car contains Engine
  → Engine doesn't inherit from Car
```

**Q15: What is the difference between Composition and Inheritance?**
```
Inheritance: IS-A — Dog IS-A Animal (type relationship)
Composition: HAS-A — Car HAS-A Engine (part-whole relationship)

Use Inheritance: When child IS a type of parent
Use Composition: When child HAS a part that is something else

Favor composition over inheritance (design principle).
```

**Q16: What is Composition over Inheritance?**
```
Composition advantages:
- More flexible (can change behavior at runtime)
- Less coupling (no tight parent-child bond)
- Easier to test (mock dependencies)
- No fragile base class problem
- Supports multiple behaviors

Inheritance disadvantages:
- Tight coupling
- Fragile base class problem
- Single inheritance only in Java
- Breaks encapsulation
```

**Q17: What is the Fragile Base Class Problem?**
```
Parent class changes → breaks child classes unexpectedly

class Parent {
    void method() { doSomething(); }
}
class Child extends Parent {
    void method() { super.method(); doMore(); }
}
// If Parent.method() logic changes → Child breaks!
// This is the fragile base class problem
```

**Q18: What is the difference between Composition and Dependency?**
```
Composition: Object is a FIELD of the class (long-term reference)
Dependency: Object is a METHOD PARAMETER (short-term use)

class OrderService {
    Database db;  // Composition/Aggregation (field)

    void process(Order order) { ... }  // Dependency (parameter)
}
```

**Q19: What is Association example?**
```java
class Doctor {
    String name;
    List<Patient> patients;  // Association: Doctor knows patients
}

class Patient {
    String name;
    Doctor doctor;  // Association: Patient knows doctor
}
// Bidirectional association
```

**Q20: What is Association vs Aggregation vs Composition?**
```
Association:      Doctor ──── Patient (knows, uses temporarily)
Aggregation:      University ◇─── Department (has, independent)
Composition:      Car ◆─── Engine (owns, lifecycle coupled)

Association < Aggregation < Composition (in terms of coupling)
```

**Q21: How to implement Composition in Java?**
```java
class Car {
    private Engine engine;  // Field

    Car() {
        this.engine = new Engine();  // Created inside = Composition
    }
}

class Engine {
    void start() { ... }
}
```

**Q22: How to implement Aggregation in Java?**
```java
class University {
    private List<Department> departments;

    University(List<Department> depts) {
        this.departments = depts;  // Passed from outside = Aggregation
    }
}

// Department created outside
Department d1 = new Department("CSE");
University uni = new University(List.of(d1));
// d1 exists independently
```

**Q23: What is the UML symbol for Composition?**
> Filled diamond (◆) placed at the "whole" end of the relationship line.

**Q24: What is the UML symbol for Aggregation?**
> Empty diamond (◇) placed at the "whole" end of the relationship line.

**Q25: What is the test for Composition vs Aggregation?**
```
Ask: "Does the part make sense without the whole?"

Engine without Car? → NO = Composition
Department without University? → YES = Aggregation
Room without House? → NO = Composition
Book without Library? → YES = Aggregation
```

---

### ⭐⭐ ADVANCED

**Q26: What is the difference between Composition and Delegation?**
```
Composition: Class contains and controls the behavior of contained class
Delegation: Class forwards request to contained class

Composition:
class Car {
    Engine engine = new Engine();
    void start() { engine.start(); }  // Controls engine
}

Delegation:
class Proxy {
    RealService real;
    void process() { real.process(); }  // Just forwards
}
```

**Q27: What is the difference between Aggregation and Association?**
```
Association: General term for any relationship
Aggregation: Specific type of association (HAS-A, weak lifecycle)

All aggregations are associations, but not all associations are aggregations.
```

**Q28: What is Bidirectional vs Unidirectional Association?**
```
Unidirectional: A knows B, B doesn't know A
class Doctor { List<Patient> patients; }

Bidirectional: A knows B, B knows A
class Doctor { List<Patient> patients; }
class Patient { Doctor doctor; }
```

**Q29: What is the relationship between Comparable and Comparator?**
```
Comparable: IS-A relationship with the class (implemented by class)
Comparator: ASSOCIATION (passed as parameter, separate object)

class Student implements Comparable<Student> { ... }  // IS-A
Comparator<Student> comp = ...;  // Association (used separately)
```

**Q30: How does Spring use DIP?**
```java
// Interface (abstraction)
interface UserRepository {
    void save(User user);
}

// Implementation (details)
@Repository
class MySqlUserRepository implements UserRepository {
    public void save(User user) { ... }
}

// High-level module depends on abstraction
@Service
class UserService {
    private UserRepository repo;  // Abstraction

    @Autowired
    UserService(UserRepository repo) {  // DI
        this.repo = repo;
    }
}
// Switch to MongoDB: just create MongoUserRepository, no change in UserService
```

**Q31: What is Composition vs Inheritance in real projects?**
```
Use Inheritance:
- Clear IS-A relationship
- Framework requires it (HttpServlet, AbstractList)
- Code reuse from parent

Use Composition:
- HAS-A relationship
- Need flexibility at runtime
- Multiple behaviors needed
- Testing is important
- Default recommendation: "Favor composition over inheritance"
```

**Q32: What is the Liskov Substitution Principle example?**
```java
// BAD (LSP violation)
class Rectangle {
    int width, height;
    void setWidth(int w) { width = w; }
    void setHeight(int h) { height = h; }
}
class Square extends Rectangle {
    void setWidth(int w) { width = height = w; }  // Changes behavior!
    void setHeight(int h) { width = height = h; }
}

// GOOD (LSP followed)
interface Shape { int area(); }
class Rectangle implements Shape { int w, h; int area() { return w*h; } }
class Square implements Shape { int side; int area() { return side*side; } }
```

**Q33: What is the relationship in Java Collections?**
```
List   extends Collection extends Iterable      (IS-A)
Set    extends Collection extends Iterable      (IS-A)
Map    (separate hierarchy)

ArrayList HAS-A Object[] (Composition — internal array)
LinkedList HAS-A Node (Composition — internal linked list)

ArrayList implements List (IS-A)
LinkedList implements List, Deque (IS-A)
```

**Q34: What is the difference between Encapsulation and Composition?**
```
Encapsulation: Hiding internal details (access modifiers, getters/setters)
Composition: Designing classes by combining simpler objects

Encapsulation is about data hiding.
Composition is about building complex objects from simpler ones.
```

**Q35: When to use which relationship?**
```
┌───────────────────────────────┬──────────────────────────────────┐
│ Scenario                       │ Relationship                     │
├───────────────────────────────┼──────────────────────────────────┤
│ Dog is an Animal               │ Inheritance (IS-A)              │
│ Car owns Engine                │ Composition (owns lifecycle)    │
│ University has Departments     │ Aggregation (independent)       │
│ Doctor knows Patient           │ Association (temporary)         │
│ Order uses Payment             │ Dependency (method param)       │
│ Want to test Service class     │ Composition + DIP (mock DB)     │
│ Framework requires base class  │ Inheritance (required)          │
│ Want runtime flexibility       │ Composition (switch behavior)   │
│ Multiple behaviors needed      │ Composition (not inheritance)   │
│ Simple code reuse              │ Inheritance (if IS-A exists)    │
└───────────────────────────────┴──────────────────────────────────┘
```

---

## 17. QUICK REFERENCE CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║       SOLID + OOP RELATIONSHIPS CHEAT SHEET                      ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  SOLID:                                                          ║
║  S - Single Responsibility → One class, one job                  ║
║  O - Open/Closed → Extend, don't modify existing code           ║
║  L - Liskov Substitution → Subtypes work as parent type         ║
║  I - Interface Segregation → Small interfaces > fat ones        ║
║  D - Dependency Inversion → Depend on abstractions               ║
║                                                                  ║
║  RELATIONSHIPS (strongest → weakest):                            ║
║  1. Inheritance    IS-A        extends       Parent-child       ║
║  2. Composition    HAS-A       ◆ Filled      Owns, lifecycle    ║
║  3. Aggregation    HAS-A       ◇ Empty       Shares, independent║
║  4. Association    KNOWS-A     ────────      Uses temporarily   ║
║  5. Dependency     USES-A      - - - - -     Method parameter   ║
║                                                                  ║
║  COMPOSITION vs AGGREGATION:                                     ║
║  Composition: new Engine() inside Car → Engine dies with Car     ║
║  Aggregation: Engine passed to Car → Engine survives Car        ║
║                                                                  ║
║  "Favor composition over inheritance"                            ║
║  DIP achieved through Dependency Injection (DI)                  ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: SOLID Principles, All OOP Relationships, UML Notation, Interview Questions*
