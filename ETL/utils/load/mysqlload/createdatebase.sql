create table hibernate_sequence
(
    next_val bigint null
);

create table score
(
    score double not null
        primary key,
    count int    null
);

create table time
(
    time_id      int not null
        primary key,
    day          int null,
    day_count    int not null,
    month        int null,
    month_count  int not null,
    season       int null,
    season_count int not null,
    year         int null,
    year_count   int not null
)
    comment '日期表';

create table movie
(
    product_id   varchar(255) not null
        primary key,
    title        varchar(255) null,
    score_score  double       null,
    time_time_id int          null,
    constraint FK2tj9n3avuiw2cjudrfvhsg0pf
        foreign key (time_time_id) references time (time_id),
    constraint FK5l466enhssasm8kdqcxd6m48a
        foreign key (score_score) references score (score)
)
    comment '电影表';

create table actor_movie
(
    actor_name  varchar(255) not null,
    product_id  varchar(255) not null,
    movie_count int          not null,
    primary key (actor_name, product_id),
    constraint FKib28or2y70q29ms635ga1c9yj
        foreign key (product_id) references movie (product_id)
)
    comment '演员参演电影表';

create table director
(
    director_name varchar(255) not null,
    product_id    varchar(255) not null,
    movie_count   varchar(255) null,
    primary key (director_name, product_id),
    constraint FKkwrvrhurtpninv0u1xq1tq0ri
        foreign key (product_id) references movie (product_id)
)
    comment '导演表';

create table label_movie
(
    label_name  varchar(255) not null,
    product_id  varchar(255) not null,
    movie_count int          not null,
    primary key (label_name, product_id),
    constraint FKn6leeeuycs046qpfj1fkfiohq
        foreign key (product_id) references movie (product_id)
)
    comment '电影标签表';

create table movie_source
(
    id       int         not null
        primary key,
    asin     varchar(20) null,
    neighbor varchar(20) null,
    constraint movie_source_movie_product_id_fk
        foreign key (asin) references movie (product_id)
);

create table user
(
    user_id      varchar(255) not null
        primary key,
    profile_name varchar(255) null
)
    comment '用户表';

create table review
(
    id               int          not null
        primary key,
    score            double       not null,
    summary          longtext     null,
    text             longtext     null,
    time_stamp       bigint       not null,
    movie_product_id varchar(255) null,
    user_user_id     varchar(255) null,
    constraint FK5bhefci502sd63299f0mw09t7
        foreign key (user_user_id) references user (user_id),
    constraint FK5cwcswvist3t5xec26h6y8dky
        foreign key (movie_product_id) references movie (product_id)
)
    comment '电影评论表';


