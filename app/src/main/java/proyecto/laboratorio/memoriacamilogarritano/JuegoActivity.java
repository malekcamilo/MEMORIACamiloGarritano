package proyecto.laboratorio.memoriacamilogarritano;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class JuegoActivity extends Activity {
    private static MediaPlayer mPlayer;
    private Recurso[] recursos = {
            Recurso.MATRA, Recurso.ARRIADOR, Recurso.CABEZADA, Recurso.BOZAL,Recurso.BAJO_MONTURA,
            Recurso.CABALLO, Recurso.CASCO, Recurso.CASCOS, Recurso.CEPILLO, Recurso.CINCHON_DE_VOLTEO,
            Recurso.COLA,Recurso.CRINES,Recurso.CUERDA,Recurso.ESCARBA_VASOS,Recurso.FUSTA,Recurso.MONTURA,
            Recurso.MONTURIN,Recurso.OJOS,Recurso.OREJAS,Recurso.PALOS,Recurso.PASTO,Recurso.PELOTA,
            Recurso.RASQUETA, Recurso.RIENDAS, Recurso.AROS, Recurso.ZANAHORIA
    };

    private ArrayList<Recurso> recursosUsados;
    int CANTIDAD_FIGURAS_MOSTRAR;
    int CANTIDAD_FIGURAS_SELECCIONADAS;
    int dificultad;
    int CANTIDAD_RESPONDIDAS;
    private Set<Integer> recursosUsadosMarcados = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.leerConfiguraciones();
        this.inicializarJuego();
        mPlayer = new MediaPlayer();

    }

    private void leerConfiguraciones() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String dificultadStr = prefs.getString("dificultad", "4");
        dificultad = Integer.parseInt(dificultadStr);
    }


    private void inicializarJuego() {
        recursosUsadosMarcados.clear();
        recursosUsados = this.cargarRecursosUsados();
        CANTIDAD_FIGURAS_MOSTRAR = this.getDificultadCantidadImagenes(dificultad);
        CANTIDAD_FIGURAS_SELECCIONADAS = recursosUsados.size();

        CANTIDAD_RESPONDIDAS = 0;

        this.armarJuego(CANTIDAD_FIGURAS_MOSTRAR,CANTIDAD_RESPONDIDAS,CANTIDAD_FIGURAS_SELECCIONADAS);

    }


    public void armarJuego(int CANTIDAD_FIGURAS_MOSTRAR, int cantidadRespondidas,Integer CANTIDAD_FIGURAS_SELECCIONADAS) {

        setContentView(R.layout.activity_juego);

        TextView text_nivel= (TextView) findViewById(R.id.textViewDificultad);
        text_nivel.setText("Dificultad: " + this.getDificultadStr(dificultad));
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutRespuestas);

        CANTIDAD_FIGURAS_MOSTRAR = Math.min(CANTIDAD_FIGURAS_MOSTRAR,CANTIDAD_FIGURAS_SELECCIONADAS);

        ArrayList<ImageView> imageViews = null;
        imageViews = this.cargarFiguras(CANTIDAD_FIGURAS_MOSTRAR);
        //posicionCorrecta tiene la posicion de la imagen correcta del 0 al (CANTIDAD_FIGURAS_MOSTRAR - 1)
        int posicionCorrecta = this.getRandomInteger(CANTIDAD_FIGURAS_MOSTRAR-1);

        //0 porque tomamos la 1er imagen como la correcta y la cual se mostrara el texto
        Collections.swap(imageViews,0,posicionCorrecta);
        Log.v("Posicion",String.valueOf(posicionCorrecta) + ": " + ((Recurso) imageViews.get(posicionCorrecta).getTag()).getDescripcion());

        this.cargarRespuesta((Recurso) imageViews.get(posicionCorrecta).getTag());

        for (ImageView imagenView :imageViews) {
            //Agrego las imageviews al layout
            layout.addView(imagenView);
        }

        this.eventosOpciones(imageViews,posicionCorrecta);

        ((TextView) findViewById(R.id.textViewProgreso)).setText(String.valueOf(cantidadRespondidas+1) +"/"+ CANTIDAD_FIGURAS_SELECCIONADAS.toString());

    }

    private ArrayList<Recurso> cargarRecursosUsados() {

        SharedPreferences settings = getSharedPreferences("imagenesSeleccionadas", MODE_PRIVATE);
        Set<String> r =  settings.getStringSet("imagenesSeleccionadas", new HashSet<String>());
        ArrayList<Recurso> res = new ArrayList<>();
        if (r != null) {
            for (Recurso recurso :recursos) {
                if (r.contains(String.valueOf(recurso.getImagen()))) {
                    res.add(recurso);
                }
            }

        }
        return res;
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
        //EN LA POSICION 0 ESTA LA CORRECTA Y SE MARCA COMO "USADA"
        Dimension dim = this.cargarDimension(cantidad_figuras_mostrar);
        ArrayList<ImageView> imagesAgregar = new ArrayList<>();

        ArrayList<Integer> listadoIndicesImagenes = this.obtenerIndicesAlAzar(recursosUsados.size(),cantidad_figuras_mostrar);
        for (Integer imagenIndice :listadoIndicesImagenes) {
            imagesAgregar.add((ImageView) this.getImageView(recursosUsados.get(imagenIndice),dim));
        }

        recursosUsadosMarcados.add(listadoIndicesImagenes.get(0));

        return imagesAgregar;
    }

    private Dimension cargarDimension(int cantidad_figuras_mostrar) {
        //Usa objeto dimension pero por ahora el height es igual que el width, asi que con un valor solo alcanzarìa.
        //0.33 queda bien. 0.50 queda mal, ahi habria que hacerla con menos height pero perderia la proporcion.
        switch (cantidad_figuras_mostrar) {
            case 1:
            case 2:
            case 3:
                return new Dimension(0.33,0.33);
            case 4:
            default:
                return new Dimension(0.25,0.25);
        }
    }

    private ArrayList<Integer> obtenerIndicesAlAzar(int len, int cantidad) {
        ArrayList<Integer> l = new ArrayList<Integer>();
        int i=0;
        while(i<cantidad) {
            Integer tmp = this.getRandomInteger(len-1);
            if (i==0) {
                //como va a ser la respuesta correcta se verifica que no haya sido marcada
                while (recursosUsadosMarcados.contains(tmp)) {
                    tmp = tmp + 1;
                    if (tmp>len-1) tmp = 0;
                }
            }
            if (!l.contains(tmp)) {
                l.add(tmp);
                i++;
            }

        }
        return l;
    }


    private View getImageView(Recurso imageId, Dimension dim){
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
        //layoutParams.width  = (int) (width * 0.25);
        layoutParams.width  = (int) (width * dim.getWidth());
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


    private void eventosOpciones(ArrayList<ImageView> listadoOpciones, int posicionCorrecta) {

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
        //CORRECTA
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    view.setOnClickListener(null);
                    AssetFileDescriptor descriptor = getAssets().openFd("Caballo/relincho.m4a");
                    JuegoActivity.reproducirSonido(descriptor);
                    view.setBackgroundColor(Color.rgb(0,173,56));
                    descriptor.close();
                    view.postDelayed(new Runnable() {
                        public void run() {

                            CANTIDAD_RESPONDIDAS++;
                            if (CANTIDAD_RESPONDIDAS<CANTIDAD_FIGURAS_SELECCIONADAS)
                                JuegoActivity.this.armarJuego(CANTIDAD_FIGURAS_MOSTRAR,CANTIDAD_RESPONDIDAS,CANTIDAD_FIGURAS_SELECCIONADAS);
                            else {
                                JuegoActivity.this.mostrarDialogoNivelCompleto(JuegoActivity.this); }
                        }
                    }, 1000);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void reproducirSonido(AssetFileDescriptor descriptor) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mPlayer.prepare();
            mPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getRandomInteger(int maximo) {
        return (new Random()).nextInt(maximo+1);
    }

    private String getDificultadStr(int dificultad) {
        switch (dificultad) {
            case 1:
                return "Inicial";
            case 2:
                return "Medio";
            case 3:
                return "Avanzado";
            case 4:
                return "Experto";
            default: return "Desconocido";
        }
    }

    private Integer getDificultadCantidadImagenes(int dificultad) {
        return dificultad;
    }

    private void mostrarDialogoNivelCompleto(final JuegoActivity act) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.fin_nivel_dialog, null);
        builder.setView(dialogView);

        Button btnRepetir = (Button) dialogView.findViewById(R.id.repetir);
        Button btnSiguiente = (Button) dialogView.findViewById(R.id.siguiente);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        btnRepetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.inicializarJuego();
                dialog.dismiss();
            }
        });
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dificultad<4) dificultad++;
                act.inicializarJuego();
                dialog.dismiss();

            }
        });
        // 3. Get the AlertDialog from create()

        dialog.show();
    }
}
