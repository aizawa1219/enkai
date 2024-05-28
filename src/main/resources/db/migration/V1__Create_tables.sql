/*マイグレーションファイルを作成*/
DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'ユーザID',
  email varchar(255) NOT NULL UNIQUE COMMENT 'メールアドレス',
  password varchar(255) NOT NULL COMMENT 'パスワード',
  created_at datetime NOT NULL DEFAULT current_timestamp() COMMENT '作成日時',
  modified_at datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新日時'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ユーザ';

DROP TABLE IF EXISTS events;
CREATE TABLE events (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'イベントID',
  name varchar(255) NOT NULL COMMENT 'イベント名',
  detail varchar(255) NOT NULL COMMENT 'イベント詳細',
  max_participant int(11) NOT NULL COMMENT '最大参加者数',
  category_id int(11) NOT NULL COMMENT 'カテゴリID',
  user_id int(11) NOT NULL COMMENT 'ユーザID',
  created_at datetime NOT NULL DEFAULT current_timestamp() COMMENT '作成日時',
  modified_at datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新日時'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='イベント';

DROP TABLE IF EXISTS event_users;
CREATE TABLE event_users (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'イベントユーザID',
  event_id int(11) NOT NULL COMMENT 'イベントID',
  user_id int(11) NOT NULL COMMENT 'ユーザID',
  created_at datetime NOT NULL DEFAULT current_timestamp() COMMENT '作成日時',
  modified_at datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新日時'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='イベントユーザ';

DROP TABLE IF EXISTS categories;
CREATE TABLE categories (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'カテゴリID',
  name varchar(255) NOT NULL COMMENT 'カテゴリ名',
  created_at datetime NOT NULL DEFAULT current_timestamp() COMMENT '作成日時',
  modified_at datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新日時'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='カテゴリ';

DROP TABLE IF EXISTS chats;
CREATE TABLE chats (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'チャットID',
  user_id int(11) NOT NULL COMMENT 'ユーザID',
  event_id int(11) NOT NULL COMMENT 'イベントID',
  body varchar(255) NOT NULL COMMENT '本文',
  created_at datetime NOT NULL DEFAULT current_timestamp() COMMENT '作成日時',
  modified_at datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新日時'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='チャット';
