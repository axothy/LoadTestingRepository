package ru.axothy.airline.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.axothy.airline.model.db.Ticket;
import ru.axothy.airline.model.dto.CreateTicketRq;
import ru.axothy.airline.service.TicketService;

@RestController
@RequestMapping(value = "ticket/")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<Ticket> getTicketById(@RequestParam ("ticketId") Long ticketId) {
        Ticket result = ticketService.getTicketById(ticketId);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Ticket> createNewTicket(@RequestBody CreateTicketRq createTicketRq) {
        Ticket result = ticketService.createTicket(toTicketEntity(createTicketRq));
        return ResponseEntity.ok(result);
    }


    private Ticket toTicketEntity(CreateTicketRq createTicketRq) {
        Ticket ticket = new Ticket();
        ticket.setType(createTicketRq.getType());

        return ticket;
    }

}
