CREATE TABLE user (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT
    ,username VARCHAR(255) NOT NULL
    ,password VARCHAR(255) NOT NULL
    ,nickname VARCHAR(255) NOT NULL
    ,created_at DATETIME NOT NULL
    ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ,UNIQUE KEY uk_username (username)
);

INSERT INTO user VALUES (1, 'admin', '{bcrypt}$2a$10$f4aQLof9kgM8mzJIP7a.Vuc3WYcQK8brcL6hrHdCdkzTH8AppEpOm', 'Jerry', NOW(), NOW());

CREATE TABLE da_user_count_1min (
    report_minute DATETIME NOT NULL PRIMARY KEY
    ,user_count BIGINT NOT NULL DEFAULT 0
    ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE da_user_count_rt (
    report_minute DATETIME NOT NULL PRIMARY KEY
    ,user_count_1min BIGINT NOT NULL DEFAULT 0
    ,user_count_5min BIGINT NOT NULL DEFAULT 0
    ,user_count_15min BIGINT NOT NULL DEFAULT 0
    ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE da_user_primary_daily (
    report_date INT NOT NULL PRIMARY KEY
    ,user_count BIGINT NOT NULL DEFAULT 0
    ,session_count BIGINT NOT NULL DEFAULT 0
    ,single_page_user_count BIGINT NOT NULL DEFAULT 0
    ,total_session_seconds BIGINT NOT NULL DEFAULT 0
    ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE da_user_source_daily (
    report_date INT NOT NULL
    ,source VARCHAR(255) NOT NULL
    ,medium VARCHAR(255) NOT NULL
    ,channel VARCHAR(255) NOT NULL
    ,user_count BIGINT NOT NULL DEFAULT 0
    ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ,PRIMARY KEY (report_date, source, medium)
    ,INDEX idx_medium (medium, report_date)
);

CREATE TABLE da_user_geo_daily (
    report_date INT NOT NULL
    ,province VARCHAR(255) NOT NULL
    ,user_count BIGINT NOT NULL DEFAULT 0
    ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ,PRIMARY KEY (report_date, province)
);
