package com.flashscreen.incomingcall.torchlight.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.flashscreen.incomingcall.torchlight.R;

public class BaseActivity extends AppCompatActivity {
    protected void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

}
