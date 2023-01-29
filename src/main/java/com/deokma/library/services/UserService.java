package com.deokma.library.services;

import com.deokma.library.models.User;
import com.deokma.library.models.enums.Role;
import com.deokma.library.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

/**
 * @author Denis Popolamov
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean createUser(User user) {
        String name = user.getUsername();
        if (userRepository.findByUsername(name) != null) return false;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.getRoles().add(Role.USER);
        user.setAvatar("https://vsegda-pomnim.com/uploads/posts/2022-04/164923" +
                "2783_41-vsegda-pomnim-com-p-pustoe-litso-foto-52.png");
        log.info("Saving new User with username: " + user.getUsername());
        userRepository.save(user);
        return true;
    }

    public List<User> list() {
        return (List<User>) userRepository.findAll();
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setActive(false);
            log.info("Ban user with id = {}; name: {}", user.getId(), user.getUsername());
        }
        userRepository.save(user);
    }

    public void unbanUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setActive(true);
            log.info("Unban user with id = {}; name: {}", user.getId(), user.getUsername());
        }
        userRepository.save(user);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByUsername(principal.getName());
    }
}
