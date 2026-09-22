# Java Servlets - Complete Interview Notes

---

## 1. WHAT IS A SERVLET?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Servlet = Java class that handles HTTP requests & responses │
    │                                                              │
    │  - Runs inside a WEB SERVER (Tomcat, Jetty)                  │
    │  - Part of Jakarta EE (formerly Java EE)                     │
    │  - Handles GET, POST, PUT, DELETE requests                   │
    │  - Generates dynamic web content (HTML, JSON, XML)           │
    │  - Spring Boot uses Servlets UNDER THE HOOD!                 │
    └──────────────────────────────────────────────────────────────┘

    What happens when you type URL in browser:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  Browser ──HTTP Request──→ Web Server (Tomcat)               │
    │                               │                              │
    │                               ▼                              │
    │                          Servlet Container                   │
    │                               │                              │
    │                               ▼                              │
    │                          Your Servlet                        │
    │                          (processes request)                 │
    │                               │                              │
    │                               ▼                              │
    │                          HTTP Response                       │
    │                               │                              │
    │                               ▼                              │
    │                          Browser renders                     │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. SERVLET CONTAINER (TOMCAT)

```
    ┌──────────────────────────────────────────────────────────────┐
    │  What is Tomcat?                                              │
    │                                                              │
    │  - Open-source Web Server + Servlet Container                │
    │  - Also called "Catalina"                                    │
    │  - Handles: HTTP, SSL, JSP, WebSocket                        │
    │  - Manages: Lifecycle, Threading, Security, Deployment       │
    │                                                              │
    │  Other Servlet Containers:                                   │
    │  - Jetty (lightweight, used by Spring Boot)                  │
    │  - WildFly (formerly JBoss)                                  │
    │  - WebLogic (Oracle)                                         │
    │  - WebSphere (IBM)                                           │
    └──────────────────────────────────────────────────────────────┘

    Tomcat Architecture:
    ┌──────────────────────────────────────────────────────────────┐
    │  Server                                                       │
    │  ├── Service                                                  │
    │  │   ├── Connector (HTTP/1.1, HTTP/2, AJP)                   │
    │  │   │   └── Port: 8080                                      │
    │  │   └── Engine                                               │
    │  │       ├── Host (virtual host)                              │
    │  │       │   ├── Context (/myapp)                             │
    │  │       │   │   ├── Servlet                                  │
    │  │       │   │   └── JSP                                      │
    │  │       │   └── Context (/otherapp)                          │
    │  │       └── Host                                             │
    │  └── Service                                                  │
    └──────────────────────────────────────────────────────────────┘
```

---

## 3. SERVLET LIFECYCLE

```
    ┌──────────────────────────────────────────────────────────────┐
    │  1. LOADING                                                   │
    │     Tomcat loads Servlet class                                │
    │     → Class.forName("com.example.MyServlet")                 │
    │                                                              │
    │  2. INSTANTIATION                                            │
    │     Tomcat creates Servlet object                             │
    │     → MyServlet servlet = new MyServlet()                    │
    │                                                              │
    │  3. INITIALIZATION                                           │
    │     init(ServletConfig) called ONCE                          │
    │     → Database connection, configuration loading             │
    │                                                              │
    │  4. SERVICE                                                   │
    │     service(request, response) called for EACH request       │
    │     → doGet(), doPost(), doPut(), doDelete()                │
    │                                                              │
    │  5. DESTROY                                                   │
    │     destroy() called ONCE before unloading                   │
    │     → Cleanup resources, close connections                   │
    │                                                              │
    │  6. GARBAGE COLLECTION                                       │
    │     Servlet object becomes eligible for GC                    │
    └──────────────────────────────────────────────────────────────┘

    Lifecycle Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  LOADING → INSTANCING → INIT → SERVICE → DESTROY → GC       │
    │     1          2          3       4          5        6      │
    │                                                              │
    │  init()  →  Called ONCE (startup)                             │
    │  service() → Called MANY TIMES (every request)               │
    │  destroy() → Called ONCE (shutdown)                           │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 4. SERVLET API - KEY CLASSES

```java
// ═══════════════════════════════════════════════════════════════
// SERVLET INTERFACE (Root)
// ═══════════════════════════════════════════════════════════════
public interface Servlet {
    void init(ServletConfig config) throws ServletException;
    void service(ServletRequest req, ServletResponse res);
    void destroy();
    ServletConfig getServletConfig();
    String getServletInfo();
}

// ═══════════════════════════════════════════════════════════════
// HTTPSERVLET (Abstract Class - Most Used!)
// ═══════════════════════════════════════════════════════════════
public abstract class HttpServlet extends GenericServlet {
    // Overrides service() to dispatch by HTTP method:
    // GET → doGet()
    // POST → doPost()
    // PUT → doPut()
    // DELETE → doDelete()
    // HEAD → doHead()
    // OPTIONS → doOptions()
    // TRACE → doTrace()
}

// ═══════════════════════════════════════════════════════════════
// SERVLETREQUEST & SERVLETRESPONSE
// ═══════════════════════════════════════════════════════════════
public interface ServletRequest {
    Object getAttribute(String name);
    void setAttribute(String name, Object value);
    String getParameter(String name);
    Map<String, String[]> getParameterMap();
    String getRemoteAddr();
    String getContentType();
    ServletInputStream getInputStream();
    BufferedReader getReader();
}

public interface ServletResponse {
    void setContentType(String type);
    PrintWriter getWriter();
    ServletOutputStream getOutputStream();
}

// ═══════════════════════════════════════════════════════════════
// HTTPSERVLETREQUEST & HTTPSERVLETRESPONSE
// ═══════════════════════════════════════════════════════════════
public interface HttpServletRequest extends ServletRequest {
    String getMethod();           // GET, POST, etc.
    String getRequestURI();       // /myapp/users
    String getQueryString();      // ?id=1&name=Amit
    String getHeader(String name);
    Cookie[] getCookies();
    HttpSession getSession();
    String getRemoteAddr();
    int getServerPort();
}

public interface HttpServletResponse extends ServletResponse {
    void setStatus(int sc);
    void setHeader(String name, String value);
    void addHeader(String name, String value);
    void addCookie(Cookie cookie);
    void sendRedirect(String location);
}
```

---

## 5. FIRST SERVLET EXAMPLE

```java
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HelloServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        // Called ONCE when servlet is loaded
        System.out.println("Servlet initialized!");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Set response type
        res.setContentType("text/html");

        // Get writer to send response
        PrintWriter out = res.getWriter();

        // Get request parameters
        String name = req.getParameter("name");
        if (name == null) name = "World";

        // Send HTML response
        out.println("<html><body>");
        out.println("<h1>Hello, " + name + "!</h1>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Read form data
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        PrintWriter out = res.getWriter();
        out.println("Username: " + username);
    }

    @Override
    public void destroy() {
        // Called ONCE when servlet is unloaded
        System.out.println("Servlet destroyed!");
    }
}
```

### 5.1 web.xml Configuration

```xml
<!-- web.xml (Deployment Descriptor) -->
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee">

    <servlet>
        <servlet-name>HelloServlet</servlet-name>
        <servlet-class>com.example.HelloServlet</servlet-class>
        <load-on-startup>1</load-on-startup>  <!-- Load at startup -->
    </servlet>

    <servlet-mapping>
        <servlet-name>HelloServlet</servlet-name>
        <url-pattern>/hello</url-pattern>  <!-- URL to access -->
    </servlet-mapping>

</web-app>

<!-- Access: http://localhost:8080/myapp/hello?name=Amit -->
```

### 5.2 Annotation-based (No web.xml)

```java
@WebServlet(name = "HelloServlet", urlPatterns = "/hello", loadOnStartup = 1)
public class HelloServlet extends HttpServlet {
    // No web.xml needed!
}

// Multiple URL patterns
@WebServlet(urlPatterns = {"/hello", "/greet", "/welcome"})
```

---

## 6. HTTP METHODS

```
    ┌──────────┬───────────────────────────────────────────────────┐
    │ Method    │ Description                                       │
    ├──────────┼───────────────────────────────────────────────────┤
    │ GET      │ Retrieve data (safe, idempotent, cached)          │
    │ POST     │ Create/submit data (not idempotent, not cached)   │
    │ PUT      │ Update/replace entire resource (idempotent)       │
    │ DELETE   │ Delete resource (idempotent)                       │
    │ PATCH    │ Partial update (not idempotent)                    │
    │ HEAD     │ Same as GET but no body (headers only)            │
    │ OPTIONS  │ Supported methods for a resource                   │
    │ TRACE    │ Loop-back test                                    │
    └──────────┴───────────────────────────────────────────────────┘

    Idempotent = Same request multiple times → same result

    GET vs POST:
    ┌──────────────────┬──────────────────┬──────────────────────┐
    │ Feature           │ GET              │ POST                  │
    ├──────────────────┼──────────────────┼──────────────────────┤
    │ URL visible       │ Yes              │ No (in body)          │
    │ Data limit        │ ~2KB (URL)       │ No limit              │
    │ Cached            │ Yes              │ No                    │
    │ Bookmarked        │ Yes              │ No                    │
    │ Security          │ Low              │ Medium                │
    │ Use case          │ Search, view     │ Login, submit form    │
    │ Parameters        │ ?key=value       │ In request body       │
    └──────────────────┴──────────────────┴──────────────────────┘
```

---

## 7. REQUEST PROCESSING FLOW

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Browser: GET /myapp/hello?name=Amit HTTP/1.1                │
    └───────────────────────┬──────────────────────────────────────┘
                            │
                            ▼
    ┌──────────────────────────────────────────────────────────────┐
    │  Tomcat (Port 8080)                                          │
    │  ├── Connector receives HTTP request                         │
    │  ├── Parses HTTP headers                                     │
    │  ├── Creates HttpServletRequest object                       │
    │  ├── Creates HttpServletResponse object                      │
    │  └── Passes to Engine                                        │
    └───────────────────────┬──────────────────────────────────────┘
                            │
                            ▼
    ┌──────────────────────────────────────────────────────────────┐
    │  Servlet Container                                            │
    │  ├── Maps URL to Servlet (/hello → HelloServlet)             │
    │  ├── Gets/Creates Servlet instance                           │
    │  ├── Creates Thread for request                              │
    │  └── Calls service() method                                  │
    └───────────────────────┬──────────────────────────────────────┘
                            │
                            ▼
    ┌──────────────────────────────────────────────────────────────┐
    │  HelloServlet.service()                                      │
    │  ├── Determines HTTP method (GET)                            │
    │  ├── Calls doGet(req, res)                                   │
    │  ├── Process request                                         │
    │  └── Send response                                           │
    └───────────────────────┬──────────────────────────────────────┘
                            │
                            ▼
    ┌──────────────────────────────────────────────────────────────┐
    │  HTTP/1.1 200 OK                                             │
    │  Content-Type: text/html                                     │
    │                                                              │
    │  <html><body><h1>Hello, Amit!</h1></body></html>            │
    └──────────────────────────────────────────────────────────────┘
```

---

## 8. THREADING MODEL

```
    ┌──────────────────────────────────────────────────────────────┐
    │  ONE Servlet Instance → MULTIPLE Threads                      │
    │                                                              │
    │  ┌─────────────┐                                            │
    │  │ Servlet      │  (Single instance - Singleton per JVM)    │
    │  │ Instance     │                                           │
    │  └──────┬──────┘                                            │
    │         │                                                    │
    │    ┌────┴────┬────────┬────────┐                            │
    │    ▼         ▼        ▼        ▼                            │
    │  Thread 1  Thread 2  Thread 3  Thread 4                     │
    │  [Request1][Request2][Request3][Request4]                   │
    │                                                              │
    │  ⚠️ DANGER: Shared state between threads!                   │
    │  Instance variables are NOT thread-safe!                    │
    └──────────────────────────────────────────────────────────────┘

    Thread Safety Example:
    ┌──────────────────────────────────────────────────────────────┐
    │  ❌ UNSAFE:                                                  │
    │  public class BadServlet extends HttpServlet {                │
    │      private int counter = 0;  // SHARED across threads!     │
    │      protected void doGet(...) {                              │
    │          counter++;  // Race condition!                       │
    │      }                                                       │
    │  }                                                           │
    │                                                              │
    │  ✅ SAFE:                                                    │
    │  public class GoodServlet extends HttpServlet {               │
    │      protected void doGet(...) {                              │
    │          int localCounter = 0;  // Thread-local              │
    │          localCounter++;  // Safe!                            │
    │      }                                                       │
    │  }                                                           │
    │                                                              │
    │  ✅ SAFE: Use synchronized or AtomicInteger                  │
    │  private AtomicInteger counter = new AtomicInteger(0);        │
    └──────────────────────────────────────────────────────────────┘
```

---

## 9. SERVLET CONFIG & CONTEXT

```java
// ═══════════════════════════════════════════════════════════════
// SERVLET CONFIG (Per Servlet)
// ═══════════════════════════════════════════════════════════════
@WebServlet(
    urlPatterns = "/hello",
    initParams = {
        @WebInitParam(name = "maxResults", value = "100"),
        @WebInitParam(name = "apiKey", value = "abc123")
    }
)
public class HelloServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        String maxResults = config.getInitParameter("maxResults");
        String apiKey = config.getInitParameter("apiKey");
    }
}

// ═══════════════════════════════════════════════════════════════
// SERVLET CONTEXT (Shared across ALL Servlets)
// ═══════════════════════════════════════════════════════════════
public class MyServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();

        // Set shared attribute
        context.setAttribute("appName", "MyApp");

        // Get context parameters (from web.xml)
        String dbUrl = context.getInitParameter("dbUrl");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        ServletContext context = getServletContext();
        String appName = (String) context.getAttribute("appName");
    }
}

// web.xml context parameters
<context-param>
    <param-name>dbUrl</param-name>
    <param-value>jdbc:mysql://localhost:3306/mydb</param-value>
</context-param>
```

```
    Config vs Context:
    ┌──────────────────────┬──────────────────────┐
    │ ServletConfig         │ ServletContext        │
    ├──────────────────────┼──────────────────────┤
    │ Per Servlet           │ Per Web Application   │
    │ init-param            │ context-param          │
    │ getInitParameter()    │ getAttribute()         │
    │ @WebInitParam         │ @WebListener           │
    └──────────────────────┴──────────────────────┘
```

---

## 10. REQUEST DISPATCHING (Forward & Include)

```java
// ═══════════════════════════════════════════════════════════════
// FORWARD (Server-side redirect)
// ═══════════════════════════════════════════════════════════════
RequestDispatcher rd = req.getRequestDispatcher("/result.jsp");
rd.forward(req, res);

// Client sees: /original-url (URL doesn't change!)
// Server internally forwards to /result.jsp

// Visual:
// Browser ──GET /login──→ Servlet ──forward──→ JSP
// Browser sees: /login (always)
// Content comes from: JSP

// ═══════════════════════════════════════════════════════════════
// INCLUDE (Combine multiple resources)
// ═══════════════════════════════════════════════════════════════
RequestDispatcher rd = req.getRequestDispatcher("/header.jsp");
rd.include(req, res);

// Adds content from another resource into current response

// Forward vs Include:
┌──────────────────────┬──────────────────────┬──────────────────────┐
│ Feature               │ forward()            │ include()             │
├──────────────────────┼──────────────────────┼──────────────────────┤
│ URL change            │ No (client sees orig) │ No                   │
│ Request scope         │ Shared               │ Shared               │
│ Response              │ Replaced by target   │ Combined             │
│ Can write to response │ No (after forward)   │ Yes                  │
│ Multiple calls        │ No (one only)        │ Yes (multiple)       │
└──────────────────────┴──────────────────────┴──────────────────────┘
```

---

## 11. REDIRECT vs FORWARD

```
    ┌──────────────────────────────────────────────────────────────┐
    │  REDIRECT (Client-side)                                       │
    │  res.sendRedirect("/success");                                │
    │                                                              │
    │  Browser ──GET /login──→ Servlet ──302──→ Browser            │
    │  Browser ──GET /success──→ New Servlet                       │
    │                                                              │
    │  - URL changes in browser                                     │
    │  - New HTTP request                                           │
    │  - Request attributes LOST                                    │
    │  - Can redirect to EXTERNAL URL                               │
    │  - Status code: 302 (Found) or 301 (Moved)                   │
    │                                                              │
    ├──────────────────────────────────────────────────────────────┤
    │  FORWARD (Server-side)                                        │
    │  req.getRequestDispatcher("/success").forward(req, res);       │
    │                                                              │
    │  Browser ──GET /login──→ Servlet1 ──forward──→ Servlet2      │
    │  Browser sees: /login (URL doesn't change!)                  │
    │                                                              │
    │  - URL stays same in browser                                  │
    │  - Same request (no new HTTP request)                         │
    │  - Request attributes KEPT                                    │
    │  - Can only forward WITHIN same application                   │
    │  - Status code: unchanged                                     │
    └──────────────────────────────────────────────────────────────┘

    Comparison:
    ┌──────────────────┬──────────────────────┬──────────────────────┐
    │ Feature           │ sendRedirect()        │ forward()            │
    ├──────────────────┼──────────────────────┼──────────────────────┤
    │ URL in browser    │ Changes               │ Same                 │
    │ HTTP request      │ New request           │ Same request         │
    │ Request scope     │ Lost                  │ Preserved            │
    │ External URL      │ ✅ Yes                │ ❌ No                 │
    │ Performance       │ Slower (2 requests)   │ Faster (1 request)   │
    │ Return after call │ Can write response    │ Must return          │
    │ Status code       │ 302/301               │ Unchanged            │
    └──────────────────┴──────────────────────┴──────────────────────┘
```

---

## 12. SESSION MANAGEMENT

```java
// ═══════════════════════════════════════════════════════════════
// WHAT IS SESSION?
// ═══════════════════════════════════════════════════════════════
// Session = Server-side storage for client state across requests

// HTTP is STATELESS! Each request is independent.
// Session maintains user state (login, cart, etc.)

// ═══════════════════════════════════════════════════════════════
// USING HTTPSESSION
// ═══════════════════════════════════════════════════════════════

// Create/Get session
HttpSession session = req.getSession();  // Creates if doesn't exist
HttpSession session = req.getSession(false);  // Get only if exists

// Store attributes
session.setAttribute("username", "Amit");
session.setAttribute("cart", cartItems);

// Get attributes
String user = (String) session.getAttribute("username");

// Remove attribute
session.removeAttribute("username");

// Session info
String id = session.getId();           // Unique session ID
long created = session.getCreationTime();
long lastAccess = session.getLastAccessedTime();
int maxInactive = session.getMaxInactiveInterval();

// Set timeout (30 min default)
session.setMaxInactiveInterval(30 * 60);  // seconds

// Invalidate (logout)
session.invalidate();

// ═══════════════════════════════════════════════════════════════
// SESSION ID MECHANISM
// ═══════════════════════════════════════════════════════════════

// 1. Client sends first request
// 2. Server creates session, generates unique ID
// 3. Server sends session ID via cookie (JSESSIONID)
// 4. Client sends cookie with subsequent requests
// 5. Server looks up session by ID

// Visual:
// Request 1: Browser ──→ Server creates session(abc123)
//                      ←── Set-Cookie: JSESSIONID=abc123
//
// Request 2: Browser ──→ Cookie: JSESSIONID=abc123
//                      ←── Server finds session(abc123)
```

---

## 13. COOKIES

```java
// ═══════════════════════════════════════════════════════════════
// COOKIES (Client-side storage)
// ═══════════════════════════════════════════════════════════════

// Create cookie
Cookie cookie = new Cookie("username", "Amit");
cookie.setMaxAge(7 * 24 * 60 * 60);  // 7 days (in seconds)
cookie.setPath("/");  // Available for all paths
cookie.setHttpOnly(true);  // Not accessible via JavaScript
cookie.setSecure(true);  // Only sent over HTTPS

// Add cookie to response
res.addCookie(cookie);

// Read cookies
Cookie[] cookies = req.getCookies();
if (cookies != null) {
    for (Cookie c : cookies) {
        if (c.getName().equals("username")) {
            String value = c.getValue();
        }
    }
}

// Delete cookie
Cookie cookie = new Cookie("username", "");
cookie.setMaxAge(0);  // Delete
res.addCookie(cookie);
```

```
    Cookie vs Session:
    ┌──────────────────────┬──────────────────────┐
    │ Cookie                │ Session               │
    ├──────────────────────┼──────────────────────┤
    │ Client-side           │ Server-side           │
    │ Stored in browser     │ Stored in server      │
    │ Limited size (4KB)    │ Unlimited             │
    │ Visible to user       │ Hidden from user      │
    │ Can be disabled       │ Requires cookies      │
    │ Persistent            │ Temporary             │
    │ Less secure           │ More secure           │
    └──────────────────────┴──────────────────────┘
```

---

## 14. FILTERS & LISTENERS

```java
// ═══════════════════════════════════════════════════════════════
// FILTER (Intercept requests/responses)
// ═══════════════════════════════════════════════════════════════

@WebFilter(urlPatterns = "/*")
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
        // Called once
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes = (HttpServletResponse) res;

        // Before processing
        long start = System.currentTimeMillis();
        System.out.println("Request: " + httpReq.getMethod() + " " + httpReq.getRequestURI());

        // Pass to next filter or servlet
        chain.doFilter(req, res);

        // After processing
        long time = System.currentTimeMillis() - start;
        System.out.println("Response time: " + time + "ms");
    }

    @Override
    public void destroy() {
        // Called once
    }
}

// Filter Chain:
// Request → Filter1 → Filter2 → Servlet → Filter2 → Filter1 → Response
```

```java
// ═══════════════════════════════════════════════════════════════
// LISTENER (React to lifecycle events)
// ═══════════════════════════════════════════════════════════════

// ServletContextListener - App start/stop
@WebListener
public class AppListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("App started!");
        // Initialize DB connection pool, cache, etc.
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("App stopped!");
        // Cleanup resources
    }
}

// HttpSessionListener - Session created/destroyed
@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent sce) {
        System.out.println("Session created: " + sce.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sce) {
        System.out.println("Session destroyed");
    }
}
```

```
    Filter Chain Visual:
    ┌──────────────────────────────────────────────────────────────┐
    │  Request                                                      │
    │    │                                                          │
    │    ▼                                                          │
    │  ┌──────────┐                                                │
    │  │ Auth     │ ──→ check auth ──→ chain.doFilter() ──→ next   │
    │  │ Filter   │                                                │
    │  └──────────┘                                                │
    │    │                                                          │
    │    ▼                                                          │
    │  ┌──────────┐                                                │
    │  │ Logging  │ ──→ log request ──→ chain.doFilter() ──→ next  │
    │  │ Filter   │                                                │
    │  └──────────┘                                                │
    │    │                                                          │
    │    ▼                                                          │
    │  ┌──────────┐                                                │
    │  │ Servlet  │ ──→ process request ──→ send response          │
    │  └──────────┘                                                │
    │    │                                                          │
    │    ▼                                                          │
    │  Response ←── Logging Filter (log response time)             │
    │             ←── Auth Filter (nothing to do)                  │
    └──────────────────────────────────────────────────────────────┘
```

---

## 15. SERVLET vs JSP vs SPRING MVC

```
    ┌──────────────────────┬──────────────────────┬──────────────────────┐
    │ Feature               │ Servlet              │ JSP                  │
    ├──────────────────────┼──────────────────────┼──────────────────────┤
    │ Purpose               │ Java code            │ HTML + Java code     │
    │ Presentation          │ Hard to embed HTML   │ Easy (like PHP)      │
    │ Code style            │ Out.println()        │ HTML tags            │
    │ Compilation           │ Compiled to .class   │ Compiled to .java    │
    │ MVC Role              │ Controller           │ View                 │
    │ Use case              │ Business logic       │ UI rendering         │
    └──────────────────────┴──────────────────────┴──────────────────────┘

    ┌──────────────────────┬──────────────────────┬──────────────────────┐
    │ Feature               │ Servlet              │ Spring MVC           │
    ├──────────────────────┼──────────────────────┼──────────────────────┤
    │ Configuration         │ web.xml / @WebServlet│ Annotations          │
    │ URL Mapping           │ @WebServlet          │ @RequestMapping      │
    │ Request Handling      │ doGet/doPost         │ @GetMapping/@PostMapping│
    │ View Resolution       │ Manual               │ ViewResolver         │
    │ Dependency Injection  │ Manual               │ @Autowired           │
    │ Testing               │ Hard                 │ Easy (@MockMvc)      │
    │ REST API              │ Manual (write JSON)  │ @RestController      │
    └──────────────────────┴──────────────────────┴──────────────────────┘

    Evolution:
    ┌──────────────────────────────────────────────────────────────┐
    │  Servlet (1997)                                              │
    │    ↓ Too verbose, hard to maintain                           │
    │  JSP (1999)                                                  │
    │    ↓ Mixed logic & presentation                              │
    │  MVC Frameworks (Struts, Spring MVC)                         │
    │    ↓ Configuration heavy                                     │
    │  Spring Boot (2014)                                          │
    │    ↓ Convention over configuration, auto-config              │
    │  Spring WebFlux (2017)                                       │
    │    ↓ Reactive, non-blocking                                  │
    └──────────────────────────────────────────────────────────────┘
```

---

## 16. SPRING BOOT & SERVLET

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Spring Boot UNDER THE HOOD uses Servlets!                   │
    │                                                              │
    │  @RestController                                             │
    │  class UserController {                                      │
    │      @GetMapping("/users")                                   │
    │      public List<User> getUsers() { ... }                    │
    │  }                                                          │
    │                                                              │
    │  Behind the scenes:                                          │
    │  1. Spring Boot embedded Tomcat (Jetty/Undertow)             │
    │  2. DispatcherServlet handles all requests                   │
    │  3. Your @Controller is called by DispatcherServlet          │
    │  4. Response sent back through Tomcat                        │
    │                                                              │
    │  Spring MVC DispatcherServlet:                               │
    │  Browser → Tomcat → DispatcherServlet → Your Controller      │
    │                                                              │
    │  WHY learn Servlets?                                         │
    │  ✅ Understand what Spring Boot does under the hood           │
    │  ✅ Debug web application issues                              │
    │  ✅ Configure filters, listeners, interceptors               │
    │  ✅ Understand request lifecycle                              │
    │  ✅ Interview questions!                                      │
    └──────────────────────────────────────────────────────────────┘
```

---

## 17. SERVLET CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║                SERVLET CHEAT SHEET                                ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  LIFECYCLE:                                                      ║
║  Loading → Init → Service → Destroy → GC                        ║
║  init()      → ONCE (startup)                                    ║
║  service()   → EVERY REQUEST                                     ║
║  destroy()   → ONCE (shutdown)                                   ║
║                                                                  ║
║  KEY CLASSES:                                                    ║
║  Servlet → GenericServlet → HttpServlet                          ║
║  HttpServletRequest  (extends ServletRequest)                    ║
║  HttpServletResponse (extends ServletResponse)                   ║
║  ServletConfig (per servlet)                                     ║
║  ServletContext (per app)                                        ║
║  HttpSession (per user)                                          ║
║                                                                  ║
║  HTTP METHODS:                                                   ║
║  GET (read)  POST (create)  PUT (update)  DELETE (delete)       ║
║                                                                  ║
║  REQUEST DISPATCHING:                                            ║
║  forward()  → Server-side, URL same, request preserved          ║
║  redirect() → Client-side, URL changes, new request             ║
║  include()  → Combine multiple resources                        ║
║                                                                  ║
║  SESSION:                                                        ║
║  req.getSession() → get/create session                          ║
║  session.setAttribute() / getAttribute()                        ║
║  session.invalidate() → logout                                  ║
║  JSESSIONID cookie → tracks session                             ║
║                                                                  ║
║  COOKIES:                                                        ║
║  new Cookie(name, value) → res.addCookie()                      ║
║  req.getCookies() → Cookie[]                                    ║
║  cookie.setMaxAge(seconds) → persistence                        ║
║                                                                  ║
║  FILTER:                                                         ║
║  @WebFilter(urlPatterns="/*")                                   ║
║  doFilter() → chain.doFilter() → pass to next                   ║
║                                                                  ║
║  LISTENER:                                                       ║
║  @WebListener → ServletContextListener, HttpSessionListener      ║
║                                                                  ║
║  THREADING:                                                      ║
║  1 Servlet instance → Many threads                               ║
║  ⚠️ Instance variables NOT thread-safe!                          ║
║                                                                  ║
║  DEPLOYMENT:                                                     ║
║  @WebServlet → Annotation-based (no web.xml)                     ║
║  web.xml → XML configuration (legacy)                            ║
║  WAR file → Deployed to Tomcat                                   ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 18. SERVLET INTERVIEW QUESTIONS (40+)

### ⭐ BASIC

**Q1: What is a Servlet?**
> Java class that handles HTTP requests/responses. Runs inside a servlet container (Tomcat).

**Q2: What is difference between Servlet and JSP?**
```
Servlet: Java code generates HTML (out.println)
JSP: HTML with embedded Java (<% %>)
JSP compiles to Servlet internally
```

**Q3: What is the lifecycle of a Servlet?**
```
Loading → Instantiation → Initialization → Service → Destruction → GC
init() called ONCE, service() called EVERY request, destroy() ONCE
```

**Q4: What is difference between init() and constructor?**
```
Constructor: Called when class is loaded (may not be a servlet context)
init(): Called when servlet is initialized by container (has ServletConfig)
```

**Q5: What is doGet() vs doPost()?**
```
doGet(): Handles GET requests (read-only, URL params)
doPost(): Handles POST requests (create/submit, body params)
```

**Q6: What is RequestDispatcher?**
> Forwards/includes requests to other resources. Methods: forward(), include()

**Q7: What is difference between forward() and redirect()?**
```
forward(): Server-side, URL same, request preserved, faster
redirect(): Client-side, URL changes, new request, slower
```

**Q8: What is difference between Request and Session?**
```
Request: Per single request, lost after response
Session: Across multiple requests, persists for user
```

---

### ⭐⭐ MIDDLE

**Q9: What is ServletConfig?**
> Per-servlet configuration. init-param in web.xml or @WebInitParam. Used in init().

**Q10: What is ServletContext?**
> Per-web-application shared context. context-param in web.xml. All servlets share it.

**Q11: What is HttpSession?**
> Server-side storage for user state. Created per user. Tracks user across requests via JSESSIONID.

**Q12: How does session work?**
```
1. req.getSession() creates session
2. Server generates unique session ID
3. Sends JSESSIONID cookie to client
4. Client sends cookie with subsequent requests
5. Server looks up session by ID
```

**Q13: What is difference between HttpSession and Cookie?**
```
Session: Server-side, unlimited size, more secure
Cookie: Client-side, 4KB limit, less secure, can be disabled
```

**Q4: What is Filter?**
> Intercepts requests/responses. @WebFilter annotation. Used for logging, auth, encoding, CORS.

**Q15: What is difference between Filter and Interceptor (Spring)?**
```
Filter: Servlet API, before/after Servlet, web.xml or @WebFilter
Interceptor: Spring MVC, before/after Controller, @Interceptor
```

**Q16: What is difference between forward and include?**
```
forward(): Replaces current response (one resource handles)
include(): Adds content from another resource (combines both)
```

**Q17: What is sendRedirect?**
> Client-side redirect. Sends 302 status. Browser makes new request. URL changes.

**Q18: What is difference between encodeURL and encodeRedirectURL?**
```
encodeURL(): Rewrites URL to include session ID (if cookies disabled)
encodeRedirectURL(): Same but for sendRedirect URLs
```

**Q19: What is ServletContextListener?**
> Listens for app startup/shutdown. Initialize resources on start, cleanup on stop.

**Q20: What is Cookie vs Session vs Application?**
```
Cookie: Client-side, per-browser
Session: Server-side, per-user
Application: Server-side, global (ServletContext)
```

**Q21: What is HttpSessionListener?**
> Listens for session creation/destruction. Track active sessions.

**Q22: What is difference between getSession() and getSession(false)?**
```
getSession(): Creates session if doesn't exist
getSession(false): Returns null if no session exists
```

**Q23: What is the difference between getParameter and getAttribute?**
```
getParameter(): Gets request parameters (URL/form data)
getAttribute(): Gets request attributes (set by servlet/filter)
```

**Q24: What is the difference between getParameter and getHeader?**
```
getParameter(): Query string / form data (?key=value)
getHeader(): HTTP headers (User-Agent, Content-Type, etc.)
```

**Q25: What is difference between GenericServlet and HttpServlet?**
```
GenericServlet: Protocol-independent, implements service()
HttpServlet: HTTP-specific, implements doGet/doPost/doPut/doDelete
Always extend HttpServlet for HTTP servlets
```

---

### ⭐⭐⭐ ADVANCED

**Q26: What is single-thread model?**
```java
// Deprecated! (SingleThreadModel)
// Only one thread can access servlet at a time
// BAD: Poor performance, not scalable
// Servlet is NOT thread-safe by default!
```

**Q27: How to make Servlet thread-safe?**
```
1. Don't use instance variables for request data
2. Use local variables (thread-local)
3. Use synchronized (carefully)
4. Use AtomicInteger for counters
5. Use HttpSession for user-specific data
```

**Q28: What is ServletOutputStream vs PrintWriter?**
```
ServletOutputStream: Binary data (images, files)
PrintWriter: Text data (HTML, JSON, XML)
```

**Q29: What is difference between GenericServlet and HttpServlet?**
```
GenericServlet: Protocol-independent
HttpServlet: HTTP-specific (doGet, doPost, etc.)
HttpServlet extends GenericServlet
```

**Q30: What is war file?**
> Web Application Archive. Contains: classes, JSPs, web.xml, libraries, static resources. Deployed to Tomcat.

**Q31: What is the difference between / and /* in URL pattern?**
```
/employee/* → Matches /employee/anything (specific)
/*          → Matches everything (all requests)
/employee   → Matches exactly /employee
```

**Q32: What is difference between DispatcherServlet and HttpServlet?**
```
HttpServlet: Standard servlet, handles HTTP directly
DispatcherServlet: Spring MVC front controller, routes to controllers
Spring Boot auto-configures DispatcherServlet
```

**Q33: What is load-on-startup in web.xml?**
```
<load-on-startup>1</load-on-startup>
Load servlet at app startup (not on first request)
Number = load order (lower = first)
```

**Q34: What is the difference between Serializable and HttpSession?**
```
Serializable: Interface for object serialization
HttpSession: Server-side session (may use serialization internally)
```

**Q35: What is URL encoding and why needed?**
```
Special characters in URL need encoding:
"hello world" → "hello%20world"
URLDecoder.decode(encoded, "UTF-8")
URLEncoder.encode(string, "UTF-8")
```

**Q36: What is the difference between javax.servlet and jakarta.servlet?**
```
javax.servlet: Java EE (pre-Java 11)
jakarta.servlet: Jakarta EE (Java 11+)
Same API, different package name
```

**Q37: What is the difference between Servlet and Filter lifecycle?**
```
Servlet: init() → service() → destroy()
Filter:  init() → doFilter() → destroy()
Both called ONCE for init/destroy
```

**Q38: What happens if doPost called but only doGet implemented?**
> Container returns 405 Method Not Allowed.

**Q39: What is the difference between getWriter and getOutputStream?**
```
getWriter(): PrintWriter for text (character stream)
getOutputStream(): ServletOutputStream for binary (byte stream)
Cannot call both in same request!
```

**Q40: What is the difference between Tomcat and Jetty?**
```
Tomcat: Full servlet container + JSP, most popular
Jetty: Lightweight, embedded, used by Spring Boot default
Both implement Servlet API
```

---

*Last Updated: September 2026*
*Covers: Servlet Lifecycle, HTTP Methods, Session, Cookies, Filters, Listeners, Interview Questions*
