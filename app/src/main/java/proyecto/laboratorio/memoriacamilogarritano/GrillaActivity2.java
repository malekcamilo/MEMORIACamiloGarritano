package proyecto.laboratorio.memoriacamilogarritano;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class GrillaActivity2 extends Activity {

    Set<String> imagenesSeleccionadas = new HashSet<>();
    @Override

        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grilla2);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (imagenesSeleccionadas.contains(v.getTag().toString())) {
                    Toast.makeText(GrillaActivity2.this, "Imagen Inhabilitada", Toast.LENGTH_SHORT).show();
                    imagenesSeleccionadas.remove(v.getTag().toString());
                    v.setBackgroundColor(Color.TRANSPARENT);
                }
                else {
                    Toast.makeText(GrillaActivity2.this, "Imagen Habilitada", Toast.LENGTH_SHORT).show();
                    imagenesSeleccionadas.add(v.getTag().toString());
                    v.setBackgroundColor(Color.rgb(28,17,188));
                }

            }

        });
        this.guardarPref();

    }
    private void guardarPref() {
        SharedPreferences settings = getSharedPreferences("imagenesSeleccionadas", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("imagenesSeleccionadas");
        editor.apply();
        editor.putStringSet("imagenesSeleccionadas", imagenesSeleccionadas);
        editor.apply();
    }


}
