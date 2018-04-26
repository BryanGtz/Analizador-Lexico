/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador_lexico;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class Analizador_Lexico {
    
    Lenguaje l = new Lenguaje();
    List<Token> tokens = new ArrayList();
    
    
    public void analizar(String ruta){
        FileReader fr = null;
        try {
            fr = new FileReader(ruta);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "No se encontró el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        BufferedReader br = new BufferedReader(fr);
        String linea;
        String aux = "";
        try {
            while ((linea=br.readLine())!=null) {
                linea = linea.trim(); //Elimina espacios en blanco al inicio y al final del string dejando los de enmedio
                if(l.isComentario(linea)){
                    //Si la linea corresponde a un comentario, se termina la iteracion actual y se continua con la sig linea
                    Token t = new Token("COMENTARIO",linea);
                    tokens.add(t);
                    continue;
                }
                int i = 0;
                while (i < linea.length()) {
                    char caract = linea.charAt(i);
                    if(String.valueOf(caract).matches("[\\s]+")){
                        i++;//si encuentra un espacio, continuar al siguiente caracter
                    }
                    else{
                        //si se encuentra un comentario despues del primer caracter, igualmente se omite el resto de la linea
                        if(l.isComentario(String.valueOf(caract))){
                            String comentario = linea.substring(i);
                            Token t = new Token("COMENTARIO",comentario);
                            tokens.add(t);
                            break;
                        }
                        else if(l.isComillas(String.valueOf(caract))){
                            aux+=caract;
                            i++;
                            while (!l.isComillas(String.valueOf(linea.charAt(i)))) {
                                caract = linea.charAt(i);
                                aux+=caract;
                                i++;
                            }
                            Token t = new Token("Cadena de caracteres",aux+"\"");
                            tokens.add(t);
                            aux="";
                            i++;
                        }
                        else if(l.isConstante(String.valueOf(caract))){ //Comprobamos que sea un numero
                            aux+=caract;
                            i++;
                            aux+=linea.charAt(i);
                            while (l.isConstante(aux)&&i<linea.length()) { 
                                aux+=caract;//Mientras sea un numero concatenamos
                                i++; //Aumentar contador para ir al siguiente caracter
                                aux+=linea.charAt(i);//Concatenamos el siguiente caracter para comparar
                            }
                            Token t = new Token("Numero",aux);
                            tokens.add(t);
                            aux="";
                        }
                        else if(Character.isLetter(caract)){
                            aux+=caract;
                            i++;
                            while (Character.isLetterOrDigit(caract = linea.charAt(i))&&i<linea.length()) { 
                                aux+=caract;//Mientras sea una letra o numero concatenamos
                                i++; //Aumentar contador para ir al siguiente caracter
                            }
                            if(l.isPalReservada(aux)){
                                Token t = new Token(l.getTipoPalabraReservada(aux),aux);
                                tokens.add(t);
                            }
                            else if(l.isTipoDato(aux)){
                                Token t = new Token(l.getTipoDato(aux),aux);
                                tokens.add(t);
                            }
                            else if(l.isID(aux)){
                                Token t = new Token("Identificador",aux);
                                tokens.add(t);
                            }
                            aux="";
                        }
                        else if(l.isSimbolo(String.valueOf(caract))){
                            Token t = new Token("Simbolo",String.valueOf(caract));
                            tokens.add(t);
                            i++;
                        }
                        else if(l.isOperador(String.valueOf(caract))){
                            Token t = new Token(l.getTipoOperador(String.valueOf(caract)),String.valueOf(caract));
                            tokens.add(t);
                            i++;
                        }
                        else{
//                            String tipo = l.reconocer(String.valueOf(caract));
//                            Token t = new Token(tipo,String.valueOf(caract));
//                            tokens.add(t);
                            Token t = new Token("Desconocido",String.valueOf(caract));
                            tokens.add(t);
                            i++;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
