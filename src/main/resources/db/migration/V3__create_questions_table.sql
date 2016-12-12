CREATE TABLE questions (
  id INT NOT NULL AUTO_INCREMENT,
  session_id INT NOT NULL,
  subject VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  variant VARCHAR(255) NOT NULL,
  format VARCHAR(255) NOT NULL,
  format_attributes TEXT,
  PRIMARY KEY(id),
  CONSTRAINT question_session_fk FOREIGN KEY (session_id) REFERENCES sessions(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=INNODB;