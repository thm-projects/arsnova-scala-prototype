CREATE TABLE comments (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  session_id INT NOT NULL,
  is_read TINYINT(1) NOT NULL DEFAULT 0,
  subject VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  created_at VARCHAR(20) NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT comment_user_fk FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT comment_session_fk FOREIGN KEY (session_id) REFERENCES sessions(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=INNODB;