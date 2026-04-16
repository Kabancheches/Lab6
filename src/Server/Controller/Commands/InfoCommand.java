package src.Server.Controller.Commands;

import Model.Managers.CollectionManager;

import java.time.format.DateTimeFormatter;

public class InfoCommand implements Command {
    public static String name = "info";
    private final CollectionManager collectionManager;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public InfoCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String[] args) {
        System.out.println("Информация о коллекции:");
        System.out.println("Тип коллекции: " + collectionManager.getCollection().getClass());
        System.out.println("Дата инициализации: " + collectionManager.getInitializationDate().format(FORMATTER));
        System.out.println("Количество элементов: " + collectionManager.getCollection().size());
        return true;
    }

    @Override
    public String getDescription() {
        return "Вывести информацию о коллекции (тип, дата инициализации, количество)";
    }

    @Override
    public String getName() {
        return name;
    }
}