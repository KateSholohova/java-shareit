

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(255) NOT NULL,
    requester_id INTEGER,
    FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT pk_request PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(512) NOT NULL,
    available BOOLEAN NOT NULL,
    owner_id INTEGER,
    request_id INTEGER,
    FOREIGN KEY (owner_id) REFERENCES users (id),
    FOREIGN KEY (request_id) REFERENCES requests (id),
    CONSTRAINT pk_item PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    status Smallint NOT NULL,
    item_id INTEGER,
    booker_id INTEGER,
    FOREIGN KEY (item_id) REFERENCES items (id),
    FOREIGN KEY (booker_id) REFERENCES users (id),
    CONSTRAINT pk_booking PRIMARY KEY (id)
    );


CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(255) NOT NULL,
    item_id INTEGER,
    author_id INTEGER,
    FOREIGN KEY (item_id) REFERENCES items (id),
    FOREIGN KEY (author_id) REFERENCES users (id),
    CONSTRAINT pk_comment PRIMARY KEY (id)
    );
