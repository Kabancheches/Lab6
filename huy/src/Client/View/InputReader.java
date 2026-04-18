package src.Client.View;

import src.Client.Model.Classes.*;
import src.Client.Model.Enums.*;
import src.Client.Model.Validator.Validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

public class InputReader {
    private final Validator validator = new Validator();
    private final BufferedReader reader;

    public InputReader() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public int readInt(String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            String input = readLine().trim();

            if (input.isEmpty()) {
                System.out.println("Значение не может быть пустым.");
                continue;
            }

            int value;
            try {
                value = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод данных. Введите целое число.");
                continue;
            }

            return value;
        }
    }

    public float readFloat(String prompt) throws IOException, IllegalArgumentException {
        while (true) {
            System.out.print(prompt);
            String input = readLine().trim();

            if (input.isEmpty()) {
                System.out.println("Значение не может быть пустым.");
                continue;
            }

            input = input.replace(',', '.');
            float value;
            try {
                value = Float.parseFloat(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ОШИБКА: Некорректный ввод данных. Введите число с плавающей точкой.");
            }
            return value;
        }
    }

    public Long readLong(String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            String input = readLine().trim();

            if (input.isEmpty()) {
                System.out.println("Значение не может быть пустым.");
                continue;
            }

            long value;
            try {
                value = Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Введите целое число.");
                continue;
            }
            return value;
        }
    }


    public String readString(String prompt, boolean nullable) throws IOException {
        if (prompt != null) {
            System.out.println(prompt);
        }
        while (true) {
            String value = readLine();
            if (value.isBlank()) {
                if (nullable) {
                    return null;
                }
                System.out.println("Строка не может быть пустой.");
                continue;
            }
            return value;
        }
    }

    public <E extends Enum<E>> E readEnum(String prompt, Class<E> enumClass) throws IOException {
        E[] enums = enumClass.getEnumConstants();

        System.out.println("Доступные значения:");
        for (int i = 0; i < enums.length; i++) {
            System.out.printf("%d) %s%n", i + 1, enums[i].name());
        }

        while (true) {
            System.out.print(prompt);
            String input = readLine().trim();

            try {
                return Enum.valueOf(enumClass, input);
            } catch (IllegalArgumentException e) {
                System.out.println("Некорректное значение. Выберите из списка.");
            }

            System.out.println("Если хотите значение null, нажмите enter");
            if (input.isEmpty()) {
                return null;
            }
        }
    }


    public Coordinates readCoordinates() throws IOException {
        Coordinates coordinates = new Coordinates();

        int x = readInt("Введите координату X: ");
        float y = readFloat("Введите координату Y: ");

        coordinates.setX(x);
        coordinates.setY(y);

        return coordinates;
    }

    public Location readLocation() throws IOException {
        Location location = new Location();

        float x = readFloat("Введите координату X: ");
        Long y = readLong("Введите координату Y: ");
        int z = readInt("Введите координату Z: ");

        location.setX(x);
        location.setY(y);
        location.setZ(z);

        return location;
    }

    public Address readAddress() throws IOException {
        Address address = new Address();

        while (true) {
            String street = readString("Введите улицу (street) (если строка пустая, значение будет null): ", true);
            Location town;
            while (true) {
                System.out.println("Введите местоположение (Location):");
                try {
                    town = readLocation();
                    if (validator.validateLocation(town)) {
                        break;
                    }
                } catch (NullPointerException e) {
                    System.out.println("Поле y не может быть null.");
                }
            }

            address.setStreet(street);
            address.setTown(town);

            return address;
        }
    }

    public Organization readOrganization() throws IOException {
        Organization organization = new Organization();

        String name = readString("Введите название организации: ", false);
        String fullName = readString("Введите полное название: ", false);

        int employeesCount;
        while (true) {
            try {
                employeesCount = readInt("Введите количество сотрудников: ");
                if (validator.validateEmployeeCount(employeesCount)) {
                break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println();
                System.out.println(e.getMessage());
                System.out.println();
            }
        }
        OrganizationType type;
        System.out.print("Ввести тип организации? (y/n): ");
        if (readLine().trim().equalsIgnoreCase("y")) {
            type = readEnum("Выберите тип организации: ", OrganizationType.class);
        } else {
            type = null;
        }

        System.out.println("Введите адрес организации (Address):");
        Address address = readAddress();


        organization.setName(name);
        organization.setFullName(fullName);
        organization.setEmployeesCount(employeesCount);
        organization.setType(type);
        organization.setOfficialAddress(address);
        return organization;
    }

    public Product readProduct() throws IOException {
        Product product = new Product();

        String name;
        while (true) {
            System.out.println("Введите название продукта (Product): ");
            try {
                name = readLine();
                if (validator.validateName(name)) {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println();
                System.out.println(e.getMessage());
                System.out.println();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }

        Coordinates coordinates;
        while (true) {
            System.out.println("Введите координаты (Coordinates): ");
            System.out.println(validator.coordinatesPrompt);
            coordinates = readCoordinates();
            
            try {
                if (validator.validateCoordinates(coordinates)) {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println();
                System.out.println(e.getMessage());
                System.out.println();
            }
        }

        float price;
        while (true) {
            try {
                price = readFloat("Введите цену (price): ");
                if (validator.validatePrice(price)) {
                    break;
                }
            } catch (IllegalArgumentException | IOException e) {
                System.out.println();
                System.out.println(e.getMessage());
                System.out.println();
            }
        }

        UnitOfMeasure unitOfMeasure;
        while (true) {
            System.out.print("Ввести единицу измерения (UnitOfMeasure)? (y/n): ");
            String line = readLine().trim();
            if (line.equalsIgnoreCase("y")) {
                unitOfMeasure = readEnum("Выберите единицу измерения (UnitOfMeasure): ", UnitOfMeasure.class);
                break;
            } else if (line.equalsIgnoreCase("n")) {
                System.out.println("Поле будет null.");
                unitOfMeasure = null;
                break;
            }
        }

        Organization manufacturer;
        System.out.print("Ввести производителя (Organization)? (y/n): ");
        if (readLine().trim().equalsIgnoreCase("y")) {
            while (true) {
                System.out.println("Введите информацию о производителе (Organization):");
                manufacturer = readOrganization();

                try {
                    if (manufacturer != null && validator.validateOrganizationBeforeId(manufacturer)) {
                        break;
                    } else {
                        System.out.println("Ошибка валидации организации (Organization). Проверьте введённые данные.");
                    }
                } catch (Exception e) {
                    System.out.println();
                    System.out.println(e.getMessage());
                    System.out.println();
                }
            }
        } else manufacturer = null;

        product.setName(name);
        product.setCoordinates(coordinates);
        product.setPrice(price);
        product.setUnitOfMeasure(unitOfMeasure);
        product.setManufacturer(manufacturer);
        product.setCreationDate(LocalDateTime.now());

        try {
            if (validator.validateWithoutIdProduct(product)) {
                return product;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return product;
    }
}