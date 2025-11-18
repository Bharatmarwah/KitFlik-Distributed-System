package in.bm.MovieManagementService.ENTITY;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "booking")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private Long movieId;
    private Long theatreId;
    private Long showTimingId;

    @ElementCollection
    private List<String> seatsBooked;

    private String status;
    private LocalDate bookingDate;
    private LocalTime bookingTime;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "price_id", referencedColumnName = "id")
    private PriceDetails priceDetails;

    @PrePersist
    public void onCreate() {
        this.bookingDate = LocalDate.now();
        this.bookingTime = LocalTime.now();
        this.status = "PENDING";
    }
}
