package com.watook.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
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
            setKmBackground();
            if (!clicked)
                setKm();
            txtDistance.setText(Utils.getDistanceBasedOnUnit(pref.getDistanceRange()));
            distanceBar.setMinStartValue(pref.getDistanceRange()).apply();
        } else {
            setMilesBackground();
            if (!clicked)
                setMiles();
            txtDistance.setText(Utils.getDistanceBasedOnUnit(pref.getDistanceRange()));
            distanceBar.setMinStartValue(pref.getDistanceRange()).apply();
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
        ageSeekBar.setMinStartValue(pref.getAgeMin()).setMaxStartValue(pref.getAgeMax()).apply();

    }

    private void inItViews(final View view) {
        switchMen = (Switch) view.findViewById(R.id.switch_men);
        switchMen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchMen.isChecked()) {
                    switchMen.setChecked(true);
                    setGenderInterestMale(true);
                } else {
                    switchWomen.setChecked(true);
                    setGenderInterestFemale(true);
                    setGenderInterestMale(false);


                }
            }
        });
        switchWomen = (Switch) view.findViewById(R.id.switch_women);
        switchWomen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchWomen.isChecked()) {
                    switchWomen.setChecked(true);
                    setGenderInterestFemale(true);
                } else {
                    switchMen.setChecked(true);
                    setGenderInterestMale(true);
                    setGenderInterestFemale(false);
                }
            }
        });

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
                txtDistance.setText(Utils.getDistanceBasedOnUnit(value.intValue()));
            }
        });
        distanceBar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                setDistance(value.intValue());
            }
        });


        ageSeekBar = (CrystalRangeSeekbar) view.findViewById(R.id.age_bar);
        ageSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                txtAgeRange.setText(minValue + "-" + maxValue);
            }
        });
        ageSeekBar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                setAge(minValue.intValue(), maxValue.intValue());
            }
        });

        switchDiscoverable = (Switch) view.findViewById(R.id.switch_discoverable);
        switchDiscoverable.setChecked(true);

    }

    private void setGenderInterestMale(boolean male) {
        Preferences pref = DatabaseManager.getInstance(activity).getPreferences();
        pref.setMaleInterest(male);
        DatabaseManager.getInstance(activity).insertPreferences(pref);
    }

    private void setGenderInterestFemale(boolean female) {
        Preferences pref = DatabaseManager.getInstance(activity).getPreferences();
        pref.setFemaleInterest(female);
        DatabaseManager.getInstance(activity).insertPreferences(pref);
    }


    private void setDistance(int dist) {
        Preferences pref = DatabaseManager.getInstance(activity).getPreferences();
        pref.setDistanceRange(dist);
        DatabaseManager.getInstance(activity).insertPreferences(pref);
    }

    private void setAge(int min, int max) {
        Preferences pref = DatabaseManager.getInstance(activity).getPreferences();
        pref.setAgeMin(min);
        pref.setAgeMax(max);
        DatabaseManager.getInstance(activity).insertPreferences(pref);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_km:
                setKm();
                setKmBackground();
                break;
            case R.id.btn_miles:
                setMiles();
                setMilesBackground();
                break;
        }
    }

    private void setMiles() {
        Preferences pref = DatabaseManager.getInstance(activity).getPreferences();
        pref.setDistanceUnitKm(false);
        DatabaseManager.getInstance(activity).insertPreferences(pref);
        try {
            MySharedPreferences.putObject(Constant.DISTANCE_UNIT_KM, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        clicked = true;
        bindViews();
    }

    private void setMilesBackground() {
        layMiles.setBackground(getResources().getDrawable(R.drawable.rounded_rect_bg_accent));
        layKm.setBackground(getResources().getDrawable(R.drawable.rounded_rect_bg_accent_empty));
        btnKm.setTextColor(getResources().getColor(R.color.colorTextPrimaryDark));
        btnMiles.setTextColor(getResources().getColor(R.color.colorTextWhite));
    }

    private void setKm() {
        Preferences pref = DatabaseManager.getInstance(activity).getPreferences();
        pref.setDistanceUnitKm(true);
        DatabaseManager.getInstance(activity).insertPreferences(pref);
        try {
            MySharedPreferences.putObject(Constant.DISTANCE_UNIT_KM, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        clicked = true;
        bindViews();
    }

    private void setKmBackground() {
        layKm.setBackground(getResources().getDrawable(R.drawable.rounded_rect_bg_accent));
        layMiles.setBackground(getResources().getDrawable(R.drawable.rounded_rect_bg_accent_empty));
        btnMiles.setTextColor(getResources().getColor(R.color.colorTextPrimaryDark));
        btnKm.setTextColor(getResources().getColor(R.color.colorTextWhite));
    }
}
