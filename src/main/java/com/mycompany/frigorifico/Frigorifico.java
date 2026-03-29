/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.frigorifico;
import javax.swing.*;
import Configuracion.CConexion;
import Formularios.MenuPrincipal;

/**
 *
 * @author ALCIDES
 */
public class Frigorifico {

    public static void main(String[] args) {
        System.out.println("Hello World!");
    //   Configuracion.CConexion objetoConexion=new CConexion();
   //     objetoConexion.estableceConexion();
MenuPrincipal objetoMenuPrincipal=new Formularios.MenuPrincipal();
   objetoMenuPrincipal.setVisible(true);
   
    }
}
