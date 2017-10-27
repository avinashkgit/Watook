package com.watook.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.watook.R;
import com.watook.activity.LoginActivity;
import com.watook.activity.PreferencesActivity;
import com.watook.application.MySharedPreferences;
import com.watook.manager.DatabaseManager;
import com.watook.model.Preferences;
import com.watook.util.Constant;
import com.watook.util.Keys;
import com.watook.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreferencesFragment extends Fragment implements View.OnClickListener {
    PreferencesActivity activity;
    Switch switchDiscoverable;
    CrystalRangeSeekbar ageSeekBar;
    CrystalSeekbar distanceBar;
    TextView txtAgeRange, txtDistance;
    LinearLayout layKm, layMiles;
    Button btnKm, btnMiles;
    Switch switchMen;
    Switch switchWomen;
    boolean clicked = false;

    public PreferencesFragment() {
    }

    public static PreferencesFragment newInstance() {
        return new PreferencesFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preferences, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inItViews(view);
        bindViews();
    }

    private void bindViews() {
        Preferences pref = DatabaseManager.getInstance(activity).getPreferences();
        if (pref.isDistanceUnitKm()) {
            if (!clicked)
                setKm();
            txtDistance.setText(Utils.milesToKm(pref.getDistanceRange()) + " Km");
            distanceBar.setPosition(Utils.milesToKm(pref.getDistanceRange()));
        } else {
            if (!clicked)
                setMiles();
            txtDistance.setText(pref.getDistanceRange() + " Miles");
            distanceBar.setPosition(pref.getDistanceRange());
        }
        if (pref.isFemaleInterest()) {
            switchWomen.setChecked(true);
        }
        if (pref.isMaleInterest()) {
            switchMen.setChecked(true);
        }
        if (pref.isDiscoverable()) {
            switchDiscoverable.setChecked(true);
        }

    }

    private void inItViews(final View view) {
        switchMen = (Switch) view.findViewById(R.id.switch_men);
        switchWomen = (Switch) view.findViewById(R.id.switch_women);

        activity = (PreferencesActivity) getActivity();
        txtAgeRange = (TextView) view.findViewById(R.id.txt_age_range);
        txtDistance = (TextView) view.findViewById(R.id.txt_dist);

        layKm = (LinearLayout) view.findViewById(R.id.lay_km);
        layMiles = (LinearLayout) view.findViewById(R.id.lay_miles);

        btnKm = (Button) view.findViewById(R.id.btn_km);
        btnKm.setOnClickListener(this);

        btnMiles = (Button) view.findViewById(R.id.btn_miles);
        btnMiles.setOnClickListener(this);

        distanceBar = (CrystalSeekbar) view.findViewById(R.id.distance_bar);
        distanceBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                txtDistance.setText(Utils.milesToKm(value.intValue()) + " Km");
            }
        });


        ageSeekBar = (CrystalRangeSeekbar) view.findViewById(R.id.age_bar);
        ageSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                txtAgeRange.setText(minValue + "-" + maxValue);
            }
        });

        switchDiscoverable = (Switch) view.findViewById(R.id.switch_discoverable);
        switchDiscoverable.setChecked(true);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_km:
                setKm();
                break;
            case R.id.btn_miles:
                setMiles();
                break;
        }
    }

    private void setMiles() {
        layMiles.setBackground(getResources().getDrawable(R.drawable.rounded_rect_bg_accent));
        layKm.setBackground(getResources().getDrawable(R.drawable.rounded_rect_bg_accent_empty));
        btnKm.setTextColor(getResources().getColor(R.color.colorTextPrimaryDark));
        btnMiles.setTextColor(getResources().getColor(R.color.colorTextWhite));

        Preferences pref = DatabaseManager.getInstance(activity).getPreferences();
        pref.setDistanceUnitKm(false);
        DatabaseManager.getInstance(activity).insertPreferences(pref);

        clicked = true;
        bindViews();

    }

    private void setKm() {
        layKm.setBackground(getResources().getDrawable(R.drawable.rounded_rect_bg_accent));
        layMiles.setBackground(getResources().getDrawable(R.drawable.rounded_rect_bg_accent_empty));
        btnMiles.setTextColor(getResources().getColor(R.color.colorTextPrimaryDark));
        btnKm.setTextColor(getResources().getColor(R.color.colorTextWhite));

        Preferences pref = DatabaseManager.getInstance(activity).getPreferences();
        pref.setDistanceUnitKm(true);
        DatabaseManager.getInstance(activity).insertPreferences(pref);

        clicked = true;
        bindViews();
    }
}
