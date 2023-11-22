grammar Adder;

start: (stat? END)* stat? EOF?;
stat:
      'def' ID ('=' expr)?             # varDeclStat
    | expr                             # exprStat
    ;
expr:
      INT                              # intLiteralExpr
    | ID                               # varReadExpr
    | '(' expr ')'                     # nestedExpr
    | expr op=(MUL | DIV) expr         # mulDivExpr
    | expr op=(PLUS | MINUS) expr      # plusMinusExpr
    | ID '=' expr                      # assignExpr
    ;

fragment DIGIT: [0-9];
fragment CHARACTER: [a-zA-Z];

PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';

ID: ('_' | CHARACTER) ('_' | CHARACTER | DIGIT)*;

INT: DIGIT+;

END: '\n' | ('\r\n');

WS: [ ]+ -> skip;