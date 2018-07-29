/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.Entidades.Nodo;
import java.util.ArrayList;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Junior
 */
public class Controller {
    
    
    
    
    public static void setCentral(String address){
        
    }
    
    public static DefaultTableModel fillTableDefault()
    {
    String[] columna = {"Archivo","Propietatio"};
    DefaultTableModel dtm = new DefaultTableModel(null,columna);      
    return dtm;
   }
    
    public static DefaultTableModel fillTable(String name,ArrayList<Nodo> duenos)
    {
    String[] columna = {"Archivo","Propietario"};
    DefaultTableModel dtm = new DefaultTableModel(null,columna);      
        for (Nodo dueno : duenos)
            {
            
             String[] row = {name,dueno.getHash().toString()};
             dtm.addRow(row);
          }  
    return dtm;
   }
    
   public static void setLog(JTextArea logs,String text){
       logs.setText(logs.getText() + "\n" +text);
   }
    
    
    
}
