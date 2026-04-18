package Client.Controller.Commands;

import Client.Controller.CommandManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExecuteScriptCommand implements Command {
    public static final String name = "execute_script";
    private final CommandManager commandManager;

    public ExecuteScriptCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Использование: execute_script <file_name>");
            return false;
        }
        String fileName = args[1];
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0;
            System.out.println("Выполнение скрипта из файла: " + fileName);
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("//") || line.startsWith("#")) continue;
                if (!line.startsWith("execute_script")) {
                    System.out.println("Строка " + lineNumber + ": " + line);
                    commandManager.executeCommand(line);
                } else {
                    System.out.println("Строка " + lineNumber + ": рекурсия пропущена");
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Выполнить скрипт из файла";
    }

    @Override
    public String getName() {
        return name;
    }
}