package lang.adder.engine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.List;

public class AdderEngineFactory implements ScriptEngineFactory {
    @Override
    public String getEngineName() {
        return "AdderEngine";
    }

    @Override
    public String getEngineVersion() {
        return "1.0.beta";
    }

    @Override
    public List<String> getExtensions() {
        return List.of(".adder", ".addr", ".add");
    }

    @Override
    public List<String> getMimeTypes() {
        return List.of("application/vnd.lang.adder");
    }

    @Override
    public List<String> getNames() {
        return List.of("Adder", "adder", "AdderScript");
    }

    @Override
    public String getLanguageName() {
        return "Adder";
    }

    @Override
    public String getLanguageVersion() {
        return "1.0.beta";
    }

    @Override
    public Object getParameter(String key) {
        return null;
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProgram(String... statements) {
        return String.join("\n", statements);
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return new AdderEngine(this);
    }
}
