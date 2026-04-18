package src.Server.Controller.Commands;

import src.Server.Model.Classes.Organization;
import src.Server.Model.Classes.Product;
import src.Server.Managers.CollectionManager;
import src.Client.View.InputReader;

public class AddCommand implements Command {
    public static String name = "add";
    private final CollectionManager collectionManager;
    private final InputReader inputReader;

    public AddCommand(CollectionManager collectionManager, InputReader inputReader) {
        this.collectionManager = collectionManager;
        this.inputReader = inputReader;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            System.out.println("Добавление нового продукта:");
            Product product = inputReader.readProduct();
            System.out.println();
            product.setId(collectionManager.getFirstNotUsedIdProduct());
            Organization manufacturer = product.getManufacturer();
            if (manufacturer != null) {
                manufacturer.setId(collectionManager.getFirstNotUsedIdOrganization());
            }

            if (product == null || product.getId() == null) {
                return false;
            }

            if (collectionManager.addProduct(product)) {
                System.out.println("Продукт успешно добавлен с ID: " + product.getId());
                return true;
            } else {
                System.out.println("Не удалось добавить продукт.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении продукта: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Добавить новый элемент в коллекцию";
    }

    @Override
    public String getName() {
        return name;
    }
}