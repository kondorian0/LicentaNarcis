package com.narcis.neamtiu.licentanarcis.firestore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.util.Constants;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
        String message = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);

        // Create Notification Data
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,"notifyNarcis")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, notificationBuilder.build());
//
//        // Init Worker
//        val work = OneTimeWorkRequest.Builder(ScheduledWorker::class.java)
//                .setInputData(notificationData)
//                .build();
//
//        // Start Worker
//        WorkManager.getInstance().beginWith(work).enqueue();
    }
}
