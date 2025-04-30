package lang.adder;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;

public class AdderProgram extends CompiledScript {
    private final AdderEngine engine;
    private final AdderParser.StartContext parseTree;

    public AdderProgram(AdderEngine engine, AdderParser.StartContext parseTree) {
        this.engine = engine;
        this.parseTree = parseTree;
    }

    @Override
    public Object eval(ScriptContext context) throws ScriptException {
        // TODO: implement visitor
        // TODO: replace with AST visitor not generated parse tree visitor
        //  (generated visitor's ancestor will be used for transformation to AST instead).
        var visitor = new AdderParserBaseVisitor<>();
        try {
            return visitor.visit(parseTree);
        } catch (ParseException e) {
            throw (ScriptException) e.getCause();
        } catch (RuntimeException e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public AdderEngine getEngine() {
        return engine;
    }

    public static AdderProgram compile(AdderEngine engine, CharStream charStream) {
        // custom error listener
        var errorListener = new ParseErrorListener();

        //  parse tokens
        var lexer = new AdderLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);
        var tokenStream = new CommonTokenStream(lexer);

        // create parse tree
        var parser = new AdderParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        var parseTree = parser.start();

        // TODO: add transformation form Parse-Tree to AST. (Semantic analysis)

        return new AdderProgram(engine, parseTree);
    }

    public static class ParseErrorListener extends BaseErrorListener {
        @Override
        public void syntaxError(
                Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg,
                RecognitionException e) {
            throw new ParseException(new ScriptException(msg, null, line, charPositionInLine));
        }
    }

    public static class ParseException extends RuntimeException {
        public ParseException(ScriptException cause) {
            super(cause);
        }
    }
}
