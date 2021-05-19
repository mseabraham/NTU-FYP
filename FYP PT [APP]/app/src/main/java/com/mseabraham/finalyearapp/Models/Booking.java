package com.mseabraham.finalyearapp.Models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class Booking implements Parcelable {
    private String client, date, workout, logs;
    private long time;
    private Boolean status;

    public Booking(){}

    public Booking(String client, String date, String workout, long time, String logs, Boolean status) {
        this.client = client;
        this.date = date;
        this.workout = workout;
        this.time = time;
        this.logs = logs;
        this.status = status;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Booking(Parcel in) {
        client = in.readString();
        date = in.readString();
        workout = in.readString();
        time = in.readLong();
        status = in.readBoolean();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(client);
        dest.writeString(date);
        dest.writeString(workout);
        dest.writeLong(time);
        dest.writeBoolean(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getWorkout() {
        return workout;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
