package ru.itmo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.itmo.dto.CoordinatesRequest;
import ru.itmo.entity.Coordinates;
import ru.itmo.entity.MusicBand;
import ru.itmo.repository.CoordinatesRepository;
import ru.itmo.repository.MusicBandRepository;
import ru.itmo.websocket.Notifier;

import java.util.List;

@Service
public class CoordinatesService {

    private final CoordinatesRepository coordinatesRepository;
    private final MusicBandRepository bandRepository;
    private final Notifier notifier;

    public CoordinatesService(CoordinatesRepository coordinatesRepository, MusicBandRepository bandRepository, Notifier notifier) {
        this.coordinatesRepository = coordinatesRepository;
        this.bandRepository =bandRepository;
        this.notifier = notifier;
    }

    public List<Coordinates> getAll() {
        return coordinatesRepository.findAll();
    }

    public Coordinates getById(Integer id) {
        return coordinatesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "координаты с id " + id + " не найдены"));
    }

    @Transactional
    public Coordinates create(CoordinatesRequest req) {
        return coordinatesRepository.save(new Coordinates(req.x(), req.y()));
    }

    @Transactional
    public Coordinates update(Integer id, CoordinatesRequest req) {
        Coordinates coordinates = getById(id);
        coordinates.setX(req.x());
        coordinates.setY(req.y());
        notifier.changed("bands");
        return coordinates;
    }

    @Transactional
    public void delete(Integer id, Integer replaceWith) {
        Coordinates coordinates = getById(id);
        List<MusicBand> bands = bandRepository.findByCoordinates(coordinates);

        if (!bands.isEmpty()) {
            if (replaceWith == null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "координаты используются в группах (" + bands.size() + "). укажи, чем их заменить");
            }
            if (replaceWith.equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "нельзя заменить координаты на них же");
            }
            Coordinates replacement = getById(replaceWith);
            bands.forEach(b -> b.setCoordinates(replacement));
        }

        coordinatesRepository.delete(coordinates);
        notifier.changed("bands");
    }
}