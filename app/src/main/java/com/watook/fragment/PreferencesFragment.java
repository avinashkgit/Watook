package com.watook.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.watook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreferencesFragment extends Fragment {
    Switch switchDiscoverable;

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
        bindViews(view);
    }

    private void bindViews(final View view) {
    }

    private void inItViews(final View view) {
        switchDiscoverable = (Switch) view.findViewById(R.id.switch_discoverable);
        switchDiscoverable.setChecked(true);
        switchDiscoverable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    view.findViewById(R.id.card_feature_in).setVisibility(View.VISIBLE);
                else
                    view.findViewById(R.id.card_feature_in).setVisibility(View.GONE);
            }
        });
    }


}
