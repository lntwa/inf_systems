package ru.itmo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.itmo.dto.AlbumRequest;
import ru.itmo.entity.Album;
import ru.itmo.entity.MusicBand;
import ru.itmo.repository.AlbumRepository;
import ru.itmo.repository.MusicBandRepository;
import ru.itmo.websocket.Notifier;

import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final MusicBandRepository bandRepository;
    private final Notifier notifier;

    public AlbumService(AlbumRepository albumRepository, MusicBandRepository bandRepository, Notifier notifier) {
        this.albumRepository = albumRepository;
        this.bandRepository = bandRepository;
        this.notifier = notifier;
    }

    public List<Album> getAll() {
        return albumRepository.findAll();
    }

    public Album getById(Integer id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "альбом с id " + id + " не найден (⌒▽⌒)☆ "));
    }

    @Transactional
    public Album create(AlbumRequest req) {
        return albumRepository.save(new Album(req.name(), req.tracks()));
    }

    @Transactional
    public Album update(Integer id, AlbumRequest req) {
        Album album = getById(id);
        album.setName(req.name());
        album.setTracks(req.tracks());
        notifier.changed("bands");
        return album;
    }
    @Transactional
    public void delete(Integer id, Integer replaceWith) {
        Album album = getById(id);
        List<MusicBand> bands = bandRepository.findByBestAlbum(album);

        if (!bands.isEmpty()) {
            if (replaceWith == null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "альбом используется в группах (" + bands.size() + "). укажи, каким альбомом его заменить");
            }
            if (replaceWith.equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "нельзя заменить альбом на него же ヽ(‵﹏´)ノ ");
            }
            Album replacement = getById(replaceWith);
            bands.forEach(b -> b.setBestAlbum(replacement));
        }

        albumRepository.delete(album);
        notifier.changed("bands");
    }
}