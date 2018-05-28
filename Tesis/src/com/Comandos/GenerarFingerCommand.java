package com.Comandos;

import com.ControladoresRed.ConexionUtils;
import com.ControladoresRed.Mensaje;
import com.Entidades.Fantasma;
import com.Entidades.Nodo;
import com.Entidades.NodoRF;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
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
public class GenerarFingerCommand extends BaseCommand {

    public static final String COMMAND_NAME = "generarFinger";

    @Override
    public String obtenerNombreComando() {
        return COMMAND_NAME;
    }

    @Override
    public void ejecutar(String[] args, OutputStream out) {
        Fantasma f= Fantasma.obtenerInstancia();
        int valorFinger;
        HashMap<Integer,NodoRF> tabla = new HashMap<Integer,NodoRF>();
        if (!f.getAnillo().isEmpty()){
        NodoRF primero = f.getAnillo().get(0);
        ArrayList<NodoRF> anillo = f.getAnillo();
            try {
                for (NodoRF nodo : anillo) {
                    tabla = new HashMap<Integer, NodoRF>();
                    int indice = 1;
                    for (int i = 1; i <= 5; i++) {
                        valorFinger = nodo.getHash().intValue() + ((int) Math.pow(2, i - 1));
                        for (NodoRF aux : anillo) {
                            if (aux.getHash().intValue() >= valorFinger) {
                                tabla.put(indice, aux);
                                indice += 1;
                                break;
                            }
                        }
                    }
                    if (tabla.isEmpty()) {
                        tabla.put(1, primero);
                    }
                    ConexionUtils.obtenerInstancia().enviarMensaje(new Mensaje("addtable", tabla, nodo));
                }

                for(NodoRF nodo: anillo){
                    ConexionUtils.obtenerInstancia().enviarMensaje(new Mensaje("share","", nodo));
                }
            }catch(ConcurrentModificationException e){


            }
        }
    }
}
