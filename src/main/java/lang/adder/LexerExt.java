package lang.adder;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import java.util.LinkedList;
import java.util.Queue;

public abstract class LexerExt extends Lexer {
    private final Queue<Token> queue = new LinkedList<>();
    private boolean counting = true;
    private int previousIndent;
    private int currentIndent;

    public LexerExt(CharStream input) {
        super(input);
    }

    @Override
    public void reset() {
        super.reset();
        counting = true;
        queue.clear();
        previousIndent = 0;
        currentIndent = 0;
    }

    @Override
    public Token nextToken() {
        var nextToken = queue.isEmpty() ? super.nextToken() : queue.poll();
        if (counting) {
            if (nextToken.getType() == getTokenTypeMap().get("TAB")) {
                currentIndent++;
                return nextToken;
            }
            if (currentIndent > previousIndent) {
                previousIndent++;
                queue.offer(nextToken);
                return newIndentToken("BEGIN", nextToken);
            }
            if (currentIndent < previousIndent) {
                previousIndent--;
                queue.offer(nextToken);
                return newIndentToken("END", nextToken);
            }
            counting = false;
        }

        if (nextToken.getType() == getTokenTypeMap().get("NL")) {
            currentIndent = 0;
            counting = true;
        }
        return nextToken;
    }

    private Token newIndentToken(String type, Token token) {
        var newToken = new CommonToken(getTokenTypeMap().get(type), type);
        newToken.setLine(token.getLine());
        newToken.setStartIndex(token.getStartIndex() - 1);
        newToken.setStopIndex(token.getStartIndex() - 1);
        newToken.setCharPositionInLine(token.getCharPositionInLine());
        return newToken;
    }
}
