package all;

/**
 * I tipi in cui si divide la popolazione.
 */
public enum Tipo {
    MOR, AVV, PRU, SPR;

    /**
     * Determina se questo tipo è maschile.
     * @return true se questo è un tipo maschile, false altrimenti.
     */
    public boolean isMaschio() {
        return this == MOR || this == AVV;
    }
}
