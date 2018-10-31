package analizador_semantico;

import java.util.ArrayList;
import java.util.Stack;

public class EvaludadorExpresion {
	
	private ArrayList<String> posfijo;
	private String tipo;
	private double valor;
	
	public EvaludadorExpresion(ArrayList<String> exp,String tipo) {
		this.tipo=tipo;
		toPosfijo(exp);
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
		if(!pila.isEmpty()) {
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
