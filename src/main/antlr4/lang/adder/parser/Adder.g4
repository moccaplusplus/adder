grammar Adder;

start: stat* EOF?;
stat:
      'def' ID ('=' expr)?                           # varDeclStat
    | 'if' expr thenBlock elseBlock? 'end'           # ifStat
    | 'while' expr thenBlock 'end'                   # whileStat
    | expr                                           # exprStat
    | END+                                           # endStat
    ;
thenBlock: 'then' stat+;
elseBlock: 'else' stat+;

expr:
      INT                                           # intLiteralExpr
    | ID                                            # varReadExpr
    | '(' expr ')'                                  # nestedExpr
    | expr op=(MUL | DIV) expr                      # mulDivExpr
    | expr op=(PLUS | MINUS) expr                   # plusMinusExpr
    | expr op=(LT | LTE | GT | GTE) expr            # compareExpr
    | expr op=(EQ | NEQ) expr                       # equalsExpr
    | expr op=(AND | OR) expr                       # andOrExpr
    | ID '=' expr                                   # assignExpr
    ;

fragment DIGIT: [0-9];
fragment CHARACTER: [a-zA-Z];

PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';

LT: '<';
LTE: '<=';
GT: '>';
GTE: '>=';
EQ: '==';
NEQ: '!=';
AND: 'and';
OR: 'or';

ID: ('_' | CHARACTER) ('_' | CHARACTER | DIGIT)*;

INT: DIGIT+;

END: '\n' | ('\r\n');

WS: [ ]+ -> skip;