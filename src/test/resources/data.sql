INSERT INTO students
  (id, name, furigana, age, gender, nickname, email, city, remark, isDeleted)
VALUES
  (1,'山田 正志','やまだまさし',20,'男性','だーやま','test0@test.com','北海道','',0),
  (2,'田中太郎','たなかたろう',25,'男性','たろー','tanaka@gmail.com','北海道',NULL,0),
  (3,'竹中ひとみ','たけなかひとみ',30,'女性','ひーちゃん','takenaka@gmail.com','青森県','',0),
  (4,'小林優太','こばやしゆうた',24,'男性','ゆー','test3@test.com','秋田県','',0),
  (5,'小林彩','こばやしあや',32,'その他','あーちゃん','atyan@test.com','東京都','',0);

INSERT INTO students_courses
  (id, student_id, course_name, enrollment_date, completion_date, remark, isDeleted)
VALUES
  (1,1,'Javaコース','2025-09-10 13:44:12','2026-03-10',NULL,0),
  (2,2,'AWS','2025-09-10 13:45:25','2026-03-10',NULL,0),
  (3,3,'AWSコース','2025-09-10 13:45:52','2026-03-10',NULL,0),
  (4,3,'JAVA','2025-09-10 13:46:02','2026-03-10',NULL,0),
  (5,4,'Web制作コース','2025-10-25 10:12:51',NULL,NULL,0),
  (6,5,'マーケティングコース','2025-10-25 18:11:35','2026-10-26',NULL,0);

  ALTER TABLE students ALTER COLUMN id RESTART WITH 100;
  ALTER TABLE students_courses ALTER COLUMN id RESTART WITH 100;
