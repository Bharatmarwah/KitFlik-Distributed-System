package in.bm.UserService.REPOSITORY;

import in.bm.UserService.ENTITY.userRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<userRegister, Integer> {
     userRegister findByUsername(String username);
     userRegister save(userRegister register);
     userRegister findByOtp(String otp);

     @Query("SELECT u.email FROM userRegister u")
     List<String> findByEmails();

     @Query("SELECT u.email FROM userRegister u WHERE u.username = :username")
     String findEmailByUsername(@Param("username") String username);

     boolean existsByUsername(String Username);
}
