# Hibernate & JPA - Zero se Seekho (Simple Hinglish)

---

## 1. HIBERNATE KYA HAI?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Hibernate = ORM Framework (Object-Relational Mapping)       │
    │                                                              │
    │  ORM = Java Object ↔ Database Table ka mapping               │
    │                                                              │
    │  Real Life Analogy:                                          │
    │  Tumhe Hindi mein likha hua English mein chahiye             │
    │                                                              │
    │  BINA Hibernate (Manual SQL):                                │
    │  ✗ Khud SQL query likho                                      │
    │  ✗ Khud result set parse karo                                │
    │  ✗ Khud object banao                                         │
    │  ✗ Har table ke liye alag code                               │
    │                                                              │
    │  WITH Hibernate:                                             │
    │  ✓ Java object use karo                                      │
    │  ✓ Hibernate khud SQL banayega                              │
    │  ✓ Hibernate khud object banayega                            │
    │  ✓ Tum sirf Java code likho!                                 │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. ORM VISUAL

```
    ORM Mapping:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  JAVA OBJECT (In Memory)         DATABASE TABLE (SQL)        │
    │  ┌──────────────────┐           ┌──────────────────────┐     │
    │  │ User              │           │ users                │     │
    │  │ ─────             │           │ ─────                │     │
    │  │ id: 1             │    ══▶    │ id: 1                │     │
    │  │ name: "Amit"      │           │ name: "Amit"         │     │
    │  │ email: "a@b.com"  │           │ email: "a@b.com"     │     │
    │  │ age: 25           │           │ age: 25              │     │
    │  └──────────────────┘           └──────────────────────┘     │
    │                                                              │
    │  Hibernate automatically:                                    │
    │  ✅ Object → INSERT INTO users (...) VALUES (...)            │
    │  ✅ Object → UPDATE users SET ... WHERE id = 1              │
    │  ✅ SELECT * FROM users → Object                            │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 3. JPA vs HIBERNATE

```
    ┌──────────────────────────────────────────────────────────────┐
    │  JPA (Java Persistence API) = SPECIFICATION (rules)          │
    │  Hibernate = IMPLEMENTATION (actual code)                    │
    │                                                              │
    │  Real Life Analogy:                                          │
    │  JPA = Blueprint (design)                                    │
    │  Hibernate = Actual building (implementation)                │
    │                                                              │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │                                                      │    │
    │  │  JPA: "Yeh karna chahiye" (interface/rules)          │    │
    │  │       ↓                                              │    │
    │  │  Hibernate: "Main yeh karunga" (implementation)      │    │
    │  │                                                      │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  Other JPA Implementations: EclipseLink, OpenJPA, etc.      │
    │  Hibernate is MOST POPULAR JPA implementation!              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 4. ENTITY & ANNOTATIONS

```java
// ═══════════════════════════════════════════════════════════════
// ENTITY = Java class jo database table se map hota hai
// ═══════════════════════════════════════════════════════════════
@Entity                          // Yeh class database table hai!
@Table(name = "users")           // Table ka naam "users"
public class User {

    @Id                          // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;

    @Column(nullable = false, length = 100)  // Column: NOT NULL, max 100 chars
    private String name;

    @Column(unique = true)       // Unique constraint
    private String email;

    private int age;             // Simple column

    @Column(name = "created_at") // Custom column name
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // ... more getters/setters
}
```

### Entity Mapping Visual:

```
    Java Object → Database Table:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  @Entity class User {                                        │
    │      @Id                                              │     │
    │      @GeneratedValue                                │     │
    │      private Long id;                               │     │
    │  }                                               │     │
    │                                                  ▼     │
    │                                        ┌─────────────┐      │
    │  @Table(name = "users")  ──────────▶  │ CREATE TABLE │      │
    │                                        │   users (    │      │
    │  @Column(nullable = false)             │   id BIGINT, │      │
    │  @Column(unique = true)                │   name VARCHAR│     │
    │                                        │   ...        │      │
    │                                        │   );         │      │
    │                                        └─────────────┘      │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 5. RELATIONSHIPS

```
    ┌──────────────────────────────────────────────────────────────┐
    │  4 Types of Relationships:                                   │
    │                                                              │
    │  1. ONE-TO-ONE (1:1)                                         │
    │     Example: Aadhaar ↔ User                                  │
    │     One user has one Aadhaar, one Aadhaar belongs to one user│
    │                                                              │
    │  2. ONE-TO-MANY (1:N)                                        │
    │     Example: Author ↔ Books                                  │
    │     One author has many books, one book has one author       │
    │                                                              │
    │  3. MANY-TO-ONE (N:1)                                        │
    │     Example: Book ↔ Author                                   │
    │     Many books belong to one author                          │
    │                                                              │
    │  4. MANY-TO-MANY (M:N)                                       │
    │     Example: Student ↔ Course                                │
    │     Many students in many courses                            │
    └──────────────────────────────────────────────────────────────┘
```

### Relationship Visuals:

```
    ONE-TO-ONE (1:1):
    ┌──────────────┐         ┌──────────────┐
    │    User      │         │  Aadhaar     │
    │──────────────│    1:1  │──────────────│
    │ id: 1        │────────▶│ id: 1        │
    │ name: "Amit" │         │ number: "XXX"│
    └──────────────┘         └──────────────┘

    ONE-TO-MANY (1:N):
    ┌──────────────┐         ┌──────────────┐
    │   Author     │         │    Book      │
    │──────────────│    1:N  │──────────────│
    │ id: 1        │────────▶│ id: 1        │
    │ name: "JK"   │────────▶│ id: 2        │
    │              │────────▶│ id: 3        │
    └──────────────┘         └──────────────┘
         1 Author                Many Books

    MANY-TO-MANY (M:N):
    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
    │   Student    │    │ student_course│    │   Course     │
    │──────────────│    │──────────────│    │──────────────│
    │ id: 1        │───▶│ student_id: 1│◀───│ id: 1        │
    │ name: "Amit" │───▶│ course_id: 1 │◀───│ name: "Java" │
    │              │───▶│ student_id: 2│    │              │
    │ id: 2        │───▶│ course_id: 1 │    │ id: 2        │
    │ name: "Rahul"│    │ student_id: 1│    │ name: "SQL"  │
    └──────────────┘    │ course_id: 2 │    └──────────────┘
                        └──────────────┘
```

```java
// ═══════════════════════════════════════════════════════════════
// ONE-TO-ONE
// ═══════════════════════════════════════════════════════════════
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)  // Cascade operations
    @JoinColumn(name = "aadhaar_id")      // Foreign key column
    private Aadhaar aadhaar;
}

// ═══════════════════════════════════════════════════════════════
// ONE-TO-MANY
// ═══════════════════════════════════════════════════════════════
@Entity
public class Author {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "author")  // "author" field in Book
    private List<Book> books = new ArrayList<>();
}

// ═══════════════════════════════════════════════════════════════
// MANY-TO-ONE
// ═══════════════════════════════════════════════════════════════
@Entity
public class Book {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")  // Foreign key in Book table
    private Author author;
}

// ═══════════════════════════════════════════════════════════════
// MANY-TO-MANY
// ═══════════════════════════════════════════════════════════════
@Entity
public class Student {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
        name = "student_course",  // Join table name
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}
```

---

## 6. HIBERNATE CRUD OPERATIONS

```java
// ═══════════════════════════════════════════════════════════════
// CRUD with Hibernate (JPA Repository)
// ═══════════════════════════════════════════════════════════════

// 1. Repository Interface (Spring Data JPA)
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Built-in methods: save, findById, findAll, deleteById, etc.

    // Custom query methods
    List<User> findByName(String name);
    List<User> findByEmailContaining(String email);
    Optional<User> findByEmail(String email);
}

// 2. Service Layer
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // CREATE
    public User createUser(User user) {
        return userRepository.save(user);  // INSERT INTO users
    }

    // READ
    public User getUserById(Long id) {
        return userRepository.findById(id)  // SELECT * FROM users WHERE id = ?
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();  // SELECT * FROM users
    }

    // UPDATE
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());

        return userRepository.save(user);  // UPDATE users SET ... WHERE id = ?
    }

    // DELETE
    public void deleteUser(Long id) {
        userRepository.deleteById(id);  // DELETE FROM users WHERE id = ?
    }
}
```

### CRUD Visual:

```
    CRUD Operations:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  CREATE:  save()        → INSERT INTO users ...             │
    │  READ:    findById()    → SELECT * FROM users WHERE id = ?  │
    │  UPDATE:  save()        → UPDATE users SET ... WHERE id = ? │
    │  DELETE:  deleteById()  → DELETE FROM users WHERE id = ?    │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 7. HIBERNATE CACHING

```
    ┌──────────────────────────────────────────────────────────────┐
    │  CACHING = Data ko memory mein store karo (fast access)      │
    │                                                              │
    │  Real Life Analogy:                                          │
    │  Tum books library mein lete ho                              │
    │                                                              │
    │  BINA Cache: Har baar library jaana padega (slow!)           │
    │  WITH Cache: Books ghar pe rakho (fast!)                     │
    │                                                              │
    │  Hibernate Cache Levels:                                     │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │                                                      │    │
    │  │  1st Level Cache (Session) - Default ON              │    │
    │  │     - Same session mein data reuse                   │    │
    │  │     - Automatically managed                          │    │
    │  │                                                      │    │
    │  │  2nd Level Cache (SessionFactory) - Optional         │    │
    │  │     - Cross-session reuse                            │    │
    │  │     - Need to configure                              │    │
    │  │                                                      │    │
    │  │  Query Cache - For query results                     │    │
    │  │     - Cache query results (not entities)             │    │
    │  │                                                      │    │
    │  └──────────────────────────────────────────────────────┘    │
    └──────────────────────────────────────────────────────────────┘
```

### Cache Visual:

```
    Hibernate Cache Flow:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  Request → 1st Level Cache → 2nd Level Cache → Database     │
    │                                                              │
    │  Step 1: Check 1st Level Cache (Session)                     │
    │     │ Found? → Return (FAST!) ✅                             │
    │     │ Not Found ↓                                           │
    │                                                              │
    │  Step 2: Check 2nd Level Cache (SessionFactory)              │
    │     │ Found? → Return (FAST!) ✅                             │
    │     │ Not Found ↓                                           │
    │                                                              │
    │  Step 3: Query Database (SLOW) ⏳                            │
    │     │ Found? → Store in Cache → Return                      │
    │     │ Not Found → Return null                               │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

```java
// 1st Level Cache (Automatic)
User user1 = userRepository.findById(1L);
// Database hit 1 (SELECT * FROM users WHERE id = 1)

User user2 = userRepository.findById(1L);
// Database hit 0! (From cache - same session)

// 2nd Level Cache (Configure in application.properties)
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=
    org.hibernate.cache.jcache.JCacheRegionFactory
spring.jpa.properties.javax.cache.provider=
    org.ehcache.jsr107.EhcacheCachingProvider
```

---

## 8. HIBERNATE CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║           HIBERNATE/JPA CHEAT SHEET                              ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  ENTITY ANNOTATIONS:                                             ║
║  @Entity              → Class is a database table              ║
║  @Table(name = "x")   → Table name                             ║
║  @Id                  → Primary key                            ║
║  @GeneratedValue      → Auto-increment ID                      ║
║  @Column              → Column properties                      ║
║  @Transient            → Skip this field (not in DB)           ║
║                                                                  ║
║  RELATIONSHIPS:                                                  ║
║  @OneToOne            → 1:1 (User-Aadhaar)                     ║
║  @OneToMany           → 1:N (Author-Books)                     ║
║  @ManyToOne           → N:1 (Book-Author)                      ║
║  @ManyToMany          → M:N (Student-Course)                   ║
║                                                                  ║
║  CASCADE:                                                        ║
║  CascadeType.ALL      → Sab operations cascade                 ║
║  CascadeType.PERSIST  → Save karo                              ║
║  CascadeType.MERGE    → Update karo                            ║
║  CascadeType.REMOVE   → Delete karo                            ║
║                                                                  ║
║  FETCH TYPE:                                                     ║
║  FetchType.LAZY       → On-demand (faster, default for Many)   ║
║  FetchType.EAGER      → Immediately (default for One)          ║
║                                                                  ║
║  SPRING DATA JPA METHODS:                                        ║
║  save()               → Create/Update                          ║
║  findById()           → Read by ID                             ║
║  findAll()            → Read all                               ║
║  deleteById()         → Delete by ID                           ║
║  count()              → Count records                          ║
║  existsById()         → Check exists                           ║
║                                                                  ║
║  CUSTOM QUERIES:                                                 ║
║  @Query("SELECT u FROM User u WHERE u.name = :name")           ║
║  List<User> findByName(@Param("name") String name);            ║
║                                                                  ║
║  DERIVED QUERIES (Method naming):                                ║
║  findByName()         → WHERE name = ?                         ║
║  findByEmailContaining() → WHERE email LIKE %?%                 ║
║  findByAgeGreaterThan() → WHERE age > ?                         ║
║                                                                  ║
║  CACHING:                                                        ║
║  1st Level Cache → Session level (automatic)                    ║
║  2nd Level Cache → SessionFactory level (optional)              ║
║                                                                  ║
║  HIBERNATE vs JPA:                                               ║
║  JPA: Specification (interface)                                 ║
║  Hibernate: Implementation (actual code)                        ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 9. HIBERNATE/JPA INTERVIEW QUESTIONS (40+)

### ⭐ BASIC

**Q1: What is Hibernate?**
> ORM framework jo Java objects ko database tables se map karta hai. Automatically SQL generate karta hai.

**Q2: What is JPA?**
> Java Persistence API - ORM ke liye specification (rules). Hibernate is implementation.

**Q3: What is difference between JPA and Hibernate?**
```
JPA: Specification (interface/rules)
Hibernate: Implementation (actual code)
JPA is like blueprint, Hibernate is the building
```

**Q4: What is @Entity?**
> Marks class as database table. Hibernate will manage it.

**Q5: What is @Id?**
> Marks field as primary key (unique identifier).

**Q6: What is @GeneratedValue?**
> Auto-generates primary key value (auto-increment).

**Q7: What is @Column?**
> Specifies column properties (name, length, nullable, unique).

**Q8: What is @Transient?**
> Field NOT stored in database (skip this field).

**Q9: What is the difference between save() and persist()?**
```
save(): Returns generated ID, works detached
persist(): Returns void, must be in transaction
```

**Q10: What is the difference between get() and load()?**
```
get(): Returns null if not found, immediately loads
load(): Returns proxy, lazy loads (throws exception if not found)
```

---

### ⭐⭐ MIDDLE

**Q11: What is difference between Lazy and Eager loading?**
```
Lazy: Data loaded on-demand (faster, default for @OneToMany)
Eager: Data loaded immediately (default for @OneToOne)
```

**Q12: What is Cascade?**
> Parent operation automatically applies to child.
```java
@OneToMany(cascade = CascadeType.ALL)
```

**Q13: What is orphan removal?**
> Delete child when removed from parent's collection.
```java
@OneToMany(orphanRemoval = true)
```

**Q14: What is the difference between mappedBy and @JoinColumn?**
```
mappedBy: Inverse side (owns relationship, no FK)
@JoinColumn: Owning side (has FK column)
```

**Q15: What is 1st Level Cache?**
> Session-level cache. Same session mein same data reuse hota hai. Automatic.

**Q16: What is 2nd Level Cache?**
> SessionFactory-level cache. Cross-session reuse. Need to configure.

**Q17: What is the difference between merge() and update()?**
```
merge(): Works with detached objects (returns managed copy)
update(): Deprecated, doesn't work with detached objects
```

**Q18: What is JPQL?**
> JPA Query Language (similar to SQL but with entities, not tables).
```java
@Query("SELECT u FROM User u WHERE u.age > 18")
```

**Q19: What is difference between JPQL and SQL?**
```
JPQL: Entity-based (User u), object-oriented
SQL: Table-based (users), relational
```

**Q20: What is derived query?**
> Method name se query automatically generate hota hai.
```java
List<User> findByName(String name);  // Auto-generates query
```

---

### ⭐⭐⭐ ADVANCED

**Q21: What is N+1 problem?**
> Ek query se 1 result, phir N queries for associations (slow!).
```java
// Bad: N+1 queries
List<Author> authors = repository.findAll();  // 1 query
for (Author a : authors) {
    a.getBooks().size();  // N queries!
}

// Good: Join fetch
@Query("SELECT a FROM Author a JOIN FETCH a.books")
```

**Q22: What is the difference between flush and clear?**
```
flush: Sync session changes to database
clear: Clear session cache (detaches all objects)
```

**Q23: What is dirty checking?**
> Hibernate automatically detects changed objects and updates database.

**Q24: What is the difference between Session and EntityManager?**
```
Session: Hibernate-specific API
EntityManager: JPA standard API (preferred)
```

**Q25: What is transaction isolation?**
> Level of isolation between concurrent transactions (READ_COMMITTED, SERIALIZABLE, etc.).

**Q26: What is the difference between @Transactional and manual transaction?**
```
@Transactional: Declarative (Spring manages)
Manual: Programmatic (you manage)
Always use @Transactional (simpler)
```

**Q27: What is optimistic locking?**
> Version field se check karta hai if record changed. Throws OptimisticLockException.

```java
@Version
private Long version;
```

**Q28: What is pessimistic locking?**
> Database lock lagata hai (SELECT FOR UPDATE). Blocks other transactions.

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
```

**Q29: What is the difference between optimistic and pessimistic locking?**
```
Optimistic: No lock, check version before update
Pessimistic: Database lock, blocks others
Optimistic: Better for read-heavy
Pessimistic: Better for write-heavy
```

**Q30: What is the difference between criteria API and JPQL?**
```
Criteria API: Type-safe, programmatic query building
JPQL: String-based query
Criteria is safer (compile-time checks)
```

**Q31: What is the difference between bulk update and regular update?**
```
Bulk: Direct SQL, bypasses cache (faster for many records)
Regular: Object-based, uses cache (safer)
```

**Q32: What is the difference between detach and clear?**
```
detach: Remove single object from session
clear: Remove all objects from session
```

**Q33: What is the difference between merge and persist?**
```
merge: Works with detached objects (returns managed copy)
persist: Works with new objects (returns void)
```

**Q34: What is the difference between save and saveOrUpdate?**
```
save: Always INSERT (even if exists)
saveOrUpdate: INSERT if new, UPDATE if exists
```

**Q35: What is the difference between delete and remove?**
```
delete: Hibernate-specific (deprecated)
remove: JPA standard (preferred)
```

**Q36: What is the difference between EntityManager and Session in Spring?**
```
EntityManager: JPA standard (use this)
Session: Hibernate-specific (avoid)
```

**Q37: What is the difference between JpaRepository and CrudRepository?**
```
CrudRepository: Basic CRUD (save, findById, delete)
JpaRepository: CRUD + pagination + sorting + batch operations
```

**Q38: What is the difference between @Modifying and @Query?**
```
@Query: Read-only query (SELECT)
@Modifying: Write query (UPDATE, DELETE)
```

**Q39: What is the difference between projection and entity?**
```
Entity: Full object (all columns)
Projection: Partial object (selected columns only)
Projection is faster (less data)
```

**Q40: What is best practice for Hibernate?**
```
1. Use JpaRepository (Spring Data JPA)
2. Use constructor injection (not field)
3. Use @Transactional properly
4. Avoid N+1 problem (use JOIN FETCH)
5. Use pagination for large data
6. Use projections for read-only operations
7. Use 2nd level cache wisely
8. Use proper fetch types (LAZY for collections)
9. Use @Version for optimistic locking
10. Log SQL for debugging
```

---

*Last Updated: September 2026*
*Covers: Hibernate, JPA, ORM, Relationships, Caching, 40 Interview Questions*
