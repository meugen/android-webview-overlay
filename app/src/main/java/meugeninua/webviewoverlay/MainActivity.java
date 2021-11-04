package meugeninua.webviewoverlay;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationChannelGroupCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.SeekBar;

import org.pcc.webviewOverlay.WebViewOverlay;

import meugeninua.webviewoverlay.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final String GROUP_ID = "webview_overlay";
    private static final String CHANNEL_ID = "webview_overlay_notification";

    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(
            v -> onRequestPermission()
        );
        binding.seekBar.setOnSeekBarChangeListener(this);
        binding.alphaPercentLabel.setText("" + getAlphaPercent() + "%");
        launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::onPermissionRequested
        );
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        binding.alphaPercentLabel.setText(
            "" + getAlphaPercent() + "%"
        );
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }

    private void onPermissionRequested(ActivityResult activityResult) {
        if (Settings.canDrawOverlays(this)) {
            setupNotification();
        }
    }

    private void onRequestPermission() {
        if (Settings.canDrawOverlays(this)) {
            setupNotification();
        } else {
            Intent intent = new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName())
            );
            launcher.launch(intent);
        }
    }

    private int getAlphaPercent() {
        return binding.seekBar.getProgress() * 100 / binding.seekBar.getMax();
    }

    private void setupNotification() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        createChannelIfNeeded(manager);

        CharSequence url = binding.etUrl.getText();

        Intent intent = new Intent(this, OverlayReceiver.class);
        if (url != null) {
            intent.putExtra(OverlayReceiver.EXTRA_URL, url.toString().trim());
        }
        intent.putExtra(OverlayReceiver.EXTRA_ALPHA_PERCENT, 100 - getAlphaPercent());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
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