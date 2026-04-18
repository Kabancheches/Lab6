package Client.Controller.Commands;

import Common.Model.Enums.CommandType;
import Client.Net.ClientNetManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public class RemoveByIdCommand implements Command {
    public static final String name = "remove_by_id";
    private final ClientNetManager netManager;

    public RemoveByIdCommand(ClientNetManager netManager) {
        this.netManager = netManager;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            System.err.println("Использование: remove_by_id <id>");
            return false;
        }
        try {
            long id = Long.parseLong(args[1]);
            CommandRequest request = new CommandRequest(CommandType.REMOVE_BY_ID, id);
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
        return "Удалить элемент по id";
    }

    @Override
    public String getName() {
        return name;
    }
}