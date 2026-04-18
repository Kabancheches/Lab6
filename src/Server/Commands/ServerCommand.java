package Server.Controller.Commands;

import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Server.Net.CommandRequest;
import Server.Net.CommandResponse;

public interface ServerCommand {
    CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fileManager);
}