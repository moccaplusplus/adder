package lang.adder.engine;

import lang.adder.parser.AdderBaseVisitor;
import lang.adder.parser.AdderParser;
import org.antlr.v4.runtime.Token;

import javax.script.ScriptContext;
import java.util.Stack;

public class AdderVisitorImpl extends AdderBaseVisitor<Void> {
    private final Stack<Integer> stack = new Stack<>();
    private final ScriptContext context;
    private Integer result;

    public AdderVisitorImpl(ScriptContext context) {
        this.context = context;
    }

    @Override
    public Void visitNestedExpr(AdderParser.NestedExprContext ctx) {
        visit(ctx.expr());
        return null;
    }

    @Override
    public Void visitIntLiteralExpr(AdderParser.IntLiteralExprContext ctx) {
        var value = Integer.parseInt(ctx.INT().getText());
        stack.push(value);
        return null;
    }

    @Override
    public Void visitMulDivExpr(AdderParser.MulDivExprContext ctx) {
        visitBinaryExpr(ctx.expr(0), ctx.expr(1), ctx.op);
        return null;
    }

    @Override
    public Void visitPlusMinusExpr(AdderParser.PlusMinusExprContext ctx) {
        visitBinaryExpr(ctx.expr(0), ctx.expr(1), ctx.op);
        return null;
    }

    @Override
    public Void visitAssignExpr(AdderParser.AssignExprContext ctx) {
        var varName = ctx.ID().getText();
        int scope = context.getAttributesScope(varName);
        // throw if not exists
        if (scope == -1) {
            throw new IllegalArgumentException("Var not exists"); // TODO: proper exception
        }
        visit(ctx.expr());
        var value = stack.peek();
        // save
        context.setAttribute(varName, value, scope);

        return null;
    }

    @Override
    public Void visitVarReadExpr(AdderParser.VarReadExprContext ctx) {
        var varName = ctx.ID().getText();
        // throw if not exists
        int scope = context.getAttributesScope(varName);
        if (scope == -1) {
            throw new IllegalArgumentException("Var not exists"); // TODO: proper exception
        }
        var value = (Integer) context.getAttribute(varName, scope);
        stack.push(value);
        return null;
    }

    @Override
    public Void visitVarDeclStat(AdderParser.VarDeclStatContext ctx) {
        var bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        var varName = ctx.ID().getText();
        // throw if redeclaration
        if (bindings.containsKey(varName)) {
            throw new IllegalArgumentException("Redeclaration"); // TODO: proper exception
        }
        Integer value = null;
        if (ctx.expr() != null) {
            visit(ctx.expr());
            value = stack.pop();
        }
        // save
        bindings.put(varName, value);
        return null;
    }

    @Override
    public Void visitAndOrExpr(AdderParser.AndOrExprContext ctx) {
        visitBooleanExpr(ctx.expr(0), ctx.expr(1), ctx.op);
        return null;
    }

    @Override
    public Void visitCompareExpr(AdderParser.CompareExprContext ctx) {
        visitBooleanExpr(ctx.expr(0), ctx.expr(1), ctx.op);
        return null;
    }

    @Override
    public Void visitEqualsExpr(AdderParser.EqualsExprContext ctx) {
        visitBooleanExpr(ctx.expr(0), ctx.expr(1), ctx.op);
        return null;
    }

    @Override
    public Void visitExprStat(AdderParser.ExprStatContext ctx) {
        visit(ctx.expr());
        result = stack.pop();
        return null;
    }

    @Override
    public Void visitIfStat(AdderParser.IfStatContext ctx) {
        visit(ctx.expr());
        var value = stack.pop();
        if (asBoolean(value)) {
            visit(ctx.thenBlock());
        } else if (ctx.elseBlock() != null) {
            visit(ctx.elseBlock());
        }
        return null;
    }

    @Override
    public Void visitWhileStat(AdderParser.WhileStatContext ctx) {
        while (true) {
            visit(ctx.expr());
            var value = stack.pop();
            if (!asBoolean(value)) break;
            visit(ctx.thenBlock());
        }
        return null;
    }

    public Integer getResult() {
        return result;
    }

    private void visitBinaryExpr(AdderParser.ExprContext leftExpr, AdderParser.ExprContext rightExpr, Token op) {
        visit(leftExpr);
        var left = stack.pop();
        visit(rightExpr);
        var right = stack.pop();
        var value = switch (op.getType()) {
            case AdderParser.PLUS -> left + right;
            case AdderParser.MINUS -> left - right;
            case AdderParser.MUL -> left * right;
            case AdderParser.DIV -> left / right;
            default -> throw new IllegalStateException("Impossible to happen");
        };
        stack.push(value);
    }

    private void visitBooleanExpr(AdderParser.ExprContext leftExpr, AdderParser.ExprContext rightExpr, Token op) {
        visit(leftExpr);
        var left = stack.pop();
        visit(rightExpr);
        var right = stack.pop();
        var value = switch (op.getType()) {
            case AdderParser.EQ -> left == right;
            case AdderParser.NEQ -> left != right;
            case AdderParser.LT -> left < right;
            case AdderParser.LTE -> left <= right;
            case AdderParser.GT -> left > right;
            case AdderParser.GTE -> left >= right;
            case AdderParser.AND -> asBoolean(left) && asBoolean(right);
            case AdderParser.OR -> asBoolean(left) || asBoolean(right);
            default -> throw new IllegalStateException("Impossible to happen");
        };
        stack.push(value ? 1 : 0);
    }

    private static boolean asBoolean(Integer value) {
        return value != null && value != 0;
    }
}
