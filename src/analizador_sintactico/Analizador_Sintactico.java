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
			tipo = getTokenType();
			while(tipo!="FIN_BLOQUE") {
				i++;
				tipo = getTokenType();
			}
			
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
				Nodo<Token> t_d = new Nodo(tokens.get(i));
				tipo_dato.agregarHijo(t_d);
				System.out.print("\n   " + tokens.get(i).getValor());
				i++;
				break;
			default:
				// Agregamos nodo hijo al arbol:
				Nodo<String> error = error(" dec | int | bool | String");
				tipo_dato.agregarHijo(error);
				break;
		}
		
		// -----------------------------------------
		// Identificador:
		Nodo<String> identificador = new Nodo("Identificador");
		tipo = getTokenType();
		switch (tipo) {
			case "IDENTIFICADOR":
				Nodo<Token> id = new Nodo(tokens.get(i));
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
				Nodo<Token> f_s = new Nodo(tokens.get(i));
				regla.agregarHijo(f_s);
				Mas_declaraciones.setHermano(f_s);
				System.out.println(tokens.get(i).getValor());
				break;
			default:
				Nodo<String> error;
				while(tipo!="FIN_SENTENCIA") {
					error = error(" ;");
					regla.agregarHijo(error);
					if(tipo=="FIN_BLOQUE")break;
					i++;
					tipo = getTokenType();
				}
				if(tipo=="FIN_SENTENCIA") {
					f_s = new Nodo(tokens.get(i).getValor());
					Mas_declaraciones.setHermano(f_s);
					regla.agregarHijo(f_s);
					System.out.println(tokens.get(i).getValor());
				}
				if(tipo=="FIN_BLOQUE")i--;
				//Nodo<String> error = error(" IDENTIFICADOR");
				//identificador.agregarHijo(error);
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
			Nodo<Token> igual = new Nodo(tokens.get(i));
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

	// Expresion_individual -> IdNumCadBool Expresion 
	public Nodo Expresion_individual() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> Expresion_individual = new Nodo("Expresion_individual");
		// -----------------------------------------
		Nodo<String> IdNumCadBool = new Nodo("");
		String tipo = getTokenType();
		switch (tipo) {
		case "IDENTIFICADOR":
		case "NUMERO":
		case "Cadena de caracteres":
		case "VERDADERO":
		case "FALSO":
			// IdNumCadBool:
			IdNumCadBool = IdNumCadBool();
			break;
		default:
			Nodo<String> error = error(" IDENTIFICADOR | NUMERO | Cadena de caracteres | true | false");
			Expresion_individual.agregarHijo(error);
			break;
		}
		//i++;
		// ---------------------------------------
		// Expresion
		Nodo<String> Expresion = Expresion();
		//
		IdNumCadBool.setHermano(Expresion);
		Expresion_individual.agregarHijo(IdNumCadBool);
		Expresion_individual.agregarHijo(Expresion);
		
		return Expresion_individual;
	}

	// IdNumCadBool -> Identificador | Numero | Cadena | Valor
	public Nodo IdNumCadBool() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> IdNumCadBool = new Nodo("IdNumCadBool");
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
				Nodo<Token> hijo = new Nodo(tokens.get(i));
				hijo.setValor(tokens.get(i).getValor());
				hijo.setTipo(tokens.get(i).getTipo());
				hijo.setEsTerminal(true);
				//
				valor.agregarHijo(hijo);
				IdNumCadBool.agregarHijo(valor);
				//
				System.out.print(" "+tokens.get(i).getValor());
				
				i++;
			break;
			case "VERDADERO":
			case "FALSO":
				valor = new Nodo("Booleano");
				hijo = new Nodo(tokens.get(i));
				hijo.setValor(tokens.get(i).getValor());
				hijo.setTipo(tokens.get(i).getTipo());
				hijo.setEsTerminal(true);
				
				valor.agregarHijo(hijo);
				IdNumCadBool.agregarHijo(valor);
				System.out.print(" "+tokens.get(i).getValor());
				
				i++;
			break;
			default:
				Nodo<String> error = error(" IDENTIFICADOR | NUMERO | Cadena de caracteres | true |	false");
				IdNumCadBool.agregarHijo(error);
			break;
		}
		
		// Agregamos nodo hijo al arbol:
		return IdNumCadBool;
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
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
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
	
	// Expresion -> Operador_aritmetico IdNumCadBool Mas_expresiones | E
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
			Nodo<Token> hijo1 = new Nodo(tokens.get(i));
			operador.agregarHijo(hijo1);
			System.out.print(tokens.get(i).getValor());
			i++;
			// -----------------------------------------
			// IdNumCadBool
			Nodo<String> IdNumCadBool = IdNumCadBool();
			operador.setHermano(IdNumCadBool);
			Expresion.agregarHijo(operador);
			// ---------------------------------------
			// Mas_Expresiones
			Nodo<String> Mas_Expresiones = Mas_Expresiones();
			IdNumCadBool.setHermano(Mas_Expresiones);
			Expresion.agregarHijo(IdNumCadBool);
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
				Nodo<Token> coma = new Nodo(tokens.get(i));
				System.out.print(tokens.get(i).getValor());
				i++;
				// ----------------------------------------------------
				// Identificador:
				Nodo<String> Identificador = new Nodo("Identificador");
				tipo = getTokenType();
				switch (tipo) {
					case "IDENTIFICADOR":
						// Agregamos la variable a la tabla de simbolos
						Nodo<Token> id = new Nodo(tokens.get(i));
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
		Nodo<Token> f_s = new Nodo();
		// ----------------------------------------------------------
		// outo:
		if (getTokenType().equals("IMPRIMIR")) {
			outo = new Nodo(tokens.get(i).getValor());
			System.out.print("\n   " + tokens.get(i).getValor());
			i++;
		} else {
			outo = error(" OUTO");
		}
		// ----------------------------------------------------------
		// (:
		if (getTokenType().equals("PARENTESIS_APERTURA")) {
			p_a = new Nodo(tokens.get(i).getValor());
			System.out.print(tokens.get(i).getValor());
			i++;
		} else {
			p_a = error(" (");
		}
		// ----------------------------------------------------------
		// Contenido
		Nodo<String> contenido = Contenido();

		// ----------------------------------------------------------
		// ):
		while (!(getTokenType().equals("PARENTESIS_CERRADURA"))) {
			p_c = error(" )");
			i++;
		}
		if (getTokenType().equals("PARENTESIS_CERRADURA")) {
			p_c = new Nodo(tokens.get(i));
			System.out.print(tokens.get(i).getValor());
			i++;
		} else {
			p_c = error(" )");
		}
		
		// ----------------------------------------------------------
		// ;
		if (getTokenType().equals("FIN_SENTENCIA")) {
			f_s = new Nodo(tokens.get(i));
			System.out.print(tokens.get(i).getValor());
			i++;
		} else {
			Nodo<String> error = error(" ;");
			Souto.agregarHijo(error);
		}
		
		outo.setHermano(p_a);
		p_a.setHermano(contenido);
		contenido.setHermano(p_c);
		p_c.setHermano(f_s);
		
		Souto.agregarHijo(outo);
		Souto.agregarHijo(p_a);
		Souto.agregarHijo(contenido);
		Souto.agregarHijo(p_c);
		Souto.agregarHijo(f_s);
		
		return Souto;
	}

	// Contenido -> IdNumCadBool Mas_Contenido	
	public Nodo Contenido() {
		Nodo<String> Contenido = new Nodo("Contenido");
		// ------------------------------------------------
		// IdNumCadBool
		Nodo<String> IdNumCadBool = IdNumCadBool();
		// ------------------------------------------------
		// Mas_contenido
		Nodo<String> Mas_Contenido = Mas_Contenido();
		
		Contenido.agregarHijo(IdNumCadBool);
		Contenido.agregarHijo(Mas_Contenido);
		return Contenido;
	}

	// Mas_Contenido -> + Contenido | E
	public Nodo Mas_Contenido() {
		Nodo<String> regla = new Nodo("Mas_Contenido");
		String tipo = getTokenType();
		switch (tipo) {
		case "SUMA":
			System.out.print(tokens.get(i).getValor());
			Nodo<String> hijo1 = new Nodo(tokens.get(i).getValor());
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
		Nodo<String> SOperacion = new Nodo("SOperacion");
		// ---------------------------------------------------
		// Identificador:
		Nodo<String> Identificador = new Nodo("Identificador");
		String tipo = getTokenType();
		switch (tipo) {
			case "IDENTIFICADOR":
				Nodo<String> id = new Nodo(tokens.get(i));
				System.out.print(" " + tokens.get(i).getValor());
				tabla.agregarVariable(tokens.get(i).getValor());
				variables.add(tokens.get(i).getValor());
				Identificador.agregarHijo(id);
				i++;
				break;
			default:
				Nodo<String> error = error(" IDENTIFICADOR");
				Identificador.agregarHijo(error);
				break;
		}
		// ---------------------------------------------------
		// Igual:
		Nodo<String> igual = new Nodo("");
		tipo = getTokenType();
		switch (tipo) {
		case "IGUAL":
			igual = new Nodo(tokens.get(i));
			System.out.print(" " + tokens.get(i).getValor());
			i++;
			break;
		default:
			igual = error(" =");
			break;
		}
		// ---------------------------------------------------
		// Expresion_individual:
		Nodo<String> Expresion_individual = Expresion_individual();
		
		Identificador.setHermano(igual);
		igual.setHermano(Expresion_individual);
		//Expresion_individual.setHermano(fin_sentencia);
		
		SOperacion.agregarHijo(Identificador);
		SOperacion.agregarHijo(igual);
		SOperacion.agregarHijo(Expresion_individual);
		//SOperacion.agregarHijo(fin_sentencia);
		// ---------------------------------------------------
		// ;
		Nodo<String> fin_sentencia = new Nodo("");
		tipo = getTokenType();
		switch (tipo) {
		case "FIN_SENTENCIA":
			fin_sentencia = new Nodo(tokens.get(i).getValor());
			System.out.print(" " + tokens.get(i).getValor());	
			SOperacion.agregarHijo(fin_sentencia);
			break;
		default:
			//fin_sentencia = error(" ;");
			while(tipo!="FIN_SENTENCIA") {
				fin_sentencia = error(" ;");
				SOperacion.agregarHijo(fin_sentencia);
				if(tipo=="FIN_BLOQUE")break;
				i++;
				tipo = getTokenType();
			}
			if(tipo=="FIN_SENTENCIA") {
				fin_sentencia = new Nodo(tokens.get(i).getValor());
				SOperacion.agregarHijo(fin_sentencia);
				System.out.println(tokens.get(i).getValor());
			}
			if(tipo=="FIN_BLOQUE")i--;
			break;
		}	
		i++;
		return SOperacion;
	}

	// Sif -> if(Operador_NOT Condicion){cuerpo} Selse
	public Nodo Sif() {
		Nodo<String> Sif = new Nodo("SIf");
		
		Nodo<Token> iff = new Nodo();
		Nodo<Token> parentesis_apertura = new Nodo();
		Nodo<String> Op_Not = new Nodo();
		Nodo<String> Condicion = new Nodo();
		Nodo<Token> parentesis_cerradura = new Nodo();
		Nodo<Token> inicio_bloque = new Nodo();
		Nodo<String> Cuerpo = new Nodo();
		Nodo<Token> fin_bloque = new Nodo();
		Nodo<String> Selse = new Nodo("Selse");
		
		// ------------------------------------------------
		// if:
		String tipo = getTokenType();
		switch (tipo) {
			case "SI":
				System.out.print(" " + tokens.get(i).getValor());
				iff = new Nodo(tokens.get(i));
				break;
			default:
				iff = error(" if");
				break;
		}
		i++;
		// ------------------------------------------------
		// Inicio del parentesis:
		tipo = getTokenType();
		switch (tipo) {
			case "PARENTESIS_APERTURA":
				System.out.print(" " + tokens.get(i).getValor());
				parentesis_apertura = new Nodo(tokens.get(i));
				break;
			default:
				parentesis_apertura = error(" (");
				break;
		}
		i++;
		// ------------------------------------------------
		// Operador NOT -> NOT | E
		Op_Not = new Nodo("Op_Not");
		tipo = getTokenType();
		switch (tipo) {
			case "NO":
				// ------------------------------------------------
				// NOT
				System.out.print(" " + tokens.get(i).getValor());
				Nodo<Token> Not = new Nodo(tokens.get(i));
				Op_Not.agregarHijo(Not);
				break;
			default:
				// ------------------------------------------------
				// Vacio
				Nodo <String> vacio = new Nodo("Vacio");
				vacio.setEsTerminal(true);
				Op_Not.agregarHijo(vacio);
				break;
		}
		// ------------------------------------------------
		// Condicion:
		Condicion = Condicion();
		// ------------------------------------------------
		// Fin del parentesis:
		tipo = getTokenType();
		switch (tipo) {
			case "PARENTESIS_CERRADURA":
				System.out.print(" " + tokens.get(i).getValor());
				parentesis_cerradura = new Nodo(tokens.get(i));
				break;
			default:
				parentesis_apertura = error(" (");
				break;
		}
		i++;
		// ------------------------------------------------
		// Inicio de bloque:
		tipo = getTokenType();
		switch (tipo) {
			case "INICIO_BLOQUE":
				System.out.print(" " + tokens.get(i).getValor());
				inicio_bloque = new Nodo(tokens.get(i));
				break;
			default:
				inicio_bloque = error(" (");
				break;
		}
		i++;
		// ------------------------------------------------
		// Cuerpo principal:
		Cuerpo = cuerpo();
		// ------------------------------------------------
		// Fin del bloque:
		tipo = getTokenType();
		switch (tipo) {
			case "FIN_BLOQUE":
				System.out.print(" " + tokens.get(i).getValor());
				fin_bloque = new Nodo(tokens.get(i));
				break;
			default:
				fin_bloque = error(" (");
				break;
		}
		i++;
		// ------------------------------------------------
		// Selse:
		tipo = getTokenType();
		switch (tipo) {
			case "DE_OTRA_FORMA":
				// else:
				Nodo<String> elsee = Selse();
				Selse.agregarHijo(elsee);
				break;
			default:
				// ------------------------------------------------
				// Vacio
				Nodo<String> vacio = new Nodo("Vacio");
				vacio.setEsTerminal(true);
				Selse.agregarHijo(vacio);
				break;
		}

		// Hermanos:
		iff.setHermano(parentesis_apertura);
		parentesis_apertura.setHermano(Op_Not);
		Op_Not.setHermano(Condicion);
		Condicion.setHermano(parentesis_cerradura);
		parentesis_cerradura.setHermano(inicio_bloque);
		inicio_bloque.setHermano(Cuerpo);
		Cuerpo.setHermano(fin_bloque);
		fin_bloque.setHermano(Selse);

		// Hijos:
		
		Sif.agregarHijo(iff);
		Sif.agregarHijo(parentesis_apertura);
		Sif.agregarHijo(Op_Not);
		Sif.agregarHijo(Condicion);
		Sif.agregarHijo(parentesis_cerradura);
		Sif.agregarHijo(inicio_bloque);
		Sif.agregarHijo(Cuerpo);
		Sif.agregarHijo(fin_bloque);
		Sif.agregarHijo(Selse);
		

		return Sif;
	}

	// Condicion -> Expresion_individual Operador_relacional Expresion_individual Mas_Condiciones
	public Nodo Condicion() {
		Nodo<String> Condicion = new Nodo("Condicion");
		
		// -----------------------------------------
		// Expresion_individual
		Nodo<String> Expresion_individual_1 = Expresion_individual();
		
		// -----------------------------------------
		// Operador_relacional
		Nodo<String> Operador_relacional = Operador_relacional();
		
		// -----------------------------------------
		// Expresion_individual
		Nodo<String> Expresion_individual_2 = Expresion_individual();

		// -----------------------------------------
		// Expresion_individual
		Nodo<String> Mas_Condiciones = Mas_Condiciones();
		
		// Hermanos:
		Expresion_individual_1.setHermano(Operador_relacional);
		Operador_relacional.setHermano(Expresion_individual_2);
		Expresion_individual_2.setHermano(Mas_Condiciones);

		// Hijos:
		Condicion.agregarHijo(Expresion_individual_1);
		Condicion.agregarHijo(Operador_relacional);
		Condicion.agregarHijo(Expresion_individual_2);
		Condicion.agregarHijo(Mas_Condiciones);
		
		return Condicion;
	}

	// Operador_relacional -> ==|!=|<|>|<=|>=
	public Nodo Operador_relacional() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> Operador_relacional = new Nodo("Op_rel");
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
			Operador_relacional.agregarHijo(hijo1);
			i++;
			break;
		default:
			Nodo<String> error = error(" OPERADOR RELACIONAL");
			Operador_relacional.agregarHijo(error);
			break;
		}
		return Operador_relacional;
	}

	// Mas_condiciones -> Operador_logico condicion | E
	public Nodo Mas_Condiciones() {
		Nodo<String> Mas_condiciones = new Nodo("Mas_Condiciones");
		
		Nodo<String> Operador_logico = new Nodo("Op_log");
		Nodo<Token> Op_log = new Nodo();
		Nodo<String> Condicion = new Nodo();
		// -----------------------------------------
		// Operadores logicos AND|OR:
		String tipo = getTokenType();
		switch (tipo) {
			case "Y":
			case "O":
				System.out.print(" " + tokens.get(i).getValor());
				Op_log = new Nodo(tokens.get(i));
				Operador_logico.agregarHijo(Op_log);
				i++;
				// -----------------------------------------
				// Condicion:
				Condicion = Condicion();
				
				// Hermanos:
				Operador_logico.setHermano(Condicion);
				
				// Hijos:
				Mas_condiciones.agregarHijo(Operador_logico);
				Mas_condiciones.agregarHijo(Condicion);
				break;
			default:
				// ------------------------------------------------
				// Vacio
				Nodo<String> vacio = new Nodo("Vacio");
				vacio.setEsTerminal(true);
				Mas_condiciones.agregarHijo(vacio);
				break;
		}
		return Mas_condiciones;
	}

	// Selse -> else {cuerpo} | E
	public Nodo Selse() {
	
		Nodo<String> Selse = new Nodo("Selse");
		
		Nodo<Token> elsee = new Nodo();
		Nodo<Token> inicio_bloque = new Nodo();
		Nodo<String> cuerpo = new Nodo();
		Nodo<Token> fin_bloque = new Nodo();
		// -----------------------------------------
		// else:
		String tipo = getTokenType();
		switch (tipo) {
			case "DE_OTRA_FORMA":
				// -------------------------------------------
				// else
				System.out.print(tokens.get(i).getValor());
				elsee = new Nodo(tokens.get(i));
				i++;
				// --------------------------------------------
				// {
				tipo = getTokenType();
				switch (tipo) {
					case "INICIO_BLOQUE":
						System.out.print(tokens.get(i).getValor());
						inicio_bloque = new Nodo(tokens.get(i));
						break;
					default:
						inicio_bloque = error(" {");
						break;
				}
				i++;
				// --------------------------------------------
				// cuerpo
				cuerpo = cuerpo();
				// --------------------------------------------
				// }
				tipo = getTokenType();
				switch (tipo) {
					case "FIN_BLOQUE":
						System.out.print(tokens.get(i).getValor());
						fin_bloque = new Nodo(tokens.get(i));
						break;
					default:
						fin_bloque = error(" {");
						break;
				}

				// Hermanos:
				elsee.setHermano(inicio_bloque);
				inicio_bloque.setHermano(cuerpo);
				cuerpo.setHermano(fin_bloque);
				
				// Hijos:
				Selse.agregarHijo(elsee);
				Selse.agregarHijo(inicio_bloque);
				Selse.agregarHijo(cuerpo);
				Selse.agregarHijo(fin_bloque);
				
				break;
			default:
				// ------------------------------------------------
				// Vacio
				Nodo<String> vacio = new Nodo("Vacio");
				vacio.setEsTerminal(true);
				Selse.agregarHijo(vacio);
				break;
		}
		return Selse;
	}

	// Sfrom -> from (Condicion_Inicial) to (Condicion ;) inc (IdNumCadBool ;) {cuerpo}
	public Nodo Sfrom() {
		//-------------------------------------------
		Nodo<String> SFrom = new Nodo("Sfrom");
		
		Nodo<Token> from = new Nodo();
		
		Nodo<Token> parentesis_apertura_1 = new Nodo();
		Nodo<String> Condicion_inicial = new Nodo();
		Nodo<Token> parentesis_cerradura_1 = new Nodo();
		
		Nodo<Token> to = new Nodo();
		
		Nodo<Token> parentesis_apertura_2 = new Nodo();
		Nodo<String> Condicion = new Nodo();
		Nodo<Token> fin_sentencia_1 = new Nodo();
		Nodo<Token> parentesis_cerradura_2 = new Nodo();
		
		Nodo<Token> inc = new Nodo();
		
		Nodo<Token> parentesis_apertura_3 = new Nodo();
		Nodo<String> IdNumCadBool = new Nodo();
		Nodo<Token> fin_sentencia_2 = new Nodo();
		Nodo<Token> parentesis_cerradura_3 = new Nodo();
		
		Nodo<Token> inicio_bloque = new Nodo();
		Nodo<String> Cuerpo = new Nodo();
		Nodo<Token> fin_bloque = new Nodo();
		
		//-------------------------------------------
		// from:
		String tipo = getTokenType();
		switch (tipo) {
			case "INICIO_FOR":
				System.out.print("   "+tokens.get(i).getValor());
				from = new Nodo(tokens.get(i));
				break;
			default:
				from = error(" outo");
				break;
		}
		i++;
		
		// ------------------------------------------
		// (
		tipo = getTokenType();
		switch (tipo) {
			case "PARENTESIS_APERTURA":
				System.out.print("   "+tokens.get(i).getValor());
				parentesis_apertura_1 = new Nodo(tokens.get(i));
				break;
			default:
				parentesis_apertura_1 = error(" (");
				break;
		}
		i++;
		// ------------------------------------------
		// Condicion_Inicial
		Condicion_inicial = Condicion_Inicial();
		i--;
		//i++;
		// ------------------------------------------
		// )
		tipo = getTokenType();
		switch (tipo) {
			case "PARENTESIS_CERRADURA":
				System.out.print("   "+tokens.get(i).getValor());
				parentesis_cerradura_1 = new Nodo(tokens.get(i));
				break;
			default:
				parentesis_cerradura_1 = error(" (");
				break;
		}
		i++;
		
		// ------------------------------------------
		// to
		tipo = getTokenType();
		switch (tipo) {
			case "FIN_FOR":
				System.out.print("   "+tokens.get(i).getValor());
				to = new Nodo(tokens.get(i));
				break;
			default:
				to = error(" to");
				break;
		}
		i++;
		
		// ------------------------------------------
		// (
		tipo = getTokenType();
		switch (tipo) {
			case "PARENTESIS_APERTURA":
				System.out.print("   "+tokens.get(i).getValor());
				parentesis_apertura_2 = new Nodo(tokens.get(i));
				break;
			default:
				parentesis_apertura_2 = error(" (");
				break;
		}
		i++;
		// ------------------------------------------
		// Condicion
		Condicion = Condicion();
		// ------------------------------------------
		// ;
		tipo = getTokenType();
		switch (tipo) {
			case "FIN_SENTENCIA":
				System.out.print("   "+tokens.get(i).getValor());
				fin_sentencia_1 = new Nodo(tokens.get(i));
				break;
			default:
				fin_sentencia_1 = error(" ;");
				break;
		}
		i++;
		// ------------------------------------------
		// )
		tipo = getTokenType();
		switch (tipo) {
			case "PARENTESIS_CERRADURA":
				System.out.print("   "+tokens.get(i).getValor());
				parentesis_cerradura_2 = new Nodo(tokens.get(i));
				break;
			default:
				parentesis_cerradura_2 = error(" (");
				break;
		}
		i++;
		
		// ------------------------------------------
		// inc
		tipo = getTokenType();
		switch (tipo) {
			case "INCREMENTO_FOR":
				System.out.print("   "+tokens.get(i).getValor());
				inc = new Nodo(tokens.get(i));
			break;
			default:
				inc = error(" inc");
			break;
		}
		i++;
				
		// ------------------------------------------
		// (
		tipo = getTokenType();
		switch (tipo) {
			case "PARENTESIS_APERTURA":
				System.out.print("   "+tokens.get(i).getValor());
				parentesis_apertura_3 = new Nodo(tokens.get(i));
			break;
			default:
				parentesis_apertura_3 = error(" (");
			break;
		}
		i++;
		// ------------------------------------------
		// IdNumCadBool
		IdNumCadBool = IdNumCadBool();
		// ------------------------------------------
		// ;
		tipo = getTokenType();
		switch (tipo) {
			case "FIN_SENTENCIA":
				System.out.print("   "+tokens.get(i).getValor());
				fin_sentencia_2 = new Nodo(tokens.get(i));
			break;
			default:
				fin_sentencia_2 = error(" ;");
			break;
		}
		i++;
		// ------------------------------------------
		// )
		tipo = getTokenType();
		switch (tipo) {
			case "PARENTESIS_CERRADURA":
				System.out.print("   "+tokens.get(i).getValor());
				parentesis_cerradura_3 = new Nodo(tokens.get(i));
			break;
			default:
				parentesis_cerradura_3 = error(" (");
			break;
		}
		i++;
		
		// ---------------------------------------------------------------
		// {
		tipo = getTokenType();
		switch (tipo) {
			case "INICIO_BLOQUE":
				System.out.print("   "+tokens.get(i).getValor());
				inicio_bloque = new Nodo(tokens.get(i));
			break;
			default:
				inicio_bloque = error(" {");
			break;
		}
		i++;
		
		// ----------------------------------------------------------------
		// Cuerpo
		Cuerpo = cuerpo();
		
		// -------------------------------------------------------------
		// }
		tipo = getTokenType();
		switch (tipo) {
			case "FIN_BLOQUE":
				System.out.print("   "+tokens.get(i).getValor());
				fin_bloque = new Nodo(tokens.get(i));
			break;
			default:
				inicio_bloque = error(" }");
			break;
		}
		i++;
		
		// Hermanos
		from.setHermano(parentesis_apertura_1);
				
		parentesis_apertura_1.setHermano(Condicion_inicial);
		Condicion_inicial.setHermano(parentesis_cerradura_1);
		parentesis_cerradura_1.setHermano(to);
				
		to.setHermano(parentesis_apertura_2);
				
		parentesis_apertura_2.setHermano(Condicion);
		Condicion.setHermano(fin_sentencia_1);
		fin_sentencia_1.setHermano(parentesis_cerradura_2);
		parentesis_cerradura_2.setHermano(inc);
				
		inc.setHermano(parentesis_apertura_3);
				
		parentesis_apertura_3.setHermano(IdNumCadBool);
		IdNumCadBool.setHermano(fin_sentencia_2);
		fin_sentencia_2.setHermano(parentesis_cerradura_3);
		parentesis_cerradura_3.setHermano(inicio_bloque);
				
		inicio_bloque.setHermano(Cuerpo);
		Cuerpo.setHermano(fin_bloque);
		
		// Hijos:
		SFrom.agregarHijo(from);
				
		SFrom.agregarHijo(parentesis_apertura_1);
		SFrom.agregarHijo(Condicion_inicial);
		SFrom.agregarHijo(parentesis_cerradura_1);
		
		SFrom.agregarHijo(to);
				
		SFrom.agregarHijo(parentesis_apertura_2);
		SFrom.agregarHijo(Condicion);			
		SFrom.agregarHijo(fin_sentencia_1);			
		SFrom.agregarHijo(parentesis_cerradura_2);
				
		SFrom.agregarHijo(inc);
				
		SFrom.agregarHijo(parentesis_apertura_3);
		SFrom.agregarHijo(IdNumCadBool);
		SFrom.agregarHijo(fin_sentencia_2);
		SFrom.agregarHijo(parentesis_cerradura_3);
				
		SFrom.agregarHijo(inicio_bloque);
		SFrom.agregarHijo(Cuerpo);
		SFrom.agregarHijo(fin_bloque);
		
		return SFrom;
	}

	// Condicion_Inicial -> SDeclaracion | SOperacion
	// Condicion_Inicial -> SOperacion | SDeclaracion
	public Nodo Condicion_Inicial() {
		// Creacion la raiz del sub-arbol semantico:
		Nodo<String> Condicion_inicial = new Nodo("Condicion_Inicial");
		// -----------------------------------------
		// Condicion_Inicial
		String tipo = getTokenType();
		switch (tipo) {
			case "IDENTIFICADOR":
			case "NUMERO":
				Nodo<String> hijo1 = SOperacion();
				Condicion_inicial.agregarHijo(hijo1);
				break;
			case "DECIMAL":
			case "ENTERO":
			case "BOLEANO":
			case "CADENA":
				Nodo<String> hijo2 = SDeclaracion();
				Condicion_inicial.agregarHijo(hijo2);
				//i++;
				break;
		}
		i++;
		return Condicion_inicial;
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
