package es.dam.di.repository;

import es.dam.di.model.Developer;
import java.util.List;
import java.util.Optional;

public interface IDeveloperRepository extends IRepositorioExtend<Developer, Long> {
    Optional<Developer> findByName(String name);
    List<Developer> findByNameContaining(String name);
}