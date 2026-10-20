package ru.itmo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> { }
