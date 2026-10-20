package ru.itmo.service;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.itmo.entity.MusicGenre;
import ru.itmo.websocket.Notifier;

@Service
public class SpecialOperationsService {

    private final JdbcTemplate jdbc;
    private final Notifier notifier;


    public SpecialOperationsService(JdbcTemplate jdbc, Notifier notifier) {
        this.jdbc = jdbc;
        this.notifier = notifier;
    }

    public Double averageAlbumsCount() {
        return jdbc.queryForObject("select avg_albums_count()", Double.class);
    }

    public Long countAlbumsLessThan(int limit) {
        return jdbc.queryForObject(
                "select count_bands_albums_less_than(?)", Long.class, limit);
    }

    public Long countFrontManGreaterThan(int limit) {
        return jdbc.queryForObject(
                "select count_bands_front_man_greater_than(?)", Long.class, limit);
    }

    public Long removeParticipant(int bandId) {
        try {
            Long result = jdbc.queryForObject("select remove_participant(?)", Long.class, bandId);
            notifier.changed("bands");
            return result;
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, extractMessage(e));
        }
    }

    public Integer nominate(int bandId, MusicGenre genre) {
        try {
            return jdbc.queryForObject(
                    "select nominate_band(?, ?::music_genre)", Integer.class,
                    bandId, genre.name());
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, extractMessage(e));
        }
    }

    private String extractMessage(DataAccessException e) {
        Throwable cause = e.getMostSpecificCause();
        String msg = cause.getMessage();
        if (msg == null) {
            return "Ошибка операции";
        }
        int nl = msg.indexOf('\n');
        msg = nl >= 0 ? msg.substring(0, nl) : msg;
        return msg.replaceFirst("^(ERROR|ОШИБКА):\\s*", "");
    }
}