package Client.Model.Classes;

import java.util.Objects;

public class Coordinates {
    private int x; //Максимальное значение поля 12
    private float y;

    public Coordinates(int x, float y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates() {}

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
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates coordinates = (Coordinates) o;
        return x == coordinates.x && y == coordinates.y;
    }

    @Override
    public String toString() {
        return String.format(
                "Coordinates(x=%d, y=%.2f)",
                x,
                y
        );
    }
}