package au.org.ridesharingoz.rideshare_oz.gcmPackage;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import au.org.ridesharingoz.rideshare_oz.groupsPackage.GroupManagementPanelActivity;
import au.org.ridesharingoz.rideshare_oz.R;

public class GcmMessageHandler extends IntentService {

    private Handler handler;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        // Set up the message
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        // If the message is a join request
        if (extras.getString("joinRequest") != null) {
            sendJoinRequest(extras.getString("joinRequest"));
        }

        // We're done with the message
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Given a group ID, send a join request notification that links to that group's management
    public void sendJoinRequest(String groupID) {

        // Make group ID available inside handler
        final String theGroupID = groupID;

        // Run this on the app's main thread
        handler.post(new Runnable() {
            public void run() {

                // Set up the link to the group management activity
                Intent manageGroupIntent = new Intent(getApplicationContext(),
                        GroupManagementPanelActivity.class);
                manageGroupIntent.putExtra("groupID", theGroupID);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                        0, manageGroupIntent, PendingIntent.FLAG_ONE_SHOT);

                // Build the notification
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.firebase_logo)
                                .setContentTitle(getString(R.string.notification_joinrequest_title))
                                .setContentText(getString(R.string.notification_joinrequest_body))
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent);

                // Send the notification
                NotificationManager manager
                        = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NotificationID.getID(), mBuilder.build());
            }
        });
    }

}
