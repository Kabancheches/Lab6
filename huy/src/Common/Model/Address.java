package Common.Model;

import java.io.Serializable;

/**
 * Адрес организации.
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String street; // Поле может быть null
    private Location town; // Поле может быть null

    public Address() {}

    public Address(String street, Location town) {
        this.street = street;
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Location getTown() {
        return town;
    }

    public void setTown(Location town) {
        this.town = town;
    }

    @Override
    public String toString() {
        return String.format("Address{street: \"%s\"; town: %s}", 
            street == null ? "null" : street, 
            town == null ? "null" : town);
    }
}
