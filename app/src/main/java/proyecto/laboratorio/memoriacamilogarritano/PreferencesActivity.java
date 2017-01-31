package proyecto.laboratorio.memoriacamilogarritano;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            // Preference em = findPreference("lista_imagenes");
            Preference em = findPreference(getString(R.string.key_lista_imagenes));
            em.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    cargarRecursosUsados();
                    return false;
                }
            });
            this.setearSummaryImagenes();
        }

        private Set<String> cargarRecursosUsados() {
            SharedPreferences settings = this.getActivity().getSharedPreferences("imagenesSeleccionadas", MODE_PRIVATE);
            Set<String> r =  settings.getStringSet("imagenesSeleccionadas", new HashSet<String>());
            GrillaActivity.imagenesSeleccionadas = r;
            return r;
        }

        private void setearSummaryImagenes(){
            //Setear summary

            SharedPreferences settings = this.getActivity().getSharedPreferences("cant_img", MODE_PRIVATE);
            int cant =  settings.getInt("cant_img", 26);

            Preference pref = findPreference(getString(R.string.key_lista_imagenes));
            pref.setSummary(cant+" / 26");
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        }

        @Override
        public void onResume() {
            super.onResume();
            this.setearSummaryImagenes();
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
