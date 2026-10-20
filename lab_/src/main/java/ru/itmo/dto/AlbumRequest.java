package ru.itmo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AlbumRequest(

        @NotBlank(message = "название альбома не должно быть пустым (〃＞＿＜;〃)")
        String name,

        @Positive(message = "как так нет треков? надо больше 0. ╮(￣～￣)╭ ")
        Integer tracks
) { }