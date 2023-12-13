ALTER TABLE bank_transactions
DROP FOREIGN KEY bank_transactions_ibfk_1;

ALTER TABLE bank_transactions
ADD CONSTRAINT bank_transactions_ibfk_1
FOREIGN KEY (account_id) REFERENCES bank_users (id);