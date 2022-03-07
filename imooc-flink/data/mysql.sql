USE pk_flink_imooc;

CREATE TABLE student
(
    id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20),
    age int(10)
);

INSERT INTO student VALUES(1, 'ls', 30);
INSERT INTO student VALUES(2, 'zs', 31);
INSERT INTO student VALUES(2, 'pk', 32);
