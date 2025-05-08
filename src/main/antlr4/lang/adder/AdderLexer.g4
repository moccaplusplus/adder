lexer grammar AdderLexer;

options {
    superClass = IndentHandlingLexer;
}

tokens {
    BEGIN, END, SC
}

DEF: 'def';
IF: 'if';
ELSE: 'else';
WHILE: 'while';
BREAK: 'break';
CONTINUE: 'continue';

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

T_INT: 'int';
T_FLOAT: 'float';
T_BOOL: 'bool';
T_VOID: 'void';
T_CHAR: 'char';
T_STRING: 'string';

NONE: 'none';

INT: [0-9]+;
FLOAT: [0-9]+ '.' [0-9]*;

BOOL: 'true' | 'false';

ID: [a-zA-Z_][a-zA-Z0-9_]*;

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
