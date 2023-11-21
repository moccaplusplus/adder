grammar Adder;

start: expr EOF?;
expr:
      INT                              # intLiteralExpr
    | expr op=(PLUS | MINUS) expr      # plusMinusExpr
    ;

PLUS: '+';
MINUS: '-';
INT: [0-9]+;