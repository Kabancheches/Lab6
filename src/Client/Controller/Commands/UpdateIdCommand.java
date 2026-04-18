package Client.Controller.Commands;

import Common.Model.Classes.Product;
import Common.Model.Enums.CommandType;
import Client.Net.ClientNetManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;
import Client.View.InputReader;

public class UpdateIdCommand implements Command {
    public static final String name = "update";
    private final InputReader inputReader;
    private final ClientNetManager netManager;

    public UpdateIdCommand(InputReader inputReader, ClientNetManager netManager) {
        this.inputReader = inputReader;
        this.netManager = netManager;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            System.err.println("Использование: update <id>");
            return false;
        }
        try {
            long id = Long.parseLong(args[1]);
            System.out.println("Введите новые данные для продукта с ID " + id);
            Product newProduct = inputReader.readProduct();
            newProduct.setId(id);
            if (newProduct.getManufacturer() != null) {
                newProduct.getManufacturer().setId(null);
            }
            Object[] updateData = new Object[]{id, newProduct};
            CommandRequest request = new CommandRequest(CommandType.UPDATE, updateData);
            CommandResponse response = netManager.sendRequest(request);
            System.out.println(response.getMessage());
            return response.isSuccess();
        } catch (NumberFormatException e) {
            System.err.println("ID должен быть числом");
            return false;
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Обновить элемент по id";
    }

    @Override
    public String getName() {
        return name;
    }
}