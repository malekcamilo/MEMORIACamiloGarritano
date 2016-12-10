package proyecto.laboratorio.memoriacamilogarritano;

import android.app.Activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.BoringLayout;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class JuegoActivity extends Activity {
    private static MediaPlayer mPlayer;
    private Integer[] images = {
            R.drawable.matra, R.drawable.arriador, R.drawable.cabezada, R.drawable.bozal,R.drawable.bajomontura,
            R.drawable.caballo, R.drawable.casco,R.drawable.cascos,R.drawable.cepillo,R.drawable.chinchondevolteo,
            R.drawable.cola,R.drawable.crines,R.drawable.cuerda,R.drawable.escarbavasos,R.drawable.fusta,R.drawable.montura,
            R.drawable.monturin,R.drawable.ojo,R.drawable.orejas,R.drawable.palos,R.drawable.pasto,R.drawable.pelota,
            R.drawable.rasqueta,R.drawable.riendas,R.drawable.ros,R.drawable.zanahoria
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayer = new MediaPlayer();
        setContentView(R.layout.activity_juego);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutRespuestas);
        ArrayList<ImageView> imagesAgregar = null;

        //CANTIDAD_FIGURAS_MOSTRAR hardcodeado por ahora
        int CANTIDAD_FIGURAS_MOSTRAR = 4;

        imagesAgregar = this.cargarFiguras(CANTIDAD_FIGURAS_MOSTRAR);
        //posicionCorrecta tiene la posicion de la imagen correcta del 0 al (CANTIDAD_FIGURAS_MOSTRAR - 1)
        int posicionCorrecta = this.getRandomInteger(CANTIDAD_FIGURAS_MOSTRAR-1);

        // Aca cambiar texto y sonido de locutor/a

        //0 porque tomamos la 1er imagen como la correcta y la cual se mostrara el texto
        Collections.swap(imagesAgregar,0,posicionCorrecta);
        Log.v("Posicion",String.valueOf(posicionCorrecta));


        for (ImageView imagenView :imagesAgregar) {
            layout.addView(imagenView);
        }

        this.eventoSonidosOpciones(imagesAgregar,posicionCorrecta);
        this.eventoSonidoRespuesta();
    }

    private ArrayList<ImageView> cargarFiguras(int cantidad_figuras_mostrar) {
        ArrayList<ImageView> imagesAgregar = new ArrayList<>();
        ArrayList<Integer> listadoIndicesImagenes = this.obtenerIndicesAlAzar(images,cantidad_figuras_mostrar);
        for (Integer imagenIndice :listadoIndicesImagenes) {
            imagesAgregar.add((ImageView) this.getImageView(images[imagenIndice]));
        }
        return imagesAgregar;
    }

    private ArrayList<Integer> obtenerIndicesAlAzar(Integer[] images, int cantidad) {
        //CONTROLAR QUE NO HAYA REPETIDOS!
        ArrayList<Integer> l = new ArrayList<Integer>();
        for(int i=0;i<cantidad;i++) {
            l.add(this.getRandomInteger(images.length-1));
        }
        return l;
    }


    private View getImageView(int imageId){
        //LinearLayout layout = new LinearLayout(getApplicationContext());
        //layout.setLayoutParams(new HorizontalScrollView.LayoutParams(250, 250));
        //layout.setGravity(Gravity.CENTER);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics); // ((display.getWidth()*20)/100)
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;// ((display.getHeight()*30)/100)

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(imageId);
        imageView.setTag(imageId);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,0);
        layoutParams.width  = (int) (width * 0.20);
        layoutParams.height  = height;

        Log.v("widthImagen", String.valueOf(layoutParams.width));
        imageView.setLayoutParams(layoutParams);

        //layout.addView(imageView);
        return imageView;
    }

    private void eventoSonidoRespuesta() {
        ImageView img = (ImageView) findViewById(R.id.imageViewParlante);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssetFileDescriptor descriptor = null;
                try {
                    descriptor = getAssets().openFd("FEMENINAS/Matra Fem.m4a");
                    JuegoActivity.reproducirSonido(descriptor);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void eventoSonidosOpciones(ArrayList<ImageView> listadoOpciones, int posicionCorrecta) {


        View.OnClickListener sonidoIncorrecto  = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AssetFileDescriptor descriptor = getAssets().openFd("resoplido.m4a");
                    JuegoActivity.reproducirSonido(descriptor);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        for (int j=0;j<listadoOpciones.size();j++) {
            ImageView img1 = listadoOpciones.get(j);
            img1.setOnClickListener(sonidoIncorrecto);
        }


        ImageView img1 = listadoOpciones.get(posicionCorrecta);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    AssetFileDescriptor descriptor = getAssets().openFd("relincho.m4a");
                    JuegoActivity.reproducirSonido(descriptor);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    public static void reproducirSonido(AssetFileDescriptor fd) {
        try {
            AssetFileDescriptor descriptor = fd;
            mPlayer.reset();
            mPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mPlayer.prepare();
            mPlayer.start();
            descriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getRandomInteger(int maximo) {
        return (new Random()).nextInt(maximo+1);
    }


}
