package it.playfellas.superapp.logic.master;

import java.util.HashMap;
import java.util.Map;

import it.playfellas.superapp.logic.master.game1.Master1Color;
import it.playfellas.superapp.logic.master.game1.Master1Direction;
import it.playfellas.superapp.logic.master.game1.Master1Shape;
import it.playfellas.superapp.logic.master.game23.Master2Alternate;
import it.playfellas.superapp.logic.master.game23.Master2Decreasing;
import it.playfellas.superapp.logic.master.game23.Master2Growing;
import it.playfellas.superapp.logic.master.game23.Master2Random;
import it.playfellas.superapp.logic.master.game23.Master3Controller;

/**
 * Created by affo on 13/10/15.
 */
public class GameModTranslator {
    private static final Map<String, String> translations;
    public static final String UNKNOWN = "UNKNOWN";

    static {
        translations = new HashMap<>();
        translations.put(Master1Color.class.getCanonicalName(), "Gioco 1 - Colore");
        translations.put(Master1Direction.class.getCanonicalName(), "Gioco 1 - Direzione");
        translations.put(Master1Shape.class.getCanonicalName(), "Gioco 1 - Forma");
        translations.put(Master2Growing.class.getCanonicalName(), "Gioco 2 - Crescente");
        translations.put(Master2Decreasing.class.getCanonicalName(), "Gioco 2 - Decrescente");
        translations.put(Master2Alternate.class.getCanonicalName(), "Gioco 2 - Alternato");
        translations.put(Master2Random.class.getCanonicalName(), "Gioco 2 - Random");
        translations.put(Master3Controller.class.getCanonicalName(), "Gioco 3");
    }

    public static String translate(Class<? extends MasterController> masterClass) {
        String t = translations.get(masterClass.getCanonicalName());
        if (t == null) {
            return UNKNOWN;
        }
        return t;
    }
}
