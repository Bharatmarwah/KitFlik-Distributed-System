package in.bm.AdminService.SERVICE;

import in.bm.AdminService.CONFIG.WebclientImp;
import in.bm.AdminService.DTO.UserResponseDTO;
import in.bm.AdminService.DTO.UsersDetails;
import in.bm.AdminService.EXCEPTION.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminService {

    @Autowired
    private WebclientImp webclientImp;

    public List<UsersDetails> usersDetailsForAdminDashboard() {
        log.info("Admin requested user dashboard data");
        try {
            List<UserResponseDTO> usersResponseDto = webclientImp.usersDetails();
            if (usersResponseDto == null) {
                log.warn("User service returned null user list");
                return new ArrayList<>();
            }
            List<UsersDetails> usersDetails = new ArrayList<>();
            for (UserResponseDTO u : usersResponseDto) {
                UsersDetails userList = new UsersDetails(
                        u.getId(),
                        u.getUsername(),
                        u.getSurname(),
                        u.getEmail(),
                        u.getCity(),
                        u.getPhoneNumber(),
                        u.getBirthDate(),
                        u.getCreatedTime(),
                        u.getUpdateTime(),
                        u.getCount(),
                        u.getRole());
                usersDetails.add(userList);
            }
            log.info("Users fetched successfully. Users size = {}", usersDetails.size());
            return usersDetails;

        }catch (Exception e) {
            log.error("Failed to fetch users for admin dashboard: {}", e.getMessage());
            throw new RuntimeException("Something went wrong " + e.getMessage());
        }
    }

    public String delete(int id) {
        log.info("Admin requested to delete user with ID: {}", id);

        try {
            String response = webclientImp.removeUser(id);
            log.info("User with ID {} removed successfully", id);
            return response;

        } catch (Exception e) {
            log.error("Failed to remove user with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Something went wrong");
        }
    }

    public UsersDetails userDetailsForAdminDashboard(int id) {
        try {
            UserResponseDTO u = webclientImp.userDetail(id);
            if(u==null){
                throw new RuntimeException("User not found with id: "+id);
            }
            return new UsersDetails(

                    u.getId(),
                    u.getUsername(),
                    u.getSurname(),
                    u.getEmail(),
                    u.getCity(),
                    u.getPhoneNumber(),
                    u.getBirthDate(),
                    u.getCreatedTime(),
                    u.getUpdateTime(),
                    u.getCount(),
                    u.getRole());

        }catch (Exception e){
            throw new CustomException(e.getMessage());

        }
    }
}
