# SQL - GROUP BY, HAVING & JOINS Complete Guide (Zero se Seekho)

---

## 📌 PART 1: GROUP BY (Detailed Guide)

### GROUP BY Kya Hai?

```
┌──────────────────────────────────────────────────────────────┐
│  GROUP BY = Rows ko GROUP mein baantna                         │
│                                                              │
│  Real Life Analogy:                                          │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Classroom mein students ko GROUP mein baanto:        │    │
│  │                                                      │    │
│  │  Group 1: Boys                                       │    │
│  │  Group 2: Girls                                      │    │
│  │  Group 3: Others                                     │    │
│  │                                                      │    │
│  │  Phir har group ka COUNT, SUM, AVG nikalo!           │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  GROUP BY = "Yeh column ke same value wale rows ko            │
│              ek group mein daalo"                             │
└──────────────────────────────────────────────────────────────┘
```

### Sample Data:

```
+----+----------+--------+---------+
| id | name     | dept   | salary  |
+----+----------+--------+---------+
|  1 | Amit     | IT     | 50000   |
|  2 | Rahul    | HR     | 45000   |
|  3 | Priya    | IT     | 60000   |
|  4 | Suresh   | Sales  | 40000   |
|  5 | Anita    | HR     | 55000   |
|  6 | Vikram   | IT     | 70000   |
|  7 | Neha     | Sales  | 42000   |
|  8 | Rajesh   | IT     | 50000   |
+----+----------+--------+---------+
```

### GROUP BY Visual:

```
    Original Table:
    +----+----------+--------+---------+
    | id | name     | dept   | salary  |
    +----+----------+--------+---------+
    |  1 | Amit     | IT     | 50000   |
    |  2 | Rahul    | HR     | 45000   |
    |  3 | Priya    | IT     | 60000   |
    |  4 | Suresh   | Sales  | 40000   |
    |  5 | Anita    | HR     | 55000   |
    |  6 | Vikram   | IT     | 70000   |
    |  7 | Neha     | Sales  | 42000   |
    |  8 | Rajesh   | IT     | 50000   |
    +----+----------+--------+---------+

    After GROUP BY dept:
    ┌─────────────────────────────────────┐
    │  IT Group:                          │
    │  +----+-------+---------+           │
    │  | 1  | Amit  | 50000   |           │
    │  | 3  | Priya | 60000   |           │
    │  | 6  | Vikram| 70000   |           │
    │  | 8  | Rajesh| 50000   |           │
    │  +----+-------+---------+           │
    │  COUNT = 4                          │
    │  SUM = 230000                       │
    │  AVG = 57500                        │
    ├─────────────────────────────────────┤
    │  HR Group:                          │
    │  +----+-------+---------+           │
    │  | 2  | Rahul | 45000   |           │
    │  | 5  | Anita | 55000   |           │
    │  +----+-------+---------+           │
    │  COUNT = 2                          │
    │  SUM = 100000                       │
    │  AVG = 50000                        │
    ├─────────────────────────────────────┤
    │  Sales Group:                       │
    │  +----+-------+---------+           │
    │  | 4  | Suresh| 40000   |           │
    │  | 7  | Neha  | 42000   |           │
    │  +----+-------+---------+           │
    │  COUNT = 2                          │
    │  SUM = 82000                        │
    │  AVG = 41000                        │
    └─────────────────────────────────────┘
```

### GROUP BY Rules:

```
╔══════════════════════════════════════════════════════════════════╗
║                    GROUP BY RULES                                ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  ✅ RULE 1: SELECT mein sirf GROUP BY column ya                 ║
║            aggregate function ho sakta hai                      ║
║                                                                  ║
║  ❌ WRONG:                                                       ║
║     SELECT name, dept FROM employee GROUP BY dept;               ║
║     (name GROUP BY mein nahi hai!)                              ║
║                                                                  ║
║  ✅ RIGHT:                                                       ║
║     SELECT dept, COUNT(*) FROM employee GROUP BY dept;           ║
║                                                                  ║
║  ✅ RULE 2: Multiple columns group kar sakte ho                 ║
║     SELECT dept, salary FROM employee GROUP BY dept, salary;     ║
║                                                                  ║
║  ✅ RULE 3: NULL bhi ek group banta hai                         ║
║                                                                  ║
║  ✅ RULE 4: GROUP BY ke baad HAVING se filter karo              ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

### Query Execution Order:

```
    SQL Query Execution Order:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  1. FROM        → Table select karo                         │
    │       │                                                      │
    │       ▼                                                      │
    │  2. WHERE       → Rows filter karo (pehle)                  │
    │       │                                                      │
    │       ▼                                                      │
    │  3. GROUP BY    → Rows ko groups mein baanto                │
    │       │                                                      │
    │       ▼                                                      │
    │  4. HAVING      → Groups filter karo (baad mein)            │
    │       │                                                      │
    │       ▼                                                      │
    │  5. SELECT      → Columns select karo                       │
    │       │                                                      │
    │       ▼                                                      │
    │  6. ORDER BY    → Sort karo                                 │
    │       │                                                      │
    │       ▼                                                      │
    │  7. LIMIT       → Kitne rows chahiye                        │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

### GROUP BY Questions:

**Q1: Department-wise employee count**
```sql
SELECT dept, COUNT(*) as emp_count
FROM employee
GROUP BY dept;

-- Result:
-- +-------+-----------+
-- | dept  | emp_count |
-- +-------+-----------+
-- | IT    |    4      |
-- | HR    |    2      |
-- | Sales |    2      |
-- +-------+-----------+
```

**Q2: Department-wise average salary**
```sql
SELECT dept, AVG(salary) as avg_salary
FROM employee
GROUP BY dept;
```

**Q3: Department-wise total salary**
```sql
SELECT dept, SUM(salary) as total_salary
FROM employee
GROUP BY dept;
```

**Q4: Department-wise max aur min salary**
```sql
SELECT dept,
       MAX(salary) as max_salary,
       MIN(salary) as min_salary
FROM employee
GROUP BY dept;
```

**Q5: Multiple columns GROUP BY**
```sql
-- Department + salary combination
SELECT dept, salary, COUNT(*) as count
FROM employee
GROUP BY dept, salary;
```

**Q6: Department-wise average salary with employee count**
```sql
SELECT dept,
       COUNT(*) as emp_count,
       AVG(salary) as avg_salary
FROM employee
GROUP BY dept;
```

**Q7: Sirf IT department ka average salary**
```sql
-- WHERE + GROUP BY
SELECT dept, AVG(salary) as avg_salary
FROM employee
WHERE dept = 'IT'
GROUP BY dept;
```

**Q8: Department-wise salary greater than 45000 wale count**
```sql
-- WHERE (row filter) + GROUP BY
SELECT dept, COUNT(*) as count
FROM employee
WHERE salary > 45000
GROUP BY dept;
```

**Q9: GROUP BY with ORDER BY**
```sql
-- Department-wise total salary, descending order
SELECT dept, SUM(salary) as total_salary
FROM employee
GROUP BY dept
ORDER BY total_salary DESC;
```

**Q10: GROUP BY with LIMIT**
```sql
-- Top 2 departments by employee count
SELECT dept, COUNT(*) as emp_count
FROM employee
GROUP BY dept
ORDER BY emp_count DESC
LIMIT 2;
```

**Q11: Departments jahan 2 se zyada employees hain**
```sql
-- GROUP BY + HAVING (baad mein)
SELECT dept, COUNT(*) as emp_count
FROM employee
GROUP BY dept
HAVING COUNT(*) > 2;
```

**Q12: Average salary 50000 se zyada wale departments**
```sql
SELECT dept, AVG(salary) as avg_salary
FROM employee
GROUP BY dept
HAVING AVG(salary) > 50000;
```

**Q13: GROUP BY with multiple aggregates**
```sql
SELECT dept,
       COUNT(*) as emp_count,
       SUM(salary) as total_salary,
       AVG(salary) as avg_salary,
       MAX(salary) as max_salary,
       MIN(salary) as min_salary
FROM employee
GROUP BY dept;
```

**Q14: GROUP BY expression**
```sql
-- Salary range ke hisaab se group
SELECT
    CASE
        WHEN salary >= 60000 THEN 'High'
        WHEN salary >= 45000 THEN 'Medium'
        ELSE 'Low'
    END as salary_band,
    COUNT(*) as emp_count
FROM employee
GROUP BY
    CASE
        WHEN salary >= 60000 THEN 'High'
        WHEN salary >= 45000 THEN 'Medium'
        ELSE 'Low'
    END;
```

---

## 📌 PART 2: HAVING (Detailed Guide)

### HAVING vs WHERE

```
┌──────────────────────────────────────────────────────────────┐
│  WHERE  = Rows filter karta hai (GROUP BY se PEHLE)           │
│  HAVING = Groups filter karta hai (GROUP BY ke BAAD)          │
│                                                              │
│  Real Life:                                                  │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  WHERE: "Sirf IT department wale students nikalo"    │    │
│  │          (Group banane se pehle filter)               │    │
│  │                                                      │    │
│  │  HAVING: "Un groups mein se jahan 5 se zyada ho"     │    │
│  │           (Group banane ke baad filter)               │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  Execution:                                                  │
│  WHERE → GROUP BY → HAVING → SELECT → ORDER BY              │
└──────────────────────────────────────────────────────────────┘
```

### WHERE vs HAVING Visual:

```
    WHERE (Before GROUP BY):
    ┌────────────────────────────────────────────────────────────┐
    │  Query: SELECT dept FROM employee                          │
    │         WHERE salary > 45000                               │
    │         GROUP BY dept                                      │
    │                                                            │
    │  Step 1: WHERE filter (rows):                              │
    │  +----+----------+--------+---------+                      │
    │  | 1 | Amit     | IT     | 50000   | ✓ (45000 se zyada)   │
    │  | 2 | Rahul    | HR     | 45000   | ✗ (45000 nahi zyada) │
    │  | 3 | Priya    | IT     | 60000   | ✓                    │
    │  | 4 | Suresh   | Sales  | 40000   | ✗                    │
    │  | 5 | Anita    | HR     | 55000   | ✓                    │
    │  | 6 | Vikram   | IT     | 70000   | ✓                    │
    │  | 7 | Neha     | Sales  | 42000   | ✗                    │
    │  | 8 | Rajesh   | IT     | 50000   | ✓                    │
    │  +----+----------+--------+---------+                      │
    │  Result rows: 1, 3, 5, 6, 8                                │
    │                                                            │
    │  Step 2: GROUP BY dept:                                    │
    │  IT: [1, 3, 6, 8] → COUNT = 4                             │
    │  HR: [5] → COUNT = 1                                       │
    │                                                            │
    │  Final Result:                                             │
    │  +------+-----------+                                      │
    │  | dept | emp_count |                                      │
    │  +------+-----------+                                      │
    │  | IT   |     4     |                                      │
    │  | HR   |     1     |                                      │
    │  +------+-----------+                                      │
    └────────────────────────────────────────────────────────────┘

    HAVING (After GROUP BY):
    ┌────────────────────────────────────────────────────────────┐
    │  Query: SELECT dept, COUNT(*) FROM employee                │
    │         GROUP BY dept                                      │
    │         HAVING COUNT(*) > 1                                │
    │                                                            │
    │  Step 1: GROUP BY dept:                                    │
    │  +-------+-----------+                                     │
    │  | IT    |     4     | ← Groups ban gaye                  │
    │  | HR    |     2     |                                     │
    │  | Sales |     2     |                                     │
    │  +-------+-----------+                                     │
    │                                                            │
    │  Step 2: HAVING filter (groups):                           │
    │  +-------+-----------+                                     │
    │  | IT    |     4     | ✓ (4 > 1)                          │
    │  | HR    |     2     | ✓ (2 > 1)                          │
    │  | Sales |     2     | ✓ (2 > 1)                          │
    │  +-------+-----------+                                     │
    │                                                            │
    │  (Agar HAVING COUNT(*) > 2 hota, toh sirf IT aata)        │
    └────────────────────────────────────────────────────────────┘
```

### HAVING Questions:

**Q1: Departments jahan 2 se zyada employees hain**
```sql
SELECT dept, COUNT(*) as emp_count
FROM employee
GROUP BY dept
HAVING COUNT(*) >= 2;
```

**Q2: Average salary 50000 se zyada wale departments**
```sql
SELECT dept, AVG(salary) as avg_salary
FROM employee
GROUP BY dept
HAVING AVG(salary) > 50000;
```

**Q3: Total salary 100000 se zyada wale departments**
```sql
SELECT dept, SUM(salary) as total_salary
FROM employee
GROUP BY dept
HAVING SUM(salary) > 100000;
```

**Q4: WHERE + HAVING dono use karo**
```sql
-- WHERE: salary > 45000 filter (rows)
-- HAVING: COUNT > 1 filter (groups)
SELECT dept, COUNT(*) as emp_count
FROM employee
WHERE salary > 45000
GROUP BY dept
HAVING COUNT(*) > 1;
```

**Q5: Multiple HAVING conditions**
```sql
SELECT dept,
       COUNT(*) as emp_count,
       AVG(salary) as avg_salary
FROM employee
GROUP BY dept
HAVING COUNT(*) >= 2
   AND AVG(salary) > 45000;
```

**Q6: Departments jahan max salary 60000 se zyada hai**
```sql
SELECT dept, MAX(salary) as max_salary
FROM employee
GROUP BY dept
HAVING MAX(salary) > 60000;
```

**Q7: Departments jahan min salary 45000 se kam hai**
```sql
SELECT dept, MIN(salary) as min_salary
FROM employee
GROUP BY dept
HAVING MIN(salary) < 45000;
```

**Q8: WHERE + GROUP BY + HAVING + ORDER BY**
```sql
-- Complete query
SELECT dept,
       COUNT(*) as emp_count,
       AVG(salary) as avg_salary
FROM employee
WHERE salary > 40000          -- Step 1: Filter rows
GROUP BY dept                  -- Step 2: Group
HAVING COUNT(*) >= 2           -- Step 3: Filter groups
ORDER BY avg_salary DESC;      -- Step 4: Sort
```

**Q9: HAVING with aggregate function in SELECT**
```sql
-- Show aggregate in result AND filter by it
SELECT dept,
       COUNT(*) as emp_count,
       AVG(salary) as avg_salary,
       SUM(salary) as total_salary
FROM employee
GROUP BY dept
HAVING AVG(salary) > 45000
   AND COUNT(*) > 1;
```

**Q10: HAVING without aggregate (using column alias)**
```sql
-- Some SQL support alias in HAVING
SELECT dept, COUNT(*) as emp_count
FROM employee
GROUP BY dept
HAVING emp_count > 2;  -- MySQL allows this
```

---

### WHERE vs HAVING - Quick Comparison:

```
╔══════════════════════════════════════════════════════════════╗
║              WHERE vs HAVING                                 ║
╠══════════════════════════════════════════════════════════════╣
║                                                              ║
║  Feature        │ WHERE           │ HAVING                   ║
║  ───────────────┼─────────────────┼─────────────────────     ║
║  Works on       │ Individual rows │ Groups                   ║
║  Timing         │ Before GROUP BY │ After GROUP BY           ║
║  Aggregate?     │ ❌ No           │ ✅ Yes                   ║
║  Performance    │ Faster          │ Slower                   ║
║  Usage          │ Filter data     │ Filter groups            ║
║                                                              ║
║  Example:                                                    ║
║  WHERE:  WHERE salary > 40000                                ║
║  HAVING: HAVING COUNT(*) > 2                                 ║
║                                                              ║
║  Can use aggregate in HAVING?  ✅ Yes                        ║
║  Can use aggregate in WHERE?   ❌ No (use HAVING)            ║
║                                                              ║
╚══════════════════════════════════════════════════════════════╝
```

---

## 📌 PART 3: JOINS (Complete Guide)

### JOIN Kya Hai?

```
┌──────────────────────────────────────────────────────────────┐
│  JOIN = Do tables ko combine karna                            │
│                                                              │
│  Real Life:                                                  │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  Tumhara ID card (Employee Table)                    │    │
│  │  Office ka directory (Department Table)              │    │
│  │                                                      │    │
│  │  JOIN = ID card se directory mein info dhundho        │    │
│  │  "Amit ka department kya hai?"                       │    │
│  │  → Employee table se Amit dhundho                    │    │
│  │  → Department table se uska dept name dhundho        │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  JOIN = "Common column se do tables ko jodo"                 │
└──────────────────────────────────────────────────────────────┘
```

### Sample Tables:

**Employee Table:**
```
+----+----------+---------+--------+
| id | name     | dept_id | salary |
+----+----------+---------+--------+
|  1 | Amit     |    1    | 50000  |
|  2 | Rahul    |    2    | 45000  |
|  3 | Priya    |    1    | 60000  |
|  4 | Suresh   |    3    | 40000  |
|  5 | Anita    |    2    | 55000  |
|  6 | Vikram   |    1    | 70000  |
|  7 | Neha     |    4    | 42000  |
|  8 | Rajesh   |    5    | 50000  |
+----+----------+---------+--------+
```

**Department Table:**
```
+----+-----------+------------+
| id | dept_name | location   |
+----+-----------+------------+
|  1 | IT        | Bangalore  |
|  2 | HR        | Mumbai     |
|  3 | Sales     | Delhi      |
|  4 | Marketing | Pune       |
|  5 | Finance   | Chennai    |
|  6 | Admin     | Kolkata    |
+----+-----------+------------+
```

---

### 3.1 INNER JOIN

```
┌──────────────────────────────────────────────────────────────┐
│  INNER JOIN = Sirf MATCHING records do                       │
│                                                              │
│  Venn Diagram:                                               │
│       ┌─────────┐     ┌─────────┐                            │
│       │ Employee│     │Department│                           │
│       │   ┌─────┼─────┼────┐   │                            │
│       │   │     │█████│    │   │  ████ = Match              │
│       │   └─────┼─────┼────┘   │                            │
│       └─────────┘     └─────────┘                            │
│                                                              │
│  Result: Sirf wo rows jahan dono tables mein match ho        │
└──────────────────────────────────────────────────────────────┘
```

```sql
-- INNER JOIN
SELECT e.name, d.dept_name, d.location
FROM employee e
INNER JOIN department d ON e.dept_id = d.id;

-- Result:
-- +--------+-----------+------------+
-- | name   | dept_name | location   |
-- +--------+-----------+------------+
-- | Amit   | IT        | Bangalore  |
-- | Rahul  | HR        | Mumbai     |
-- | Priya  | IT        | Bangalore  |
-- | Suresh | Sales     | Delhi      |
-- | Anita  | HR        | Mumbai     |
-- | Vikram | IT        | Bangalore  |
-- | Neha   | Marketing | Pune       |
-- | Rajesh | Finance   | Chennai    |
-- +--------+-----------+------------+
```

**INNER JOIN Questions:**

**Q1: Har employee ka department name**
```sql
SELECT e.name, d.dept_name
FROM employee e
INNER JOIN department d ON e.dept_id = d.id;
```

**Q2: Department-wise employee count (JOIN + GROUP BY)**
```sql
SELECT d.dept_name, COUNT(e.id) as emp_count
FROM department d
INNER JOIN employee e ON d.id = e.dept_id
GROUP BY d.dept_name;
```

**Q3: Department-wise total salary**
```sql
SELECT d.dept_name, SUM(e.salary) as total_salary
FROM department d
INNER JOIN employee e ON d.id = e.dept_id
GROUP BY d.dept_name;
```

**Q4: Aise employees dikhao jinka department Bangalore mein hai**
```sql
SELECT e.name, d.dept_name, d.location
FROM employee e
INNER JOIN department d ON e.dept_id = d.id
WHERE d.location = 'Bangalore';
```

**Q5: Department jahan average salary 50000 se zyada hai**
```sql
SELECT d.dept_name, AVG(e.salary) as avg_salary
FROM department d
INNER JOIN employee e ON d.id = e.dept_id
GROUP BY d.dept_name
HAVING AVG(e.salary) > 50000;
```

---

### 3.2 LEFT JOIN (LEFT OUTER JOIN)

```
┌──────────────────────────────────────────────────────────────┐
│  LEFT JOIN = SAARE left table records + matching right       │
│                                                              │
│  Venn Diagram:                                               │
│       ┌─────────┐     ┌─────────┐                            │
│       │ Employee│     │Department│                           │
│       │ ████████│─────│    █    │                            │
│       │ ████████│     │    █    │  ████ = Left all           │
│       └─────────┘     └─────────┘        + Matching          │
│                                                              │
│  Result:                                                     │
│  - Sab employee records (chahe department ho ya na ho)        │
│  - Agar department match nahi → NULL                         │
└──────────────────────────────────────────────────────────────┘
```

```sql
-- LEFT JOIN
SELECT e.name, d.dept_name
FROM employee e
LEFT JOIN department d ON e.dept_id = d.id;

-- Note: Department table mein Admin (id=6) hai
--       but koi employee Admin mein nahi hai
--       Admin LEFT JOIN mein NAHI aayega
--       (Kyunki Admin employee table mein match nahi karta)
```

**LEFT JOIN Questions:**

**Q6: Aise employees dikhao jinka koi department NAHI hai**
```sql
-- LEFT JOIN + IS NULL
SELECT e.name, e.dept_id
FROM employee e
LEFT JOIN department d ON e.dept_id = d.id
WHERE d.id IS NULL;

-- Agar kisi employee ka dept_id = 99 (department table mein nahi)
-- toh woh aayega with NULL
```

**Q7: Aise departments dikhao jahan koi employee NAHI hai**
```sql
-- Reverse: Department LEFT JOIN Employee
SELECT d.dept_name
FROM department d
LEFT JOIN employee e ON d.id = e.dept_id
WHERE e.id IS NULL;

-- Result: Admin (kyunki Admin mein koi employee nahi)
```

**Q8: LEFT JOIN with GROUP BY**
```sql
-- Har department ka employee count (0 bhi dikhao)
SELECT d.dept_name, COUNT(e.id) as emp_count
FROM department d
LEFT JOIN employee e ON d.id = e.dept_id
GROUP BY d.dept_name;

-- Admin ka emp_count = 0 aayega
```

**Q9: LEFT JOIN + WHERE condition**
```sql
-- Sirf IT department ke employees with their details
SELECT e.name, d.dept_name, d.location
FROM employee e
LEFT JOIN department d ON e.dept_id = d.id
WHERE d.dept_name = 'IT';
```

**Q10: LEFT JOIN with aggregate**
```sql
-- Department-wise average salary (0 employees wale bhi)
SELECT d.dept_name,
       COUNT(e.id) as emp_count,
       AVG(e.salary) as avg_salary
FROM department d
LEFT JOIN employee e ON d.id = e.dept_id
GROUP BY d.dept_name;
```

---

### 3.3 RIGHT JOIN (RIGHT OUTER JOIN)

```
┌──────────────────────────────────────────────────────────────┐
│  RIGHT JOIN = SAARE right table records + matching left      │
│                                                              │
│  Venn Diagram:                                               │
│       ┌─────────┐     ┌─────────┐                            │
│       │ Employee│     │Department│                           │
│       │    █    │─────│█████████│                            │
│       │    █    │     │█████████│  ████ = Right all          │
│       └─────────┘     └─────────┘        + Matching          │
│                                                              │
│  Result:                                                     │
│  - Sab department records (chahe employee ho ya na ho)        │
│  - Agar employee match nahi → NULL                           │
└──────────────────────────────────────────────────────────────┘
```

```sql
-- RIGHT JOIN
SELECT e.name, d.dept_name
FROM employee e
RIGHT JOIN department d ON e.dept_id = d.id;

-- Result includes:
-- - All departments (including Admin with no employee)
-- - Employees with NULL department (if any)
```

**RIGHT JOIN Questions:**

**Q11: Right JOIN se saare departments dikhao**
```sql
-- Equivalent to: FROM department LEFT JOIN employee
SELECT d.dept_name, e.name
FROM employee e
RIGHT JOIN department d ON e.dept_id = d.id;
```

**Q12: Departments without employees (RIGHT JOIN method)**
```sql
SELECT d.dept_name
FROM employee e
RIGHT JOIN department d ON e.dept_id = d.id
WHERE e.id IS NULL;

-- Result: Admin
```

**Q13: RIGHT JOIN with aggregate**
```sql
SELECT d.dept_name, COUNT(e.id) as emp_count
FROM employee e
RIGHT JOIN department d ON e.dept_id = d.id
GROUP BY d.dept_name;
```

**Q14: Compare LEFT vs RIGHT**
```sql
-- LEFT JOIN (from employee perspective)
SELECT e.name, d.dept_name
FROM employee e
LEFT JOIN department d ON e.dept_id = d.id;

-- RIGHT JOIN (from department perspective)
SELECT e.name, d.dept_name
FROM employee e
RIGHT JOIN department d ON e.dept_id = d.id;

-- LEFT JOIN gives: All employees
-- RIGHT JOIN gives: All departments
```

---

### 3.4 FULL OUTER JOIN

```
┌──────────────────────────────────────────────────────────────┐
│  FULL OUTER JOIN = SAARE records dono tables se              │
│                                                              │
│  Venn Diagram:                                               │
│       ┌─────────┐     ┌─────────┐                            │
│       │ Employee│█████│Department│                           │
│       │ ████████│█████│█████████│                           │
│       │ ████████│█████│█████████│  ████ = Everything         │
│       └─────────┘     └─────────┘                            │
│                                                              │
│  Result:                                                     │
│  - Sab employee records                                      │
│  - Sab department records                                    │
│  - Non-matching → NULL                                      │
└──────────────────────────────────────────────────────────────┘
```

```sql
-- FULL OUTER JOIN (MySQL mein direct nahi hai)
-- MySQL workaround:
SELECT e.name, d.dept_name
FROM employee e
LEFT JOIN department d ON e.dept_id = d.id
UNION
SELECT e.name, d.dept_name
FROM employee e
RIGHT JOIN department d ON e.dept_id = d.id;

-- PostgreSQL/SQL Server:
-- SELECT e.name, d.dept_name
-- FROM employee e
-- FULL OUTER JOIN department d ON e.dept_id = d.id;
```

**FULL OUTER JOIN Questions:**

**Q15: Saare records dono taraf se**
```sql
-- MySQL workaround
SELECT e.name, d.dept_name
FROM employee e
LEFT JOIN department d ON e.dept_id = d.id
UNION
SELECT e.name, d.dept_name
FROM employee e
RIGHT JOIN department d ON e.dept_id = d.id;
```

**Q16: Non-matching records dono taraf se**
```sql
-- Employees without department OR departments without employees
SELECT e.name, d.dept_name
FROM employee e
LEFT JOIN department d ON e.dept_id = d.id
WHERE d.id IS NULL
UNION
SELECT e.name, d.dept_name
FROM employee e
RIGHT JOIN department d ON e.dept_id = d.id
WHERE e.id IS NULL;
```

---

### 3.5 SELF JOIN

```
┌──────────────────────────────────────────────────────────────┐
│  SELF JOIN = Table ko APNE SAATH join karo                   │
│                                                              │
│  Use case: Employee aur unka Manager                         │
│                                                              │
│  Employee Table:                                             │
│  +----+----------+------------+                              │
│  | id | name     | manager_id |                              │
│  +----+----------+------------+                              │
│  |  1 | Raj      | NULL       |  ← Boss (no manager)        │
│  |  2 | Amit     | 1          |  ← Raj is manager           │
│  |  3 | Priya    | 1          |                              │
│  |  4 | Rahul    | 2          |  ← Amit is manager          │
│  |  5 | Suresh   | 2          |                              │
│  +----+----------+------------+                              │
│                                                              │
│  Visual:                                                     │
│  Employee (e)          Manager (m)                           │
│  +----+--------+       +----+--------+                       │
│  | 1  | Raj    |──────▶| NULL | (none) |                     │
│  | 2  | Amit   |──────▶| 1    | Raj    |                     │
│  | 3  | Priya  |──────▶| 1    | Raj    |                     │
│  | 4  | Rahul  |──────▶| 2    | Amit   |                     │
│  | 5  | Suresh |──────▶| 2    | Amit   |                     │
│  +----+--------+       +----+--------+                       │
└──────────────────────────────────────────────────────────────┘
```

**SELF JOIN Questions:**

**Q17: Har employee aur uska manager**
```sql
SELECT e.name as employee, m.name as manager
FROM employee e
LEFT JOIN employee m ON e.manager_id = m.id;

-- Result:
-- +----------+----------+
-- | employee | manager  |
-- +----------+----------+
-- | Raj      | NULL     |
-- | Amit     | Raj      |
-- | Priya    | Raj      |
-- | Rahul    | Amit     |
-- | Suresh   | Amit     |
-- +----------+----------+
```

**Q18: Employees jinka manager ki salary unse zyada hai**
```sql
SELECT e.name as employee, e.salary as emp_salary,
       m.name as manager, m.salary as mgr_salary
FROM employee e
JOIN employee m ON e.manager_id = m.id
WHERE e.salary < m.salary;
```

**Q19: Manager-wise employee count**
```sql
SELECT m.name as manager, COUNT(e.id) as team_size
FROM employee e
JOIN employee m ON e.manager_id = m.id
GROUP BY m.name;
```

**Q20: Boss level employees (jinka manager NULL hai)**
```sql
SELECT name
FROM employee
WHERE manager_id IS NULL;
```

---

### 3.6 CROSS JOIN

```
┌──────────────────────────────────────────────────────────────┐
│  CROSS JOIN = Cartesian product (har row har row ke saath)   │
│                                                              │
│  Employee: 3 rows, Department: 4 rows                        │
│  CROSS JOIN = 3 × 4 = 12 rows                               │
│                                                              │
│  Visual:                                                     │
│  E1 × D1, E1 × D2, E1 × D3, E1 × D4                        │
│  E2 × D1, E2 × D2, E2 × D3, E2 × D4                        │
│  E3 × D1, E3 × D2, E3 × D3, E3 × D4                        │
│                                                              │
│  Use case: Generate all combinations                         │
└──────────────────────────────────────────────────────────────┘
```

**CROSS JOIN Questions:**

**Q21: Har employee har department ke saath**
```sql
SELECT e.name, d.dept_name
FROM employee e
CROSS JOIN department d;
```

**Q22: All possible combinations (sizes × colors)**
```sql
-- Product combinations
SELECT s.size, c.color
FROM sizes s
CROSS JOIN colors c;
```

---

### 3.7 Multiple JOINs

```
┌──────────────────────────────────────────────────────────────┐
│  MULTIPLE JOINS = 3+ tables join karo                        │
│                                                              │
│  Example: Employee + Department + Location                   │
│                                                              │
│  employee ──▶ department ──▶ location                        │
│  e.dept_id = d.id           d.location_id = l.id            │
└──────────────────────────────────────────────────────────────┘
```

**Multiple JOIN Questions:**

**Q23: 3 tables JOIN**
```sql
SELECT e.name, d.dept_name, l.city, l.country
FROM employee e
JOIN department d ON e.dept_id = d.id
JOIN location l ON d.location_id = l.id;
```

**Q24: Mixed JOIN types (INNER + LEFT)**
```sql
SELECT e.name, d.dept_name, p.project_name
FROM employee e
INNER JOIN department d ON e.dept_id = d.id
LEFT JOIN project p ON e.id = p.employee_id;
```

**Q25: JOIN with aggregate**
```sql
SELECT d.dept_name,
       COUNT(e.id) as emp_count,
       AVG(e.salary) as avg_salary,
       MAX(e.salary) as max_salary
FROM department d
LEFT JOIN employee e ON d.id = e.dept_id
GROUP BY d.dept_name
HAVING COUNT(e.id) > 0
ORDER BY avg_salary DESC;
```

---

### JOIN Types Comparison:

```
╔══════════════════════════════════════════════════════════════════╗
║                    JOINS COMPARISON                              ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  Type          │ Result                                          ║
║  ──────────────┼────────────────────────────────────────────     ║
║  INNER JOIN    │ Only matching rows from both tables             ║
║  LEFT JOIN     │ All from left + matching from right             ║
║  RIGHT JOIN    │ All from right + matching from left             ║
║  FULL JOIN     │ All from both tables                            ║
║  SELF JOIN     │ Table joined with itself                        ║
║  CROSS JOIN    │ All combinations (cartesian product)            ║
║                                                                  ║
║  Visual:                                                         ║
║                                                                  ║
║  INNER:      ╔═══╗                                              ║
║              ║ █ ║  (Intersection only)                         ║
║              ╚═══╝                                              ║
║                                                                  ║
║  LEFT:       ╔═══════╗                                          ║
║              ║ ███   ║  (Left all + intersection)               ║
║              ╚═══════╝                                          ║
║                                                                  ║
║  RIGHT:          ╔═══════╗                                      ║
║                  ║   ███ ║  (Right all + intersection)          ║
║                  ╚═══════╝                                      ║
║                                                                  ║
║  FULL:       ╔═════════════╗                                    ║
║              ║ ███████████ ║  (Everything)                      ║
║              ╚═════════════╝                                    ║
║                                                                  ║
║  CROSS:      ╔═══╗ ╔═══╗ ╔═══╗                                 ║
║              ║ █ ║ ║ █ ║ ║ █ ║  (All combinations)            ║
║              ╚═══╝ ╚═══╝ ╚═══╝                                 ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 📌 ADVANCED JOIN QUESTIONS

### Sample Data (3 Tables):

**Orders Table:**
```
+----+------------+---------+------------+--------+
| id | product    | cust_id | order_date | amount |
+----+------------+---------+------------+--------+
|  1 | Laptop     |    1    | 2024-01-15 | 50000  |
|  2 | Phone      |    2    | 2024-02-20 | 20000  |
|  3 | Laptop     |    1    | 2024-03-10 | 55000  |
|  4 | Tablet     |    3    | 2024-01-25 | 15000  |
|  5 | Phone      |    2    | 2024-04-05 | 22000  |
|  6 | Laptop     |    4    | 2024-02-15 | 48000  |
|  7 | Tablet     |    5    | 2024-03-20 | 18000  |
+----+------------+---------+------------+--------+
```

**Customers Table:**
```
+----+----------+-----------+
| id | name     | city      |
+----+----------+-----------+
|  1 | Amit     | Delhi     |
|  2 | Rahul    | Mumbai    |
|  3 | Priya    | Bangalore |
|  4 | Suresh   | Delhi     |
|  5 | Anita    | Mumbai    |
|  6 | Vikram   | Pune      |  ← No orders
+----+----------+-----------+
```

**Products Table:**
```
+----+------------+--------+-------+
| id | name       | price  | stock |
+----+------------+--------+-------+
|  1 | Laptop     | 50000  |  10   |
|  2 | Phone      | 20000  |  50   |
|  3 | Tablet     | 15000  |  30   |
|  4 | Desktop    | 40000  |  15   |  ← No orders
+----+------------+--------+-------+
```

### Advanced Questions:

**Q1: Customer-wise order count and total amount**
```sql
SELECT c.name,
       COUNT(o.id) as order_count,
       SUM(o.amount) as total_spent
FROM customers c
LEFT JOIN orders o ON c.id = o.cust_id
GROUP BY c.name
ORDER BY total_spent DESC;
```

**Q2: Customers jinhone koi order NAHI kiya**
```sql
SELECT c.name
FROM customers c
LEFT JOIN orders o ON c.id = o.cust_id
WHERE o.id IS NULL;

-- Result: Vikram
```

**Q3: Products jinki koi order NAHI hui**
```sql
SELECT p.name
FROM products p
LEFT JOIN orders o ON p.id = o.product_id
WHERE o.id IS NULL;

-- Result: Desktop
```

**Q4: City-wise total sales**
```sql
SELECT c.city,
       SUM(o.amount) as total_sales,
       COUNT(o.id) as order_count
FROM customers c
JOIN orders o ON c.id = o.cust_id
GROUP BY c.city
ORDER BY total_sales DESC;
```

**Q5: Product-wise sales count and revenue**
```sql
SELECT p.name as product,
       COUNT(o.id) as times_ordered,
       SUM(o.amount) as total_revenue,
       AVG(o.amount) as avg_order_value
FROM products p
LEFT JOIN orders o ON p.id = o.product_id
GROUP BY p.name
ORDER BY total_revenue DESC;
```

**Q6: Customer, Product, Order details (3-table JOIN)**
```sql
SELECT c.name as customer,
       p.name as product,
       o.order_date,
       o.amount
FROM orders o
JOIN customers c ON o.cust_id = c.id
JOIN products p ON o.product_id = p.id
ORDER BY o.order_date;
```

**Q7: Customers with more than 1 order**
```sql
SELECT c.name, COUNT(o.id) as order_count
FROM customers c
JOIN orders o ON c.id = o.cust_id
GROUP BY c.name
HAVING COUNT(o.id) > 1;
```

**Q8: Month-wise sales with customer details**
```sql
SELECT DATE_FORMAT(o.order_date, '%Y-%m') as month,
       COUNT(o.id) as orders,
       SUM(o.amount) as revenue
FROM orders o
JOIN customers c ON o.cust_id = c.id
GROUP BY DATE_FORMAT(o.order_date, '%Y-%m')
ORDER BY month;
```

**Q9: Customers who bought Laptop**
```sql
SELECT DISTINCT c.name
FROM customers c
JOIN orders o ON c.id = o.cust_id
JOIN products p ON o.product_id = p.id
WHERE p.name = 'Laptop';
```

**Q10: Self JOIN - Customers from same city**
```sql
SELECT c1.name as customer1,
       c2.name as customer2,
       c1.city
FROM customers c1
JOIN customers c2 ON c1.city = c2.city AND c1.id < c2.id
ORDER BY c1.city;
```

**Q11: Customers with orders AND without orders**
```sql
-- With orders
SELECT c.name, COUNT(o.id) as orders
FROM customers c
LEFT JOIN orders o ON c.id = o.cust_id
GROUP BY c.name
HAVING COUNT(o.id) > 0

UNION

-- Without orders
SELECT c.name, 0 as orders
FROM customers c
LEFT JOIN orders o ON c.id = o.cust_id
WHERE o.id IS NULL;
```

**Q12: Product sales vs stock**
```sql
SELECT p.name,
       p.stock,
       COUNT(o.id) as orders_placed,
       CASE
           WHEN COUNT(o.id) > p.stock THEN 'OVERSOLD'
           ELSE 'OK'
       END as status
FROM products p
LEFT JOIN orders o ON p.id = o.product_id
GROUP BY p.name, p.stock;
```

---

## 📌 CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║            GROUP BY, HAVING, JOINS CHEAT SHEET                   ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  GROUP BY:                                                       ║
║  SELECT dept, COUNT(*) FROM emp GROUP BY dept;                   ║
║  Rules:                                                          ║
║  - SELECT mein sirf GROUP BY col ya aggregate ho                ║
║  - Multiple columns: GROUP BY col1, col2                        ║
║  - NULL = separate group                                        ║
║                                                                  ║
║  HAVING:                                                         ║
║  SELECT dept, COUNT(*) FROM emp                                 ║
║  GROUP BY dept                                                  ║
║  HAVING COUNT(*) > 2;                                           ║
║  - Aggregate use kar sakte ho                                   ║
║  - WHERE se different (WHERE = rows, HAVING = groups)           ║
║                                                                  ║
║  WHERE vs HAVING:                                                ║
║  WHERE  → Before GROUP BY → Row filter → No aggregate          ║
║  HAVING → After GROUP BY  → Group filter → Aggregate ok        ║
║                                                                  ║
║  JOINS:                                                          ║
║  INNER JOIN  → Only matching rows                               ║
║  LEFT JOIN   → All left + matching right                        ║
║  RIGHT JOIN  → All right + matching left                        ║
║  FULL JOIN   → Everything from both                             ║
║  SELF JOIN   → Table with itself                                ║
║  CROSS JOIN  → All combinations                                 ║
║                                                                  ║
║  JOIN Syntax:                                                    ║
║  SELECT cols FROM t1                                             ║
║  JOIN t2 ON t1.col = t2.col                                    ║
║                                                                  ║
║  Execution Order:                                                ║
║  FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER → LIMIT     ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Last Updated: September 2026*
*Covers: GROUP BY, HAVING, JOINs (INNER, LEFT, RIGHT, FULL, SELF, CROSS), 60+ Questions*
