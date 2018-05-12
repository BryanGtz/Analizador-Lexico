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
			i++;
		} else {
			System.out.println("ERROR: Se esperaba } | Token recibido: " + tokens.get(i).getValor());
		}

	}

	public void r_cuerpo_starto(List<Token> tokens) {

		String tipo = tokens.get(i).getTipo();

		switch (tipo) {
		case "COMENTARIO":
			r_Comentario(tokens);
			break;
		case "IMPRIMIR":
			r_S_outo(tokens);
			break;
		}

		// r_Declaracion

		// r_S_If

		// r_S_From

		// Vacio

	}

	public void r_Comentario(List<Token> tokens) {
		if (tokens.get(i).getTipo().equals("COMENTARIO")) {
			System.out.println(tokens.get(i).getValor());
			i++;
			r_cuerpo_starto(tokens);
		} else {
			System.out.println("ERROR: Se esperaba # | Token recibido: " + tokens.get(i).getValor());
			r_cuerpo_starto(tokens);
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
		}

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
