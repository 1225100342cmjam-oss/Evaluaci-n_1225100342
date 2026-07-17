package utng.gtid2.ajmc.modelo;

public class Responsable {

    private int idResponsable;
    private String nombreCompleto;
    private String departamento;
    private String email;
    private String telefono;

    public Responsable() {
    }

    public Responsable(int idResponsable,
                        String nombreCompleto,
                        String departamento,
                        String email,
                        String telefono) {

        this.idResponsable = idResponsable;
        this.nombreCompleto = nombreCompleto;
        this.departamento = departamento;
        this.email = email;
        this.telefono = telefono;
    }

    public int getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return nombreCompleto;
    }

}