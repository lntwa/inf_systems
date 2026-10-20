package ru.itmo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.itmo.entity.Album;
import ru.itmo.entity.Coordinates;
import ru.itmo.entity.MusicBand;
import ru.itmo.entity.Person;

import java.util.List;

public interface MusicBandRepository extends JpaRepository<MusicBand, Integer>, JpaSpecificationExecutor<MusicBand> {
    List<MusicBand> findByBestAlbum(Album album);
    List<MusicBand> findByCoordinates(Coordinates coordinates);
    List<MusicBand> findByFrontMan(Person frontMan);
}