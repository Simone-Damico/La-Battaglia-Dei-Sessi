package all;

import all.people.Donna;
import all.people.Umano;
import all.people.Uomo;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Luogo di incontro tra {@link Uomo} e {@link Donna}.
 */
public class Bar {

    /**
     * Coda delle donne {@link Donna} spregiudicate.
     */
    private static ConcurrentLinkedQueue<Donna> codaSpr = new ConcurrentLinkedQueue<>();

    /**
     * Coda delle donne {@link Donna} prudenti.
     */
    private static ConcurrentLinkedQueue<Donna> codaPru = new ConcurrentLinkedQueue<>();

    /**
     * Coda delgli uomini {@link Uomo} avventurieri.
     */
    private static ConcurrentLinkedQueue<Uomo> codaAvv = new ConcurrentLinkedQueue<>();

    /**
     * Coda delgli uomini {@link Uomo} morigerati.
     */
    private static ConcurrentLinkedQueue<Uomo> codaMor = new ConcurrentLinkedQueue<>();

    private static Random rand = new Random();

    /**
     * Determina se le code delle {@link Donna} sono vuote.
     * @return true se le code delle {@link Donna} sono vuote.
     */
    public static boolean isEmptyDonne() {
        return codaPru.isEmpty() && codaSpr.isEmpty();
    }

    /**
     * Determina se la coda degli uomini è vuota.
     * @return true se la coda degli uomini è vuota.
     */
    public static boolean isEmptyUomini() {
        return codaMor.isEmpty() && codaAvv.isEmpty();
    }

    /**
     * Inserisce un {@link Umano} nella coda appropriata.
     * @param u l'{@link Umano} da inserire nella coda.
     * @return true se l'{@link Umano} è stato inserito con successo.
     */
    public static boolean insert(Umano u) {
        switch (u.tipo) {
            case AVV:
                return codaAvv.add( (Uomo) u);
            case MOR:
                return codaMor.add( (Uomo) u);
            case PRU:
                return codaPru.add( (Donna) u);
            case SPR:
                return codaSpr.add( (Donna) u);
            default:
                return false;
        }

    }

    /**
     * Estrae la prima {@link Donna} da una dalle relative code del Bar.
     * Se una delle due code è vuota estrae dall'altra, altrimenti sceglie a caso,
     * con probabilità proporzionale alla dimensione delle code.
     * @return una {@link Donna}.
     */
    public static Donna extractDonna() {
        if (codaSpr.isEmpty())
            return codaPru.poll();
        if (codaPru.isEmpty())
            return codaSpr.poll();
        int r = rand.nextInt(codaPru.size()+codaSpr.size());
        if (r < codaSpr.size())
            return codaSpr.poll();
        else
            return codaPru.poll();
    }

    /**
     * Estrae il primo {@link Uomo} da una dalle relative code del Bar.
     * Se una delle due code è vuota estrae dall'altra, altrimenti sceglie a caso.
     * con probabilità proporzionale alla dimensione delle code.
     * @return un {@link Uomo}.
     */
    public static Uomo extractUomo() {
        if (codaAvv.isEmpty())
            return codaMor.poll();
        if (codaMor.isEmpty())
            return codaAvv.poll();
        int r = rand.nextInt(codaMor.size()+codaAvv.size());
        if (r < codaAvv.size())
            return codaAvv.poll();
        else
            return codaMor.poll();
    }

    public static void clear() {
        codaMor.clear();
        codaAvv.clear();
        codaPru.clear();
        codaSpr.clear();
    }
}