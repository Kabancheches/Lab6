package Client.Controller.Commands;

import Common.Model.Enums.CommandType;
import Client.Net.ClientNetManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class ClearCommand implements Command {
    public static final String name = "clear";
    private final ClientNetManager netManager;

    public ClearCommand(ClientNetManager netManager) {
        this.netManager = netManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            CommandRequest request = new CommandRequest(CommandType.CLEAR, null);
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
        return "Очистить коллекцию";
    }

    @Override
    public String getName() {
        return name;
    }
}