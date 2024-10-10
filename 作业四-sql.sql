-- -------------------------
-- 作业50题
-- -------------------------
# 1. 查询所有学生的信息。
SELECT *
FROM student;

# 2. 查询所有课程的信息。
SELECT *
FROM course;

# 3. 查询所有学生的姓名、学号和班级。
SELECT name 姓名, student_id 学号, my_class 班级
FROM student;

# 4. 查询所有教师的姓名和职称。
SELECT name 姓名, title 职称
FROM teacher;

# 5. 查询不同课程的平均分数。
SELECT course_name 课程, ROUND(AVG(score), 1) AS 平均分
FROM score
JOIN course c on score.course_id = c.course_id
GROUP BY course_name;

# 6. 查询每个学生的平均分数。
SELECT name 学生姓名, ROUND(AVG(score), 1) AS 平均分
FROM student
JOIN score s on student.student_id = s.student_id
GROUP BY s.student_id;

# 7. 查询分数大于85分的学生学号和课程号。
SELECT student.student_id 学号, score.course_id 课程号
FROM student, score
WHERE score.student_id = student.student_id AND score > 85;

# 8. 查询每门课程的选课人数。
SELECT course.course_name 课程, COUNT(student_id) 人数
FROM course
JOIN score s on course.course_id = s.course_id
GROUP BY s.course_id;

# 9. 查询选修了"高等数学"课程的学生姓名和分数。
SELECT student.name 学生姓名, score.score 分数
FROM student, score, course
WHERE student.student_id = score.student_id AND score.course_id = course.course_id AND course_name = '高等数学';

# 10. 查询没有选修"大学物理"课程的学生姓名。
SELECT name 没有选修“大学物理”的学生姓名
FROM student
WHERE student_id NOT IN (
    SELECT student_id
    FROM course, score
    WHERE score.course_id = course.course_id AND course_name = '大学物理'
);

# 11. 查询C001比C002课程成绩高的学生信息及课程分数。
SELECT student.*, C001.score 课程分数
FROM (
    SELECT student_id, score
    FROM score
    WHERE course_id = 'C001'
     ) AS C001,
    (
    SELECT student_id, score
    FROM score
    WHERE course_id = 'C002'
    ) AS C002,
    student
WHERE C001.student_id = student.student_id AND C002.student_id = student.student_id AND C001.score > C002.score;

# 12. 统计各科成绩各分数段人数：课程编号，课程名称，[100-85]，[85-70]，[70-60]，[60-0] 及所占百分比
SELECT qian.course_id 课程编号, course_name 课程名称,
       IFNULL(CONCAT(ROUND((yx / total) * 100, 2), '%'), 0.00) '[100-85]',
       IFNULL(CONCAT(ROUND((yl / total) * 100, 2), '%'), 0.00)  '[85-70]',
       IFNULL(CONCAT(ROUND((zd / total) * 100, 2), '%'), 0.00)  '[70-60]',
       IFNULL(CONCAT(ROUND((jige / total) * 100, 2), '%'), 0.00)  '[60-0]'
FROM (
    SELECT course.course_id, course.course_name
    FROM course
    INNER JOIN score s on course.course_id = s.course_id
    GROUP BY course.course_id
     ) AS qian
LEFT JOIN (
    SELECT score.course_id, count(student_id) total
    FROM score
    GROUP BY course_id
     ) AS total on qian.course_id = total.course_id
LEFT JOIN (
    SELECT score.course_id, count(student_id) jige
    FROM score
    WHERE score < 60
    GROUP BY course_id
) AS jige on jige.course_id = total.course_id
LEFT JOIN (
    SELECT score.course_id, count(student_id) zd
    FROM score
    WHERE score >= 60 AND score < 70
    GROUP BY course_id
) AS zd on zd.course_id = total.course_id
LEFT JOIN (
    SELECT score.course_id, count(student_id) yl
    FROM score
    WHERE score >= 70 AND score < 85
    GROUP BY course_id
) AS yl on yl.course_id = total.course_id
LEFT JOIN (
    SELECT score.course_id, count(student_id) yx
    FROM score
    WHERE score >= 85
    GROUP BY course_id
) AS yx on yx.course_id = total.course_id;

# 13. 查询选择C002课程但没选择C004课程的成绩情况(不存在时显示为 null )。
SELECT DISTINCT c2.name 学生姓名, c2.course_id 课程编号, c2.score 分数
FROM (
    SELECT student.student_id, course_id, student.name, s.score
    FROM student
    JOIN score s on student.student_id = s.student_id
    WHERE course_id = 'C002') as c2
LEFT JOIN (
    SELECT student.student_id, course_id, student.name, s.score
    FROM student
    JOIN score s on student.student_id = s.student_id
    WHERE student.student_id NOT IN (
        SELECT student.student_id
        FROM student
        JOIN score s on student.student_id = s.student_id
        WHERE course_id = 'C004'
    )
) AS c4 on c2.student_id = c4.student_id;

# 14. 查询平均分数最高的学生姓名和平均分数。
SELECT student.name 学生姓名, ROUND(AVG(score), 1) AS 平均分
FROM student
JOIN score s on student.student_id = s.student_id
GROUP BY s.student_id
ORDER BY 平均分 DESC
LIMIT 1;

# 15. 查询总分最高的前三名学生的姓名和总分。
SELECT student.name 学生姓名, SUM(score) AS 总分
FROM student
JOIN score s on student.student_id = s.student_id
GROUP BY s.student_id
ORDER BY 总分 DESC
LIMIT 3;

# 16. 查询各科成绩最高分、最低分和平均分。要求如下：
# 以如下形式显示：课程 ID，课程 name，最高分，最低分，平均分，及格率，中等率，优良率，优秀率
# 及格为>=60，中等为：70-80，优良为：80-90，优秀为：>=90
# 要求输出课程号和选修人数，查询结果按人数降序排列，若人数相同，按课程号升序排列
SELECT qian.course_id, course_name, 最高分, 最低分, 平均分,
       IFNULL(CONCAT(ROUND((jige / total) * 100, 2), '%'), 0.00) 及格率,
       IFNULL(CONCAT(ROUND((zd / total) * 100, 2), '%'), 0.00) 中等率,
       IFNULL(CONCAT(ROUND((yl / total) * 100, 2), '%'), 0.00) 优良率,
       IFNULL(CONCAT(ROUND((yx / total) * 100, 2), '%'), 0.00) 优秀率
FROM (
    SELECT course.course_id, course.course_name, MAX(score) 最高分, MIN(score) 最低分, ROUND(AVG(score), 1) 平均分
    FROM course
    INNER JOIN score s on course.course_id = s.course_id
    GROUP BY course.course_id
     ) AS qian
LEFT JOIN (
    SELECT score.course_id, count(student_id) total
    FROM score
    GROUP BY course_id
     ) AS total on qian.course_id = total.course_id
LEFT JOIN (
    SELECT score.course_id, count(student_id) jige
    FROM score
    WHERE score >= 60
    GROUP BY course_id
) AS jige on jige.course_id = total.course_id
LEFT JOIN (
    SELECT score.course_id, count(student_id) zd
    FROM score
    WHERE score >= 70 AND score < 80
    GROUP BY course_id
) AS zd on zd.course_id = total.course_id
LEFT JOIN (
    SELECT score.course_id, count(student_id) yl
    FROM score
    WHERE score >= 80 AND score < 90
    GROUP BY course_id
) AS yl on yl.course_id = total.course_id
LEFT JOIN (
    SELECT score.course_id, count(student_id) yx
    FROM score
    WHERE score >= 90
    GROUP BY course_id
) AS yx on yx.course_id = total.course_id;

# 17. 查询男生和女生的人数。
SELECT gender 性别, COUNT(gender) 人数
FROM student
GROUP BY gender ;

# 18. 查询年龄最大的学生姓名。
SELECT name 学生姓名
FROM student
ORDER BY TIMESTAMPDIFF(YEAR , birth_date, NOW()) DESC
LIMIT 1;

# 19. 查询年龄最小的教师姓名。
SELECT name 教师姓名
FROM teacher
ORDER BY TIMESTAMPDIFF(YEAR , birth_date, NOW()) ASC
LIMIT 1;

# 20. 查询学过「张教授」授课的同学的信息。
SELECT student.*
FROM student, score, course,teacher
WHERE student.student_id = score.student_id AND score.course_id = course.course_id AND course.teacher_id = teacher.teacher_id
AND teacher.name = '张教授';

# 21. 查询查询至少有一门课与学号为"2021001"的同学所学相同的同学的信息。
SELECT DISTINCT student.*
FROM student
JOIN score s on student.student_id = s.student_id
WHERE s.course_id IN (
    SELECT course_id
    FROM score
    WHERE score.student_id = '2021001'
);

# 22. 查询每门课程的平均分数，并按平均分数降序排列。
SELECT course.course_name 课程名称, ROUND(AVG(score), 1) AS 平均分
FROM course
JOIN score s on course.course_id = s.course_id
GROUP BY course.course_name
ORDER BY 平均分 DESC ;

# 23. 查询学号为"2021001"的学生所有课程的分数。
SELECT course.course_name 课程名称, s.score 分数
FROM course
JOIN score s on course.course_id = s.course_id
WHERE s.student_id = '2021001';

# 24. 查询所有学生的姓名、选修的课程名称和分数。
SELECT student.name 学生姓名, c.course_name 课程名称, s.score 分数
FROM student
JOIN score s on student.student_id = s.student_id
JOIN course c on s.course_id = c.course_id;

# 25. 查询每个教师所教授课程的平均分数。
SELECT teacher.name 教师姓名, c.course_name 课程名称, ROUND(AVG(s.score), 1) AS 平均分
FROM teacher
JOIN course c on teacher.teacher_id = c.teacher_id
JOIN score s on c.course_id = s.course_id
GROUP BY teacher.name, c.course_name ;

# 26. 查询分数在80到90之间的学生姓名和课程名称。
SELECT student.name 学生姓名, c.course_name 课程名称
FROM student
JOIN score s on student.student_id = s.student_id
JOIN course c on s.course_id = c.course_id
WHERE score > 80 AND score < 90;

# 27. 查询每个班级的平均分数。
SELECT student.my_class 班级, ROUND(AVG(average), 1) AS 平均分
FROM (
    SELECT student.student_id, AVG(score) AS average
    FROM student
    JOIN score s on student.student_id = s.student_id
    GROUP BY s.student_id
     ) AS average_score
JOIN student on student.student_id = average_score.student_id
GROUP BY student.my_class ;

# 28. 查询没学过"王讲师"老师讲授的任一门课程的学生姓名。
SELECT student.name 学生姓名
FROM student
WHERE student_id NOT IN (
    SELECT student.student_id
    FROM student
    JOIN score s on student.student_id = s.student_id
    JOIN course c on s.course_id = c.course_id
    JOIN teacher t on c.teacher_id = t.teacher_id
    WHERE t.name = '王讲师'
);

# 29. 查询两门及其以上小于85分的同学的学号，姓名及其平均成绩。
SELECT student.student_id 学号, student.name 学生姓名, ROUND(avg.average, 1) AS 平均分
FROM (
    SELECT score.student_id, COUNT(score)
    FROM score
    WHERE score < 85
    GROUP BY student_id
    ) AS xiaoyu,
    (
    SELECT score.student_id, AVG(score.score) AS average
    FROM score
    GROUP BY student_id
    ) AS avg,
    student
WHERE xiaoyu.student_id = avg.student_id AND avg.student_id = student.student_id;

# 30. 查询所有学生的总分并按降序排列。
SELECT student.name 学生姓名, SUM(score) AS 总分
FROM student
JOIN score s on student.student_id = s.student_id
GROUP BY student.name
ORDER BY 总分 DESC ;

# 31. 查询平均分数超过85分的课程名称。
SELECT course_name 课程名称
FROM course
JOIN score s on course.course_id = s.course_id
GROUP BY course.course_id
HAVING AVG(s.score) > 85;

# 32. 查询每个学生的平均成绩排名。
SELECT DENSE_RANK() OVER(ORDER BY AVG(score) DESC) AS 排名, student.name 学生姓名
FROM student
JOIN score s on student.student_id = s.student_id
GROUP BY s.student_id;

# 33. 查询每门课程分数最高的学生姓名和分数。
SELECT course_name 课程名,
       name 学生姓名,
       score 分数
FROM (
    SELECT course_name,
       name,
       score,
       DENSE_RANK() over (
           PARTITION BY course_name
           ORDER BY score DESC
           ) AS 'Rank'
    FROM course
    JOIN score s on course.course_id = s.course_id
    JOIN student st on s.student_id = st.student_id
     ) as ranking
WHERE `Rank` = 1;

# 34. 查询选修了"高等数学"和"大学物理"的学生姓名。
SELECT student.name 学生姓名
FROM (
    SELECT student.student_id
    FROM student
    JOIN score s on student.student_id = s.student_id
    JOIN course c on s.course_id = c.course_id
    WHERE course_name = '高等数学'
     ) AS gs,
    (
    SELECT student.student_id
    FROM student
    JOIN score s on student.student_id = s.student_id
    JOIN course c on s.course_id = c.course_id
    WHERE course_name = '大学物理'
     ) AS wl,
    student
WHERE gs.student_id = wl.student_id AND wl.student_id = student.student_id;

# 35. 按平均成绩从高到低显示所有学生的所有课程的成绩以及平均成绩（没有选课则为空）。
SELECT DISTINCT score.*,ROUND(avg, 1) 平均分, DENSE_RANK() over (ORDER BY avg DESC ) AS 排名
FROM (
        SELECT student_id, AVG(score) AS avg
        FROM score
        GROUP BY student_id
     ) AS average
LEFT JOIN  score on score.student_id = average.student_id;

# 36. 查询分数最高和最低的学生姓名及其分数。
# 37. 查询每个班级的最高分和最低分。
SELECT my_class 班级,avg 平均分
FROM (
    SELECT my_class, avg, DENSE_RANK() over (PARTITION BY my_class ORDER BY avg DESC ) t
    FROM
        (
        SELECT my_class, name, AVG(s.score) AS avg
        FROM student
        JOIN score s on student.student_id = s.student_id
        GROUP BY student.student_id
        ) AS topavg) AS top
WHERE top.t = 1
UNION
SELECT my_class,avg
FROM (
    SELECT my_class, avg, DENSE_RANK() over (PARTITION BY my_class ORDER BY avg ASC ) b
    FROM (
    SELECT my_class, name, AVG(s.score) AS avg
    FROM student
    JOIN score s on student.student_id = s.student_id
    GROUP BY student.student_id
     ) AS bottomavg) AS bottom
WHERE bottom.b = 1
ORDER BY 班级;

# 38. 查询每门课程的优秀率（优秀为90分）。
SELECT t.course_name 课程名, IFNULL(CONCAT(ROUND((outstanding / total) * 100, 2), '%') , 0.00) 优秀率
FROM (
    SELECT course_name, COUNT(score) total
    FROM course
    JOIN score s on course.course_id = s.course_id
    GROUP BY course_name) AS t
LEFT JOIN
    (
    SELECT course_name, COUNT(score) outstanding
    FROM course
    JOIN score s on course.course_id = s.course_id
    WHERE score > 90
    GROUP BY course_name) AS o on t.course_name = o.course_name;

# 39. 查询平均分数超过班级平均分数的学生。
SELECT name 学生姓名, ROUND(average, 1) AS 平均分, ROUND(class_average, 1) AS 班级平均分
FROM (
    SELECT my_class, name, AVG(score) AS average
    FROM student
    JOIN score s on student.student_id = s.student_id
    GROUP BY s.student_id) AS person_avg
JOIN (
    SELECT student.my_class, AVG(average) AS class_average
    FROM (
        SELECT student.student_id, AVG(score) AS average
        FROM student
        JOIN score s on student.student_id = s.student_id
        GROUP BY s.student_id
         ) AS average_score
    JOIN student on student.student_id = average_score.student_id
    GROUP BY student.my_class) AS class_avg on class_avg.my_class = person_avg.my_class
WHERE average > class_average;

# 40. 查询每个学生的分数及其与课程平均分的差值。
SELECT name 学生姓名, ROUND(ABS(average - class_average), 1) AS 分数与班级平均分差值
FROM (
    SELECT my_class, name, AVG(score) AS average
    FROM student
    JOIN score s on student.student_id = s.student_id
    GROUP BY s.student_id) AS person_avg
JOIN (
    SELECT student.my_class, AVG(average) AS class_average
    FROM (
        SELECT student.student_id, AVG(score) AS average
        FROM student
        JOIN score s on student.student_id = s.student_id
        GROUP BY s.student_id
         ) AS average_score
    JOIN student on student.student_id = average_score.student_id
    GROUP BY student.my_class) AS class_avg on class_avg.my_class = person_avg.my_class;

# 41. 查询至少有一门课程分数低于80分的学生姓名。
SELECT name 学生姓名
FROM student
WHERE student_id IN (
    SELECT student_id
    FROM score
    WHERE score < 80
    );

# 42. 查询所有课程分数都高于85分的学生姓名。
SELECT name 学生姓名
FROM student
WHERE student_id NOT IN (
    SELECT student_id
    FROM score
    WHERE score < 85
    );
# 43. 查询查询平均成绩大于等于90分的同学的学生编号和学生姓名和平均成绩。
SELECT student.student_id 学号, name 学生姓名, AVG(score) AS 平均分
FROM student
JOIN score s on student.student_id = s.student_id
GROUP BY 学号, 学生姓名
HAVING 平均分 > 90;

# 44. 查询选修课程数量最少的学生姓名。
SELECT name 学生姓名
FROM (
    SELECT student.name ,DENSE_RANK() over (PARTITION BY name ORDER BY COUNT(s.student_id) ASC ) AS ranking
    FROM student
    JOIN score s on student.student_id = s.student_id
    JOIN course c on s.course_id = c.course_id
    GROUP BY s.student_id
     ) AS sort
WHERE ranking = 1;

# 45. 查询每个班级的第2名学生（按平均分数排名）。
SELECT my_class 班级, name 学生姓名
FROM (
    SELECT my_class, student.name, DENSE_RANK() over (PARTITION BY my_class ORDER BY AVG(score) DESC ) as average_sort
    FROM student
    JOIN score s on student.student_id = s.student_id
    GROUP BY student.student_id
     ) AS second
WHERE average_sort = 2;

# 46. 查询每门课程分数前三名的学生姓名和分数。
SELECT my_class 班级, name 学生姓名
FROM (
    SELECT my_class, student.name, DENSE_RANK() over (PARTITION BY my_class ORDER BY AVG(score) DESC ) as average_sort
    FROM student
    JOIN score s on student.student_id = s.student_id
    GROUP BY student.student_id
     ) AS second
WHERE average_sort = 1 OR average_sort = 2 OR average_sort = 3;

# 47. 查询平均分数最高和最低的班级。
SELECT my_class 班级, avg 平均分
FROM(
    SELECT my_class, ROUND(AVG(score), 1) AS avg, DENSE_RANK() over (ORDER BY ROUND(AVG(score), 1) DESC ) as sort
    FROM student
    JOIN score s on student.student_id = s.student_id
    GROUP BY my_class
    ) AS top
WHERE sort = 1
UNION
SELECT my_class, avg
FROM(
    SELECT my_class, ROUND(AVG(score), 1) AS avg, DENSE_RANK() over (ORDER BY ROUND(AVG(score), 1) ASC ) sort
    FROM student
    JOIN score s on student.student_id = s.student_id
    GROUP BY my_class ) AS bottom
WHERE sort = 1;

# 48. 查询每个学生的总分和他所在班级的平均分数。
SELECT name 姓名, sum 总分, class_avg 班级平均分
FROM (
    SELECT my_class, name, SUM(score) AS sum
    FROM student
    JOIN score s on student.student_id = s.student_id
    GROUP BY s.student_id
    )  sum
JOIN (
    SELECT my_class, ROUND(AVG(score), 1) AS class_avg
    FROM student
    JOIN score s on student.student_id = s.student_id
    GROUP BY my_class
) avg on sum.my_class = avg.my_class;

# 49. 查询每个学生的最高分的课程名称, 学生名称，成绩。
SELECT course_name 课程名称, name 学生姓名, score 成绩
FROM (
    SELECT course_name, name, score, DENSE_RANK() over (PARTITION BY name ORDER BY score DESC ) AS sort
    FROM student
    JOIN score s on student.student_id = s.student_id
    JOIN course c on s.course_id = c.course_id
    ) AS top
WHERE sort = 1;

# 50. 查询每个班级的学生人数和平均年龄。
SELECT ps.my_class 班级, people_sum 总人数, year_avg 平均年龄
FROM (
    SELECT my_class, COUNT(student_id) AS people_sum
    FROM student
    GROUP BY my_class
     ) ps
JOIN
    (
     SELECT my_class, ROUND(AVG(TIMESTAMPDIFF(YEAR, birth_date, NOW())), 1) AS year_avg
    FROM student
    GROUP BY my_class
    ) ya on ps.my_class = ya.my_class;



-- --------------------------------------
-- 练习50题
-- --------------------------------------


# 1.查询所有员工的姓名、邮箱和工作岗位。
SELECT CONCAT(first_name, ' ', last_name) 姓名, email 邮箱, job_title 工作岗位
FROM employees;

# 2.查询所有部门的名称和位置。
SELECT dept_name 部门, location 位置
FROM departments;

# 3.查询工资超过70000的员工姓名和工资。
SELECT CONCAT(first_name, ' ', last_name) 姓名, salary 工资
FROM employees
WHERE salary > 70000;

# 4.查询IT部门的所有员工
SELECT CONCAT(first_name, ' ', last_name) 姓名
FROM employees
INNER JOIN departments ON employees.dept_id = departments.dept_id
WHERE dept_name = 'IT';

# 5.查询入职日期在2020年之后的员工信息。
SELECT *
FROM employees
WHERE hire_date > '2020-1-1';

# 6. 计算每个部门的平均工资。
SELECT dept_name 部门, ROUND(AVG(salary), 2) AS 平均工资
FROM employees
INNER JOIN departments ON employees.dept_id = departments.dept_id
GROUP BY dept_name;

# 7. 查询工资最高的前3名员工信息。
SELECT *
FROM employees
ORDER BY salary DESC
LIMIT 3;

# 8. 查询每个部门员工数量。
SELECT dept_name 部门, COUNT(dept_name) AS 员工数量
FROM employees
INNER JOIN departments ON employees.dept_id = departments.dept_id
GROUP BY dept_name;

# 9. 查询没有分配部门的员工。
SELECT CONCAT(first_name, ' ', last_name) 姓名
FROM employees, departments
WHERE employees.dept_id = departments.dept_id AND dept_name IS NULL;

# 10. 查询参与项目数量最多的员工。
SELECT CONCAT(first_name, ' ', last_name) 姓名
FROM (
    SELECT employees.first_name, employees.last_name, DENSE_RANK() over (ORDER BY COUNT(project_id) DESC )  p_sum_sort
    FROM employees
    JOIN employee_projects ep on employees.emp_id = ep.emp_id
    GROUP BY employees.first_name, employees.last_name
     ) AS sort
WHERE p_sum_sort = 1;

# 11. 计算所有员工的工资总和。
SELECT SUM(salary) 工资总和
FROM employees;

# 12. 查询姓"Smith"的员工信息。
SELECT *
FROM employees
WHERE last_name = 'Smith';

# 13. 查询即将在半年内到期的项目。
SELECT *
FROM projects
WHERE TIMESTAMPDIFF(YEAR, end_date,NOW()) < 0.5;

# 14. 查询至少参与了两个项目的员工。
SELECT CONCAT(first_name, ' ', last_name) 姓名
FROM (
    SELECT first_name, last_name, COUNT(project_id) AS projects
    FROM employees, employee_projects
    WHERE employees.emp_id = employee_projects.emp_id
    GROUP BY employees.emp_id) AS project
WHERE projects >= 2;

# 15. 查询没有参与任何项目的员工。
SELECT CONCAT(first_name, ' ', last_name) 姓名
FROM employees, employee_projects
WHERE employees.emp_id = employee_projects.emp_id AND project_id IS NULL;

# 16. 计算每个项目参与的员工数量。
SELECT projects.project_name 项目名称, COUNT(employees.emp_id) 员工数量
FROM employees, employee_projects, projects
WHERE employees.emp_id = employee_projects.emp_id AND employee_projects.project_id = projects.project_id
GROUP BY projects.project_name;

# 17. 查询工资第二高的员工信息。
SELECT *
FROM employees
ORDER BY salary DESC
LIMIT 1, 1;

# 18. 查询每个部门工资最高的员工。
SELECT dept_name 部门, CONCAT(first_name, ' ', last_name) 姓名
FROM (
    SELECT dept_name, first_name, last_name, DENSE_RANK() over (PARTITION BY dept_name ORDER BY salary DESC ) s_sort
    FROM employees
    JOIN departments d on employees.dept_id = d.dept_id
     ) AS sort
WHERE s_sort = 1;

# 19. 计算每个部门的工资总和,并按照工资总和降序排列。
SELECT departments.dept_name 部门, SUM(employees.salary) AS 总工资
FROM departments, employees
WHERE employees.dept_id = departments.dept_id
GROUP BY departments.dept_name
ORDER BY 总工资 DESC ;

# 20. 查询员工姓名、部门名称和工资。
SELECT CONCAT(first_name, ' ', last_name) 姓名, dept_name 部门, salary 工资
FROM employees
INNER JOIN departments d ON employees.dept_id = d.dept_id;

# 21. 查询每个员工的上级主管(假设emp_id小的是上级)。
SELECT CONCAT(f1, ' ', l1) 员工姓名, dept_name 部门, CONCAT(f2, ' ', l2) 主管姓名
FROM (
    SELECT dept_sort.first_name f1, dept_sort.last_name l1, dept_name, dept_sort2.emp_id, dept_sort2.first_name f2, dept_sort2.last_name l2,
           DENSE_RANK() over (PARTITION BY dept_sort.first_name, dept_sort.last_name ORDER BY dept_sort2.emp_id ASC ) sort
    FROM (
        SELECT first_name, last_name, dept_name, employees.dept_id, emp_id, DENSE_RANK() over (PARTITION BY d.dept_id ORDER BY emp_id DESC ) sort
        FROM employees
        JOIN departments d on d.dept_id = employees.dept_id
        ) AS dept_sort
    JOIN (
        SELECT  employees.dept_id, emp_id, first_name, last_name, DENSE_RANK() over (PARTITION BY d.dept_id ORDER BY emp_id DESC ) sort
        FROM employees
        JOIN departments d on d.dept_id = employees.dept_id
    ) AS dept_sort2 on dept_sort.dept_id = dept_sort2.dept_id
    WHERE dept_sort.emp_id != dept_sort2.emp_id AND dept_sort.emp_id < dept_sort2.emp_id
     ) AS sort2
WHERE sort = 1;


# 22. 查询所有员工的工作岗位,不要重复。
SELECT DISTINCT CONCAT(first_name, ' ', last_name) 姓名, job_title 工作岗位
FROM employees;

# 23. 查询平均工资最高的部门。
SELECT dept_name 部门, ROUND(AVG(salary), 2) AS 平均工资
FROM employees
INNER JOIN departments ON employees.dept_id = departments.dept_id
GROUP BY dept_name
ORDER BY 平均工资 DESC LIMIT 1;

# 24. 查询工资高于其所在部门平均工资的员工。
SELECT dept_name 部门, CONCAT(first_name, ' ', last_name) 姓名, salary 工资, s_avg 平均工资
FROM (
    SELECT employees.first_name, employees.last_name, dept_id, employees.salary
    FROM employees
     ) AS e_s
JOIN (
    SELECT dept_id, ROUND(AVG(employees.salary), 2) s_avg
    FROM employees
    GROUP BY dept_id
    ) AS e_s_avg on e_s.dept_id = e_s_avg.dept_id
JOIN departments on e_s.dept_id = departments.dept_id
GROUP BY dept_name, first_name, last_name, salary, s_avg
HAVING salary > s_avg;

# 25. 查询每个部门工资前两名的员工。
SELECT dept_name 部门, CONCAT(first_name, ' ', last_name) 姓名, salary 工资
FROM (
    SELECT dept_name, first_name, last_name, salary, DENSE_RANK() over (PARTITION BY dept_name ORDER BY salary DESC ) sort
    FROM (
        SELECT employees.first_name, employees.last_name, dept_id, employees.salary
        FROM employees
         ) AS e_s
    JOIN departments on e_s.dept_id = departments.dept_id
     ) AS s_sort
WHERE sort = 1 OR sort = 2;

# 27. 查询每个员工的工作年限,并按工作年限降序排序。
SELECT CONCAT(first_name, ' ', last_name) 姓名, TIMESTAMPDIFF(YEAR , employees.hire_date, NOW()) 工作年限
FROM employees
ORDER BY 工作年限 DESC;

# 29. 查询即将在90天内到期的项目和负责该项目的员工。
SELECT CONCAT(first_name, ' ', last_name) 姓名,TIMESTAMPDIFF(DAY, NOW(), end_date) AS 剩余时间
FROM employees, employee_projects, projects
WHERE employees.emp_id = employee_projects.emp_id AND projects.project_id = employee_projects.project_id
AND TIMESTAMPDIFF(DAY, NOW(), end_date) < 90 AND TIMESTAMPDIFF(DAY, NOW(), end_date) > 0;

# 30. 计算每个项目的持续时间(天数)。
SELECT projects.project_name 项目名称, TIMESTAMPDIFF(DAY, projects.start_date, projects.end_date) 持续时间
FROM projects;

# 32. 查询员工数量最多的部门。
SELECT dept_name 部门, COUNT(dept_name) AS 员工人数
FROM employees
INNER JOIN departments ON employees.dept_id = departments.dept_id
GROUP BY dept_name
ORDER BY 员工人数 DESC
LIMIT 1;

# 33. 查询参与项目最多的部门。
SELECT dept_name 部门
FROM (
    SELECT departments.dept_name, DENSE_RANK() over (ORDER BY COUNT(ep.project_id) DESC ) sort
    FROM departments
    JOIN employees e on departments.dept_id = e.dept_id
    JOIN employee_projects ep on e.emp_id = ep.emp_id
    GROUP BY departments.dept_name
     ) AS dept_sort
WHERE sort = 1;

# 35. 查询入职时间最长的3名员工。
SELECT employees.*, TIMESTAMPDIFF(DAY, hire_date, NOW()) AS 入职时间
FROM employees
ORDER BY 入职时间 DESC
LIMIT 3;

# 36. 查询名字和姓氏相同的员工。
SELECT CONCAT(first_name, ' ', last_name) 姓名
FROM employees
WHERE first_name = last_name;

# 39. 查询姓名包含"son"的员工信息。
SELECT *
FROM employees
WHERE first_name LIKE '%son%' OR last_name LIKE '%son%';
