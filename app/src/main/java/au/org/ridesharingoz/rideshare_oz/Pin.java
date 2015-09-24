package au.org.ridesharingoz.rideshare_oz;

import java.io.Serializable;
import java.sql.Time;

/**
 * Created by Johnny Mao on 15/9/23.
 */
public class Pin implements Serializable {
    private int longtitude;
    private int latitude;
    private String address;
    private Time time;
}
