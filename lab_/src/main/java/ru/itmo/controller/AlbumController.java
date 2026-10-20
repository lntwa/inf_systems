package ru.itmo.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itmo.dto.AlbumRequest;
import ru.itmo.entity.Album;
import ru.itmo.repository.MusicBandRepository;
import ru.itmo.service.AlbumService;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final MusicBandRepository bandRepository;

    public AlbumController(AlbumService albumService, MusicBandRepository bandRepository) {
        this.albumService = albumService;
        this.bandRepository = bandRepository;
    }

    @GetMapping
    public List<Album> getAll() {
        return albumService.getAll();
    }

    @GetMapping("/{id}")
    public Album getById(@PathVariable Integer id) {
        return albumService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Album create(@Valid @RequestBody AlbumRequest request) {
        return albumService.create(request);
    }

    @PutMapping("/{id}")
    public Album update(@PathVariable Integer id, @Valid @RequestBody AlbumRequest request) {
        return albumService.update(id, request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id,
                       @RequestParam(required = false) Integer replaceWith) {
        albumService.delete(id, replaceWith);
    }
}