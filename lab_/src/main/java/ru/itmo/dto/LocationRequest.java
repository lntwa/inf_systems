package ru.itmo.dto;

import jakarta.validation.constraints.NotNull;

public record LocationRequest(

        @NotNull(message = "x не может быть null")
        Long x,

        @NotNull(message = "y не может быть null")
        Integer y,

        @NotNull(message = "z не может быть null")
        Long z
) { }