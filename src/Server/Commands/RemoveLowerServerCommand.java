package Server.Commands;

import Model.Classes.Organization;
import Model.Classes.Product;
import Model.Managers.CollectionManager;
import View.InputReader;

import java.util.ArrayList;
import java.util.List;

public class RemoveLowerCommand implements Command {
    public static String name = "remove_lower";
    private final CollectionManager collectionManager;
    private final InputReader inputReader;

    public RemoveLowerCommand(CollectionManager collectionManager, InputReader inputReader) {
        this.collectionManager = collectionManager;
        this.inputReader = inputReader;
    }

    private ArrayList<Product> productsFilteredLowerThanPrice(float price) {
        List<Product> collectionCopy = List.copyOf(collectionManager.getCollection());
        ArrayList<Product> correctProducts = new ArrayList<>();
        for (Product product : collectionCopy) {
            if (product.getPrice() < price) {
                correctProducts.add(product);
            }
        }
        return correctProducts;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            Product product = inputReader.readProduct();
            product.setId(collectionManager.getFirstNotUsedIdProduct());
            Organization manufacturer = product.getManufacturer();
            if (manufacturer != null) {
                manufacturer.setId(collectionManager.getFirstNotUsedIdOrganization());
            }
            ArrayList<Product> filteredProducts = productsFilteredLowerThanPrice(product.getPrice());
            System.out.println("Удалено элементов у которых цена ниже " + product.getPrice() + ": " + filteredProducts.size());
            filteredProducts.forEach(System.out::println);
            return true;
        } catch (Exception e) {
            System.err.println("[ОШИБКА] " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Удалить из коллекции все элементы, меньшие заданного";
    }

    @Override
    public String getName() {
        return name;
    }
}