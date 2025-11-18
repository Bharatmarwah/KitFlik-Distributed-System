package in.bm.MovieManagementService.REPOSITORY;

import in.bm.MovieManagementService.ENTITY.ShowTiming;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowTimingDeo extends JpaRepository<ShowTiming,Long> {
}
