use ajt;

load data local inpath '/root/ajt/movie.txt' overwrite into table movie;
load data local inpath '/root/ajt/release_date.txt' overwrite into table release_date;
load data local inpath '/root/ajt/score_movie.txt' overwrite into table score_movie;
load data local inpath '/root/ajt/actor_actor.txt' overwrite into table actor_actor;
load data local inpath '/root/ajt/actor_director.txt' overwrite into table actor_director;
load data local inpath '/root/ajt/actor_movie.txt' overwrite into table actor_movie;
load data local inpath '/root/ajt/director_actor.txt' overwrite into table director_actor;
load data local inpath '/root/ajt/director_director.txt' overwrite into table director_director;
load data local inpath '/root/ajt/director_movie.txt' overwrite into table director_movie;
load data local inpath '/root/ajt/label_movie.txt' overwrite into table label_movie;