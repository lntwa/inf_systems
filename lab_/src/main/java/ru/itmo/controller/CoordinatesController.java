package ru.itmo.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itmo.dto.CoordinatesRequest;
import ru.itmo.entity.Coordinates;
import ru.itmo.service.CoordinatesService;

import java.util.List;

@RestController
@RequestMapping("/api/coordinates")
public class CoordinatesController {

    private final CoordinatesService coordinatesService;

    public CoordinatesController(CoordinatesService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }

    @GetMapping
    public List<Coordinates> getAll() {
        return coordinatesService.getAll();
    }

    @GetMapping("/{id}")
    public Coordinates getById(@PathVariable Integer id) {
        return coordinatesService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Coordinates create(@Valid @RequestBody CoordinatesRequest request) {
        return coordinatesService.create(request);
    }

    @PutMapping("/{id}")
    public Coordinates update(@PathVariable Integer id, @Valid @RequestBody CoordinatesRequest request) {
        return coordinatesService.update(id, request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id,
                       @RequestParam(required = false) Integer replaceWith) {
        coordinatesService.delete(id, replaceWith);
    }
}