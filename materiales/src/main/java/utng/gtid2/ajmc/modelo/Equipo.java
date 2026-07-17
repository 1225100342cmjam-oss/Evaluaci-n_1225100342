package utng.gtid2.ajmc.modelo;

import java.sql.Timestamp;

public class Equipo {

    private int idEquipo;
    private String codigo;
    private String nombre;
    private TipoEquipo tipo;
    private Responsable responsable;
    private String estado;
    private String descripcion;
    private Timestamp fechaRegistro;

    public Equipo() {
    }

    public Equipo(int idEquipo, String codigo, String nombre,
                  TipoEquipo tipo,
                  Responsable responsable,
                  String estado,
                  String descripcion,
                  Timestamp fechaRegistro) {

        this.idEquipo = idEquipo;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.responsable = responsable;
        this.estado = estado;
        this.descripcion = descripcion;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoEquipo getTipo() {
        return tipo;
    }

    public void setTipo(TipoEquipo tipo) {
        this.tipo = tipo;
    }

    public Responsable getResponsable() {
        return responsable;
    }

    public void setResponsable(Responsable responsable) {
        this.responsable = responsable;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

}