grammar Adder;

start: expr EOF?;
expr:
      INT                              # intLiteralExpr
    | '(' expr ')'                     # nestedExpr
    | expr op=(MUL | DIV) expr         # mulDivExpr
    | expr op=(PLUS | MINUS) expr      # plusMinusExpr
    ;

PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
INT: [0-9]+;

WS: [ ]+ -> skip;