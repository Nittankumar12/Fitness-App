package com.fitness.user_profile.service;

import com.fitness.user_profile.model.UserProfile;
import com.fitness.user_profile.repo.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * Service class for managing user profiles.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserProfileRepository repository;

    /**
     * Creates a new user profile.
     *
     * @param userProfile The user profile to create.
     * @return The created UserProfile.
     */
    public UserProfile createUser(UserProfile userProfile) {
        logger.info("Creating user profile: {}", userProfile);
        UserProfile createdUserProfile = repository.save(userProfile);
        logger.info("Created user profile: {}", createdUserProfile);
        return createdUserProfile;
    }

    /**
     * Retrieves a user profile by ID.
     *
     * @param id The ID of the user profile to retrieve.
     * @return An Optional containing the UserProfile if found, or empty if not found.
     */
    public Optional<UserProfile> getUser(String id) {
        logger.info("Retrieving user profile with id: {}", id);
        Optional<UserProfile> userProfile = repository.findById(id);
        if (userProfile.isPresent()) {
            logger.info("User profile found: {}", userProfile.get());
        } else {
            logger.warn("User profile not found for id: {}", id);
        }
        return userProfile;
    }

    /**
     * Retrieves all user profiles.
     *
     * @return A List of all UserProfiles.
     */
    public List<UserProfile> getAllUsers() {
        logger.info("Retrieving all user profiles");
        Iterable<UserProfile> userProfiles = repository.findAll();
        List<UserProfile> userProfileList = convertIterableToList(userProfiles);
        logger.info("Retrieved {} user profiles", userProfileList.size());
        return userProfileList;
    }

    /**
     * Deletes a user profile by ID.
     *
     * @param id The ID of the user profile to delete.
     */
    public void deleteUser(String id) {
        logger.info("Deleting user profile with id: {}", id);
        repository.deleteById(id);
        logger.info("Deleted user profile with id: {}", id);
    }

    /**
     * Adds a new goal to a user's profile.
     *
     * @param id The ID of the user profile.
     * @param newGoal The new goal to add.
     * @return A message indicating the result.
     */
    public String addGoal(String id, String newGoal) {
        logger.info("Adding goal to user profile with id: {}", id);
        Optional<UserProfile> userProfileOptional = repository.findById(id);
        if (userProfileOptional.isPresent()) {
            UserProfile userProfile = userProfileOptional.get();
            if (userProfile.getFitnessGoals() == null) {
                userProfile.setFitnessGoals(new ArrayList<>());
            }
            userProfile.getFitnessGoals().add(newGoal);
            repository.save(userProfile);
            logger.info("Added goal to user profile: {}", userProfile);
            return "Goal Added";
        } else {
            logger.warn("User  not found for id: {}", id);
            return "User profile not found";
        }
    }

    /**
     * Retrieves all goals for a user profile by ID.
     *
     * @param id The ID of the user profile.
     * @return A list of goals.
     */
    public List<String> getGoals(String id) {
        logger.info("Retrieving goals for user profile with id: {}", id);
        Optional<UserProfile> userProfileOptional = repository.findById(id);
        if (userProfileOptional.isPresent()) {
            List<String> goals = userProfileOptional.get().getFitnessGoals();
            logger.info("Retrieved goals for user profile: {}", goals);
            return goals;
        } else {
            logger.warn("profile not found for id: {}", id);
            return new ArrayList<>();
        }
    }

    /**
     * Loads initial user data into the repository.
     */
    public void loadData() {
        logger.info("Loading initial user data");
        UserProfile userProfile1 = new UserProfile("1", "Nitan", "xyz@gmail.com", 21, "M", new ArrayList<>());
        UserProfile userProfile2 = new UserProfile("2", "Abc", "aman@gmail.com", 20, "F", new ArrayList<>());
        UserProfile userProfile3 = new UserProfile("3", "Nakul", "nakul@gmail.com", 21, "M", new ArrayList<>());
        repository.save(userProfile1);
        repository.save(userProfile2);
        repository.save(userProfile3);
        logger.info("Loaded initial user data");
    }

    /**
     * Converts an Iterable to a List.
     *
     * @param <T> The type of elements in the iterable.
     * @param iterable The iterable to convert.
     * @return A List containing the elements of the iterable.
     */
    private <T> List<T> convertIterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .toList();
    }
}
