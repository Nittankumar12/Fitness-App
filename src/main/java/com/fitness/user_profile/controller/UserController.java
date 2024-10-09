package com.fitness.user_profile.controller;

import com.fitness.user_profile.model.UserProfile;
import com.fitness.user_profile.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for handling user profile-related operations.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * Endpoint to check if the service is working.
     *
     * @return A string indicating the service status.
     */
    @GetMapping("/demo")
    public String demo(){
        logger.info("Entering demo endpoint");
        String response = "Working";
        logger.info("Exiting demo endpoint with response: {}", response);
        return response;
    }

    /**
     * Endpoint to load user data.
     *
     * @return A list of users.
     */
    @GetMapping("/load")
    public List<UserProfile> loadData(){
        logger.info("Entering loadData endpoint");
        userService.loadData();
        List<UserProfile> response = userService.getAllUsers();
        logger.info("Exiting loadData endpoint with response: {}", response);
        return response;
    }

    /**
     * Endpoint to create a new user profile.
     *
     * @param userProfile The user profile details.
     * @return ResponseEntity containing the created user profile.
     */
    @PostMapping("/create")
    public ResponseEntity<UserProfile> createUser(@RequestBody UserProfile userProfile){
        logger.info("Entering createUser endpoint with userProfile: {}", userProfile);
        UserProfile createdUserProfile = userService.createUser(userProfile);
        logger.info("Exiting createUser endpoint with createdUserProfile: {}", createdUserProfile);
        return ResponseEntity.ok(createdUserProfile);
    }

    /**
     * Endpoint to retrieve a user profile by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity with the user profile if found, or a not found message if null.
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserProfile> getUser(@PathVariable String id){
        logger.info("Entering getUser endpoint with id: {}", id);
        Optional<UserProfile> userProfile = userService.getUser(id);
        if (userProfile.isPresent()) {
            logger.info("Exiting getUser endpoint with userProfile: {}", userProfile.get());
            return ResponseEntity.ok(userProfile.get());
        } else {
            logger.warn("User profile not found for id: {}", id);
            return ResponseEntity.status(404).body(null);
        }
    }

    /**
     * Endpoint to retrieve all user profiles.
     *
     * @return A list of all user profiles.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserProfile>> getAll(){
        logger.info("Entering getAll endpoint");
        List<UserProfile> response = userService.getAllUsers();
        logger.info("Exiting getAll endpoint with response: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to delete a user profile by ID.
     *
     * @param id The ID of the user to delete.
     * @return ResponseEntity indicating the result of the deletion.
     */
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id){
        logger.info("Entering deleteUser endpoint with id: {}", id);
        userService.deleteUser(id);
        logger.info("Exiting deleteUser endpoint with result: deleted");
        return ResponseEntity.ok("deleted");
    }

    /**
     * Endpoint to set a new goal for a user.
     *
     * @param id The ID of the user.
     * @param newGoal The new goal to set.
     * @return ResponseEntity indicating the result of setting the new goal.
     */
    @PostMapping("/user/{id}/goal")
    public ResponseEntity<String> setGoal(@PathVariable String id, @RequestParam String newGoal){
        logger.info("Entering setGoal endpoint with id: {} and newGoal: {}", id, newGoal);
        String result = userService.addGoal(id, newGoal);
        logger.info("Exiting setGoal endpoint with result: {}", result);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint to retrieve all goals for a user.
     *
     * @param id The ID of the user.
     * @return ResponseEntity with the list of goals.
     */
    @GetMapping("/user/{id}/goals")
    public ResponseEntity<List<String>> getGoals(@PathVariable String id){
        logger.info("Entering getGoals endpoint with id: {}", id);
        List<String> goals = userService.getGoals(id);
        logger.info("Exiting getGoals endpoint with goals: {}", goals);
        return ResponseEntity.ok(goals);
    }
}
