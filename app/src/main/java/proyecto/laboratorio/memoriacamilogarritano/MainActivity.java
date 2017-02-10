package proyecto.laboratorio.memoriacamilogarritano;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("cant_img", MODE_PRIVATE);
        int imagenes = prefs.getInt("cant_img", 0);
        if (imagenes == 0) {
            this.seleccionarTodas();
        }
        this.eventoBotonComenzar();
    }

    private void eventoBotonComenzar() {
        Button botonComenzar = (Button) findViewById(R.id.button_comenzar);
        botonComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JuegoActivity.nivel_maximo = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("dificultad","4"));
                Intent c = new Intent(MainActivity.this,JuegoActivity.class);
                startActivity(c);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preferences_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            Intent i = new Intent(this, PreferencesActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void seleccionarTodas() {

        for (Recurso r : JuegoActivity.recursos) {
            GrillaActivity.imagenesSeleccionadas.add(String.valueOf(r.getImagen()));
        }
        SharedPreferences settings = getApplicationContext().getSharedPreferences("imagenesSeleccionadas", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("imagenesSeleccionadas");
        editor.apply();
        editor.putStringSet("imagenesSeleccionadas", GrillaActivity.imagenesSeleccionadas);
        editor.apply();
        editor.commit();
    }
}
