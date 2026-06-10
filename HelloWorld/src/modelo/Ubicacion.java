package modelo;

public class Ubicacion {

    private int idUbicacion;
    private String nombreLugar;
    private String direccion;

    public Ubicacion() {
    }

    public Ubicacion(int idUbicacion, String nombreLugar, String direccion) {
        this.idUbicacion = idUbicacion;
        this.nombreLugar = nombreLugar;
        this.direccion = direccion;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public void setNombreLugar(String nombreLugar) {
        this.nombreLugar = nombreLugar;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}