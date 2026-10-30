package kz.KEZ.FinanceTracker.service;

import jakarta.transaction.Transactional;
import kz.KEZ.FinanceTracker.models.User;
import kz.KEZ.FinanceTracker.repositories.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Data
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new NoSuchElementException("пользователь с таким электронным адресом: " + user.getEmail() + " уже существует");
        }

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userRepository.save(user);
    }
}
