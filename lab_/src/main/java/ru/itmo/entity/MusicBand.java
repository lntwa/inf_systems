package ru.itmo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Entity
@Table(name = "music_band")
public class MusicBand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "название группы не может быть пустым ..・ヾ(。＞＜)シ ")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "координаты обязательны ! ! !")
    @ManyToOne(optional = false)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @Generated(event = EventType.INSERT)
    @Column(name = "creation_date", insertable = false, updatable = false)
    private LocalDate creationDate;

    @NotNull(message = "жанр обязателен ! !")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "genre", nullable = false)
    private MusicGenre genre;

    @Positive(message = "количество участников должно быть больше 0")
    @Column(name = "number_of_participants")
    private Long numberOfParticipants;

    @NotNull(message = "количество синглов обязательно")
    @Positive(message = "количество синглов должно быть больше 0")
    @Column(name = "singles_count", nullable = false)
    private Integer singlesCount;

    @NotNull(message = "описание не может быть null 〜(＞＜)〜 ")
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "best_album_id")
    private Album bestAlbum;

    @Positive(message = "количество альбомов должно быть больше 0")
    @Column(name = "albums_count", nullable = false)
    private int albumsCount;

    @NotNull(message = "фронтмен обязателен! ! !")
    @ManyToOne(optional = false)
    @JoinColumn(name = "front_man_id", nullable = false)
    private Person frontMan;

    protected MusicBand() { }

    public MusicBand(String name, Coordinates coordinates, MusicGenre genre,
                     Long numberOfParticipants, Integer singlesCount, String description,
                     Album bestAlbum, int albumsCount, Person frontMan) {
        this.name = name;
        this.coordinates = coordinates;
        this.genre = genre;
        this.numberOfParticipants = numberOfParticipants;
        this.singlesCount = singlesCount;
        this.description = description;
        this.bestAlbum = bestAlbum;
        this.albumsCount = albumsCount;
        this.frontMan = frontMan;
    }

    public Integer getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }

    public LocalDate getCreationDate() { return creationDate; }

    public MusicGenre getGenre() { return genre; }
    public void setGenre(MusicGenre genre) { this.genre = genre; }

    public Long getNumberOfParticipants() { return numberOfParticipants; }
    public void setNumberOfParticipants(Long numberOfParticipants) { this.numberOfParticipants = numberOfParticipants; }

    public Integer getSinglesCount() { return singlesCount; }
    public void setSinglesCount(Integer singlesCount) { this.singlesCount = singlesCount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Album getBestAlbum() { return bestAlbum; }
    public void setBestAlbum(Album bestAlbum) { this.bestAlbum = bestAlbum; }

    public int getAlbumsCount() { return albumsCount; }
    public void setAlbumsCount(int albumsCount) { this.albumsCount = albumsCount; }

    public Person getFrontMan() { return frontMan; }
    public void setFrontMan(Person frontMan) { this.frontMan = frontMan; }
}