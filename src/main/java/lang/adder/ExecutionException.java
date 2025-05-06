package lang.adder;

import org.antlr.v4.runtime.Token;

public class ExecutionException extends RuntimeException {
    private final Token token;

    public ExecutionException(String message, Token token) {
        super(message);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
