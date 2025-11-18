package in.bm.UserService.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class userForgetPassword {

    @Column(name = "otp")
    private String otp;

    @NotBlank
    @JsonProperty("password")
    @Column(unique = true, nullable = false)
    private String password;

}
