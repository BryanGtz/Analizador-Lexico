package analizador_sintactico;

import java.util.*;

import analizador_lexico.*;
import analizador_semantico.*;

public class Analizador_Sintactico {

	// Lista que contiene todos los tokens obtenidos desde el Lexico.
	List<Token> tokens;
	// Contador que permite tener el orden de los tokens leidos.
	int i;
	//
	public Arbol_Sintactico as;
	// Fila de errores
	Queue<String> errores = new LinkedList<>();
	//
	public Tabla_Simbolos tabla;

	// Metodo inicial.
	public void analizar(Analizador_Lexico a_l) {
		System.out.println("==================================");
		System.out.println("Comienza el analizador Sintactico:");
		System.out.println("----------------------------------");
		// Obtenemos la lista de tokens.
		tokens = a_l.tokens;
		// Inicializamos el contador de tokens.
		i = 0;
		//
		tabla = new Tabla_Simbolos();
		if (tokens.size() != 0) {
			// Comenzamos con la primer regla.
			try {
				Nodo<String> Sstarto = Sstarto();
				as = new Arbol_Sintactico(Sstarto);
			} catch (Exception e) {
				Nodo<String> Sstarto = new Nodo("ERROR");
				as = new Arbol_Sintactico(Sstarto);
				System.out.print("ERROR");
				errores.add("ERROR: No cumple con la sintaxis");
			}
		}
		System.out.println("\n----------------------------------");
		System.out.println("Finaliza el analizador Sintactico:");
		System.out.println("==================================");
		// Imprimimos los errores detectados
		System.err.println(errores.toString().replaceAll(",", "\n"));
	}

	// Sstarto -> starto () {cuerpo}
	public Nodo Sstarto() {
		Nodo<String> regla = new Nodo("Sstarto");
		
		// ------------------------------------------------
		// starto:
		if (getTokenType().equals("INICIO")) {
			Nodo<Token> starto = new Nodo(tokens.get(i));
			regla.agregarHijo(starto);
			System.out.print("starto");
		} else {
			Nodo<String> error = error("starto");
			regla.agregarHijo(error);
		}
		i++;
		
		// ------------------------------------------------
		// (
		if (tokens.get(i).getTipo().equals("PARENTESIS_APERTURA")) {
			Nodo<Token> p_a = new Nodo(tokens.get(i));
			regla.agregarHijo(p_a);
			System.out.print("(");
		} else {
			Nodo<String> error = error("(");
			regla.agregarHijo(error);
		}
		i++;
		
		// -----------------------------------------------
		// )
		while (!(tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA"))) {
			Nodo<String> error = error(")");
			regla.agregarHijo(error);
			i++;
		}
		if (tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA")) {
			System.out.println(")");
			Nodo<Token> p_c = new Nodo(tokens.get(i));
			regla.agregarHijo(p_c);
		} else {
			Nodo<String> error = error(")");
			regla.agregarHijo(error);
		}
		i++;
		
		// -------------------------------------------------
		// {
		while (!(tokens.get(i).getTipo().equals("INICIO_BLOQUE"))) {
			Nodo<String> error = error("{");
			regla.agregarHijo(error);
			i++;
			
			// -------------------------------------------------
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
			System.out.print("{");
			i++;
		}
		
		// ----------------------------------------------------
		// Cuerpo:
		Nodo<String> cuerpo = cuerpo();
		regla.agregarHijo(cuerpo);
		
		// ----------------------------------------------------
		// Fin del bloque:
		// }
		if (getTokenType().equals("FIN_BLOQUE")) {
			Nodo<Token> f_b = new Nodo(tokens.get(i));
			regla.agregarHijo(f_b);
			System.out.println("\n}");
		} else {
			Nodo<String> error = error("}");
			regla.agregarHijo(error);
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
		Nodo<String> regla = new Nodo("[Cuerpo]");	
		// ----------------------------------------
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
			Nodo<String> Soperacion = SOperacion();
			regla.agregarHijo(Soperacion);
			mas_instrucciones = Mas_Instrucciones();
			regla.agregarHijo(mas_instrucciones);
			break;
		// Sif Mas_Instrucciones
		case "SI":
			Nodo<String> Sif = Sif();
			regla.agregarHijo(Sif);
			mas_instrucciones = Mas_Instrucciones();
			regla.agregarHijo(mas_instrucciones);
			break;

		// Sfrom Mas_Instrucciones
		case "INICIO_FOR":
			Nodo<String> Sfrom = Sfrom();
			regla.agregarHijo(Sfrom);
			mas_instrucciones = Mas_Instrucciones();
			regla.agregarHijo(mas_instrucciones);
			break;

		// ERROR:
		default:
			Nodo<String> error = error("Declaracion | Operacion | If | Else | From ");
			regla.agregarHijo(error);
			break;
		}
		return regla;
	}

	// Mas_Instrucciones -> cuerpo | E
	public Nodo Mas_Instrucciones() {
		Nodo<String> regla = new Nodo("[Mas_instrucciones]");
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
			vacio.setEsTerminal(true);
			regla.agregarHijo(vacio);
			break;
		}
		return regla;
	}

	// Declaracion -> Tipo_dato Identificador Igual_Asignacion Mas_declaraciones ;
	// IGUAL_ASIGNACION -> = ASIGNACION | E
	public Nodo SDeclaracion() {
		// Creacion la raiz del arbol semantico:
		Nodo<String> regla = new Nodo("SDeclaracion");
		// -----------------------------------------
		// Tipo_dato:
		// Agregamos nodo hijo al arbol:
		Nodo<String> tipo_dato = new Nodo("Tipo_dato");
		regla.agregarHijo(tipo_dato);
		// Obtenemos el tipo de dato:
		String tipo = getTokenType();
		// -------------------------------------------
		// cuerpo
		switch (tipo) {
		case "DECIMAL":
		case "ENTERO":
		case "BOLEANO":
		case "CADENA":
			// Agregamos nodo hijo al arbol:
			Nodo<Token> t_d = new Nodo(tokens.get(i));
			tipo_dato.agregarHijo(t_d);
			System.out.print("\n   " + tokens.get(i).getValor());
			break;
		default:
			// Agregamos nodo hijo al arbol:
			Nodo<String> error = error(" dec | int | bool | String");
			tipo_dato.agregarHijo(error);
			break;
		}
		i++;
		// -----------------------------------------
		// Identificador:
		Nodo<Token> id = new Nodo();
		if (tokens.get(i).getTipo().equals("IDENTIFICADOR")) {
			// Agregamos nodo hijo al arbol:
			id = new Nodo(tokens.get(i));
			// --------------------------
			// Agregar hermano derecho ->
			tipo_dato.setHermano(id);
			// --------------------------
			regla.agregarHijo(id);
			System.out.print(" " + tokens.get(i).getValor());
			// --------------------------
			// Agregamos la variable a la tabla de simbolos
			tabla.agregarVariable(tokens.get(i).getValor());
			i++;
		} else {
			// Agregamos nodo hijo al arbol:
			Nodo<String> error = error(" IDENTIFICADOR");
			regla.agregarHijo(error);
		}
		// --------------------------------------
		// Asignacion: CODIGO DE BRYAN
		// Nodo<String> asignacion = Asignacion();
		// regla.agregarHijo(asignacion);
		// -----------------------------------------
		// Igual_Asignacion <- = Asignacion | E
		Nodo<String> i_a = Igual_Asignacion();
		// --------------------------
		// Agregar hermano derecho ->
		id.setHermano(i_a);
		// --------------------------
		regla.agregarHijo(i_a);
		// id.setHermano(i_a);
		// --------------------------------------
		// Mas_declaraciones:
		Nodo<String> m_d = Mas_declaraciones();
		// --------------------------
		// Agregar hermano derecho ->
		i_a.setHermano(m_d);
		// --------------------------
		regla.agregarHijo(m_d);
		// -----------------------------------------
		// ;
		if (tokens.get(i).getTipo().equals("FIN_SENTENCIA")) {
			Nodo<Token> f_s = new Nodo(tokens.get(i));
			regla.agregarHijo(f_s);
			// --------------------------
			// Agregar hermano derecho ->
			m_d.setHermano(f_s);
			// --------------------------
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			Nodo<String> error = error(" ;");
			regla.agregarHijo(error);
		}
		return regla;
	}

	// Igual_Asignacion <- = Asignacion | E
	public Nodo Igual_Asignacion() {
		//
		Nodo<String> padre = new Nodo("[Igual_Asignacion]");
		if (tokens.get(i).getTipo().equals("IGUAL")) {
			// Agregamos nodo hijo al arbol:
			Nodo<Token> igual = new Nodo(tokens.get(i));
			padre.agregarHijo(igual);
			System.out.print(" " + tokens.get(i).getValor());
			i++;
			// --------------------------------------
			// Asignacion:
			// Agregamos nodo hijo al arbol:
			Nodo<String> asignacion = Asignacion();
			// --------------------------
			// Agregar hermano derecho ->
			igual.setHermano(asignacion);
			// --------------------------
			padre.agregarHijo(asignacion);
		} else {
			// Vacio
			Nodo<String> vacio = new Nodo("Vacio");
			vacio.setEsTerminal(true);
			padre.agregarHijo(vacio);
		}
		return padre;
	}

	// Asignacion -> = Valor | E
	// MODIFICACION: Asignacion -> Valor | Expresion_individual | Expresion_Cadena
	public Nodo Asignacion() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> padre = new Nodo("Asignacion");
		// -----------------------------------------
		String tipo = getTokenType();
		switch (tipo) {
		// -----------------------------------------
		// Valor -> VERDADERO | FALSO | CARACTER
		case "VERDADERO":
		case "FALSO":
		case "CARACTER":
			// Agregamos nodo hijo al arbol:
			Nodo<String> valor = Valor();
			padre.agregarHijo(valor);
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
			Nodo<String> hijo_Expresion_cadena = Expresion_cadena();
			padre.agregarHijo(hijo_Expresion_cadena);
			break;
		default:
			Nodo<String> error = error(" numero | identificador | cadena de caracteres | true | false ");
			padre.agregarHijo(error);
			break;
		}
		// -----------------------------------------
		// Retornamos el sub-arbol generado
		return padre;
	}

	// Expresion_cadena -> Cadena Mas_cadenas
	public Nodo Expresion_cadena() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> padre = new Nodo("Expresion_cadena");
		// -----------------------------------------
		String tipo = getTokenType();
		switch (tipo) {
		// Expresion_Cadena:
		case "Cadena de caracteres":
			// -----------------------------------------
			// Cadena
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			padre.agregarHijo(hijo1);
			System.out.print(" " + tokens.get(i).getValor());
			i++;
			// -----------------------------------------
			// Mas_cadenas:
			Nodo<Token> mas_cadenas = Mas_cadenas();
			padre.agregarHijo(mas_cadenas);
			break;
		default:
			Nodo<String> error = error(" cadena de caracteres");
			padre.agregarHijo(error);
			i++;
			break;
		}

		return padre;
	}

	// Mas_cadenas -> + Expresion_cadena | E
	public Nodo Mas_cadenas() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> padre = new Nodo("Mas_cadenas");
		// -----------------------------------------
		// +
		String tipo = getTokenType();
		switch (tipo) {
		case "SUMA":
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			padre.agregarHijo(hijo1);
			System.out.print(tokens.get(i).getValor());
			i++;
			// -----------------------------------------
			// Expresion_cadena
			Nodo<Token> hijo2 = Expresion_cadena();
			padre.agregarHijo(hijo2);
			break;
		default:
			// Vacio
			Nodo<String> vacio = new Nodo("Vacio");
			vacio.setEsTerminal(true);
			padre.agregarHijo(vacio);
			break;
		}
		return padre;
	}

	// Valor -> NUMERO | VERDADERO | FALSO | Cadena de caracteres | CARACTER
	// MODIFICACION: Valor -> VERDADERO | FALSO | CARACTER
	public Nodo Valor() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> padre = new Nodo("Valor");
		// -----------------------------------------
		String tipo = getTokenType();
		switch (tipo) {
		case "VERDADERO":
		case "FALSO":
		case "CARACTER":
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			padre.agregarHijo(hijo1);
			System.out.print(tokens.get(i).getValor());
			i++;
			break;
		default:
			Nodo<String> error = error(" TRUE | FALSE");
			padre.agregarHijo(error);
			break;
		}
		return padre;
	}

	// Expresion_individual -> IdNum Expresion
	public Nodo Expresion_individual() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> padre = new Nodo("Expresion_individual");
		// -----------------------------------------
		String tipo = getTokenType();
		switch (tipo) {
		case "IDENTIFICADOR":
		case "NUMERO":
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			padre.agregarHijo(hijo1);
			System.out.print(" "+tokens.get(i).getValor());
			
			// --------------------------
			// Agregamos la variable a la tabla de simbolos
			tabla.agregarVariable(tokens.get(i).getValor());
			
			i++;
			// Agregamos nodo hijo al arbol:
			Nodo<Token> expresion = Expresion();
			// --------------------------
			// Agregar hermano derecho ->
			hijo1.setHermano(expresion);
			// --------------------------
			padre.agregarHijo(expresion);
			
			break;
		default:
			Nodo<String> error = error(" IDENTIFICADOR | NUMERO");
			padre.agregarHijo(error);
			break;
		}
		return padre;
	}

	// Expresion -> Operador_aritmetico IdNum Mas_expresiones | E
	public Nodo Expresion() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> padre = new Nodo("Expresion");
		// -----------------------------------------
		// Operador_aritmetico
		String tipo = getTokenType();
		switch (tipo) {
		case "SUMA":
		case "RESTA":
		case "MULTIPLICACION":
		case "DIVISION":
		case "MODULO":
			Nodo<Token> hijo1 = new Nodo("Operador");
			//hijo1.setOperador(tokens.get(i));
			padre.agregarHijo(hijo1);
			System.out.print(tokens.get(i).getValor());
			i++;
			// -----------------------------------------
			// IdNum
			String tipo2 = getTokenType();
			switch (tipo2) {
			case "IDENTIFICADOR":
			case "NUMERO":
				Nodo<Token> idnum = new Nodo("IdNum");
				padre.agregarHijo(idnum);
				// --------------------------
				// Agregar hermano derecho ->
				hijo1.setHermano(idnum);
				// --------------------------
				System.out.print(tokens.get(i).getValor());
				// --------------------------
				// Agregamos la variable a la tabla de simbolos
				tabla.agregarVariable(tokens.get(i).getValor());
				i++;
				// -----------------------------------
				// Mas_Expresiones
				Nodo<String> hijo3 = Mas_Expresiones();
				// --------------------------
				// Agregar hermano derecho ->
				idnum.setHermano(hijo3);
				// --------------------------
				padre.agregarHijo(hijo3);
				// System.out.print(tokens.get(i).getValor());
				// i++;
				break;
			default:
				Nodo<String> error = error(" IDENTIFICADOR | NUMERO");
				padre.agregarHijo(error);
				i++;
				// -----------------------------------
				// Mas_Expresiones
				Nodo<String> hijo4 = Mas_Expresiones();
				padre.agregarHijo(hijo4);
				break;
			}
		break;
		default:
			// Vacio
			Nodo<String> vacio = new Nodo("Vacio");
			vacio.setEsTerminal(true);
			padre.agregarHijo(vacio);
			break;
		}
		return padre;
	}

	// Mas_expresiones -> Expresion
	public Nodo Mas_Expresiones() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> padre = new Nodo("Mas_Expresion");
		// padre.agregarHijo(padre);
		// -----------------------------------------
		Nodo<String> expresion = Expresion();
		padre.agregarHijo(expresion);
		// -----------------------------------------
		return padre;
	}

	// Mas_declaraciones -> , Identificador Asignacion Mas_declaraciones | E
	public Nodo Mas_declaraciones() {
		Nodo<String> regla = new Nodo("Mas_declaraciones");
		// ------------------------------------------------
		// ,
		Nodo<Token> id= new Nodo();
		if (tokens.get(i).getTipo().equals("COMA")) {
			Nodo<Token> coma = new Nodo(tokens.get(i));
			regla.agregarHijo(coma);
			System.out.print(tokens.get(i).getValor());
			i++;
			// ------------------------------------------------
			// Identificador:
			if (tokens.get(i).getTipo().equals("IDENTIFICADOR")) {
				id = new Nodo(tokens.get(i));
				coma.setHermano(id);
				regla.agregarHijo(id);
				System.out.print(" " + tokens.get(i).getValor());
				// --------------------------
				// Agregamos la variable a la tabla de simbolos
				tabla.agregarVariable(tokens.get(i).getValor());
				i++;
			} else {
				Nodo<String> error = error(" IDENTIFICADOR");
				regla.agregarHijo(error);
			}
			// ------------------------------------------------
			// Asignacion
			Nodo<String> asig = Igual_Asignacion();
			id.setHermano(asig);
			regla.agregarHijo(asig);
			// ------------------------------------------------
			// Mas_declaraciones
			Nodo<String> m_d = Mas_declaraciones();
			asig.setHermano(m_d);
			regla.agregarHijo(m_d);
		}
		// ------------------------------------------------
		// Vacio
		else {
			Nodo<String> vacio = new Nodo("Vacio");
			vacio.setEsTerminal(true);
			regla.agregarHijo(vacio);
		}
		return regla;
	}

	// Souto -> outo (Cuerpo_outo) ;
	public Nodo Souto() {
		Nodo<String> padre = new Nodo("[Souto]");
		Nodo<Token> outo = new Nodo();
		Nodo<Token> p_a = new Nodo();
		Nodo<Token> p_c = new Nodo();
		// ----------------------------------------------------------
		// outo:
		if (getTokenType().equals("IMPRIMIR")) {
			outo = new Nodo(tokens.get(i));
			padre.agregarHijo(outo);
			System.out.print("\n   " + tokens.get(i).getValor());
			i++;
		} else {
			Nodo<String> error = error(" OUTO");
			padre.agregarHijo(error);
		}
		
		// ----------------------------------------------------------
		// (:
		if (getTokenType().equals("PARENTESIS_APERTURA")) {
			p_a = new Nodo(tokens.get(i));
			padre.agregarHijo(p_a);
			outo.setHermano(p_a);
			System.out.print(tokens.get(i).getValor());
			i++;
		} else {
			Nodo<String> error = error(" (");
			padre.agregarHijo(error);
		}
		
		// ----------------------------------------------------------
		// CUERPO_OUTO
		Nodo<String> contenido = Contenido();
		p_a.setHermano(contenido);
		padre.agregarHijo(contenido);
		
		// ----------------------------------------------------------
		// ):
		while (!(getTokenType().equals("PARENTESIS_CERRADURA"))) {
			Nodo<String> error = error(" )");
			padre.agregarHijo(error);
			i++;
		}
		if (getTokenType().equals("PARENTESIS_CERRADURA")) {
			p_c = new Nodo(tokens.get(i));
			contenido.setHermano(p_c);
			padre.agregarHijo(p_c);
			System.out.print(tokens.get(i).getValor());
			i++;
		} else {
			Nodo<String> error = error(" )");
			padre.agregarHijo(error);
		}
		
		// ----------------------------------------------------------
		// ;:
		if (getTokenType().equals("FIN_SENTENCIA")) {
			Nodo<Token> f_s = new Nodo(tokens.get(i));
			padre.agregarHijo(f_s);
			System.out.print(tokens.get(i).getValor());
			p_c.setHermano(f_s);
			i++;
		} else {
			Nodo<String> error = error(" ;");
			padre.agregarHijo(error);
		}
		return padre;

	}

	// Contenido -> Contenido_outo Mas_Contenido
	// Contenido_outo -> Identificador | Numero | Cadena_caracteres | VERDADERO | FALSO
	public Nodo Contenido() {
		Nodo<String> regla = new Nodo("[Contenido]");
		String tipo = getTokenType();
		switch (tipo) {
		case "IDENTIFICADOR":
		case "Cadena de caracteres":
		case "NUMERO":
		case "VERDADERO":
		case "FALSO":
			System.out.print(tokens.get(i).getValor());
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);
			if(tipo=="IDENTIFICADOR") {
				// --------------------------
				// Agregamos la variable a la tabla de simbolos
				tabla.agregarVariable(tokens.get(i).getValor());
			}
			i++;
			// ------------------------------------------------
			// Mas_contenido
			Nodo<String> hijo2 = Mas_Contenido();
			regla.agregarHijo(hijo2);
			break;
		default:
			Nodo<String> error = error("IDENTIFICADOR | NUMERO | CADENA DE CARACTERES");
			regla.agregarHijo(error);
			break;
		}
		return regla;
	}

	// Mas_Contenido -> + Contenido | E
	public Nodo Mas_Contenido() {
		Nodo<String> regla = new Nodo("[Contenido]");
		String tipo = getTokenType();
		switch (tipo) {
		case "SUMA":
			System.out.print(tokens.get(i).getValor());
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);
			i++;
			// ------------------------------------------------
			// Mas_contenido
			Nodo<String> hijo2 = Contenido();
			regla.agregarHijo(hijo2);
			break;
		default:
			// ------------------------------------------------
			// Vacio
			Nodo<String> vacio = new Nodo("Vacio");
			vacio.setEsTerminal(true);
			regla.agregarHijo(vacio);
			break;
		}
		return regla;
	}

	// SOperacion -> Identificador = Expresion_individual ;
	public Nodo SOperacion() {
		Nodo<String> regla = new Nodo("SOperacion");
		// ---------------------------------------------------
		// Identificador:
		if (getTokenType().equals("IDENTIFICADOR")) {
			System.out.print(" " + tokens.get(i).getValor());
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);
			tabla.agregarVariable(tokens.get(i).getValor());
			i++;
			
			// -----------------------------------------------
			// =
			if (getTokenType().equals("IGUAL")) {
				System.out.print(tokens.get(i).getValor());
				Nodo<Token> hijo2 = new Nodo(tokens.get(i));
				regla.agregarHijo(hijo2);
				i++;
				
				// -------------------------------------------
				// Expresion_individual
				Nodo<String> hijo3 = Expresion_individual();
				regla.agregarHijo(hijo3);
			} else {
				i++;
				Nodo<String> error = error(" =");
				regla.agregarHijo(error);
			}
			
			// -----------------------------------------------
			// ;
			if (getTokenType().equals("FIN_SENTENCIA")) {
				System.out.print(tokens.get(i).getValor());
				Nodo<Token> hijo4 = new Nodo(tokens.get(i));
				regla.agregarHijo(hijo4);
				i++;
			} else {
				Nodo<String> error = error(" ;");
				regla.agregarHijo(error);
				i++;
			}

		}
		return regla;
	}

	// Sif -> if(Operador_NOT Condicion){cuerpo} Selse
	public Nodo Sif() {
		Nodo<String> regla = new Nodo("SIf");
		// ------------------------------------------------
		// if:
		if (getTokenType().equals("SI")) {
			System.out.print(" " + tokens.get(i).getValor());
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);

		} else {
			Nodo<String> error = error(" if");
			regla.agregarHijo(error);
		}
		i++;
		// ------------------------------------------------
		// Inicio del parentesis:
		if (getTokenType().equals("PARENTESIS_APERTURA")) {
			System.out.print(" " + tokens.get(i).getValor());
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);
		} else {
			Nodo<String> error = error(" (");
			regla.agregarHijo(error);
		}
		i++;
		// ------------------------------------------------
		// Operador NOT -> NOT | E
		if (getTokenType().equals("NOT")) {
			System.out.print("NOT ");
			Nodo<String> hijo1 = new Nodo("[NOT]");
			regla.agregarHijo(hijo1);

			Nodo<Token> hijo2 = new Nodo(tokens.get(i));
			hijo1.agregarHijo(hijo2);
			i++;
		} else {
			// ------------------------------------------------
			// Vacio
			Nodo<String> hijo1 = new Nodo("[NOT]");
			regla.agregarHijo(hijo1);
			
			Nodo<String> vacio = new Nodo("Vacio");
			vacio.setEsTerminal(true);
			hijo1.agregarHijo(vacio);
		}
		// ------------------------------------------------
		// Condicion:
		Nodo<String> hijo3 = Condicion();
		regla.agregarHijo(hijo3);
		// ------------------------------------------------
		// Fin del parentesis:
		if (getTokenType().equals("PARENTESIS_CERRADURA")) {
			System.out.print(" " + tokens.get(i).getValor());
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);
		} else {
			Nodo<String> error = new Nodo("Error");
			regla.agregarHijo(error);
			System.out.println(" ERROR");
			errores.add("ERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		// ------------------------------------------------
		// Inicio de bloque:
		if (getTokenType().equals("INICIO_BLOQUE")) {
			System.out.print(" " + tokens.get(i).getValor());
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);
		} else {
			Nodo<String> error = new Nodo("Error");
			regla.agregarHijo(error);
			System.out.println(" ERROR");
			errores.add("ERROR: Se esperaba { | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		// ------------------------------------------------
		// Cuerpo principal:
		Nodo<String> hijo7 = cuerpo();
		regla.agregarHijo(hijo7);
		// ------------------------------------------------
		// Fin del bloque:
		if (getTokenType().equals("FIN_BLOQUE")) {
			System.out.print(" " + tokens.get(i).getValor());
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);
		} else {
			Nodo<String> error = new Nodo("Error");
			regla.agregarHijo(error);
			System.out.println(" ERROR");
			errores.add("ERROR: Se esperaba } | Token recibido: " + tokens.get(i).getValor());
		}
		i++;
		// ------------------------------------------------
		// Selse:
		if (getTokenType().equals("DE_OTRA_FORMA")) {
			// --------------------------------------------
			// Selse
			Nodo<String> hijo8 = Selse();
			regla.agregarHijo(hijo8);
		}
		return regla;
	}

	// Condicion -> Expresion_individual Operador_relacional Expresion_individual Mas_Condiciones
	public Nodo Condicion() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> regla = new Nodo("[Condicion]");
		// -----------------------------------------
		// Expresion_individual
		Nodo<String> hijo1 = Expresion_individual();
		regla.agregarHijo(hijo1);
		// -----------------------------------------
		// Operador_relacional
		Nodo<String> hijo2 = Operador_relacional();
		regla.agregarHijo(hijo2);
		// -----------------------------------------
		// Expresion_individual
		Nodo<String> hijo3 = Expresion_individual();
		regla.agregarHijo(hijo3);
		// -----------------------------------------
		// Expresion_individual
		Nodo<String> hijo4 = Mas_Condiciones();
		regla.agregarHijo(hijo4);	

		return regla;
	}

	// Operador_relacional -> ==|!=|<|>|<=|>=
	public Nodo Operador_relacional() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> regla = new Nodo("Operador_relacional");
		// -----------------------------------------
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
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);
			i++;
			break;
		default:
			Nodo<String> error = error(" OPERADOR RELACIONAL");
			regla.agregarHijo(error);
			break;
		}
		return regla;
	}

	// Mas_condiciones -> Operador_logico condicion | E
	public Nodo Mas_Condiciones() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> regla = new Nodo("Mas_Condiciones");
		// -----------------------------------------
		// Operadores logicos AND|OR
		if (getTokenType().equals("Y") || getTokenType().equals("O")) {
			System.out.print(" "+tokens.get(i).getValor());
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);
			i++;
			Nodo<Token> hijo2 = Condicion();
			regla.agregarHijo(hijo2);
		} else {
			// ------------------------------------------------
			// Vacio
			Nodo<String> vacio = new Nodo("Vacio");
			vacio.setEsTerminal(true);
			regla.agregarHijo(vacio);
		}
		return regla;
	}

	// Selse -> else {cuerpo} | E
	public Nodo Selse() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> regla = new Nodo("Selse");
		// -----------------------------------------
		// else:
		if (getTokenType().equals("DE_OTRA_FORMA")) {
			//System.out.println("   else");
			System.out.print(tokens.get(i).getValor());
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);
			i++;
			// Inicio de bloque:
			if (getTokenType().equals("INICIO_BLOQUE")) {
				//System.out.println("   {");
				System.out.print(tokens.get(i).getValor());
				Nodo<Token> hijo2 = new Nodo(tokens.get(i));
				regla.agregarHijo(hijo2);
			} else {
				Nodo<String> error = error(" {");
				regla.agregarHijo(error);
			}
			i++;

			// Cuerpo principal:
			Nodo<String> hijo3 = cuerpo();
			regla.agregarHijo(hijo3);

			// Fin del bloque:
			if (getTokenType().equals("FIN_BLOQUE")) {
				//System.out.println("\n   }");
				System.out.print(tokens.get(i).getValor());
				Nodo<Token> hijo2 = new Nodo(tokens.get(i));
				regla.agregarHijo(hijo2);
			} else {
				Nodo<String> error = error(" }");
				regla.agregarHijo(error);
			}
			i++;
		} else {
			Nodo<String> error = error(" ELSE");
			regla.agregarHijo(error);
		}
		return regla;
	}

	// Sfrom -> from (Condicion_Inicial) to (Condicion) inc (IdNum) {cuerpo}
	public Nodo Sfrom() {
		//-------------------------------------------
		Nodo<String> regla = new Nodo("Sfrom");
		
		//-------------------------------------------
		// from:
		if (getTokenType().equals("INICIO_FOR")) {
			System.out.print("   "+tokens.get(i).getValor());
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);
		} else {
			Nodo<String> error = error(" outo");
			regla.agregarHijo(error);
		}
		i++;
		
		// ------------------------------------------
		// (
		if (tokens.get(i).getTipo().equals("PARENTESIS_APERTURA")) {
			System.out.print(" "+tokens.get(i).getValor());
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);
		} else {
			Nodo<String> error = error(" (");
			regla.agregarHijo(error);
		}
		i++;
		// ------------------------------------------
		// Condicion_Inicial
		Nodo<Token> hijo1 = Condicion_Inicial();
		regla.agregarHijo(hijo1);
		// ------------------------------------------
		// )
		if (tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA")) {
			System.out.print(" "+tokens.get(i).getValor());
			Nodo<Token> hijo2 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo2);
		} else {
			Nodo<String> error = error(" )");
			regla.agregarHijo(error);
		}
		i++;
		
		// ------------------------------------------
		// to
		if (tokens.get(i).getTipo().equals("FIN_FOR")) {
			System.out.print(" "+tokens.get(i).getValor());
			Nodo<Token> hijo2 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo2);
		} else {
			Nodo<String> error = error(" to");
			regla.agregarHijo(error);
		}
		i++;
		
		// ------------------------------------------
		// (
		if (tokens.get(i).getTipo().equals("PARENTESIS_APERTURA")) {
			System.out.print(" "+tokens.get(i).getValor());
			Nodo<Token> hijo3 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo3);
		} else {
			Nodo<String> error = error(" (");
			regla.agregarHijo(error);
		}
		i++;
		
		// -------------------------------------------------------------
		// Condicion
		Nodo<Token> hijo2 = Condicion();
		regla.agregarHijo(hijo2);
		
		// -------------------------------------------------------------
		// )
		if (tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA")) {
			System.out.print(" "+tokens.get(i).getValor());
			Nodo<Token> hijo4 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo4);
		} else {
			Nodo<String> error = error(" )");
			regla.agregarHijo(error);
		}
		i++;
		
		// ------------------------------------------------------------
		// Incremento
		if (tokens.get(i).getTipo().equals("INCREMENTO_FOR")) {
			System.out.print(" "+tokens.get(i).getValor());
			Nodo<Token> hijo4 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo4);
		} else {
			Nodo<String> error = error(" inc");
			regla.agregarHijo(error);
		}
		i++;
		
		// --------------------------------------------------------------
		// (
		if (tokens.get(i).getTipo().equals("PARENTESIS_APERTURA")) {
			System.out.print(" "+tokens.get(i).getValor());
			Nodo<Token> hijo4 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo4);
		} else {
			Nodo<String> error = error(" (");
			regla.agregarHijo(error);
		}
		i++;
		
		// ---------------------------------------------------------------
		// IdNum
		if (tokens.get(i).getTipo().equals("IDENTIFICADOR") || (tokens.get(i).getTipo().equals("NUMERO"))) {
			System.out.print(" "+tokens.get(i).getValor());
			Nodo<Token> hijo4 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo4);
		} else {
			Nodo<String> error = error(" )");
			regla.agregarHijo(error);
		}
		i++;
		
		// ----------------------------------------------------------------
		// )
		while (!(tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA"))) {
			Nodo<String> error = error(" )");
			regla.agregarHijo(error);
			i++;
		}
		if (tokens.get(i).getTipo().equals("PARENTESIS_CERRADURA")) {
			System.out.print(" "+tokens.get(i).getValor());
			Nodo<Token> hijo4 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo4);
		} else {
			Nodo<String> error = error(" )");
			regla.agregarHijo(error);
		}
		i++;
		
		// ---------------------------------------------------------------
		// {
		if (tokens.get(i).getTipo().equals("INICIO_BLOQUE")) {
			System.out.print(" "+tokens.get(i).getValor());
			Nodo<Token> hijo4 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo4);
		} else {
			Nodo<String> error = error(" {");
			regla.agregarHijo(error);
		}
		i++;
		
		// ----------------------------------------------------------------
		// Cuerpo
		Nodo<Token> hijo5 = cuerpo();
		regla.agregarHijo(hijo5);
		
		// -------------------------------------------------------------
		// }
		if (tokens.get(i).getTipo().equals("FIN_BLOQUE")) {
			System.out.print(" "+tokens.get(i).getValor());
			Nodo<Token> hijo4 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo4);
		} else {
			Nodo<String> error = error(" }");
			regla.agregarHijo(error);
		}
		i++;
		
		return regla;
	}

	// Condicion_Inicial -> SOperacion | SDeclaracion
	public Nodo Condicion_Inicial() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> regla = new Nodo("[Condicion_Inicial]");
		// -----------------------------------------
		// Condicion_Inicial
		String tipo = getTokenType();
		switch (tipo) {
			case "IDENTIFICADOR":
			case "NUMERO":
				Nodo<String> hijo1 = SOperacion();
				regla.agregarHijo(hijo1);
				break;
			case "DECIMAL":
			case "ENTERO":
			case "BOLEANO":
			case "CADENA":
				Nodo<String> hijo2 = SDeclaracion();
				regla.agregarHijo(hijo2);
				//i++;
				break;
		}
		return regla;
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

	public Nodo error(String e) {
		Nodo<String> error = new Nodo("Error");
		System.out.println(" ERROR");
		errores.add("ERROR: Se esperaba "+ e + " | Token recibido: " +tokens.get(i).getValor());
		return error;
	}
	
}
