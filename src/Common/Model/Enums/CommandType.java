package Common.Model.Enums;

import java.io.Serializable;

public enum CommandType implements Serializable {
    TEST("test"),
    HELP("help"),
    INFO("info"),
    SHOW("show"),
    ADD("add"),
    UPDATE("update"),
    REMOVE_BY_ID("remove_by_id"),
    CLEAR("clear"),
    SAVE("save"),
    EXIT("exit"),
    SERVEREXIT("exit"),
    ADD_IF_MIN("add_if_min"),
    REMOVE_GREATER("remove_greater"),
    REMOVE_LOWER("remove_lower"),
    FILTER_BY_PRICE("filter_by_price"),
    FILTER_GREATER_THAN_PRICE("filter_greater_than_price"),
    PRINT_FIELD_DESCENDING_UNIT_OF_MEASURE("print_field_descending_unit_of_measure"),
    EXECUTE_SCRIPT("execute_script"); // выполняется на клиенте, но тип нужен

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