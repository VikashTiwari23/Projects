# Spring Security - Zero se Seekho (Simple Hinglish)

---

## 1. SPRING SECURITY KYA HAI?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Spring Security = Application ko PROTECT karne ka framework │
    │                                                              │
    │  Real Life Analogy:                                          │
    │  Tumhara ghar (Application)                                  │
    │                                                              │
    │  BINA Security:                                              │
    │  ✗ Koi bhi ghus sakta hai (unauthorized access)              │
    │  ✗ Sab kuch open hai                                         │
    │                                                              │
    │  WITH Security:                                              │
    │  ✓ Darwaze pe TALA (Authentication)                          │
    │  ✓ Key sirf Sahi Aadmi ke paas (Authorization)               │
    │  ✓ Security Guard (Filter)                                   │
    │  ✓ CCTV Camera (Logging)                                     │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. AUTHENTICATION vs AUTHORIZATION

```
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  AUTHENTICATION (Kaun ho tum?)                               │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  "Mujhe batao tum KAUN ho"                           │    │
    │  │                                                      │    │
    │  │  Tum: "Main Amit hoon"                               │    │
    │  │  Guard: "ID dikhao"                                   │    │
    │  │  Tum: [ID card dikhaya]                               │    │
    │  │  Guard: "Sahi hai, aao andar!" ✅                    │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  AUTHORIZATION (Kya kar sakte ho?)                           │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  "Ab batao tum KYA kar sakte ho"                     │    │
    │  │                                                      │    │
    │  │  Guard: "Tum Sirf Kitchen mein ja sakte ho"          │    │
    │  │  Guard: "Tum Office mein NAHI ja sakte ho" ❌        │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘

    Simple Example:
    ┌──────────────────────────────────────────────────────────────┐
    │  Login Page:                                                │
    │  Username: amit                                              │
    │  Password: 1234                                              │
    │                                                              │
    │  → Authentication: "Amit sahi hai, login karo" ✅           │
    │  → Authorization: "Amit USER hai, admin page NAHI dekh sakta"│
    └──────────────────────────────────────────────────────────────┘
```

---

## 3. SPRING SECURITY FLOW

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Request kaise handle hoti hai:                              │
    │                                                              │
    │  1. Client Request aata hai                                  │
    │         │                                                    │
    │         ▼                                                    │
    │  2. Security Filter Chain (saare filters)                    │
    │         │                                                    │
    │         ├──▶ UsernamePasswordAuthenticationFilter            │
    │         │    (Username/Password check)                       │
    │         │                                                    │
    │         ├──▶ JwtAuthenticationFilter                         │
    │         │    (JWT token check)                               │
    │         │                                                    │
    │         ├──▶ ExceptionTranslationFilter                      │
    │         │    (Error handle karo)                             │
    │         │                                                    │
    │         └──▶ FilterSecurityInterceptor                       │
    │              (Authorization check)                           │
    │         │                                                    │
    │         ▼                                                    │
    │  3. AuthenticationManager                                    │
    │         │                                                    │
    │         ▼                                                    │
    │  4. UserDetailsService                                       │
    │         │                                                    │
    │         ▼                                                    │
    │  5. Database se user details lao                             │
    │         │                                                    │
    │         ▼                                                    │
    │  6. Password check (BCrypt)                                  │
    │         │                                                    │
    │         ▼                                                    │
    │  7. ✅ Access Granted / ❌ Access Denied                     │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 4. SPRING SECURITY BASIC SETUP

```java
// ═══════════════════════════════════════════════════════════════
// 1. DEPENDENCY (pom.xml)
// ═══════════════════════════════════════════════════════════════
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

// ═══════════════════════════════════════════════════════════════
// 2. BASIC CONFIGURATION (SecurityConfig.java)
// ═══════════════════════════════════════════════════════════════
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()  // Public
                .requestMatchers("/admin/**").hasRole("ADMIN")  // Admin only
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")  // User/Admin
                .anyRequest().authenticated()  // Sab authenticated honi chahiye
            )
            .formLogin(form -> form
                .loginPage("/login")  // Custom login page
                .defaultSuccessUrl("/home")  // Success pe kahan jaana hai
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")  // Logout ke baad
                .permitAll()
            );

        return http.build();
    }

    // Password encoder (hashing)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

## 5. AUTHENTICATION SETUP

```java
// ═══════════════════════════════════════════════════════════════
// USER DETAILS SERVICE (Database se user lao)
// ═══════════════════════════════════════════════════════════════
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRoles().toArray(new String[0]))  // Roles add karo
            .build();
    }
}

// ═══════════════════════════════════════════════════════════════
// USER ENTITY
// ═══════════════════════════════════════════════════════════════
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    // Getters and Setters
}
```

---

## 6. JWT (JSON Web Token) AUTHENTICATION

```
    ┌──────────────────────────────────────────────────────────────┐
    │  JWT = Token jo login ke baad milta hai                       │
    │                                                              │
    │  Real Life Analogy:                                          │
    │  Tum exam mein jaate ho                                      │
    │                                                              │
    │  1. Roll number leke jaao (Login with username/password)     │
    │  2. Admit card milta hai (JWT Token)                         │
    │  3. Exam hall mein admit card dikhao (Token bhejo)           │
    │  4. Guard check karega (Validate token)                      │
    │  5. Andar ja sakte ho ✅                                     │
    │                                                              │
    │  JWT Structure:                                              │
    │  ┌─────────────┬─────────────┬─────────────┐                 │
    │  │   Header    │   Payload   │  Signature  │                 │
    │  │  (Algorithm)│   (Data)    │  (Security) │                 │
    │  └─────────────┴─────────────┴─────────────┘                 │
    │                                                              │
    │  Example JWT:                                                │
    │  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWl0Iiwicm9sZSI6InVzZXIifQ.xyz│
    │  ─────────────── ─────────────────────── ─────────────────  │
    │      Header              Payload              Signature     │
    └──────────────────────────────────────────────────────────────┘
```

### JWT Flow:

```
    JWT Authentication Flow:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  1. Client → POST /login {username, password}                │
    │         │                                                    │
    │         ▼                                                    │
    │  2. Server → Validate credentials                           │
    │         │                                                    │
    │         ▼                                                    │
    │  3. Server → Generate JWT token                             │
    │         │                                                    │
    │         ▼                                                    │
    │  4. Server → Return JWT to client                           │
    │         │                                                    │
    │         ▼                                                    │
    │  5. Client → Store JWT (localStorage/cookie)                │
    │         │                                                    │
    │         ▼                                                    │
    │  6. Client → Send JWT in every request                      │
    │              Authorization: Bearer <token>                   │
    │         │                                                    │
    │         ▼                                                    │
    │  7. Server → Validate JWT                                   │
    │         │                                                    │
    │         ▼                                                    │
    │  8. ✅ Access Granted / ❌ Access Denied                     │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

```java
// ═══════════════════════════════════════════════════════════════
// JWT UTILITY CLASS
// ═══════════════════════════════════════════════════════════════
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Token generate karo
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    // Username extract karo
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Token validate karo
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}

// ═══════════════════════════════════════════════════════════════
// JWT AUTHENTICATION FILTER
// ═══════════════════════════════════════════════════════════════
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Authorization header se token lo
        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);  // "Bearer " hatao
            username = jwtUtil.extractUsername(jwt);
        }

        // 2. Token valid hai toh authentication set karo
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

---

## 7. AUTHORIZATION

```
    ┌──────────────────────────────────────────────────────────────┐
    │  AUTHORIZATION = Kya kar sakte ho?                           │
    │                                                              │
    │  Real Life:                                                  │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  USER:  Sirf apna profile dekh sakta hai            │    │
    │  │  ADMIN: Sab ke profiles dekh sakta hai              │    │
    │  │  SUPER: Sab kuch kar sakta hai                      │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  In Code:                                                    │
    │  @PreAuthorize("hasRole('ADMIN')")                           │
    │  @PreAuthorize("hasAuthority('WRITE')")                      │
    │  @PreAuthorize("@customSecurity.check(user, id)")            │
    └──────────────────────────────────────────────────────────────┘
```

```java
// ═══════════════════════════════════════════════════════════════
// METHOD-LEVEL SECURITY
// ═══════════════════════════════════════════════════════════════
@RestController
@RequestMapping("/api")
public class UserController {

    // Sirf USER aur ADMIN access kar sakte hain
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String getUser() {
        return "User data";
    }

    // Sirf ADMIN access kar sakta hai
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdmin() {
        return "Admin data";
    }

    // Custom check
    @GetMapping("/profile/{id}")
    @PreAuthorize("@securityService.checkUserId(authentication, #id)")
    public String getProfile(@PathVariable Long id) {
        return "Profile of user " + id;
    }
}

// ═══════════════════════════════════════════════════════════════
// CONFIGURATION FOR METHOD SECURITY
// ═══════════════════════════════════════════════════════════════
@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {
}
```

---

## 8. SPRING SECURITY CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║           SPRING SECURITY CHEAT SHEET                            ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  CORE CONCEPTS:                                                  ║
║  Authentication → Kaun ho? (Login)                              ║
║  Authorization  → Kya kar sakte ho? (Permissions)               ║
║  Principal      → Logged-in user                                ║
║  Credentials    → Password                                      ║
║  GrantedAuthority → Permission (ROLE_USER, WRITE)               ║
║                                                                  ║
║  KEY CLASSES:                                                    ║
║  UserDetailsService → User data load karo                       ║
║  PasswordEncoder → Password hash karo (BCrypt)                  ║
║  AuthenticationManager → Authentication handle karo             ║
║  SecurityContext → Current user info store karo                 ║
║                                                                  ║
║  ANNOTATIONS:                                                    ║
║  @EnableWebSecurity → Security enable karo                      ║
║  @EnableMethodSecurity → Method security enable karo            ║
║  @PreAuthorize → Method call se pehle check karo                ║
║  @PostAuthorize → Method call ke baad check karo                ║
║  @Secured → Simple role check                                   ║
║  @RolesAllowed → JSR-250 role check                             ║
║                                                                  ║
║  CONFIGURATION:                                                  ║
║  authorizeHttpRequests() → URL-based rules                      ║
║  formLogin() → Login page                                        ║
║  logout() → Logout handling                                      ║
║  cors() → CORS configuration                                     ║
║  csrf() → CSRF protection                                        ║
║  sessionManagement() → Session rules                            ║
║                                                                  ║
║  PASSWORD ENCODING:                                              ║
║  BCryptPasswordEncoder() → Best (salted hash)                   ║
║  passwordEncoder.encode("raw") → Hash banao                     ║
║  passwordEncoder.matches("raw", "hash") → Check karo            ║
║                                                                  ║
║  JWT:                                                            ║
║  generateToken() → Token banao                                  ║
║  extractUsername() → Token se user lo                            ║
║  validateToken() → Token validate karo                          ║
║                                                                  ║
║  COMMON RULES:                                                   ║
║  permitAll() → Public access                                     ║
║  authenticated() → Login required                               ║
║  hasRole("ADMIN") → ADMIN role required                         ║
║  hasAnyRole("USER", "ADMIN") → Any role                         ║
║  hasAuthority("WRITE") → Permission required                    ║
║  denyAll() → Koi access nahi                                    ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 9. SPRING SECURITY INTERVIEW QUESTIONS (40+)

### ⭐ BASIC

**Q1: What is Spring Security?**
> Framework jo authentication (kaun ho) aur authorization (kya kar sakte ho) handle karta hai.

**Q2: What is difference between Authentication and Authorization?**
```
Authentication: Login karna (verify identity)
Authorization: Permission check karna (access control)
```

**Q3: What is BCrypt?**
> Password hashing algorithm with salt. Secure for storing passwords.

**Q4: What is JWT?**
> JSON Web Token - stateless authentication ke liye token.

**Q5: What is CSRF?**
> Cross-Site Request Forgery - attack jo unauthorized actions karta hai. Spring Security mein enabled by default.

**Q6: What is the difference between @PreAuthorize and @Secured?**
```
@PreAuthorize: SpEL expressions (more powerful)
@Secured: Simple role check
```

**Q7: What is SecurityContext?**
> Store karta hai current user ki information (username, roles, authorities).

**Q8: What is the difference between hasRole and hasAuthority?**
```
hasRole: Checks ROLE_ prefix (ROLE_USER)
hasAuthority: Checks exact authority (WRITE)
```

**Q9: What is the difference between permitAll and authenticated?**
```
permitAll: Koi bhi access kar sakta hai (no login)
authenticated: Login required
```

**Q10: What is UserDetailsService?**
> Interface jo database se user details load karta hai during authentication.

---

### ⭐⭐ MIDDLE

**Q11: What is the difference between Session-based and JWT authentication?**
```
Session-based: Server stores session (stateful)
JWT: Client stores token (stateless, scalable)
```

**Q12: What is the difference between AuthenticationManager and UserDetailsService?**
```
AuthenticationManager: Orchestrates authentication process
UserDetailsService: Loads user data from database
```

**Q13: What is the difference between BCrypt and SHA?**
```
BCrypt: Salted, slow (good for passwords)
SHA: Fast, no salt (not good for passwords)
Always use BCrypt for passwords!
```

**Q14: What is the difference between @EnableWebSecurity and @EnableMethodSecurity?**
```
@EnableWebSecurity: URL-based security
@EnableMethodSecurity: Method-level security (@PreAuthorize)
```

**Q15: What is CORS in Spring Security?**
> Cross-Origin Resource Sharing - different domains se requests allow karna.

**Q16: What is the difference between @CrossOrigin and CORS configuration?**
```
@CrossOrigin: Method/Controller level
cors(): Global configuration in SecurityConfig
```

**Q17: What is the difference between JWT and OAuth?**
```
JWT: Token format (can be used with OAuth)
OAuth: Authorization framework (delegated access)
JWT can be used as OAuth token
```

**Q18: What is OAuth2?**
> Authorization framework for delegated access (Login with Google, Facebook).

**Q19: What is the difference between Authentication and Access Control?**
```
Authentication: Identity verification
Access Control: Permission checking (Authorization)
```

**Q20: What is the difference between stateless and stateful security?**
```
Stateless: Server doesn't store session (JWT)
Stateful: Server stores session (traditional)
JWT is stateless (better for microservices)
```

---

### ⭐⭐⭐ ADVANCED

**Q21: What is the difference between AuthenticationSuccessHandler and AuthenticationFailureHandler?**
```
Success: What to do after successful login
Failure: What to do after failed login
```

**Q22: What is the difference between Remember-Me and Session?**
```
Remember-Me: Persistent login (cookie-based)
Session: Temporary login (server-based)
```

**Q23: What is the difference between SecurityFilterChain and WebSecurityConfigurerAdapter?**
```
SecurityFilterChain: Modern approach (Spring Boot 2.7+)
WebSecurityConfigurerAdapter: Old approach (deprecated)
Always use SecurityFilterChain!
```

**Q24: What is the difference between AuthenticationProvider and UserDetailsService?**
```
AuthenticationProvider: Handles authentication logic
UserDetailsService: Loads user data only
AuthenticationProvider uses UserDetailsService
```

**Q24: What is the difference between PasswordEncoder and PasswordStorage?**
```
PasswordEncoder: Encodes/decodes passwords
PasswordStorage: Stores encoded passwords (database)
```

**Q25: What is the difference between JWT and Session token?**
```
JWT: Self-contained (no server lookup needed)
Session: Server lookup needed (stateful)
JWT is faster for distributed systems
```

**Q26: What is the difference between access token and refresh token?**
```
Access token: Short-lived (minutes/hours)
Refresh token: Long-lived (days/weeks)
Refresh token gets new access token
```

**Q27: What is the difference between Authentication and Authorization in JWT?**
```
Authentication: JWT token generate karo (login)
Authorization: JWT token validate karo (access)
```

**Q28: What is the difference between Authentication and Access token?**
```
Authentication: Process of verifying identity
Access token: Token given after authentication
```

**Q29: What is the difference between Authentication and Identity?**
```
Authentication: Process of verifying who you are
Identity: Who you actually are (username, roles)
```

**Q30: What is the difference between Authentication and Credential?**
```
Authentication: Process of verifying credentials
Credential: Username/password (what you provide)
```

**Q31: What is the difference between Authentication and Authorization in JWT?**
```
Authentication JWT: Token banao with user info
Authorization JWT: Token se user info extract karo
```

**Q32: What is the difference between Authentication and Identity in JWT?**
```
Authentication: JWT generate karo (login)
Identity: JWT mein stored user info
```

**Q33: What is the difference between Authentication and Credential in JWT?**
```
Authentication: JWT banao from credentials
Credential: Username/password used to create JWT
```

**Q34: What is the difference between Authentication and Authorization in JWT?**
```
Authentication: JWT token generate (login)
Authorization: JWT token validate (access control)
```

**Q35: What is the difference between Authentication and Identity in Spring Security?**
```
Authentication: Process of verifying identity
Identity: Who you are (username, authorities)
```

**Q36: What is the difference between Authentication and Authorization in Spring Security?**
```
Authentication: Username/password check
Authorization: Role/permission check
```

**Q37: What is the difference between Authentication and Access in Spring Security?**
```
Authentication: Verify identity
Access: Check permissions (Authorization)
```

**Q38: What is the difference between Authentication and Session in Spring Security?**
```
Authentication: Login process
Session: Post-login state management
```

**Q39: What is the difference between Authentication and Token in Spring Security?**
```
Authentication: Process of login
Token: Evidence of login (JWT)
```

**Q40: What is best practice for Spring Security?**
```
1. Always use BCrypt for passwords
2. Use JWT for stateless authentication
3. Enable CSRF protection
4. Use HTTPS
5. Validate tokens on every request
6. Use method-level security (@PreAuthorize)
7. Don't store passwords in plain text
8. Use short-lived access tokens
9. Implement proper CORS
10. Log all authentication attempts
```

---

*Last Updated: September 2026*
*Covers: Spring Security, Authentication, Authorization, JWT, 40 Interview Questions*
