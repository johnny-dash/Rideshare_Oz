package au.org.ridesharingoz.rideshare_oz;

import java.sql.Timestamp;

/**
 * Created by Ocunidee on 09/10/2015.
 */
public class Event {

    private String eventName;
    private String eventDescription;
    private Timestamp eventStartDate;
    private Timestamp eventEndDate;
    private String pinID;

    public Event(String eventName, String eventDescription, Timestamp eventStartDate, Timestamp eventEndDate, String eventPinID) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.pinID = eventPinID;
    }

    public Event(){
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Timestamp getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Timestamp eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public Timestamp getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(Timestamp eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getPinID() {
        return pinID;
    }

    public void setPinID(String pinID) {
        this.pinID = pinID;
    }
}
