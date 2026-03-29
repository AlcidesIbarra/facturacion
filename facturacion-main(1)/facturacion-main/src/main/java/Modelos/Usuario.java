package Modelos;

public class Usuario {

    private int id; // ✅ AGREGADO

    private String email;
    private String password;
    private String telefono;
    private String rol;
    private byte[] dniFrente;
    private byte[] dniDorso;

    // ===== GETTERS =====

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getRol() {
        return rol;
    }

    public byte[] getDniFrente() {
        return dniFrente;
    }

    public byte[] getDniDorso() {
        return dniDorso;
    }

    // ===== SETTERS =====

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setDniFrente(byte[] dniFrente) {
        this.dniFrente = dniFrente;
    }

    public void setDniDorso(byte[] dniDorso) {
        this.dniDorso = dniDorso;
    }
}