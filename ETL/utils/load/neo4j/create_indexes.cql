// define schema with index and constraint

// movie_schema
create constraint on (movie:Movie) assert movie.asin is unique;
create index on :Movie(score);

// actor_schema
create constraint on (actor:Actor) assert actor.name is unique;
create index on :Actor(count);

// director_schema
create constraint on (director:Director) assert director.name is unique;
create index on :Director(count);

// label_schema
create constraint on (label:Label) assert label.genre is unique;
create index on :Label(count);

// day_schema
create constraint on (time:Time) assert time.day_id is unique;
create index on :Time(year);
create index on :Time(month);
create index on :Time(season);
create index on :Time(day);
create index on :Time(year_count);
create index on :Time(month_count);
create index on :Time(season_count);
create index on :Time(day_count);

