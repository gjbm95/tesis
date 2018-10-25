package com.ControladoresRed;

import com.Comandos.EjecutarComando;
import com.Entidades.Estadistica;
import com.Entidades.Fantasma;
import com.Entidades.Nodo;
import com.Entidades.NodoRF;
import com.Utils.LoggerUtil;
import com.Utils.SistemaUtil;
import static com.Utils.SistemaUtil.obtenerHora;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class RedProcesos extends Thread {

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Mensaje mensaje;

    public RedProcesos(Mensaje mensaje,ObjectInputStream ois,ObjectOutputStream oos){
       this.mensaje = mensaje;
       this.ois = ois;
       this.oos = oos;
    }


    public void run(){
        try {
            //LoggerUtil.Log("Funcion: "+this.mensaje.getFuncion()+" Tiempo de llegada: "+SistemaUtil.obtenerHora());
            this.realizarAccion(this.mensaje,this.ois,this.oos);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo encargado de ejecutar una accion en base a un comando recibido por socket
     * @param mensaje
     * @param ois
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public synchronized void realizarAccion(Mensaje mensaje,ObjectInputStream ois,ObjectOutputStream oos)
                                                              throws IOException, ClassNotFoundException {
        String funcion = mensaje.getFuncion();
        //Reportando
        //-------------------------------------------------------
        if (SistemaUtil.tipo.equals("fantasma"))
        SistemaUtil.generarReporte();
        //------------------------------------------------------- 
            if(funcion.equals("addnode")){
                    NodoRF nodo = (NodoRF) mensaje.getData();
                    EjecutarComando.linea("addnode "+nodo.getDireccion()+" "+nodo.getPuertopeticion());
                    LoggerUtil.obtenerInstancia().Log("Agregado nodo "+nodo.getDireccion()+" tiempo: "+SistemaUtil.obtenerHora());
                    SistemaUtil.reportarTiempo("addnode", "final", nodo);
                    System.out.println("Se ha agregado un nodo de forma exitosa");
                    Estadistica.add_nodos();
                    EjecutarComando.linea("order");
                    oos.writeObject(new Mensaje("addnode","",nodo));
                     EjecutarComando.linea("generarFinger");
                     if (SistemaUtil.terminal)
                    EjecutarComando.linea("listring");
            }
            if(funcion.equals("deletenode")){
                if (Fantasma.obtenerInstancia().existe(mensaje.getData())){
                    if (mensaje.getData() instanceof Nodo) {
                        Nodo nodo = (Nodo) mensaje.getData();
                        EjecutarComando.linea("deletenode " + nodo.getDireccion() + " " + nodo.getPuertopeticion());
                        EjecutarComando.linea("order");
                        oos.writeObject(new Mensaje("finalice", "", nodo));
                        EjecutarComando.linea("generarFinger");
                    }else if (mensaje.getData() instanceof  NodoRF){
                        NodoRF nodo = (NodoRF) mensaje.getData();
                        EjecutarComando.linea("deletenode " + nodo.getDireccion() + " " + nodo.getPuertopeticion());
                        oos.writeObject(new Mensaje("finalice", "", nodo));
                    }else{
                        oos.writeObject(null);
                    }
                }
                oos.writeObject(null);
            }
            
            if(funcion.equals("leavenode")){
                if (Fantasma.obtenerInstancia().existe(mensaje.getData())){
                    if (mensaje.getData() instanceof Nodo) {
                        Nodo nodo = (Nodo) mensaje.getData();
                        EjecutarComando.linea("leave " + nodo.getDireccion() + " " + nodo.getPuertopeticion());
                        EjecutarComando.linea("order");
                        oos.writeObject(new Mensaje("finalice", "", nodo));
                        if(Fantasma.obtenerInstancia().getAnillo().size()>0)
                        EjecutarComando.linea("generarFinger");
                    }else if (mensaje.getData() instanceof  NodoRF){
                        NodoRF nodo = (NodoRF) mensaje.getData();
                        EjecutarComando.linea("leave " + nodo.getDireccion() + " " + nodo.getPuertopeticion());
                        oos.writeObject(new Mensaje("finalice", "", nodo));
                    }else{
                        oos.writeObject(null);
                    }
                }
                oos.writeObject(null);
            }
            
            if(funcion.equals("addtable")){
                    Nodo.getInstancia().getTablaRecursos().clear();
                    Nodo.getInstancia().setTabla((HashMap<Integer, NodoRF>)mensaje.getData());
                    LoggerUtil.obtenerInstancia().Log("Finger almacenado "+Nodo.getInstancia().getDireccion()+" tiempo: "+obtenerHora());
                    try {
                        SistemaUtil.reportarTiempo("generarFinger", "final", new NodoRF(Nodo.getInstancia().getDireccion(), Nodo.getInstancia().getPuertopeticion()));
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(RedProcesos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Se ha agregado la tabla de forma exitosa");
                   // EjecutarComando.linea("share");
                    if (SistemaUtil.terminal)
                    EjecutarComando.linea("listfinger");
                    oos.writeObject(null);
            }
            if(funcion.equals("getip")){
                    Long hash = (Long)mensaje.getData();
                    NodoRF nodo = Fantasma.obtenerInstancia().obtenerNodo(hash);
                    oos.writeObject(new Mensaje("getip",nodo,nodo));
            }

            if(funcion.equals("havefile")){
                    Long hash = (Long)mensaje.getData();
                    if(Nodo.getInstancia().buscarRecurso(hash)!=null){
                        oos.writeObject(new Mensaje("havefile",Nodo.getInstancia(),Nodo.getInstancia()));
                    }else{
                        oos.writeObject(null);
                    }
            }

            if(funcion.equals("download")){
                    Long hash = (Long) mensaje.getData();
                    if (Nodo.getInstancia().buscarRecurso(hash) != null) {
                        oos.writeObject(new Mensaje("havefile",Nodo.getInstancia(),Nodo.getInstancia()));
                    }else {
                        System.out.println("redireccionando...");
                        NodoRF hashnode = Nodo.obtenerInstancia().seleccionarNodo(hash);
                        Mensaje data = new Mensaje("getip",hashnode.getHash().longValue(),Fantasma.obtenerInstancia());
                        Mensaje respuesta = (Mensaje)new ConexionUtils().enviarMensaje(data);
                        NodoRF nodo = (NodoRF) respuesta.getData();
                        if (!nodo.getDireccion().equals(mensaje.getOrigen().getDireccion())){
                        data = new Mensaje("download",hash,mensaje.getOrigen(),nodo);
                        nodo = (NodoRF)new ConexionUtils().enviarMensaje(data);
                        oos.writeObject(nodo);
                        }else{
                            oos.writeObject(null);
                        }
                    }
            }

            if(funcion.equals("resource")){/*
                Nodo nodo =(Nodo)mensaje.getOrigen();
                Long hash = Long.parseLong("0");
                if (mensaje.getData() instanceof BigInteger)
                hash = ((BigInteger)mensaje.getData()).longValue();
                if (mensaje.getData() instanceof Long)
                hash = (Long)mensaje.getData();
                if (hash<=Nodo.getInstancia().getHash().longValue()) {
                    Nodo.getInstancia().agregarRecurso(nodo, hash);
                   // System.out.println("Actualizando tabla de recursos");
                    oos.writeObject("asignado");
                }else if (!Nodo.getInstancia().getHash().equals(nodo.getHash())){
                        System.out.println("Redireccionando asignacion...");
                        NodoRF hashnode = Nodo.obtenerInstancia().seleccionarNodo(hash);
                        if (!(hashnode.getDireccion().equals(Nodo.getInstancia().getDireccion())){
                                &&!(hashnode.getPuertopeticion()==Nodo.obtenerInstancia().getPuertopeticion()))
                        new ConexionUtils().enviarMensaje(new Mensaje("resource",hash,
                                nodo,hashnode));
                
                        oos.writeObject("redireccionado");
                        }
                        else{
                            Nodo.getInstancia().agregarRecurso(nodo, hash);
                            oos.writeObject("asignado");
                        }
                    
                }else{
                    Nodo.getInstancia().agregarRecurso(nodo, hash);
                    //System.out.println("Actualizando tabla de recursos");
                    Nodo.getInstancia().setSolicitante(false);
                    oos.writeObject("asignado");
                }*/
                Nodo nodo =(Nodo)mensaje.getOrigen();
                Long hash = Long.parseLong("0");
                if (mensaje.getData() instanceof BigInteger)
                hash = ((BigInteger)mensaje.getData()).longValue();
                if (mensaje.getData() instanceof Long)
                hash = (Long)mensaje.getData();
                
                if(hash <= Nodo.getInstancia().getHash().longValue()){
                    Nodo.getInstancia().agregarRecurso(nodo, hash);
                    oos.writeObject("asignado");
                }else if (nodo.getDireccion().equals(Nodo.getInstancia().getDireccion())){
                    Nodo.getInstancia().agregarRecurso(nodo, hash);
                    oos.writeObject("asignado");
                    Nodo.getInstancia().setSolicitante(false);
                }else{
                        NodoRF hashnode = Nodo.obtenerInstancia().seleccionarNodo(hash);
                        if (!(hashnode.getDireccion().equals(Nodo.getInstancia().getDireccion()))
                                &&!(hashnode.getPuertopeticion()==Nodo.obtenerInstancia().getPuertopeticion())){
                        new ConexionUtils().enviarMensaje(new Mensaje("resource",hash,
                                nodo,hashnode));
                        oos.writeObject("redireccionado");
                        }
                        else{
                            Nodo.getInstancia().agregarRecurso(nodo, hash);
                            oos.writeObject("asignado");
                        }
                    }
                    
            }

            if (funcion.equals("who")){

                Nodo nodo =(Nodo)mensaje.getOrigen();
                Long hash = (Long)mensaje.getData();
                System.out.println("El hash es: "+ hash);
                ArrayList<Nodo> respuesta = Nodo.getInstancia().tieneRecurso(hash);
                if (respuesta.size()>0){
                    oos.writeObject(respuesta);
                }else if (!(nodo.getDireccion().equals(Nodo.getInstancia().getDireccion()))){   
                    System.out.println("Redireccionando consulta...");
                        NodoRF hashnode = Nodo.obtenerInstancia().seleccionarNodo(hash);
                        if(hashnode.getDireccion()==Nodo.getInstancia().getDireccion())
                            oos.writeObject(null);
                        else
                    if (!(nodo.getDireccion().equals(Nodo.getInstancia().getDireccion()))
                            &&!(nodo.getPuertopeticion()==Nodo.obtenerInstancia().getPuertopeticion())){
                        Nodo.getInstancia().setSolicitante(true);
                        oos.writeObject((ArrayList<Nodo>)new ConexionUtils().enviarMensaje(new Mensaje("who", hash,
                                nodo, hashnode)));
                        Nodo.getInstancia().setSolicitante(false);
                    }else{
                        oos.writeObject(null);
                    }
                }else {
                    Nodo.getInstancia().setSolicitante(false);
                    oos.writeObject(null);
                }
            }

            if(funcion.equals("getresourse")){
                Object object =  ois.readObject();
                if (object instanceof String){
                    String datos = (String)object;
                    String atributos [] = datos.split(":");

                }
                oos.writeObject(null);
            }

            if(funcion.equals("share")){
                Nodo.getInstancia().setCompartir(true);
                oos.writeObject("");
                System.out.println("Compartiendo...");
                EjecutarComando.linea("share");
            }

            if(funcion.equals("first")){
                if( Fantasma.obtenerInstancia().getAnillo()!= null && !Fantasma.obtenerInstancia().getAnillo().isEmpty())
                    oos.writeObject(Fantasma.obtenerInstancia().getAnillo().get(0));
                else
                    oos.writeObject(null);
            }

            if(funcion.equals("clean")){
                EjecutarComando.linea("cleanresources " + ((String)mensaje.getData()).split(":")[0] +
                " " + ((String)mensaje.getData()).split(":")[1]);
                oos.writeObject("");
            }

            if(funcion.equals("size")){
                Long hashArchivo = (Long)(mensaje.getData());
                oos.writeObject(Nodo.getInstancia().buscarRecurso(hashArchivo).getTamano());
            }
            
            if(funcion.equals("areyouthere?")){
                oos.writeObject("yes");
            }

       
    }
}
