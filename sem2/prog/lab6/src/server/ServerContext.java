package server;

import models.TicketManager;

public class ServerContext {
  private final TicketManager ticketManager;
  private final DumpManager dumpManager;

  public ServerContext(TicketManager ticketManager, DumpManager dumpManager) {
    this.ticketManager = ticketManager;
    this.dumpManager = dumpManager;
  }

  public TicketManager getTicketManager() {
    return this.ticketManager;
  }

  public DumpManager getDumpManager() {
    return this.dumpManager;
  }
}
