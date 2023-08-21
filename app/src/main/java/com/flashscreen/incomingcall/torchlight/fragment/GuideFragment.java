package com.flashscreen.incomingcall.torchlight.fragment;

import static com.flashscreen.incomingcall.torchlight.SingletonClasses.AppOpenAds.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.flashscreen.incomingcall.torchlight.R;
import com.flashscreen.incomingcall.torchlight.databinding.ActivityGuideBinding;
import com.flashscreen.incomingcall.torchlight.databinding.DialogExitBinding;
import com.flashscreen.incomingcall.torchlight.databinding.FragmentGuideBinding;
import com.flashscreen.incomingcall.torchlight.ui.DashBoardActivity;
import com.flashscreen.incomingcall.torchlight.ui.Guide1Activity;
import com.flashscreen.incomingcall.torchlight.ui.Guide2Activity;
import com.flashscreen.incomingcall.torchlight.ui.Guide3Activity;
import com.flashscreen.incomingcall.torchlight.ui.GuideActivity;

public class GuideFragment extends Fragment {
    FragmentGuideBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guide, container, false);
        AdUtils.showNativeAd(requireActivity(), Constants.adsResponseModel.getNative_ads().getAdx(), binding.large1.findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);

        binding.guide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), GuideActivity.class));
            }
        });
        binding.guide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), Guide1Activity.class));
            }
        });
        binding.guide3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), Guide2Activity.class));
            }
        });
        binding.guide4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), Guide3Activity.class));
            }
        });
        return binding.getRoot();
    }


}