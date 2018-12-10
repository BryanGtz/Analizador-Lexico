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
    ArrayList<String> exp = new ArrayList();
    //Bandera para saber si la expresion que se está evaluando tiene algun error o no
    boolean error = false;
    
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
                case "Mas_Declaraciones":
                    Object t = regla.getH();
                    for (int i = 1; i < regla.getHijos().size(); i++) {
                        regla.getHijo(i).setH(t);
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
            case "Asignacion":
                reglaAsignacion(n);
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
                break;
            case "OPERADOR_ARITMETICO":
                reglaOperadorAritmetico(n);
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
        System.out.println("======Declaracion========");      
        if(n.getHijos().size()==5){
            Nodo<String> regla = n;
            Nodo<String> tipo_dato = regla.getHijo(0);
            String tipoDato = tipo_dato.getTipo();
            Nodo<Token> identificador = regla.getHijo(1);
            String id = identificador.getDatos().getValor();            
            Nodo<String> i_a = regla.getHijo(2);
            String valor = i_a.getValor();
            String tipoAsig = i_a.getTipo();
            System.out.println("Tipo de dato: "+tipoDato);
            System.out.println("ID: "+id);
            System.out.println("Tipo de asignacion: "+tipoAsig);
            System.out.println("Valor: "+valor);
            //Revisar si la variable ya fue declarada
            //si no, agregarle el tipo de dato
            boolean aux = ts.agregarTipo(id, tipoDato);
            //Si la variable ya tiene tipo de dato se imprime un mensaje de error
            if(!aux){
                System.out.println("Error. La variable "+id+" ya ha sido declarada");
            }
            //Se comprueba que el valor que se quiere asignar corresponda con el tipo de dato indicado
            if(!"".equals(tipoAsig)){
                if(tipoDato.equals(tipoAsig)){                
                    ts.agregarValor(id, tipoAsig, valor);
                }
                else if(tipoDato.equals("CADENA")&&tipoAsig.equals("Cadena de caracteres")){
                    ts.agregarValor(id, tipoAsig, valor);
                }
                else{
                    System.out.println("Error. No se puede asignar "+tipoAsig+" a una variable "+tipoDato);
                }
            }
            
        }        
    }
    
    public void reglaIgualAsig(Nodo n){
        System.out.println("===Igual asignacion====");
        if(n.getHijos().size()==2){
            Nodo<String> hijo = n.getHijo(1); //Nodo asignacion
            n.setValor(hijo.getValor());
            n.setTipo(hijo.getTipo());
        }
        System.out.println("Tipo: "+n.getTipo());
        System.out.println("Valor: "+n.getValor());
    }
    
    public void reglaAsignacion(Nodo n){
        System.out.println("===ASIGNACION===");
        if(n.getHijos().size()==1){
            Nodo<String> hijo = n.getHijo(0);
            System.out.println("Nodo: "+hijo.getDatos());
            n.setValor(hijo.getValor());
            n.setTipo(hijo.getTipo());
        }        
        System.out.println(n.getTipo());
        System.out.println(n.getValor());
    }
    
    // Expresion_cadena -> Cadena Mas_cadenas
    public void reglaExpCadena(Nodo n){
        if(n.getHijos().size()==2){
            System.out.println("====Expresion cadena====");
            Nodo<String> regla = n;
            if(regla.getHijo(0).getDatos() instanceof Token){
                Nodo<Token> cadena = regla.getHijo(0);
                String tipo = cadena.getDatos().getTipo();
                regla.setTipo(tipo);
                String valor = cadena.getDatos().getValor();
                regla.setValor(valor);
                System.out.println("Tipo: "+tipo);
                System.out.println("Valor: "+valor);
            }
        }
        
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
        System.out.println("Tipo: "+n.getTipo());
    }
    //Expresion_individual -> idNum Expresion
    public void reglaExpIndividual(Nodo n) {
        System.out.println("===Expresion individual===");
//        String primerOperando="";
//        String primerTipo="";
//        String operador="";
//        String segundoOperando="";
//        String segundoTipo="";
        if(n.getHijos().size()==2){
            Nodo<String> regla = n;
            Nodo<String> idNum = n.getHijo(0);
            String tipo = idNum.getTipo();
            if(tipo!=null){
                System.out.println("--------------------Arreglo---------------------");
                for (int i = 0; i < exp.size(); i++) {
                    System.out.print(exp.get(i));
                }
                if(exp.size()%2==1){
                    System.out.println("\nEvaluando la expresion anterior: ");
                    EvaludadorExpresion e_e = new EvaludadorExpresion(exp,tipo);
                    regla.setValor(e_e.getValor());
                    System.out.println(e_e.getValor());
                }
                else{
                    System.out.println("ERROR EN EL VALOR QUE SE DESEA ASIGNAR. NO SE PUEDE REALIZAR LA OPERACION");
                }
                regla.setTipo(tipo);
                exp.clear();
                //regla.setValor(e_e.getValor());
            }
            else{
                System.out.println("Error. La variable "+idNum.getHijo(0).getDatos()+" no ha sido declarada");
            }
            
//            if("NUMERO".equals(tipo)){
//                primerOperando = idNum.getValor();
//                primerTipo = idNum.getTipo();
//            }
//            else if("IDENTIFICADOR".equals(tipo)){
//                String valor = ts.getValor(aux.getValor());
//                idNum.setValor(valor);
//                primerOperando = valor;
//                String tipoDato = ts.getTipoDato(aux.getValor());
//                idNum.setTipo(tipoDato);
//                primerTipo = tipoDato;
//            }
//            Nodo<String> exp = n.getHijo(1);
//            operador = exp.getOperador();
//            segundoOperando = exp.getValor();
//            segundoTipo = exp.getTipo();
//            realizarOperacion(n,primerOperando,primerTipo,operador,segundoOperando,segundoTipo);
        }
        
        
//        System.out.println(primerOperando);
//        System.out.println(primerTipo);
//        System.out.println(operador);
//        System.out.println(segundoOperando);
//        System.out.println(segundoTipo);
    }
    
    //Expresion -> Operador_aritmetico IdNum Mas_expresiones | E
    public void reglaExpresion(Nodo n){        
        if(n.getHijos().size()==3){
            System.out.println("======Expresion======");
            Nodo<Token> operador_a = n.getHijo(0);
            String operador = operador_a.getOperador();
            n.setOperador(operador);
            Nodo<String> idNum = n.getHijo(1);
            String tipoId = idNum.getTipo();  
            n.setTipo(tipoId);
            String valor1 = idNum.getValor();
            n.setValor(valor1);
            if("BOLEANO".equals(tipoId)){
                System.out.println("Error. No se admiten operaciones con variables boleanas");
            }
            else if("CADENA".equals(tipoId)&&!"+".equals(operador)){
                System.out.println("Error. No se admiten operaciones diferentes a la concatenacion con variables string");
            }
            Nodo<String> mas_e = n.getHijo(2);
            String tipo2 = mas_e.getTipo();
            System.out.println("Operador: "+operador);
            System.out.println("Valor: "+valor1);
            System.out.println(tipoId);
        }
    }
    
    //Mas_expresiones -> Expresion
    public void reglaMasExpresiones(Nodo n){
        if(n.getHijos().size()==1&&!"".equals(n.getHijo(0).getValor())){
            System.out.println("====Mas expresiones=====");
            Nodo<String> expresion = n.getHijo(0);
            String valor = expresion.getValor();
            String tipo = expresion.getTipo();
            String operador = expresion.getOperador();
            n.setTipo(tipo);
            n.setValor(valor);
            n.setOperador(operador);
            System.out.println(tipo);
            System.out.println(valor);
            System.out.println(operador);
        }
            
    }
        
    public void reglaIdNum(Nodo n) {
        System.out.println("=====IdNum=======");
        if(n.getHijos().size()==1){
            if(n.getHijo(0).getDatos() instanceof Token){
                Nodo<String> regla = n;
                Nodo<Token> idNum = n.getHijo(0);
                String tipo="";
                String valor="";
                if("IDENTIFICADOR".equals(idNum.getDatos().getTipo())){
                    String v = idNum.getDatos().getValor();
                    System.out.println("Variable: "+v);
                    tipo = ts.getTipoDato(v);
                    System.out.println("Tipo de dato: "+tipo);
                    valor = ts.getValor(v);
                    if("".equals(tipo)){
                        System.out.println("Error. La variable "+v+" no ha sido declarada");                        
                    }
                    else{
                        exp.add(valor);
                    }
                }
                else if("NUMERO".equals(idNum.getDatos().getTipo())){
                    if(idNum.getDatos().getValor().contains("\\.")){
                        tipo= "DECIMAL";
                    }
                    else{
                        tipo="ENTERO";
                    }
                    valor = idNum.getDatos().getValor();
                    exp.add(valor);
                }
                regla.setValor(valor);
                regla.setTipo(tipo);
                System.out.println("Valor: "+valor);
                System.out.println("Tipo: "+tipo);
            }
        }
        
    }
    
    public void reglaOperadorAritmetico(Nodo n) {
        Nodo<String> regla = n;
        if(regla.getHijo(0).getDatos() instanceof Token){
            Nodo<Token> op = n.getHijo(0);
            String operador = op.getDatos().getValor();
            regla.setOperador(operador);
            exp.add(operador);
        }
    }
    
    public void reglaMasDeclaraciones(Nodo n){              
        if(n.getHijos().size()==4){
            System.out.println("======Mas Declaraciones========");
            Nodo<String> regla = n;
            String tipoDato = (String) regla.getH();
            Nodo<Token> identificador = regla.getHijo(1);
            String id = identificador.getDatos().getValor();            
            Nodo<String> i_a = regla.getHijo(2);
            String valor = i_a.getValor();
            String tipoAsig = i_a.getTipo();
            System.out.println("Tipo de dato: "+tipoDato);
            System.out.println("ID: "+id);
            System.out.println("Tipo de asignacion: "+tipoAsig);
            System.out.println("Valor: "+valor);
            //Revisar si la variable ya fue declarada
            //si no, agregarle el tipo de dato
            boolean aux = ts.agregarTipo(id, tipoDato);
            //Si la variable ya tiene tipo de dato se imprime un mensaje de error
            if(!aux){
                System.out.println("Error. La variable "+id+" ya ha sido declarada");
            }
            //Se comprueba que el valor que se quiere asignar corresponda con el tipo de dato indicado
            if(tipoDato.equals(tipoAsig)){                
                ts.agregarValor(id, tipoAsig, valor);
            }
        }        
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
