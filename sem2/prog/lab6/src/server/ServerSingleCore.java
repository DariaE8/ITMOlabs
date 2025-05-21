package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Logger;

import models.TicketManager;
import utils.Response;
import utils.ServerCommand;

public class ServerSingleCore {
    private static final int PORT = 9999;
    private DatagramSocket socket;
    private final TicketManager tm = new TicketManager();
    private final DumpManager dm = new DumpManager("db.csv");
    private final Logger logger = Logger.getLogger(ServerSingleCore.class.getName());

    public void start() throws IOException, ClassNotFoundException {
        logger.info("Server started on port " + PORT);
        this.socket = new DatagramSocket(PORT);
        byte[] buffer = new byte[65535];
        ServerContext context = new ServerContext(tm, dm);

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            logger.info("Received packet from " + packet.getAddress());

            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            ServerCommand cmd = (ServerCommand) ois.readObject();
            Response response = cmd.execute(context);
            System.out.println(response.getMessage());


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(response);
            
            byte[] responseBytes = baos.toByteArray();
            System.out.println("Sending response to " + packet.getAddress() + ":" + packet.getPort());
            socket.send(new DatagramPacket(responseBytes, responseBytes.length, packet.getAddress(), packet.getPort()));
            logger.info("Response sent");
        }
    }
}
