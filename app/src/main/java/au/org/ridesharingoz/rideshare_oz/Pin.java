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
    private String group;
    private String event;
    private String type;


    public Pin() {}

    public Pin(String RideID,double longitude,double latitude,String address,Timestamp datetime, String group, String event, String type){
        this.rideID = RideID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.timestamp = datetime;
        this.group = group;
        this.event = event;
        this.type = type;

    }

    public String getaddress() {return address;}

    public double getlatitude() {return latitude;}

    public double getlongitude() {return longitude;}

    public String getrideID() {return rideID;}

    public Timestamp getTimestamp() {return timestamp;}

    public String getGroup() {return  group;}

    public String getEvent() {return event;}

    public String getType() {return type;}

}
