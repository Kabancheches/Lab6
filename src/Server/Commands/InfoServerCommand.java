package Server.Commands;

import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

import java.time.format.DateTimeFormatter;

public class InfoServerCommand implements ServerCommand {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fm) {
        String info = String.format("Тип коллекции: %s\nДата инициализации: %s\nКоличество элементов: %d",
                collectionManager.getCollection().getClass().getSimpleName(),
                collectionManager.getInitializationDate().format(FORMATTER),
                collectionManager.getCollection().size()
        );
        return new CommandResponse(true, info);
    }
}