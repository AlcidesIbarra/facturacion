/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author ALCIDES
 */
public class ModeloCuentaCorriente {
    String movimiento;
    Integer CodCliente;
    Double Importe;

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public Integer getCodCliente() {
        return CodCliente;
    }

    public void setCodCliente(Integer CodCliente) {
        this.CodCliente = CodCliente;
    }

    public Double getImporte() {
        return Importe;
    }

    public void setImporte(Double Importe) {
        this.Importe = Importe;
    }
    
}
