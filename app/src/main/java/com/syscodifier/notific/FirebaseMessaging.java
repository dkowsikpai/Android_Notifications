package com.syscodifier.notific;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null){
            NotificationHelper.displayNotificationHelper(
                    getApplicationContext(),
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody()
                    );
        }
    }
}
