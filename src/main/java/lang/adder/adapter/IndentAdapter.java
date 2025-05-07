package lang.adder.adapter;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.Supplier;

public class IndentAdapter implements Supplier<Token> {
    private final Supplier<Token> tokenSupplier;
    private final Map<String, Integer> tokenTypeMap;
    private final Vocabulary vocabulary;
    private final Queue<Token> queue = new LinkedList<>();
    private boolean isCounting = true;
    private int indent;

    public IndentAdapter(
            Map<String, Integer> tokenTypeMap,
            Vocabulary vocabulary,
            Supplier<Token> tokenSupplier) {
        this.tokenTypeMap = tokenTypeMap;
        this.vocabulary = vocabulary;
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public Token get() {
        var token = queue.isEmpty() ? tokenSupplier.get() : queue.poll();
        if (isCounting) {
            int newIndent = 0;
            while (token.getType() == tokenTypeMap.get("TAB")) {
                newIndent++;
//                queue.add(token);
                token = tokenSupplier.get();
            }
            int d = newIndent - indent;
            if (d != 0) {
                int type = tokenTypeMap.get(d > 0 ? "BEGIN" : "END");
                for (int i = Math.abs(d); i > 0; i--) {
                    queue.offer(newTokenBefore(type, token));
                }
                queue.add(token);
                indent = newIndent;
                token = queue.remove();
            }
            isCounting = false;
        }
        if (token.getType() == tokenTypeMap.get("NL")) {
//            queue.add(token);
            isCounting = true;
            token = newTokenBefore(tokenTypeMap.get("SC"), token);
        }
        return token;
    }

    private Token newTokenBefore(int type, Token tokenAfter) {
        var newToken = new CommonToken(type, vocabulary.getDisplayName(type));
        newToken.setLine(tokenAfter.getLine());
        newToken.setStartIndex(tokenAfter.getStartIndex() - 1);
        newToken.setStopIndex(tokenAfter.getStartIndex() - 1);
        newToken.setCharPositionInLine(tokenAfter.getCharPositionInLine());
        return newToken;
    }
}
