insert into PROJECT(name, created_at, file_path)
VALUES ('Project 1', current_timestamp, 'example.cpp')
    insert
into BUILD_RESULT(project_id, source_code_hash, compilation_status, error_messages, warning_messages,
                  executable_file_path, build_logs, timestamp)
VALUES (1, 'source_code_hash', True, 'error_messages', 'warning_messages', 'executable_file_path', 'build_logs', current_timestamp)