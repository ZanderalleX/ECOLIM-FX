package modelo;

import java.time.LocalDateTime;

public class RegistroRecoleccion {

    private int idRegistro;
    private int idUsuario;
    private int idUbicacion;
    private int idResiduo;
    private double cantidad;
    private String unidad;
    private LocalDateTime fecha;
    private String observaciones;

    public RegistroRecoleccion() {
    }

    public RegistroRecoleccion(int idRegistro, int idUsuario, int idUbicacion,
                               int idResiduo, double cantidad, String unidad,
                               LocalDateTime fecha, String observaciones) {
        this.idRegistro = idRegistro;
        this.idUsuario = idUsuario;
        this.idUbicacion = idUbicacion;
        this.idResiduo = idResiduo;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.fecha = fecha;
        this.observaciones = observaciones;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public int getIdResiduo() {
        return idResiduo;
    }

    public void setIdResiduo(int idResiduo) {
        this.idResiduo = idResiduo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}