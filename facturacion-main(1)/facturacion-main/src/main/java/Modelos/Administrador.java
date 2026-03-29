/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author ALCIDES
 */
public class Administrador {
    
  // Atributos privados (Encapsulamiento)
    private int id;
    private String telefono1;
    private String telefono2;
    private String celular;
    private String rutaImagenes;
    private String mensaje;

    // Constructor vacío (Útil para frameworks o inicializaciones limpias)
    public Administrador() {
    }

    // Constructor con parámetros
    public Administrador(int id, String telefono1, String telefono2, String celular, String rutaImagenes, String mensaje) {
        this.id = id;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.celular = celular;
        this.rutaImagenes = rutaImagenes;
        this.mensaje = mensaje;
    }

    // --- MÉTODOS GETTER Y SETTER ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getRutaImagenes() {
        return rutaImagenes;
    }

    public void setRutaImagenes(String rutaImagenes) {
        this.rutaImagenes = rutaImagenes;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    // Método opcional para ver los datos en consola (Debug)
    @Override
    public String toString() {
        return "Administrador{" + "id=" + id + ", tel1=" + telefono1 + ", cel=" + celular + '}';
    }
}