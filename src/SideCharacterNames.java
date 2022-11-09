public enum SideCharacterNames {
    ZORO("ZORO"), GOJO("GOJO"), ALLMIGHT("ALLMIGHT"), LELOUCH("LELOUCH"), HISOKA("HISOKA"), JOTARO("JOTARO"),
    DIO("DIO"),ITACHI("ITACHI"), YUGI("YUGI"), PUCK("PUCK"), YUU("YUU"), SENKUU("SENKUU");

    private final String name;

    SideCharacterNames(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}