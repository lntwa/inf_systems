package ru.itmo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.itmo.dto.LocationRequest;
import ru.itmo.entity.Location;
import ru.itmo.entity.Person;
import ru.itmo.repository.LocationRepository;
import ru.itmo.repository.PersonRepository;
import ru.itmo.websocket.Notifier;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final PersonRepository personRepository;
    private final Notifier notifier;

    public LocationService(LocationRepository locationRepository,
                           PersonRepository personRepository, Notifier notifier) {
        this.locationRepository = locationRepository;
        this.personRepository = personRepository;
        this.notifier = notifier;
    }

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Location getById(Integer id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "локация с id " + id + " не найдена"));
    }

    @Transactional
    public Location create(LocationRequest req) {
        return locationRepository.save(new Location(req.x(), req.y(), req.z()));
    }

    @Transactional
    public Location update(Integer id, LocationRequest req) {
        Location location = getById(id);
        location.setX(req.x());
        location.setY(req.y());
        location.setZ(req.z());
        notifier.changed("bands");

        return location;
    }

    @Transactional
    public void delete(Integer id, Integer replaceWith) {
        Location location = getById(id);
        List<Person> people = personRepository.findByLocation(location);

        if (!people.isEmpty()) {
            if (replaceWith != null && replaceWith.equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "нельзя заменить локацию на нее же");
            }
            Location replacement = replaceWith == null ? null : getById(replaceWith);
            people.forEach(p -> p.setLocation(replacement));
        }

        locationRepository.delete(location);
        notifier.changed("bands");

    }
}