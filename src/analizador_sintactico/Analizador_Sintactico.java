package analizador_sintactico;

import java.util.List;

import analizador_lexico.*;

public class Analizador_Sintactico {

	// Lista que contiene todos los tokens obtenidos desde el Lexico.
	List<Token> tokens;
	// Contador que permite tener el orden de los tokens leidos.
	int i;

	// Metodo inicial.
	public void analizar(Analizador_Lexico a_l) {
		System.out.println("==================================");
		System.out.println("Comienza el analizador Sintactico:");
		System.out.println("----------------------------------");
		// Obtenemos la lista de tokens.
		tokens = a_l.tokens;
		// Inicializamos el contador de tokens.
		i = 0;
		if (tokens.size() != 0) {
			// Comenzamos con la primer regla.
			try {
				Sstarto();
			} catch (Exception e) {
				System.out.println("ERROR: No cumple con la sintaxis");
			}
		}
		System.out.println("----------------------------------");
		System.out.println("Finaliza el analizador Sintactico:");
		System.out.println("==================================");
	}

	// Sstarto -> starto () {cuerpo}
	public void Sstarto() {
		// starto:
		if (getTokenType().equals("INICIO")) {
			System.out.print("starto");

		} else {
			System.out.println("ERROR: Se esperaba starto | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		// Parentesis:
		// (
		if (tokens.get(i).getTipo().equals("PARENTESIS_APERTURA")) {
			System.out.print("(");

		} else {
			System.out.println("\nERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}
		i++;

		// )
		while (!(tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA"))) {
			System.out.print("\nERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
			i++;
		}
		if (tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA")) {
			System.out.println(")");

		} else {
			System.out.println("\nERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		// Inicio del bloque:
		// {
		while (!(tokens.get(i).getTipo().equals("INICIO_BLOQUE"))) {
			System.out.println("ERROR: Se esperaba { | Token recibido: " + tokens.get(i).getValor());
			i++;
			// }
			if (getTokenType().equals("FIN_BLOQUE"))
				break;
			if (i <= tokens.size()) {
				i++;
				break;
			}
		}
		if (tokens.get(i).getTipo().equals("INICIO_BLOQUE")) {
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
			System.out.println("\nERROR: Se esperaba } | Token recibido: " + tokens.get(i).getValor());
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
		case "IDENTIFICADOR":
			SOperacion();
			Mas_Instrucciones();
			break;
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
			System.out.println("ERROR: Se esperaba tipo de dato, outo \n" + ", un identificador, if o from "
					+ "\n| Tolen recivido: " + tokens.get(i).getValor());
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

	// Declaracion -> Tipo_dato Identificador Asignacion ;
	public void SDeclaracion() {
		if (tokens.get(i).getTipo().equals("DECIMAL") || tokens.get(i).getTipo().equals("ENTERO")
				|| tokens.get(i).getTipo().equals("BOLEANO") || tokens.get(i).getTipo().equals("CADENA")
				|| tokens.get(i).getTipo().equals("CARACTER")) {
			System.out.print(tokens.get(i).getValor());
		} else {
			System.out.print(
					"\nERROR: Se esperaba string, dec, int, bool, char | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		if (tokens.get(i).getTipo().equals("IDENTIFICADOR")) {
			System.out.print(" " + tokens.get(i).getValor());
			i++;
		} else {
			System.out.print("\nERROR: Se esperaba IDENTIFICADOR | Token recibido: " + tokens.get(i).getValor());
		}
		Asignacion();
		Mas_declaraciones();
		if (tokens.get(i).getTipo().equals("FIN_SENTENCIA")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.print("\nERROR: Se esperaba ; | Token recibido: " + tokens.get(i).getValor() + "\n");
		}
	}

	// Asignacion -> = Valor | E
	public void Asignacion() {
		if (tokens.get(i).getTipo().equals("IGUAL")) {
			System.out.print(tokens.get(i).getValor());
			i++;
			Valor();
		}
	}

	// Valor -> NUMERO | VERDADERO | FALSO | Cadena de caracteres | CARACTER
	public void Valor() {
		String tipo = tokens.get(i).getTipo();
		switch (tipo) {
		case "VERDADERO":
		case "FALSO":
		case "Cadena de caracteres":
		case "CARACTER":
			System.out.print(tokens.get(i).getValor());
			i++;
			break;
		case "IDENTIFICADOR":
		case "NUMERO":
			Expresion_individual();
			break;
		default:
			System.out.println("ERROR. Se esperaba un numero o una cadena de caracteres "
					+ "o true o false o un caracter | Token recibido: " + tokens.get(i).getValor());
			break;
		}
	}

	// Mas_declaraciones -> , Identificador Asignacion Mas_declaraciones | E
	public void Mas_declaraciones() {
		if (tokens.get(i).getTipo().equals("COMA")) {
			System.out.print(tokens.get(i).getValor());
			i++;
			if (tokens.get(i).getTipo().equals("IDENTIFICADOR")) {
				System.out.print(" " + tokens.get(i).getValor());
				i++;
			} else {
				System.out.println("ERROR: Se esperaba IDENTIFICADOR | Token recibido: " + tokens.get(i).getValor());
			}
			Asignacion();
			Mas_declaraciones();
		}
	}

	// Souto -> outo (Cuerpo_outo) ;
	public void Souto() {
		if (getTokenType().equals("IMPRIMIR")) {
			System.out.print("\n   " + tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba outo | Token recibido: " + tokens.get(i).getValor());
		}
		if (getTokenType().equals("PARENTESIS_APERTURA")) {
			System.out.print(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}
		// CUERPO_OUTO
		Contenido();
		while (!(getTokenType().equals("PARENTESIS_CERRADURA"))) {
			System.out.print("ERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor() + "\n");
			i++;
		}
		if (getTokenType().equals("PARENTESIS_CERRADURA")) {
			System.out.print(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("\nERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		if (getTokenType().equals("FIN_SENTENCIA")) {
			System.out.print(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ; | Token recibido: " + tokens.get(i).getValor());
		}

	}

	// Contenido -> Identificador | Numero | Cadena_caracteres
	public void Contenido() {
		String tipo = getTokenType();
		switch (tipo) {
		case "IDENTIFICADOR":
		case "Cadena de caracteres":
		case "NUMERO":
		case "VERDADERO":
		case "FALSO":
			System.out.print(tokens.get(i).getValor());
			i++;
			Mas_Contenido();
			break;
		default:
			System.out
					.println("\n   ERROR: Se esperaba IDENTIFICADOR o NUMERO o Cadena de caracteres | Token recibido: "
							+ tokens.get(i).getTipo());
			break;
		}
	}

	// Mas_Contenido -> + Contenido | E
	public void Mas_Contenido() {
		String tipo = getTokenType();
		switch (tipo) {
		case "SUMA":
			System.out.print(tokens.get(i).getValor());
			i++;
			Contenido();
			break;
		default:
			break;
		}
	}

	// SOperacion -> Identificador = Expresion_individual ;
	public void SOperacion() {
		// Identificador:
		if (getTokenType().equals("IDENTIFICADOR")) {
			System.out.print("   " + tokens.get(i).getValor());
			i++;
			if (getTokenType().equals("IGUAL")) {
				System.out.print(tokens.get(i).getValor());
				i++;
				Expresion_individual();
			} else {
				i++;
				System.out.println("ERROR: Se esperaba = | Token recibido: " + tokens.get(i).getValor());
			}
			if (getTokenType().equals("FIN_SENTENCIA")) {
				System.out.println(tokens.get(i).getValor());
				i++;
			} else {
				System.out.println("ERROR: Se esperaba ; | Token recibido: " + tokens.get(i).getValor());
				i++;
			}

		}
	}

	// Sif -> if(Operador_NOT Condicion){cuerpo} Selse
	public void Sif() {
		// if:
		if (getTokenType().equals("SI")) {
			System.out.print("   " + "if");

		} else {
			System.out.println("ERROR: Se esperaba if | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		// Inicio del parentesis:
		if (getTokenType().equals("PARENTESIS_APERTURA")) {
			System.out.print("(");

		} else {
			System.out.println("ERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		// Operador NOT -> NOT | E
		if (getTokenType().equals("NO")) {
			System.out.print(tokens.get(i).getValor() + " ");
			i++;
		} else {
			// VACIO = E;
		}

		// Condicion:
		Condicion();

		// Fin del parentesis:
		if (getTokenType().equals("PARENTESIS_CERRADURA")) {
			System.out.println(")");
		} else {
			System.out.println("ERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		// Inicio de bloque:
		if (getTokenType().equals("INICIO_BLOQUE")) {
			System.out.println("   {");
		} else {
			System.out.println("ERROR: Se esperaba { | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		// Cuerpo principal:
		cuerpo();

		// Fin del bloque:
		if (getTokenType().equals("FIN_BLOQUE")) {
			System.out.println("\n   }");
		} else {
			System.out.println("ERROR: Se esperaba } | Token recibido: " + tokens.get(i).getValor());
		}
		i++;

		// Selse
		Selse();
	}

	// Condicion -> Expresion_individual Operador_relacional Expresion_individual
	// Mas_Condiciones
	public void Condicion() {

		Expresion_individual();
		Operador_relacional();
		Expresion_individual();
		Mas_Condiciones();
	}

	// Expresion_individual -> IdNum Expresion
	public void Expresion_individual() {

		String tipo = getTokenType();
		switch (tipo) {
		case "IDENTIFICADOR":
		case "NUMERO":
		case "VERDADERO":
		case "FALSO":
			System.out.print(tokens.get(i).getValor());
			i++;
			Expresion();
			break;
		default:
			System.out.println("ERROR: Se esperaba IDENTIFICADOR o NUMERO " + " | Token recibido: "
					+ tokens.get(i).getValor() + getTokenType());
			break;
		}

	}

	// Expresion -> Operador_aritmetico IdNum Mas_expresiones | E
	public void Expresion() {
		// Operador_aritmetico
		String tipo = getTokenType();
		switch (tipo) {
		case "SUMA":
		case "RESTA":
		case "MULTIPLICACION":
		case "DIVISION":
		case "MODULO":
			System.out.print(tokens.get(i).getValor());
			i++;
			// IdNum
			String tipo2 = getTokenType();
			switch (tipo2) {
			case "IDENTIFICADOR":
			case "NUMERO":
				System.out.print(tokens.get(i).getValor());
				i++;
				Mas_Expresiones();
				break;
			default:
				System.out.println("\nERROR: Se esperaba IDENTIFICADOR o NUMEROS | Token recibido: "
						+ tokens.get(i).getValor() + tokens.get(i).getTipo());
				break;
			}
		default:
			// Vacio
			break;
		}
	}

	// Mas_expresiones -> Expresion
	public void Mas_Expresiones() {
		Expresion();
	}

	// Operador_relacional -> ==|!=|<|>|<=|>=
	public void Operador_relacional() {
		// Operadores relacionales ==|!=|<|>|<=|>=
		String tipo = getTokenType();
		switch (tipo) {
		case "MENOR_QUE":
		case "MAYOR_QUE":
		case "MENOR_IGUAL_QUE":
		case "MAYOR_IGUAL_QUE":
		case "IGUAL_QUE":
		case "DIFERENTE_QUE":
			System.out.print(tokens.get(i).getValor());
			i++;
			break;
		default:
			System.out.println("ERROR: Se esperaba OPERADOR RELACIONAL " + " | Token recibido: "
					+ tokens.get(i).getValor() + getTokenType());
			break;
		}
	}

	// Mas_condiciones -> Operador_logico condicion | E
	public void Mas_Condiciones() {
		// Operadores logicos AND|OR
		if (getTokenType().equals("Y") || getTokenType().equals("O")) {
			System.out.println(tokens.get(i).getValor());
			i++;
			Condicion();
		} else {
			// Vacio;
		}
	}

	// Selse -> else {cuerpo} | E
	public void Selse() {
		// else:
		if (getTokenType().equals("DE_OTRA_FORMA")) {
			System.out.println("   else");
			i++;
			// Inicio de bloque:
			if (getTokenType().equals("INICIO_BLOQUE")) {
				System.out.println("   {");
			} else {
				System.out.println("ERROR: Se esperaba { | Token recibido: " + tokens.get(i).getValor());
			}
			i++;

			// Cuerpo principal:
			cuerpo();

			// Fin del bloque:
			if (getTokenType().equals("FIN_BLOQUE")) {
				System.out.println("\n   }");
			} else {
				System.out.println("ERROR: Se esperaba } | Token recibido: " + tokens.get(i).getValor());
			}
			i++;
		} else {
			// Vacio
		}
	}

	// Sfrom -> from (Condicion_Inicial) to (Condicion) inc (IdNum) {cuerpo}
	public void Sfrom() {
		if (getTokenType().equals("INICIO_FOR")) {
			System.out.print("   " + tokens.get(i).getValor());
		} else {
			System.out.println("\nERROR: Se esperaba outo | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		if (tokens.get(i).getTipo().equals("PARENTESIS_APERTURA")) {
			System.out.print(tokens.get(i).getValor());
		} else {
			System.out.println("\nERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		Condicion_Inicial();
		while (!(tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA"))) {
			System.out.print("\nERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor()+"\n");
			i++;
		} 
		if (tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA")) {
			System.out.print(tokens.get(i).getValor());
		} else {
			System.out.println("\nERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		if (tokens.get(i).getTipo().equals("FIN_FOR")) {
			System.out.print(tokens.get(i).getValor());
		} else {
			System.out.println("\nERROR: Se esperaba to | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		if (tokens.get(i).getTipo().equals("PARENTESIS_APERTURA")) {
			System.out.print(tokens.get(i).getValor());
		} else {
			System.out.println("ERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		Condicion();
		if (tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA")) {
			System.out.print(tokens.get(i).getValor());
		} else {
			System.out.println("ERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		if (tokens.get(i).getTipo().equals("INCREMENTO_FOR")) {
			System.out.print(tokens.get(i).getValor());
		} else {
			System.out.println("ERROR: Se esperaba inc | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		if (tokens.get(i).getTipo().equals("PARENTESIS_APERTURA")) {
			System.out.print(tokens.get(i).getValor());
		} else {
			System.out.println("ERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		if (tokens.get(i).getTipo().equals("IDENTIFICADOR") || (tokens.get(i).getTipo().equals("NUMERO"))) {
			System.out.print(tokens.get(i).getValor());
		} else {
			System.out.println("ERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		while (!(tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA"))) {
			System.out.println("\nERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
			i++;
		}
		if (tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA")) {
			System.out.print(tokens.get(i).getValor());
		} else {
			System.out.println("\nERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		if (tokens.get(i).getTipo().equals("INICIO_BLOQUE")) {
			System.out.println(tokens.get(i).getValor());
		} else {
			System.out.println("ERROR: Se esperaba { | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		cuerpo();
		if (tokens.get(i).getTipo().equals("FIN_BLOQUE")) {
			System.out.println(tokens.get(i).getValor());
		} else {
			System.out.println("ERROR: Se esperaba } | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
	}

	// Condicion_Inicial -> Expresion_individual| SDeclaracion
	public void Condicion_Inicial() {
		if (tokens.get(i).getTipo().equals("IDENTIFICADOR") || (tokens.get(i).getTipo().equals("NUMERO"))) {
			Expresion_individual();
		} else {
			if (tokens.get(i).getTipo().equals("IDENTIFICADOR")) {
				SDeclaracion();
				i--;
			}
			else
			{
				System.out.println("\nERROR: Se esperaba IDENTIFICADOR o EXPRESION | Token recibido: " + tokens.get(i).getValor());
			}
		}
	}

	// Metodo que recorre el siguiente token, evita los comentarios y retorna el
	// tipo del token
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

}
