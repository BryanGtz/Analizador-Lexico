/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador_sintactico;

import java.util.ArrayList;
import analizador_lexico.Token;

/**
 *
 * @author User
 */
public class Nodo<T> {
    ArrayList<Nodo> hijos=null;
    T datos;
    boolean esTerminal;
    
    //Constructor
    public Nodo(T t,boolean terminal){
        esTerminal = terminal;
        datos = t;
        if(!terminal){
            hijos = new ArrayList();
        }
    }
    
}
