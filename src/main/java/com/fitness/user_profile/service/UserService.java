package com.fitness.user_profile.service;

import com.fitness.user_profile.exception.FitnessGoalsNotFoundException;
import com.fitness.user_profile.exception.GenericException;
import com.fitness.user_profile.exception.UserNotFoundException;
import com.fitness.user_profile.model.FitnessGoals;
import com.fitness.user_profile.model.UserProfile;
import com.fitness.user_profile.repo.FitnessGoalsRepository;
import com.fitness.user_profile.repo.UserProfileRepository;
import com.fitness.user_profile.util.LogMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing user profiles.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserProfileRepository repository;

    @Autowired
    private FitnessGoalsRepository fitnessGoalsRepository;

    /**
     * Creates a new user profile.
     *
     * @param userProfile The user profile to create.
     * @return The created UserProfile.
     */
    public UserProfile createUser(UserProfile userProfile) {
        try {
            UserProfile newUserProfile = new UserProfile();

            newUserProfile.setName(userProfile.getName());
            newUserProfile.setEmail(userProfile.getEmail());
            newUserProfile.setAge(userProfile.getAge());
            newUserProfile.setGender(userProfile.getGender());
            newUserProfile = repository.save(newUserProfile);
            System.out.println(newUserProfile);
            logger.info(LogMessages.USER_PROFILE_CREATED,newUserProfile.getId());
            logger.info(LogMessages.SETTING_GOAL);

            for(FitnessGoals goal: userProfile.getFitnessGoals()){
                FitnessGoals fitnessGoal = new FitnessGoals();
                fitnessGoal.setGoal(goal.getGoal());
                fitnessGoal.setUserId(newUserProfile.getId());
                fitnessGoalsRepository.save(fitnessGoal);
            }

            newUserProfile.setFitnessGoals(fitnessGoalsRepository.getFitnessGoalsByUserId(newUserProfile.getId()));

            UserProfile createdUserProfile = repository.save(newUserProfile);
            logger.info(LogMessages.USER_PROFILE_CREATED, createdUserProfile.getId());
            return createdUserProfile;
        } catch (GenericException e) {
            logger.error(LogMessages.GENERIC_EXCEPTION, e.getMessage());
            throw new GenericException("An error occurred while creating the user profile");
        } catch (Exception e) {
            logger.error(LogMessages.UNEXPECTED_EXCEPTION, e);
            throw new GenericException("An unexpected error occurred while creating the user profile");
        }
    }

    /**
     * Retrieves a user profile by ID.
     *
     * @param id The ID of the user profile to retrieve.
     * @return An Optional containing the UserProfile if found, or empty if not found.
     */
    public UserProfile getUser(String id) {
        try {
            Optional<UserProfile> userProfile = repository.findById(id);
            if (userProfile.isPresent()) {
                logger.info(LogMessages.FOUND_USER_PROFILE, userProfile.get().getId());
                return userProfile.get();
            } else {
                logger.warn(LogMessages.USER_NOT_FOUND_EXCEPTION, id);
                throw new UserNotFoundException("User not found for id: " + id);
            }
        } catch (GenericException e) {
            logger.error(LogMessages.GENERIC_EXCEPTION, e.getMessage());
            throw new GenericException("An error occurred while fetching the user profile");
        } catch (Exception e) {
            logger.error(LogMessages.UNEXPECTED_EXCEPTION, e);
            throw new GenericException("An unexpected error occurred while fetching the user profile");
        }
    }

    /**
     * Updates an existing user profile.
     *
     * @param id          The ID of the user profile to update.
     * @param userProfile The updated user profile data.
     * @return The updated UserProfile.
     **/
    public UserProfile updateUser(String id, UserProfile userProfile) {
        try {
            Optional<UserProfile> existingUserProfile = repository.findById(id);
            if (existingUserProfile.isPresent()) {
                logger.info(LogMessages.FOUND_USER_PROFILE,id);
                UserProfile updatedUserProfile = existingUserProfile.get();
                updatedUserProfile.setName(userProfile.getName());
                updatedUserProfile.setEmail(userProfile.getEmail());

                UserProfile savedUserProfile = repository.save(updatedUserProfile);
                logger.info(LogMessages.USER_PROFILE_UPDATED, savedUserProfile.getId());
                return savedUserProfile;
            } else {
                logger.warn(LogMessages.USER_NOT_FOUND_EXCEPTION, id);
                throw new UserNotFoundException("User not found for id: " + id);
            }
        } catch (GenericException e) {
            logger.error(LogMessages.GENERIC_EXCEPTION, e.getMessage());
            throw new GenericException("An error occurred while updating the user profile");
        } catch (Exception e) {
            logger.error(LogMessages.UNEXPECTED_EXCEPTION, e);
            throw new GenericException("An unexpected error occurred while updating the user profile");
        }
    }

    /**
     * Deletes a user profile by ID.
     *
     * @param id The ID of the user profile to delete.
     */
    public String deleteUser(String id) {
        try {
            Optional<UserProfile> userProfile = repository.findById(id);
            if (!userProfile.isPresent()) {
                logger.warn(LogMessages.USER_NOT_FOUND_EXCEPTION, id);
                throw new UserNotFoundException("User profile not found for id " + id);
            }
            repository.delete(userProfile.get());
            logger.info(LogMessages.USER_PROFILE_DELETED, id);
            return userProfile.get().getId();
        } catch (GenericException e) {
            logger.error(LogMessages.GENERIC_EXCEPTION, e.getMessage());
            throw new GenericException("An error occurred while deleting the user profile with id: " + id);
        } catch (Exception e) {
            logger.error(LogMessages.UNEXPECTED_EXCEPTION, e);
            throw new GenericException("An unexpected error occurred while deleting the user profile with id: " + id);
        }
    }

   /**
     * Adds a new goal to a user's profile.
     *
     * @param id      The ID of the user profile.
     * @param newGoal The new goal to add.
     * @return A message indicating the result.
     */
    public String addGoal(String id, String newGoal) {
        try {
            Optional<UserProfile> userProfileOptional = repository.findById(id);
            if (userProfileOptional.isPresent()) {
                UserProfile userProfile = userProfileOptional.get();
                logger.info(LogMessages.FOUND_USER_PROFILE, id);
                if (userProfile.getFitnessGoals() == null) {
                    userProfile.setFitnessGoals(new ArrayList<>());
                }

                // Create a new FitnessGoals object and add it to the user's fitnessGoals
                FitnessGoals fitnessGoal = new FitnessGoals();
                logger.info(LogMessages.SETTING_GOAL);
                fitnessGoal.setGoal(newGoal);
                fitnessGoal.setUserId(userProfile.getId());
                fitnessGoal = fitnessGoalsRepository.save(fitnessGoal);
                userProfile.getFitnessGoals().add(fitnessGoal);

                repository.save(userProfile);
                return "Goal Added";
            } else {
                logger.warn(LogMessages.USER_NOT_FOUND_EXCEPTION, id);
                throw new UserNotFoundException("User profile not found for id: " + id);
            }
        } catch (GenericException e) {
            logger.error(LogMessages.GENERIC_EXCEPTION, e.getMessage());
            throw new GenericException("An error occurred while adding a goal to the user profile");
        } catch (Exception e) {
            logger.error(LogMessages.UNEXPECTED_EXCEPTION, e);
            throw new GenericException("An unexpected error occurred while adding a goal to the user profile");
        }
    }

    /**
     * Retrieves all goals for a user profile by ID.
     *
     * @param id The ID of the user profile.
     * @return A list of goals.
     */
    public List<FitnessGoals> getGoals(String id) {
        try {
            Optional<UserProfile> userProfileOptional = repository.findById(id);
            if (userProfileOptional.isPresent()) {
                logger.info(LogMessages.FOUND_USER_PROFILE, id);
                List<FitnessGoals> goals = userProfileOptional.get().getFitnessGoals();
                if (goals == null || goals.isEmpty()) {
                    logger.warn(LogMessages.FITNESS_GOALS_NOT_FOUND_EXCEPTION, id);
                    throw new FitnessGoalsNotFoundException("No fitness goals found for user profile with id: " + id);
                }
                return goals;
            } else {
                logger.warn(LogMessages.USER_NOT_FOUND_EXCEPTION, id);
                throw new UserNotFoundException("User profile not found for id: " + id);
            }
        } catch (GenericException e) {
            logger.error(LogMessages.GENERIC_EXCEPTION, e.getMessage());
            throw new GenericException("An error occurred while fetching goals for the user profile");
        } catch (Exception e) {
            logger.error(LogMessages.UNEXPECTED_EXCEPTION, e);
            throw new GenericException("An unexpected error occurred while fetching goals for the user profile");
        }
    }
}
