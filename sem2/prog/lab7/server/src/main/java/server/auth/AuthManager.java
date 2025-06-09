package server.auth;

import java.sql.Connection;
import java.sql.SQLException;

import auth_utils.User;
import managers.AuthManagerInterface;
import server.database.dao.UserDao;
import server.database.DatabaseManager;

public class AuthManager implements AuthManagerInterface{
   Connection conn = DatabaseManager.getConnection();
   UserDao userDAO = new UserDao(conn);

   @Override
   public Integer validate(User user) throws Exception {
      return userDAO.authenticate(user.getUsername(), PasswordUtils.hashPassword(user.getPassword()));
   }

   @Override
   public Integer register(User user) {
      try {
         return userDAO.register(user.getUsername(), PasswordUtils.hashPassword(user.getPassword()));
      } catch (SQLException e) {
         return null;
      } 
   }
}
