package ru.itmo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "название альбома не должно быть пустым (〃＞＿＜;〃)")
    @Column(nullable = false)
    private String name;

    @Positive(message = "как так нет треков? надо больше 0. ╮(￣～￣)╭ ")
    private Integer tracks;

    protected Album() { }

    public Album(String name, Integer tracks){
        this.name = name;
        this.tracks = tracks;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTracks() {
        return tracks;
    }

    public void setTracks(Integer tracks) {
        this.tracks = tracks;
    }
}
