CREATE TABLE zw_follows (
    who UUID NOT NULL REFERENCES zw_user(iduser),
    whom UUID NOT NULL REFERENCES zw_user(iduser),

    CONSTRAINT cns_no_self_follow CHECK (who != whom),
    CONSTRAINT pk_who_whom PRIMARY KEY (who, whom)
);
