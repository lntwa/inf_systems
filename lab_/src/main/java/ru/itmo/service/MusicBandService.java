package ru.itmo.service;

import jakarta.persistence.criteria.JoinType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.itmo.dto.MusicBandRequest;
import ru.itmo.entity.*;
import ru.itmo.repository.AlbumRepository;
import ru.itmo.repository.CoordinatesRepository;
import ru.itmo.repository.MusicBandRepository;
import ru.itmo.repository.PersonRepository;
import ru.itmo.websocket.Notifier;

import java.util.Arrays;
import java.util.List;

@Service
public class MusicBandService {

    private final MusicBandRepository bandRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final PersonRepository personRepository;
    private final AlbumRepository albumRepository;
    private final Notifier notifier;

    public MusicBandService(MusicBandRepository bandRepository,
                            CoordinatesRepository coordinatesRepository,
                            PersonRepository personRepository,
                            AlbumRepository albumRepository, Notifier notifier) {
        this.bandRepository = bandRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.personRepository = personRepository;
        this.albumRepository = albumRepository;
        this.notifier = notifier;
    }

    @Transactional
    public MusicBand create(MusicBandRequest req) {
        Coordinates coordinates = findCoordinates(req.coordinatesId());
        Person frontMan = findFrontMan(req.frontManId());
        Album bestAlbum = findAlbum(req.bestAlbumId());

        MusicBand band = new MusicBand(req.name(), coordinates, req.genre(),
                req.numberOfParticipants(), req.singlesCount(), req.description(),
                bestAlbum, req.albumsCount(), frontMan);
        notifier.changed("bands");
        return bandRepository.save(band);
    }

    @Transactional
    public MusicBand update(Integer id, MusicBandRequest req) {
        MusicBand band = getById(id);

        band.setName(req.name());
        band.setCoordinates(findCoordinates(req.coordinatesId()));
        band.setGenre(req.genre());
        band.setNumberOfParticipants(req.numberOfParticipants());
        band.setSinglesCount(req.singlesCount());
        band.setDescription(req.description());
        band.setBestAlbum(findAlbum(req.bestAlbumId()));
        band.setAlbumsCount(req.albumsCount());
        band.setFrontMan(findFrontMan(req.frontManId()));
        notifier.changed("bands");
        return band;
    }

    @Transactional
    public void delete(Integer id) {
        MusicBand band = getById(id);
        bandRepository.delete(band);
        notifier.changed("bands");
    }

    private ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    private Coordinates findCoordinates(Integer id) {
        return coordinatesRepository.findById(id)
                .orElseThrow(() -> badRequest("координаты с id " + id + " не найдены"));
    }

    private Person findFrontMan(Integer id) {
        return personRepository.findById(id)
                .orElseThrow(() -> badRequest("фронтмен с id " + id + " не найден"));
    }

    private Album findAlbum(Integer id) {
        if (id == null) {
            return null;
        }
        return albumRepository.findById(id)
                .orElseThrow(() -> badRequest("альбом с id " + id + " не найден"));
    }

    public MusicBand getById(Integer id) {
        return bandRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "группа с таким id " + id + " не найдена (⌒▽⌒)☆ "));
    }

    public Page<MusicBand> getPage(String name, String description, String genre,
                                   String frontMan, String album, Pageable pageable) {
        Specification<MusicBand> spec = (root, query, cb) -> cb.conjunction();

        if (hasText(name)) {
            spec = spec.and((root, q, cb) ->
                    cb.like(cb.lower(root.get("name")), like(name)));
        }
        if (hasText(description)) {
            spec = spec.and((root, q, cb) ->
                    cb.like(cb.lower(root.get("description")), like(description)));
        }
        if (hasText(genre)) {
            List<MusicGenre> matching = Arrays.stream(MusicGenre.values())
                    .filter(g -> g.name().toLowerCase().contains(genre.trim().toLowerCase()))
                    .toList();
            if (matching.isEmpty()) {
                spec = spec.and((root, q, cb) -> cb.disjunction());
            } else {
                spec = spec.and((root, q, cb) -> root.get("genre").in(matching));
            }
        }
        if (hasText(frontMan)) {
            spec = spec.and((root, q, cb) ->
                    cb.like(cb.lower(root.join("frontMan").get("name")), like(frontMan)));
        }
        if (hasText(album)) {
            spec = spec.and((root, q, cb) ->
                    cb.like(cb.lower(root.join("bestAlbum", JoinType.LEFT).get("name")), like(album)));
        }

        return bandRepository.findAll(spec, pageable);
    }

    private boolean hasText(String s) {
        return s != null && !s.isBlank();
    }

    private String like(String value) {
        return "%" + value.trim().toLowerCase() + "%";
    }
}