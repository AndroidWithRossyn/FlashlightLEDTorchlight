package com.flashscreen.incomingcall.torchlight.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.flashscreen.incomingcall.torchlight.R;
import com.flashscreen.incomingcall.torchlight.receiver.IncomingCallReceiver;

public class FlashlightService extends Service {
    private static final String CHANNEL_ID = "flashlight_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final long BLINK_DURATION = 500;

    private CameraManager cameraManager;
    private String cameraId;
    private IncomingCallReceiver incomingCallReceiver;
    private boolean isFlashlightOn = false;
    private final Handler handler = new Handler();
    private final Runnable blinkRunnable = new Runnable() {
        @Override
        public void run() {
            if (isFlashlightOn) {
                turnFlashlightOff();
            } else {
                turnFlashlightOn();
            }
            handler.postDelayed(this, BLINK_DURATION);
        }
    };

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("com.flashscreen.incomingcall.torchlight.NOTIFICATION_RECEIVED")) {
                startBlinking();
            }
        }
    };
    private final PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    startBlinking();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    stopBlinking();
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
            stopSelf();
        }

        incomingCallReceiver = new IncomingCallReceiver();
        registerReceiver(incomingCallReceiver, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));
//        registerReceiver(notificationReceiver, new IntentFilter("com.flashscreen.incomingcall.torchlight.NOTIFICATION_RECEIVED"));


        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= 31)
        {
            Log.e("READ_PHONE_STATE", "onCreate: " + (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) );
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
        else // no permission needed
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
//        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

       /* IntentFilter inf=new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        BroadcastReceiver br=new BroadcastReceiver() {
            boolean incomingFlag;
            @Override
            public void onReceive(Context context, Intent intent) {

                TelephonyManager tm=(TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);

                switch (tm.getCallState())
                {
                    case TelephonyManager.CALL_STATE_RINGING:
                        incomingFlag=true;
                        startBlinking();
                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        if(incomingFlag)
                        {
                            stopBlinking();
                        }
                        break;

                    case TelephonyManager.CALL_STATE_IDLE:
                      *//*  if(incomingFlag)
                        {

                        }*//*
                        break;
                }
            }
        };
        registerReceiver(br,inf);*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showForegroundNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopFlashlight();
        handler.removeCallbacks(blinkRunnable);
        unregisterReceiver(incomingCallReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showForegroundNotification() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Flashlight Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Flashlight Service")
                        .setContentText("Flashlight service is running")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Notification notification = notificationBuilder.build();
        startForeground(NOTIFICATION_ID, notification);
    }

    private void turnFlashlightOn() {
        if (!isFlashlightOn) {
            try {
                cameraManager.setTorchMode(cameraId, true);
                isFlashlightOn = true;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void turnFlashlightOff() {
        if (isFlashlightOn) {
            try {
                cameraManager.setTorchMode(cameraId, false);
                isFlashlightOn = false;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopFlashlight() {
        turnFlashlightOff();
        stopForeground(true);
    }


    private void startBlinking() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!handler.hasCallbacks(blinkRunnable)) {
                handler.post(blinkRunnable);
            }
        }
    }

    private void stopBlinking() {
        handler.removeCallbacks(blinkRunnable);
        // Make sure the flashlight is turned off when the blinking stops
        if (isFlashlightOn) {
            turnFlashlightOff();
        }
    }

    private boolean isSpecificSms(String senderPhoneNumber, String messageBody) {
        String specificSender = "1234567890";
        String specificKeyword = "FLASHLIGHT_ON";
        return senderPhoneNumber.equals(specificSender) && messageBody.contains(specificKeyword);
    }

    private void handleIncomingSms(Context context, String senderPhoneNumber, String messageBody) {
        if (isSpecificSms(senderPhoneNumber, messageBody)) {
            Intent intent = new Intent("com.flashscreen.incomingcall.torchlight.SMS_RECEIVED");
            intent.putExtra("senderPhoneNumber", senderPhoneNumber);
            intent.putExtra("messageBody", messageBody);
            context.sendBroadcast(intent);
            blinkFlashlightTwice();
        }
    }

    private static final int TOTAL_BLINKS = 4;
    private int currentBlinkCycle = 0;

    private void blinkFlashlightTwice() {
        if (currentBlinkCycle < TOTAL_BLINKS) {
            blinkFlash();
            handler.postDelayed(this::blinkFlash, BLINK_DURATION * 2);
            currentBlinkCycle++;
        } else {
            currentBlinkCycle = 0;
            stopBlinking();
        }
    }

    private void blinkFlash() {
        if (isFlashlightOn) {
            turnFlashlightOff();
        } else {
            turnFlashlightOn();
        }
    }


}
