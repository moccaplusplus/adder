package lang.adder;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

public abstract class LexerExt extends Lexer {
    private final java.util.Queue<Token> queue = new java.util.LinkedList<>();
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
                return newIndentToken(getTokenTypeMap().get("BEGIN"), nextToken);
            }
            if (currentIndent < previousIndent) {
                previousIndent--;
                queue.offer(nextToken);
                return newIndentToken(getTokenTypeMap().get("END"), nextToken);
            }
            counting = false;
        }

        if (nextToken.getType() == getTokenTypeMap().get("NL")) {
            currentIndent = 0;
            counting = true;
        }
        return nextToken;
    }

    private Token newIndentToken(int type, Token token) {
        var newToken = new CommonToken(type, "");
        newToken.setStartIndex(token.getStartIndex());
        newToken.setStopIndex(token.getStopIndex());
        newToken.setLine(token.getLine());
        newToken.setCharPositionInLine(token.getCharPositionInLine() + token.getText().length());
        return newToken;
    }
}
