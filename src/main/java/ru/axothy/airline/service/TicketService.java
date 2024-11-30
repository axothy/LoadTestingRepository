package ru.axothy.airline.service;

import ru.axothy.airline.model.db.Ticket;

public interface TicketService {
    Ticket createTicket(Ticket ticket);
    Ticket getTicketById(Long ticketId);

}
