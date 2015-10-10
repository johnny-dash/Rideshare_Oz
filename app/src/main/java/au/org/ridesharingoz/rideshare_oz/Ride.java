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
    private String time;
    private String driverID;
    private Map<String,Boolean> passengers;
    private int seatNum;
    private String group;
    private String event;
    private String type;

    //private String time;
    //private Map<String,Boolean> pins;

    public Ride(){}

    public Ride(String driverID, int seatNum, String date, String time,String group, String event, String type){
        this.driverID = driverID;
        this.seatNum = seatNum;
        this.date =date;
        this.time = time;
        this.group = group;
        this.event = event;
        this.type = type;
        //this.time = time;
    }

    public String getDate() {return date;}

    public String getTime() {return time;}

    public String getdriverID(){return driverID;}

    public int getSeatNum(){return seatNum;}

    public String getGroup(){return group;}

    public String getEvent(){return event;}

    public String getType(){return type;}


    public Map<String, Boolean> getPassengers() {
        return passengers;
    }
}
