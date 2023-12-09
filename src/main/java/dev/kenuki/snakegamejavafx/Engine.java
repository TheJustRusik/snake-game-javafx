package dev.kenuki.snakegamejavafx;


import dev.kenuki.snakegamejavafx.util.*;
import java.util.ArrayList;
import java.util.Random;

public class Engine {
    private final Random random = new Random(System.currentTimeMillis());
    private final int START_LENGTH = 4;
    private final int APPLES_AT_TIME = 2;
    private final Entity[][] field;
    private final ArrayList<Vec2I> snakePieces;
    public Direction direction = Direction.RIGHT;
    public boolean alive = true;
    private Vec2I headPos = new Vec2I(START_LENGTH - 1, 0);
    public Engine(int width, int height) throws Exception {
        snakePieces = new ArrayList<>();
        field = new Entity[width][height];
        generateField(width, height);
    }
    private void generateField(int w, int h) throws Exception {
        if (START_LENGTH >= w)
            throw new Exception("Snake length is >= fields width");
        for (int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                field[y][x] = Entity.AIR;
            }
        }
        for (int i = 0; i < START_LENGTH; i++){
            field[0][i] = Entity.BODY;

        }
        for (int i = START_LENGTH - 1; i >= 0; i--){
            snakePieces.add(new Vec2I(i, 0));
        }
        setApple(APPLES_AT_TIME);
    }
    private Vec2I makeMove(Vec2I old, Direction dir){
        switch (dir){
            case UP -> {
                return old.y - 1 == -1 ? new Vec2I(old.x, field.length -1) : new Vec2I(old.x, old.y - 1);
            }
            case DOWN -> {
                return old.y + 1 == field.length ? new Vec2I(old.x, 0) : new Vec2I(old.x, old.y + 1);
            }
            case RIGHT -> {
                return old.x + 1 == field[0].length ? new Vec2I(0, old.y) : new Vec2I(old.x + 1, old.y);
            }
            case LEFT -> {
                return old.x - 1 == -1 ? new Vec2I(field[0].length - 1, old.y) : new Vec2I(old.x - 1, old.y);
            }
        }
        return new Vec2I();
    }
    public void makeIteration(){
        Vec2I futureHead = makeMove(headPos, direction);
        Vec2I oldPos = new Vec2I(snakePieces.get(snakePieces.size() - 1));
        switch (field[futureHead.y][futureHead.x]){
            case BODY, WALL -> alive = false;
            case APPLE -> {
                snakePieces.add(new Vec2I(oldPos));
                setApple(1);
            }
        }
        for (int i = snakePieces.size() - 1; i > 0; i--){
            snakePieces.set(i, snakePieces.get(i - 1));
        }
        snakePieces.set(0, makeMove(headPos, direction));
        headPos = snakePieces.get(0);
        field[headPos.y][headPos.x] = Entity.BODY;
        field[oldPos.y][oldPos.x] = Entity.AIR;
    }
    private void setApple(int num){
        if (num > 0){
            int xRand, yRand;
            do {
                xRand = random.nextInt(field[0].length);
                yRand = random.nextInt(field.length);
            }while (field[yRand][xRand] != Entity.AIR);
            field[yRand][xRand] = Entity.APPLE;
            setApple(num - 1);
        }
    }
    public Entity[][] getField(){
        return field;
    }
}