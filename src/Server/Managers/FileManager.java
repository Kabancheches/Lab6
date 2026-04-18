package Server.Managers;

import Common.Model.Classes.*;
import Common.Model.Enums.*;
import Common.Model.Validator.Validator;
import com.opencsv.CSVReader;
import com.opencsv.CSVParser;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FileManager  {
    private final String PATH;
    private final File file;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final String head = "id,name,x,y,creationDate,price,unitOfMeasure,orgId,orgName,orgFullName,employeesCount,orgType,street,locX,locY,locZ";
    private static final int fieldsNumber = 16;
    Validator validator = new Validator();

    public FileManager(String PATH) {
        this.PATH = PATH;
        this.file = new File(PATH);
    }

    public FileManager(File file) {
        this.PATH = file.getPath();
        this.file = file;
    }

    //КУДА ДЕТЬ ВАЛИДЕЙТ
    public boolean validateForReadingFile() {
        if (!file.exists()) {
            String text = String.format("ОШИБКА: Файл %s не существует.",PATH);
            System.err.println(text);
            return false;
        }
        if (!file.isFile()) {
            String text = String.format("ОШИБКА: %s не файл.",PATH);
            System.err.println(text);
            return false;
        }
        if (!file.canRead()) {
            String text = String.format("ОШИБКА: Нет прав доступа для чтения файла %s.",PATH);
            System.err.println(text);
            return false;
        }
        return true;
    }

    public boolean validateForWritingFile() {
        if (!file.exists()) {
            String text = String.format("ОШИБКА: Файл %s не существует.",PATH);
            System.err.println(text);
            return false;
        }
        if (!file.isFile()) {
            String text = String.format("ОШИБКА: %s не файл.",PATH);
            System.err.println(text);
            return false;
        }
        if (!file.canRead()) {
            String text = String.format("ОШИБКА: Нет прав доступа для чтения файла %s.",PATH);
            System.err.println(text);
            return false;
        }
        return true;
    }

    public boolean validateWritingReadingFile() {
        return (validateForWritingFile() || validateForReadingFile());
    }
    private Long parseLong(String num, String field) {
        Long id;
        try {
            id = Long.parseLong(num);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("поле " + field + " должно быть целым числом. Текущее значение: '" + num + "'");
        }
        return id;
    }

    private String parseString(String string, String field) {
        if (string.isBlank()) {
            throw new IllegalArgumentException("Пустая строка в поле " + field +". Текущее значение: '" + string + "'");
        }
        return string;
    }

    private Coordinates parseCoordinates(String x, String y, String fieldX, String fieldY) {
        Integer correctX;
        Float correctY;
        correctX = parseInteger(x,fieldX);
        if (correctX == null) return null;

        correctY = parseFloat(y,fieldY);
        if (correctY == null) return null;

        return new Coordinates(correctX, correctY);
    }

    private LocalDateTime parseLocalDateTime(String creationDate, String field) {
        LocalDateTime dateTimeFormatter = null;
        try {
            if (creationDate.isBlank()) {
                throw new IllegalArgumentException("поле " + field + " не может быть пустым.");
            }
            dateTimeFormatter = LocalDateTime.parse(creationDate);
        } catch (DateTimeParseException e) {
            throw new NumberFormatException("Неверный формат даты: '" + creationDate + "'. Ожидаемый формат: YYYY-MM-DDTHH:MM:SS");
        }
        return dateTimeFormatter;
    }

    private Float parseFloat(String num, String field) {
        float niceNum;
        try {
            niceNum = Float.parseFloat(num);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Ошибка: поле" + field + " должно быть числом. Текущее значение: '" + num + "'");
        }
        return niceNum;
    }

    private Integer parseInteger(String num, String field) {
        int niceNum;
        try {
            niceNum = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("поле " + field + " должно быть числом. Текущее значение: " + num + "'");
        }
        return niceNum;
    }

    private <E extends Enum<E>> E parseEnum(Class<E> enumClass, String strEnum, String field) {
        if (strEnum.isBlank()) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, strEnum);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("В поле " + field + " некорректное значение. Текущее значение: " + strEnum);
        }
    }

    private Organization parseOrganization(String[] line, String[] fields, int partNumber) {
        Organization manufacturer = new Organization();

        String partOfLine = line[partNumber++];
        if (!partOfLine.isBlank()) {
            Long id = parseLong(partOfLine, fields[partNumber - 1]);
            manufacturer.setId(id);
        } else {
            throw new IllegalArgumentException("Поле " + fields[partNumber - 1] + " пустое. Organization будет null.");
        }

        String name = parseString(line[partNumber++], fields[partNumber - 1]);
        manufacturer.setName(name);

        String fullName = parseString(line[partNumber++], fields[partNumber - 1]);
        manufacturer.setFullName(fullName);

        Integer employeesCount = parseInteger(line[partNumber++], fields[partNumber - 1]);
        if (employeesCount == null || employeesCount <= 0) return null;
        manufacturer.setEmployeesCount(employeesCount);

        OrganizationType type = parseEnum(OrganizationType.class, line[partNumber++], fields[partNumber - 1]);
        manufacturer.setType(type);

        Address address = parseAddress(line, fields, partNumber);
        if (address == null) {
            return null;
        }
        manufacturer.setOfficialAddress(address);
        return manufacturer;
    }

    private Address parseAddress(String[] line, String[] fields, int partNumber) {
        Address address = new Address();

        address.setStreet(line[partNumber++]);

        Location location = parseLocation(line, fields,partNumber);
        if (location == null) {
            return null;
        }
        address.setTown(location);
        return address;
    }

    private Location parseLocation(String[] line, String[] fields, int partNumber) {
        Float x = parseFloat(line[partNumber++], fields[partNumber - 1]);
        if (x == null) return null;

        Long y = parseLong(line[partNumber++], fields[partNumber - 1]);
        if (y == null) return null;

        Integer z = parseInteger(line[partNumber++], fields[partNumber - 1]);
        if (z == null) return null;

        return new Location(x, y, z);
    }

    public Product parseProduct(String[] line, String[] fields) {
        Product product = new Product();
        int partNumber = 0;

        if (line.length < fieldsNumber) {
            throw new IllegalArgumentException("Недостаточно полей класса Product.");
        }

        if (line.length > fieldsNumber) {
            throw new IllegalArgumentException("Переизбыток полей класса Product.");
        }

        Long idProduct = parseLong(line[partNumber++], fields[partNumber - 1]);
        if (idProduct != null) {
            product.setId(idProduct);
        } else {
            throw new IllegalArgumentException("Id у Product обязателен.");
        }

        product.setName(parseString(line[partNumber++], fields[partNumber - 1]));

        product.setCoordinates(parseCoordinates(line[partNumber++], line[partNumber++], fields[partNumber - 2], fields[partNumber - 1]));

        product.setCreationDate(parseLocalDateTime(line[partNumber++], fields[partNumber - 1]));

        product.setPrice(parseFloat(line[partNumber++], fields[partNumber - 1]));

        product.setUnitOfMeasure(parseEnum(Common.Model.Enums.UnitOfMeasure.class, line[partNumber++], fields[partNumber - 1]));

        product.setManufacturer(parseOrganization(line,fields, partNumber));

        return product;
    }

    //подключить библиотеку csv
    public PriorityQueue<Product> readCollection() {
        Set<Long> usedProductIds = new HashSet<>();
        Set<Long> usedOrgIds = new HashSet<>();
        List<Product> products = new ArrayList<>();

        if (!validateForReadingFile()) {
            return new PriorityQueue<>();
        }

        try (InputStream input = new FileInputStream(PATH);
             InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] line;
            int lineNumber = 0;
            boolean wasHead = false;

            while ((line = csvReader.readNext()) != null) {
                lineNumber++;

                if (line.length == 0 || (line.length == 1 && line[0].isBlank())) {
                    System.err.println("[ПРОПУСК] Строка номер: " + lineNumber + " - пустая. Пропускаем");
                    continue;
                }

                if (line[0].trim().startsWith("#")) {
                    continue;
                }

                if (!wasHead) {
                    if (line.length == fieldsNumber && String.join(",", line).equals(head)) {
                        wasHead = true;
                        continue;
                    } else {
                        throw new IllegalArgumentException("Заголовок неверного формата.");
                    }
                }

                try {
                    Product product = parseProduct(line, new CSVParser().parseLine(head));
                    if (product != null) {
                        if (!validator.validateWithIdProduct(product)) {
                            System.err.println("Product на строке: " + lineNumber + ". Product пропущен из-за некорректных значений полей.");
                            continue;
                        }
                        if (usedProductIds.contains(product.getId())) {
                            System.err.println("Продукт с ID " + product.getId() + " уже загружен. Пропуск.");
                            continue;
                        }
                        if (product.getManufacturer() != null && usedOrgIds.contains(product.getManufacturer().getId())) {
                            System.err.println("Организация с ID " + product.getManufacturer().getId() + " уже использована. Пропуск.");
                            continue;
                        }
                        usedProductIds.add(product.getId());
                        if (product.getManufacturer() != null) {
                            usedOrgIds.add(product.getManufacturer().getId());
                        }
                        products.add(product);
                    } else {
                        System.err.println("Product на  строке: " + lineNumber + ". Product пропущен из-за ошибок парсинга.");
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Ошибка при парсинге строки номер: " + lineNumber + ". Ошибка: "+ e.getMessage());
                }
            }

        } catch (IOException | CsvException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return null;
        }
        return new PriorityQueue<>(products);
    }

    private String correctToString(Object o) {
        if (o == null) {
            return "";
        } else {
            return o.toString();
        }
    }

    private String[] productToString(Product product) {
        String[] line = new String[fieldsNumber];
        int fieldNumber = 0;

        line[fieldNumber++] = correctToString(product.getId());
        line[fieldNumber++] = correctToString(product.getName());

        Coordinates coordinates = product.getCoordinates();
        line[fieldNumber++] = String.valueOf(coordinates.getX());
        line[fieldNumber++] = String.valueOf(coordinates.getY());

        line[fieldNumber++] = correctToString(product.getCreationDate());
        line[fieldNumber++] = String.valueOf(product.getPrice());
        line[fieldNumber++] = correctToString(product.getUnitOfMeasure());

        Organization manufacturer = product.getManufacturer();
        if (manufacturer != null) {
            line[fieldNumber++] = correctToString(manufacturer.getId());
            line[fieldNumber++] = correctToString(manufacturer.getName());
            line[fieldNumber++] = correctToString(manufacturer.getFullName());
            line[fieldNumber++] = String.valueOf(manufacturer.getEmployeesCount());
            line[fieldNumber++] = correctToString(manufacturer.getType());

            Address address = manufacturer.getOfficialAddress();
            if (address != null) {
                line[fieldNumber++] = correctToString(address.getStreet());

                Location location = address.getTown();
                if (location != null) {
                    line[fieldNumber++] = String.valueOf(location.getX());
                    line[fieldNumber++] = correctToString(location.getY());
                    line[fieldNumber] = String.valueOf(location.getZ());
                } else {
                    for (int i = 0; i < 3; i++) {
                        line[fieldNumber++] = "";
                    }
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    line[fieldNumber++] = "";
                }
            }
        } else {
            for (int i = 0; i < 9; i++) {
                line[fieldNumber++] = "";
            }
        }
        return line;
    }

    public boolean saveCollection(PriorityQueue<Product> collection) {
        if (!validateForWritingFile()) {
            return false;
        }

        if (collection == null) {
            System.err.println("ОШИБКА: Передана коллекция null.");
            return false;
        }

        try (OutputStream output = new FileOutputStream(file);
             Writer writer = new OutputStreamWriter(output);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            if (collection.isEmpty()) {
                System.out.println("Коллекция на запись пуста. Файл будет пустым.");
                return true;
            }

            bufferedWriter.write(head);
            bufferedWriter.newLine();

            Product product;
            PriorityQueue<Product> bufferQueque = new PriorityQueue<>(collection);

            while ((product = bufferQueque.poll()) != null) {
                if (!validator.validateWithIdProduct(product)) {
                    System.err.println("Product id=" + product.getId() + "не прошёл валидацию и не будет записан в файл. ");
                    continue;
                }
                String[] line = productToString(product);
                bufferedWriter.write(String.join(",",line));
                bufferedWriter.newLine();
            }

            return true;

        } catch (IOException e) {
            System.err.println("[ОШИБКА] Файл " + file.getName() + " не записался: " + e);
            return false;
        }
    }
}