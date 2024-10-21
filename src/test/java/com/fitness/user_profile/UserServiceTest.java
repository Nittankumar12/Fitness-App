package com.fitness.user_profile;

import com.fitness.user_profile.entity.FitnessGoals;
import com.fitness.user_profile.entity.UserProfile;
import com.fitness.user_profile.exception.GenericException;
import com.fitness.user_profile.exception.UserNotFoundException;
import com.fitness.user_profile.model.UserModel;
import com.fitness.user_profile.repo.FitnessGoalsRepository;
import com.fitness.user_profile.repo.UserProfileRepository;
import com.fitness.user_profile.service.UserService;
import com.fitness.user_profile.util.Conversions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private FitnessGoalsRepository fitnessGoalsRepository;

    @Mock
    private Conversions conversions;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UserModel userModel = new UserModel();
        userModel.setId("1");
        userModel.setName("Nittan");
        userModel.setEmail("nittan@gmail.com");
        userModel.setAge(21);
        userModel.setFitnessGoals(new ArrayList<>()); // No goals initially
        userModel.getFitnessGoals().add("running");

        UserProfile savedUserProfile = new UserProfile();
        savedUserProfile.setId("1");
        savedUserProfile.setName("Nittan");
        savedUserProfile.setEmail("nittan@gmail.com");
        savedUserProfile.setAge(21);
        FitnessGoals fitnessGoal = new FitnessGoals();
        fitnessGoal.setId("11");
        fitnessGoal.setUserId("1");
        fitnessGoal.setGoal("running");
        List<FitnessGoals> fitnessGoals = new ArrayList<>();
        fitnessGoals.add(fitnessGoal);

        savedUserProfile.setFitnessGoals(fitnessGoals);

        when(conversions.modelToEntity(userModel)).thenReturn(savedUserProfile); // Convert model to entity
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(savedUserProfile); // Saving the entity returns the same entity
        when(fitnessGoalsRepository.getFitnessGoalsByUserId(savedUserProfile.getId())).thenReturn(fitnessGoals); // Mock empty goals list
        when(conversions.entityToModel(savedUserProfile, fitnessGoals)).thenReturn(userModel); // Convert back from entity to model

        // Calling the service method
        UserModel createdUserProfile = userService.createUser(userModel);

        assertNotNull(createdUserProfile);
        assertEquals("1", createdUserProfile.getId());
        assertEquals("Nittan", createdUserProfile.getName());
        assertEquals("nittan@gmail.com", createdUserProfile.getEmail());
        assertEquals(21, createdUserProfile.getAge());// Verify that no goals were created

        verify(fitnessGoalsRepository, times(1)).save(any(FitnessGoals.class));
    }

    @Test
    void testCreateUserGenericException() {
        UserModel userModel = new UserModel();
        when(conversions.modelToEntity(userModel)).thenThrow(new GenericException("Conversion failed"));

        GenericException thrown = assertThrows(GenericException.class, () -> userService.createUser(userModel));
        assertEquals("An error occurred while creating the user profile", thrown.getMessage());
    }

    @Test
    void testCreateUserUnexpectedException() {
        UserModel userModel = new UserModel();
        when(conversions.modelToEntity(userModel)).thenThrow(new RuntimeException("Unexpected error"));

        GenericException thrown = assertThrows(GenericException.class, () -> userService.createUser(userModel));
        assertEquals("An unexpected error occurred while creating the user profile", thrown.getMessage());
    }

    @Test
    void testGetUserFound() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId("1");
        userProfile.setName("Nittan");

        when(userProfileRepository.findById("1")).thenReturn(Optional.of(userProfile));
        when(conversions.entityToModel(userProfile, userProfile.getFitnessGoals())).thenReturn(new UserModel());

        UserModel foundUserProfile = userService.getUser("1");

        assertNotNull(foundUserProfile);
        assertEquals("1", userProfile.getId());
        assertEquals("Nittan", userProfile.getName());
    }

    @Test
    void testGetUserNotFound() {
        when(userProfileRepository.findById("1")).thenThrow(new UserNotFoundException("User not found"));

        GenericException thrown = assertThrows(GenericException.class, () -> userService.getUser("1"));
        assertEquals("An unexpected error occurred while fetching the user profile", thrown.getMessage());
    }

    @Test
    void testGetUserGenericException() {
        when(userProfileRepository.findById("1")).thenThrow(new GenericException("Database error"));

        GenericException thrown = assertThrows(GenericException.class, () -> userService.getUser("1"));
        assertEquals("An error occurred while fetching the user profile", thrown.getMessage());
    }

    @Test
    void testUpdateUserFound() {
        UserProfile existingUserProfile = new UserProfile();
        existingUserProfile.setId("1");
        existingUserProfile.setName("Nittan");
        existingUserProfile.setEmail("nittan@gmail.com");
        List<FitnessGoals> fitnessGoals = new ArrayList<>();
        FitnessGoals fitnessGoals1 = new FitnessGoals();
        fitnessGoals1.setGoal("walk");
        fitnessGoals1.setUserId("1");
        fitnessGoals1.setId("11");
        existingUserProfile.setFitnessGoals(fitnessGoals);

        UserModel updatedUserModel = new UserModel();
        updatedUserModel.setName("Nittan Updated");
        updatedUserModel.setEmail("nittan.updated@gmail.com");
        List<String> userModelGoals = new ArrayList<>();
        userModelGoals.add("walk");
        updatedUserModel.setFitnessGoals(userModelGoals);

        when(userProfileRepository.findById("1")).thenReturn(Optional.of(existingUserProfile));
        when(conversions.entityToModel(any(UserProfile.class), anyList())).thenReturn(updatedUserModel);
        when(fitnessGoalsRepository.getFitnessGoalsByUserId("1")).thenReturn(fitnessGoals);
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(existingUserProfile);

        userService.updateUser("1", updatedUserModel);
        assertEquals("Nittan Updated", updatedUserModel.getName());
    }

    @Test
    void testUpdateUserNotFound() {
        UserModel updatedUserModel = new UserModel();
        updatedUserModel.setName("Nittan Updated");

        when(userProfileRepository.findById("1")).thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> userService.updateUser("1", updatedUserModel));
        assertEquals("User not found for id: 1", thrown.getMessage());
    }

    @Test
    void testDeleteUserFound() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId("1");

        when(userProfileRepository.findById("1")).thenReturn(Optional.of(userProfile));

        String deletedUserId = userService.deleteUser("1");

        assertEquals("1", deletedUserId);
        verify(userProfileRepository).delete(userProfile);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userProfileRepository.findById("1")).thenReturn(Optional.empty());

        GenericException thrown = assertThrows(GenericException.class, () -> userService.deleteUser("1"));
        assertEquals("An unexpected error occurred while deleting the user profile with id: 1", thrown.getMessage());
    }

    @Test
    void testAddGoalUserFound() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId("1");
        userProfile.setFitnessGoals(new ArrayList<>());

        when(userProfileRepository.findById("1")).thenReturn(Optional.of(userProfile));
        when(fitnessGoalsRepository.save(any(FitnessGoals.class))).thenAnswer(invocation -> {
            FitnessGoals goal = invocation.getArgument(0);
            goal.setId("goal1"); // Simulating a saved goal ID
            return goal;
        });

        String result = userService.addGoal("1", "Running");

        assertEquals("Goal Added", result);
        assertEquals(1, userProfile.getFitnessGoals().size());
        assertEquals("Running", userProfile.getFitnessGoals().get(0).getGoal());
    }

    @Test
    void testAddGoalUserNotFound() {
        when(userProfileRepository.findById("1")).thenReturn(Optional.empty());

        GenericException thrown = assertThrows(GenericException.class, () -> userService.addGoal("1", "Running"));
        assertEquals("An unexpected error occurred while adding a goal to the user profile", thrown.getMessage());
    }

    @Test
    void testGetGoalsUserFoundWithGoals() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId("1");

        FitnessGoals goal1 = new FitnessGoals();
        goal1.setGoal("Running");
        FitnessGoals goal2 = new FitnessGoals();
        goal2.setGoal("Walking");
        userProfile.setFitnessGoals(List.of(goal1, goal2));

        when(userProfileRepository.findById("1")).thenReturn(Optional.of(userProfile));

        List<FitnessGoals> goals = userService.getGoals("1");

        assertNotNull(goals);
        assertEquals(2, goals.size());
        assertEquals("Running", goals.get(0).getGoal());
        assertEquals("Walking", goals.get(1).getGoal());
    }

    @Test
    void testGetGoalsUserFoundNoGoals() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId("1");
        userProfile.setFitnessGoals(new ArrayList<>());

        when(userProfileRepository.findById("1")).thenReturn(Optional.of(userProfile));

        GenericException thrown = assertThrows(GenericException.class, () -> userService.getGoals("1"));
        assertEquals("An unexpected error occurred while fetching goals for the user profile", thrown.getMessage());
    }

    @Test
    void testGetGoalsUserNotFound() {
        when(userProfileRepository.findById("1")).thenReturn(Optional.empty());

        GenericException thrown = assertThrows(GenericException.class, () -> userService.getGoals("1"));
        assertEquals("An unexpected error occurred while fetching goals for the user profile", thrown.getMessage());
    }

    @Test
    void testGetGoalsGenericException() {
        when(userProfileRepository.findById("1")).thenThrow(new GenericException("Database error"));

        GenericException thrown = assertThrows(GenericException.class, () -> userService.getGoals("1"));
        assertEquals("An error occurred while fetching goals for the user profile", thrown.getMessage());
    }

    @Test
    void testAddGoalGenericException() {
        when(userProfileRepository.findById("1")).thenThrow(new GenericException("Database error"));

        GenericException thrown = assertThrows(GenericException.class, () -> userService.addGoal("1", "Running"));
        assertEquals("An error occurred while adding a goal to the user profile", thrown.getMessage());
    }

    @Test
    void testDeleteUserGenericException() {
        when(userProfileRepository.findById("1")).thenThrow(new GenericException("Database error"));

        GenericException thrown = assertThrows(GenericException.class, () -> userService.deleteUser("1"));
        assertEquals("An error occurred while deleting the user profile with id: 1", thrown.getMessage());
    }

    @Test
    void testUpdateUserGenericException(){
        when(userProfileRepository.findById("1")).thenThrow(new GenericException("Database error"));
        UserModel userModel = new UserModel();
        userModel.setId("1");
        GenericException thrown = assertThrows(GenericException.class, () -> userService.updateUser("1",userModel));
        assertEquals("An error occurred while updating the user profile", thrown.getMessage());
    }
}