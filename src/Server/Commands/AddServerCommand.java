package Server.Commands;

import Client.Net.ClientNetManager;
import Model.Classes.Product;
import View.InputReader;
import common.Model.Enums.CommandType; // импорт общего enum
import Server.Net.CommandRequest;
import Server.Net.CommandResponse;

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
            // ID и Organization ID будут сгенерированы на сервере, поэтому здесь их не устанавливаем
            product.setId(null);
            if (product.getManufacturer() != null) {
                product.getManufacturer().setId(null);
            }

            CommandRequest request = new CommandRequest(CommandType.ADD, product);
            CommandResponse response = netManager.sendRequest(request);
            System.out.println(response.getMessage());
            return response.isSuccess();
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
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