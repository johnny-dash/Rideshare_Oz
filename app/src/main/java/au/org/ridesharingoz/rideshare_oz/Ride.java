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
    private String date;
    private String driverID;
    private Map<String,Boolean> passengers;
    private int seatNum;

    //private String time;
    //private Map<String,Boolean> pins;

    public Ride(){}

    public Ride(String driverID, int seatNum, String date){
        this.driverID = driverID;
        this.seatNum = seatNum;
        this.date =date;
        //this.time = time;
    }

    public String getDate() {return date;}

    public String getdriverID(){return driverID;}

    public int getSeatNum(){return seatNum;}



    public Map<String, Boolean> getPassengers() {
        return passengers;
    }
}
