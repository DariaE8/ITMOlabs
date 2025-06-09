package server_cmd;

import auth_utils.User;
import managers.TicketManagerInterface;
import utils.ExecutionContext;
import utils.Response;
import utils.ServerCommand;
import utils.UserAwareCommand;

/**
 * Команда для удаления из коллекции всех элементов, ключ которых превышает заданный.
 * Удаляет все элементы с ключами, большими чем указанный ключ.
 */
public class RemoveGreaterKey implements ServerCommand, UserAwareCommand {
    private final Integer key;
    private static final String SUCCESS_MSG = "Удалено элементов: %d. Текущий размер коллекции: %d";
    private static final String NO_ELEMENTS_MSG = "Элементы с ключами больше %d не найдены";
    private User user;
    @Override
    public void attachUser(User user) {
        this.user = user;
    }
    public RemoveGreaterKey(Integer key) {
        this.key = key;
    }

    @Override
    public Response execute(ExecutionContext context) {
        if (user == null) {
            return Response.error("Пользователь не авторизован");
        }
        try {
            TicketManagerInterface tm = context.getTicketManager();
            int removedCount = removeElements(tm, key);
            
            if (removedCount > 0) {
                return Response.ok(String.format(SUCCESS_MSG, 
                    removedCount, tm.size()));
            } else {
                return Response.ok(String.format(NO_ELEMENTS_MSG, key));
            }
        } catch (IllegalArgumentException e) {
            return Response.error(e.getMessage());
        }
    }

    /**
     * Удаляет элементы с ключами больше заданного.
     *
     * @return количество удаленных элементов
     */
    private int removeElements(TicketManagerInterface tm, int key) {
        int initialSize = tm.size();
        tm.removeGreaterKeys(key);
        return initialSize - tm.size();
    }
}

