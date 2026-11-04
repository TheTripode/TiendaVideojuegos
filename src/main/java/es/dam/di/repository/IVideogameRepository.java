package es.dam.di.repository;

import es.dam.di.model.Videogame;
import java.util.List;

public interface IVideogameRepository extends IRepositorioExtend<Videogame, Long> {
    List<Videogame> findByDeveloperId(Long developerId);
    List<Videogame> findByCategoryId(Long categoryId);
}