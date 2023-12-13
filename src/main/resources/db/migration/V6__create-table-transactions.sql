create table bank_transactions (
    id int not null auto_increment,
    account_id int not null,
    amount float(18,2) not null,
    transaction_date timestamp not null default current_timestamp,
    transaction_type enum('DEPOSIT', 'WITHDRAW') not null,
    primary key (id),
    foreign key (id) references bank_users(id)
);