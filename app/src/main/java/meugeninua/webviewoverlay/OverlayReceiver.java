package meugeninua.webviewoverlay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.pcc.webviewOverlay.WebViewOverlay;

public class OverlayReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        WebViewOverlay overlay = new WebViewOverlay(context);
        overlay.loadWebViewOverlay("https://ptyagicodecamp.github.io", null);
    }
}
