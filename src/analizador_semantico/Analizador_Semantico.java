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
    
    public void analizar(Analizador_Sintactico a_l){
        as = a_l.as;
        System.out.println("========================");
        System.out.println("Inicia análisis semantico");
        Nodo<String> raiz = as.getRaiz();
        recorrer(raiz);
    }
    
    public void recorrer(Nodo n){
        if(n.esTerminal()&&n.getHijos().isEmpty()){
            return;
        }
        for (int i = 0; i < n.getHijos().size(); i++) {
            recorrer(n.getHijo(i));
        }
        determinarRegla(n);        
    }
    
    public void determinarRegla(Nodo n){
        String regla;
        if(n.getDatos() instanceof Token){
            Nodo<Token> aux = n;
            regla = aux.getDatos().getValor();
        }
        else{
            Nodo<String> aux = n;
            regla = aux.getDatos();
        }
        //System.out.println(regla);
        switch(regla){
            case "Sstarto":
                reglaStarto(n);
                break;
            case "Cuerpo":
                reglaCuerpo(n);
                break;
            case "Mas_instrucciones":
                reglaMasInstrucciones(n);
                break;
            case "SDeclaracion":
                reglaDeclaracion(n);
                break;
            case "Igual_Asignacion":
                reglaIgualAsig(n);
                break;
            case "Asignacion":
                reglaAsignacion(n);
                break;
            case "Expresion_cadena":
                reglaExpCadena(n);
                break;
            case "Mas_cadenas":
                reglaMasCadenas(n);
                break;
            case "Valor":
                reglaValor(n);
                break;
            case "Expresion_inidividual":
                reglaExpIndividual(n);
            case "Tipo_dato":
                reglaTipoDato(n);
            default:
                break;
        }
    }
    
    public void reglaStarto(Nodo n){
        
    }
    
    public void reglaCuerpo(Nodo n){        
        
    }
    
    public void reglaMasInstrucciones(Nodo n){
        
    }
    
    public void reglaDeclaracion(Nodo n){
        //Agregar el tipo y el valor a la(s) variable(s)
    }
    
    public void reglaIgualAsig(Nodo n){
        if(n.getHijos().size()==2){
            Nodo<String> hijo = n.getHijo(1); //Nodo asignacion
            n.setValor(hijo.getValor());
            n.setTipo(hijo.getTipo());
        }
        System.out.println(n.getTipo());
        System.out.println(n.getValor());
    }
    
    public void reglaAsignacion(Nodo n){
        if(n.getHijos().size()==1){
            Nodo<String> hijo = n.getHijo(0);
            n.setValor(hijo.getValor());
            n.setTipo(hijo.getTipo());
        }
        System.out.println(n.getTipo());
        System.out.println(n.getValor());
    }
    
    public void reglaExpCadena(Nodo n){
        
    }
    
    public void reglaMasCadenas(Nodo n){
        
    }
    
    public void reglaValor(Nodo n){
        
    }
    
    public void reglaTipoDato(Nodo n){
        String tipo;
        if(n.getHijo(0).getDatos() instanceof Token){
            Nodo<Token> aux = n.getHijo(0);
            tipo = aux.getDatos().getTipo();
            n.setTipo(tipo);
            Nodo<Token> id = n.getHermano();
            id.setTipo(n.getTipo());            
        }
        System.out.println(n.getTipo());
        System.out.println(n.getHermano().getTipo());
    }
    
    private void reglaExpIndividual(Nodo n) {
        
    }
    
    public void reglaIf(Nodo n){
        
    }

    
}
