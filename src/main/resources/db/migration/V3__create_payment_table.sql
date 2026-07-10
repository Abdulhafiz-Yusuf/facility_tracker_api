CREATE TABLE payment (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     facility_id BIGINT NOT NULL,
     amount_paid DECIMAL(15,2) NOT NULL,
     payment_date DATE NOT NULL,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     CONSTRAINT fk_payment_facility FOREIGN KEY (facility_id) REFERENCES facility(id)
);
