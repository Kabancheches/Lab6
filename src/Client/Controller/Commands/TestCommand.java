package Client.Controller.Commands;

import Common.Model.Enums.CommandType;
import Client.Net.ClientNetManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class TestCommand implements Command {
    public static final String name = "test";
    private final ClientNetManager netManager;

    public TestCommand(ClientNetManager netManager) {
        this.netManager = netManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            String arg = (args.length > 1) ? args[1] : "ChikiBamboni";
            CommandRequest request = new CommandRequest(CommandType.TEST, arg);
            CommandResponse response = netManager.sendRequest(request);
            System.out.println("Ответ сервера: " + response.getMessage());
            return response.isSuccess();
        } catch (Exception e) {
            System.err.println("Ошибка при тестовом запросе: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Проверить соединение с сервером";
    }

    @Override
    public String getName() {
        return name;
    }
}