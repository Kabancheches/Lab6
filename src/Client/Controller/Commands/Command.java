package Client.Controller.Commands;

public interface Command {
    boolean execute(String[] args);

    String getDescription();

    String getName();
}