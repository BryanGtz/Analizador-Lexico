package analizador_sintactico;

import java.util.List;

import analizador_lexico.*;

public class Analizador_Sintactico {

	List<Token> tokens;
	int i;

	public void analizar(Analizador_Lexico a_l) {
		tokens = a_l.tokens;
		i = 0;
		Sstarto();
	}

	// Sstarto -> starto () {cuerpo}
	public void Sstarto() {
		// starto:
		if (getTokenType().equals("INICIO")) {
			System.out.println("starto");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba starto | Token recibido: " + tokens.get(i).getValor());
		}
		// Parentesis:
		// (
		if (getTokenType().equals("PARENTESIS_APERTURA")) {
			System.out.println("(");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}
		// )
		if (getTokenType().equals("PARENTESIS_CERRADURA")) {
			System.out.println(")");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		// Inicio del bloque:
		// {
		if (getTokenType().equals("INICIO_BLOQUE")) {
			System.out.println("{");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba { | Token recibido: " + tokens.get(i).getValor());
		}
		// Cuerpo:
		cuerpo();
		// Fin del bloque:
		// }
		if (getTokenType().equals("FIN_BLOQUE")) {
			System.out.println("}");
		} else {
			System.out.println("ERROR: Se esperaba } | Token recibido: " + tokens.get(i).getValor());
		}
	}

	// cuerpo ->
	// Souto Mas_Instrucciones
	// | SDeclaracion Mas_Instrucciones
	// | SOperacion Mas_Instrucciones
	// | Sif Mas_Instrucciones
	// | Sfrom Mas_Instrucciones

	public void cuerpo() {
		String tipo = getTokenType();
		switch (tipo) {
		// Souto Mas_Instrucciones:
		case "IMPRIMIR":
			Souto();
			Mas_Instrucciones();
			break;
		// SDeclaracion Mas_Instrucciones:
		case "DECIMAL":
		case "ENTERO":
		case "BOLEANO":
		case "CARACTER":
		case "CADENA":
			SDeclaracion();
			Mas_Instrucciones();
			break;
		// SOperacion Mas_Instrucciones:

		// Sif Mas_Instrucciones
		case "SI":
			Sif();
			Mas_Instrucciones();
			break;

		// Sfrom Mas_Instrucciones
		case "INICIO_FOR":
			Sfrom();
			Mas_Instrucciones();
			break;

		// ERROR:
		default:
			System.out.println("ERROR");
			break;
		}
	}

	// Mas_Instrucciones -> cuerpo | E
	public void Mas_Instrucciones() {
		String tipo = getTokenType();
		switch (tipo) {
		case "IMPRIMIR":
		case "DECIMAL":
		case "ENTERO":
		case "BOLEANO":
		case "CARACTER":
		case "CADENA":
		case "SI":
		case "INICIO_FOR":
		case "IDENTIFICADOR":
			cuerpo();
		default:
			break;
		}

	}

	public void SDeclaracion() {
		System.out.println(tokens.get(i).getValor());

		i++;
		if (getTokenType().equals("IDENTIFICADOR")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba IDENTIFICADOR | Token recibido: " + tokens.get(i).getValor());
		}
		r_asignacion();

		if (getTokenType().equals("FIN_SENTENCIA")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ; | Token recibido: " + tokens.get(i).getValor());
			Mas_Instrucciones();
		}
	}

	public void r_asignacion() {
		String tipo = tokens.get(i).getTipo();
		if (tokens.get(i).getTipo().equals("IGUAL")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba IDENTIFICADOR | Token recibido: " + tokens.get(i).getValor());
		}
		switch (tipo) {
		case "DECIMAL":
			if (getTokenType().equals("NUMERO")) {
				System.out.println(tokens.get(i).getValor());
				i++;
			} else {
				System.out.println("ERROR: Se esperaba NUMEROS | Token recibido: " + tokens.get(i).getTipo());
			}
			break;
		case "ENTERO":
			if (getTokenType().equals("NUMERO")) {
				System.out.println(tokens.get(i).getValor());
				i++;
			} else {
				System.out.println("ERROR: Se esperaba NUMEROS | Token recibido: " + tokens.get(i).getTipo());
			}
			break;
		case "BOLEANO":
			if (getTokenType().equals("VERDADERO") || tokens.get(i).getTipo().equals("FALSO")) {
				System.out.println(tokens.get(i).getValor());
				i++;
			} else {
				System.out.println("ERROR: Se esperaba TRUE o FALSE | Token recibido: " + tokens.get(i).getTipo());
			}
			break;
		case "CARACTER":
			if (getTokenType().equals("Cadena de caracteres")) {
				System.out.println(tokens.get(i).getValor());
				i++;
			} else {
				System.out.println("ERROR: Se esperaba Cadena | Token recibido: " + tokens.get(i).getTipo());
			}
			break;
		case "CADENA":
			if (getTokenType().equals("Cadena de caracteres")) {
				System.out.println(tokens.get(i).getValor());
				i++;
			} else {
				System.out.println("ERROR: Se esperaba Cadena | Token recibido: " + tokens.get(i).getTipo());
			}
			break;
		}
	}

	public void Souto() {
		if (getTokenType().equals("IMPRIMIR")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba outo | Token recibido: " + tokens.get(i).getValor());
		}
		if (getTokenType().equals("PARENTESIS_APERTURA")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}
		// CUERPO_OUTO
		r_cuerpo_outo();
		if (getTokenType().equals("PARENTESIS_CERRADURA")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		if (getTokenType().equals("FIN_SENTENCIA")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ; | Token recibido: " + tokens.get(i).getValor());
			Mas_Instrucciones();
		}

	}

	public void Sfrom() {

	}

	// S_if -> if ( Operador_NOT Condicion ) { cuerpo } Selse
	public void Sif() {
		// Inicio del parentesis:
		if (getTokenType().equals("PARENTESIS_APERTURA")) {
			System.out.println("(");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}

		// Operador NOT -> NOT | E
		if (getTokenType().equals("OP_LOGICOS")) {
			System.out.println("NOT");
			i++;
		} else {
			// VACIO = E;
		}

		// Fin del parentesis:
		if (getTokenType().equals("PARENTESIS_CERRADURA")) {
			System.out.println(")");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}

		// Inicio de bloque:
		if (getTokenType().equals("INICIO_BLOQUE")) {
			System.out.println("{");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba { | Token recibido: " + tokens.get(i).getValor());
		}

		// Cuerpo principal:
		cuerpo();

		// Fin del bloque:
		if (getTokenType().equals("FIN_BLOQUE")) {
			System.out.println("}");
		}

		// S_Else
	}

	public void r_cuerpo_outo() {
		String tipo = getTokenType();
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

	public String getTokenType() {
		String tipo;
		if (!tokens.get(i).getTipo().equals("COMENTARIO")) {
			tipo = tokens.get(i).getTipo();
		} else {
			i++;
			tipo = getTokenType();
		}
		return tipo;
	}

	private void expresion() {

	}

}
