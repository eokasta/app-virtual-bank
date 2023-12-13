alter table bank_users add created_at timestamp default current_timestamp not null;

update bank_users set created_at = now();