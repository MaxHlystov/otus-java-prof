alter table client
    add column address_id bigint
        references address (id) on update cascade on delete cascade;