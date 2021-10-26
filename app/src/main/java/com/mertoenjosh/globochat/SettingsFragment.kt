package com.mertoenjosh.globochat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.*


class SettingsFragment : PreferenceFragmentCompat() {

    private val tag1 = this::class.simpleName

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val dataStore = DataStore()
        /*
        // enables datastore for the entire hierarchy - disables sharedPreferences

        preferenceManager.preferenceDataStore = dataStore
        */

        val accSettingsPref =findPreference<Preference>(getString(R.string.key_account_settings))

        accSettingsPref?.setOnPreferenceClickListener {
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_frag) as NavHostFragment
            val navController = navHostFragment.navController
            val action = SettingsFragmentDirections.actionSettingsToAccSettings()
            navController.navigate(action)


           true
        }

        // Read preference values in a Fragment

        // 1: Get reference to the shared preference
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        // 2: Get the 'value' using the 'key'
        val autoReplyTime = sharedPreferences.getString(getString(R.string.key_auto_reply_time), "")
        Log.i(tag1, "Auto Reply Time: $autoReplyTime")

        val autoDownload = sharedPreferences.getBoolean(getString(R.string.key_auto_download), false)
        Log.i(tag1, "Auto Download: $autoDownload")

        val statusPref = findPreference<EditTextPreference>(getString(R.string.key_status))
        statusPref?.setOnPreferenceChangeListener { preference, newValue ->
            // executed before the value has changed
            Log.i(tag1, "New status: $newValue")

            val newStatus = newValue as String

            if (newStatus.contains("bad")) {
                Toast.makeText(context, "Inappropriate choice of state", Toast.LENGTH_SHORT).show()
                false
            } else {
                true    // accept the new value
            }

        }

//        statusPref?.setOnPreferenceChangeListener(object : Preference.OnPreferenceChangeListener {
//            override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
//                Log.i(tag1, "New status: $newValue")
//                return true    // accept the new value
//            }
//
//        })

        val notificationPref = findPreference<SwitchPreferenceCompat>(getString(R.string.key_new_msg_notif))
        notificationPref?.summaryProvider = Preference.SummaryProvider<SwitchPreferenceCompat> { switchPref ->

            if (switchPref?.isChecked!!)
                "Status: ON"
            else
                "Status: OFF"
        }

        notificationPref?.preferenceDataStore = dataStore
        val isNotifEnabled = dataStore.getBoolean("key_new_msg_notif", false)
    }

    class DataStore : PreferenceDataStore() {
        private val tag = this::class.simpleName

        // override methods only as per your need

        // After overriding remove the super call

        override fun getBoolean(key: String?, defValue: Boolean): Boolean {
            if (key == "key_new_msg_notif") {
                // retrieve value from db

                Log.i(tag, "getBoolean executed for $key")
            }

            return defValue
        }

        override fun putBoolean(key: String?, value: Boolean) {

            if (key == "key_new_msg_notif") {
                // save value to cloud or a db

                Log.i(tag, "putBoolean executed for $key with new value: $value")
            }
        }
    }

}