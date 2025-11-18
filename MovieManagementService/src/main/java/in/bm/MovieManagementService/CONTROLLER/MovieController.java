package in.bm.MovieManagementService.CONTROLLER;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import in.bm.MovieManagementService.ENTITY.Movie;
import in.bm.MovieManagementService.SERVICE.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        String key = "Movies";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<Movie> movies = null;
        try {
            Object moviesData = redisTemplate.opsForValue().get(key);

            if (moviesData != null && !moviesData.toString().isBlank()) {
                List<Movie> cachedMovies = objectMapper.readValue(
                        moviesData.toString(),
                        new TypeReference<List<Movie>>() {}
                );

                if (!cachedMovies.isEmpty()) {
                    return ResponseEntity.ok(cachedMovies); // return cache
                }
            }

        } catch (Exception e) {
            log.error("Redis UNAVAILABLE, falling back to DB: {}", e.getMessage());
        }
        movies = movieService.getAllMovies();
        try {
            String moviesJson = objectMapper.writeValueAsString(movies);
            redisTemplate.opsForValue().set(key, moviesJson);
        } catch (Exception e) {
            log.warn("Redis still unavailable → skipping caching");
        }
        return ResponseEntity.ok(movies);
    }


    @GetMapping("/searchByName")
    public ResponseEntity<Movie> searchByName(@RequestParam String name) {
        Movie movie = movieService.searchByName(name);
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(movie);
    }

        @GetMapping("/searchByType")
    public ResponseEntity<List<Movie>> searchByType(@RequestParam String movieType) {
        List<Movie> movies = movieService.searchByType(movieType);
        if (movies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(movies);
        }
        return ResponseEntity.ok(movies);
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.addMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }


}
