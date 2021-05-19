package com.mseabraham.finalyearapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Set implements Parcelable {
    public long reps, weight;

    public Set(){}

    public Set(long reps, long weight){
        this.reps = reps;
        this.weight = weight;
    }

    protected Set(Parcel in) {
        reps = in.readLong();
        weight = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(reps);
        dest.writeLong(weight);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Set> CREATOR = new Creator<Set>() {
        @Override
        public Set createFromParcel(Parcel in) {
            return new Set(in);
        }

        @Override
        public Set[] newArray(int size) {
            return new Set[size];
        }
    };

    public long getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
