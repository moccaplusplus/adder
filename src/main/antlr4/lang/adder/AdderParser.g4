parser grammar AdderParser;

options {
    tokenVocab = AdderLexer;
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
