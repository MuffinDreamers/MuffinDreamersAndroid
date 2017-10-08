package com.github.muffindreamers.rous.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Brooke on 10/7/2017.
 */

public class RatData implements Serializable {

    private int id;

    private Date dateCreated;

    private String locationType;

    private int zipCode;

    private String streetAddress;

    private String city;

    private String borough;

    private double latitude;

    private double longitude;

    public RatData() {
        this(0, new Date(), "test", 0, "test", "test", "test", 0.0, 0.0);
    }

    public RatData(int id, Date dateCreated, String locationType, int zipCode,
                   String streetAddress, String city, String borough, double latitude,
                   double longitude) {
        this.id = id;
        this.dateCreated = dateCreated;
        this. locationType = locationType;
        this.zipCode = zipCode;
        this.streetAddress = streetAddress;
        this.city = city;
        this.borough = borough;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }


    public Date getDateCreated() {
        return dateCreated;
    }

    public String getLocationType() {
        return locationType;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getBorough() {
        return borough;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter1= new SimpleDateFormat("MM-dd");
        String date1 = formatter1.format(this.getDateCreated());
        return "Rat Sighting: " + String.valueOf(this.id) + ", Date Created: " + date1;
    }
}
