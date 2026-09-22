# Java Design Patterns - Complete Interview Notes

---

## 1. WHAT ARE DESIGN PATTERNS?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Design Patterns = Reusable solutions to common problems     │
    │                                                              │
    │  - Not code, but TEMPLATES for solving problems              │
    │  - Discovered by Gang of Four (GoF) in 1994                  │
    │  - 23 patterns in 3 categories                               │
    │  - Language-agnostic concepts                                │
    └──────────────────────────────────────────────────────────────┘

    3 Categories:
    ┌──────────────────────────────────────────────────────────────┐
    │  CREATIONAL (5)      │ How objects are created               │
    │  STRUCTURAL (7)      │ How objects are composed              │
    │  BEHAVIORAL (11)     │ How objects communicate               │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. CREATIONAL PATTERNS

### 2.1 SINGLETON (Most Asked!)

```
    Problem: Only ONE instance of a class should exist
    Solution: Private constructor + static instance

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Class: Singleton                                            │
    │  ┌──────────────────────────────────────────────────────┐   │
    │  │ - static instance: Singleton                         │   │
    │  │ - private data                                       │   │
    │  ├──────────────────────────────────────────────────────┤   │
    │  │ - private Singleton() {}  ← Only one instance!       │   │
    │  │ + static getInstance() → Singleton                   │   │
    │  └──────────────────────────────────────────────────────┘   │
    │                                                              │
    │  Client A ──→ getInstance() ──→ Same Instance ←── Client B │
    │  Client C ──→ getInstance() ──→ Same Instance               │
    └──────────────────────────────────────────────────────────────┘
```

#### Implementation 1: Eager (Simple)

```java
class Singleton {
    private static final Singleton INSTANCE = new Singleton();
    private Singleton() {}  // Private constructor

    public static Singleton getInstance() {
        return INSTANCE;
    }
}
```

#### Implementation 2: Lazy (Not Thread-Safe)

```java
class Singleton {
    private static Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();  // Not thread-safe!
        }
        return instance;
    }
}
```

#### Implementation 3: Thread-Safe (Synchronized)

```java
class Singleton {
    private static Singleton instance;

    private Singleton() {}

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
    // ⚠️ Slow: synchronized on every call
}
```

#### Implementation 4: Double-Checked Locking (Best!)

```java
class Singleton {
    private static volatile Singleton instance;  // volatile!

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {                  // 1st check (no lock)
            synchronized (Singleton.class) {
                if (instance == null) {          // 2nd check (with lock)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

#### Implementation 5: Enum (Best - Joshua Bloch)

```java
enum Singleton {
    INSTANCE;

    public void doSomething() {
        System.out.println("Singleton method");
    }
}

// Usage
Singleton.INSTANCE.doSomething();
```

#### Why Enum is Best?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Enum Singleton Advantages:                                   │
    │  ✅ Thread-safe (JVM guarantees)                              │
    │  ✅ Serialization handled automatically                      │
    │  ✅ Reflection-proof (can't create via reflection)           │
    │  ✅ Lazy initialization possible                              │
    │  ✅ No memory issues                                          │
    │  ✅ Simplest code                                            │
    └──────────────────────────────────────────────────────────────┘
```

---

### 2.2 FACTORY METHOD

```
    Problem: Create objects without specifying exact class
    Solution: Delegate instantiation to subclass/method

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │                    Factory Method                             │
    │                                                              │
    │  Client ──→ Creator.create() ──→ returns Product             │
    │                    │                                         │
    │                    ├── ConcreteCreatorA.create() → ProductA  │
    │                    └── ConcreteCreatorB.create() → ProductB  │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Product
interface Animal {
    void speak();
}

// Concrete Products
class Dog implements Animal {
    public void speak() { System.out.println("Woof!"); }
}

class Cat implements Animal {
    public void speak() { System.out.println("Meow!"); }
}

// Factory
class AnimalFactory {
    public static Animal create(String type) {
        return switch (type.toLowerCase()) {
            case "dog" -> new Dog();
            case "cat" -> new Cat();
            default -> throw new IllegalArgumentException("Unknown: " + type);
        };
    }
}

// Usage
Animal animal = AnimalFactory.create("dog");
animal.speak();  // Woof!
```

---

### 2.3 ABSTRACT FACTORY

```
    Problem: Create families of related objects
    Solution: Interface for creating families without specifying classes

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │                  Abstract Factory                             │
    │                                                              │
    │  Client ──→ AbstractFactory                                  │
    │                    │                                         │
    │                    ├── ConcreteFactoryA                      │
    │                    │   ├── createButton() → ButtonA          │
    │                    │   └── createTextBox() → TextBoxA        │
    │                    │                                         │
    │                    └── ConcreteFactoryB                      │
    │                        ├── createButton() → ButtonB          │
    │                        └── createTextBox() → TextBoxB        │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Abstract Products
interface Button { void render(); }
interface TextBox { void render(); }

// Concrete Products
class DarkButton implements Button { public void render() { /* dark */ } }
class DarkTextBox implements TextBox { public void render() { /* dark */ } }
class LightButton implements Button { public void render() { /* light */ } }
class LightTextBox implements TextBox { public void render() { /* light */ } }

// Abstract Factory
interface UIFactory {
    Button createButton();
    TextBox createTextBox();
}

// Concrete Factories
class DarkThemeFactory implements UIFactory {
    public Button createButton() { return new DarkButton(); }
    public TextBox createTextBox() { return new DarkTextBox(); }
}

class LightThemeFactory implements UIFactory {
    public Button createButton() { return new LightButton(); }
    public TextBox createTextBox() { return new LightTextBox(); }
}

// Usage
UIFactory factory = new DarkThemeFactory();
Button button = factory.createButton();
```

---

### 2.3 BUILDER

```
    Problem: Create complex objects with many parameters
    Solution: Step-by-step construction with fluent API

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  User user = User.builder()                                  │
    │      .name("Amit")                                           │
    │      .age(25)                                                │
    │      .email("amit@mail.com")                                 │
    │      .build();                                               │
    │                                                              │
    │  User.builder() → Builder → .name() → Builder → .build() → User│
    └──────────────────────────────────────────────────────────────┘
```

```java
class User {
    private final String name;
    private final int age;
    private final String email;

    private User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.email = builder.email;
    }

    public static class Builder {
        private String name;
        private int age;
        private String email;

        public Builder name(String name) { this.name = name; return this; }
        public Builder age(int age) { this.age = age; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public User build() { return new User(this); }
    }
}

// Usage (fluent API)
User user = User.builder()
    .name("Amit")
    .age(25)
    .email("amit@mail.com")
    .build();
```

---

### 2.4 PROTOTYPE

```
    Problem: Create new objects by copying existing ones
    Solution: Clone existing object

    ┌──────────────────────────────────────────────────────────────┐
    │  Interface: Cloneable                                        │
    │  Class: implements Cloneable                                 │
    │  Method: clone() → returns copy of object                    │
    └──────────────────────────────────────────────────────────────┘
```

```java
class Sheep implements Cloneable {
    String name;
    String color;

    public Sheep(String name, String color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public Sheep clone() {
        try {
            return (Sheep) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

// Usage
Sheep original = new Sheep("Dolly", "White");
Sheep copy = original.clone();  // Shallow copy
```

---

## 3. STRUCTURAL PATTERNS

### 3.1 ADAPTER

```
    Problem: Incompatible interfaces need to work together
    Solution: Wrapper that converts one interface to another

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Client ──→ Target Interface ←── Adapter ←── Adaptee         │
    │                                                              │
    │  Client calls: target.method()                               │
    │  Adapter translates to: adaptee.specificMethod()             │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Old interface
class OldPrinter {
    void printOld(String text) { System.out.println("Old: " + text); }
}

// New interface
interface NewPrinter {
    void printNew(String text);
}

// Adapter
class PrinterAdapter implements NewPrinter {
    private OldPrinter oldPrinter;

    PrinterAdapter(OldPrinter oldPrinter) {
        this.oldPrinter = oldPrinter;
    }

    public void printNew(String text) {
        oldPrinter.printOld(text);  // Translates call
    }
}

// Usage
NewPrinter printer = new PrinterAdapter(new OldPrinter());
printer.printNew("Hello");  // Old: Hello
```

---

### 3.2 DECORATOR

```
    Problem: Add behavior to objects dynamically
    Solution: Wrap object with decorator classes

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Coffee coffee = new MilkDecorator(new SugarDecorator(new SimpleCoffee()));│
    │  coffee.getCost();  // Simple + Sugar + Milk                  │
    │                                                              │
    │  ┌──────────────┐                                          │
    │  │ MilkDecorator │                                          │
    │  │ ┌──────────────┐                                        │
    │  │ │SugarDecorator│                                        │
    │  │ │ ┌──────────────┐                                      │
    │  │ │ │SimpleCoffee  │                                      │
    │  │ │ └──────────────┘                                      │
    │  │ └──────────────┘                                        │
    │  └──────────────┘                                          │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Component
interface Coffee {
    double getCost();
    String getDescription();
}

// Concrete Component
class SimpleCoffee implements Coffee {
    public double getCost() { return 5.0; }
    public String getDescription() { return "Simple coffee"; }
}

// Decorator
abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;
    CoffeeDecorator(Coffee coffee) { this.coffee = coffee; }
}

// Concrete Decorators
class MilkDecorator extends CoffeeDecorator {
    MilkDecorator(Coffee coffee) { super(coffee); }
    public double getCost() { return coffee.getCost() + 2.0; }
    public String getDescription() { return coffee.getDescription() + ", Milk"; }
}

class SugarDecorator extends CoffeeDecorator {
    SugarDecorator(Coffee coffee) { super(coffee); }
    public double getCost() { return coffee.getCost() + 1.0; }
    public String getDescription() { return coffee.getDescription() + ", Sugar"; }
}

// Usage
Coffee coffee = new MilkDecorator(new SugarDecorator(new SimpleCoffee()));
System.out.println(coffee.getCost());        // 8.0
System.out.println(coffee.getDescription()); // Simple coffee, Sugar, Milk
```

---

### 3.3 FACADE

```
    Problem: Complex subsystem with many classes
    Solution: Simple interface to complex subsystem

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Client ──→ Facade ──→ SubsystemA                           │
    │                        SubsystemB                           │
    │                        SubsystemC                           │
    │                                                              │
    │  Client only talks to Facade, not subsystems directly        │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Subsystems
class CPU {
    void start() { System.out.println("CPU started"); }
}

class Memory {
    void load() { System.out.println("Memory loaded"); }
}

class HardDrive {
    void read() { System.out.println("HardDrive reading"); }
}

// Facade
class ComputerFacade {
    private CPU cpu = new CPU();
    private Memory memory = new Memory();
    private HardDrive hd = new HardDrive();

    public void startComputer() {
        hd.read();
        memory.load();
        cpu.start();
    }
}

// Usage
ComputerFacade computer = new ComputerFacade();
computer.startComputer();  // Simple call, hides complexity
```

---

### 3.4 PROXY

```
    Problem: Control access to real object
    Solution: Placeholder (proxy) controls access

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Client ──→ Proxy ──→ RealSubject                            │
    │                                                              │
    │  Proxy adds: caching, logging, security, lazy loading       │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Interface
interface Image {
    void display();
}

// Real subject
class RealImage implements Image {
    RealImage(String file) { loadFromDisk(file); }
    void display() { System.out.println("Displaying image"); }
    private void loadFromDisk(String file) { System.out.println("Loading: " + file); }
}

// Proxy
class ProxyImage implements Image {
    private RealImage realImage;
    private String fileName;

    ProxyImage(String fileName) {
        this.fileName = fileName;
    }

    public void display() {
        if (realImage == null) {
            realImage = new RealImage(fileName);  // Lazy loading
        }
        realImage.display();
    }
}

// Usage
Image image = new ProxyImage("photo.jpg");
image.display();  // Loads + displays
image.display();  // Just displays (already loaded)
```

---

### 3.5 COMPOSITE

```
    Problem: Tree structure where objects and compositions are treated uniformly
    Solution: Treat individual objects and compositions the same

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  FileSystem                                                  │
    │  ├── File (read)                                            │
    │  ├── Folder                                                 │
    │  │   ├── File (read)                                        │
    │  │   └── File (read)                                        │
    │  └── Folder                                                 │
    │      └── Folder                                             │
    │          └── File (read)                                    │
    │                                                              │
    │  All implement same interface: getSize(), display()          │
    └──────────────────────────────────────────────────────────────┘
```

---

## 4. BEHAVIORAL PATTERNS

### 4.1 OBSERVER

```
    Problem: One object changes, all dependents must be notified
    Solution: Subject maintains list of observers, notifies on change

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Subject (Publisher)                                         │
    │  ├── Observer A (Subscriber)                                │
    │  ├── Observer B (Subscriber)                                │
    │  └── Observer C (Subscriber)                                │
    │                                                              │
    │  Subject.notify() → A.update()                               │
    │                   → B.update()                               │
    │                   → C.update()                               │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Observer interface
interface Observer {
    void update(String message);
}

// Subject
class NewsAgency {
    private List<Observer> observers = new ArrayList<>();
    private String news;

    public void subscribe(Observer o) { observers.add(o); }
    public void unsubscribe(Observer o) { observers.remove(o); }

    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(news);
        }
    }

    public void setNews(String news) {
        this.news = news;
        notifyObservers();
    }
}

// Concrete Observers
class NewsChannel implements Observer {
    String name;
    NewsChannel(String name) { this.name = name; }
    public void update(String message) {
        System.out.println(name + " received: " + message);
    }
}

// Usage
NewsAgency agency = new NewsAgency();
NewsChannel ch1 = new NewsChannel("CNN");
NewsChannel ch2 = new NewsChannel("BBC");

agency.subscribe(ch1);
agency.subscribe(ch2);
agency.setNews("Breaking: Java 21 released!");
// CNN received: Breaking: Java 21 released!
// BBC received: Breaking: Java 21 released!
```

---

### 4.2 STRATEGY

```
    Problem: Algorithm should be interchangeable at runtime
    Solution: Define family of algorithms, encapsulate each, make interchangeable

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Context ──→ Strategy interface                              │
    │                    │                                         │
    │                    ├── BubbleSortStrategy                    │
    │                    ├── QuickSortStrategy                     │
    │                    └── MergeSortStrategy                     │
    │                                                              │
    │  Context.setStrategy(quickSort);  ← Change algorithm!       │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Strategy interface
interface SortStrategy {
    void sort(int[] array);
}

// Concrete Strategies
class BubbleSort implements SortStrategy {
    public void sort(int[] array) { /* bubble sort */ }
}

class QuickSort implements SortStrategy {
    public void sort(int[] array) { /* quick sort */ }
}

// Context
class Sorter {
    private SortStrategy strategy;

    public void setStrategy(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public void sort(int[] array) {
        strategy.sort(array);  // Delegate to strategy
    }
}

// Usage
Sorter sorter = new Sorter();
sorter.setStrategy(new BubbleSort());
sorter.sort(array);

sorter.setStrategy(new QuickSort());  // Change at runtime!
sorter.sort(array);
```

---

### 4.3 TEMPLATE METHOD

```
    Problem: Define algorithm skeleton, let subclasses override specific steps
    Solution: Abstract class with template method + hook methods

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Abstract Class: Game                                        │
    │  ┌──────────────────────────────────────────────────────┐   │
    │  │ final void play() {     ← Template method (final)    │   │
    │  │     initialize();                                     │   │
    │  │     startPlay();      ← Abstract (subclass implements)│   │
    │  │     endPlay();        ← Hook (optional override)     │   │
    │  │ }                                                   │   │
    │  └──────────────────────────────────────────────────────┘   │
    │                                                              │
    │  CricketGame extends Game: startPlay() → "Cricket started"  │
    │  FootballGame extends Game: startPlay() → "Football started" │
    └──────────────────────────────────────────────────────────────┘
```

---

### 4.4 CHAIN OF RESPONSIBILITY

```
    Problem: Request passes through chain of handlers
    Solution: Each handler decides to process or pass to next

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Request ──→ Handler A ──→ Handler B ──→ Handler C ──→ End   │
    │              (logs)         (auth)         (business)        │
    │                                                              │
    │  Each handler: process() or pass to nextHandler              │
    └──────────────────────────────────────────────────────────────┘
```

```java
abstract class Handler {
    protected Handler nextHandler;

    public Handler setNext(Handler next) {
        this.nextHandler = next;
        return next;
    }

    public void handle(int level) {
        if (nextHandler != null) {
            nextHandler.handle(level);
        }
    }
}

class DebugLogger extends Handler {
    public void handle(int level) {
        if (level >= 1) System.out.println("DEBUG: " + level);
        super.handle(level);
    }
}

class ErrorLogger extends Handler {
    public void handle(int level) {
        if (level >= 5) System.out.println("ERROR: " + level);
        super.handle(level);
    }
}

// Usage
Handler chain = new DebugLogger();
chain.setNext(new ErrorLogger());
chain.handle(7);  // DEBUG: 7, ERROR: 7
```

---

### 4.5 COMMAND

```
    Problem: Encapsulate request as object
    Solution: Command object with execute() and undo()

    Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Invoker ──→ Command ──→ Receiver                            │
    │                                                              │
    │  Command.execute() → receiver.action()                       │
    │  Command.undo()    → receiver.reverseAction()                │
    └──────────────────────────────────────────────────────────────┘
```

---

### 4.6 ITERATOR

```
    Problem: Traverse collection without exposing internal structure
    Solution: Iterator interface with hasNext(), next()

    Already built into Java! java.util.Iterator
```

---

## 5. ALL PATTERNS SUMMARY

```
    ┌────────────────────┬──────────────────────────────────────────┐
    │ Category            │ Patterns                                 │
    ├────────────────────┼──────────────────────────────────────────┤
    │ CREATIONAL (5)     │ Singleton, Factory Method,               │
    │                    │ Abstract Factory, Builder, Prototype     │
    ├────────────────────┼──────────────────────────────────────────┤
    │ STRUCTURAL (7)     │ Adapter, Decorator, Facade, Proxy,      │
    │                    │ Composite, Bridge, Flyweight              │
    ├────────────────────┼──────────────────────────────────────────┤
    │ BEHAVIORAL (11)    │ Observer, Strategy, Template Method,     │
    │                    │ Chain of Responsibility, Command,        │
    │                    │ Iterator, Mediator, State,               │
    │                    │ Visitor, Interpreter, Memento            │
    └────────────────────┴──────────────────────────────────────────┘
```

---

## 6. DESIGN PATTERNS INTERVIEW QUESTIONS (40+)

### ⭐ BASIC

**Q1: What are Design Patterns?**
> Reusable solutions to common software design problems. Templates discovered by Gang of Four (GoF).

**Q2: What are the 3 categories of design patterns?**
```
Creational: Object creation (Singleton, Factory, Builder)
Structural: Object composition (Adapter, Decorator, Facade)
Behavioral: Object communication (Observer, Strategy, Template)
```

**Q3: What is Singleton pattern?**
> Ensures only one instance of a class exists. Private constructor + static getInstance().

**Q4: What is Factory pattern?**
> Creates objects without specifying exact class. Delegates instantiation to factory method.

**Q5: What is difference between Factory and Abstract Factory?**
```
Factory: Creates one type of object
Abstract Factory: Creates families of related objects
```

**Q6: What is Adapter pattern?**
> Converts incompatible interface to compatible one. Like a power adapter.

**Q7: What is Decorator pattern?**
> Adds behavior to objects dynamically without inheritance. Like wrapping gifts.

**Q8: What is Observer pattern?**
> One-to-many dependency. When subject changes, all observers notified. Like newspaper subscription.

---

### ⭐⭐ MIDDLE

**Q9: What is the difference between Factory and Builder?**
```
Factory: Creates single object based on parameter
Builder: Creates complex object step-by-step
```

**Q10: What is the difference between Decorator and Proxy?**
```
Decorator: Adds behavior dynamically (same interface)
Proxy: Controls access to object (may not add behavior)
```

**Q11: What is the difference between Adapter and Facade?**
```
Adapter: Converts one interface to another (single class)
Facade: Simplifies complex subsystem (multiple classes)
```

**Q12: What is Strategy pattern used for?**
> Interchangeable algorithms at runtime. Example: Different sorting algorithms, payment methods.

**Q13: What is Template Method?**
> Defines algorithm skeleton in base class. Subclasses override specific steps.

**Q14: What is Chain of Responsibility?**
> Request passes through chain of handlers. Each decides to process or forward.

**Q15: How to implement Singleton thread-safe?**
```
Double-checked locking + volatile
Enum (best approach)
```

**Q16: What is the problem with Singleton?**
```
- Testing difficulty (hard to mock)
- Hidden dependencies
- Violates Single Responsibility
- Global state
- Tight coupling
```

**Q17: What is Builder pattern advantages?**
```
- Readable (fluent API)
- Immutable objects
- Step-by-step construction
- Handles optional parameters
```

**Q18: What is the difference between Creational and Structural patterns?**
```
Creational: How to create objects (Singleton, Factory)
Structural: How to compose objects (Adapter, Decorator)
```

**Q19: What is Proxy pattern used for?**
```
- Lazy loading
- Access control
- Logging
- Caching
- Remote proxy (RPC)
```

**Q20: What is the difference between Strategy and State pattern?**
```
Strategy: Client chooses algorithm
State: Object changes behavior based on internal state
Similar structure, different intent
```

---

### ⭐⭐⭐ ADVANCED

**Q21: What is SOLID principle?**
```
S - Single Responsibility: One class, one job
O - Open/Closed: Open for extension, closed for modification
L - Liskov Substitution: Subclass must be substitutable
I - Interface Segregation: Small, specific interfaces
D - Dependency Inversion: Depend on abstractions, not concretions
```

**Q22: How does Singleton violate SRP?**
> Handles both business logic AND instance management. Can fix with dependency injection.

**Q23: What is the difference between Singleton and Monostate?**
```
Singleton: One instance, static access
Monostate: Many instances, shared state (all instances behave same)
```

**Q24: What is dependency injection and how it relates to Factory?**
```
DI: Inject dependencies instead of creating them
Factory: Creates dependencies
DI container often uses Factory pattern internally
```

**Q25: What is the Composite pattern?**
> Treats individual objects and compositions uniformly. Like file system (files + folders).

**Q26: What is the Bridge pattern?**
> Separates abstraction from implementation. Both can vary independently.

**Q27: What is the Mediator pattern?**
> Reduces chaotic dependencies between objects. Like air traffic controller.

**Q28: What is the State pattern?**
> Object changes behavior when its state changes. Like vending machine states.

**Q29: What is the Memento pattern?**
> Captures and restores internal state. Like undo functionality.

**Q30: What is the Visitor pattern?**
> Adds operations to classes without modifying them. Like double dispatch.

**Q31: What is the Flyweight pattern?**
> Shares common state between objects to save memory. Like String pool.

**Q32: What is the Command pattern used for?**
> Encapsulates request as object. Supports undo, redo, queuing, logging.

**Q33: What is the Iterator pattern?**
> Traverses collection without exposing structure. Built into Java.

**Q34: What is the Interpreter pattern?**
> Defines grammar and interpreter for language. Like regex, SQL parsers.

**Q35: When to use which pattern?**
```
┌───────────────────────────────┬──────────────────────────────────┐
│ Problem                        │ Pattern                          │
├───────────────────────────────┼──────────────────────────────────┤
│ Only one instance needed       │ Singleton                        │
│ Create objects without class   │ Factory                          │
│ Create families of objects     │ Abstract Factory                 │
│ Complex object step-by-step    │ Builder                          │
│ Copy existing object           │ Prototype                        │
│ Incompatible interfaces        │ Adapter                          │
│ Add behavior dynamically       │ Decorator                        │
│ Simplify complex subsystem     │ Facade                           │
│ Control access to object       │ Proxy                            │
│ One-to-many notification       │ Observer                         │
│ Interchangeable algorithms     │ Strategy                         │
│ Algorithm skeleton             │ Template Method                  │
│ Chain of handlers              │ Chain of Responsibility          │
│ Encapsulate request            │ Command                          │
│ Object changes behavior        │ State                            │
│ Save/restore state             │ Memento                          │
│ Add operations without modify  │ Visitor                          │
│ Share common state             │ Flyweight                        │
└───────────────────────────────┴──────────────────────────────────┘
```

---

## 7. QUICK REFERENCE CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║              DESIGN PATTERNS CHEAT SHEET                          ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  CREATIONAL (Object Creation):                                   ║
║  Singleton      → One instance (enum best)                       ║
║  Factory        → Create object without specifying class         ║
║  Abstract Factory→ Create families of related objects            ║
║  Builder        → Step-by-step complex object construction      ║
║  Prototype      → Clone existing objects                         ║
║                                                                  ║
║  STRUCTURAL (Object Composition):                                ║
║  Adapter        → Convert incompatible interface                 ║
║  Decorator      → Add behavior dynamically (wrapping)            ║
║  Facade         → Simplify complex subsystem                     ║
║  Proxy          → Control access to object (placeholder)         ║
║  Composite      → Tree structure, uniform treatment              ║
║  Bridge         → Separate abstraction from implementation       ║
║  Flyweight      → Share common state, save memory                ║
║                                                                  ║
║  BEHAVIORAL (Object Communication):                              ║
║  Observer       → One-to-many notification (pub/sub)             ║
║  Strategy       → Interchangeable algorithms                     ║
║  Template Method→ Algorithm skeleton, subclass overrides         ║
║  Chain of Resp  → Request passes through handler chain           ║
║  Command        → Encapsulate request as object (undo/redo)      ║
║  Iterator       → Traverse collection (built-in Java)            ║
║  State          → Object behavior changes with state             ║
║  Mediator       → Centralize complex communications              ║
║  Memento        → Save/restore state (undo)                      ║
║  Visitor        → Add operations without modifying classes       ║
║  Interpreter    → Define grammar/interpreter for language        ║
║                                                                  ║
║  SOLID:                                                          ║
║  S - Single Responsibility (one class, one job)                  ║
║  O - Open/Closed (open extend, closed modify)                    ║
║  L - Liskov Substitution (substitutable subclasses)              ║
║  I - Interface Segregation (small interfaces)                    ║
║  D - Dependency Inversion (depend on abstractions)               ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: All 23 GoF Patterns, SOLID, Examples, Interview Questions*
