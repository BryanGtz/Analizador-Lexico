/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador_semantico;

import java.util.Objects;

/**
 *
 * @author User
 */
public class Simbolo {
	String nombre;
	String tipo_dato;
	Object valor;
  
  public Simbolo(String nombre){
      this.nombre = nombre;
      this.tipo_dato = null;
      this.valor = null;
  }

	public Simbolo(String nombre, String tipo_dato, Object valor) {
		this.nombre = nombre;
		this.tipo_dato = tipo_dato;
		this.valor = valor;
	}

	public Simbolo(Simbolo s) {
		this.nombre = s.getNombre();
		this.tipo_dato = s.getTipo_dato();
		this.valor = s.getValor();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo_dato() {
		return tipo_dato;
	}

	public void setTipo_dato(String tipo_dato) {
		this.tipo_dato = tipo_dato;
	}

	public Object getValor() {
		return valor;
	}

	public void setValor(Object valor) {
		this.valor = valor;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Simbolo other = (Simbolo) obj;
		if (!Objects.equals(this.nombre, other.nombre)) {
			return false;
		}
		return true;
	}
}
