package in.bm.MovieManagementService.SERVICE;

import in.bm.MovieManagementService.ENTITY.Movie;
import in.bm.MovieManagementService.ENTITY.ShowTiming;
import in.bm.MovieManagementService.ENTITY.Theatre;
import in.bm.MovieManagementService.REPOSITORY.MovieDeo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class MovieService {

    @Autowired
    private MovieDeo deo;



    // Add movie with theatres and show timings linked
    public Movie addMovie(Movie movie) {
        if (movie.getTheatres() != null) {
            for (Theatre theatre : movie.getTheatres()) {
                theatre.setMovie(movie);
                if (theatre.getShowTimings() != null) {
                    for (ShowTiming show : theatre.getShowTimings()) {
                        show.setTheatre(theatre); // link show back to theatre
                    }
                }
            }
        }
        Random random = new Random();
        String code = "MV"+ (100+random.nextInt(900));
        movie.setMovieCode(code);
        return deo.save(movie);
    }

    public List<Movie> getAllMovies() {

        return deo.findAll();

    }


    public Movie searchByName(String name) {
        return deo.findbyName(name);
    }


    public List<Movie> searchByType(String movieType) {
        return deo.findByMovieType(movieType);
    }
}
