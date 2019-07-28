package all.GUI;

import all.Master;
import all.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import static java.lang.System.out;

/**
 * Controller della finestra che permette di inserire i parametri della simulazione.
 * Genera il {@link Master} in base ai parametri scelti.
 */
public class CheckSettings {

    private BorderPane layout;
    private SimulationGUI simulationGUI;

    @FXML
    private TextField parA;
    @FXML
    private TextField parB;
    @FXML
    private TextField parC;

    @FXML
    private TextField popMor;
    @FXML
    private TextField popAvv;
    @FXML
    private TextField popPru;
    @FXML
    private TextField popSpr;

    /**
     * Genera la finestra delle impostazioni.
     * @param layout il BorderPane in cui si andrà ad inserire la finestra delle impostazioni.
     */
    public CheckSettings(BorderPane layout) {
        this.layout = layout;
        loadPanel(layout);
    }

    /**
     * Carica la finestra delle impostazioni e la inserisce nel BorderPane ricevuto in input.
     * @param layout il BorderPane in cui viene inserita la finestra delle impostazioni.
     */
    @FXML
    private void loadPanel(BorderPane layout) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("settings.fxml"));
            loader.setController(this);
            layout.setCenter(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        out.println("LOAD");
    }

    /**
     * Metodo che controlla i parametri inseriti e genera il {@link Master} di conseguenza.
     * Se il controllo fallisce mostra un popup all'utente.
     */
    @FXML
    private void handleStart() {
        try {
            int a = Integer.parseInt(parA.getText());
            int b = Integer.parseInt(parB.getText());
            int c = Integer.parseInt(parC.getText());

            int mor = Integer.parseInt(popMor.getText());
            int avv = Integer.parseInt(popAvv.getText());
            int pru = Integer.parseInt(popPru.getText());
            int spr = Integer.parseInt(popSpr.getText());

            if (a<0 || b<0 || c<0 || mor<0 || avv<0 || pru<0 || spr<0) {
                Utils.popupFx(Alert.AlertType.ERROR,
                        "I valori devo essere tutti non negativi.",
                        "Parametri non validi", "Correggere il campo errato");
            }

            out.println("Parametri: " + a + " " + b + " " + c);
            out.println("Popolazione: " + mor + " " + avv + " " + pru + " " + spr);

            new Master(a, b, c, mor, avv, pru, spr);
            simulationGUI = new SimulationGUI(layout);
        }
        catch (NumberFormatException ex) {
            Utils.popupFx(Alert.AlertType.ERROR,
                    "I valori devono essere numeri interi.",
                    "Parametri non validi", "Correggere il campo errato");
        }
    }

    /**
     * Chiama {@link SimulationGUI#shutdown()}
     */
    public void shutdown() {
        if (simulationGUI != null)
            simulationGUI.shutdown();
    }
}
