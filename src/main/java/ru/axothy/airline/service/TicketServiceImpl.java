package ru.axothy.airline.service;

import org.springframework.stereotype.Service;
import ru.axothy.airline.appender.TicketsAggregator;
import ru.axothy.airline.model.db.Ticket;
import ru.axothy.airline.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        TicketsAggregator.aggregate(ticket);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId).get();
    }
}
