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
        return mThumbIds.length;
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

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.arriador, R.drawable.aros,
            R.drawable.bozal, R.drawable.bajomontura,
            R.drawable.caballo, R.drawable.cabezada,
            R.drawable.casco, R.drawable.cascos,
            R.drawable.cepillo, R.drawable.cinchondevolteo,
            R.drawable.cola, R.drawable.crines,
            R.drawable.escarbavasos, R.drawable.cuerda,
            R.drawable.fusta, R.drawable.matra,
            R.drawable.montura, R.drawable.monturin,
            R.drawable.ojo, R.drawable.palos,
            R.drawable.pasto, R.drawable.pelota
    };
}
