package lang.adder;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.Reader;

public class AdderProgram {
    private final AdderParser.StartContext parseTree;

    public AdderProgram(AdderParser.StartContext parseTree) {
        this.parseTree = parseTree;
    }

    public Object eval() {
        // TODO: implement visitor
        // TODO: replace with AST visitor not generated parse tree visitor
        //  (generated visitor's ancestor will be used for transformation to AST instead).
        var visitor = new AdderVisitorImpl();
        visitor.visit(parseTree);
        return visitor.getResult();
    }

    public static Object eval(String script) {
        return compile(script).eval();
    }

    public static Object eval(Reader reader) throws IOException {
        return compile(reader).eval();
    }

    public static AdderProgram compile(String script) {
        return compile(CharStreams.fromString(script));
    }

    public static AdderProgram compile(Reader reader) throws IOException {
        return compile(CharStreams.fromReader(reader));
    }

    private static AdderProgram compile(CharStream charStream) {
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

        return new AdderProgram(parseTree);
    }

}
