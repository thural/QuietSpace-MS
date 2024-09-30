package com.jellybrains.quietspace.user_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jellybrains.quietspace.common_service.exception.UserNotFoundException;
import com.jellybrains.quietspace.common_service.model.response.UserResponse;
import com.jellybrains.quietspace.user_service.controller.UserController;
import com.jellybrains.quietspace.user_service.entity.Profile;
import com.jellybrains.quietspace.user_service.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerIT {

    @Autowired
    UserController userController;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testListUsers() throws Exception {
        mockMvc.perform(get(UserController.USER_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(3)))
                .andExpect(jsonPath("$.size()", is(11)));
    }

    @WithUserDetails("tural@email.com")
    @Test
    void testUpdateUserNameTooLong() throws Exception {

        Profile user = profileRepository.findByEmail("tural@email.com")
                .orElseThrow();

        UserResponse userResponse = userMapper.userEntityToResponse(user);

        userResponse.setUsername("a long user name to cause transaction exception");

        mockMvc.perform(patch(UserController.USER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userResponse)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(3))).andReturn();
    }

    @Test
    void testListUserByNamePage1() throws Exception {
        mockMvc.perform(get(UserController.USER_PATH)
                        .queryParam("username", "john")
                        .queryParam("page-number", "1")
                        .queryParam("page-size", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(11)))
                .andExpect(jsonPath("$.content.[0].username", is("john")));
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteAllUsers() {
        profileRepository.deleteAll();
        Page<UserResponse> userList = userController.listUsersBySearchTerm("", 1, 25);
        assertThat(userList.getContent().size()).isEqualTo(0);
    }

    @Test
    void testGetById() {
        Profile profile = profileRepository.findByUserId(userid).get(0);
        ResponseEntity<?> response = userController.getUserById(profile.getId());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userController.getUserById(String.valueOf(UUID.randomUUID())));
    }

    @WithUserDetails("tural@email.com")
    @Rollback
    @Transactional
    @Test
    void testUpdateExistingUser() {
        User user = profileRepository.findUserEntityByEmail("tural@email.com").orElseThrow();
        UserRegisterRequest userRegisterRequest = userMapper.userEntityToRequest(user);
        final String updatedName = "updatedName";
        userRegisterRequest.setUsername(updatedName);

        ResponseEntity<?> response = userController.patchUser(userRegisterRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        User updatedUser = profileRepository.findById(user.getId()).orElse(null);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo(updatedName);
    }

    @WithUserDetails("tural@email.com")
    @Test
    void testUpdateNotFound() {
        assertThrows(UserNotFoundException.class, () -> userController.patchUser(UserRegisterRequest.builder().build()));
    }

    @WithUserDetails("tural@email.com")
    @Rollback
    @Transactional
    @Test
    void testDeleteUser() {
        User user = profileRepository.findAll().get(0);

        ResponseEntity<?> response = userController.deleteUser(user.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(profileRepository.findById(user.getId())).isEmpty();
    }

}