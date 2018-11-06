package analizador_semantico;

import java.util.ArrayList;
import java.util.Stack;

public class EvaludadorExpresion {
	
	private ArrayList<String> posfijo;
	private String tipo;
	private String valor;
	
	public EvaludadorExpresion(ArrayList<String> exp,String tipo) {
		this.tipo=tipo;
		if (!tipo.equals("CADENA"))
			limpiaArreglo(exp);
		
		if (!exp.isEmpty()) {				
			if (exp.size() > 1) {
				if (tipo.equals("CADENA")) {
					this.valor=calcularValorCadena(exp);
				}else {
					toPosfijo(exp);
					calculaValor(this.posfijo);
				}
			} else {
				this.valor = exp.get(0);
			}
		} else {
			System.out.println("Expresion vacia");
		}
	}	

	private String calcularValorCadena(ArrayList<String> exp) {
		String aux="";
		for(int i=0; i<exp.size(); i+=2)
			aux+=exp.get(i);
		return aux;
	}

	public String getValor() {
		if(tipo.equals("ENTERO"))
			return(Integer.toString((int)Double.parseDouble(this.valor)));
		else
			return this.valor;
	}
	
	private void limpiaArreglo(ArrayList<String> exp) {
		exp.removeIf(str ->str.replaceAll("\\s", "").equals(""));
	}
	
	private void calculaValor(ArrayList<String> pos) {
		Stack <Double>pila=new Stack<>();
		for(String str:pos) {
			if(isOperandor(str)) {
				if(!pila.isEmpty()) {
					double op2=pila.pop();
					double op1=pila.pop();
					pila.push(opera(str,op1,op2));
				}else {
					System.out.println("Error de estructura posfija");
				}
			}else {
				pila.push(Double.parseDouble(str));
			}
		}
		valor=Double.toString(pila.pop());		
	}

	private Double opera(String str, double op1, double op2) {
		switch(str){
		case"*":
			return op1*op2;
		case"/":
			return op1/op2;
		case"+":
			return op1+op2;
		case"-":
			return op1-op2;
		default:
			return 0.0;
		}
	}

	private void toPosfijo(ArrayList<String> exp) {
		Stack<String> pila=new Stack<>();
		posfijo=new ArrayList<>();
		for(int i=0;i<exp.size();i++) {
			String aux=exp.get(i);
			if(isOperandor(aux)) {
				if(pila.empty()) {
					pila.push(aux);
				}else {
					if(isMayor(aux,pila.peek())) {
						pila.push(aux);
					}else {
						while(!pila.empty()&&!isMayor(aux,pila.peek())) {
							posfijo.add(pila.pop());
						}
						pila.push(aux);
					}
				}
			}else {
				posfijo.add(aux);
			}
		}
		while(!pila.isEmpty()) {
			posfijo.add(pila.pop());
		}
		
	}

	private boolean isMayor(String str1, String str2) {
		int op1=getJerarquia(str1);
		int op2=getJerarquia(str2);
		if(op1>op2)
			return true;
		else
			return false;
	}

	private int getJerarquia(String str) {
		switch(str){
			case"(":case")":
				return 0;
			case "+":case"-":
				return 1;
			case "*":case"/":
				return 2;
		}
		return -1;
	}

	private boolean isOperandor(String str) {
		if(str.matches("\\+|\\*|\\/|\\-"))
			return true;
		else
			return false;
	}
}
