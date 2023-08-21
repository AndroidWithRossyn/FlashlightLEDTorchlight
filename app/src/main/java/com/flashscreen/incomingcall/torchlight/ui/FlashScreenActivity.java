package com.flashscreen.incomingcall.torchlight.ui;

import static com.flashscreen.incomingcall.torchlight.SingletonClasses.AppOpenAds.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.flashscreen.incomingcall.torchlight.R;

public class FlashScreenActivity extends AppCompatActivity {
    private Handler colorChangeHandler;
    private Runnable colorChangeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        Window window = getWindow();
        int visibilityFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(visibilityFlags);

        window.setStatusBarColor(Color.TRANSPARENT);
    }



    @Override
    public void onResume() {
        super.onResume();
        startColorChange();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopColorChange();
    }

    private void startColorChange() {
        colorChangeHandler = new Handler();
        colorChangeRunnable = new Runnable() {
            @Override
            public void run() {
                changeBackgroundColor();
                colorChangeHandler.postDelayed(this, 200);
            }
        };
        colorChangeHandler.post(colorChangeRunnable);
    }

    private void stopColorChange() {
        if (colorChangeHandler != null && colorChangeRunnable != null) {
            colorChangeHandler.removeCallbacks(colorChangeRunnable);
        }
    }

    private void changeBackgroundColor() {
        // Generate a random color (you can replace this with any logic to get the next color)
        int nextColor = Color.rgb(
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256)
        );

        View rootView = getWindow().getDecorView().getRootView();
        rootView.setBackgroundColor(nextColor);
    }
    @Override
    public void onBackPressed() {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            if (isLoaded) {
                // The interstitial ad is shown, set the result to indicate that the ad is shown.
                setResult(RESULT_OK);
            } else {
                // The interstitial ad is not shown, set the result to indicate that the ad is not shown.
                setResult(RESULT_CANCELED);
            }
            super.onBackPressed();
        });
    }
}
