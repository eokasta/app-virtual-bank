alter table bank_transactions
    add column old_balance float(18, 2) not null;
update bank_transactions
set old_balance = 0;
alter table bank_transactions
    add column new_balance float(18, 2) not null;
update bank_transactions
set new_balance = 0;
