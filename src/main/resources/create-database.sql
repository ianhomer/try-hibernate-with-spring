CREATE DATABASE exercise;
CREATE USER 'exercise'@'localhost' IDENTIFIED BY 'exercise';
GRANT ALL PRIVILEGES ON exercise.* TO 'exercise'@'localhost';
FLUSH PRIVILEGES;