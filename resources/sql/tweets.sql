-- :name query-tweets-for-user :? :*
SELECT
    zw_follows.*,
    zw_tweet.*,
    zw_user.*,
    CASE
        WHEN zw_likes.ts IS NULL
        THEN FALSE
        ELSE TRUE
    END
FROM zw_follows
INNER JOIN zw_user
    ON zw_user.iduser = zw_follows.whom
INNER JOIN zw_tweet
    ON zw_tweet.user_id = zw_user.iduser
LEFT OUTER JOIN zw_likes
    ON zw_likes.user_id = zw_user.iduser AND
       zw_likes.tweet_id = zw_tweet.idtweet
WHERE zw_follows.who = :uid;
