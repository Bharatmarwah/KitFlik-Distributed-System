package in.bm.MovieManagementService.REPOSITORY;

import in.bm.MovieManagementService.ENTITY.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDeo extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.status = 'CONFIRMED'")
    List<Booking> findAllConfirmedBookings();

}


