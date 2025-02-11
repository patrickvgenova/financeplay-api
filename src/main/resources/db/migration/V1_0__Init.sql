CREATE TABLE IF NOT EXISTS sys_user
(
    id_user            BIGSERIAL PRIMARY KEY,
    id_external        UUID                     NOT NULL DEFAULT uuid_generate_v4(),
    name               VARCHAR(256),
    email              VARCHAR(256)             NOT NULL,
    photo_url          VARCHAR(512),
    document           VARCHAR(16),
    phone_country_code VARCHAR(8),
    phone_area_code    VARCHAR(8),
    phone_number       VARCHAR(16),
    created_at         TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at         TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    last_login         TIMESTAMP WITH TIME ZONE,
    status             VARCHAR(50)                       DEFAULT 'ACTIVE',
    deleted            BOOLEAN                  NOT NULL DEFAULT FALSE
);
CREATE INDEX ON sys_user (id_user);
CREATE INDEX ON sys_user (id_external);
CREATE INDEX ON sys_user (name);
CREATE INDEX ON sys_user (email);
CREATE INDEX ON sys_user (document);
CREATE INDEX ON sys_user (phone_number);
CREATE INDEX ON sys_user (status);
CREATE INDEX ON sys_user (plan);

CREATE TABLE plan
(
    id_plan     BIGSERIAL PRIMARY KEY,
    id_external UUID                     NOT NULL DEFAULT uuid_generate_v4(),
    name        VARCHAR(255)             NOT NULL,
    description TEXT,
    type VARCHAR(50) NOT NULL DEFAULT 'FREE', -- FREE, STANDARD, PREMIUM
    price       DECIMAL(15, 2),
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    deleted     BOOLEAN                  NOT NULL DEFAULT FALSE
);

CREATE TABLE user_plan
(
    id_user_plan BIGSERIAL PRIMARY KEY,
    user_id      BIGINT REFERENCES sys_user (id_user) NOT NULL UNIQUE,
    plan         BIGINT REFERENCES plan (id_plan),
    last_plan    BIGINT REFERENCES plan (id_plan),
    updated_at   TIMESTAMP WITH TIME ZONE             NOT NULL DEFAULT now()
);

CREATE TABLE user_coins
(
    id_user_coins BIGSERIAL PRIMARY KEY,
    user_id       BIGINT REFERENCES sys_user (id_user) NOT NULL UNIQUE,
    current_balance BIGINT NOT NULL DEFAULT 0,
    total_earned BIGINT NOT NULL DEFAULT 0,
    total_spent BIGINT NOT NULL DEFAULT 0,
    last_monthly_credit_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE user_coins_transaction
(
    id_user_coins_transaction BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES sys_user (id_user) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL, -- PURCHASE, MONTHLY_CREDIT, USAGE, REFUND
    amount BIGINT NOT NULL,
    balance_after BIGINT NOT NULL,
    description TEXT,
    feature_used VARCHAR(100), -- Nome da funcionalidade da IA usada (quando aplicável)
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    metadata JSONB -- Dados adicionais específicos de cada tipo de transação
);

CREATE TABLE feature_price
(
    id_feature_price BIGSERIAL PRIMARY KEY,
    feature_name VARCHAR(100) NOT NULL UNIQUE,
    coins_price BIGINT NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabela para pacotes de moedas disponíveis para compra
CREATE TABLE coin_package
(
    id_coin_package BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    coins_amount BIGINT NOT NULL,
    price DECIMAL(15, 2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);


CREATE TABLE user_devices
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT REFERENCES sys_user (id_user) NOT NULL,
    device_token VARCHAR(255),
    device_type  VARCHAR(50),
    last_active  TIMESTAMP WITH TIME ZONE,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- gerado até aqui


CREATE TABLE user_accounts
(
    id          BIGSERIAL PRIMARY KEY,
    id_external UUID                                 NOT NULL DEFAULT uuid_generate_v4(),
    user_id     BIGINT REFERENCES sys_user (id_user) NOT NULL,
    name        VARCHAR(255)                         NOT NULL,
    type        VARCHAR(50)                          NOT NULL,
    balance     DECIMAL(15, 2)                                DEFAULT 0,
    currency    VARCHAR(3)                                    DEFAULT 'BRL',
    color       VARCHAR(7),
    icon        VARCHAR(50),
    is_active   BOOLEAN                                       DEFAULT true,
    created_at  TIMESTAMP WITH TIME ZONE                      DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE                      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE category
(
    id          BIGSERIAL PRIMARY KEY,
    id_external UUID                                 NOT NULL DEFAULT uuid_generate_v4(),
    user_id     BIGINT REFERENCES sys_user (id_user) NOT NULL,
    name        VARCHAR(255)                         NOT NULL,
    type        VARCHAR(50)                          NOT NULL, -- EXPENSE, INCOME
    color       VARCHAR(7),
    parent_id   BIGINT REFERENCES category (id),
    created_at  TIMESTAMP WITH TIME ZONE                      DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX ON category (name);
CREATE INDEX ON category (type);



CREATE TABLE tag
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES sys_user (id_user) NOT NULL,
    name       VARCHAR(255)                         NOT NULL,
    color      VARCHAR(7),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE transaction
(
    id           BIGSERIAL PRIMARY KEY,
    id_external  UUID                                 NOT NULL DEFAULT uuid_generate_v4(),
    user_id      BIGINT REFERENCES sys_user (id_user) NOT NULL,
    category_id  BIGINT REFERENCES category (id),
    amount       DECIMAL(15, 2)                       NOT NULL,
    type         VARCHAR(50)                          NOT NULL, -- EXPENSE, INCOME, TRANSFER
    description  VARCHAR(255),
    date         TIMESTAMP WITH TIME ZONE             NOT NULL,
    status       VARCHAR(50)                                   DEFAULT 'COMPLETED',
    is_recurring BOOLEAN                                       DEFAULT false,
    recurring_id UUID,
    created_at   TIMESTAMP WITH TIME ZONE                      DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP WITH TIME ZONE                      DEFAULT CURRENT_TIMESTAMP,
    deleted      BOOLEAN                              NOT NULL DEFAULT FALSE,
    metadata     TEXT
);


CREATE TABLE IF NOT EXISTS wallet_transaction
(
    id_wallet_transaction BIGSERIAL PRIMARY KEY,
    id_transaction        BIGINT REFERENCES transaction (id_transaction) NOT NULL,
    account_id            BIGINT REFERENCES user_accounts (id),
    value                 NUMERIC(13, 2)                                 NOT NULL
);
CREATE INDEX ON wallet_transaction (id_transaction);
CREATE INDEX ON wallet_transaction (id_wallet);


CREATE TABLE transaction_tags
(
    transaction_id BIGINT REFERENCES transaction (id),
    tag_id         BIGINT REFERENCES tag (id),
    PRIMARY KEY (transaction_id, tag_id)
);



CREATE TABLE recurring_transactions
(
    id                 UUID PRIMARY KEY,
    user_id            BIGINT REFERENCES sys_user (id_user) NOT NULL,
    category_id        BIGINT REFERENCES category (id),
    account_default_id BIGINT REFERENCES user_accounts (id),
    amount             DECIMAL(15, 2)                       NOT NULL,
    type               VARCHAR(50)                          NOT NULL,
    description        TEXT,
    frequency          VARCHAR(50)                          NOT NULL, -- DAILY, WEEKLY, MONTHLY, YEARLY
    start_date         TIMESTAMP WITH TIME ZONE             NOT NULL,
    end_date           TIMESTAMP WITH TIME ZONE,
    last_generated     TIMESTAMP WITH TIME ZONE,
    created_at         TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE financial_goals
(
    id             UUID PRIMARY KEY,
    user_id        BIGINT REFERENCES sys_user (id_user) NOT NULL,
    category_id    BIGINT REFERENCES category (id),
    name           VARCHAR(255)                         NOT NULL,
    target_amount  DECIMAL(15, 2)                       NOT NULL,
    current_amount DECIMAL(15, 2)           DEFAULT 0,
    start_date     TIMESTAMP WITH TIME ZONE             NOT NULL,
    target_date    TIMESTAMP WITH TIME ZONE,
    status         VARCHAR(50)              DEFAULT 'ACTIVE',
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE budgets
(
    id           UUID PRIMARY KEY,
    user_id      BIGINT REFERENCES sys_user (id_user) NOT NULL,
    category_id  BIGINT REFERENCES category (id),
    amount       DECIMAL(15, 2)                       NOT NULL,
    period_start DATE                                 NOT NULL,
    period_end   DATE                                 NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE financial_insights
(
    id          UUID PRIMARY KEY,
    user_id     BIGINT REFERENCES sys_user (id_user) NOT NULL,
    type        VARCHAR(50)                          NOT NULL,
    description TEXT                                 NOT NULL,
    metadata    JSONB,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at  TIMESTAMP WITH TIME ZONE
);


CREATE TABLE achievements
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    icon        VARCHAR(50),
    points      INTEGER                  DEFAULT 0,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_achievements
(
    user_id        BIGINT REFERENCES sys_user (id_user) NOT NULL,
    achievement_id UUID REFERENCES achievements (id),
    achieved_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, achievement_id)
);

CREATE TABLE import_jobs
(
    id                UUID PRIMARY KEY,
    user_id           BIGINT REFERENCES sys_user (id_user) NOT NULL,
    status            VARCHAR(50)                          NOT NULL,
    file_name         VARCHAR(255),
    file_type         VARCHAR(50),
    total_records     INTEGER,
    processed_records INTEGER                  DEFAULT 0,
    error_records     INTEGER                  DEFAULT 0,
    started_at        TIMESTAMP WITH TIME ZONE,
    completed_at      TIMESTAMP WITH TIME ZONE,
    created_at        TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    metadata          TEXT
);


CREATE TABLE IF NOT EXISTS transaction_item
(
    id_transaction_item BIGSERIAL PRIMARY KEY,
    id_external         UUID                                           NOT NULL,
    name                VARCHAR(512)                                   NOT NULL,
    measure             VARCHAR(56),
    quantity            NUMERIC(13, 2)                                 NOT NULL,
    unit_value          NUMERIC(13, 2)                                 NOT NULL,
    id_category         BIGINT REFERENCES category (id_category),
    id_transaction      BIGINT REFERENCES transaction (id_transaction) NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE                       NOT NULL DEFAULT now(),
    deleted             BOOLEAN                                        NOT NULL DEFAULT FALSE
);
CREATE INDEX ON transaction_item (id_transaction_item);
CREATE INDEX ON transaction_item (id_external);
CREATE INDEX ON transaction_item (code);
CREATE INDEX ON transaction_item (code_type);
CREATE INDEX ON transaction_item (name);
CREATE INDEX ON transaction_item (measure);
CREATE INDEX ON transaction_item (id_category);
CREATE INDEX ON transaction_item (id_transaction);
