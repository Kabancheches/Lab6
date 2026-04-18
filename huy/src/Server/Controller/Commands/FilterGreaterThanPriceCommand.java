package src.Server.Controller.Commands;

import src.Server.Model.Classes.Product;
import src.Server.Managers.CollectionManager;

import java.util.ArrayList;
import java.util.List;

public class FilterGreaterThanPriceCommand implements Command {

    private final CollectionManager collectionManager;
    public static String name = "filter_greater_than_price";

    public FilterGreaterThanPriceCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    private List<Product> productsFilteredGreaterThanPrice(float price) {
        List<Product> collectionCopy = List.copyOf(collectionManager.getCollection());
        List<Product> correctProducts = new ArrayList<>();
        for (Product product : collectionCopy) {
            if (product.getPrice() > price) {
                correctProducts.add(product);
            }
        }
        return correctProducts;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Использование: filter_greater_than_price <price>");
            return false;
        }

        try {
            float price = Float.parseFloat(args[1]);
            List<Product> filtered = productsFilteredGreaterThanPrice(price);
            if (filtered.isEmpty()) {
                System.out.println("Продукты с ценой больше " + price + " не найдены.");
            } else {
                System.out.println("Продукты с ценой больше " + price + ":");
                filtered.forEach(System.out::println);
            }
            return true;
        } catch (NumberFormatException e) {
            System.out.println("[ОШИБКА] Некорректная цена.");
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Вывести элементы с ценой больше заданной";
    }

    @Override
    public String getName() {
        return name;
    }
}