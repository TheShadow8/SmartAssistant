package com.example.thanh.mobilefinal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thanh on 2017-12-22.
 */

public class AutomobileGasStatus implements Parcelable {

    private String monthYear;
    private double totalPurchases;

    public AutomobileGasStatus(String monthYear, double totalPurchases) {
        this.monthYear = monthYear;
        this.totalPurchases = totalPurchases;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public double getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(double totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    protected AutomobileGasStatus(Parcel in) {
        this.monthYear = in.readString();
        this.totalPurchases = in.readDouble();
    }

    public static final Creator<AutomobileGasStatus> CREATOR = new Creator<AutomobileGasStatus>() {
        @Override
        public AutomobileGasStatus createFromParcel(Parcel in) {
            return new AutomobileGasStatus(in);
        }

        @Override
        public AutomobileGasStatus[] newArray(int size) {
            return new AutomobileGasStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.monthYear);
        dest.writeDouble(this.totalPurchases);
    }
}
