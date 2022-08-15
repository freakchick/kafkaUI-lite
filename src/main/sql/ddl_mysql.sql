CREATE TABLE `kafka_auth`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `source_id`   int(11) NOT NULL,
    `add_auth`    int(11),
    `update_auth` int(11),
    `remove_auth` int(11),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `zookeeper_auth`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `source_id`   int(11) NOT NULL,
    `add_auth`    int(11),
    `update_auth` int(11),
    `remove_auth` int(11),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `redis_auth`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `source_id`   int(11) NOT NULL,
    `add_auth`    int(11),
    `update_auth` int(11),
    `remove_auth` int(11),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `source`
(
    `id`     int(11) NOT NULL AUTO_INCREMENT,
    `name`   varchar(255) DEFAULT NULL,
    `broker` varchar(255) DEFAULT NULL,
    `username` varchar(255) DEFAULT NULL,
    `password` varchar(255) DEFAULT NULL
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `zookeeper_source`
(
    `id`      int(11) NOT NULL AUTO_INCREMENT,
    `address` varchar(255) DEFAULT NULL,
    `name`    varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `redis_source`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT,
    `name`     varchar(255) DEFAULT NULL,
    `ip`       varchar(255) DEFAULT NULL,
    `port`     int(11),
    `password` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
