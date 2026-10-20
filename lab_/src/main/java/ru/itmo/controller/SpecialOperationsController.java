package ru.itmo.controller;

import org.springframework.web.bind.annotation.*;
import ru.itmo.entity.MusicGenre;
import ru.itmo.service.SpecialOperationsService;

import java.util.Map;

@RestController
@RequestMapping("/api/special")
public class SpecialOperationsController {

    private final SpecialOperationsService service;

    public SpecialOperationsController(SpecialOperationsService service) {
        this.service = service;
    }

    @GetMapping("/average-albums-count")
    public Map<String, Object> averageAlbumsCount() {
        Double result = service.averageAlbumsCount();
        return Map.of("result", result == null ? "нет данных" : result);
    }

    @GetMapping("/count-albums-less-than")
    public Map<String, Long> countAlbumsLessThan(@RequestParam int value) {
        return Map.of("result", service.countAlbumsLessThan(value));
    }

    @GetMapping("/count-front-man-greater-than")
    public Map<String, Long> countFrontManGreaterThan(@RequestParam int value) {
        return Map.of("result", service.countFrontManGreaterThan(value));
    }

    @PostMapping("/bands/{id}/remove-participant")
    public Map<String, Long> removeParticipant(@PathVariable int id) {
        return Map.of("numberOfParticipants", service.removeParticipant(id));
    }

    @PostMapping("/bands/{id}/nominate")
    public Map<String, Integer> nominate(@PathVariable int id,
                                         @RequestParam MusicGenre genre) {
        return Map.of("nominationId", service.nominate(id, genre));
    }
}