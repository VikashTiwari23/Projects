# Design Patterns - Bilkul Zero Se Seekho (Hindi + English)

---

## PEHLE SAMAJH: DESIGN PATTERN KYA HAI?

```
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  REAL LIFE EXAMPLE:                                           │
    │                                                              │
    │  Tujhe ghar banana hai. Kya tu har baar naya tarika sochega?│
    │  Nahi! Tu ARCHITECT se puchega jo pehle se jaanta hai:       │
    │                                                              │
    │  "Bhai, bedroom idhar ho, kitchen udhar, bathroom paas..."  │
    │                                                              │
    │  Wahi cheez software mein bhi hoti hai!                      │
    │                                                              │
    │  DESIGN PATTERN = Proven solution to common problems         │
    │                                                              │
    │  Kisi ne pehle problem solve ki, sabne use kiya,             │
    │  aur sabko kaam aaya → Usse pattern bolte hain              │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 3 CATEGORIES (Yaad Rakh!)

```
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  1. CREATIONAL  = Object KAISE BANAYE?                       │
    │     (Kaise create karein - 5 patterns)                       │
    │                                                              │
    │  2. STRUCTURAL  = Object KO KAISE JODEIN?                    │
    │     (Kaise connect karein - 7 patterns)                      │
    │                                                              │
    │  3. BEHAVIORAL  = Object KAISE BAAT KAREIN?                  │
    │     (Kaise communicate karein - 11 patterns)                 │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## PATTERN 1: SINGLETON (Sabse Simple & Famous!)

### Real Life Analogy

```
    ┌──────────────────────────────────────────────────────────────┐
    │  SCHOOL MEIN PRINCIPAL:                                       │
    │                                                              │
    │  Poore school mein sirf EK principal hota hai.               │
    │  Chahe principal ka naam badlo, person same rahega.          │
    │                                                              │
    │  Principal p1 = principal.getInstance();                     │
    │  Principal p2 = principal.getInstance();                     │
    │  p1 == p2  → TRUE (same person!)                             │
    │                                                              │
    │  Kabhi 2 principal nahi hote school mein!                    │
    │  Wahi Singleton hai!                                         │
    └──────────────────────────────────────────────────────────────┘
```

### Code Example

```java
// ❌ GALAT: Har baar naya object banega
class BadSingleton {
    // No restriction - anyone can create new object
}

// ✅ SAHI: Sirf ek hi object banega
class Singleton {
    // Step 1: Private static variable (ek hi object store karega)
    private static Singleton instance = null;

    // Step 2: Private constructor (bahar se new mat karo!)
    private Singleton() {
        System.out.println("Singleton created!");
    }

    // Step 3: Public static method (sirf yahan se object milega)
    public static Singleton getInstance() {
        if (instance == null) {              // Agar abhi tak nahi bana
            instance = new Singleton();      // Tabhi banao
        }
        return instance;                     // Wahi purana do
    }
}

// Usage
Singleton s1 = Singleton.getInstance();  // "Singleton created!" print hoga
Singleton s2 = Singleton.getInstance();  // Kuch print NAHI hoga (pehle se hai)

System.out.println(s1 == s2);  // TRUE (same object!)
```

### Visual

```
    GALAT:
    ┌─────────────┐  ┌─────────────┐  ┌─────────────┐
    │   Object 1   │  │   Object 2   │  │   Object 3   │
    │  (RAM: 100)  │  │  (RAM: 200)  │  │  (RAM: 300)  │
    └─────────────┘  └─────────────┘  └─────────────┘
    Har baar naya! Memory waste!

    SAHI (Singleton):
    ┌─────────────┐
    │   Object 1   │  ← Sirf EK!
    │  (RAM: 100)  │
    └─────────────┘
         ↑
    s1  ─┤
    s2  ─┤  Sab same object point karte hain
    s3  ─┤
```

### Kab Use Karein?

```
    ✅ Database Connection Pool (ek hi pool sabke liye)
    ✅ Logger (ek hi logger file mein likhe)
    ✅ Config Manager (ek hi config file padhe)
    ✅ Cache (ek hi cache sab access karein)
```

---

## PATTERN 2: FACTORY (Object Banana Asan!)

### Real Life Analogy

```
    ┌──────────────────────────────────────────────────────────────┐
    │  MOBILE SHOP:                                                │
    │                                                              │
    │  Tu shop mein jaata hai aur bolta hai:                       │
    │  "Bhai iPhone do"  → Shopkeeper iPhone deta hai              │
    │  "Bhai Samsung do" → Shopkeeper Samsung deta hai             │
    │                                                              │
    │  Tu nahi jaanta phone kaise banta hai.                       │
    │  Tu bas BOLTA hai kya chahiye.                               │
    │  SHOPKEEPER decide karta hai kaun sa phone dena hai.         │
    │                                                              │
    │  Wahi Factory Pattern hai!                                   │
    └──────────────────────────────────────────────────────────────┘
```

### Code Example

```java
// Step 1: Common interface (sab phone ka common kaam)
interface Phone {
    void call();
    void message();
}

// Step 2: Concrete phones (alag-alag phones)
class iPhone implements Phone {
    public void call() { System.out.println("iPhone se call ho raha hai"); }
    public void message() { System.out.println("iPhone se message ja raha hai"); }
}

class Samsung implements Phone {
    public void call() { System.out.println("Samsung se call ho raha hai"); }
    public void message() { System.out.println("Samsung se message ja raha hai"); }
}

class OnePlus implements Phone {
    public void call() { System.out.println("OnePlus se call ho raha hai"); }
    public void message() { System.out.println("OnePlus se message ja raha hai"); }
}

// Step 3: Factory (shopkeeper jo decide karega)
class PhoneFactory {
    public static Phone createPhone(String type) {
        return switch (type.toLowerCase()) {
            case "iphone" -> new iPhone();
            case "samsung" -> new Samsung();
            case "oneplus" -> new OnePlus();
            default -> throw new IllegalArgumentException("Unknown phone: " + type);
        };
    }
}

// Usage
Phone phone1 = PhoneFactory.createPhone("iphone");
phone1.call();   // iPhone se call ho raha hai
phone1.message(); // iPhone se message ja raha hai

Phone phone2 = PhoneFactory.createPhone("samsung");
phone2.call();   // Samsung se call ho raha hai
```

### Visual

```
    CLIENT (Tu): "Bhai iPhone do"
         │
         ▼
    ┌─────────────────┐
    │   PhoneFactory   │  ← Shopkeeper
    │                  │
    │  createPhone()   │
    │   │              │
    │   ├─ "iphone"  → new iPhone()    ─┐
    │   ├─ "samsung" → new Samsung()   ─┤─→ Phone interface
    │   └─ "oneplus" → new OnePlus()   ─┘
    └─────────────────┘

    Client ko NAHI pata kaun sa object bana.
    Factory decide karti hai.
```

---

## PATTERN 3: BUILDER (Complex Cheez Step-by-Step Banao!)

### Real Life Analogy

```
    ┌──────────────────────────────────────────────────────────────┐
    │  BURGER BANANA:                                               │
    │                                                              │
    │  Tu bolta hai:                                               │
    │  "Mujhe burger do, with cheese, with mayo, no tomato"        │
    │                                                              │
    │  Step by step banata hai:                                     │
    │  1. Bun lo                                                   │
    │  2. Patty rakho                                              │
    │  3. Cheese dalo                                              │
    │  4. Mayo lagao                                               │
    │  5. Tomato mat dalna                                         │
    │                                                              │
    │  Wahi Builder Pattern hai!                                   │
    └──────────────────────────────────────────────────────────────┘
```

### Code Example

```java
// Step 1: Product (jo banana hai)
class Burger {
    String bun;
    String patty;
    String cheese;
    boolean tomato;
    boolean mayo;

    @Override
    public String toString() {
        return "Burger{" + bun + ", " + patty + ", " + cheese +
               (tomato ? ", tomato" : "") +
               (mayo ? ", mayo" : "") + "}";
    }
}

// Step 2: Builder (step-by-step banata hai)
class BurgerBuilder {
    private Burger burger = new Burger();

    public BurgerBuilder bun(String bun) {
        burger.bun = bun;
        return this;  // ← chaining ke liye return this
    }

    public BurgerBuilder patty(String patty) {
        burger.patty = patty;
        return this;
    }

    public BurgerBuilder cheese(String cheese) {
        burger.cheese = cheese;
        return this;
    }

    public BurgerBuilder tomato(boolean tomato) {
        burger.tomato = tomato;
        return this;
    }

    public BurgerBuilder mayo(boolean mayo) {
        burger.mayo = mayo;
        return this;
    }

    public Burger build() {
        return burger;
    }
}

// Usage (fluent API - chaining)
Burger burger = new BurgerBuilder()
    .bun("Sesame Bun")
    .patty("Chicken")
    .cheese("Cheddar")
    .tomato(false)
    .mayo(true)
    .build();

System.out.println(burger);
// Burger{Sesame Bun, Chicken, Cheddar, mayo}
```

### Visual

```
    new BurgerBuilder()
        .bun("Sesame")        → Builder with bun set
        .patty("Chicken")     → Builder with bun + patty set
        .cheese("Cheddar")    → Builder with bun + patty + cheese set
        .mayo(true)           → Builder with all set
        .build()              → Returns final BURGER object

    ┌──────────────────┐
    │   BurgerBuilder   │
    │  bun: Sesame      │
    │  patty: Chicken   │    ┌──────────────┐
    │  cheese: Cheddar  │──→ │   BURGER      │
    │  tomato: false    │    │ bun: Sesame   │
    │  mayo: true       │    │ patty: Chicken│
    └──────────────────┘    │ cheese: Cheddar│
                             │ mayo: true     │
                             └──────────────┘
```

---

## PATTERN 4: ADAPTER (Purana Kaam Naye Mein!)

### Real Life Analogy

```
    ┌──────────────────────────────────────────────────────────────┐
    │  ELECTRIC PLUG ADAPTER:                                       │
    │                                                              │
    │  Tere paas purana phone hai (old charger - 2 pin plug).     │
    │  Naya socket hai (3 pin).                                     │
    │                                                              │
    │  Seedha nahi lagega!                                          │
    │  ADAPTER lagata hai → 2 pin ko 3 pin mein convert karta hai │
    │                                                              │
    │  Wahi Adapter Pattern hai!                                   │
    │  Purana code naye system mein use karna hai → Adapter lagao  │
    └──────────────────────────────────────────────────────────────┘
```

### Code Example

```java
// OLD printer (purana system)
class OldPrinter {
    void printOld(String text) {
        System.out.println("OLD PRINTER: " + text);
    }
}

// NEW printer interface (naya system)
interface NewPrinter {
    void printNew(String text);
}

// ADAPTER (purana ko naye mein convert kare)
class PrinterAdapter implements NewPrinter {
    private OldPrinter oldPrinter;  // Purana printer

    PrinterAdapter(OldPrinter oldPrinter) {
        this.oldPrinter = oldPrinter;
    }

    public void printNew(String text) {
        oldPrinter.printOld(text);  // Purane method se call karo
    }
}

// Usage
NewPrinter printer = new PrinterAdapter(new OldPrinter());
printer.printNew("Hello World");
// Output: OLD PRINTER: Hello World
```

### Visual

```
    BINA ADAPTER:
    Client ──printNew()──→ OldPrinter  ❌ (OldPrinter ko printNew pata nahi!)

    ADAPTER KE SAATH:
    Client ──printNew()──→ Adapter ──printOld()──→ OldPrinter ✅
                           (convert karta hai)
```

---

## PATTERN 5: DECORATOR (Cheez mein Extra Add Karo!)

### Real Life Analogy

```
    ┌──────────────────────────────────────────────────────────────┐
    │  ICE CREAM PARLOR:                                            │
    │                                                              │
    │  Basic ice cream: Vanilla = ₹30                              │
    │  + Chocolate sauce = ₹35  (+ ₹5)                             │
    │  + Nuts = ₹45  (+ ₹10)                                       │
    │  + Cherry = ₹50  (+ ₹5)                                      │
    │                                                              │
    │  Tum har cheez WRAP karte ho ice cream ke upar!              │
    │  Wahi Decorator Pattern hai!                                 │
    │                                                              │
    │  Basic cheez + extra features = Decorated cheez              │
    └──────────────────────────────────────────────────────────────┘
```

### Code Example

```java
// Step 1: Basic interface
interface Coffee {
    double getCost();
    String getDescription();
}

// Step 2: Basic coffee
class SimpleCoffee implements Coffee {
    public double getCost() { return 5.0; }
    public String getDescription() { return "Simple Coffee"; }
}

// Step 3: Decorator (abstract - wraps coffee)
abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;  // Wrapped coffee

    CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

// Step 4: Concrete decorators
class MilkDecorator extends CoffeeDecorator {
    MilkDecorator(Coffee coffee) { super(coffee); }

    public double getCost() {
        return coffee.getCost() + 2.0;  // Milk extra ₹2
    }
    public String getDescription() {
        return coffee.getDescription() + " + Milk";
    }
}

class SugarDecorator extends CoffeeDecorator {
    SugarDecorator(Coffee coffee) { super(coffee); }

    public double getCost() {
        return coffee.getCost() + 1.0;  // Sugar extra ₹1
    }
    public String getDescription() {
        return coffee.getDescription() + " + Sugar";
    }
}

// Usage (wrap karte jaao!)
Coffee coffee = new SimpleCoffee();                    // ₹5
coffee = new MilkDecorator(coffee);                    // ₹7
coffee = new SugarDecorator(coffee);                   // ₹8

System.out.println(coffee.getDescription());
// Simple Coffee + Milk + Sugar

System.out.println(coffee.getCost());
// 8.0
```

### Visual

```
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  SugarDecorator                                              │
    │  ┌────────────────────────────────────────────────────┐     │
    │  │  MilkDecorator                                      │     │
    │  │  ┌────────────────────────────────────────────┐   │     │
    │  │  │  SimpleCoffee                               │   │     │
    │  │  │  (₹5)                                       │   │     │
    │  │  └────────────────────────────────────────────┘   │     │
    │  │  + Milk (+₹2)                                      │     │
    │  └────────────────────────────────────────────────────┘     │
    │  + Sugar (+₹1)                                               │
    └──────────────────────────────────────────────────────────────┘

    Total = ₹5 + ₹2 + ₹1 = ₹8
```

---

## PATTERN 6: FACADE (Complex Ka Asan Chehra!)

### Real Life Analogy

```
    ┌──────────────────────────────────────────────────────────────┐
    │  HOSPITAL:                                                    │
    │                                                              │
    │  Patient: "Mujhe operation karwana hai"                      │
    │                                                              │
    │  Internally kya hota hai:                                     │
    │  1. Reception pe registration                                │
    │  2. Doctor se milna                                           │
    │  3. Test karna                                                │
    │  4. Medicine lena                                             │
    │  5. Operation karna                                           │
    │  6. Billing karna                                             │
    │                                                              │
    │  Patient ko sab nahi pata!                                    │
    │  Patient RECEPTIONIST ko bolta hai: "Mujhe operation karna hai"│
    │  Receptionist sab manage karta hai!                           │
    │                                                              │
    │  Receptionist = FACADE                                       │
    └──────────────────────────────────────────────────────────────┘
```

### Code Example

```java
// Complex subsystems
class CPU {
    void start() { System.out.println("CPU started"); }
}

class Memory {
    void load() { System.out.println("Memory loaded"); }
}

class HardDrive {
    void read() { System.out.println("HardDrive reading"); }
}

class Monitor {
    void display() { System.out.println("Monitor displaying"); }
}

// FACADE (sabko simplify karta hai)
class ComputerFacade {
    private CPU cpu = new CPU();
    private Memory memory = new Memory();
    private HardDrive hd = new HardDrive();
    private Monitor monitor = new Monitor();

    public void startComputer() {
        // Internally complex hai, but bahar se easy
        hd.read();
        memory.load();
        cpu.start();
        monitor.display();
    }
}

// Usage (bilkul easy!)
ComputerFacade computer = new ComputerFacade();
computer.startComputer();  // Done! Sab ho gaya!
```

---

## PATTERN 7: OBSERVER (Kuch Badla Toh Sabko Batao!)

### Real Life Analogy

```
    ┌──────────────────────────────────────────────────────────────┐
    │  YOUTUBE SUBSCRIPTION:                                        │
    │                                                              │
    │  Tu YouTuber ko SUBSCRIBE karta hai.                         │
    │  Jab YouTuber naya video daalta hai → tuhe NOTIFICATION aata │
    │                                                              │
    │  YouTuber = Subject (jisko log subscribe karte hain)         │
    │  Tu = Observer (jo dekh raha hai)                             │
    │  Video upload = Notification                                  │
    │                                                              │
    │  Koi bhi subscribe kar sakta hai, unsubscribe kar sakta hai  │
    └──────────────────────────────────────────────────────────────┘
```

### Code Example

```java
// Observer interface (sab subscribers ka common kaam)
interface Observer {
    void update(String news);
}

// Subject (YouTuber - jisko log dekhte hain)
class NewsChannel {
    private List<Observer> subscribers = new ArrayList<>();
    private String news;

    void subscribe(Observer o) { subscribers.add(o); }
    void unsubscribe(Observer o) { subscribers.remove(o); }

    void notifyAll(String news) {
        this.news = news;
        for (Observer o : subscribers) {
            o.update(news);  // Sabko batao!
        }
    }
}

// Concrete observers (subscribers)
class Subscriber implements Observer {
    String name;
    Subscriber(String name) { this.name = name; }

    public void update(String news) {
        System.out.println(name + " ko news mili: " + news);
    }
}

// Usage
NewsChannel channel = new NewsChannel();

Subscriber s1 = new Subscriber("Rahul");
Subscriber s2 = new Subscriber("Amit");
Subscriber s3 = new Subscriber("Priya");

channel.subscribe(s1);
channel.subscribe(s2);
channel.subscribe(s3);

channel.notifyAll("Java 25 release hua!");
// Rahul ko news mili: Java 25 release hua!
// Amit ko news mili: Java 25 release hua!
// Priya ko news mili: Java 25 release hua!

channel.unsubscribe(s2);
channel.notifyAll("Spring Boot update!");
// Rahul ko news mili: Spring Boot update!
// Priya ko news mili: Spring Boot update!
// (Amit ko nahi gaya - unsubscribe kar diya)
```

---

## PATTERN 8: STRATEGY (Algorithm Badlo Bina Code Badle!)

### Real Life Analogy

```
    ┌──────────────────────────────────────────────────────────────┐
    │  ONLINE PAYMENT:                                              │
    │                                                              │
    │  Tujhe pay karna hai. Tu choose karta hai:                   │
    │  "Aaj UPI se karunga"  → UPI method use hoga                 │
    │  "Kal card se karunga"  → Card method use hoga               │
    │  "Parso cash se karunga"→ Cash method use hoga               │
    │                                                              │
    │  Payment ka TARIIKA badal sakta hai, but payment ka kaam     │
    │  same rahega!                                                 │
    │                                                              │
    │  Wahi Strategy Pattern hai!                                  │
    └──────────────────────────────────────────────────────────────┘
```

### Code Example

```java
// Strategy interface (common payment kaam)
interface PaymentStrategy {
    void pay(int amount);
}

// Concrete strategies (alag-alag payment methods)
class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " via Credit Card");
    }
}

class UPIPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " via UPI");
    }
}

class CashPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " via Cash");
    }
}

// Context (jo strategy use karega)
class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;  // Runtime mein badal sakta hai!
    }

    void checkout(int amount) {
        paymentStrategy.pay(amount);
    }
}

// Usage
Cart cart = new ShoppingCart();

cart.setPaymentStrategy(new UPIPayment());
cart.checkout(500);     // Paid 500 via UPI

cart.setPaymentStrategy(new CreditCardPayment());
cart.checkout(1000);    // Paid 1000 via Credit Card
```

---

## PATTERN 9: TEMPLATE METHOD (Same Process, Different Details!)

### Real Life Analogy

```
    ┌──────────────────────────────────────────────────────────────┐
    │  MAKING TEA vs COFFEE:                                        │
    │                                                              │
    │  Dono ka process SAME hai:                                    │
    │  1. Water garam karo                                          │
    │  2. Dalo (chai patti ya coffee powder)                        │
    │  3. Ubalo                                                     │
    │  4. Pour karo                                                 │
    │                                                              │
    │  Sirf Step 2 mein FARK hai!                                   │
    │                                                              │
    │  Same skeleton, different details!                           │
    └──────────────────────────────────────────────────────────────┘
```

### Code Example

```java
// Abstract class (template)
abstract class Beverage {
    // Template method (final - subclass override nahi kar sakta)
    final void prepare() {
        boilWater();        // Common step
        brew();             // ← Different for each subclass
        pourInCup();        // Common step
        addCondiments();    // ← Different for each subclass
    }

    void boilWater() { System.out.println("Water boiled"); }
    void pourInCup() { System.out.println("Poured in cup"); }

    // Abstract methods (subclass decide karega)
    abstract void brew();
    abstract void addCondiments();
}

// Concrete classes
class Tea extends Beverage {
    void brew() { System.out.println("Steeping tea bags"); }
    void addCondiments() { System.out.println("Adding lemon"); }
}

class Coffee extends Beverage {
    void brew() { System.out.println("Dripping coffee through filter"); }
    void addCondiments() { System.out.println("Adding sugar and milk"); }
}

// Usage
Beverage tea = new Tea();
tea.prepare();
// Water boiled
// Steeping tea bags
// Poured in cup
// Adding lemon

Beverage coffee = new Coffee();
coffee.prepare();
// Water boiled
// Dripping coffee through filter
// Poured in cup
// Adding sugar and milk
```

---

## PATTERN 10: CHAIN OF RESPONSIBILITY (Ek Se Na Ho Toh Agla!)

### Real Life Analogy

```
    ┌──────────────────────────────────────────────────────────────┐
    │  COMPANY MEIN APPROVAL CHAIN:                                 │
    │                                                              │
    │  ₹1000 ka expense → Team Lead approve karega                 │
    │  ₹5000 ka expense → Manager approve karega                   │
    │  ₹50000 ka expense → Director approve karega                 │
    │                                                              │
    │  Request EK EK se jaati hai. Agar koi handle nahi kar sakta, │
    │  toh AGLE ko bhejta hai.                                      │
    └──────────────────────────────────────────────────────────────┘
```

### Code Example

```java
// Handler interface
abstract class Handler {
    protected Handler nextHandler;

    Handler setNext(Handler next) {
        this.nextHandler = next;
        return next;
    }

    void handle(int amount) {
        if (nextHandler != null) {
            nextHandler.handle(amount);
        }
    }
}

class TeamLead extends Handler {
    void handle(int amount) {
        if (amount <= 1000) {
            System.out.println("Team Lead approved: ₹" + amount);
        } else {
            super.handle(amount);  // Agle ko bhejo
        }
    }
}

class Manager extends Handler {
    void handle(int amount) {
        if (amount <= 5000) {
            System.out.println("Manager approved: ₹" + amount);
        } else {
            super.handle(amount);
        }
    }
}

class Director extends Handler {
    void handle(int amount) {
        System.out.println("Director approved: ₹" + amount);
    }
}

// Usage (chain banao)
Handler chain = new TeamLead();
chain.setNext(new Manager()).setNext(new Director());

chain.handle(500);    // Team Lead approved: ₹500
chain.handle(3000);   // Manager approved: ₹3000
chain.handle(10000);  // Director approved: ₹10000
```

---

## QUICK RECAP CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║              DESIGN PATTERNS - YAAD RAKHO!                       ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  SINGLETON     → Sirf EK object (Principal, Logger)             ║
║  FACTORY       → Object kaunsa banega, decide factory karegi   ║
║  BUILDER       → Complex cheez step-by-step banao (Burger)      ║
║  ADAPTER       → Purana kaam naye mein convert (Plug adapter)   ║
║  DECORATOR     → Extra features add karo (Ice cream toppings)   ║
║  FACADE        → Complex system ka easy chehra (Receptionist)   ║
║  OBSERVER      → Badla toh sabko batao (YouTube subscribe)      ║
║  STRATEGY      → Algorithm badlo bina code badle (Payment)      ║
║  TEMPLATE      → Same process, different details (Tea/Coffee)   ║
║  CHAIN         → Ek se na ho toh agla (Approval chain)          ║
║                                                                  ║
║  CREATIONAL:  Singleton, Factory, Builder, Abstract Factory,    ║
║               Prototype                                          ║
║  STRUCTURAL:  Adapter, Decorator, Facade, Proxy, Composite,    ║
║               Bridge, Flyweight                                  ║
║  BEHAVIORAL:  Observer, Strategy, Template, Chain, Command,     ║
║               Iterator, State, Mediator, Visitor, Memento       ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Zero se sikho, interview mein impress karo! 🚀*
