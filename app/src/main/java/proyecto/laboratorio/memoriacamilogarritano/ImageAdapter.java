package proyecto.laboratorio.memoriacamilogarritano;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by malek on 22/01/17.
 */

public class ImageAdapter extends BaseAdapter { private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return recursos.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(recursos[position].getImagen());
        imageView.setTag(recursos[position].getImagen());
        return imageView;
    }

    // references to our images

    private Recurso[] recursos = {
            Recurso.MATRA, Recurso.ARRIADOR, Recurso.CABEZADA, Recurso.BOZAL,Recurso.BAJO_MONTURA,
            Recurso.CABALLO, Recurso.CASCO, Recurso.CASCOS, Recurso.CEPILLO, Recurso.CINCHON_DE_VOLTEO,
            Recurso.COLA,Recurso.CRINES,Recurso.CUERDA,Recurso.ESCARBA_VASOS,Recurso.FUSTA,Recurso.MONTURA,
            Recurso.MONTURIN,Recurso.OJOS,Recurso.OREJAS,Recurso.PALOS,Recurso.PASTO,Recurso.PELOTA,
            Recurso.RASQUETA, Recurso.RIENDAS, Recurso.AROS, Recurso.ZANAHORIA

    };
}
