import java.util.Arrays;
import java.util.List;


public class CharacterNames {
     public static final String CHIZURU = "CHIZURU MIZUHARA";
     public static final String ZEROTWO = "ZERO TWO";
     public static final String MIKU = "MIKU NAKANO";
     public static final String MAI = "MAI SAKURAJIMA";
     public static final String NINO = "NINO NAKANO";
     public static final String NAO = "NAO TOMORI";
     public static final String RAPHTALIA = "RAPHTALIA";
     public static final String RUKA = "RUKA SARASHINA";
     public static final String AKI = "AKI ADAGAKI";
     public static final String ASUNA = "ASUNA YUUKI";
     public static final String EMILIA = "EMILIA";
     public static final String CHIKA = "CHIKA FUJIWARA";

     public static final String JOTARO = "JOTARO KUJO";
     public static final String DIO = "DIO BRANDO";
     public static final String GOKU = "SON GOKU";
     public static final String ALLMIGHT = "ALL MIGHT";
     public static final String ZORO = "RORONOA ZORO";
     public static final String YUGI = "YAMI YUGI";
     public static final String HISOKA = "HISOKA MOROW";
     public static final String ITACHI = "ITACHI UCHIHA";
     public static final String GOJO = "GOJO SATORU";
     public static final String LELOUCH = "LELOUCH LAMPEROUGE";
     public static final String YUU = "YUU OTOSAKA";

     public List<String> getAllCharacterNames() {
          return Arrays.asList(
                  CHIZURU, ZEROTWO, MIKU, MAI,
                  NINO, NAO, RAPHTALIA, RUKA,
                  AKI, ASUNA, EMILIA, CHIKA);
     }

     public List<String> getAllNames() {
          return Arrays.asList(
                  CHIZURU, ZEROTWO, MIKU, MAI,
                  NINO, NAO, RAPHTALIA, RUKA,
                  AKI, ASUNA, EMILIA, CHIKA,
                  JOTARO, DIO, GOKU, ALLMIGHT,
                  ZORO, YUGI, HISOKA, ITACHI,
                  GOJO, LELOUCH, YUU);
     }
}
