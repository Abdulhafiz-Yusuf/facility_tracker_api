CREATE TABLE facility (
      id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
      customer_id BIGINT NOT NULL,
      facility_type VARCHAR(20) NOT NULL,
      principal DECIMAL(15,2) NOT NULL,
      profit_rate DECIMAL(5,2) NOT NULL,
      status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
      start_date DATE,
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      CONSTRAINT fk_facility_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
);
