package Common.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Продукт - основной объект коллекции.
 */
public class Product implements Serializable, Comparable<Product> {
    private static final long serialVersionUID = 1L;
    
    private Long id; // Поле не может быть null, Значение поля должно быть больше 0, уникальное, генерируется автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private LocalDateTime creationDate; // Поле не может быть null, генерируется автоматически
    private float price; // Значение поля должно быть больше 0
    private UnitOfMeasure unitOfMeasure; // Поле может быть null
    private Organization manufacturer; // Поле может быть null

    public Product() {}

    public Product(Long id, String name, Coordinates coordinates, LocalDateTime creationDate, float price, UnitOfMeasure unitOfMeasure, Organization manufacturer) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.unitOfMeasure = unitOfMeasure;
        this.manufacturer = manufacturer;
    }

    public Product(Long id, String name, Coordinates coordinates, float price, UnitOfMeasure unitOfMeasure, Organization manufacturer) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.price = price;
        this.unitOfMeasure = unitOfMeasure;
        this.manufacturer = manufacturer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Organization getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Organization manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public int compareTo(Product other) {
        return Double.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        return String.format(
                "Product{id: %d; name: \"%s\"; coordinates: %s; creationDate: %s; price: %f; unitOfMeasure: %s; manufacturer: %s}",
                id,
                name,
                coordinates,
                creationDate != null ? creationDate.toString() : "null",
                price,
                unitOfMeasure == null ? "null" : "\"" + unitOfMeasure + "\"",
                manufacturer
        );
    }
}
