public enum Symbol{
    // It should be fine to change the Strings, as long as the length stays the same.
    // Please don't change EMPTY for your own sake.
    A("A0"),
    B("BD"),
    C("1C"),
    D("55"),
    E("E9"),
    F("FA"),
    G("42"),
    H("88"),
    EMPTY("--");

    private final String code;
    Symbol(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
