import server.Server;
public class StartServer {
    public static void main(String[] args) {
        try {
            new Server().start(); // запускает сервер
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
