# SQL Interview Questions - Zero se Seekho (Basic to Advanced)

---

## 📌 TABLE 1: BASIC SELECT (Employee Table)

### Sample Data:
```
+----+----------+--------+---------+--------+------------+
| id | name     | dept   | salary  | age    | join_date  |
+----+----------+--------+---------+--------+------------+
|  1 | Amit     | IT     | 50000   | 28     | 2020-01-15 |
|  2 | Rahul    | HR     | 45000   | 32     | 2019-06-20 |
|  3 | Priya    | IT     | 60000   | 26     | 2021-03-10 |
|  4 | Suresh   | Sales  | 40000   | 35     | 2018-11-05 |
|  5 | Anita    | HR     | 55000   | 29     | 2020-08-25 |
|  6 | Vikram   | IT     | 70000   | 31     | 2017-04-12 |
|  7 | Neha     | Sales  | 42000   | 27     | 2022-02-18 |
|  8 | Rajesh   | IT     | 50000   | 30     | 2021-09-30 |
+----+----------+--------+---------+--------+------------+
```

### Questions:

**Q1: Saare employees ka naam aur salary dikhao**
```sql
-- Simple SELECT
SELECT name, salary FROM employee;
```

**Q2: IT department mein kaun kaun hai?**
```sql
-- WHERE clause
SELECT * FROM employee WHERE dept = 'IT';
```

**Q3: Salary 50000 se zyada wale employees dikhao**
```sql
-- Comparison operators
SELECT name, salary FROM employee WHERE salary > 50000;
```

**Q4: Age 28 se kam aur salary 50000 se zyada wale?**
```sql
-- AND operator
SELECT name, age, salary FROM employee
WHERE age < 28 AND salary > 50000;
```

**Q5: IT ya HR department wale?**
```sql
-- OR operator
SELECT name, dept FROM employee
WHERE dept = 'IT' OR dept = 'HR';
```

**Q6: IN operator use karo (IT aur Sales wale)**
```sql
-- IN operator (OR ka short form)
SELECT name, dept FROM employee
WHERE dept IN ('IT', 'Sales');
```

**Q7: NOT operator (IT department ke alawa sab)**
```sql
SELECT name, dept FROM employee
WHERE dept != 'IT';
-- OR
SELECT name, dept FROM employee
WHERE dept <> 'IT';
```

**Q8: Salary 40000 aur 60000 ke beech mein?**
```sql
-- BETWEEN operator
SELECT name, salary FROM employee
WHERE salary BETWEEN 40000 AND 60000;
```

**Q9: NULL check karo (agar kisi ki salary NULL ho)**
```sql
-- NULL check (Galat: salary = NULL)
-- Sahi: IS NULL
SELECT * FROM employee WHERE salary IS NULL;
SELECT * FROM employee WHERE salary IS NOT NULL;
```

**Q10: Names A se shuru hote hain?**
```sql
-- LIKE operator (wildcard)
SELECT name FROM employee WHERE name LIKE 'A%';  -- A se shuru
SELECT name FROM employee WHERE name LIKE '%a%'; -- a kahin bhi
SELECT name FROM employee WHERE name LIKE '_mit'; -- 1 char + mit
```

---

## 📌 TABLE 2: SORTING & LIMIT (Employee Table)

### Questions:

**Q1: Salary ke hisaab se ascending order mein sort karo**
```sql
-- ASC (default)
SELECT name, salary FROM employee ORDER BY salary ASC;
```

**Q2: Salary ke hisaab se DESCENDING order mein sort karo**
```sql
SELECT name, salary FROM employee ORDER BY salary DESC;
```

**Q3: Pehle dept ke hisaab se sort, phir salary se?**
```sql
-- Multiple columns
SELECT name, dept, salary FROM employee
ORDER BY dept ASC, salary DESC;
```

**Q4: Top 3 sabse zyada salary wale dikhao**
```sql
-- LIMIT (MySQL)
SELECT name, salary FROM employee
ORDER BY salary DESC
LIMIT 3;
```

**Q5: 2nd to 4th employee dikhao (pagination)**
```sql
-- OFFSET (skip first 1, take next 3)
SELECT name FROM employee
ORDER BY id
LIMIT 3 OFFSET 1;
```

**Q6: Department ke hisaab se kitne employees hain?**
```sql
-- GROUP BY + COUNT
SELECT dept, COUNT(*) as emp_count
FROM employee
GROUP BY dept;
```

**Q7: Department ke hisaab se average salary?**
```sql
SELECT dept, AVG(salary) as avg_salary
FROM employee
GROUP BY dept;
```

**Q8: Department ke hisaab se total salary?**
```sql
SELECT dept, SUM(salary) as total_salary
FROM employee
GROUP BY dept;
```

**Q9: Sirf un departments ko dikhao jahan 2 se zyada employees hain**
```sql
-- HAVING clause (GROUP BY ke baad filter)
SELECT dept, COUNT(*) as emp_count
FROM employee
GROUP BY dept
HAVING COUNT(*) > 2;
```

**Q10: Average salary 50000 se zyada wale departments?**
```sql
SELECT dept, AVG(salary) as avg_sal
FROM employee
GROUP BY dept
HAVING AVG(salary) > 50000;
```

---

## 📌 TABLE 3: AGGREGATE FUNCTIONS

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

### Questions:

**Q1: Total kitne employees hain?**
```sql
SELECT COUNT(*) as total_employees FROM employee;
```

**Q2: Total salary kitni hai?**
```sql
SELECT SUM(salary) as total_salary FROM employee;
```

**Q3: Average salary kitni hai?**
```sql
SELECT AVG(salary) as avg_salary FROM employee;
```

**Q4: Sabse zyada salary kitni hai?**
```sql
SELECT MAX(salary) as max_salary FROM employee;
```

**Q5: Sabse kam salary kitni hai?**
```sql
SELECT MIN(salary) as min_salary FROM employee;
```

**Q6: Department ke saath average salary (GROUP BY)**
```sql
SELECT dept, AVG(salary) as avg_sal
FROM employee
GROUP BY dept;
```

**Q7: Har department mein sabse zyada salary?**
```sql
SELECT dept, MAX(salary) as max_sal
FROM employee
GROUP BY dept;
```

**Q8: DISTINCT departments kitne hain?**
```sql
SELECT COUNT(DISTINCT dept) as unique_depts FROM employee;
```

**Q9: Average salary ke upar wale employees?**
```sql
-- Subquery
SELECT name, salary FROM employee
WHERE salary > (SELECT AVG(salary) FROM employee);
```

**Q10: Department-wise count aur average salary together?**
```sql
SELECT dept,
       COUNT(*) as emp_count,
       AVG(salary) as avg_sal,
       MAX(salary) as max_sal,
       MIN(salary) as min_sal
FROM employee
GROUP BY dept;
```

---

## 📌 TABLE 4: JOINS (2 Tables)

### Employee Table:
```
+----+----------+--------+---------+--------+
| id | name     | dept_id| salary  | age    |
+----+----------+--------+---------+--------+
|  1 | Amit     |   1    | 50000   | 28     |
|  2 | Rahul    |   2    | 45000   | 32     |
|  3 | Priya    |   1    | 60000   | 26     |
|  4 | Suresh   |   3    | 40000   | 35     |
|  5 | Anita    |   2    | 55000   | 29     |
|  6 | Vikram   |   1    | 70000   | 31     |
|  7 | Neha     |   4    | 42000   | 27     |
|  8 | Rajesh   |   5    | 50000   | 30     |
+----+----------+--------+---------+--------+
```

### Department Table:
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

### Visual: Employee + Department JOIN

```
    Employee Table          Department Table
    +----+--------+         +----+-----------+
    | id | dept_id|         | id | dept_name |
    +----+--------+         +----+-----------+
    |  1 |   1    |───┐     |  1 | IT        |
    |  2 |   2    │   │     |  2 | HR        |
    |  3 |   1    │   ├────▶|  3 | Sales     |
    |  4 |   3    │   │     |  4 | Marketing |
    |  5 |   2    │   │     |  5 | Finance   |
    |  6 |   1    │   │     |  6 | Admin     |
    |  7 |   4    │   │     +----+-----------+
    |  8 |   5    │   │
    +----+--------+   │
                      │
              dept_id = id
```

### Questions:

**Q1: Har employee ka naam aur uska department name? (INNER JOIN)**
```sql
-- Sirf matching records
SELECT e.name, d.dept_name
FROM employee e
INNER JOIN department d ON e.dept_id = d.id;
```

**Q2: LEFT JOIN - Saare employees, department ho ya na ho**
```sql
-- Sab employees + matching department
SELECT e.name, d.dept_name
FROM employee e
LEFT JOIN department d ON e.dept_id = d.id;
```

**Q3: RIGHT JOIN - Saare departments, employee ho ya na ho**
```sql
-- Sab departments + matching employee
SELECT e.name, d.dept_name
FROM employee e
RIGHT JOIN department d ON e.dept_id = d.id;
```

**Q4: FULL OUTER JOIN - Saare records dono taraf se**
```sql
-- Sab kuch
SELECT e.name, d.dept_name
FROM employee e
FULL OUTER JOIN department d ON e.dept_id = d.id;
```

**Q5: Aise employees dikhao jinka koi department nahi hai**
```sql
-- LEFT JOIN + IS NULL
SELECT e.name
FROM employee e
LEFT JOIN department d ON e.dept_id = d.id
WHERE d.id IS NULL;
```

**Q6: Aise departments dikhao jahan koi employee nahi hai**
```sql
SELECT d.dept_name
FROM department d
LEFT JOIN employee e ON d.id = e.dept_id
WHERE e.id IS NULL;
```

**Q7: Department ke saath employee count (JOIN + GROUP BY)**
```sql
SELECT d.dept_name, COUNT(e.id) as emp_count
FROM department d
LEFT JOIN employee e ON d.id = e.dept_id
GROUP BY d.dept_name;
```

**Q8: Self JOIN - Employee aur uske manager (agar manager_id ho)**
```sql
-- Employee table mein manager_id column hai
SELECT e.name as employee, m.name as manager
FROM employee e
LEFT JOIN employee m ON e.manager_id = m.id;
```

**Q9: CROSS JOIN - Har employee har department ke saath**
```sql
-- Cartesian product (8 × 6 = 48 rows)
SELECT e.name, d.dept_name
FROM employee e
CROSS JOIN department d;
```

**Q10: INNER JOIN mein sirf matching records aati hain**
```sql
-- Employee 7 ka dept_id=4 (Marketing) - match hai
-- Employee 8 ka dept_id=5 (Finance) - match hai
-- Agar kisi ka dept_id = 99 (nahi hai) → exclude hoga
```

---

## 📌 TABLE 5: SUBQUERIES (Nested Queries)

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

### Questions:

**Q1: Average salary se zyada kamane wale employees?**
```sql
-- Subquery in WHERE
SELECT name, salary FROM employee
WHERE salary > (SELECT AVG(salary) FROM employee);
```

**Q2: Sabse zyada salary wale employee ka naam?**
```sql
-- Subquery
SELECT name, salary FROM employee
WHERE salary = (SELECT MAX(salary) FROM employee);
```

**Q3: Aise employees dikhao jo IT department mein hain (IN subquery)**
```sql
-- Subquery with IN
SELECT name FROM employee
WHERE dept IN (SELECT DISTINCT dept FROM employee WHERE dept = 'IT');
```

**Q4: EXISTS operator - Aise departments dikhao jahan employee hai**
```sql
SELECT d.dept_name
FROM department d
WHERE EXISTS (
    SELECT 1 FROM employee e WHERE e.dept_id = d.id
);
```

**Q5: NOT EXISTS - Aise departments jahan employee NAHI hai**
```sql
SELECT d.dept_name
FROM department d
WHERE NOT EXISTS (
    SELECT 1 FROM employee e WHERE e.dept_id = d.id
);
```

**Q6: Scalar subquery (single value return kare)**
```sql
SELECT name, salary,
       (SELECT AVG(salary) FROM employee) as avg_salary,
       salary - (SELECT AVG(salary) FROM employee) as diff_from_avg
FROM employee;
```

**Q7: Correlated subquery (row-by-row execute hota hai)**
```sql
-- Har employee ke dept ka average se compare
SELECT name, dept, salary
FROM employee e
WHERE salary > (
    SELECT AVG(salary) FROM employee WHERE dept = e.dept
);
```

**Q8: Derived table (subquery in FROM)**
```sql
SELECT dept, avg_sal
FROM (
    SELECT dept, AVG(salary) as avg_sal
    FROM employee
    GROUP BY dept
) as dept_avg
WHERE avg_sal > 50000;
```

**Q9: Multiple subqueries**
```sql
SELECT name, salary FROM employee
WHERE salary > (SELECT AVG(salary) FROM employee)
AND dept = 'IT';
```

**Q10: Subquery with JOIN**
```sql
SELECT e.name, d.dept_name, e.salary
FROM employee e
JOIN department d ON e.dept_id = d.id
WHERE e.salary > (SELECT AVG(salary) FROM employee);
```

---

## 📌 TABLE 6: WINDOW FUNCTIONS (Advanced)

### Sample Data:
```
+----+----------+--------+---------+------------+
| id | name     | dept   | salary  | join_date  |
+----+----------+--------+---------+------------+
|  1 | Amit     | IT     | 50000   | 2020-01-15 |
|  2 | Rahul    | HR     | 45000   | 2019-06-20 |
|  3 | Priya    | IT     | 60000   | 2021-03-10 |
|  4 | Suresh   | Sales  | 40000   | 2018-11-05 |
|  5 | Anita    | HR     | 55000   | 2020-08-25 |
|  6 | Vikram   | IT     | 70000   | 2017-04-12 |
|  7 | Neha     | Sales  | 42000   | 2022-02-18 |
|  8 | Rajesh   | IT     | 50000   | 2021-09-30 |
+----+----------+--------+---------+------------+
```

### Questions:

**Q1: Har employee ko salary rank do (ROW_NUMBER)**
```sql
-- Row number (1, 2, 3...)
SELECT name, salary,
       ROW_NUMBER() OVER (ORDER BY salary DESC) as row_num
FROM employee;
```

**Q2: Salary rank do (ties mein same rank)**
```sql
-- RANK (ties mein skip karta hai: 1, 1, 3)
SELECT name, salary,
       RANK() OVER (ORDER BY salary DESC) as rank_val
FROM employee;
```

**Q3: Dense rank (ties mein skip NAHI karta)**
```sql
-- DENSE_RANK (ties mein skip nahi: 1, 1, 2)
SELECT name, salary,
       DENSE_RANK() OVER (ORDER BY salary DESC) as dense_rank_val
FROM employee;
```

**Q4: Department-wise salary rank?**
```sql
-- PARTITION BY (GROUP BY jaisa but row preserve karta hai)
SELECT name, dept, salary,
       RANK() OVER (PARTITION BY dept ORDER BY salary DESC) as dept_rank
FROM employee;
```

**Q5: Running total (cumulative sum)**
```sql
-- Har row ka running total
SELECT name, salary,
       SUM(salary) OVER (ORDER BY id) as running_total
FROM employee;
```

**Q6: Department-wise running total**
```sql
SELECT name, dept, salary,
       SUM(salary) OVER (PARTITION BY dept ORDER BY id) as dept_running_total
FROM employee;
```

**Q7: Salary ka difference previous employee se (LAG)**
```sql
-- Pichle row ki value
SELECT name, salary,
       LAG(salary, 1) OVER (ORDER BY id) as prev_salary,
       salary - LAG(salary, 1) OVER (ORDER BY id) as salary_diff
FROM employee;
```

**Q8: Next employee ki salary (LEAD)**
```sql
SELECT name, salary,
       LEAD(salary, 1) OVER (ORDER BY id) as next_salary
FROM employee;
```

**Q9: Department ka average salary har row mein dikhao**
```sql
SELECT name, dept, salary,
       AVG(salary) OVER (PARTITION BY dept) as dept_avg
FROM employee;
```

**Q10: Nth highest salary (Window function se)**
```sql
-- 3rd highest salary
WITH ranked AS (
    SELECT name, salary,
           DENSE_RANK() OVER (ORDER BY salary DESC) as rnk
    FROM employee
)
SELECT name, salary
FROM ranked
WHERE rnk = 3;
```

---

## 📌 TABLE 7: ADVANCED QUERIES

### Employee Table:
```
+----+----------+--------+---------+------------+
| id | name     | dept   | salary  | join_date  |
+----+----------+--------+---------+------------+
|  1 | Amit     | IT     | 50000   | 2020-01-15 |
|  2 | Rahul    | HR     | 45000   | 2019-06-20 |
|  3 | Priya    | IT     | 60000   | 2021-03-10 |
|  4 | Suresh   | Sales  | 40000   | 2018-11-05 |
|  5 | Anita    | HR     | 55000   | 2020-08-25 |
|  6 | Vikram   | IT     | 70000   | 2017-04-12 |
|  7 | Neha     | Sales  | 42000   | 2022-02-18 |
|  8 | Rajesh   | IT     | 50000   | 2021-09-30 |
+----+----------+--------+---------+------------+
```

### Questions:

**Q1: Department-wise 2nd highest salary nikalo**
```sql
WITH ranked AS (
    SELECT dept, salary,
           DENSE_RANK() OVER (PARTITION BY dept ORDER BY salary DESC) as rnk
    FROM employee
)
SELECT dept, salary
FROM ranked
WHERE rnk = 2;
```

**Q2: Har department mein sabse purana employee (jo pehle join hua)**
```sql
SELECT dept, name, join_date
FROM (
    SELECT dept, name, join_date,
           ROW_NUMBER() OVER (PARTITION BY dept ORDER BY join_date ASC) as rn
    FROM employee
) t
WHERE rn = 1;
```

**Q3: Employees jinka salary department average se zyada hai**
```sql
SELECT name, dept, salary
FROM employee e
WHERE salary > (
    SELECT AVG(salary) FROM employee WHERE dept = e.dept
);
```

**Q4: Year-wise employee count (EXTRACT)**
```sql
SELECT YEAR(join_date) as join_year,
       COUNT(*) as emp_count
FROM employee
GROUP BY YEAR(join_date)
ORDER BY join_year;
```

**Q5: Month-wise employee count**
```sql
SELECT MONTH(join_date) as join_month,
       COUNT(*) as emp_count
FROM employee
GROUP BY MONTH(join_date)
ORDER BY join_month;
```

**Q6: Employees jo 2020 ke baad join hue**
```sql
-- Multiple ways
SELECT name, join_date FROM employee
WHERE join_date > '2020-12-31';

-- OR
SELECT name, join_date FROM employee
WHERE YEAR(join_date) >= 2021;

-- OR
SELECT name, join_date FROM employee
WHERE join_date >= '2021-01-01';
```

**Q7: Salary bands banao (CASE WHEN)**
```sql
SELECT name, salary,
    CASE
        WHEN salary >= 60000 THEN 'High'
        WHEN salary >= 45000 THEN 'Medium'
        ELSE 'Low'
    END as salary_band
FROM employee;
```

**Q8: Department-wise percentage contribution**
```sql
SELECT dept,
       COUNT(*) as emp_count,
       ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM employee), 2) as percentage
FROM employee
GROUP BY dept;
```

**Q9: Employees jinka naam same hai (duplicate names)**
```sql
SELECT name, COUNT(*) as name_count
FROM employee
GROUP BY name
HAVING COUNT(*) > 1;
```

**Q10: Second highest salary (subquery method)**
```sql
SELECT MAX(salary) as second_highest
FROM employee
WHERE salary < (SELECT MAX(salary) FROM employee);
```

---

## 📌 TABLE 8: LEETCODE STYLE PROBLEMS

### Employee Table:
```
+----+-------+--------+------------+
| id | name  | salary | departmentId|
+----+-------+--------+------------+
|  1 | Joe   | 70000  |     1      |
|  2 | Henry | 80000  |     2      |
|  3 | Sam   | 60000  |     2      |
|  4 | Max   | 90000  |     1      |
+----+-------+--------+------------+
```

### Department Table:
```
+----+----------+
| id | name     |
+----+----------+
|  1 | IT       |
|  2 | Sales    |
+----+----------+
```

### Questions:

**Q1: Department ka highest salary (LeetCode 184)**
```sql
SELECT d.name as Department, e.name as Employee, e.salary as Salary
FROM Employee e
JOIN Department d ON e.departmentId = d.id
WHERE e.salary = (
    SELECT MAX(salary)
    FROM Employee
    WHERE departmentId = e.departmentId
);
```

**Q2: Second highest salary (LeetCode 176)**
```sql
-- Method 1: Subquery
SELECT MAX(salary) as SecondHighestSalary
FROM Employee
WHERE salary < (SELECT MAX(salary) FROM Employee);

-- Method 2: LIMIT
SELECT DISTINCT salary as SecondHighestSalary
FROM Employee
ORDER BY salary DESC
LIMIT 1 OFFSET 1;
```

**Q3: Delete duplicate emails (LeetCode 196)**
```sql
DELETE p1
FROM Person p1
JOIN Person p2
WHERE p1.email = p2.email AND p1.id > p2.id;
```

**Q4: Consecutive numbers (LeetCode 180)**
```sql
SELECT DISTINCT l1.num as ConsecutiveNums
FROM Logs l1
JOIN Logs l2 ON l1.id = l2.id - 1
JOIN Logs l3 ON l1.id = l3.id - 2
WHERE l1.num = l2.num AND l2.num = l3.num;
```

**Q5: Employees earning more than managers (LeetCode 181)**
```sql
SELECT e1.name as Employee
FROM Employee e1
JOIN Employee e2 ON e1.managerId = e2.id
WHERE e1.salary > e2.salary;
```

**Q6: Find employees who earn more than average (LeetCode)**
```sql
SELECT name, salary
FROM Employee
WHERE salary > (SELECT AVG(salary) FROM Employee);
```

**Q7: Department top 3 salaries (LeetCode 185)**
```sql
WITH RankedEmployees AS (
    SELECT d.name as Department,
           e.name as Employee,
           e.salary as Salary,
           DENSE_RANK() OVER (PARTITION BY e.departmentId ORDER BY e.salary DESC) as rnk
    FROM Employee e
    JOIN Department d ON e.departmentId = d.id
)
SELECT Department, Employee, Salary
FROM RankedEmployees
WHERE rnk <= 3;
```

**Q8: Triangle Judgement (LeetCode 1757)**
```sql
SELECT x, y, z,
    CASE
        WHEN x + y > z AND x + z > y AND y + z > x THEN 'Yes'
        ELSE 'No'
    END as triangle
FROM Triangle;
```

**Q9: Customers who never order (LeetCode 183)**
```sql
SELECT c.Name as Customers
FROM Customers c
LEFT JOIN Orders o ON c.Id = o.CustomerId
WHERE o.Id IS NULL;
```

**Q10: Combine two tables (LeetCode 175)**
```sql
SELECT p.firstName, p.lastName, a.city, a.state
FROM Person p
LEFT JOIN Address a ON p.personId = a.personId;
```

---

## 📌 TABLE 9: INTERVIEW FAVORITES

### Sales Table:
```
+----+------------+------------+--------+------------+
| id | product    | region     | amount | sale_date  |
+----+------------+------------+--------+------------+
|  1 | Laptop     | North      | 50000  | 2024-01-15 |
|  2 | Phone      | South      | 20000  | 2024-02-20 |
|  3 | Laptop     | East       | 55000  | 2024-01-25 |
|  4 | Tablet     | North      | 15000  | 2024-03-10 |
|  5 | Phone      | North      | 22000  | 2024-02-05 |
|  6 | Laptop     | South      | 48000  | 2024-03-15 |
|  7 | Tablet     | East       | 18000  | 2024-01-30 |
|  8 | Phone      | West       | 25000  | 2024-02-28 |
|  9 | Laptop     | North      | 52000  | 2024-03-20 |
| 10 | Tablet     | South      | 16000  | 2024-04-01 |
+----+------------+------------+--------+------------+
```

### Questions:

**Q1: Region-wise total sales**
```sql
SELECT region, SUM(amount) as total_sales
FROM sales
GROUP BY region
ORDER BY total_sales DESC;
```

**Q2: Product-wise count aur total amount**
```sql
SELECT product,
       COUNT(*) as times_sold,
       SUM(amount) as total_amount,
       AVG(amount) as avg_amount
FROM sales
GROUP BY product;
```

**Q3: Month-wise sales trend**
```sql
SELECT DATE_FORMAT(sale_date, '%Y-%m') as sale_month,
       COUNT(*) as orders,
       SUM(amount) as total_sales
FROM sales
GROUP BY DATE_FORMAT(sale_date, '%Y-%m')
ORDER BY sale_month;
```

**Q4: Har region ka top product (Window function)**
```sql
WITH ranked AS (
    SELECT region, product, SUM(amount) as total,
           RANK() OVER (PARTITION BY region ORDER BY SUM(amount) DESC) as rnk
    FROM sales
    GROUP BY region, product
)
SELECT region, product, total
FROM ranked
WHERE rnk = 1;
```

**Q5: Aise products dikhao jo kisi ek region mein nahi bikte**
```sql
SELECT DISTINCT product
FROM sales
WHERE product NOT IN (
    SELECT DISTINCT product FROM sales WHERE region = 'North'
);
```

**Q6: Region-wise percentage contribution**
```sql
SELECT region,
       SUM(amount) as total,
       ROUND(SUM(amount) * 100.0 / (SELECT SUM(amount) FROM sales), 2) as percentage
FROM sales
GROUP BY region;
```

**Q7: Cumulative sales by region**
```sql
SELECT region, sale_date, amount,
       SUM(amount) OVER (PARTITION BY region ORDER BY sale_date) as cumulative_sales
FROM sales;
```

**Q8: Month-over-month growth**
```sql
WITH monthly AS (
    SELECT DATE_FORMAT(sale_date, '%Y-%m') as month,
           SUM(amount) as total
    FROM sales
    GROUP BY DATE_FORMAT(sale_date, '%Y-%m')
)
SELECT month, total,
       LAG(total) OVER (ORDER BY month) as prev_month,
       ROUND((total - LAG(total) OVER (ORDER BY month)) * 100.0 /
             LAG(total) OVER (ORDER BY month), 2) as growth_pct
FROM monthly;
```

**Q9: Product sales in each region vs overall average**
```sql
SELECT product, region, SUM(amount) as region_sales,
       (SELECT AVG(amount) FROM sales) as overall_avg
FROM sales
GROUP BY product, region
HAVING SUM(amount) > (SELECT AVG(amount) FROM sales);
```

**Q10: Running total and rank together**
```sql
SELECT product, region, amount,
       SUM(amount) OVER (ORDER BY sale_date) as running_total,
       ROW_NUMBER() OVER (ORDER BY amount DESC) as sales_rank
FROM sales;
```

---

## 📌 CHEAT SHEET

```
╔══════════════════════════════════════════════════════════════════╗
║                    SQL CHEAT SHEET                               ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  BASIC:                                                          ║
║  SELECT col FROM table WHERE condition ORDER BY col LIMIT n;      ║
║                                                                  ║
║  OPERATORS:                                                       ║
║  =, !=, <>, <, >, <=, >=                                        ║
║  AND, OR, NOT, IN, BETWEEN, LIKE, IS NULL                       ║
║                                                                  ║
║  AGGREGATE:                                                       ║
║  COUNT(*), SUM(), AVG(), MAX(), MIN()                            ║
║                                                                  ║
║  GROUP BY + HAVING:                                              ║
║  SELECT dept, COUNT(*) FROM emp GROUP BY dept HAVING COUNT(*)>2; ║
║                                                                  ║
║  JOINS:                                                           ║
║  INNER JOIN - Only matching rows                                ║
║  LEFT JOIN  - All from left + matching from right               ║
║  RIGHT JOIN - All from right + matching from left               ║
║  FULL JOIN  - All from both                                     ║
║                                                                  ║
║  SUBQUERY:                                                        ║
║  SELECT * FROM t WHERE col > (SELECT AVG(col) FROM t);           ║
║                                                                  ║
║  WINDOW FUNCTIONS:                                               ║
║  ROW_NUMBER(), RANK(), DENSE_RANK()                             ║
║  LAG(), LEAD()                                                  ║
║  SUM() OVER (PARTITION BY dept ORDER BY id)                     ║
║                                                                  ║
║  CTE (WITH clause):                                              ║
║  WITH cte AS (SELECT ...) SELECT * FROM cte;                    ║
║                                                                  ║
║  CASE WHEN:                                                      ║
║  CASE WHEN x > 10 THEN 'High' ELSE 'Low' END                    ║
║                                                                  ║
║  DATE:                                                            ║
║  YEAR(), MONTH(), DATE_FORMAT(), DATEDIFF()                     ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 📌 INTERVIEW TIPS

```
1. Always clarify requirements before writing query
2. Start with simple SELECT, then add WHERE, GROUP BY, HAVING
3. Use aliases for readability
4. Handle NULL values properly (IS NULL, not = NULL)
5. Practice JOINs thoroughly (most asked in interviews)
6. Know window functions (ROW_NUMBER, RANK, LAG/LEAD)
7. Understand difference between WHERE and HAVING
8. Practice LeetCode SQL problems (175-185 range)
9. Know subqueries and CTEs
10. Always test with sample data first
```

---

*Last Updated: September 2026*
*Covers: Basic to Advanced SQL, 90+ Interview Questions with ASCII Tables*
