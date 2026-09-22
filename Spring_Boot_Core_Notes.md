# Spring Core - Zero se Seekho (Simple Hinglish)

---

## 1. SPRING KYA HAI?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Spring = Ek Java Framework jo development easy banata hai   │
    │                                                              │
    │  Real Life Analogy:                                          │
    │  Tum ek restaurant khol rahe ho                               │
    │                                                              │
    │  BINA Spring: Khud sab kuch banana padega                     │
    │  ✗ Tables khud banana                                         │
    │  ✗ Kitchen khud banana                                        │
    │  ✗ Bills khud banana                                          │
    │                                                              │
    │  SPRING ke saath: Sab ready milta hai                         │
    │  ✓ Tables ready                                               │
    │  ✓ Kitchen ready                                              │
    │  ✓ Billing system ready                                       │
    │  Tum bas KHAANA banao (business logic likho)                  │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. IoC (Inversion of Control)

```
    ┌──────────────────────────────────────────────────────────────┐
    │  IoC = CONTROL kaam karne wale ke haath se hata ke           │
    │        Framework ke haath mein dena                           │
    │                                                              │
    │  BINA IoC (Tum control karte ho):                            │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  Tum: "Mujhe Object banao, inject karo, manage karo" │    │
    │  │  Tum sab karte ho (hard work!)                        │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  IoC KE SAATH (Framework control karta hai):                 │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  Framework: "Main sab karunga, tum bas use karo"     │    │
    │  │  Tum sirf kaam karte ho (easy!)                      │    │
    │  └──────────────────────────────────────────────────────┘    │
    └──────────────────────────────────────────────────────────────┘
```

### Simple Example:

```java
// ═══════════════════════════════════════════════════════════════
// BINA IoC (Tum khud sab karte ho)
// ═══════════════════════════════════════════════════════════════
public class Car {
    private Engine engine;

    public Car() {
        this.engine = new Engine();  // Tum khud object bana rahe ho!
    }
}

// ═══════════════════════════════════════════════════════════════
// IoC KE SAATH (Spring banata hai, tum use karte ho)
// ═══════════════════════════════════════════════════════════════
public class Car {
    private Engine engine;

    public Car(Engine engine) {  // Spring khud Engine dega!
        this.engine = engine;
    }
}
```

---

## 3. DI (Dependency Injection)

```
    ┌──────────────────────────────────────────────────────────────┐
    │  DI = Object ko uski zarurat ki cheezein khud dhoondh ke    │
    │       nahi, koi aur DE de                                     │
    │                                                              │
    │  Real Life Analogy:                                          │
    │  Tumhe khana khana hai                                       │
    │                                                              │
    │  BINA DI: Tum khud jaake sabzi, daal, chawal lao (hard!)     │
    │                                                              │
    │  DI: Tum bas bolo "mujhe khana chahiye"                       │
    │      Waiter sab le aayega (easy!)                            │
    └──────────────────────────────────────────────────────────────┘
```

### DI Ke 3 Types:

```java
// ═══════════════════════════════════════════════════════════════
// 1. CONSTRUCTOR INJECTION (Best - use this!)
// ═══════════════════════════════════════════════════════════════
@Service
public class CarService {
    private final Engine engine;  // final = immutable

    @Autowired  // Spring ko batao: mujhe Engine do
    public CarService(Engine engine) {
        this.engine = engine;
    }
}

// ═══════════════════════════════════════════════════════════════
// 2. SETTER INJECTION
// ═══════════════════════════════════════════════════════════════
@Service
public class CarService {
    private Engine engine;

    @Autowired
    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}

// ═══════════════════════════════════════════════════════════════
// 3. FIELD INJECTION (Avoid karo - hard to test)
// ═══════════════════════════════════════════════════════════════
@Service
public class CarService {
    @Autowired
    private Engine engine;  // Direct field injection
}
```

### DI Visual:

```
    Dependency Injection Flow:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  1. Tum CarService banao (Spring ko bolo)                    │
    │         │                                                    │
    │         ▼                                                    │
    │  2. Spring check karega: "CarService ko kya chahiye?"        │
    │         │                                                    │
    │         ▼                                                    │
    │  3. Spring bolega: "Engine chahiye!"                         │
    │         │                                                    │
    │         ▼                                                    │
    │  4. Spring Engine object banayega                            │
    │         │                                                    │
    │         ▼                                                    │
    │  5. Spring Engine ko CarService mein inject kar dega        │
    │         │                                                    │
    │         ▼                                                    │
    │  6. Tum bas CarService use karo! ✅                          │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 4. BEAN KYA HAI?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Bean = Object jo Spring banata hai aur manage karta hai     │
    │                                                              │
    │  Real Life Analogy:                                          │
    │  Bean = Factory mein bana product                            │
    │                                                              │
    │  Spring Container = Factory                                   │
    │  Bean = Product (Object)                                     │
    │                                                              │
    │  Tum sirf order karo, Spring banake dega!                     │
    └──────────────────────────────────────────────────────────────┘
```

### Bean Banane Ke Tarike:

```java
// ═══════════════════════════════════════════════════════════════
// METHOD 1: @Component, @Service, @Repository, @Controller
// ═══════════════════════════════════════════════════════════════
@Component  // Generic component
public class MyComponent { }

@Service   // Business logic ke liye
public class MyService { }

@Repository // Database operations ke liye
public class MyRepository { }

@RestController // REST API ke liye
public class MyController { }

// ═══════════════════════════════════════════════════════════════
// METHOD 2: @Bean (Java configuration class mein)
// ═══════════════════════════════════════════════════════════════
@Configuration
public class AppConfig {
    @Bean
    public Engine engine() {
        return new Engine();  // Tum khud object bana rahe ho
    }
}
```

### Bean Lifecycle:

```
    Bean Lifecycle (Simple):
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  1. Bean Instantiation  → Object banao                      │
    │         │                                                    │
    │         ▼                                                    │
    │  2. Populate Properties → @Autowired inject karo            │
    │         │                                                    │
    │         ▼                                                    │
    │  3. BeanNameAware      → Naam set karo (agar ho toh)        │
    │         │                                                    │
    │         ▼                                                    │
    │  4. BeanFactoryAware   → Factory reference do              │
    │         │                                                    │
    │         ▼                                                    │
    │  5. @PostConstruct     → Initialization code chalao         │
    │         │                                                    │
    │         ▼                                                    │
    │  6. Bean Ready ✅      → Use karo!                          │
    │         │                                                    │
    │         ▼                                                    │
    │  7. @PreDestroy        → Cleanup code chalao                │
    │         │                                                    │
    │         ▼                                                    │
    │  8. Bean Destroyed     → Object destroy ho gaya             │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

```java
@Service
public class MyService {
    private static final Logger log = LoggerFactory.getLogger(MyService.class);

    @PostConstruct  // Bean banne ke baad chalega
    public void init() {
        log.info("Bean initialized!");
    }

    @PreDestroy  // Bean destroy hone se pehle chalega
    public void cleanup() {
        log.info("Bean destroyed!");
    }
}
```

---

## 5. SPRING ANNOTATIONS CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║              SPRING ANNOTATIONS (Simple)                         ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  BEAN CREATION:                                                  ║
║  @Component    → Generic component (koi bhi class)              ║
║  @Service      → Business logic (Service layer)                 ║
║  @Repository   → Database operations (DAO layer)                ║
║  @Controller   → Web controller (MVC)                           ║
║  @RestController → REST API controller (@Controller + @Response)║
║  @Configuration → Java configuration class                      ║
║  @Bean         → Method-level bean creation                     ║
║                                                                  ║
║  DEPENDENCY INJECTION:                                           ║
║  @Autowired    → Automatic injection (Spring ko batao)          ║
║  @Qualifier    → Specific bean chahiye (multiple options ho toh)║
║  @Resource     → Name-based injection (@Autowired ka alternative)║
║                                                                  ║
║  SCOPES:                                                         ║
║  @Scope("singleton") → Ek hi object (default)                  ║
║  @Scope("prototype") → Har baar naya object                    ║
║  @Scope("request")   → Har request mein naya                   ║
║  @Scope("session")   → Har session mein naya                   ║
║                                                                  ║
║  LIFECYCLE:                                                      ║
║  @PostConstruct  → Bean banne ke baad chalega                  ║
║  @PreDestroy     → Bean destroy hone se pehle chalega          ║
║                                                                  ║
║  WEB:                                                            ║
║  @RequestMapping    → URL route define karo                     ║
║  @GetMapping        → GET request handle karo                   ║
║  @PostMapping       → POST request handle karo                  ║
║  @PutMapping        → PUT request handle karo                   ║
║  @DeleteMapping     → DELETE request handle karo                ║
║  @RequestBody       → Request body read karo (JSON)             ║
║  @ResponseBody      → Response body mein bhejo (JSON)           ║
║  @PathVariable      → URL se value lo (/user/{id})              ║
║  @RequestParam      → Query parameter lo (/user?id=1)           ║
║                                                                  ║
║  CONFIGURATION:                                                  ║
║  @Value("${property.name}") → application.properties se value   ║
║  @EnableAutoConfiguration  → Auto configuration enable karo     ║
║  @SpringBootApplication     → Main annotation (start mein)      ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 6. SPRING CONTAINER

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Spring Container = Jo beans banata hai aur manage karta hai│
    │                                                              │
    │  Container ke andar:                                         │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  1. BeanFactory  → Basic container (old)            │    │
    │  │  2. ApplicationContext → Advanced container (new)    │    │
    │  │     - BeanFactory sab karta hai PLUS:                │    │
    │  │     - Event publishing                              │    │
    │  │     - AOP integration                               │    │
    │  │     - Internationalization                          │    │
    │  │     - Web application support                       │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  ApplicationContext USE KARO (BeanFactory mat use karo)       │
    └──────────────────────────────────────────────────────────────┘
```

### Container Types:

```java
// ═══════════════════════════════════════════════════════════════
// 1. ANNOTATION CONFIG (Most Common)
// ═══════════════════════════════════════════════════════════════
@SpringBootApplication  // = @Configuration + @EnableAutoConfiguration + @ComponentScan
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);  // Container start!
    }
}

// ═══════════════════════════════════════════════════════════════
// 2. XML CONFIG (Old way - avoid karo)
// ═══════════════════════════════════════════════════════════════
ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
MyService service = ctx.getBean(MyService.class);

// ═══════════════════════════════════════════════════════════════
// 3. JAVA CONFIG (Modern way)
// ═══════════════════════════════════════════════════════════════
@Configuration
public class AppConfig {
    @Bean
    public MyService myService() {
        return new MyService();
    }
}
```

---

## 7. SPRING MVC (Model-View-Controller)

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Spring MVC = Web application banane ka pattern              │
    │                                                              │
    │  Real Life Analogy: Restaurant                                │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  Customer → Waiter → Kitchen → Waiter → Customer     │    │
    │  │  (Request) →(Controller)→(Model)→(View)→(Response)   │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  MVC Flow:                                                   │
    │  1. Client Request → DispatcherServlet (front controller)    │
    │  2. DispatcherServlet → HandlerMapping → Controller         │
    │  3. Controller → Service → Repository → Database            │
    │  4. Controller → Model (data) → View (response)             │
    │  5. Response → Client                                        │
    └──────────────────────────────────────────────────────────────┘
```

### MVC Visual:

```
    Spring MVC Flow:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  ┌────────┐    ┌────────────────┐    ┌──────────────┐        │
    │  │ Client │───▶│DispatcherServlet│───▶│HandlerMapping│        │
    │  └────────┘    │(Front Controller)│    └──────┬───────┘        │
    │                └────────────────┘           │                │
    │                                              ▼                │
    │                                     ┌──────────────┐          │
    │                                     │  Controller   │          │
    │                                     └──────┬───────┘          │
    │                                              │                │
    │                    ┌────────────────────────┼──────────┐     │
    │                    ▼                        ▼          │     │
    │             ┌──────────────┐        ┌──────────────┐   │     │
    │             │   Service    │        │    Model     │   │     │
    │             └──────┬───────┘        └──────┬───────┘   │     │
    │                    │                        │          │     │
    │                    ▼                        ▼          │     │
    │             ┌──────────────┐        ┌──────────────┐   │     │
    │             │  Repository  │        │    View      │◀──┘     │
    │             └──────┬───────┘        └──────┬───────┘         │
    │                    │                        │                │
    │                    ▼                        │                │
    │             ┌──────────────┐                │                │
    │             │  Database    │                │                │
    │             └──────────────┘                │                │
    │                                              │                │
    │  ┌────────┐    ┌────────────────┐           │                │
    │  │ Client │◀───│  Response      │◀──────────┘                │
    │  └────────┘    └────────────────┘                             │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

```java
// ═══════════════════════════════════════════════════════════════
// CONTROLLER EXAMPLE
// ═══════════════════════════════════════════════════════════════
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET /api/users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET /api/users/1
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // POST /api/users
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // PUT /api/users/1
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // DELETE /api/users/1
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
```

---

## 8. SPRING BOOT KYA HAI?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Spring Boot = Spring ko EASY banana wala framework          │
    │                                                              │
    │  BINA Spring Boot (Old Spring):                              │
    │  ✗ XML configuration file likho                              │
    │  ✗ Server manually setup karo                                │
    │  ✗ Dependencies manually add karo                            │
    │  ✗ Database setup khud karo                                  │
    │  ✗ Security khud configure karo                              │
    │                                                              │
    │  SPRING BOOT ke saath:                                       │
    │  ✓ Zero XML configuration                                    │
    │  ✓ Embedded server (Tomcat/Undertow) ready                  │
    │  ✓ Auto-configuration (sab kuch auto detect)                │
    │  ✓ Starter dependencies (one line mein sab add)              │
    │  ✓ Actuator (monitoring ready)                               │
    │  ✓ Ready for production! 🚀                                  │
    └──────────────────────────────────────────────────────────────┘
```

### Spring Boot Key Features:

```
╔══════════════════════════════════════════════════════════════════╗
║              SPRING BOOT FEATURES                                ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  1. AUTO-CONFIGURATION                                          ║
║     - Khud samajhta hai kya chahiye                             ║
║     - Example: H2 database in classpath → auto configure!       ║
║                                                                  ║
║  2. EMBEDDED SERVER                                              ║
║     - Tomcat/Undertow/Jetty built-in                            ║
║     - java -jar app.jar → Server start! 🚀                      ║
║                                                                  ║
║  3. STARTER DEPENDENCIES                                         ║
║     - spring-boot-starter-web → Web app ready                   ║
║     - spring-boot-starter-data-jpa → JPA ready                  ║
║     - spring-boot-starter-security → Security ready             ║
║     - spring-boot-starter-test → Testing ready                  ║
║                                                                  ║
║  4. APPLICATION.PROPERTIES                                       ║
║     - Sab configuration ek file mein                            ║
║     - server.port=8080                                          ║
║     - spring.datasource.url=jdbc:h2:mem:test                    ║
║                                                                  ║
║  5. ACTUATOR                                                     ║
║     - Monitoring endpoints (/health, /metrics, /info)           ║
║                                                                  ║
║  6. SPRING INITIALIZER                                           ║
║     - start.spring.io se project generate karo                  ║
║     - 1 click mein ready project!                               ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

### Spring Boot Project Structure:

```
    ┌──────────────────────────────────────────────────────────────┐
    │  my-spring-boot-app/                                         │
    │  ├── src/                                                    │
    │  │   └── main/                                              │
    │  │       ├── java/com/example/app/                          │
    │  │       │   ├── MyApp.java         ← Main class           │
    │  │       │   ├── controller/        ← REST controllers     │
    │  │       │   ├── service/           ← Business logic       │
    │  │       │   ├── repository/        ← Database operations  │
    │  │       │   └── model/             ← Entity classes       │
    │  │       └── resources/                                    │
    │  │           ├── application.properties ← Config file      │
    │  │           └── static/              ← Static files (CSS) │
    │  │           └── templates/           ← Templates (Thymeleaf)│
    │  ├── pom.xml          ← Maven dependencies                 │
    │  └── build.gradle     ← Gradle dependencies (alt)          │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

### Spring Boot Main Class:

```java
// ═══════════════════════════════════════════════════════════════
// MAIN CLASS (Entry point)
// ═══════════════════════════════════════════════════════════════
@SpringBootApplication  // = @Configuration + @EnableAutoConfiguration + @ComponentScan
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);  // Server start!
    }
}
```

### application.properties:

```properties
# ═══════════════════════════════════════════════════════════════
# SPRING BOOT CONFIGURATION
# ═══════════════════════════════════════════════════════════════

# Server port
server.port=8080

# Database (H2 for development)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console (database browser)
spring.h2.console.enabled=true

# Application name
spring.application.name=my-spring-boot-app
```

---

## 9. SPRING BOOT INTERVIEW QUESTIONS (40+)

### ⭐ BASIC

**Q1: What is Spring?**
> Ek Java framework jo development easy banata hai with IoC, DI, and other features.

**Q2: What is IoC?**
> Control object banane ka kaam tumse hata ke Spring ke haath mein dena.

**Q3: What is Dependency Injection?**
> Object ko uski zarurat ki dependencies khud dhoondh ke nahi, Spring inject karta hai.

**Q4: What is a Bean?**
> Object jo Spring banata hai aur manage karta hai.

**Q5: What is Spring Boot?**
> Spring ko easy banane wala framework with auto-configuration, embedded server, and starter dependencies.

**Q6: What is @SpringBootApplication?**
> Main annotation = @Configuration + @EnableAutoConfiguration + @ComponentScan

**Q7: What is difference between @Component and @Service?**
```
@Component: Generic component
@Service: Business logic (semantically different, same functionality)
@Repository: Database operations
@Controller: Web controller
```

**Q8: What is @Autowired?**
> Spring ko batao ki is field/method ko dependency inject karo.

**Q9: What is difference between @ComponentScan and @EnableAutoConfiguration?**
```
@ComponentScan: Packages scan karo for components
@EnableAutoConfiguration: Auto-configure based on classpath
```

**Q10: What is embedded server?**
> Server jo application ke saath aata hai (Tomcat, Undertow). No need to install separately.

---

### ⭐⭐ MIDDLE

**Q11: What is difference between Constructor and Setter injection?**
```
Constructor: Final field, immutable, test-friendly (BEST)
Setter: Mutable, easy to change after creation
Field: Hard to test (avoid!)
```

**Q12: What is Bean lifecycle?**
```
Instantiation → Populate Properties → @PostConstruct → 
Ready → @PreDestroy → Destroyed
```

**Q13: What is difference between Singleton and Prototype scope?**
```
Singleton: Ek hi object (default)
Prototype: Har baar naya object
```

**Q14: What is Spring MVC?**
> Model-View-Controller pattern for web applications.

**Q15: What is DispatcherServlet?**
> Front controller jo saare requests handle karta hai.

**Q16: What is difference between @RequestMapping and @GetMapping?**
```
@RequestMapping: Any HTTP method
@GetMapping: Sirf GET requests (specific)
```

**Q17: What is @PathVariable vs @RequestParam?**
```
PathVariable: URL se (/user/{id})
RequestParam: Query string se (/user?id=1)
```

**Q18: What is REST API?**
> HTTP methods (GET, POST, PUT, DELETE) use karke data exchange.

**Q19: What is difference between @RestController and @Controller?**
```
@RestController: @Controller + @ResponseBody (JSON auto)
@Controller: View return karta hai (HTML)
```

**Q20: What is Spring Boot auto-configuration?**
> Khud samajhta hai kya chahiye based on classpath dependencies.

---

### ⭐⭐⭐ ADVANCED

**Q21: What is Spring Boot Starter?**
> Pre-configured dependencies ka bundle. Ek line mein sab add ho jata hai.

**Q22: What is Actuator?**
> Monitoring endpoints (/health, /metrics, /info) provide karta hai.

**Q23: What is difference between application.properties and application.yml?**
```
properties: Key=value format
yml: Hierarchical format (more readable)
Both are same functionality
```

**Q24: What is @ConfigurationProperties?**
> Type-safe configuration binding to Java class.

**Q25: What is Spring Boot DevTools?**
> Development tools - hot reload, live reload, automatic restart.

**Q26: What is Spring Boot Profile?**
> Different configurations for different environments (dev, test, prod).

```properties
# application-dev.properties
server.port=8080

# application-prod.properties
server.port=80
```

**Q27: What is Spring Cloud?**
> Tools for microservices - config server, service discovery, circuit breaker.

**Q28: What is difference between Monolith and Microservices?**
```
Monolith: Single application (all in one)
Microservices: Small independent services
Spring Boot is great for microservices!
```

**Q29: What is Service Discovery?**
> Microservices ko ek doosre ko dhoondne ka mechanism (Eureka, Consul).

**Q30: What is Circuit Breaker?**
> Failure handle karne ka pattern (Resilience4j, Hystrix).

**Q31: What is Spring Boot Test?**
> Testing framework with @SpringBootTest, @WebMvcTest, @DataJpaTest.

**Q32: What is @MockBean?**
> Test mein real bean ki jagah mock object inject karo.

**Q33: What is difference between @SpringBootTest and @WebMvcTest?**
```
@SpringBootTest: Full application context
@WebMvcTest: Sirf web layer (controller + web config)
```

**Q34: What is Spring Boot Security?**
> Authentication (kaun ho) and Authorization (kya kar sakte ho) handle karta hai.

**Q35: What is JWT in Spring Boot?**
> JSON Web Token - stateless authentication ke liye.

**Q36: What is CORS?**
> Cross-Origin Resource Sharing - different domains se requests allow karna.

**Q37: What is @CrossOrigin?**
> CORS enable karna specific controller/method ke liye.

**Q38: What is Spring Boot Logging?**
> SLF4J + Logback by default. Application.properties se configure karo.

**Q39: What is Spring Boot Metrics?**
> Application performance track karna (Actuator + Micrometer).

**Q40: What is best practice for Spring Boot project structure?**
```
controller/  → REST endpoints
service/     → Business logic
repository/  → Database operations
model/       → Entity classes
config/      → Configuration classes
dto/         → Data Transfer Objects
```

---

*Last Updated: September 2026*
*Covers: Spring Core, IoC, DI, Beans, MVC, Spring Boot, 40 Interview Questions*
