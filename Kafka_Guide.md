# Apache Kafka - Zero se Seekho (Complete Guide)

---

## 📌 PART 1: KAFKA KYA HAI?

### Kafka Concept:

```
┌──────────────────────────────────────────────────────────────┐
│  KAFKA = Message Broker (Messages ko bhejne ka system)        │
│                                                              │
│  Real Life Analogy:                                          │
│  ┌──────────────────────────────────────────────────────┐    │
│  │                                                      │    │
│  │  DAK SYSTEM (Post Office):                           │    │
│  │                                                      │    │
│  │  Tum (Producer) → DAkia (Kafka) → Receiver (Consumer)│    │
│  │                                                      │    │
│  │  • Letter bhejo (Message produce karo)               │    │
│  │  • Dakia collect karega (Kafka store karega)         │    │
│  │  • Receiver ko deliver hoga (Consumer consume karega)│    │
│  │                                                      │    │
│  │  Kafka = Super fast dak system! 🚀                   │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Kafka = Distributed event streaming platform                 │
│  - Messages ko store karta hai (log mein)                    │
│  - Real-time mein bhejta hai                                 │
│  - Billions of messages handle karta hai                     │
└──────────────────────────────────────────────────────────────┘
```

### Kafka vs Message Queue:

```
╔══════════════════════════════════════════════════════════════════╗
║           KAFKA vs MESSAGE QUEUE (RabbitMQ)                      ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  Feature       │ Kafka                  │ RabbitMQ               ║
║  ──────────────┼────────────────────────┼────────────────────    ║
║  Model         │ Pub/Sub (log-based)    │ Queue (message-based)  ║
║  Data          │ Persistent log         │ Temporary queue        ║
║  Retention     │ Keep messages (TTL)    │ Delete after consume   ║
║  Replay        │ ✅ Yes (re-read)       │ ❌ No                  ║
║  Throughput    │ Millions/sec           │ Thousands/sec          ║
║  Ordering      │ Per partition          │ Per queue              ║
║  Use Case      │ Event streaming        │ Task queue             ║
║                                                                  ║
║  Rule:                                                          ║
║  • High throughput + event streaming → Kafka                   ║
║  • Task queue + routing + ack → RabbitMQ                       ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 📌 PART 2: KAFKA ARCHITECTURE

### Core Components:

```
┌──────────────────────────────────────────────────────────────┐
│  KAFKA ARCHITECTURE                                          │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐    │
│  │                   PRODUCER                           │    │
│  │              (Message bhejta hai)                    │    │
│  │                      │                               │    │
│  │                      ▼                               │    │
│  └──────────────────────────────────────────────────────┘    │
│                          │                                    │
│                          ▼                                    │
│  ┌──────────────────────────────────────────────────────┐    │
│  │                  KAFKA CLUSTER                       │    │
│  │                                                      │    │
│  │   ┌─────────────────────────────────────────────┐   │    │
│  │   │              TOPIC: "orders"                 │   │    │
│  │   │  (Category/Channel for messages)             │   │    │
│  │   │                                              │   │    │
│  │   │  ┌─────────────┐  ┌─────────────┐           │   │    │
│  │   │  │ Partition 0 │  │ Partition 1 │           │   │    │
│  │   │  │ ┌───┬───┬──┐│  │ ┌───┬───┬──┐│           │   │    │
│  │   │  │ │ 0 │ 1 │ 2 ││  │ │ 0 │ 1 │ 2 ││           │   │    │
│  │   │  │ └───┴───┴──┘│  │ └───┴───┴──┘│           │   │    │
│  │   │  │   Leader     │  │   Leader    │           │   │    │
│  │   │  └─────────────┘  └─────────────┘           │   │    │
│  │   └─────────────────────────────────────────────┘   │    │
│  │                                                      │    │
│  │   ┌─────────────────────────────────────────────┐   │    │
│  │   │              TOPIC: "payments"               │   │    │
│  │   │  ┌─────────────┐  ┌─────────────┐           │   │    │
│  │   │  │ Partition 0 │  │ Partition 1 │           │   │    │
│  │   │  │ ┌───┬───┬──┐│  │ ┌───┬───┬──┐│           │   │    │
│  │   │  │ │ 0 │ 1 │ 2 ││  │ │ 0 │ 1 │ 2 ││           │   │    │
│  │   │  │ └───┴───┴──┘│  │ └───┴───┴──┘│           │   │    │
│  │   │  └─────────────┘  └─────────────┘           │   │    │
│  │   └─────────────────────────────────────────────┘   │    │
│  └──────────────────────────────────────────────────────┘    │
│                          │                                    │
│              ┌───────────┴───────────┐                       │
│              ▼                       ▼                       │
│  ┌──────────────────┐    ┌──────────────────┐                │
│  │    CONSUMER 1    │    │    CONSUMER 2    │                │
│  │ (Order Service)  │    │ (Email Service)  │                │
│  └──────────────────┘    └──────────────────┘                │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

### Components Explained:

```
╔══════════════════════════════════════════════════════════════════╗
║              KAFKA CORE CONCEPTS                                 ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  1. TOPIC = Category/Channel                                    ║
║     ┌──────────────────────────────────────────────────────┐    ║
║     │  "orders" topic → Order-related messages             │    ║
║     │  "payments" topic → Payment-related messages         │    ║
║     │  "notifications" topic → Notification messages       │    ║
║     └──────────────────────────────────────────────────────┘    ║
║                                                                  ║
║  2. PARTITION = Topic ko chunks mein baantna                    ║
║     ┌──────────────────────────────────────────────────────┐    ║
║     │  "orders" topic                                      │    ║
║     │  ├── Partition 0 (messages 0-999)                    │    ║
║     │  ├── Partition 1 (messages 1000-1999)                │    ║
║     │  └── Partition 2 (messages 2000-2999)                │    ║
║     │                                                      │    ║
║     │  Benefit: Parallelism + Scalability                  │    ║
║     └──────────────────────────────────────────────────────┘    ║
║                                                                  ║
║  3. OFFSET = Message ka unique ID (position)                    ║
║     ┌──────────────────────────────────────────────────────┐    ║
║     │  Partition 0:                                         │    ║
║     │  [0] → [1] → [2] → [3] → [4] → ...                  │    ║
║     │   ↑     ↑     ↑     ↑     ↑                          │    ║
║     │  Offset 0, 1, 2, 3, 4 (sequential)                   │    ║
║     └──────────────────────────────────────────────────────┘    ║
║                                                                  ║
║  4. PRODUCER = Message bhejne wala                              ║
║     ┌──────────────────────────────────────────────────────┐    ║
║     │  Order Service → Produces "order created" message    │    ║
║     └──────────────────────────────────────────────────────┘    ║
║                                                                  ║
║  5. CONSUMER = Message lene wala                                ║
║     ┌──────────────────────────────────────────────────────┐    ║
║     │  Email Service → Consumes "order created" → sends    │    ║
║     │  confirmation email                                  │    ║
║     └──────────────────────────────────────────────────────┘    ║
║                                                                  ║
║  6. BROKER = Kafka server                                       ║
║     ┌──────────────────────────────────────────────────────┐    ║
║     │  Stores topics and partitions                        │    ║
║     │  Multiple brokers = Kafka Cluster                    │    ║
║     └──────────────────────────────────────────────────────┘    ║
║                                                                  ║
║  7. CONSUMER GROUP = Group of consumers                         ║
║     ┌──────────────────────────────────────────────────────┐    ║
║     │  Same group → Load balancing (each gets different)   │    ║
║     │  Different groups → Each gets all messages           │    ║
║     └──────────────────────────────────────────────────────┘    ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

### Topic & Partition Visual:

```
    TOPIC: "orders" with 3 Partitions:
    ┌────────────────────────────────────────────────────────────┐
    │                                                            │
    │  ┌─────────────────┐                                      │
    │  │  Partition 0    │                                      │
    │  │  ┌───┬───┬───┬───┬───┬───┐                             │
    │  │  │ 0 │ 1 │ 2 │ 3 │ 4 │ 5 │ ← Messages (append only)  │
    │  │  └───┴───┴───┴───┴───┴───┘                             │
    │  │  Leader: Broker 1                                       │
    │  │  Replicas: Broker 1, Broker 2                           │
    │  └─────────────────┘                                      │
    │                                                            │
    │  ┌─────────────────┐                                      │
    │  │  Partition 1    │                                      │
    │  │  ┌───┬───┬───┬───┬───┐                                 │
    │  │  │ 0 │ 1 │ 2 │ 3 │ 4 │                                 │
    │  │  └───┴───┴───┴───┴───┘                                 │
    │  │  Leader: Broker 2                                       │
    │  │  Replicas: Broker 2, Broker 3                           │
    │  └─────────────────┘                                      │
    │                                                            │
    │  ┌─────────────────┐                                      │
    │  │  Partition 2    │                                      │
    │  │  ┌───┬───┬───┬───┐                                     │
    │  │  │ 0 │ 1 │ 2 │ 3 │                                     │
    │  │  └───┴───┴───┴───┘                                     │
    │  │  Leader: Broker 3                                       │
    │  │  Replicas: Broker 3, Broker 1                           │
    │  └─────────────────┘                                      │
    │                                                            │
    │  OFFSET: Har partition ka apna offset (0, 1, 2, ...)      │
    │                                                            │
    └────────────────────────────────────────────────────────────┘
```

---

## 📌 PART 3: PRODUCER

### Producer Flow:

```
┌──────────────────────────────────────────────────────────────┐
│  PRODUCER = Message bhejta hai Kafka mein                     │
│                                                              │
│  Flow:                                                       │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  1. Application → Producer                           │    │
│  │  2. Producer → Serialize message (JSON/Avro)         │    │
│  │  3. Producer → Choose partition                      │    │
│  │  4. Producer → Send to leader broker                 │    │
│  │  5. Broker → Replicate to followers                  │    │
│  │  6. Broker → Ack to producer                         │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Visual:                                                     │
│                                                              │
│    App ──▶ Producer ──▶ Kafka Broker (Partition) ──▶ Storage  │
│                              │                               │
│                              ▼                               │
│                         Ack (Success!)                       │
└──────────────────────────────────────────────────────────────┘
```

### Partition Selection:

```
┌──────────────────────────────────────────────────────────────┐
│  PRODUCER kaunsa partition choose karega?                     │
│                                                              │
│  Method 1: Round Robin (Default)                             │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Message 1 → Partition 0                             │    │
│  │  Message 2 → Partition 1                             │    │
│  │  Message 3 → Partition 2                             │    │
│  │  Message 4 → Partition 0 (cycle)                     │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Method 2: Key-based (Hash)                                  │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Key = "user123" → Hash → Partition 1               │    │
│  │  Key = "user456" → Hash → Partition 2               │    │
│  │                                                      │    │
│  │  Same key → Same partition (Ordering guarantee!)     │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Method 3: Manual (Explicit partition)                       │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Producer decides partition explicitly               │    │
│  └──────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────┘
```

### Producer Configuration:

```java
// Producer Configuration
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

// Reliability
props.put("acks", "all");  // Wait for all replicas (safe!)
props.put("retries", 3);   // Retry on failure
props.put("enable.idempotence", true);  // Prevent duplicates

// Performance
props.put("batch.size", 16384);  // Batch messages
props.put("linger.ms", 5);       // Wait to batch more
props.put("compression.type", "snappy");  // Compress

KafkaProducer<String, String> producer = new KafkaProducer<>(props);
```

### Acks Configuration:

```
╔══════════════════════════════════════════════════════════════════╗
║                    ACKS CONFIGURATION                            ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  acks = 0 (No acknowledgment)                                   ║
║  ┌──────────────────────────────────────────────────────┐       ║
║  │  Producer → Broker → Fire and forget!                │       ║
║  │  Fastest but may lose messages                       │       ║
║  └──────────────────────────────────────────────────────┘       ║
║                                                                  ║
║  acks = 1 (Leader acknowledgment)                               ║
║  ┌──────────────────────────────────────────────────────┐       ║
║  │  Producer → Leader Broker → Ack                      │       ║
║  │  Faster, but may lose if leader crashes              │       ║
║  └──────────────────────────────────────────────────────┘       ║
║                                                                  ║
║  acks = all (All replicas acknowledgment) ✅ BEST              ║
║  ┌──────────────────────────────────────────────────────┐       ║
║  │  Producer → Leader → Followers → Ack from all        │       ║
║  │  Safest but slower                                   │       ║
║  └──────────────────────────────────────────────────────┘       ║
║                                                                  ║
║  Rule: Use "all" for important data (orders, payments)          ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 📌 PART 4: CONSUMER

### Consumer Flow:

```
┌──────────────────────────────────────────────────────────────┐
│  CONSUMER = Message leti hai Kafka se                         │
│                                                              │
│  Flow:                                                       │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  1. Consumer → Subscribe to topic                    │    │
│  │  2. Consumer → Poll messages (pull-based)            │    │
│  │  3. Consumer → Process message                       │    │
│  │  4. Consumer → Commit offset (mark as processed)     │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Visual:                                                     │
│                                                              │
│    Kafka Broker ──▶ Consumer ──▶ Process ──▶ Commit Offset    │
│         │                                                      │
│         │  (Consumer decides when to read - Pull based)       │
│         ▼                                                      │
│    [0] [1] [2] [3] [4] ← Consumer reads from offset 2        │
│                          (0, 1 already consumed)              │
└──────────────────────────────────────────────────────────────┘
```

### Consumer Group:

```
┌──────────────────────────────────────────────────────────────┐
│  CONSUMER GROUP = Group of consumers                          │
│                                                              │
│  Case 1: Same Consumer Group (Load Balancing)                │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Topic "orders" (3 partitions):                      │    │
│  │                                                      │    │
│  │  Partition 0 ──── Consumer A                         │    │
│  │  Partition 1 ──── Consumer B                         │    │
│  │  Partition 2 ──── Consumer C                         │    │
│  │                                                      │    │
│  │  Each partition → ONE consumer in group              │    │
│  │  Messages distributed across consumers               │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Case 2: Different Consumer Groups (Broadcast)               │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Topic "orders" (3 partitions):                      │    │
│  │                                                      │    │
│  │  Group 1 (Email Service):                            │    │
│  │    Partition 0 ──── Consumer                         │    │
│  │    Partition 1 ──── Consumer                         │    │
│  │    Partition 2 ──── Consumer                         │    │
│  │                                                      │    │
│  │  Group 2 (Analytics Service):                        │    │
│  │    Partition 0 ──── Consumer                         │    │
│  │    Partition 1 ──── Consumer                         │    │
│  │    Partition 2 ──── Consumer                         │    │
│  │                                                      │    │
│  │  Both groups get ALL messages!                       │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Rule: Same group = Load balancing                            │
│        Different groups = Broadcast (everyone gets all)       │
└──────────────────────────────────────────────────────────────┘
```

### Consumer Group Visual:

```
    Consumer Group Assignment:
    ┌────────────────────────────────────────────────────────────┐
    │                                                            │
    │  Topic "orders" with 4 Partitions:                         │
    │                                                            │
    │  [P0] [P1] [P2] [P3]                                      │
    │   │    │    │    │                                         │
    │   │    │    │    └──────────── Consumer A                  │
    │   │    │    └────────────────── Consumer B                 │
    │   │    └──────────────────────── Consumer A                │
    │   └────────────────────────────── Consumer B               │
    │                                                            │
    │  Result:                                                   │
    │  Consumer A: P0, P2                                        │
    │  Consumer B: P1, P3                                        │
    │                                                            │
    │  ⚠️  More consumers than partitions? Extra ones idle!      │
    │  Maximum consumers = Number of partitions                  │
    │                                                            │
    └────────────────────────────────────────────────────────────┘
```

### Offset Management:

```
┌──────────────────────────────────────────────────────────────┐
│  OFFSET COMMITMENT                                           │
│                                                              │
│  Offset = Consumer ka position (kitna consume kiya)          │
│                                                              │
│  Auto-commit (default):                                      │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Consumer → Read messages → Auto commit every 5 sec  │    │
│  │  Risk: Messages may be lost if crash before commit   │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Manual commit (safer):                                      │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Consumer → Read messages → Process → Commit manually│    │
│  │  If crash before commit → Messages re-consumed       │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Commit types:                                               │
│  - auto.commit.enable=true → Auto (risky)                    │
│  - auto.commit.enable=false → Manual (safe)                  │
│                                                              │
│  At-least-once: Commit after process (may duplicate)         │
│  At-most-once: Commit before process (may lose)              │
│  Exactly-once: Special handling (complex)                    │
└──────────────────────────────────────────────────────────────┘
```

---

## 📌 PART 5: KAFKA OPERATIONS

### Topic Operations:

```bash
# Create topic
kafka-topics.sh --create \
  --bootstrap-server localhost:9092 \
  --topic orders \
  --partitions 3 \
  --replication-factor 2

# List topics
kafka-topics.sh --list --bootstrap-server localhost:9092

# Describe topic
kafka-topics.sh --describe \
  --bootstrap-server localhost:9092 \
  --topic orders

# Delete topic
kafka-topics.sh --delete \
  --bootstrap-server localhost:9092 \
  --topic orders

# Increase partitions (can't decrease!)
kafka-topics.sh --alter \
  --bootstrap-server localhost:9092 \
  --topic orders \
  --partitions 5
```

### Producer Commands:

```bash
# Start producer (console)
kafka-console-producer.sh \
  --broker-list localhost:9092 \
  --topic orders

# Type messages:
# {"id":1,"product":"Laptop","amount":50000}
# {"id":2,"product":"Phone","amount":20000}
```

### Consumer Commands:

```bash
# Start consumer (from beginning)
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic orders \
  --from-beginning

# Start consumer (new messages only)
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic orders

# Consumer group
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic orders \
  --group my-consumer-group
```

### Consumer Groups Commands:

```bash
# List consumer groups
kafka-consumer-groups.sh --list --bootstrap-server localhost:9092

# Describe consumer group
kafka-consumer-groups.sh --describe \
  --group my-consumer-group \
  --bootstrap-server localhost:9092

# Reset offsets (reprocess messages)
kafka-consumer-groups.sh --reset-offsets \
  --to-earliest \
  --group my-consumer-group \
  --topic orders \
  --execute
```

---

## 📌 PART 6: SPRING BOOT + KAFKA

### Dependencies:

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### Configuration:

```yaml
# application.yml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      acks: all
      properties:
        enable.idempotence: true
    consumer:
      group-id: my-consumer-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
```

### Producer:

```java
// Simple Producer
@Service
public class OrderProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "orders";

    public void sendOrder(Order order) {
        String message = toJson(order);

        // Send with key (for partitioning)
        kafkaTemplate.send(TOPIC, order.getId().toString(), message)
            .addCallback(
                result -> {
                    // Success
                    System.out.println("Sent to partition: " +
                        result.getRecordMetadata().partition());
                },
                ex -> {
                    // Failure
                    System.err.println("Failed: " + ex.getMessage());
                }
            );
    }
}

// Producer with JSON
@Service
public class OrderProducer {

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    public void sendOrder(Order order) {
        // Spring auto-serializes to JSON
        kafkaTemplate.send("orders", order.getId().toString(), order);
    }
}
```

### Consumer:

```java
// Simple Consumer
@Component
public class OrderConsumer {

    @KafkaListener(topics = "orders", groupId = "my-group")
    public void consume(String message) {
        System.out.println("Received: " + message);
        // Process order
    }
}

// Consumer with JSON
@Component
public class OrderConsumer {

    @KafkaListener(topics = "orders", groupId = "my-group")
    public void consume(@Payload Order order,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Header(KafkaHeaders.OFFSET) long offset) {
        System.out.println("Order: " + order);
        System.out.println("Partition: " + partition);
        System.out.println("Offset: " + offset);
    }
}

// Multiple listeners
@Component
public class OrderListeners {

    @KafkaListener(topics = "orders", groupId = "email-group")
    public void sendEmail(Order order) {
        // Send confirmation email
    }

    @KafkaListener(topics = "orders", groupId = "analytics-group")
    public void trackAnalytics(Order order) {
        // Track analytics
    }
}
```

### Producer-Consumer Flow:

```
┌──────────────────────────────────────────────────────────────┐
│  SPRING BOOT KAFKA FLOW                                      │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  PRODUCER (Order Service)                            │    │
│  │                                                      │    │
│  │  @RestController                                     │    │
│  │  public class OrderController {                      │    │
│  │      @PostMapping("/orders")                         │    │
│  │      public Order create(@RequestBody Order order) { │    │
│  │          orderService.create(order);                 │    │
│  │          kafkaTemplate.send("orders", order);  // 📤 │    │
│  │          return order;                               │    │
│  │      }                                               │    │
│  │  }                                                   │    │
│  └──────────────────────────────────────────────────────┘    │
│                          │                                    │
│                          ▼                                    │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  KAFKA TOPIC: "orders"                               │    │
│  │  [msg1] [msg2] [msg3] [msg4] ...                     │    │
│  └──────────────────────────────────────────────────────┘    │
│                    │              │                            │
│                    ▼              ▼                            │
│  ┌──────────────────────┐ ┌──────────────────────┐           │
│  │ Email Service        │ │ Analytics Service    │           │
│  │                      │ │                      │           │
│  │ @KafkaListener       │ │ @KafkaListener       │           │
│  │ sendEmail()          │ │ trackEvent()         │           │
│  │                      │ │                      │           │
│  │ Send confirmation ✉️ │ │ Store in DB 📊       │           │
│  └──────────────────────┘ └──────────────────────┘           │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

---

## 📌 PART 7: KAFKA PATTERNS

### Pattern 1: Event Sourcing

```java
// Store events, not current state
@Service
public class OrderEventService {

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void placeOrder(Order order) {
        // 1. Create event
        OrderEvent event = new OrderEvent(
            UUID.randomUUID().toString(),
            "ORDER_PLACED",
            order,
            Instant.now()
        );

        // 2. Publish event
        kafkaTemplate.send("order-events", order.getId().toString(), event);

        // 3. Event stored in Kafka (audit trail!)
    }

    public void cancelOrder(Long orderId, String reason) {
        OrderEvent event = new OrderEvent(
            UUID.randomUUID().toString(),
            "ORDER_CANCELLED",
            orderId,
            reason,
            Instant.now()
        );
        kafkaTemplate.send("order-events", orderId.toString(), event);
    }
}
```

### Pattern 2: CQRS (Command Query Responsibility Segregation)

```java
// Write side (Command) - Produces to Kafka
@Service
public class OrderCommandService {

    @Autowired
    private KafkaTemplate<String, OrderCommand> kafkaTemplate;

    public void createOrder(CreateOrderCommand cmd) {
        kafkaTemplate.send("order-commands", cmd);
        // Return immediately (async)
    }
}

// Read side (Query) - Consumes from Kafka, maintains read model
@Component
public class OrderCommandConsumer {

    @Autowired
    private OrderReadRepository readRepo;

    @KafkaListener(topics = "order-commands")
    public void handle(CreateOrderCommand cmd) {
        // Update read-optimized model
        OrderReadModel readModel = new OrderReadModel(cmd);
        readRepo.save(readModel);
    }
}

// Query side - Reads from optimized model
@Service
public class OrderQueryService {

    public OrderView getOrder(Long id) {
        return readRepo.findById(id);  // Fast read!
    }
}
```

### Pattern 3: Saga (Distributed Transactions)

```
┌──────────────────────────────────────────────────────────────┐
│  SAGA PATTERN = Distributed transaction with compensation    │
│                                                              │
│  Order Flow:                                                 │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  1. Order Service → Create Order → Publish event     │    │
│  │  2. Payment Service → Process Payment → Publish      │    │
│  │  3. Inventory Service → Reserve Stock → Publish      │    │
│  │  4. Shipping Service → Schedule Delivery → Publish   │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  If Step 3 fails:                                            │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Compensation:                                        │    │
│  │  3. Inventory → Failed!                               │    │
│  │  2. Payment → Refund (compensate)                     │    │
│  │  1. Order → Cancel (compensate)                       │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Each step publishes events to Kafka                         │
│  Each service listens and reacts                             │
└──────────────────────────────────────────────────────────────┘
```

### Pattern 4: Dead Letter Queue (DLQ)

```java
// Handle failed messages
@Component
public class OrderConsumer {

    @KafkaListener(topics = "orders")
    public void consume(Order order) {
        try {
            processOrder(order);
        } catch (Exception e) {
            // Send to DLQ for manual investigation
            kafkaTemplate.send("orders-dlq", order.getId().toString(),
                buildErrorMessage(order, e));
            throw e;  // Don't commit offset
        }
    }
}

// Monitor DLQ
@Component
public class DLQMonitor {

    @KafkaListener(topics = "orders-dlq")
    public void monitorDLQ(String errorMessage) {
        // Alert, log, or retry
        log.error("Failed message: {}", errorMessage);
        alertService.send("Kafka DLQ message!");
    }
}
```

### Pattern 5: Schema Evolution

```java
// Use Avro for schema management
// Schema Registry ensures compatibility

// V1 schema
{
  "type": "record",
  "name": "Order",
  "fields": [
    {"name": "id", "type": "string"},
    {"name": "amount", "type": "double"}
  ]
}

// V2 schema (backward compatible - added field)
{
  "type": "record",
  "name": "Order",
  "fields": [
    {"name": "id", "type": "string"},
    {"name": "amount", "type": "double"},
    {"name": "currency", "type": "string", "default": "USD"}  // NEW!
  ]
}
```

---

## 📌 PART 8: KAFKA RELIABILITY

### Delivery Semantics:

```
╔══════════════════════════════════════════════════════════════════╗
║              DELIVERY SEMANTICS                                  ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  AT-MOST-ONCE (Fastest, may lose)                               ║
║  ┌──────────────────────────────────────────────────────┐       ║
║  │  Consumer → Process → Commit (before processing)     │       ║
║  │  Risk: If crash after commit, before process → LOST  │       ║
║  └──────────────────────────────────────────────────────┘       ║
║                                                                  ║
║  AT-LEAST-ONCE (Default, may duplicate) ✅ MOST COMMON         ║
║  ┌──────────────────────────────────────────────────────┐       ║
║  │  Consumer → Process → Commit (after processing)      │       ║
║  │  Risk: If crash after process, before commit →        │       ║
║  │        Re-consumed → DUPLICATE                        │       ║
║  │  Solution: Make processing idempotent!                │       ║
║  └──────────────────────────────────────────────────────┘       ║
║                                                                  ║
║  EXACTLY-ONCE (Complex, perfect)                                ║
║  ┌──────────────────────────────────────────────────────┐       ║
║  │  Consumer → Process + Commit atomically              │       ║
║  │  Uses: Transactions + Idempotent producer            │       ║
║  │  Complex but guarantees exactly once                 │       ║
║  └──────────────────────────────────────────────────────┘       ║
║                                                                  ║
║  Rule:                                                          ║
║  • Most apps: At-least-once + Idempotent processing            ║
║  • Critical data: Exactly-once (if needed)                     ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

### Idempotent Consumer:

```java
// Prevent duplicate processing
@Component
public class IdempotentOrderConsumer {

    @Autowired
    private ProcessedMessageRepository repo;

    @KafkaListener(topics = "orders")
    public void consume(Order order) {
        // Check if already processed
        if (repo.existsByMessageId(order.getId())) {
            log.info("Duplicate message, skipping: {}", order.getId());
            return;  // Skip!
        }

        // Process
        processOrder(order);

        // Mark as processed
        repo.save(new ProcessedMessage(order.getId()));
    }
}
```

---

## 📌 PART 9: KAFKA vs OTHERS

```
╔══════════════════════════════════════════════════════════════════╗
║           KAFKA vs OTHERS                                        ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  Kafka vs RabbitMQ:                                              ║
║  ┌──────────────────────────────────────────────────────┐       ║
║  │  Kafka: Log-based, high throughput, replay,          │       ║
║  │         event streaming, persistent                  │       ║
║  │  RabbitMQ: Queue-based, routing, ack,                │       ║
║  │            task queue, temporary                     │       ║
║  └──────────────────────────────────────────────────────┘       ║
║                                                                  ║
║  Kafka vs Redis Pub/Sub:                                         ║
║  ┌──────────────────────────────────────────────────────┐       ║
║  │  Kafka: Persistent, replay, high throughput,         │       ║
║  │         consumer groups                             │       ║
║  │  Redis Pub/Sub: Temporary, no persistence,           │       ║
║  │                 simple, fast for small scale         │       ║
║  └──────────────────────────────────────────────────────┘       ║
║                                                                  ║
║  Kafka vs RabbitMQ vs Redis:                                     ║
║  ┌──────────────────────────────────────────────────────┐       ║
║  │  Use Kafka: Event streaming, log aggregation,        │       ║
║  │             real-time analytics, CDC                 │       ║
║  │  Use RabbitMQ: Task queues, RPC, routing rules       │       ║
║  │  Use Redis: Cache, session, pub/sub (small scale)    │       ║
║  └──────────────────────────────────────────────────────┘       ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 📌 PART 10: KAFKA INTERVIEW QUESTIONS (50+)

### ⭐ BASIC

**Q1: What is Kafka?**
> Distributed event streaming platform. Messages ko store aur real-time mein bhejta hai.

**Q2: What is a Topic?**
> Category/Channel for messages (like folder for messages).

**Q3: What is a Partition?**
> Topic ko chunks mein baantna for parallelism and scalability.

**Q4: What is an Offset?**
> Unique position/id of message in partition (sequential number).

**Q5: What is a Producer?**
> Application jo messages Kafka mein bhejta hai.

**Q6: What is a Consumer?**
> Application jo messages Kafka se leta hai.

**Q7: What is a Consumer Group?**
> Group of consumers jo same topic consume karte hain (load balancing).

**Q8: What is a Broker?**
> Kafka server jo topics aur partitions store karta hai.

**Q9: What is Kafka Cluster?**
> Multiple brokers working together for scalability and HA.

**Q10: What is the difference between Kafka and RabbitMQ?**
```
Kafka: Log-based, high throughput, replay, event streaming
RabbitMQ: Queue-based, routing, task queue, temporary
```

---

### ⭐⭐ MIDDLE

**Q11: What is the difference between at-least-once and exactly-once?**
```
At-least-once: May process duplicates (default)
Exactly-once: Process exactly once (complex, uses transactions)
```

**Q12: What is acks configuration?**
```
acks=0: No acknowledgment (fast, may lose)
acks=1: Leader ack (balanced)
acks=all: All replicas ack (safe, slow)
```

**Q13: What is the difference between push and pull?**
```
Push: Producer pushes to broker (Kafka producer)
Pull: Consumer pulls from broker (Kafka consumer - pull based)
```

**Q14: What is idempotent producer?**
> Prevents duplicate messages in Kafka (enable.idempotence=true).

**Q15: What is consumer offset commit?**
> Marking messages as processed (so they won't be re-consumed).

**Q16: What happens if consumer crashes?**
> Uncommitted messages are re-consumed by another consumer in group.

**Q17: What is the difference between auto-commit and manual commit?**
```
Auto: Commits periodically (may lose messages)
Manual: Commits after processing (safer, at-least-once)
```

**Q18: What is the maximum number of consumers in a group?**
> Equal to number of partitions (extra consumers will be idle).

**Q19: What is replication factor?**
> Number of copies of each partition (for fault tolerance).

**Q20: What is a leader and follower in Kafka?**
```
Leader: Handles all reads/writes for partition
Follower: Replicates from leader (backup)
```

**Q21: What is ISR (In-Sync Replicas)?**
> Replicas that are up-to-date with leader (for acks=all).

**Q22: What is the difference between Kafka and Redis Pub/Sub?**
```
Kafka: Persistent, replay, high throughput
Redis Pub/Sub: Temporary, no persistence, simple
```

**Q23: What is message ordering in Kafka?**
> Guaranteed per partition (not across partitions).

**Q24: What is the benefit of key-based partitioning?**
> Same key → Same partition → Order guaranteed for that key.

**Q25: What is Kafka Connect?**
> Framework for integrating Kafka with external systems (databases, etc.).

---

### ⭐⭐⭐ ADVANCED

**Q26: What is exactly-once semantics?**
> Using transactions + idempotent producer to ensure exactly once processing.

**Q27: What is Kafka Streams?**
> Client library for stream processing (real-time data transformation).

**Q28: What is the difference between Kafka and Flink?**
```
Kafka: Message broker (storage + delivery)
Flink: Stream processing (computation)
Often used together: Kafka → Flink → Kafka/DB
```

**Q29: What is log compaction?**
> Keep only latest value per key (for changelog topics).

**Q30: What is log retention?**
> How long to keep messages (time-based or size-based).

**Q31: What is consumer lag?**
> Difference between latest offset and consumer's current offset.

**Q32: How to handle consumer lag?**
```
1. Increase consumers (max = partitions)
2. Increase partitions
3. Optimize processing
4. Scale infrastructure
```

**Q33: What is dead letter queue (DLQ)?**
> Queue for failed messages (for manual investigation/retry).

**Q34: What is schema registry?**
> Central repository for message schemas (Avro, Protobuf).

**Q35: What is schema evolution?**
> Changing schema over time while maintaining compatibility.

**Q36: What is Kafka transactions?**
> Atomic writes across multiple partitions (for exactly-once).

**Q37: What is the difference between Kafka and Pulsar?**
```
Kafka: Mature, high throughput, simpler architecture
Pulsar: Multi-tenancy, geo-replication, tiered storage
```

**Q38: What is Kafka MirrorMaker?**
> Replicate topics across Kafka clusters (geo-replication).

**Q39: What is the difference between Kafka and EventStore?**
```
Kafka: Distributed log, high throughput
EventStore: Event sourcing database, queries, projections
```

**Q40: What is the best practice for partition count?**
```
• Start with number of consumers needed
• Plan for growth (can't decrease!)
• More partitions = more parallelism but more overhead
• Typically: 2-10 partitions per topic initially
```

**Q41: How to monitor Kafka?**
```
• Consumer lag (critical!)
• Broker metrics (CPU, memory, disk)
• Topic metrics (throughput, replication)
• Tools: Kafka Manager, Burrow, Prometheus + Grafana
```

**Q42: What is the difference between Kafka and AWS SNS/SQS?**
```
Kafka: Self-managed, high throughput, replay, complex
SNS/SQS: Managed, simple, no replay, AWS ecosystem
```

**Q43: What is the difference between Kafka and Kinesis?**
```
Kafka: Self-managed or Confluent, more features
Kinesis: AWS managed, simpler, AWS ecosystem
```

**Q44: What is Kafka security?**
```
• SSL/TLS: Encryption
• SASL: Authentication (PLAIN, SCRAM, Kerberos)
• ACL: Authorization (who can read/write)
```

**Q45: What is the difference between Kafka and Pulsar vs RocketMQ?**
```
Kafka: High throughput, log-based, streaming
Pulsar: Multi-tenant, geo-replication
RocketMQ: Transaction support, Redis-like commands
```

**Q46: What is event sourcing vs state?**
```
State: Current value only (e.g., balance = 100)
Event Sourcing: All events (deposit 50, withdraw 20, etc.)
Event Sourcing → Rebuild state from events
```

**Q47: What is CQRS with Kafka?**
```
Command side: Writes to Kafka
Query side: Reads from Kafka, maintains read model
Separate read/write optimization
```

**Q48: What is the best practice for Kafka topic design?**
```
1. Meaningful topic names (orders, payments)
2. Right partition count
3. Set retention policy
4. Use keys for ordering
5. Separate topics by domain
6. Monitor consumer lag
7. Plan for growth
```

**Q49: How to handle failed message processing?**
```
1. Retry with backoff
2. Dead letter queue (DLQ)
3. Idempotent processing
4. Alert and manual intervention
```

**Q50: What are Kafka best practices?**
```
1. Use appropriate partition count
2. Set acks=all for important data
3. Enable idempotent producer
4. Use consumer groups properly
5. Monitor consumer lag
6. Set retention policy
7. Use schema registry
8. Implement idempotent consumers
9. Handle errors with DLQ
10. Use compression for throughput
11. Batch messages for performance
12. Plan for partition growth
```

---

## 📌 CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║                    KAFKA CHEAT SHEET                             ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  CORE CONCEPTS:                                                  ║
║  Topic     → Category for messages (orders, payments)           ║
║  Partition → Topic chunks (parallelism)                         ║
║  Offset    → Message position (0, 1, 2, ...)                   ║
║  Producer  → Sends messages                                     ║
║  Consumer  → Receives messages                                  ║
║  Broker    → Kafka server                                       ║
║  Group     → Consumer group (load balancing)                    ║
║                                                                  ║
║  PRODUCER CONFIG:                                                ║
║  acks=all       → Wait for all replicas (safe)                 ║
║  retries=3      → Retry on failure                             ║
║  idempotence=true → Prevent duplicates                          ║
║  compression     → snappy/gzip (faster transfer)               ║
║                                                                  ║
║  CONSUMER CONFIG:                                                ║
║  group.id         → Consumer group name                        ║
║  auto.offset-reset → earliest/latest (where to start)           ║
║  enable.auto.commit → false (manual commit for safety)         ║
║                                                                  ║
║  DELIVERY SEMANTICS:                                             ║
║  At-most-once:  Commit before process (may lose)               ║
║  At-least-once: Commit after process (may duplicate) ✅         ║
║  Exactly-once:  Transactions (complex)                         ║
║                                                                  ║
║  SPRING BOOT:                                                    ║
║  Producer: kafkaTemplate.send(topic, key, value)                ║
║  Consumer: @KafkaListener(topics="x", groupId="y")             ║
║                                                                  ║
║  COMMANDS:                                                       ║
║  kafka-topics.sh     → Create/list/describe topics             ║
║  kafka-console-producer.sh → Send messages                     ║
║  kafka-console-consumer.sh → Receive messages                  ║
║  kafka-consumer-groups.sh  → Manage consumer groups            ║
║                                                                  ║
║  PATTERNS:                                                       ║
║  Event Sourcing → Store events, not state                      ║
║  CQRS → Separate read/write models                             ║
║  Saga → Distributed transactions with compensation             ║
║  DLQ → Dead letter queue for failed messages                   ║
║                                                                  ║
║  BEST PRACTICES:                                                 ║
║  ✅ acks=all for important data                                ║
║  ✅ Idempotent consumer (handle duplicates)                    ║
║  ✅ Monitor consumer lag                                        ║
║  ✅ Set retention policy                                        ║
║  ✅ Use keys for ordering                                       ║
║  ✅ Plan partition growth                                       ║
║  ✅ Use schema registry                                         ║
║  ✅ Handle errors with DLQ                                      ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: Kafka Architecture, Producer, Consumer, Patterns, Spring Boot Integration, 50 Interview Questions*
