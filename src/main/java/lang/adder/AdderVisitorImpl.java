package lang.adder;

import java.util.HashMap;
import java.util.Stack;

public class AdderVisitorImpl extends AdderParserBaseVisitor<Void> {
    private final Stack<AdderValue> stack = new Stack<>();
    private Scope scope = new Scope(null, new HashMap<>());
    private Object result;

    @Override
    public Void visitBlockStat(AdderParser.BlockStatContext ctx) {
        scope = new Scope(scope, new HashMap<>());
        try {
            for (var stat : ctx.stat()) {
                visit(stat);
            }
        } finally {
            scope = scope.parent();
        }
        return null;
    }

    @Override
    public Void visitNestedExpr(AdderParser.NestedExprContext ctx) {
        visit(ctx.expr());
        return null;
    }

    @Override
    public Void visitNoneLiteralExpr(AdderParser.NoneLiteralExprContext ctx) {
        stack.push(new AdderValue(AdderType.VOID, null));
        return null;
    }

    @Override
    public Void visitStringLiteralExpr(AdderParser.StringLiteralExprContext ctx) {
        var value = ctx.STRING().getText();
        var trimmed = value.substring(1, value.length() - 1);
        stack.push(new AdderValue(AdderType.STRING, trimmed));
        return null;
    }

    @Override
    public Void visitBoolLiteralExpr(AdderParser.BoolLiteralExprContext ctx) {
        var value = Boolean.valueOf(ctx.BOOL().getText());
        stack.push(new AdderValue(AdderType.BOOL, value));
        return null;
    }

    @Override
    public Void visitFloatLiteralExpr(AdderParser.FloatLiteralExprContext ctx) {
        var value = Double.parseDouble(ctx.FLOAT().getText());
        stack.push(new AdderValue(AdderType.FLOAT, value));
        return null;
    }

    @Override
    public Void visitIntLiteralExpr(AdderParser.IntLiteralExprContext ctx) {
        var value = Integer.parseInt(ctx.INT().getText());
        stack.push(new AdderValue(AdderType.INT, value));
        return null;
    }

    @Override
    public Void visitIdentifierExpr(AdderParser.IdentifierExprContext ctx) {
        var identifier = ctx.ID();
        var value = scope.read(identifier);
        stack.push(value);
        return null;
    }

    @Override
    public Void visitExprStat(AdderParser.ExprStatContext ctx) {
        visit(ctx.expr());
        while (!stack.isEmpty()) {
            result = stack.pop().value();
        }
        return null;
    }

    public Object getResult() {
        return result;
    }
}
