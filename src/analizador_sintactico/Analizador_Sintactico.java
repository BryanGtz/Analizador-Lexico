package analizador_sintactico;

import java.util.List;
import java.util.ListIterator;

import analizador_lexico.*;

public class Analizador_Sintactico {

	ListIterator<Token> iterator;
	int i;

	public void analizar(List<Token> tokens) {
		i = 0;
		r_starto(tokens);
		// System.out.println(iterator.next().getValor());
	}

	public void r_starto(List<Token> tokens) {
		if (tokens.get(i).getValor().equals("starto")) {
			System.out.print("starto");
			i++;
			if (tokens.get(i).getValor().equals("(")) {
				System.out.print("(");
				i++;
				if (tokens.get(i).getValor().equals(")")) {
					System.out.print(")");
					i++;
					if (tokens.get(i).getValor().equals("{")) {
						System.out.println("{");
						i++;
						// CUERPO_STARTO
						if (tokens.get(i).getValor().equals("}")) {
							System.out.println("}");
							i++;
						} else {
							System.out.println("ERROR: Se esperaba } | Token recibido: "+tokens.get(i).getValor());
						}
					} else {
						System.out.println("ERROR: Se esperaba { | Token recibido: "+tokens.get(i).getValor());
					}
				} else {
					System.out.println("ERROR: Se esperaba ) | Token recibido: "+tokens.get(i).getValor());
				}
			} else {
				System.out.println("ERROR: Se esperaba ( | Token recibido: "+tokens.get(i).getValor());
			}
		} else {
			System.out.println("ERROR: Se esperaba starto | Token recibido: "+tokens.get(i).getValor());
		}
	}

	public void r_cuerpo_starto() {

	}
}
