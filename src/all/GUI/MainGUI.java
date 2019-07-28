package all.GUI;

/**
 * Il controller della finestra principale dell'applicazione.
 */

import all.Master;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import static java.lang.System.out;

public class MainGUI extends Application {

    private Stage primaryStage;
    private BorderPane layout;
    private CheckSettings checkSettings;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Avvia l'applicazione.
     * @param primaryStage lo stage principale.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("La battaglia dei sessi");
        out.println("primary stage: " + primaryStage);
        this.primaryStage.getIcons().add(new Image(getClass().getResource("icona.png").toString()));
        primaryStage.setHeight(650);
        initRootLayout();
    }

    /**
     * Metodo che crea la finestra dei settaggi, imposta la scena e mostra lo stage.
     */
    @FXML
    private void initRootLayout() {
        layout = new BorderPane();
        checkSettings = new CheckSettings(layout);
        out.println("rootLayout: " + layout + ".");
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Ferma la simulazione e chiude la finestra.
     */
    @Override
    public void stop(){
        Master.shutDown();
        if (checkSettings != null)
            checkSettings.shutdown();
        System.out.println("Simulation is closing");
    }
}
