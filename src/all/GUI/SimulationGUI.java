package all.GUI;

import all.Master;
import all.Tipo;
import all.Utils;
import all.people.Umano;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;

import static all.Tipo.*;
import static java.lang.System.out;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Controller della finestra che mostra i risultati della simulazione.
 */
public class SimulationGUI {

    private BorderPane layout;
    private Thread thread;
    private int counter = 1;
    private XYChart.Series<Number, Number> morSeries;
    private XYChart.Series<Number, Number> avvSeries;
    private XYChart.Series<Number, Number> pruSeries;
    private XYChart.Series<Number, Number> sprSeries;
    private Timeline animation;
    private PieChart maleChart;
    private PieChart femaleChart;
    private Label mor = new Label();
    private Label avv = new Label();
    private Label pru = new Label();
    private Label spr = new Label();

    /**
     * Genera la finestra e avvia l'animazione.
     * @param layout il BorderPane in cui viene inserita la finestra delle impostazioni.
     */
    public SimulationGUI(BorderPane layout) {
        this.layout = layout;
        play();

        // crea l'animazione
        animation = new Timeline();
        animation.getKeyFrames()
                .add(new KeyFrame(Duration.millis(1000), (ActionEvent actionEvent) -> count() ));
        animation.setCycleCount(28);

        //crea i grafici e li aggiunge alla finestra
        VBox vBox = new VBox(20);
        VBox vBoxM = new VBox();
        vBoxM.setAlignment(Pos.CENTER);
        VBox vBoxF = new VBox();
        vBoxF.setAlignment(Pos.CENTER);
        vBox.getChildren().add(createLineChart());
        vBoxM.getChildren().add(createMalePieChart());
        vBoxM.getChildren().add(mor);
        vBoxM.getChildren().add(avv);
        vBoxF.getChildren().add(createFemalePieChart());
        vBoxF.getChildren().add(pru);
        vBoxF.getChildren().add(spr);
        HBox hBox = new HBox();
        hBox.getChildren().add(vBoxM);
        hBox.getChildren().add(vBoxF);
        vBox.getChildren().add(hBox);
        String s = "Secondo gli studi di Dawkins con i parametri di default si raggiunge la stabilità" +
                "\ncon il 62% dei Morigerati e l'83% delle Prudenti.";
        Text conclusioni = new Text(s);
        conclusioni.setTextAlignment(TextAlignment.CENTER);
        vBox.getChildren().add(conclusioni);
        Button back = new Button("< Indietro");
        back.setOnAction((e) -> handleBack());
        vBox.getChildren().add(back);
        vBox.setPadding(new Insets(30));
        hBox.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        layout.setCenter(vBox);

        //fa partire l'animazione
        animation.play();
    }

    /**
     * Avvia la simulazione chiamando {@link Master#start(SimulationGUI)} e la ferma dopo 30 secondi
     * invocando {@link Master#shutDown()}.
     */
    private void play() {
        thread = new Thread( () -> {
            Master.start(this);
            try {
                Thread.sleep(30_000);
                Master.shutDown();

            } catch (InterruptedException e) {
                System.out.println("Thread: simulazione interrotta");
            }
        });
        thread.start();
    }

    /**
     * Aggiorna i grafici coi dati campionati ad ogni secondo da {@link Master#popolazione}.
     */
    public void count() {
        Thread t = new Thread( () -> {
            try {
                Thread.sleep(1000);
                out.println(counter);
                counter++;
                Queue<Umano> q = Master.popolazione;
                out.println(q.size());

                /*
                 * Produce una mappa con per chiavi i 4 tipi di persone e per valore il numero di persone
                 * di quel tipo vive in questo momento.
                 */
                Map<Tipo, Long> map = q.stream().collect(groupingBy(Umano :: getTipo, counting()));
                out.println("Vivi: " + map);

                /* Aggiorna il lineChart */
                morSeries.getData().add(new XYChart.Data<Number, Number>(counter, map.get(MOR)));
                avvSeries.getData().add(new XYChart.Data<Number, Number>(counter, map.get(AVV)));
                pruSeries.getData().add(new XYChart.Data<Number, Number>(counter, map.get(PRU)));
                sprSeries.getData().add(new XYChart.Data<Number, Number>(counter, map.get(SPR)));


                /* Le percentuali tipo/sesso */
                Map<Tipo, Integer> percentuali = Utils.percentuali(map);
                out.println("Percentuali: " + percentuali);

                /* Aggiorna i pieChart */
                Platform.runLater( () -> {
                            maleChart.getData().setAll(FXCollections.observableArrayList(
                                    new PieChart.Data("Morigerati", percentuali.get(MOR)),
                                    new PieChart.Data("Avventurieri", percentuali.get(AVV))));
                            femaleChart.getData().setAll(FXCollections.observableArrayList(
                                    new PieChart.Data("Prudenti", percentuali.get(PRU)),
                                    new PieChart.Data("Spregiudicate", percentuali.get(SPR))));

                            mor.setText("Morigerati: " + percentuali.get(MOR).toString() + "%");
                            avv.setText("Avventurieri: " + percentuali.get(AVV).toString() + "%");
                            pru.setText("Prudenti: " + percentuali.get(PRU).toString() + "%");
                            spr.setText("Spregiudicate: " + percentuali.get(SPR).toString() + "%");
                        });

                /*
                 * Produce una mappa con per chiavi i 4 tipi di persone e per valore il numero di persone
                 * di quel tipo morte fino a questo momento.
                 */
                map = Master.cimitero.stream().collect(groupingBy(Umano :: getTipo, counting()));
                out.println("Morti: " + map);

                out.println("\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    /**
     * Crea il grafico a linee che mostra la crescita della popolazione.
     * @return il line chart.
     */
    public Parent createLineChart() {
        NumberAxis timeAxis = new NumberAxis(0, 30, 1);
        final NumberAxis populationAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(timeAxis, populationAxis);

        // inizializza il grafico
        chart.setCreateSymbols(false);
        chart.setAnimated(false);
        chart.setTitle("Simulazione");
        chart.getStylesheets().add(getClass().getResource("LineChartStyle.css").toExternalForm());
        timeAxis.setLabel("Tempo");
        timeAxis.setForceZeroInRange(false);
        populationAxis.setLabel("Popolazione");

        // inizializza le serie
        morSeries = new XYChart.Series<>();
        morSeries.setName("Morigerati");
        avvSeries = new XYChart.Series<>();
        avvSeries.setName("Avventurieri");
        pruSeries = new XYChart.Series<>();
        pruSeries.setName("Prudenti");
        sprSeries = new XYChart.Series<>();
        sprSeries.setName("Spregiudicate");

        // crea i dati iniziali
        morSeries.getData().add(new XYChart.Data<Number, Number>(0, Master.getPopMor()));
        avvSeries.getData().add(new XYChart.Data<Number, Number>(0, Master.getPopAvv()));
        pruSeries.getData().add(new XYChart.Data<Number, Number>(0, Master.getPopPru()));
        sprSeries.getData().add(new XYChart.Data<Number, Number>(0, Master.getPopSpr()));
        chart.getData().add(morSeries);
        chart.getData().add(avvSeries);
        chart.getData().add(pruSeries);
        chart.getData().add(sprSeries);

        return chart;
    }

    /**
     * Crea il grafico a torta che mostra le proporzioni tra {@link Tipo#MOR} e {@link Tipo#AVV}.
     * @return il pie chart degli {@link all.people.Uomo}.
     */
    public Parent createMalePieChart() {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Morigerati", Master.getPopMor()),
                        new PieChart.Data("Avventurieri", Master.getPopAvv()) );
        maleChart = new PieChart(pieChartData);
        maleChart.setTitle("Percentuali uomini");
        maleChart.setLegendVisible(false);
        maleChart.setLabelsVisible(false);
        maleChart.setAnimated(false);
        maleChart.getStylesheets().add(getClass().getResource("MaleChartStyle.css").toExternalForm());

        final Label caption = new Label("uffa");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");

        int nMor = Math.round( ( (float) Master.getPopMor() / (float) (Master.getPopMor() + Master.getPopAvv()) ) * 100);
        int nAvv = 100 - nMor;

        for (final PieChart.Data data : maleChart.getData()) {
            switch (data.getName()) {
                case "Morigerati":
                    mor.setText("Morigerati: " + nMor + "%");
                    break;
                case "Avventurieri":
                    avv.setText("Avventurieri: " + nAvv + "%");
                    break;
                default:
                    break;
            }
        }

        return maleChart;
    }

    /**
     * Crea il grafico a torta che mostra le proporzioni tra {@link Tipo#PRU} e {@link Tipo#SPR}.
     * @return il pie chart delle {@link all.people.Donna}.
     */
    public Parent createFemalePieChart() {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Prudenti", Master.getPopPru()),
                        new PieChart.Data("Spregiudicate", Master.getPopSpr()) );
        femaleChart = new PieChart(pieChartData);
        femaleChart.setTitle("Percentuali donne");
        femaleChart.setLegendVisible(false);
        femaleChart.setLabelsVisible(false);
        femaleChart.setAnimated(false);
        femaleChart.getStylesheets().add(getClass().getResource("FemaleChartStyle.css").toExternalForm());

        final Label caption = new Label("uffa");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");

        int nPru = Math.round( ( (float) Master.getPopPru() / (float) (Master.getPopPru() + Master.getPopSpr()) ) * 100);
        int nSpr = 100 - nPru;

        for (final PieChart.Data data : femaleChart.getData()) {
            switch (data.getName()) {
                case "Prudenti":
                    pru.setText("Prudenti: " + nPru + "%");
                    break;
                case "Spregiudicate":
                    spr.setText("Spregiudicate: " + nSpr + "%");
                    break;
                default:
                    break;
            }
        }

        return femaleChart;
    }

    /**
     * Chiama {@link #shutdown()} e {@link Master#shutDown()}, poi ritorna alla schermata delle impostazioni.
     */
    private void handleBack() {
        shutdown();
        Master.shutDown();
        new CheckSettings(layout);
    }

    /**
     * Interrompe il campionamento dei dati e l'animazione.
     */
    public void shutdown() {
        if (thread != null && animation != null) {
            thread.interrupt();
            animation.pause();
        }
    }

}
