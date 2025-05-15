parser grammar AdderParser;

options {
    tokenVocab = AdderLexer;
}

//https://github.com/wevre/wry/blob/master/grammars/DentLexer.g4

program: stat* EOF?;

stat:
    DEF ID (COLON typeSpec | (COLON typeSpec)? ASSIGN expr) SC      # varDeclStat
    | DEF ID genericTypeList? paramList
        (ARROW typeSpec)? COLON stat                                # funcDeclStat
    | TYPE_DEF TYPE_ID genericTypeList? ASSIGN typeSpec SC          # typeDefStat
    | RETURN expr? SC                                               # returnStatement
    | IF expr COLON stat (ELSE stat)? SC                            # ifStat
    | WHILE expr COLON stat SC                                      # whileStat
    | op=(BREAK | CONTINUE) SC                                      # loopControlStat
    | expr SC                                                       # exprStat
    | BEGIN stat* END                                               # blockStat
    | SC                                                            # emptyStat
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
    | genericTypeList? paramList (ARROW typeSpec)? COLON (expr | BEGIN stat* END)  # lambdaExpr
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
    op=(T_INT | T_FLOAT | T_BOOL | T_CHAR | T_STRING | T_VOID)
    | TYPE_ID (LT typeSpec (COMMA typeSpec)* GT)?
    | typeSpec B_SQUARE_L B_SQUARE_R
    | PAREN_L typeSpec PAREN_R B_SQUARE_L B_SQUARE_R
    | genericTypeList? PAREN_L (typeSpec (COMMA typeSpec)*)? PAREN_R ARROW typeSpec
    ;

paramDecl:
    ID (COLON typeSpec)?;

optParamDecl:
    ID (COLON typeSpec)? (ASSIGN expr);

paramList:
    PAREN_L (paramDecl (COMMA paramDecl)* (COMMA optParamDecl)*)? PAREN_R
    | PAREN_L (optParamDecl (COMMA optParamDecl)*)? PAREN_R
    ;

genericTypeList:
    LT TYPE_ID (COMMA TYPE_ID)* GT;
