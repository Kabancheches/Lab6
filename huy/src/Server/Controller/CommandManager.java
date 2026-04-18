package src.Server.Controller;

import src.Server.Controller.Commands.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands;

    public CommandManager() {
        this.commands = new HashMap<>();
    }

    public void registerCommand(String name, Command command) {
        commands.put(name.toLowerCase(), command);
    }

    public void executeCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String[] parts = input.trim().split("\\s+", 2);
        String commandName = parts[0].toLowerCase();

        Command command = commands.get(commandName);
        if (command == null) {
            System.out.println("Неизвестная команда: " + commandName);
            System.out.println("Введите 'help' для получения справки.");
            return;
        }

        String[] args;
        if (parts.length > 1) {
            args = ("cmd " + parts[1]).split("\\s+");
        } else {
            args = new String[]{"cmd"};
        }

        command.execute(args);
    }

    public List<Command> getAllCommands() {
        return new ArrayList<>(commands.values());
    }
}