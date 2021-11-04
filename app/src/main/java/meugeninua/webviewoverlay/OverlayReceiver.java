package meugeninua.webviewoverlay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;

import org.pcc.webviewOverlay.WebViewOverlay;

import meugeninua.webviewoverlay.databinding.WebviewBinding;

public class OverlayReceiver extends BroadcastReceiver {

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_ALPHA_PERCENT = "alpha_percent";

    @Override
    public void onReceive(Context context, Intent intent) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Log.d("OverlayReceiver", "windowManager = " + windowManager);
        LayoutInflater inflater = LayoutInflater.from(context);
        WebviewBinding binding = WebviewBinding.inflate(inflater);
        binding.webview.setAlpha(
            intent.getIntExtra(EXTRA_ALPHA_PERCENT, 0) / 100f
        );
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setBackgroundColor(Color.TRANSPARENT);
        String url = intent.getStringExtra(EXTRA_URL);
        if (TextUtils.isEmpty(url)) {
            url = "https://6183e1d154f77.htmlsave.net/";
        }
        binding.webview.loadUrl(url);
        DisplayMetrics metrics = getDisplayMetrics(windowManager);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            Math.max(metrics.heightPixels, metrics.widthPixels) * 9 / 10,
            Math.min(metrics.heightPixels, metrics.widthPixels) * 9 / 10,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        );
        binding.closeButton.setOnClickListener(new RemoveViewClickListener(binding.getRoot()));
        windowManager.addView(binding.getRoot(), params);
    }

    private Drawable buildContainerBackground(Intent intent) {
        ColorDrawable drawable = new ColorDrawable(Color.WHITE);
        int percent = intent.getIntExtra(EXTRA_ALPHA_PERCENT, 0);
        drawable.setAlpha(255 * percent / 100);
        return drawable;
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
