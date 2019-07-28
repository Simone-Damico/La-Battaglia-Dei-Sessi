package all;

import all.GUI.SimulationGUI;
import all.people.Donna;
import all.people.Umano;
import all.people.Uomo;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.groupingBy;

/**
 * Crea e gestisce la popolazione.
 */
public class Master {

    /**
     * Premio per il successo della generazione di figli. Di default a = 15.
     */
    private static int a;

    /**
     * Costo per crescere i figli. Di default b = 20.
     */
    private static int b;

    /**
     * Costo del corteggiamento. Di default c = 3.
     */
    private static int c;

    /**
     * Quantità iniziale di {@link Uomo} di tipo {@link Tipo#MOR}.
     */
    private static int popMor;

    /**
     * Quantità iniziale di {@link Uomo} di tipo {@link Tipo#AVV}.
     */
    private static int popAvv;

    /**
     * Quantità iniziale di {@link Donna} di tipo {@link Tipo#PRU}.
     */
    private static int popPru;

    /**
     * Quantità iniziale di {@link Donna} di tipo {@link Tipo#SPR}.
     */
    private static int popSpr;

    /**
     * Flag che indica se il metodo {@link #lifeTime()} è in esecuzione.
     */
    private static volatile boolean running = true;

    /**
     * Coda della popolazione globale contenente tutti gli {@link Umano} vivi.
     */
    public static final Queue<Umano> popolazione = new ConcurrentLinkedQueue<>();

    /**
     * Coda della popolazione globale contenente tutti gli {@link Umano} morti.
     */
    public static final Queue<Umano> cimitero = new ConcurrentLinkedQueue<>();

    /**
     * Costruttore che imposta i parametri scelti dall'utente tramite GUI.
     * @param parA Premio per il successo della generazione di figli.
     * @param parB Costo per crescere i figli.
     * @param parC Costo del corteggiamento.
     * @param mor Quantità iniziale di morigerati.
     * @param avv Quantità iniziale di avventurieri.
     * @param pru Quantità iniziale di prudenti.
     * @param spr Quantità iniziale di spregiudicate.
     */
    public Master(int parA, int parB, int parC, int mor, int avv, int pru, int spr) {
        popMor = mor;
        popAvv = avv;
        popPru = pru;
        popSpr = spr;
        a = parA;
        b = parB;
        c = parC;
    }

    /**
     * Metodo per calcolare il minore punteggio che un {@link Umano} possa avere in base ai parametri impostati.
     * @return il minimo punteggio possibile in base ad a, b, c.
     */
    public static int getMin() {
        int s1 = a - b/2 - c;
        int s2 = a - b/2;
        int s3 = 0;
        int s4 = a - b;
        int s5 = a;
        return Collections.min(Arrays.asList(s1, s2, s3, s4, s5));
    }

    /**
     * Metodo per calcolare il maggiore punteggio che un {@link Umano} possa avere in base ai parametri impostati.
     * @return il massimo punteggio possibile in base ad a, b, c.
     */
    public static int getMax() {
        int s1 = a - b/2 - c;
        int s2 = a - b/2;
        int s3 = 0;
        int s4 = a - b;
        int s5 = a;
        return Collections.max(Arrays.asList(s1, s2, s3, s4, s5));
    }

    /**
     * Avvia il Master, chiamando {@link #create()}, {@link #lifeTime()} e {@link SimulationGUI#count()}.
     * @param sGUI la finestra in cui viene mostrata questa simulazione.
     */
    public static void start(SimulationGUI sGUI) {
        create();
        running = true;
        lifeTime();
        sGUI.count();
    }

    /**
     * "Dio li fa..."
     * La genesi: il master crea le popolazioni iniziali, secondo i parametri impostati dall'utente.
     */
    private static void create() {
        for (int i = 0; i<popAvv; i++) {
            new Uomo(Tipo.AVV);
        }
        for (int i = 0; i<popMor; i++) {
            new Uomo(Tipo.MOR);
        }
        for (int i = 0; i<popPru; i++) {
            new Donna(Tipo.PRU);
        }
        for (int i = 0; i<popSpr; i++) {
            new Donna(Tipo.SPR);
        }
    }

    /**
     * "...e poi li accoppia"
     * Il Master pesca un {@link Uomo} e una {@link Donna} dalle rispettive code del {@link Bar}
     * e li fa procreare, assegnandogli i nuovi punteggi e modificandone la vita di conseguenza.
     * Se sopravvivono, li rimette nelle rispettive code.
     */
    private static void lifeTime(){
        Thread t = new Thread( () -> {
            while (running) {
                Uomo myUomo;
                do {
                    if (Bar.isEmptyUomini())
                        try {
                            sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    myUomo = Bar.extractUomo();
                } while (myUomo == null);

                Donna myDonna;
                do {
                    if (Bar.isEmptyDonne())
                        try {
                            sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    myDonna = Bar.extractDonna();
                } while (myDonna == null);

                Procreazione.doFigli(myDonna, myUomo);
                Procreazione.setScores(myDonna, myUomo);
                myDonna.setLife();
                myUomo.setLife();

                if (myUomo.isDead()) {
                    popolazione.remove(myUomo);
                    cimitero.add(myUomo);
                }
                else
                    Bar.insert(myUomo);
                if (myDonna.isDead()) {
                    popolazione.remove(myDonna);
                    cimitero.add(myDonna);
                }
                else
                    Bar.insert(myDonna);
            }
        });
        t.start();
    }

    /**
     * Imposta {@link #running} a false, terminando la simulazione.
     */
    public static void shutDown() {
        running = false;
        popolazione.clear();
        cimitero.clear();
        Bar.clear();
    }

    /**
     * Ritorna il valore di {@link #running}.
     * @return true se la simulazione è in corso.
     */
    public static boolean isRunning() {
        return running;
    }

    /**
     * Metodo per ottenere il valore del parametro {@link #a}.
     * @return il valore di {@link #a}.
     */
    public static int getA() {
        return a;
    }

    /**
     * Metodo per ottenere il valore del parametro {@link #b}.
     * @return il valore di {@link #b}.
     */
    public static int getB() {
        return b;
    }

    /**
     * Metodo per ottenere il valore del parametro {@link #c}.
     * @return il valore di {@link #c}.
     */
    public static int getC() {
        return c;
    }

    /**
     * Metodo per ottenere il valore del parametro {@link #popMor}.
     * @return il valore di {@link #popMor}.
     */
    public static int getPopMor() {
        return popMor;
    }

    /**
     * Metodo per ottenere il valore del parametro {@link #popAvv}.
     * @return il valore di {@link #popAvv}.
     */
    public static int getPopAvv() {
        return popAvv;
    }

    /**
     * Metodo per ottenere il valore del parametro {@link #popPru}.
     * @return il valore di {@link #popPru}.
     */
    public static int getPopPru() {
        return popPru;
    }

    /**
     * Metodo per ottenere il valore del parametro {@link #popSpr}.
     * @return il valore di {@link #popSpr}.
     */
    public static int getPopSpr() {
        return popSpr;
    }
}
