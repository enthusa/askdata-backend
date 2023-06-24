CREATE TABLE bi_post (
  id          INT               AUTO_INCREMENT PRIMARY KEY,
  title       VARCHAR(255),
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);