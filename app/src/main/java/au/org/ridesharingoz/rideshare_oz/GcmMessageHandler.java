package au.org.ridesharingoz.rideshare_oz;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;

public class GcmMessageHandler extends IntentService {

    String mes;
    private Handler handler;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras.getString("joinRequest") != null) {
            sendNotification(extras.getString("joinRequest"));
            Log.v("gcm", "Preparing to build notification");
            GcmBroadcastReceiver.completeWakefulIntent(intent);

        }

    }

    public void sendNotification(String groupID) {
        final String theGroupID = groupID;
        handler.post(new Runnable() {
            public void run() {
                Intent manageGroupIntent = new Intent(getApplicationContext(), GroupManagementPanelActivity.class);
                manageGroupIntent.putExtra("groupID", theGroupID);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, manageGroupIntent, PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.firebase_logo)
                                .setContentTitle("New join request in RideShare-OZ")
                                .setContentText("A user has requested to join your group in RideShare-OZ.")
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent);
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NotificationID.getID(), mBuilder.build());
            }
        });
    }

}

class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}