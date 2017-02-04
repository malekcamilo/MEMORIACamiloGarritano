package proyecto.laboratorio.memoriacamilogarritano;

/**
 * Created by tuny on 19/01/2017.
 */

public class Dimension {
    double width;

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    double height;

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    Dimension(double w,double h) {
        this.width = w;
        this.height = h;
    }
}
