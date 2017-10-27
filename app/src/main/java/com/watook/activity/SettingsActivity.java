package com.watook.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.watook.R;
import com.watook.fragment.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setUpToolBar();
        setFragment();

    }

    private void setFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame_layout_settings, SettingsFragment.newInstance()).commit();
    }
}
