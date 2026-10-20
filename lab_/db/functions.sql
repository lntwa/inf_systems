create or replace function avg_albums_count()
    returns double precision
    language sql
as $$
select avg(albums_count)::double precision from music_band;
$$;

create or replace function count_bands_albums_less_than(limit_value integer)
    returns bigint
    language sql
as $$
select count(*) from music_band where albums_count < limit_value;
$$;

create or replace function count_bands_front_man_greater_than(limit_id integer)
    returns bigint
    language sql
as $$
select count(*) from music_band where front_man_id > limit_id;
$$;

create or replace function remove_participant(p_band_id integer)
    returns bigint
    language plpgsql
as $$
declare
    current_count bigint;
begin
    select number_of_participants into current_count
    from music_band where id = p_band_id for update;

    if not found then
        raise exception 'группа с id % не найдена', p_band_id;
    end if;
    if current_count is null then
        raise exception 'у группы % не указано количество участников', p_band_id;
    end if;
    if current_count <= 1 then
        raise exception 'в группе % остался один участник, удалять некого', p_band_id;
    end if;

    update music_band set number_of_participants = current_count - 1 where id = p_band_id;
    return current_count - 1;
end;
$$;

create or replace function nominate_band(p_band_id integer, p_genre music_genre)
    returns integer
    language plpgsql
as $$
declare
    new_id integer;
begin
    if not exists (select 1 from music_band where id = p_band_id) then
        raise exception 'Группа с id % не найдена', p_band_id;
    end if;

    insert into nomination (band_id, genre) values (p_band_id, p_genre)
    returning id into new_id;
    return new_id;
end;
$$;