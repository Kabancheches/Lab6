package Client.View;


public class ConsoleUI {
    private static final String errorPrefix = "[ОШИБКА] ";
    private static final String infoPrefix = "[ИНФО] ";
    private static final String successPrefix = "[УСПЕХ] ";

    public void printError(String message) {
        System.out.println(errorPrefix + message);
    }

    public void printInfo(String message) {
        System.out.println(infoPrefix + message);
    }

    public void printSuccess(String message) {
        System.out.println(successPrefix + message);
    }

    public void printSeparator() {
        System.out.println("=========================================");
    }
}