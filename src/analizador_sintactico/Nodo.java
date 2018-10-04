/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador_sintactico;

import java.util.ArrayList;
import analizador_lexico.Token;
import java.util.Stack;

/**
 *
 * @author User
 */
public class Nodo<T> {
    T datos;
    ArrayList<Nodo> hijos=new ArrayList();
    boolean esTerminal;
    
    //Constructor
    public Nodo(T t,boolean terminal){
        esTerminal = terminal;
        datos = t;
        if(!terminal){
            hijos = new ArrayList();
        }
    }
    
    //Constructor por default
    public Nodo(){
        hijos = new ArrayList();
        datos = null;
        esTerminal = false;
    }
    
    //constructor copia
    public Nodo(Nodo<T> n){
        this.datos = n.datos;
        this.esTerminal = n.esTerminal;
        this.hijos = new ArrayList(n.hijos);
    }
    
    public boolean addHijo(Nodo n){
        return hijos.add(n);
    }

    public ArrayList<Nodo> getHijos() {
        return hijos;
    }

    public void setHijos(ArrayList<Nodo> hijos) {
        this.hijos = hijos;
    }
    
    public Nodo getHijo(int i){
        return hijos.get(i);
    }
    
    public Nodo setHijo(int hijo, Nodo n){
        return hijos.set(hijo, n);
    }

    public T getDatos() {
        return datos;
    }

    public void setDatos(T datos) {
        this.datos = datos;
    }

    public boolean esTerminal() {
        return esTerminal;
    }

    public void setEsTerminal(boolean esTerminal) {
        this.esTerminal = esTerminal;
    }

    @Override
    public String toString() {
        String texto="";
        if(datos instanceof Token){
            Token t = (Token)datos;
            texto+= t.getTipo()+" ";
        }else{
            texto+=datos.toString()+" ";
        }
//        if(hijos!=null&&!hijos.isEmpty()){
//            texto+="\n";
//            for (Nodo hijo : hijos) {
//                texto+=hijo.toString();
//            }
//        }
        return texto;
    }
}
