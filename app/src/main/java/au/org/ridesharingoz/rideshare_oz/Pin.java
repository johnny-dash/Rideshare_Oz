package au.org.ridesharingoz.rideshare_oz;

import java.io.Serializable;


/**
 * Created by Johnny Mao on 15/9/23.
 */
public class Pin implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rideID;
    private double longitude;
    private double latitude;
    private String address;
    private String time;
    private String date;


    public Pin() {}

    public Pin(String RideID,double longitude,double latitude,String address,String time,String date){
        this.rideID = RideID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.time = time;
        this.date = date;
    }

    public String getaddress() {return address;}

    public String getdate() {return date;}

    public double getlatitude() {return latitude;}

    public double getlongitude() {return longitude;}

    public String getrideID() {return rideID;}

    public String gettime() {return time;}

}
