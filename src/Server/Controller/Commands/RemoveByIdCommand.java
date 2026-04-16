package src.Server.Controller.Commands;

import Model.Managers.CollectionManager;

public class RemoveByIdCommand implements Command {
    private final CollectionManager collectionManager;
    public static String name = "remove_by_id";
    public RemoveByIdCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            System.err.println("[ОШИБКА] Использование: remove_by_id <id>, где <id> - это id объекта класса Product, который вы хотите удалить.");
            return false;
        }

        try {
            long id = Long.parseLong(args[1]);
            if (collectionManager.removeById(id)) {
                System.out.println("Продукт с ID " + id + " успешно удален.");
                return true;
            } else {
                System.err.println("[ОШИБКА] Продукт с ID " + id + " не найден.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.err.println("[ОШИБКА] Некорректный ID. Должно быть число.");
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Удалить элемент из коллекции по его id";
    }

    @Override
    public String getName() {
        return name;
    }
}