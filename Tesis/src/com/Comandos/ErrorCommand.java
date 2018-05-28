package com.Comandos;

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
public class ErrorCommand extends BaseCommand {

    @Override
    public String obtenerNombreComando() {
        return null;
    }

    @Override
    public void ejecutar(String[] args, OutputStream out) {
        String message = "Error al invokar el comando";
        write(out, message);
    }
}
