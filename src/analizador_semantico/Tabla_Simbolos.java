/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador_semantico;

import java.util.HashMap;

/**
 *
 * @author User
 */
public class Tabla_Simbolos{
    
    HashMap<String,Simbolo> variables;
    
    /**
     * Constructor por default
     */
    public Tabla_Simbolos(){        
        variables = new HashMap();
    }
    /**
     * Constructor copia
     * @param t tabla de simbolos que queremos duplicar
     */
    public Tabla_Simbolos(Tabla_Simbolos t){ 
        variables = new HashMap(t.variables);
    }
    
    /**
     * Metodo que agrega a la tabla solo el nombre de la variable si no existe aun
     * @param variable Nombre de la variable a agregar
     * @return false si ya fue agregado previamente al mapa
     * sino se intenta agregar al mapa
     * y retorna true si se pudo agregar al mapa
     */
    public boolean agregarVariable(String variable){
        if(variables.containsKey(variable)){
            return false;
        }
        else{
            //Método put retorna el objeto que tenia vinculado, sino habia alguno retorna null
            return variables.put(variable, new Simbolo(variable))==null;
        }
    }
    /**
     * Metodo que comprueba si ya fue agregada la variable en la tabla
     * @param variable La variable a buscar
     * @return true si ya se encuentra en la tabla
     * false en caso contrario
     */
    public boolean estaVarible(String variable){
        return variables.containsKey(variable);
    }
    
    /**
     * Metodo para buscar una variable en la tabla y agregarle el tipo de dato
     * @param variable nombre de la variable a buscar en el mapa
     * @param tipo tipo de dato a agregar
     * @return false si no existe la variable en el mapa
     * true si se pudo cambiar el tipo, false en caso contrario
     */
    public boolean agregarTipo(String variable, String tipo){
        Simbolo var = variables.get(variable);
        if(var!=null){
            var.setTipo_dato(tipo);
            return (tipo.equals(var.getTipo_dato()));
        }
        else{
            return false;
        }
    }
    
    public String agregarValor(String variable, String tipo_valor, Object valor){
        Simbolo var = variables.get(variable);
        if(var==null){
            return "Error. La variable "+ variable+ " no ha sido declarada";
        }
        else{
            return "";
        }
    }
    
    public String getValor(String variable){
        return variables.get(variable).getValor();
    }
    
    public String getTipoDato(String variable){
        return variables.get(variable).getTipo_dato();
    }
}