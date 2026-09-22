# Spring REST API - Zero se Seekho (Simple Hinglish)

---

## 1. REST API KYA HAI?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  REST = Representational State Transfer                      │
    │  API = Application Programming Interface                     │
    │                                                              │
    │  Simple mein: Ek tareeka jisse do applications               │
    │  ek doosre se baat kar sakte hain                            │
    │                                                              │
    │  Real Life Analogy:                                          │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  TUM (Mobile App)  ←→  REST API  ←→  DATABASE       │    │
    │  │                                                      │    │
    │  │  Tum: "Mujhe users ki list chahiye"                  │    │
    │  │  API: "Thee hai, yeh lo list" → Returns JSON         │    │
    │  │                                                      │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  REST API = HTTP methods se data exchange karna              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. HTTP METHODS

```
    ┌──────────────────────────────────────────────────────────────┐
    │  HTTP Methods = Kya karna hai? (CRUD operations)            │
    │                                                              │
    │  ┌────────────┬────────────┬────────────────────────────┐    │
    │  │ Method     │ Meaning    │ Example                    │    │
    │  ├────────────┼────────────┼────────────────────────────┤    │
    │  │ GET        │ Read/Get   │ /api/users (list lao)      │    │
    │  │ POST       │ Create     │ /api/users (naya banao)    │    │
    │  │ PUT        │ Update     │ /api/users/1 (update karo) │    │
    │  │ PATCH      │ Partial    │ /api/users/1 (thoda update)│    │
    │  │ DELETE     │ Delete     │ /api/users/1 (delete karo) │    │
    │  └────────────┴────────────┴────────────────────────────┘    │
    │                                                              │
    │  Real Life:                                                  │
    │  GET    = Book library se lao (read)                         │
    │  POST   = Nayi book khareed ke lao (create)                  │
    │  PUT    = Purani book ko nayi se badlo (update)              │
    │  DELETE = Book ko wapas karo (delete)                        │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

### HTTP Methods Visual:

```
    CRUD Mapping:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  Operation    HTTP Method    Endpoint         Action         │
    │  ─────────    ───────────    ────────         ──────         │
    │  CREATE       POST           /api/users       Insert         │
    │  READ (all)   GET            /api/users       Select all     │
    │  READ (one)   GET            /api/users/1     Select by id   │
    │  UPDATE       PUT            /api/users/1     Update         │
    │  DELETE       DELETE         /api/users/1     Delete         │
    │                                                              │
    │  Example Flow:                                               │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  1. POST   /api/users     → Create new user        │    │
    │  │  2. GET    /api/users     → Get all users          │    │
    │  │  3. GET    /api/users/1   → Get user with id=1     │    │
    │  │  4. PUT    /api/users/1   → Update user id=1       │    │
    │  │  5. DELETE /api/users/1   → Delete user id=1       │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 3. HTTP STATUS CODES

```
╔══════════════════════════════════════════════════════════════════╗
║           HTTP STATUS CODES (Simple)                             ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  2xx - SUCCESS ✅                                                ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  200 OK              → Sab theek hai (success)            │  ║
║  │  201 Created         → Nayi cheez bani (POST success)     │  ║
║  │  204 No Content      → Success but no response body       │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  3xx - REDIRECT 🔀                                               ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  301 Moved Permanently → Permanently moved                │  ║
║  │  302 Found            → Temporarily moved                 │  ║
║  │  304 Not Modified     → Cache valid (no change)           │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  4xx - CLIENT ERROR ❌                                           ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  400 Bad Request      → Galat request (validation error)  │  ║
║  │  401 Unauthorized     → Login nahi kiya                   │  ║
║  │  403 Forbidden        → Login kiya but permission nahi    │  ║
║  │  404 Not Found        → Page/Resource nahi mila           │  ║
║  │  405 Method Not Allowed → Galat HTTP method               │  ║
║  │  409 Conflict         → Data conflict (duplicate)         │  ║
║  │  422 Unprocessable    → Validation failed                 │  ║
║  │  429 Too Many Requests → Rate limit exceeded              │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
║  5xx - SERVER ERROR ⚠️                                           ║
║  ┌────────────────────────────────────────────────────────────┐  ║
║  │  500 Internal Server Error → Server mein error            │  ║
║  │  502 Bad Gateway           → Invalid response             │  ║
║  │  503 Service Unavailable   → Server down/maintenance      │  ║
║  │  504 Gateway Timeout       → Server timeout               │  ║
║  └────────────────────────────────────────────────────────────┘  ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 4. SPRING REST CONTROLLER

```java
// ═══════════════════════════════════════════════════════════════
// REST CONTROLLER (Complete CRUD Example)
// ═══════════════════════════════════════════════════════════════
@RestController                    // = @Controller + @ResponseBody
@RequestMapping("/api/users")       // Base URL path
public class UserController {

    @Autowired
    private UserService userService;

    // CREATE - POST /api/users
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.create(user);
        return ResponseEntity
            .status(HttpStatus.CREATED)  // 201 Created
            .body(created);
    }

    // READ ALL - GET /api/users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);  // 200 OK
    }

    // READ ONE - GET /api/users/1
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(user);  // 200 OK
    }

    // UPDATE - PUT /api/users/1
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {
        User updated = userService.update(id, user);
        return ResponseEntity.ok(updated);  // 200 OK
    }

    // DELETE - DELETE /api/users/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
```

---

## 5. SPRING REST ANNOTATIONS

```
╔══════════════════════════════════════════════════════════════════╗
║           SPRING REST ANNOTATIONS                                ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  CONTROLLER:                                                     ║
║  @RestController           → REST controller (@Controller+@Resp)║
║  @RequestMapping("/path")  → Base URL path                      ║
║                                                                  ║
║  HTTP METHODS:                                                   ║
║  @GetMapping("/path")      → GET request                        ║
║  @PostMapping("/path")     → POST request                       ║
║  @PutMapping("/path")      → PUT request                        ║
║  @PatchMapping("/path")    → PATCH request                      ║
║  @DeleteMapping("/path")   → DELETE request                     ║
║                                                                  ║
║  PARAMETERS:                                                     ║
║  @RequestBody              → JSON body read karo                ║
║  @ResponseBody             → Response body mein bhejo           ║
║  @PathVariable("id")       → URL se value lo (/users/{id})      ║
║  @RequestParam("name")     → Query param lo (/users?name=Amit)  ║
║  @RequestHeader            → Header value lo                    ║
║  @CookieValue               → Cookie value lo                   ║
║  @ModelAttribute            → Form data lo                      ║
║                                                                  ║
║  RESPONSE:                                                       ║
║  ResponseEntity<T>          → Status + Headers + Body           ║
║  HttpStatus.CREATED         → 201                               ║
║  HttpStatus.OK              → 200                               ║
║  HttpStatus.NOT_FOUND       → 404                               ║
║  HttpStatus.BAD_REQUEST     → 400                               ║
║  ResponseEntity.ok()        → 200 with body                     ║
║  ResponseEntity.noContent() → 204 without body                  ║
║                                                                  ║
║  VALIDATION:                                                     ║
║  @Valid                    → Validate @RequestBody              ║
║  @NotNull                  → Not null check                     ║
║  @NotBlank                 → Not blank check                    ║
║  @NotEmpty                 → Not empty check                    ║
║  @Size(min=1, max=100)     → Size check                         ║
║  @Email                    → Email format check                 ║
║  @Min/@Max                 → Min/Max value check                ║
║  @Pattern                  → Regex pattern check                ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 6. REQUEST & RESPONSE

```java
// ═══════════════════════════════════════════════════════════════
// REQUEST EXAMPLES
// ═══════════════════════════════════════════════════════════════

// GET with Path Variable
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) { }

// GET with Request Param
@GetMapping("/search")
public List<User> search(
    @RequestParam String name,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
) { }

// GET with Request Header
@GetMapping("/check")
public String check(@RequestHeader("Authorization") String token) { }

// POST with Request Body
@PostMapping
public User create(@RequestBody User user) { }

// POST with Validation
@PostMapping
public User create(@Valid @RequestBody User user) { }

// ═══════════════════════════════════════════════════════════════
// RESPONSE EXAMPLES
// ═══════════════════════════════════════════════════════════════

// Simple response
@GetMapping("/hello")
public String hello() {
    return "Hello World";  // Returns as JSON string
}

// ResponseEntity with status
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = service.getById(id);
    return ResponseEntity.ok(user);  // 200 OK
}

// ResponseEntity with custom status
@PostMapping
public ResponseEntity<User> create(@RequestBody User user) {
    User created = service.create(user);
    return ResponseEntity
        .status(HttpStatus.CREATED)  // 201 Created
        .header("Location", "/api/users/" + created.getId())
        .body(created);
}

// Error response
@GetMapping("/{id}")
public ResponseEntity<?> getUser(@PathVariable Long id) {
    Optional<User> user = service.findById(id);
    if (user.isEmpty()) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "User not found"));
    }
    return ResponseEntity.ok(user.get());
}
```

### Request/Response Visual:

```
    REST API Request/Response:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  REQUEST (Client → Server):                                  │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  POST /api/users HTTP/1.1                           │    │
    │  │  Content-Type: application/json                      │    │
    │  │  Authorization: Bearer <token>                       │    │
    │  │                                                      │    │
    │  │  {                                                   │    │
    │  │    "name": "Amit",                                   │    │
    │  │    "email": "amit@email.com"                         │    │
    │  │  }                                                   │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                          │                                   │
    │                          ▼                                   │
    │  RESPONSE (Server → Client):                                 │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  HTTP/1.1 201 Created                                │    │
    │  │  Content-Type: application/json                      │    │
    │  │  Location: /api/users/1                              │    │
    │  │                                                      │    │
    │  │  {                                                   │    │
    │  │    "id": 1,                                          │    │
    │  │    "name": "Amit",                                   │    │
    │  │    "email": "amit@email.com"                         │    │
    │  │  }                                                   │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 7. EXCEPTION HANDLING IN REST API

```java
// ═══════════════════════════════════════════════════════════════
// GLOBAL EXCEPTION HANDLER
// ═══════════════════════════════════════════════════════════════
@RestControllerAdvice  // = @Controller + @ResponseBody for all controllers
public class GlobalExceptionHandler {

    // Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(
            ResourceNotFoundException ex) {
        Map<String, String> error = Map.of(
            "error", ex.getMessage(),
            "status", "404"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Validation error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Generic exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        Map<String, String> error = Map.of(
            "error", ex.getMessage(),
            "status", "500"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

---

## 8. SPRING REST CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║           SPRING REST API CHEAT SHEET                            ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  HTTP METHODS → CRUD:                                            ║
║  GET    → Read    (SELECT)                                       ║
║  POST   → Create  (INSERT)                                       ║
║  PUT    → Update  (UPDATE - full)                                ║
║  PATCH  → Update  (UPDATE - partial)                             ║
║  DELETE → Delete  (DELETE)                                       ║
║                                                                  ║
║  STATUS CODES:                                                   ║
║  200 OK            → Success                                     ║
║  201 Created       → Resource created                            ║
║  204 No Content    → Success, no body                            ║
║  400 Bad Request   → Client error (validation)                   ║
║  401 Unauthorized  → Not logged in                               ║
║  403 Forbidden     → No permission                               ║
║  404 Not Found     → Resource not found                          ║
║  500 Server Error  → Server error                                ║
║                                                                  ║
║  ANNOTATIONS:                                                    ║
║  @RestController     → REST controller                           ║
║  @RequestMapping     → Base path                                 ║
║  @GetMapping         → GET                                       ║
║  @PostMapping        → POST                                      ║
║  @PutMapping         → PUT                                       ║
║  @DeleteMapping      → DELETE                                    ║
║  @RequestBody        → Read JSON body                            ║
║  @PathVariable       → URL parameter (/users/{id})               ║
║  @RequestParam       → Query parameter (?name=Amit)              ║
║  ResponseEntity      → Status + Body response                    ║
║                                                                  ║
║  VALIDATION:                                                     ║
║  @Valid + @NotNull, @NotBlank, @Size, @Email, @Min, @Max        ║
║                                                                  ║
║  EXCEPTION HANDLING:                                             ║
║  @RestControllerAdvice → Global exception handler                ║
║  @ExceptionHandler     → Handle specific exception               ║
║                                                                  ║
║  BEST PRACTICES:                                                 ║
║  ✅ Use plural nouns (/users, /products)                         ║
║  ✅ Use HTTP methods correctly                                   ║
║  ✅ Return proper status codes                                   ║
║  ✅ Use DTOs (don't expose entities directly)                    ║
║  ✅ Validate input (@Valid)                                      ║
║  ✅ Handle exceptions globally                                   ║
║  ✅ Use versioning (/api/v1/users)                               ║
║  ✅ Support pagination                                           ║
║  ✅ Use HATEOAS (links in response)                              ║
║  ✅ Document with Swagger/OpenAPI                                ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 9. SPRING REST API INTERVIEW QUESTIONS (40+)

### ⭐ BASIC

**Q1: What is REST API?**
> HTTP methods se data exchange karne ka tareeka. Client-server communication.

**Q2: What is difference between REST and SOAP?**
```
REST: Lightweight, JSON, HTTP methods, stateless
SOAP: XML, WSDL, complex, stateful
REST is more popular now
```

**Q3: What is @RestController?**
> @Controller + @ResponseBody. Automatically returns JSON response.

**Q4: What is difference between @Controller and @RestController?**
```
@Controller: Returns view (HTML)
@RestController: Returns data (JSON/XML)
```

**Q5: What is @RequestBody?**
> Reads JSON from request body and converts to Java object.

**Q6: What is @PathVariable vs @RequestParam?**
```
@PathVariable: URL se value lo (/users/{id})
@RequestParam: Query string se value lo (/users?id=1)
```

**Q7: What is ResponseEntity?**
> Wraps response with HTTP status, headers, and body.

**Q8: What is the difference between GET and POST?**
```
GET: Read data, safe, idempotent, no body
POST: Create data, not safe, not idempotent, has body
```

**Q9: What is the difference between PUT and PATCH?**
```
PUT: Full update (send all fields)
PATCH: Partial update (send only changed fields)
```

**Q10: What is idempotent?**
> Multiple same requests give same result. GET, PUT, DELETE are idempotent. POST is not.

---

### ⭐⭐ MIDDLE

**Q11: What is the difference between @Valid and @Validated?**
```
@Valid: JSR-303 validation (standard)
@Validated: Spring validation (supports groups)
```

**Q12: What is DTO?**
> Data Transfer Object. Don't expose entities directly. Use separate class for API response.

**Q13: Why use DTO instead of Entity?**
```
Entity: Exposes database structure (security risk)
DTO: Controls what data to expose (safer)
Also decouples API from database schema
```

**Q14: What is the difference between 401 and 403?**
```
401: Not logged in (Unauthorized)
403: Logged in but no permission (Forbidden)
```

**Q15: What is @RestControllerAdvice?**
> Global exception handler for all controllers. Like @ControllerAdvice + @ResponseBody.

**Q16: What is HATEOAS?**
> Hypermedia links in response. Client can discover related resources.

**Q17: What is API versioning?**
> Different versions of API for backward compatibility.
```
/api/v1/users
/api/v2/users
```

**Q18: What is the difference between synchronous and asynchronous API?**
```
Synchronous: Wait for response (blocking)
Asynchronous: Return immediately, respond later (callback, polling)
```

**Q19: What is rate limiting?**
> Limit number of requests per time period (e.g., 100 requests/minute).

**Q20: What is CORS?**
> Cross-Origin Resource Sharing. Allow requests from different domains.

---

### ⭐⭐⭐ ADVANCED

**Q21: What is difference between REST and GraphQL?**
```
REST: Multiple endpoints, over-fetching/under-fetching
GraphQL: Single endpoint, client specifies what data needed
GraphQL is flexible but complex
```

**Q22: What is pagination in REST API?**
> Return data in pages (not all at once). Use Pageable in Spring Data JPA.

**Q23: What is the difference between cursor-based and offset-based pagination?**
```
Offset-based: ?page=0&size=10 (simple, but slow for large data)
Cursor-based: ?cursor=abc123 (fast, but complex)
```

**Q24: What is idempotency in REST?**
> Same request multiple times = same result. GET, PUT, DELETE are idempotent.

**Q25: What is the difference between 200 and 204?**
```
200 OK: Success with response body
204 No Content: Success without response body
```

**Q26: What is content negotiation?**
> Client specifies response format (JSON, XML) via Accept header.

```
Accept: application/json
Accept: application/xml
```

**Q27: What is the difference between @JsonInclude and @JsonIgnore?**
```
@JsonInclude: Include/exclude fields based on condition
@JsonIgnore: Never include this field in JSON
```

**Q28: What is @JsonProperty?**
> Custom JSON field name (when Java name differs from JSON name).

```java
@JsonProperty("user_name")
private String userName;
```

**Q29: What is the difference between @RequestBody and @ModelAttribute?**
```
@RequestBody: JSON/XML body (REST API)
@ModelAttribute: Form data (traditional web)
```

**Q30: What is async in REST API?**
> Return immediately, process in background. Return 202 Accepted.

**Q31: What is the difference between 409 and 422?**
```
409 Conflict: Data conflict (duplicate resource)
422 Unprocessable: Validation failed (syntactically correct but semantically wrong)
```

**Q32: What is the difference between authentication and authorization in REST?**
```
Authentication: Who are you? (login, JWT)
Authorization: What can you do? (roles, permissions)
```

**Q33: What is JWT in REST API?**
> JSON Web Token for stateless authentication. Sent in Authorization header.

**Q34: What is the difference between session-based and token-based auth?**
```
Session-based: Server stores session (stateful)
Token-based: Client stores token (stateless, scalable)
JWT is token-based
```

**Q35: What is the difference between OAuth1 and OAuth2?**
```
OAuth1: Complex, signature-based
OAuth2: Simpler, token-based (more popular)
```

**Q36: What is OpenID Connect?**
> Identity layer on top of OAuth2. Used for authentication (Login with Google).

**Q37: What is the difference between REST and RPC?**
```
REST: Resource-based (GET, POST, PUT, DELETE)
RPC: Action-based (getUser, createUser)
REST is more standardized
```

**Q38: What is the difference between REST and gRPC?**
```
REST: JSON, HTTP, text-based, slower
gRPC: Protocol Buffers, HTTP/2, binary, faster
gRPC better for microservices communication
```

**Q39: What is the difference between REST API and Web Service?**
```
REST API: Can use any format (JSON, XML), lightweight
Web Service: Usually SOAP (XML), standards-based
REST API is a type of web service (loosely)
```

**Q40: What is best practice for REST API?**
```
1. Use plural nouns (/users, /products)
2. Use HTTP methods correctly
3. Return proper status codes
4. Use versioning (/api/v1/)
5. Use DTOs (don't expose entities)
6. Validate input
7. Handle exceptions globally
8. Support pagination
9. Use HTTPS
10. Document with Swagger
11. Make it stateless
12. Use HATEOAS when possible
13. Rate limiting
14. CORS configuration
```

---

*Last Updated: September 2026*
*Covers: REST API, HTTP Methods, Status Codes, Controller, 40 Interview Questions*
