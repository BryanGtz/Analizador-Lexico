package analizador_sintactico;

import java.util.List;

import analizador_lexico.*;
import java.util.ArrayList;

public class Analizador_Sintactico {

	// Lista que contiene todos los tokens obtenidos desde el Lexico.
	List<Token> tokens;
	// Contador que permite tener el orden de los tokens leidos.
	int i;
	//
	public Arbol_Sintactico as;

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
				Nodo Sstarto = Sstarto();
				as = new Arbol_Sintactico(Sstarto);
			} catch (Exception e) {
				Nodo Sstarto = new Nodo("ERROR");
				as = new Arbol_Sintactico(Sstarto);
				System.out.println("ERROR: No cumple con la sintaxis");
			}
		}
		System.out.println("----------------------------------");
		System.out.println("Finaliza el analizador Sintactico:");
		System.out.println("==================================");
	}

	// Sstarto -> starto () {cuerpo}
	public Nodo Sstarto() {
        Nodo<String> regla = new Nodo("Sstarto");
		// starto:
		if (getTokenType().equals("INICIO")) {
			Nodo<Token> starto = new Nodo(tokens.get(i));
			regla.agregarHijo(starto);
			System.out.print("starto");
		} else {
			Nodo<String> starto = new Nodo("Error");
			regla.agregarHijo(starto);
			System.out.println("ERROR: Se esperaba starto | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		// Parentesis:
		// (
		if (tokens.get(i).getTipo().equals("PARENTESIS_APERTURA")) {
			Nodo<Token> p_a = new Nodo(tokens.get(i));
			regla.agregarHijo(p_a);
			System.out.print("(");
		} else {
			Nodo<String> p_a = new Nodo("Error");
			regla.agregarHijo(p_a);
			System.out.println("\nERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		// )
		while (!(tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA"))) {
			Nodo<String> p_c = new Nodo("Error");
			regla.agregarHijo(p_c);
			System.out.print("\nERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
			i++;
		}
		if (tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA")) {
			System.out.println(")");
			Nodo<Token> p_c = new Nodo(tokens.get(i));
			regla.agregarHijo(p_c);
		} else {
			Nodo<String> p_c = new Nodo("Error");
			regla.agregarHijo(p_c);
			System.out.println("\nERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		// Inicio del bloque:
		// {
		while (!(tokens.get(i).getTipo().equals("INICIO_BLOQUE"))) {
			System.out.println("ERROR: Se esperaba { | Token recibido: " + tokens.get(i).getValor());
			Nodo<String> i_b = new Nodo("Error");
			regla.agregarHijo(i_b);
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
			Nodo<Token> i_b = new Nodo(tokens.get(i));
			regla.agregarHijo(i_b);
			System.out.println("{");
			i++;
		} else {
			Nodo<String> i_b = new Nodo("Error");
			regla.agregarHijo(i_b);
			System.out.println("ERROR: Se esperaba { | Token recibido: " + tokens.get(i).getValor());
		}
        // Cuerpo:
        Nodo<String> cuerpo = cuerpo();
        regla.agregarHijo(cuerpo);
		// Fin del bloque:
		// }
		if (getTokenType().equals("FIN_BLOQUE")) {
                        Nodo<String> f_b = new Nodo(tokens.get(i));
                        regla.agregarHijo(f_b);
			System.out.println("}");
		} else {
                        Nodo<String> f_b = new Nodo("Error");
                        regla.agregarHijo(f_b);
			System.out.println("\nERROR: Se esperaba } | Token recibido: " + tokens.get(i).getValor());
		}
                return regla;
	}

	// cuerpo ->
	// Souto Mas_Instrucciones
	// | SDeclaracion Mas_Instrucciones
	// | SOperacion Mas_Instrucciones
	// | Sif Mas_Instrucciones
	// | Sfrom Mas_Instrucciones
	public Nodo cuerpo() {
		Nodo<String> regla = new Nodo("Cuerpo");
		String tipo = getTokenType();
		Nodo<String> mas_instrucciones;
		switch (tipo) {                        
		// Souto Mas_Instrucciones:
		case "IMPRIMIR":
			Nodo<String> souto = Souto();
			regla.agregarHijo(souto);
			mas_instrucciones = Mas_Instrucciones();
			regla.agregarHijo(mas_instrucciones);
			break;
		// SDeclaracion Mas_Instrucciones:
		case "DECIMAL":
		case "ENTERO":
		case "BOLEANO":
		case "CARACTER":
		case "CADENA":
			Nodo<String> Sdecla = SDeclaracion();
			regla.agregarHijo(Sdecla);
			mas_instrucciones = Mas_Instrucciones();
			regla.agregarHijo(mas_instrucciones);
			break;
		// SOperacion Mas_Instrucciones:
		case "IDENTIFICADOR":
			SOperacion();
			mas_instrucciones = Mas_Instrucciones();
			regla.agregarHijo(mas_instrucciones);
			break;
		// Sif Mas_Instrucciones
		case "SI":
			Sif();
			mas_instrucciones = Mas_Instrucciones();
			regla.agregarHijo(mas_instrucciones);
			break;

		// Sfrom Mas_Instrucciones
		case "INICIO_FOR":
			Sfrom();
			mas_instrucciones = Mas_Instrucciones();
			regla.agregarHijo(mas_instrucciones);
			break;
		// ERROR:
		default:
			System.out.println("ERROR: Se esperaba tipo de dato, outo \n" + ", un identificador, if o from "
					+ "\n| Tolen recivido: " + tokens.get(i).getValor());
			break;
		}
        return regla;
	}

	// Mas_Instrucciones -> cuerpo | E
	public Nodo Mas_Instrucciones() {
		Nodo<String> regla = new Nodo("Mas instrucciones");
		String tipo = getTokenType();
		// cuerpo
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
			Nodo<String> cuerpo = cuerpo();
			regla.agregarHijo(cuerpo);
			break;
		// E
		default:
			Nodo<String> vacio = new Nodo("Vacio");
			regla.agregarHijo(vacio);
			break;
		}
		return regla;
	}

	// Declaracion -> Tipo_dato Identificador Asignacion ;
	// MODIFICACION: Declaracion -> Tipo_dato Identificador Igual_Asignacion Mas_declaraciones ;
	// IGUAL_ASIGNACION -> = ASIGNACION | E
	public Nodo SDeclaracion() {
		// Creacion la raiz del arbol semantico:
		Nodo<String> regla = new Nodo("SDeclaracion");
		// -----------------------------------------
		// Tipo_dato:
		// Agregamos nodo hijo al arbol:
		Nodo<String> tipo_dato = new Nodo("Tipo_dato");
		regla.hijos.add(tipo_dato);
		// Obtenemos el tipo de dato:
		switch(tokens.get(i).getTipo()){
			case "DECIMAL":
			case "ENTERO":
			case "BOLEANO":
			case "CARACTER":
			case "CADENA":
				// Agregamos nodo hijo al arbol:
				Nodo<Token> t_d = new Nodo(tokens.get(i));
				tipo_dato.hijos.add(t_d);
				System.out.print("   "+tokens.get(i).getValor());
			break;
			default:
				// Agregamos nodo hijo al arbol:
                Nodo<String> t_d = new Nodo("Error");
                tipo_dato.agregarHijo(t_d);
				System.out.print("\nERROR: Se esperaba string, dec, int, bool, char | Token recibido: " + tokens.get(i).getValor());
				break;
		}
		i++;
		// -----------------------------------------
		// Identificador:
		if (tokens.get(i).getTipo().equals("IDENTIFICADOR")) {
			// Agregamos nodo hijo al arbol:
			Nodo<Token> id = new Nodo(tokens.get(i));
			regla.agregarHijo(id);
			System.out.print(" " + tokens.get(i).getValor());
			i++;
		} else {
			// Agregamos nodo hijo al arbol:
			Nodo<String> id = new Nodo("Error");
			regla.hijos.add(id);
			System.out.print("\nERROR: Se esperaba IDENTIFICADOR | Token recibido: " + tokens.get(i).getValor());
		}
		// --------------------------------------
		// Asignacion: CODIGO DE BRYAN
		//Nodo<String> asignacion = Asignacion();
		//regla.agregarHijo(asignacion);
		// -----------------------------------------
		// Igual_Asignacion <- = Asignacion | E
		if(tokens.get(i).getTipo().equals("IGUAL")){
			// Agregamos nodo hijo al arbol:
			Nodo<String> igual =  new Nodo("=");
			regla.agregarHijo(igual);
			System.out.print(" " + tokens.get(i).getValor());
			i++;
			// --------------------------------------
			// Asignacion:
			// Agregamos nodo hijo al arbol:
			Nodo<String> asignacion = Asignacion();
			regla.agregarHijo(asignacion);
		}
		// --------------------------------------
		// Mas_declaraciones:
		Nodo m_d = Mas_declaraciones();
		regla.agregarHijo(m_d);
		// -----------------------------------------
		// ;
		if (tokens.get(i).getTipo().equals("FIN_SENTENCIA")) {
			Nodo f_s = new Nodo(tokens.get(i));
			regla.agregarHijo(f_s);
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			Nodo f_s = new Nodo("Error");
			regla.agregarHijo(f_s);
			System.out.print("\nERROR: Se esperaba ; | Token recibido: " + tokens.get(i).getValor() + "\n");
		}
		return regla;
	}

	// Asignacion -> = Valor | E
	// MODIFICACION: Asignacion -> Valor | Expresion_individual | Expresion_Cadena
	public Nodo Asignacion() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> padre = new Nodo("Asignacion");
		// -----------------------------------------
		String tipo = tokens.get(i).getTipo();
		switch (tipo) {
			// -----------------------------------------
			// Valor -> VERDADERO | FALSO | CARACTER
			case "VERDADERO":
			case "FALSO":
			case "CARACTER":
				// Agregamos nodo hijo al arbol:
				Nodo<Token> valor = new Nodo(tokens.get(i));
				padre.add(valor);
				System.out.print("   "+tokens.get(i).getValor());
				i++;
				break;
			// -------------------------------
			// Expresion_individual -> Identificador_Numero Expresion
			case "IDENTIFICADOR":
			case "NUMERO":
				Nodo<String> hijo_expresion_individual = Expresion_individual();
				padre.agregarHijo(hijo_expresion_individual);
				break;
			// Expresion_Cadena:
			case "Cadena de caracteres":
				//Nodo<String> hijo_Expresion_cadena = Expresion_cadena();
				//padre.agregarHijo(hijo_Expresion_cadena);
				break;
			default:
				System.out.println("ERROR. Se esperaba un numero o una cadena de caracteres "
						+ "o true o false o un caracter | Token recibido: " + tokens.get(i).getValor());
				break;
		}
		// -----------------------------------------
        // Retornamos el sub-arbol generado
        return padre;
	}

	/*
		// Valor -> NUMERO | VERDADERO | FALSO | Cadena de caracteres | CARACTER
		public Nodo Valor() {
	                Nodo padre = new Nodo("Valor");
			String tipo = tokens.get(i).getTipo();
			switch (tipo) {
			case "VERDADERO":
			case "FALSO":
			case "Cadena de caracteres":
			case "CARACTER":
	                        Nodo hijo1 = new Nodo(tokens.get(i));
	                        padre.agregarHijo(hijo1);
				System.out.print(tokens.get(i).getValor());
				i++;
				break;
			case "IDENTIFICADOR":
			case "NUMERO":
	                        Nodo e_i = Expresion_individual();
	                        padre.agregarHijo(e_i);
				break;
			default:
	                        Nodo error = new Nodo("Error");
	                        error.agregarHijo(error);
				System.out.println("ERROR. Se esperaba un numero o una cadena de caracteres "
						+ "o true o false o un caracter | Token recibido: " + tokens.get(i).getValor());
				break;
			}
	                return padre;
		}
	*/

	// Expresion_individual -> IdNum Expresion
	public Nodo Expresion_individual() {
		Nodo padre = new Nodo("Expresion_individual");
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
                return padre;
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

	// Mas_declaraciones -> , Identificador Asignacion Mas_declaraciones | E
	public Nodo Mas_declaraciones() {
        Nodo<String> regla = new Nodo("Mas_declaraciones");
		if (tokens.get(i).getTipo().equals("COMA")) {
			Nodo<Token> coma = new Nodo(tokens.get(i));
			regla.agregarHijo(coma);
			System.out.print(tokens.get(i).getValor());
			i++;
			if (tokens.get(i).getTipo().equals("IDENTIFICADOR")) {
				Nodo<Token> id = new Nodo(tokens.get(i));
				regla.agregarHijo(id);
				System.out.print(" " + tokens.get(i).getValor());
				i++;
			} else {
				Nodo<String> error = new Nodo("Error");
				regla.agregarHijo(error);
				System.out.println("ERROR: Se esperaba IDENTIFICADOR | Token recibido: " + tokens.get(i).getValor());
			}
			Nodo asig = Asignacion();
			regla.agregarHijo(asig);
			Nodo m_d = Mas_declaraciones();
			regla.agregarHijo(m_d);
		}
		else{
			Nodo<String> vacio = new Nodo("Vacio");
			regla.agregarHijo(vacio);
		}
		return regla;
	}

	// Souto -> outo (Cuerpo_outo) ;
	public Nodo Souto() {
		Nodo<String> padre = new Nodo("Souto");
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
                return padre;
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
		if(getTokenType().equals("DE_OTRA_FORMA")) {
			// Selse			
			Selse();
		}
	}

	// Condicion -> Expresion_individual Operador_relacional Expresion_individual
	// Mas_Condiciones
	public void Condicion() {

		Expresion_individual();
		Operador_relacional();
		Expresion_individual();
		Mas_Condiciones();
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
			System.out.println("ERROR: Se esperaba else | Token recibido: "+tokens.get(i).getValor());
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
		i++;
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
			System.out.println("\n   "+tokens.get(i).getValor());
		} else {
			System.out.println("ERROR: Se esperaba } | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
	}

	// Condicion_Inicial -> Expresion_individual| SDeclaracion
	public void Condicion_Inicial() {
		if (tokens.get(i).getTipo().equals("IDENTIFICADOR") || (tokens.get(i).getTipo().equals("NUMERO"))) {
			Expresion_individual();
			i--;
		} else {
			if (tokens.get(i).getTipo().equals("DECIMAL") || tokens.get(i).getTipo().equals("ENTERO")
							|| tokens.get(i).getTipo().equals("BOLEANO") || tokens.get(i).getTipo().equals("CADENA")
							|| tokens.get(i).getTipo().equals("CARACTER")) {
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
