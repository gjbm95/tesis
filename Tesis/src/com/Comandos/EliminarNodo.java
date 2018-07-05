package com.Comandos;

import com.ControladoresRed.ConexionUtils;
import com.ControladoresRed.Mensaje;
import com.Entidades.Estadistica;
import com.Entidades.Fantasma;
import com.Entidades.Nodo;
import com.Entidades.NodoRF;

import java.io.OutputStream;

/**
 * Universidad Catolica Andres Bello
 * Facultad de Ingenieria
 * Escuela de Ingenieria Informatica
 * Trabajo Especial de Grado
 * ----------------------------------
 * Tutor:
 * --------------
 * Wilmer Pereira
 *
 * Autores:
 * --------------
 * Garry Bruno
 * Carlos Valero
 */
public class EliminarNodo extends BaseCommand {

    public static final String COMMAND_NAME="deletenode";

    @Override
    public String obtenerNombreComando() {
        return COMMAND_NAME;
    }

    @Override
    public void ejecutar(String[] args, OutputStream out) {
      Fantasma fantasma = Fantasma.obtenerInstancia();
      int index =0;
      boolean encontrado = false;
          for (int i=0 ; i<fantasma.getAnillo().size();i++){
              NodoRF nodo = fantasma.getAnillo().get(i);
              if ((nodo.getDireccion().equals(args[0]))&&(nodo.getPuertopeticion()==Integer.parseInt(args[1]))) {
                  index=i; 
                       System.out.println("El nodo " +args[0] + " ha salido de la red");
                       Estadistica.add_caidos();
                       encontrado = true;
              }
              else{
                  new ConexionUtils().enviarMensaje(new Mensaje("clean", args[0]+":"+args[1],nodo));
              }
          }
               if (encontrado)
               fantasma.getAnillo().remove(index);
        
    }


}
