CREATE TABLE PROJECT
(
    project_id BIGSERIAL primary key,
    name       varchar(50) not null,
    created_at timestamp   not null,
    file_path  varchar(100)
);

CREATE TABLE BUILD_RESULT
(
    build_result_id      BIGSERIAL PRIMARY KEY,
    project_id           BIGINT NOT NULL,
    source_code_hash     VARCHAR(255),
    compilation_status   VARCHAR(255),
    executable_file_path VARCHAR(255),
    build_logs           TEXT,
    timestamp            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES PROJECT (project_id)
);

CREATE TABLE PROJECT_LANGUAGE
(
    project_language_id  BIGSERIAL PRIMARY KEY,
    project_id           BIGINT NOT NULL,
    file_extension       varchar(100),
    programming_language varchar(100),
    FOREIGN KEY (project_id) REFERENCES PROJECT (project_id)
);
