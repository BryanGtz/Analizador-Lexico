/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador_semantico;
import analizador_lexico.Token;
import analizador_sintactico.*;
import java.util.ArrayList;
/**
 *
 * @author User
 */
public class Analizador_Semantico {
    Arbol_Sintactico as;
    Tabla_Simbolos ts;
    ArrayList<String> errores = new ArrayList();
    ArrayList<String> e = new ArrayList();
    
    public void analizar(Analizador_Sintactico a_l){
        as = a_l.as;
        ts = a_l.tabla;
        System.out.println("========================");
        System.out.println("Inicia análisis semantico");
        Nodo<String> raiz = as.getRaiz();
        recorrer(raiz);
    }
    
    public void recorrer(Nodo n){
        if(n.esTerminal()&&n.getHijos().isEmpty()){
            return;
        }
        if(n.getDatos() instanceof String){
            Nodo<String> regla = n;
            switch (regla.getDatos()) {
                case "SDeclaracion":
                    Nodo<Token> hijo = new Nodo();
                    Nodo<Token> nieto = new Nodo();
                    if(!regla.getHijos().isEmpty()){
                        hijo = regla.getHijo(0);
                        if(!hijo.getHijos().isEmpty()){
                            nieto = hijo.getHijo(0);
                        }
                    }
                    String tipo = nieto.getDatos().getTipo();
                    hijo.setH(tipo);
                    regla.setH(hijo.getH());
                case "Mas_Declaracion":
                    for (int i = 1; i < regla.getHijos().size(); i++) {
                        regla.getHijo(i).setH(regla.getH());
                    }
                    break;
                default:
                    break;
            }
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
            case "Tipo_dato":
                reglaTipoDato(n);
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
            case "Expresion_individual":
                reglaExpIndividual(n);
                break;
            case "Expresion":
                reglaExpresion(n);
                break;
            case "Mas_Expresion":
                reglaMasExpresiones(n);
                break;
            case "IdNum":
                reglaIdNum(n);
            case "Mas_declaraciones":
                reglaMasDeclaraciones(n);
                break;
            case "Souto":
                reglaOuto(n);
                break;
            case "Contenido":
                reglaContenido(n);
                break;
            case "Mas_contenido":
                reglaMasContenido(n);
                break;
            case "SOperacion":
                reglaOperacion(n);
                break;
            case "SIf":
                reglaIf(n);
                break;
            case "Condicion":
                reglaCondicion(n);
                break;
            case "Operador_relacional":
                reglaOperadorRelacional(n);
                break;
            case "Mas_Condiciones":
                reglaMasCondiciones(n);
                break;
            case "Selse":
                reglaElse(n);
                break;
            case "Sfrom":
                reglaFrom(n);
                break;
            case "Condicion_Inicial":
                reglaCondicionInicial(n);
                break;
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
        System.out.println("===ASIGNACION===");
        System.out.println(n.getTipo());
        System.out.println(n.getValor());
    }
    
    public void reglaExpCadena(Nodo n){
        
    }
    
    public void reglaMasCadenas(Nodo n){
        
    }
    
    public void reglaValor(Nodo n){
        if(n.getDatos() instanceof Token){
            Nodo<Token> hijo = n.getHijo(0);
            String valor = hijo.getDatos().getValor();
            String tipo = hijo.getDatos().getTipo();
            n.setValor(valor);
            n.setTipo(tipo);
        }
        System.out.println(n.getTipo());   
        System.out.println(n.getValor());
    }
    
    public void reglaTipoDato(Nodo n){
        String tipo;
        if(n.getHijo(0).getDatos() instanceof Token){
            Nodo<Token> aux = n.getHijo(0);
            tipo = aux.getDatos().getTipo();
            n.setTipo(tipo);      
        }
        System.out.println("===TIPO DE DATO====");
        System.out.println(n.getTipo()+" nodo "+n.getDatos());
    }
    //Expresion_individual -> idNum Expresion
    public void reglaExpIndividual(Nodo n) {
        String primerOperando="";
        String primerTipo="";
        String operador="";
        String segundoOperando="";
        String segundoTipo="";
        if(n.getHijos().size()==2){
            Nodo<Token> idNum = n.getHijo(0);
            Token aux = idNum.getDatos();
            //idNum -> id | Numero
            String tipo = aux.getTipo();
            if(tipo.equals("NUMERO")){
                idNum.setValor(aux.getValor());
                primerOperando = aux.getValor();
                primerTipo = aux.getTipo();
            }
            else if(tipo.equals("IDENTIFICADOR")){
                String valor = ts.getValor(aux.getValor());
                idNum.setValor(valor);
                primerOperando = valor;
                String tipoDato = ts.getTipoDato(aux.getValor());
                idNum.setTipo(tipoDato);
                primerTipo = tipoDato;
            }
            Nodo<String> expresion = n.getHijo(1);
            operador = expresion.getOperador();
            segundoOperando = expresion.getValor();
            segundoTipo = expresion.getTipo();
            realizarOperacion(n,primerOperando,primerTipo,operador,segundoOperando,segundoTipo);
        }
        
        System.out.println("===Expresion individual===");
        System.out.println(primerOperando);
        System.out.println(primerTipo);
        System.out.println(operador);
        System.out.println(segundoOperando);
        System.out.println(segundoTipo);
    }
    
    //Expresion -> Operador_aritmetico IdNum Mas_expresiones | E
    public void reglaExpresion(Nodo n){
        System.out.println("======Expresion=====");
        if(n.getHijos().size()==3){
            Nodo<Token> operador_a = n.getHijo(0);
            String operador = operador_a.getOperador();
            n.setOperador(operador);
            Nodo<String> idNum = n.getHijo(1);
        }
    }
    
    //Mas_expresiones -> Expresion
    public void reglaMasExpresiones(Nodo n){
        if(n.getHijos().size()==1){
            Nodo<String> expresion = n.getHijo(0);
            String valor = expresion.getValor();
            String tipo = expresion.getTipo();
            String operador = expresion.getOperador();
            n.setTipo(tipo);
            n.setValor(valor);
            n.setOperador(operador);
        }
            
    }
        
    public void reglaIdNum(Nodo n) {
        System.out.println("=====IdNum=======");
        if(n.getHijos().size()==1){
            if(n.getHijo(0).getDatos() instanceof Token){
                Nodo<Token> idNum = n.getHijo(0);
                String tipo ="";
                String valor = "";
                if("IDENTIFICADOR".equals(idNum.getDatos().getTipo())){
                    tipo = ts.getTipoDato(idNum.getDatos().getValor ());
                    valor = ts.getValor(idNum.getDatos().getValor());
                }
                else{
                    tipo = idNum.getDatos().getTipo();
                }
            }
        }
        
    }
    
    public void reglaMasDeclaraciones(Nodo n){
        
    }
    
    public void reglaOuto(Nodo n){
        
    }
    
    public void reglaContenido(Nodo n){
        
    }
    
    public void reglaMasContenido(Nodo n){
        
    }
    
    public void reglaOperacion(Nodo n){
        
    }
    
    public void reglaIf(Nodo n){
        
    }
    
    // Condicion -> Expresion_individual Operador_relacional Expresion_individual Mas_Condiciones
    public void reglaCondicion(Nodo n){
        System.out.println("=====Condicion=====");
        if(n.getHijos().size()==4){
            Nodo<String> regla = n;
            Nodo<String> ex_ind = regla.getHijo(0);
            String primerValor = ex_ind.getValor();            
            String primerTipo = ex_ind.getTipo();
            Nodo<String> ex_ind2 = regla.getHijo(2);
            String segundoValor = ex_ind2.getValor();
            String segundoTipo = ex_ind2.getTipo();
            Nodo<String> mas_cond = regla.getHijo(3);
            Nodo<String> op_rel = n.getHijo(1);
            String op = op_rel.getValor();
            switch (op) {
                case ">":
                case "<":
                case ">=":
                case "<=":
                    if("ENTERO".equals(primerTipo)||"DECIMAL".equals(primerTipo)){
                        if("ENTERO".equals(segundoTipo)||"DECIMAL".equals(segundoTipo)){
                            
                        }
                    }
                    break;
                case "==":
                    
                    break;
                default:
                    break;
            }
            
        }
    }

    public void reglaOperadorRelacional(Nodo n){
        if(n.getHijos().size()==1&&n.getHijo(0).getDatos() instanceof Token){
            Nodo<Token> hijo = n.getHijo(0);
            String valor = hijo.getDatos().getValor();
            n.setValor(valor);
        }
    }
    
    public void reglaMasCondiciones(Nodo n){
        
    }
    
    public void reglaElse(Nodo n){
        
    }
    
    public void reglaFrom(Nodo n){
        
    }
    
    public void reglaCondicionInicial(Nodo n){
        
    }
    
    private void realizarOperacion(Nodo regla, String primerValor, String primerTipo, String operador, String segundoValor, String segundoTipo){
        System.out.println(primerValor+operador+segundoValor);
        if(operador==null||segundoValor==null||segundoTipo==null){
            regla.setValor(primerValor);
            regla.setTipo(primerTipo);
        }
        else{
            
        }
    }
}
