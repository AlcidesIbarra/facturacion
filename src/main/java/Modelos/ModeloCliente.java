/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;


/**
 *
 * @author ALCIDES
 */
 
public class ModeloCliente {

    Integer Codigo;
    String Nombre;
        String Direccion;
            Integer Telefono;
    Integer cuitDni;
    Boolean Activo;
    Double LimiteCompra;
    Double SaldoAcumulado;
    String metodoPago;

    @Override
    public String toString() {
        return "ModeloCliente{" + "Codigo=" + Codigo + ", Nombre=" + Nombre + ", Direccion=" + Direccion + ", Telefono=" + Telefono + ", cuitDni=" + cuitDni + ", Activo=" + Activo + ", LimiteCompra=" + LimiteCompra + ", SaldoAcumulado=" + SaldoAcumulado + ", metodoPago=" + metodoPago + ", observaciones=" + observaciones + ", diasPago=" + diasPago + '}';
    }
  String observaciones;
  Integer diasPago;

    public String getObservaciones() {
        return observaciones;
    }

    public Integer getDiasPago() {
        return diasPago;
    }

    public void setDiasPago(Integer diasPago) {
        this.diasPago = diasPago;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    


    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public Integer getCuitDni() {
        return cuitDni;
    }

    public void setCuitDni(Integer cuitDni) {
        this.cuitDni = cuitDni;
    }

    public Integer getCodigo() {
        return Codigo;
    }

    public void setCodigo(Integer Codigo) {
        this.Codigo = Codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Integer getTelefono() {
        return Telefono;
    }

    public void setTelefono(Integer Telefono) {
        this.Telefono = Telefono;
    }



    public Boolean getActivo() {
        return Activo;
    }

    public void setActivo(Boolean Activo) {
        this.Activo = Activo;
    }

    public Double getLimiteCompra() {
        return LimiteCompra;
    }

    public void setLimiteCompra(Double LimiteCompra) {
        this.LimiteCompra = LimiteCompra;
    }

    public Double getSaldoAcumulado() {
        return SaldoAcumulado;
    }

    public void setSaldoAcumulado(Double SaldoAcumulado) {
        this.SaldoAcumulado = SaldoAcumulado;
    }
    
}
