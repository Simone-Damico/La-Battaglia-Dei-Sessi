package all;

import javafx.scene.control.Alert;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe che raccoglie metodi di utilità.
 */
public class Utils {

    /**
     * Metodo di utilità per mostrare popup.
     * @param type il tipo di messaggio.
     * @param infoMessage il testo del messaggio.
     * @param titleBar il titolo del messaggio.
     * @param headerMessage l'header del messaggio.
     */
    public static void popupFx(Alert.AlertType type, String infoMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(type);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }

    /**
     * Produce una mappa con per chiavi i quattro {@link Tipo} di persone e per valore la percentuale di quel tipo
     * rispetto a tutte le persone del sesso corrispondente.
     * @param map una mappa le cui chiavi sono i tipi di persone e i valori le relative quantità nella popolazione.
     * @return le percentuali dei tipi di persone rispetto al totale delle persone del sesso corrispondente.
     */
    public static Map<Tipo, Integer> percentuali(Map<Tipo, Long> map) {
        Map<Tipo, Integer> percentuali = new HashMap<>();
        long m=0;
        long f=0;
        for (Tipo tipo : map.keySet()) {
            if (tipo.isMaschio())
                m += map.get(tipo);
            else
                f += map.get(tipo);
        }
        double[] sessi = new double[2];
        sessi[0] = m;
        sessi[1] = f;
        map.forEach((t, q) -> {
            if (t.isMaschio())
                percentuali.put(t, (int) Math.round((q/sessi[0])*100));
            else
                percentuali.put(t, (int) Math.round((q/sessi[1])*100));
        });
        return percentuali;
    }
}
