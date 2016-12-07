package proyecto.laboratorio.memoriacamilogarritano;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Created by malek on 04/12/16.
 */

public class PreferencesActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Leer preferenias
        //SharedPreferences prefs =getSharedPreferences("", MODE_PRIVATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Guardar preferencias
    }

    public static class PreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }
    }


}
