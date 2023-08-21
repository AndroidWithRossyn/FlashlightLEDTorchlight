package com.flashscreen.incomingcall.torchlight.ui;

import static com.flashscreen.incomingcall.torchlight.SingletonClasses.AppOpenAds.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.flashscreen.incomingcall.torchlight.R;
import com.flashscreen.incomingcall.torchlight.databinding.ActivityMainBinding;
import com.flashscreen.incomingcall.torchlight.fragment.FlashFragment;
import com.flashscreen.incomingcall.torchlight.fragment.FlashScreenFragment;
import com.flashscreen.incomingcall.torchlight.fragment.GuideFragment;
import com.flashscreen.incomingcall.torchlight.fragment.TorchFragment;
import com.flashscreen.incomingcall.torchlight.utils.Utility;

public class MainActivity extends BaseActivity {
    private static final String SELECTED_ITEM_ID = "SELECTED_ITEM_ID";

    private ActivityMainBinding mainActivityBinding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainActivityBinding.getRoot());

        mainActivityBinding.bottomNavigationView.setItemIconTintList(null);
        mainActivityBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> changeFragment(item));
            return true;
        });

        mainActivityBinding.menuBtn.setOnClickListener(v -> mainActivityBinding.drawer.openDrawer(Gravity.LEFT));

        mainActivityBinding.flashLl.setOnClickListener(v -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
                mainActivityBinding.drawer.closeDrawer(Gravity.LEFT);
                loadFragmentWithIcon(new FlashFragment(), R.drawable.flash_clear, "Flash");
            });
        });

        mainActivityBinding.torchLl.setOnClickListener(v -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
                mainActivityBinding.drawer.closeDrawer(Gravity.LEFT);
                loadFragmentWithIcon(new TorchFragment(), R.drawable.torch_clear, "Torch");
            });
        });

        mainActivityBinding.flashScreenLl.setOnClickListener(v -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
                mainActivityBinding.drawer.closeDrawer(Gravity.LEFT);
                loadFragmentWithIcon(new FlashScreenFragment(), R.drawable.flash_screen_clear, "Flash Screen");
            });
        });

        mainActivityBinding.guideLl.setOnClickListener(v -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
                mainActivityBinding.drawer.closeDrawer(Gravity.LEFT);
                loadFragmentWithIcon(new GuideFragment(), R.drawable.guide_clear, "Downloads");
            });
        });

        mainActivityBinding.privacyPolicyLl.setOnClickListener(v -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
                mainActivityBinding.drawer.closeDrawer(Gravity.LEFT);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cosmomediasolution.blogspot.com/p/privacy-policy.html"));
                startActivity(browserIntent);
            });
        });

        mainActivityBinding.termsAndConditionLl.setOnClickListener(v -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
                mainActivityBinding.drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, TermsConditionsActivity.class);
                startActivity(intent);
            });
        });

        int selectedItem = savedInstanceState != null ? savedInstanceState.getInt(SELECTED_ITEM_ID) : R.id.flashNav;
        mainActivityBinding.bottomNavigationView.setSelectedItemId(selectedItem);

        int lgBlueColor = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            lgBlueColor = getColor(R.color.light_blue);
        }
        int primaryColor = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            primaryColor = getColor(R.color.main_blue);
        }
        Utility.setGradientShaderToTextView(mainActivityBinding.txtMenu, lgBlueColor, primaryColor);

        mainActivityBinding.bottomNavigationView.setOnItemReselectedListener(item -> {
            if (item.getItemId() != mainActivityBinding.bottomNavigationView.getSelectedItemId()) {
                mainActivityBinding.bottomNavigationView.setSelectedItemId(item.getItemId());
            }
        });
        mainActivityBinding.close.setOnClickListener(view -> mainActivityBinding.drawer.closeDrawer(Gravity.LEFT));
        String name = getIntent().getStringExtra("fragmentClass");
        Log.e("fragmentClass", "onCreate: " + name );
        if(name != null) getFragment(name);
    }



    private void getFragment(String name) {
        Fragment fragment;

        if (name.equals(FlashFragment.class.getName())) {
            mainActivityBinding.txtMenu.setText("Flash");
            mainActivityBinding.bottomNavigationView.setSelectedItemId(R.id.flashNav);
            fragment = new FlashFragment();
        } else if (name.equals(TorchFragment.class.getName())) {
            mainActivityBinding.txtMenu.setText("Torch");
            mainActivityBinding.bottomNavigationView.setSelectedItemId(R.id.torchyNav);
            fragment = new TorchFragment();
        } else if (name.equals(FlashScreenFragment.class.getName())) {
            mainActivityBinding.txtMenu.setText("Flash Screen");
            mainActivityBinding.bottomNavigationView.setSelectedItemId(R.id.flashScreenNav);
            fragment = new FlashScreenFragment();
        } else if (name.equals(GuideFragment.class.getName())) {
            mainActivityBinding.txtMenu.setText("Guide");
            mainActivityBinding.bottomNavigationView.setSelectedItemId(R.id.guideNav);
            fragment = new GuideFragment();
        } else {
            mainActivityBinding.bottomNavigationView.setSelectedItemId(R.id.flashNav);
            fragment = new FlashFragment();
        }

        loadFragment(fragment);
    }

    private  void changeFragment(MenuItem item){
        Fragment fragment;
        int itemId = item.getItemId();

        if (itemId == R.id.flashNav) {
            mainActivityBinding.txtMenu.setText("Flash");
            fragment = new FlashFragment();
        } else if (itemId == R.id.torchyNav) {
            mainActivityBinding.txtMenu.setText("Torch");
            fragment = new TorchFragment();
        } else if (itemId == R.id.flashScreenNav) {
            mainActivityBinding.txtMenu.setText("Flash Screen");
            fragment = new FlashScreenFragment();
        } else if (itemId == R.id.guideNav) {
            mainActivityBinding.txtMenu.setText("Guide");
            fragment = new GuideFragment();
        } else {
            fragment = new FlashFragment();
        }

        loadFragment(fragment);
    }

    private void loadFragmentWithIcon(@NonNull Fragment fragment, int iconResourceId, String itemName) {
        Bundle bundle = new Bundle();
        bundle.putInt("iconResourceId", iconResourceId);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();

        // Update the selected item in the bottom navigation view
        int selectedItem = -1;
        if (fragment instanceof FlashFragment) {
            mainActivityBinding.txtMenu.setText(itemName);
            selectedItem = R.id.flashNav;
        } else if (fragment instanceof TorchFragment) {
            mainActivityBinding.txtMenu.setText(itemName);
            selectedItem = R.id.torchyNav;
        } else if (fragment instanceof FlashScreenFragment) {
            mainActivityBinding.txtMenu.setText(itemName);
            selectedItem = R.id.flashScreenNav;
        } else if (fragment instanceof GuideFragment) {
            mainActivityBinding.txtMenu.setText(itemName);
            selectedItem = R.id.guideNav;
        }

        mainActivityBinding.bottomNavigationView.setSelectedItemId(selectedItem);
    }

    @Override
    public void onBackPressed() {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            Intent intent = new Intent(activity, DashBoardActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID, mainActivityBinding.bottomNavigationView.getSelectedItemId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPhoneStatePermission();
    }

    private void checkPhoneStatePermission() {
        // Check if the permission is already granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    632001);
        }
    }
}