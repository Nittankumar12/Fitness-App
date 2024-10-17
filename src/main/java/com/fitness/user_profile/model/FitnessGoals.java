package com.fitness.user_profile.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Container(containerName = "n_fitnessGoals")
public class FitnessGoals {

    @Id
    @GeneratedValue
    private String id;
    private String goal;

    @PartitionKey
    private String userId;

}
