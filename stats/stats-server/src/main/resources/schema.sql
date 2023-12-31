create table if not exists endpoint_hits
(
    id       bigint generated by default as identity primary key,
    app      varchar(50) not null,
    uri      varchar(50) not null,
    ip       varchar(20) not null,
    hit_time timestamp   not null
);