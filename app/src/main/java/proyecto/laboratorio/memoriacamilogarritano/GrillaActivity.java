package proyecto.laboratorio.memoriacamilogarritano;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class GrillaActivity extends Activity {


    static Set<String> imagenesSeleccionadas = new HashSet<>();

    @Override

        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grilla);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        final ImageAdapter c = (ImageAdapter) gridview.getAdapter();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.v("tag",v.getTag().toString());
                if (imagenesSeleccionadas.contains(v.getTag().toString()))   {
                    if (imagenesSeleccionadas.size()>1) {
                        Toast.makeText(GrillaActivity.this, "Imagen Inhabilitada", Toast.LENGTH_SHORT).show();
                        imagenesSeleccionadas.remove(v.getTag().toString());
                        v.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
                else {
                    Toast.makeText(GrillaActivity.this, "Imagen Habilitada", Toast.LENGTH_SHORT).show();
                    imagenesSeleccionadas.add(v.getTag().toString());
                    v.setBackgroundColor(c.getColorMarcado());
                }

            }

        });

    }


    protected void onPause() {
        super.onPause();
        this.guardarPref();
    }


    void guardarPref() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("imagenesSeleccionadas", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("imagenesSeleccionadas");
        editor.apply();
        editor.putStringSet("imagenesSeleccionadas", imagenesSeleccionadas);
        editor.apply();
        Log.v("guardado el valor",String.valueOf(imagenesSeleccionadas.size()));
        editor.commit();

        //Guardar cantidad
        SharedPreferences settings2 = getApplicationContext().getSharedPreferences("cant_img", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = settings2.edit();
        editor2.putInt("cant_img", imagenesSeleccionadas.size());
        editor2.apply();
        editor2.commit();


    }




}
