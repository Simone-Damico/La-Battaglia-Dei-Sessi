package all.people;

import all.Master;
import all.Tipo;

/**
 * Un generico essere umano.
 */
public abstract class Umano {

    /**
     * Il punteggio della persona, calcolato in base alla tabella MAPS.
     */
    private int score = 0;

    /**
     * Aspettativa di vita di una persona, varia in base allo {@link #score}.
     * ottenuto ad ogni accoppiamento.
     */
    public int life = 15;


    /**
     * Il {@link Tipo} di questo essere umano.
     */
    public Tipo tipo;

    /**
     * Restituisce il {@link Tipo} di questo essere umano.
     * @return il tipo di questo essere umano.
     */
    public Tipo getTipo() {return tipo; }

    /**
     * Determina se questo essere umano è morto ({@link #life} <= 0).
     * @return true se una persona è viva, false altrimenti.
     */
    public boolean isDead() {
        return life <= 0;
    }

    /**
     * Aggiorna la vita di questo essere umano in base al suo {@link #score}.
     * Il modello prevede che il {@link #life} di una persona venga decrementato
     * a seconda dello {@link #score} ottenuto.
     */
    public void setLife() {
        if (score == Master.getMin())
            life-= 13;                     //assumiamo che vale sempre a<b
        else if (score == Master.getMax())
            life-= 1;
        else if (score > Master.getMin() && score <= 0)
            life-= 5;
        else
            life-= 3;
    }

    /**
     * Imposta lo {@link #score} di questo essere umano al valore passato come parametro.
     * @param i il valore a cui settare lo {@link #score}.
     */
    public void setScore(int i){
        score = i;
    }

    /**
     * Ritorna lo {@link #score} di questo essere umano.
     * @return lo {@link #score} di questo essere umano.
     */
    public int getScore(){
        return score;
    }
}

