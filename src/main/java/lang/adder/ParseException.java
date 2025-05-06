package lang.adder;

public class ParseException extends RuntimeException {
    private final int lineNumber;
    private final int columnNumber;

    public ParseException(String message, Throwable cause, int lineNumber, int columnNumber) {
        super(message, cause);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
