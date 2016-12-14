package proyecto.laboratorio.memoriacamilogarritano;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class JuegoActivity extends Activity {
    private static MediaPlayer mPlayer;
    private Recurso[] recursos = {
            Recurso.MATRA, Recurso.ARRIADOR, Recurso.CABEZADA, Recurso.BOZAL,Recurso.BAJO_MONTURA,
            Recurso.CABALLO, Recurso.CASCO, Recurso.CASCOS, Recurso.CEPILLO, Recurso.CINCHON_DE_VOLTEO,
            Recurso.COLA,Recurso.CRINES,Recurso.CUERDA,Recurso.ESCARBA_VASOS,Recurso.FUSTA,Recurso.MONTURA,
            Recurso.MONTURIN,Recurso.OJOS,Recurso.OREJAS,Recurso.PALOS,Recurso.PASTO,Recurso.PELOTA,
            Recurso.RASQUETA, Recurso.RIENDAS, Recurso.AROS, Recurso.ZANAHORIA
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayer = new MediaPlayer();
        setContentView(R.layout.activity_juego);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutRespuestas);
        ArrayList<ImageView> imageViews = null;

        //CANTIDAD_FIGURAS_MOSTRAR Y CANTIDAD_FIGURAS_SELECCIONADAS hardcodeados por ahora
        int CANTIDAD_FIGURAS_MOSTRAR = 4;
        Integer CANTIDAD_FIGURAS_SELECCIONADAS = 26;

        imageViews = this.cargarFiguras(CANTIDAD_FIGURAS_MOSTRAR);
        //posicionCorrecta tiene la posicion de la imagen correcta del 0 al (CANTIDAD_FIGURAS_MOSTRAR - 1)
        int posicionCorrecta = this.getRandomInteger(CANTIDAD_FIGURAS_MOSTRAR-1);

        //0 porque tomamos la 1er imagen como la correcta y la cual se mostrara el texto
        Collections.swap(imageViews,0,posicionCorrecta);
        Log.v("Posicion",String.valueOf(posicionCorrecta) + ": " + ((Recurso) imageViews.get(posicionCorrecta).getTag()).getDescripcion());

        Respuesta respuesta = this.cargarRespuesta((Recurso) imageViews.get(posicionCorrecta).getTag());

        for (ImageView imagenView :imageViews) {
            //Agrego las imageviews al layout
            layout.addView(imagenView);
        }

        this.eventoSonidosOpciones(imageViews,posicionCorrecta);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        TextView text_nivel= (TextView) findViewById(R.id.textViewDificultad);
        text_nivel.setText("Dificultad: " + this.getDificultadStr(prefs.getString("dificultad", "4")));

        ((TextView) findViewById(R.id.textViewProgreso)).setText("1/"+ CANTIDAD_FIGURAS_SELECCIONADAS.toString());

    }


    private Respuesta cargarRespuesta(Recurso image) {
        //cargar preferencia voz femenina o masculina. VOZ_TIPO esta hardcodeado por ahora
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String voz = prefs.getString("voz", "F");
        String VOZ_TIPO = "F";
        String texto = image.getDescripcion();
        String sonidoPath;
        if (VOZ_TIPO.equals(voz)) {
            sonidoPath = image.getFemenina();
        }
        else {
            sonidoPath = image.getMasculina();
        }

        Respuesta respuesta = new Respuesta(image.getImagen(),texto,sonidoPath);
        this.setearNuevaRespuesta(respuesta);
        return respuesta;
    }


    private void setearNuevaRespuesta(Respuesta respuesta) {
        TextView textViewRta = (TextView) findViewById(R.id.textView);
        textViewRta.setText(respuesta.getTexto());
        this.eventoSonidoRespuesta(respuesta);
    }

    private ArrayList<ImageView> cargarFiguras(int cantidad_figuras_mostrar) {
        ArrayList<ImageView> imagesAgregar = new ArrayList<>();
        ArrayList<Integer> listadoIndicesImagenes = this.obtenerIndicesAlAzar(recursos.length,cantidad_figuras_mostrar);
        for (Integer imagenIndice :listadoIndicesImagenes) {
            imagesAgregar.add((ImageView) this.getImageView(recursos[imagenIndice]));
        }
        return imagesAgregar;
    }

    private ArrayList<Integer> obtenerIndicesAlAzar(int len, int cantidad) {
        ArrayList<Integer> l = new ArrayList<Integer>();
        int i=0;
        while(i<cantidad) {
            Integer tmp = this.getRandomInteger(len-1);
            if (!l.contains(tmp)) {
                l.add(tmp);
                i++;
            }

        }
        return l;
    }


    private View getImageView(Recurso imageId){
        //LinearLayout layout = new LinearLayout(getApplicationContext());
        //layout.setLayoutParams(new HorizontalScrollView.LayoutParams(250, 250));
        //layout.setGravity(Gravity.CENTER);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(imageId.getImagen());
        imageView.setTag(imageId);

        imageView.setPadding(8, 8, 8, 8);
        imageView.setBackgroundColor(Color.GRAY);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,0);
        layoutParams.width  = (int) (width * 0.25);
        layoutParams.height  = layoutParams.width;

        Log.v("widthImagen", String.valueOf(layoutParams.width));
        imageView.setLayoutParams(layoutParams);

        //layout.addView(imageView);
        return imageView;
    }


    private void eventoSonidoRespuesta(final Respuesta respuesta) {
        ImageView img = (ImageView) findViewById(R.id.imageViewParlante);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssetFileDescriptor descriptor = null;
                try {
                    descriptor = getAssets().openFd(respuesta.getSonidoPath());
                    JuegoActivity.reproducirSonido(descriptor);
                    descriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void eventoSonidosOpciones(ArrayList<ImageView> listadoOpciones, int posicionCorrecta) {

        View.OnClickListener sonidoIncorrecto  = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                    AssetFileDescriptor descriptor = getAssets().openFd("Caballo/resoplido.m4a");
                    JuegoActivity.reproducirSonido(descriptor);
                    view.setBackgroundColor(Color.RED);
                    view.postDelayed(new Runnable() {
                        public void run() {
                            view.setBackgroundColor(Color.GRAY);
                        }
                    }, 1000);
                    descriptor.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        for (int j=0;j<listadoOpciones.size();j++) {
            ImageView img1 = listadoOpciones.get(j);
            img1.setOnClickListener(sonidoIncorrecto);
        }

        final ImageView img1 = listadoOpciones.get(posicionCorrecta);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    AssetFileDescriptor descriptor = getAssets().openFd("Caballo/relincho.m4a");
                    JuegoActivity.reproducirSonido(descriptor);
                    view.setBackgroundColor(Color.rgb(0,173,56));
                    descriptor.close();
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

    private String getDificultadStr(String dificultad) {
        switch (dificultad) {
            case "1":
                return "Inicial";
            case "2":
                return "Medio";
            case "3":
                return "Avanzado";
            case "4":
                return "Experto";
            default: return "Desconocido";
        }
    }
}
