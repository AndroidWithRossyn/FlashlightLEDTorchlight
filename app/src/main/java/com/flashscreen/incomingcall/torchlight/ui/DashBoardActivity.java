package com.flashscreen.incomingcall.torchlight.ui;

import static com.flashscreen.incomingcall.torchlight.SingletonClasses.AppOpenAds.activity;
import static com.flashscreen.incomingcall.torchlight.utils.Utility.setGradientShaderToTextView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.flashscreen.incomingcall.torchlight.R;
import com.flashscreen.incomingcall.torchlight.databinding.ActivityDashBoardBinding;
import com.flashscreen.incomingcall.torchlight.databinding.DialogExitBinding;
import com.flashscreen.incomingcall.torchlight.fragment.FlashFragment;
import com.flashscreen.incomingcall.torchlight.fragment.FlashScreenFragment;
import com.flashscreen.incomingcall.torchlight.fragment.GuideFragment;
import com.flashscreen.incomingcall.torchlight.fragment.TorchFragment;

public class DashBoardActivity extends AppCompatActivity {
    private ActivityDashBoardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setGradientShaderToTextView(binding.toolbarText, getColor(R.color.light_blue), getColor(R.color.main_blue));
        }

        binding.menuBtn.setOnClickListener(view -> binding.drawer.openDrawer(Gravity.LEFT));
        binding.close.setOnClickListener(view -> binding.drawer.closeDrawer(Gravity.LEFT));

        binding.flashLl.setOnClickListener(view -> AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            binding.drawer.closeDrawer(Gravity.LEFT);
            navigateToMainActivityWithFragment(FlashFragment.class, R.drawable.flash_clear);
        }));

        binding.torchLl.setOnClickListener(view -> AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            binding.drawer.closeDrawer(Gravity.LEFT);
            navigateToMainActivityWithFragment(TorchFragment.class, R.drawable.torch_clear);
        }));

        binding.flashScreen.setOnClickListener(view ->  navigateToMainActivityWithFragment(FlashFragment.class, R.drawable.flash_screen_clear));

        binding.guideLl.setOnClickListener(view -> AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            binding.drawer.closeDrawer(Gravity.LEFT);
            navigateToMainActivityWithFragment(GuideFragment.class, R.drawable.guide_clear);
        }));

        binding.flashScreenLl.setOnClickListener(view -> AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            binding.drawer.closeDrawer(Gravity.LEFT);
            navigateToMainActivityWithFragment(FlashScreenFragment.class, R.drawable.flash_screen_clear);
        }));

        binding.torchScreen.setOnClickListener(view -> navigateToMainActivityWithFragment(TorchFragment.class, R.drawable.torch_clear));

        binding.flashButtonScreen.setOnClickListener(view -> {
            binding.drawer.closeDrawer(Gravity.LEFT);
            navigateToMainActivityWithFragment(FlashScreenFragment.class, R.drawable.flash_screen_clear);
        });

        binding.guideScreen.setOnClickListener(view -> navigateToMainActivityWithFragment(GuideFragment.class, R.drawable.guide_clear));

        binding.privacyPolicyLl.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cosmomediasolution.blogspot.com/p/privacy-policy.html"));
            startActivity(browserIntent);
        });

        binding.termsAndConditionLl.setOnClickListener(view -> AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            startActivity(new Intent(DashBoardActivity.this, TermsConditionsActivity.class));
        }));

    }

    private void navigateToMainActivityWithFragment(Class<? extends Fragment> fragmentClass, int iconResourceId) {
        Intent intent = new Intent(DashBoardActivity.this, MainActivity.class);
        intent.putExtra("fragmentClass", fragmentClass.getName());
        intent.putExtra("iconResourceId", iconResourceId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START))
            binding.drawer.closeDrawer(GravityCompat.START);
        else {
            openCloseDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), binding.large.findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
    }

    private void openCloseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
        DialogExitBinding bind = DialogExitBinding.inflate(LayoutInflater.from(DashBoardActivity.this));
        builder.setView(bind.getRoot());
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), bind.large.findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
        bind.btnExit.setOnClickListener(v -> {
            dialog.dismiss();
            finishAffinity();
            System.exit(0);
        });
        bind.btnBack.setOnClickListener(v -> dialog.dismiss());

    }


}