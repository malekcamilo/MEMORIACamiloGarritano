package proyecto.laboratorio.memoriacamilogarritano;

/**
 * Created by malek on 11/12/16.
 */

public enum Recurso {
    Aros("Aros", R.drawable.aros, "Femeninas/Aros.m4a", "Masculinas/Aros.m4a"),
    Arriador("Arriador", R.drawable.arriador, "Femeninas/Arriador.m4a", "Masculinas/Arriador.m4a"),
    BajoMontura("Bajo Montura", R.drawable.bajomontura, "Femeninas/Bajo Montura.m4a", "Masculinas/Bajo Montura.m4a"),
    Bozal("Bozal", R.drawable.bozal, "Femeninas/Bozal.m4a", "Masculinas/Bozal.m4a"),
    Caballo("Caballo", R.drawable.caballo, "Femeninas/Caballo.m4a", "Masculinas/Caballo.m4a"),
    Cabezada("Cabezada", R.drawable.cabezada, "Femeninas/Cabezada.m4a", "Masculinas/Cabezada.m4a"),
    Casco("Casco", R.drawable.casco, "Femeninas/Casco.m4a", "Masculinas/Casco.m4a"),
    Cascos("Cascos", R.drawable.cascos, "Femeninas/Cascos.m4a", "Masculinas/Cascos.m4a");

    private String descripcion;
    private int imagen;
    private String masculina;
    private String femenina;

    Recurso(String descripcion, int imagen, String femenina, String masculina) {
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.femenina = femenina;
        this.masculina = masculina;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getImagen() {
        return imagen;
    }

    public String getMasculina() {
        return masculina;
    }

    public String getFemenina() {
        return femenina;
    }

}
