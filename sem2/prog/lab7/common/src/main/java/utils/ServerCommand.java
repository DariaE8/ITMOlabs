package utils;
import java.io.Serializable;

public interface ServerCommand extends Serializable {
    public Response execute(ExecutionContext context); 
}
