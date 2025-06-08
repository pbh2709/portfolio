-- 🧨 외래키 제약조건 일시 해제
SET FOREIGN_KEY_CHECKS = 0;

-- 📦 기존 테이블 제거
DROP TABLE IF EXISTS user_recommend;
DROP TABLE IF EXISTS user_favorite;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS refresh_token;
DROP TABLE IF EXISTS video;
DROP TABLE IF EXISTS user;

-- 🔒 외래키 제약조건 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;

-- 👤 user 테이블
CREATE TABLE user (
    user_id VARCHAR(50) PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    age INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 🎞 video 테이블
CREATE TABLE video (
    video_no INT AUTO_INCREMENT PRIMARY KEY,
    video_title VARCHAR(255) NOT NULL,
    channel_name VARCHAR(50) NOT NULL,
    view_count INT DEFAULT 0,
    upload_date DATETIME DEFAULT NOW(),
    video_like INT DEFAULT 0,
    field VARCHAR(100),
    fitness_part_no INT,
    video_id VARCHAR(100),
    thumbnail_url VARCHAR(500),
    tag VARCHAR(100)
);

-- 📝 review 테이블 (기존 매퍼 쿼리 기준 완전 복원)
CREATE TABLE review (
    review_no INT AUTO_INCREMENT PRIMARY KEY,
    review_title VARCHAR(100) DEFAULT '(제목없음)',
    review_content TEXT,
    video_no INT NOT NULL,
    writer_id VARCHAR(50) NOT NULL,
    reg_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (video_no) REFERENCES video(video_no) ON DELETE CASCADE,
    FOREIGN KEY (writer_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- ⭐ user_favorite 테이블
CREATE TABLE user_favorite (
    favorite_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50),
    video_no INT,
    video_id VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (video_no) REFERENCES video(video_no) ON DELETE CASCADE
);

-- 💡 user_recommend 테이블
CREATE TABLE user_recommend (
    user_id VARCHAR(50),
    video_id VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, video_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- 🔒 refresh_token 테이블
CREATE TABLE refresh_token (
    user_id VARCHAR(50) PRIMARY KEY,
    token TEXT,
    expiry DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- 👥 더미 유저 데이터
INSERT INTO user (user_id, user_name, password, email, age) VALUES
('admin', '관리자', 'admin', 'admin@test.com', 30),
('user01', '김철수', 'pass01', 'chulsoo@test.com', 25),
('user02', '이영희', 'pass02', 'younghee@test.com', 24),
('user03', '박민수', 'pass03', 'minsoo@test.com', 22),
('user04', '최지연', 'pass04', 'jiyoun@test.com', 27),
('user05', '정우성', 'pass05', 'woosung@test.com', 31),
('user06', '한지민', 'pass06', 'jimin@test.com', 29),
('user07', '신동엽', 'pass07', 'dongyeop@test.com', 33),
('user08', '윤아', 'pass08', 'yoona@test.com', 23),
('user09', '서강준', 'pass09', 'kangjoon@test.com', 28),
('user10', '장도연', 'pass10', 'doyeon@test.com', 30),
('user11', '임슬기', 'pass11', 'seulgi@test.com', 27),
('user12', '하준', 'pass12', 'hajun@test.com', 21),
('user13', '배수지', 'pass13', 'suzy@test.com', 26),
('user14', '남주혁', 'pass14', 'joohyuk@test.com', 29),
('user15', '김세정', 'pass15', 'sejeong@test.com', 24),
('user16', '백현', 'pass16', 'baekhyun@test.com', 30),
('user17', '홍은지', 'pass17', 'eunji@test.com', 25),
('user18', '이현우', 'pass18', 'hyunwoo@test.com', 23),
('user19', '송지효', 'pass19', 'jihyo@test.com', 32); 