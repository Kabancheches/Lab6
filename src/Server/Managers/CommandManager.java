package Server.Managers;

import Common.Model.Enums.CommandType;
import Server.Commands.*;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

import java.util.EnumMap;
import java.util.Map;

public class CommandManager {
    private final Map<CommandType, ServerCommand> commands = new EnumMap<>(CommandType.class);
    private final CollectionManager collectionManager;
    private final FileManager fileManager;

    public CommandManager(CollectionManager collectionManager, FileManager fileManager) {
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
        registerCommands();
    }

    private void registerCommands() {
        commands.put(CommandType.TEST, new TestServerCommand());
        commands.put(CommandType.HELP, new HelpServerCommand());
        commands.put(CommandType.INFO, new InfoServerCommand());
        commands.put(CommandType.SHOW, new ShowServerCommand());
        commands.put(CommandType.ADD, new AddServerCommand());
        commands.put(CommandType.UPDATE, new UpdateIdServerCommand());
        commands.put(CommandType.REMOVE_BY_ID, new RemoveByIdServerCommand());
        commands.put(CommandType.CLEAR, new ClearServerCommand());
        commands.put(CommandType.SAVE, new SaveServerCommand());
        commands.put(CommandType.EXIT, new ExitServerCommand());
        commands.put(CommandType.ADD_IF_MIN, new AddIfMinServerCommand());
        commands.put(CommandType.REMOVE_GREATER, new RemoveGreaterServerCommand());
        commands.put(CommandType.REMOVE_LOWER, new RemoveLowerServerCommand());
        commands.put(CommandType.FILTER_BY_PRICE, new FilterByPriceServerCommand());
        commands.put(CommandType.FILTER_GREATER_THAN_PRICE, new FilterGreaterThanPriceServerCommand());
        commands.put(CommandType.PRINT_FIELD_DESCENDING_UNIT_OF_MEASURE, new PrintFieldDescendingUnitOfMeasureServerCommand());
    }

    public CommandResponse dispatch(CommandRequest request) {
        CommandType type = request.getCommandType();
        ServerCommand command = commands.get(type);
        if (command == null) {
            return new CommandResponse(false, "Команда не поддерживается сервером: " + type);
        }
        return command.execute(request, collectionManager, fileManager);
    }
}