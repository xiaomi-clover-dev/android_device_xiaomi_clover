/* Copyright (C) 2026 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */
package org.lineageos.settings.clover.power;

import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.Toast;

import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import org.lineageos.settings.clover.R;

public class PowerProfileSettingsFragment extends PreferenceFragment {
    private static final String TAG = "CloverPowerProfiles";
    private static final String PROFILE_PROPERTY = "persist.sys.clover.power_profile";
    private ListPreference mProfilePreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.power_profile_settings);
        mProfilePreference = (ListPreference) findPreference("power_profile");
        mProfilePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String value = (String) newValue;
            if (mProfilePreference.findIndexOfValue(value) < 0) return false;
            try {
                SystemProperties.set(PROFILE_PROPERTY, value);
                mProfilePreference.setValue(value);
                updateSummary();
                return true;
            } catch (RuntimeException e) {
                Log.e(TAG, "Unable to change power profile", e);
                Toast.makeText(getActivity(), R.string.power_profile_error,
                        Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String value = SystemProperties.get(PROFILE_PROPERTY, "balanced");
        mProfilePreference.setValue(mProfilePreference.findIndexOfValue(value) < 0
                ? "balanced" : value);
        updateSummary();
    }

    private void updateSummary() {
        mProfilePreference.setSummary(mProfilePreference.getEntry());
    }
}
