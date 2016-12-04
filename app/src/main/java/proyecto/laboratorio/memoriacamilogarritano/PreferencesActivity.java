package proyecto.laboratorio.memoriacamilogarritano;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;

/**
 * Created by malek on 04/12/16.
 */

public class PreferencesActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
    }
}
