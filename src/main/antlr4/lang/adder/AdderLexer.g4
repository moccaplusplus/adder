lexer grammar AdderLexer;

DEF: 'def';

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
T_STRING: 'string';

NONE: 'none';

INT: [0-9]+;
FLOAT: [0-9]+ '.' [0-9]*;

BOOL: 'true' | 'false';

ID: [a-zA-Z_][a-zA-Z0-9_]*;

STRING:  '"' ( ESC_CHAR | ~('\\'|'"') )* '"';
fragment ESC_CHAR: '\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\');

NEWLINE : ( '\r'? '\n' | '\r' ) {
//	if (pendingDent) { setChannel(HIDDEN); }
//	pendingDent = true;
//	indentCount = 0;
//	initialIndentToken = null;
} ;

WS : [ \t]+ -> channel(HIDDEN);

INDENT : 'INDENT' -> channel(HIDDEN);
DEDENT : 'DEDENT' -> channel(HIDDEN);

COMMENT: ( '//' ~[\r\n]* | '/*' .*? '*/' ) -> skip;
