parser grammar AdderParser;

options {
    tokenVocab = AdderLexer;
}

//https://github.com/wevre/wry/blob/master/grammars/DentLexer.g4

start: stat* EOF?;

stat:
      DEF ID COLON typeSpec SC                      # varDeclStat
    | DEF ID (COLON typeSpec)? ASSIGN expr SC       # varDeclAssignStat
    | IF expr COLON stat (ELSE stat)? SC            # ifStat
    | WHILE expr COLON stat SC                      # whileStat
    | op=(BREAK | CONTINUE) SC                      # loopControlStat
    | expr SC                                       # exprStat
    | BEGIN stat* END                               # blockStat
    | SC                                            # emptyStat
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
    T_INT | T_FLOAT | T_BOOL | T_CHAR | T_STRING | T_VOID | typeSpec PAREN_L PAREN_R;
