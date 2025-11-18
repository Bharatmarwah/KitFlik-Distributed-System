package in.bm.AdminService.CONTROLLER;

import in.bm.AdminService.CONFIG.WebclientImp;
import in.bm.AdminService.DTO.AdminLogin;
import in.bm.AdminService.DTO.BookingDTO;
import in.bm.AdminService.DTO.UsersDetails;
import in.bm.AdminService.SERVICE.AdminService;
import in.bm.AdminService.SERVICE.BlacklistService;
import in.bm.AdminService.SERVICE.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private AuthenticationManager adminAuthenticationManager;

    @Autowired
    private WebclientImp webclientImp;

    //localhost:9898/admin/login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody AdminLogin admin) {
        Map<String, String> body = new HashMap<>();
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword());
        Authentication authentication = adminAuthenticationManager.authenticate(token);

        if (authentication.isAuthenticated()) {
            String accessToken = jwtToken.AdminAccessTokenGeneration(admin.getUsername());
            log.info("Admin Logged in");
            body.put("adminToken", accessToken);
            return ResponseEntity.ok(body);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }
    //localhost:9898/admin/dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<List<UsersDetails>> adminDashboard() {
        List<UsersDetails> detailsList = adminService.usersDetailsForAdminDashboard();
        return new ResponseEntity<>(detailsList,HttpStatus.OK);
    }

    @GetMapping("dashboard/user/{id}")
    public ResponseEntity<UsersDetails> adminDashboardUserById(@PathVariable int id){
        UsersDetails user = adminService.userDetailsForAdminDashboard(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @DeleteMapping("/remove/user/{id}")
    public ResponseEntity<String> userBlocked(@PathVariable int id, HttpServletRequest request) {
        String response = adminService.delete(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            blacklistService.blacklistToken(token);
            log.info("Admin logged out");
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or missing Authorization header");
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDTO>> bookings(){
        return new ResponseEntity<>
                (webclientImp.moviesBookingsdetails()
                        ,HttpStatus.OK);
    }

    @GetMapping("bookings/{bookingId}")
    public ResponseEntity<BookingDTO> bookingsPerId(@PathVariable Long bookingId){
        return new ResponseEntity<>(webclientImp.movieBookingDetailsPerUser(bookingId),HttpStatus.OK);

    }
}
