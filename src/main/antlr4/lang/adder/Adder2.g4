grammar Adder2;

@lexer::members {
    private final java.util.Queue<Token> queue = new java.util.LinkedList<>();
    private boolean counting = true;
    private int previousIndent;
    private int currentIndent;

    @Override
    public void reset() {
        super.reset();
        counting = true;
    }

    @Override
    public Token nextToken() {
        var nextToken = queue.isEmpty() ? super.nextToken() : queue.poll();

        if (counting) {
            if (nextToken.getType() == TAB) {
                currentIndent++;
                return nextToken;
            }
            if (currentIndent > previousIndent) {
                previousIndent++;
                queue.offer(nextToken);
                return newIndentToken(BEGIN, nextToken);
            }
            if (currentIndent < previousIndent) {
                previousIndent--;
                queue.offer(nextToken);
                return newIndentToken(END, nextToken);
            }
            counting = false;
        }

        if (nextToken.getType() == NL) {
            currentIndent = 0;
            counting = true;
        }
        return nextToken;
    }

    private Token newIndentToken(int type, Token token) {
        var newToken = new CommonToken(type, "");
        newToken.setLine(token.getLine());
        newToken.setCharPositionInLine(token.getCharPositionInLine() + token.getText().length());
        return newToken;
    }
}

//https://github.com/wevre/wry/blob/master/grammars/DentLexer.g4

start: stat* EOF?;

stat:
      DEF ID COLON typeSpec NL                      # varDeclStat
    | IF expr COLON stat (ELSE stat)? NL            # ifStat
    | WHILE expr COLON stat NL                      # whileStat
    | DEF ID (COLON typeSpec)? ASSIGN expr NL       # varDeclAssignStat
    | expr NL                                       # exprStat
    | BEGIN stat* END                               # blockStat
    | NL                                            # emptyStat
    ;

expr:
    PAREN_L expr PAREN_R                            # nestedExpr
    | MINUS expr                                    # unaryMinusExpr
    | NOT expr                                      # negationExpr
    | expr op=(MUL | DIV | MOD) expr                # mulDivModExpr
    | expr op=(PLUS | MINUS) expr                   # plusMinusExpr
    | expr op=(LT | LTE | GT | GTE) expr            # compareExpr
    | expr op=(EQ | NEQ) expr                       # equalsExpr
    | expr op=(AND | OR) expr                       # andOrExpr
    | STRING                                        # stringLiteralExpr
    | CHAR                                          # charLiteralExpr
    | BOOL                                          # boolLiteralExpr
    | FLOAT                                         # floatLiteralExpr
    | INT                                           # intLiteralExpr
    | NONE                                          # noneLiteralExpr
    | ID                                            # identifierExpr
    | <assoc=right> ID ASSIGN expr                  # assignExpr
    ;

typeSpec:
    T_INT | T_FLOAT | T_BOOL | T_CHAR | T_STRING | T_VOID;

BEGIN: 'begin';
END: 'end';

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
