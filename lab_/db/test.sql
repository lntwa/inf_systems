\echo 'пройдут'
insert into coordinates (x, y) values (170, 20);

insert into coordinates (x, y) values (-500, 1);

insert into album (name, tracks) values ('Untitled', null);

insert into album (name, tracks) values ('Abbey Road', 17);

insert into location (x, y, z) values (1, 2, 3);

insert into person (name) values ('Freddie');

insert into person (name, eye_color, hair_color, location_id, birthday, nationality)
values ('Brian', 'BLUE', 'BLACK', (select min(id) from location), '1947-07-19 12:00:00', 'UNITED_KINGDOM');

insert into music_band (name, coordinates_id, genre, singles_count, description, albums_count, front_man_id)
values ('Queen', (select min(id) from coordinates), 'BRIT_POP', 5, 'test', 3, (select min(id) from person));

insert into music_band (name, coordinates_id, genre, number_of_participants, singles_count, description, best_album_id, albums_count, front_man_id)
values ('Beatles', (select min(id) from coordinates), 'SOUL', 4, 10, 'full', (select min(id) from album), 12, (select min(id) from person));

\echo 'не пройдут'
insert into music_band (name, coordinates_id, genre, singles_count, description, albums_count, front_man_id)
values ('   ', (select min(id) from coordinates), 'SOUL', 1, 'x', 1, (select min(id) from person));

insert into coordinates (x, y) values (171, 20);

insert into coordinates (x, y) values (10, null);

insert into music_band (name, coordinates_id, genre, singles_count, description, albums_count, front_man_id)
values ('A', (select min(id) from coordinates), 'SOUL', 1, 'x', 0, (select min(id) from person));

insert into music_band (name, coordinates_id, genre, singles_count, description, albums_count, front_man_id)
values ('A', (select min(id) from coordinates), 'SOUL', 0, 'x', 1, (select min(id) from person));

insert into music_band (name, coordinates_id, genre, singles_count, description, albums_count, front_man_id)
values ('A', (select min(id) from coordinates), 'POP', 1, 'x', 1, (select min(id) from person));

insert into music_band (name, coordinates_id, genre, singles_count, description, albums_count, front_man_id)
values ('A', (select min(id) from coordinates), 'SOUL', 1, 'x', 1, null);

insert into music_band (name, coordinates_id, genre, singles_count, description, albums_count, front_man_id)
values ('A', null, 'SOUL', 1, 'x', 1, (select min(id) from person));

insert into music_band (name, coordinates_id, genre, singles_count, description, albums_count, front_man_id)
values ('A', (select min(id) from coordinates), 'SOUL', 1, null, 1, (select min(id) from person));

insert into music_band (name, coordinates_id, genre, number_of_participants, singles_count, description, albums_count, front_man_id)
values ('A', (select min(id) from coordinates), 'SOUL', 0, 1, 'x', 1, (select min(id) from person));

insert into music_band (name, coordinates_id, genre, singles_count, description, albums_count, front_man_id)
values ('A', (select min(id) from coordinates), 'SOUL', 1, 'x', 1, 99999);

insert into album (name, tracks) values ('', 5);

insert into album (name, tracks) values ('Test', 0);

insert into location (x, y, z) values (1, 2, null);

insert into person (name) values ('');

insert into person (name, eye_color) values ('X', 'GREEN');

delete from person where id = (select min(id) from person);

select id, name, genre, creation_date from music_band order by id;