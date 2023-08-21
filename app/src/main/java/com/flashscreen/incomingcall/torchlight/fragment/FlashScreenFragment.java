package com.flashscreen.incomingcall.torchlight.fragment;

import static com.flashscreen.incomingcall.torchlight.SingletonClasses.AppOpenAds.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.flashscreen.incomingcall.torchlight.R;
import com.flashscreen.incomingcall.torchlight.ui.FlashScreenActivity;

import java.util.Timer;
import java.util.TimerTask;

public class FlashScreenFragment extends Fragment {
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_flash_screen, container, false);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), view.findViewById(R.id.large).findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);

        view.findViewById(R.id.flashNow).setOnClickListener(v->{
            navigateToYourActivity();
        });
        return view;
    }


    private void navigateToYourActivity() {
        Intent intent = new Intent(getActivity(), FlashScreenActivity.class);
        startActivity(intent);
    }
}
