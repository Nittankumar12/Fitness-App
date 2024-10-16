package com.fitness.user_profile.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Container(containerName = "user_nittan")
public class FitnessGoals {

    @Id
    @GeneratedValue
    private String id;  // Automatically generated ID
    private String goal; // The fitness goal description
}
