package proyecto.laboratorio.memoriacamilogarritano;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GrillaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grilla);

        LinearLayout l = (LinearLayout) findViewById(R.id.sublinear);
        LinearLayout l1 = (LinearLayout) findViewById(R.id.sublinear2);
        LinearLayout l2 = (LinearLayout) findViewById(R.id.sublinear3);
        Recurso[] recursos = {
                Recurso.MATRA, Recurso.ARRIADOR, Recurso.CABEZADA, Recurso.BOZAL,Recurso.BAJO_MONTURA,
                Recurso.CABALLO, Recurso.CASCO, Recurso.CASCOS, Recurso.CEPILLO, Recurso.CINCHON_DE_VOLTEO,
                Recurso.COLA,Recurso.CRINES,Recurso.CUERDA,Recurso.ESCARBA_VASOS,Recurso.FUSTA,Recurso.MONTURA,
                Recurso.MONTURIN,Recurso.OJOS,Recurso.OREJAS,Recurso.PALOS,Recurso.PASTO,Recurso.PELOTA,
                Recurso.RASQUETA, Recurso.RIENDAS, Recurso.AROS, Recurso.ZANAHORIA
        };

        int i=0;
        for (Recurso imagen :recursos) {
            View iv = this.getImageView(imagen.getImagen());
            iv.setOnLongClickListener(new AdapterView.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    view.setBackgroundColor(Color.rgb(28,17,188));
                    Toast.makeText(GrillaActivity.this, "Acaba de presionar la imagen", Toast.LENGTH_SHORT).show();
                    return true;
                }

            });
            i++;
            if (i < recursos.length/3) {
            l.addView(iv); }
            else if ((i > recursos.length/3) && (i <= recursos.length * 0.6)) {
            l1.addView(iv); Log.v("borraresto", String.valueOf(i));}
            else {
            l2.addView(iv); }
        }

    }


    private View getImageView(int imageId){
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new HorizontalScrollView.LayoutParams(250, 250));
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new HorizontalScrollView.LayoutParams(220, 220));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(1, 1, 1, 1);
        imageView.setImageResource(imageId);
        imageView.setTag(imageId);
        layout.addView(imageView);
        return layout;
    }

}
