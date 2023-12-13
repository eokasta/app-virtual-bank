create table bank_users
(
    id        int auto_increment not null,
    cpf       char(11)     not null,
    full_name varchar(255) not null,
    password  varchar(255) not null,
    email     varchar(255) not null,
    balance   float(18, 2
) default 0.00,
    role varchar(255) not null,
    created_at timestamp default current_timestamp not null,
    primary key (id),
    index(cpf)
);