package ru.itmo.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itmo.dto.MusicBandRequest;
import ru.itmo.entity.MusicBand;
import ru.itmo.entity.MusicGenre;
import ru.itmo.service.MusicBandService;

@RestController
@RequestMapping("/api/bands")
public class MusicBandController {

    private final MusicBandService bandService;

    public MusicBandController(MusicBandService bandService) {
        this.bandService = bandService;
    }

    @GetMapping("/{id}")
    public MusicBand getById(@PathVariable Integer id) {
        return bandService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MusicBand create(@Valid @RequestBody MusicBandRequest request) {
        return bandService.create(request);
    }

    @PutMapping("/{id}")
    public MusicBand update(@PathVariable Integer id, @Valid @RequestBody MusicBandRequest request) {
        return bandService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        bandService.delete(id);
    }

    @GetMapping
    public Page<MusicBand> getPage(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String description,
                                   @RequestParam(required = false) String genre,
                                   @RequestParam(required = false) String frontMan,
                                   @RequestParam(required = false) String album,
                                   @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return bandService.getPage(name, description, genre, frontMan, album, pageable);
    }
}