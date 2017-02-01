CREATE TABLE zw_likes (
    user_id UUID NOT NULL REFERENCES zw_user(iduser),
    tweet_id UUID NOT NULL REFERENCES zw_tweet(idtweet),
    ts TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT pk_user_tweet PRIMARY KEY (user_id, tweet_id)
);
