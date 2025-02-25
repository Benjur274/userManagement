package com.testcompany.usermanagement.service;

import com.testcompany.usermanagement.exceptions.UserAlreadyExistsException;
import com.testcompany.usermanagement.mapper.UserMapper;
import com.testcompany.usermanagement.model.dto.UpdateUserDto;
import com.testcompany.usermanagement.model.dto.UserDto;
import com.testcompany.usermanagement.model.entity.User;
import com.testcompany.usermanagement.exceptions.UserNotFoundException;
import com.testcompany.usermanagement.model.dto.CreateUserDto;
import com.testcompany.usermanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        return userMapper.toUserResponseDTO(user);
    }

    @Transactional
    public UserDto createUser(CreateUserDto userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with username " + userDTO.getUsername() + " already exists");
        }
        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    @Transactional
    public UserDto updateUser(Long id, UpdateUserDto updateUserDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        userMapper.updateUserFromDto(updateUserDTO, existingUser);
        if (StringUtils.hasText(updateUserDTO.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }

        return userMapper.toUserResponseDTO(userRepository.save(existingUser));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }
}
