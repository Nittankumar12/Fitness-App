package com.fitness.user_profile.util;


import com.fitness.user_profile.entity.FitnessGoals;
import com.fitness.user_profile.entity.UserProfile;
import com.fitness.user_profile.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class Conversions {


    public <T> List<T> convertIterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .toList();
    }

    public UserModel entityToModel(UserProfile userEntity, Iterable<FitnessGoals> goalEntities)
    {
        List<FitnessGoals> goals = convertIterableToList(goalEntities);
        List<String> fitnessGoals = new ArrayList<>();
        goals.forEach(goalEntity -> fitnessGoals.add(goalEntity.getGoal()));
        UserModel userModel = new UserModel();

        userModel.setId(userEntity.getId());
        userModel.setName(userEntity.getName());
        userModel.setAge(userEntity.getAge());
        userModel.setEmail(userEntity.getEmail());
        userModel.setGender(userEntity.getGender());
        userModel.setFitnessGoals(fitnessGoals);
        return userModel;
    }

    public UserProfile modelToEntity(UserModel userModel)
    {
        UserProfile userEntity = new UserProfile(userModel.getId(), userModel.getName(), userModel.getEmail(), userModel.getAge(),userModel.getGender(),new ArrayList<>());
        userEntity.setId(userModel.getId());
        userEntity.setName(userModel.getName());
        userEntity.setEmail(userModel.getEmail());
        userEntity.setGender(userModel.getGender());
        userEntity.setFitnessGoals(new ArrayList<>());
        return userEntity;

    }

}