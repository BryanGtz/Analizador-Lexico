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
     * Metodo para agregarle a una variable el tipo de dato, asi como un valor
     * por defecto acorde al tipo de dato
     * @param variable nombre de la variable a buscar en el mapa
     * @param tipo tipo de dato a agregar
     * @return false si no existe la variable en el mapa o ya tiene tipo de dato
     * true si se pudo cambiar el tipo, false en caso contrario
     */
    public boolean agregarTipo(String variable, String tipo){
        //Se obtiene la variable para cambiarle su tipo y valor
        Simbolo var = variables.get(variable);
        //si existe la variable y no tiene tipo de dato
        if(var!=null&&"".equals(var.getTipo_dato())){
            var.setTipo_dato(tipo);
            //Establecer valor por defecto
            switch (tipo) {
                case "ENTERO":
                    var.setValor("0");
                    break;
                case "DECIMAL":
                    var.setValor("0.0");
                    break;
                case "BOLEANO":
                    var.setValor("false");
                    break;
                case "CADENA":
                    var.setValor("");
                    break;
                default:
                    System.out.println("Tipo de dato no reconocido "+tipo);
                    break;
            }
            return (tipo.equals(var.getTipo_dato()));
        }
        else{
            return false;
        }
    }
    
    
    public void agregarValor(String variable, Object valor){
        Simbolo var = variables.get(variable);            
        System.out.print("Se le asigna el valor de "+valor);
        System.out.println(" a la variable "+variable);
        var.setValor((String) valor);   
    }
    
    public String getValor(String variable){
        String aux = variables.get(variable).getValor();
        return (aux==null)?"":aux;
    }
    
    public String getTipoDato(String variable){
        String aux = variables.get(variable).getTipo_dato();
        return (aux==null)?"":aux;
    }
}