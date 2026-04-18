package Common.Model.Validator;


import Common.Model.Classes.*;

public class Validator {
    public String coordinatesPrompt = "Значение поле X должно быть не больше 12.";

    public boolean validateWithIdProduct(Product product) {
        if (product.getId() == null || !validateId(product.getId())) return false;
        if (product.getName() == null || product.getName().isEmpty()) return false;
        if (product.getCoordinates() == null || !validateCoordinates(product.getCoordinates())) return false;
        if (product.getManufacturer() != null && !validateOrganizationAfterId(product.getManufacturer())) return false;
        if (product.getCreationDate() == null) return false;
        if (product.getPrice() <= 0) return false;
        return true;
    }

    public boolean validateWithoutIdProduct(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) return false;
        if (product.getCoordinates() == null || !validateCoordinates(product.getCoordinates())) return false;
        if (product.getManufacturer() != null && !validateOrganizationBeforeId(product.getManufacturer())) return false;
        if (product.getCreationDate() == null) return false;
        if (product.getPrice() <= 0) return false;
        return true;
    }

    public boolean validateCoordinates(Coordinates coordinates) throws IllegalArgumentException {
        if (coordinates != null) {
            if (coordinates.getX() <= 12) {
                return true;
            } else {
                throw new IllegalArgumentException("ОШИБКА: Максимальное значение поля x в классе Coordinates = 12.");
            }
        } else {
            return false;
        }
    }

    public boolean validateLocation(Location location) throws NullPointerException {
        if (location.getY() == null) {
            throw new NullPointerException();
        } else {
            return true;
        }
    }

    public boolean validateOrganizationBeforeId(Organization organization) {
        if (organization.getName() == null || organization.getName().isEmpty()) {
            throw new IllegalArgumentException("ОШИБКА: Поле Name не может быть null.");
        }
        if (organization.getFullName() == null || organization.getFullName().isEmpty()){
            throw new IllegalArgumentException("ОШИБКА: Поле FullName не может быть null.");
        }
        if (organization.getEmployeesCount() <= 0) {
            throw new IllegalArgumentException("ОШИБКА: Поле EmployeesCount не может быть меньше 0.");
        }
        if (organization.getOfficialAddress() == null){
            throw new IllegalArgumentException("ОШИБКА: Поле OfficialAddress не может быть null.");
        }
        return true;
    }

    public boolean validateOrganizationAfterId(Organization organization) {
        if (organization.getId() == null || organization.getId() <= 0) {
            throw new IllegalArgumentException("ОШИБКА: Поле Id должно быть больше 0.");
        }
        if (organization.getName() == null || organization.getName().isEmpty()) {
            throw new IllegalArgumentException("ОШИБКА: Поле Name не может быть null.");
        }
        if (organization.getFullName() == null || organization.getFullName().isEmpty()){
            throw new IllegalArgumentException("ОШИБКА: Поле FullName не может быть null.");
        }
        if (organization.getEmployeesCount() <= 0) {
            throw new IllegalArgumentException("ОШИБКА: Поле EmployeesCount не может быть меньше 0.");
        }
        if (organization.getOfficialAddress() == null){
            throw new IllegalArgumentException("ОШИБКА: Поле OfficialAddress не может быть null.");
        }
        return true;
    }

    public boolean validateId(Long id) throws IllegalArgumentException {
        Long idMin = 1L;
        if (id >= idMin) {
            return true;
        } else {
            throw new IllegalArgumentException("ОШИБКА: Значение поля id у Product должно быть больше " + idMin + ".");
        }
    }

    public boolean validateEmployeeCount(int employeeCount) {
        int minEmployeeCount = 0;
        if (employeeCount > minEmployeeCount) {
            return true;
        } else {
            throw new IllegalArgumentException("ОШИБКА: Значение поля employeesCount у Organization должно быть больше " + minEmployeeCount + ".");
        }
    }

    public boolean validateName(String name) throws IllegalArgumentException {
        if (name.isBlank()) {
            throw new IllegalArgumentException("ОШИБКА: поле name не может быть пустым.");
        }
        return true;
    }

    public boolean validatePrice(float price) throws IllegalArgumentException {
        float priceMinNotIncluded = 0;
        if (price > priceMinNotIncluded) {
            return true;
        } else {
            throw new IllegalArgumentException("ОШИБКА: поле price должно быть больше " + priceMinNotIncluded +".");
        }
    }
}