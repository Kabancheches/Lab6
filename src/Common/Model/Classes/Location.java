package Common.Model.Classes;

import java.io.Serializable;
import java.util.Objects;

public class Location implements Serializable {
    private static final long serialVersionUID = 1L;
    private float x;
    private Long y; //Поле не может быть null
    private int z;

    public Location(float x, Long y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location() {}

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x && Objects.equals(y, location.y) && z == location.z;
    }

    @Override
    public String toString() {
        return String.format(
                "Location(%f, %d, %d)",
                x,
                y,
                z
        );
    }
}