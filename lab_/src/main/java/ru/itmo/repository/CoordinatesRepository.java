package ru.itmo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.entity.Coordinates;

public interface CoordinatesRepository extends JpaRepository<Coordinates, Integer> { }
