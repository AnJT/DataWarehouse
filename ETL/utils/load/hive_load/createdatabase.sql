use ajt;

create table movie(
    id              string,
    title           string,
    score           float,
    director        array<string>,
    actor           array<string>,
    release_date    string,
    label           array<string>
)
row format delimited
  fields terminated by '|'
  collection items terminated by ','
  map keys terminated by ':';

create table movie_orc(
    id              string,
    title           string,
    score           float,
    director        array<string>,
    actor           array<string>,
    release_date    string,
    label           array<string>
)
row format delimited
  fields terminated by '|'
  collection items terminated by ','
  map keys terminated by ':'
stored as orc ;

create table release_date(
    year            int,
    year_count      int,
    season          int,
    season_count    int,
    month           int,
    month_count     int,
    day             int,
    day_count       int
)
row format delimited
  fields terminated by '|'
  collection items terminated by ','
  map keys terminated by ':';

create table actor_movie(
    name                 string,
    movie_id             array<string>
)
row format delimited
  fields terminated by '|'
  collection items terminated by ','
  map keys terminated by ':';

create table director_movie(
    name                 string,
    movie_id             array<string>
)
row format delimited
  fields terminated by '|'
  collection items terminated by ','
  map keys terminated by ':';

create table label_movie(
    name                 string,
    movie_id             array<string>
)
row format delimited
  fields terminated by '|'
  collection items terminated by ','
  map keys terminated by ':';

create table score_movie(
    score                float,
    movie_id             array<string>
)
row format delimited
  fields terminated by '|'
  collection items terminated by ','
  map keys terminated by ':';

create table actor_actor(
    name                 string,
    cooperation_name     array<string>,
    cooperation_count    array<int>
)
row format delimited
  fields terminated by '|'
  collection items terminated by ','
  map keys terminated by ':';

create table director_actor(
    name                 string,
    cooperation_name     array<string>,
    cooperation_count    array<int>
)
row format delimited
  fields terminated by '|'
  collection items terminated by ','
  map keys terminated by ':';

create table director_director(
    name                 string,
    cooperation_name     array<string>,
    cooperation_count    array<int>
)
row format delimited
  fields terminated by '|'
  collection items terminated by ','
  map keys terminated by ':';

create table actor_director(
    name                 string,
    cooperation_name     array<string>,
    cooperation_count    array<int>
)
row format delimited
  fields terminated by '|'
  collection items terminated by ','
  map keys terminated by ':';