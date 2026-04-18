package Common.Net;

import java.io.Serializable;

public class CommandResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private final boolean success;
    private final String message;
    private final Object data;

    public CommandResponse(boolean success, String message) {
        this(success, message, null);
    }

    public CommandResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return message;
    }
}