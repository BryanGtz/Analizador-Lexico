package analizador_sintactico;

import java.util.*;

import analizador_lexico.*;
import analizador_semantico.*;

public class Analizador_Sintactico {

	// Lista que contiene todos los tokens obtenidos desde el Lexico.
	List<Token> tokens;
	// Contador que permite tener el orden de los tokens leidos.
	int i;
	// Arbol semantico
	public Arbol_Sintactico as;
	// Fila de errores
	Queue<String> errores = new LinkedList<>();
	// Fila de variables detectadas
	Queue<String> variables = new LinkedList<>();
	// Tabla de simbolos
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
		System.out.println(errores.toString().replaceAll(",", "\n"));
		System.out.println("==================================");
		// Imprimimos las variables detectadas
		System.out.println("Cantidad de variables: "+variables.size());
		System.out.println(variables.toString().replaceAll(",", "\n"));
	}

	// Sstarto -> starto () {cuerpo}
	public Nodo Sstarto() {
		Nodo<String> regla = new Nodo("Sstarto");
		
		// ------------------------------------------------
		// starto:
		if (getTokenType().equals("INICIO")) {
			Nodo<Token> starto = new Nodo(tokens.get(i).getValor());
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
			Nodo<Token> p_a = new Nodo(tokens.get(i).getValor());
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
			Nodo<Token> p_c = new Nodo(tokens.get(i).getValor());
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
			Nodo<Token> i_b = new Nodo(tokens.get(i).getValor());
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
			Nodo<Token> f_b = new Nodo(tokens.get(i).getValor());
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
		Nodo<String> regla = new Nodo("Cuerpo");	
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
		Nodo<String> regla = new Nodo("Mas_instrucciones");
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

	// Declaracion -> Tipo_dato Identificador Asignacion Mas_declaraciones ;
	public Nodo SDeclaracion() {
		// Creacion la raiz del arbol semantico:
		Nodo<String> regla = new Nodo("SDeclaracion");
		// -----------------------------------------
		// Tipo_dato:
		Nodo<String> tipo_dato = new Nodo("Tipo_dato");
		String tipo = getTokenType();
		switch (tipo) {
			case "DECIMAL":
			case "ENTERO":
			case "BOLEANO":
			case "CADENA":
				// Agregamos nodo hijo al arbol:
				Nodo<Token> t_d = new Nodo(tokens.get(i).getValor());
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
		Nodo<String> identificador = new Nodo("Identificador");
		tipo = getTokenType();
		switch (tipo) {
			case "IDENTIFICADOR":
				Nodo<String> id = new Nodo(tokens.get(i).getValor());
				identificador.agregarHijo(id);
				System.out.print(" " + tokens.get(i).getValor());
				// --------------------------
				// Agregamos la variable a la tabla de simbolos
				tabla.agregarVariable(tokens.get(i).getValor());
				variables.add(tokens.get(i).getValor());
				break;
			default:
				Nodo<String> error = error(" IDENTIFICADOR");
				identificador.agregarHijo(error);
				break;
		}
		i++;
		// --------------------------------------
		// Asignacion:
		Nodo<String> Asignacion = Asignacion();
		// --------------------------------------
		// Mas_declaraciones:
		Nodo<String> Mas_declaraciones = Mas_declaraciones();
		//
		tipo_dato.setHermano(identificador);
		identificador.setHermano(Asignacion);
		Asignacion.setHermano(Mas_declaraciones);
		
		regla.agregarHijo(tipo_dato);
		regla.agregarHijo(identificador);
		regla.agregarHijo(Asignacion);
		regla.agregarHijo(Mas_declaraciones);
		// -----------------------------------------
		// ;
		tipo = getTokenType();
		switch (tipo) {
			case "FIN_SENTENCIA":
				Nodo<Token> f_s = new Nodo(tokens.get(i).getValor());
				regla.agregarHijo(f_s);
				Mas_declaraciones.setHermano(f_s);
				System.out.println(tokens.get(i).getValor());
				break;
			default:
				while(tipo!="FIN_SENTENCIA"||tipo=="FIN_BLOQUE") {
					Nodo<String> error = error(" ;");
					regla.agregarHijo(error);
					i++;
					tipo = getTokenType();
				}
				f_s = new Nodo(tokens.get(i).getValor());
				regla.agregarHijo(f_s);
				Mas_declaraciones.setHermano(f_s);
				System.out.println(tokens.get(i).getValor());
				break;
		}
		i++;
		return regla;
	}

	//  Asignacion -> = Expresion_individual | E
	public Nodo Asignacion() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> Asignacion = new Nodo("Asignacion");
		// -----------------------------------------
		String tipo = getTokenType();
		switch (tipo) {
		case "IGUAL":
			// Agregamos nodo hijo al arbol:
			Nodo<String> igual = new Nodo(tokens.get(i).getValor());
			System.out.print(" " + tokens.get(i).getValor());
			i++;
			// --------------------------------------
			// Expresion_individual:
			Nodo<String> Expresion_individual = Expresion_individual();
			//
			igual.setHermano(Expresion_individual);
			Asignacion.agregarHijo(igual);
			Asignacion.agregarHijo(Expresion_individual);
			break;
		default:
			// Vacio
			Nodo<String> vacio = new Nodo("Vacio");
			vacio.setEsTerminal(true);
			Asignacion.agregarHijo(vacio);
		}
		// -----------------------------------------
		// Retornamos el sub-arbol generado
		return Asignacion;
	}

	// Expresion_individual -> IdNumCad Expresion | Valor
	public Nodo Expresion_individual() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> Expresion_individual = new Nodo("Expresion_individual");
		// -----------------------------------------
		String tipo = getTokenType();
		switch (tipo) {
		case "IDENTIFICADOR":
		case "NUMERO":
		case "Cadena de caracteres":
			// IdNumCad:
			Nodo<String> IdNumCad = IdNumCad();
			// ---------------------------------------
			// Expresion
			Nodo<String> Expresion = Expresion();
			//
			IdNumCad.setHermano(Expresion);
			Expresion_individual.agregarHijo(IdNumCad);
			Expresion_individual.agregarHijo(Expresion);
			break;
		case "VERDADERO":
		case "FALSO":
			// Valor:
			Nodo<String> Valor = Valor();
			Expresion_individual.agregarHijo(Valor);
			break;
		default:
			Nodo<String> error = error(" IDENTIFICADOR | NUMERO | Cadena de caracteres | true | false");
			Expresion_individual.agregarHijo(error);
			break;
		}
		//i++;
		return Expresion_individual;
	}

	// IdNumCad -> Identificador | Numero | Cadena
	public Nodo IdNumCad() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> IdNumCad = new Nodo("IdNumCad");
		String tipo = getTokenType();
		Nodo<String> valor = new Nodo("Valor");
		switch (tipo) {
			case "IDENTIFICADOR":
			case "NUMERO":
			case "Cadena de caracteres":
				switch (tipo) {
				case "IDENTIFICADOR":
					valor = new Nodo("Identificador");
					// Agregamos la variable a la tabla de simbolos
					tabla.agregarVariable(tokens.get(i).getValor());
					variables.add(tokens.get(i).getValor());
					break;
				case "NUMERO":
					valor = new Nodo("Numero");
					break;
				case "Cadena de caracteres":
					valor = new Nodo("Cadena de caracteres");
					break;
				}
				//
				Nodo<String> hijo = new Nodo(tokens.get(i).getValor());
				hijo.setValor(tokens.get(i).getValor());
				hijo.setTipo(tokens.get(i).getTipo());
				hijo.setEsTerminal(true);
				//
				valor.agregarHijo(hijo);
				IdNumCad.agregarHijo(valor);
				//
				System.out.print(" "+tokens.get(i).getValor());
			break;
			default:
				Nodo<String> error = error(" IDENTIFICADOR | NUMERO | Cadena de caracteres");
				IdNumCad.agregarHijo(error);
			break;
		}
		i++;
		// Agregamos nodo hijo al arbol:
		return IdNumCad;
	}
	
	// Valor -> VERDADERO | FALSO 
	public Nodo Valor() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> Valor = new Nodo("Valor");
		// -----------------------------------------
		String tipo = getTokenType();
		switch (tipo) {
		case "VERDADERO":
		case "FALSO":
			Nodo<Token> hijo1 = new Nodo(tokens.get(i).getValor());
			Valor.agregarHijo(hijo1);
			System.out.print(tokens.get(i).getValor());
			i++;
			break;
		default:
			Nodo<String> error = error(" true | false");
			Valor.agregarHijo(error);
			break;
		}
		return Valor;
	}
	
	// Expresion -> Operador_aritmetico IdNumCad Mas_expresiones | E
	public Nodo Expresion() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> Expresion = new Nodo("Expresion");
		// -----------------------------------------
		// Operador_aritmetico
		String tipo = getTokenType();
		switch (tipo) {
		case "SUMA":
		case "RESTA":
		case "MULTIPLICACION":
		case "DIVISION":
		case "MODULO":
			Nodo<String> operador = new Nodo("Op_arit");
			Nodo<String> hijo1 = new Nodo(tokens.get(i).getValor());
			operador.agregarHijo(hijo1);
			System.out.print(tokens.get(i).getValor());
			i++;
			// -----------------------------------------
			// IdNumCad
			Nodo<String> IdNumCad = IdNumCad();
			operador.setHermano(IdNumCad);
			Expresion.agregarHijo(operador);
			// ---------------------------------------
			// Mas_Expresiones
			Nodo<String> Mas_Expresiones = Mas_Expresiones();
			IdNumCad.setHermano(Mas_Expresiones);
			Expresion.agregarHijo(IdNumCad);
			Expresion.agregarHijo(Mas_Expresiones);
		break;
		default:
			// Vacio
			Nodo<String> vacio = new Nodo("Vacio");
			vacio.setEsTerminal(true);
			Expresion.agregarHijo(vacio);
			break;
		}
		return Expresion;
	}

	// Mas_expresiones -> Expresion
	public Nodo Mas_Expresiones() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> Mas_Expresiones = new Nodo("Mas_Expresiones");
		// -----------------------------------------
		Nodo<String> expresion = Expresion();
		
		Mas_Expresiones.agregarHijo(expresion);
		// -----------------------------------------
		return Mas_Expresiones;
	}

	// Mas_declaraciones -> , Identificador Asignacion Mas_declaraciones | E
	public Nodo Mas_declaraciones() {
		Nodo<String> Mas_declaraciones = new Nodo("Mas_declaraciones");
		// ------------------------------------------------
		// ,
		String tipo = getTokenType();
		switch (tipo) {
			case "COMA":
				Nodo<String> coma = new Nodo(tokens.get(i).getValor());
				System.out.print(tokens.get(i).getValor());
				i++;
				// ----------------------------------------------------
				// Identificador:
				Nodo<String> Identificador = new Nodo("Identificador");
				tipo = getTokenType();
				switch (tipo) {
					case "IDENTIFICADOR":
						// Agregamos la variable a la tabla de simbolos
						Nodo<String> id = new Nodo(tokens.get(i).getValor());
						Identificador.agregarHijo(id);
						tabla.agregarVariable(tokens.get(i).getValor());
						variables.add(tokens.get(i).getValor());
						System.out.print(tokens.get(i).getValor());
						break;
					default:
						Nodo<String> error = error(" IDENTIFICADOR");
						Identificador.agregarHijo(error);
						break;
				}
				i++;
				// ----------------------------------------------------
				// Asignacion
				Nodo<String> Asignacion = Asignacion();
				// ----------------------------------------------------
				// Mas_declaraciones
				Nodo<String> Mas_declaraciones_hijo = Mas_declaraciones();
				// Asignamos hermanos y los agregamos al padre:
				coma.setHermano(Identificador);
				Identificador.setHermano(Asignacion);
				Asignacion.setHermano(Mas_declaraciones_hijo);
				
				Mas_declaraciones.agregarHijo(coma);
				Mas_declaraciones.agregarHijo(Identificador);
				Mas_declaraciones.agregarHijo(Asignacion);
				Mas_declaraciones.agregarHijo(Mas_declaraciones_hijo);
				break;
			default:
				Nodo<String> vacio = new Nodo("Vacio");
				vacio.setEsTerminal(true);
				Mas_declaraciones.agregarHijo(vacio);
				break;
		}
		return Mas_declaraciones;
	}

	// Souto -> outo (Contenido) ;
	public Nodo Souto() {
		Nodo<String> Souto = new Nodo("Souto");
		Nodo<Token> outo = new Nodo();
		Nodo<Token> p_a = new Nodo();
		Nodo<Token> p_c = new Nodo();
		// ----------------------------------------------------------
		// outo:
		if (getTokenType().equals("IMPRIMIR")) {
			outo = new Nodo(tokens.get(i));
			Souto.agregarHijo(outo);
			System.out.print("\n   " + tokens.get(i).getValor());
			i++;
		} else {
			Nodo<String> error = error(" OUTO");
			Souto.agregarHijo(error);
		}
		
		// ----------------------------------------------------------
		// (:
		if (getTokenType().equals("PARENTESIS_APERTURA")) {
			p_a = new Nodo(tokens.get(i));
			Souto.agregarHijo(p_a);
			outo.setHermano(p_a);
			System.out.print(tokens.get(i).getValor());
			i++;
		} else {
			Nodo<String> error = error(" (");
			Souto.agregarHijo(error);
		}
		
		// ----------------------------------------------------------
		// CUERPO_OUTO
		Nodo<String> contenido = Contenido();
		p_a.setHermano(contenido);
		Souto.agregarHijo(contenido);
		
		// ----------------------------------------------------------
		// ):
		while (!(getTokenType().equals("PARENTESIS_CERRADURA"))) {
			Nodo<String> error = error(" )");
			Souto.agregarHijo(error);
			i++;
		}
		if (getTokenType().equals("PARENTESIS_CERRADURA")) {
			p_c = new Nodo(tokens.get(i));
			contenido.setHermano(p_c);
			Souto.agregarHijo(p_c);
			System.out.print(tokens.get(i).getValor());
			i++;
		} else {
			Nodo<String> error = error(" )");
			Souto.agregarHijo(error);
		}
		
		// ----------------------------------------------------------
		// ;:
		if (getTokenType().equals("FIN_SENTENCIA")) {
			Nodo<Token> f_s = new Nodo(tokens.get(i));
			Souto.agregarHijo(f_s);
			System.out.print(tokens.get(i).getValor());
			p_c.setHermano(f_s);
			i++;
		} else {
			Nodo<String> error = error(" ;");
			Souto.agregarHijo(error);
		}
		return Souto;

	}

	// Contenido -> Contenido_outo Mas_Contenido
	// Contenido_outo -> Identificador | Numero | Cadena_caracteres | VERDADERO | FALSO
	public Nodo Contenido() {
		Nodo<String> regla = new Nodo("Contenido");
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
				variables.add(tokens.get(i).getValor());
			}
			i++;
			// ------------------------------------------------
			// Mas_contenido
			Nodo<String> hijo2 = Mas_Contenido();
			hijo1.setHermano(hijo2);
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
		Nodo<String> regla = new Nodo("Contenido");
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
			hijo1.setHermano(hijo2);
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
			variables.add(tokens.get(i).getValor());
			i++;
			
			// -----------------------------------------------
			// =
			if (getTokenType().equals("IGUAL")) {
				System.out.print(tokens.get(i).getValor());
				Nodo<Token> hijo2 = new Nodo(tokens.get(i));
				hijo1.setHermano(hijo2);
				regla.agregarHijo(hijo2);
				i++;
				
				// -------------------------------------------
				// Expresion_individual
				Nodo<String> hijo3 = Expresion_individual();
				hijo2.setHermano(hijo1);
				regla.agregarHijo(hijo3);
			} else {
				i++;
				Nodo<String> error = error(" =");
				regla.agregarHijo(error);
			}
			//i++;
			// -----------------------------------------------
			// ;
			if (getTokenType().equals("FIN_SENTENCIA")) {
				System.out.print(tokens.get(i).getValor());
				Nodo<Token> hijo4 = new Nodo(tokens.get(i));
				regla.agregarHijo(hijo4);
			} else {
				Nodo<String> error = error(" ;");
				regla.agregarHijo(error);
			}
			i++;
			
		}
		return regla;
	}

	// Sif -> if(Operador_NOT Condicion){cuerpo} Selse
	public Nodo Sif() {
		Nodo<String> regla = new Nodo("SIf");
		Nodo<Token> hijo1 = new Nodo();
		Nodo<Token> hijo2 = new Nodo();
		Nodo<String> hijo3 = new Nodo();
		Nodo<Token> hijo3_1 = new Nodo();
		Nodo<String> hijo4 = new Nodo();
		Nodo<Token> hijo5 = new Nodo();
		Nodo<String> hijo6 = new Nodo();
		Nodo<Token> hijo7 = new Nodo();
		Nodo<Token> hijo8 = new Nodo();
		// ------------------------------------------------
		// if:
		if (getTokenType().equals("SI")) {
			System.out.print(" " + tokens.get(i).getValor());
			hijo1 = new Nodo(tokens.get(i));
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
			hijo2 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo2);
		} else {
			Nodo<String> error = error(" (");
			regla.agregarHijo(error);
		}
		i++;
		// ------------------------------------------------
		// Operador NOT -> NOT | E
		if (getTokenType().equals("NO")) {
			System.out.print("NO ");
			hijo3 = new Nodo("NO");
			regla.agregarHijo(hijo3);

			hijo3_1 = new Nodo(tokens.get(i));
			hijo3.agregarHijo(hijo3_1);

			i++;
		} else {
			// ------------------------------------------------
			// Vacio
			hijo3 = new Nodo("NO");
			regla.agregarHijo(hijo3);
			
			Nodo<String> vacio = new Nodo("Vacio");
			vacio.setEsTerminal(true);
			hijo3.agregarHijo(vacio);
		}
		// ------------------------------------------------
		// Condicion:
		hijo4 = Condicion();
		regla.agregarHijo(hijo4);
		// ------------------------------------------------
		// Fin del parentesis:
		if (getTokenType().equals("PARENTESIS_CERRADURA")) {
			System.out.print(" " + tokens.get(i).getValor());
			hijo5 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo5);
		} else {
			Nodo<String> error = error(" )");
			regla.agregarHijo(error);
		}
		i++;
		// ------------------------------------------------
		// Inicio de bloque:
		if (getTokenType().equals("INICIO_BLOQUE")) {
			System.out.print(" " + tokens.get(i).getValor());
			hijo5 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo5);
		} else {
			Nodo<String> error = error(" {");
			regla.agregarHijo(error);
		}
		i++;
		// ------------------------------------------------
		// Cuerpo principal:
		hijo6 = cuerpo();
		regla.agregarHijo(hijo6);
		// ------------------------------------------------
		// Fin del bloque:
		if (getTokenType().equals("FIN_BLOQUE")) {
			System.out.print(" " + tokens.get(i).getValor());
			hijo7 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo7);
		} else {
			Nodo<String> error = error(" }");
			regla.agregarHijo(error);
		}
		i++;
		// ------------------------------------------------
		// Selse:
		if (getTokenType().equals("DE_OTRA_FORMA")) {
			// --------------------------------------------
			// Selse
			hijo8 = Selse();
			regla.agregarHijo(hijo8);
		}
		return regla;
	}

	// Condicion -> Expresion_individual Operador_relacional Expresion_individual Mas_Condiciones
	public Nodo Condicion() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> regla = new Nodo("Condicion");
		// -----------------------------------------
		// Expresion_individual
		Nodo<String> hijo1 = Expresion_individual();
		regla.agregarHijo(hijo1);
		// -----------------------------------------
		// Operador_relacional
		Nodo<String> hijo2 = Operador_relacional();
		hijo1.setHermano(hijo2);
		regla.agregarHijo(hijo2);
		// -----------------------------------------
		// Expresion_individual
		Nodo<String> hijo3 = Expresion_individual();
		hijo2.setHermano(hijo3);
		regla.agregarHijo(hijo3);
		// -----------------------------------------
		// Expresion_individual
		Nodo<String> hijo4 = Mas_Condiciones();
		hijo3.setHermano(hijo4);
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
			hijo2.setHermano(hijo1);
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
		Nodo<Token> hijo1 = new Nodo();
		Nodo<Token> hijo2 = new Nodo();
		// -----------------------------------------
		// else:
		if (getTokenType().equals("DE_OTRA_FORMA")) {
			//System.out.println("   else");
			System.out.print(tokens.get(i).getValor());
			hijo1 = new Nodo(tokens.get(i));
			regla.agregarHijo(hijo1);
			i++;
			// Inicio de bloque:
			if (getTokenType().equals("INICIO_BLOQUE")) {
				//System.out.println("   {");
				System.out.print(tokens.get(i).getValor());
				hijo2 = new Nodo(tokens.get(i));
				hijo1.setHermano(hijo2);
				regla.agregarHijo(hijo2);
			} else {
				Nodo<String> error = error(" {");
				regla.agregarHijo(error);
			}
			i++;

			// Cuerpo principal:
			Nodo<String> hijo3 = cuerpo();
			hijo3.setHermano(hijo2);
			regla.agregarHijo(hijo3);

			// Fin del bloque:
			if (getTokenType().equals("FIN_BLOQUE")) {
				//System.out.println("\n   }");
				System.out.print(tokens.get(i).getValor());
				hijo2 = new Nodo(tokens.get(i));
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
		i--;
		//i++;
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
			// --------------------------
			if(tokens.get(i).getTipo().equals("IDENTIFICADOR")) {
				// Agregamos la variable a la tabla de simbolos
				tabla.agregarVariable(tokens.get(i).getValor());
			}
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
		Nodo<String> regla = new Nodo("Condicion_Inicial");
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
		i++;
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
