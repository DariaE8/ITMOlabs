package server_cmd;

import utils.Response;
import utils.ServerCommand;
import auth_utils.User;
import utils.ExecutionContext;

public class SignUp implements ServerCommand {
  private final User user;

  public SignUp(User user) {
    this.user = user;
  }

  @Override
  public Response execute(ExecutionContext context) {
    try{
      Integer id = context.getAuthManager().register(user);
      if(id != null) {
        return Response.ok("Вы успешно зарегистрированы", id);
      } else {
        return Response.error("Возникла ошибка при регистрации, попробуйте изменить данные");
      }
      } catch (IllegalArgumentException e) {
          return Response.error(e.getMessage());
      } catch (Exception e) {
          return Response.error("Системная ошибка: " + e.getMessage());
      }
  }
}
