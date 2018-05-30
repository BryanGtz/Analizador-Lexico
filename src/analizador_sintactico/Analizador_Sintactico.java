package analizador_sintactico;

import java.util.List;
import java.util.ListIterator;

import analizador_lexico.*;

public class Analizador_Sintactico {

	ListIterator<Token> iterator;
	int i;

	public void analizar(List<Token> tokens) {
		i = 0;
		Sstarto(tokens);
	}

	// Sstarto -> starto () {cuerpo}
	public void Sstarto(List<Token> tokens) {		
		// starto: 
		if (tokens.get(i).getTipo().equals("INICIO")) {
			System.out.println("starto");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba starto | Token recibido: " + tokens.get(i).getValor());
		}
		// Parentesis:
		// (
		if (tokens.get(i).getTipo().equals("PARENTESIS_APERTURA")) {
			System.out.println("(");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}
		// )
		if (tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA")) {
			System.out.println(")");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		// Inicio del bloque:
		// {
		if (tokens.get(i).getTipo().equals("INICIO_BLOQUE")) {
			System.out.println("{");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba { | Token recibido: " + tokens.get(i).getValor());
		}
		// Cuerpo:
		cuerpo(tokens);
		// Fin del bloque:
		// }
		if (tokens.get(i).getTipo().equals("FIN_BLOQUE")) {
			System.out.println("}");
		} else {
			System.out.println("ERROR: Se esperaba } | Token recibido: " + tokens.get(i).getValor());
		}
	}

	// cuerpo -> 
	//    Souto Mas_Instrucciones 
	//  | SDeclaracion Mas_Instrucciones 
	//  | SOperacion Mas_Instrucciones 
	//  | Sif Mas_Instrucciones 
	//  | Sfrom Mas_Instrucciones
	
	public void cuerpo(List<Token> tokens) {
		String tipo = tokens.get(i).getTipo();
		switch (tipo) {
		// Souto Mas_Instrucciones:
		case "IMPRIMIR":
			Souto(tokens);
			Mas_Instrucciones(tokens);
			break;
		// Scomentario Mas_Instrucciones:
		case "COMENTARIO":
			SComentario(tokens);
			Mas_Instrucciones(tokens);
			break;
		// SDeclaracion Mas_Instrucciones:
		case "DECIMAL":
		case "ENTERO":
		case "BOLEANO":
		case "CARACTER":
		case "CADENA":
			SDeclaracion(tokens);
			Mas_Instrucciones(tokens);
			break;
		// SOperacion Mas_Instrucciones:
			
			
		// Sif Mas_Instrucciones 
		case "SI":
			Sif(tokens);
			Mas_Instrucciones(tokens);
			break;
			
		// Sfrom Mas_Instrucciones
		case "INICIO_FOR":
			Sfrom(tokens);
			Mas_Instrucciones(tokens);
			break;
			
		// ERROR:
		default:
			if (!(tokens.get(i).getTipo().equals("FIN_BLOQUE"))) {
				System.out.println("ERROR");
			}
			break;
		}
	}

	// Mas_Instrucciones -> cuerpo | E 
	public void Mas_Instrucciones(List<Token> tokens) {
		// cuerpo
		cuerpo(tokens);
	}
	
	public void SDeclaracion(List<Token> tokens) {
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
			if (tokens.get(i).getTipo().equals("VERDADERO") || tokens.get(i).getTipo().equals("FALSO")) {
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
			Mas_Instrucciones(tokens);
		}
	}
	
	public void SComentario(List<Token> tokens) {
		if (tokens.get(i).getTipo().equals("COMENTARIO")) {
			//System.out.println(tokens.get(i).getValor());
			i++;
			Mas_Instrucciones(tokens);
		} else {
			System.out.println("ERROR: Se esperaba # | Token recibido: " + tokens.get(i).getValor());
			Mas_Instrucciones(tokens);
		}
	}
	
	public void Souto(List<Token> tokens) {
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
			Mas_Instrucciones(tokens);
		}

	}

	public void Sfrom(List<Token> tokens) {

	}

	// S_if -> if ( Operador_NOT Condicion ) { cuerpo } Selse
	public void Sif(List<Token> tokens) {
		// Inicio del parentesis:
		if (tokens.get(i).getTipo().equals("PARENTESIS_APERTURA")) {
			System.out.println("(");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}

		// Operador NOT -> NOT | E
		if (tokens.get(i).getTipo().equals("OP_LOGICOS")) {
			System.out.println("NOT");
			i++;
		} else {
			// VACIO = E;
		}

		// Fin del parentesis:
		if (tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA")) {
			System.out.println(")");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}

		// Inicio de bloque:
		if (tokens.get(i).getTipo().equals("INICIO_BLOQUE")) {
			System.out.println("{");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba { | Token recibido: " + tokens.get(i).getValor());
		}

		// Cuerpo principal:
		cuerpo(tokens);

		// Fin del bloque:
		if (tokens.get(i).getTipo().equals("FIN_BLOQUE")) {
			System.out.println("}");
		}

		// S_Else
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
