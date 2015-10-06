package au.org.ridesharingoz.rideshare_oz;

/**
 * Created by helen on 5/10/15.
 */
public class Rating {

    private float asDriver;
    private float asPassenger;
    private int dRatingNb;
    private int pRatingNb;

    public Rating(){}

    public Rating(float asDriver, float asPassenger, int dRatingNb, int pRatingNb){
        this.asDriver = asDriver;
        this.asPassenger = asPassenger;
        this.dRatingNb = dRatingNb;
        this.pRatingNb = pRatingNb;
    }

    public float getAsDriver(){
        return  asDriver;
    }

    public float getAsPassenger() {
        return asPassenger;
    }

    public int getdRatingNb() {
        return dRatingNb;
    }

    public int getpRatingNb() {
        return pRatingNb;
    }
}
