package Server.Commands;

import Common.Model.Classes.Product;
import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class AddServerCommand implements ServerCommand {
    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fm) {
        Object arg = request.getArgument();
        if (!(arg instanceof Product)) {
            return new CommandResponse(false, "Аргумент должен быть Product");
        }
        Product product = (Product) arg;
        Long newId = collectionManager.getFirstNotUsedIdProduct();
        product.setId(newId);
        if (product.getManufacturer() != null) {
            product.getManufacturer().setId(collectionManager.getFirstNotUsedIdOrganization());
        }
        if (collectionManager.addProduct(product)) {
            return new CommandResponse(true, "Продукт добавлен, ID = " + newId);
        } else {
            return new CommandResponse(false, "Не удалось добавить продукт");
        }
    }
}