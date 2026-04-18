package Common.Commands;

import java.io.Serializable;

/**
 * Перечисление всех доступных команд.
 */
public enum CommandType implements Serializable {
    HELP("help"),
    INFO("info"),
    SHOW("show"),
    ADD("add"),
    UPDATE("update"),
    REMOVE_BY_ID("remove_by_id"),
    CLEAR("clear"),
    SAVE("save"),
    EXIT("exit"),
    ADD_IF_MIN("add_if_min"),
    REMOVE_GREATER("remove_greater"),
    REMOVE_LOWER("remove_lower"),
    FILTER_BY_PRICE("filter_by_price"),
    FILTER_GREATER_THAN_PRICE("filter_greater_than_price");

    private final String commandName;

    CommandType(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

    public static CommandType fromString(String name) {
        for (CommandType type : values()) {
            if (type.commandName.equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Неизвестная команда: " + name);
    }
}
