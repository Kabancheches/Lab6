package src.Server.Controller.Commands;


import src.Server.Managers.CollectionManager;

public class ClearCommand implements Command {
    public static String name = "clear";
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String[] args) {
        collectionManager.clear();
        System.out.println("Коллекция успешно очищена.");
        return true;
    }

    @Override
    public String getDescription() {
        return "Очистить коллекцию";
    }

    @Override
    public String getName() {
        return name;
    }
}