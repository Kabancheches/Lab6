package Client.Controller.Commands;

import Common.Model.Enums.CommandType;
import Client.Net.ClientNetManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class PrintFieldDescendingUnitOfMeasureCommand implements Command {
    public static final String name = "print_field_descending_unit_of_measure";
    private final ClientNetManager netManager;

    public PrintFieldDescendingUnitOfMeasureCommand(ClientNetManager netManager) {
        this.netManager = netManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            CommandRequest request = new CommandRequest(CommandType.PRINT_FIELD_DESCENDING_UNIT_OF_MEASURE, null);
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
        return "Вывести единицы измерения в порядке убывания";
    }

    @Override
    public String getName() {
        return name;
    }
}