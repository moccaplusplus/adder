package lang.adder.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngineManager;

class AdderEngineFactoryTest {
    @Test
    void findEngineTest() {
        var manager = new ScriptEngineManager();

        var engineByExt = manager.getEngineByExtension(".adder");
        var engineByName = manager.getEngineByName("Adder");

        Assertions.assertNotNull(engineByExt);
        Assertions.assertNotNull(engineByName);
    }
}