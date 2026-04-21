package Client;

import Client.Controller.CommandManager;
import Client.Controller.Commands.*;
import Client.Net.ClientNetManager;
import Client.View.InputReader;
import Common.Model.Enums.CommandType;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

import java.io.IOException;

public class ClientMain {
    private static final int DEFAULT_PORT = 6778;
    private static final String DEFAULT_HOST = "localhost";

    public static void main(String[] args) {
        InputReader inputReader = new InputReader();

        String str = "Клиент управления коллекцией продуктов";
        System.out.println("=".repeat(str.length()));
        System.out.println(str);
        System.out.println("=".repeat(str.length()));

        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        try {
            System.out.print("Введите адрес сервера (или нажмите Enter для localhost): ");
            String inputHost = inputReader.readLine();
            if (inputHost != null && !inputHost.trim().isEmpty()) {
                host = inputHost.trim();
            }

            System.out.print("Введите порт сервера (или нажмите Enter для " + DEFAULT_PORT + "): ");
            String inputPort = inputReader.readLine();
            if (inputPort != null && !inputPort.trim().isEmpty()) {
                port = Integer.parseInt(inputPort.trim());
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Ошибка ввода, используются значения по умолчанию: " + DEFAULT_HOST + ":" + DEFAULT_PORT);
        }


        System.out.println("Подключение к серверу " + host + ":" + port);

        try {
            ClientNetManager netManager = new ClientNetManager(host, port);
            CommandManager commandManager = new CommandManager();
            ExitCommand exitCommand = new ExitCommand(netManager);

            registerCommands(commandManager, inputReader, netManager, exitCommand);

            CommandRequest testRequest = new CommandRequest(CommandType.TEST, "LazeevTop");
            CommandResponse response = null;
            try {
                response = netManager.sendRequest(testRequest);
            } catch (ClassNotFoundException e) {
                System.out.println("Этого не должно было произойти" +e.getMessage());
            }
            if (response != null && response.isSuccess()) {
                System.out.println("Соединение установлено. Введите 'help' для справки.");
            } else {
                System.out.println("Не удалось подключиться к серверу. Программа завершена.");
                System.exit(1);
            }
            runInteractiveMode(commandManager, inputReader, exitCommand);

            netManager.close();
        } catch (IOException e) {
            System.out.println("Не удалось подключиться к серверу: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void registerCommands(CommandManager manager,
                                         InputReader inputReader,
                                         ClientNetManager netManager,
                                         ExitCommand exitCommand) {
        manager.registerCommand(TestCommand.name, new TestCommand(netManager));
        manager.registerCommand(HelpCommand.name, new HelpCommand(netManager));
        manager.registerCommand(InfoCommand.name, new InfoCommand(netManager));
        manager.registerCommand(ShowCommand.name, new ShowCommand(netManager));
        manager.registerCommand(AddCommand.name, new AddCommand(inputReader, netManager));
        manager.registerCommand(UpdateIdCommand.name, new UpdateIdCommand(inputReader, netManager));
        manager.registerCommand(RemoveByIdCommand.name, new RemoveByIdCommand(netManager));
        manager.registerCommand(ClearCommand.name, new ClearCommand(netManager));
        manager.registerCommand(ExecuteScriptCommand.name, new ExecuteScriptCommand(manager));
        manager.registerCommand(ExitCommand.name, exitCommand);
        manager.registerCommand(AddIfMinCommand.name, new AddIfMinCommand(inputReader, netManager));
        manager.registerCommand(RemoveGreaterCommand.name, new RemoveGreaterCommand(inputReader, netManager));
        manager.registerCommand(RemoveLowerCommand.name, new RemoveLowerCommand(inputReader, netManager));
        manager.registerCommand(FilterByPriceCommand.name, new FilterByPriceCommand(netManager));
        manager.registerCommand(FilterGreaterThanPriceCommand.name, new FilterGreaterThanPriceCommand(netManager));
        manager.registerCommand(PrintFieldDescendingUnitOfMeasureCommand.name,
                new PrintFieldDescendingUnitOfMeasureCommand(netManager));
    }

    private static void runInteractiveMode(CommandManager commandManager, InputReader inputReader, ExitCommand exitCommand) {
        try {
            while (!exitCommand.isNeedExit()) {
                System.out.print("> ");
                String input = inputReader.readLine();
                if (input == null) {
                    System.out.println("Вы нажали ctrl+D, программа анигилируется...");
                    break;
                }
                if (input.trim().isEmpty()) {
                    continue;
                }
                commandManager.executeCommand(input.trim());
            }
        } catch (IOException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        }
    }
}