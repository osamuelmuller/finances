package com.samuel.finances_api.service;

import com.samuel.finances_api.entity.User;
import com.samuel.finances_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        return userRepository.findByEmail("email@demouseremail.com")
                .orElseThrow(() -> new RuntimeException("User not found."));
    }
}
