package com.fitness.user_profile;

import com.fitness.user_profile.controller.UserController;
import com.fitness.user_profile.entity.FitnessGoals;
import com.fitness.user_profile.model.UserModel;
import com.fitness.user_profile.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testCreateUserProfile() throws Exception {
        UserModel mockUserProfile = new UserModel();
        mockUserProfile.setId("1");
        mockUserProfile.setName("Nittan");
        mockUserProfile.setEmail("nittan@gmail.com");
        mockUserProfile.setAge(21);
        mockUserProfile.setFitnessGoals(new ArrayList<>());
        mockUserProfile.getFitnessGoals().add("running");

        when(userService.createUser(any(UserModel.class))).thenReturn(mockUserProfile);

        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Nittan\",\"email\":\"nittan@gmail.com\",\"age\": 21,\"fitnessGoals\": [\"running\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("User profile created successfully"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.name").value("Nittan"))
                .andExpect(jsonPath("$.data.email").value("nittan@gmail.com"))
                .andExpect(jsonPath("$.data.fitnessGoals[0]").value("running"))
                .andExpect(jsonPath("$.data.age").value(21));
    }

    @Test
    void testGetUserProfile() throws Exception {
        UserModel mockUserProfile = new UserModel();
        mockUserProfile.setId("1");
        mockUserProfile.setName("Nittan");
        mockUserProfile.setEmail("nittan@gmail.com");
        mockUserProfile.setAge(21);

        when(userService.getUser("1")).thenReturn(mockUserProfile);

        mockMvc.perform(get("/api/v1/users/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("User profile retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.name").value("Nittan"))
                .andExpect(jsonPath("$.data.email").value("nittan@gmail.com"))
                .andExpect(jsonPath("$.data.age").value(21));
    }

    @Test
    void testUpdateUserProfile() throws Exception {
        UserModel mockUserProfile = new UserModel();
        mockUserProfile.setId("1");
        mockUserProfile.setName("Nittan");
        mockUserProfile.setEmail("nittan@gmail.com");
        mockUserProfile.setAge(22); // Updated age

        UserModel updatedUser = new UserModel();
        when(userService.updateUser("1", mockUserProfile)).thenReturn(updatedUser);

        mockMvc.perform(put("/api/v1/users/user/{id}","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Nittan\", \"email\": \"nittan@gmail.com\", \"age\": 22}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("User profile updated successfully"));
    }

    @Test
    void testDeleteUserProfile() throws Exception {
        mockMvc.perform(delete("/api/v1/users/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("User profile deleted successfully"));
    }

    @Test
    void testAddGoal() throws Exception {
        UserModel mockUserProfile = new UserModel();
        mockUserProfile.setId("1");
        mockUserProfile.setName("Nittan");
        mockUserProfile.setEmail("nittan@gmail.com");
        mockUserProfile.setAge(21);
        mockUserProfile.setFitnessGoals(List.of("running"));
        String message = "Goal added successfully";

        when(userService.addGoal("1", "Running")).thenReturn(message);

        mockMvc.perform(post("/api/v1/users/user/1/goal")
                        .param("newGoal","running"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Goal added successfully"));
    }

    @Test
    void testGetGoals() throws Exception {
        List<FitnessGoals> fitnessGoals = new ArrayList<>();

        FitnessGoals fitnessGoals1 = new FitnessGoals();
        fitnessGoals1.setGoal("running");
        fitnessGoals1.setUserId("1");
        fitnessGoals1.setId("11");

        FitnessGoals fitnessGoals2 = new FitnessGoals();
        fitnessGoals2.setGoal("walking");
        fitnessGoals2.setUserId("1");
        fitnessGoals2.setId("12");

        fitnessGoals.add(fitnessGoals1);
        fitnessGoals.add(fitnessGoals2);

        when(userService.getGoals("1")).thenReturn(fitnessGoals);

        mockMvc.perform(get("/api/v1/users/user/1/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Goals retrieved successfully"))
                .andExpect(jsonPath("$.data[0].id").value("11"))
                .andExpect(jsonPath("$.data[0].goal").value("running"))
                .andExpect(jsonPath("$.data[0].userId").value("1"))
                .andExpect(jsonPath("$.data[1].id").value("12"))
                .andExpect(jsonPath("$.data[1].goal").value("walking"))
                .andExpect(jsonPath("$.data[1].userId").value("1"));
    }
}