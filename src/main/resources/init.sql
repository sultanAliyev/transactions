CREATE TABLE IF NOT EXISTS accounts
(
    id          SERIAL      NOT NULL,
    customer_id VARCHAR(80) NOT NULL,
    country     VARCHAR(120),
    create_at   TIMESTAMP   NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);



CREATE TABLE IF NOT EXISTS transactions
(
    id          SERIAL           NOT NULL,
    sender_id   INT              NOT NULL,
    receiver_id INT              NOT NULL,
    amount      NUMERIC          NOT NULL DEFAULT 0,
    currency    VARCHAR(3)          NOT NULL,
    description TEXT             NOT NULL,
    create_at   TIMESTAMP        NOT NULL DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (sender_id) REFERENCES accounts (id),
    FOREIGN KEY (receiver_id) REFERENCES accounts (id)
);

CREATE TABLE IF NOT EXISTS balances
(
    id         SERIAL           NOT NULL,
    currency   VARCHAR(3)       NOT NULL,
    amount     NUMERIC          NOT NULL DEFAULT 0,
    account_id INT              NOT NULL,
    create_at  TIMESTAMP        NOT NULL DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES accounts (id)

);