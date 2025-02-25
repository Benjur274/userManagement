package com.testcompany.usermanagement;

import com.testcompany.usermanagement.exceptions.UserNotFoundException;
import com.testcompany.usermanagement.mapper.UserMapper;
import com.testcompany.usermanagement.model.dto.CreateUserDto;
import com.testcompany.usermanagement.model.dto.UpdateUserDto;
import com.testcompany.usermanagement.model.dto.UserDto;
import com.testcompany.usermanagement.model.entity.User;
import com.testcompany.usermanagement.repository.UserRepository;
import com.testcompany.usermanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User One");
        user1.setUsername("userone");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("User Two");
        user2.setUsername("usertwo");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        UserDto dto1 = new UserDto();
        dto1.setId(1L);
        dto1.setName("User One");
        dto1.setUsername("userone");

        UserDto dto2 = new UserDto();
        dto2.setId(2L);
        dto2.setName("User Two");
        dto2.setUsername("usertwo");

        when(userMapper.toUserResponseDTO(user1)).thenReturn(dto1);
        when(userMapper.toUserResponseDTO(user2)).thenReturn(dto2);

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById_UserExists() {
        User user = new User();
        user.setId(1L);
        user.setName("User One");
        user.setUsername("userone");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("User One");
        userDto.setUsername("userone");

        when(userMapper.toUserResponseDTO(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("User One", result.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void testCreateUser() {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName("User Three");
        createUserDto.setUsername("userthree");
        createUserDto.setPassword("password123");

        User user = new User();
        user.setName("User Three");
        user.setUsername("userthree");
        user.setPassword("password123");

        when(userMapper.toUser(createUserDto)).thenReturn(user);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(3L);
        savedUser.setName("User Three");
        savedUser.setUsername("userthree");
        savedUser.setPassword("encodedPassword");

        when(userRepository.save(user)).thenReturn(savedUser);

        UserDto userDto = new UserDto();
        userDto.setId(3L);
        userDto.setName("User Three");
        userDto.setUsername("userthree");

        when(userMapper.toUserResponseDTO(savedUser)).thenReturn(userDto);

        UserDto result = userService.createUser(createUserDto);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Old Name");
        user.setUsername("testuser");
        user.setPassword("oldEncodedPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setName("New Name");
        updateUserDto.setPassword("newPassword123");

        doAnswer(invocation -> {
            UpdateUserDto dto = invocation.getArgument(0);
            User u = invocation.getArgument(1);
            u.setName(dto.getName());
            return null;
        }).when(userMapper).updateUserFromDto(updateUserDto, user);

        when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedPassword");

        user.setPassword("newEncodedPassword");

        User savedUser = new User();
        savedUser.setId(userId);
        savedUser.setName("New Name");
        savedUser.setUsername("testuser");
        savedUser.setPassword("newEncodedPassword");

        when(userRepository.save(user)).thenReturn(savedUser);

        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("New Name");
        userDto.setUsername("testuser");

        when(userMapper.toUserResponseDTO(savedUser)).thenReturn(userDto);

        UserDto result = userService.updateUser(userId, updateUserDto);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).updateUserFromDto(updateUserDto, user);
        verify(passwordEncoder, times(1)).encode("newPassword123");
    }

    @Test
    public void testUpdateUser_NotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setName("New Name");
        updateUserDto.setPassword("newPassword123");

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, updateUserDto));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testDeleteUser_Success() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_NotFound() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository, times(0)).deleteById(userId);
    }
}
