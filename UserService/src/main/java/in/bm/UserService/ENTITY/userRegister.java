package in.bm.UserService.ENTITY;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user")
public class userRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @JsonProperty("username")
    @Column(unique = true)
    private String username;

    @JsonProperty("surname")
    @Column
    private String surname;

    @NotBlank
    @JsonProperty("password")
    @Column(unique = false, nullable = false)
    private String password;

    @JsonProperty("email")
    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Please provide a valid email address with a proper domain"
    )
    private String email;

    @JsonProperty("birthDate")
    @Column(nullable = false)
    private LocalDate birthDate; // yy/mm/dd

    @JsonProperty("city")
    @Column(length = 50)
    private String city;

    @NotBlank
    @JsonProperty("phoneNumber")
    @Column(length = 10, unique = true)
    private String phoneNumber;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    @Column(name = "loginCount")
    private int count = 0;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;

    private String role;

    @PrePersist
    public void role() {
        this.role = "USER";
    }
}
