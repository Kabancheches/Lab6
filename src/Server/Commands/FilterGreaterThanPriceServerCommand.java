package Server.Commands;

import Common.Model.Classes.Product;
import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class FilterGreaterThanPriceServerCommand implements ServerCommand {
    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fm) {
        Object arg = request.getArgument();
        if (!(arg instanceof Number)) {
            return new CommandResponse(false, "Требуется цена (число)");
        }
        float price = ((Number) arg).floatValue();
        StringBuilder sb = new StringBuilder();
        for (Product p : collectionManager.getCollection()) {
            if (p.getPrice() > price) {
                sb.append(p).append("\n");
            }
        }
        if (sb.length() == 0) {
            return new CommandResponse(true, "Продуктов с ценой выше " + price + " не найдено");
        }
        return new CommandResponse(true, sb.toString());
    }
}