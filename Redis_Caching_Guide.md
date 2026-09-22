# Redis & Caching - Zero se Seekho (Complete Guide)

---

## 📌 PART 1: CACHING KYA HAI?

### Caching Concept:

```
┌──────────────────────────────────────────────────────────────┐
│  CACHE = Frequently used data ko FAST jagah pe rakhna         │
│                                                              │
│  Real Life Analogy:                                          │
│  ┌──────────────────────────────────────────────────────┐    │
│  │                                                      │    │
│  │  BINA Cache (Slow):                                  │    │
│  │  Tumhe recipe chahiye → Library jaao → Book dhundho   │    │
│  │  → Wapas ghar → Padho → Cook karo                   │    │
│  │  (Har baar Library jaana padega! 😫)                 │    │
│  │                                                      │    │
│  │  WITH Cache (Fast):                                  │    │
│  │  Tumhe recipe chahiye → Kitchen mein rakhi hai! ✅   │    │
│  │  (Turant mil jayegi! 🚀)                            │    │
│  │                                                      │    │
│  │  Cache = Recipe book kitchen mein (fast access)      │    │
│  │  Database = Library (slow but complete)              │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Cache = FAST memory (RAM)                                    │
│  Database = SLOW storage (Disk)                               │
│                                                              │
│  Cache Speed: ~0.1ms (100x faster than DB!)                  │
│  Database Speed: ~10-100ms                                   │
└──────────────────────────────────────────────────────────────┘
```

### Cache Flow:

```
    Without Cache:
    ┌─────────┐      ┌─────────────┐      ┌─────────────┐
    │ Client  │─────▶│  Application │─────▶│  Database   │
    │         │◀─────│             │◀─────│  (Disk)     │
    └─────────┘      └─────────────┘      └─────────────┘
                     Every request goes to DB (SLOW!)

    With Cache:
    ┌─────────┐      ┌─────────────┐      ┌─────────────┐
    │ Client  │─────▶│  Application │─────▶│   Cache     │
    │         │◀─────│             │◀─────│   (RAM)     │
    └─────────┘      └──────┬──────┘      └─────────────┘
                            │
                            │ (Cache miss?)
                            ▼
                      ┌─────────────┐
                      │  Database   │
                      │   (Disk)    │
                      └─────────────┘

    Flow:
    1. Client request → Application
    2. Application → Check Cache
    3. Cache HIT → Return from cache (FAST!) ✅
    4. Cache MISS → Go to Database → Save in Cache → Return
```

### Cache Hit vs Miss:

```
┌──────────────────────────────────────────────────────────────┐
│                                                              │
│  CACHE HIT (Data cache mein hai):                            │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Request → Cache → "Found!" → Return (0.1ms) 🚀      │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  CACHE MISS (Data cache mein NAHI hai):                      │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Request → Cache → "Not found!" → DB query →          │    │
│  │  → Save in Cache → Return (10-100ms) 😫              │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Goal: Maximize Cache Hit Rate!                              │
│  Good: 90%+ hit rate                                         │
│  Bad: <50% hit rate                                          │
│                                                              │
│  Hit Rate = (Hits / Total Requests) × 100                    │
└──────────────────────────────────────────────────────────────┘
```

---

## 📌 PART 2: REDIS KYA HAI?

### Redis Overview:

```
┌──────────────────────────────────────────────────────────────┐
│  REDIS = Remote Dictionary Server                            │
│                                                              │
│  Redis = In-Memory Data Store (Cache ke liye BEST!)          │
│                                                              │
│  Real Life:                                                  │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Database = Fridge (slow to open, but stores a lot)   │    │
│  │  Redis = Kitchen counter (fast access, limited space) │    │
│  │                                                      │    │
│  │  Frequently used items → Kitchen counter (Redis)     │    │
│  │  Rarely used items → Fridge (Database)               │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Why Redis?                                                   │
│  ✅ Super fast (in-memory, ~0.1ms)                           │
│  ✅ Supports many data structures                            │
│  ✅ Built-in caching (TTL, eviction)                         │
│  ✅ Pub/Sub, Lua scripting, Transactions                     │
│  ✅ Persistence (optional)                                   │
│  ✅ Distributed (can cluster)                                │
└──────────────────────────────────────────────────────────────┘
```

### Redis vs Database:

```
╔══════════════════════════════════════════════════════════════════╗
║                 REDIS vs DATABASE                                ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  Feature        │ Redis              │ Database                  ║
║  ───────────────┼────────────────────┼──────────────────────     ║
║  Storage        │ RAM (Memory)       │ Disk (Storage)            ║
║  Speed          │ ~0.1ms (FAST!)     │ ~10-100ms                ║
║  Capacity       │ Limited (GBs)      │ Large (TBs)              ║
║  Data Structure │ Key-Value, List,   │ Tables, Rows             ║
║                 │ Set, Hash, etc.    │                           ║
║  Persistence    │ Optional           │ Always                    ║
║  Cost           │ Expensive (RAM)    │ Cheap (Disk)             ║
║  Use Case       │ Cache, Session,    │ Permanent storage         ║
║                 │ Real-time          │                           ║
║                                                                  ║
║  Rule: Redis = FAST but LIMITED                                ║
║        Database = SLOW but UNLIMITED                           ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

### Redis Architecture:

```
┌──────────────────────────────────────────────────────────────┐
│  REDIS ARCHITECTURE                                          │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐    │
│  │                    REDIS SERVER                      │    │
│  │                                                      │    │
│  │  ┌─────────────────────────────────────────────┐    │    │
│  │  │              IN-MEMORY STORE                 │    │    │
│  │  │  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐  │    │    │
│  │  │  │Key1 │ │Key2 │ │Key3 │ │Key4 │ │Key5 │  │    │    │
│  │  │  │     │ │     │ │     │ │     │ │     │  │    │    │
│  │  │  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘  │    │    │
│  │  └─────────────────────────────────────────────┘    │    │
│  │                                                      │    │
│  │  Features:                                           │    │
│  │  - Single-threaded (fast for small ops)             │    │
│  │  - Event-driven (non-blocking I/O)                  │    │
│  │  - Persistence: RDB (snapshot) / AOF (append log)   │    │
│  │  - Eviction: LRU/LFU when memory full              │    │
│  │                                                      │    │
│  └──────────────────────────────────────────────────────┘    │
│                          │                                    │
│         ┌────────────────┼────────────────┐                  │
│         ▼                ▼                ▼                  │
│    ┌─────────┐     ┌──────────┐     ┌──────────┐            │
│    │ App 1   │     │ App 2    │     │ App 3    │            │
│    └─────────┘     └──────────┘     └──────────┘            │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

---

## 📌 PART 3: REDIS DATA STRUCTURES

### 1. STRING (Most Common!)

```
┌──────────────────────────────────────────────────────────────┐
│  STRING = Simple key-value pair                              │
│                                                              │
│  Use Case: Cache, Counter, Session, Flags                    │
│                                                              │
│  Visual:                                                     │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Key                    Value                        │    │
│  │  ┌──────────────────┐   ┌──────────────────┐        │    │
│  │  │ user:1:name      │──▶│ "Amit"           │        │    │
│  │  │ user:1:age       │──▶│ "28"             │        │    │
│  │  │ user:1:email     │──▶│ "amit@email.com" │        │    │
│  │  │ session:abc123   │──▶│ "loggedIn:true"  │        │    │
│  │  │ counter:pageviews│──▶│ "1234"           │        │    │
│  │  └──────────────────┘   └──────────────────┘        │    │
│  └──────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────┘
```

```bash
# STRING Commands
SET user:1:name "Amit"          # Set key-value
GET user:1:name                 # Get value → "Amit"
SETEX cache:product:1 3600 '{"id":1,"name":"Laptop"}'  # Set with TTL (1 hour)
GET cache:product:1             # Get cached value
DEL user:1:name                 # Delete key
EXISTS user:1:name              # Check exists → 1 or 0
INCR counter:pageviews          # Increment by 1
INCRBY counter:pageviews 10     # Increment by 10
DECR counter:pageviews          # Decrement by 1
APPEND user:1:name " Kumar"    # Append to string
STRLEN user:1:name              # String length
```

### 2. HASH (Object Storage!)

```
┌──────────────────────────────────────────────────────────────┐
│  HASH = Key mein aur key-value pairs (Object jaisa!)          │
│                                                              │
│  Use Case: Store object fields (User, Product, etc.)         │
│                                                              │
│  Visual:                                                     │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Key: user:1                                         │    │
│  │  ┌──────────────────────────────────────────────┐    │    │
│  │  │  Field           │  Value                    │    │    │
│  │  ├──────────────────┼───────────────────────────┤    │    │
│  │  │  name            │  "Amit"                   │    │    │
│  │  │  age             │  "28"                     │    │    │
│  │  │  email           │  "amit@email.com"         │    │    │
│  │  │  city            │  "Delhi"                  │    │    │
│  │  └──────────────────┴───────────────────────────┘    │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Advantage: Ek field update karo, baaki intact rehte hain     │
└──────────────────────────────────────────────────────────────┘
```

```bash
# HASH Commands
HSET user:1 name "Amit" age 28 email "amit@email.com"  # Set multiple fields
HGET user:1 name              # Get one field → "Amit"
HGETALL user:1                # Get all fields → {name: "Amit", age: "28", ...}
HDEL user:1 age              # Delete field
HKEYS user:1                 # Get all field names
HVALS user:1                 # Get all values
HLEN user:1                  # Number of fields
HEXISTS user:1 name          # Check field exists → 1 or 0
HINCRBY user:1 age 1         # Increment field value by 1
HMGET user:1 name email      # Get multiple fields
```

### 3. LIST (Queue/Stack!)

```
┌──────────────────────────────────────────────────────────────┐
│  LIST = Ordered list of strings (doubly-linked list)         │
│                                                              │
│  Use Case: Queue, Stack, Recent items, Timeline              │
│                                                              │
│  Visual (as Queue - FIFO):                                   │
│  ┌──────────────────────────────────────────────────────┐    │
│  │                                                      │    │
│  │  LPUSH (add to left)    RPUSH (add to right)         │    │
│  │       │                       │                      │    │
│  │       ▼                       ▼                      │    │
│  │  ┌─────┬─────┬─────┬─────┬─────┐                    │    │
│  │  │  3  │  2  │  1  │  4  │  5  │                    │    │
│  │  └─────┴─────┴─────┴─────┴─────┘                    │    │
│  │  LPOP                     RPOP                      │    │
│  │  (remove from left)       (remove from right)       │    │
│  │                                                      │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Queue (FIFO): LPUSH → RPOP (First In First Out)            │
│  Stack (LIFO): LPUSH → LPOP (Last In First Out)             │
└──────────────────────────────────────────────────────────────┘
```

```bash
# LIST Commands
LPUSH queue:tasks "task1" "task2" "task3"  # Add to left
RPUSH queue:tasks "task4" "task5"          # Add to right
LPOP queue:tasks          # Remove from left → "task3"
RPOP queue:tasks          # Remove from right → "task5"
LRANGE queue:tasks 0 -1   # Get all elements
LLEN queue:tasks          # List length
LINDEX queue:tasks 0      # Get element at index 0
LSET queue:tasks 0 "new"  # Set element at index
LTRIM queue:tasks 0 9     # Keep only first 10 elements
```

### 4. SET (Unique Collection!)

```
┌──────────────────────────────────────────────────────────────┐
│  SET = Unordered collection of UNIQUE strings                │
│                                                              │
│  Use Case: Tags, Unique items, Membership, De-duplication    │
│                                                              │
│  Visual:                                                     │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Key: tags:blog:1                                     │    │
│  │  ┌──────────────────────────────────────────────┐    │    │
│  │  │  { "java", "spring", "redis", "caching" }    │    │    │
│  │  └──────────────────────────────────────────────┘    │    │
│  │                                                      │    │
│  │  SADD tags:blog:1 "java" "spring"   # Add           │    │
│  │  SADD tags:blog:1 "java"            # Duplicate! Ignored│ │
│  │  SMEMBERS tags:blog:1               # Get all       │    │
│  │  SISMEMBER tags:blog:1 "java"       # Check → 1     │    │
│  │  SCARD tags:blog:1                  # Count → 4     │    │
│  └──────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────┘
```

```bash
# SET Commands
SADD tags:blog:1 "java" "spring" "redis"    # Add members
SMEMBERS tags:blog:1                         # Get all members
SISMEMBER tags:blog:1 "java"                 # Check → 1 or 0
SCARD tags:blog:1                            # Count members
SREM tags:blog:1 "redis"                     # Remove member
SUNION set1 set2                             # Union (all unique)
SINTER set1 set2                             # Intersection (common)
SDIFF set1 set2                              # Difference (in set1 not set2)
```

### 5. SORTED SET (ZSET - Ranked!)

```
┌──────────────────────────────────────────────────────────────┐
│  SORTED SET = Set with SCORE (for ranking/sorting)           │
│                                                              │
│  Use Case: Leaderboard, Priority queue, Range queries        │
│                                                              │
│  Visual:                                                     │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Key: leaderboard:game1                               │    │
│  │  ┌──────────────────────────────────────────────┐    │    │
│  │  │  Member      │  Score                        │    │    │
│  │  ├──────────────┼───────────────────────────────┤    │    │
│  │  │  "player1"   │  1500  ← (highest score)     │    │    │
│  │  │  "player3"   │  1200                          │    │    │
│  │  │  "player2"   │  900                           │    │    │
│  │  │  "player4"   │  600   ← (lowest score)      │    │    │
│  │  └──────────────┴───────────────────────────────┘    │    │
│  │                                                      │    │
│  │  Sorted automatically by score!                      │    │
│  └──────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────┘
```

```bash
# SORTED SET Commands
ZADD leaderboard:game1 1500 "player1" 900 "player2" 1200 "player3"
ZRANGE leaderboard:game1 0 -1 WITHSCORES    # Get all sorted by score
ZREVRANGE leaderboard:game1 0 2 WITHSCORES  # Top 3 (reverse)
ZSCORE leaderboard:game1 "player1"          # Get score → 1500
ZINCRBY leaderboard:game1 100 "player1"     # Increase score by 100
ZRANK leaderboard:game1 "player1"           # Get rank (0-based)
ZREVRANK leaderboard:game1 "player1"        # Get reverse rank
ZRANGEBYSCORE leaderboard:game1 1000 2000   # Get scores in range
ZCARD leaderboard:game1                     # Count members
ZREM leaderboard:game1 "player4"            # Remove member
```

### Data Structures Summary:

```
╔══════════════════════════════════════════════════════════════════╗
║              REDIS DATA STRUCTURES                               ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  Type         │ Use Case              │ Example                 ║
║  ─────────────┼───────────────────────┼─────────────────────    ║
║  STRING       │ Cache, Counter,       │ user:1:name → "Amit"    ║
║               │ Session, Flag         │                         ║
║  HASH         │ Object fields,        │ user:1 → {name, age}    ║
║               │ Profile, Config       │                         ║
║  LIST         │ Queue, Stack,         │ queue:tasks → [1,2,3]   ║
║               │ Timeline, History     │                         ║
║  SET          │ Tags, Unique items,   │ tags → {java, spring}   ║
║               │ De-duplication        │                         ║
║  SORTED SET   │ Leaderboard, Priority,│ leaderboard → [(1,500), │ ║
║ (ZSET)        │ Range queries         │  (2,300)]               ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 📌 PART 4: CACHING STRATEGIES

### Strategy 1: Cache-Aside (Lazy Loading)

```
┌──────────────────────────────────────────────────────────────┐
│  CACHE-ASIDE (Most Common!)                                  │
│                                                              │
│  Application khud cache manage karta hai                     │
│                                                              │
│  Read Flow:                                                  │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  1. Application → Cache mein check karo              │    │
│  │  2. Cache HIT → Return cached value ✅               │    │
│  │  3. Cache MISS → Database se read                    │    │
│  │  4. Save in Cache → Return value                     │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Write Flow:                                                 │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  1. Application → Database mein write                │    │
│  │  2. Invalidate (delete) cache entry                  │    │
│  │  3. Next read will fetch fresh data                  │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Visual:                                                     │
│                                                              │
│     Read:  App → Cache → (HIT? Return)                      │
│                     │ (MISS)                                 │
│                     ▼                                        │
│                  Database → Save in Cache → Return           │
│                                                              │
│     Write: App → Database (update) → Delete Cache            │
│                                                              │
│  Pros: Simple, only fetch when needed                        │
│  Cons: Cache miss = 3 trips (App→Cache→DB→Cache→App)         │
└──────────────────────────────────────────────────────────────┘
```

```java
// Cache-Aside Implementation
@Service
public class ProductService {

    @Autowired
    private RedisTemplate<String, Product> redisTemplate;

    @Autowired
    private ProductRepository productRepository;

    public Product getProduct(Long id) {
        String key = "product:" + id;

        // Step 1: Check cache
        Product product = redisTemplate.opsForValue().get(key);

        if (product != null) {
            // Cache HIT
            return product;
        }

        // Cache MISS - Go to database
        product = productRepository.findById(id).orElse(null);

        if (product != null) {
            // Save in cache with TTL (1 hour)
            redisTemplate.opsForValue().set(key, product, 1, TimeUnit.HOURS);
        }

        return product;
    }

    public Product updateProduct(Long id, Product updated) {
        // Step 1: Update database
        Product product = productRepository.save(updated);

        // Step 2: Invalidate cache
        redisTemplate.delete("product:" + id);

        return product;
    }
}
```

### Strategy 2: Write-Through

```
┌──────────────────────────────────────────────────────────────┐
│  WRITE-THROUGH                                               │
│                                                              │
│  Har write CACHE aur DB dono mein hota hai                   │
│                                                              │
│  Write Flow:                                                 │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  1. Application → Write to Cache                     │    │
│  │  2. Cache → Write to Database (synchronously)        │    │
│  │  3. Both updated!                                    │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Read Flow:                                                  │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  1. Always read from Cache (guaranteed HIT!)         │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Visual:                                                     │
│                                                              │
│     Write: App → Cache → Database (both updated)            │
│                                                              │
│     Read:  App → Cache → Return (always HIT)                │
│                                                              │
│  Pros: Always consistent, always fast reads                  │
│  Cons: Write is slower (2 operations), cache always full     │
└──────────────────────────────────────────────────────────────┘
```

### Strategy 3: Write-Behind (Write-Back)

```
┌──────────────────────────────────────────────────────────────┐
│  WRITE-BEHIND (Async)                                        │
│                                                              │
│  Write CACHE mein hota hai, DB mein ASYNC (baad mein)        │
│                                                              │
│  Write Flow:                                                 │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  1. Application → Write to Cache                     │    │
│  │  2. Return immediately (FAST!)                       │    │
│  │  3. Background process → Write to Database (later)   │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Visual:                                                     │
│                                                              │
│     Write: App → Cache → Return (fast!)                     │
│                  │                                           │
│                  └──▶ Background → Database (later)         │
│                                                              │
│  Pros: Super fast writes, batch writes to DB                 │
│  Cons: Data loss risk if cache crashes before DB write       │
└──────────────────────────────────────────────────────────────┘
```

### Strategy 4: Read-Through

```
┌──────────────────────────────────────────────────────────────┐
│  READ-THROUGH                                                │
│                                                              │
│  Cache khud database se data load karta hai                  │
│                                                              │
│  Flow:                                                       │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  1. Application → Request from Cache                 │    │
│  │  2. Cache MISS → Cache itself queries Database       │    │
│  │  3. Cache saves data → Returns to Application        │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Difference from Cache-Aside:                                │
│  - Cache-Aside: Application handles DB fetch                │
│  - Read-Through: Cache handles DB fetch                     │
│                                                              │
│  Pros: Application simpler (doesn't handle DB)               │
│  Cons: Cache needs DB logic                                  │
└──────────────────────────────────────────────────────────────┘
```

### Caching Strategies Comparison:

```
╔══════════════════════════════════════════════════════════════════╗
║              CACHING STRATEGIES COMPARISON                      ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  Strategy      │ Read        │ Write       │ Best For            ║
║  ──────────────┼─────────────┼─────────────┼────────────────    ║
║  Cache-Aside   │ Check cache │ Update DB   │ Read-heavy apps     ║
║                │ (app logic) │ + invalidate│ (Most common!)      ║
║                                                                  ║
║  Write-Through │ Always from │ Write both  │ Consistency needed  ║
║                │ cache       │ (sync)      │                     ║
║                                                                  ║
║  Write-Behind  │ Always from │ Cache first │ Write-heavy apps    ║
║                │ cache       │ (async DB)  │ (fast writes)       ║
║                                                                  ║
║  Read-Through  │ Cache loads │ Update DB   │ Simple app logic    ║
║                │ from DB     │ + invalidate│                     ║
║                                                                  ║
║  When to use:                                                   ║
║  - Read-heavy (90% reads) → Cache-Aside                        ║
║  - Write-heavy → Write-Behind                                  ║
║  - Need consistency → Write-Through                            ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 📌 PART 5: CACHE EXPIRATION & EVICTION

### TTL (Time To Live):

```
┌──────────────────────────────────────────────────────────────┐
│  TTL = Cache entry kitni der tak rahegi                       │
│                                                              │
│  Real Life:                                                  │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Milk carton expiry date:                            │    │
│  │  "Use by 2024-01-15"                                 │    │
│  │                                                      │    │
│  │  Cache TTL:                                          │    │
│  │  "product:1 expires in 3600 seconds (1 hour)"        │    │
│  │                                                      │    │
│  │  TTL expire hone pe → Cache entry AUTO delete!       │    │
│  └──────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────┘
```

```bash
# TTL Commands
SETEX cache:user:1 3600 '{"name":"Amit"}'  # Set with 1 hour TTL
SET cache:user:1 '{"name":"Amit"}'         # Set without TTL (never expires)
EXPIRE cache:user:1 300                    # Set TTL to 5 minutes
TTL cache:user:1                          # Check remaining TTL (seconds)
PERSIST cache:user:1                      # Remove TTL (never expire)
```

### Eviction Policies:

```
┌──────────────────────────────────────────────────────────────┐
│  EVICTION = Memory full hone pe purane data delete karna      │
│                                                              │
│  Real Life:                                                  │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Tumhari shelf full hai, kuch hatana padega          │    │
│  │  Kaun hatayein? → Oldest, Least used, Random?        │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Eviction Policies:                                          │
│  ┌──────────────────────────────────────────────────────┐    │
│  │                                                      │    │
│  │  noeviction → Error when full (default for RDB)      │    │
│  │                                                      │    │
│  │  allkeys-lru → Sab keys mein se LRU evict            │    │
│  │  (Least Recently Used - jo sabse kam use hua)        │    │
│  │                                                      │    │
│  │  volatile-lru → Sirf TTL wale keys mein se LRU       │    │
│  │                                                      │    │
│  │  allkeys-lfu → Sab keys mein se LFU evict            │    │
│  │  (Least Frequently Used - jo sabse kam baar use hua) │    │
│  │                                                      │    │
│  │  allkeys-random → Random evict                       │    │
│  │                                                      │    │
│  │  volatile-ttl → Jo sabse jaldi expire hoga           │    │
│  │                                                      │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Best for cache: allkeys-lru or allkeys-lfu                  │
└──────────────────────────────────────────────────────────────┘
```

```bash
# Configure eviction policy
CONFIG SET maxmemory 256mb
CONFIG SET maxmemory-policy allkeys-lru

# Check memory
INFO memory
DBSIZE  # Number of keys
```

---

## 📌 PART 6: REDIS PATTERNS

### Pattern 1: Cache with TTL

```java
// Cache product with 1 hour TTL
@Service
public class ProductService {

    @Autowired
    private RedisTemplate<String, Product> redisTemplate;

    @Autowired
    private ProductRepository repository;

    private static final Duration CACHE_TTL = Duration.ofHours(1);

    public Product getProduct(Long id) {
        String key = "product:" + id;

        // Try cache first
        Product cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;  // Cache HIT
        }

        // Cache MISS - fetch from DB
        Product product = repository.findById(id).orElse(null);

        if (product != null) {
            // Save with TTL
            redisTemplate.opsForValue().set(key, product, CACHE_TTL);
        }

        return product;
    }
}
```

### Pattern 2: Cache Invalidation

```java
// Invalidate cache on update/delete
@Service
public class ProductService {

    public Product updateProduct(Long id, Product updated) {
        // Update DB
        Product product = repository.save(updated);

        // Invalidate cache (delete stale entry)
        redisTemplate.delete("product:" + id);

        // OR update cache directly
        // redisTemplate.opsForValue().set("product:" + id, product, CACHE_TTL);

        return product;
    }

    public void deleteProduct(Long id) {
        repository.deleteById(id);
        redisTemplate.delete("product:" + id);  // Invalidate
    }
}
```

### Pattern 3: Cache-Aside for List

```java
// Cache list of products
public List<Product> getProductsByCategory(String category) {
    String key = "products:category:" + category;

    // Check cache
    List<Product> cached = redisTemplate.opsForList().range(key, 0, -1);
    if (cached != null && !cached.isEmpty()) {
        return cached;
    }

    // Fetch from DB
    List<Product> products = repository.findByCategory(category);

    // Save to cache
    if (!products.isEmpty()) {
        redisTemplate.opsForList().leftPushAll(key, products);
        redisTemplate.expire(key, 30, TimeUnit.MINUTES);
    }

    return products;
}
```

### Pattern 4: Session Management

```java
// Store user session in Redis
@Service
public class SessionService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void createSession(String sessionId, User user) {
        String key = "session:" + sessionId;
        redisTemplate.opsForValue().set(key, user.getId().toString(), 30, TimeUnit.MINUTES);
    }

    public User getSession(String sessionId) {
        String key = "session:" + sessionId;
        String userId = redisTemplate.opsForValue().get(key);
        if (userId != null) {
            // Refresh TTL
            redisTemplate.expire(key, 30, TimeUnit.MINUTES);
            return userRepository.findById(Long.parseLong(userId)).orElse(null);
        }
        return null;
    }

    public void destroySession(String sessionId) {
        redisTemplate.delete("session:" + sessionId);
    }
}
```

### Pattern 5: Rate Limiting

```java
// Limit API calls (e.g., 100 requests per minute)
@Service
public class RateLimitService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean isAllowed(String clientId) {
        String key = "ratelimit:" + clientId;
        Long current = redisTemplate.opsForValue().increment(key);

        if (current == 1) {
            // First request - set TTL
            redisTemplate.expire(key, 60, TimeUnit.SECONDS);
        }

        return current <= 100;  // Allow up to 100 requests
    }
}
```

### Pattern 6: Distributed Lock

```java
// Prevent multiple processes from doing same work
@Service
public class LockService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean tryLock(String lockKey, String value, Duration timeout) {
        Boolean acquired = redisTemplate.opsForValue()
            .setIfAbsent(lockKey, value, timeout);
        return Boolean.TRUE.equals(acquired);
    }

    public void unlock(String lockKey) {
        redisTemplate.delete(lockKey);
    }
}

// Usage
if (lockService.tryLock("report:generate", "process1", Duration.ofMinutes(5))) {
    try {
        // Generate report
    } finally {
        lockService.unlock("report:generate");
    }
}
```

### Pattern 7: Pub/Sub (Real-time)

```java
// Publish messages to channel
@Service
public class NotificationService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void sendNotification(String userId, String message) {
        redisTemplate.convertAndSend("notifications:" + userId, message);
    }
}

// Subscribe to channel
@Component
public class NotificationSubscriber {

    @Autowired
    private RedisMessageListenerContainer container;

    @PostConstruct
    public void subscribe() {
        container.addMessageListener((message, pattern) -> {
            String msg = new String(message.getBody());
            System.out.println("Notification: " + msg);
        }, new PatternTopic("notifications:*"));
    }
}
```

---

## 📌 PART 7: SPRING BOOT + REDIS

### Dependencies:

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>  <!-- For connection pool -->
</dependency>
```

### Configuration:

```yaml
# application.yml
spring:
  redis:
    host: localhost
    port: 6379
    password:  # if password set
    timeout: 2000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
        max-wait: -1ms
```

### Redis Configuration Class:

```java
@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1))  // Default TTL 1 hour
            .serializeKeysWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build();
    }
}
```

### Using @Cacheable (Simplest!):

```java
@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    // Cache result automatically!
    @Cacheable(value = "products", key = "#id")
    public Product getProduct(Long id) {
        // This runs only on cache MISS
        // On cache HIT, returns cached value directly
        return repository.findById(id).orElse(null);
    }

    // Cache list
    @Cacheable(value = "products", key = "'all'")
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    // Update cache on update
    @CachePut(value = "products", key = "#result.id")
    public Product updateProduct(Product product) {
        return repository.save(product);
    }

    // Delete cache on delete
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }

    // Clear all cache
    @CacheEvict(value = "products", allEntries = true)
    public void clearCache() {
        // Cache cleared
    }
}
```

### @Cacheable Visual:

```
┌──────────────────────────────────────────────────────────────┐
│  @Cacheable ANNOTATION FLOW                                  │
│                                                              │
│  @Cacheable(value = "products", key = "#id")                 │
│  public Product getProduct(Long id) { ... }                  │
│                                                              │
│  Flow:                                                       │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  1. Call getProduct(1)                               │    │
│  │  2. Spring checks cache "products" with key "1"      │    │
│  │  3. Cache HIT → Return cached value (method skip!)   │    │
│  │  4. Cache MISS → Execute method → Save in cache      │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Annotations:                                                │
│  @Cacheable  → Read from cache (or execute + cache)         │
│  @CachePut   → Always execute + update cache                │
│  @CacheEvict → Remove from cache                            │
│  @Caching    → Multiple cache operations                    │
└──────────────────────────────────────────────────────────────┘
```

---

## 📌 PART 8: ADVANCED REDIS PATTERNS

### Pattern: Cache Penetration Protection

```java
// Problem: Invalid IDs hit DB every time
// Solution: Cache null values too!

public Product getProduct(Long id) {
    String key = "product:" + id;

    Product cached = redisTemplate.opsForValue().get(key);

    if (cached != null) {
        if ("NULL".equals(cached.getId().toString())) {
            return null;  // Cached null
        }
        return cached;
    }

    Product product = repository.findById(id).orElse(null);

    if (product != null) {
        redisTemplate.opsForValue().set(key, product, 1, TimeUnit.HOURS);
    } else {
        // Cache null for 5 minutes to prevent penetration
        Product nullMarker = new Product();
        nullMarker.setId(-1L);
        redisTemplate.opsForValue().set(key, nullMarker, 5, TimeUnit.MINUTES);
    }

    return product;
}
```

### Pattern: Cache Breakdown Protection

```java
// Problem: Hot key expires, many requests hit DB simultaneously
// Solution: Distributed lock (only 1 request hits DB)

public Product getProduct(Long id) {
    String key = "product:" + id;

    // Check cache
    Product cached = redisTemplate.opsForValue().get(key);
    if (cached != null) {
        return cached;
    }

    // Try to acquire lock
    String lockKey = "lock:" + key;
    boolean locked = redisTemplate.opsForValue()
        .setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);

    if (locked) {
        try {
            // Double check cache (another thread might have loaded it)
            cached = redisTemplate.opsForValue().get(key);
            if (cached != null) {
                return cached;
            }

            // Load from DB
            Product product = repository.findById(id).orElse(null);
            if (product != null) {
                redisTemplate.opsForValue().set(key, product, 1, TimeUnit.HOURS);
            }
            return product;
        } finally {
            redisTemplate.delete(lockKey);
        }
    } else {
        // Wait and retry
        Thread.sleep(50);
        return getProduct(id);  // Recursive retry
    }
}
```

### Pattern: Cache Avalanche Protection

```java
// Problem: Many keys expire at same time
// Solution: Random TTL

public void cacheProduct(Product product) {
    String key = "product:" + product.getId();

    // Random TTL between 55-65 minutes
    long baseTTL = 60;  // minutes
    long randomOffset = ThreadLocalRandom.current().nextLong(0, 10);
    long ttl = baseTTL + randomOffset;

    redisTemplate.opsForValue().set(key, product, ttl, TimeUnit.MINUTES);
}
```

### Pattern: Write-Behind Queue

```java
// Async write to DB using Redis Queue
@Service
public class WriteBehindService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ProductRepository repository;

    private static final String QUEUE_KEY = "queue:product:updates";

    // Write to cache + queue
    public void updateProduct(Product product) {
        String key = "product:" + product.getId();
        redisTemplate.opsForValue().set(key, product, 1, TimeUnit.HOURS);
        redisTemplate.opsForList().rightPush(QUEUE_KEY, toJson(product));
    }

    // Background worker (scheduled)
    @Scheduled(fixedDelay = 5000)  // Every 5 seconds
    public void processQueue() {
        String json = redisTemplate.opsForList().leftPop(QUEUE_KEY);
        while (json != null) {
            Product product = fromJson(json);
            repository.save(product);  // Write to DB
            json = redisTemplate.opsForList().leftPop(QUEUE_KEY);
        }
    }
}
```

---

## 📌 PART 9: REDIS INTERVIEW QUESTIONS (50+)

### ⭐ BASIC

**Q1: What is Redis?**
> In-memory data store used as cache, database, and message broker. Super fast (~0.1ms).

**Q2: Why use Redis over Database?**
> Redis is 100x faster (in-memory vs disk). Use Redis for cache, Database for permanent storage.

**Q3: What is Cache HIT vs MISS?**
```
HIT: Data found in cache (fast, 0.1ms)
MISS: Data not in cache, need to fetch from DB (slow, 10ms)
Goal: Maximize HIT rate (90%+)
```

**Q4: What is Cache-Aside pattern?**
> Application checks cache first. On miss, fetch from DB and save in cache.

**Q5: What are Redis data structures?**
```
STRING: Key-value pairs
HASH: Object with fields
LIST: Ordered list (queue/stack)
SET: Unique collection
SORTED SET: Ranked collection
```

**Q6: What is TTL in Redis?**
> Time To Live - seconds after which key auto-expires.

**Q7: What happens when Redis memory is full?**
> Eviction policy runs (LRU, LFU, etc.) to remove keys.

**Q8: What is LRU eviction?**
> Least Recently Used - evicts keys that were accessed least recently.

**Q9: What is the difference between SET and SETEX?**
```
SET: No TTL (never expires)
SETEX: With TTL (auto-expires)
```

**Q10: What is Redis persistence?**
> Saving Redis data to disk. Two types: RDB (snapshot) and AOF (append log).

---

### ⭐⭐ MIDDLE

**Q11: What is the difference between Cache-Aside and Write-Through?**
```
Cache-Aside: App checks cache, on miss fetches from DB
Write-Through: Writes go to both cache and DB synchronously
```

**Q12: What is Cache Invalidation?**
> Removing/updating cache when data changes to prevent stale data.

**Q13: When to use HASH vs STRING?**
```
STRING: Simple key-value (single value)
HASH: Multiple fields (object with properties)
```

**Q14: What is Cache Penetration?**
> Querying for non-existent keys, always hitting DB. Solution: Cache null values.

**Q15: What is Cache Breakdown?**
> Hot key expires, many requests hit DB simultaneously. Solution: Distributed lock.

**Q16: What is Cache Avalanche?**
> Many keys expire at same time, overwhelming DB. Solution: Random TTL.

**Q17: What is the difference between RDB and AOF?**
```
RDB: Periodic snapshots (faster, may lose recent data)
AOF: Append every write (safer, larger file)
```

**Q18: What is Redis Cluster?**
> Distributed Redis across multiple nodes for scalability and high availability.

**Q19: What is Redis Sentinel?**
> High availability solution. Monitors Redis, automatic failover if master fails.

**Q20: What is the difference between GET and MGET?**
```
GET: Get one key
MGET: Get multiple keys at once (faster)
```

**Q21: What is pipelining in Redis?**
> Send multiple commands at once (reduce network round trips).

**Q22: What is Redis Pub/Sub?**
> Publish-subscribe messaging. Send messages to channels, subscribers receive them.

**Q23: What is the difference between LPUSH and RPUSH?**
```
LPUSH: Add to left (beginning)
RPUSH: Add to right (end)
```

**Q24: What is ZSET (Sorted Set)?**
> Set with scores, auto-sorted. Used for leaderboards, range queries.

**Q25: What is the difference between SADD and SET?**
```
SADD: Add to SET (unique, no duplicates)
SET: Set STRING value (overwrite)
```

---

### ⭐⭐⭐ ADVANCED

**Q26: What are the caching strategies?**
```
Cache-Aside: App manages cache (most common)
Write-Through: Write to both cache and DB
Write-Behind: Write to cache, async to DB
Read-Through: Cache loads from DB
```

**Q27: When to use Write-Behind?**
> Write-heavy applications where speed is critical and slight data loss risk is acceptable.

**Q28: How to handle cache stampede?**
> Use distributed lock (setIfAbsent) to prevent multiple DB queries.

**Q29: What is the difference between cache-aside and read-through?**
```
Cache-Aside: Application fetches from DB on miss
Read-Through: Cache itself fetches from DB on miss
```

**Q30: What is Redis Lua scripting?**
> Execute multiple commands atomically as a script (prevents race conditions).

```lua
-- Example: Atomic increment with limit
local current = redis.call('INCR', KEYS[1])
if current == 1 then
    redis.call('EXPIRE', KEYS[1], ARGV[1])
end
return current
```

**Q31: What is Redis transaction?**
> MULTI/EXEC commands for atomic operations (not ACID like SQL).

```bash
MULTI
SET key1 value1
SET key2 value2
EXEC
```

**Q32: What is the difference between DEL and UNLINK?**
```
DEL: Synchronous delete (blocks)
UNLINK: Asynchronous delete (non-blocking, Redis 4+)
```

**Q33: What is Redis Streams?**
> Log-like data structure for message streaming (alternative to Kafka for small scale).

**Q34: How to implement rate limiting with Redis?**
```java
Long count = redisTemplate.opsForValue().increment(key);
if (count == 1) {
    redisTemplate.expire(key, 60, TimeUnit.SECONDS);
}
return count <= limit;
```

**Q35: What is distributed lock in Redis?**
```java
Boolean acquired = redisTemplate.opsForValue()
    .setIfAbsent(lockKey, "value", Duration.ofSeconds(30));
```

**Q36: What is the difference between EXISTS and GET?**
```
EXISTS: Check if key exists (returns 1 or 0)
GET: Get value (returns value or null)
```

**Q37: What is Redis MEMORY USAGE command?**
> Check memory used by a key (helpful for optimization).

**Q38: How to handle cache consistency?**
```
1. TTL-based expiration
2. Write-through (update both)
3. Cache invalidation on write
4. Event-driven invalidation (CDC)
```

**Q39: What is the best eviction policy for cache?**
```
allkeys-lru: Best for general cache
allkeys-lfu: Best for access-pattern based
volatile-lru: Best when you want to keep non-expiring keys
```

**Q40: What is Redis HyperLogLog?**
> Probabilistic data structure for counting unique items (approximate, very memory efficient).

**Q41: What is Redis Bitmap?**
> Bit-level operations for flags, user activity tracking, etc.

```bash
SETBIT user:1:login:2024-01-01 0 1  # Mark logged in
GETBIT user:1:login:2024-01-01 0     # Check login
BITCOUNT user:1:login:2024-01        # Count logins in month
```

**Q42: What is Redis Geospatial?**
> Store and query locations (nearby stores, distance calculation).

```bash
GEOADD locations 13.361389 38.115556 "Palermo"
GEORADIUS locations 15.0 37.0 200 km  # Find within 200km
```

**Q43: What is the difference between Redis and Memcached?**
```
Redis: Data structures, persistence, replication, richer features
Memcached: Simple key-value, multi-threaded, no persistence
Redis is more versatile
```

**Q44: How to monitor Redis performance?**
```bash
INFO stats     # Statistics
INFO memory    # Memory usage
SLOWLOG GET    # Slow commands
LATENCY LATEST # Latency info
```

**Q45: What is Redis pipeline performance benefit?**
> Reduces network round trips. 1000 commands: 1000 RTT vs 1 RTT.

**Q46: What are best practices for Redis caching?**
```
1. Set TTL for all cache entries
2. Use appropriate data structure
3. Implement cache invalidation
4. Monitor hit rate
5. Use connection pooling
6. Handle cache penetration/breakdown/avalanche
7. Use pipelining for batch operations
8. Choose right eviction policy
9. Keep cache keys short
10. Use Redis Cluster for scalability
```

**Q47: What is the difference between @Cacheable and manual caching?**
```
@Cacheable: Declarative, Spring manages (easier)
Manual: Programmatic, more control (flexible)
```

**Q48: What is Redis replication?**
> Master-slave replication for read scaling and high availability.

**Q49: What is Redis failover?**
> Automatic promotion of slave to master if master fails (Sentinel).

**Q50: When NOT to use Redis?**
```
- Large datasets (memory expensive)
- Complex queries (use database)
- Need strong consistency (use database)
- Binary blobs (use object storage)
```

---

## 📌 CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║                 REDIS & CACHING CHEAT SHEET                      ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  DATA STRUCTURES:                                                ║
║  STRING: SET, GET, DEL, EXPIRE, INCR                            ║
║  HASH:   HSET, HGET, HGETALL, HDEL, HINCRBY                     ║
║  LIST:   LPUSH, RPUSH, LPOP, RPOP, LRANGE                       ║
║  SET:    SADD, SMEMBERS, SISMEMBER, SREM                        ║
║  ZSET:   ZADD, ZRANGE, ZREVRANGE, ZSCORE, ZRANK                 ║
║                                                                  ║
║  CACHING STRATEGIES:                                             ║
║  Cache-Aside:   App checks cache → DB on miss (Most common)     ║
║  Write-Through: Write to both cache + DB (sync)                 ║
║  Write-Behind:  Write to cache, async DB (fast writes)          ║
║  Read-Through:  Cache loads from DB (app simpler)               ║
║                                                                  ║
║  TTL & EVICTION:                                                 ║
║  SETEX key seconds value    → Set with TTL                      ║
║  EXPIRE key seconds         → Set TTL                           ║
║  TTL key                    → Check remaining TTL               ║
║  Eviction: allkeys-lru (best for cache)                         ║
║                                                                  ║
║  PROBLEMS & SOLUTIONS:                                           ║
║  Penetration:  Cache null values                                ║
║  Breakdown:    Distributed lock (setIfAbsent)                   ║
║  Avalanche:    Random TTL                                       ║
║                                                                  ║
║  SPRING @Cacheable:                                              ║
║  @Cacheable  → Read cache (or execute + cache)                  ║
║  @CachePut   → Always execute + update cache                    ║
║  @CacheEvict → Remove from cache                                ║
║                                                                  ║
║  COMMON PATTERNS:                                                ║
║  Session management, Rate limiting, Distributed lock,           ║
║  Leaderboard (ZSET), Pub/Sub, Queue (LIST)                      ║
║                                                                  ║
║  BEST PRACTICES:                                                 ║
║  ✅ Always set TTL                                              ║
║  ✅ Implement cache invalidation                                ║
║  ✅ Monitor hit rate (target 90%+)                              ║
║  ✅ Use connection pooling                                       ║
║  ✅ Handle penetration/breakdown/avalanche                      ║
║  ✅ Use right data structure                                     ║
║  ✅ Keep keys short                                              ║
║  ✅ Use Redis Cluster for scale                                  ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: Caching, Redis, Data Structures, Strategies, Patterns, Spring Boot Integration, 50 Interview Questions*
