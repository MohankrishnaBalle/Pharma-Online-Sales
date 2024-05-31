package service;
import entity.User;
import exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        validateUserLoggedIn(user.getId());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        // Assuming you have a way to get the logged-in user's ID
        Long loggedInUserId = getLoggedInUserId();
        validateUserLoggedIn(loggedInUserId);
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        validateUserLoggedIn(id);
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User must have an ID to be updated.");
        }
        validateUserLoggedIn(getLoggedInUserId());
        return userRepository.save(user);
    }


    public void deleteUser(Long id) {
        validateUserLoggedIn(id);
        userRepository.deleteById(id);
    }

    private User validateUserLoggedIn(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with ID " + id + " is not logged in.");
        }
        return user.get();
    }

    private Long getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("No user is currently logged in.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<User> userOptional = userRepository.findByEmail(username);
            if (userOptional.isPresent()) {
                return userOptional.get().getId();
            } else {
                throw new UserNotFoundException("Logged in user not found in the database.");
            }
        } else {
            throw new UserNotFoundException("User authentication principal is not recognized.");
        }
    }

}
