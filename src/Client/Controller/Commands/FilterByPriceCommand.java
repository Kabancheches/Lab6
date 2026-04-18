package Client.Controller.Commands;

import Common.Model.Enums.CommandType;
import Client.Net.ClientNetManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class FilterByPriceCommand implements Command {
    public static final String name = "filter_by_price";
    private final ClientNetManager netManager;

    public FilterByPriceCommand(ClientNetManager netManager) {
        this.netManager = netManager;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            System.err.println("Использование: filter_by_price <price>");
            return false;
        }
        try {
            float price = Float.parseFloat(args[1]);
            CommandRequest request = new CommandRequest(CommandType.FILTER_BY_PRICE, price);
            CommandResponse response = netManager.sendRequest(request);
            System.out.println(response.getMessage());
            return response.isSuccess();
        } catch (NumberFormatException e) {
            System.err.println("Цена должна быть числом");
            return false;
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Вывести элементы с заданной ценой";
    }

    @Override
    public String getName() {
        return name;
    }
}