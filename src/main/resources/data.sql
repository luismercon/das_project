insert into PROJECT_LANGUAGE(file_extension, programming_language)
VALUES ('cpp', 'C++'), ('c', 'C');

insert into PROJECT(project_language_id,name, created_at, file_path)
VALUES (1,'Project 1', current_timestamp, 'example.cpp');

insert into BUILD_RESULT(project_id, compilation_time_ms, compilation_status, executable_file_path, build_logs, timestamp)
VALUES (1, 1000, 'IN_PROGRESS', 'executable_file_path', 'build_logs', current_timestamp);

