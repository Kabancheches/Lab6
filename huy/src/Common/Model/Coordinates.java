package Common.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Координаты продукта.
 */
public class Coordinates implements Serializable, Comparable<Coordinates> {
    private static final long serialVersionUID = 1L;
    
    private int x; // Максимальное значение поля 12
    private float y;

    public Coordinates() {}

    public Coordinates(int x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public int compareTo(Coordinates other) {
        int xCompare = Integer.compare(this.x, other.x);
        if (xCompare != 0) return xCompare;
        return Float.compare(this.y, other.y);
    }

    @Override
    public String toString() {
        return String.format("Coordinates(x=%d, y=%.2f)", x, y);
    }
}
