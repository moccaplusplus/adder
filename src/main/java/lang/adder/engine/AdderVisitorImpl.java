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
        var bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        var varName = ctx.ID().getText();
        // throw if not exists
        if (!bindings.containsKey(varName)) {
            throw new IllegalArgumentException("Var not exists"); // TODO: proper exception
        }
        visit(ctx.expr());
        var value = stack.peek();
        // save
        bindings.put(varName, value);

        return null;
    }

    @Override
    public Void visitVarReadExpr(AdderParser.VarReadExprContext ctx) {
        var bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        var varName = ctx.ID().getText();
        // throw if not exists
        if (!bindings.containsKey(varName)) {
            throw new IllegalArgumentException("Var not exists"); // TODO: proper exception
        }
        var value = (Integer) bindings.get(varName);
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
    public Void visitExprStat(AdderParser.ExprStatContext ctx) {
        visit(ctx.expr());
        result = stack.pop();
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
        var value =  switch (op.getType()) {
            case AdderParser.PLUS -> left + right;
            case AdderParser.MINUS -> left - right;
            case AdderParser.MUL -> left * right;
            case AdderParser.DIV -> left / right;
            default -> throw new IllegalStateException("Impossible to happen");
        };
        stack.push(value);
    }
}
