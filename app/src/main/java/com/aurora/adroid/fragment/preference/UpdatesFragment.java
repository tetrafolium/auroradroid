/*
 * Aurora Droid
 * Copyright (C) 2019, Rahul Kumar Patel <whyorean@gmail.com>
 *
 * Aurora Droid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aurora Droid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aurora Droid.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aurora.adroid.fragment.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.aurora.adroid.Constants;
import com.aurora.adroid.R;
import com.aurora.adroid.receiver.UpdatesReceiver;
import com.aurora.adroid.util.PrefUtil;
import com.aurora.adroid.util.Util;

public class UpdatesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(Constants.SHARED_PREFERENCES_KEY);
        setPreferencesFromResource(R.xml.preferences_updates, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences mPrefs = Util.getPrefs(context);
        mPrefs.registerOnSharedPreferenceChangeListener(this);

        ListPreference updatesIntervalList = findPreference(Constants.PREFERENCE_UPDATES_INTERVAL);
        assert updatesIntervalList != null;
        updatesIntervalList.setOnPreferenceChangeListener((preference, newValue) -> {
            String value = newValue.toString();
            int interval = Util.parseInt(value, 0);
            PrefUtil.putString(context, Constants.PREFERENCE_UPDATES_INTERVAL, value);
            UpdatesReceiver.setUpdatesInterval(context, interval);
            return true;
        });

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
