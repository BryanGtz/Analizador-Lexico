/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador_lexico;

import java.util.List;

/**
 *
 * @author HP
 */
public class Analizador_Sintactico {
    
    Analizador_Lexico a;
    List<Token> listaToken;
    private int num_token_actual = -1;
    Token t;
    
    public Analizador_Sintactico(Analizador_Lexico a){
        this.a = a;
        listaToken = a.tokens;
    }
    
    public void inicioPrograma(){
        t = nextToken();
        if("INICIO".equals(t.tipo)){
            t = nextToken();
        }
        else{
            error("starto",t);
            return;
        }
        if("PARENTESIS_APERTURA".equals(t.tipo)){
            t = nextToken();
        }
        else{
            error("(",t);
        }
        if("PARENTESIS_CERRADURA".equals(t.tipo)){
            t = nextToken();
        }
        else{
            error(")",t);
        }
        if("INICIO_BLOQUE".equals(t.tipo)){
            t = nextToken();
        }
        else{
            error("{",t);
        }
        cuerpoPrograma();
        if("FIN_BLOQUE".equals(t.tipo)){
        }
        else{
            error("}",t);
        }
    }
    
    private void cuerpoPrograma(){
        sentencia();
        masSentencias();
    }
    
    private void sentencia(){
        if(a.l.isTipoDato(t.valor)){
            declaracion();
        }
        else if(a.l.isPalReservada(t.valor)){
            impresion();
        }
        System.out.println(t.valor);
    }
    
    private void masSentencias(){
        sentencia();
    }
    
    private void declaracion(){
        tipoDato();
        t = nextToken();
        if("IDENTIFICADOR".equals(t.tipo)){
            t = nextToken();
        }
        else{
            error("identificador",t);
        }
    }
    
    private void tipoDato(){
        if("int".equals(t.tipo)||"dec".equals(t.tipo)||"char".equals(t.tipo)
                ||"string".equals(t.tipo)||"bool".equals(t.tipo)){
        }
        else{
            error("un tipo de dato",t);
        }
    }
    
    private void impresion(){
        if("IMPRIMIR".equals(t.tipo)){
            t = nextToken();
        }
        else{
            error("outo",t);
        }
        if("PARENTESIS_APERTURA".equals(t.tipo)){
            t = nextToken();
        }
        else{
            error("(",t);
        }
        if("Cadena de caracteres".equals(t.tipo)){
            t = nextToken();
        }
        else{
            error("\"",t);
        }
        if("PARENTESIS_CERRADURA".equals(t.tipo)){
            t = nextToken();
        }
        else{
            error(")",t);
        }
        if("FIN_SENTENCIA".equals(t.tipo)){
            t = nextToken();
        }
        else{
            error(";",t);
        }
    }
    
    private Token nextToken(){
        num_token_actual++;
        try{
            return listaToken.get(num_token_actual);
        }
        catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }
    
    private void error(String c, Token t){
        System.out.print("Error. Se esperaba: "+c+". ");
        System.out.println("Se encontró: "+t.valor);
        System.out.println("Num de token: "+num_token_actual);
    }
   
}
