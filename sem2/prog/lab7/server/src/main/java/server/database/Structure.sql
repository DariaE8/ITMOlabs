CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL
);

CREATE TABLE venue (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    capacity INT CHECK (capacity > 0),
    type TEXT NOT NULL
);

CREATE TABLE ticket (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    x FLOAT NOT NULL CHECK (x BETWEEN -180 AND 180),
    y BIGINT NOT NULL CHECK (y BETWEEN -90 AND 90),
    creation_date DATE NOT NULL,
    price INT CHECK (price > 0),
    discount DOUBLE PRECISION NOT NULL CHECK (discount > 0 AND discount <= 100),
    refundable BOOLEAN,
    type TEXT NOT NULL,
    venue_id INT NOT NULL REFERENCES venue(id) ON DELETE CASCADE,
    owner_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);
