package lang.adder.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

class AdderEngineTest {

    private AdderEngine objectUnderTest;

    @BeforeEach
    void before() {
        objectUnderTest = new AdderEngine();
    }

    @Test
    void firstTest() throws ScriptException {
        // given
        var expr = "1 + 3 - 5";

        // when
        var result = objectUnderTest.eval(expr);

        // then
        Assertions.assertEquals(-1, result);
    }

    @Test
    void secondTest() throws ScriptException {
        // given
        var expr = "1+3-5-11+77";

        // when
        var result = objectUnderTest.eval(expr);

        // then
        Assertions.assertEquals(65, result);
    }

    @Test
    void thirdTest() throws ScriptException {
        // given
        var expr1 = "1+3*5-11/2";
        var expr2 = "(1+3*5-11)/2";
        var expr3 = "";

        // when
        var result1 = objectUnderTest.eval(expr1);
        var result2 = objectUnderTest.eval(expr2);
        var result3 = objectUnderTest.eval(expr3);

        // then
        Assertions.assertEquals(11, result1);
        Assertions.assertEquals(2, result2);
        Assertions.assertEquals(null, result3);
    }

    @Test
    void fourthTest() throws ScriptException {
        // given
        var reader = readResourceFile("/test4.adder");

        // when
        var result = objectUnderTest.eval(reader);

        // then
        var bindings = objectUnderTest.getBindings(ScriptContext.ENGINE_SCOPE);
        Assertions.assertEquals(9, result);
        Assertions.assertEquals(1, bindings.get("a"));
        Assertions.assertEquals(2, bindings.get("b"));
        Assertions.assertEquals(9, bindings.get("c"));
    }

    @Test
    void fifthTest() throws ScriptException {
        // given
        var reader = readResourceFile("/test5.adder");

        // when
        var context = new SimpleScriptContext();
        var globalVars = objectUnderTest.createBindings();
        globalVars.put("a", 5);
        globalVars.put("b", 10);
        globalVars.put("c", 3);
        context.setBindings(globalVars, ScriptContext.GLOBAL_SCOPE);
        var result = objectUnderTest.eval(reader, context);

        // then
        Assertions.assertEquals(45, result);
        Assertions.assertEquals(45, context.getAttribute("result"));
        Assertions.assertEquals(ScriptContext.ENGINE_SCOPE, context.getAttributesScope("result"));
    }

    @Test
    void sixthTest() throws ScriptException {
        // given
        var reader = readResourceFile("/test6.adder");

        // when
        objectUnderTest.eval(reader);

        // then
        var bindings = objectUnderTest.getBindings(ScriptContext.ENGINE_SCOPE);
        Assertions.assertEquals(2, bindings.get("c"));
        Assertions.assertEquals(6, bindings.get("d"));
        Assertions.assertEquals(0, bindings.get("e"));
        Assertions.assertEquals(4, bindings.get("i"));
    }

    @Test
    void compiledScriptTest() throws ScriptException {
        // given
        var reader = readResourceFile("/test5.adder");

        var context1 = new SimpleScriptContext();
        var globalVars1 = objectUnderTest.createBindings();
        globalVars1.putAll(Map.of("a", 5, "b", 10, "c", 3));
        context1.setBindings(globalVars1, ScriptContext.GLOBAL_SCOPE);

        var context2 = new SimpleScriptContext();
        var globalVars2 = objectUnderTest.createBindings();
        globalVars2.putAll(Map.of("a", 1, "b", 2, "c", 3));
        context2.setBindings(globalVars2, ScriptContext.GLOBAL_SCOPE);

        // when
        var compiledScript = objectUnderTest.compile(reader);
        var result1 = compiledScript.eval(context1);
        var result2 = compiledScript.eval(context2);

        // then
        Assertions.assertEquals(45, result1);
        Assertions.assertEquals(45, context1.getAttribute("result"));

        Assertions.assertEquals(9, result2);
        Assertions.assertEquals(9, context2.getAttribute("result"));
    }


    private static Reader readResourceFile(String path) {
        var stream = AdderEngineTest.class.getResourceAsStream(path);
        return new BufferedReader(new InputStreamReader(stream));
    }
}