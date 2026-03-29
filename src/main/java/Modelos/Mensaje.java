/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author ALCIDES
 */
public class Mensaje {
    int Codigo;
    String Mensaje;

    public Mensaje(int Codigo, String Mensaje) {
        this.Codigo = Codigo;
        this.Mensaje = Mensaje;
    }
  public Mensaje() {
      
    }
    @Override
    public String toString() {
        return "Mensaje{" + "Codigo=" + Codigo + ", Mensaje=" + Mensaje + '}';
    }

    public void setCodigo(int Codigo) {
        this.Codigo = Codigo;
    }

    public void setMensaje(String Mensaje) {
        this.Mensaje = Mensaje;
    }

    public int getCodigo() {
        return Codigo;
    }

    public String getMensaje() {
        return Mensaje;
    }
}
