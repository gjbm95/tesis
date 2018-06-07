package com.Comandos;

import com.ControladoresRed.ConexionUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

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
public class EnviarMensajeCommand extends AsyncCommand {

    public static final String COMMAND_NAME="send";

    @Override
    public String obtenerNombreComando() {
        return COMMAND_NAME;
    }

    @Override
    public void executeOnBackground(String[] args, OutputStream out) {
       try {
            if (args.length==3) {
                enviarDato(args[2], args[0], Integer.parseInt(args[1]));
            }else{
                System.out.println("La cantidad de parametros es erronea!");
            }
       }catch (Exception e){
           System.out.println("Erro durante envio: "+e.getMessage());
       }
    }

    /**
     * Este metodo se encarga de enviar datos en forma de objetos serializados
     * a un nodo (fantasma o de anillo) que cuya ip y puerto se conoce perfectamente.
     * @param dato
     * @param ip
     * @param puerto
     * @return
     */
    public static void enviarDato(Object dato,String ip, int puerto){
      //  ConexionUtils.obtenerInstancia().enviarMensaje(dato);
    }
}
