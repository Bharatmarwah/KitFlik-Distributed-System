package in.bm.AuthGateway.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class userRegisterDto {

    @NotBlank(message = "Username is required")
    @JsonProperty("username")
    private String username;

    @JsonProperty("surname")
    private String surname;

    @NotBlank(message = "Password is required")
    @JsonProperty("password")
    private String password;

    @NotBlank(message = "Email is required")
    @JsonProperty("email")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Please provide a valid email address"
    )
    private String email;

    @NotBlank(message = "Phone number is required")
    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("city")
    private String city;

    @JsonProperty("birthDate")
    @NotBlank(message = "Birth date is required")
    private String birthDate;
}
