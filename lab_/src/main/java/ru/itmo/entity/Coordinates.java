package ru.itmo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "coordinates")
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Max(value = 170, message = "х должен быть меньше либо равен 170 !!!")
    private long x;

    @NotNull(message = "у не может быть null !!")
    @Column(nullable = false)
    private Integer y;

    protected Coordinates() { }

    public Coordinates (long x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getId() {
        return id;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
