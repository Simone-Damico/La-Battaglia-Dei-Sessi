package all.people;

import all.Bar;
import all.Master;
import all.Tipo;

import java.util.Queue;

/**
 * Una donna, può avere tipo {@link Tipo#PRU} o {@link Tipo#SPR}.
 */
public class Donna extends Umano {

    /**
     * Crea una Donna del {@link Tipo} indicato, la inserisce nella popolazione globale e nella relativa coda.
     * @param t il tipo di questa donna.
     * @throws IllegalArgumentException se il {@link Tipo} non è un tipo femminile.
     */
    public Donna(Tipo t) {
        if (t.isMaschio())
            throw new IllegalArgumentException("Una donna non può essere uomo!");
        tipo = t;
        Master.popolazione.add(this);
        Bar.insert(this);
    }

}
