package Server.Commands;

import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class SaveServerCommand implements ServerCommand {
    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fm) {
        boolean saved = fm.saveCollection(collectionManager.getCollection());
        if (saved) {
            return new CommandResponse(true, "Коллекция сохранена в файл");
        } else {
            return new CommandResponse(false, "Ошибка при сохранении коллекции");
        }
    }
}