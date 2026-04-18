package src.Server.Controller.Commands;

public class ExitCommand implements Command {
    public static String name = "exit";
    private boolean needExit = false;

    @Override
    public boolean execute(String[] args) {
        System.out.println("Завершение программы...");
        needExit = true;
        return true;
    }

    public boolean isNeedExit() {
        return needExit;
    }

    @Override
    public String getDescription() {
        return "Завершить программу (без сохранения в файл)";
    }

    @Override
    public String getName() {
        return name;
    }
}