package lang.adder;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Map;

public record Scope(Scope parent, Map<String, AdderValue> bindings) {
    public void declare(TerminalNode id, AdderValue value) {
        var name = id.getText();
        if (bindings.containsKey(name)) {
            throw new ExecutionException(
                    "Identifier " + name + " already declared in current scope",
                    id.getSymbol());
        }
        bindings.put(name, value);
    }

    public void write(TerminalNode id, AdderValue value) {
    }

    public AdderValue read(TerminalNode identifier) {
        return null;
    }
}