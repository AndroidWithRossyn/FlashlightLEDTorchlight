package com.flashscreen.incomingcall.torchlight.ui;

import static com.flashscreen.incomingcall.torchlight.SingletonClasses.AppOpenAds.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.flashscreen.incomingcall.torchlight.R;
import com.flashscreen.incomingcall.torchlight.databinding.ActivityGuide3Binding;
import com.flashscreen.incomingcall.torchlight.databinding.ActivityGuideBinding;
import com.flashscreen.incomingcall.torchlight.utils.Utility;

public class GuideActivity extends AppCompatActivity {
    ActivityGuideBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), binding.large.findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), binding.large1.findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), binding.large2.findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), binding.small.findViewById(com.adsmodule.api.R.id.native_ad), 2, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int lgBlueColor = getColor(R.color.light_blue);
            int primaryColor = getColor(R.color.main_blue);
            Utility.setGradientShaderToTextView(binding.a, lgBlueColor, primaryColor);
        }
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            super.onBackPressed();
        });
    }
}