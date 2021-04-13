CREATE TABLE "kafka_auth"
(
    "id"          integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "source_id"   INTEGER NOT NULL,
    "add_auth"    integer,
    "update_auth" integer,
    "remove_auth" integer
);

CREATE TABLE "zookeeper_auth"
(
    "id"          integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "source_id"   INTEGER NOT NULL,
    "add_auth"    integer,
    "update_auth" integer,
    "remove_auth" integer
);

CREATE TABLE "redis_auth"
(
    "id"          integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "source_id"   INTEGER NOT NULL,
    "add_auth"    integer,
    "update_auth" integer,
    "remove_auth" integer
);

CREATE TABLE "source"
(
    "id"     INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name"   text(64),
    "broker" text(64)
);

CREATE TABLE "zookeeper_source"
(
    "id"      INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "address" TEXT,
    "name"    TEXT
);

CREATE TABLE "redis_source"
(
    "id"       INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name"     TEXT,
    "ip"       TEXT,
    "port"     integer,
    "password" TEXT
);
