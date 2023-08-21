package com.flashscreen.incomingcall.torchlight.ui;

import static com.flashscreen.incomingcall.torchlight.SingletonClasses.AppOpenAds.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.flashscreen.incomingcall.torchlight.R;
import com.flashscreen.incomingcall.torchlight.databinding.ActivityTermsOfUseBinding;

public class TermsOfUseActivity extends AppCompatActivity {
    ActivityTermsOfUseBinding binding;
    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_of_use);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), binding.small.findViewById(com.adsmodule.api.R.id.native_ad), 2, null);

        type = getIntent().getIntExtra("type", 0);
        binding.privacyPolicyTV.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cosmomediasolution.blogspot.com/p/privacy-policy.html"));
            startActivity(browserIntent);
        });

        binding.termsConditionsTV.setOnClickListener(view -> AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            startActivity(new Intent(TermsOfUseActivity.this, TermsConditionsActivity.class));
        }));

        binding.tvText.setOnClickListener(view -> mStartAct());

    }

    private void mStartAct() {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            startActivity(new Intent(TermsOfUseActivity.this, DashBoardActivity.class));
            finish();
        });
    }
}