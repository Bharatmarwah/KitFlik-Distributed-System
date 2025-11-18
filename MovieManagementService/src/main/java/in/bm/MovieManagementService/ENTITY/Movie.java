package in.bm.MovieManagementService.ENTITY;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @Column(unique = true)
    private String movieCode;

    private String title;
    private String genre;
    private String language;

    @Lob
    private String description;

    private String movieType;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Theatre> theatres;
}
// cascadeType.ALL the operations(save,update,delete) happening on the parent side should be happen at the child side too
// fetchType.LAZY means here that when the theatre.getTheatre will call only when fetch the data avoiding unnecessaries serialization