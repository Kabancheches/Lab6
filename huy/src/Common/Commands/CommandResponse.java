package Common.Commands;

import java.io.Serializable;

/**
 * Ответ сервера клиенту.
 */
public class CommandResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean success;
    private String message;
    private Object data;

    public CommandResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
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
