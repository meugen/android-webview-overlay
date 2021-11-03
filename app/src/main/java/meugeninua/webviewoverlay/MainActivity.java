package meugeninua.webviewoverlay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationChannelGroupCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import org.pcc.webviewOverlay.WebViewOverlay;

public class MainActivity extends AppCompatActivity {

    private static final String GROUP_ID = "webview_overlay";
    private static final String CHANNEL_ID = "webview_overlay_notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Hack.context = this;

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        createChannelIfNeeded(manager);

        Intent intent = new Intent(this, OverlayReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, 0
        );
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.outline_layers_black_18)
            .setContentTitle("Open overlay")
            .setContentText("")
            .setContentIntent(pendingIntent)
            .build();
        manager.notify(1, notification);
    }

    private void createChannelIfNeeded(NotificationManagerCompat manager) {
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return;

        NotificationChannelGroupCompat group = new NotificationChannelGroupCompat.Builder(GROUP_ID)
            .setName(getText(R.string.app_name))
            .build();
        manager.createNotificationChannelGroup(group);

        NotificationChannelCompat channel = new NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_DEFAULT)
            .setGroup(GROUP_ID)
            .setName(getText(R.string.app_name))
            .build();
        manager.createNotificationChannel(channel);
    }
}