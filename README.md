# Analizador Sintactico:
# Prueba
## Reglas:

__Sstarto__ -> starto () {cuerpo}

_cuerpo_ -> Souto Mas_Instrucciones | 
	  SDeclaracion Mas_Instrucciones | 
	  SOperacion Mas_Instrucciones | 
	  Sif Mas_Instrucciones | 
	  Sfrom Mas_Instrucciones

---

__Souto__ -> outo (Cuerpo_outo) ;

_Cuerpo_outo_ -> Contenido Mas_Contenido | E

_Mas_Contenido_ -> + Contenido | E

_Contenido_ -> Identificador | Numero | Cadena_caracteres

---

__SDeclaracion__ -> Tipo_dato Identificador Asignacion ;

_Tipo_dato_ -> int | double | float | String 

_Asignacion_ -> = Valor | E

_Valor_ -> Expresion_individual | Caracter | Cadena_caracteres | True | False

---

__SOperacion__ -> Identificador = Expresion_individual ;

---

__Sif__ -> if(Operador_NOT Condicion){cuerpo} Selse

_Operador_NOT_ -> NOT|E

_Condicion_ -> Expresion_individual Operador_relacional Expresion_individual Mas_Condiciones

_Expresion_individual_ -> IdNum Expresion

_Expresion_ -> Operador_aritmetico IdNum Mas_expresiones | E

_Operador_aritmetico_ -> +|-|%|/|*

_Mas_expresiones_ -> Expresion | E

_Operador_relacional_ -> ==|!=|<|>|<=|>=

_Mas_condiciones_ -> Operador_logico condicion | E

_Operador_logico_ -> AND|OR

__Selse__ -> else {cuerpo} | E

---

__Sfrom__ -> from (Condicion_Inicial) to (Condicion) inc (IdNum) {cuerpo}

_Condicion_Inicial_ -> Expresion_individual| SDeclaracion
