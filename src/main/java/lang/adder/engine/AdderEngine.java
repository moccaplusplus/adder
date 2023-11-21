package lang.adder.engine;

import lang.adder.parser.AdderLexer;
import lang.adder.parser.AdderParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.Reader;

public class AdderEngine extends AbstractScriptEngine {
    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return eval(CharStreams.fromString(script), context);
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        try {
            return eval(CharStreams.fromReader(reader), context);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public Bindings createBindings() {
        return null;
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return null;
    }

    private Object eval(CharStream charStream, ScriptContext context) throws ScriptException {
        var lexer = new AdderLexer(charStream);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new AdderParser(tokenStream);
        var tree = parser.start();
        var visitor = new AdderVisitorImpl();
        return visitor.visit(tree);
    }
}