package Server.Commands;

import Common.Model.Classes.Product;
import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class AddIfMinServerCommand implements ServerCommand {
    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fm) {
        Object arg = request.getArgument();
        if (!(arg instanceof Product)) {
            return new CommandResponse(false, "Требуется Product");
        }
        Product product = (Product) arg;
        if (collectionManager.getCollection().isEmpty()) {
            Long newId = collectionManager.getFirstNotUsedIdProduct();
            product.setId(newId);
            if (product.getManufacturer() != null) {
                product.getManufacturer().setId(collectionManager.getFirstNotUsedIdOrganization());
            }
            collectionManager.addProduct(product);
            return new CommandResponse(true, "Продукт добавлен (коллекция была пуста)");
        }
        float minPrice = collectionManager.getCollection().peek().getPrice();
        if (product.getPrice() < minPrice) {
            Long newId = collectionManager.getFirstNotUsedIdProduct();
            product.setId(newId);
            if (product.getManufacturer() != null) {
                product.getManufacturer().setId(collectionManager.getFirstNotUsedIdOrganization());
            }
            collectionManager.addProduct(product);
            return new CommandResponse(true, "Продукт добавлен (цена меньше минимальной)");
        } else {
            return new CommandResponse(false, "Цена не минимальна, продукт не добавлен");
        }
    }
}