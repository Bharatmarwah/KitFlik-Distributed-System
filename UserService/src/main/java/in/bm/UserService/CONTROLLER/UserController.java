package in.bm.UserService.CONTROLLER;

import in.bm.UserService.CLIENT.WebClientImp;
import in.bm.UserService.DTO.userForgetPassword;
import in.bm.UserService.DTO.userLogin;
import in.bm.UserService.ENTITY.userRegister;
import in.bm.UserService.REPOSITORY.UserRepo;
import in.bm.UserService.SERVICE.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepo repo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private WebClientImp webClientImp;

    @GetMapping("/test")
    public String test(@RequestHeader("Username")String username,@RequestHeader("Role")String role){
        return username+" is a "+ role;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody userRegister register) {
        try {
            if (repo.existsByUsername(register.getUsername().toLowerCase()))
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");

            register.setUsername(register.getUsername().toLowerCase());
            register.setPassword(encoder.encode(register.getPassword()));

            userRegister saved = service.saveUser(register);

            webClientImp.sendRegistationMail(saved.getEmail(), saved.getUsername());
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register");
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, String>> validate(@Valid @RequestBody userLogin credentials) {
        userRegister user = repo.findByUsername(credentials.getUsername().toLowerCase());

        if (user == null || !encoder.matches(credentials.getPassword(), user.getPassword()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));

        user.setCount(user.getCount() + 1);
        repo.save(user);

        Map<String, String> body = new HashMap<>();
        body.put("Username", user.getUsername());
        body.put("Role", user.getRole());

        return ResponseEntity.ok(body);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String username) {
        try {
            userRegister user = repo.findByUsername(username.toLowerCase());
            if (user == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            String otp = String.format("%04d", new Random().nextInt(10000));
            System.out.println(otp);
            user.setOtp(otp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
            repo.save(user);

            webClientImp.sendOtpMail(otp, user.getUsername(), user.getEmail());
            return ResponseEntity.ok("OTP sent to email");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody userForgetPassword request) {
        try {
            userRegister user = repo.findByOtp(request.getOtp());
            if (user == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid OTP");
            if(encoder.matches(request.getPassword(),user.getPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please choose a different password");
            }
            if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP expired");

            user.setPassword(encoder.encode(request.getPassword()));
            user.setOtp(null);
            user.setOtpExpiry(null);
            user.setUpdateTime(LocalDateTime.now());
            repo.save(user);

            return ResponseEntity.ok("Password reset successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to reset password");
        }
    }

    @GetMapping("/details/{username}")
    public ResponseEntity<Map<String, String>> getUserDetails(@PathVariable String username) {
        userRegister user = repo.findByUsername(username.toLowerCase());
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));

        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "role", user.getRole()
        ));
    }

    @GetMapping("/userdetails")
    public ResponseEntity<List<userRegister>> usersForAdmin() {
        return ResponseEntity.ok(service.fetchUserforAdmin());
    }

    @DeleteMapping("/userRemove/{id}")
    public ResponseEntity<String> removeUserByAdmin(@PathVariable int id) {
        return ResponseEntity.ok(service.removeByID(id));
    }

    @GetMapping("/emails")
    public ResponseEntity<List<String>> getEmails() {
        return ResponseEntity.ok(service.fetchEmails());
    }

    @GetMapping("/username/email")
    public ResponseEntity<String> getEmailByUsername(@RequestParam String username) {
        return ResponseEntity.ok(service.EmailByUsername(username.toLowerCase()));
    }


    @GetMapping("/{id}")
    public ResponseEntity<userRegister> fetchUserByIdForAdmin(@PathVariable int id) {
        userRegister user = service.userById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
