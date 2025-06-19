package utils;

import java.io.Serializable;

/**
 * Класс, представляющий ответ сервера после выполнения команды.
 */
public class CommandResponse implements Serializable {
    private final String message;
    private final Object data;

    public CommandResponse(String message) {
        this(message, null);
    }

    public CommandResponse(Object data) {
        this(null, data);
    }

    public CommandResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public boolean hasData() {
        return data != null;
    }

    public boolean hasMessage() {
        return message != null && !message.isEmpty();
    }
}

