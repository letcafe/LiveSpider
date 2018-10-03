DROP TABLE IF EXISTS huya_tasks;

CREATE TABLE huya_tasks (
  id                    INTEGER     NOT NULL PRIMARY KEY AUTO_INCREMENT,
  task_id               INTEGER     NOT NULL,
  name                  VARCHAR(25) NOT NULL,
  description           VARCHAR(25),
  enable                VARCHAR(5),
  exper                 INTEGER,
  icon                  VARCHAR(100),
  class_name            VARCHAR(25),
  award_prize           VARCHAR(5),
  progress              INTEGER,
  progress_mode         INTEGER,
  t_prize_id            INTEGER                          DEFAULT NULL,
  sub_task_target_level INTEGER                          DEFAULT NULL,
  target_level          INTEGER,
  type                  INTEGER,
  create_time           DATETIME                         DEFAULT CURRENT_TIMESTAMP,
  update_time           DATETIME ON UPDATE CURRENT_TIMESTAMP
)
  DEFAULT CHARSET UTF8MB4