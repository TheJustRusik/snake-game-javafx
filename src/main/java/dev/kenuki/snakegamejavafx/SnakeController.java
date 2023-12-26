package dev.kenuki.snakegamejavafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.*;
import javafx.util.Duration;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import dev.kenuki.snakegamejavafx.util.*;

public class SnakeController {
    @FXML
    private Pane battleField;
    @FXML
    private Button button;
    @FXML
    private Slider difSlider;
    @FXML
    private Label difText;
    @FXML
    private Label scoreText;
    @FXML
    private ChoiceBox<String> choiceSize;
    private int fieldSize = 10;
    private double entitySize = 50;
    private int gameFps = 170;
    private Engine engine;
    private Timeline timeline;
    private void drawFrame(){
        battleField.getChildren().clear();
        Entity[][] field = engine.getField();
        for(int y = 0; y < field.length; y++){
            for (int x = 0; x < field[y].length; x++){
                switch (field[y][x]){
                    case AIR -> {
                        //Nothing...
                    }
                    case APPLE -> {
                        Rectangle tmp = new Rectangle(entitySize * x,entitySize * y,entitySize,entitySize);
                        tmp.setFill(Color.RED);
                        battleField.getChildren().add(tmp);
                    }case BODY -> {
                        Rectangle tmp = new Rectangle(entitySize * x,entitySize * y,entitySize,entitySize);
                        tmp.setFill(Color.WHITE);
                        battleField.getChildren().add(tmp);
                    }case WALL -> {
                        Rectangle tmp = new Rectangle(entitySize * x,entitySize * y,entitySize,entitySize);
                        tmp.setFill(Color.rgb(32, 33, 36));
                        battleField.getChildren().add(tmp);
                    }
                }
            }
        }
    }

    public void initialize() throws Exception {
        ObservableList<String> choices = FXCollections.observableArrayList(
                "10x10",
                "20x20",
                "50x50"
        );
        choiceSize.setItems(choices);
        choiceSize.setValue("10x10");
        difSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                difSliderChanged();
            }
        });

    }
    @FXML
    private void difSliderChanged(){
        gameFps = (int) difSlider.getValue();
        if (gameFps > 250){
            difText.setText("Easy (" + (int) difSlider.getValue() + ")");
            difText.setTextFill(Color.LIME);
        }else if (gameFps > 125){
            difText.setText("Normal (" + (int) difSlider.getValue() + ")");
            difText.setTextFill(Color.YELLOW);
        }else{
            difText.setText("Hard (" + (int) difSlider.getValue() + ")");
            difText.setTextFill(Color.RED);
        }
    }
    @FXML
    public void buttonPressed() throws Exception {
        if (button.getText().equals("Start") || button.getText().equals("Restart")){
            switch (choiceSize.getValue()){
                case "10x10" -> {fieldSize = 10; entitySize = 50;}
                case "20x20" -> {fieldSize = 20; entitySize = 25;}
                case "50x50" -> {fieldSize = 50; entitySize = 10;}
            }
            engine = new Engine(fieldSize,fieldSize);
            startGame();
            difSlider.setDisable(true);
            choiceSize.setDisable(true);
            button.setText("Stop");
        }else if(button.getText().equals("Resume")){
            startGame();
            difSlider.setDisable(true);
            choiceSize.setDisable(true);
            button.setText("Stop");
        }else if(button.getText().equals("Stop")){
            stopGame();
            button.setText("Resume");
            difSlider.setDisable(true);
            choiceSize.setDisable(true);
        }

    }
    @FXML
    public void keyHandle(KeyEvent event){
        if(engine == null){
            return;
        }
        if(!engine.isAlive()){
            return;
        }
        switch (event.getCode()) {
            case W -> {
                engine.direction = Direction.UP;
            }
            case S -> {
                engine.direction = Direction.DOWN;
            }
            case D -> {
                engine.direction = Direction.RIGHT;
            }
            case A -> {
                engine.direction = Direction.LEFT;
            }
        }
    }
    public void startGame(){
        Duration duration = new Duration(gameFps);
        timeline = new Timeline(new KeyFrame(duration, event -> {
            engine.makeIteration();
            scoreText.setText("Score: " + engine.getScore());
            if (!engine.isAlive()){
                stopGame();
                try {
                    engine = new Engine(fieldSize,fieldSize);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                
                engine.direction = Direction.RIGHT;
                button.setText("Restart");
                difSlider.setDisable(false);
                choiceSize.setDisable(false);
                return;
            }
            drawFrame();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    public void stopGame(){
        timeline.stop();
    }
}