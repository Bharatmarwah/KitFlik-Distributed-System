package in.bm.MovieManagementService.REPOSITORY;

import in.bm.MovieManagementService.ENTITY.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieDeo extends JpaRepository<Movie,Long> {

    @Query(value = "SELECT * FROM movies m WHERE m.title =:name",nativeQuery = true)
    Movie findbyName(String name);

    @Query("SELECT m FROM Movie m WHERE m.movieType = :movieType")
    List<Movie> findByMovieType(@Param("movieType") String movieType);

}
