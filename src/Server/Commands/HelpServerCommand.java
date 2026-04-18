package Server.Commands;

import Common.Model.Enums.CommandType;
import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class HelpServerCommand implements ServerCommand {
    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fileManager) {
        StringBuilder stringBuilder = new StringBuilder("Доступные команды:\n");
        for (CommandType type : CommandType.values()) {
            if (type != CommandType.EXECUTE_SCRIPT) {
                stringBuilder.append("  ").append(type.getCommandName()).append("\n");
            }
        }
        return new CommandResponse(true, stringBuilder.toString());
    }
}