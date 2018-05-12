package analizador_sintactico;

import java.util.List;
import java.util.ListIterator;

import analizador_lexico.*;

public class Analizador_Sintactico {

	ListIterator<Token> iterator;
	int i;

	public void analizar(List<Token> tokens) {
		i = 0;
		r_S_starto(tokens);
	}

	public void r_S_starto(List<Token> tokens) {
		if (tokens.get(i).getTipo().equals("INICIO")) {
			System.out.println("starto");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba starto | Token recibido: " + tokens.get(i).getValor());
		}
		if (tokens.get(i).getTipo().equals("PARENTESIS_APERTURA")) {
			System.out.println("(");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}
		if (tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA")) {
			System.out.println(")");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		if (tokens.get(i).getTipo().equals("INICIO_BLOQUE")) {
			System.out.println("{");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba { | Token recibido: " + tokens.get(i).getValor());
		}
		// CUERPO_STARTO
		r_cuerpo_starto(tokens);
		if (tokens.get(i).getTipo().equals("FIN_BLOQUE")) {
			System.out.println("}");
		} else {
			System.out.println("ERROR: Se esperaba } | Token recibido: " + tokens.get(i).getValor());
		}

	}

	public void r_cuerpo_starto(List<Token> tokens) {
		String tipo = tokens.get(i).getTipo();
		switch (tipo) {
		case "COMENTARIO":
			r_Comentario(tokens);
			r_mas_instrucciones(tokens);
			break;
		case "IMPRIMIR":
			r_S_outo(tokens);
			r_mas_instrucciones(tokens);
			break;
		case "DECIMAL":
			r_Identificador(tokens);
			r_mas_instrucciones(tokens);
			break;
		default:
			if (!(tokens.get(i).getTipo().equals("FIN_BLOQUE"))) {
				System.out.println("ERROR");
			}
			break;
		}
	}

	public void r_Identificador(List<Token> tokens) {
		System.out.println(tokens.get(i).getValor());
		String tipo = tokens.get(i).getTipo();
		i++;
		if (tokens.get(i).getTipo().equals("IDENTIFICADOR")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba IDENTIFICADOR | Token recibido: " + tokens.get(i).getValor());
		}
		if (tokens.get(i).getTipo().equals("IGUAL")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba IDENTIFICADOR | Token recibido: " + tokens.get(i).getValor());
		}
		switch (tipo) {
		case "DECIMAL":
			if (tokens.get(i).getTipo().equals("NUMERO")) {
				System.out.println(tokens.get(i).getValor());
				i++;
			} else {
				System.out.println("ERROR: Se esperaba NUMEROS | Token recibido: " + tokens.get(i).getTipo());
			}
			break;
		case "ENTERO":
			if (tokens.get(i).getTipo().equals("NUMERO")) {
				System.out.println(tokens.get(i).getValor());
				i++;
			} else {
				System.out.println("ERROR: Se esperaba NUMEROS | Token recibido: " + tokens.get(i).getTipo());
			}
			break;
		case "BOLEANO":
			if (tokens.get(i).getTipo().equals("VERDADERO")||tokens.get(i).getTipo().equals("FALSO")) {
				System.out.println(tokens.get(i).getValor());
				i++;
			} else {
				System.out.println("ERROR: Se esperaba TRUE o FALSE | Token recibido: " + tokens.get(i).getTipo());
			}
			break;
		case "CARACTER":
			if (tokens.get(i).getTipo().equals("Cadena de caracteres")) {
				System.out.println(tokens.get(i).getValor());
				i++;
			} else {
				System.out.println("ERROR: Se esperaba Cadena | Token recibido: " + tokens.get(i).getTipo());
			}
			break;
		case "CADENA":
			if (tokens.get(i).getTipo().equals("Cadena de caracteres")) {
				System.out.println(tokens.get(i).getValor());
				i++;
			} else {
				System.out.println("ERROR: Se esperaba Cadena | Token recibido: " + tokens.get(i).getTipo());
			}
			break;
		}
		if (tokens.get(i).getTipo().equals("FIN_SENTENCIA")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ; | Token recibido: " + tokens.get(i).getValor());
			r_mas_instrucciones(tokens);
		}
	}

	public void r_mas_instrucciones(List<Token> tokens) {
		String tipo = tokens.get(i).getTipo();
		switch (tipo) {
		case "COMENTARIO":
			r_Comentario(tokens);
			r_mas_instrucciones(tokens);
			break;
		case "IMPRIMIR":
			r_S_outo(tokens);
			r_mas_instrucciones(tokens);
			break;
		case "DECIMAL":
			r_Identificador(tokens);
			r_mas_instrucciones(tokens);
			break;
		case "ENTERO":
			r_Identificador(tokens);
			r_mas_instrucciones(tokens);
			break;
		case "BOLEANO":
			r_Identificador(tokens);
			r_mas_instrucciones(tokens);
			break;
		case "CARACTER":
			r_Identificador(tokens);
			r_mas_instrucciones(tokens);
			break;
		case "CADENA":
			r_Identificador(tokens);	
			r_mas_instrucciones(tokens);
			break;
		case "SI":
			r_S_If();
			break;
		case "INICIO_FOR":
			r_S_From();
			break;
		default:
			if (!(tokens.get(i).getTipo().equals("FIN_BLOQUE"))) {
				System.out.println("ERROR");
			}
			//i++;
			//r_mas_instrucciones(tokens);
			break;
		}
		
	}

	public void r_Comentario(List<Token> tokens) {
		if (tokens.get(i).getTipo().equals("COMENTARIO")) {
			System.out.println(tokens.get(i).getValor());
			i++;
			r_mas_instrucciones(tokens);
		} else {
			System.out.println("ERROR: Se esperaba # | Token recibido: " + tokens.get(i).getValor());
			r_mas_instrucciones(tokens);
		}
	}

	public void r_S_outo(List<Token> tokens) {
		if (tokens.get(i).getTipo().equals("IMPRIMIR")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba outo | Token recibido: " + tokens.get(i).getValor());
		}
		if (tokens.get(i).getTipo().equals("PARENTESIS_APERTURA")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}
		// CUERPO_OUTO
		r_cuerpo_outo(tokens);
		if (tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		if (tokens.get(i).getTipo().equals("FIN_SENTENCIA")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ; | Token recibido: " + tokens.get(i).getValor());
			r_mas_instrucciones(tokens);
		}

	}

	public void r_S_From() {
		
	}
	
	public void r_S_If() {
		
	}
	
	public void r_cuerpo_outo(List<Token> tokens) {
		String tipo = tokens.get(i).getTipo();
		switch (tipo) {
		case "IDENTIFICADOR":
			System.out.println(tokens.get(i).getValor());
			i++;
			break;
		case "Cadena de caracteres":
			System.out.println(tokens.get(i).getValor());
			i++;
			break;
		default:
			System.out.println("ERROR: Se esperaba IDENTIFICADOR o TEXTO | Token recibido: " + tokens.get(i).getTipo());
			break;
		}
	}
}
