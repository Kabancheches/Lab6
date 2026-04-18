package src.Server.Controller.Commands;

import src.Server.Model.Classes.Product;
import src.Server.Managers.CollectionManager;

import java.util.ArrayList;
import java.util.List;

public class FilterByPriceCommand implements Command {
    private final CollectionManager collectionManager;
    public static String name = "filter_by_price";
    public FilterByPriceCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    private ArrayList<Product> productsFilteredByPrice(float price) {
        List<Product> collectionCopy = List.copyOf(collectionManager.getCollection());
        ArrayList<Product> correctProducts = new ArrayList<>();
        for (Product product : collectionCopy) {
            if (product.getPrice() == price) {
                correctProducts.add(product);
            }
        }
        return correctProducts;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Некорректный ввод данных. Введите filter_by_price <price>, где <price> - это цена.");
            return false;
        }

        try {
            float price = Float.parseFloat(args[1]);
            ArrayList<Product> filtered = productsFilteredByPrice(price);

            if (filtered.isEmpty()) {
                System.out.println("Продукты с ценой " + price + " не найдены.");
            } else {
                System.out.println("Продукты с ценой " + price + ":");
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
        return "Вывести элементы с заданной ценой";
    }

    @Override
    public String getName() {
        return name;
    }
}