package in.bm.AuthGateway.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class userForgetPasswordDto {

    @NotBlank
    @JsonProperty("otp")
    private String otp;

    @NotBlank
    @JsonProperty("password")
    private String password;

}
