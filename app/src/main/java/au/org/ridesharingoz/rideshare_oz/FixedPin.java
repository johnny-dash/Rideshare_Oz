package au.org.ridesharingoz.rideshare_oz;

/**
 * Created by Ocunidee on 30/09/2015.
 */
public class FixedPin {

    private double longitude;
    private double latitude;
    private String address;


    public FixedPin() {}

    public FixedPin(double longtitude, double latitude, String address){
        this.longitude = longtitude;
        this.latitude = latitude;
        this.address = address;

    }

    public double getLongtitude() {return longitude;}

    public double getLatitude() {return latitude;}

    public String getAddress() {return address;}

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
