package lang.adder.engine;

import lang.adder.parser.AdderLexer;
import lang.adder.parser.AdderParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.IOException;
import java.io.Reader;

public class AdderEngine extends AbstractScriptEngine implements Compilable {

    private final AdderEngineFactory engineFactory;
    public AdderEngine(AdderEngineFactory engineFactory) {
        this.engineFactory = engineFactory;
    }

    public AdderEngine() {
        this(null);
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return compile(script).eval(context);
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        return compile(reader).eval(context);
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return engineFactory;
    }

    @Override
    public CompiledScript compile(String script) throws ScriptException {
        return compile(CharStreams.fromString(script));
    }

    @Override
    public CompiledScript compile(Reader script) throws ScriptException {
        try {
            return compile(CharStreams.fromReader(script));
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    private CompiledScript compile(CharStream charStream) {
        var lexer = new AdderLexer(charStream);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new AdderParser(tokenStream);
        var tree = parser.start();
        return new AdderScript(this, tree);
    }
}