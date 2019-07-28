package all.people;

import all.Bar;
import all.Master;
import all.Tipo;

import java.util.Queue;

import static java.lang.Thread.sleep;

/**
 * Un uomo, può avere tipo {@link Tipo#MOR} o {@link Tipo#AVV}.
 */
public class Uomo extends Umano {

    /**
     * Crea un Uomo del {@link Tipo} indicato, lo inserisce nella popolazione globale e nella relativa coda.
     * @param t il tipo di quest'uomo.
     * @throws IllegalArgumentException se il {@link Tipo} non è un tipo maschile.
     */
    public Uomo(Tipo t) {
        if (! t.isMaschio())
            throw new IllegalArgumentException("Un uomo non può essere donna!");
        tipo = t;
        Master.popolazione.add(this);
        Bar.insert(this);
    }

}