-- 1. 用户表（存储用户的基本信息）
CREATE TABLE user
(
    id         INT AUTO_INCREMENT PRIMARY KEY, -- 用户唯一标识
    username   VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名，唯一标识每个用户',
    password   VARCHAR(255) NOT NULL COMMENT '加密后的用户密码',
    email      VARCHAR(100) NOT NULL UNIQUE COMMENT '用户的邮箱地址，用于联系和验证',
    role       ENUM('普通用户', '管理员') NOT NULL COMMENT '用户角色，区分普通用户、管理员',
    identity   ENUM('管理员', '求职者', '企业HR') COMMENT '用户身份，区分管理员，求职者和企业HR',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户信息最后更新时间'
);

-- 2. 简历表（存储求职者的简历信息）
CREATE TABLE resume
(
    id           INT AUTO_INCREMENT PRIMARY KEY,                 -- 简历唯一标识
    user_id      INT         NOT NULL COMMENT '关联的用户ID，表示简历的所有者',
    name         VARCHAR(50) NOT NULL COMMENT '求职者的姓名',
    contact_info VARCHAR(200) COMMENT '联系方式，包括电话和邮箱',
    education    TEXT COMMENT '教育背景信息',
    experience   TEXT COMMENT '工作经验',
    skills       TEXT COMMENT '技能列表，描述求职者具备的技能',
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '简历创建时间',
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '简历最后更新时间',
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE -- 外键关联用户表
);

-- 3. 职位表（存储企业发布的职位信息）
CREATE TABLE job
(
    id           INT AUTO_INCREMENT PRIMARY KEY,               -- 职位唯一标识
    hr_id        INT          NOT NULL COMMENT '发布职位的HR用户ID',
    title        VARCHAR(100) NOT NULL COMMENT '职位标题',
    description  TEXT COMMENT '职位的详细描述，包括职责和福利',
    requirements TEXT COMMENT '职位要求，包括学历、经验等',
    location     VARCHAR(100) COMMENT '工作地点',
    salary_range VARCHAR(50) COMMENT '薪资范围',
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '职位创建时间',
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '职位最后更新时间',
    FOREIGN KEY (hr_id) REFERENCES user (id) ON DELETE CASCADE -- 外键关联用户表
);

-- 4. 简历投递表（记录求职者对职位的投递情况）
CREATE TABLE application
(
    id         INT AUTO_INCREMENT PRIMARY KEY,                        -- 投递记录唯一标识
    resume_id  INT NOT NULL COMMENT '投递的简历ID',
    job_id     INT NOT NULL COMMENT '目标职位的ID',
    status     ENUM('已投递', '已查看', '面试中', '已完成') DEFAULT '已投递' COMMENT '投递状态',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '投递时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '投递记录最后更新时间',
    FOREIGN KEY (resume_id) REFERENCES resume (id) ON DELETE CASCADE, -- 外键关联简历表
    FOREIGN KEY (job_id) REFERENCES job (id) ON DELETE CASCADE        -- 外键关联职位表
);

-- 5. 在线面试安排表（记录在线面试的时间和安排）
CREATE TABLE interview_schedule
(
    id             INT AUTO_INCREMENT PRIMARY KEY,                             -- 面试安排唯一标识
    application_id INT      NOT NULL COMMENT '关联的投递记录ID',
    interview_time DATETIME NOT NULL COMMENT '面试时间',
    platform       VARCHAR(100) COMMENT '面试平台，例如Zoom或Teams',
    status         ENUM('待面试', '已完成') DEFAULT '待面试' COMMENT '面试状态',
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '面试安排创建时间',
    updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '面试安排最后更新时间',
    FOREIGN KEY (application_id) REFERENCES application (id) ON DELETE CASCADE -- 外键关联投递表
);

-- 6. 企业评价表（记录用户对企业的评价信息）
CREATE TABLE company_review
(
    id           INT AUTO_INCREMENT PRIMARY KEY,                 -- 企业评价唯一标识
    user_id      INT          NOT NULL COMMENT '评价的用户ID',
    company_name VARCHAR(100) NOT NULL COMMENT '被评价的公司名称',
    rating       TINYINT(1) CHECK (rating BETWEEN 1 AND 5) COMMENT '评分，范围1-5',
    comments     TEXT COMMENT '用户对企业的评价内容',
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评价创建时间',
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE -- 外键关联用户表
);

-- 插入用户表数据
-- 初始密码均为 z123456
INSERT INTO user (username, password, email, role, identity, created_at)
VALUES ('admin', '$2a$10$oCIHeDpCOp46OU1fRCWBeei1gqwtveOZV3.ad9Q/Gbhw2ZeRrQvxu', 'admin@admin.com', '管理员', '管理员',
        '2023-01-15 09:30:00'),
       ('zhangwei', '$2a$10$oCIHeDpCOp46OU1fRCWBeei1gqwtveOZV3.ad9Q/Gbhw2ZeRrQvxu', 'zhang.wei@example.com', '普通用户',
        '求职者', '2023-02-10 14:45:00'),
       ('lina', '$2a$10$oCIHeDpCOp46OU1fRCWBeei1gqwtveOZV3.ad9Q/Gbhw2ZeRrQvxu', 'li.na@example.com', '普通用户',
        '求职者', '2023-03-05 11:20:00'),
       ('wangfang', '$2a$10$oCIHeDpCOp46OU1fRCWBeei1gqwtveOZV3.ad9Q/Gbhw2ZeRrQvxu', 'wang.fang@example.com', '普通用户',
        '求职者', '2023-04-12 16:00:00'),
       ('liuyang', '$2a$10$oCIHeDpCOp46OU1fRCWBeei1gqwtveOZV3.ad9Q/Gbhw2ZeRrQvxu', 'liu.yang@example.com', '普通用户',
        '求职者', '2023-05-18 09:15:00'),
       ('chenhong', '$2a$10$oCIHeDpCOp46OU1fRCWBeei1gqwtveOZV3.ad9Q/Gbhw2ZeRrQvxu', 'chen.hong@example.com', '普通用户',
        '企业HR', '2023-06-22 13:30:00'),
       ('yangming', '$2a$10$oCIHeDpCOp46OU1fRCWBeei1gqwtveOZV3.ad9Q/Gbhw2ZeRrQvxu', 'yang.ming@example.com', '普通用户',
        '企业HR', '2023-07-10 10:45:00'),
       ('zhaolei', '$2a$10$oCIHeDpCOp46OU1fRCWBeei1gqwtveOZV3.ad9Q/Gbhw2ZeRrQvxu', 'zhao.lei@example.com', '普通用户',
        '企业HR', '2023-08-15 15:00:00'),
       ('sunyue', '$2a$10$oCIHeDpCOp46OU1fRCWBeei1gqwtveOZV3.ad9Q/Gbhw2ZeRrQvxu', 'sun.yue@example.com', '普通用户',
        '求职者', '2023-09-20 11:30:00'),
       ('zhoujie', '$2a$10$oCIHeDpCOp46OU1fRCWBeei1gqwtveOZV3.ad9Q/Gbhw2ZeRrQvxu', 'zhou.jie@example.com', '普通用户',
        '求职者', '2023-10-05 14:00:00'),
       ('hubing', '$2a$10$oCIHeDpCOp46OU1fRCWBeei1gqwtveOZV3.ad9Q/Gbhw2ZeRrQvxu', 'hu.bing@example.com', '普通用户',
        '求职者', '2023-11-01 09:45:00');

-- 插入简历表数据
INSERT INTO resume (user_id, name, contact_info, education, experience, skills, created_at)
VALUES (2, '张伟', '1234567890, zhang.wei@example.com', '计算机科学学士', '3年在XYZ公司', 'Java, Python, SQL',
        '2023-02-11 10:00:00'),
       (3, '李娜', '0987654321, li.na@example.com', '数据科学硕士', '2年在ABC公司', 'R, SQL, 机器学习',
        '2023-03-06 12:00:00'),
       (4, '王芳', '1122334455, wang.fang@example.com', '软件工程学士', '1年在科技解决方案公司', 'JavaScript, Node.js',
        '2023-04-13 14:00:00'),
       (5, '刘洋', '5566778899, liu.yang@example.com', '网页开发学士', '自由职业', 'HTML, CSS, JavaScript',
        '2023-05-19 16:00:00'),
       (6, '陈红', '6677889900, chen.hong@example.com', '人工智能博士', '5年在斯塔克工业', 'AI, 深度学习, Python',
        '2023-06-23 10:00:00'),
       (7, '杨明', '2233445566, yang.ming@example.com', '工商管理硕士', '10年在韦恩企业', '管理, 领导力, 策略',
        '2023-07-11 11:00:00'),
       (8, '赵磊', '7788996677, zhao.lei@example.com', '市场营销学士', '2年在每日星报', 'SEO, 数字营销',
        '2023-08-16 13:00:00'),
       (9, '孙悦', '3344556677, sun.yue@example.com', '网络安全硕士', '3年在神盾局', '网络安全, 伦理黑客',
        '2023-09-21 15:00:00'),
       (10, '周杰', '8899001122, zhou.jie@example.com', '物理学学士', '3年在神盾局', '策略, 团队管理',
        '2023-10-06 17:00:00'),
       (11, '胡兵', '2233448899, hu.bing@example.com', '核物理博士', '2年在斯塔克工业', '研究, 数据分析',
        '2023-11-02 09:00:00');

-- 插入职位表数据
INSERT INTO job (hr_id, title, description, requirements, location, salary_range, created_at)
VALUES (6, 'Java开发工程师', '负责开发和维护Java应用程序', '要求3年及以上工作经验，熟悉Spring框架', '北京', '10K-15K',
        '2023-01-20 10:00:00'),
       (7, '前端开发工程师', '负责网站前端开发工作，优化用户体验', '要求熟练掌握HTML、CSS、JavaScript', '上海', '8K-12K',
        '2023-02-25 11:00:00'),
       (8, '市场营销经理', '负责制定市场战略，提升品牌影响力', '要求3年以上市场营销经验', '北京', '15K-20K',
        '2023-03-30 12:00:00'),
       (9, '网络安全工程师', '负责企业信息系统的安全保护工作', '要求有一定的网络安全经验', '深圳', '12K-18K',
        '2023-04-15 13:00:00'),
       (10, '数据分析师', '负责数据的收集、分析与报告工作', '要求熟悉数据分析工具，如Python、SQL', '上海', '10K-15K',
        '2023-05-20 14:00:00'),
       (6, 'AI工程师', '开发人工智能解决方案', '要求硕士以上学历，掌握深度学习技术', '北京', '18K-25K',
        '2023-06-25 15:00:00'),
       (7, '产品经理', '负责产品规划和协调工作', '要求具有至少2年产品管理经验', '广州', '12K-18K',
        '2023-07-30 16:00:00'),
       (8, '人力资源经理', '负责公司的人力资源规划和管理工作', '要求有HR相关经验', '北京', '14K-20K',
        '2023-08-05 17:00:00'),
       (9, '后端开发工程师', '负责开发和维护后端服务', '要求3年及以上工作经验，熟悉Java或Python', '上海', '10K-15K',
        '2023-09-10 18:00:00'),
       (10, 'UI设计师', '负责产品UI界面的设计与优化', '要求有2年以上设计经验', '深圳', '9K-14K', '2023-10-15 19:00:00');

-- 插入简历投递表数据
INSERT INTO application (resume_id, job_id, status, created_at)
VALUES (1, 1, '已投递', '2023-01-21 10:30:00'),
       (2, 2, '已投递', '2023-02-26 11:30:00'),
       (3, 3, '已投递', '2023-03-31 12:30:00'),
       (4, 4, '已查看', '2023-04-16 13:30:00'),
       (5, 5, '已查看', '2023-05-21 14:30:00'),
       (6, 6, '已查看', '2023-06-26 15:30:00'),
       (7, 7, '已投递', '2023-07-31 16:30:00'),
       (8, 8, '已投递', '2023-08-06 17:30:00'),
       (9, 9, '已查看', '2023-09-11 18:30:00'),
       (10, 10, '已投递', '2023-10-16 19:30:00');

-- 插入企业评价表数据
INSERT INTO company_review (user_id, company_name, rating, comments, created_at)
VALUES (2, 'XYZ公司', 4, '公司待遇不错，团队氛围良好。', '2023-01-24 11:15:00'),
       (3, 'ABC公司', 5, '公司文化非常好，机会多。', '2023-03-01 12:15:00'),
       (4, '科技解决方案公司', 3, '工作压力较大，项目进度紧张。', '2023-04-03 13:15:00'),
       (8, '斯塔克工业', 5, '企业技术先进，创新氛围浓厚。', '2023-05-24 15:15:00'),
       (6, '韦恩企业', 5, '非常重视员工发展，有很好的晋升机会。', '2023-06-29 16:15:00'),
       (7, '每日星报', 4, '媒体行业节奏快，职业发展前景不错。', '2023-08-03 17:15:00'),
       (9, '神盾局', 5, '挑战性工作，团队协作紧密，值得推荐。', '2023-08-09 18:15:00'),
       (10, '神盾局', 5, '稳定，工作氛围好，员工福利不错。', '2023-09-14 19:15:00'),
       (11, '斯塔克工业', 5, '科技公司，员工待遇好，工作环境非常好。', '2023-10-19 20:15:00');
