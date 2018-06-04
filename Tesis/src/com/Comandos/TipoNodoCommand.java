package com.Comandos;

import com.ControladoresRed.ConexionUtils;
import com.ControladoresRed.Mensaje;
import com.Entidades.Fantasma;
import com.Entidades.Nodo;
import com.Entidades.NodoRF;
import com.Utils.SistemaUtil;

import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
   
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
public class TipoNodoCommand extends BaseCommand {

    public static final String COMMAND_NAME ="type";

    @Override
    public String obtenerNombreComando() {
        return COMMAND_NAME;
    }

    @Override
    public void ejecutar(String[] args, OutputStream out) {
        if (args[0].equals("miembro")) {
            if (args.length == 1)
            EjecutarComando.linea("selectnetwork miembro");
            else if (args.length == 2)
            EjecutarComando.linea("selectnetwork miembro "+args[1]);    
            System.out.println("Se ha asignado el tipo de nodo exitosamente");
           try {
                NodoRF mynodorf = new NodoRF(Nodo.obtenerInstancia().getDireccion(),Nodo.getInstancia().getPuertopeticion());
                ConexionUtils.obtenerInstancia().enviarMensaje(new Mensaje("addnode",mynodorf,Fantasma.obtenerInstancia()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }else if (args[0].equals("fantasma")) {
            if (args.length == 1)
            EjecutarComando.linea("selectnetwork fantasma");
            else if (args.length == 2)
            EjecutarComando.linea("selectnetwork fantasma "+args[1]); 
            System.out.println("Se ha asignado el tipo de nodo exitosamente");
        }else
            write(out,"Este tipo de nodo no existe! ");

    }
}
