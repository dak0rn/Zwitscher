CREATE TABLE zw_user (
    iduser UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(64) UNIQUE NOT NULL,
    password VARCHAR(255),
    follower_count INTEGER
                   DEFAULT 0
                   NOT NULL
                   CHECK(follower_count >= 0),
    tweet_count INTEGER
                DEFAULT 0
                NOT NULL
                CHECK(tweet_count >= 0)
);
--;;
CREATE INDEX idx_user_name ON zw_user(name);
