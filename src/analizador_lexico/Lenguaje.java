/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador_lexico;

import java.util.*;

/**
 *
 * @author User
 */
public class Lenguaje {
    
//    private static final List<String> PAL_RESERVADAS = new ArrayList();
//    private static final List<String> OPERADORES = new ArrayList();
//    private static final List<String> SIMBOLOS = new ArrayList();
//    private static final List<String> TIPOS_DATOS = new ArrayList();
    private static final Map<String,String> PAL_RESERVADAS = new HashMap();
    private static final Map<String,String> OP_ARITMETICOS = new HashMap();
    private static final List<String> SIMBOLOS = new ArrayList();
    private static final Map<String,String> TIPOS_DATOS = new HashMap();
    private static final String IDENTIFICADOR = "[A-Za-z_][A-Za-z0-9_]*";
    private static final String CONSTANTES = "[0-9][0-9\\.]*";
    private static final String COMENTARIOS = "#";
    private static final String TEXTO = "\"";
    
    public Lenguaje(){
//        PAL_RESERVADAS.addAll(Arrays.asList("starto", "outo", "from", "to", "inc"));
//        OPERADORES.addAll(Arrays.asList("+","-","*","/","%",">","<","="));
//        SIMBOLOS.addAll(Arrays.asList("(",")","{","}",";","#","/*","*/"));
//        TIPOS_DATOS.addAll(Arrays.asList("int","dec","char","string","bool"));
        PAL_RESERVADAS.put("starto", "INICIO");
        PAL_RESERVADAS.put("outo", "IMPRIMIR");
        PAL_RESERVADAS.put("from", "INICIO_FOR");
        PAL_RESERVADAS.put("to", "FIN_FOR");
        PAL_RESERVADAS.put("inc", "INCREMENTO_FOR");
        PAL_RESERVADAS.put("if", "CONDICIONAL");
        OP_ARITMETICOS.put("+", "SUMA");
        OP_ARITMETICOS.put("-", "RESTA");
        OP_ARITMETICOS.put("*", "MULTIPLICACION");
        OP_ARITMETICOS.put("/", "DIVISION");
        OP_ARITMETICOS.put("=", "ASIGNACION");
        SIMBOLOS.addAll(Arrays.asList("(",")","{","}",";","#","/*","*/"));
        TIPOS_DATOS.put("int","ENTERO");
        TIPOS_DATOS.put("bool", "BOLEANO");
        TIPOS_DATOS.put("char", "CARACTER");
        TIPOS_DATOS.put("string", "CADENA");
        TIPOS_DATOS.put("dec", "DECIMAL");
    }
    
    public boolean isPalReservada(String p){
        return PAL_RESERVADAS.containsKey(p.toLowerCase());
    }
    
    public boolean isOperador(String o){
        return OP_ARITMETICOS.containsKey(o);
    }
    
    public boolean isSimbolo(String s){
        return SIMBOLOS.contains(s);
    }
    
    public boolean isTipoDato(String t){
        return TIPOS_DATOS.containsKey(t.toLowerCase());
    }
    
    public boolean isID(String i){
        return i.matches(IDENTIFICADOR);
    }
    
    public boolean isConstante(String n){
        return n.matches(CONSTANTES);
    }
    //si el string comienza con # se toma como comentario y se omite
    public boolean isComentario(String c){
        return c.startsWith(COMENTARIOS);
    }
    
    public boolean isComillas(String t){
        return t.startsWith(TEXTO);
    }
    
    public String getTipoPalabraReservada(String palabra){
        return PAL_RESERVADAS.get(palabra);
    }
    
    public String getTipoOperador(String operador){
        return OP_ARITMETICOS.get(operador);
    }
    
    public String getTipoDato(String tipo){
        return TIPOS_DATOS.get(tipo);
    }
}