package Server.Commands;

import Controller.CommandManager;

import java.util.List;

public class HelpCommand implements Command {
    public static String name = "help";
    private final CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public boolean execute(String[] args) {
        System.out.println("Доступные команды:");
        System.out.println("------------------");
        List<Command> commands = commandManager.getAllCommands();
        for (Command cmd : commands) {
            System.out.printf("%s - %s%n", cmd.getName(), cmd.getDescription());
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "Вывести справку по доступным командам";
    }

    @Override
    public String getName() {
        return name;
    }
}