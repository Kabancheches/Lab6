package src.Server.Controller.Commands;

import src.Server.Model.Classes.Product;
import src.Server.Managers.CollectionManager;

import java.util.PriorityQueue;

public class ShowCommand implements Command {
    private final CollectionManager collectionManager;
    public static String name = "show";
    public ShowCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String[] args) {
        if (collectionManager.isEmpty()) {
            System.out.println("Коллекция пуста.");
            return true;
        }

        System.out.println("Элементы коллекции:");
        System.out.println("-------------------");

        PriorityQueue<Product> collectionCopy = new PriorityQueue<>(collectionManager.getCollection());
        while (!collectionCopy.isEmpty()) {
            System.out.println(collectionCopy.poll());
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "Вывести все элементы коллекции в строковом представлении";
    }

    @Override
    public String getName() {
        return name;
    }
}