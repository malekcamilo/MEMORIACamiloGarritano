package proyecto.laboratorio.memoriacamilogarritano;

import android.app.Activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class JuegoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        ArrayList<ImageView> listadoOpciones = new ArrayList<>();
        //Crear las imagenes en forma programatica o utilizar distintos layouts

        //Como es modo Experto son siempre 4 imagenes

        ImageView img1 = (ImageView) findViewById(R.id.imageView1);
        ImageView img2 = (ImageView) findViewById(R.id.imageView2);
        ImageView img3 = (ImageView) findViewById(R.id.imageView3);
        ImageView img4 = (ImageView) findViewById(R.id.imageView4);

        listadoOpciones.add(img1); listadoOpciones.add(img2); listadoOpciones.add(img3); listadoOpciones.add(img4);


        this.eventoSonidosOpciones(listadoOpciones);

        this.eventoSonidoRespuesta();
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


    private void eventoSonidosOpciones(ArrayList<ImageView> listadoOpciones) {

        ImageView img1 = listadoOpciones.get(0);
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

        ImageView img2 = listadoOpciones.get(1);
        img2.setOnClickListener(sonidoIncorrecto);
        ImageView img3 = listadoOpciones.get(2);
        img3.setOnClickListener(sonidoIncorrecto);
        ImageView img4 = listadoOpciones.get(3);
        img4.setOnClickListener(sonidoIncorrecto);

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
            MediaPlayer mPlayer = new MediaPlayer();
            mPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mPlayer.prepare();
            mPlayer.start();
            descriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
