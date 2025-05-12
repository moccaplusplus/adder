package lang.adder;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;

public abstract class IndentHandlingLexer extends Lexer {
    private record Queued(Token token, boolean handled) {
    }

    private final Stack<Integer> brackets = new Stack<>();
    private final Queue<Queued> queue = new LinkedList<>();
    private boolean countTabs = true;
    private int indent;
    private int newIndent;

    public IndentHandlingLexer(CharStream input) {
        super(input);
    }

    public void reset() {
        queue.clear();
        brackets.clear();
        countTabs = true;
        indent = newIndent = 0;
    }

    @Override
    public Token nextToken() {
        var queued = queue.poll();
        if (queued != null && queued.handled) {
            return queued.token;
        }
        var token = queued == null ? super.nextToken() : queued.token;

        if (countTabs) {
            if (token.getType() == getTokenTypeMap().get("TAB")) {
                newIndent++;
                return token;
            }

            if (newIndent > indent) {
                queue.add(new Queued(token, false));
                indent++;
                return newTokenBefore(getTokenTypeMap().get("BEGIN"), token);
            }

            if (newIndent < indent) {
                queue.add(new Queued(token, false));
                indent--;
                return newTokenBefore(getTokenTypeMap().get("END"), token);
            }

            countTabs = false;
            newIndent = 0;
        }

        if (token.getType() == getTokenTypeMap().get("NL")) {
            if (!brackets.isEmpty()) {
                return token;
            }
            queue.add(new Queued(token, true));
            countTabs = true;
            return newTokenInPlace(getTokenTypeMap().get("SC"), token);
        }

        if (token.getType() == getTokenTypeMap().get("B_CURLY_L")) {
            brackets.push(getTokenTypeMap().get("B_CURLY_R"));
        } else if (token.getType() == getTokenTypeMap().get("B_SQUARE_L")) {
            brackets.push(getTokenTypeMap().get("B_SQUARE_R"));
        } else if (token.getType() == getTokenTypeMap().get("PAREN_L")) {
            brackets.push(getTokenTypeMap().get("PAREN_R"));
        } else if (token.getType() == getTokenTypeMap().get("B_CURLY_R")
                || token.getType() == getTokenTypeMap().get("B_SQUARE_R")
                || token.getType() == getTokenTypeMap().get("PAREN_R")) {
            if (!brackets.isEmpty() && Objects.equals(brackets.peek(), token.getType())) {
                brackets.pop();
            }
        }

        return token;
    }

    private CommonToken newTokenBefore(int type, Token token) {
        var newToken = newTokenInPlace(type, token);
        newToken.setStartIndex(token.getStartIndex() - 1);
        newToken.setStopIndex(token.getStartIndex() - 1);
        newToken.setCharPositionInLine(token.getCharPositionInLine() - 1);
        return newToken;
    }

    private CommonToken newTokenInPlace(int type, Token token) {
        var newToken = new CommonToken(token);
        newToken.setType(type);
        newToken.setText(getVocabulary().getDisplayName(type));
        newToken.setChannel(Token.DEFAULT_CHANNEL);
        return newToken;
    }
}
