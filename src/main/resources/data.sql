insert into PROJECT(project_language_id,name, created_at, file_path)
VALUES (1,'Project 1', current_timestamp, 'example.cpp');

insert into BUILD_RESULT(project_id, source_code_hash, compilation_status, executable_file_path, build_logs, timestamp)
VALUES (1, 'source_code_hash', 'IN_PROGRESS', 'executable_file_path', 'build_logs', current_timestamp);

insert into PROJECT_LANGUAGE(file_extension, programming_language)
VALUES ('file_extension', 'programming_language');