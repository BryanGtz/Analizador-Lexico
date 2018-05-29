# Analizador Sintactico:

## Reglas:

Sstarto -> starto () {cuerpo}

---

cuerpo -> Souto Mas_Instrucciones 
		  | SDeclaracion Mas_Instrucciones 
		  | SOperacion Mas_Instrucciones 
		  | Sif Mas_Instrucciones 
		  | Sfrom Mas_Instrucciones
---

Souto -> outo (Cuerpo_outo) ;

Cuerpo_outo -> Contenido Mas_Contenido | E

Mas_Contenido -> + Contenido | E

Contenido -> Identificador | Numero | Cadena_caracteres

---

SDeclaracion -> Tipo_dato Identificador Asignacion ;

Tipo_dato -> int | double | float | String 

Asignacion -> = Valor | E

Valor -> Expresion_individual | Caracter | Cadena_caracteres | True | False

---

SOperacion -> Identificador = Expresion_individual ;

---

Sif -> if(Operador_NOT Condicion){cuerpo} Selse

Operador_NOT -> NOT|E

Condicion -> Expresion_individual Operador_relacional Expresion_individual Mas_Condiciones

Expresion_individual -> IdNum Expresion

Expresion -> Operador_aritmetico IdNum Mas_expresiones | E

Operador_aritmetico -> +|-|%|/|*

Mas_expresiones -> Expresion | E

Operador_relacional -> ==|!=|<|>|<=|>=

Mas_condiciones -> Operador_logico condicion | E

Operador_logico -> AND|OR

Selse -> else {cuerpo} | E

---

Sfrom -> from (Condicion_Inicial) to (Condicion) inc (IdNum) {cuerpo}

Condicion_Inicial -> Expresion_individual| SDeclaracion
