package meugeninua.webviewoverlay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;

import org.pcc.webviewOverlay.WebViewOverlay;

public class OverlayReceiver extends BroadcastReceiver {

    public static final String EXTRA_URL = "url";

    @Override
    public void onReceive(Context context, Intent intent) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Log.d("OverlayReceiver", "windowManager = " + windowManager);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.webview, null);
        WebView webView = view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        String url = intent.getStringExtra(EXTRA_URL);
        if (TextUtils.isEmpty(url)) {
            url = "https://6183b6075d678.htmlsave.net/";
        }
        webView.loadUrl(url);
        DisplayMetrics metrics = getDisplayMetrics(windowManager);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            Math.max(metrics.heightPixels, metrics.widthPixels) * 9 / 10,
            Math.min(metrics.heightPixels, metrics.widthPixels) * 9 / 10,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        );
        ImageButton closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new RemoveViewClickListener(view));
        windowManager.addView(view, params);
    }

    private DisplayMetrics getDisplayMetrics(WindowManager manager) {
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    private static class RemoveViewClickListener implements View.OnClickListener {

        private final View viewToRemove;

        public RemoveViewClickListener(View viewToRemove) {
            this.viewToRemove = viewToRemove;
        }

        @Override
        public void onClick(View v) {
            WindowManager windowManager = (WindowManager) v.getContext().getSystemService(Context.WINDOW_SERVICE);
            windowManager.removeView(viewToRemove);
        }
    }
}
