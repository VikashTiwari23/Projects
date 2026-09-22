# Spring Boot Testing - Zero se Seekho (Simple Hinglish)

---

## 1. TESTING KYA HAI?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Testing = Code ka CHECK karna ki sahi kaam kar raha hai      │
    │                                                              │
    │  Real Life Analogy:                                          │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  Exam ke baad CHECK karte ho:                        │    │
    │  │  ✓ Answers sahi hain?                                │    │
    │  │  ✓ Steps follow kiye?                                │    │
    │  │  ✓ Marks milenge?                                    │    │
    │  │                                                      │    │
    │  │  Testing mein:                                       │    │
    │  │  ✓ Code sahi kaam kar raha hai?                      │    │
    │  │  ✓ Expected output aa raha hai?                      │    │
    │  │  ✓ Bugs mil gaye?                                    │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  Why Test?                                                   │
    │  ✅ Bugs pakadna (before production)                         │
    │  ✅ Confidence (code sahi hai!)                              │
    │  ✅ Refactoring safe (code badlo, test pass ho)              │
    │  ✅ Documentation (test = how code works)                    │
    │  ✅ Quality (production mein kam errors)                     │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. TYPES OF TESTING

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Types of Testing:                                           │
    │                                                              │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │                                                      │    │
    │  │  1. UNIT TESTING                                     │    │
    │  │     - Ek method/class test karo                     │    │
    │  │     - Fast, isolated                                 │    │
    │  │     - Example: add(2,3) == 5 ?                       │    │
    │  │                                                      │    │
    │  │  2. INTEGRATION TESTING                              │    │
    │  │     - Multiple components test karo                 │    │
    │  │     - API + Database + Service                      │    │
    │  │     - Slower than unit test                          │    │
    │  │                                                      │    │
    │  │  3. END-TO-END (E2E) TESTING                         │    │
    │  │     - Poora application test karo                   │    │
    │  │     - User jaisa flow test karo                     │    │
    │  │     - Slowest                                        │    │
    │  │                                                      │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  Testing Pyramid:                                            │
    │           ╱╲                                                  │
    │          ╱E2╲     ← Kam tests (slow, expensive)             │
    │         ╱────╲                                               │
    │        ╱ Integ╲   ← Medium tests                            │
    │       ╱────────╲                                             │
    │      ╱   Unit   ╱  ← Zyada tests (fast, cheap)              │
    │     ╱────────────╲                                           │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 3. JUNIT 5 (Testing Framework)

```java
// ═══════════════════════════════════════════════════════════════
// BASIC UNIT TEST
// ═══════════════════════════════════════════════════════════════
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    Calculator calc = new Calculator();

    @Test  // Yeh method test hai
    void testAdd() {
        int result = calc.add(2, 3);
        assertEquals(5, result, "2+3 should be 5");  // Assertion
    }

    @Test
    void testSubtract() {
        int result = calc.subtract(5, 3);
        assertEquals(2, result);
    }

    @Test
    void testDivideByZero() {
        assertThrows(ArithmeticException.class, () -> {
            calc.divide(10, 0);
        });
    }
}

// ═══════════════════════════════════════════════════════════════
// ANNOTATIONS (JUnit 5)
// ═══════════════════════════════════════════════════════════════
/*
    @Test              → Method is a test
    @BeforeEach        → Run before EACH test
    @AfterEach         → Run after EACH test
    @BeforeAll         → Run ONCE before ALL tests (static)
    @AfterAll          → Run ONCE after ALL tests (static)
    @Disabled          → Skip this test
    @DisplayName       → Custom test name
    @RepeatedTest(3)   → Run test 3 times
    @ParameterizedTest → Run with different inputs
    @Nested            → Group tests in nested class
*/
```

### JUnit Annotations Visual:

```
    Test Lifecycle:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  @BeforeAll (static)                                         │
    │       │                                                      │
    │       ▼                                                      │
    │  ┌─ @BeforeEach → Test 1 → @AfterEach ─┐                    │
    │  │                                      │                   │
    │  ├─ @BeforeEach → Test 2 → @AfterEach ─┤                   │
    │  │                                      │                   │
    │  ├─ @BeforeEach → Test 3 → @AfterEach ─┤                   │
    │  │                                      │                   │
    │  └──────────────────────────────────────┘                   │
    │       │                                                      │
    │       ▼                                                      │
    │  @AfterAll (static)                                          │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 4. ASSERTIONS

```
╔══════════════════════════════════════════════════════════════════╗
║           JUNIT 5 ASSERTIONS                                     ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  BASIC:                                                          ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  assertEquals(expected, actual)     → Equality check       │  ║
║  │  assertNotEquals(expected, actual)  → Not equal            │  ║
║  │  assertTrue(condition)              → Condition is true    │  ║
║  │  assertFalse(condition)             → Condition is false   │  ║
║  │  assertNull(object)                 → Is null              │  ║
║  │  assertNotNull(object)              → Is not null          │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  ARRAY/COLLECTION:                                               ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  assertArrayEquals(expected, actual) → Arrays equal       │  ║
║  │  assertIterableEquals(expected, actual) → Collections eq  │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  EXCEPTION:                                                      ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  assertThrows(Exception.class, () -> { ... })             │  ║
║  │  assertDoesNotThrow(() -> { ... })                        │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  EXECUTION:                                                      ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  assertAll(                                                │  ║
║  │      () -> assertEquals(5, result),                        │  ║
║  │      () -> assertTrue(result > 0)                          │  ║
║  │  );  // Run multiple assertions                            │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  MESSAGE:                                                        ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  assertEquals(expected, actual, "Custom message")         │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

```java
// Examples
@Test
void testAssertions() {
    // Basic
    assertEquals(5, 2 + 3);
    assertNotEquals(5, 2 + 2);
    assertTrue(5 > 3);
    assertFalse(5 < 3);
    assertNull(null);
    assertNotNull("hello");

    // Array
    assertArrayEquals(new int[]{1, 2, 3}, new int[]{1, 2, 3});

    // Exception
    assertThrows(ArithmeticException.class, () -> 10 / 0);

    // Multiple assertions
    assertAll(
        () -> assertEquals(5, 2 + 3),
        () -> assertTrue(5 > 0),
        () -> assertNotNull("test")
    );

    // With message
    assertEquals(5, 2 + 3, "Addition failed!");
}
```

---

## 5. MOCKITO (Mocking Framework)

```
    ┌──────────────────────────────────────────────────────────────┐
    │  MOCKITO = Fake objects banane ka framework                  │
    │                                                              │
    │  Problem: Test mein real dependencies use mat karo!          │
    │                                                              │
    │  Real Life:                                                  │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  Real World:                                          │    │
    │  │  Test driving → Real car → Real road → Real petrol   │    │
    │  │  (Expensive, slow, risky!)                           │    │
    │  │                                                      │    │
    │  │  Mock:                                               │    │
    │  │  Test driving → Simulator → Fake road → No petrol    │    │
    │  │  (Cheap, fast, safe!) ✅                             │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  Mock = Fake object (pretends to be real)                    │
    │  Spy = Wraps real object (partial mock)                      │
    │  Stub = Define what mock should return                       │
    └──────────────────────────────────────────────────────────────┘
```

```java
// ═══════════════════════════════════════════════════════════════
// MOCKITO EXAMPLES
// ═══════════════════════════════════════════════════════════════
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock           // Mock object (fake)
    private UserRepository userRepository;

    @InjectMocks    // Inject mocks into this
    private UserService userService;

    @Test
    void testGetUser() {
        // Arrange: Set up mock behavior
        User mockUser = new User(1L, "Amit");
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(mockUser));

        // Act: Call method
        User result = userService.getById(1L);

        // Assert: Verify result
        assertEquals("Amit", result.getName());
        verify(userRepository).findById(1L);  // Verify called
    }

    @Test
    void testSaveUser() {
        User user = new User(null, "Rahul");
        when(userRepository.save(any(User.class)))
            .thenAnswer(inv -> {
                User u = inv.getArgument(0);
                u.setId(1L);
                return u;
            });

        User saved = userService.save(user);

        assertEquals(1L, saved.getId());
        verify(userRepository).save(user);
    }
}
```

### Mockito Visual:

```
    Mockito Flow:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  1. @Mock → Create fake object                              │
    │     ┌─────────────────────┐                                 │
    │     │ UserRepository (fake)│                                │
    │     └─────────────────────┘                                 │
    │                                                              │
    │  2. when().thenReturn() → Define behavior                  │
    │     when(repo.findById(1L)) → Returns mockUser              │
    │                                                              │
    │  3. Call method under test                                   │
    │     userService.getById(1L)                                 │
    │                                                              │
    │  4. verify() → Check if mock was called                    │
    │     verify(repo).findById(1L) → Was it called?              │
    │                                                              │
    │  AAA Pattern:                                                │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  Arrange → Set up mocks and data                     │    │
    │  │  Act     → Call the method                          │    │
    │  │  Assert  → Verify the result                        │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 6. SPRING BOOT TEST ANNOTATIONS

```
╔══════════════════════════════════════════════════════════════════╗
║           SPRING BOOT TEST ANNOTATIONS                           ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  TEST TYPES:                                                     ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  @SpringBootTest              → Full application context  │  ║
║  │  @WebMvcTest                  → Web layer only (controller)│  ║
║  │  @DataJpaTest                 → JPA layer only (repository)│  ║
║  │  @JsonTest                    → JSON serialization only   │  ║
║  │  @RestClientTest              → REST client only          │  ║
║  │  @JdbcTest                    → JDBC layer only           │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  MOCKING:                                                        ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  @MockBean                 → Mock a Spring bean           │  ║
║  │  @SpyBean                  → Spy on a Spring bean         │  ║
║  │  @Mock                     → Mockito mock (no Spring)     │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  SLICING:                                                        ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  @AutoConfigureMockMvc      → Auto-configure MockMvc      │  ║
║  │  @AutoConfigureWebMvc       → Auto-configure WebMvc       │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  LIFECYCLE:                                                      ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  @BeforeAll / @BeforeEach    → Setup before tests         │  ║
║  │  @AfterEach / @AfterAll      → Cleanup after tests        │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

```java
// ═══════════════════════════════════════════════════════════════
// SPRING BOOT TEST EXAMPLES
// ═══════════════════════════════════════════════════════════════

// 1. Full Application Test
@SpringBootTest
class MyAppTests {
    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertNotNull(context);
    }
}

// 2. Web Layer Test (Controller only)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testGetUser() throws Exception {
        when(userService.getById(1L))
            .thenReturn(new User(1L, "Amit"));

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Amit"));
    }
}

// 3. Repository Test (with H2 database)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByName() {
        User user = new User(null, "Amit");
        entityManager.persist(user);
        entityManager.flush();

        List<User> result = userRepository.findByName("Amit");
        assertEquals(1, result.size());
    }
}
```

---

## 7. MOCK MVC (Testing REST APIs)

```java
// ═══════════════════════════════════════════════════════════════
// MOCK MVC - REST API Testing
// ═══════════════════════════════════════════════════════════════
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Simulates HTTP requests

    @MockBean
    private UserService userService;

    @Test
    void testGetUser() throws Exception {
        // Arrange
        when(userService.getById(1L))
            .thenReturn(new User(1L, "Amit", "amit@email.com"));

        // Act & Assert
        mockMvc.perform(get("/api/users/1"))  // HTTP GET
            .andExpect(status().isOk())       // 200 OK
            .andExpect(jsonPath("$.name").value("Amit"))
            .andExpect(jsonPath("$.email").value("amit@email.com"));
    }

    @Test
    void testCreateUser() throws Exception {
        User user = new User(null, "Rahul", "rahul@email.com");
        when(userService.save(any(User.class)))
            .thenAnswer(inv -> {
                User u = inv.getArgument(0);
                u.setId(1L);
                return u;
            });

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Rahul\",\"email\":\"rahul@email.com\"}"))
            .andExpect(status().isCreated())  // 201 Created
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
            .andExpect(status().isNoContent());  // 204 No Content
    }
}
```

### MockMvc Visual:

```
    MockMvc Flow:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  Test: mockMvc.perform(get("/api/users/1"))                 │
    │                          │                                  │
    │                          ▼                                  │
    │                 ┌─────────────────┐                         │
    │                 │   MockMvc       │                         │
    │                 │ (simulates HTTP)│                         │
    │                 └────────┬────────┘                         │
    │                          │                                  │
    │                          ▼                                  │
    │                 ┌─────────────────┐                         │
    │                 │  Dispatcher     │                         │
    │                 │  Servlet        │                         │
    │                 └────────┬────────┘                         │
    │                          │                                  │
    │                          ▼                                  │
    │                 ┌─────────────────┐                         │
    │                 │  Controller     │                         │
    │                 │  (tested)       │                         │
    │                 └────────┬────────┘                         │
    │                          │                                  │
    │                          ▼                                  │
    │                 ┌─────────────────┐                         │
    │                 │  Mock Service   │                         │
    │                 │  (fake data)    │                         │
    │                 └────────┬────────┘                         │
    │                          │                                  │
    │                          ▼                                  │
    │                 ┌─────────────────┐                         │
    │                 │  Response       │                         │
    │                 │  (JSON)         │                         │
    │                 └─────────────────┘                         │
    │                                                              │
    │  Assertions:                                                 │
    │  .andExpect(status().isOk())                                │
    │  .andExpect(jsonPath("$.name").value("Amit"))               │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 8. TESTING CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║           SPRING BOOT TESTING CHEAT SHEET                        ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  TEST ANNOTATIONS:                                               ║
║  @SpringBootTest   → Full app (slow, use for integration)       ║
║  @WebMvcTest       → Controller only (fast)                     ║
║  @DataJpaTest      → Repository only (with H2)                  ║
║  @JsonTest         → JSON serialization only                    ║
║                                                                  ║
║  MOCKING:                                                        ║
║  @MockBean         → Mock Spring bean                           ║
║  @Mock             → Mockito mock (no Spring)                   ║
║  when().thenReturn() → Define mock behavior                     ║
║  verify()          → Check if mock was called                   ║
║                                                                  ║
║  JUNIT 5 ANNOTATIONS:                                            ║
║  @Test             → This is a test                             ║
║  @BeforeEach       → Before each test                           ║
║  @AfterEach        → After each test                            ║
║  @BeforeAll        → Before all tests (static)                  ║
║  @AfterAll         → After all tests (static)                   ║
║  @DisplayName      → Custom name                                ║
║  @Disabled         → Skip test                                  ║
║                                                                  ║
║  ASSERTIONS:                                                     ║
║  assertEquals(a, b)  → a equals b                               ║
║  assertTrue(cond)    → condition is true                         ║
║  assertNotNull(obj)  → obj is not null                           ║
║  assertThrows(Ex.class, () -> ...) → Throws exception            ║
║  assertAll(...)      → Multiple assertions                      ║
║                                                                  ║
║  MOCK MVC:                                                       ║
║  mockMvc.perform(get("/path")) → HTTP GET                       ║
║  .andExpect(status().isOk())   → Check status                   ║
║  .andExpect(jsonPath("$.x").value(y)) → Check JSON              ║
║                                                                  ║
║  BEST PRACTICES:                                                 ║
║  ✅ Use @WebMvcTest for controller tests (faster)               ║
║  ✅ Use @DataJpaTest for repository tests                       ║
║  ✅ Mock dependencies (don't use real ones)                      ║
║  ✅ Follow AAA pattern (Arrange, Act, Assert)                   ║
║  ✅ Use descriptive test names (@DisplayName)                   ║
║  ✅ Test edge cases (null, empty, boundary)                     ║
║  ✅ Keep tests independent (no dependencies)                    ║
║  ✅ Use H2 for database tests (in-memory)                       ║
║  ⚠️  Avoid @SpringBootTest when possible (slow)                ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 9. TESTING INTERVIEW QUESTIONS (30+)

### ⭐ BASIC

**Q1: What is unit testing?**
> Testing individual method/class in isolation. Fast and focused.

**Q2: What is JUnit?**
> Java testing framework. Provides annotations and assertions for writing tests.

**Q3: What is @Test annotation?**
> Marks a method as a test method in JUnit.

**Q4: What is assertion?**
> Statement that checks if expected result matches actual result.

**Q5: What is the difference between @BeforeAll and @BeforeEach?**
```
@BeforeAll: Runs once before ALL tests (static method)
@BeforeEach: Runs before EACH test
```

**Q6: What is Mockito?**
> Framework for creating mock objects (fakes) in tests.

**Q7: What is @MockBean?**
> Creates a mock Spring bean for testing (replaces real bean).

**Q8: What is the difference between @Mock and @MockBean?**
```
@Mock: Mockito mock (no Spring context)
@MockBean: Spring bean mock (in Spring context)
```

**Q9: What is @SpringBootTest?**
> Tests the full Spring application context (integration test).

**Q10: What is MockMvc?**
> Simulates HTTP requests for testing REST controllers without actual server.

---

### ⭐⭐ MIDDLE

**Q11: What is the difference between @SpringBootTest and @WebMvcTest?**
```
@SpringBootTest: Full application context (slow)
@WebMvcTest: Web layer only (fast, controller + web config)
```

**Q12: What is AAA pattern?**
```
Arrange: Set up test data and mocks
Act: Call the method being tested
Assert: Verify the result
```

**Q13: What is the difference between when() and verify()?**
```
when(): Define what mock should return
verify(): Check if mock method was called
```

**Q14: What is @DataJpaTest?**
> Tests JPA repositories with in-memory database (H2).

**Q15: What is H2 database?**
> In-memory database for testing. Fast, no setup needed.

**Q16: What is the difference between unit test and integration test?**
```
Unit test: Single component (fast, isolated)
Integration test: Multiple components (slower, real dependencies)
```

**Q17: What is @JsonTest?**
> Tests JSON serialization/deserialization only.

**Q18: What is the difference between assertEquals and assertNotEquals?**
```
assertEquals: Asserts two values are equal
assertNotEquals: Asserts two values are NOT equal
```

**Q19: What is assertThrows?**
> Asserts that a specific exception is thrown.

```java
assertThrows(ArithmeticException.class, () -> 10 / 0);
```

**Q20: What is @DisplayName?**
> Provides custom display name for test method (more readable).

---

### ⭐⭐⭐ ADVANCED

**Q21: What is test coverage?**
> Percentage of code covered by tests. Aim for 80%+ coverage.

**Q22: What is the difference between mock and spy?**
```
Mock: Completely fake (all methods return default)
Spy: Wraps real object (real methods + can be stubbed)
```

**Q23: What is @InjectMocks?**
> Injects mock dependencies into the class being tested.

**Q24: What is argument captor?**
> Captures arguments passed to mock for verification.

```java
ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
verify(repo).save(captor.capture());
assertEquals("Amit", captor.getValue().getName());
```

**Q25: What is the difference between verify() and verifyNoInteractions()?**
```
verify(): Check specific method was called
verifyNoInteractions(): Check NO methods were called
```

**Q26: What is parameterized test?**
> Run same test with different inputs.

```java
@ParameterizedTest
@ValueSource(ints = {1, 2, 3})
void testMultipleInputs(int number) { ... }
```

**Q27: What is the difference between @Nested and separate test classes?**
```
@Nested: Group tests in inner class (shared state)
Separate: Independent test classes (isolation)
```

**Q28: What is TestRestTemplate?**
> RestTemplate for testing REST APIs in Spring Boot tests.

**Q29: What is the difference between @WebMvcTest and @RestClientTest?**
```
@WebMvcTest: Tests controllers (server side)
@RestClientTest: Tests REST clients (client side)
```

**Q30: What is the best practice for testing?**
```
1. Write tests first (TDD)
2. Use @WebMvcTest for controllers (faster)
3. Mock external dependencies
4. Keep tests independent
5. Use descriptive names
6. Test edge cases
7. Aim for 80%+ coverage
8. Use H2 for database tests
9. Follow AAA pattern
10. Don't test framework code
```

---

*Last Updated: September 2026*
*Covers: JUnit 5, Mockito, Spring Boot Testing, MockMvc, 30 Interview Questions*
