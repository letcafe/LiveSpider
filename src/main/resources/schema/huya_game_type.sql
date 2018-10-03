DROP TABLE IF EXISTS huya_game_type;

CREATE TABLE huya_game_type
(
  gid         INT(11) PRIMARY KEY NOT NULL,
  name        VARCHAR(30)         NOT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME ON UPDATE CURRENT_TIMESTAMP
)
  DEFAULT CHARSET UTF8MB4