package Server.Commands;

import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public interface ServerCommand {
    CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fileManager);
}