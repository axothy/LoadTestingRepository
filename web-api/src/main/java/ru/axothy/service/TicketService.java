package ru.axothy.service;

import ru.axothy.model.db.Ticket;

public interface TicketService {
    Ticket createTicket(Ticket ticket);
    Ticket getTicketById(Long ticketId);

}
