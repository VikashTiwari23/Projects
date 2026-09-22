# Spring Data JPA - Zero se Seekho (Simple Hinglish)

---

## 1. SPRING DATA JPA KYA HAI?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Spring Data JPA = JPA ko EASY banane wala module            │
    │                                                              │
    │  Real Life Analogy:                                          │
    │                                                              │
    │  BINA Spring Data JPA:                                       │
    │  ✗ Har CRUD operation ka code likho                          │
    │  ✗ Har query ka method implement karo                        │
    │  ✗ Bohot SARA boilerplate code (same thing bar bar)          │
    │                                                              │
    │  WITH Spring Data JPA:                                       │
    │  ✓ Interface extend karo                                     │
    │  ✓ Method ka NAAM likho → Query khud ban jayegi!             │
    │  ✓ 90% code AUTO-GENERATE hota hai                           │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. SPRING DATA JPA vs HIBERNATE

```
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  Spring Data JPA (Tum likhte ho):                            │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  public interface UserRepository                     │    │
    │  │      extends JpaRepository<User, Long> {             │    │
    │  │                                                      │    │
    │  │      List<User> findByName(String name);  // Done!   │    │
    │  │  }                                                   │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                          │                                   │
    │                          ▼                                   │
    │  Hibernate (Automatically generated):                        │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  @Override                                           │    │
    │  │  public List<User> findByName(String name) {         │    │
    │  │      // Hibernate generates SQL automatically!       │    │
    │  │      return em.createQuery(                           │    │
    │  │          "SELECT u FROM User u WHERE u.name = ?1")   │    │
    │  │          .setParameter(1, name)                       │    │
    │  │          .getResultList();                           │    │
    │  │  }                                                   │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  TUM sirf NAAM likho, Spring Data JPA khud QUERY banayega!  │
    └──────────────────────────────────────────────────────────────┘
```

---

## 3. REPOSITORY INTERFACE

```java
// ═══════════════════════════════════════════════════════════════
// STEP 1: Repository Interface banao (Bas interface extend karo!)
// ═══════════════════════════════════════════════════════════════
@Repository  // Spring automatically detect karega
public interface UserRepository extends JpaRepository<User, Long> {
    //                ↑           ↑
    //           Entity class   Primary key type

    // Bas method ka NAAM likho → Query automatically ban jayegi!
    List<User> findByName(String name);           // WHERE name = ?
    List<User> findByAgeGreaterThan(int age);     // WHERE age > ?
    Optional<User> findByEmail(String email);     // WHERE email = ?
    List<User> findByNameContaining(String name); // WHERE name LIKE %?%
    long countByAge(int age);                     // COUNT WHERE age = ?
    void deleteByEmail(String email);             // DELETE WHERE email = ?
}
```

### Repository Visual:

```
    Spring Data JPA Repository:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  TUM (Interface):                                            │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  interface UserRepository                            │    │
    │  │      extends JpaRepository<User, Long> {            │    │
    │  │                                                      │    │
    │  │      List<User> findByName(String name);  ← Tum     │    │
    │  │  }                                                   │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                          │                                   │
    │                          ▼                                   │
    │  Spring Data JPA (Auto-generates):                           │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  class UserRepositoryImpl implements UserRepository │    │
    │  │                                                      │    │
    │  │      // 20+ methods auto-generated:                  │    │
    │  │      save(), findById(), findAll(), deleteById()...  │    │
    │  │                                                      │    │
    │  │      // Tumhara custom method:                       │    │
    │  │      findByName() → SQL generated automatically!     │    │
    │  │  }                                                   │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  Result: 90% code AUTO! 🚀                                   │
    └──────────────────────────────────────────────────────────────┘
```

---

## 4. BUILT-IN METHODS (JpaRepository)

```
╔══════════════════════════════════════════════════════════════════╗
║        SPRING DATA JPA - BUILT-IN METHODS                        ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  CRUD OPERATIONS:                                                ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  save(entity)           → Create or Update                │  ║
║  │  saveAll(entities)      → Batch save                      │  ║
║  │  findById(id)           → Read by ID                      │  ║
║  │  findAll()              → Read all                        │  ║
║  │  findAllById(ids)       → Read multiple by IDs            │  ║
║  │  existsById(id)         → Check exists                    │  ║
║  │  count()                → Count records                   │  ║
║  │  deleteById(id)         → Delete by ID                    │  ║
║  │  delete(entity)         → Delete entity                   │  ║
║  │  deleteAll()            → Delete all                      │  ║
║  │  deleteAll(entities)    → Batch delete                    │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  PAGINATION & SORTING:                                           ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  findAll(Pageable)      → Paginated results               │  ║
║  │  findAll(Sort)          → Sorted results                  │  ║
║  │  findAll(Sort, Pageable) → Both                          │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  BATCH OPERATIONS:                                               ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  saveAll()              → Save multiple                   │  ║
║  │  deleteAll()            → Delete multiple                 │  ║
║  │  flush()                → Force flush to DB               │  ║
║  │  saveAndFlush()         → Save + Flush                    │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

```java
// ═══════════════════════════════════════════════════════════════
// EXAMPLE: Using built-in methods
// ═══════════════════════════════════════════════════════════════
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // CREATE
    public User create(User user) {
        return userRepository.save(user);  // INSERT
    }

    // READ
    public User getById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found"));
    }

    public List<User> getAll() {
        return userRepository.findAll();  // SELECT * FROM users
    }

    // UPDATE
    public User update(Long id, User details) {
        User user = getById(id);
        user.setName(details.getName());
        user.setEmail(details.getEmail());
        return userRepository.save(user);  // UPDATE
    }

    // DELETE
    public void delete(Long id) {
        userRepository.deleteById(id);  // DELETE
    }

    // COUNT
    public long count() {
        return userRepository.count();  // SELECT COUNT(*)
    }

    // EXISTS
    public boolean exists(Long id) {
        return userRepository.existsById(id);  // SELECT EXISTS
    }
}
```

---

## 5. DERIVED QUERIES (Method Naming)

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Derived Query = Method ka NAAM se QUERY ban jati hai       │
    │                                                              │
    │  Formula:                                                    │
    │  find[By]Property[And/Or][Property]...                       │
    │                                                              │
    │  Examples:                                                   │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  findByName(String name)                             │    │
    │  │  → SELECT * FROM users WHERE name = ?                │    │
    │  │                                                      │    │
    │  │  findByAgeGreaterThan(int age)                       │    │
    │  │  → SELECT * FROM users WHERE age > ?                 │    │
    │  │                                                      │    │
    │  │  findByNameAndAge(String name, int age)              │    │
    │  │  → SELECT * FROM users WHERE name = ? AND age = ?    │    │
    │  │                                                      │    │
    │  │  findByEmailContaining(String email)                 │    │
    │  │  → SELECT * FROM users WHERE email LIKE %?%          │    │
    │  │                                                      │    │
    │  │  countByAge(int age)                                 │    │
    │  │  → SELECT COUNT(*) FROM users WHERE age = ?          │    │
    │  │                                                      │    │
    │  │  deleteByEmail(String email)                         │    │
    │  │  → DELETE FROM users WHERE email = ?                 │    │
    │  │                                                      │    │
    │  │  existsByEmail(String email)                         │    │
    │  │  → SELECT EXISTS(SELECT * FROM users WHERE email = ?)│    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

### Derived Query Keywords:

```
╔══════════════════════════════════════════════════════════════════╗
║           DERIVED QUERY KEYWORDS                                 ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  FINDING METHODS:                                                ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  find      → Return results                               │  ║
║  │  read      → Same as find                                 │  ║
║  │  get       → Same as find                                 │  ║
║  │  count     → Return count                                 │  ║
║  │  exists    → Return boolean                               │  ║
║  │  delete    → Delete records                               │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  CONDITIONS:                                                     ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  By              → WHERE                                  │  ║
║  │  And             → AND                                    │  ║
║  │  Or              → OR                                     │  ║
║  │  Not             → NOT                                    │  ║
║  │  Between         → BETWEEN                                │  ║
║  │  LessThan        → <                                      │  ║
║  │  GreaterThan     → >                                      │  ║
║  │  LessThanEqual   → <=                                     │  ║
║  │  GreaterThanEqual→ >=                                     │  ║
║  │  IsNull          → IS NULL                                │  ║
║  │  IsNotNull       → IS NOT NULL                            │  ║
║  │  In              → IN                                     │  ║
║  │  Like            → LIKE                                   │  ║
║  │  StartingWith    → LIKE 'x%'                              │  ║
║  │  EndingWith      → LIKE '%x'                              │  ║
║  │  Containing      → LIKE '%x%'                             │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  ORDERING:                                                       ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  Ascending       → ORDER BY x ASC                         │  ║
║  │  Descending      → ORDER BY x DESC                        │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  EXAMPLES:                                                       ║
║  findByAgeBetween(int min, int max)                              ║
║  findByStatusIn(List<String> statuses)                           ║
║  findByNameContainingIgnoreCase(String name)                    ║
║  findByCreatedAtAfter(LocalDateTime date)                        ║
║  findTop10ByOrderByCreatedAtDesc()  // Top 10 newest            ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

```java
// ═══════════════════════════════════════════════════════════════
// DERIVED QUERIES EXAMPLE
// ═══════════════════════════════════════════════════════════════
public interface UserRepository extends JpaRepository<User, Long> {

    // Simple
    List<User> findByName(String name);
    Optional<User> findByEmail(String email);
    List<User> findByAgeGreaterThan(int age);

    // AND
    List<User> findByNameAndAge(String name, int age);

    // OR
    List<User> findByNameOrEmail(String name, String email);

    // LIKE
    List<User> findByNameContaining(String name);  // LIKE %name%
    List<User> findByNameStartingWith(String prefix);  // LIKE prefix%
    List<User> findByNameEndingWith(String suffix);  // LIKE %suffix

    // Case-insensitive
    List<User> findByNameContainingIgnoreCase(String name);

    // IN
    List<User> findByAgeIn(List<Integer> ages);

    // BETWEEN
    List<User> findByAgeBetween(int min, int max);

    // NULL check
    List<User> findByEmailIsNull();
    List<User> findByEmailIsNotNull();

    // Ordering
    List<User> findByNameOrderByAgeAsc(String name);
    List<User> findByNameOrderByAgeDesc(String name);

    // Top/Limit
    List<User> findTop5ByOrderByAgeDesc();  // Top 5 oldest

    // Count
    long countByAge(int age);

    // Exists
    boolean existsByEmail(String email);

    // Delete
    void deleteByEmail(String email);
}
```

---

## 6. CUSTOM QUERIES (@Query)

```java
// ═══════════════════════════════════════════════════════════════
// CUSTOM QUERIES (Jab derived query kaam na kare)
// ═══════════════════════════════════════════════════════════════
public interface UserRepository extends JpaRepository<User, Long> {

    // JPQL Query (Entity-based)
    @Query("SELECT u FROM User u WHERE u.age > :age")
    List<User> findUsersOlderThan(@Param("age") int age);

    // SQL Query (Table-based)
    @Query(value = "SELECT * FROM users WHERE age > :age",
           nativeQuery = true)
    List<User> findUsersOlderThanNative(@Param("age") int age);

    // Multiple conditions
    @Query("SELECT u FROM User u WHERE u.name = :name AND u.age = :age")
    List<User> findByNameAndAge(@Param("name") String name,
                                 @Param("age") int age);

    // UPDATE query
    @Modifying
    @Query("UPDATE User u SET u.age = :age WHERE u.id = :id")
    int updateAge(@Param("id") Long id, @Param("age") int age);

    // DELETE query
    @Modifying
    @Query("DELETE FROM User u WHERE u.age < :age")
    int deleteUsersYoungerThan(@Param("age") int age);

    // JOIN query
    @Query("SELECT u FROM User u JOIN u.orders o WHERE o.total > :amount")
    List<User> findUsersWithOrdersGreaterThan(@Param("amount") double amount);
}
```

### JPQL vs SQL:

```
    ┌──────────────────────────────────────────────────────────────┐
    │  JPQL (JPA Query Language):                                  │
    │  ✅ Entity-based (User u)                                    │
    │  ✅ Object-oriented                                          │
    │  ✅ Database independent                                      │
    │  ✅ Uses field names (u.name)                                │
    │                                                              │
    │  SELECT u FROM User u WHERE u.age > 18                       │
    │                                                              │
    │  SQL (Structured Query Language):                            │
    │  ✅ Table-based (users)                                      │
    │  ✅ Relational                                               │
    │  ✅ Database specific                                        │
    │  ✅ Uses column names (name)                                 │
    │                                                              │
    │  SELECT * FROM users WHERE age > 18                          │
    │                                                              │
    │  Prefer JPQL (portable)! Use SQL only when needed.          │
    └──────────────────────────────────────────────────────────────┘
```

---

## 7. PAGINATION & SORTING

```java
// ═══════════════════════════════════════════════════════════════
// PAGINATION (Data ko pages mein baanto)
// ═══════════════════════════════════════════════════════════════
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
}

// Usage
@Service
public class UserService {
    public Page<User> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
}

// ═══════════════════════════════════════════════════════════════
// SORTING
// ═══════════════════════════════════════════════════════════════
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll(Sort sort);
}

// Usage
Sort sort = Sort.by("name").ascending();  // A-Z
Sort sort = Sort.by("age").descending();   // 9-0
List<User> users = userRepository.findAll(sort);
```

### Pagination Visual:

```
    Pagination:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  Total Records: 100                                          │
    │  Page Size: 10                                               │
    │  Total Pages: 10                                             │
    │                                                              │
    │  Page 0: Records 0-9     (first 10)                         │
    │  Page 1: Records 10-19   (next 10)                          │
    │  Page 2: Records 20-29   (next 10)                          │
    │  ...                                                         │
    │  Page 9: Records 90-99   (last 10)                          │
    │                                                              │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  Pageable = PageRequest.of(page, size)               │    │
    │  │                                                      │    │
    │  │  Page 0, Size 10 → SELECT * FROM users LIMIT 10      │    │
    │  │                       OFFSET 0                       │    │
    │  │                                                      │    │
    │  │  Page 1, Size 10 → SELECT * FROM users LIMIT 10      │    │
    │  │                       OFFSET 10                      │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  Page<User> result:                                          │
    │  - getContent()    → List of records                         │
    │  - getTotalPages() → Total pages count                       │
    │  - getTotalElements() → Total records                        │
    │  - hasNext()       → Next page exists?                       │
    │  - hasPrevious()   → Previous page exists?                   │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Pagination + Sorting together
Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
Page<User> users = userRepository.findAll(pageable);

// Get results
List<User> userList = users.getContent();  // Actual data
int totalPages = users.getTotalPages();     // Total pages
long totalRecords = users.getTotalElements(); // Total records
boolean hasNext = users.hasNext();         // Next page?
```

---

## 8. SPRING DATA JPA CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║           SPRING DATA JPA CHEAT SHEET                            ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  REPOSITORY INTERFACES:                                          ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  CrudRepository<T, ID>      → Basic CRUD                  │  ║
║  │  PagingAndSortingRepository → Pagination + Sorting        │  ║
║  │  JpaRepository<T, ID>       → ALL (use this!)             │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  BUILT-IN METHODS:                                               ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  save(), saveAll()           → Create/Update              │  ║
║  │  findById(), findAll()       → Read                       │  ║
║  │  deleteById(), delete()      → Delete                     │  ║
║  │  count(), existsById()       → Count/Exists               │  ║
║  │  findAll(Pageable)           → Paginated                  │  ║
║  │  findAll(Sort)               → Sorted                    │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  DERIVED QUERIES (Method naming):                                ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  findByName(String)          → WHERE name = ?             │  ║
║  │  findByAgeGreaterThan(int)   → WHERE age > ?              │  ║
║  │  findByNameAndAge(String, int) → WHERE name = ? AND age =?│  ║
║  │  findByEmailContaining(String) → WHERE email LIKE %?%     │  ║
║  │  countByAge(int)             → COUNT WHERE age = ?        │  ║
║  │  deleteByEmail(String)       → DELETE WHERE email = ?     │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  CUSTOM QUERIES (@Query):                                        ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  @Query("SELECT u FROM User u WHERE u.age > :age")       │  ║
║  │  @Modifying → For UPDATE/DELETE                           │  ║
║  │  nativeQuery = true → Use SQL instead of JPQL            │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  PAGINATION:                                                     ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  Pageable pageable = PageRequest.of(page, size);          │  ║
║  │  Page<User> page = repo.findAll(pageable);                │  ║
║  │  page.getContent()     → Data                             │  ║
║  │  page.getTotalPages()  → Total pages                      │  ║
║  │  page.hasNext()        → Next page?                       │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  SORTING:                                                        ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  Sort sort = Sort.by("name").ascending();                 │  ║
║  │  Sort sort = Sort.by("age").descending();                 │  ║
║  │  repo.findAll(sort);                                      │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  TIPS:                                                           ║
║  ✅ Use JpaRepository (not CrudRepository)                      ║
║  ✅ Use derived queries for simple cases                        ║
║  ✅ Use @Query for complex queries                              ║
║  ✅ Use pagination for large data                              ║
║  ✅ Use Optional for single result                              ║
║  ✅ Use List for multiple results                               ║
║  ✅ Use @Param for named parameters                             ║
║  ⚠️  Avoid N+1 problem (use JOIN FETCH)                         ║
║  ⚠️  Don't use native query unless needed                       ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 9. SPRING DATA JPA INTERVIEW QUESTIONS (40+)

### ⭐ BASIC

**Q1: What is Spring Data JPA?**
> Module jo JPA ko easy banata hai. Repository interface extend karo, methods auto-generate ho jate hain.

**Q2: What is the difference between JPA and Spring Data JPA?**
```
JPA: Specification (interface)
Spring Data JPA: Simplifies JPA (auto-generates repository implementations)
```

**Q3: What is the difference between CrudRepository and JpaRepository?**
```
CrudRepository: Basic CRUD (save, findById, delete)
JpaRepository: CRUD + pagination + sorting + batch operations
Always use JpaRepository!
```

**Q4: What is a derived query?**
> Method name se automatically query generate hoti hai.
```java
List<User> findByName(String name);  // WHERE name = ?
```

**Q5: What is @Query annotation?**
> Custom query likhne ke liye (JPQL or SQL).

**Q6: What is the difference between JPQL and SQL?**
```
JPQL: Entity-based (User u), database independent
SQL: Table-based (users), database specific
```

**Q7: What is @Param?**
> Named parameter provide karta hai in @Query.

**Q8: What is @Modifying?**
> UPDATE/DELETE queries ke liye use hota hai (read-only nahi).

**Q9: What is Pageable?**
> Pagination information (page number, size, sort) provide karta hai.

**Q10: What is Page?**
> Paginated result container. Has content, total pages, total elements, etc.

---

### ⭐⭐ MIDDLE

**Q11: What is the difference between findAll() and findAll(Pageable)?**
```
findAll(): Returns ALL records (no pagination)
findAll(Pageable): Returns specific page (paginated)
```

**Q12: How to implement pagination?**
```java
Pageable pageable = PageRequest.of(0, 10);  // Page 0, size 10
Page<User> page = userRepository.findAll(pageable);
```

**Q13: How to implement sorting?**
```java
Sort sort = Sort.by("name").ascending();  // A-Z
List<User> users = userRepository.findAll(sort);
```

**Q14: What is the difference between Sort and Order?**
```
Sort: Contains multiple Order objects
Order: Single sort criteria (property + direction)
```

**Q15: What is derived query keyword?**
> Method name mein use hone wale keywords (find, By, And, Or, GreaterThan, etc.).

**Q16: What is the difference between find and read?**
> Same! Both return results. find is more common.

**Q17: What is the difference between count and exists?**
```
count: Returns number of records
exists: Returns boolean (true/false)
```

**Q18: What is the difference between delete and deleteAll?**
```
delete: Delete single record
deleteAll: Delete all records
```

**Q19: What is the difference between save and saveAndFlush?**
```
save: Save (may not immediately flush to DB)
saveAndFlush: Save + immediately flush to DB
```

**Q20: What is flush in Spring Data JPA?**
> Forces pending changes to be written to database immediately.

---

### ⭐⭐⭐ ADVANCED

**Q21: What is the difference between @Query and derived query?**
```
@Query: Custom query (manual)
Derived query: Auto-generated from method name
Use derived for simple, @Query for complex
```

**Q22: What is native query?**
> SQL query (not JPQL). Use when JPQL can't express logic.
```java
@Query(value = "SELECT * FROM users", nativeQuery = true)
```

**Q23: What is the difference between JPQL and native query?**
```
JPQL: Entity-based, portable, preferred
Native query: SQL, database-specific, use only when needed
```

**Q24: What is projection in Spring Data JPA?**
> Select specific columns only (not full entity). Faster!

```java
// Interface projection
public interface UserNameProjection {
    String getName();
    String getEmail();
}

List<UserNameProjection> findByAge(int age);
```

**Q25: What is the difference between entity and projection?**
```
Entity: Full object (all columns)
Projection: Partial object (selected columns)
Projection is faster (less data)
```

**Q26: What is specification in Spring Data JPA?**
> Dynamic query building using Criteria API. Good for complex WHERE clauses.

```java
Specification<User> spec = (root, query, cb) ->
    cb.and(
        cb.equal(root.get("name"), "Amit"),
        cb.greaterThan(root.get("age"), 18)
    );
```

**Q27: What is the difference between specification and @Query?**
```
@Query: Static query (fixed)
Specification: Dynamic query (built at runtime)
Use specification for dynamic filters
```

**Q28: What is auditing in Spring Data JPA?**
> Auto-track created/updated timestamps and users.

```java
@CreatedDate
private LocalDateTime createdAt;

@LastModifiedDate
private LocalDateTime updatedAt;
```

**Q29: What is the difference between @CreatedDate and @CreateDate?**
```
@CreatedDate: JPA annotation (createdAt)
@CreateDate: Hibernate-specific (deprecated)
Use @CreatedDate (JPA standard)
```

**Q30: What is the difference between @Version and optimistic locking?**
```
@Version: Version field for optimistic locking
Optimistic locking: Check version before update
If version changed → OptimisticLockException
```

**Q31: What is the difference between Page and Slice?**
```
Page: Contains total count (extra COUNT query)
Slice: No total count (faster)
Use Slice when you don't need total pages
```

**Q32: What is the difference between Pageable and PageRequest?**
```
Pageable: Interface (pagination abstraction)
PageRequest: Implementation (actual pagination)
PageRequest.of() returns Pageable
```

**Q33: What is the difference between findAll(Sort) and findAll(Pageable)?**
```
findAll(Sort): Sorted results (all records)
findAll(Pageable): Paginated results (specific page)
```

**Q34: What is the difference between @Query and @Modifying?**
```
@Query: Read-only (SELECT)
@Modifying: Write (UPDATE, DELETE)
Must use @Modifying for UPDATE/DELETE queries
```

**Q35: What is the difference between JPQL and Criteria API?**
```
JPQL: String-based (simpler)
Criteria API: Type-safe (programmatic)
Criteria is safer (compile-time checks)
```

**Q36: What is the difference between single result and list result?**
```
Optional<User> → Single result (findByEmail)
List<User> → Multiple results (findByName)
```

**Q37: What is the difference between Optional and null?**
```
Optional: Handles null safely (no NullPointerException)
null: Can cause NullPointerException
Always use Optional for single results!
```

**Q38: What is the difference between @Transactional and manual transaction?**
```
@Transactional: Declarative (Spring manages)
Manual: Programmatic (you manage)
Always use @Transactional (simpler)
```

**Q39: What is the difference between @Transactional(readOnly = true) and default?**
```
readOnly = true: Optimization for read queries
default: Read-write (for INSERT, UPDATE, DELETE)
Use readOnly = true for SELECT queries
```

**Q40: What is best practice for Spring Data JPA?**
```
1. Use JpaRepository (not CrudRepository)
2. Use derived queries for simple cases
3. Use @Query for complex queries
4. Use pagination for large data
5. Use Optional for single results
6. Use projections for read-only operations
7. Use @Transactional properly
8. Avoid N+1 problem (use JOIN FETCH)
9. Use readOnly = true for SELECT
10. Use specifications for dynamic queries
```

---

*Last Updated: September 2026*
*Covers: Spring Data JPA, Repository, Derived Queries, Pagination, 40 Interview Questions*
