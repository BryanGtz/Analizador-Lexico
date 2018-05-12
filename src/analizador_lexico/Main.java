/*
 * 
 * 
 * 
 */
package analizador_lexico;
import analizador_sintactico.*;

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
        Analizador_Sintactico a_s = new Analizador_Sintactico();
        a_l.analizar("ejemplo.txt");
        a_s.analizar(a_l.tokens);
    }
}
