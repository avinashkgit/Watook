package com.watook.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.watook.application.MyApplication;
import com.watook.application.MySharedPreferences;
import com.watook.manager.ApiManager;
import com.watook.manager.DatabaseManager;
import com.watook.model.Preferences;
import com.watook.model.response.PreferencesSaveResponse;
import com.watook.util.Constant;
import com.watook.util.Keys;
import com.watook.util.Utils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreferencesFragment extends Fragment implements View.OnClickListener {
    String TAG = PreferencesFragment.class.getSimpleName();
    Preferences pref;
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
        pref = DatabaseManager.getInstance(activity).getPreferences();
        bindViews();

    }

    private void bindViews() {
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
        pref.setMaleInterest(male);
        apiCallSavePreferences();

    }

    private void setGenderInterestFemale(boolean female) {
        pref.setFemaleInterest(female);
        apiCallSavePreferences();
        apiCallSavePreferences();
    }


    private void setDistance(int dist) {
        pref.setDistanceRange(dist);
        apiCallSavePreferences();
    }

    private void setAge(int min, int max) {
        pref.setAgeMin(min);
        pref.setAgeMax(max);
        apiCallSavePreferences();
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
        pref.setDistanceUnitKm(false);
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

    @Override
    public void onPause() {
        super.onPause();
    }

    private void apiCallSavePreferences() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", MyApplication.getInstance().getUserId());
        map.put("distanceRange", pref.getDistanceRange() * 1000 + "");
        map.put("distanceIn", MyApplication.getDistanceCode().get(Constant.METER) + "");
        map.put("ageMin", pref.getAgeMin() + "");
        map.put("ageMax", pref.getAgeMax() + "");
        if (pref.isFemaleInterest())
            map.put("femaleInterest", MyApplication.getGenderCode().get(Constant.FEMALE) + "");
        else
            map.put("femaleInterest", 0 + "");
        if (pref.isMaleInterest())
            map.put("maleInterest", MyApplication.getGenderCode().get(Constant.MALE) + "");
        else
            map.put("maleInterest", 0 + "");

        Log.d(TAG, map.toString());


        Call<PreferencesSaveResponse> saveProfile = ApiManager.getApiInstance().setPreferences(Constant.CONTENT_TYPE,
                DatabaseManager.getInstance(activity).getRegistrationData().getData(), map);
        saveProfile.enqueue(new Callback<PreferencesSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<PreferencesSaveResponse> call, @NonNull Response<PreferencesSaveResponse> response) {
                int statusCode = response.code();
                PreferencesSaveResponse saveResponse = response.body();
                if (statusCode == 200 && saveResponse != null && saveResponse.getStatus() != null && saveResponse.getStatus().equalsIgnoreCase("success")) {
                    DatabaseManager.getInstance(activity).insertPreferences(pref);
                    try {
                        MySharedPreferences.putObject(Constant.IS_PREFERENCES_CHANGED, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    activity.showAToast(getResources().getString(R.string.oops_something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PreferencesSaveResponse> call, @NonNull Throwable t) {
                activity.showAToast(getResources().getString(R.string.oops_server_response_failure));
            }
        });

    }
}
