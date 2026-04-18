package Server.Commands;

import Model.Managers.CollectionManager;
import Model.Managers.FileManager;

public class SaveCommand implements Command {
    public static String name = "save";
    private final CollectionManager collectionManager;
    private final FileManager fileManager;

    public SaveCommand(CollectionManager collectionManager, FileManager fileManager) {
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            if (fileManager.saveCollection(collectionManager.getCollection())) {
                System.out.println("Коллекция успешно сохранена в файл.");
                return true;
            } else {
                System.err.println("[ОШИБКА] Не удалось сохранить коллекцию.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("[ОШИБКА] Ошибка при сохранении: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Сохранить коллекцию в файл";
    }

    @Override
    public String getName() {
        return name;
    }
}