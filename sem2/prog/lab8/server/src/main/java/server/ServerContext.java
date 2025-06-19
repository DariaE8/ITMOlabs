package server;

import server.auth.AuthManager;
import utils.ExecutionContext;

public class ServerContext implements ExecutionContext {
    private final AuthManager authManager;
    private final TicketManager ticketManager;

    public ServerContext(AuthManager authManager, TicketManager ticketManager) {
        this.authManager = authManager;
        this.ticketManager = ticketManager;
    }

    @Override
    public TicketManager getTicketManager() {
        return this.ticketManager;
    }

    @Override
    public AuthManager getAuthManager() {
        return this.authManager;
    }
}