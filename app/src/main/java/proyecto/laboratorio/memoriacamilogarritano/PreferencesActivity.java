package proyecto.laboratorio.memoriacamilogarritano;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

/**
 * Created by malek on 04/12/16.
 */

public class PreferencesActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //PreferenceManager.setDefaultValues(this, R.xml.settings, false);
/*
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
*/

    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.settings_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }

    public static class PreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            //Elegir imágenes
            /*Preference em = (Preference) findPreference("lista_imagenes");
            em.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent();
                    intent.setType("drawable");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    int PICK_IMAGE = 1;
                    startActivityForResult(Intent.createChooser(intent, "Elegir Imágenes"), PICK_IMAGE);
                    return true;
                }
            });*/
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            //Por ahora porque son listas le puse s% en el summary
            /* Preference pref = findPreference(key);
            if (pref instanceof ListPreference) {
                ListPreference listPref = (ListPreference) pref;
                pref.setSummary(listPref.getEntry());
            }*/
        }

        @Override
        public void onResume() {
            super.onResume();
            // Registrar escucha
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            // Eliminar registro de la escucha
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
    }


}
