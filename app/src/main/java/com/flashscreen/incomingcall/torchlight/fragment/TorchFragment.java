package com.flashscreen.incomingcall.torchlight.fragment;

import static com.flashscreen.incomingcall.torchlight.SingletonClasses.AppOpenAds.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.flashscreen.incomingcall.torchlight.R;
import com.flashscreen.incomingcall.torchlight.databinding.FragmentTorchBinding;
import com.flashscreen.incomingcall.torchlight.service.FlashlightService;

public class TorchFragment extends Fragment {
    FragmentTorchBinding binding;
    private SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_torch, container, false);
        sharedPreferences = requireContext().getSharedPreferences("flash2", Context.MODE_PRIVATE);

        // Restore the states of the switches and button from SharedPreferences
        binding.normalSwitch.setChecked(sharedPreferences.getBoolean("normalSwitchChecked", false));
        binding.vibrateSwitch.setChecked(sharedPreferences.getBoolean("vibrateSwitchChecked", false));
        binding.silentSwitch.setChecked(sharedPreferences.getBoolean("silentSwitchChecked", false));
        binding.onOffButton.setChecked(sharedPreferences.getBoolean("onOffButtonChecked", false));

        // For each checkbox and on/off button, update the setOnCheckedChangeListener as follows:
        binding.normalSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.normalSwitch.setBackgroundResource(R.drawable.switch_on);
                setAudioModeToNormal();
            } else {
                binding.normalSwitch.setBackgroundResource(R.drawable.switch_off);
            }
            // Save the state when the checkbox state changes
            sharedPreferences.edit().putBoolean("normalSwitchChecked", isChecked).apply();
        });



        binding.vibrateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.vibrateSwitch.setBackgroundResource(R.drawable.switch_on);
                setAudioModeToVibrate();
            }else{
                binding.vibrateSwitch.setBackgroundResource(R.drawable.switch_off);
            }
            sharedPreferences.edit().putBoolean("vibrateSwitchChecked", isChecked).apply();
        });

        binding.silentSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.silentSwitch.setBackgroundResource(R.drawable.switch_on);
                setAudioModeToSilent();
            }else{
                binding.silentSwitch.setBackgroundResource(R.drawable.switch_off);
            }
            sharedPreferences.edit().putBoolean("silentSwitchChecked", isChecked).apply();
        });

        binding.onOffButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.onOffButton.setBackgroundResource(R.drawable.on_torch);
                startFlashlightService();
            } else {
                binding.onOffButton.setBackgroundResource(R.drawable.off_torch);
                stopFlashlightService();
            }
            sharedPreferences.edit().putBoolean("onOffButtonChecked", isChecked).apply();
        });

        return binding.getRoot();
    }



    private void setAudioModeToNormal() {
        AudioManager audioManager = (AudioManager) requireActivity().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    private void setAudioModeToVibrate() {
        AudioManager audioManager = (AudioManager) requireActivity().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }
    }
    private void setAudioModeToSilent() {
        AudioManager audioManager = (AudioManager) requireActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        try {
            if (audioManager != null) {
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else {
                Toast.makeText(requireActivity(), "AudioManager not available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireActivity(), "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void startFlashlightService() {
        Intent serviceIntent = new Intent(requireActivity(), FlashlightService.class);
        requireActivity().startService(serviceIntent);
    }

    private void stopFlashlightService() {
        Intent serviceIntent = new Intent(requireActivity(), FlashlightService.class);
        requireActivity().stopService(serviceIntent);
    }
    @Override
    public void onResume() {
        super.onResume();
        AdUtils.showNativeAd(requireActivity(), Constants.adsResponseModel.getNative_ads().getAdx(), binding.small.findViewById(com.adsmodule.api.R.id.native_ad), 2, null);

        boolean isNormalSwitchChecked = sharedPreferences.getBoolean("normalSwitchChecked", false);
        boolean isVibrateSwitchChecked = sharedPreferences.getBoolean("vibrateSwitchChecked", false);
        boolean isSilentSwitchChecked = sharedPreferences.getBoolean("silentSwitchChecked", false);
        boolean isOnOffButtonChecked = sharedPreferences.getBoolean("onOffButtonChecked", false);

        binding.normalSwitch.setChecked(isNormalSwitchChecked);
        binding.vibrateSwitch.setChecked(isVibrateSwitchChecked);
        binding.silentSwitch.setChecked(isSilentSwitchChecked);
        binding.onOffButton.setChecked(isOnOffButtonChecked);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                if (notificationManager.isNotificationPolicyAccessGranted()) {
                    // Permission granted
                    Log.e("Granted", "onResume: Notification Permission granted" );
                } else {
                    // Permission not granted, prompt the user to grant it
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    startActivity(intent);
                }
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences.edit().putBoolean("onOffButtonChecked", binding.onOffButton.isChecked()).apply();
        sharedPreferences.edit().putBoolean("vibrateSwitchChecked", binding.vibrateSwitch.isChecked()).apply();
        sharedPreferences.edit().putBoolean("silentSwitchChecked", binding.silentSwitch.isChecked()).apply();
        sharedPreferences.edit().putBoolean("normalSwitchChecked", binding.normalSwitch.isChecked()).apply();
    }
}