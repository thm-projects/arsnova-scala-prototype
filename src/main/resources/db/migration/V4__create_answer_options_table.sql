CREATE TABLE answer_options (
  id INT NOT NULL AUTO_INCREMENT,
  question_id INT NOT NULL,
  correct TINYINT(1) NOT NULL,
  content TEXT NOT NULL,
  points INT NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT possible_answer_question_fk FOREIGN KEY (question_id) REFERENCES questions(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=INNODB;