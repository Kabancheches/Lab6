package Client.Controller.Commands;

import Common.Model.Classes.Product;
import Common.Model.Enums.CommandType;
import Client.Net.ClientNetManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;
import Client.View.InputReader;

public class AddCommand implements Command {
    public static final String name = "add";
    private final InputReader inputReader;
    private final ClientNetManager netManager;

    public AddCommand(InputReader inputReader, ClientNetManager netManager) {
        this.inputReader = inputReader;
        this.netManager = netManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            System.out.println("Введите данные нового продукта:");
            Product product = inputReader.readProduct();
            product.setId(null);
            if (product.getManufacturer() != null) {
                product.getManufacturer().setId(null);
            }
            System.out.println(product);
            CommandRequest request = new CommandRequest(CommandType.ADD, product);
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
        return "Добавить новый элемент в коллекцию";
    }

    @Override
    public String getName() {
        return name;
    }
}