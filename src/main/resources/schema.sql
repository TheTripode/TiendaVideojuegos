CREATE TABLE IF NOT EXISTS accounts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_name TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    registration_date TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS developers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS videogames (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    price NUMERIC NOT NULL,
    release_date TEXT NOT NULL,
    developer_id INTEGER NOT NULL,
    FOREIGN KEY (developer_id) REFERENCES developers(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS videogame_categories (
    videogame_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    PRIMARY KEY (videogame_id, category_id),
    FOREIGN KEY (videogame_id) REFERENCES videogames(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS libraries (
    account_id INTEGER NOT NULL,
    videogame_id INTEGER NOT NULL,
    purchase_date TEXT NOT NULL,
    hours_played NUMERIC DEFAULT 0,
    PRIMARY KEY (account_id, videogame_id),
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
    FOREIGN KEY (videogame_id) REFERENCES videogames(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    account_id INTEGER NOT NULL,
    videogame_id INTEGER NOT NULL,
    recommended INTEGER NOT NULL,
    comment TEXT,
    publication_date TEXT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
    FOREIGN KEY (videogame_id) REFERENCES videogames(id) ON DELETE CASCADE,
    UNIQUE (account_id, videogame_id)
);