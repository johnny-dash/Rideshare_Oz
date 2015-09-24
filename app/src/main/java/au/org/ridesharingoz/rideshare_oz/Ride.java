package au.org.ridesharingoz.rideshare_oz;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Ocunidee on 20/09/2015.
 *
 */
public class Ride {
    private String RideID;
    private String DriverID;
    private Map<String,String> passenger;
    private int seatNum;
    private String date;
    //private Map<String,Integer> pins;

    public Ride(){}

    public Ride(String RideID,String DriverID, int seatNum, String date){
        this.RideID = RideID;
        this.DriverID = DriverID;
        //this.passenger = passenger;
        this.seatNum = seatNum;
        this.date =date;
        //this.pins = pins;

    }

    public String getRideID(){return RideID;}

    public String getDriverID(){return DriverID;}

    public Map<String,String> getPassenger() {return passenger;}

    public int getSeatNum(){return seatNum;}

    public String getDate() {return date;}


}
