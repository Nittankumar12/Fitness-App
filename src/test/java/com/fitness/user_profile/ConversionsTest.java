package com.fitness.user_profile;

import com.fitness.user_profile.entity.FitnessGoals;
import com.fitness.user_profile.entity.UserProfile;
import com.fitness.user_profile.model.UserModel;
import com.fitness.user_profile.util.Conversions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConversionsTest {

    private Conversions conversions;

    @BeforeEach
    void setUp() {
        conversions = new Conversions();
    }

    @Test
    void testConvertIterableToList() {
        List<String> stringList = conversions.convertIterableToList(Arrays.asList("Nittan", "Shivam", "Vivek"));
        assertEquals(3, stringList.size());
        assertTrue(stringList.contains("Nittan"));
        assertTrue(stringList.contains("Shivam"));
        assertTrue(stringList.contains("Vivek"));
    }

    @Test
    void testEntityToModelWithGoals() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId("1");
        userProfile.setName("Nittan");
        userProfile.setAge(21);
        userProfile.setEmail("nittan@gmail.com");
        userProfile.setGender("Male");

        FitnessGoals goal1 = new FitnessGoals();
        goal1.setGoal("Running");

        FitnessGoals goal2 = new FitnessGoals();
        goal2.setGoal("Walking");

        Iterable<FitnessGoals> goals = Arrays.asList(goal1, goal2);

        UserModel userModel = conversions.entityToModel(userProfile, goals);

        assertNotNull(userModel);
        assertEquals("1", userModel.getId());
        assertEquals("Nittan", userModel.getName());
        assertEquals(21, userModel.getAge());
        assertEquals("nittan@gmail.com", userModel.getEmail());
        assertEquals("Male", userModel.getGender());
        assertEquals(2, userModel.getFitnessGoals().size());
        assertTrue(userModel.getFitnessGoals().contains("Running"));
        assertTrue(userModel.getFitnessGoals().contains("Walking"));
    }

    @Test
    void testEntityToModelNoGoals() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId("2");
        userProfile.setName("Shivam");
        userProfile.setAge(30);
        userProfile.setEmail("shivam@gmail.com");
        userProfile.setGender("Male");

        Iterable<FitnessGoals> goals = List.of(); // No goals

        UserModel userModel = conversions.entityToModel(userProfile, goals);

        assertNotNull(userModel);
        assertEquals("2", userModel.getId());
        assertEquals("Shivam", userModel.getName());
        assertEquals(30, userModel.getAge());
        assertEquals("shivam@gmail.com", userModel.getEmail());
        assertEquals("Male", userModel.getGender());
        assertTrue(userModel.getFitnessGoals().isEmpty());
    }

    @Test
    void testModelToEntity() {
        UserModel userModel = new UserModel();
        userModel.setId("3");
        userModel.setName("Vivek");
        userModel.setAge(25);
        userModel.setEmail("vivek@gmail.com");
        userModel.setGender("male");
        userModel.setFitnessGoals(List.of("Cycling"));

        UserProfile userProfile = conversions.modelToEntity(userModel);

        assertNotNull(userProfile);
        assertEquals("3", userProfile.getId());
        assertEquals("Vivek", userProfile.getName());
        assertEquals(25, userProfile.getAge());
        assertEquals("vivek@gmail.com", userProfile.getEmail());
        assertEquals("male", userProfile.getGender());
        assertTrue(userProfile.getFitnessGoals().isEmpty());
    }
}