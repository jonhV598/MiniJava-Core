lexer grammar MiniJavaLexer;
// Palavras reservadas

CLASS   : 'class';
PUBLIC  : 'public';
STATIC  : 'static';
VOID    : 'void';
MAIN    : 'main';
STRING  : 'String';

INT     : 'int';
BOOLEAN : 'boolean';

IF      : 'if';
ELSE    : 'else';
WHILE   : 'while';
FOR     : 'for';

RETURN  : 'return';
NEW     : 'new';

BOOLEAN_LITERAL : 'true' | 'false';

// Operadores relacionais
EQUAL       : '==';
NOT_EQUAL   : '!=';
LESS_EQUAL  : '<=';
GREATER_EQUAL : '>=';

LESS_THAN : '<';
GREATER_THAN : '>';

// Operadores logicos
AND : '&&';
OR  : '||';
NOT : '!';

// Operadores aritmeticos
PLUS    : '+';
MINUS   : '-';
MULTIPLY : '*';
DIVIDE  : '/';
MODULO  : '%';

// Atribuicao
ASSIGN : '=';

// Delimitadores
LPAREN    : '(';
RPAREN    : ')';

LBRACE    : '{';
RBRACE    : '}';

LBRACKET  : '[';
RBRACKET  : ']';

SEMI      : ';';
COMMA     : ',';
DOT       : '.';

// Identificadores
IDENTIFIER : [a-zA-Z_][a-zA-Z_0-9]*;

// Numeros inteiros
INTEGER_LITERAL : [0-9]+;

// Espaços em branco
WS : [ \t\r\n]+ -> skip;

// Comentários de uma linha
LINE_COMMENT : '//' ~[\r\n]* -> skip;

// Comentários de múltiplas linhas
BLOCK_COMMENT : '/*' .*? '*/' -> skip;