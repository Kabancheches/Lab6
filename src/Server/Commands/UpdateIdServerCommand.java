package Server.Commands;

import Model.Classes.Organization;
import Model.Classes.Product;
import Model.Managers.CollectionManager;
import View.InputReader;


public class UpdateIdCommand implements Command {
    private final CollectionManager collectionManager;
    private final InputReader inputReader;
    public static String name = "update";
    public UpdateIdCommand(CollectionManager collectionManager, InputReader inputReader) {
        this.collectionManager = collectionManager;
        this.inputReader = inputReader;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            System.err.println("[ОШИБКА] Использование: update <id>, где <id> - это id объекта класса Product, который вы хотите обновить.");
            return false;
        }

        try {
            long id = Long.parseLong(args[1]);
            System.out.println("Обновление продукта с ID: " + id);

            Product existingProduct = collectionManager.getProductById(id);
            if (existingProduct == null) {
                System.err.println("[ОШИБКА] Продукт с ID " + id + " не найден.");
                return false;
            }

            Product newProduct = inputReader.readProduct();
            newProduct.setId(id);
            Organization manufacturer = newProduct.getManufacturer();
            if (manufacturer != null) {
                manufacturer.setId(collectionManager.getFirstNotUsedIdOrganization());
            }
            newProduct.setCreationDate(existingProduct.getCreationDate());
            collectionManager.removeById(id);
            collectionManager.addProduct(newProduct);
            return true;

        } catch (NumberFormatException e) {
            System.err.println("[ОШИБКА] Некорректный ID. Должно быть число.");
            return false;
        } catch (Exception e) {
            System.err.println("[ОШИБКА] Ошибка при обновлении: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Обновить значение элемента коллекции по его id";
    }

    @Override
    public String getName() {
        return name;
    }
}