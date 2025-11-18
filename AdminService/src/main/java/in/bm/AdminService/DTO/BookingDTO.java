package in.bm.AdminService.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BookingDTO {

    private Long bookingId;
    private Long movieId;
    private Long theatreId;
    private Long showTimingId;
    private List<String> seatsBooked;
    private String status;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private PriceDetailsDTO priceDetails;
}
