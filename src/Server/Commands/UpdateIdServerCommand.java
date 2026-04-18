package Server.Commands;

import Common.Model.Classes.Product;
import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class UpdateIdServerCommand implements ServerCommand {
    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fm) {
        Object arg = request.getArgument();
        if (!(arg instanceof Object[])) {
            return new CommandResponse(false, "Ожидается массив [id, Product]");
        }
        Object[] data = (Object[]) arg;
        if (data.length < 2 || !(data[0] instanceof Number) || !(data[1] instanceof Product)) {
            return new CommandResponse(false, "Неверный формат: [id, Product]");
        }
        long id = ((Number) data[0]).longValue();
        Product newProduct = (Product) data[1];
        Product old = collectionManager.getProductById(id);
        if (old == null) {
            return new CommandResponse(false, "Продукт с ID " + id + " не найден");
        }
        newProduct.setId(id);
        newProduct.setCreationDate(old.getCreationDate());
        if (newProduct.getManufacturer() != null && newProduct.getManufacturer().getId() == null) {
            newProduct.getManufacturer().setId(collectionManager.getFirstNotUsedIdOrganization());
        }
        collectionManager.removeById(id);
        collectionManager.addProduct(newProduct);
        return new CommandResponse(true, "Продукт с ID " + id + " обновлён");
    }
}