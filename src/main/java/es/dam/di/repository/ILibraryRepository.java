package es.dam.di.repository;

import es.dam.di.model.Library;
import java.util.List;
import java.util.Optional;

public interface ILibraryRepository extends IRepositorioExtend<Library, Long> {
    List<Library> findByAccountId(Long accountId);
    Optional<Library> findByAccountIdAndVideogameId(Long accountId, Long videogameId);
}