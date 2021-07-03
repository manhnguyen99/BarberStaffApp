package com.example.barberstaffapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Salon implements Parcelable {

    private String nameSalon, addressSalon, website, phone, openHours, salonID;

    public Salon() {
    }
    protected Salon(Parcel in) {
        nameSalon = in.readString();
        addressSalon = in.readString();
        website = in.readString();
        phone = in.readString();
        openHours = in.readString();
        salonID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameSalon);
        dest.writeString(addressSalon);
        dest.writeString(website);
        dest.writeString(phone);
        dest.writeString(openHours);
        dest.writeString(salonID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Salon> CREATOR = new Creator<Salon>() {
        @Override
        public Salon createFromParcel(Parcel in) {
            return new Salon(in);
        }

        @Override
        public Salon[] newArray(int size) {
            return new Salon[size];
        }
    };

    public String getNameSalon() {
        return nameSalon;
    }

    public void setNameSalon(String nameSalon) {
        this.nameSalon = nameSalon;
    }

    public String getAddressSalon() {
        return addressSalon;
    }

    public void setAddressSalon(String addressSalon) {
        this.addressSalon = addressSalon;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getSalonID() {
        return salonID;
    }

    public void setSalonID(String salonID) {
        this.salonID = salonID;
    }
}
