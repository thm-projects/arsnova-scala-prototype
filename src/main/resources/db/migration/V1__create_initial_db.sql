CREATE TABLE users (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL,
  pwd VARCHAR(255) NOT NULL,
  PRIMARY KEY(id)
) ENGINE=INNODB;

CREATE TABLE sessions (
  id INT NOT NULL AUTO_INCREMENT,
  keyword VARCHAR(8) NOT NULL,
  user_id INT NOT NULL,
  title VARCHAR(255) NOT NULL,
  short_name VARCHAR(255) NOT NULL,
  last_owner_activity varchar(30) NOT NULL,
  creation_time varchar(30) NOT NULL,
  active TINYINT(1) NOT NULL DEFAULT 0,
  feedback_lock TINYINT(1) NOT NULL DEFAULT 0,
  flip_flashcards TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY(id),
  CONSTRAINT session_user_fk FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=INNODB;

CREATE TABLE questions (
  id INT NOT NULL AUTO_INCREMENT,
  session_id INT NOT NULL,
  subject VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  variant VARCHAR(255) NOT NULL,
  format VARCHAR(255) NOT NULL,
  hint TEXT,
  solution TEXT,
  active TINYINT(1) NOT NULL DEFAULT 0,
  voting_disabled TINYINT(1) NOT NULL DEFAULT 1,
  show_statistic TINYINT(1) NOT NULL DEFAULT 0,
  show_answer TINYINT(1) NOT NULL DEFAULT 0,
  abstention_allowed TINYINT(1) NOT NULL DEFAULT 1,
  format_attributes TEXT,
  PRIMARY KEY(id),
  CONSTRAINT question_session_fk FOREIGN KEY (session_id) REFERENCES sessions(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=INNODB;

CREATE TABLE answer_options (
  id INT NOT NULL AUTO_INCREMENT,
  question_id INT NOT NULL,
  correct TINYINT(1) NOT NULL,
  content TEXT NOT NULL,
  points INT NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT possible_answer_question_fk FOREIGN KEY (question_id) REFERENCES questions(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=INNODB;

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

CREATE TABLE features (
  id INT NOT NULL AUTO_INCREMENT,
  session_id INT NOT NULL,
  slides TINYINT(1) NOT NULL DEFAULT 0,
  flashcards TINYINT(1) NOT NULL DEFAULT 0,
  peer_grading TINYINT(1) NOT NULL DEFAULT 0,
  peer_instruction TINYINT(1) NOT NULL DEFAULT 0,
  comments TINYINT(1) NOT NULL DEFAULT 0,
  tile_view TINYINT(1) NOT NULL DEFAULT 0,
  jitt TINYINT(1) NOT NULL DEFAULT 0,
  learning_progress TINYINT(1) NOT NULL DEFAULT 0,
  feedback TINYINT(1) NOT NULL DEFAULT 0,
  live_questions TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY(id),
  CONSTRAINT features_session_fk FOREIGN KEY (session_id) REFERENCES sessions(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=INNODB;

CREATE TABLE session_motds (
  id INT NOT NULL AUTO_INCREMENT,
  startdate VARCHAR(30) NOT NULL,
  enddate VARCHAR(30) NOT NULL,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  audience VARCHAR(255) NOT NULL,
  PRIMARY KEY(id)
) ENGINE=INNODB;

CREATE TABLE global_motds (
  id INT NOT NULL AUTO_INCREMENT,
  session_id INT NOT NULL,
  startdate VARCHAR(30) NOT NULL,
  enddate VARCHAR(30) NOT NULL,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT global_motd_session_fk FOREIGN KEY (session_id) REFERENCES sessions(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=INNODB;
