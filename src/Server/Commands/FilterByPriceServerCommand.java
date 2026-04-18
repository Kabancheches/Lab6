package Server.Commands;

import Common.Model.Classes.Product;
import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class FilterByPriceServerCommand implements ServerCommand {
    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fileManager) {
        Object arg = request.getArgument();
        if (!(arg instanceof Number)) {
            return new CommandResponse(false, "Требуется цена (число)");
        }
        float price = ((Number) arg).floatValue();
        StringBuilder stringBuilder = new StringBuilder();
        for (Product p : collectionManager.getCollection()) {
            if (Math.abs(p.getPrice() - price) < 0.0001f) {
                stringBuilder.append(p).append("\n");
            }
        }
        if (stringBuilder.isEmpty()) {
            return new CommandResponse(true, "Продукты с ценой " + price + " не найдены");
        }
        return new CommandResponse(true, stringBuilder.toString());
    }
}