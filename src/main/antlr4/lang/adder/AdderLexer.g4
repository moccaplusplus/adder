lexer grammar AdderLexer;

options {
    superClass = IndentHandlingLexer;
}

//tokens {
//    BEGIN, END, SC
//}

BEGIN: 'begin';
END: 'end';
SC: ';';


DEF: 'def';
TYPE_DEF: 'typedef';
IF: 'if';
ELSE: 'else';
WHILE: 'while';
BREAK: 'break';
CONTINUE: 'continue';
RETURN: 'return';

ARROW: '->';

COLON: ':';

COMMA: ',';
PAREN_L: '(';
PAREN_R: ')';
B_CURLY_L: '{';
B_CURLY_R: '}';
B_SQUARE_L: '[';
B_SQUARE_R: ']';

ASSIGN: '=';

MUL: '*';
DIV: '/';
PLUS: '+';
MINUS: '-';
MOD: '%';

EQ: '==';
NEQ: '!=';
GTE: '>=';
LTE: '<=';
GT: '>';
LT: '<';

AND: 'and';
OR: 'or';
NOT: 'not';

T_INT: 'Int';
T_FLOAT: 'Float';
T_BOOL: 'Bool';
T_VOID: 'Void';
T_CHAR: 'Char';
T_STRING: 'String';

NONE: 'null';

INT: [0-9]+;
FLOAT: [0-9]+ '.' [0-9]*;

BOOL: 'true' | 'false';

ID: [a-z_][a-zA-Z0-9_]*;
TYPE_ID: [A-Z][a-zA-Z0-9_]*;

CHAR:  '\'' CHAR_LITERAL '\'';
STRING:  '"' CHAR_LITERAL* '"';
fragment CHAR_LITERAL: ESC_CHAR | ~('\\'|'"');
fragment ESC_CHAR: '\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\');

ESC_NL: '\\' SINGLE_NL -> skip;
NL: SINGLE_NL+ -> channel(HIDDEN);
fragment SINGLE_NL: '\r'? '\n' | '\r';

TAB: ('\t' | '    ') -> channel(HIDDEN);
WS: ' ' -> channel(HIDDEN);

COMMENT: ( '//' ~[\r\n]* | '/*' .*? '*/' ) -> skip;
