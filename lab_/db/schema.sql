drop table if exists album cascade;
drop table if exists coordinates cascade;
drop table if exists location cascade;
drop table if exists person cascade;
drop table if exists music_band cascade;
drop type if exists color cascade;
drop type if exists country cascade;
drop type if exists music_genre cascade;
drop table if exists nomination cascade;


create table album (
    id integer generated always as identity primary key,
    name varchar not null check ( length(trim(name)) > 0 ),
    tracks integer check (tracks > 0)
);

create table coordinates (
    id integer generated always as identity primary key,
    x bigint not null check ( x <= 170 ),
    y integer not null
);

create table location (
    id integer generated always as identity primary key,
    x bigint not null ,
    y integer not null ,
    z bigint not null
);

create type color as enum ('RED', 'BLACK', 'BLUE', 'YELLOW');
create type country as enum ('RUSSIA', 'UNITED_KINGDOM', 'GERMANY', 'JAPAN');
create type music_genre as enum ('PSYCHEDELIC_CLOUD_RAP', 'SOUL', 'BLUES', 'BRIT_POP');

create table person (
    id integer generated always as identity primary key,
    name varchar not null check ( length(trim(name)) > 0 ),
    eye_color color ,
    hair_color color ,
    location_id integer references location(id) on delete restrict ,
    birthday timestamp ,
    nationality country
);

create table music_band (
    id integer generated always as identity primary key,
    name varchar not null check ( length(trim(name)) > 0 ),
    coordinates_id integer not null references coordinates(id) on delete restrict,
    creation_date date not null default current_date,
    genre music_genre not null,
    number_of_participants bigint check ( number_of_participants > 0 ),
    singles_count integer not null check ( singles_count > 0 ),
    description varchar not null,
    best_album_id integer references album(id) on delete restrict,
    albums_count integer not null check ( albums_count > 0 ),
    front_man_id integer not null references person(id) on delete restrict
);

create table if not exists nomination (
    id integer generated always as identity primary key,
    band_id integer not null references music_band(id) on delete cascade,
    genre music_genre not null,
    nominated_at timestamp not null default now()
);