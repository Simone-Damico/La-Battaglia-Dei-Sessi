package all;

import all.people.Donna;
import all.people.Umano;
import all.people.Uomo;
import static all.Tipo.*;

/**
 * Classe con metodi di utilità specifici per la procreazione.
 */
public class Procreazione {

    /**
     * Imposta i punteggi di un {@link Uomo} e una {@link Donna} usando i parametri del {@link Master}.
     * @param d la donna
     * @param u l'uomo
     * @throws IllegalArgumentException se i tipi non sono corretti.
     */
    public static void setScores(Donna d, Uomo u) {
        int a = Master.getA();
        int b = Master.getB();
        int c = Master.getC();

        if (u.tipo == MOR && d.tipo == PRU) {
            u.setScore(a - b/2 - c);
            d.setScore(a - b/2 - c);
        }
        else if (u.tipo == MOR && d.tipo == SPR) {
            u.setScore(a - b/2);
            d.setScore(a - b/2);
        }
        else if (u.tipo == AVV && d.tipo == PRU) {
            u.setScore(0);
            d.setScore(0);
        }
        else if (u.tipo == AVV && d.tipo == SPR) {
            d.setScore(a - b);
            u.setScore(a);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Metodo per la procreazione.
     * @param d donna il cui tipo è usato per generare la figlia.
     * @param u uomo il cui tipo è usato per generare il figlio.
     */
    public static void doFigli(Donna d, Uomo u) {
        new Donna(d.tipo);
        new Uomo(u.tipo);
    }
}
