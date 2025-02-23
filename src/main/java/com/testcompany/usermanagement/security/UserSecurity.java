package com.testcompany.usermanagement.security;

import com.testcompany.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSecurity {

    private final UserRepository userRepository;

    public boolean hasAccess(Long id, String username) {
        return userRepository.findById(id)
                .map(user -> user.getUsername().equals(username))
                .orElse(false);
    }
}
