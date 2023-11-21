package lang.adder.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.script.ScriptException;

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

        // when
        var result1 = objectUnderTest.eval(expr1);
        var result2 = objectUnderTest.eval(expr2);

        // then
        Assertions.assertEquals(11, result1);
        Assertions.assertEquals(2, result2);
    }
}