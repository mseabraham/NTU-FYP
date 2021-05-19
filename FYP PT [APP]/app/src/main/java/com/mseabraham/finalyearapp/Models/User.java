package com.mseabraham.finalyearapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class User extends Model implements Parcelable {
    private String username, img;

    public User(String username, String img) {
        this.username = username;
        this.img = img;
    }

    public User(){}

    protected User(Parcel in) {
        username = in.readString();
        img = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(img);
    }
}
