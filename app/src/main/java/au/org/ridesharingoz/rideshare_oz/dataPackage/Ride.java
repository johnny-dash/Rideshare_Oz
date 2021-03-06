package au.org.ridesharingoz.rideshare_oz.dataPackage;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Ocunidee on 20/09/2015.
 *
 */
public class Ride {
    private Timestamp timestamp;
    private String driverID;
    private Map<String,String> pins;
    private Map<String,Boolean> pendingJoinRequests;
    private int seatNum;
    private String groupEventID ;
    private Boolean isEvent;
    private String type;


    public Ride(){}

    public Ride(String driverID, int seatNum, Timestamp timestamp, Map<String,String> pins, String groupEventID , Boolean isEvent, String type){
        this.driverID = driverID;
        this.seatNum = seatNum;
        this.timestamp = timestamp;
        this.pins = pins;
        this.groupEventID  = groupEventID ;
        this.isEvent = isEvent;
        this.type = type;
    }
    public  Timestamp getTimestamp(){return timestamp;}

    public String getDriverID(){return driverID;}

    public int getSeatNum(){return seatNum;}

    public String getGroupEventID(){return groupEventID ;}

    public Boolean getIsEvent(){return isEvent;}

    public String getType(){return type;}

    public Map<String,String> getPins(){return pins;}

    public Map<String,Boolean> getPendingJoinRequests(){return pendingJoinRequests;}


    public String getDate() {
        return null;
    }
}
