package in.bm.AdminService.DTO;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class UsersDetails {

    private final int id;
    private final String username;
    private final String surname;
    private final String email;
    private final String city;
    private final String phoneNumber;
    private final LocalDate birthDate;

    private final LocalDateTime createdTime;
    private final LocalDateTime updateTime;

    private final int loginCount;
    private final String role;

    public UsersDetails(
            int id,
            String username,
            String surname,
            String email,
            String city,
            String phoneNumber,
            LocalDate birthDate,
            LocalDateTime createdTime,
            LocalDateTime updateTime,
            int loginCount,
            String role
    ) {
        this.id = id;
        this.username = username;
        this.surname = surname;
        this.email = email;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.createdTime = createdTime;
        this.updateTime = updateTime;
        this.loginCount = loginCount;
        this.role = role;
    }
}
