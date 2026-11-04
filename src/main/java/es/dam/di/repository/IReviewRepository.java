package es.dam.di.repository;

import es.dam.di.model.Review;
import java.util.List;

public interface IReviewRepository extends IRepositorioExtend<Review, Long> {
    List<Review> findByVideogameId(Long videogameId);
    double getRecommendationPercentageByVideogameId(Long videogameId);
}