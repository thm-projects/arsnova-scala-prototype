CREATE TABLE choice_answers (
  id INT NOT NULL AUTO_INCREMENT,
  question_id INT NOT NULL,
  session_id INT NOT NULL,
  answer_option_id INT NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT choice_answer_question_fk FOREIGN KEY (question_id) REFERENCES questions(id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT choice_answer_session_fk FOREIGN KEY (session_id) REFERENCES sessions(id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT choice_answer_answer_option_fk FOREIGN KEY (answer_option_id) REFERENCES answer_options(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=INNODB;