package com.fitness.user_profile.util;

/**
 * Utility class to store log messages as constants.
 */
public class LogMessages {

    // General log messages
    public static final String ENTERING_METHOD = "Entering {} method";
    public static final String EXITING_METHOD = "Exiting {} method";

    // CosmosConfiguration log messages
    public static final String CREATING_COSMOS_CLIENT = "Creating CosmosClientBuilder with URI: {}";
    public static final String CREATING_COSMOS_CONFIG = "Creating CosmosConfig with query metrics enabled";
    public static final String GETTING_DB_NAME = "Getting database name: {}";
    public static final String RESPONSE_DIAGNOSTICS = "Response Diagnostics: {}";
    public static final String RESPONSE_DIAGNOSTICS_NULL = "Response Diagnostics is null";

    // GlobalExceptionHandler log messages
    public static final String USER_NOT_FOUND_EXCEPTION = "Exception: UserNotFoundException with id - {}";
    public static final String FITNESS_GOALS_NOT_FOUND_EXCEPTION = "Exception: FitnessGoalsNotFoundException - {}";
    public static final String GENERIC_EXCEPTION = "Exception: GenericException - {}";
    public static final String UNEXPECTED_EXCEPTION = "An unexpected error occurred";

    // UserService log messages
    public static final String USER_PROFILE_CREATED = "Created user profile with id: {}";
    public static final String FOUND_USER_PROFILE = "Found user profile with id: {}";
    public static final String USER_PROFILE_DELETED = "Deleted user profile with id: {}";
    public static final String USER_PROFILE_UPDATED = "Profile updated with id: {}";
    public static final String SETTING_GOAL = "Setting new goal for user id: {}";
}
