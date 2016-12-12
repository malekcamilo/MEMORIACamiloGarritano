package proyecto.laboratorio.memoriacamilogarritano;

/**
 * Created by malek on 11/12/16.
 */

public enum Recurso {
    AROS("Aros", R.drawable.aros, "Femeninas/Aros.m4a", "Masculinas/Aros.m4a"),
    ARRIADOR("Arriador", R.drawable.arriador, "Femeninas/Arriador.m4a", "Masculinas/Arriador.m4a"),
    BAJO_MONTURA("Bajo Montura", R.drawable.bajomontura, "Femeninas/Bajo Montura.m4a", "Masculinas/Bajo Montura.m4a"),
    BOZAL("Bozal", R.drawable.bozal, "Femeninas/Bozal.m4a", "Masculinas/Bozal.m4a"),
    CABALLO("Caballo", R.drawable.caballo, "Femeninas/Caballo.m4a", "Masculinas/Caballo.m4a"),
    CABEZADA("Cabezada", R.drawable.cabezada, "Femeninas/Cabezada.m4a", "Masculinas/Cabezada.m4a"),
    CASCO("Casco", R.drawable.casco, "Femeninas/Casco.m4a", "Masculinas/Casco.m4a"),
    CASCOS("Cascos", R.drawable.cascos, "Femeninas/Cascos.m4a", "Masculinas/Cascos.m4a"),
    CEPILLO("Cepillo", R.drawable.cepillo, "Femeninas/Cepillo.m4a", "Masculinas/Cepillo.m4a"),
    CINCHON_DE_VOLTEO("Cinchon de volteo", R.drawable.cinchondevolteo, "Femeninas/Cinchon de volteo.m4a", "Masculinas/Cinchon de Volteo.m4a"),
    COLA("Cola", R.drawable.cola, "Femeninas/Cola.m4a", "Masculinas/Cola.m4a"),
    CRINES("Crisnes", R.drawable.crines, "Femeninas/Crines.m4a", "Masculinas/Crines.m4a"),
    CUERDA("Cuerda", R.drawable.cuerda, "Femeninas/Cuerda.m4a", "Masculinas/Cuerda.m4a"),
    ESCARBA_VASOS("Escarba Vasos", R.drawable.escarbavasos, "Femeninas/Escarba Vasos.m4a", "Masculinas/Escarba Vasos.m4a"),
    FUSTA("Fusta", R.drawable.fusta, "Femeninas/Fusta.m4a", "Masculinas/Fusta.m4a"),
    MATRA("Matra", R.drawable.matra, "Femeninas/Matra.m4a", "Masculinas/Matra.m4a"),
    MONTURA("Montura", R.drawable.montura, "Femeninas/Montura.m4a", "Masculinas/Montura.m4a"),
    MONTURIN("Monturín", R.drawable.monturin, "Femeninas/Monturin.m4a", "Masculinas/Monturin.m4a"),
    OJOS("Ojos", R.drawable.ojo, "Femeninas/Ojos.m4a", "Masculinas/Ojos.m4a"),
    OREJAS("Orejas", R.drawable.orejas, "Femeninas/Orejas.m4a", "Masculinas/Orejas.m4a"),
    PALOS("Palos", R.drawable.palos, "Femeninas/Palos.m4a", "Masculinas/Palos.m4a"),
    PASTO("Pasto", R.drawable.pasto, "Femeninas/Pasto.m4a", "Masculinas/Pasto.m4a"),
    PELOTA("Pelora", R.drawable.pelota, "Femeninas/Pelota.m4a", "Masculinas/Pelota.m4a"),
    RASQUETA("Rasqueta", R.drawable.rasqueta, "Femeninas/Rasqueta.m4a", "Masculinas/Rasqueta.m4a"),
    RIENDAS("Riendas", R.drawable.riendas, "Femeninas/Riendas.m4a", "Masculinas/Riendas.m4a"),
    ZANAHORIA("Zanahoria", R.drawable.zanahoria, "Femeninas/Zanahoria.m4a", "Masculinas/Zanahoria.m4a");

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
