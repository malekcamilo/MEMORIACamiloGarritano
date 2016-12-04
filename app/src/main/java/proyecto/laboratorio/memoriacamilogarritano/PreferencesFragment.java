package proyecto.laboratorio.memoriacamilogarritano;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by malek on 04/12/16.
 */

public class PreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
