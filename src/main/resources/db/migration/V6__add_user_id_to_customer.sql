ALTER TABLE customer ADD COLUMN user_id BIGINT NULL UNIQUE;
ALTER TABLE customer ADD CONSTRAINT fk_customer_user FOREIGN KEY (user_id) REFERENCES app_user(id);
