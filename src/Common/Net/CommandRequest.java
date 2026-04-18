package Common.Net;

import Common.Model.Enums.CommandType;

import java.io.Serializable;

public class CommandRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private final CommandType commandType;
    private final Object argument;

    public CommandRequest(CommandType commandType, Object argument) {
        this.commandType = commandType;
        this.argument = argument;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getCommandName() {
        return commandType.getCommandName();
    }

    public Object getArgument() {
        return argument;
    }

    @Override
    public String toString() {
        return "CommandRequest{type=" + commandType + ", argument=" + argument + "}";
    }
}