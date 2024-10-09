package com.fitness.user_profile.repo;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.fitness.user_profile.model.UserProfile;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for UserProfile entities.
 * Extends CosmosRepository to provide CRUD operations.
 */
@Repository
public interface UserProfileRepository extends CosmosRepository<UserProfile, String> {

}
