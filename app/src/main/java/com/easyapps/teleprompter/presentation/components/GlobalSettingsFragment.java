package com.easyapps.teleprompter.presentation.components;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.View;
import android.widget.Toast;

import com.easyapps.teleprompter.R;
import com.easyapps.teleprompter.infrastructure.communication.bluetooth.BluetoothScreenShareServer;
import com.easyapps.teleprompter.presentation.helper.PresentationConstants;

import static android.app.Activity.RESULT_OK;
import static com.easyapps.teleprompter.R.xml.global_preferences;

/**
 * screen for global settings of the application
 * Created by daniel on 28/06/2017.
 */

public class GlobalSettingsFragment extends PreferenceFragment {

    private BluetoothScreenShareServer screenShareServer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(global_preferences);
        screenShareServer = new BluetoothScreenShareServer(BluetoothAdapter.getDefaultAdapter());

        PreferenceScreen preferenceScreen = this.getPreferenceScreen();

        PreferenceCategory pcBluetooth = new PreferenceCategory(preferenceScreen.getContext());
        pcBluetooth.setTitle(getResources().getString(R.string.pref_bluetooth));
        preferenceScreen.addPreference(pcBluetooth);

        final CheckBoxPreference cbpBluetoothEnabled =
                new CheckBoxPreference(preferenceScreen.getContext());
        cbpBluetoothEnabled.setChecked(screenShareServer.isProtocolEnabled());
        cbpBluetoothEnabled.setKey(getResources().getString(R.string.pref_isBluetoothEnabled));
        cbpBluetoothEnabled.setTitle(R.string.pref_isBluetoothEnabled);
        cbpBluetoothEnabled.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (cbpBluetoothEnabled.isChecked()){
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, PresentationConstants.REQUEST_ENABLE_BT);
                }
                else{
                    screenShareServer.disable();
                }
                return true;
            }
        });
        pcBluetooth.addPreference(cbpBluetoothEnabled);
    }

    public void clickOnBluetoothCheckbox(){
        PreferenceScreen preferenceScreen = this.getPreferenceScreen();
        CheckBoxPreference cbpBluetooth = (CheckBoxPreference)
                preferenceScreen.findPreference(
                        getResources().getString(R.string.pref_isBluetoothEnabled));

        if (cbpBluetooth != null){
            cbpBluetooth.setChecked(!cbpBluetooth.isChecked());
        }
    }
}