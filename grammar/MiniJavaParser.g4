parser grammar MiniJavaParser;

options {
    tokenVocab = MiniJavaLexer;
}
program
    : mainClass classDeclaration* EOF
    ;
    mainClass
    : CLASS IDENTIFIER LBRACE
      PUBLIC STATIC VOID MAIN
      LPAREN STRING LBRACKET RBRACKET IDENTIFIER RPAREN
      block
      RBRACE
    ;
    
    block
    : LBRACE (variableDeclaration | statement)* RBRACE
    ;
    classDeclaration
    : CLASS IDENTIFIER LBRACE
      variableDeclaration*
      methodDeclaration*
      RBRACE
    ;

variableDeclaration
    : type IDENTIFIER (ASSIGN expression)? SEMI
    ;

type
    : INT
    | BOOLEAN
    | IDENTIFIER
    ;
    parameterList
    : parameter (COMMA parameter)*
    ;

parameter
    : type IDENTIFIER
    ;
    methodDeclaration
    : PUBLIC type IDENTIFIER LPAREN parameterList? RPAREN
      LBRACE
      variableDeclaration*
      statement*
      RETURN expression SEMI
      RBRACE
    ;
    assignmentStatement
    : IDENTIFIER ASSIGN expression SEMI
    ;
    statement
    : assignmentStatement
    | block
    | ifStatement
    | whileStatement
    | forStatement
    | printStatement
    | methodCallStatement
    ;
    printStatement
    : IDENTIFIER DOT IDENTIFIER DOT IDENTIFIER
      LPAREN expression RPAREN SEMI
    ;
    methodCallStatement
    : IDENTIFIER LPAREN argumentList? RPAREN SEMI
    ;
    ifStatement
    : IF LPAREN expression RPAREN statement
      (ELSE statement)?
    ;
    whileStatement
    : WHILE LPAREN expression RPAREN statement
    ;
    forStatement
    : FOR LPAREN forInitializer? SEMI expression? SEMI forUpdate? RPAREN statement
    ;
    forInitializer
    : type IDENTIFIER (ASSIGN expression)?
    | IDENTIFIER ASSIGN expression
    ;
    forUpdate
    : IDENTIFIER ASSIGN expression
    ;
    expression
    : logicalOrExpression
    ;

logicalOrExpression
    : logicalAndExpression (OR logicalAndExpression)*
    ;

logicalAndExpression
    : equalityExpression (AND equalityExpression)*
    ;

equalityExpression
    : relationalExpression ((EQUAL | NOT_EQUAL) relationalExpression)*
    ;

relationalExpression
    : additiveExpression
      ((LESS_THAN | GREATER_THAN | LESS_EQUAL | GREATER_EQUAL) additiveExpression)*
    ;

additiveExpression
    : multiplicativeExpression ((PLUS | MINUS) multiplicativeExpression)*
    ;

multiplicativeExpression
    : unaryExpression ((MULTIPLY | DIVIDE | MODULO) unaryExpression)*
    ;

unaryExpression
    : (NOT | MINUS) unaryExpression
    | primaryExpression
    ;
    primaryExpression
    : INTEGER_LITERAL
    | BOOLEAN_LITERAL
    | IDENTIFIER LPAREN argumentList? RPAREN
    | IDENTIFIER
    | NEW IDENTIFIER LPAREN RPAREN
    | LPAREN expression RPAREN
    ;
    argumentList
    : expression (COMMA expression)*
    ;