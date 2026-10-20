package ru.itmo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record CoordinatesRequest(

        @NotNull(message = "x не может быть null")
        @Max(value = 170, message = "x не может быть больше 170 !!!")
        Long x,

        @NotNull(message = "y не может быть null !")
        Integer y
) { }