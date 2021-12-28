// load Movie node
USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM 'file:///movie_schema.csv' AS row
merge (m:Movie {asin: row.asin})
SET m.score = toFloat(row.score)
SET m.score_count = toInteger(1)
SET m.title = row.title;

// load Actor node and create actor-movie edge
using periodic commit 500
load csv with headers from 'file:///actor_schema.csv' as row
match (m:Movie) where m.asin = row.asin
merge (a:Actor {actor: row.name})
set a.count = toInteger(row.count)
merge (a) -[:act_movie]->(m);

// load Director node and create director-movie edge
using periodic commit 500
load csv with headers from 'file:///director_schema.csv' as row
match (m:Movie) where m.asin = row.asin
merge (d:Director {director: row.name})
set d.count = toInteger(row.count)
merge (d) -[:director_movie]->(m);

// load Label node and create label-movie edge
using periodic commit 500
load csv with headers from 'file:///label_schema.csv' as row
match (m:Movie) where m.asin = row.asin
merge (l:Label {label: row.genre})
set l.count = toInteger(row.count)
merge (l) -[:label_movie]->(m);

// load Time node
using periodic commit 500
load csv with headers from 'file:///time_schema.csv' as row
merge (t:Time {day_id: row.id})
set t.year = toInteger(row.year)
set t.season = toInteger(row.season)
set t.month = toInteger(row.month)
set t.day = toInteger(row.day)
set t.year_count = toInteger(row.year_count)
set t.season_count = toInteger(row.season_count)
set t.month_count = toInteger(row.month_count)
set t.day_count = toInteger(row.day_count);

// create time-movie edge
using periodic commit 500
load csv with headers from 'file:///movie_schema.csv' as row
match (m:Movie) where m.asin = row.asin
match (t:Time) where toInteger(t.day_id) = toInteger(row.day_id)
set m.day_id = toInteger(row.day_id)
merge (t) -[:time_movie]->(m);

// set score attribute of movie schema
using periodic commit 500
load csv with headers from 'file:///score_schema.csv' as row
match(m:Movie)
where m.score = toFloat(row.score)
set m.score_count = toInteger(row.score_count);

// pre-calculate 
match (a:Actor)-[:act_movie]->(:Movie)<-[:act_movie]-(b:Actor)
where a.actor <> b.actor and size((b)-->(a))=0
merge (a)-[r:actor_actor]->(b)
set r.count = toInteger(1);

match (a:Actor)-[:act_movie]->(:Movie)<-[:act_movie]-(b:Actor)
where a.actor <> b.actor and size((b)-->(a))=0
with a,b
match (a)-[r]->(b)
set r.count = r.count+1;

match (a:Actor)-[:act_movie]->(:Movie)<-[:director_movie]-(b:Director)
merge (a)-[r:actor_director]->(b)
set r.count = case r.count
when r.count then toInteger(1)
else r.count+1
end;

match (a:Director)-[:director_movie]->(:Movie)<-[:director_movie]-(b:Director)
where a.director <> b.director and size((b)-->(a))=0
merge (a)-[r:director_director]->(b)
set r.count = toInteger(1);

match (a:Director)-[:director_movie]->(:Movie)<-[:director_movie]-(b:Director)
where a.director <> b.director and size((b)-->(a))=0
with a,b
match (a)-[r]->(b)
set r.count = r.count+1;