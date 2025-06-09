package auth_utils;

import utils.ServerCommand;
import utils.UserAwareCommand;
import utils.ExecutionContext;
import utils.Response;

import java.io.Serializable;

/**
 * Обёртка для команд, которые требуют авторизации.
 * Содержит логин и пароль пользователя, передаётся с клиента.
 */
public class AuthorizedCommandDecorator implements ServerCommand, Serializable {
    private final ServerCommand original;
    private final User user;

    public AuthorizedCommandDecorator(ServerCommand original, User user) {
        this.original = original;
        this.user = user;
    }

    public ServerCommand getOriginal() {
        return original;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Response execute(ExecutionContext context) {
        // Проверка валидности пользователя
        try{
            Integer id = context.getAuthManager().validate(user);
            if (id == null) {
                return Response.error("Ошибка авторизации");
            }
        } catch (Exception e) {
            return Response.error("Ошибка авторизации"); 
        }


        // Если команда поддерживает прикрепление пользователя — прикрепляем
        if (original instanceof UserAwareCommand userAware) {
            userAware.attachUser(user);
        }

        return original.execute(context);
    }
}
