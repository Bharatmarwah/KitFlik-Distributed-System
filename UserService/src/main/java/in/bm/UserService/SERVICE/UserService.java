package in.bm.UserService.SERVICE;


import in.bm.UserService.ENTITY.userRegister;
import in.bm.UserService.EXCEPTION.CustomException;
import in.bm.UserService.REPOSITORY.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepo repo;


    public userRegister saveUser(userRegister register) {
        try {
            return repo.save(register);
        } catch (Exception e) {
            log.error("Error saving user: {}", e.getMessage());
            throw new RuntimeException("Something went wrong");
        }
    }

    public List<userRegister> fetchUserforAdmin() {
        try {
            List<userRegister> UsersList = repo.findAll();
            return UsersList;
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong in finding users " + e.getMessage());
        }
    }

    public String removeByID(int id) {
        try {
            if (!repo.existsById(id)) {
                return "User not found with id: " + id;
            }
            repo.deleteById(id);
            return "Successfully Deleted";
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong in deleting user " + e.getMessage());
        }
    }

    public List<String> fetchEmails() {
        List<String> emailsIds = repo.findByEmails();
        return emailsIds;
    }

    public String EmailByUsername(String username) {
        return repo.findEmailByUsername(username);
    }


    public userRegister userById(int id) {

        Optional<userRegister> user = repo.findById(id);

        if (user.isEmpty()) {
            throw new CustomException("User not found with id: " + id);
        }

        return user.get();
    }

}
