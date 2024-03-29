CREATE TABLE zw_tweet (
    idtweet UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    text VARCHAR(145)
         NOT NULL
         CHECK(text != ''),
    ts TIMESTAMP NOT NULL DEFAULT now(),
    like_count INTEGER
               NOT NULL
               CHECK (like_count >= 0)
               DEFAULT 0,
    user_id UUID REFERENCES zw_user(iduser) NOT NULL
);
