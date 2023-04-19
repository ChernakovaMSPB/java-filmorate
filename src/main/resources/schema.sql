DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS film_likes CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS film_genre CASCADE;
DROP TABLE IF EXISTS genre CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS mpa CASCADE;

CREATE TABLE IF NOT EXISTS mpa (
    rating_id INTEGER   NOT NULL,
    mpa_name VARCHAR(10)   NOT NULL,
    PRIMARY KEY (rating_id)
    );

CREATE TABLE IF NOT EXISTS films (
    film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(300),
    release_date DATE,
    duration INTEGER,
    rating_id INTEGER,
    CONSTRAINT film_mpa_fk FOREIGN KEY(rating_id) REFERENCES mpa (rating_id)
    );

CREATE TABLE IF NOT EXISTS genre (
    genre_id int   NOT NULL,
    genre_name varchar   NOT NULL,
    PRIMARY KEY (genre_id)
    );

CREATE TABLE IF NOT EXISTS film_genre (
    film_id int   NOT NULL,
    genre_id int   NOT NULL,
    CONSTRAINT film_genre_film_fk FOREIGN KEY(film_id) REFERENCES films (film_id),
    CONSTRAINT film_genre_genre_fk FOREIGN KEY(genre_id) REFERENCES genre (genre_id)
    );

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(100),
    login VARCHAR(100),
    name VARCHAR(100),
    birthday VARCHAR
    );

CREATE TABLE IF NOT EXISTS film_likes (
    film_id int   NOT NULL,
    user_id int   NOT NULL,

    CONSTRAINT pk_like PRIMARY KEY (user_id),
    CONSTRAINT like_user_fk FOREIGN KEY(user_id) REFERENCES users (user_id),
    CONSTRAINT like_film_fk FOREIGN KEY(film_id) REFERENCES films (film_id)
    );

CREATE TABLE IF NOT EXISTS friends (
    user_id INTEGER NOT NULL,
    friend_id INTEGER NOT NULL,
    status VARCHAR(40) NOT NULL,
    CONSTRAINT user_fk FOREIGN KEY(user_id) REFERENCES users (user_id),
    CONSTRAINT friend_fk FOREIGN KEY(friend_id) REFERENCES users (user_id)
    );