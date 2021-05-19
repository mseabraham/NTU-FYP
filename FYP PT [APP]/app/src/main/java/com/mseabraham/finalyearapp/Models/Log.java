package com.mseabraham.finalyearapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Log implements Parcelable {

    public List<Set> sets;
    public String exerciseName;

    public Log() {
    }

    public Log(List<Set> sets, String exerciseName) {
        this.sets = sets;
        this.exerciseName = exerciseName;
    }

    protected Log(Parcel in) {
        exerciseName = in.readString();
        sets = new ArrayList<Set>();
        in.readTypedList(sets, Set.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(exerciseName);
        dest.writeTypedList(sets);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Log> CREATOR = new Creator<Log>() {
        @Override
        public Log createFromParcel(Parcel in) {
            return new Log(in);
        }

        @Override
        public Log[] newArray(int size) {
            return new Log[size];
        }
    };

    public List<Set> getSets() {
        return sets;
    }

    public void setLogs(List<Set> log) {
        this.sets = log;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }
}
