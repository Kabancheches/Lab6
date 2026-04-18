package Server.Commands;

import Common.Model.Classes.Product;
import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class ShowServerCommand implements ServerCommand {
    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fm) {
        if (collectionManager.isEmpty()) {
            return new CommandResponse(true, "Коллекция пуста.");
        }
        StringBuilder sb = new StringBuilder();
        for (Product p : collectionManager.getCollection()) {
            sb.append(p).append("\n");
        }
        return new CommandResponse(true, sb.toString());
    }
}