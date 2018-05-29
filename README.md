# Analizador-Lexico

# Analizador Sintactico:

##Reglas:

###SMain

###SOuto

###SIf

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
