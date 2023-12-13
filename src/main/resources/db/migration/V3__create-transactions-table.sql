create table bank_transactions
(
    id               int          not null auto_increment,
    account_id       int          not null,
    amount           float(18, 2) not null,
    transaction_date timestamp    not null default current_timestamp,
    transaction_type varchar(32)  not null,
    old_balance      float(18, 2) not null,
    new_balance      float(18, 2) not null,
    primary key (id),
    foreign key (account_id) references bank_users (id)
);