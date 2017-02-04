package proyecto.laboratorio.memoriacamilogarritano;

/**
 * Created by tuny on 11/12/2016.
 */

public class Respuesta {
    int imageId;
    String texto;
    String sonidoPath;

    Respuesta(int imageId,String texto,String sonidoPath) {
        this.imageId = imageId;
        this.texto = texto;
        this.sonidoPath = sonidoPath;
    }
    public String getSonidoPath() {
        return sonidoPath;
    }

    public void setSonidoPath(String sonidoPath) {
        this.sonidoPath = sonidoPath;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }


}
