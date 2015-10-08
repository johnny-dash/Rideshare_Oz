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
    private String DriverID;
    private Map<String,Boolean> passengers;
    private int seatNum;
    private String date;
    //private String time;
    //private Map<String,Boolean> pins;

    public Ride(){}

    public Ride(String DriverID, int seatNum, String date){
        this.DriverID = DriverID;
        this.seatNum = seatNum;
        this.date =date;
        //this.time = time;
    }


    public String getDriverID(){return DriverID;}

    public int getSeatNum(){return seatNum;}

    public String getDate() {return date;}

    public Map<String, Boolean> getPassengers() {
        return passengers;
    }
}
