package dev.kenuki.snakegamejavafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
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
    private int gameFps = 170;
    private Engine engine;
    private Timeline timeline;
    private void drawFrame(){
        battleField.getChildren().clear();
        Entity[][] field = engine.getField();
        for(int y = 0; y < field.length; y++){
            for (int x = 0; x < field[y].length; x++){
                switch (field[y][x]){
                    case APPLE -> {
                        Rectangle tmp = new Rectangle(50,50,Color.RED);
                        tmp.setLayoutX(50 * x);
                        tmp.setLayoutY(50 * y);
                        battleField.getChildren().add(tmp);
                    }case BODY -> {
                        Rectangle tmp = new Rectangle(50,50,Color.WHITE);
                        tmp.setLayoutX(50 * x);
                        tmp.setLayoutY(50 * y);
                        battleField.getChildren().add(tmp);
                    }
                }
            }
        }
    }

    public void initialize() throws Exception {
        engine = new Engine(10,10);

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
    public void buttonPressed(){
        if (button.getText().equals("Start") || button.getText().equals("Restart")){
            startGame();
            difSlider.setVisible(false);
            button.setText("Stop");
        }else if(button.getText().equals("Resume")){
            startGame();
            difSlider.setVisible(false);
            button.setText("Stop");
        }else if(button.getText().equals("Stop")){
            stopGame();
            button.setText("Resume");
            difSlider.setVisible(false);
        }

    }
    @FXML
    public void keyHandle(KeyEvent event){
        if(engine == null){
            return;
        }
        if(!engine.alive){
            return;
        }
        switch (event.getCode()) {
            case W -> {
                if (engine.direction != Direction.DOWN)
                    engine.direction = Direction.UP;
            }
            case S -> {
                if (engine.direction != Direction.UP)
                    engine.direction = Direction.DOWN;
            }
            case D -> {
                if (engine.direction != Direction.LEFT)
                    engine.direction = Direction.RIGHT;
            }
            case A -> {
                if (engine.direction != Direction.RIGHT)
                    engine.direction = Direction.LEFT;
            }
        }
    }
    public void startGame(){
        Duration duration = new Duration(gameFps);
        timeline = new Timeline(new KeyFrame(duration, event -> {
            engine.makeIteration();
            if (!engine.alive){
                stopGame();
                try {
                    engine = new Engine(10,10);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                drawFrame();
                engine.direction = Direction.RIGHT;
                button.setText("Restart");
                difSlider.setVisible(true);
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