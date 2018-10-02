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
public class Nodo {
    ArrayList<Nodo> hijos;
    String nombre;
    Token token;
    boolean esTerminal;
    
    //Constructor cuando el Nodo es terminal
    public Nodo(Token t){
        esTerminal = true;
        
    }
    
}
