package Server.Commands;

import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class ExitServerCommand implements ServerCommand {

    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fileManager) {
        System.out.println("Клиент завершает работу");
        return new CommandResponse(true, "Прощай легенда.");
    }
}