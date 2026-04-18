package Server.Commands;

import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class RemoveByIdServerCommand implements ServerCommand {
    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fm) {
        Object arg = request.getArgument();
        if (!(arg instanceof Number)) {
            return new CommandResponse(false, "Требуется числовой ID");
        }
        long id = ((Number) arg).longValue();
        if (collectionManager.removeById(id)) {
            return new CommandResponse(true, "Продукт с ID " + id + " удалён");
        } else {
            return new CommandResponse(false, "Продукт с ID " + id + " не найден");
        }
    }
}