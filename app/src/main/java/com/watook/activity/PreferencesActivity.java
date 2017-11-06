package com.watook.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.watook.R;
import com.watook.fragment.PreferencesFragment;
import com.watook.fragment.ProfileFragment;

public class PreferencesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        setUpToolBar();
        getSupportActionBar().setTitle("Preferences");
        setFragment();
    }

    private void setFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame_layout_preferences, PreferencesFragment.newInstance()).commit();
    }
}
