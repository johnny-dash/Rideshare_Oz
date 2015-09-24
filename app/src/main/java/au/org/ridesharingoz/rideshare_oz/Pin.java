package au.org.ridesharingoz.rideshare_oz;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Johnny Mao on 15/9/23.
 */
public class Pin {
    private String RideID;
    private double longitude;
    private double latitude;
    private String address;
    private String time;
    private String date;


    public Pin() {}

    public Pin(String RideID,double longtitude,double latitude,String address,String time,String date){
        this.RideID = RideID;
        this.longitude = longtitude;
        this.latitude = latitude;
        this.address = address;
        this.time = time;
        this.date = date;
    }

    public String getRideID() {return RideID;}

    public double getLongtitude() {return longitude;}

    public double getLatitude() {return latitude;}

    public String getAddress() {return address;}

    public String getTime() {return time;}

    public String getDate() {return date;}

    

}
