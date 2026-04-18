package Client.Controller.Commands;

import Common.Model.Classes.Product;
import Common.Model.Enums.CommandType;
import Client.Net.ClientNetManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;
import Client.View.InputReader;

public class RemoveGreaterCommand implements Command {
    public static final String name = "remove_greater";
    private final InputReader inputReader;
    private final ClientNetManager netManager;

    public RemoveGreaterCommand(InputReader inputReader, ClientNetManager netManager) {
        this.inputReader = inputReader;
        this.netManager = netManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            System.out.println("Введите продукт для сравнения (будут удалены элементы с большей ценой):");
            Product product = inputReader.readProduct();
            product.setId(null);
            if (product.getManufacturer() != null) {
                product.getManufacturer().setId(null);
            }
            CommandRequest request = new CommandRequest(CommandType.REMOVE_GREATER, product);
            CommandResponse response = netManager.sendRequest(request);
            System.out.println(response.getMessage());
            return response.isSuccess();
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Удалить элементы, превышающие заданный";
    }

    @Override
    public String getName() {
        return name;
    }
}