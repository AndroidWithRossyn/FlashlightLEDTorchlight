package com.flashscreen.incomingcall.torchlight.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.flashscreen.incomingcall.torchlight.R;
import com.flashscreen.incomingcall.torchlight.databinding.FragmentFlashBinding;
import com.flashscreen.incomingcall.torchlight.service.FlashlightService;
import com.flashscreen.incomingcall.torchlight.utils.CameraHelper;

import java.util.Timer;
import java.util.TimerTask;

public class FlashFragment extends Fragment {
    FragmentFlashBinding binding;
    private CameraHelper helper;
    private final Handler handler = new Handler();
    private boolean isTestRunning = false;
    private Timer testTimer;
    private boolean isFlashOn;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_flash, container, false);

        sharedPreferences = requireContext().getSharedPreferences("flash1", Context.MODE_PRIVATE);
        binding.checkboxStatus.setVisibility(View.VISIBLE);
        binding.status.setVisibility(View.VISIBLE);
        binding.checklist.setVisibility(View.VISIBLE);
        binding.statusIncomingCall.setVisibility(View.GONE);
        binding.large1.setVisibility(View.GONE);
        binding.large.setVisibility(View.VISIBLE);
//        binding.speedIncomingCall.setVisibility(View.GONE);
        binding.test.setVisibility(View.GONE);
        binding.incomingNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.checkboxStatus.setVisibility(View.GONE);
                binding.status.setVisibility(View.GONE);
                binding.checklist.setVisibility(View.GONE);
                binding.statusIncomingCall.setVisibility(View.VISIBLE);
//                binding.speedIncomingCall.setVisibility(View.VISIBLE);
                binding.test.setVisibility(View.VISIBLE);
                binding.large1.setVisibility(View.VISIBLE);
                binding.large.setVisibility(View.GONE);
                binding.statusIncomingIcon.setImageDrawable(getResources().getDrawable(R.drawable.notifications));
                binding.statusIncomingText.setText(R.string.turn_notification);
            }
        });


        binding.checkboxStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && binding.checkboxStatus.isChecked()) {
                binding.checkboxStatus.setBackgroundResource(R.drawable.switch_on);
                binding.onOffText.setText(R.string.on);
                Intent serviceIntent = new Intent(requireActivity(), FlashlightService.class);
                requireActivity().startService(serviceIntent);
            } else {
                binding.checkboxStatus.setBackgroundResource(R.drawable.switch_off);
                binding.onOffText.setText(R.string.off);
                Intent serviceIntent = new Intent(requireActivity(), FlashlightService.class);
                requireActivity().stopService(serviceIntent);
            }
        });

        binding.statusIncomingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                binding.statusIncomingSwitch.setBackgroundResource(R.drawable.switch_on);
                Intent serviceIntent = new Intent(requireActivity(), FlashlightService.class);
                requireActivity().startService(serviceIntent);
            } else {
                binding.statusIncomingSwitch.setBackgroundResource(R.drawable.switch_off);
                Intent serviceIntent = new Intent(requireActivity(), FlashlightService.class);
                requireActivity().stopService(serviceIntent);
            }

        });
        sharedPreferences = requireContext().getSharedPreferences("flash1", Context.MODE_PRIVATE);

        binding.test.setOnClickListener(v -> {
            if (!isTestRunning) {
                startFlashlightBlinking();
            } else {
                stopFlashlightBlinking();
            }
        });


        helper = CameraHelper.getInstance(requireContext());


        return binding.getRoot();
    }



    private void startFlashlightBlinking() {
        // Set the on and off time to 100 milliseconds each
        int onTimeMillis = 100;
        int offTimeMillis = 100;

        testTimer = new Timer();
        testTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        toggleFlashlight();
                    }
                });
            }
        }, 0, onTimeMillis + offTimeMillis);

        isTestRunning = true;
        binding.test.setText("Stop");
    }
    private void stopFlashlightBlinking() {
        if (testTimer != null) {
            testTimer.cancel();
            testTimer = null;
        }

        // Ensure the flashlight is turned off after stopping the test
        if (isFlashOn) {
            toggleFlashlight();
        }

        isTestRunning = false;
        binding.test.setText("Start");
    }

    private void toggleFlashlight() {
        if (isFlashOn) {
            // Turn off the flashlight
            helper.turnOffAll(getContext());
            isFlashOn = false;
        } else {
            // Turn on the flashlight
            helper.toggleNormalFlash(getContext());
            isFlashOn = true;
        }
    }




    @Override
    public void onResume() {
        AdUtils.showNativeAd(requireActivity(), Constants.adsResponseModel.getNative_ads().getAdx(), binding.large.findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
        AdUtils.showNativeAd(requireActivity(), Constants.adsResponseModel.getNative_ads().getAdx(), binding.large1.findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);

        boolean isCheckboxStatusChecked = sharedPreferences.getBoolean("checkboxStatusChecked", false);
        boolean isStatusIncomingSwitchChecked = sharedPreferences.getBoolean("statusIncomingSwitchChecked", false);

        binding.checkboxStatus.setChecked(isCheckboxStatusChecked);
        binding.statusIncomingSwitch.setChecked(isStatusIncomingSwitchChecked);
        binding.large1.setVisibility(View.GONE);
        binding.large.setVisibility(View.VISIBLE);
        binding.checkboxStatus.setVisibility(View.VISIBLE);
        binding.status.setVisibility(View.VISIBLE);
        binding.checklist.setVisibility(View.VISIBLE);
        binding.statusIncomingCall.setVisibility(View.GONE);
        binding.speedIncomingCall.setVisibility(View.GONE);
        binding.test.setVisibility(View.GONE);
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();

        // Save the state of the checkboxes to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("checkboxStatusChecked", binding.checkboxStatus.isChecked());
        editor.putBoolean("statusIncomingSwitchChecked", binding.statusIncomingSwitch.isChecked());
        editor.apply();
    }
}
