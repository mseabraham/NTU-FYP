package com.mseabraham.finalyearapp.Models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;

public class Workout {
    @DocumentId String workoutId;
    private String name, trainerID;
    private ArrayList<String> exercises;

    public Workout(String workoutId, String name, String trainerID, ArrayList<String> exercises) {
        this.workoutId = workoutId;
        this.name = name;
        this.trainerID = trainerID;
        this.exercises = exercises;

    }

    public Workout(){}

    public String getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrainerID() {
        return trainerID;
    }

    public void setTrainerID(String trainerID) {
        this.trainerID = trainerID;
    }

    public ArrayList<String> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<String> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
