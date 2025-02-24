package com.testcompany.usermanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testcompany.usermanagement.model.dto.CreateUserDto;
import com.testcompany.usermanagement.model.dto.UpdateUserDto;
import com.testcompany.usermanagement.model.entity.User;
import com.testcompany.usermanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName("Test User");
        createUserDto.setUsername("testuser");
        createUserDto.setPassword("password1");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test User")))
                .andExpect(jsonPath("$.username", is("testuser")));
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setName("Integration User");
        user.setUsername("integrationuser");
        user.setPassword("encodedPassword");
        user = userRepository.save(user);

        mockMvc.perform(get("/api/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Integration User")))
                .andExpect(jsonPath("$.username", is("integrationuser")));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setName("User One");
        user1.setUsername("userone");
        user1.setPassword("encodedPassword");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("User Two");
        user2.setUsername("usertwo");
        user2.setPassword("encodedPassword");
        userRepository.save(user2);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "updateuser")
    public void testUpdateUserSuccess() throws Exception {
        User user = new User();
        user.setName("Old Name");
        user.setUsername("updateuser");
        user.setPassword("encodedPassword");
        user = userRepository.save(user);

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setName("New Name");
        updateUserDto.setPassword("newPassword1");

        mockMvc.perform(patch("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Name")))
                .andExpect(jsonPath("$.username", is("updateuser")));
    }

    @Test
    @WithMockUser(username = "otheruser")
    public void testUpdateUserForbidden() throws Exception {
        User user = new User();
        user.setName("Old Name");
        user.setUsername("updateuser");
        user.setPassword("encodedPassword");
        user = userRepository.save(user);

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setName("New Name");
        updateUserDto.setPassword("newPassword1");

        mockMvc.perform(patch("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "deleteuser")
    public void testDeleteUser() throws Exception {
        User user = new User();
        user.setName("Delete User");
        user.setUsername("deleteuser");
        user.setPassword("encodedPassword");
        user = userRepository.save(user);

        mockMvc.perform(delete("/api/users/{id}", user.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/{id}", user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test")
    public void testDeleteUserForbidden() throws Exception {
        User user = new User();
        user.setName("Delete User");
        user.setUsername("deleteuser");
        user.setPassword("encodedPassword");
        user = userRepository.save(user);

        mockMvc.perform(delete("/api/users/{id}", user.getId()))
                .andExpect(status().isForbidden());
    }

}
