package dev.kenuki.snakegamejavafx.util;

public class Vec2I {
    public int x;
    public int y;
    public Vec2I(){
        x = 0;
        y = 0;
    }
    public Vec2I(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vec2I(Vec2I vec){
        x = vec.x;
        y = vec.y;
    }
    @Override
    public String toString(){
        return String.format("Vec2I@%s{x=%d,y=%d}", Integer.toHexString(System.identityHashCode(this)), x, y);
    }
}