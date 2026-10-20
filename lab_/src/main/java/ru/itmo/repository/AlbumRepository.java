package ru.itmo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.entity.Album;

public interface AlbumRepository extends JpaRepository<Album, Integer> { }
