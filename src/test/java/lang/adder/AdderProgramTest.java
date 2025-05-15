package lang.adder;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

class AdderProgramTest {
    @Test
    void test() {
        String prog = "def a = 1\n    def b = 2\ndef c = 2\n";
        var cs = CharStreams.fromString(prog);
        var lexer = new AdderLexer(cs);
        var tokenStream = new CommonTokenStream(lexer);
        tokenStream.fill();
        System.out.println(tokenStream.getTokens());
    }

    @Test
    void test2() {
        String prog = "def a = 1\n    def b = 2\ndef c = 2\n";
        var cs = CharStreams.fromString(prog);
        var lexer = new AdderLexer(cs);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new AdderParser(tokenStream);
        var visitor = new AdderVisitorImpl();
        visitor.visit(parser.program());
    }
}