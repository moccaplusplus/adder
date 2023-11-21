package lang.adder.engine;

import lang.adder.parser.AdderBaseListener;
import lang.adder.parser.AdderLexer;
import lang.adder.parser.AdderParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Test;

class AdderEngineTest {
    @Test
    void firstTest() {
        var expr = "1+3-5";
        var charStream = CharStreams.fromString(expr);
        var lexer = new AdderLexer(charStream);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new AdderParser(tokenStream);
        var tree = parser.start();

        var listener = new AdderBaseListener() {
            @Override
            public void enterIntLiteralExpr(AdderParser.IntLiteralExprContext ctx) {
                System.out.printf("intExpr: %s%n", ctx.INT().getText());
            }

            @Override
            public void enterPlusMinusExpr(AdderParser.PlusMinusExprContext ctx) {
                System.out.printf("exprExpr: left=%s right=%s op=%s%n",
                        ctx.expr(0).getText(), ctx.expr(1).getText(), ctx.op.getText());
            }
        };
        ParseTreeWalker.DEFAULT.walk(listener, tree);
    }
}