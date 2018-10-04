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
//        Analizador_Lexico a_l = new Analizador_Lexico();
//        a_l.analizar("ejemplo.txt");
//        System.out.println("Inicia análisis léxico...");
//        System.out.println("TOKENS RECONOCIDOS");
//        for(Token t: a_l.tokens){
//            System.out.println(t);
//        }
//        System.out.println("Inicia análisis sintáctico...");
//        AnalizadorSintactico a_s = new AnalizadorSintactico(a_l);
//        a_s.inicioPrograma();
        Analizador_Lexico a_l = new Analizador_Lexico();
        Analizador_Sintactico a_s = new Analizador_Sintactico();
        a_l.analizar("ejemplo2.txt");
        a_s.analizar(a_l);
        System.out.println(a_s.as);
    }
}
