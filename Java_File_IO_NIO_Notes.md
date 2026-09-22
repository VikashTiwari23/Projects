# Java File I/O & NIO - Complete Interview Notes

---

## 1. FILE I/O OVERVIEW

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Java File I/O = Reading/Writing files, directories,         │
    │                  network streams, etc.                       │
    │                                                              │
    │  2 Packages:                                                 │
    │  1. java.io   → Classic I/O (Streams, Readers/Writers)       │
    │  2. java.nio  → New I/O (Channels, Buffers, Files) - Faster │
    └──────────────────────────────────────────────────────────────┘

    File Handling Operations:
    ┌──────────────────────────────────────────────────────────────┐
    │  ✅ Create file/directory                                    │
    │  ✅ Read file                                                │
    │  ✅ Write file                                               │
    │  ✅ Delete file/directory                                    │
    │  ✅ Copy/Move file                                           │
    │  ✅ List directory contents                                  │
    │  ✅ Check file properties (size, permissions, etc.)          │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. FILE CLASS (java.io.File)

```java
import java.io.File;

// ═══════════════════════════════════════════════════════════════
// CREATING FILE OBJECT
// ═══════════════════════════════════════════════════════════════
File file = new File("data.txt");                    // Relative path
File file2 = new File("C:/Users/Amit/data.txt");    // Absolute path
File dir = new File("C:/Users/Amit/Documents");      // Directory

// ═══════════════════════════════════════════════════════════════
// CREATING FILE
// ═══════════════════════════════════════════════════════════════
boolean created = file.createNewFile();  // true if created
boolean created2 = file2.createNewFile();

// ═══════════════════════════════════════════════════════════════
// CREATING DIRECTORY
// ═══════════════════════════════════════════════════════════════
File folder = new File("myFolder");
folder.mkdir();        // Create single directory
folder.mkdirs();       // Create parent directories too

// ═══════════════════════════════════════════════════════════════
// CHECK FILE PROPERTIES
// ═══════════════════════════════════════════════════════════════
file.exists();          // true/false
file.isFile();          // true if file
file.isDirectory();     // true if directory
file.getName();         // "data.txt"
file.getAbsolutePath(); // Full path
file.getParent();       // Parent directory
file.length();          // Size in bytes
file.canRead();         // Read permission
file.canWrite();        // Write permission
file.canExecute();      // Execute permission
file.isHidden();        // Hidden file?

// ═══════════════════════════════════════════════════════════════
// LIST DIRECTORY CONTENTS
// ═══════════════════════════════════════════════════════════════
File dir = new File("C:/myFolder");
String[] files = dir.list();           // File names only
File[] files = dir.listFiles();        // File objects

// Filter files
String[] txtFiles = dir.list((d, name) -> name.endsWith(".txt"));

// ═══════════════════════════════════════════════════════════════
// DELETE / RENAME
// ═══════════════════════════════════════════════════════════════
boolean deleted = file.delete();
boolean renamed = file.renameTo(new File("newname.txt"));
```

---

## 3. CLASSIC I/O (java.io Streams)

```
    Stream Hierarchy:
    ┌──────────────────────────────────────────────────────────────┐
    │                      java.io                                  │
    │                                                              │
    │  BYTE STREAMS (for binary data):                             │
    │  ┌──────────────┐  ┌──────────────┐                         │
    │  │ InputStream   │  │ OutputStream  │                         │
    │  └──────┬───────┘  └──────┬───────┘                         │
    │         │                  │                                  │
    │  ┌──────▼───────┐  ┌──────▼───────┐                         │
    │  │FileInputStream│ │FileOutputStream│                        │
    │  │BufferedInput │ │BufferedOutput  │                        │
    │  │DataInput      │ │DataOutput      │                        │
    │  │ObjectInput    │ │ObjectOutput    │                        │
    │  └──────────────┘  └──────────────┘                         │
    │                                                              │
    │  CHARACTER STREAMS (for text data):                          │
    │  ┌──────────────┐  ┌──────────────┐                         │
    │  │ Reader        │  │ Writer        │                         │
    │  └──────┬───────┘  └──────┬───────┘                         │
    │         │                  │                                  │
    │  ┌──────▼───────┐  ┌──────▼───────┐                         │
    │  │FileReader     │ │FileWriter     │                         │
    │  │BufferedReader │ │BufferedWriter  │                        │
    │  │InputStreamRdr │ │OutputStreamWtr │                        │
    │  └──────────────┘  └──────────────┘                         │
    └──────────────────────────────────────────────────────────────┘
```

### 3.1 Byte Streams

```java
// ═══════════════════════════════════════════════════════════════
// FILE INPUT STREAM (Read bytes)
// ═══════════════════════════════════════════════════════════════
FileInputStream fis = new FileInputStream("data.txt");
int data;
while ((data = fis.read()) != -1) {  // -1 = end of file
    System.out.print((char) data);
}
fis.close();

// ═══════════════════════════════════════════════════════════════
// FILE OUTPUT STREAM (Write bytes)
// ═══════════════════════════════════════════════════════════════
FileOutputStream fos = new FileOutputStream("output.txt");
fos.write(65);          // Writes 'A'
fos.write("Hello".getBytes());
fos.close();
```

### 3.2 Buffered Streams (Faster!)

```java
// ═══════════════════════════════════════════════════════════════
// BUFFERED READER (Read text line by line - MOST USED!)
// ═══════════════════════════════════════════════════════════════
BufferedReader br = new BufferedReader(new FileReader("data.txt"));
String line;
while ((line = br.readLine()) != null) {
    System.out.println(line);
}
br.close();

// ═══════════════════════════════════════════════════════════════
// BUFFERED WRITER (Write text)
// ═══════════════════════════════════════════════════════════════
BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));
bw.write("Hello World");
bw.newLine();  // Platform-independent newline
bw.write("Second line");
bw.close();

// ═══════════════════════════════════════════════════════════════
// PRINT WRITER (Easiest for writing)
// ═══════════════════════════════════════════════════════════════
PrintWriter pw = new PrintWriter("output.txt");
pw.println("Hello World");
pw.printf("Name: %s, Age: %d", "Amit", 25);
pw.close();
```

### 3.3 Scanner (Read user input / files)

```java
// From file
Scanner scanner = new Scanner(new File("data.txt"));
while (scanner.hasNextLine()) {
    System.out.println(scanner.nextLine());
}
scanner.close();

// From System.in (keyboard)
Scanner sc = new Scanner(System.in);
String name = sc.nextLine();
int age = sc.nextInt();
```

---

## 4. JAVA NIO (java.nio)

```
    ┌──────────────────────────────────────────────────────────────┐
    │  NIO = New I/O (Java 1.4+, improved in Java 7+)             │
    │                                                              │
    │  Advantages over Classic I/O:                                │
    │  ✅ Faster (Channel + Buffer approach)                       │
    │  ✅ Non-blocking (can do other work while reading)           │
    │  ✅ Buffer-based (more efficient than stream)                │
    │  ✅ File handling easier (java.nio.file)                     │
    │  ✅ Supports memory-mapped files                             │
    └──────────────────────────────────────────────────────────────┘

    NIO Core Components:
    ┌──────────────────────────────────────────────────────────────┐
    │  1. BUFFER    → Data container (holds data temporarily)      │
    │  2. CHANNEL   │ Data highway (reads/writes to buffer)        │
    │  3. SELECTOR  │ Monitors multiple channels (non-blocking)    │
    └──────────────────────────────────────────────────────────────┘
```

### 4.1 Buffer

```java
// ═══════════════════════════════════════════════════════════════
// BUFFER TYPES
// ═══════════════════════════════════════════════════════════════
ByteBuffer buffer = ByteBuffer.allocate(1024);      // Heap buffer
ByteBuffer direct = ByteBuffer.allocateDirect(1024); // Direct buffer (faster)
CharBuffer cbuf = CharBuffer.allocate(1024);

// ═══════════════════════════════════════════════════════════════
// BUFFER OPERATIONS
// ═══════════════════════════════════════════════════════════════
buffer.put((byte) 65);          // Write to buffer
buffer.put("Hello".getBytes());

buffer.flip();                   // Switch to read mode
while (buffer.hasRemaining()) {
    byte b = buffer.get();       // Read from buffer
    System.out.print((char) b);
}

buffer.clear();                  // Reset (ready for writing again)
buffer.rewind();                 // Go back to start (for re-reading)
buffer.reset();                  // Go to marked position
buffer.mark();                   // Mark current position
```

### 4.2 Buffer Visual

```
    Buffer has 4 key properties:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  Capacity: Total buffer size (fixed)                         │
    │  Position: Current read/write position                       │
    │  Limit:    How much data is available to read                │
    │  Max:      Maximum capacity                                  │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘

    After allocate(10) and put("Hello"):
    ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐
    │ H │ e │ l │ l │ o │   │   │   │   │   │
    └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘
      0   1   2   3   4   5   6   7   8   9
                          ↑                   ↑
                      position=5          limit=10
                                        capacity=10

    After flip() (switch to read mode):
    ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐
    │ H │ e │ l │ l │ o │   │   │   │   │   │
    └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘
      0   1   2   3   4   5   6   7   8   9
      ↑                       ↑               ↑
  position=0              limit=5          capacity=10

    After clear() (ready for writing):
    ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐
    │   │   │   │   │   │   │   │   │   │   │
    └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘
      ↑                                   ↑
  position=0                      limit=capacity=10
```

### 4.3 Channel

```java
// ═══════════════════════════════════════════════════════════════
// FILE CHANNEL (Read/Write via buffer)
// ═══════════════════════════════════════════════════════════════

// Write
FileOutputStream fos = new FileOutputStream("data.txt");
FileChannel writeChannel = fos.getChannel();

ByteBuffer buffer = ByteBuffer.wrap("Hello NIO".getBytes());
writeChannel.write(buffer);
writeChannel.close();

// Read
FileInputStream fis = new FileInputStream("data.txt");
FileChannel readChannel = fis.getChannel();

ByteBuffer readBuffer = ByteBuffer.allocate(1024);
readChannel.read(readBuffer);

readBuffer.flip();  // Switch to read mode
String content = new String(readBuffer.array()).trim();
System.out.println(content);  // Hello NIO
```

### 4.4 Files (Java 7+ - NIO.2) ⭐ MOST USED

```java
import java.nio.file.*;
import java.nio.file.attribute.*;

// ═══════════════════════════════════════════════════════════════
// PATH (Represents file/directory path)
// ═══════════════════════════════════════════════════════════════
Path path = Paths.get("C:/Users/Amit/data.txt");
Path path2 = Path.of("C:/Users/Amit/data.txt");  // Java 11+

path.toAbsolutePath();     // Full path
path.getParent();          // Parent directory
path.getFileName();        // File name
path.normalize();          // Remove .. and .
path.resolve("sub/file.txt");  // Join paths

// ═══════════════════════════════════════════════════════════════
// FILES CLASS (Most useful!)
// ═══════════════════════════════════════════════════════════════

// CREATE
Files.createFile(Path.of("newfile.txt"));
Files.createDirectory(Path.of("newDir"));
Files.createDirectories(Path.of("parent/child/grandchild"));

// READ (Easiest way!)
String content = Files.readString(Path.of("data.txt"));
List<String> lines = Files.readAllLines(Path.of("data.txt"));
byte[] bytes = Files.readAllBytes(Path.of("data.txt"));

// WRITE (Easiest way!)
Files.writeString(Path.of("output.txt"), "Hello World");
Files.write(Path.of("output.txt"), "Line 1\nLine 2".getBytes());
Files.write(Path.of("list.txt"), List.of("A", "B", "C"));

// APPEND
Files.writeString(Path.of("log.txt"), "New line\n",
    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

// COPY
Files.copy(Path.of("source.txt"), Path.of("dest.txt"));
Files.copy(Path.of("source.txt"), Path.of("dest.txt"),
    StandardCopyOption.REPLACE_EXISTING);

// MOVE
Files.move(Path.of("old.txt"), Path.of("new.txt"));

// DELETE
Files.delete(Path.of("file.txt"));
Files.deleteIfExists(Path.of("maybe.txt"));

// CHECK
Files.exists(Path.of("file.txt"));
Files.isRegularFile(Path.of("file.txt"));
Files.isDirectory(Path.of("folder"));
Files.size(Path.of("file.txt"));          // Size in bytes
Files.isReadable(Path.of("file.txt"));
Files.isWritable(Path.of("file.txt"));

// LIST DIRECTORY
List<Path> files = Files.list(Path.of("C:/myDir"));  // Direct
try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of("."))) {
    for (Path file : stream) {
        System.out.println(file.getFileName());
    }
}

// WALK TREE (recursively)
Files.walk(Path.of("src"))
    .filter(Files::isRegularFile)
    .forEach(System::.out);

// FIND FILES
Files.find(Path.of("."), 10,
    (path, attrs) -> path.toString().endsWith(".java"))
    .forEach(System::out);
```

### 4.5 Files Visual

```
    Files Class Methods:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  CREATE:           createFile(), createDirectory(),          │
    │                    createDirectories()                       │
    │                                                              │
    │  READ:             readString(), readAllLines(),             │
    │                    readAllBytes()                            │
    │                                                              │
    │  WRITE:            writeString(), write()                    │
    │                                                              │
    │  COPY/MOVE:        copy(), move()                            │
    │                                                              │
    │  DELETE:           delete(), deleteIfExists()                │
    │                                                              │
    │  CHECK:            exists(), isRegularFile(), isDirectory()  │
    │                    size(), isReadable(), isWritable()        │
    │                                                              │
    │  LIST:             list(), newDirectoryStream()              │
    │                                                              │
    │  WALK/FIND:        walk(), find()                            │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 5. SERIALIZATION

```java
// ═══════════════════════════════════════════════════════════════
// WHAT IS SERIALIZATION?
// ═══════════════════════════════════════════════════════════════
// Converting Java object to byte stream (save to file / send over network)
// Deserialization = Converting byte stream back to object

import java.io.*;

class Student implements Serializable {
    private static final long serialVersionUID = 1L;  // Version control
    String name;
    int age;
    transient String password;  // Won't be serialized!

    Student(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }
}

// SERIALIZE (write object to file)
Student s1 = new Student("Amit", 25, "secret123");
ObjectOutputStream oos = new ObjectOutputStream(
    new FileOutputStream("student.ser"));
oos.writeObject(s1);
oos.close();

// DESERIALIZE (read object from file)
ObjectInputStream ois = new ObjectInputStream(
    new FileInputStream("student.ser"));
Student s2 = (Student) ois.readObject();
ois.close();

System.out.println(s2.name);     // Amit
System.out.println(s2.age);      // 25
System.out.println(s2.password); // null (transient!)
```

### 5.1 Serialization Key Points

```
    ┌──────────────────────────────────────────────────────────────┐
    │  ✅ Class must implement Serializable                        │
    │  ✅ serialVersionUID (version control)                       │
    │  ✅ All fields serialized by default                         │
    │  ✅ transient keyword = skip field (not serialized)          │
    │  ✅ static fields NOT serialized (belong to class)           │
    │  ✅ Default values restored on deserialization               │
    │  ❌ Cannot serialize: threads, sockets, connections          │
    └──────────────────────────────────────────────────────────────┘

    serialVersionUID:
    ┌──────────────────────────────────────────────────────────────┐
    │  - Unique ID for class version                               │
    │  - If class changes and serialVersionUID differs →            │
    │    InvalidClassException during deserialization              │
    │  - Always declare it explicitly!                             │
    └──────────────────────────────────────────────────────────────┘
```

---

## 6. TRY-WITH-RESOURCES

```java
// ═══════════════════════════════════════════════════════════════
// OLD WAY (Java 6 and before)
// ═══════════════════════════════════════════════════════════════
BufferedReader br = null;
try {
    br = new BufferedReader(new FileReader("data.txt"));
    String line = br.readLine();
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (br != null) {
        try {
            br.close();  // Ugly!
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// NEW WAY (Java 7+ - try-with-resources)
// ═══════════════════════════════════════════════════════════════
try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
    String line = br.readLine();
    System.out.println(line);
}  // br.close() AUTOMATICALLY called!

// Multiple resources
try (FileInputStream fis = new FileInputStream("in.txt");
     FileOutputStream fos = new FileOutputStream("out.txt")) {
    // Both auto-closed!
}
```

---

## 7. FILE I/O CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║              FILE I/O & NIO CHEAT SHEET                          ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  FILE CLASS (java.io):                                           ║
║  new File("path")                                                ║
║  exists(), isFile(), isDirectory(), getName()                    ║
║  length(), canRead(), canWrite()                                 ║
║  createNewFile(), delete(), renameTo()                           ║
║  list(), listFiles()                                             ║
║                                                                  ║
║  STREAMS (Classic I/O):                                          ║
║  InputStream/OutputStream → Bytes (binary)                       ║
║  Reader/Writer → Characters (text)                               ║
║  Buffered* → Faster (uses buffer)                                ║
║                                                                  ║
║  READING FILE:                                                   ║
║  BufferedReader br = new BufferedReader(new FileReader("f.txt")); ║
║  String line; while ((line = br.readLine()) != null) { }        ║
║                                                                  ║
║  WRITING FILE:                                                   ║
║  PrintWriter pw = new PrintWriter("f.txt");                      ║
║  pw.println("Hello");                                            ║
║                                                                  ║
║  NIO FILES (Java 7+ - BEST WAY!):                                ║
║  Files.readString(Path.of("file.txt"))                           ║
║  Files.readAllLines(Path.of("file.txt"))                         ║
║  Files.writeString(Path.of("file.txt"), "Hello")                 ║
║  Files.copy(), Files.move(), Files.delete()                      ║
║  Files.exists(), Files.size(), Files.isDirectory()               ║
║  Files.walk(), Files.find()                                      ║
║                                                                  ║
║  PATH:                                                           ║
║  Paths.get("path") / Path.of("path")                             ║
║  toAbsolutePath(), getParent(), getFileName()                    ║
║                                                                  ║
║  BUFFER:                                                         ║
║  ByteBuffer.allocate(1024)                                       ║
║  put() → flip() → get() → clear()                               ║
║                                                                  ║
║  CHANNEL:                                                        ║
║  FileChannel.read(buffer) / FileChannel.write(buffer)            ║
║                                                                  ║
║  SERIALIZATION:                                                  ║
║  implements Serializable                                         ║
║  ObjectOutputStream.writeObject()                                ║
║  ObjectInputStream.readObject()                                  ║
║  transient = don't serialize                                     ║
║  serialVersionUID = version control                              ║
║                                                                  ║
║  TRY-WITH-RESOURCES:                                             ║
║  try (Resource r = ...) { } // Auto-closed!                     ║
║                                                                  ║
║  ⚠️  Always use try-with-resources for auto-closing              ║
║  ⚠️  Use Files.* methods (NIO) instead of File class             ║
║  ⚠️  Use NIO.2 (Path, Files) for modern file operations         ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 8. FILE I/O INTERVIEW QUESTIONS (30+)

### ⭐ BASIC

**Q1: What is difference between File and Path?**
```
File:     Old (java.io), can create/delete, less features
Path:     New (java.nio), more methods, immutable, recommended
```

**Q2: What is difference between InputStream and Reader?**
```
InputStream: Reads bytes (binary data - images, files)
Reader:      Reads characters (text data - .txt, .csv)
```

**Q3: What is try-with-resources?**
> Automatically closes resources (streams, connections) after use. Introduced in Java 7. Uses AutoCloseable interface.

**Q4: What is difference between createNewFile() and createFile()?**
```
File.createNewFile():  java.io.File class
Files.createFile():    java.nio.file.Files class (NIO.2)
```

**Q5: What is the easiest way to read a file in Java 7+?**
```java
String content = Files.readString(Path.of("file.txt"));
List<String> lines = Files.readAllLines(Path.of("file.txt"));
```

**Q6: What is the easiest way to write a file in Java 7+?**
```java
Files.writeString(Path.of("file.txt"), "Hello World");
Files.write(Path.of("file.txt"), "Hello".getBytes());
```

**Q7: What is Serializable?**
> Marker interface for converting object to byte stream. Enables object to be saved to file or sent over network.

**Q8: What is transient keyword?**
> Marks field to be skipped during serialization. Field gets default value (null for objects, 0 for int) on deserialization.

**Q9: What is serialVersionUID?**
> Unique version ID for serialized class. Ensures sender and receiver have compatible class versions.

**Q10: What is difference between File and Files?**
```
File:     Old API (java.io), instance methods
Files:    New API (java.nio.file), static methods, more features
Always prefer Files (NIO.2)
```

---

### ⭐⭐ MIDDLE

**Q11: What is the difference between BufferedReader and Scanner?**
```
BufferedReader: Faster, reads line by line, no parsing
Scanner: Slower, can parse (nextInt, nextDouble), regex support
Use BufferedReader for file reading, Scanner for user input
```

**Q12: What is difference between Buffer and Channel?**
```
Buffer: Data container (holds data temporarily)
Channel: Data highway (transfers data between buffer and source)
```

**Q13: What is the difference between ByteBuffer and CharBuffer?**
```
ByteBuffer: 8-bit bytes (binary data)
CharBuffer: 16-bit chars (text data)
```

**Q14: What is the difference between flip() and clear()?**
```
flip(): Switches buffer from write mode to read mode
clear(): Resets buffer (ready for writing again, doesn't erase data)
```

**Q15: What is the difference between absolute and relative I/O?**
```
Absolute: Specifies exact position (buffer position doesn't change)
Relative: Uses current buffer position (position advances)
```

**Q16: What is the difference between NIO and Classic I/O?**
```
Classic I/O: Stream-based, blocking, character/byte streams
NIO: Channel+Buffer based, non-blocking, selector-based
NIO is faster for large files and network operations
```

**Q17: What is Memory-Mapped File?**
```
File mapped directly to memory (RAM)
Faster than regular file I/O
ByteBuffer buffer = fileChannel.map(mode, position, size);
Used for large files that need random access
```

**Q18: What is the difference between copy() with and without REPLACE_EXISTING?**
```
Without: Throws FileAlreadyExistsException if destination exists
With REPLACE_EXISTING: Overwrites existing file
```

**Q19: How to read file line by line efficiently?**
```java
// Best: Files.readAllLines()
List<String> lines = Files.readAllLines(Path.of("file.txt"));

// Or BufferedReader
try (BufferedReader br = Files.newBufferedReader(Path.of("file.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
}
```

**Q20: How to write to file without overwriting?**
```java
Files.write(Path.of("file.txt"), "Hello".getBytes(),
    StandardOpenOption.CREATE,
    StandardOpenOption.APPEND);
```

---

### ⭐⭐⭐ ADVANCED

**Q21: What is the difference between Serializable and Externalizable?**
```
Serializable: Automatic serialization (all fields)
Externalizable: Manual serialization (you control what to serialize)
Externalizable is faster but more code
```

**Q22: What happens if serialVersionUID differs during deserialization?**
> InvalidClassException thrown. Class versions are incompatible.

**Q23: What is the difference between static and transient?**
```
static: Belongs to class, NOT serialized
transient: Belongs to instance, NOT serialized
Both result in default value after deserialization
```

**Q24: What is the difference between ObjectInputStream and DataInputStream?**
```
ObjectInputStream: Reads full Java objects (deserialization)
DataInputStream: Reads primitive types (int, double, etc.)
```

**Q25: What is Channel-to-Channel transfer?**
```java
// Fast file copy (kernel-level optimization)
FileChannel src = new FileInputStream("src.txt").getChannel();
FileChannel dest = new FileOutputStream("dest.txt").getChannel();
src.transferTo(0, src.size(), dest);
// Much faster than reading/writing byte by byte
```

**Q26: What is the difference between Files.walk and Files.walkFileTree?**
```
walk(): Simple stream-based, one method for all
walkFileTree(): FileVisitor-based, more control (pre/post visit)
walk() is easier, walkFileTree() for complex operations
```

**Q27: What is Files.lines() vs Files.readAllLines()?**
```
lines(): Lazy Stream<String> - memory efficient for large files
readAllLines(): Loads entire file into List - uses more memory
Use lines() for large files
```

**Q28: What is the difference between newLine() and \n?**
```
\n: Unix newline (1 character)
newLine(): Platform-dependent (\n on Unix, \r\n on Windows)
Always use newLine() for portability
```

**Q29: What is the difference between path.toFile() and File.toPath()?**
```
Path.toFile():   Converts Path to File (backwards compatible)
File.toPath():   Converts File to Path (modern approach)
Both are for interop between old and new APIs
```

**Q30: What is the difference between StandardOpenOption values?**
```
CREATE: Create if doesn't exist
CREATE_NEW: Create, error if exists
APPEND: Add to end of file
TRUNCATE_EXISTING: Clear file if exists
WRITE: Open for writing
READ: Open for reading
```

**Q31: What is the best practice for file I/O?**
```
1. Always use try-with-resources (auto-close)
2. Use NIO.2 (Files, Path) over old File class
3. Use BufferedReader/BufferedWriter for text
4. Use Files.readAllLines() for simple reads
5. Use Files.writeString() for simple writes
6. Always handle IOException
7. Use StandardCopyOption.REPLACE_EXISTING when needed
8. Use Files.lines() for large files (lazy loading)
```

---

*Last Updated: September 2026*
*Covers: File, Streams, NIO, Buffer, Channel, Serialization, Interview Questions*
