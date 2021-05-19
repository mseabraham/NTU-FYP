package com.mseabraham.finalyearapp.Models;

public class Exercise {
    private String muscle, split;

    public Exercise(String muscle, String split){
        this.muscle = muscle;
        this.split = split;
    }

    public Exercise(){}

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

}
