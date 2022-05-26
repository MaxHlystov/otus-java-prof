create table phone
(
    id        bigint not null primary key,
    number    text,
    client_id bigint references client (id) on update cascade on delete restrict
);