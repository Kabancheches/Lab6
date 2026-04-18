package Client.Controller.Commands;

import Common.Model.Enums.CommandType;
import Client.Net.ClientNetManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class ExitCommand implements Command {
    public static final String name = "exit";
    private final ClientNetManager netManager;
    private boolean needExit = false;

    public ExitCommand(ClientNetManager netManager) {
        this.netManager = netManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            CommandRequest request = new CommandRequest(CommandType.EXIT, null);
            CommandResponse response = netManager.sendRequest(request);
            System.out.println(response.getMessage());
            needExit = true;
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка при завершении: " + e.getMessage());
            return false;
        }
    }

    public boolean isNeedExit() {
        return needExit;
    }

    @Override
    public String getDescription() {
        return "Завершить программу (с сохранением)";
    }

    @Override
    public String getName() {
        return name;
    }
}