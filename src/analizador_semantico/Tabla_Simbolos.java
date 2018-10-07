/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador_semantico;

import analizador_lexico.Token;
import java.util.HashSet;

/**
 *
 * @author User
 */
public class Tabla_Simbolos extends HashSet<Simbolo>{
    
    public Tabla_Simbolos(){
        
    }
    
    public Tabla_Simbolos(Tabla_Simbolos t){
        
    }
    
    public boolean agregarSimbolo(Simbolo s){
        return add(s);
    }
    
}
