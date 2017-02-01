-- :name query-create-user :i! :1
INSERT INTO zw_user (name, password)
    VALUES (:name, :pass)
RETURNING *;

-- :name query-get-by-id :? :1
SELECT * FROM zw_user WHERE iduser = :uid;

-- :name query-get-enabled-by-id :? :1
SELECT * FROM zw_user WHERE iduser = :uid AND password IS NOT NULL;

-- :name query-get-by-username :? :1
SELECT * FROM zw_user WHERE name = :name;
