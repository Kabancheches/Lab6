package src.Server.Controller.Commands;

import src.Server.Controller.CommandManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExecuteScriptCommand implements Command {
    private final CommandManager commandManager;
    public static String name = "execute_script";
    public ExecuteScriptCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Команда: execute_script <file_name>, где <file_name> - это путь до файла.");
            return false;
        }

        String fileName = args[1];

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0;

            System.out.println("Начало выполнения скрипта в файле: " + fileName);
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() ||line.startsWith("//") || line.startsWith("#")) {
                    continue;
                }

                if (!line.startsWith("execute_script")) {
                    System.out.println("Номер команды: " + lineNumber + ". Выполнение команды: " + line);
                    commandManager.executeCommand(line);
                } else {
                    System.out.println("Номер команды: " + lineNumber + ". Команда вызовет рекурсию пропускаем: " + line);
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("[ОШИБКА] Ошибка чтения файла: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Считать и исполнить скрипт из указанного файла";
    }

    @Override
    public String getName() {
        return name;
    }
}