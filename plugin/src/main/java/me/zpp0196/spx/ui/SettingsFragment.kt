package me.zpp0196.spx.ui

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import me.zpp0196.spx.library.PrefsBridge
import me.zpp0196.spx.R

/**
 * @author zpp0196
 */
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val selectAppPreference =
            findPreference<AppSelectListPreference>(getString(R.string.pref_target_app_select_key))!!
        findPreference<Preference>(getString(R.string.pref_target_app_launch_key))?.let { pref ->
            pref.setOnPreferenceClickListener { _ ->
                selectAppPreference.getSelectIntent()?.let {
                    PrefsBridge.launchWithDefaultPrefs(requireContext(), it)
                } != null
            }
        }
    }
}