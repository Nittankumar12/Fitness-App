package com.fitness.user_profile.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserModel {

    private String id;
    private String name;
    private String email;
    private int age;
    private String gender;
    private List<String> fitnessGoals = new ArrayList<>();

}