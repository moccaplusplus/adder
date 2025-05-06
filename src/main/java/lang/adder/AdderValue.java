package lang.adder;

public class AdderValue {
    private final AdderType type;
    private Object value;

    public AdderValue(AdderType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Object value() {
        return value;
    }

    public void value(Object value) {
        this.value = value;
    }
}
