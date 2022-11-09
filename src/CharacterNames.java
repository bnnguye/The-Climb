public enum CharacterNames {
     CHIZURU("CHIZURU"), ZEROTWO("ZEROTWO"), MIKU("MIKU"), MAI("MAI"), NINO("NINO"), FUTABA("FUTABA"),
     RAPHTALIA("RAPHTALIA"), RUKA("RUKA"), AKI("AKI"), ASUNA("ASUNA"), EMILIA("EMILIA"), CHIKA("CHIKA");

     private final String name;

     CharacterNames(final String name) {
          this.name = name;
     }

     @Override
     public String toString() {
          return name;
     }
}
