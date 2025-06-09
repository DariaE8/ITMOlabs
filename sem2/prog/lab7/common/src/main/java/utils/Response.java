package utils;

import java.io.Serializable;

/**
 * Класс для хранения ответа сервера клиенту.
 */
public class Response implements Serializable {
    private final String message;
    private final Object data;
    private final boolean error;

    public Response(String message) {
        this(message, null, false);
    }

    public Response(String message, boolean error) {
        this(message, null, error);
    }

    public Response(String message, Object data) {
        this(message, data, false);
    }

    public Response(String message, Object data, boolean error) {
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }

    @Override
    public String toString() {
        return (error ? "[ERROR] " : "[OK] ") + message;
    }

    // ======= Статические фабричные методы =======

    public static Response ok(String msg) {
        return new Response(msg, null, false);
    }

    public static Response ok(String msg, Object data) {
        return new Response(msg, data, false);
    }

    public static Response error(String msg) {
        return new Response(msg, null, true);
    }
}
