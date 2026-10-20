package ru.itmo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.itmo.dto.PersonRequest;
import ru.itmo.entity.Location;
import ru.itmo.entity.MusicBand;
import ru.itmo.entity.Person;
import ru.itmo.repository.LocationRepository;
import ru.itmo.repository.MusicBandRepository;
import ru.itmo.repository.PersonRepository;
import ru.itmo.websocket.Notifier;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final LocationRepository locationRepository;
    private final MusicBandRepository bandRepository;
    private final Notifier notifier;

    public PersonService(PersonRepository personRepository,
                         LocationRepository locationRepository,
                         MusicBandRepository bandRepository, Notifier notifier) {
        this.personRepository = personRepository;
        this.locationRepository = locationRepository;
        this.bandRepository = bandRepository;
        this.notifier = notifier;
    }

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public Person getById(Integer id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "человек с id " + id + " не найден ( `ε´ ) "));
    }

    private Location findLocation(Integer id) {
        if (id == null) {
            return null;
        }
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "локация с id " + id + " не найдена"));
    }

    @Transactional
    public Person create(PersonRequest req) {
        Person person = new Person(req.name(), req.eyeColor(), req.hairColor(),
                findLocation(req.locationId()), req.birthday(), req.nationality());
        return personRepository.save(person);
    }

    @Transactional
    public Person update(Integer id, PersonRequest req) {
        Person person = getById(id);
        person.setName(req.name());
        person.setEyeColor(req.eyeColor());
        person.setHairColor(req.hairColor());
        person.setLocation(findLocation(req.locationId()));
        person.setBirthday(req.birthday());
        person.setNationality(req.nationality());
        notifier.changed("bands");

        return person;
    }

    @Transactional
    public void delete(Integer id, Integer replaceWith) {
        Person person = getById(id);
        List<MusicBand> bands = bandRepository.findByFrontMan(person);

        if (!bands.isEmpty()) {
            if (replaceWith == null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "человек является фронтменом в группах (" + bands.size() + "). укажи, кем его заменить");
            }
            if (replaceWith.equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "нельзя заменить человека на него же ( `ε´ ) ");
            }
            Person replacement = getById(replaceWith);
            bands.forEach(b -> b.setFrontMan(replacement));
        }

        personRepository.delete(person);
        notifier.changed("bands");

    }
}