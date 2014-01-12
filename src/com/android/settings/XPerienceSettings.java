/*
* Copyright (C) 2014 The XPerience Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.settings;

import com.android.settings.CMDProcessor;
import com.android.settings.Helpers;
import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.backup.IBackupManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.VerifierDeviceIdentity;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.IWindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class XPerienceSettings extends PreferenceFragment {

    private static final String TAG = "XPerienceSettings";
    private static final String WRT = "Setting changed: ";
    private static final String UPD = "Setting updated: ";

    private static final boolean DEBUG = true;

        private static final String CENTER_CLOCK_STATUS_BAR_PROP = "pref_center_clock_status_bar";

        private CheckBoxPreference mCenterClockStatusBar;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
                
                addPreferencesFromResource(R.xml.xperiencesettings_prefs);

                PreferenceScreen PrefScreen = getPreferenceScreen();

        mCenterClockStatusBar = (CheckBoxPreference) findPreference(CENTER_CLOCK_STATUS_BAR_PROP);

    }

    @Override
    public void onResume() {
        super.onResume();

        final ContentResolver cr = getActivity().getContentResolver();

                updateCenterClockStatusBar();
    }

    /* Update functions */
    private void updateCenterClockStatusBar() {
        mCenterClockStatusBar.setChecked(Settings.System.getInt(getActivity().getContentResolver(), Settings.System.CENTER_CLOCK_STATUS_BAR, 0) == 1);
            if (DEBUG) Log.i(TAG, UPD + "CenterClock");
    }

    /* Write functions */
        private void writeCenterClockStatusBar() {
        Settings.System.putInt(getActivity().getContentResolver(), Settings.System.CENTER_CLOCK_STATUS_BAR, mCenterClockStatusBar.isChecked() ? 1 : 0);
                if (DEBUG) Log.i(TAG, WRT + "CenterClock");
        Helpers.restartSystemUI();
                if (DEBUG) Log.i(TAG, "Restarting SystemUI");
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        if (Utils.isMonkeyRunning()) {
            return false;
        }

        if (preference == mCenterClockStatusBar) {
            writeCenterClockStatusBar();
        }

        return false;
    }
}
