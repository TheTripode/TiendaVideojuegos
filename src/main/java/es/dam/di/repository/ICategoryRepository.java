package es.dam.di.repository;

import es.dam.di.model.Category;
import java.util.List;
import java.util.Optional;

public interface ICategoryRepository extends IRepositorioExtend<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findByVideogameId(Long videogameId);
}