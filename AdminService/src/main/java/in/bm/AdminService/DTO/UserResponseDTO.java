package in.bm.AdminService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private int id;
    private String username;
    private String surname;
    private String email;
    private String city;
    private String phoneNumber;
    private LocalDate birthDate;

    private LocalDateTime createdTime;
    private LocalDateTime updateTime;

    private int count;
    private String role;
}
