CREATE TABLE freetext_answers (
  id INT NOT NULL AUTO_INCREMENT,
  question_id INT NOT NULL,
  session_id INT NOT NULL,
  subject TEXT NOT NULL,
  content TEXT NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT freetext_answer_question_fk FOREIGN KEY (question_id) REFERENCES questions(id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT freetext_answer_session_fk FOREIGN KEY (session_id) REFERENCES sessions(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=INNODB;