alter table bank_users add role varchar(255) not null;

update bank_users set bank_users.role = 'USER';