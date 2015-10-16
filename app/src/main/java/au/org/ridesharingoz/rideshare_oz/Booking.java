package au.org.ridesharingoz.rideshare_oz;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by Ocunidee on 14/10/2015.
 */

public class Booking {

    private String rideID;
    private String pinID;
    private Timestamp departureTime;
    private Timestamp arrivalTime;
    private String rideType;
    private String userID;


    public Booking(){}

    public Booking(String rideID, String rideType, String pinID, Timestamp departureTime, String userID, Timestamp arrivalTime) {
        this.rideID = rideID;
        this.pinID = pinID;
        this.departureTime = departureTime;
        this.rideType = rideType;
        this.userID = userID;
        this.arrivalTime = arrivalTime;
    }

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public String getRideID() {
        return rideID;
    }

    public void setRideID(String rideID) {
        this.rideID = rideID;
    }

    public String getPinID() {
        return pinID;
    }

    public void setPinID(String pinID) {
        this.pinID = pinID;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
