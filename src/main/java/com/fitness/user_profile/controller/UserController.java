package com.fitness.user_profile.controller;

import com.fitness.user_profile.model.FitnessGoals;
import com.fitness.user_profile.model.UserProfile;
import com.fitness.user_profile.service.UserService;
import com.fitness.user_profile.util.ApiResponse;
import com.fitness.user_profile.util.LogMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/demo")
    public ResponseEntity<ApiResponse<String>> demo() {
        logger.info(LogMessages.ENTERING_METHOD, "demo()");
        String response = "Working";
        logger.info(LogMessages.EXITING_METHOD, "demo()");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Demo endpoint is working", response));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserProfile>> createUser(@RequestBody UserProfile userProfile) {
        logger.info(LogMessages.ENTERING_METHOD, "createUser()");
        UserProfile createdUserProfile = userService.createUser(userProfile);
        logger.info(LogMessages.EXITING_METHOD, "createUser()");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(), "User profile created successfully", createdUserProfile));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserProfile>> getUser(@PathVariable String id) {
        logger.info(LogMessages.ENTERING_METHOD, "getUser()");
        UserProfile userProfile = userService.getUser(id);
        logger.info(LogMessages.EXITING_METHOD, "getUser()");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User profile retrieved successfully", userProfile));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserProfile>> updateUser(@PathVariable String id, @RequestBody UserProfile userProfile) {
        logger.info(LogMessages.ENTERING_METHOD, "updateUser()");
        UserProfile updatedUserProfile = userService.updateUser(id, userProfile);
        logger.info(LogMessages.EXITING_METHOD, "updateUser()");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User profile updated successfully", updatedUserProfile));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String id) {
        logger.info(LogMessages.ENTERING_METHOD, "deleteUser()");
        userService.deleteUser(id);
        logger.info(LogMessages.EXITING_METHOD, "deleteUser()");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User profile deleted successfully", id));
    }

    @PostMapping("/user/{id}/goal")
    public ResponseEntity<ApiResponse<String>> setGoal(@PathVariable String id, @RequestParam String newGoal) {
        logger.info(LogMessages.ENTERING_METHOD, "setGoal()");
        String result = userService.addGoal(id, newGoal);
        logger.info(LogMessages.EXITING_METHOD, "setGoal()");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Goal added successfully", result));
    }

    @GetMapping("/user/{id}/goals")
    public ResponseEntity<ApiResponse<List<FitnessGoals>>> getGoals(@PathVariable String id) {
        logger.info(LogMessages.ENTERING_METHOD, "getGoals()");
        List<FitnessGoals> goals = userService.getGoals(id);
        logger.info(LogMessages.EXITING_METHOD, "getGoals()");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Goals retrieved successfully", goals));
    }
}
