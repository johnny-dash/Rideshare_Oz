package au.org.ridesharingoz.rideshare_oz.gcmPackage;

import java.util.concurrent.atomic.AtomicInteger;

// Generate a notification ID guaranteed to be unique

class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}
