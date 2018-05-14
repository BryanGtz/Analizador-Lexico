package analizador_sintactico;

import java.util.List;
import java.util.ListIterator;

import analizador_lexico.*;

public class Analizador_Sintactico {

	ListIterator<Token> iterator;
        List<Token> tokens;
	int i;

	public void analizar(List<Token> tokens) {
                this.tokens = tokens;
		i = 0;
		r_S_starto();
	}

	public void r_S_starto() {
		if (getTokenType().equals("INICIO")) {
			System.out.println("starto");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba starto | Token recibido: " + tokens.get(i).getValor());
		}
		if (getTokenType().equals("PARENTESIS_APERTURA")) {
			System.out.println("(");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ( | Token recibido: " + tokens.get(i).getValor());
		}
		if (getTokenType().equals("PARENTESIS_CERRADURA")) {
			System.out.println(")");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ) | Token recibido: " + tokens.get(i).getValor());
		}
		if (getTokenType().equals("INICIO_BLOQUE")) {
			System.out.println("{");
			i++;
		} else {
			System.out.println("ERROR: Se esperaba { | Token recibido: " + tokens.get(i).getValor());
		}
		// CUERPO_STARTO
		r_cuerpo_starto();
		if (getTokenType().equals("FIN_BLOQUE")) {
			System.out.println("}");
		} else {
			System.out.println("ERROR: Se esperaba } | Token recibido: " + tokens.get(i).getValor());
		}

	}

	public void r_cuerpo_starto() {
		String tipo = getTokenType();
		switch (tipo) {
		case "IMPRIMIR":
			r_S_outo();
			r_mas_instrucciones();
			break;
                case "ENTERO":
                case "BOLEANO":
		case "DECIMAL":
                case "CADENA":
                case "CARACTER":
			r_Identificador();
			r_mas_instrucciones();
			break;
		default:
			if (!(getTokenType().equals("FIN_BLOQUE"))) {
				System.out.println("ERROR");
			}
			break;
		}
	}

	public void r_Identificador() {
		System.out.println(tokens.get(i).getValor());
		String tipo = getTokenType();
		i++;
		if (getTokenType().equals("IDENTIFICADOR")) {
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
			if (getTokenType().equals("VERDADERO")||tokens.get(i).getTipo().equals("FALSO")) {
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
		if (getTokenType().equals("FIN_SENTENCIA")) {
			System.out.println(tokens.get(i).getValor());
			i++;
		} else {
			System.out.println("ERROR: Se esperaba ; | Token recibido: " + tokens.get(i).getValor());
			r_mas_instrucciones();
		}
	}

	public void r_mas_instrucciones() {
		String tipo = getTokenType();
		switch (tipo) {
		case "IMPRIMIR":
			r_S_outo();
			r_mas_instrucciones();
			break;
		case "DECIMAL":			
		case "ENTERO":			
		case "BOLEANO":			
		case "CARACTER":			
		case "CADENA":
			r_Identificador();	
			r_mas_instrucciones();
			break;
		case "SI":
			r_S_If();
			break;
		case "INICIO_FOR":
			r_S_From();
			break;
		default:
			if (!(getTokenType().equals("FIN_BLOQUE"))) {
				System.out.println("ERROR");
			}
			//i++;
			//r_mas_instrucciones(tokens);
			break;
		}
		
	}

	public void r_S_outo() {
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
			r_mas_instrucciones();
		}

	}

	public void r_S_From() {
		
	}
	
	public void r_S_If() {
		if (getTokenType().equals("SI")) {
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
			r_mas_instrucciones();
		}
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
        
        public String getTokenType(){
            String tipo;
            if(!tokens.get(i).getTipo().equals("COMENTARIO")){
                tipo = tokens.get(i).getTipo();
            }
            else{
                i++;
                tipo = getTokenType();
            }
            return tipo;
        }
}
