
package com.roman.romcontrol.fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.CheckBoxPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;

import com.roman.romcontrol.R;
import com.roman.romcontrol.SettingsPreferenceFragment;

public class Led extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    public static final String TAG = "LEDPreferences";

    private static final String PREF_LED_OFF = "led_off";
    private static final String PREF_LED_ON = "led_on";
    private static final String PREF_LED_SCREEN_ON = "led_screen_on";
    private static final String PREF_USE_BLN = "use_bln";

    ListPreference mLedOffTime;
    ListPreference mLedOnTime;
    CheckBoxPreference mLedScreenOn;
    CheckBoxPreference mUseBln;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs_led);

        mLedOffTime = (ListPreference) findPreference(PREF_LED_OFF);
        mLedOffTime.setOnPreferenceChangeListener(this);
        String ledOffTime = Settings.System
                .getInt(getActivity().getContentResolver(),
                        Settings.System.NOTIFICATION_LIGHT_OFF,
                        getActivity()
                                .getResources()
                                .getInteger(
                                        com.android.internal.R.integer.config_defaultNotificationLedOff))
                + "";
        mLedOffTime.setValue(ledOffTime);
        Log.i(TAG, "led off time set to: " + ledOffTime);

        mLedOnTime = (ListPreference) findPreference(PREF_LED_ON);
        mLedOnTime.setOnPreferenceChangeListener(this);
        String ledOnTime = Settings.System
                .getInt(getActivity().getContentResolver(),
                        Settings.System.NOTIFICATION_LIGHT_ON,
                        getActivity().getResources().getInteger(
                                com.android.internal.R.integer.config_defaultNotificationLedOn))
                + "";
        mLedOnTime.setValue(ledOnTime);
        Log.i(TAG, "led on time set to: " + ledOnTime);

				mLedScreenOn = (CheckBoxPreference) findPreference(PREF_LED_SCREEN_ON);
				mLedScreenOn.setChecked(Settings.Secure.getInt(getActivity().getContentResolver(),
					Settings.Secure.LED_SCREEN_ON, 0) == 1);
		mUseBln = (CheckBoxPreference) findPreference(PREF_USE_BLN);
		mUseBln.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.NOTIFICATION_USE_BUTTON_BACKLIGHT, 0) == 1);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
				if (preference == mLedScreenOn) {
						boolean checked = ((CheckBoxPreference) preference).isChecked();
						Settings.Secure.putInt(getActivity().getContentResolver(),
							Settings.Secure.LED_SCREEN_ON, checked ? 1 : 0);
						Log.i(TAG, "LED flash when screen ON is set to: " + checked);
						return true;
				} else if (preference == mUseBln) {
                        boolean checked = ((CheckBoxPreference) preference).isChecked();
                        Settings.System.putInt(getActivity().getContentResolver(),
                            Settings.System.NOTIFICATION_USE_BUTTON_BACKLIGHT, checked ? 1 : 0);
                }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean result = false;

        if (preference == mLedOffTime) {

            int val = Integer.parseInt((String) newValue);
            Log.i(TAG, "led off time new value: " + val);
            result = Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NOTIFICATION_LIGHT_OFF, val);

        } else if (preference == mLedOnTime) {

            int val = Integer.parseInt((String) newValue);
            Log.i(TAG, "led on time new value: " + val);
            result = Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NOTIFICATION_LIGHT_ON, val);
        }

        return result;
    }

}
