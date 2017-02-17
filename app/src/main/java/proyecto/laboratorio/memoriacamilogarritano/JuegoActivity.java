package proyecto.laboratorio.memoriacamilogarritano;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.concurrent.TimeUnit;

public class JuegoActivity extends Activity {
    private static MediaPlayer mPlayer;
    public static int nivel_maximo;
    private int CANTIDAD_FIGURAS_MOSTRAR;
    private int CANTIDAD_FIGURAS_SELECCIONADAS;
    private int CANTIDAD_RESPONDIDAS;
    private int dificultad;
    private int tiempo;
    static private final int unidad_tiempo=1000*10;
//    private int imagenes;
    static Recurso[] recursos = {
            Recurso.MATRA, Recurso.ARREADOR, Recurso.CABEZADA, Recurso.BOZAL, Recurso.BAJO_MONTURA,
            Recurso.CABALLO, Recurso.CASCO, Recurso.CASCOS, Recurso.CEPILLO, Recurso.CINCHON_DE_VOLTEO,
            Recurso.COLA, Recurso.CRINES, Recurso.CUERDA, Recurso.ESCARBA_VASOS, Recurso.FUSTA, Recurso.MONTURA,
            Recurso.MONTURIN, Recurso.OJOS, Recurso.OREJAS, Recurso.PALOS, Recurso.PASTO, Recurso.PELOTA,
            Recurso.RASQUETA, Recurso.RIENDAS, Recurso.AROS, Recurso.ZANAHORIA
    };
    private ArrayList<Recurso> recursosUsados;
    private Set<Integer> recursosUsadosMarcados = new HashSet<>();
    private Handler hTiempo = null;
    private Runnable rTiempo = null;
    private CountDownTimer counterDownTimer = null;
    private static boolean activityVisible;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayer = new MediaPlayer();
        activityVisible = true;
        this.leerConfiguraciones();
        this.inicializarJuego();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JuegoActivity.activityPaused();
        if (isFinishing()) {
            this.cancelarAnterior();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JuegoActivity.activityResumed();
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    //Inicio menú nivel

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nivel_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_nivel) {
            JuegoActivity.this.mostrarDialogoElegirNivel(JuegoActivity.this);
            return true;
        }
        //Esto no se si se puede sacar
        return super.onOptionsItemSelected(item);
    }

    //Fin menú nivel

    private void leerConfiguraciones() {
        //Es cualquiera el tema del String pero con la lista no se puede zafar de otra manera
        //salvo crear otra clase.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String dificultadStr = prefs.getString("dificultad", "4");
        dificultad = Integer.parseInt(dificultadStr);

        //Si es falso el switch la var tiempo es 0
        String tiempoStr = "0";
        if (prefs.getBoolean("switch_tiempo", false) != false) {
            tiempoStr = prefs.getString("tiempo", "1");
        }
        tiempo = Integer.parseInt(tiempoStr);
        Log.v("tiempo", String.valueOf(tiempo));

    }

    private void inicializarJuego() {
        recursosUsadosMarcados.clear();
//        if (imagenes == 0) {
//            this.seleccionarTodas();
//        }
        recursosUsados = this.cargarRecursosUsados();
        CANTIDAD_FIGURAS_MOSTRAR = this.getDificultadCantidadImagenes(dificultad);
        CANTIDAD_FIGURAS_SELECCIONADAS = recursosUsados.size();
        CANTIDAD_RESPONDIDAS = 0;

        this.armarJuego(CANTIDAD_FIGURAS_MOSTRAR, CANTIDAD_RESPONDIDAS, CANTIDAD_FIGURAS_SELECCIONADAS);
    }

    public void armarJuego(int CANTIDAD_FIGURAS_MOSTRAR, int cantidadRespondidas, Integer CANTIDAD_FIGURAS_SELECCIONADAS) {
        nivel_maximo = Math.max(dificultad,nivel_maximo);
        Log.v("nivel_maximo",String.valueOf(nivel_maximo));
        setContentView(R.layout.activity_juego);
        if (hTiempo !=null) {
            this.cancelarAnterior();
        }

        TextView text_nivel = (TextView) findViewById(R.id.textViewDificultad);
        text_nivel.setText("Dificultad: " + this.getDificultadStr(dificultad));
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutRespuestas);

        //CANTIDAD_FIGURAS_MOSTRAR = Math.min(CANTIDAD_FIGURAS_MOSTRAR, CANTIDAD_FIGURAS_SELECCIONADAS);

        ArrayList<ImageView> imageViews = null;
        imageViews = this.cargarFiguras(CANTIDAD_FIGURAS_MOSTRAR);
        //posicionCorrecta tiene la posicion de la imagen correcta del 0 al (CANTIDAD_FIGURAS_MOSTRAR - 1)
        int posicionCorrecta = this.getRandomInteger(CANTIDAD_FIGURAS_MOSTRAR - 1);

        //0 porque tomamos la 1er imagen como la correcta y la cual se mostrara el texto
        Collections.swap(imageViews, 0, posicionCorrecta);
        Log.v("Posicion", String.valueOf(posicionCorrecta) + ": " + ((Recurso) imageViews.get(posicionCorrecta).getTag()).getDescripcion());

        this.cargarRespuesta((Recurso) imageViews.get(posicionCorrecta).getTag());

        for (ImageView imagenView : imageViews) {
            //Agrego las imageviews al layout
            layout.addView(imagenView);
        }
        if (tiempo != 0) {
            this.correrTiempo();
        } else {
            TextView text_tiempo = (TextView) findViewById(R.id.textViewTiempo);
            text_tiempo.setText("Tiempo: Ilimitado");
        }

        this.eventosOpciones(imageViews, posicionCorrecta);

        ((TextView) findViewById(R.id.textViewProgreso)).setText("Progreso: " + String.valueOf(cantidadRespondidas + 1) + "/" + CANTIDAD_FIGURAS_SELECCIONADAS.toString());
    }

    private ArrayList<Recurso> cargarRecursosUsados() {

        SharedPreferences settings = getSharedPreferences("imagenesSeleccionadas", MODE_PRIVATE);
        Set<String> r = settings.getStringSet("imagenesSeleccionadas", new HashSet<String>());
        ArrayList<Recurso> res = new ArrayList<>();
        if (r != null) {
            for (Recurso recurso : recursos) {
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
        } else {
            sonidoPath = image.getMasculina();
        }

        Respuesta respuesta = new Respuesta(image.getImagen(), texto, sonidoPath);
        this.setearNuevaRespuesta(respuesta);
        return respuesta;
    }

    private void setearNuevaRespuesta(Respuesta respuesta) {
        TextView textViewRta = (TextView) findViewById(R.id.textView);
        textViewRta.setText(respuesta.getTexto());
        this.reproducirSonidoInicio(respuesta);
        this.eventoSonidoRespuesta(respuesta);
    }
    
    
    private void reproducirSonidoInicio(Respuesta respuesta){
        AssetFileDescriptor descriptor;
        if ((!isFinishing()) && (isActivityVisible())) {
            try {
                descriptor = getAssets().openFd(respuesta.getSonidoPath());
                JuegoActivity.reproducirSonido(descriptor);
                descriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*private ArrayList<ImageView> cargarFiguras_ant(int cantidad_figuras_mostrar) {
        //EN LA POSICION 0 ESTA LA CORRECTA Y SE MARCA COMO "USADA"
        Dimension dim = this.cargarDimension(cantidad_figuras_mostrar);
        ArrayList<ImageView> imagesAgregar = new ArrayList<>();

        ArrayList<Integer> listadoIndicesImagenes = this.obtenerIndicesAlAzar(recursosUsados.size(), cantidad_figuras_mostrar);
        for (Integer imagenIndice : listadoIndicesImagenes) {
            imagesAgregar.add((ImageView) this.getImageView(recursosUsados.get(imagenIndice), dim));
        }

        recursosUsadosMarcados.add(listadoIndicesImagenes.get(0));

        return imagesAgregar;
    }*/

    private ArrayList<ImageView> cargarFiguras(int cantidad_figuras_mostrar) {
        //EN LA POSICION 0 ESTA LA CORRECTA Y SE MARCA COMO "USADA"
        boolean estaIncluida;
        Dimension dim = this.cargarDimension(cantidad_figuras_mostrar);
        ArrayList<ImageView> imagesAgregar = new ArrayList<>();

        ArrayList<Integer> indiceImagenCorrecta = this.obtenerIndicesAlAzar(recursosUsados.size(), 1);
        ImageView imageCorrecta = (ImageView) this.getImageView(recursosUsados.get(indiceImagenCorrecta.get(0)),dim);


        ArrayList<Integer> listadoIndicesImagenes = this.obtenerIndicesAlAzarIncorrectas(recursos.length, cantidad_figuras_mostrar-1);

        for (Integer imagenIndice : listadoIndicesImagenes) {
            imagesAgregar.add((ImageView) this.getImageView(recursos[imagenIndice], dim));
        }

        estaIncluida = this.estaIncluida(imageCorrecta,imagesAgregar);
        while (estaIncluida) {
            imagesAgregar.clear();
            listadoIndicesImagenes = this.obtenerIndicesAlAzarIncorrectas(recursos.length, cantidad_figuras_mostrar-1);
            for (Integer imagenIndice : listadoIndicesImagenes) {
                imagesAgregar.add((ImageView) this.getImageView(recursos[imagenIndice], dim));
            }
            estaIncluida = this.estaIncluida(imageCorrecta,imagesAgregar);
        }


        imagesAgregar.add(0,imageCorrecta);
        recursosUsadosMarcados.add(indiceImagenCorrecta.get(0));

        return imagesAgregar;
    }

    private boolean estaIncluida(ImageView imageCorrecta, ArrayList<ImageView> imagesAgregar) {
        Recurso tmp;
        for (ImageView imagenIncorrecta: imagesAgregar) {
            tmp = (Recurso) imagenIncorrecta.getTag();
            if (tmp.getImagen() == ((Recurso)imageCorrecta.getTag()).getImagen()) {
                return true;
            }
        }
        return false;
    }


    private Dimension cargarDimension(int cantidad_figuras_mostrar) {
        //Usa objeto dimension pero por ahora el height es igual que el width, asi que con un valor solo alcanzarìa.
        //0.33 queda bien. 0.50 queda mal, ahi habria que hacerla con menos height pero perderia la proporcion.
        switch (cantidad_figuras_mostrar) {
            case 1:
            case 2:
            case 3:
                return new Dimension(0.33, 0.33);
            case 4:
            default:
                return new Dimension(0.25, 0.25);
        }
    }

    private ArrayList<Integer> obtenerIndicesAlAzar(int len, int cantidad) {
        ArrayList<Integer> l = new ArrayList<Integer>();
        int i = 0;
        while (i < cantidad) {
            Integer tmp = this.getRandomInteger(len - 1);
            if (i == 0) {
                //como va a ser la respuesta correcta se verifica que no haya sido marcada
                while (recursosUsadosMarcados.contains(tmp)) {
                    tmp = tmp + 1;
                    if (tmp > len - 1) tmp = 0;
                }
            }
            if (!l.contains(tmp)) {
                l.add(tmp);
                i++;
            }

        }
        return l;
    }

    private ArrayList<Integer> obtenerIndicesAlAzarIncorrectas(int len, int cantidad) {
        ArrayList<Integer> l = new ArrayList<Integer>();
        int i = 0;
        while (i < cantidad) {
            Integer tmp = this.getRandomInteger(len - 1);
            if (!l.contains(tmp)) {
                l.add(tmp);
                i++;
            }

        }
        return l;
    }



    private View getImageView(Recurso imageId, Dimension dim) {
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

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
        //layoutParams.width  = (int) (width * 0.25);
        layoutParams.width = (int) (width * dim.getWidth());
        layoutParams.height = layoutParams.width;

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

    //listadoOpcionnes es final para usarla en varios lados
    private void eventosOpciones(final ArrayList<ImageView> listadoOpciones, int posicionCorrecta) {

        final View.OnClickListener sonidoIncorrecto = new View.OnClickListener() {
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

        for (int j = 0; j < listadoOpciones.size(); j++) {
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
                    //Desactivo los eventos
                    for (int j = 0; j < listadoOpciones.size(); j++) {
                        ImageView imagenIconrrecta = listadoOpciones.get(j);
                        imagenIconrrecta.setOnClickListener(null);
                    }
                    ImageView parlante = (ImageView) findViewById(R.id.imageViewParlante);
                    parlante.setOnClickListener(null);
                    //
                    AssetFileDescriptor descriptor = getAssets().openFd("Caballo/relincho.m4a");
                    JuegoActivity.reproducirSonido(descriptor);
                    view.setBackgroundColor(Color.rgb(0, 173, 56));
                    descriptor.close();
                    //Cancelo el handler si es que se inició
                    if (tiempo != 0) {
                        hTiempo.removeCallbacks(rTiempo);
                        counterDownTimer.cancel();
                    }
                    view.postDelayed(new Runnable() {
                        public void run() {
                            CANTIDAD_RESPONDIDAS++;
                            if (CANTIDAD_RESPONDIDAS < CANTIDAD_FIGURAS_SELECCIONADAS) {
                                JuegoActivity.this.armarJuego(CANTIDAD_FIGURAS_MOSTRAR, CANTIDAD_RESPONDIDAS, CANTIDAD_FIGURAS_SELECCIONADAS);
                            } else {
                                JuegoActivity.this.mostrarDialogoNivelCompleto(JuegoActivity.this);
//                                JuegoActivity.this.mostrarDialogoElegirNivel(JuegoActivity.this);
                            }
                        }
                    }, 2000);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private int getRandomInteger(int maximo) {
        return (new Random()).nextInt(maximo + 1);
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
            default:
                return "Desconocido";
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
        if (dificultad < 4) {

            btnSiguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dificultad<4) { dificultad++; }
                    act.inicializarJuego();
                    dialog.dismiss();

                }
            });
        } else {
            AssetFileDescriptor descriptor = null;
            if ((!isFinishing()) && (isActivityVisible())) {
                try {
                    descriptor = getAssets().openFd("Final/festejo_final.m4a");
                    JuegoActivity.reproducirSonido(descriptor);
                    descriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            btnSiguiente.setBackgroundResource(R.drawable.exit);
            TextView t = (TextView) dialogView.findViewById(R.id.textView4);
            t.setTextColor(Color.RED);
            t.setText("Salir");
            btnSiguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    finish();

                }
            });

        }
        // 3. Get the AlertDialog from create()
        if (!isFinishing()) {
            dialog.show();
        }
    }

    private void cancelarAnterior(){
        //Paro todo lo anterior
        mPlayer.stop();
        if (tiempo != 0) {
            hTiempo.removeCallbacks(rTiempo);
            counterDownTimer.cancel();
        }
        //
    }

    private void mostrarDialogoElegirNivel(final JuegoActivity act) {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.elegir_nivel_dialog, null);
        builder.setView(dialogView);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
//        Log.v("with",String.valueOf(width));

        Button btns[] = new Button[4];

        btns[0] = (Button) dialogView.findViewById(R.id.nivel1);
        btns[1]= (Button) dialogView.findViewById(R.id.nivel2);
        btns[2]= (Button) dialogView.findViewById(R.id.nivel3);
        btns[3]= (Button) dialogView.findViewById(R.id.nivel4);


        final AlertDialog dialog = builder.create();

        btns[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                act.leerConfiguraciones();
                dificultad=1;
                act.inicializarJuego();
                dialog.dismiss();
            }
        });

        btns[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                act.leerConfiguraciones();
                dificultad=2;
                act.inicializarJuego();
                dialog.dismiss();
            }
        });
        btns[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                act.leerConfiguraciones();
                dificultad=3;
                act.inicializarJuego();
                dialog.dismiss();
            }
        });
        btns[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                act.leerConfiguraciones();
                dificultad=4;
                act.inicializarJuego();
                dialog.dismiss();
            }
        });

        for (int i = 0; i < btns.length; i++) {
            btns[i].setWidth(width);
            if (i+1 > nivel_maximo ) btns[i].setEnabled(false);
        }
        dialog.show();
    }

    private void correrTiempo() {
        //Tengo que ver si está bien acá o debería crear un sólo handler
        hTiempo = new Handler();
        rTiempo = new Runnable() {
            public void run() {
                //Modificar
                CANTIDAD_RESPONDIDAS++;
                if (CANTIDAD_RESPONDIDAS < CANTIDAD_FIGURAS_SELECCIONADAS)
                    JuegoActivity.this.armarJuego(CANTIDAD_FIGURAS_MOSTRAR, CANTIDAD_RESPONDIDAS, CANTIDAD_FIGURAS_SELECCIONADAS);
                else {
                    JuegoActivity.this.mostrarDialogoNivelCompleto(JuegoActivity.this);
                }
            }
        };
        //Acá tendría que obtener el tiempo mejor porque ahora son minutos y queda bien
        hTiempo.postDelayed(rTiempo, unidad_tiempo * tiempo);

        //Timer
        final TextView text_tiempo = (TextView) findViewById(R.id.textViewTiempo);
        counterDownTimer = new CountDownTimer(unidad_tiempo * tiempo, 1000) {
            public void onTick(long millisUntilFinished) {
                text_tiempo.setText("Tiempo: " + String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) +
                        ":" + String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
            }
        }.start();

    }

}
