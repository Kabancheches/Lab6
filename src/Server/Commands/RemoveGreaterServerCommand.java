package Server.Commands;

import Common.Model.Classes.Product;
import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

import java.util.ArrayList;
import java.util.List;

public class RemoveGreaterServerCommand implements ServerCommand {
    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fm) {
        Object arg = request.getArgument();
        if (!(arg instanceof Product threshold)) {
            return new CommandResponse(false, "Требуется Product для сравнения");
        }
        List<Product> toRemove = new ArrayList<>();
        for (Product p : collectionManager.getCollection()) {
            if (p.getPrice() > threshold.getPrice()) {
                toRemove.add(p);
            }
        }
        for (Product p : toRemove) {
            collectionManager.removeById(p.getId());
        }
        return new CommandResponse(true, "Удалено элементов: " + toRemove.size());
    }
}