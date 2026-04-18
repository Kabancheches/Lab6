package src.Server.Controller.Commands;

import Model.Classes.Product;
import Model.Managers.CollectionManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PrintFieldDescendingUnitOfMeasureCommand implements Command {
    private final CollectionManager collectionManager;
    public static String name = "print_field_descending_unit_of_measure";
    public PrintFieldDescendingUnitOfMeasureCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String[] args) {
        List<Product> collection = new ArrayList<>(collectionManager.getCollection());
        collection.sort(Comparator.reverseOrder());

        System.out.println("Значения полей unitOfMeasure в порядке убывания Product:");
        for (Product product : collection) {
            System.out.println(product.getUnitOfMeasure());
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "Вывести значения поля unitOfMeasure всех элементов в порядке убывания";
    }

    @Override
    public String getName() {
        return name;
    }
}