package com.flashscreen.incomingcall.torchlight.ui;

import static com.flashscreen.incomingcall.torchlight.SingletonClasses.AppOpenAds.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.flashscreen.incomingcall.torchlight.R;
import com.flashscreen.incomingcall.torchlight.databinding.ActivityIntro1Binding;

public class Intro1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityIntro1Binding binding = ActivityIntro1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), binding.small.findViewById(com.adsmodule.api.R.id.native_ad), 2, null);
        Window window = getWindow();
        int visibilityFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(visibilityFlags);

        window.setStatusBarColor(Color.TRANSPARENT);
        binding.txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
                    startActivity(new Intent(activity, TermsOfUseActivity.class));
                });

            }
        });
    }
}