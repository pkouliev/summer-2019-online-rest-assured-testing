package com.automation.pojos;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("location_id")
    private int locationID;

    @SerializedName("street_address")
    private String streetAddress;

    @SerializedName("postal_code")
    private String postalCode;

    private String city;

    @SerializedName("state_province")
    private String stateProvince;

    @SerializedName("country_id")
    private String countryID;

    public Location() {
    }

    public Location(int locationID, String streetAddress, String postalCode, String city, String stateProvince, String countryID) {
        this.locationID = locationID;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.city = city;
        this.stateProvince = stateProvince;
        this.countryID = countryID;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationID=" + locationID +
                ", streetAddress='" + streetAddress + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", stateProvince='" + stateProvince + '\'' +
                ", countryID='" + countryID + '\'' +
                '}';
    }
}

