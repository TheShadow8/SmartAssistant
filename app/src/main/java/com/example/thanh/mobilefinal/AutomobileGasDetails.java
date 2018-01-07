package com.example.thanh.mobilefinal;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by thanh on 2017-12-22.
 */

public class AutomobileGasDetails implements Parcelable {
    private double price;
    private double litres;
    private double kilometers;
    private Date date;

    public AutomobileGasDetails(double price, double litres, double kilometers, Date date) {
        setPrice(price);
        setLitres(litres);
        setKilometers(kilometers);
        setDate(date);
    }

    public double getPrice() {
        //DecimalFormat df = new DecimalFormat("0.00");
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLitres() {
        return litres;
    }

    public void setLitres(double litres) {
        this.litres = litres;
    }

    public double getKilometers() {
        return kilometers;
    }

    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    protected AutomobileGasDetails(Parcel in) {
        this.price = in.readDouble();
        this.litres = in.readDouble();
        this.kilometers = in.readDouble();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<AutomobileGasDetails> CREATOR = new Creator<AutomobileGasDetails>() {
        @Override
        public AutomobileGasDetails createFromParcel(Parcel in) {
            return new AutomobileGasDetails(in);
        }

        @Override
        public AutomobileGasDetails[] newArray(int size) {
            return new AutomobileGasDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.price);
        dest.writeDouble(this.litres);
        dest.writeDouble(this.kilometers);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
    }
}
