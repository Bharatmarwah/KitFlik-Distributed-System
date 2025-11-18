package in.bm.MovieManagementService.ENTITY;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "show_timings")
public class ShowTiming {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long showId;

    private LocalDate date;
    private LocalTime time;

    private int totalSeats;
    private int availableSeats;

    @ManyToOne
    @JoinColumn(name = "theatre_id")
    @JsonBackReference
    private Theatre theatre;

    @ElementCollection
    @CollectionTable(name = "booked_seats", joinColumns = @JoinColumn(name = "show_id"))
    @Column(name = "seat")
    private List<String> bookedSeats;

    @ElementCollection
    @CollectionTable(name = "all_seats", joinColumns = @JoinColumn(name = "show_id"))
    @Column(name = "seat")
    private List<String> allSeats;

    @ElementCollection
    @CollectionTable(name = "seat_prices", joinColumns = @JoinColumn(name = "show_id"))
    @MapKeyColumn(name = "seat")
    @Column(name = "price")
    private Map<String, Double> seatPrices;
}
