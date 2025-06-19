package server_cmd;

import utils.Response;
import utils.ServerCommand;
import auth_utils.User;
import utils.ExecutionContext;

public class SignIn implements ServerCommand {
  private final User user;

  public SignIn(User user) {
    this.user = user;
  }

  @Override
  public Response execute(ExecutionContext context) {
    try{
      Integer id = context.getAuthManager().validate(user);
      if(id != null) {
        return Response.ok("Вы успешно вошли в систему", id);
      } else {
        return Response.error("Проверьте правильность введенных данных");
      }
      } catch (IllegalArgumentException e) {
          return Response.error(e.getMessage());
      } catch (Exception e) {
          return Response.error("Системная ошибка: " + e.getMessage());
      }
  }
}
