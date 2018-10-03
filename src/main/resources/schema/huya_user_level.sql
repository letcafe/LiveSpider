DROP TABLE IF EXISTS huya_user_level;

CREATE TABLE huya_user_level
(
  id               INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  yy_id            VARCHAR(25)                        NOT NULL,
  curr_level_exp   VARCHAR(10),
  next_level_exp   VARCHAR(10),
  next_2_level_exp VARCHAR(10),
  user_exp         VARCHAR(10),
  user_level       VARCHAR(5),
  birthday         DATE,
  gender           INTEGER,
  sign             VARCHAR(11),
  area             VARCHAR(11),
  location         VARCHAR(10),
  nick             VARCHAR(20),
  level_progress   INTEGER,
  daily_inc_exp    INTEGER,
  create_time      DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time      DATETIME ON UPDATE CURRENT_TIMESTAMP
)
  DEFAULT CHARSET UTF8MB4