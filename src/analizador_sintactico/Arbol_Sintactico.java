/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador_sintactico;

import analizador_lexico.Token;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author User
 */
public class Arbol_Sintactico {
    Nodo raiz;
    
    public Arbol_Sintactico(Nodo r){
        raiz = r;
    }
    
    public Arbol_Sintactico(){
        raiz = new Nodo();
    }

    public Nodo getRaiz() {
        return raiz;
    }

    public void setRaiz(Nodo raiz) {
        this.raiz = raiz;
    }
    
//    @Override
//    public String toString(){
//        
//        return raiz.toString();
//    }
    
    @Override
    public String toString(){
        String texto = "";
        Queue<Nodo> aux = new LinkedList();
        aux.add(raiz);
        texto+= raiz.getDatos();
        while(!aux.isEmpty()){
            Nodo n = aux.poll();
            texto+=(n.getHijos().isEmpty())?"":"\n";
            for (int i = 0; i < n.getHijos().size(); i++) {
                Nodo hijoActual = n.getHijo(i);
                aux.add(hijoActual);
                if(hijoActual.getDatos() instanceof Token){
                    Nodo<Token> nodo_aux = new Nodo(hijoActual);
                    texto+=nodo_aux.getDatos().getTipo()+" ";
                }
                else{
                    texto+=hijoActual.getDatos()+" ";
                }
            }
        }
        return texto;
    }
    
}
