/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador_semantico;
import analizador_lexico.Token;
import analizador_sintactico.*;
/**
 *
 * @author User
 */
public class Analizador_Semantico {
    Arbol_Sintactico as;
    Tabla_Simbolos ts;
    
    public Analizador_Semantico(Analizador_Sintactico a_l){
        as = a_l.as;
        //ts = a_l.ts;
    }
    
    public void analizar(){
        Nodo<String> raiz = as.getRaiz();
        recorrer(raiz);
    }
    
    public void recorrer(Nodo<String> n){
        if(n.esTerminal()&&n.getHijos().isEmpty()){
            
            return;
        }
        for (int i = 0; i < n.getHijos().size(); i++) {
            recorrer(n.getHijo(i));
        }
        determinarRegla(n);
    }
    
    public void determinarRegla(Nodo<String> n){
        String regla = n.getDatos();
        switch(regla){
            case "Sstarto":
                reglaStarto(n);
                break;
            case "Cuerpo":
                reglaCuerpo(n);
                break;
            case "Mas_instrucciones":
                break;
            case "SDeclaracion":
                reglaDeclaracion(n);
                break;
            case "":
                break;
            default:
                
                break;
        }
    }
    
    public void reglaStarto(Nodo n){
        
    }
    
    public void reglaCuerpo(Nodo n){        
        
    }
    
    public void reglaDeclaracion(Nodo n){
        
    }
    
    public void reglaAsignacion(Nodo n){
        Nodo hijo = n.getHijo(0);
        String s = "";
        if(hijo.getDatos() instanceof String){
            Nodo<String> h = hijo;
            s = h.getDatos();
        }
        else if(hijo.getDatos() instanceof Token){
            Nodo<Token> h = hijo;
            s = h.getDatos().getValor();
        }
        switch (s) {
            case "":
                break;
            default:
                break;
        }
    }
    
    public void reglaIf(Nodo n){
        
    }
}
