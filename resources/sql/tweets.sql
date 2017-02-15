-- :name query-tweets-for-user :? :*
WITH affected_tweets AS (
    SELECT
        zw_user.name,
        zw_tweet.text,
        zw_tweet.ts,
        zw_tweet.user_id,
        zw_tweet.idtweet,
        CASE
            WHEN zw_likes.ts IS NULL
            THEN FALSE
            ELSE TRUE
        END AS liked
    FROM zw_follows
    INNER JOIN zw_user
        ON zw_user.iduser = zw_follows.whom
    INNER JOIN zw_tweet
        ON zw_tweet.user_id = zw_user.iduser
    LEFT JOIN zw_likes
        ON zw_likes.user_id = :uid AND
           zw_likes.tweet_id = zw_tweet.idtweet
    WHERE zw_follows.who = :uid

    UNION

    SELECT
        zw_user.name,
        zw_tweet.text,
        zw_tweet.ts,
        zw_tweet.user_id,
        zw_tweet.idtweet,
        NULL as liked
    FROM zw_tweet
    INNER JOIN zw_user
        ON zw_user.iduser = zw_tweet.user_id
    WHERE zw_tweet.user_id = :uid
)
SELECT * FROM affected_tweets ORDER BY ts DESC;

-- :name query-insert-tweet :i!
INSERT INTO zw_tweet (text, user_id) VALUES (:text, :uid);

-- :name query-increase-tweet-count :!
UPDATE zw_user
  SET tweet_count = tweet_count + 1
WHERE iduser = :uid;

-- :name query-user-tweets :? :*
SELECT
    zw_tweet.*,
    zw_user.*,
    CASE
        WHEN zw_likes.ts IS NULL
        THEN FALSE
        ELSE TRUE
    END AS liked
FROM zw_tweet
INNER JOIN zw_user
    ON zw_tweet.user_id = zw_user.iduser
LEFT JOIN zw_likes
    ON zw_likes.tweet_id = zw_tweet.idtweet
WHERE zw_tweet.user_id = :uid;

-- :name query-get-follow-entry :? :1
SELECT
    *
FROM zw_follows
WHERE who = :who AND
      whom = :whom;

-- :name query-follow-user :!
INSERT INTO zw_follows (who, whom)
VALUES (:who, :whom)
ON CONFLICT DO NOTHING;

-- :name query-unfollow-user :!
DELETE FROM zw_follows
WHERE who = :who AND whom = :whom;

-- :name query-increase-follower-count :!
UPDATE zw_user
SET follower_count = follower_count + 1
WHERE iduser = :uid;

-- :name query-decrease-follower-count :!
UPDATE zw_user
SET follower_count = follower_count - 1
WHERE iduser = :uid;

-- :name query-delete-tweet :!
DELETE FROM zw_tweet
WHERE zw_tweet.idtweet = :tid AND
      zw_tweet.user_id = :uid;

-- :name query-decrease-tweet-count :!
UPDATE zw_user
SET tweet_count = tweet_count - 1
WHERE iduser = :uid;

-- :name query-like-tweet :!
WITH the_tweet AS (
    SELECT :uid as user_id,
           idtweet
    FROM zw_tweet
    WHERE user_id != :uid AND
          idtweet = :tid
)
INSERT INTO zw_likes (user_id, tweet_id)
SELECT user_id, idtweet from the_tweet;

-- :name query-dislike-tweet :!
DELETE FROM zw_likes
WHERE user_id = :uid AND
      tweet_id = :tid;

-- :name query-get-followers :? :*
SELECT
    *
FROM zw_follows
INNER JOIN zw_user
    ON zw_follows.who = zw_user.iduser
WHERE zw_follows.whom = :uid;
