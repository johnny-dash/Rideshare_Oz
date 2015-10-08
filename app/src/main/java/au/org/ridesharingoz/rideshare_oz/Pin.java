package au.org.ridesharingoz.rideshare_oz;

import java.io.Serializable;
import java.sql.Timestamp;



/**
 * Created by Johnny Mao on 15/9/23.
 */
public class Pin implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rideID;
    private double longitude;
    private double latitude;
    private String address;
    private Timestamp timestamp;


    public Pin() {}

    public Pin(String RideID,double longitude,double latitude,String address,Timestamp datetime){
        this.rideID = RideID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.timestamp = datetime;
    }

    public String getaddress() {return address;}

    public double getlatitude() {return latitude;}

    public double getlongitude() {return longitude;}

    public String getrideID() {return rideID;}

    public Timestamp getTimestamp() {return timestamp;}

}
