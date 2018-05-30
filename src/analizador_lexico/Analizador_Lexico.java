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

	public Lenguaje l = new Lenguaje();
	public List<Token> tokens = new ArrayList();

	public void analizar(String ruta) {
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
			while ((linea = br.readLine()) != null) {
				linea = linea.trim(); // Elimina espacios en blanco al inicio y al final del string dejando los de
										// enmedio
				if (l.isComentario(linea)) {
					// Si la linea corresponde a un comentario, se termina la iteracion actual y se
					// continua con la sig linea
					Token t = new Token("COMENTARIO", linea);
					tokens.add(t);
					continue;
				}
				int i = 0;
				while (i < linea.length()) {
					char caract = linea.charAt(i);
					if (String.valueOf(caract).matches("[\\s]+")) {
						i++;// si encuentra un espacio, continuar al siguiente caracter
					} else {
						// si se encuentra un comentario despues del primer caracter, igualmente se
						// omite el resto de la linea
						if (l.isComentario(String.valueOf(caract))) {
							String comentario = linea.substring(i);
							Token t = new Token("COMENTARIO", comentario);
							tokens.add(t);
							break;
						}
						//si se encuentran comillas
                        else if(l.isComillas(String.valueOf(caract))){
                            aux+=caract;
                            i++;
                            //Recorre la linea mientras i se menor al tama�o de la linea o encuentre comillas de cierre
                            while (i<linea.length()&&!l.isComillas(String.valueOf(linea.charAt(i)))) {
                                caract = linea.charAt(i);
                                aux+=caract;                     
                                i++;
                            }//Si encontro comillas de cierre
                            if(i!=linea.length()) {
                            	Token t = new Token("Cadena de caracteres",aux+"\"");
                            	tokens.add(t);
                            }else {
                            	Token t = new Token("DESCONOCIDO",aux);
                                tokens.add(t);
                            }                            	
                            aux="";
                            i++;
                        }
                        //Si se encuentra un apostrofo
						//Considera solo un caracter
                        else if(l.isApostrofo(String.valueOf(caract))){
                            aux+=caract;
                            i++;
                            if(i<linea.length()) {
                            	aux+=String.valueOf(linea.charAt(i));
                            	if(l.isApostrofo(String.valueOf(linea.charAt(i)))) {                            		
                            		Token t = new Token("CARACTER",aux);
                            		tokens.add(t);
                            	}else{
                            		i++;
                            		if(i<linea.length()&&l.isApostrofo(String.valueOf(linea.charAt(i)))) {
                            			aux+=String.valueOf(linea.charAt(i));
                            			Token t = new Token("CARACTER",aux);
                                		tokens.add(t);
                            		}else {
                            			if(i<linea.length())
                            				aux+=String.valueOf(linea.charAt(i));
                            			Token t = new Token("DESCONOCIDO",aux);
                                        tokens.add(t);
                            		}
                            	}
                            }else{
                            	Token t = new Token("DESCONOCIDO",aux);
                                tokens.add(t);
                            }                             
                            i++;                        
						} else if (l.isNumero(String.valueOf(caract))) { // Comprobamos que sea un numero
							aux += caract;
							i++;// Incrementamos para pasar al siguiente caracter
							while (l.isNumeroOPunto(String.valueOf(caract = linea.charAt(i))) && i < linea.length()) {
								aux += caract;// Mientras sea un numero o punto concatenamos
								i++; // Aumentar contador para ir al siguiente caracter
							}
							if (l.isConstante(aux)) {
								Token t = new Token("NUMERO", aux);
								tokens.add(t);
							} else {
								Token t = new Token("DESCONOCIDO", aux);
								tokens.add(t);
							}
							aux = "";
						} else if (Character.isLetter(caract)) {
							aux += caract;
							i++;
							while (Character.isLetterOrDigit(caract = linea.charAt(i)) && i < linea.length()) {
								aux += caract;// Mientras sea una letra o numero concatenamos
								i++; // Aumentar contador para ir al siguiente caracter
							}
							if (l.isPalReservada(aux)) {
								Token t = new Token(l.getTipoPalabraReservada(aux), aux);
								tokens.add(t);
							} else if (l.isOperadorLogicos(aux)) {
								Token t = new Token(l.getTipoOperadorLogico(aux), aux);
								tokens.add(t);
							} else if (l.isTipoDato(aux)) {
								Token t = new Token(l.getTipoDato(aux), aux);
								tokens.add(t);
							} else if (l.isID(aux)) {
								Token t = new Token("IDENTIFICADOR", aux);
								tokens.add(t);
							}
							aux = "";
						} else if (l.isSimbolo(String.valueOf(caract))) {
							Token t = new Token(l.getTipoSimbolo(String.valueOf(caract)), String.valueOf(caract));
							tokens.add(t);
							i++;
						} else if (l.isOperadorAritmetico(String.valueOf(caract))) {
							aux += caract;
							// Esta condicion es para observar si es la condicion ==
							if (String.valueOf(caract).equals("=")) {
								i++;
								caract = linea.charAt(i);
								if (String.valueOf(caract).equals("=")) {
									aux += caract;
									i++;
									Token t = new Token(l.getTipoOperadorRelacional(aux), aux);
									tokens.add(t);
									aux = "";
								} else {
									// En caso contrario solo se encuentra con un operador
									Token t = new Token(l.getTipoOperadorAritmetico(String.valueOf(aux)),
											String.valueOf(aux));
									tokens.add(t);
									i++;
									aux = "";
								}
							} else {
								// En caso contrario solo se encuentra con un operador
								Token t = new Token(l.getTipoOperadorAritmetico(String.valueOf(aux)),
										String.valueOf(aux));
								tokens.add(t);
								i++;
								aux = "";
							}
						} else if (l.isOperadorLogicos(String.valueOf(caract))) {
							Token t = new Token(l.getTipoOperadorLogico(String.valueOf(caract)),
									String.valueOf(caract));
							tokens.add(t);
							i++;
						} else if (l.isOperadorRelacional(String.valueOf(caract))) {
							aux += caract;
							i++;
							caract = linea.charAt(i);
							// Mientras que sean simbolos relacionales se concatenan
							if (String.valueOf(caract).equals("=")) {
								aux += caract;
							}
							if (String.valueOf(aux).equals("!")) {
								if (String.valueOf(caract).equals("=")) {
									aux += caract;
								}
							}
							Token t = new Token(l.getTipoOperadorRelacional(aux), aux);
							tokens.add(t);
							aux = "";
							i++;
						} else {
							// String tipo = l.reconocer(String.valueOf(caract));
							// Token t = new Token(tipo,String.valueOf(caract));
							// tokens.add(t);
							Token t = new Token("Desconocido", String.valueOf(caract));
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
