package utils;
import java.io.Serializable;

import server.ServerContext;

public interface ServerCommand extends Serializable {
    public Response execute(ServerContext context); 
}
