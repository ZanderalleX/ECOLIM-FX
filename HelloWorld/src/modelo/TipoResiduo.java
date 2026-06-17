package modelo;

public class TipoResiduo {

    private int idResiduo;
    private String nombreResiduo;
    private String categoria;

    public TipoResiduo() {
    }

    public TipoResiduo(int idResiduo, String nombreResiduo, String categoria) {
        this.idResiduo = idResiduo;
        this.nombreResiduo = nombreResiduo;
        this.categoria = categoria;
    }

    public int getIdResiduo() {
        return idResiduo;
    }

    public void setIdResiduo(int idResiduo) {
        this.idResiduo = idResiduo;
    }

    public String getNombreResiduo() {
        return nombreResiduo;
    }

    public void setNombreResiduo(String nombreResiduo) {
        this.nombreResiduo = nombreResiduo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}