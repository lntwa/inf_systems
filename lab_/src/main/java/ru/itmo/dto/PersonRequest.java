package ru.itmo.dto;

import jakarta.validation.constraints.NotBlank;
import ru.itmo.entity.Color;
import ru.itmo.entity.Country;

import java.time.LocalDateTime;

public record PersonRequest(

        @NotBlank(message = "имя исполнителя не должно быть пустым (〃＞＿＜;〃)")
        String name,

        Color eyeColor,
        Color hairColor,
        Integer locationId,
        LocalDateTime birthday,
        Country nationality
) { }