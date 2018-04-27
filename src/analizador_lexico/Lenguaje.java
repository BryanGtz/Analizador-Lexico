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
    
    private static final Map<String,String> PAL_RESERVADAS = new HashMap();
    private static final Map<String,String> OP_ARITMETICOS = new HashMap();
    private static final Map<String,String> OP_LOGICOS = new HashMap();
    private static final Map<String,String> OP_RELACIONALES = new HashMap();
    private static final Map<String,String> SIMBOLOS = new HashMap();
    private static final Map<String,String> TIPOS_DATOS = new HashMap();
    private static final String IDENTIFICADOR = "[A-Za-z_][A-Za-z0-9_]*";
    private static final String CONSTANTES = "[0-9]+([\\\\.]){0,1}([0-9])*";
    private static final String COMENTARIOS = "#";
    private static final String TEXTO = "\"";
    private static final String CARACTER = "'";
    
    public Lenguaje(){
        PAL_RESERVADAS.put("starto", "INICIO");
        PAL_RESERVADAS.put("outo", "IMPRIMIR");
        PAL_RESERVADAS.put("from", "INICIO_FOR");
        PAL_RESERVADAS.put("to", "FIN_FOR");
        PAL_RESERVADAS.put("inc", "INCREMENTO_FOR");
        PAL_RESERVADAS.put("if", "SI");
        PAL_RESERVADAS.put("else", "DE_OTRA_FORMA");
        PAL_RESERVADAS.put("true", "VERDADERO");
        PAL_RESERVADAS.put("false", "FALSO");
        OP_ARITMETICOS.put("+", "SUMA");
        OP_ARITMETICOS.put("-", "RESTA");
        OP_ARITMETICOS.put("*", "MULTIPLICACION");
        OP_ARITMETICOS.put("/", "DIVISION");
        OP_ARITMETICOS.put("%", "MODULO");
        OP_ARITMETICOS.put("=", "IGUAL");
        OP_LOGICOS.put("and", "Y");
        OP_LOGICOS.put("or", "O");
        OP_LOGICOS.put("not", "NO");
        OP_RELACIONALES.put("<", "MENOR_QUE");
        OP_RELACIONALES.put(">", "MAYOR_QUE");
        OP_RELACIONALES.put("<=", "MENOR_IGUAL_QUE");
        OP_RELACIONALES.put(">=", "MENOR_IGUAL_QUE");
        OP_RELACIONALES.put("==", "IGUAL_QUE");
        OP_RELACIONALES.put("!=", "DIFERENTE_QUE");
        OP_RELACIONALES.put("!", "DIFERENTE_QUE"); // Necesito este elemento para concatenar con un = [Ana]
        SIMBOLOS.put("(","PARENTESIS_APERTURA");
        SIMBOLOS.put(")","PARENTESIS_CERRADURA");
        SIMBOLOS.put("{","INICIO_BLOQUE");
        SIMBOLOS.put("}","FIN_BLOQUE");
        SIMBOLOS.put(";","FIN_SENTENCIA");
        TIPOS_DATOS.put("int","ENTERO");
        TIPOS_DATOS.put("bool", "BOLEANO");
        TIPOS_DATOS.put("char", "CARACTER");
        TIPOS_DATOS.put("string", "CADENA");
        TIPOS_DATOS.put("dec", "DECIMAL");
    }
    
    public boolean isPalReservada(String p){
        return PAL_RESERVADAS.containsKey(p.toLowerCase());
    }
    
    public boolean isOperadorAritmetico(String o){
        return OP_ARITMETICOS.containsKey(o);
    }
    
    public boolean isSimbolo(String s){
        return SIMBOLOS.containsKey(s);
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
    
    public boolean isNumero(String n){
        return n.matches("\\d");
    }
    
    public boolean isNumeroOPunto(String n){
        return n.matches("[\\d||\\\\.]");
    }
    //si el string comienza con # se toma como comentario y se omite
    public boolean isComentario(String c){
        return c.startsWith(COMENTARIOS);
    }
    
    public boolean isComillas(String t){
        return t.startsWith(TEXTO);
    }
    
    public boolean isApostrofo(String t){
    	return t.startsWith(CARACTER);
    }
    
    public boolean isOperadorLogicos(String o){
        return OP_LOGICOS.containsKey(o);
    }
    
    public boolean isOperadorRelacional(String o){
        return OP_RELACIONALES.containsKey(o);
    }
    
    public String getTipoPalabraReservada(String palabra){
        return PAL_RESERVADAS.get(palabra);
    }
    
    public String getTipoOperadorAritmetico(String operador){
        return OP_ARITMETICOS.get(operador);
    }
    
    public String getTipoDato(String tipo){
        return TIPOS_DATOS.get(tipo);
    }
    
    public String getTipoSimbolo(String simbolo){
        return SIMBOLOS.get(simbolo);
    }
    
    public String getTipoOperadorLogico(String operador){
        return OP_LOGICOS.get(operador);
    }
    
    public String getTipoOperadorRelacional(String operador){
        return OP_RELACIONALES.get(operador);
    }
    
}