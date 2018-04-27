/*
 * 
 * 
 * 
 */
package analizador_lexico;

/**
 *
 * @author User
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Analizador_Lexico a_l = new Analizador_Lexico();
        a_l.analizar("ejemplo.txt");
        for(Token t: a_l.tokens){            
            System.out.println(t);
        }
    }
    
}
