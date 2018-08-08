package com.liebersonsantos.weathermap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Wind implements Parcelable{

    @SerializedName("speed")
    private Double speed;
    @SerializedName("deg")
    private Double deg;

    public Wind() {
    }

    public Wind(Double speed, Double deg) {
        this.speed = speed;
        this.deg = deg;
    }

    protected Wind(Parcel in) {
        if (in.readByte() == 0) {
            speed = null;
        } else {
            speed = in.readDouble();
        }
        if (in.readByte() == 0) {
            deg = null;
        } else {
            deg = in.readDouble();
        }
    }

    public static final Creator<Wind> CREATOR = new Creator<Wind>() {
        @Override
        public Wind createFromParcel(Parcel in) {
            return new Wind(in);
        }

        @Override
        public Wind[] newArray(int size) {
            return new Wind[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (speed == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(speed);
        }
        if (deg == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(deg);
        }
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getDeg() {
        return deg;
    }

    public void setDeg(Double deg) {
        this.deg = deg;
    }
}
