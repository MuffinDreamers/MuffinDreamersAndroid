package com.github.muffindreamers.rous.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Brooke on 10/7/2017.
 * The ratdata information holder class
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

    /**
     * A no args constructor in order have have test
     * or blank data
     */
    public RatData() {
        this(0, new Date(), "test", 0, "test", "test", "test", 0.0, 0.0);
    }

    /**
     * Initializes a RatData object with all the necessary
     * data
     * @param id of the rat sighting
     * @param dateCreated of the rat sighting
     * @param locationType of the rat sighting
     * @param zipCode of the rat sighting
     * @param streetAddress of the rat sighting
     * @param city of the rat sighting
     * @param borough of the rat sighting
     * @param latitude of the rat sighting
     * @param longitude of the rat sighting
     */
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

    /**
     * Returns the rat sighting id
     * @return the id of the rat sigthing
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the date the rat sighting was created
     * @return the date of the rat sighting
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Returns the location type of the rat sighting
     * @return the location type of the rat sighting
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     * Returns the zip code of the rat sighting
     * @return the zip code of the rat sighting
     */
    public int getZipCode() {
        return zipCode;
    }

    /**
     * Returns the street address of the rat sighting
     * @return the street address of the rat sighting
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * Returns the city of the rat sighting
     * @return returns the city of the rat sighting
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the borough of the rat sighting
     * @return the borough of the rat sighting
     */
    public String getBorough() {
        return borough;
    }

    /**
     * Returns the latitude of the rat sighting
     * @return the latitude of the rat sighting
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude of the rat sighting
     * @return the longitude of the rat sighting
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the id of the rat sighting
     * @param id of the rat sighting
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the date created of the rat sighting
     * @param dateCreated of the rat sighting
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Sets the location type of the rat sighting
     * @param locationType of the rat sighting
     */
    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    /**
     * Sets the zip code of the rat sighting
     * @param zipCode of the rat sighting
     */
    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Sets the street address of the rat sighting
     * @param streetAddress of the rat sighting
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     * Sets the city of the rat sighting
     * @param city of the rat sighting
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Sets the borough of the rat sighting
     * @param borough of the rat sighting
     */
    public void setBorough(String borough) {
        this.borough = borough;
    }

    /**
     * Sets the latitude of the rat sighting
     * @param latitude of the rat sighting
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Sets the longitude of the rat sighting
     * @param longitude of the rat sighting
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Overrides Object's toString method
     * @return a String representing the RatData object
     */
    @Override
    public String toString() {
        SimpleDateFormat formatter1= new SimpleDateFormat("MM-dd-yyyy");
        String date1 = formatter1.format(this.getDateCreated());
        return "Sighting ID: " + String.valueOf(this.id) + ", Date Created: " + date1;
    }
}
