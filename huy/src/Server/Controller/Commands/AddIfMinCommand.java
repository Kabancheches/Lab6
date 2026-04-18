package src.Server.Controller.Commands;

import src.Server.Model.Classes.Organization;
import src.Server.Model.Classes.Product;
import src.Server.Managers.CollectionManager;
import src.Client.View.InputReader;

import java.util.PriorityQueue;

public class AddIfMinCommand implements Command {
    public static String name = "";
    private final CollectionManager collectionManager;
    private final InputReader inputReader;

    public AddIfMinCommand(CollectionManager collectionManager, InputReader inputReader) {
        this.collectionManager = collectionManager;
        this.inputReader = inputReader;
    }

    private boolean ifLowerThanMin(Product product) {
        float productPrice = product.getPrice();
        PriorityQueue<Product> collectionCopy = collectionManager.getCollection();
        for (int i = 0;i < collectionCopy.size(); i++){
            if (productPrice > collectionCopy.poll().getPrice()){
                return false;
            }
        }
        return true;
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

            if (ifLowerThanMin(product)) {
                collectionManager.addProduct(product);
                System.out.println("Продукт добавлен (его цена меньше минимальной).");
                return true;
            } else {
                System.out.println("Продукт не добавлен (его цена больше минимальной).");
                return false;
            }
        } catch (Exception e) {
            System.out.println("[ОШИБКА] " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Добавить новый элемент, если его значение меньше наименьшего";
    }

    @Override
    public String getName() {
        return name;
    }
}