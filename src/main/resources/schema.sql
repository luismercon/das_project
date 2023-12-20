CREATE TABLE PROJECT(
    project_id BIGSERIAL primary key,
    name varchar(50) not null,
    created_at timestamp not null
);