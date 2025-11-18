package in.bm.MovieManagementService.CONTROLLER;

import in.bm.MovieManagementService.CLIENT.UserServiceWebClient;
import in.bm.MovieManagementService.ENTITY.Booking;
import in.bm.MovieManagementService.SERVICE.BookingService;
import in.bm.MovieManagementService.SERVICE.ConfirmationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ConfirmationService confirmationService;
    
    @Autowired
    private UserServiceWebClient userServiceWebClient;


    //localhost:8889/bookings/add
    @PostMapping("/add")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        return bookingService.addBooking(booking);
    }

    //localhost:8889/bookings/1
    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<String> cancelingBooking(@PathVariable long bookingId) {
        return bookingService.cancelBooking(bookingId);
    }

    //localhost:8889/bookings/update
    @PutMapping("/update")
    public ResponseEntity<Booking> updateBooking(@RequestBody Booking booking){
        return bookingService.changeBookingDetails(booking);
    }

    //localhost:8889/bookings/confirm/{id}
    @GetMapping("/confirm/{id}")
    public ResponseEntity<String> confirmBooking(@PathVariable Long id,
         @RequestHeader("Username")String username) {
        String userEmail = userServiceWebClient.getUserEmailByUsername(username);
        return confirmationService.confirmation(id, userEmail);
    }

    //http://localhost:8889/bookings/info
    @GetMapping("/info")
    public ResponseEntity<List<Booking>> BookingMoviesDetails(){
        return new ResponseEntity<>(bookingService.fetchAllBooking(), HttpStatus.OK);
    }
    //http://localhost:8889/bookings/1
    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> bookingDetailsAsPerBookingId(@PathVariable long bookingId){
        Booking bookingDetailsById = bookingService.fetchBookingDetailsById(bookingId);
        return new ResponseEntity<>(bookingDetailsById, HttpStatus.OK);
    }

}
