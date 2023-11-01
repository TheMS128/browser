package de.baumann.browser.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceManager;

import java.util.Objects;

import de.baumann.browser.R;
import de.baumann.browser.preferences.BasePreferenceFragment;
import de.baumann.browser.view.NinjaToast;

public class Fragment_settings_General extends BasePreferenceFragment  implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState,String rootKey) {
        setPreferencesFromResource(R.xml.preference_general, rootKey);
        updatePrefSummary();
    }

    private void updatePrefSummary() {
        Context context = getContext();
        assert context != null;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean customSE = sp.getBoolean("searchEngineSwitch", false);

        ListPreference searchEngines;
        searchEngines= (ListPreference)findPreference("sp_search_engine");
        assert searchEngines != null;
        String customSearchEngine = sp.getString("sp_search_engine_custom", "");
        String text = getString(R.string.setting_title_searchEngine) + ": " + getString(R.string.toast_input_empty);

        if(customSE) {
            searchEngines.setEnabled(false);
            if (customSearchEngine.equals("")) {
                NinjaToast.show(context, text);
            }
        } else {
            searchEngines.setEnabled(true);
        }
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sp, String key) {
        updatePrefSummary();
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).unregisterOnSharedPreferenceChangeListener(this);
    }
}
