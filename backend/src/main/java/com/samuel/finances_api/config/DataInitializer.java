package com.samuel.finances_api.config;

import com.samuel.finances_api.entity.User;
import com.samuel.finances_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final String DEMO_EMAIL = "email@demouseremail.com";

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Executing startup tasks...");

        if(userRepository.findByEmail(DEMO_EMAIL).isPresent()) {
            System.out.println("Demo user already exists.");
            return;
        }

        User demoUser = new User();
        demoUser.setName("demo user");
        demoUser.setEmail(DEMO_EMAIL);
        demoUser.setPassword("demo");

        userRepository.save(demoUser);

        System.out.println("Demo user created successfully.");

    }



}
