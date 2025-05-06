lexer grammar AdderLexer;

options {
    superClass = LexerExt;
}

tokens {
    BEGIN, END
}

DEF: 'def';
IF: 'if';
ELSE: 'else';
WHILE: 'while';

COLON: ':';
COMMA: ',';
PAREN_L: '(';
PAREN_R: ')';

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

NL: ( '\r'? '\n' | '\r' );
TAB: ('\t' | '    ') -> channel(HIDDEN);
WS: ' ' -> channel(HIDDEN);

COMMENT: ( '//' ~[\r\n]* | '/*' .*? '*/' ) -> skip;
