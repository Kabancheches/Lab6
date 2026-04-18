package Common.Model;

import java.io.Serializable;

/**
 * Местоположение (для адреса организации).
 */
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private float x;
    private Long y; // Поле не может быть null
    private int z;

    public Location() {}

    public Location(float x, Long y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

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
    public String toString() {
        return String.format("Location(%f, %d, %d)", x, y, z);
    }
}
