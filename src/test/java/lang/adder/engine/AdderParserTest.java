package lang.adder.engine;

import lang.adder.parser.AdderBaseListener;
import lang.adder.parser.AdderBaseVisitor;
import lang.adder.parser.AdderLexer;
import lang.adder.parser.AdderParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AdderParserTest {
    @Test
    void listenerTest() {
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

    @Test
    void visitorTest() {
        // given
        var expr = "1+3-5";

        //when
        var charStream = CharStreams.fromString(expr);
        var lexer = new AdderLexer(charStream);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new AdderParser(tokenStream);
        var tree = parser.start();
        var visitor = new AdderBaseVisitor<Integer>() {

            @Override
            public Integer visitStart(AdderParser.StartContext ctx) {
                return visit(ctx.expr());
            }

            @Override
            public Integer visitIntLiteralExpr(AdderParser.IntLiteralExprContext ctx) {
                return Integer.parseInt(ctx.INT().getText());
            }

            @Override
            public Integer visitPlusMinusExpr(AdderParser.PlusMinusExprContext ctx) {
                var left = visit(ctx.expr(0));
                var right = visit(ctx.expr(1));
                return switch (ctx.op.getType()) {
                    case AdderParser.PLUS -> left + right;
                    case AdderParser.MINUS -> left - right;
                    default -> throw new IllegalStateException("Impossible to happen");
                };
            }
        };
        var result = visitor.visit(tree);

        // then
        Assertions.assertEquals(-1, result);
    }
}