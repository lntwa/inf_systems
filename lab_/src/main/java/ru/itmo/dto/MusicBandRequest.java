package ru.itmo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.itmo.entity.MusicGenre;

public record MusicBandRequest(

        @NotBlank(message = "название группы не может быть пустым!!")
        String name,

        @NotNull(message = "нужно указать id координат")
        Integer coordinatesId,

        @NotNull(message = "жанр обязателен!")
        MusicGenre genre,

        @Positive(message = "количество участников должно быть больше 0")
        Long numberOfParticipants,

        @NotNull(message = "количество синглов обязательно")
        @Positive(message = "количество синглов должно быть больше 0")
        Integer singlesCount,

        @NotNull(message = "описание не может быть null")
        String description,

        Integer bestAlbumId,

        @NotNull(message = "количество альбомов обязательно")
        @Positive(message = "количество альбомов должно быть больше 0")
        Integer albumsCount,

        @NotNull(message = "Нужно указать id фронтмена")
        Integer frontManId
) { }