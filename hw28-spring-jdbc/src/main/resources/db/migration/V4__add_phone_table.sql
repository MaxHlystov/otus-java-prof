create table phone
(
    id        bigserial not null primary key,
    number    text,
    client_id bigint references client (id) on update  cascade on delete cascade
);