package Client.Controller;

import Client.Controller.Commands.Command;
import java.util.*;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public void registerCommand(String name, Command command) {
        commands.put(name.toLowerCase(), command);
    }

    public void executeCommand(String input) {
        if (input == null || input.trim().isEmpty()) return;
        String[] parts = input.trim().split("\\s+", 2);
        String commandName = parts[0].toLowerCase();
        Command command = commands.get(commandName);
        if (command == null) {
            System.out.println("Неизвестная команда: " + commandName);
            System.out.println("Введите 'help' для справки.");
            return;
        }
        String[] args = parts.length > 1 ? ("cmd " + parts[1]).split("\\s+") : new String[]{"cmd"};
        command.execute(args);
    }
}