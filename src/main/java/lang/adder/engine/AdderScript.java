package lang.adder.engine;

import lang.adder.parser.AdderParser;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class AdderScript extends CompiledScript {
    private final AdderEngine engine;
    private final AdderParser.StartContext tree;

    public AdderScript(AdderEngine engine, AdderParser.StartContext tree) {
        this.engine = engine;
        this.tree = tree;
    }
    @Override
    public Object eval(ScriptContext context) throws ScriptException {
        var visitor = new AdderVisitorImpl(context);
        visitor.visit(tree);
        return visitor.getResult();
    }

    @Override
    public ScriptEngine getEngine() {
        return engine;
    }
}
