package utils;

import managers.AuthManagerInterface;
import managers.TicketManagerInterface;


public interface ExecutionContext {
    AuthManagerInterface getAuthManager();
    TicketManagerInterface getTicketManager();
}