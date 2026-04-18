package Server.Commands;

import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class TestServerCommand implements ServerCommand {
    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fileManager) {
        Object arg = request.getArgument();
        String echo = (arg != null) ? arg.toString() : "ChikiBamboni";
        return new CommandResponse(true, "LazeevTop: " + echo);
    }
}