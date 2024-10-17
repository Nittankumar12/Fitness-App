package com.fitness.user_profile.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Container(containerName = "user_nittan")
public class UserProfile {

    @Id
    @GeneratedValue
    private String id;
    private String name;

    @PartitionKey
    private String email;
    private int age;
    private String gender;
    private List<FitnessGoals> fitnessGoals;
}
