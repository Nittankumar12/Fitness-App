package com.fitness.user_profile.repo;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import com.fitness.user_profile.model.FitnessGoals;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository interface for FitnessGoals entities.
 * Extends CosmosRepository to provide CRUD operations.
 */
@Repository
public interface FitnessGoalsRepository extends CosmosRepository<FitnessGoals, String> {

    @Query("Select * from g where g.userId = @userId")
    List<FitnessGoals> getFitnessGoalsByUserId(@Param("userId") String userId);
}
