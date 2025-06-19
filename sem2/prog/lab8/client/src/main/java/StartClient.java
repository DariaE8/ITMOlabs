

import client.Client;

public class StartClient {
    public static void main(String[] args) {
        Client client = new Client();
        client.init(args);
        client.run(); // запускает клиент
    }
}
    