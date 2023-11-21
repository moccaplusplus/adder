package lang.adder.engine;

import lang.adder.parser.AdderBaseVisitor;
import lang.adder.parser.AdderParser;
import org.antlr.v4.runtime.Token;

public class AdderVisitorImpl extends AdderBaseVisitor<Integer> {
    @Override
    public Integer visitStart(AdderParser.StartContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Integer visitNestedExpr(AdderParser.NestedExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Integer visitIntLiteralExpr(AdderParser.IntLiteralExprContext ctx) {
        return Integer.parseInt(ctx.INT().getText());
    }

    @Override
    public Integer visitMulDivExpr(AdderParser.MulDivExprContext ctx) {
        return visitBinaryExpr(ctx.expr(0), ctx.expr(1), ctx.op);
    }

    @Override
    public Integer visitPlusMinusExpr(AdderParser.PlusMinusExprContext ctx) {
        return visitBinaryExpr(ctx.expr(0), ctx.expr(1), ctx.op);
    }

    private Integer visitBinaryExpr(AdderParser.ExprContext leftExpr, AdderParser.ExprContext rightExpr, Token op) {
        var left = visit(leftExpr);
        var right = visit(rightExpr);
        return switch (op.getType()) {
            case AdderParser.PLUS -> left + right;
            case AdderParser.MINUS -> left - right;
            case AdderParser.MUL -> left * right;
            case AdderParser.DIV -> left / right;
            default -> throw new IllegalStateException("Impossible to happen");
        };
    }
}
