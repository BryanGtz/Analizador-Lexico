/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador_semantico;

//import analizador_lexico.Token;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author User
 */
public class Tabla_Simbolos{
    
    HashSet<Simbolo> variables;
    
    public Tabla_Simbolos(){
        
        variables = new HashSet();
    }
    
    public Tabla_Simbolos(Tabla_Simbolos t){
        
        for(Simbolo s: t.variables){
            variables.add(new Simbolo(s));
        }
    }
    
    public boolean agregarSimbolo(Simbolo s){
        return variables.add(s);
    }
    
    public boolean containsSimbolo(Simbolo s){
        
        return variables.contains(s);
    }
    
}